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

# shellcheck source=lib/gerar-mensagem.sh
source "$(dirname "${BASH_SOURCE[0]}")/lib/gerar-mensagem.sh"

RED='\033[0;31m'; GREEN='\033[0;32m'; YELLOW='\033[1;33m'; CYAN='\033[0;36m'; NC='\033[0m'
info()  { echo -e "${CYAN}→${NC} $*"; }
ok()    { echo -e "${GREEN}✓${NC} $*"; }
warn()  { echo -e "${YELLOW}!${NC} $*"; }
fail()  { echo -e "${RED}✗${NC} $*" >&2; exit 1; }

ASSUME_YES=0
CUSTOM_TITLE=""
CUSTOM_BODY=""

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

BRANCH="$(git branch --show-current)"
[[ "$BRANCH" == "$ALLOWED_BRANCH" ]] \
  || fail "Branch atual: '${BRANCH}'. O /finalizar só opera em '${ALLOWED_BRANCH}'."
ok "Branch: ${ALLOWED_BRANCH}"

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

if git diff --cached --name-only \
   | grep -E '(^|/)\.env($|\.)|(^|/)credentials\.json$|(^|/)id_rsa$|\.pem$' >/dev/null; then
  fail "Arquivos sensíveis no stage. Remova-os antes de finalizar."
fi

if [[ -n "$CUSTOM_TITLE" ]]; then
  if [[ -n "$CUSTOM_BODY" ]]; then
    MESSAGE="$(printf '%s\n\n%s\n' "$CUSTOM_TITLE" "$CUSTOM_BODY")"
  else
    MESSAGE="$CUSTOM_TITLE"
  fi
else
  EDUBASE_STATUS="$(git diff --cached --name-status)"
  EDUBASE_FILES="$(git diff --cached --name-only)"
  MESSAGE="$(edubase_gerar_mensagem "$ALLOWED_BRANCH")"
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
