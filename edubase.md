# 🎓 Desafio Técnico - Sistema Acadêmico

> Documento central do projeto contendo escopo, requisitos, tecnologias, regras de negócio e decisões de implementação.

---

# Informações Gerais

| Item | Valor |
|------|--------|
| Projeto | Sistema Acadêmico de Matrículas |
| Objetivo | Desenvolver uma aplicação Full Stack para gerenciamento de matrículas acadêmicas |
| Empresa | Tribe Lyceum - Techne |
| Nível | Desenvolvedor Full Stack Júnior |
| Prazo | 7 dias corridos |
| Dedicação esperada | 8 a 16 horas |

---

# Objetivos do Desafio

Construir uma aplicação capaz de:

- Gerenciar alunos
- Gerenciar cursos
- Gerenciar disciplinas
- Gerenciar turmas
- Gerenciar matrículas
- Aplicar regras de negócio
- Disponibilizar API REST
- Possuir persistência em banco de dados
- Possuir interface para consumo da API

---

# Stack Obrigatória

## Backend

- Java
- Spring Boot

## Frontend

Pode utilizar uma das opções:

- Angular
- JavaScript
- TypeScript
- Framework Web equivalente

---

# Persistência

Obrigatório utilizar banco relacional.

Exemplos:

- PostgreSQL
- MySQL
- MariaDB
- H2

Pode utilizar:

- Spring Data JPA
- Hibernate
- SQL puro

---

# Requisitos Obrigatórios

## Backend

- API REST
- CRUD completo
- Regras de negócio
- Persistência
- Separação de responsabilidades

---

## Frontend

Interface simples capaz de consumir a API.

Fluxos mínimos:

- Cadastro
- Listagem
- Atualização
- Exclusão
- Matrícula

---

## README

Deve conter:

- Como executar
- Tecnologias utilizadas
- Banco utilizado
- Configuração
- Endpoints principais
- Fluxo de matrícula
- Decisões tomadas
- Limitações
- Uso de IA

---

# Entidades

## Aluno

Descrição:

Representa um estudante.

CRUD obrigatório

- Criar
- Editar
- Listar
- Excluir

---

## Curso

Representa um curso da instituição.

CRUD obrigatório

---

## Disciplina

Representa uma disciplina pertencente a um curso.

CRUD obrigatório

---

## Turma

Representa uma oferta de uma disciplina.

CRUD obrigatório

---

## Matrícula

Representa a matrícula de um aluno em uma turma.

CRUD recomendado

Fluxos obrigatórios:

- Matricular
- Confirmar
- Cancelar

---

# Regras de Negócio

## RN001

Um aluno somente pode ser matriculado em turmas abertas.

---

## RN002

Uma turma possui limite de vagas.

---

## RN003

Um aluno não pode possuir duas matrículas na mesma turma.

---

## RN004

Uma matrícula possui apenas um dos status abaixo:

- PENDENTE
- CONFIRMADA
- CANCELADA

---

## RN005

Ao confirmar uma matrícula:

- alterar status
- consumir uma vaga

---

## RN006

Ao cancelar uma matrícula confirmada:

- alterar status
- devolver vaga

---

## RN007

Consultar matrículas por aluno.

---

## RN008

Consultar matrículas por turma.

---

# Validações

## Aluno

- Nome obrigatório
- Email obrigatório

---

## Curso

- Nome obrigatório

---

## Disciplina

- Nome obrigatório

---

## Turma

- Nome obrigatório
- Quantidade de vagas maior que zero
- Status obrigatório

---

## Matrícula

- Aluno obrigatório
- Turma obrigatória

---

# Fluxos

## Cadastro

```text
Usuário

↓

Frontend

↓

Controller

↓

Service

↓

Repository

↓

Banco
```

---

## Matrícula

