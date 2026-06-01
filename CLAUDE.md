# CLAUDE.md — Automatix

## Project Overview

**Automatix** is a Spring Boot + Thymeleaf web application for small business management (inventory, sales, cash flow, receivables). Package: `com.teamsits.pbs`.

## Tech Stack

- **Language**: Java 8
- **Framework**: Spring Boot 2.7.8
- **View Layer**: Thymeleaf
- **ORM**: Spring Data JPA / Hibernate
- **Database**: PostgreSQL
- **Build Tool**: Maven (wrapper included)
- **Extras**: Apache POI (Excel), Lombok

## Build & Run

```bash
# Build
./mvnw clean package

# Run (dev)
./mvnw spring-boot:run
```

Requires `.env` in project root (already present). The app reads env vars from `.env` via `spring.config.import=optional:file:.env[.properties]`.

## Application Ports

| Variable          | Default |
|-------------------|---------|
| `PBS_HTTP_PORT`   | 8090    |
| `PBS_SERVER_PORT` | 8443    |

## Database

PostgreSQL connection configured via `.env`:

```
PBS_DB_IP=localhost
PBS_DB_PORT=5432
PBS_DB_NAME=automatix
PBS_DB_USERNAME=postgres
PBS_DB_PASSWORD=1234
```

DDL mode: `spring.jpa.hibernate.ddl-auto=update` (schema auto-updated on startup).

## Project Structure

```
src/main/java/com/teamsits/pbs/
├── PbsApplication.java       # Entry point
├── config/                   # Spring config classes
├── controller/               # MVC controllers (10)
├── entities/                 # JPA entities (10)
├── enums/                    # Enumerations
├── models/                   # DTOs / request-response models
├── repository/               # Spring Data JPA repositories
├── service/                  # Business logic
├── utils/                    # Utilities
└── web/                      # Web components

src/main/resources/
├── application.properties    # App configuration
├── static/css/               # Stylesheets
└── templates/                # Thymeleaf HTML templates
```

## Domain Modules

| Module             | Controller             | Service             | Entity       |
|--------------------|------------------------|---------------------|--------------|
| Cash In            | CashInController       | CashInService       | CashIn       |
| Cash Out           | CashOutController      | CashOutService      | CashOut      |
| Sales              | SalesController        | SalesService        | Sales        |
| Stock              | StockController        | StockService        | Stock        |
| Receivables        | ReceivableController   | ReceivableService   | Receivable   |
| Banks              | BankController         | BankService         | Bank         |
| Parties            | PartyController        | PartyService        | Party        |
| Products           | ProductController      | ProductService      | Product      |
| Measurement Units  | MeasurementUnitController | MeasurementUnitService | MeasurementUnit |

All entities extend `CommonColumn` (base class with shared audit fields).

## Tests

No test suite currently exists (`src/test/` is absent). Test dependencies are commented out in `pom.xml`.
