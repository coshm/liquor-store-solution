package com.learning.liquorstore.phasetwo.commandhandler;

import com.learning.liquorstore.phasetwo.data.Inventory;
import com.learning.liquorstore.phasetwo.data.model.Product;

import java.util.Scanner;

public class QuantityCommandHandler implements CommandHandler {

    private final Scanner scanner;
    private final Inventory inventory;

    public QuantityCommandHandler(Scanner scanner, Inventory inventory) {
        this.scanner = scanner;
        this.inventory = inventory;
    }

    /**
     * Handles the "quantity" command. Prompts user for productId
     *   and looks up the quantity of that Product in the Inventory.
     */
    public boolean handleCommand() {
        System.out.println("\nNow checking the quantity of a Product in the Inventory...");

        System.out.println("Enter the productId of the Product you want to lookup:");
        String productId = scanner.nextLine();
        if (BACK_COMMAND.equalsIgnoreCase(productId)) {
            return true;
        }

        int quantityInStock = inventory.getQtyForProduct(productId);
        Product product = inventory.getProductById(productId);
        if (product == null) {
            System.out.println("Could not find \"" + productId + "\" in the Inventory");
        }

        System.out.println("Found " + quantityInStock + " \"" + product.getName() + "\"'s in stock.");
        return true;
    }
}
