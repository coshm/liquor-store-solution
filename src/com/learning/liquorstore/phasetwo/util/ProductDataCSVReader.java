package com.learning.liquorstore.phasetwo.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;

import com.learning.liquorstore.phasetwo.data.Inventory;
import com.learning.liquorstore.phasetwo.data.model.Product;
import com.learning.liquorstore.phasetwo.data.model.Product.AlcoholType;
import com.learning.liquorstore.phasetwo.data.model.Product.BottleSize;

public class ProductDataCSVReader {

    private static final String PRODUCT_DATA_CSV = "src/com/learning/liquorstore/phasetwo/data/product_data.csv";
    private static final int PRODUCT_VALUE_COUNT = 5;

    private static final String INVENTORY_DATA_CSV = "src/com/learning/liquorstore/phasetwo/data/inventory_data.csv";
    private static final int INVENTORY_VALUE_COUNT = 2;

    private static final String DELIMITER = ",";

    /**
     * Reads in the Product data from a CSV file, parses the data, creates
     *   Product objects, and adds them to the ProductCatalog.
     * @param inventory the Inventory with the ProductCatalog to add Products to.
     * @throws IOException - if file does not exist or cannot be read.
     * @throws ParseException - if any line does not have the correct number of Product values.
     */
    public static void initializeProducts(Inventory inventory) throws IOException, ParseException {
        BufferedReader csvReader = null;
        String line;
        int lineCount = 2;

        try {
            csvReader = new BufferedReader(new FileReader(PRODUCT_DATA_CSV));

            // Data begins on second row so skip the
            //   first row of header column headers.
            csvReader.readLine();
            line = csvReader.readLine();

            while (line != null) {
                Logger.debug("Line #%s='%s'", String.valueOf(lineCount), line);

                // Product values: ProductId,Name,AlcoholType,BottleSize,Price
                String[] productValues = line.split(DELIMITER);
                if (productValues.length != PRODUCT_VALUE_COUNT) {
                    String errMsg = "Expected Product data to have " + PRODUCT_VALUE_COUNT
                            + " value but found " + productValues.length;
                    throw new ParseException(errMsg, lineCount);
                }

                // Parse the Product values
                String productId = productValues[0];
                String name = productValues[1];
                AlcoholType alcoholType = AlcoholType.valueOf(productValues[2]);
                BottleSize bottleSize = BottleSize.valueOf(productValues[3]);
                double price = Double.parseDouble(productValues[4]);

                // Create the new Product and add it to the Inventory
                Product product = new Product(productId, name, alcoholType, bottleSize, price);
                Logger.debug("Product='%s'", product.toString());

                inventory.addNewProduct(product);

                // Read in the next line
                line = csvReader.readLine();
                lineCount++;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse Price as double for line " + lineCount);
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }
    }

    /**
     * Reads in Inventory data from a CSV file, parses the data, and adds
     *    the quantities for each productId to the Inventory.
     * @param inventory the Inventory to add Products to.
     * @throws IOException - if file does not exist or cannot be read.
     * @throws ParseException - if any line does not have the correct number of Inventory values.
     */
    public static void initializeInventory(Inventory inventory) throws IOException, ParseException {
        BufferedReader csvReader = null;
        String line;
        int lineCount = 2;

        try {
            csvReader = new BufferedReader(new FileReader(INVENTORY_DATA_CSV));

            // Data begins on second row so skip the
            //   first row of header column headers.
            csvReader.readLine();
            line = csvReader.readLine();

            while (line != null) {
                Logger.debug("Line #%s='%s'", String.valueOf(lineCount), line);

                // Product values: ProductId,Name,AlcoholType,BottleSize,Price
                String[] productValues = line.split(DELIMITER);
                if (productValues.length != INVENTORY_VALUE_COUNT) {
                    String errMsg = "Expected Inventory data to have " + INVENTORY_VALUE_COUNT
                            + " value but found " + productValues.length;
                    throw new ParseException(errMsg, lineCount);
                }

                // Parse the Inventory values and add quantity
                //   of the productId to the Inventory
                String productId = productValues[0];
                int quantity = Integer.valueOf(productValues[1]);
                boolean successfulAdd = inventory.addProducts(productId, quantity);
                if (!successfulAdd) {
                    throw new IllegalStateException(String.format("Failed to add %s units of '%s' to the Inventory.",
                            String.valueOf(quantity), productId));
                }

                // Log updated Product quantity
                int currentQuantity = inventory.getQtyForProduct(productId);
                Logger.debug("Quantity of '%s' in Inventory now at %s", productId, String.valueOf(currentQuantity));

                // Read in the next line
                line = csvReader.readLine();
                lineCount++;
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Failed to parse quantity as int for line " + lineCount);
        } finally {
            if (csvReader != null) {
                csvReader.close();
            }
        }
    }

}
