package edu.psu.se411.lab11_threads;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import edu.psu.se411.lab11_threads.model.*;
import edu.psu.se411.lab11_threads.exceptions.InsufficientBalanceException;

class AppTest {
    @Test void initialTotal() {
        assertEquals(10000.0, App.totalBalance(App.createAccounts()), 0.001);
    }

    @Test void transferAndRejectionAreAtomic() throws Exception {
        CryptoAccount a = new CryptoAccount("a", 10.0);
        CryptoAccount b = new CryptoAccount("b", 0.0);
        new Transaction(a, b, 4.0).execute();
        assertEquals(6.0, a.getBalance());
        assertEquals(4.0, b.getBalance());
        assertThrows(InsufficientBalanceException.class,
                () -> Transaction.executeSynchronized(new Transaction(a, b, 7.0)));
        assertEquals(6.0, a.getBalance());
        assertEquals(4.0, b.getBalance());
    }

    @Test void selfTransferAndInvalidAmounts() throws Exception {
        CryptoAccount a = new CryptoAccount("a", 10.0);
        new Transaction(a, a, 3.0).execute();
        assertEquals(10.0, a.getBalance());
        for (Double amount : new Double[]{null, -1.0, Double.NaN, Double.POSITIVE_INFINITY})
            assertThrows(IllegalArgumentException.class, () -> new Transaction(a, a, amount));
    }

    @Test void concurrentTransfersPreserveMoney() throws Exception {
        assertEquals(10000.0, App.runSimulation(true), 0.000001);
    }
}
