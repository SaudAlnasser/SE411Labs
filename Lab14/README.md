# Lab 14: Spring CRUD

Java 17 / Spring Boot 4.0.5 REST API using Spring Data JDBC, H2, and Jakarta Bean Validation.

## Run and test

From this directory with Java 17+ and Maven installed:

```sh
mvn clean test
mvn spring-boot:run
```

The API runs at `http://localhost:8080/api/v1/courses`. H2 is in memory: data resets on restart. `schema.sql` creates the table and `data.sql` seeds two courses.

| Method | Path | Success | Errors |
| --- | --- | --- | --- |
| POST | /api/v1/courses | 201 with Location header | 400 invalid input; 422 duplicate ID |
| GET | /api/v1/courses | 200 | |
| GET | /api/v1/courses/{id} | 200 | 404 missing |
| PUT | /api/v1/courses/{id} | 204 | 400 invalid input or mismatched ID; 404 missing |
| DELETE | /api/v1/courses/{id} | 204 | 404 missing |

POST and PUT accept the following JSON:

```json
{"id":14,"code":"SE414","name":"Web Development","description":"Building REST APIs","credits":3}
```

IDs are supplied by the client, required, and nonnegative (zero is allowed). Code must not be blank, name must have at least two characters and not be blank, and credits are required and between 1 and 10. PUT requires a full course with the same ID as the URL. Description is optional. An explicit JDBC aggregate insert handles assigned IDs correctly instead of treating every non-null ID as an update.

Integration tests use a real HTTP server and H2 database to cover seeded reads, creation and Location, duplicate IDs, updates, deletion, missing resources, malformed JSON, and validation boundaries.

Implementation assistance: OpenAI Codex was used for code and validation (rather than Copilot).
