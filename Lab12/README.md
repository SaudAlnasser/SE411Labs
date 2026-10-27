# Lab 12: Threads and Locks

Requires Java 21 and Maven. This project extends the supplied crypto-account starter with a fixed thread pool, a second cached-pool main program, and atomic transfers protected by ordered ReentrantLocks.

## Build and run

From Lab12:

    mvn clean test package
    java -cp target/classes edu.psu.se411.lab12_threads.App 1000 1000000
    java -cp target/classes edu.psu.se411.lab12_threads.CachedPoolApp 1000 1000000
    java -cp target/classes edu.psu.se411.lab12_threads.App 10 1000000
    java -cp target/classes edu.psu.se411.lab12_threads.CachedPoolApp 10 1000000

The arguments are account count and transaction count. Defaults are 1,000 accounts and one million transactions. Each account starts with 10,000.00. Both programs generate the same sequence of account pairs and amounts using seed 41112, create fresh accounts, wait for completion, and verify the final total and nonnegative balances. Timing includes transaction generation, submission, execution, and waiting, but excludes account initialization and result printing.

The fixed pool uses eight platform threads. The cached pool uses Executors.newCachedThreadPool with platform threads. Both use a semaphore allowing at most 256 outstanding tasks, applying identical submission backpressure. This avoids an uncontrolled native-thread explosion when many tasks wait for locks; it is a bounded-submission comparison, not a measurement of unrestricted cached-pool submission. The starter's fixed pool size equal to the transaction count would also be impractical at one million transactions.

## Measured comparison

Measured on the local Windows machine with Java 21 on October 27, 2026. One run per configuration, in the order below:

| Pool | Accounts | Transactions | Time (ms) | Threads created | Rejected transfers | Initial total | Final total |
| --- | ---: | ---: | ---: | ---: | ---: | ---: | ---: |
| Fixed | 1,000 | 1,000,000 | 534.09 | 8 | 0 | 10,000,000.00 | 10,000,000.00 |
| Cached | 1,000 | 1,000,000 | 1,636.94 | 19 | 0 | 10,000,000.00 | 10,000,000.00 |
| Fixed | 10 | 1,000,000 | 567.17 | 8 | 3,182 | 100,000.00 | 100,000.00 |
| Cached | 10 | 1,000,000 | 1,617.10 | 34 | 3,232 | 100,000.00 | 100,000.00 |

Observation: the fixed pool was about three times faster for these short transfer tasks. Adding cached-pool threads did not improve throughput. With ten accounts, more transfers compete for the same locks, and the cached pool created more threads. Its timing was similar between the two account counts; these single-run measurements do not establish a statistically reliable difference between those cases. Scheduling, thread creation, JVM warmup, and submission overhead affect the timings, so results are machine-dependent.

Rejected transfers have insufficient funds and leave both balances unchanged. Even with the same seeded requests, scheduling changes the order of transfers and therefore the rejection count. A rejection is included in the completed transaction count.

## What could go wrong with locks?

Simply locking the sender and then the receiver can deadlock. A transfer A to B could hold A while waiting for B, while a transfer B to A holds B while waiting for A. Neither can progress. Forgetting to unlock after an exception can also block later transfers.

The solution establishes a total order: compare immutable account IDs, with a unique account creation sequence as a tie-breaker when IDs match. Every transfer locks the lower account first and then the higher account, irrespective of transfer direction. A cycle of waiting is therefore impossible. Locks are released in reverse order using nested finally blocks.

Both locks remain held while checking funds and updating both balances. Account reads, credits, and withdrawals also use the same reentrant locks, so their nested acquisition is safe. Validation and receiver overflow checks happen before either balance changes. Transaction fields are immutable so a submitted transfer cannot change its accounts or amount while running. No synchronized methods or blocks are used.

The starter's Double representation is retained. Total comparisons allow 0.001 of floating-point rounding error; production monetary code would normally use integer minor units or a decimal type.

## Verification

Four JUnit tests cover:
- Successful transfers and unchanged balances after insufficient-funds rejection.
- Invalid amounts, self-transfers, and atomic rejection of overflow.
- 80,000 concurrent transfers in opposite directions, with time-bounded completion and exact final balances.
- The same concurrent workload for distinct accounts with duplicate IDs, exercising the total-order tie-breaker.

All four million-transaction benchmark runs also check completed-task count, total conservation, and nonnegative balances.
