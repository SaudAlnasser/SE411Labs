package edu.psu.se411.lab09_inventory;

import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class InventoryTest {
    private final Book book = new Book("B1", "Concurrency in Java", "Brian Goetz");
    private final ElectronicDevice device = new ElectronicDevice("E1", "Laptop", "Computers");

    private Inventory<Item> mixed() {
        Inventory<Item> inventory = new Inventory<>();
        inventory.addItem(book);
        inventory.addItem(device);
        return inventory;
    }

    @Test void addsAndRemovesItems() {
        Inventory<Item> inventory = mixed();
        assertEquals(Arrays.asList(book, device), inventory.getAllItems());
        assertTrue(inventory.removeItem(book));
        assertFalse(inventory.removeItem(book));
        assertEquals(Arrays.asList(device), inventory.getAllItems());
    }

    @Test void findsByIdAndName() {
        Inventory<Item> inventory = mixed();
        assertEquals(Arrays.asList(book), inventory.findItems(new SearchById("B1")));
        assertEquals(Arrays.asList(book), inventory.findItems(new SearchByName("Concurrency in Java")));
        assertEquals(Arrays.asList(device), inventory.findItems(new SearchByName("Laptop")));
        assertTrue(inventory.findItems(new SearchById("missing")).isEmpty());
    }

    @Test void typeSpecificStrategiesWorkInMixedInventory() {
        Inventory<Item> inventory = mixed();
        assertEquals(Arrays.asList(book), inventory.findItems(new SearchByAuthor("Brian Goetz")));
        assertEquals(Arrays.asList(device), inventory.findItems(new SearchByCategory("Computers")));
        assertTrue(inventory.findItems(new SearchByAuthor("Unknown")).isEmpty());
        assertTrue(inventory.findItems(new SearchByCategory("Audio")).isEmpty());
    }

    @Test void lambdaReturnsEveryMatchInInsertionOrder() {
        Inventory<Item> inventory = mixed();
        assertEquals(Arrays.asList(book, device), inventory.findItems(item -> item.getId().endsWith("1")));
    }

    @Test void returnedListsCannotModifyInventory() {
        Inventory<Item> inventory = mixed();
        inventory.getAllItems().clear();
        inventory.findItems(item -> true).clear();
        assertEquals(2, inventory.getAllItems().size());
    }

    @Test void supportsTypedAndUnrelatedInventories() {
        Inventory<Book> books = new Inventory<>();
        books.addItem(book);
        assertEquals(Arrays.asList(book), books.findItems(new SearchByAuthor("Brian Goetz")));
        Inventory<String> strings = new Inventory<>();
        strings.addItem("example");
        assertEquals(Arrays.asList("example"), strings.findItems(value -> value.length() > 3));
    }

    @Test void wildcardDisplayPrintsDetailsForAnyType() {
        Inventory<String> strings = new Inventory<>();
        strings.addItem("example");
        PrintStream original = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(output));
            App.displayInventory(mixed());
            App.displayInventory(strings);
        } finally {
            System.setOut(original);
        }
        assertTrue(output.toString().contains(book.toString()));
        assertTrue(output.toString().contains(device.toString()));
        assertTrue(output.toString().contains("example"));
    }

    @Test void emptyInventoryAndNullInputs() {
        Inventory<Item> inventory = new Inventory<>();
        assertTrue(inventory.findItems(item -> true).isEmpty());
        assertFalse(inventory.removeItem(book));
        assertThrows(NullPointerException.class, () -> inventory.addItem(null));
        assertThrows(NullPointerException.class, () -> inventory.findItems(null));
    }
}
