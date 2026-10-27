package edu.psu.se411.lab09_inventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        logger.info("Application is starting...");
        try {
            Inventory<Item> inventory = new Inventory<>();
            Book book = new Book("B1", "Concurrency in Java", "Brian Goetz");
            inventory.addItem(book);
            inventory.addItem(new Book("B2", "Effective Java", "Joshua Bloch"));
            inventory.addItem(new ElectronicDevice("E1", "Laptop", "Computers"));
            inventory.addItem(new ElectronicDevice("E2", "Headphones", "Audio"));

            System.out.println("All inventory items:");
            displayInventory(inventory);
            System.out.println("Book by name: " + inventory.findItems(new SearchByName("Concurrency in Java")));
            System.out.println("Device by name: " + inventory.findItems(new SearchByName("Laptop")));
            System.out.println("By ID: " + inventory.findItems(new SearchById("B2")));
            System.out.println("By author: " + inventory.findItems(new SearchByAuthor("Brian Goetz")));
            System.out.println("By category: " + inventory.findItems(new SearchByCategory("Audio")));
            System.out.println("Lambda search: " + inventory.findItems(item -> item.getName().startsWith("Effective")));

            inventory.removeItem(book);
            System.out.println("After removing B1:");
            displayInventory(inventory);
        } catch (RuntimeException exception) {
            logger.error("Inventory operation failed", exception);
            throw exception;
        } finally {
            logger.info("Application is closing...");
        }
    }

    public static void displayInventory(Inventory<?> inventory) {
        for (Object item : inventory.getAllItems()) {
            System.out.println(item);
        }
    }
}
