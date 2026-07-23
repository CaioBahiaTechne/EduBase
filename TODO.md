# TODO — EduBase (Sistema Acadêmico de Matrículas)

Checklist de entrega do desafio. Prioridade: **P0** obrigatório · **P1** recomendado · **P2** diferencial.

---

## Etapa 1 — Setup do projeto

- [x] Criar projeto Spring Boot (Java 21 + Maven)
- [x] Configurar banco relacional (H2 padrão; perfil `postgres` pronto)
- [x] Criar projeto frontend (Angular / TypeScript)
- [x] Estruturar pastas: `controller`, `service`, `repository`, `entity`, `dto`, `mapper`, `exception`, `config`
- [x] Configurar CORS para o frontend consumir a API

---

## Etapa 2 — Modelo e persistência

- [x] Entidade `Aluno` (nome, email)
- [x] Entidade `Curso` (nome)
- [x] Entidade `Disciplina` (nome, vínculo com Curso)
- [x] Entidade `Turma` (nome, vagas, status, vínculo com Disciplina)
- [x] Entidade `Matricula` (aluno, turma, status)
- [x] Enum status da matrícula: `PENDENTE`, `CONFIRMADA`, `CANCELADA`
- [x] Enum / status da turma: aberta / fechada (ou equivalente)
- [x] Repositories JPA para todas as entidades
- [x] Relacionamentos corretos no banco

---

## Etapa 3 — Backend CRUD (API REST)

### Aluno
- [x] `GET` listar / buscar
- [x] `POST` criar
- [x] `PUT` atualizar
- [x] `DELETE` excluir

### Curso
- [x] `GET` / `POST` / `PUT` / `DELETE`

### Disciplina
- [x] `GET` / `POST` / `PUT` / `DELETE`

### Turma
- [x] `GET` / `POST` / `PUT` / `DELETE`

### Matrícula
- [x] `GET` / `POST` / `PUT` / `DELETE` (CRUD recomendado)
- [x] DTOs + mappers
- [x] Services com separação de responsabilidades

---

## Etapa 4 — Regras de negócio (P0 — núcleo)

- [x] **RN001** — Matricular somente em turmas abertas
- [x] **RN002** — Respeitar limite de vagas
- [x] **RN003** — Impedir duas matrículas do mesmo aluno na mesma turma
- [x] **RN004** — Status apenas: `PENDENTE` | `CONFIRMADA` | `CANCELADA`
- [x] **RN005** — Confirmar matrícula → alterar status + consumir 1 vaga
- [x] **RN006** — Cancelar matrícula confirmada → alterar status + devolver vaga
- [x] **RN007** — `GET` matrículas por aluno
- [x] **RN008** — `GET` matrículas por turma
- [x] Endpoints: `POST` confirmar e `POST` cancelar

---

## Etapa 5 — Validações

- [x] Aluno: nome e email obrigatórios
- [x] Curso: nome obrigatório
- [x] Disciplina: nome obrigatório
- [x] Turma: nome obrigatório, vagas > 0, status obrigatório
- [x] Matrícula: aluno e turma obrigatórios
- [x] (P1) Bean Validation + tratamento global de exceções

---

## Etapa 6 — Frontend

- [ ] Tela de alunos (CRUD)
- [ ] Tela de cursos (CRUD)
- [ ] Tela de disciplinas (CRUD)
- [ ] Tela de turmas (CRUD)
- [ ] Tela de matrículas (listar, matricular, confirmar, cancelar)
- [ ] Integração HTTP com a API
- [ ] Fluxo completo: selecionar aluno → turma → matricular → confirmar / cancelar

---

## Etapa 7 — Documentação e entrega

- [ ] README: como executar
- [ ] README: tecnologias utilizadas
- [ ] README: banco e configuração
- [ ] README: endpoints principais
- [ ] README: fluxo de matrícula
- [ ] README: decisões tomadas e limitações
- [ ] README: uso de IA
- [ ] Atualizar checklist / observações no `edubase.md` se necessário

---

## Diferenciais (P2 — opcional)

- [ ] Swagger / OpenAPI
- [ ] Testes unitários (services das regras de matrícula)
- [ ] Lombok
- [ ] MapStruct
- [ ] Logs

---

## Fora de escopo (não fazer)

- Microsserviços, mensageria, filas, cache
- CI/CD, Kubernetes, observabilidade
- Auth / JWT (salvo se sobrar muito tempo)
- Cobertura alta de testes
- Migrations obrigatórias (Flyway/Liquibase — opcional)

---

## Ordem sugerida se o tempo apertar

1. Setup + entidades + CRUD backend  
2. Regras RN001–RN006 + consultas RN007–RN008  
3. Frontend mínimo (matrícula + 1–2 CRUDs)  
4. README  
5. Diferenciais (Swagger, Docker, testes)  
