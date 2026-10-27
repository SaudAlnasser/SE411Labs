package edu.psu.se411.lab12_threads.model;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;

public class CryptoAccount {
    private static final AtomicLong NEXT_ORDER = new AtomicLong();
    private final long order = NEXT_ORDER.getAndIncrement();
    private final String id;
    private double balance;
    final ReentrantLock lock = new ReentrantLock();

    public CryptoAccount(String id, Double balance) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Account ID required");
        validateAmount(balance);
        this.id = id;
        this.balance = balance;
    }

    static void validateAmount(Double amount) {
        if (amount == null || !Double.isFinite(amount) || amount < 0)
            throw new IllegalArgumentException("Amount must be finite and nonnegative");
    }

    // Immutable IDs define the order; a unique sequence breaks ties for duplicate IDs.
    int compareOrder(CryptoAccount other) {
        int byId = id.compareTo(other.id);
        return byId != 0 ? byId : Long.compare(order, other.order);
    }

    public void addToBalance(Double amount) {
        validateAmount(amount);
        lock.lock();
        try {
            if (!Double.isFinite(balance + amount)) throw new IllegalArgumentException("Balance overflow");
            balance += amount;
        } finally { lock.unlock(); }
    }

    public void retrieveFromBalance(Double amount) throws InsufficientBalanceException {
        validateAmount(amount);
        lock.lock();
        try {
            if (amount > balance) throw new InsufficientBalanceException(id);
            balance -= amount;
        } finally { lock.unlock(); }
    }

    public String getId() { return id; }

    public Double getBalance() {
        lock.lock();
        try { return balance; }
        finally { lock.unlock(); }
    }
}
