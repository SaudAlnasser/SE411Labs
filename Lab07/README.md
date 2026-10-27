# Lab 07 - Polymorphism in Java

Maven implementation of the travel agency booking scenario.

## Run

Requires JDK 8+ and Maven. From `Lab07`:

```sh
mvn clean package exec:java
```

The main class is `edu.psu.se411.lab07.App`. The demonstration first catches and
logs missing luggage information and an invalid rental duration. It then fills
in the required information and processes a `Booking[]` using
`computeTotalPrice(Booking)`, with no type-specific branches in that method.

Expected totals:

| Booking | Inputs | Total |
| --- | --- | --- |
| Flight | Base 500.00, luggage 20 kg | 600.00 |
| Standard train | 400 km | 200.00 |
| First-class train | 400 km | 320.00 |
| Car rental | Daily rate 150.00, 3 days | 450.00 |

## Design and business rules

`Booking` owns the ID, customer full name, travel date, and destination city.
Its subclasses override `calculateTotalPrice()`. Shared validation helpers and
configuration constants avoid duplicate validation and hard-coded limits.
`SeatClass` represents STANDARD and FIRST_CLASS train seats.

- Flight: base price plus weight times the extra luggage rate; 0-40 kg inclusive.
- Train: distance times the selected seat rate; 1-2000 km inclusive.
- Car rental: daily rate times days; 1-30 whole days inclusive.

Luggage weight, distance, and rental days are supplied after construction.
Calculating before a value is supplied raises `MissingInformationException`.
Invalid supplied values raise `InvalidArgumentException` before changing state.
Shared fields, rates, and seat classes are also validated. NaN and infinite
weights or distances are rejected. Monetary arithmetic uses `BigDecimal`;
console output rounds to two decimal places.

The assignment does not specify numeric rates or a currency. `Configuration`
uses illustrative rates of 5.00 per kg, 0.50 per standard km, and 0.80 per
first-class km, in a single consistent currency. Base and daily prices are
constructor arguments.

## File logging

SLF4J 2.0.16 and the assignment's `slf4j-log4j12` dependency are configured.
Maven relocates that dependency to `slf4j-reload4j` 2.0.16, which reads the
Log4j-compatible `src/main/resources/log4j.properties` configuration.
The exec Maven plugin is version 3.6.3, as requested.

Run from this folder to write `logs/App/log4j/log.out`. The file contains
application start and stop events plus caught exceptions and their stack traces.
Generated logs and Maven build output are ignored by Git.

## Validation

```sh
mvn clean verify
```

Nine JUnit tests cover polymorphic prices, both train seat classes, missing
information, inclusive boundaries, invalid values, unchanged state after rejected
updates, fractional weights and distances, and shared constructor validation.
