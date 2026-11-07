# Lab 13: Spring Introduction

Java 17+, Maven, and Spring Boot 4.1.2. A Spring Web MVC API backed by Spring Data JDBC and an in-memory H2 database. Constructor injection connects the controller, service, and repository.

## Run

From Lab13:

```sh
mvn clean test package
mvn spring-boot:run
```

Open http://localhost:8080/api/v1/products in a browser or send GET requests with Postman. The database reloads the 17 products from the handout each time the application starts; it is not persistent.

| Request | Expected result |
| --- | --- |
| GET /api/v1/products | 200, JSON array of 17 products |
| GET /api/v1/products/10 | 200, product 10 with price 1840.25 |
| GET /api/v1/products/999 | 404, product not found |
| GET /api/v1/products/-1 | 400, invalid ID |
| GET /api/v1/products/0 | 400, invalid ID |
| GET /api/v1/products/abc | 400, invalid integer |

## Validation and money

AI-assisted recommendation: use `spring-boot-starter-validation` with Jakarta Bean Validation. The controller's `@Positive` constraint is enforced by Spring MVC method validation. A negative ID returns **HTTP 400 Bad Request** before repository lookup. Product fields have constraints for positive IDs, nonblank names, maximum text lengths, nonnegative prices, and two fractional digits. Null IDs are permitted for future database-generated inserts. This lab exposes only the requested read endpoints; record constraints are also tested directly through the validator.

Prices use `BigDecimal` and SQL `DECIMAL(10,2)` instead of floating-point numbers. The repository extends both `CrudRepository` and `PagingAndSortingRepository`. Generated IDs start at 27 to avoid collisions with the supplied IDs 10–26.

## Tests

Five integration tests start a real HTTP server on a random port and check JSON responses, seed count, exact stored price, missing products, invalid IDs, and record validation.
