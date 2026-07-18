#!/usr/bin/env bash
# finalizar.sh — /finalizar
# Pega as mudanças, verifica a branch, monta automaticamente a mensagem
# de commit (baseada no que foi alterado) e faz o push.
#
# Uso:
#   ./ferramentas/finalizar.sh              # monta tudo e pede confirmação
#   ./ferramentas/finalizar.sh -y           # sem confirmação
#   ./ferramentas/finalizar.sh "mensagem"   # usa a mensagem informada
#   ./ferramentas/finalizar.sh --branch main  # troca a branch permitida

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
CUSTOM_MSG=""

# --- Argumentos ---
while [[ $# -gt 0 ]]; do
  case "$1" in
    -y|--yes)    ASSUME_YES=1; shift ;;
    --branch)    ALLOWED_BRANCH="${2:?informe a branch}"; shift 2 ;;
    -h|--help)
      awk 'NR>1 && /^#/ {sub(/^# ?/,""); print; next} NR>1 {exit}' "$0"
      exit 0 ;;
    *)           CUSTOM_MSG="$1"; shift ;;
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
generate_message() {
  local status added modified deleted renamed
  status="$(git diff --cached --name-status)"

  added=$(echo "$status"   | grep -c '^A' || true)
  modified=$(echo "$status"| grep -c '^M' || true)
  deleted=$(echo "$status" | grep -c '^D' || true)
  renamed=$(echo "$status" | grep -c '^R' || true)

  local files
  files="$(git diff --cached --name-only)"

  # Escopos presentes (top-level dirs)
  local scopes=()
  echo "$files" | grep -q '^backend/'     && scopes+=("backend")
  echo "$files" | grep -q '^frontend/'    && scopes+=("frontend")
  echo "$files" | grep -q '^ferramentas/' && scopes+=("ferramentas")
  local root_docs
  root_docs="$(echo "$files" | grep -E '^[^/]+\.md$' || true)"
  [[ -n "$root_docs" ]] && scopes+=("docs")

  local scope=""
  if [[ ${#scopes[@]} -eq 1 ]]; then
    scope="(${scopes[0]})"
  elif [[ ${#scopes[@]} -gt 1 ]]; then
    scope="(repo)"
  fi

  # Tipo inferido
  local type="chore"
  local only_md=1
  while IFS= read -r f; do
    [[ -z "$f" ]] && continue
    [[ "$f" == *.md ]] || only_md=0
  done <<< "$files"

  if [[ "$only_md" -eq 1 ]]; then
    type="docs"
  elif echo "$files" | grep -qE '(Test|\.spec\.)'; then
    type="test"
  elif [[ "$added" -gt 0 && "$added" -ge "$modified" ]]; then
    type="feat"
  elif [[ "$modified" -gt 0 ]]; then
    type="refactor"
  fi

  # Assunto (subject)
  local subject
  if [[ "$only_md" -eq 1 ]]; then
    subject="atualiza documentação"
  else
    local parts=()
    [[ "$added" -gt 0 ]]    && parts+=("${added} novo(s)")
    [[ "$modified" -gt 0 ]] && parts+=("${modified} alterado(s)")
    [[ "$deleted" -gt 0 ]]  && parts+=("${deleted} removido(s)")
    [[ "$renamed" -gt 0 ]]  && parts+=("${renamed} renomeado(s)")
    local joined
    joined="$(IFS=', '; echo "${parts[*]}")"
    if [[ ${#scopes[@]} -ge 1 ]]; then
      subject="atualiza ${scopes[*]} (${joined})"
    else
      subject="atualiza projeto (${joined})"
    fi
  fi

  local header="${type}${scope}: ${subject}"

  # Corpo: lista de arquivos agrupada
  local body
  body="$(git diff --cached --name-status | sed 's/\t/  /g')"

  printf '%s\n\n%s\n' "$header" "$body"
}

if [[ -n "$CUSTOM_MSG" ]]; then
  MESSAGE="$CUSTOM_MSG"
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
git commit -m "$MESSAGE"
ok "Commit criado."

CURRENT="$(git branch --show-current)"
[[ "$CURRENT" == "$ALLOWED_BRANCH" ]] || fail "Branch mudou para '${CURRENT}'. Push abortado."

info "Push para ${REMOTE}/${ALLOWED_BRANCH}..."
git push -u "$REMOTE" "HEAD:${ALLOWED_BRANCH}"
ok "Finalizado: ${REMOTE}/${ALLOWED_BRANCH}"
echo ""
git status -sb
