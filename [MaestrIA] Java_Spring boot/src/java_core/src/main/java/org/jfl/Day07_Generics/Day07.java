package org.jfl.Day07_Generics;


import org.jfl.Day07_Generics.example.Apartment;
import org.jfl.Day07_Generics.example.Property;
import org.jfl.Day07_Generics.practice.ApartmentV2;
import org.jfl.Day07_Generics.practice.House;
import org.jfl.Day07_Generics.practice.Propriedade;
import org.jfl.Day07_Generics.practice.Repository;

import java.util.ArrayList;
import java.util.List;

public class Day07 {
    //4. Generic methods
    public static <T> void print(T value){
        System.out.println(value);
    }

    //4. Generic methods
    public static <T> T first (List<T> values){
        return values.get(0);
    }

    //5. Bounded type parameters
    public static <T extends Number> double doubleValue(T value){
        return value.doubleValue() * 2;
    }

    //6. extends with interfaces
    public static <T extends Comparable<T>> T max(T a, T b){
        return a.compareTo(b) >= 0 ? a:b;
    }

    // 10. Wildcard ?
    public static void printList(List<?> values) {
        for (Object value : values) {
            System.out.println(value);
        }
    }

    //14. Practical example with your domain
    public static double calculateTotalPrice(
            List<? extends Property> properties) {

        double total = 0;

        for (Property property : properties) {
            total += property.getPrice();
        }

        return total;
    }


    public static void main(String[] args) {

        List<String> names = new ArrayList<>();
        names.add("Jackson");
        // names.add(10); // compile error

        //2. Generic classes
        Box<String> stringBox = new Box<>();
        stringBox.setValue("Hello");
        String value = stringBox.getValue();
        System.out.println(value);

        Box<Integer> numberBox = new Box<>();
        numberBox.setValue(10);
        Integer number = numberBox.getValue();
        System.out.println(number);

        //3. Multiple type parameters
        Pair<Long,String> customer = new Pair<>(1L,"Jackson Lima");
        Long id = customer.getKey();
        String name = customer.getValue();
        System.out.println("\n\nCustomer id: "+id+"\nCustomer name:"+name);

        //4. Generic methods
        print(customer.getKey());
        print(customer.getValue());
        List<String> nameList = List.of("Eva","Lovia");
        String firstName = first(nameList);
        System.out.println("\nfirstName: "+firstName);

        //5. Bounded type parameters
        System.out.println(doubleValue(10)); // Integer
        System.out.println(doubleValue(10.05)); // Double
        System.out.println(doubleValue(5L)); // Long
        //System.out.println(doubleValue("5L")); // compile error

        //6. extends with interfaces
        System.out.println(max(10,20));
        System.out.println(max("Zaz","Maria"));

        //10. Wildcard ?
        printList(List.of("A", "B"));
        printList(List.of(1, 2, 3));

        //14. Practical example with your domain
        List<Apartment> apartments = List.of(
                new Apartment(1L, 2000),
                new Apartment(2L, 3000)
        );

        double total = calculateTotalPrice(apartments);
        System.out.println("total: "+total);

        List<Property> apartments2 = List.of(
                new Apartment(1L, 2000),
                new Apartment(2L, 3000)
        );
        double total2 = calculateTotalPrice(apartments2);
        System.out.println("total: "+total2);

        //Practice
        Repository<ApartmentV2> apartmentsV2 = new Repository<>();

        apartmentsV2.add( new ApartmentV2(1L,"Batel", 3000));
        apartmentsV2.add( new ApartmentV2(1L,"Portao", 3000));

        List<Propriedade> properties = List.of(
                new Propriedade(1L, 1000),
                new Propriedade(2L, 2000)
        );

        List<ApartmentV2> apartments3 = List.of(
                new ApartmentV2(3L, "Batel", 3000),
                new ApartmentV2(4L, "Centro", 4000)
        );

        List<House> houses = List.of(
                new House(),
                new House()
        );

        double totalPriceProperties = Repository.calculateTotalPrice(properties);
        double totalPriceHouses     = Repository.calculateTotalPrice(houses);
        double totalPriceApartments = Repository.calculateTotalPrice(apartments3);

        System.out.println("\ntotalPriceProperties: "+totalPriceProperties);
        System.out.println("totalPriceHouses: "+totalPriceHouses);
        System.out.println("totalPriceApartments: "+totalPriceApartments);

        List<ApartmentV2> apartments4 = new ArrayList<>();

        apartments4.add(new ApartmentV2(1L, "Batel", 3000));
        apartments4.add(new ApartmentV2(2L, "Centro", 2500));

        List<Propriedade> properties2 = new ArrayList<>();
        Repository.copyProperties(apartments4,properties2);
        System.out.println("\ncopyProperties:");
        properties2.forEach(System.out::println);
    }
}
