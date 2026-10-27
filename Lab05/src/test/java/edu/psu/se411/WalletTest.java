package edu.psu.se411;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import edu.psu.se411.exceptions.InsufficientFundsException;
import static org.junit.jupiter.api.Assertions.*;

class WalletTest {
    private final Wallet wallet = new Wallet(new BigDecimal("100.00"));

    @Test
    void transfersWithdrawalToBank() throws InsufficientFundsException {
        wallet.withdraw(new BigDecimal("25.50"));
        assertEquals(new BigDecimal("74.50"), wallet.getBalance());
        assertEquals(new BigDecimal("25.50"), wallet.getBankBalance());
    }

    @Test
    void permitsWithdrawingEntireBalance() throws InsufficientFundsException {
        wallet.withdraw(new BigDecimal("100"));
        assertEquals(new BigDecimal("0.00"), wallet.getBalance());
        assertEquals(new BigDecimal("100.00"), wallet.getBankBalance());
    }

    @Test
    void insufficientFundsLeaveBothBalancesUnchanged() {
        assertThrows(InsufficientFundsException.class,
                () -> wallet.withdraw(new BigDecimal("100.01")));
        assertEquals(new BigDecimal("100.00"), wallet.getBalance());
        assertEquals(new BigDecimal("0.00"), wallet.getBankBalance());
    }

    @Test
    void invalidWithdrawalsLeaveBothBalancesUnchanged() {
        for (String amount : new String[] {"0", "-1", "1.001"}) {
            assertThrows(IllegalArgumentException.class,
                    () -> wallet.withdraw(new BigDecimal(amount)));
        }
        assertThrows(IllegalArgumentException.class, () -> wallet.withdraw(null));
        assertEquals(new BigDecimal("100.00"), wallet.getBalance());
        assertEquals(new BigDecimal("0.00"), wallet.getBankBalance());
    }

    @Test
    void rejectsInvalidOpeningBalances() {
        assertThrows(IllegalArgumentException.class, () -> new Wallet(null));
        assertThrows(IllegalArgumentException.class, () -> new Wallet(new BigDecimal("-1")));
        assertThrows(IllegalArgumentException.class, () -> new Wallet(new BigDecimal("0.001")));
    }

    @Test
    void preservesDecimalPrecisionAcrossRepeatedWithdrawals() throws InsufficientFundsException {
        Wallet smallWallet = new Wallet(new BigDecimal("0.30"));
        for (int i = 0; i < 3; i++) {
            smallWallet.withdraw(new BigDecimal("0.10"));
        }
        assertEquals(new BigDecimal("0.00"), smallWallet.getBalance());
        assertEquals(new BigDecimal("0.30"), smallWallet.getBankBalance());
    }
}
