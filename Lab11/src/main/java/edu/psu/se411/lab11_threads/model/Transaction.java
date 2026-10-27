package edu.psu.se411.lab11_threads.model;

import java.util.UUID;
import edu.psu.se411.lab11_threads.exceptions.InsufficientBalanceException;

public class Transaction {
    private final String id;
    private final CryptoAccount sender;
    private final CryptoAccount receiver;
    private final Double amount;

    public Transaction(CryptoAccount sender, CryptoAccount receiver, Double amount) {
        this(UUID.randomUUID().toString(), sender, receiver, amount);
    }

    public Transaction(String id, CryptoAccount sender, CryptoAccount receiver, Double amount) {
        if (id == null || sender == null || receiver == null || amount == null
                || !Double.isFinite(amount) || amount < 0)
            throw new IllegalArgumentException("Invalid transaction");
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public void execute() throws InsufficientBalanceException {
        if (sender == receiver) return;
        sender.retrieveFromBalance(amount);
        receiver.addToBalance(amount);
    }

    // A static synchronized method uses one shared monitor across ALL transactions.
    // Synchronizing an instance method would lock a different object per transfer.
    public static synchronized void executeSynchronized(Transaction transaction)
            throws InsufficientBalanceException {
        transaction.execute();
    }

    public String getId() { return id; }
    public CryptoAccount getSender() { return sender; }
    public CryptoAccount getReceiver() { return receiver; }
    public Double getAmount() { return amount; }
}
