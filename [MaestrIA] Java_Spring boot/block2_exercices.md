# Block 2 — Days 06–10: Interview Questions & Exercises

**Days covered:**
[Day 06 — Ordering](./%5BDay%2006%5D%20Java%20Ordering_%20Comparable%2C%20Comparator%2C%20Tr.md) •
[Day 07 — Generics](./%5BDay%2007%5D%20%20Java%20Generics.md) •
[Day 08 — Exceptions](./%5BDay%2008%5D%20Exceptions%20%26%20Error%20Handling.md) •
[Day 09 — final, Immutability, Scopes & Lifecycle](./%5BDay%2009%5D%20final%2C%20Immutability%2C%20Spring%20Bean%20Scopes%20%26.md) •
[Day 10 — Annotations & @Configuration/@Bean](./%5BDay%2010%5D%20Annotations.md)

**How to use:** answer each question out loud in English first, then open the answer to check yourself. Do the exercises without looking at your notes. Plain-Java exercises go in `src/java_core`; Spring ones go in a scratch Boot module.

---

## Day 06 — Ordering: Comparable, Comparator, TreeSet & TreeMap

### Interview questions

**1. `Comparable` vs `Comparator`?**
<details><summary>Answer</summary>

`Comparable` defines a class's **natural order** inside the class (`compareTo`), and there's only one. `Comparator` is an **external** strategy (`compare`), and you can have as many as you want. Use `Comparator` for classes you don't own or for alternative orderings.
</details>

**2. What's wrong with `return this.price - other.price;`?**
<details><summary>Answer</summary>

Integer overflow. For large values with opposite signs the subtraction wraps around and returns the wrong sign. Use `Integer.compare(a, b)` or `Comparator.comparingInt(...)`.
</details>

**3. A `TreeSet<Player>` sorted by score is "losing" players. Why?**
<details><summary>Answer</summary>

`TreeSet` decides uniqueness with `compareTo`/`compare`, **not** `equals`. Two players with the same score compare as 0, so the second one is treated as a duplicate and dropped. Add a tie-breaker, for example `.thenComparing(Player::getId)`.
</details>

**4. `HashMap` vs `TreeMap`?**
<details><summary>Answer</summary>

`HashMap`: O(1) average, no ordering, one null key allowed. `TreeMap`: O(log n), keys sorted, plus navigation methods (`floorKey`, `ceilingKey`, `headMap`, `tailMap`). No null keys with natural ordering.
</details>

**5. What does `comparing(A).thenComparing(B).reversed()` reverse?**
<details><summary>Answer</summary>

The **whole chain**, so both A and B end up descending. To reverse only B, write `thenComparing(B, Comparator.reverseOrder())`.
</details>

**6. How do you sort when the sort field can be `null`?**
<details><summary>Answer</summary>

Wrap the key comparator: `Comparator.comparing(Property::getCity, Comparator.nullsLast(Comparator.naturalOrder()))`.
</details>

### Exercises

**Exercise 6.1 — Multi-field sort**

Sort a `List<Property>` by **city ascending**, then **price descending**, then **id ascending**, using a single `Comparator` chain. Then do it again with `Property implements Comparable` and explain which version you'd choose, and why.

**Exercise 6.2 — Fix the leaderboard**

```java
TreeSet<Player> board = new TreeSet<>(Comparator.comparingInt(Player::getScore).reversed());
board.add(new Player(1, "Ana", 90));
board.add(new Player(2, "Bia", 90));
board.add(new Player(3, "Caio", 75));
System.out.println(board.size());   // ?
```

Predict the output, explain it, and fix it so that all three players stay **and** equal scores are ordered by id.

**Exercise 6.3 — Price tiers with `TreeMap`**

```java
TreeMap<Integer, String> tiers = new TreeMap<>(Map.of(
    0, "BUDGET", 2000, "STANDARD", 5000, "PREMIUM", 10000, "LUXURY"));
String tierFor(int price);   // 3500 → STANDARD, 10000 → LUXURY
```

Implement `tierFor` in one line. Which `TreeMap` method did you use, and what does it return for `-1`?

---

## Day 07 — Generics

### Interview questions

**1. Why isn't `List<Integer>` a `List<Number>`?**
<details><summary>Answer</summary>

