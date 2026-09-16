---
title: '[Day 07]  Java Generics'
updated: 2026-09-11 14:11:53Z
created: 2026-09-11 14:11:16Z
latitude: -25.42777520
longitude: -49.27306160
altitude: 0.0000
---


Understand how Java Generics provide type safety, reusability,
and compile-time validation.
1. What are Generics?
Generics allow classes, interfaces, and methods to work with different
types while preserving type safety.
``` java
List<String> names = new ArrayList<>();
names.add("Jackson");
// names.add(10); // Compile error
```
Without generics, type mistakes can survive until runtime and require
casts.
> Generics move many type errors from runtime to compile time.
2. Generic Classes
``` java
public class Box<T> {
    private T value;

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}
```
Usage:
``` java
Box<String> stringBox = new Box<>();
stringBox.setValue("Hello");

Box<Integer> numberBox = new Box<>();
numberBox.setValue(100);
```
`T` is a type parameter.
3. Multiple Type Parameters
``` java
public class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
}
```
This is the same general concept used by `Map<K,V>`.
4. Generic Methods
``` java
public static <T> T first(List<T> values) {
    return values.get(0);
}
```
Usage:
``` java
List<String> names = List.of("Jackson", "John");
String firstName = first(names);
```
The `<T>` before the return type declares the method's generic type.
5. Bounded Type Parameters
``` java
public static <T extends Number> double doubleValue(T value) {
    return value.doubleValue() * 2;
}
```
Valid for `Integer`, `Double`, `Long`, etc., but not `String`.
With an interface, generic bounds still use `extends`:
``` java
public static <T extends Comparable<T>> T max(T a, T b) {
    return a.compareTo(b) >= 0 ? a : b;
}
```
There is no `<T implements ...>` syntax for generic bounds.
6. Raw Types
Avoid:
``` java
List list = new ArrayList();
```
Prefer:
``` java
List<String> list = new ArrayList<>();
```
This connects to `Comparator`:
``` java
class Checker implements Comparator<Player> {
    @Override
    public int compare(Player p1, Player p2) {
        return 0;
    }
}
```
Using `Comparator<Player>` preserves type information and avoids casts.
7. Generic Invariance
Suppose:
``` java
class Animal {}
class Dog extends Animal {}
```
This works:
``` java
Animal animal = new Dog();
```
But this does not:
``` java
List<Dog> dogs = new ArrayList<>();
List<Animal> animals = dogs; // Compile error
```
Mental model:
``` text
Dog IS-A Animal              ✓
List<Dog> IS-A List<Animal>  ✗
```
Java generic types such as `List<T>` are invariant.
Why? If the assignment were legal, someone could add a plain `Animal`
through `animals`, corrupting a list that is supposed to contain only
`Dog`.
Important distinction
This is valid:
``` java
List<Animal> animals = List.of(
    new Dog(),
    new Dog()
);
```
The list's declared element type is `Animal`, and `Dog` objects are
valid `Animal` values.
8. Interfaces Do Not Change Invariance
``` java
interface Property {}
class Apartment implements Property {}
```
This works:
``` java
Property property = new Apartment();

List<Property> properties = List.of(
    new Apartment(),
    new Apartment()
);
```
But this does not:
``` java
List<Apartment> apartments = List.of(new Apartment());
List<Property> properties = apartments; // Compile error
```
So:
``` text
Apartment IS-A Property                  ✓
List<Apartment> IS-A List<Property>      ✗
```
The rule is the same for classes and interfaces.
9. Wildcard `?`
``` java
public static void printList(List<?> values) {
    for (Object value : values) {
        System.out.println(value);
    }
}
```
`List<?>` means a list of some unknown element type.
It can accept `List<String>`, `List<Integer>`, `List<Customer>`, etc.
Because the actual element type is unknown, you generally cannot safely
add arbitrary values to it.
10. `? extends T`
``` java
public static void printAnimals(List<? extends Animal> animals) {
    for (Animal animal : animals) {
        System.out.println(animal);
    }
}
```
It can accept:
``` text
List<Animal>
List<Dog>
List<Cat>
```
Mental model:
``` text
? extends Animal
= some unknown type that is Animal or a subtype of Animal
```
You can safely read values as `Animal`, but cannot generally add a
`Dog`, because the actual list might be `List<Cat>`.
11. `? super T`
``` java
public static void addDogs(List<? super Dog> list) {
    list.add(new Dog());
}
```
It can accept:
``` text
List<Dog>
List<Animal>
List<Object>
```
Mental model:
``` text
? super Dog
= Dog or some supertype of Dog
```
This is useful when the collection consumes values supplied by your
code.
12. PECS
``` text
PECS = Producer Extends, Consumer Super
```
Example:
``` java
public static void copyDogs(
        List<? extends Dog> source,
        List<? super Dog> destination) {

    for (Dog dog : source) {
        destination.add(dog);
    }
}
```
Mental model:
``` text
source      → produces Dog → extends
destination → consumes Dog → super
```
13. Practical Property Example
``` java
public class Property {
    private final Long id;
    private final double price;

    public Property(Long id, double price) {
        this.id = id;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}

class Apartment extends Property {
    public Apartment(Long id, double price) {
        super(id, price);
    }
}
```
Flexible method:
``` java
public static double calculateTotalPrice(
        List<? extends Property> properties) {

    double total = 0;

    for (Property property : properties) {
        total += property.getPrice();
    }

    return total;
}
```
Both work:
``` java
List<Apartment> apartments = List.of(
    new Apartment(1L, 2000),
    new Apartment(2L, 3000)
);
calculateTotalPrice(apartments); // OK
```
``` java
List<Property> properties = List.of(
    new Apartment(1L, 2000),
    new Apartment(2L, 3000)
);
calculateTotalPrice(properties); // OK
```
Critical distinction
If the method instead declared:
``` java
calculateTotalPrice(List<Property> properties)
```
then:
``` java
List<Property> properties = ...;
calculateTotalPrice(properties); // OK

List<Apartment> apartments = ...;
calculateTotalPrice(apartments); // Compile error
```
`List<? extends Property>` is what lets the method accept both
`List<Property>` and lists of subtypes such as `List<Apartment>`.
14. Type Erasure
Java generics primarily provide compile-time type information.
``` java
List<String> strings = new ArrayList<>();
List<Integer> numbers = new ArrayList<>();
```
Much of the generic type information is erased at runtime.
Therefore this is invalid:
``` java
if (obj instanceof List<String>) {
}
```
But this is valid:
``` java
if (obj instanceof List<?>) {
}
```
You also generally cannot instantiate a type parameter directly:
``` java
class Box<T> {
    public T create() {
        // return new T(); // Compile error
    }
}
```
Interview Summary
``` text
Why Generics?
→ Type safety
→ Reusable code
→ Fewer casts
→ Compile-time error detection

<T>
→ Generic type parameter

<T extends X>
→ T is restricted to X/subtypes

?
→ Unknown type

? extends X
→ Producer / generally read

? super X
→ Consumer / generally write

PECS
→ Producer Extends, Consumer Super

Invariance
→ List<Child> is NOT List<Parent>

Raw types
→ Avoid when possible

Type erasure
→ Generic type information is largely a compile-time mechanism
```
Quick Mental Model
``` text
GENERICS
│
├── <T>                 generic type
├── <T extends X>       bounded type
├── ?                   unknown type
├── ? extends X         producer / read
├── ? super X           consumer / write
├── PECS                 Producer Extends, Consumer Super
├── Invariance           List<Child> != List<Parent>
└── Type Erasure         generic information largely erased at runtime
```