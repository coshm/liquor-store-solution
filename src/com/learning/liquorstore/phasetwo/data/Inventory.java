package com.learning.liquorstore.phasetwo.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.learning.liquorstore.phasetwo.data.model.Product;
import com.learning.liquorstore.phasetwo.data.model.Product.AlcoholType;
import com.learning.liquorstore.phasetwo.data.model.Product.BottleSize;
import com.learning.liquorstore.phasetwo.util.Logger;
import com.learning.liquorstore.phasetwo.util.ProductPriceComparator;

public class Inventory {

    // A collection of every available Product.
    private HashMap<String, Product> productCatalog;

    // The quantity of each Product in the Inventory.
    private Map<Product, Integer> productQuantities;

    // Products by Alcohol Type
    private Map<AlcoholType, Set<Product>> productsByAlcoholType;

    // Products by Bottle Size
    private Map<BottleSize, Set<Product>> productsByBottleSize;

    // List of Products sorted by Price
    private SortedSet<Product> productsByPrice;

    /**
     * Constructor for Inventory.
     */
    public Inventory() {
        productCatalog = new HashMap<>();
        productQuantities = new HashMap<>();

        productsByAlcoholType = new HashMap<>(AlcoholType.values().length);
        for (AlcoholType alcoholType : AlcoholType.values()) {
            productsByAlcoholType.put(alcoholType, new HashSet<>());
        }

        productsByBottleSize = new HashMap<>(BottleSize.values().length);
        for (BottleSize bottleSize : BottleSize.values()) {
            productsByBottleSize.put(bottleSize, new HashSet<>());
        }

        // Pass our Comparator into the constructor. This will tell the
        //   Set how to sort our Products when we add them to the Set.
        productsByPrice = new TreeSet<>(new ProductPriceComparator());
    }

    /**
     * Add a newly created Product to the productCatalog and any other
     *   Product collections so that it can be used in the Inventory.
     * @param product the new Product to add
     */
    public void addNewProduct(Product product) {
        // Don't add the same Product twice
        if (productCatalog.containsKey(product)) {
            Logger.debug("Skipping duplicate productId='%s'.", product.getProductId());
            return;
        }

        // Add Product to the productCatalog, the master record of all Products.
        productCatalog.put(product.getProductId(), product);

        // Add Product to the set of Products with the same AlcoholType
        Set<Product> productsOfSameAlcoholType = productsByAlcoholType.get(product.getAlcoholType());
        productsOfSameAlcoholType.add(product);

        // Add Product to the set of Products with the same BottleSize
        Set<Product> productsOfSameBottleSize = productsByBottleSize.get(product.getBottleSize());
        productsOfSameBottleSize.add(product);

        // Add Product to the set of Products ordered by price.
        productsByPrice.add(product);
    }

    /**
     * Add to the quantity of the Product with the given
     *   productId in the Inventory.
     * @param productId the Id of the Product to add.
     * @param quantity the number of units to add to the Inventory.
     * @return the success of the add operation.
     */
    public boolean addProducts(String productId, int quantity) {
        if (!productCatalog.containsKey(productId)) {
            Logger.debug("ProductId not found in ProductCatalog. ProductId='%s'.", productId);
            return false;
        }

        Product product = productCatalog.get(productId);
        int existingQty = productQuantities.getOrDefault(product, 0);
        int updatedQty = existingQty + quantity;
        productQuantities.put(product, updatedQty);
        return true;
    }

    /**
     * Remove from the quantity of the Product with the given
     *   productId in the Inventory.
     * @param productId the Id of the Product to remove.
     * @param quantity the number of units to remove from the Inventory.
     * @return the success of the remove operation.
     */
    public boolean removeProducts(String productId, int quantity) {
        if (!productCatalog.containsKey(productId)) {
            Logger.debug("ProductId not found in ProductCatalog. ProductId='%s'.", productId);
            return false;
        }

        Product product = productCatalog.get(productId);
        Integer existingQty = productQuantities.get(product);
        if (existingQty == null || existingQty < quantity) {
            Logger.debug("Not enough quantity for Remove. ProductId='%s'.", productId);
            return false;
        }

        int updatedQty = existingQty - quantity;
        productQuantities.put(product, updatedQty);
        return true;
    }

    /**
     * Lookup the quantity of a given Product in the Inventory.
     * @param productId the Id of the Product to lookup.
     * @return the Product's quantity.
     */
    public int getQtyForProduct(String productId) {
        Product product = productCatalog.get(productId);
        return productQuantities.getOrDefault(product, 0);
    }

    /**
     * Return a Set of all the Products.
     * @return a Set of all the Products.
     */
    public Set<Product> getAllProducts() {
        return new HashSet<>(productCatalog.values());
    }

    /**
     * Returns all Products of the given AlcoholType.
     * @param alcoholType the AlcoholType.
     * @return the Set of Products.
     */
    public Set<Product> getProductsByAlcoholType(AlcoholType alcoholType) {
        return productsByAlcoholType.get(alcoholType);
    }

    /**
     * Returns all Products of the given BottleSize.
     * @param bottleSize the BottleSize.
     * @return the Set of Products.
     */
    public Set<Product> getProductsByBottleSize(BottleSize bottleSize) {
        return productsByBottleSize.get(bottleSize);
    }

    /**
     * Returns all Products that have a price between min and max inclusive.
     * @param min the minimum price.
     * @param max the maximum price.
     * @return the Set of Products in range.
     */
    public Set<Product> getProductsInPriceRange(double min, double max) {
        Set<Product> productsInPriceRange = new HashSet<>();
        Iterator<Product> iter = productsByPrice.iterator();
        while (iter.hasNext()) {
            Product product = iter.next();
            if (product.getPrice() >= min) {
                if (product.getPrice() <= max) {
                    productsInPriceRange.add(product);
                } else {
                    break;
                }
            }
        }

        return productsInPriceRange;
    }

    /**
     * Returns the Product corresponding to the given productId.
     * @param productId the Id of the Product to return.
     * @return the Product.
     */
    public Product getProductById(String productId) {
        return productCatalog.get(productId);
    }

}