Generics are **invariant**. If it were allowed, you could call `numbers.add(3.14)` through the `List<Number>` reference and corrupt a list of Integers. Arrays **are** covariant, which is why `Object[] a = new Integer[1]; a[0] = "x";` compiles and then fails at runtime with `ArrayStoreException`.
</details>

**2. Explain PECS.**
<details><summary>Answer</summary>

Producer `extends`, Consumer `super`. If you only **read** `T`s from a collection, use `? extends T`. If you only **write** `T`s into it, use `? super T`. `Collections.copy(List<? super T> dest, List<? extends T> src)` is the classic example.
</details>

**3. What is type erasure, and what can't you do because of it?**
<details><summary>Answer</summary>

Generic type information is checked at compile time and removed from the bytecode, so `T` becomes its bound (or `Object`). Because of that you can't write `new T()`, `new T[]`, `instanceof List<String>`, or overload methods that differ only by type parameter (`m(List<String>)` vs `m(List<Integer>)`).
</details>

**4. What does `<T extends Comparable<? super T>>` mean, and why the `super`?**
<details><summary>Answer</summary>

T must be comparable to T **or to one of its supertypes**. That lets a subclass, say `Apartment extends Property` where `Property implements Comparable<Property>`, work even though `Apartment` isn't a `Comparable<Apartment>`.
</details>

**5. Why avoid raw types?**
<details><summary>Answer</summary>

A raw `List` turns off type checking, so mistakes show up later as `ClassCastException` far from the cause. Raw types exist only for compatibility with code written before generics.
</details>

### Exercises

**Exercise 7.1 — Generic in-memory repository**

```java
public interface Identifiable<ID> { ID getId(); }

public class InMemoryRepository<T extends Identifiable<ID>, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
}
```

Implement it, then use it for both `Property` (`Long` id) and `Customer` (`String` email id). Try to call `save` with the wrong type and read the compile error.

**Exercise 7.2 — PECS utilities**

```java
static <T extends Comparable<? super T>> T max(Collection<? extends T> items);
static <T> void copyMatching(List<? extends T> src, List<? super T> dst, Predicate<? super T> filter);
```

Implement both. Show that `copyMatching(List<Apartment>, List<Property>, p -> p.getPrice() > 1000)` compiles, then remove one wildcard at a time and explain which call breaks.

---

## Day 08 — Exceptions & Error Handling

### Interview questions

**1. Checked vs unchecked: when do you use each?**
<details><summary>Answer</summary>

Checked (`Exception`, but not `RuntimeException`) must be declared or caught. Use them for **recoverable** conditions the caller is expected to handle, such as I/O. Unchecked (`RuntimeException`) is for programming errors and business rule violations the caller usually can't fix locally. Modern Spring code mostly uses unchecked exceptions.
</details>

**2. `throw` vs `throws`?**
<details><summary>Answer</summary>

`throw` is a statement that raises an exception instance. `throws` is part of a method signature and declares which checked exceptions the method can propagate.
</details>

**3. What does try-with-resources do, and what are suppressed exceptions?**
<details><summary>Answer</summary>

It calls `close()` on every `AutoCloseable` automatically, in **reverse** order of declaration. If the body throws and `close()` also throws, the body's exception is kept, and the close exception is attached to it through `getSuppressed()` instead of replacing it.
</details>

**4. What does this return?** `try { return 1; } finally { return 2; }`
<details><summary>Answer</summary>

`2`. A `return` in `finally` overrides the earlier one and also **swallows any exception** thrown in the `try`. Never return from `finally`.
</details>

**5. Which exceptions make `@Transactional` roll back by default?**
<details><summary>Answer</summary>

Only unchecked ones (`RuntimeException` and `Error`). A **checked** exception **commits** the transaction unless you declare `rollbackFor = ...`. Also, catching the exception inside the method means no rollback, because the proxy never sees it.
</details>

**6. What makes a good custom exception?**
<details><summary>Answer</summary>

It's named after the problem (`PropertyNotFoundException`) and carries useful context (the id). It usually extends `RuntimeException`. When wrapping, **always pass the cause**, `new X(msg, cause)`, so you don't lose the original stack trace.
</details>

### Exercises

**Exercise 8.1 — Not-found flow**

