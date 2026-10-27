# Exception-handling code review

Reviewer: OpenAI Codex

Date: 2026-10-27

Scope: `App`, `Wallet`, both custom exceptions, Maven configuration, and both
JUnit test classes, as implemented in commit `f038e5e`.

## Findings

No correctness issues requiring changes were found for this single-user console
lab. The review checked the following behavior:

- `validateAge` rejects every age below 18 with `InvalidAgeException` and accepts
  18 and above. The main method handles invalid ages and non-integer input.
- Both custom exceptions extend `Exception`, have descriptive messages, and
  reside in a package containing only exception classes. Callers explicitly
  handle or declare the checked exceptions.
- Withdrawals exceeding the available balance throw
  `InsufficientFundsException` before either balance changes. A withdrawal equal
  to the balance succeeds and credits the linked bank account.
- Null, zero, negative, and fractional-cent withdrawal amounts are rejected.
  Invalid opening balances are also rejected. `BigDecimal` preserves decimal
  precision, and rounding errors are not silently accepted.
- Catch blocks handle expected exception types without swallowing failures.
  Fractional-cent validation preserves the underlying arithmetic exception as
  its cause. Failed console withdrawals report the reason and allow a retry.
- Maven configures the required exec plugin version 3.5.0 and the correct main
  class. The tests cover age boundaries, wallet and bank balance changes,
  rejected operations, and repeated decimal withdrawals.

## Non-blocking observation

For malformed money input, the console currently displays the Java number
parser's message. A friendlier message such as "Enter a numeric amount, for
example 25.50" would improve usability. The current behavior still handles the
error and lets the user retry.

## Validation and limits

All eight JUnit tests pass. The Maven demo exercises ages 17 and 18, a successful
withdrawal, and insufficient funds. Console checks also cover malformed input,
non-positive amounts, fractional cents, recovery after errors, and emptying the
wallet.

This is a simulated, single-threaded wallet. The review does not establish
production banking security, concurrency safety, or real transfer reliability.

This review was performed by OpenAI Codex, not GitHub Copilot. It serves as the
substitute review requested by the author; acceptance of that substitution for
the assignment's Copilot-specific requirement is up to the instructor.
