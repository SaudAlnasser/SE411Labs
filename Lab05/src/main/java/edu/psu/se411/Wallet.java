package edu.psu.se411;

import java.math.BigDecimal;
import java.math.RoundingMode;
import edu.psu.se411.exceptions.InsufficientFundsException;

/** Simulates a wallet and its linked bank account; no external banking service. */
public class Wallet {
    private BigDecimal balance;
    private BigDecimal bankBalance = new BigDecimal("0.00");

    public Wallet(BigDecimal openingBalance) {
        balance = money(openingBalance);
        if (balance.signum() < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative.");
        }
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal getBankBalance() {
        return bankBalance;
    }

    public void withdraw(BigDecimal amount) throws InsufficientFundsException {
        BigDecimal withdrawal = money(amount);
        if (withdrawal.signum() <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        if (withdrawal.compareTo(balance) > 0) {
            throw new InsufficientFundsException("Cannot withdraw " + withdrawal
                    + "; available wallet balance is " + balance + ".");
        }
        balance = balance.subtract(withdrawal);
        bankBalance = bankBalance.add(withdrawal);
    }

    private static BigDecimal money(BigDecimal value) {
        if (value == null) {
            throw new IllegalArgumentException("Amount is required.");
        }
        try {
            return value.setScale(2, RoundingMode.UNNECESSARY);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Amount must have at most two decimal places.", e);
        }
    }
}
