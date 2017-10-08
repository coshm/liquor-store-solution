package com.learning.liquorstore.phasetwo;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import com.learning.liquorstore.phasetwo.commandhandler.AddCommandHandler;
import com.learning.liquorstore.phasetwo.commandhandler.QuantityCommandHandler;
import com.learning.liquorstore.phasetwo.commandhandler.RemoveCommandHandler;
import com.learning.liquorstore.phasetwo.commandhandler.ViewCommandHandler;
import com.learning.liquorstore.phasetwo.data.Inventory;
import com.learning.liquorstore.phasetwo.util.Logger;
import com.learning.liquorstore.phasetwo.util.ProductDataCSVReader;

public class LiquorStore {

    // Define the recognized commands
    private static final String HELP_COMMAND = "help";
    private static final String VIEW_COMMAND = "view";
    private static final String ADD_COMMAND = "add";
    private static final String REMOVE_COMMAND = "remove";
    private static final String QUANTITY_COMMAND = "quantity";
    private static final String EXIT_COMMAND = "exit";

    private static Scanner scanner;
    private static Inventory inventory;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        inventory = new Inventory();
        Logger.isDebugging(true);

        // Initialize the LiquorStore/Inventory
        initializeLiquorStore(inventory);

        // Initialize Command Handlers
        ViewCommandHandler viewHandler = new ViewCommandHandler(scanner, inventory);
        AddCommandHandler addHandler = new AddCommandHandler(scanner, inventory);
        RemoveCommandHandler removeHandler = new RemoveCommandHandler(scanner, inventory);
        QuantityCommandHandler quantityHandler = new QuantityCommandHandler(scanner, inventory);

        // Display greeting and list of commands
        System.out.println("Welcome to Kyle's Liquor Store!");
        displayHelp();

        String menuSelection;
        while (true) {
            menuSelection = getUsersMenuSelection();
            if (menuSelection.equalsIgnoreCase(HELP_COMMAND)) {
                displayHelp();
            } else if (menuSelection.equalsIgnoreCase(VIEW_COMMAND)) {
                viewHandler.handleCommand();
            } else if (menuSelection.equalsIgnoreCase(ADD_COMMAND)) {
                addHandler.handleCommand();
            } else if (menuSelection.equalsIgnoreCase(REMOVE_COMMAND)) {
                removeHandler.handleCommand();
            } else if (menuSelection.equalsIgnoreCase(QUANTITY_COMMAND)) {
                quantityHandler.handleCommand();
            } else if (menuSelection.equalsIgnoreCase(EXIT_COMMAND)) {
                break;
            } else {
                System.out.println("\nYou entered an invalid command, \"" + menuSelection + "\". Try again." );
            }
        }
    }

    /**
     * Initialize the LiquorStore with seed data.
     * @param inventory the inventory
     */
    private static void initializeLiquorStore(Inventory inventory) {
        try {
            ProductDataCSVReader.initializeProducts(inventory);
            ProductDataCSVReader.initializeInventory(inventory);
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Displays all of the main menu commands and their descriptions.
     */
    private static void displayHelp() {
        System.out.println("\n\thelp - Display this list of commands.");
        System.out.println("\tview - View the Products in the Inventory.");
        System.out.println("\tadd - Add some quantity of a Product to the Inventory.");
        System.out.println("\tremove - Remove some quantity of a Product from the Inventory.");
        System.out.println("\tquantity - Lookup the quantity of a Product in the Inventory.");
        System.out.println("\texit - End the program.");
    }

    /**
     * Prompts the user for a command from the main menu.
     * @return the user input.
     */
    private static String getUsersMenuSelection() {
        System.out.println("\nPlease enter a command [help, create, add, remove, quantity, exit]:");
        return scanner.nextLine();
    }

}
