#!/usr/bin/env bash
# lib/abrir-terminal.sh — abre um NOVO terminal integrado do Cursor (via Tasks)
#
# Uso (source):
#   source "$(dirname "$0")/lib/abrir-terminal.sh"
#   edubase_abrir_task "EduBase Backend"

edubase_abrir_task() {
  local task_label="$1"
  local root="${EDUBASE_ROOT:-}"

  if [[ -z "$root" ]]; then
    echo "EDUBASE_ROOT não definido." >&2
    return 1
  fi

  if [[ ! -f "$root/.vscode/tasks.json" ]]; then
    echo "✗ .vscode/tasks.json não encontrado." >&2
    return 1
  fi

  # Estamos dentro do Cursor? (terminal integrado ou agent)
  local in_cursor=0
  if [[ -n "${VSCODE_IPC_HOOK_CLI:-}" || -n "${CURSOR_AGENT:-}" || -n "${VSCODE_INJECTION:-}" ]]; then
    in_cursor=1
  fi

  echo "→ Terminal integrado do Cursor: Task \"${task_label}\""
  echo ""

  if [[ "$in_cursor" -eq 1 ]]; then
    cat <<EOF
  Abra um novo terminal na IDE assim:

    1. Ctrl+Shift+P  (Command Palette)
    2. Digite:  Tasks: Run Task
    3. Escolha: ${task_label}

  Ou pelo menu: Terminal → Run Task… → ${task_label}

EOF
  else
    cat <<EOF
  Abra o projeto no Cursor e rode a Task:

    Ctrl+Shift+P → Tasks: Run Task → ${task_label}

EOF
  fi

  # Tenta focar a palette de tasks abrindo o arquivo de tasks (atalho visual)
  if command -v cursor >/dev/null 2>&1 && [[ -n "${VSCODE_IPC_HOOK_CLI:-}" ]]; then
    cursor -r -g "$root/.vscode/tasks.json:1" >/dev/null 2>&1 || true
  fi

  echo "  Dica: depois da primeira vez, a Task aparece no histórico do Command Palette."
}
