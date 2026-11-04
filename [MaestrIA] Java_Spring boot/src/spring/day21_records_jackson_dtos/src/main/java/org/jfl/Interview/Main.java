package org.jfl.Interview;

import java.util.ArrayList;
import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        ArrayList<Employee> l = new ArrayList<>();
        l.add(new Employee(1,"A",2000.00));
        l.add(new Employee(2,"B",2500.00)); //***
        l.add(new Employee(3,"C",500.00));
        l.stream().sorted(Comparator.comparingDouble(Employee::getSalary).reversed()).limit(1).forEach(System.out::println);

    }
}

