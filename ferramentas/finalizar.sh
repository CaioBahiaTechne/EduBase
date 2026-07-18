#!/usr/bin/env bash
# finalizar.sh — /finalizar
# Pega as mudanças, verifica a branch, monta automaticamente a mensagem
# de commit (baseada no que foi alterado) e faz o push.
#
# Formato da mensagem:
#   #ADD #EDIT Título amigável descrevendo o que o usuário/negócio ganhou
#
#   Parágrafo detalhado: o que foi alterado, onde, e o porquê.
#
# Uso:
#   ./ferramentas/finalizar.sh
#   ./ferramentas/finalizar.sh -y
#   ./ferramentas/finalizar.sh "#FIX #EDIT Corrige validação de vagas" "Detalhe do corpo..."
#   ./ferramentas/finalizar.sh --branch main

set -euo pipefail

ALLOWED_BRANCH="develop"
REMOTE="${GIT_REMOTE:-origin}"
ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; NC='\033[0m'
info()  { echo -e "${CYAN}→${NC} $*"; }
ok()    { echo -e "${GREEN}✓${NC} $*"; }
warn()  { echo -e "${YELLOW}!${NC} $*"; }
fail()  { echo -e "${RED}✗${NC} $*" >&2; exit 1; }

ASSUME_YES=0
CUSTOM_TITLE=""
CUSTOM_BODY=""

# --- Argumentos ---
while [[ $# -gt 0 ]]; do
  case "$1" in
    -y|--yes)    ASSUME_YES=1; shift ;;
    --branch)    ALLOWED_BRANCH="${2:?informe a branch}"; shift 2 ;;
    -h|--help)
      awk 'NR>1 && /^#/ {sub(/^# ?/,""); print; next} NR>1 {exit}' "$0"
      exit 0 ;;
    *)
      if [[ -z "$CUSTOM_TITLE" ]]; then
        CUSTOM_TITLE="$1"
      else
        CUSTOM_BODY="$1"
      fi
      shift
      ;;
  esac
done

git rev-parse --is-inside-work-tree >/dev/null 2>&1 || fail "Não é um repositório Git."

# --- 1. Verifica branch ---
BRANCH="$(git branch --show-current)"
[[ "$BRANCH" == "$ALLOWED_BRANCH" ]] \
  || fail "Branch atual: '${BRANCH}'. O /finalizar só opera em '${ALLOWED_BRANCH}'."
ok "Branch: ${ALLOWED_BRANCH}"

# --- 2. Pega as mudanças ---
info "Adicionando mudanças (git add -A)..."
git add -A

if git diff --cached --quiet; then
  if git status -sb | grep -q "ahead"; then
    warn "Sem novas mudanças, mas há commits locais pendentes."
    info "Enviando para ${REMOTE}/${ALLOWED_BRANCH}..."
    git push -u "$REMOTE" "HEAD:${ALLOWED_BRANCH}"
    ok "Push concluído."
    exit 0
  fi
  fail "Nada para finalizar (working tree limpa)."
fi

# --- Bloqueia arquivos sensíveis ---
if git diff --cached --name-only \
   | grep -E '(^|/)\.env($|\.)|(^|/)credentials\.json$|(^|/)id_rsa$|\.pem$' >/dev/null; then
  fail "Arquivos sensíveis no stage. Remova-os antes de finalizar."
fi

