# Ferramentas — EduBase

Scripts utilitários do repositório.

## /front e /back (terminal da IDE)

Abrem um **novo terminal integrado do Cursor** (não o Windows Terminal).

### Pelo chat (recomendado)

Peça `/front` ou `/back` no chat — o agente sobe a app em uma nova aba de terminal da IDE.

### Pela Command Palette (nativo)

```
Ctrl+Shift+P → Tasks: Run Task → EduBase Frontend
Ctrl+Shift+P → Tasks: Run Task → EduBase Backend
```

Ou menu: **Terminal → Run Task…**

As tasks estão em `.vscode/tasks.json` com `"panel": "new"`.

### Pelos scripts

```bash
./ferramentas/front.sh          # mostra como abrir a Task na IDE
./ferramentas/back.sh

# Roda neste terminal (já dentro da IDE)
./ferramentas/front.sh --here
./ferramentas/back.sh --here
```

- Frontend: http://localhost:4200  
- Backend: http://localhost:8080  

## /commit

Gera e **imprime apenas o texto** da mensagem de commit (não committa nem faz push).

```bash
./ferramentas/commit.sh
git commit -m "$(./ferramentas/commit.sh)"
```

## /finalizar

Pega as mudanças, verifica a branch `develop`, monta a mensagem e faz **commit + push**.

```bash
./ferramentas/finalizar.sh
./ferramentas/finalizar.sh -y
```

### Formato do commit

```
#ADD #EDIT Título amigável

Parágrafo detalhado do que mudou e por quê.
```

| Tag | Quando |
|-----|--------|
| `#ADD` | Arquivos novos |
| `#EDIT` | Arquivos alterados/renomeados |
| `#DEL` | Arquivos removidos |
| `#FIX` | Correção |
| `#DOC` | Só documentação |
