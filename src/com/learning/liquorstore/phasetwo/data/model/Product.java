package com.learning.liquorstore.phasetwo.data.model;

public class Product {

    public enum AlcoholType {
        BOURBON,
        GIN,
        MEZCAL,
        RUM,
        SCOTCH,
        TEQUILA,
        VODKA,
        WHISKY;
    }

    public enum BottleSize {
        PINT("375ml"),
        FIFTH("750ml"),
        LITER("1L"),
        HANDLE("1.75L");

        private String volume;

        BottleSize(String volume) {
            this.volume = volume;
        }

        public String getVolume() {
            return volume;
        }
    }

    private String productId;
    private String name;
    private AlcoholType alcoholType;
    private BottleSize bottleSize;
    private double price;

    /**
     * Constructor for Product.
     * @param productId the Id of the Product.
     * @param name the name of the Product.
     * @param type the alcohol type of the Product.
     * @param bottleSize the Product's bottle size.
     * @param price the price of the Product.
     */
    public Product(String productId, String name, AlcoholType type, BottleSize bottleSize, double price) {
        this.productId = productId;
        this.name = name;
        this.alcoholType = type;
        this.bottleSize = bottleSize;
        this.price = price;
    }

    /**
     * Getter for the productId.
     * @return the productId.
     */
    public String getProductId() {
        return productId;
    }

    /**
     * Getter for the name.
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the alcohol type.
     * @return the alcohol type.
     */
    public AlcoholType getAlcoholType() {
        return alcoholType;
    }

    /**
     * Getter for the bottle size.
     * @return the bottle size.
     */
    public BottleSize getBottleSize() {
        return bottleSize;
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
        return productId + " - " + name + " - " + alcoholType + " - " + bottleSize.getVolume() + " - $" + price;
    }

}
