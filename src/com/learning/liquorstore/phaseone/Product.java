package com.learning.liquorstore.phaseone;

public class Product {

    private String name;
    private double price;

    /**
     * Constructor for Product.
     * @param name the name of the Product.
     * @param price the price of the Product.
     */
    public Product(String name, double price) {
        this.name = name;
        this.price = price;
    }

    /**
     * Getter for the name.
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the price.
     * @return the price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * Generates a String that describes the Product.
     * @return a String that describes the Product.
     */
    @Override
    public String toString() {
        return name + " - $" + price;
    }

}
