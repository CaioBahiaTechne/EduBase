#!/usr/bin/env bash
# back.sh — /back
# Abre um NOVO terminal integrado do Cursor e sobe o backend (porta 8080).
#
# Uso:
#   ./ferramentas/back.sh           # instrui / dispara Task na IDE
#   ./ferramentas/back.sh --here    # roda neste terminal (já dentro da IDE)

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

resolve_java_home() {
  if [[ -n "${JAVA_HOME:-}" && -x "${JAVA_HOME}/bin/java" ]]; then
    echo "$JAVA_HOME"
    return
  fi
  local candidate
  for candidate in "$HOME"/.local/jdk-21*; do
    if [[ -x "$candidate/bin/java" ]]; then
      echo "$candidate"
      return
    fi
  done
  return 1
}

if [[ "$MODE" == "here" ]]; then
  JAVA_HOME_RESOLVED="$(resolve_java_home)" || {
    echo "✗ JDK 21 não encontrado." >&2
    exit 1
  }
  export JAVA_HOME="$JAVA_HOME_RESOLVED"
  export PATH="$JAVA_HOME/bin:$PATH"
  echo "[EduBase] Backend | http://localhost:8080"
  cd "$ROOT/backend"
  exec ./mvnw spring-boot:run
fi

edubase_abrir_task "EduBase Backend"
echo "  Health: http://localhost:8080/api/health"
