# Block 1 — Days 01–05: Interview Questions & Exercises

**Days covered:**
[Day 01 — Collections](./%5BDay%2001%5D%20Java%20Collections%20Framework%20%E2%80%94%20Overview.md) •
[Day 02 — IoC & DI](./%5BDay%2002%5D%20Spring%20Core_%20IoC%2C%20Dependency%20Injection%2C%20B.md) •
[Day 03 — HashMap & HashSet](./%5BDay%2003%5D%20Java_%20HashMap%20%26%20HashSet.md) •
[Day 04 — Component Scanning](./%5BDay%2004%5D%20Spring%20Component%20Scanning%20%26%20Dependency%20In.md) •
[Day 05 — equals() & hashCode()](./%5BDay%2005%5D%20Java_%20equals%28%29%20and%20hashCode%28%29.md)

**How to use:** answer each question out loud in English first, then open the answer to check yourself. Do the exercises without looking at your notes. Plain-Java exercises go in `src/java_core`; Spring ones go in a scratch Boot module.

---

## Day 01 — Java Collections Framework

### Interview questions

**1. What is the difference between `Collection` and `Collections`?**
<details><summary>Answer</summary>

`Collection` is the root **interface** for `List`, `Set` and `Queue`. `Collections` is a **utility class** with static helpers: `sort`, `unmodifiableList`, `emptyList`, `synchronizedList`, and so on.
</details>

**2. Does `Map` extend `Collection`? Why or why not?**
<details><summary>Answer</summary>

No. A `Collection` holds single elements (`add(E)`), but a `Map` holds key→value pairs (`put(K, V)`). The contracts don't fit together. A map exposes collection *views* instead: `keySet()`, `values()`, `entrySet()`.
</details>

**3. When does `LinkedList` actually beat `ArrayList`?**
<details><summary>Answer</summary>

Almost never in practice. `LinkedList` only wins when you insert or remove at a position you **already hold an iterator to**, or at the ends. Even then, `ArrayDeque` is usually better for the ends. The trap is that inserting "in the middle" of a `LinkedList` is still O(n), because you have to walk to the node first. `ArrayList` also benefits from CPU cache locality, since its elements sit in one contiguous array.
</details>

**4. What does "amortized O(1)" mean for `ArrayList.add()`?**
<details><summary>Answer</summary>

Most adds are O(1). When the backing array is full, it grows (about 1.5×) and copies everything, which is O(n). Because growth is geometric, those copies are rare enough that the **average** cost per add over many adds stays O(1).
</details>

**5. Why use `ArrayDeque` instead of `Stack`?**
<details><summary>Answer</summary>

`Stack` extends `Vector`, so every method is synchronized (overhead you rarely need). It also exposes index-based methods that break the stack abstraction. `ArrayDeque` is faster, and `Deque` supports both stack (`push`/`pop`) and queue (`offer`/`poll`) operations.
</details>

**6. What causes `ConcurrentModificationException` in a single-threaded loop, and how do you avoid it?**
<details><summary>Answer</summary>

Changing the collection structurally (add/remove) while a for-each loop iterates over it. The iterator's `modCount` check fails fast. Fixes: `iterator.remove()`, `list.removeIf(...)`, or collect the items first and remove them afterwards.
</details>

### Exercises

**Exercise 1.1 — Recently viewed properties**

Write `RecentlyViewed` with:

```java
public class RecentlyViewed {
    public RecentlyViewed(int capacity) { ... }
    public void view(long propertyId) { ... }
    public List<Long> recent() { ... }   // most recent first
}
```

Rules:
- There are no duplicates. Viewing an existing ID moves it to the front.
- Only the last `capacity` IDs are kept.

Done when `view(1); view(2); view(3); view(1)` with capacity 3 gives `[1, 3, 2]`, and a following `view(4)` gives `[4, 1, 3]`.

*Think about:* which collection gives you uniqueness **and** order? Also compare the Big-O of your solution with a plain `ArrayList`.

**Exercise 1.2 — Fix the bug**

```java
List<Property> properties = new ArrayList<>(repository.findAll());
for (Property p : properties) {
    if (p.getPrice() > 5000) {
        properties.remove(p);
    }
}
```

Run it and explain the exception. Fix it two different ways, and say which one you'd write in production.

---

## Day 02 — Spring Core: IoC & Dependency Injection

### Interview questions

**1. What is Inversion of Control, and how does DI relate to it?**
<details><summary>Answer</summary>

IoC means your code no longer controls how its dependencies are created and wired; the container does. DI is the **technique** Spring uses to implement IoC: dependencies are passed in (constructor, setter or field) instead of being created with `new`.
</details>

**2. Why is constructor injection preferred over field injection?**
<details><summary>Answer</summary>

- Dependencies are **explicit** and required: the object can't exist without them.
- Fields can be `final`, so they can't be swapped after construction.
- It's **testable without Spring**: just call `new Service(fakeRepo)`.
- A class with too many constructor parameters is a visible design smell.
- Circular dependencies fail at startup instead of hiding.
</details>

