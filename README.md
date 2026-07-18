# EduBase — Sistema Acadêmico de Matrículas

Aplicação Full Stack (Spring Boot + Angular) para gerenciamento de matrículas acadêmicas.

## Estrutura

```
EduBase/
├── backend/     # Java 21 + Spring Boot 4 (API REST)
├── frontend/    # Angular 19
├── edubase.md   # Escopo do desafio
└── TODO.md      # Checklist de entrega
```

## Pré-requisitos

- **JDK 21** (obrigatório para o backend)
- **Maven 3.8+** (ou use o `./mvnw` do backend)
- **Node.js 20+** e npm

> **JDK:** foi instalado um Temurin 21 em `~/.local/jdk-21.0.11+10` (uso local).
> Para o terminal: `export JAVA_HOME=$HOME/.local/jdk-21.0.11+10 && export PATH=$JAVA_HOME/bin:$PATH`

## Backend

```bash
export JAVA_HOME=$HOME/.local/jdk-21.0.11+10
export PATH=$JAVA_HOME/bin:$PATH
cd backend
./mvnw spring-boot:run
```

- API: http://localhost:8080
- Health: http://localhost:8080/api/health
- H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:edubase`)

Perfil PostgreSQL (quando o banco estiver disponível):

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Frontend

```bash
cd frontend
npm start
```

- App: http://localhost:4200
- Proxy `/api` → `http://localhost:8080` (ver `proxy.conf.json`)

## CORS

O backend libera origens do Angular (`http://localhost:4200`) para rotas `/api/**` via `CorsConfig`.
Veja a explicação no chat / em `edubase.md` / abaixo.

Sem CORS, o navegador bloqueia o frontend (porta 4200) de chamar a API (porta 8080),
porque são **origens diferentes** (protocolo + host + porta).

## Status

Etapa 1 (setup) concluída. Próximo: modelo e persistência (entidades JPA).
