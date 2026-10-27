package edu.spu.se411.lab06_logging.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import edu.spu.se411.lab06_logging.exceptions.InsufficientFundsException;

public class WalletAccount {
    private static final Logger logger = LoggerFactory.getLogger(WalletAccount.class);
    private double balance;

    public WalletAccount(double balance) {
        setBalance(balance);
        logger.debug("Wallet account created");
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        validateAmount(amount);
        if (amount > balance) {
            throw new InsufficientFundsException("Insufficient funds for withdrawal");
        }
        balance -= amount;
        logger.debug("Withdrawal successful");
    }

    public void deposit(double amount) {
        validateAmount(amount);
        double updated = balance + amount;
        if (!Double.isFinite(updated)) {
            throw new IllegalArgumentException("Deposit would overflow balance");
        }
        balance = updated;
        logger.debug("Deposit successful");
    }

    public void setBalance(double balance) {
        if (!Double.isFinite(balance) || balance < 0) {
            throw new IllegalArgumentException("Balance must be finite and non-negative");
        }
        this.balance = balance;
    }

    public double getBalance() {
        return balance;
    }

    private static void validateAmount(double amount) {
        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Amount must be finite and non-negative");
        }
    }
}
