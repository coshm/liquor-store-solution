package com.learning.liquorstore.phasetwo.commandhandler;

import com.learning.liquorstore.phasetwo.data.Inventory;
import com.learning.liquorstore.phasetwo.data.model.Product;

import java.util.Scanner;

public class AddCommandHandler implements CommandHandler {

    private final Scanner scanner;
    private final Inventory inventory;

    public AddCommandHandler(Scanner scanner, Inventory inventory) {
        this.scanner = scanner;
        this.inventory = inventory;
    }

    /**
     * Handles the "add" command. Prompts the user for a productId and
     *   a quantity. Then adds the given quantity of that Product to
     *   the Inventory. Displays whether the operation was successful or not.
     */
    public boolean handleCommand() {
        System.out.println("\nNow adding stock for a Product in the Inventory...");

        System.out.println("Enter the productId of the Product you want to add:");
        String productId = scanner.nextLine();
        if (BACK_COMMAND.equalsIgnoreCase(productId)) {
            return true;
        }

        System.out.println("Enter the number quantity of the Product you want to add:");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        boolean wasSuccessful = inventory.addQtyForProduct(productId, quantity);

        if (wasSuccessful) {
            Product product = inventory.getProductById(productId);
            System.out.println("Successfully added " + quantity + " " + product.getName() + "'s.");
        } else {
            System.out.println("Could not find \"" + productId + "\" in the Inventory");
        }

        return wasSuccessful;
    }

}
