#!/usr/bin/env bash
# front.sh — /front
# Abre um NOVO terminal integrado do Cursor e sobe o frontend (porta 4200).
#
# Uso:
#   ./ferramentas/front.sh           # instrui / dispara Task na IDE
#   ./ferramentas/front.sh --here    # roda neste terminal (já dentro da IDE)

set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
export EDUBASE_ROOT="$ROOT"

# shellcheck source=lib/abrir-terminal.sh
source "$(dirname "${BASH_SOURCE[0]}")/lib/abrir-terminal.sh"

MODE="ide"
if [[ "${1:-}" == "--here" ]]; then
  MODE="here"
elif [[ "${1:-}" == "-h" || "${1:-}" == "--help" ]]; then
  awk 'NR>1 && /^#/ {sub(/^# ?/,""); print; next} NR>1 {exit}' "$0"
  exit 0
fi

if [[ "$MODE" == "here" ]]; then
  if ! command -v npm >/dev/null 2>&1; then
    echo "✗ npm não encontrado." >&2
    exit 1
  fi
  if [[ ! -d "$ROOT/frontend/node_modules" ]]; then
    echo "→ npm install..."
    (cd "$ROOT/frontend" && npm install)
  fi
  echo "[EduBase] Frontend | http://localhost:4200"
  cd "$ROOT/frontend"
  exec npm start
fi

edubase_abrir_task "EduBase Frontend"
echo "  App: http://localhost:4200"