# --- 3. Monta a mensagem automaticamente ---
# Padrão:
#   #ADD #EDIT Título amigável
#
#   Corpo detalhado explicando o que mudou e o porquê.
generate_message() {
  local status files
  status="$(git diff --cached --name-status)"
  files="$(git diff --cached --name-only)"

  local added modified deleted renamed
  added=$(echo "$status"    | grep -c '^A' || true)
  modified=$(echo "$status" | grep -c '^M' || true)
  deleted=$(echo "$status"  | grep -c '^D' || true)
  renamed=$(echo "$status"  | grep -c '^R' || true)

  # --- Tags (#ADD #EDIT #FIX #DEL #DOC) ---
  local tags=()
  [[ "$added" -gt 0 ]]    && tags+=("#ADD")
  [[ "$modified" -gt 0 || "$renamed" -gt 0 ]] && tags+=("#EDIT")
  [[ "$deleted" -gt 0 ]]  && tags+=("#DEL")

  local only_md=1
  local has_test=0
  local has_fix_hint=0
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

  # Remove duplicatas preservando ordem
  local unique_tags=()
  local t
  for t in "${tags[@]}"; do
    local seen=0
    local u
    for u in "${unique_tags[@]:-}"; do
      [[ "$u" == "$t" ]] && seen=1 && break
    done
    [[ "$seen" -eq 0 ]] && unique_tags+=("$t")
  done
  tags=("${unique_tags[@]}")
  [[ ${#tags[@]} -eq 0 ]] && tags=("#EDIT")

  local tag_str
  tag_str="$(IFS=' '; echo "${tags[*]}")"

  # --- Áreas tocadas ---
  local areas=()
  echo "$files" | grep -q '^backend/'     && areas+=("backend")
  echo "$files" | grep -q '^frontend/'    && areas+=("frontend")
  echo "$files" | grep -q '^ferramentas/' && areas+=("ferramentas")
  echo "$files" | grep -qE '^[^/]+\.md$'  && areas+=("documentação")

  local areas_txt=""
  if [[ ${#areas[@]} -gt 0 ]]; then
    areas_txt="$(IFS=', '; echo "${areas[*]}")"
  else
    areas_txt="o projeto"
  fi

  # --- Título amigável (frase curta, começando com verbo) ---
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

  # --- Corpo detalhado ---
  local body_parts=()

  if [[ "$added" -gt 0 ]]; then
    local novos
    novos="$(echo "$status" | awk -F'\t' '/^A/ {print $2}' | sed 's|.*/||' | head -12 | paste -sd ', ' -)"
    body_parts+=("Incluídos ${added} arquivo(s) novo(s)${novos:+ (${novos})}.")
  fi
  if [[ "$modified" -gt 0 ]]; then
    local alts
    alts="$(echo "$status" | awk -F'\t' '/^M/ {print $2}' | sed 's|.*/||' | head -12 | paste -sd ', ' -)"
    body_parts+=("Alterados ${modified} arquivo(s)${alts:+ (${alts})}.")
  fi
  if [[ "$deleted" -gt 0 ]]; then
    local rems
    rems="$(echo "$status" | awk -F'\t' '/^D/ {print $2}' | sed 's|.*/||' | head -12 | paste -sd ', ' -)"
    body_parts+=("Removidos ${deleted} arquivo(s)${rems:+ (${rems})}.")
  fi
  if [[ "$renamed" -gt 0 ]]; then
    body_parts+=("Renomeados ${renamed} arquivo(s).")
  fi

  body_parts+=("Mudanças concentradas em ${areas_txt}, alinhadas ao fluxo de entrega na branch ${ALLOWED_BRANCH}, mantendo o repositório sincronizado com o remoto.")

  local body
  body="$(printf '%s ' "${body_parts[@]}")"
  body="$(echo "$body" | sed 's/[[:space:]]*$//')"

  printf '%s %s\n\n%s\n' "$tag_str" "$title" "$body"
}

if [[ -n "$CUSTOM_TITLE" ]]; then
  # Se o usuário já passou tags no título, mantém; senão deixa como está
  if [[ -n "$CUSTOM_BODY" ]]; then
    MESSAGE="$(printf '%s\n\n%s\n' "$CUSTOM_TITLE" "$CUSTOM_BODY")"
  else
    MESSAGE="$CUSTOM_TITLE"
  fi
else
  MESSAGE="$(generate_message)"
fi

echo ""
info "Arquivos no stage:"
git diff --cached --stat
echo ""
info "Mensagem de commit proposta:"
echo "------------------------------------------------------------"
echo "$MESSAGE"
echo "------------------------------------------------------------"
echo ""

if [[ "$ASSUME_YES" -ne 1 ]]; then
  read -r -p "Confirmar commit e push? [Y/n]: " confirm
  [[ -z "${confirm:-}" || "$confirm" =~ ^[Yy]$ ]] || fail "Abortado pelo usuário."
fi

# --- 4. Commit + push ---
# Usa HEREDOC para preservar título + corpo (múltiplas linhas)
git commit -m "$(cat <<EOF
${MESSAGE}
EOF
)"
ok "Commit criado."

CURRENT="$(git branch --show-current)"
[[ "$CURRENT" == "$ALLOWED_BRANCH" ]] || fail "Branch mudou para '${CURRENT}'. Push abortado."

info "Push para ${REMOTE}/${ALLOWED_BRANCH}..."
git push -u "$REMOTE" "HEAD:${ALLOWED_BRANCH}"
ok "Finalizado: ${REMOTE}/${ALLOWED_BRANCH}"
echo ""
git status -sb