**3. What is a Spring bean?**
<details><summary>Answer</summary>

An object whose creation, wiring and lifecycle are managed by the Spring IoC container. A class you create yourself with `new` is **not** a bean, even if it has `@Component` on it.
</details>

**4. `BeanFactory` vs `ApplicationContext`?**
<details><summary>Answer</summary>

`BeanFactory` is the basic container: bean creation and lazy lookup. `ApplicationContext` extends it and adds eager singleton creation, event publishing, i18n (`MessageSource`), environment/profile support, and automatic registration of `BeanPostProcessor`s. In practice you always use `ApplicationContext`.
</details>

**5. Do you need `@Autowired` on a constructor?**
<details><summary>Answer</summary>

Not when the class has **one** constructor (since Spring 4.3). With several constructors, you mark the one Spring should use.
</details>

### Exercise

**Exercise 2.1 — Remove the `new`**

```java
public class RentalService {
    private final PropertyRepository repository = new InMemoryPropertyRepository();
    private final PriceCalculator calculator = new PriceCalculator();

    public double monthlyRent(long propertyId) {
        Property p = repository.findById(propertyId);
        return calculator.calculate(p);
    }
}
```

1. Refactor it to constructor injection and let Spring wire it (`@Service`, `@Repository`, `@Component`).
2. Write a **plain JUnit test**, without Spring, that passes a fake `PropertyRepository` returning a fixed property.
3. Explain in one sentence why step 2 was impossible before the refactor.

---

## Day 03 — HashMap & HashSet

### Interview questions

**1. Walk through what happens on `map.put(key, value)`.**
<details><summary>Answer</summary>

1. `key.hashCode()` is computed and spread (`h ^ (h >>> 16)`).
2. The bucket index is `(n - 1) & hash`.
3. If the bucket is empty, a new node is stored there.
4. Otherwise it walks the bucket: if a node has an equal hash **and** `equals()` matches, the value is replaced. If not, a new node is appended.
5. If the size exceeds `capacity × loadFactor`, the table resizes (doubles) and entries are redistributed.
</details>

**2. What is the load factor, and what happens on resize?**
<details><summary>Answer</summary>

The default is 0.75: the table resizes once it's 75% full. On resize the capacity doubles and every entry moves to a new bucket, which is O(n). If you know the size in advance, pre-size the map to avoid repeated resizes.
</details>

**3. What happens when many keys land in the same bucket?**
<details><summary>Answer</summary>

Lookups in that bucket degrade toward O(n). Since Java 8, a bucket with more than 8 entries is converted to a red-black tree (if the table capacity is at least 64), so the worst case becomes O(log n).
</details>

**4. How is `HashSet` implemented?**
<details><summary>Answer</summary>

It's backed by a `HashMap`: the elements are the keys, and every value is a shared dummy object. `add` returns `false` when the key already exists.
</details>

**5. Is `HashMap` thread-safe? What do you use instead?**
<details><summary>Answer</summary>

No. Concurrent writes can lose updates or corrupt internal state. Use `ConcurrentHashMap`, which gives safe concurrent access plus atomic `compute`/`merge`. `Collections.synchronizedMap` uses one lock for everything and is rarely the right choice.
</details>

**6. Does `HashMap` preserve insertion order?**
<details><summary>Answer</summary>

No. If you need insertion order, use `LinkedHashMap`. For sorted keys, use `TreeMap`.
</details>

### Exercises

**Exercise 3.1 — Group and count**

Given a `List<Property>` (fields: `id`, `city`, `price`), write methods **without streams**:

```java
Map<String, Integer> countByCity(List<Property> properties);        // use merge()
Map<String, List<Property>> groupByCity(List<Property> properties); // use computeIfAbsent()
```

Then write both again using streams (`groupingBy`, `counting`). Which version is easier to read?

**Exercise 3.2 — First duplicate email**

```java
Optional<String> firstDuplicate(List<String> emails);
```

Return the first email that appears twice, treating emails as case-insensitive. Use a single pass and the return value of `Set.add()`. What's the time complexity?

---

## Day 04 — Component Scanning & Multiple Implementations

### Interview questions

**1. How does Spring Boot decide which packages to scan?**
<details><summary>Answer</summary>

`@SpringBootApplication` includes `@ComponentScan`. By default it scans the package of the main class **and all its sub-packages**. A `@Component` class outside that tree is never found.
</details>

**2. What is the difference between `@Component`, `@Service`, `@Repository` and `@Controller`?**
<details><summary>Answer</summary>

All four register a bean. The specialized ones state the class's role. `@Repository` also enables **exception translation** (turning persistence exceptions into Spring's `DataAccessException`). `@Controller` marks a web handler for Spring MVC. `@Service` currently adds no behavior beyond showing intent.
</details>

