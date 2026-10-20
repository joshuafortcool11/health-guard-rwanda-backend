# HealthGuard Rwanda - Backend

**Student:** Iradukunda Joshua
**Student ID:** 27967
**Project:** HealthGuard Rwanda - Community Health Insurance (Mutuelle) Enrollment Backend
**Exam:** Mid-Term Project Examination

A Spring Boot + PostgreSQL backend that manages household categories (Ubudehe), sectors,
enrollment officers, citizens, Mutuelle enrollments and coverage verifications.

## Tech stack

- Java 17
- Spring Boot 4.1.1 (`spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`)
- PostgreSQL
- Maven

## Prerequisites

- JDK 17+
- Maven
- PostgreSQL running on `localhost:5432`

## Database

Create the database (once):

```sql
CREATE DATABASE health_guard_rwanda;
```

Connection settings live in `src/main/resources/application.properties`:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/health_guard_rwanda
spring.datasource.username=postgres
spring.datasource.password=fortcool11
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8085
```

The schema is created/updated automatically by Hibernate on startup.

## Run

```bash
mvn spring-boot:run
```

The API is then served at `http://localhost:8085`.

## Domain model

| Entity | Table | Notes |
| --- | --- | --- |
| HouseholdCategory | household_category | Ubudehe categories |
| Sector | sector | unique `sectorNumber` |
| EnrollmentOfficer | enrollment_officer | holds many categories, belongs to one sector |
| Citizen | citizen | unique `nationalId` |
| Enrollment | enrollment | links a citizen to an officer, has `EnrollmentStatus` |
| CoverageVerification | coverage_verification | only for COVERED enrollments |

`EnrollmentStatus` values: `APPLIED`, `PAYMENT_PENDING`, `COVERED`, `REJECTED`, `CANCELLED`.

## Endpoints

### CRUD (generic)
Each entity exposes `POST /save`, `GET /all`, `GET /{id}`, `PUT /update/{id}`, `DELETE /delete/{id}`.
Base paths: `/api/citizens`, `/api/sectors`, `/api/household-categories`,
`/api/enrollment-officers`, `/api/enrollments`, `/api/coverage-verifications`.

### Requirement-specific

| ID | Method | Path |
| --- | --- | --- |
| B1 | GET | `/api/citizens/by-village?village=...` |
| B2 | GET | `/api/enrollments/by-status?status=...` |
| B3 | GET | `/api/enrollments/between?start=...&end=...` |
| C1 | GET | `/api/enrollment-officers/by-household-category?name=...` |
| C2 | GET | `/api/sectors/free` |
| C3 | GET | `/api/household-categories/unused` |
| C4 | GET | `/api/citizens/of-enrollment-officer/{enrollmentOfficerId}` |
| D1 | POST | `/api/enrollments/save` (date validation + officer day-uniqueness) |
| D2 | PATCH | `/api/enrollments/{id}/status?status=...` |
| D3 | PUT | `/api/enrollment-officers/{officerId}/household-categories/{categoryId}` |
| D4 | PATCH | `/api/enrollment-officers/{officerId}/sector?sectorNumber=...` |
| E1 | POST | `/api/coverage-verifications/save?enrollmentId=...` |
| E2 | GET | `/api/coverage-verifications/citizen/{nationalId}` |
| E3 | DELETE | `/api/coverage-verifications/delete/{id}` |
| F1 | GET | `/api/enrollments/stats/by-status` |
| F2 | PATCH | `/api/enrollments/cancel-day?enrollmentOfficerId=...&date=...` |
| Bonus | GET | `/api/dashboard/summary` |
| Bonus | GET | `/api/enrollments/page?page=0&size=5&sort=enrollmentDate,desc` |

## Postman

Import `health-guard-rwanda-midterm.postman_collection.json`. It contains one request per
requirement, named by its ID (A2, B1, C4, D2, ...), with the `baseUrl` and seeded ids stored
as collection variables.

## Seeded data

The database is populated through the API's own endpoints with 4 household categories,
4 sectors, 3 enrollment officers, 5 citizens, 10 enrollments (mixing every status) and
coverage verifications.
