package org.jfl.Day07_Generics.practice;

import lombok.Data;

@Data
public class Propriedade {
    private Long id;
    private String address;
    private double price;

    public Propriedade() {
    }

    public Propriedade(Long id, double price) {
        this.id = id;
        this.price = price;
    }

}
