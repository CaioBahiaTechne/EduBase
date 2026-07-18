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
- [ ] `GET` listar / buscar
- [ ] `POST` criar
- [ ] `PUT` atualizar
- [ ] `DELETE` excluir

### Curso
- [ ] `GET` / `POST` / `PUT` / `DELETE`

### Disciplina
- [ ] `GET` / `POST` / `PUT` / `DELETE`

### Turma
- [ ] `GET` / `POST` / `PUT` / `DELETE`

### Matrícula
- [ ] `GET` / `POST` / `PUT` / `DELETE` (CRUD recomendado)
- [ ] DTOs + mappers
- [ ] Services com separação de responsabilidades

---

## Etapa 4 — Regras de negócio (P0 — núcleo)

- [ ] **RN001** — Matricular somente em turmas abertas
- [ ] **RN002** — Respeitar limite de vagas
- [ ] **RN003** — Impedir duas matrículas do mesmo aluno na mesma turma
- [ ] **RN004** — Status apenas: `PENDENTE` | `CONFIRMADA` | `CANCELADA`
- [ ] **RN005** — Confirmar matrícula → alterar status + consumir 1 vaga
- [ ] **RN006** — Cancelar matrícula confirmada → alterar status + devolver vaga
- [ ] **RN007** — `GET` matrículas por aluno
- [ ] **RN008** — `GET` matrículas por turma
- [ ] Endpoints: `POST` confirmar e `POST` cancelar

---

## Etapa 5 — Validações

- [ ] Aluno: nome e email obrigatórios
- [ ] Curso: nome obrigatório
- [ ] Disciplina: nome obrigatório
- [ ] Turma: nome obrigatório, vagas > 0, status obrigatório
- [ ] Matrícula: aluno e turma obrigatórios
- [ ] (P1) Bean Validation + tratamento global de exceções

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
