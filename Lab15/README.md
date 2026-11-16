# Lab 15 — Spring CRUD with MySQL

Saud Abdulrahman Alnasser

## Implementation

Migrates the supplied Lab 14 course starter to MySQL, retaining its controller, service, repository and model layers. The handout illustrates the migration using a product table, but its supplied project implements courses; this solution applies the same changes to the course table.

- MySQL Connector/J 8.0.33 replaces H2 in the application. Uses the relocated Maven coordinates com.mysql:mysql-connector-j.
- MySQL schema uses AUTO_INCREMENT and drops/recreates the course table on startup, as requested by the lab. Use a dedicated lab database: startup deletes existing course records.
- Seed inserts omit the generated ID.
- Aiven connection settings come from environment variables; no passwords are committed.
- Corrected the starter's POST Location header to include /courses/.

## Part A — Aiven setup

1. Sign in to https://console.aiven.io/ and create a free MySQL service under a personal project.
2. Create the database se411db from the service's Databases tab.
3. Copy your service host, port, username and password. Configure the VS Code database extension with those values and enable SSL.
4. Run the application with those same settings below.

Aiven provisioning and live connection verification still require the student's account and service credentials. They have not been completed by this submission.

## Part B — Run

Requires Java 17 or newer and Maven (or the included Maven wrapper).

PowerShell, from Lab15:

~~~powershell
$env:DB_HOST = 'YOUR_AIVEN_HOST'
$env:DB_PORT = 'YOUR_AIVEN_PORT'
$env:DB_NAME = 'se411db'
$env:DB_USERNAME = 'avnadmin'
$env:DB_PASSWORD = 'YOUR_AIVEN_PASSWORD'
./mvnw.cmd spring-boot:run
~~~

The JDBC URL enables sslMode=REQUIRED. The application initializes schema.sql and data.sql on startup.

## Part C — Exercise all five CRUD operations

Use requests.http as the request reference for Eclipse PutMan/Postman or a VS Code HTTP client.

| Operation | Method and URL | Expected status |
| --- | --- | --- |
| Read all | GET /api/v1/courses | 200 |
| Create | POST /api/v1/courses | 201 + Location |
| Read one | GET /api/v1/courses/{id} | 200, or 404 if absent |
| Update | PUT /api/v1/courses/{id} | 204, or 404 if absent |
| Delete | DELETE /api/v1/courses/{id} | 204, or 404 if absent |

For POST and PUT, send Content-Type: application/json and omit id from the body:

~~~json
{"code":"SE411","name":"Software Construction","description":"Lab 15 MySQL","credits":3}
~~~

Use the generated ID in the POST response Location header for the remaining requests. GET after PUT must show the changed fields; GET after DELETE must return 404.

## Automated verification

~~~powershell
./mvnw.cmd clean verify
~~~

The tests exercise the full HTTP CRUD lifecycle, seed data, generated IDs, Location header, missing records and invalid input. H2 is present only in test scope with MySQL compatibility mode; it is excluded from the packaged application. These tests do not prove connectivity to Aiven or replace the handout's live MySQL/PutMan checks.
