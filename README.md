# EduBase — Sistema Acadêmico de Matrículas

Aplicação Full Stack (Spring Boot + Angular) para gerenciamento de matrículas acadêmicas.

## Estrutura

```
EduBase/
├── backend/        # Java 21 + Spring Boot 4 (API REST)
├── frontend/       # Angular 19
├── ferramentas/    # Scripts: /front, /back, /commit, /finalizar
├── edubase.md      # Escopo do desafio
└── TODO.md         # Checklist de entrega
```

## Pré-requisitos

- **JDK 21** (obrigatório para o backend)
- **Maven 3.8+** (ou use o `./mvnw` do backend)
- **Node.js 20+** e npm

> O script `ferramentas/back.sh` resolve `JAVA_HOME` automaticamente (variável já definida ou `~/.local/jdk-21*`).

## Como iniciar (recomendado)

Use os scripts em `ferramentas/` — eles sobem cada app em um **novo terminal integrado do Cursor**.

### Pelo chat

Peça `/front` ou `/back` no chat do Cursor.

### Pela Command Palette

```
Ctrl+Shift+P → Tasks: Run Task → EduBase Frontend
Ctrl+Shift+P → Tasks: Run Task → EduBase Backend
```

### Pelos scripts

```bash
# Abre novo terminal na IDE e sobe o serviço
./ferramentas/front.sh
./ferramentas/back.sh

# Ou roda neste terminal (já dentro da IDE)
./ferramentas/front.sh --here
./ferramentas/back.sh --here
```

| Serviço  | URL |
|----------|-----|
| Frontend | http://localhost:4200 |
| Backend  | http://localhost:8080 |
| Health   | http://localhost:8080/api/health |
| H2 Console | http://localhost:8080/h2-console (JDBC: `jdbc:h2:mem:edubase`) |

Detalhes em [`ferramentas/README.md`](ferramentas/README.md).

### Alternativa manual

```bash
# Backend
cd backend && ./mvnw spring-boot:run

# Frontend (outro terminal)
cd frontend && npm start
```

Perfil PostgreSQL (quando o banco estiver disponível):

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

O frontend faz proxy de `/api` → `http://localhost:8080` (`proxy.conf.json`).

## API REST (CRUD)

| Recurso | Base |
|---------|------|
| Alunos | `/api/alunos` |
| Cursos | `/api/cursos` |
| Disciplinas | `/api/disciplinas` |
| Turmas | `/api/turmas` |
| Matrículas | `/api/matriculas` |

Métodos: `GET` (lista / `{id}`), `POST`, `PUT` / `{id}`, `DELETE` / `{id}`.

Filtros opcionais na listagem:

- Alunos: `?nome=` · `?email=`
- Disciplinas: `?cursoId=`
- Turmas: `?disciplinaId=` · `?status=ABERTA|FECHADA`
- Matrículas: `?alunoId=` · `?turmaId=`

### Matrícula — regras e fluxos

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `POST` | `/api/matriculas` | Matricular (status `PENDENTE`; turma deve estar `ABERTA` e com vagas) |
| `POST` | `/api/matriculas/{id}/confirmar` | Confirma e consome 1 vaga |
| `POST` | `/api/matriculas/{id}/cancelar` | Cancela; se era `CONFIRMADA`, devolve a vaga |
| `GET` | `/api/matriculas/aluno/{alunoId}` | Matrículas do aluno (RN007) |
| `GET` | `/api/matriculas/turma/{turmaId}` | Matrículas da turma (RN008) |

Status possíveis: `PENDENTE` · `CONFIRMADA` · `CANCELADA`.

## CORS

O backend libera origens do Angular (`http://localhost:4200`) para rotas `/api/**` via `CorsConfig`.

## Outras ferramentas

```bash
./ferramentas/commit.sh              # só imprime a mensagem de commit
./ferramentas/finalizar.sh           # commit + push na develop
```

## Status

Etapas 1–5 concluídas (setup, modelo, CRUD, regras de matrícula, validações). Próximo: frontend (Etapa 6).
