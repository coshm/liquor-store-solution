package com.learning.liquorstore.phasetwo.util;

import com.learning.liquorstore.phasetwo.data.model.Product;

import java.util.Comparator;

public class ProductPriceComparator implements Comparator<Product> {

    public int compare(Product p1, Product p2) {
        return (int) (p1.getPrice() - p2.getPrice());
    }

}
