#!/usr/bin/env bash
# commit.sh — /commit
# Analisa as mudanças atuais e imprime APENAS o texto da mensagem de commit.
# Não adiciona, não committa e não faz push.
#
# Uso:
#   ./ferramentas/commit.sh
#   ./ferramentas/commit.sh | clip.exe          # copiar (WSL)
#   MSG=$(./ferramentas/commit.sh)              # capturar em variável

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

# shellcheck source=lib/gerar-mensagem.sh
source "$(dirname "${BASH_SOURCE[0]}")/lib/gerar-mensagem.sh"

if [[ "${1:-}" == "-h" || "${1:-}" == "--help" ]]; then
  awk 'NR>1 && /^#/ {sub(/^# ?/,""); print; next} NR>1 {exit}' "$0"
  exit 0
fi

git rev-parse --is-inside-work-tree >/dev/null 2>&1 || {
  echo "Não é um repositório Git." >&2
  exit 1
}

BRANCH="$(git branch --show-current 2>/dev/null || echo develop)"

edubase_coletar_mudancas

if [[ -z "${EDUBASE_STATUS:-}" && -z "${EDUBASE_FILES:-}" ]]; then
  echo "Sem mudanças para gerar mensagem de commit." >&2
  exit 1
fi

# Saída limpa: só o texto do commit (stdout)
edubase_gerar_mensagem "$BRANCH"
