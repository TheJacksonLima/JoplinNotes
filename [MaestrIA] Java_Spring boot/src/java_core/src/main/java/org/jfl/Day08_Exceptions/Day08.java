package org.jfl.Day08_Exceptions;

import org.jfl.Day07_Generics.practice.ApartmentV2;
import org.jfl.Day07_Generics.practice.Repository;

public class Day08 {
    public static void main(String[] args) {
        Repository<ApartmentV2> apartmentsV2 = new Repository<>();

        apartmentsV2.add( new ApartmentV2(1L,"Batel", 3000));
        apartmentsV2.add( new ApartmentV2(2L,"Portao", 3000));


        try {
            var apt = apartmentsV2.findPropriedade(3L);
            System.out.println(apt.toString());
        } catch (PropertyNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
