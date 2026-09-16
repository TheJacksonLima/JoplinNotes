package org.jfl.Day07_Generics.practice;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ApartmentV2 extends Propriedade {
    private final String block;
    private final Integer number;

    @Override
    public String toString() {
        return "ApartmentV2{" +
                "super=" + super.toString() +
                ", block='" + block + '\'' +
                ", number=" + number +
                '}';
    }

    public ApartmentV2(Long id, String block, double price) {
        super(id, price);
        this.block = block;
        this.number = null;
    }

}
