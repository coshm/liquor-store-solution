package com.learning.liquorstore.phasetwo.commandhandler;

import com.learning.liquorstore.phasetwo.data.Inventory;
import com.learning.liquorstore.phasetwo.data.model.Product;

import java.util.Scanner;

public class RemoveCommandHandler implements CommandHandler {

    private final Scanner scanner;
    private final Inventory inventory;

    public RemoveCommandHandler(Scanner scanner, Inventory inventory) {
        this.scanner = scanner;
        this.inventory = inventory;
    }

    /**
     * Handles the "remove" command. Prompts the user for a productId and
     *   a quantity. Then removes the given quantity of that Product from
     *   the Inventory. Displays whether the operation was successful or not.
     */
    public boolean handleCommand() {
        System.out.println("\nNow removing stock for a Product from the Inventory...");

        System.out.println("Enter the productId of the Product you want to remove:");
        String productId = scanner.nextLine();
        if (BACK_COMMAND.equalsIgnoreCase(productId)) {
            return true;
        }

        System.out.println("Enter the number quantity of the Product you want to remove:");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        boolean wasSuccessful = inventory.removeProducts(productId, quantity);

        if (wasSuccessful) {
            Product product = inventory.getProductById(productId);
            System.out.println("Successfully removed " + quantity + " " + product.getName() + "'s.");
        } else {
            System.out.println("Could not find \"" + productId + "\" in the Inventory");
        }

        return wasSuccessful;
    }
}
