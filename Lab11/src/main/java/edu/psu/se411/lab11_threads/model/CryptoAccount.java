package edu.psu.se411.lab11_threads.model;

import edu.psu.se411.lab11_threads.exceptions.InsufficientBalanceException;

public class CryptoAccount {
    private final String id;
    private double balance;

    public CryptoAccount(String id, Double balance) {
        if (id == null || balance == null || !Double.isFinite(balance) || balance < 0)
            throw new IllegalArgumentException("An account needs an ID and a nonnegative finite balance");
        this.id = id;
        this.balance = balance;
    }

    private static void validate(Double amount) {
        if (amount == null || !Double.isFinite(amount) || amount < 0)
            throw new IllegalArgumentException("Amount must be nonnegative and finite");
    }

    public void addToBalance(Double amount) {
        validate(amount);
        double updated = balance + amount;
        // Widen the race window so the unsynchronized experiment is easy to observe.
        Thread.yield();
        balance = updated;
    }

    public void retrieveFromBalance(Double amount) throws InsufficientBalanceException {
        validate(amount);
        if (balance < amount) throw new InsufficientBalanceException("Insufficient balance in " + id);
        double updated = balance - amount;
        Thread.yield();
        balance = updated;
    }

    public String getId() { return id; }
    public Double getBalance() { return balance; }
}
