package org.jfl.PriorityQueue;

import java.util.Comparator;
import java.util.PriorityQueue;

public class PriorityQueueReview {

    public static class Person {

        private String name;
        private int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    public static class PersonAgeComparator implements Comparator<Person>{

        @Override
        public int compare(Person p1, Person p2) {
            return Integer.compare(p1.getAge(), p2.getAge());
        }
    }

    //Multiple comparison fields
    //
    //This is where custom comparators become particularly useful.
    //
    //Suppose priority is:
    //
    //Lowest age first.
    //If same age, alphabetical name.
    public static class PersonComparatorAgeAndName implements Comparator<Person> {

        @Override
        public int compare(Person p1, Person p2) {

            int ageComparison =
                    Integer.compare(p1.getAge(), p2.getAge());

            if (ageComparison != 0) {
                return ageComparison;
            }

            return p1.getName().compareTo(p2.getName());
        }
    }

    public static void main(String[] args) {
        //By default, Java gives you a min-heap:
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.offer(10);
        pq.offer(7);
        pq.offer(5);
        pq.offer(30);
        System.out.println(pq.poll()); //5

        //Option 1 — Comparator directly in PriorityQueue
        PriorityQueue<Person> pqPerson = new PriorityQueue<>(Comparator.comparing(Person::getAge));
        pqPerson.offer(new Person("João",75));
        pqPerson.offer(new Person("Jackson",36));
        pqPerson.offer(new Person("Arthur",1));
        pqPerson.offer(new Person("Pedro",1));

        Person person = pqPerson.poll();
        System.out.println("Mais jovem: "+person.getName());

        PriorityQueue<Person> pqPerson_v2 = new PriorityQueue<>(new PersonAgeComparator());
        pqPerson_v2.offer(new Person("João",75));
        pqPerson_v2.offer(new Person("Jackson",36));
        pqPerson_v2.offer(new Person("Arthur",1));

        person = pqPerson_v2.poll();
        System.out.println("Mais jovem: "+person.getName());

        //Multiple comparison fields
        PriorityQueue<Person> pqPerson_v3 = new PriorityQueue<>(new PersonComparatorAgeAndName());
        pqPerson_v3.offer(new Person("João",75));
        pqPerson_v3.offer(new Person("Jackson",36));
        pqPerson_v3.offer(new Person("Arthur",1));
        pqPerson_v3.offer(new Person("Pedro",1));
        pqPerson_v3.offer(new Person("Jonas",1));

        System.out.println("Mais jovem[1]: "+pqPerson_v3.poll().getName());
        System.out.println("Mais jovem[2]: "+pqPerson_v3.poll().getName());
        System.out.println("Mais jovem[3]: "+pqPerson_v3.poll().getName());

        //The modern Java version is much cleaner:
        Comparator<Person> comparator = Comparator.comparingInt(Person::getAge).thenComparing(Person::getName);
        PriorityQueue<Person> pqPerson_v4 = new PriorityQueue<>(comparator);

        pqPerson_v4.offer(new Person("João",75));
        pqPerson_v4.offer(new Person("Jackson",36));
        pqPerson_v4.offer(new Person("Arthur",1));
        pqPerson_v4.offer(new Person("Pedro",1));
        pqPerson_v4.offer(new Person("Jonas",1));

        System.out.println("Mais jovem[1]: "+pqPerson_v4.poll().getName());
        System.out.println("Mais jovem[2]: "+pqPerson_v4.poll().getName());
        System.out.println("Mais jovem[3]: "+pqPerson_v4.poll().getName());



    }
}
