package com.learning.liquorstore.phaseone;

import java.util.Scanner;

public class LiquorStore {

    private static Scanner scanner;
    private static Inventory inventory;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        inventory = new Inventory();

        // Main Menu Options
        String help = "help";
        String create = "create";
        String add = "add";
        String remove = "remove";
        String get = "get";
        String exit = "exit";

        // Display greeting and list of commands
        System.out.println("Welcome to Kyle's Liquor Store!");
        displayHelp();

        String menuSelection;
        while (true) {
            menuSelection = getUsersMenuSelection();
            if (menuSelection.equalsIgnoreCase(help)) {
                displayHelp();
            } else if (menuSelection.equalsIgnoreCase(create)) {
                handleCreate();
            } else if (menuSelection.equalsIgnoreCase(add)) {
                handleAdd();
            } else if (menuSelection.equalsIgnoreCase(remove)) {
                handleRemove();
            } else if (menuSelection.equalsIgnoreCase(get)) {
                handleGet();
            } else if (menuSelection.equalsIgnoreCase(exit)) {
                break;
            } else {
                System.out.println("\nYou entered an invalid command, \"" + menuSelection + "\". Try again." );
            }
        }
    }

    private static void displayHelp() {
        System.out.println("\n\thelp - Display this list of commands.");
        System.out.println("\tcreate - Create a new Product.");
        System.out.println("\tadd - Add some quantity of a Product to the Inventory.");
        System.out.println("\tremove - Remove some quantity of a Product from the Inventory.");
        System.out.println("\tget - Lookup the quantity of a Product in the Inventory.");
        System.out.println("\texit - End the program.");
    }

    private static String getUsersMenuSelection() {
        System.out.println("\nPlease enter a command [help, create, add, remove, get, exit]:");
        return scanner.nextLine();
    }

    private static void handleCreate() {
        System.out.println("\nNow creating a new Product...");

        System.out.println("Enter a name for the product:");
        String name = scanner.nextLine();

        System.out.println("Enter a price for the product (i.e. 19.99):");
        double price = scanner.nextDouble();
        scanner.nextLine();

        Product product = new Product(name, price);
        inventory.addNewProduct(product);

        System.out.println("Successfully created the product, \"" + product.toString() + "\"");
    }

    private static void handleAdd() {
        System.out.println("\nNow adding stock for a Product in the Inventory...");

        System.out.println("Enter the name of Product you want to add:");
        String name = scanner.nextLine();

        System.out.println("Enter the number quantity of the Product you want to add::");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        boolean wasSuccessful = inventory.addProducts(name, quantity);

        if (wasSuccessful) {
            System.out.println("Successfully added " + quantity + " " + name + "'s.");
        } else {
            System.out.println("Could not find \"" + name + "\" in the Inventory");
        }
    }

    private static void handleRemove() {
        System.out.println("\nNow removing stock for a Product from the Inventory...");

        System.out.println("Enter the name of Product you want to remove:");
        String name = scanner.nextLine();

        System.out.println("Enter the number quantity of the Product you want to remove::");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        boolean wasSuccessful = inventory.removeProducts(name, quantity);

        if (wasSuccessful) {
            System.out.println("Successfully remove " + quantity + " " + name + "'s.");
        } else {
            System.out.println("Could not find \"" + name + "\" in the Inventory");
        }
    }

    private static void handleGet() {
        System.out.println("\nNow checking the quantity of a Product in the Inventory...");

        System.out.println("Enter the name of Product you want to lookup:");
        String name = scanner.nextLine();

        int quantityInStock = inventory.getQtyForProduct(name);
        System.out.println("Found " + quantityInStock + " \"" + name + "\"'s in stock.");
    }

}
