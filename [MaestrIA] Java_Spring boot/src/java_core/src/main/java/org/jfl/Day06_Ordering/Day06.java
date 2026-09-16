package org.jfl.Day06_Ordering;

import java.util.*;

import net.datafaker.Faker;

public class Day06 {
    public static void main(String[] args) {
        Faker faker = new Faker();
        List<Customer> customerList = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Long id = faker.number().randomNumber(5, true);
            Integer age = faker.number().numberBetween(18, 80);
            String name = faker.name().fullName();
            customerList.add(new Customer(id, age, name));
        }

        System.out.println("Antes de ordenar:");
        customerList.forEach(System.out::println);

        /*Collections.sort(customerList);

        System.out.println("\nDepois de ordenar (por id):");
        customerList.forEach(System.out::println);
        */

        Comparator<Customer> byId = Comparator.comparing(Customer::getId);
        customerList.sort(byId);
        System.out.println("\nDepois de ordenar (por id):");
        customerList.forEach(System.out::println);

        Comparator<Customer> byName = Comparator.comparing(Customer::getName);
        customerList.sort(byName);
        System.out.println("\nDepois de ordenar (por name):");
        customerList.forEach(System.out::println);

        Comparator<Customer> byAgeThenNameThenId = Comparator.comparing(Customer::getAge).reversed()
                                               .thenComparing(Customer::getName)
                                               .thenComparing(Customer::getId);
        customerList.sort(byAgeThenNameThenId);
        System.out.println("\nDepois de ordenar (por Age, Name and Id):");
        customerList.forEach(System.out::println);

        //TreeSet
        System.out.println("\n\nTreeSet");
        Set<Integer> numbers = new TreeSet<>();
        numbers.add(10);
        numbers.add(2);
        numbers.add(7);
        System.out.println(numbers);

        System.out.println("\n\nTreeSet Customers");
        Set<Customer> customerTreeSet = new TreeSet<>(byAgeThenNameThenId);

        System.out.println("Adding: "+ customerList.get(1));
        customerTreeSet.add(customerList.get(1));

        System.out.println("Adding: "+ customerList.get(2));
        customerTreeSet.add(customerList.get(2));

        System.out.println("Adding: "+ customerList.get(3));
        customerTreeSet.add(customerList.get(3));

        System.out.println(customerTreeSet.toString());


        //TreeMap
        System.out.println("\n\nTreeMap");
        Map<Integer,String> customers = new TreeMap<>();
        customers.put(10,"Joao");
        customers.put(2,"Maria");
        customers.put(1,"Jose");
        System.out.println(customers.toString());

        System.out.println("\n\nTreeMap Customers");
        Map<Integer,Customer> customerTreeMap = new TreeMap<>();

        System.out.println("Adding: "+ customerList.get(9));
        customerTreeMap.put(9,customerList.get(9));

        System.out.println("Adding: "+ customerList.get(7));
        customerTreeMap.put(7,customerList.get(7));

        System.out.println("Adding: "+ customerList.get(1));
        customerTreeMap.put(1,customerList.get(1));
        System.out.println(customerTreeMap.toString());
    }
}