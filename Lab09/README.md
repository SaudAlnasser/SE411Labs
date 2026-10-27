# Lab 09 - Managing an Inventory with the Strategy Pattern

Requires JDK 8+ and Maven. From this folder:

```sh
mvn clean verify exec:java
```

The starter package and SLF4J file logging are retained. The compiler target is
Java 8 so the lab also builds on the available JDK 17. Logs are written to
`logs/log.out`; generated logs and build output are ignored.

## Assignment implementation

- `Inventory<T>` stores any product type, with `addItem`, `removeItem`, and
  `getAllItems`. Returned lists are independent copies.
- `Item` supplies ID and name; `Book` adds author and `ElectronicDevice` adds
  category. Their string representations include all details.
- `SearchStrategy<T>` is a functional interface. `findItems` returns every match
  in insertion order. It accepts strategies for T or its supertypes.
- `SearchById` and `SearchByName` search shared fields. `SearchByAuthor` and
  `SearchByCategory` check the product type before reading specialized fields,
  so both work safely in a mixed inventory and in a typed product inventory.
- Searches use exact, case-sensitive string equality. A missing match returns
  an empty list. Null items, strategies, and product fields are rejected.
- `App` demonstrates a mixed `Inventory<Item>`, all four strategies, book and
  device name searches, a lambda strategy, removal, and wildcard display through
  `displayInventory(Inventory<?>)`. Start, stop, and unexpected failures are logged.

The handout uses both `findItem` and `findItems`; this implementation uses the
plural name shown in its required example because the result is a list.

## Verification

Eight JUnit tests cover adding/removing, shared and specialized searches, missing
matches, lambda searches with multiple results, defensive copies, typed and
unrelated inventories, wildcard display, empty inventories, and null rejection.
