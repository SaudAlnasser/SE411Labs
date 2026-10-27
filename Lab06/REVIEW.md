# Lab 06 AI logging review

Reviewer: OpenAI Codex. Scope: the supplied wallet starter, the Lab 06 event
requirements, logging configuration, and the completed implementation.

## Evaluation and applied recommendations

| Finding | Applied improvement |
| --- | --- |
| Console printing bypasses log levels and destinations. | Use static final SLF4J loggers in App, WalletAccount and InsufficientFundsException. |
| Successful wallet activity should not clutter normal output. | Keep creation and successful deposit/withdrawal events at DEBUG; keep lifecycle events at INFO. |
| Exception messages alone lose the failure location. | Pass the throwable to ERROR logging at the App handling boundary, preserving its stack trace. |
| Logging the same stack trace at multiple layers adds noise. | Wallet methods throw without logging ERROR; App logs each caught failure once. The lab-required construction WARN has no stack trace. |
| Balance and transaction values can disclose financial details. | Omit amounts, balances and account identifiers from logs and exception messages. Operation names and exception types identify the failure. |
| A single unbounded log file grows indefinitely. | Use RollingFileAppender with a 1 MB limit and three backups while retaining the requested file path and pattern. |
| Installing both providers makes provider choice ambiguous. | Separate Logback, Log4j-compatible and API-only demonstrations into Maven profiles; run one profile at a time. |
| NaN, infinity and overflow could corrupt wallet state and produce misleading success logs. | Reject non-finite inputs and overflowing deposits before mutation; log success only after mutation succeeds. |

## Assignment-specific tradeoffs

The assignment explicitly requests WARN when an InsufficientFundsException is
constructed and ERROR for a thrown exception. This implementation preserves
that mapping: construction produces WARN and the caught failed operation
produces ERROR. In a production wallet, insufficient funds is often an expected
business outcome; a single WARN at the handling boundary would usually be
more appropriate. Logging inside exception constructors is retained here only
to satisfy the exercise.

The class-name pattern `%C` is retained as requested, although caller location
lookup costs more than logger-name formatting. A production high-volume service
could use `%c` instead. The starter uses double arithmetic; exact financial
accounting should use a decimal money representation in a separate change.
File retention limits disk use but is not a replacement for operational log
collection or access controls.

## Verification

- `mvn clean verify`: six tests, no failures or errors.
- API-only profile: required no-provider warning, successful completion.
- Logback profile: lifecycle messages and handled failures appear on the console.
- Default Log4j-compatible profile: file output includes the required format.
- ERROR, WARN, INFO and DEBUG thresholds: checked against the event matrix in README.md.
- Failed withdrawals, invalid inputs and overflow preserve the previous balance.

Reference: [SLF4J manual](https://slf4j.org/manual.html), including provider selection
and relocation of slf4j-log4j12 to slf4j-reload4j.
