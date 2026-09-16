package org.jfl.Day05_EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

public class Day05 {
    public static void main(String[] args) {
        Customer c1 = new Customer(1L,"john@test.com");
        Customer c2 = new Customer(1L,"john@test.com");

        Set<Customer> customers = new HashSet<>();
        customers.add(c1);
        customers.add(c2);
        System.out.println(customers.size());

        Set<Pair> uniquePairs = new HashSet<>();
        Pair p1 = new Pair("Dragon","Drago");
        Pair p2 = new Pair("Dragon","Drago");
        Pair p3 = new Pair("Dragon","Dragones");

        uniquePairs.add(p1);
        System.out.println(uniquePairs.size());
        uniquePairs.add(p2);
        System.out.println(uniquePairs.size());
        uniquePairs.add(p3);
        System.out.println(uniquePairs.size());

    }
}
