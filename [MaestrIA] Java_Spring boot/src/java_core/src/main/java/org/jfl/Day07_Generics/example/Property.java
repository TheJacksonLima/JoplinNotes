package org.jfl.Day07_Generics.example;

public class Property {

    private final Long id;
    private final double price;

    public Property(Long id, double price) {
        this.id = id;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}