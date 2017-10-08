package com.learning.liquorstore.phasetwo.commandhandler;

import com.learning.liquorstore.phasetwo.data.Inventory;
import com.learning.liquorstore.phasetwo.data.model.Product;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class ViewCommandHandler implements CommandHandler {

    // Define the recognized VIEW commands
    private static final String VIEW_ALL = "all";
    private static final String VIEW_BY_ALCHOL_TYPE = "type";
    private static final String VIEW_BY_BOTTLE_SIZE = "size";
    private static final String VIEW_IN_PRICE_RANGE = "price";

    private static final int VIEW_PAGE_SIZE = 10;

    private final Scanner scanner;
    private final Inventory inventory;

    public ViewCommandHandler(Scanner scanner, Inventory inventory) {
        this.scanner = scanner;
        this.inventory = inventory;
    }

    /**
     * Handles the "view" command. Prompts the user for a viewing command.
     *   Based on the viewing command, the user may be prompted for additional
     *   input. Then the relevant Products are looked up in the Inventory and
     *   displayed to the screen.
     */
    public boolean handleCommand() {
        System.out.println("\nNow viewing Products in the Inventory...");
        System.out.println("\tall - View all Products.");
        System.out.println("\ttype - View all Products of the given AlcoholType.");
        System.out.println("\tsize - View all Products of the given BottleSize.");
        System.out.println("\tprice - View all Products with prices within the given price range.");
        System.out.println("\tback - Return to main menu.");
        System.out.println("Please enter a command from the list above:");
        String menuSelection = scanner.nextLine();

        Set<Product> products = new HashSet<>();
        if (menuSelection.equalsIgnoreCase(VIEW_ALL)) {
            // Display all the Products
            products = inventory.getAllProducts();
        } else if (menuSelection.equalsIgnoreCase(VIEW_BY_ALCHOL_TYPE)) {
            products = getProductsForAlcoholType();
        } else if (menuSelection.equalsIgnoreCase(VIEW_BY_BOTTLE_SIZE)) {
            products = getProductsForBottleSize();
        } else if (menuSelection.equalsIgnoreCase(VIEW_IN_PRICE_RANGE)) {
            products = getProductsInPriceRange();
        } else if (menuSelection.equalsIgnoreCase(BACK_COMMAND)) {
            return true;
        } else {
            System.out.println("Unrecognized viewing command, '" + menuSelection + "'.");
        }

        displayProducts(products);

        return true;
    }

    /**
     * Prompts user for AlcoholType and returns relevant Products.
     * @return the Set of Products.
     */
    private Set<Product> getProductsForAlcoholType() {
        // Display all the Products with the given AlcoholType
        System.out.println("\nAlcoholTypes: [BOURBON, GIN, MEZCAL, RUM, SCOTCH, TEQUILA, VODKA, WHISKY]");
        System.out.println("Please enter which AlcoholType to view:");
        String userInput = scanner.nextLine();

        try {
            // Attempt to parse String input as AlcoholType enum
            Product.AlcoholType alcoholType = Product.AlcoholType.valueOf(userInput);
            return inventory.getProductsByAlcoholType(alcoholType);
        } catch (Exception e) {
            System.out.println("Unrecognized AlcoholType, '" + userInput + "'");
            return Collections.EMPTY_SET;
        }
    }

    /**
     * Prompts user for BottleSize and returns relevant Products.
     * @return the Set of Products.
     */
    private Set<Product> getProductsForBottleSize() {
        // Display all the Products with the given BottleSize
        System.out.println("\nBottleSizes: [PINT, FIFTH, LITER, HANDLE]");
        System.out.println("Please enter which BottleSize to view:");
        String userInput = scanner.nextLine();

        try {
            // Attempt to parse String input as BottleSize enum
            Product.BottleSize bottleSize = Product.BottleSize.valueOf(userInput);
            return inventory.getProductsByBottleSize(bottleSize);
        } catch (Exception e) {
            System.out.println("Unrecognized BottleSize, '" + userInput + "'");
            return Collections.EMPTY_SET;
        }
    }

    /**
     * Prompts user for price range and returns relevant Products.
     * @return the Set of Products.
     */
    private Set<Product> getProductsInPriceRange() {
        // Display all the Products with prices within the given price range.
        System.out.println("\nPlease enter the minimum price to view:");
        double min = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Please enter the maximum price to view:");
        double max = scanner.nextDouble();
        scanner.nextLine();

        if (min > max) {
            System.out.println("The minimum must be less than the maximum.");
        }

        return inventory.getProductsInPriceRange(min, max);
    }

    /**
     * Displays the given Products in pages of size VIEW_PAGE_SIZE.
     * @param products the products to display.
     */
    private void displayProducts(Set<Product> products) {
        if (products == null || products.size() == 0) {
            System.out.println("\nNo Products found.");
            return;
        }

        System.out.println("\nDisplaying selected Products...");

        int productCount = 0;
        int pageCount = 0;
        Iterator<Product> iter = products.iterator();

        // Handles looping through pages of Products
        while (productCount < products.size()) {
            // Handles printing Product details to the screen until we
            //   run out of Products or we reach the page size.
            int maxProductCount = (pageCount + 1) * VIEW_PAGE_SIZE;
            while (iter.hasNext() && (productCount < maxProductCount)) {
                Product product = iter.next();
                System.out.println("\t" + product.toString());
                productCount++;
            }
            pageCount++;
            System.out.println("Press ENTER to continue:");
            scanner.nextLine();
        }
    }

}
