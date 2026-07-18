# Ferramentas — EduBase

Scripts utilitários do repositório.

## /commit

Gera e **imprime apenas o texto** da mensagem de commit (não committa nem faz push).

```bash
./ferramentas/commit.sh

# Copiar para a área de transferência (WSL)
./ferramentas/commit.sh | clip.exe

# Usar no commit manual
git commit -m "$(./ferramentas/commit.sh)"
```

Não altera o index do Git (usa index temporário para analisar as mudanças).

## /finalizar

Pega as mudanças, verifica a branch `develop`, monta a mensagem e faz **commit + push**.

```bash
./ferramentas/finalizar.sh
./ferramentas/finalizar.sh -y

# Título + corpo manuais (opcional)
./ferramentas/finalizar.sh \
  "#FIX #EDIT Exibe mensagens amigáveis ao cadastrar qualificação duplicada" \
  "Adicionadas as chaves em messages.properties..."
```

### Formato do commit

```
#ADD #EDIT Título amigável descrevendo o ganho para o usuário/negócio

Parágrafo detalhado: o que foi alterado, em quais arquivos/áreas, e o porquê.
```

| Tag | Quando |
|-----|--------|
| `#ADD` | Há arquivos novos |
| `#EDIT` | Há arquivos alterados ou renomeados |
| `#DEL` | Há arquivos removidos |
| `#FIX` | Caminhos sugerem correção (fix/bug/hotfix) |
| `#DOC` | Mudança só em documentação (`.md`) |

A lógica de mensagem fica em `lib/gerar-mensagem.sh` (compartilhada por `/commit` e `/finalizar`).
