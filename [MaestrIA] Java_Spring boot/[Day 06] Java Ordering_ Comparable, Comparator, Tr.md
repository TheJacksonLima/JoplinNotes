---
title: '[Day 06] Java Ordering: Comparable, Comparator, TreeSet & TreeMap'
updated: 2026-09-10 13:25:54Z
created: 2026-09-10 13:25:10Z
latitude: -25.42777520
longitude: -49.27306160
altitude: 0.0000
---

# Day 6 — Java Ordering: Comparable, Comparator, TreeSet & TreeMap

## 1. Comparable

`Comparable<T>` defines the **natural/default ordering** of a class.

The class itself implements the interface:

```java
public class Customer implements Comparable<Customer> {

    private final Long id;
    private final Integer age;
    private final String name;

    @Override
    public int compareTo(Customer other) {
        return Long.compare(this.id, other.id);
    }
}
````

In this example, the natural ordering of `Customer` is by `id`.

### compareTo() result

```text
negative → this comes before other
0        → same position in ordering
positive → this comes after other
```

Example:

```java
customer1.compareTo(customer2);
```

### Mental Model

```text
Comparable
    ↓
implemented by the class
    ↓
compareTo()
    ↓
natural/default ordering
```

---

## 2. Comparator

`Comparator<T>` defines an **external/custom ordering strategy**.

Unlike `Comparable`, it allows us to have multiple sorting strategies for the
same class.

### Sort by ID

```java
Comparator<Customer> byId =
    Comparator.comparing(Customer::getId);
```

### Sort by name

```java
Comparator<Customer> byName =
    Comparator.comparing(Customer::getName);
```

### Multiple fields

```java
Comparator<Customer> byAgeThenNameThenId =
    Comparator.comparing(Customer::getAge)
        .reversed()
        .thenComparing(Customer::getName)
        .thenComparing(Customer::getId);
```

This means:

```text
age descending
    ↓
if same age
    ↓
name ascending
    ↓
if same name
    ↓
id ascending
```

---

## 3. Comparable vs Comparator

### Comparable

```text
Class defines its own ordering
        ↓
compareTo()
        ↓
one natural/default ordering
```

Example:

```java
class Customer implements Comparable<Customer>
```

### Comparator

```text
External ordering strategy
        ↓
compare()
        ↓
multiple possible orderings
```

Example:

```java
Comparator<Customer> byName;
Comparator<Customer> byAge;
Comparator<Customer> byId;
```

### Interview Answer

> Comparable defines the natural ordering of an object through `compareTo()`.
> Comparator defines an external/custom ordering strategy and allows multiple
> ways to order the same type.

---

# 4. Sorting a List

A list can use either the natural ordering or a Comparator.

Using natural ordering:

```java
Collections.sort(customerList);
```

This uses:

```java
Customer.compareTo()
```

Using a Comparator:

```java
customerList.sort(byName);
```

or:

```java
customerList.sort(byAgeThenNameThenId);
```

The Comparator does not change the natural ordering defined by `Comparable`.
It only defines the ordering for that particular operation.

---

# 5. TreeSet

`TreeSet` maintains its elements in sorted order.

Example:

```java
Set<Integer> numbers = new TreeSet<>();

numbers.add(10);
numbers.add(2);
numbers.add(7);

System.out.println(numbers);
```

Result:

```text
[2, 7, 10]
```

Typical complexity:

```text
add()      O(log n)
remove()   O(log n)
contains() O(log n)
```

Compare with `HashSet`:

```text
HashSet
→ average O(1)
→ no guaranteed iteration order

TreeSet
→ O(log n)
→ sorted
```

---

# 6. TreeSet with Comparable

If we create:

```java
Set<Customer> customers = new TreeSet<>();
```

Java needs to know how Customers should be ordered.

If `Customer` implements:

```java
Comparable<Customer>
```

then the `TreeSet` uses:

```java
Customer.compareTo()
```

Example:

```java
@Override
public int compareTo(Customer other) {
    return Long.compare(this.id, other.id);
}
```

Therefore:

```text
TreeSet<Customer>
       ↓
Customer.compareTo()
       ↓
ordered by ID
```

---

# 7. TreeSet with Comparator

We can override the natural ordering for a specific TreeSet by supplying a
Comparator:

```java
Set<Customer> customers =
    new TreeSet<>(byAgeThenNameThenId);
```

Now:

```text
TreeSet
   ↓
provided Comparator
   ↓
age → name → id
```

Mental model:

```text
new TreeSet<>()
        ↓
Comparable / compareTo()

new TreeSet<>(comparator)
        ↓
Comparator / compare()
```

---

# 8. TreeSet and Uniqueness

This is an important difference between `HashSet` and `TreeSet`.

## HashSet

Uses:

```text
hashCode()
    ↓
bucket
    ↓