1. Create `PropertyNotFoundException extends RuntimeException` that stores the missing id.
2. Make `PropertyService.getById(long id)` throw it.
3. Call it from `main`, catch it, and print a user-friendly message plus the id. Don't print the stack trace.

**Exercise 8.2 — Predict the order**

```java
class Res implements AutoCloseable {
    private final String name;
    Res(String name) { this.name = name; System.out.println("open " + name); }
    public void close() { System.out.println("close " + name); throw new IllegalStateException("close " + name); }
}

try (Res a = new Res("A"); Res b = new Res("B")) {
    throw new RuntimeException("body");
} catch (RuntimeException e) {
    System.out.println("caught " + e.getMessage());
    for (Throwable s : e.getSuppressed()) System.out.println("suppressed " + s.getMessage());
}
```

Write down the full output before you run it.

**Exercise 8.3 — Wrap without losing the cause**

Write `String readTemplate(Path path)` that uses `Files.readString`, and turn `IOException` into your own unchecked `TemplateLoadException`. Show that the original `IOException` still appears in the stack trace under "Caused by".

---

## Day 09 — final, Immutability, Bean Scopes & Lifecycle

### Interview questions

**1. Does `final` make an object immutable?**
<details><summary>Answer</summary>

No. `final` makes the **reference** impossible to reassign. The object it points to can still change: `final List<String> l = new ArrayList<>(); l.add("x");` is legal.
</details>

**2. What's the recipe for an immutable class?**
<details><summary>Answer</summary>

- Make the class `final`, or give it private constructors.
- Make all fields `private final`.
- Don't write setters.
- Make **defensive copies** of mutable inputs in the constructor and of mutable outputs in getters (`List.copyOf`).
- Don't let `this` escape during construction.
</details>

**3. Is a Spring singleton bean thread-safe?**
<details><summary>Answer</summary>

Not automatically. There's one instance shared by all threads. It's safe only if it's **stateless**, or its shared state is immutable or properly synchronized. Keeping per-request data (current user, a counter) in a field is a race condition.
</details>

**4. You inject a prototype bean into a singleton. What happens, and how do you fix it?**
<details><summary>Answer</summary>

The prototype is created **once**, when the singleton is wired, so it behaves like a singleton. Fix it with `ObjectProvider<T>` (call `getObject()` each time), `@Lookup` method injection, or a scoped proxy.
</details>

**5. `@PostConstruct` vs doing the work in the constructor?**
<details><summary>Answer</summary>

`@PostConstruct` runs **after** all dependencies are injected, including setter and field injection, and after the bean is fully configured. Use it for initialization that needs the dependencies, like warming a cache or validating config.
</details>

**6. Is `@PreDestroy` called for prototype beans?**
<details><summary>Answer</summary>

No. Spring creates prototypes and hands them over without tracking them, so their cleanup is your job.
</details>

### Exercises

**Exercise 9.1 — Make `Lease` immutable**

```java
public class Lease {
    private Customer tenant;
    private List<String> rules;
    private Date start;

    public Lease(Customer tenant, List<String> rules, Date start) {
        this.tenant = tenant; this.rules = rules; this.start = start;
    }
    public List<String> getRules() { return rules; }
    public Date getStart() { return start; }
    public void setTenant(Customer c) { this.tenant = c; }
}
```

Make it truly immutable. Replace `Date` with `LocalDate` and explain why that helps. Write a test showing that changing the original `rules` list after construction doesn't affect the lease.

**Exercise 9.2 — Singleton race**

```java
@Service
public class BookingCounter {
    private int bookingsToday = 0;
    public int book() { return ++bookingsToday; }
}
```

Call `book()` 10,000 times from a thread pool of 8 threads and print the final count. Explain the result, then fix it two ways: `AtomicInteger`, and moving the state out of the bean. Say which fix you'd defend in a code review.

**Exercise 9.3 — Prototype trap**

Make a `@Scope("prototype") ReceiptBuilder` that prints its `hashCode()` when created. Inject it into a singleton `CheckoutService`, call checkout three times, and observe the result. Fix it with `ObjectProvider<ReceiptBuilder>`.

---

## Day 10 — Annotations & `@Configuration` / `@Bean`

### Interview questions

**1. Do annotations do anything by themselves?**
<details><summary>Answer</summary>

