# EduBase — Sistema Acadêmico de Matrículas

Aplicação Full Stack para gerenciamento de matrículas acadêmicas (desafio Tribe Lyceum / Techne).

## Tecnologias

| Camada | Stack |
|--------|--------|
| Backend | Java 21, Spring Boot 4, Spring Data JPA, Bean Validation |
| Frontend | Angular 19 (standalone), TypeScript, HttpClient |
| Banco (padrão) | H2 em memória (`jdbc:h2:mem:edubase`) |
| Banco (opcional) | PostgreSQL via perfil `postgres` |
| Build | Maven Wrapper (`./mvnw`), npm / Angular CLI |

## Estrutura

```
EduBase/
├── backend/        # API REST (DDD — Bounded Context academico)
├── frontend/       # Interface Angular
├── ferramentas/    # Scripts /front, /back, /commit, /finalizar
├── edubase.md      # Escopo do desafio
└── TODO.md         # Checklist de entrega
```

Backend (`com.edubase`):

```
shared/domain/exception/     # DomainException, NotFoundException
academico/
  domain/                    # Modelos ricos, ports de repositório, PoliticaMatricula
  application/               # DTOs, mappers, use cases
  infrastructure/            # JPA adapters, controllers, CORS, exception handler
```

## Pré-requisitos

- JDK 21
- Maven 3.8+ (ou `./mvnw`)
- Node.js 20+ e npm

> `ferramentas/back.sh` resolve `JAVA_HOME` automaticamente (`JAVA_HOME` ou `~/.local/jdk-21*`).

## Como executar

### Recomendado — scripts / Tasks da IDE

```bash
./ferramentas/back.sh    # API em :8080 (novo terminal Cursor)
./ferramentas/front.sh   # App em :4200
```

Ou: `Ctrl+Shift+P` → **Tasks: Run Task** → `EduBase Backend` / `EduBase Frontend`.

No chat do Cursor: peça `/back` ou `/front`.

No próprio terminal: `./ferramentas/back.sh --here` e `./ferramentas/front.sh --here`.

| Serviço | URL |
|---------|-----|
| Frontend | http://localhost:4200 |
| Backend | http://localhost:8080 |
| Health | http://localhost:8080/api/health |
| H2 Console | http://localhost:8080/h2-console — JDBC `jdbc:h2:mem:edubase`, user `sa`, senha vazia |

O frontend usa **sempre** URLs relativas `/api/...` (`environment.apiUrl`). O `ng serve` aplica proxy `/api` → `http://localhost:8080` (`proxy.conf.json`), evitando CORS no browser. O backend também libera CORS para `http://localhost:4200` e `http://127.0.0.1:4200` (chamadas diretas à API).

### Manual

```bash
# Terminal 1 — backend
cd backend && ./mvnw spring-boot:run

# Terminal 2 — frontend
cd frontend && npm install && npm start
```

### PostgreSQL

```bash
# Banco: edubase | user/senha: edubase (ajuste em application.yml se preciso)
cd backend
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Banco e configuração

- **Padrão:** H2 em memória, `ddl-auto: update`, porta `8080` (`backend/src/main/resources/application.yml`).
- **CORS:** `edubase.cors.allowed-origins` (`localhost:4200`, `127.0.0.1:4200`) via `CorsFilter` em `infrastructure/config`.
- **Perfil `postgres`:** JDBC `jdbc:postgresql://localhost:5432/edubase`.

Dados do H2 são perdidos ao reiniciar a API.

## Endpoints principais

Base: `/api`

| Recurso | Métodos |
|---------|---------|
| `/health` | `GET` |
| `/alunos` | `GET`, `GET/{id}`, `POST`, `PUT/{id}`, `DELETE/{id}` |
| `/cursos` | CRUD |
| `/disciplinas` | CRUD (`?cursoId=`) |
| `/turmas` | CRUD (`?disciplinaId=`, `?status=`) |
| `/matriculas` | CRUD + fluxos abaixo |

### Matrícula

| Método | Endpoint | Efeito |
|--------|----------|--------|
| `POST` | `/matriculas` | Cria com status `PENDENTE` (turma `ABERTA` + vagas) |
| `POST` | `/matriculas/{id}/confirmar` | `CONFIRMADA` e consome 1 vaga |
| `POST` | `/matriculas/{id}/cancelar` | `CANCELADA`; se era confirmada, devolve vaga |
| `GET` | `/matriculas/aluno/{alunoId}` | Por aluno |
| `GET` | `/matriculas/turma/{turmaId}` | Por turma |

Status: `PENDENTE` · `CONFIRMADA` · `CANCELADA`.

## Fluxo de matrícula

1. Cadastrar **Curso** → **Disciplina** → **Turma** (`ABERTA`, vagas > 0) → **Aluno**.
2. Em **Matrículas**, selecionar aluno e turma → **Matricular** (`PENDENTE`).
3. **Confirmar** → status `CONFIRMADA` e `vagas - 1`.
4. **Cancelar** matrícula confirmada → `CANCELADA` e `vagas + 1`.

Regras aplicadas no backend: turma aberta (RN001), limite de vagas (RN002), matrícula única aluno+turma (RN003), status controlados (RN004–RN006).

## Frontend

Telas em http://localhost:4200:

- Matrículas (fluxo completo)
- Alunos, Cursos, Disciplinas, Turmas (CRUD)

## Decisões e limitações

**Decisões**

- Monólito API + SPA Angular; backend em DDD modular (`academico`: domain / application / infrastructure) com um Bounded Context.
- H2 para desenvolvimento rápido; perfil PostgreSQL pronto.
- Status de matrícula só muda por `confirmar` / `cancelar` (não via `PUT` livre).
- Vagas = restante disponível; decremento na confirmação.
- Scripts em `ferramentas/` para subir/commitar no fluxo da IDE.

**Limitações**

- Sem autenticação/autorização.
- H2 em memória não persiste entre restarts.
- Sem Swagger, Docker ou suíte ampla de testes (opcionais do desafio).
- Exclusão de entidades relacionadas pode falhar por FK (comportamento JPA esperado).
- Frontend funcional, sem design system externo.

## Uso de IA

Assistente (Cursor / Composer) usado para:

- Scaffold Spring Boot + Angular e estrutura de pastas
- Entidades JPA, CRUD REST, regras RN001–RN008 e validações
- Telas Angular e integração HTTP
- Scripts em `ferramentas/` e documentação (`README`, `TODO`)

Revisão humana nas regras de negócio, contrato da API e critérios de entrega.

## Outras ferramentas

```bash
./ferramentas/commit.sh      # imprime mensagem de commit
./ferramentas/finalizar.sh   # commit + push na develop
```

Detalhes: [`ferramentas/README.md`](ferramentas/README.md).

## Status

Etapas 1–7 concluídas (setup → modelo → CRUD → regras → validações → frontend → documentação).
