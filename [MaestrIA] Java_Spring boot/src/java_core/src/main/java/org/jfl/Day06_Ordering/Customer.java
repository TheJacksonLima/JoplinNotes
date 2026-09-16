package org.jfl.Day06_Ordering;

import lombok.Data;

@Data
public class Customer implements Comparable<Customer>{
    private final Long id;
    private final Integer age;
    private final String name;

    @Override
    public int compareTo(Customer other) {
        return Long.compare(this.id,other.id);
    }
}
