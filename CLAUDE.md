# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Automatix is a Spring Boot + Thymeleaf web application for small business cash/inventory management (branded as "Automatix"). It manages cash in/out, sales, receivables, stock, and master data (parties, products, banks, measurement units).

## Build & Run

```bash
# Run the application (Windows)
mvnw.cmd spring-boot:run

# Build (skip tests)
mvnw.cmd clean package -DskipTests

# Unix/Mac
./mvnw spring-boot:run
./mvnw clean package -DskipTests
```

**Prerequisites:** PostgreSQL running locally. Configure connection via `.env` file (copy `.env` values into `application.properties` if needed). Default DB: `automatix` on `localhost:5432`.

App runs on **HTTP port 8090** and **HTTPS port 8443** by default.

## Tests

Test dependencies are currently commented out in `pom.xml`. When re-enabled:

```bash
mvnw.cmd test                                   # all tests
mvnw.cmd test -Dtest=ClassName                  # single class
mvnw.cmd test -Dtest=ClassName#methodName       # single method
```

## Architecture

Three-tier layered architecture with server-side rendering:

```
Thymeleaf templates (resources/templates/)
    ↓
AutomatixWebController  (web/)         — server-rendered pages
REST Controllers        (controller/)  — JSON API endpoints
    ↓
Service layer           (service/)     — all business logic lives here
    ↓
JPA Repositories        (repository/)  — data access with custom @Query methods
    ↓
PostgreSQL via Hibernate
```

**Package root:** `com.teamsits.automatix`

**Entry point:** `AutomatixApplication.java` — enables `@EnableScheduling`, `@EnableAsync`, `@EnableCaching`

## Domain Model

Five core transaction domains, each with its own controller/service/repository/model:

| Domain | Description |
|--------|-------------|
| **CashIn** | Money received (linked to Party and Bank) |
| **CashOut** | Money spent (linked to Party and Bank) |
| **Sales** | Product sales (linked to Product, Party, and Stock) |
| **Receivable** | Amounts owed by parties |
| **Stock** | Inventory changes; `StockTransactionType` enum: PURCHASE or SALES |

**Master data** (CRUD only): `Party`, `Product`, `Bank`, `MeasurementUnit`

**Audit base class:** `CommonColumn` — all entities inherit `createdBy`, `updatedBy`, timestamps, and `isDeleted` (soft delete flag). Never hard-delete rows; set `isDeleted = true`.

## Key Conventions

- **DTOs**: Separate `Request` and `Response` models in `models/` — controllers accept Requests, return Responses. Entities are never exposed directly.
- **Stock side-effect**: Creating a Sale or Purchase also writes a `Stock` record. Both saves happen inside the same service method (not transactional annotation — keep this in mind when making changes).
- **Date filtering**: Most repository queries accept a date range. `DateUtil` and `AppDate` handle date parsing/formatting.
- **Excel export**: `StockService` generates Excel reports via Apache POI; the endpoint returns a file download.
- **Thymeleaf layout**: All pages extend `templates/fragments/layout.html` as the master template.
- **CORS**: `@CrossOrigin("*")` is on all REST controllers; the `WebConfig.java` CORS config is currently commented out.
- **Lombok**: All entities and models use Lombok (`@Data`, `@Builder`, `@NoArgsConstructor`, etc.) — do not write manual getters/setters.
- **Currency utility**: `NumberToBanglaTakaUtil` converts numeric amounts to Bangla Taka text (used in reports).

## Tech Stack

- Java 8, Spring Boot 2.7.8, Hibernate/JPA
- Thymeleaf (server-side templates)
- PostgreSQL
- Maven (use `mvnw`/`mvnw.cmd` wrapper)
- Lombok, Apache POI, Commons Lang3