equals()
    ↓
uniqueness
```

## TreeSet

Uses its ordering comparison:

```text
compareTo()
or
Comparator.compare()
       ↓
result == 0
       ↓
elements considered equivalent for set ordering
```

Example:

```java
Customer a =
    new Customer(1L, 20, "John");

Customer b =
    new Customer(1L, 50, "Mary");
```

Suppose `equals()` compares all fields:

```java
a.equals(b); // false
```

But `compareTo()` compares only ID:

```java
a.compareTo(b); // 0
```

Then:

```java
Set<Customer> customers = new TreeSet<>();

customers.add(a);
customers.add(b);

System.out.println(customers.size());
```

Result:

```text
1
```

The TreeSet considers them equivalent according to its ordering.

---

# 9. Ordering Consistent with equals()

Ideally:

```text
compare(a, b) == 0
```

should correspond to:

```text
a.equals(b) == true
```

This is called an ordering **consistent with equals**.

If they are inconsistent:

```text
equals() → false
compareTo() → 0
```

a `TreeSet` may contain only one of the objects.

This can produce surprising behavior.

---

# 10. TreeMap

`TreeMap` maintains entries sorted by **key**.

Example:

```java
Map<Integer, String> customers = new TreeMap<>();

customers.put(10, "Joao");
customers.put(2, "Maria");
customers.put(1, "Jose");
```

Result:

```text
1  → Jose
2  → Maria
10 → Joao
```

Typical complexity:

```text
put()    O(log n)
get()    O(log n)
remove() O(log n)
```

---

# 11. TreeMap Orders Keys, Not Values

Important:

```java
TreeMap<K, V>
        ↑
   ordering applies here
```

For:

```java
Map<Integer, Customer> map = new TreeMap<>();
```

the map is ordered by:

```text
Integer keys
```

`Customer.compareTo()` is irrelevant because `Customer` is the value.

However:

```java
Map<Customer, String> map = new TreeMap<>();
```

would use `Customer` ordering because `Customer` is now the key.

---

# 12. TreeMap vs HashMap

```text
HashMap
→ hashing
→ average O(1) get/put
→ no sorted order

TreeMap
→ ordered tree
→ O(log n) get/put
→ keys kept sorted
```

Use `TreeMap` when sorted/navigable key behavior is required.

Use `HashMap` when fast general-purpose key/value lookup is the main requirement.

---

# 13. Streams and sorted()

Important discovery from the exercise:

```java
customerTreeSet.stream()
    .sorted(byAgeThenNameThenId);
```

does NOT modify the original TreeSet.

Streams do not mutate the source collection.

Also, intermediate stream operations such as `sorted()` are lazy.

You need a terminal operation:

```java
customerTreeSet.stream()
    .sorted(byAgeThenNameThenId)
    .forEach(System.out::println);
```

This prints elements using the specified ordering, but the original TreeSet
still keeps its own ordering.

If the TreeSet itself must use that ordering:

```java
Set<Customer> customers =
    new TreeSet<>(byAgeThenNameThenId);
```

---

# Interview Cheat Sheet

## Comparable vs Comparator

```text
Comparable
→ natural ordering
→ implemented by the class
→ compareTo()

Comparator
→ custom/external ordering
→ multiple strategies possible
→ compare()
```

## HashSet vs TreeSet

```text
HashSet
→ hashCode + equals
→ average O(1)
→ no sorted order

TreeSet
→ compareTo / Comparator
→ O(log n)
→ sorted
```

## HashMap vs TreeMap

```text
HashMap
→ hashing
→ average O(1)
→ no sorted order

TreeMap
→ ordered tree
→ O(log n)
→ sorted by KEY
```

## TreeSet uniqueness

```text
compare(a, b) == 0
        ↓
TreeSet considers them equivalent
```

This can happen even when:

```text
a.equals(b) == false
```

if the ordering is inconsistent with equals.

## Stream sorted()

```text
stream().sorted()
→ does NOT modify original collection
→ returns a sorted stream
→ needs a terminal operation
```

---

# Final Mental Model

```text
             Java Ordering
                  │
        ┌─────────┴─────────┐
        │                   │
   Comparable           Comparator
        │                   │
   compareTo()           compare()
        │                   │
natural ordering      custom ordering


HashSet                   TreeSet
   │                         │
hashCode + equals       comparison
   │                         │
avg O(1)                 O(log n)


HashMap                   TreeMap
   │                         │
hashing                  sorted keys
   │                         │
avg O(1)                 O(log n)
```

## Key Interview Sentence

> `Comparable` defines a type's natural ordering, while `Comparator` provides
> external/custom ordering strategies. `TreeSet` and `TreeMap` use comparison
> to maintain sorted data, whereas `HashSet` and `HashMap` rely on hashing and
> equality for lookup.

```
```