No. They are **metadata**. Something has to read them and act on them: the compiler (`@Override`), an annotation processor (Lombok, MapStruct), or runtime reflection (Spring, JUnit).
</details>

**2. `SOURCE` vs `CLASS` vs `RUNTIME` retention?**
<details><summary>Answer</summary>

- `SOURCE`: thrown away by the compiler.
- `CLASS`: kept in the `.class` file but not visible through reflection (this is the default).
- `RUNTIME`: available through reflection. Spring needs `RUNTIME`, because it reads annotations while the app runs.
</details>

**3. `@Component` vs `@Bean`?**
<details><summary>Answer</summary>

`@Component` goes on **your own class** and is found by scanning. `@Bean` goes on a **method** inside a `@Configuration` class, and you build the object yourself. Use `@Bean` for third-party classes you can't annotate, or when creating the object takes real logic.
</details>

**4. What does `@Configuration` do that `@Component` doesn't, when one `@Bean` method calls another?**
<details><summary>Answer</summary>

`@Configuration` classes are CGLIB-proxied (`proxyBeanMethods = true`). A call to another `@Bean` method returns the **existing singleton** instead of creating a new object. With `@Component` or `proxyBeanMethods = false` the call is a plain Java call, so you get a new instance each time. Use method-parameter injection to avoid depending on this.
</details>

**5. What's the bean name of `@Bean public Clock systemClock()`?**
<details><summary>Answer</summary>

`systemClock`, the method name, unless you set `@Bean("name")`.
</details>

**6. What is the special `value` attribute of an annotation?**
<details><summary>Answer</summary>

If it's the only attribute you set, you can leave out its name: `@Audit("create")` instead of `@Audit(value = "create")`.
</details>

### Exercises

**Exercise 10.1 — `@Timed` with reflection**

Create `@Timed` (`@Target(METHOD)`, `@Retention(RUNTIME)`, with a `String label() default ""`). Annotate two methods in `PropertyService`, one of which calls `Thread.sleep`. Write a runner that uses reflection to invoke every `@Timed` method and print `label: Xms`. Then change the retention to `CLASS` and explain what happens.

**Exercise 10.2 — A testable clock**

1. Register `java.time.Clock` as a `@Bean` in a `@Configuration` class. You can't put `@Component` on a JDK class.
2. Inject it into `LeaseService.isExpired(LocalDate end)`.
3. Unit-test `isExpired` with `Clock.fixed(...)`, without Spring.

**Exercise 10.3 — `proxyBeanMethods` experiment**

```java
@Configuration
public class AppConfig {
    @Bean PropertyFormatter formatter() { return new PropertyFormatter(); }
    @Bean PropertyService service() { return new PropertyService(formatter()); }
}
```

Print `formatter()`'s identity from the context and from inside `service`. Switch to `@Configuration(proxyBeanMethods = false)` and compare the results. Then rewrite `service()` so its behavior doesn't depend on the flag.

---

## Block review — mixed questions

**1. Why do immutable objects make good `HashMap` keys *and* safe shared Spring state?**
<details><summary>Answer</summary>

Their hash can't change after insertion (Day 05/09), and threads can share them without locks because nothing can change (Day 09). The same property solves both problems.
</details>

**2. How do annotations, reflection and `@Transactional` rollback fit together?**
<details><summary>Answer</summary>

`@Transactional` is `RUNTIME` metadata (Day 10). Spring reads it through reflection and wraps the bean in a proxy. The proxy starts the transaction and rolls back when an unchecked exception crosses it (Day 08). That's also why self-invocation skips the transaction: the call never goes through the proxy.
</details>

**3. Code review: what's wrong here?**

```java
@Service
public class ReportService {
    private final List<Property> cache = new ArrayList<>();
    public List<Property> top(int n) {
        cache.sort((a, b) -> b.getPrice() - a.getPrice());
        return cache.subList(0, n);
    }
}
```

<details><summary>Answer</summary>

- A shared mutable list in a singleton, with no synchronization (Day 09).
- `final` doesn't protect it (Day 09).
- The subtraction comparator can overflow (Day 06). Use `comparingInt(Property::getPrice).reversed()`.
- `subList` returns a **view** of the shared internal list, so callers can change the cache. Return `List.copyOf(...)`.
- `subList` throws if `n > size`.
</details>
