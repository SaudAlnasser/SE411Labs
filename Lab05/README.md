# Lab 05 - Exception Handling

SE411, Fall 2026-2027. Both exercises share this Maven project.

## Run

Requires JDK 8+ and Maven. From `Lab05`:

```sh
mvn clean package exec:java
```

Enter an age, then withdrawal amounts or `q` to quit. The simulated wallet
starts at 100.00 and the linked bank account starts at 0.00. Successful
withdrawals debit the wallet and credit the bank. No real bank is contacted.

For a repeatable demonstration of valid and invalid ages, a successful
withdrawal, and insufficient funds:

```sh
mvn clean package exec:java -Dexec.args="--demo"
```

## Exercise 1

`App.validateAge(int)` throws checked `InvalidAgeException` below 18 and prints
`Age valid message.` at 18 or above. The main method catches the exception and
reports the reason. Custom exceptions live in `edu.psu.se411.exceptions`, which
contains only exception classes. The configured main class is `edu.psu.se411.App`
and the exec Maven plugin is version 3.5.0.

## Exercise 2

`Wallet.withdraw(BigDecimal)` throws checked `InsufficientFundsException` when
the amount exceeds the wallet balance. Validation happens before either balance
changes. Zero, negative, missing, and fractional-cent amounts are rejected with
`IllegalArgumentException`. Decimal arithmetic avoids floating-point errors.
The console catches expected input and business exceptions so users can retry.

## Validation and review

```sh
mvn clean verify
```

Eight JUnit tests cover age boundaries, transfers, withdrawing the full balance,
unchanged balances after failures, invalid amounts and opening balances, and
repeated decimal withdrawals.

Exception-handling review: checked exceptions represent recoverable business
failures; invalid arguments use a standard runtime exception; catches are
specific; messages explain the failure; and failed withdrawals preserve state.

The assignment also requests a GitHub Copilot review. That review has **not**
been performed: GitHub CLI is not authenticated and no Copilot review integration
is available in this session. The checks above are local validation, not a
Copilot review. To finish that step, use Copilot's code review on these Java files
in your editor or request a Copilot review on a pull request if available for
your account.
