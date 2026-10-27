package edu.psu.se411.lab12_threads;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.concurrent.*;
import org.junit.jupiter.api.Test;
import edu.psu.se411.lab12_threads.model.*;
import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;

class TransactionTest {
    @Test void transferAndRejectionAreAtomic() throws Exception {
        CryptoAccount a = new CryptoAccount("a", 100.0);
        CryptoAccount b = new CryptoAccount("b", 0.0);
        new Transaction("1", a, b, 40.0).execute();
        assertEquals(60.0, a.getBalance());
        assertEquals(40.0, b.getBalance());
        assertThrows(InsufficientBalanceException.class, () -> new Transaction("2", a, b, 61.0).execute());
        assertEquals(60.0, a.getBalance());
        assertEquals(40.0, b.getBalance());
        new Transaction("3", b, a, 10.0).execute();
        assertEquals(70.0, a.getBalance());
    }

    @Test void invalidInputsAndOverflowLeaveBalancesUnchanged() {
        CryptoAccount a = new CryptoAccount("a", 100.0);
        CryptoAccount b = new CryptoAccount("b", Double.MAX_VALUE);
        for (Double value : new Double[] {null, -1.0, Double.NaN, Double.POSITIVE_INFINITY}) {
            assertThrows(IllegalArgumentException.class, () -> new Transaction("x", a, b, value));
            assertThrows(IllegalArgumentException.class, () -> a.retrieveFromBalance(value));
        }
        assertThrows(IllegalArgumentException.class, () -> new Transaction("x", a, a, 1.0));
        CryptoAccount c = new CryptoAccount("c", Double.MAX_VALUE);
        assertThrows(IllegalArgumentException.class, () -> new Transaction("x", c, b, Double.MAX_VALUE).execute());
        assertEquals(Double.MAX_VALUE, c.getBalance());
        assertEquals(Double.MAX_VALUE, b.getBalance());
    }

    @Test void oppositeTransfersDoNotDeadlock() throws Exception { concurrentTransfers("a", "b"); }

    @Test void duplicateIdsStillHaveTotalOrder() throws Exception { concurrentTransfers("same", "same"); }

    private void concurrentTransfers(String firstId, String secondId) throws Exception {
        CryptoAccount a = new CryptoAccount(firstId, 100_000.0);
        CryptoAccount b = new CryptoAccount(secondId, 100_000.0);
        ExecutorService pool = Executors.newFixedThreadPool(8, task -> {
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            return thread;
        });
        CountDownLatch start = new CountDownLatch(1);
        ArrayList<Future<?>> futures = new ArrayList<>();
        try {
            for (int i = 0; i < 8; i++) {
                boolean forward = i % 2 == 0;
                futures.add(pool.submit(() -> {
                    start.await();
                    for (int j = 0; j < 10_000; j++)
                        new Transaction("x", forward ? a : b, forward ? b : a, 1.0).execute();
                    return null;
                }));
            }
            start.countDown();
            for (Future<?> future : futures) future.get(15, TimeUnit.SECONDS);
            assertEquals(100_000.0, a.getBalance());
            assertEquals(100_000.0, b.getBalance());
        } finally { pool.shutdownNow(); }
    }
}
