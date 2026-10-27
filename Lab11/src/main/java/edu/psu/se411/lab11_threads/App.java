package edu.psu.se411.lab11_threads;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.psu.se411.lab11_threads.model.CryptoAccount;
import edu.psu.se411.lab11_threads.model.Transaction;
import edu.psu.se411.lab11_threads.exceptions.InsufficientBalanceException;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static final int nbAccounts = 10;
    public static final double initialBalance = 1000.0;
    public static final int nbTransactions = 10000;

    public static CryptoAccount[] createAccounts() {
        CryptoAccount[] accounts = new CryptoAccount[nbAccounts];
        for (int i = 0; i < accounts.length; i++)
            accounts[i] = new CryptoAccount("acc_" + i, initialBalance);
        return accounts;
    }

    public static double totalBalance(CryptoAccount[] accounts) {
        double total = 0;
        for (CryptoAccount account : accounts) total += account.getBalance();
        return total;
    }

    public static double runSimulation(boolean synchronizedMode) throws InterruptedException {
        CryptoAccount[] accounts = createAccounts();
        CountDownLatch start = new CountDownLatch(1);
        List<Thread> threads = new ArrayList<>();
        AtomicInteger failures = new AtomicInteger();
        Random random = new Random(411);
        for (int i = 0; i < nbTransactions; i++) {
            Transaction transaction = new Transaction(
                    accounts[random.nextInt(nbAccounts)], accounts[random.nextInt(nbAccounts)],
                    (random.nextInt(1000) + 100) / 100.0);
            // Java 21 virtual threads allow 10,000 real Thread instances without
            // requiring 10,000 operating-system threads.
            threads.add(Thread.ofVirtual().unstarted(() -> {
                try {
                    start.await();
                    if (synchronizedMode) Transaction.executeSynchronized(transaction);
                    else transaction.execute();
                } catch (InsufficientBalanceException e) {
                    failures.incrementAndGet();
                    logger.warn("Transaction rejected", e);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }));
        }
        for (Thread thread : threads) thread.start();
        start.countDown();
        for (Thread thread : threads) thread.join();
        double total = totalBalance(accounts);
        System.out.printf("%s: %d threads, total = %.2f SAR, difference = %.2f SAR, rejected = %d%n",
                synchronizedMode ? "Synchronized" : "Unsynchronized",
                nbTransactions, total, total - nbAccounts * initialBalance, failures.get());
        return total;
    }

    public static void main(String[] args) {
        logger.info("Application is starting...");
        try {
            runSimulation(false);
            runSimulation(true);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Simulation interrupted", e);
        } finally {
            logger.info("Application is closing...");
        }
    }
}
