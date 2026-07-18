# Ferramentas — EduBase

Scripts utilitários do repositório.

## /finalizar

`finalizar.sh` pega as mudanças, verifica a branch, **monta a mensagem de commit automaticamente** e faz o push na `develop`.

```bash
./ferramentas/finalizar.sh
./ferramentas/finalizar.sh -y

# Título + corpo manuais (opcional)
./ferramentas/finalizar.sh \
  "#FIX #EDIT Exibe mensagens amigáveis ao cadastrar qualificação duplicada" \
  "Adicionadas as chaves em messages.properties, alinhadas a validarPrimaryKey()..."
```

### Formato do commit

```
#ADD #EDIT Título amigável descrevendo o ganho para o usuário/negócio

Parágrafo detalhado: o que foi alterado, em quais arquivos/áreas, e o porquê
da mudança (evitar erro genérico, alinhar validação, etc.).
```

Tags usadas na frente do título:

| Tag | Quando |
|-----|--------|
| `#ADD` | Há arquivos novos |
| `#EDIT` | Há arquivos alterados ou renomeados |
| `#DEL` | Há arquivos removidos |
| `#FIX` | Caminhos sugerem correção (fix/bug/hotfix) |
| `#DOC` | Mudança só em documentação (`.md`) |

### Fluxo

1. Confere branch `develop`
2. `git add -A`
3. Gera mensagem no padrão acima
4. Confirma (pule com `-y`)
5. `git commit` + `git push origin develop`

Bloqueia `.env`, `credentials.json` e chaves no stage.
