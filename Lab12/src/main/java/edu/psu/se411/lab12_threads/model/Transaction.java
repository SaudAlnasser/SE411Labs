package edu.psu.se411.lab12_threads.model;

import edu.psu.se411.lab12_threads.exceptions.InsufficientBalanceException;

public final class Transaction implements Runnable {
    private final String id;
    private final CryptoAccount sender;
    private final CryptoAccount receiver;
    private final Double amount;

    public Transaction(String id, CryptoAccount sender, CryptoAccount receiver, Double amount) {
        if (id == null || sender == null || receiver == null || sender == receiver)
            throw new IllegalArgumentException("Distinct accounts and a transaction ID required");
        CryptoAccount.validateAmount(amount);
        this.id = id;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public void execute() throws InsufficientBalanceException {
        CryptoAccount first = sender.compareOrder(receiver) < 0 ? sender : receiver;
        CryptoAccount second = first == sender ? receiver : sender;
        first.lock.lock();
        try {
            second.lock.lock();
            try {
                // Validate both sides before changing either balance.
                if (sender.getBalance() < amount) throw new InsufficientBalanceException(sender.getId());
                if (!Double.isFinite(receiver.getBalance() + amount))
                    throw new IllegalArgumentException("Balance overflow");
                sender.retrieveFromBalance(amount);
                receiver.addToBalance(amount);
            } finally { second.lock.unlock(); }
        } finally { first.lock.unlock(); }
    }

    @Override public void run() {
        try { execute(); }
        catch (InsufficientBalanceException e) { /* Rejected transfer changes neither account. */ }
    }

    public String getId() { return id; }
    public CryptoAccount getSender() { return sender; }
    public CryptoAccount getReceiver() { return receiver; }
    public Double getAmount() { return amount; }
}
