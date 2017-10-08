package com.learning.liquorstore.phaseone;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    // A collection of every available Product.
    private Map<String, Product> productCatalog;

    // The quantity of each Product in the Inventory.
    private Map<String, Integer> productQuantities;

    /**
     * Constructor for Inventory.
     */
    public Inventory() {
        productCatalog = new HashMap<>();
        productQuantities = new HashMap<>();
    }

    /**
     * Add a newly created Product to the productCatalog so
     *   that it can be used in the Inventory.
     * @param product the new Product to add
     */
    public void addNewProduct(Product product) {
        if (productCatalog.get(product.getName()) == null) {
            productCatalog.put(product.getName(), product);
        }
    }

    /**
     * Add to the quantity of the Product with the given
     *   productName in the Inventory.
     * @param productName the name of the Product to add.
     * @param quantity the number of units to add to the Inventory.
     * @return the success of the add operation.
     */
    public boolean addProducts(String productName, int quantity) {
        if (!productCatalog.containsKey(productName)) {
            return false;
        }

        int existingQty = productQuantities.getOrDefault(productName, 0);
        int updatedQty = existingQty + quantity;
        productQuantities.put(productName, updatedQty);
        return true;
    }

    /**
     * Remove from the quantity of the Product with the given
     *   productName in the Inventory.
     * @param productName the name of the Product to remove.
     * @param quantity the number of units to remove from the Inventory.
     * @return the success of the remove operation.
     */
    public boolean removeProducts(String productName, int quantity) {
        Integer existingQty = productQuantities.get(productName);
        if (existingQty == null || existingQty < quantity) {
            return false;
        }

        int updatedQty = existingQty - quantity;
        productQuantities.put(productName, updatedQty);
        return true;
    }

    /**
     * Lookup the quantity of a given Product in the Inventory.
     * @param productName the name of the Product to lookup.
     * @return the Product's quantity.
     */
    public int getQtyForProduct(String productName) {
        return productQuantities.getOrDefault(productName, 0);
    }

}
