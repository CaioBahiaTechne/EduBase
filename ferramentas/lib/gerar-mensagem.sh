#!/usr/bin/env bash
# lib/gerar-mensagem.sh — gera mensagem de commit no padrão EduBase
#
# Uso (source):
#   source "$(dirname "$0")/lib/gerar-mensagem.sh"
#   edubase_coletar_mudancas   # define EDUBASE_STATUS e EDUBASE_FILES
#   edubase_gerar_mensagem "develop"
#
# Formato:
#   #ADD #EDIT Título amigável
#
#   Corpo detalhado...

# Coleta mudanças vs HEAD sem alterar o index real (usa index temporário).
edubase_coletar_mudancas() {
  local tmp_index
  tmp_index="$(mktemp)"
  (
    export GIT_INDEX_FILE="$tmp_index"
    git read-tree HEAD 2>/dev/null || true
    git add -A 2>/dev/null
    git diff --cached --name-status
  ) > "${tmp_index}.status"
  (
    export GIT_INDEX_FILE="$tmp_index"
    git read-tree HEAD 2>/dev/null || true
    git add -A 2>/dev/null
    git diff --cached --name-only
  ) > "${tmp_index}.files"

  EDUBASE_STATUS="$(cat "${tmp_index}.status")"
  EDUBASE_FILES="$(cat "${tmp_index}.files")"
  rm -f "$tmp_index" "${tmp_index}.status" "${tmp_index}.files"
}

# Gera a mensagem a partir de EDUBASE_STATUS / EDUBASE_FILES (ou args).
# $1 = branch (opcional, default develop)
# $2 = status name-status (opcional)
# $3 = files name-only (opcional)
edubase_gerar_mensagem() {
  local branch="${1:-develop}"
  local status="${2:-${EDUBASE_STATUS:-}}"
  local files="${3:-${EDUBASE_FILES:-}}"

  if [[ -z "$status" && -z "$files" ]]; then
    echo "Sem mudanças para gerar mensagem." >&2
    return 1
  fi

  local added modified deleted renamed
  added=$(printf '%s\n' "$status"    | grep -c '^A' || true)
  modified=$(printf '%s\n' "$status" | grep -c '^M' || true)
  deleted=$(printf '%s\n' "$status"  | grep -c '^D' || true)
  renamed=$(printf '%s\n' "$status"  | grep -c '^R' || true)

  local tags=()
  [[ "$added" -gt 0 ]]    && tags+=("#ADD")
  [[ "$modified" -gt 0 || "$renamed" -gt 0 ]] && tags+=("#EDIT")
  [[ "$deleted" -gt 0 ]]  && tags+=("#DEL")

  local only_md=1
  local has_test=0
  local has_fix_hint=0
  local f
  while IFS= read -r f; do
    [[ -z "$f" ]] && continue
    [[ "$f" == *.md ]] || only_md=0
    [[ "$f" == *Test* || "$f" == *.spec.* ]] && has_test=1
    echo "$f" | grep -qiE '(fix|bug|hotfix|corrige)' && has_fix_hint=1
  done <<< "$files"

  if [[ "$only_md" -eq 1 ]]; then
    tags=("#DOC")
    [[ "$added" -gt 0 ]]    && tags+=("#ADD")
    [[ "$modified" -gt 0 ]] && tags+=("#EDIT")
  fi

  if [[ "$has_fix_hint" -eq 1 ]]; then
    tags=("#FIX" "${tags[@]}")
  fi

  local unique_tags=()
  local t u seen
  for t in "${tags[@]}"; do
    seen=0
    for u in "${unique_tags[@]:-}"; do
      [[ "$u" == "$t" ]] && seen=1 && break
    done
    [[ "$seen" -eq 0 ]] && unique_tags+=("$t")
  done
  tags=("${unique_tags[@]}")
  [[ ${#tags[@]} -eq 0 ]] && tags=("#EDIT")

  local tag_str
  tag_str="$(IFS=' '; echo "${tags[*]}")"

  local areas=()
  echo "$files" | grep -q '^backend/'     && areas+=("backend")
  echo "$files" | grep -q '^frontend/'    && areas+=("frontend")
  echo "$files" | grep -q '^ferramentas/' && areas+=("ferramentas")
  echo "$files" | grep -qE '^[^/]+\.md$'  && areas+=("documentação")

  local areas_txt
  if [[ ${#areas[@]} -gt 0 ]]; then
    printf -v areas_txt '%s, ' "${areas[@]}"
    areas_txt="${areas_txt%, }"
  else
    areas_txt="o projeto"
  fi

  local title
  if [[ "$only_md" -eq 1 ]]; then
    title="Atualiza documentação do projeto EduBase"
  elif [[ "$added" -gt 0 && "$added" -ge "$modified" && "$modified" -eq 0 ]]; then
    title="Adiciona estrutura inicial de ${areas_txt}"
  elif [[ "$added" -gt 0 && "$modified" -gt 0 ]]; then
    title="Evolui ${areas_txt} com novas inclusões e ajustes"
  elif [[ "$deleted" -gt 0 && "$deleted" -ge "$modified" ]]; then
    title="Remove arquivos obsoletos em ${areas_txt}"
  elif [[ "$has_test" -eq 1 && "$added" -eq 0 ]]; then
    title="Ajusta testes e cobertura em ${areas_txt}"
  else
    title="Atualiza ${areas_txt} do sistema acadêmico"
  fi

  local body_parts=()
  if [[ "$added" -gt 0 ]]; then
    local novos
    novos="$(printf '%s\n' "$status" | awk -F'\t' '/^A/ {print $2}' | sed 's|.*/||' | head -12 | paste -sd ',' - | sed 's/,/, /g')"
    body_parts+=("Incluídos ${added} arquivo(s) novo(s)${novos:+ (${novos})}.")
  fi
  if [[ "$modified" -gt 0 ]]; then
    local alts
    alts="$(printf '%s\n' "$status" | awk -F'\t' '/^M/ {print $2}' | sed 's|.*/||' | head -12 | paste -sd ',' - | sed 's/,/, /g')"
    body_parts+=("Alterados ${modified} arquivo(s)${alts:+ (${alts})}.")
  fi
  if [[ "$deleted" -gt 0 ]]; then
    local rems
    rems="$(printf '%s\n' "$status" | awk -F'\t' '/^D/ {print $2}' | sed 's|.*/||' | head -12 | paste -sd ',' - | sed 's/,/, /g')"
    body_parts+=("Removidos ${deleted} arquivo(s)${rems:+ (${rems})}.")
  fi
  if [[ "$renamed" -gt 0 ]]; then
    body_parts+=("Renomeados ${renamed} arquivo(s).")
  fi

  body_parts+=("Mudanças concentradas em ${areas_txt}, alinhadas ao fluxo de entrega na branch ${branch}, mantendo o repositório sincronizado com o remoto.")

  local body
  body="$(printf '%s ' "${body_parts[@]}")"
  body="$(echo "$body" | sed 's/[[:space:]]*$//')"

  printf '%s %s\n\n%s\n' "$tag_str" "$title" "$body"
}
