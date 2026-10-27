# Lab 08 - Observer Pattern

Maven implementation using the assignment's `Observer` and `Subject` interfaces.

## Run

Requires JDK 8+ and Maven. From `Lab08`:

```sh
mvn clean package exec:java
```

Main class: `edu.spu.se411.lab08.App` (matching the assignment annex).
A while loop simulates ten temperature readings (20-35 C) and humidity
readings (40-60%), with a one-second pause. Both observers print each update
with `System.out.printf`. The logger also writes readings through SLF4J.
The demonstration then changes an unobserved clone and registers a separate
dashboard for it. Interruption stops the loop and preserves the interrupt flag.

## Design

`Sensor` shares registration, unregistration, notification, reading state, and
cloning. `TemperatureSensor` and `HumiditySensor` inherit this behavior and
provide their names and units. Observers receive the changed subject and read
its current value. Duplicate subscriptions are ignored; repeated readings do
not trigger notifications. The first reading always notifies, including zero.
Notification uses a subscription snapshot so callbacks can unregister safely.
Readings must be finite and observers must be non-null.

Cloning preserves the concrete sensor type and current reading but starts with
an empty, separate observer collection. Changing a clone's reading or
subscriptions does not change the original sensor.

## Logging and verification

The requested SLF4J API and `slf4j-log4j12` dependencies use version 2.0.16.
Maven relocates the latter to `slf4j-reload4j`, which reads the provided
Log4j-compatible configuration. The exec plugin uses version 3.6.3.
Logs are written to `logs/App/log4j/log.out`; logs and build outputs are ignored.

```sh
mvn clean verify
```

Five JUnit tests cover both sensors, change notifications, duplicate
registration, unregistration, callback removal, invalid readings, and clone
value/type preservation and independent subscriptions.