```text
Selecionar aluno

↓

Selecionar turma

↓

Turma existe?

↓

Está aberta?

↓

Possui vagas?

↓

Aluno já está matriculado?

↓

Criar matrícula

↓

Status PENDENTE

↓

Confirmar matrícula

↓

Consumir vaga
```

---

## Cancelamento

```text
Selecionar matrícula

↓

Está confirmada?

↓

Cancelar

↓

Liberar vaga
```

---

# Arquitetura

Estrutura esperada:

```
controller/

service/

repository/

entity/

dto/

mapper/

exception/

config/
```

---

# API REST

Operações mínimas.

## Aluno

GET

POST

PUT

DELETE

---

## Curso

GET

POST

PUT

DELETE

---

## Disciplina

GET

POST

PUT

DELETE

---

## Turma

GET

POST

PUT

DELETE

---

## Matrícula

GET

POST

PUT

DELETE

POST Confirmar

POST Cancelar

GET Por aluno

GET Por turma

---

# Diferenciais (Não Obrigatórios)

- Swagger
- OpenAPI
- Docker
- Testes Unitários
- Bean Validation
- Tratamento global de exceções
- Logs
- Lombok
- MapStruct

---

# O que NÃO é Obrigatório

Segundo a documentação do desafio:

- Arquitetura complexa
- Microsserviços
- Mensageria
- CI/CD
- Cobertura alta de testes
- Observabilidade
- Migrations
- Kubernetes
- Cache
- Filas

---

# Critérios de Avaliação

## Backend

- API funcionando
- Código organizado
- Separação de responsabilidades

---

## Regras

- Limite de vagas
- Matrícula única
- Confirmação
- Cancelamento

---

## Persistência

Banco relacional funcionando.

---

## Frontend

Interface funcional.

---

## README

Documentação clara.

---

## Entrevista

Será necessário explicar:

- Arquitetura
- Fluxos
- Regras
- Banco
- API
- Uso de IA

---

# Uso de IA

Ferramentas utilizadas:

- ChatGPT

Partes auxiliadas:

- Planejamento
- Estrutura do projeto
- Revisão de código
- Documentação

Todo o código foi revisado manualmente antes da entrega.

---

# Decisões Técnicas

## Backend

- Java 21
- Spring Boot

## Frontend

- Angular (ou framework escolhido)

## Banco

- PostgreSQL (ou banco escolhido)

## ORM

- Spring Data JPA

## Build

- Maven

---

# Checklist

## Backend

- [x] Spring Boot
- [x] API REST
- [x] CRUD
- [x] Services
- [x] Repository
- [x] DTO
- [x] Exceptions

---

## Frontend

- [x] Tela de alunos
- [x] Tela de cursos
- [x] Tela de disciplinas
- [x] Tela de turmas
- [x] Tela de matrículas

---

## Regras

- [x] Turma aberta
- [x] Limite de vagas
- [x] Matrícula única
- [x] Confirmar matrícula
- [x] Cancelar matrícula

---

## Persistência

- [x] Banco configurado
- [x] JPA
- [x] Relacionamentos

---

## Documentação

- [x] README
- [x] Fluxos
- [x] Endpoints
- [x] Uso de IA

---

# Observações

## Decisões

- Stack: Spring Boot 4 (Java 21) + Angular 19 + H2 (perfil PostgreSQL opcional).
- Camadas: controller → service → repository; DTOs + mappers manuais; tratamento global de exceções.
- Status de matrícula alterado apenas via `POST .../confirmar` e `POST .../cancelar`.
- Scripts em `ferramentas/` para subir backend/frontend e auxiliar commits no fluxo da IDE.

## Dificuldades / limitações

- H2 em memória perde dados ao reiniciar.
- Sem auth; exclusões com FK podem falhar se houver dependentes.
- Diferenciais (Swagger, Docker, testes amplos) ficaram de fora de propósito.

## Melhorias futuras

- Seed de dados de demonstração.
- Soft-delete / rematrícula após cancelamento.
- OpenAPI + testes das regras de matrícula.