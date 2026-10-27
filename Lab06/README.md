# Lab 06 - Logging with SLF4J

SE411 logging exercise, based on the supplied wallet starter project.

## Run

Requires JDK 8+ and Maven. Import this directory as an existing Maven project in Eclipse, or run from `Lab06`:

```sh
mvn clean package exec:java
```

The demonstration creates a wallet with 1000, deposits 100, withdraws 100,
tries to withdraw 1500, then tries to deposit -100. Both failures are handled,
and the application ends normally. The final wallet balance is 1000.

The default provider writes to `logs/App/log4j/log.out`, relative to the working
directory. Generated logs and build files are excluded from Git.

## Provider exercises

Run each command separately. Select exactly one provider profile.

```sh
# SLF4J facade only: displays "No SLF4J providers were found."
mvn -Papi-only clean package exec:java

# Logback: displays application logs in the console
mvn -Plogback clean package exec:java

# Final lab configuration: Log4j-compatible file logging
mvn -Plog4j clean package exec:java
```

The final profile declares the assignment's `org.slf4j:slf4j-log4j12:2.0.16`.
Maven relocates this artifact to `slf4j-reload4j:2.0.16`; the relocation warning
is expected. It uses the requested `log4j.properties` format and
`org.apache.log4j` appender classes. See the
[SLF4J manual](https://slf4j.org/manual.html) for the relocation explanation.
The API is explicitly pinned to 2.0.16 in all profiles.

## Level experiments

Maven filters `log.level` into both configuration files. Use a build command
to apply a changed level; invoking only `exec:java` uses the last copied resources.

```sh
mvn clean package exec:java "-Dlog.level=ERROR"
mvn clean package exec:java "-Dlog.level=WARN"
mvn clean package exec:java "-Dlog.level=INFO"
mvn clean package exec:java "-Dlog.level=DEBUG"
```

| Threshold | Events per demonstration run |
| --- | --- |
| ERROR | Two handled failures with stack traces |
| WARN | Above, plus exception construction warning |
| INFO | Above, plus application start and end |
| DEBUG | Above, plus wallet creation, deposit and withdrawal success |

Unlike the early single-INFO step in the worksheet, the completed application
still emits ERROR events at the ERROR threshold because it deliberately
exercises two failures. INFO startup/shutdown messages are suppressed there.
Logs append between runs. Compare the newest timestamps to see each run.

The pattern includes timestamp, priority, thread, class, message and newline.
The file appender rolls at 1 MB and keeps three backups, an improvement from
the worksheet's unbounded FileAppender.

## AI evaluation and validation

[REVIEW.md](REVIEW.md) records the Codex logging evaluation, applied improvements,
and remaining tradeoffs. All required event levels are preserved.

```sh
mvn clean verify
```

Six JUnit tests cover successful transfers and full withdrawal, insufficient
funds, invalid amounts, invalid balances, overflow and zero amounts. The
starter's double-based API and allowance of zero transactions are retained;
non-finite values are rejected before mutation.
