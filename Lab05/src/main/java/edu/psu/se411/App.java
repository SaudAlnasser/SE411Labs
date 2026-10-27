package edu.psu.se411;

import java.math.BigDecimal;
import java.util.Scanner;
import edu.psu.se411.exceptions.InvalidAgeException;
import edu.psu.se411.exceptions.InsufficientFundsException;

public class App {
    public static void validateAge(int age) throws InvalidAgeException {
        if (age < 18) {
            throw new InvalidAgeException("Age must be at least 18; received " + age + ".");
        }
        System.out.println("Age valid message.");
    }

    public static void main(String[] args) {
        if (args.length == 1 && "--demo".equals(args[0])) {
            runDemo();
            return;
        }
        try (Scanner input = new Scanner(System.in)) {
            System.out.print("Enter your age: ");
            if (!input.hasNextLine()) {
                return;
            }
            try {
                validateAge(Integer.parseInt(input.nextLine().trim()));
            } catch (InvalidAgeException e) {
                System.out.println("Age validation failed: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Age must be a whole number.");
            }

            Wallet wallet = new Wallet(new BigDecimal("100.00"));
            showBalances(wallet);
            while (true) {
                System.out.print("Amount to withdraw to your bank (or q to quit): ");
                if (!input.hasNextLine()) {
                    break;
                }
                String line = input.nextLine().trim();
                if ("q".equalsIgnoreCase(line)) {
                    break;
                }
                try {
                    wallet.withdraw(new BigDecimal(line));
                    System.out.println("Withdrawal transferred to your linked bank account.");
                } catch (InsufficientFundsException e) {
                    System.out.println("Withdrawal failed: " + e.getMessage());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid amount: " + e.getMessage());
                }
                showBalances(wallet);
            }
        }
    }

    private static void runDemo() {
        for (int age : new int[] {17, 18}) {
            System.out.println("Validating age " + age);
            try {
                validateAge(age);
            } catch (InvalidAgeException e) {
                System.out.println("Age validation failed: " + e.getMessage());
            }
        }
        Wallet wallet = new Wallet(new BigDecimal("100.00"));
        for (String amount : new String[] {"40.00", "80.00"}) {
            System.out.println("Withdrawing " + amount);
            try {
                wallet.withdraw(new BigDecimal(amount));
                System.out.println("Withdrawal transferred to your linked bank account.");
            } catch (InsufficientFundsException e) {
                System.out.println("Withdrawal failed: " + e.getMessage());
            }
            showBalances(wallet);
        }
    }

    private static void showBalances(Wallet wallet) {
        System.out.println("Wallet: " + wallet.getBalance() + "; bank: " + wallet.getBankBalance());
    }
}
