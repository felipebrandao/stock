# Stock API

API REST para gestão de **categorias**, **produtos**, **locais** e **itens/movimentações de estoque**, com suporte a **importação de NFC-e** (via um serviço externo de “scraper”).

> **Fonte da verdade dos contratos**: acesse a UI do Swagger em `http://localhost:8080/swagger-ui`.

## Stack

- Java **17**
- Spring Boot **3.5.0**
- Spring Web, Validation, Actuator
- Spring Data JPA + Hibernate
- PostgreSQL (dev/prod)
- Flyway (migrations)
- OpenAPI/Swagger (springdoc)
- Spring Cloud OpenFeign (integração com o scraper de NFC-e)
- Testes: Spring Boot Test, H2 (modo PostgreSQL), RestAssured, WireMock

## Requisitos

- **JDK 17**
- **Maven** (ou usar o wrapper `./mvnw` / `mvnw.cmd`)
- **PostgreSQL** (para rodar em dev com banco real)

## Como rodar (dev)

Por padrão a aplicação sobe na porta **8080**.

### 1) Configure as variáveis de ambiente (opcional, recomendado)

O projeto vem com *defaults* em `src/main/resources/application.properties`, mas o ideal é sobrescrever credenciais via variáveis.

No **PowerShell**:

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/stock"
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="postgres"

# (Opcional) URL do serviço de scrape de NFC-e
$env:NFCE_SCRAPER_URL="http://localhost:3000"
```

### 2) Suba a aplicação

```powershell
cd C:\fiap\stock
.\mvnw.cmd spring-boot:run
```

Acessos úteis:

- Swagger UI: `http://localhost:8080/swagger-ui`
- OpenAPI JSON: `http://localhost:8080/api-docs`
- Actuator (exposto por padrão): `http://localhost:8080/actuator/health`

## Configuração

Arquivo principal: `src/main/resources/application.properties`

Principais chaves:

- **Banco**
  - `spring.datasource.url` (env: `DB_URL`)
  - `spring.datasource.username` (env: `DB_USERNAME`)
  - `spring.datasource.password` (env: `DB_PASSWORD`)
- **JPA**
  - `spring.jpa.hibernate.ddl-auto` (env: `DDL_AUTO`) — default: `validate`
- **Flyway**
  - `spring.flyway.locations=classpath:db/migration`
- **NFC-e Scraper (Feign)**
  - `nfce.scraper.url` (env: `NFCE_SCRAPER_URL`) — default: `http://localhost:3000`
- **Swagger**
  - `springdoc.api-docs.path=/api-docs`
  - `springdoc.swagger-ui.path=/swagger-ui`

### Perfil de testes

Em testes, o projeto usa H2 em memória (compatível com PostgreSQL):

- `src/test/resources/application-test.properties`

## Endpoints principais

Base URL local: `http://localhost:8080`

### Categorias

- `POST /api/categories`
- `GET /api/categories` (paginação via `Pageable`)
- `GET /api/categories/{id}`
- `DELETE /api/categories/{id}`

### Produtos

- `POST /api/products`
- `GET /api/products` (paginação)
- `GET /api/products/{id}`
- `DELETE /api/products/{id}`

### Locais

- `POST /api/locations`
- `GET /api/locations` (paginação)
- `GET /api/locations/{id}`
- `DELETE /api/locations/{id}`

### Estoque

#### Itens de estoque

- `GET /api/stock-items` (filtros: `productId`, `locationId`, `state`)
- `GET /api/stock-items/{id}`
- `POST /api/stock-items/{id}/open`
- `POST /api/stock-items/{id}/transfer`
- `POST /api/stock-items/{id}/consume`
- `POST /api/stock-items/{id}/convert`

#### Movimentações de estoque

- `POST /api/stock-movements`
- `GET /api/stock-movements` (filtros: `productId`, `locationId`)

### NFC-e

Fluxo típico:

1. Solicita importação
2. Consulta “review” dos itens extraídos
3. Atualiza mapeamentos/quantidades/validade/local
4. Aprova importação para efetivar no estoque

Endpoints:

- `POST /api/nfce/import` *(retorna 202 Accepted com o `id` da importação)*
- `GET /api/nfce/imports/{id}/review`
- `PUT /api/nfce/imports/{id}/review`
- `POST /api/nfce/imports/{id}/approve`

## Scheduler (importações NFC-e)

Existe um job que processa importações pendentes de NFC-e:

- Classe: `br.com.felipebrandao.stock.api.scheduler.NfceImportScheduler`
- Intervalo: `@Scheduled(fixedDelay = 5000)` (a cada ~5s após a última execução)

## Como rodar testes

```powershell
cd C:\fiap\stock
.\mvnw.cmd test
```

Se você quiser rodar um teste específico:

```powershell
.\mvnw.cmd -Dtest=NomeDaClasseTest test
```

## Build do JAR

```powershell
cd C:\fiap\stock
.\mvnw.cmd clean package
```

O artefato fica em `target/stock-0.0.1-SNAPSHOT.jar`.

## Troubleshooting

- **Falha nos testes/ITs por causa do Flyway + H2**: se você vir o erro `Unknown data type: "TIMESTAMPTZ"` ao subir o contexto com perfil `test`, o H2 (mesmo em `MODE=PostgreSQL`) não aceita `TIMESTAMPTZ` nas migrations.
  - Soluções comuns:
    - Trocar `TIMESTAMPTZ` por `TIMESTAMP WITH TIME ZONE` nas migrations (compatível com PostgreSQL e H2)
    - ou manter migrations específicas por banco (ex.: locations diferentes do Flyway por profile)
    - ou rodar testes de integração apontando para um PostgreSQL real (Testcontainers / banco local)
- **Falha no Flyway/validate**: se o schema do banco não estiver alinhado com as migrations, `spring.jpa.hibernate.ddl-auto=validate` vai falhar. Garanta que:
  - o banco aponta para o schema correto
  - as migrations em `src/main/resources/db/migration` foram aplicadas
- **Conexão com PostgreSQL**: verifique `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
- **Importação NFC-e não processa**: confira se o scraper está acessível em `NFCE_SCRAPER_URL`.
- **Swagger não abre**: confirme que a aplicação está na porta correta (`server.port`) e acesse `/swagger-ui`.

---