**3. Two beans implement the same interface and you inject that interface. What happens?**
<details><summary>Answer</summary>

Startup fails with `NoUniqueBeanDefinitionException` (expected a single matching bean but found 2). Fix it with `@Primary`, `@Qualifier`, or by injecting `List<T>` / `Map<String, T>` to get all of them.
</details>

**4. `@Primary` vs `@Qualifier`: which wins?**
<details><summary>Answer</summary>

`@Qualifier` wins. `@Primary` sets the **default** candidate, and `@Qualifier` at the injection point **overrides** it for that one dependency.
</details>

**5. What is the default bean name of `@Component class SmsSender`?**
<details><summary>Answer</summary>

`smsSender`: the class name with a lowercase first letter. That's the name `@Qualifier("smsSender")` refers to unless you set your own.
</details>

### Exercise

**Exercise 4.1 — Notification senders**

1. Create `interface NotificationSender { void send(String to, String msg); }` with two implementations: `EmailSender` and `SmsSender`.
2. Inject `NotificationSender` into `BookingService` without any extra annotation. Run it and read the error.
3. Make `EmailSender` the default with `@Primary`.
4. Create `UrgentAlertService` that must use SMS, using `@Qualifier`.
5. Move `SmsSender` to a package **outside** the main class's package tree. Predict the error before you run it.

---

## Day 05 — equals() & hashCode()

### Interview questions

**1. `==` vs `equals()`?**
<details><summary>Answer</summary>

`==` compares **references** for objects (and values for primitives). `equals()` compares **logical equality** as defined by the class. The default `Object.equals` is the same as `==`.
</details>

**2. What is the `equals()` contract?**
<details><summary>Answer</summary>

It must be reflexive, symmetric, transitive and consistent, and `x.equals(null)` must be `false`.
</details>

**3. What is the `equals()`/`hashCode()` contract, and what breaks if you only override `equals()`?**
<details><summary>Answer</summary>

Equal objects **must** have equal hash codes. Unequal objects may collide. If you only override `equals`, two "equal" objects get different identity hash codes and land in different buckets. A `HashSet` then keeps both, and `contains()` returns `false` for an equal copy.
</details>

**4. Why are mutable fields dangerous as hash keys?**
<details><summary>Answer</summary>

If a field used in `hashCode` changes after insertion, the object sits in the bucket for its **old** hash. `contains()`/`get()` look in the new bucket and miss it, so the entry is effectively lost (and a memory leak). Use immutable fields for identity.
</details>

**5. `getClass()` vs `instanceof` in `equals()`?**
<details><summary>Answer</summary>

`instanceof` allows a subclass instance to equal a parent instance. If the subclass adds fields and overrides `equals`, symmetry can break. `getClass()` requires the exact same class, which is safer for non-final classes. With `final` classes and records the question doesn't come up.
</details>

**6. What do records give you here?**
<details><summary>Answer</summary>

`equals`, `hashCode` and `toString` generated from all components. Their components are also final, which avoids most of the mutable-key trap. The fields' own objects can still be mutable, though.
</details>

### Exercises

**Exercise 5.1 — Customer identity by email**

Implement `equals`/`hashCode` for:

```java
public final class Customer {
    private final String email;
    private String name;   // can change
}
```

Two customers are equal when their emails match, **ignoring case**. Prove it with a `HashSet` test: `new Customer("A@x.com", "Ana")` and `new Customer("a@x.com", "Ana Maria")` must count as one element. Where should the lowercase normalization happen, and why there?

**Exercise 5.2 — Predict the output**

```java
class Room {
    int number;
    Room(int n) { number = n; }
    @Override public boolean equals(Object o) {
        return o instanceof Room r && r.number == number;
    }
    @Override public int hashCode() { return Integer.hashCode(number); }
}

Set<Room> rooms = new HashSet<>();
Room r = new Room(101);
rooms.add(r);
r.number = 202;
System.out.println(rooms.contains(r));
System.out.println(rooms.contains(new Room(101)));
System.out.println(rooms.size());
```

Write your prediction for all three lines **before** running it, then explain each one.

---

## Block review — mixed questions

**1. A `HashSet<Customer>` has duplicates after a bulk import. Which days' topics do you check first?**
<details><summary>Answer</summary>

Day 05: is `hashCode` overridden consistently with `equals`? Are the identity fields mutable, and did anything change after insertion? Also check whether the data is normalized, since case or whitespace differences in the keys look like duplicates.
</details>

**2. Why is a Spring singleton `@Service` that stores results in a `HashMap` field risky?**
<details><summary>Answer</summary>

One instance is shared by all request threads (Day 02), and `HashMap` isn't thread-safe (Day 03). Use `ConcurrentHashMap`, or better, keep the service stateless.
</details>

**3. Explain DI to a junior dev using one sentence and one code line.**
<details><summary>Answer</summary>

"The class says what it needs, and the container provides it": `public BookingService(NotificationSender sender) { this.sender = sender; }`
</details>
