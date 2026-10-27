package edu.psu.se411.lab12_threads;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import edu.psu.se411.lab12_threads.model.*;
import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;

public final class Benchmark {
    private Benchmark() {}

    public static void run(boolean cached, String[] args) throws InterruptedException {
        int count = args.length > 0 ? Integer.parseInt(args[0]) : 1000;
        int transactions = args.length > 1 ? Integer.parseInt(args[1]) : 1_000_000;
        if (count < 2 || transactions < 1) throw new IllegalArgumentException("Need >=2 accounts and >=1 transaction");
        CryptoAccount[] accounts = new CryptoAccount[count];
        for (int i = 0; i < count; i++) accounts[i] = new CryptoAccount("acc_" + i, 10_000.0);
        AtomicInteger created = new AtomicInteger();
        ThreadFactory factory = task -> new Thread(task, "transfer-" + created.incrementAndGet());
        ExecutorService pool = cached ? Executors.newCachedThreadPool(factory)
                                     : Executors.newFixedThreadPool(8, factory);
        // Same backpressure for both pools; do not create a million native threads.
        Semaphore outstanding = new Semaphore(256);
        AtomicInteger completed = new AtomicInteger();
        AtomicInteger rejected = new AtomicInteger();
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Random random = new Random(41112);
        long start = System.nanoTime();
        try {
            for (int i = 0; i < transactions; i++) {
                int from = random.nextInt(count);
                int to = random.nextInt(count - 1);
                if (to >= from) to++;
                Transaction transfer = new Transaction("tx_" + i, accounts[from], accounts[to],
                        random.nextInt(10_001) / 100.0);
                outstanding.acquire();
                try {
                    pool.execute(() -> {
                        try { transfer.execute(); }
                        catch (InsufficientBalanceException e) { rejected.incrementAndGet(); }
                        catch (Throwable e) { failure.compareAndSet(null, e); }
                        finally { completed.incrementAndGet(); outstanding.release(); }
                    });
                } catch (RuntimeException | Error e) {
                    outstanding.release();
                    throw e;
                }
            }
        } finally {
            pool.shutdown();
        }
        if (!pool.awaitTermination(5, TimeUnit.MINUTES)) {
            pool.shutdownNow();
            throw new IllegalStateException("Transfers did not finish");
        }
        double elapsed = (System.nanoTime() - start) / 1_000_000.0;
        double total = 0;
        for (CryptoAccount account : accounts) {
            if (account.getBalance() < 0) throw new IllegalStateException("Negative balance");
            total += account.getBalance();
        }
        if (failure.get() != null) throw new IllegalStateException("Transfer failed", failure.get());
        if (completed.get() != transactions || Math.abs(total - count * 10_000.0) > 0.001)
            throw new IllegalStateException("Completion or conservation check failed");
        System.out.printf("%s accounts=%d transactions=%d time_ms=%.2f threads=%d rejected=%d initial=%.2f final=%.2f%n",
                cached ? "cached" : "fixed", count, completed.get(), elapsed, created.get(),
                rejected.get(), count * 10_000.0, total);
    }
}
