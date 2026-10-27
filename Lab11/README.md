# Lab 11: Introduction to Threads

This Maven project demonstrates the double-spending problem and a simple synchronized solution.

Requires Java 21 and Maven. From this directory run:

    mvn clean test exec:java

The application creates ten accounts containing 1,000 SAR each and starts 10,000 Java virtual threads, one per random transaction. A start latch lets transfers overlap, and all threads are joined before the total is calculated. Each experiment starts with fresh accounts and the same seeded random transaction sequence.

The first experiment calls Transaction.execute() without synchronization. Account updates deliberately yield between reading and writing the balance to expose lost updates. Its final total varies with scheduling; a race is not guaranteed on every run.

The second experiment calls the single static synchronized method Transaction.executeSynchronized(). Its shared class monitor protects both the debit and credit across all transaction instances. Synchronizing an instance method on each separate transaction would not protect the shared accounts. All concurrent callers in the safe experiment must use this entry point.

A rejected transfer leaves both accounts unchanged. Transfers to the same account do nothing. Invalid, negative, and nonfinite amounts are rejected. Start/stop events and rejected transfers are written to logs/log.out.

## Verified output

    Unsynchronized: 10000 threads, total = 9656.07 SAR, difference = -343.93 SAR, rejected = 0
    Synchronized: 10000 threads, total = 10000.00 SAR, difference = -0.00 SAR, rejected = 0

All four JUnit tests passed, covering initial funds, transfer/rejection behavior, invalid amounts and self-transfers, and conservation after 10,000 synchronized transfers. Floating-point comparisons use a tolerance because the starter represents amounts using Double; tiny rounding differences can print as -0.00.

The shared monitor serializes transfers, providing a simple lab solution at the cost of concurrency. Java 21 virtual threads keep the required 10,000 Thread instances practical.
