# Ferramentas — EduBase

Scripts utilitários do repositório.

## /finalizar

`finalizar.sh` pega as mudanças, verifica a branch, **monta a mensagem de commit automaticamente** com base no que foi alterado e faz o push.

```bash
# Monta tudo e pede confirmação
./ferramentas/finalizar.sh

# Sem confirmação
./ferramentas/finalizar.sh -y

# Usando uma mensagem própria
./ferramentas/finalizar.sh "feat: adiciona entidades JPA"

# Trocar a branch permitida (padrão: develop)
./ferramentas/finalizar.sh --branch main
```

### O que ele faz

1. Verifica se está na branch `develop` (aborta se não estiver)
2. `git add -A`
3. Analisa os arquivos e **gera a mensagem** no padrão `tipo(escopo): resumo`, com o corpo listando os arquivos
4. Mostra o resumo e pede confirmação (pule com `-y`)
5. `git commit` + `git push origin develop`

### Como a mensagem é gerada

- **Escopo**: `backend`, `frontend`, `ferramentas`, `docs` ou `repo` (vários)
- **Tipo**: `docs` (só `.md`), `test` (arquivos de teste), `feat` (predominam novos arquivos), `refactor` (predominam alterações), senão `chore`
- **Resumo**: contagem de arquivos novos/alterados/removidos/renomeados
- **Corpo**: lista `name-status` das mudanças

Também bloqueia arquivos sensíveis (`.env`, `credentials.json`, chaves) no stage.

> Sugestão: crie um alias para chamar como `/finalizar`:
> ```bash
> alias finalizar='./ferramentas/finalizar.sh'
> ```
