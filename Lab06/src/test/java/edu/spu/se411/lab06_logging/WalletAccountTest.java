package edu.spu.se411.lab06_logging;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import edu.spu.se411.lab06_logging.model.WalletAccount;
import edu.spu.se411.lab06_logging.exceptions.InsufficientFundsException;

class WalletAccountTest {
    @Test void successfulTransactions() throws Exception {
        WalletAccount account = new WalletAccount(1000);
        account.deposit(100);
        account.withdraw(400);
        assertEquals(700, account.getBalance());
        account.withdraw(700);
        assertEquals(0, account.getBalance());
    }

    @Test void insufficientFundsPreserveBalance() {
        WalletAccount account = new WalletAccount(1000);
        assertThrows(InsufficientFundsException.class, () -> account.withdraw(1500));
        assertEquals(1000, account.getBalance());
    }

    @Test void invalidAmountsPreserveBalance() {
        WalletAccount account = new WalletAccount(1000);
        for (double invalid : new double[] {-1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
            assertThrows(IllegalArgumentException.class, () -> account.deposit(invalid));
            assertThrows(IllegalArgumentException.class, () -> account.withdraw(invalid));
            assertEquals(1000, account.getBalance());
        }
    }

    @Test void invalidOpeningAndReplacementBalances() {
        WalletAccount account = new WalletAccount(10);
        for (double invalid : new double[] {-1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
            assertThrows(IllegalArgumentException.class, () -> new WalletAccount(invalid));
            assertThrows(IllegalArgumentException.class, () -> account.setBalance(invalid));
            assertEquals(10, account.getBalance());
        }
    }

    @Test void overflowPreservesBalance() {
        WalletAccount account = new WalletAccount(Double.MAX_VALUE);
        assertThrows(IllegalArgumentException.class, () -> account.deposit(Double.MAX_VALUE));
        assertEquals(Double.MAX_VALUE, account.getBalance());
    }

    @Test void zeroAmountsAreAllowedByStarterContract() throws Exception {
        WalletAccount account = new WalletAccount(10);
        account.deposit(0);
        account.withdraw(0);
        assertEquals(10, account.getBalance());
    }
}
