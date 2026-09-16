---
title: '[Day 09] final, Immutability, Spring Bean Scopes & Lifecycle'
updated: 2026-09-15 12:48:27Z
created: 2026-09-15 12:47:25Z
latitude: -25.42777520
longitude: -49.27306160
altitude: 0.0000
---

# [Day 09] final, Immutability, Spring Bean Scopes & Lifecycle

## Objective

Understand:

- The Java `final` keyword
- Immutability
- Defensive copying
- Mutable objects inside immutable classes
- Why immutability matters for concurrency and hash-based collections
- Spring bean scopes
- Singleton concurrency concerns
- Spring bean lifecycle
- `@PostConstruct`
- `@PreDestroy`

---

# Part 1 — Java `final`

The `final` keyword can be applied to:

- variables
- fields
- methods
- classes

---

## 1. `final` Variables

```java
final int maxRetries = 3;
````

This cannot later be reassigned:

```java
maxRetries = 5; // compile error
```

Mental model:

```text
final variable
→ cannot be reassigned after initialization
```

---

# 2. `final` References

Consider:

```java
final List<String> names = new ArrayList<>();
```

This is allowed:

```java
names.add("Jackson");
```

This is not:

```java
names = new ArrayList<>();
```

Why?

Because `final` applies to the **reference**.

It prevents the reference from pointing to another object.

It does NOT automatically make the referenced object immutable.

Mental model:

```text
final reference
      ↓
reference cannot change

BUT

referenced object may still change
```

Important:

```text
final reference
≠
immutable object
```

---

# 3. `final` Fields

Example:

```java
public class Customer {

    private final Long id;

    public Customer(Long id) {
        this.id = id;
    }
}
```

A final field must be initialized before construction completes.

This usually happens:

```text
field declaration
OR
constructor
```

---

# 4. `final` Methods

```java
public final void calculatePrice() {
}
```

A subclass cannot override this method.

---

# 5. `final` Classes

```java
public final class Customer {
}
```

The class cannot be extended.

A well-known example:

```java
String
```

`String` is final.

---

# Part 2 — Immutability

An immutable object's observable state cannot change after construction.

Example:

```java
public final class Customer {

    private final Long id;
    private final String name;

    public Customer(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
```

Typical characteristics:

```text
final class
private fields
final fields
constructor initialization
no setters
no mutation methods
```

Important:

These rules help create immutable classes, but the real goal is:

> No external code should be able to change the object's observable state after construction.

---

# 6. Mutable Fields Inside Immutable Classes

This class is NOT truly immutable:

```java
public final class User {

    private final String name;
    private final List<String> roles;

    public User(
        String name,
        List<String> roles
    ) {
        this.name = name;
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }
}
```

Why?

Because the caller still owns the original list.

Example:

```java
List<String> roles = new ArrayList<>();

User user = new User("Jackson", roles);

roles.add("ADMIN");
```

The internal state of `User` changed indirectly.

Also:

```java
user.getRoles().add("USER");
```

can modify the internal list.

So:

```text
private + final
```

alone is not sufficient when the referenced object itself is mutable.

---

# 7. Defensive Copy

A common solution:

```java
public final class User {

    private final String name;
    private final List<String> roles;

    public User(
        String name,
        List<String> roles
    ) {
        this.name = name;
        this.roles = List.copyOf(roles);
    }

    public List<String> getRoles() {
        return roles;
    }
}
```

`List.copyOf()` creates an unmodifiable copy.

Now modifications to the original list do not modify the internal state.

Mental model:

```text
External mutable list
        ↓
    copy
        ↓
Internal protected list
```

Instead of:

```text
External list
     ↓
same object
     ↓
Internal state
```

---

# 8. Why Immutability Matters

## Predictability

State does not unexpectedly change.

This makes objects easier to understand and debug.

---

## Concurrency

Immutable objects are naturally safer to share between threads because their state cannot change after construction.

Important nuance:

```text
immutable object
≠
entire application is thread-safe
```

A better interview answer:

> Immutable objects are safe from concurrent state mutation, which makes them easier to share between threads.

---

## HashMap / HashSet Keys

Mutable keys are dangerous.

Example:

```java
Map<Customer, String> map = new HashMap<>();
```

Suppose `hashCode()` depends on:

```text
id
email
```

The object is inserted:

```text
Customer
   ↓
hashCode = 100
   ↓
bucket 100
```

Then one of those fields changes:

```text
hashCode = 250
```

Now:

```java
map.get(customer)
```

may search another bucket.

The object physically remains in the old bucket.

Mental model:

```text
Mutable fields used by equals/hashCode
        ↓
dangerous HashMap key
```

Immutability helps guarantee stable hashing behavior.

---

# Part 3 — Spring Bean Scopes

A Spring bean can have different scopes.

The most important:

```text
singleton
prototype
request
session
```

---

# 9. Singleton Scope

Singleton is Spring's default bean scope.

Example:

```java
@Service
public class PropertyService {
}
```

Conceptually:

```text
ApplicationContext
       ↓
PropertyService
       ↓
one bean instance
```

Multiple consumers use the same bean:

```text
Controller A ─┐
              ├── PropertyService
Controller B ─┘
```

Important definition:

> One bean instance per bean definition per `ApplicationContext`.

This is NOT exactly the same as the classic GoF Singleton pattern.

Spring singleton does NOT necessarily mean:

```text
one instance in the entire JVM
```

It means one managed instance inside that Spring context.

---

# 10. Singleton Beans and Thread Safety ⭐

Singleton does NOT automatically mean thread-safe.

Consider:

```java
@Service
public class OrderService {

    private String currentCustomer;

    public void process(String customer) {
        currentCustomer = customer;

        // process
    }
}
```

Suppose:

```text
Request A → customer = Alice
Request B → customer = Bob
```

Because both requests use the same `OrderService` instance:

```text
Request A
      \
       → same OrderService
      /
Request B
```

Both threads can access:

```java
currentCustomer
```

This creates shared mutable state and can lead to a race condition.

Possible sequence:

```text
Thread A:
currentCustomer = "Alice"

Thread B:
currentCustomer = "Bob"

Thread A:
reads currentCustomer
→ sees "Bob"
```

---

# 11. Important Correction — `final` Is NOT the Fix

It may seem tempting to write:

```java
private final String currentCustomer;
```

But this does not solve the design problem.

Request-specific state should normally NOT live in a singleton field.

Better:

```java
@Service
public class OrderService {

    public void process(String customer) {

        String currentCustomer = customer;

        // process
    }
}
```

Or simply use:

```java
public void process(String customer) {
    // use customer directly
}
```

Now:

```text
Thread A
→ own method-local customer

Thread B
→ own method-local customer
```

Method-local variables are not shared between requests in the same way instance fields are.

Mental model:

```text
Singleton
+
shared mutable field
=
danger
```

Prefer:

```text
Singleton
+
stateless methods
=
normal Spring service design
```

---

# 12. Stateless Spring Services

Typical Spring services should normally be stateless.

Good:

```java
@Service
public class PriceService {

    public BigDecimal calculate(
            Property property) {

        return property.getPrice()
            .multiply(...);
    }
}
```

State exists in:

```text
method parameters
local variables
database
external systems
```

rather than request-specific mutable fields in the service.

Interview mental model:

```text
@Service
→ singleton by default
→ potentially accessed concurrently
→ keep it stateless
```

---

# 13. Prototype Scope

Example:

```java
@Component
@Scope("prototype")
public class SearchContext {
}
```

Spring creates a new instance whenever the bean is requested from the container.

Conceptually:

```text
getBean()
→ instance A

getBean()
→ instance B

getBean()
→ instance C
```

---

# 14. Prototype + Singleton Trap ⭐

Suppose:

```java
@Service
public class SearchService {

    private final SearchContext context;

    public SearchService(
        SearchContext context
    ) {
        this.context = context;
    }
}
```

Even if:

```java
SearchContext
```

is prototype scoped, the singleton `SearchService` is normally created only once.

The prototype dependency is resolved when the singleton is created.

Therefore:

```text
prototype injected into singleton
≠
new prototype every method invocation
```

This is a common interview trap.

---

# 15. Request Scope

Available in web-aware Spring applications.

```java
@RequestScope
@Component
public class RequestContext {
}
```

One instance per HTTP request.

```text
HTTP Request A
→ RequestContext A

HTTP Request B
→ RequestContext B
```

Useful for genuinely request-specific state.

---

# 16. Session Scope

```java
@SessionScope
@Component
public class UserSession {
}
```

One bean instance per HTTP session.

Conceptually:

```text
Session A
├── Request 1
├── Request 2
└── Request 3

all use the same session-scoped bean
```

---

# 17. Scope Summary

```text
singleton
→ one per bean definition / ApplicationContext

prototype
→ new when requested from container

request
→ one per HTTP request

session
→ one per HTTP session
```

For most application services:

```text
singleton + stateless
```

is the normal choice.

---

# Part 4 — Bean Lifecycle

Simplified Spring bean lifecycle:

```text
Bean definition
      ↓
Instantiation
      ↓
Dependency injection
      ↓
Initialization callbacks
      ↓
Bean ready
      ↓
Application uses bean
      ↓
Destruction callbacks
```

---

# 18. `@PostConstruct`

Runs after bean construction and dependency injection.

Example:

```java
@Component
public class PropertyCache {

    @PostConstruct
    public void init() {

        System.out.println(
            "Loading cache..."
        );
    }
}
```

Simplified lifecycle:

```text
Constructor
    ↓
Dependencies injected
    ↓
@PostConstruct
    ↓
Bean ready
```

Possible uses:

```text
validate configuration
initialize internal structures
warm small caches
perform initialization
```

Avoid putting large or slow workflows there without a good reason.

---

# 19. `@PreDestroy`

Runs when Spring is shutting down/destroying the bean.

```java
@Component
public class PropertyCache {

    @PreDestroy
    public void cleanup() {

        System.out.println(
            "Cleaning resources..."
        );
    }
}
```

Typical uses:

```text
cleanup
close custom resources
stop background workers
release resources
```

---

# 20. Prototype Lifecycle Nuance ⭐

Spring does not manage prototype destruction the same way it manages singleton destruction.

Conceptually:

```text
Spring
  ↓
creates prototype
  ↓
configures prototype
  ↓
hands object to caller
```

After that, cleanup responsibility may belong to the caller.

Therefore, don't assume:

```text
prototype
→ automatic @PreDestroy lifecycle
```

in the same way as singleton beans.

---

# 21. Bean Lifecycle Mental Model

```text
SPRING BEAN

Definition
   ↓
Instantiation
   ↓
Dependency Injection
   ↓
@PostConstruct
   ↓
READY
   ↓
Application usage
   ↓
@PreDestroy
```

---

# Interview Questions

Before marking this topic complete, I should be able to answer:

## Java

1. What does `final` mean for a primitive?
2. What does `final` mean for an object reference?
3. Does `final List<T>` make the list immutable?
4. What makes a class immutable?
5. Why are defensive copies needed?
6. Why are immutable objects useful in concurrent code?
7. Why are mutable HashMap keys dangerous?

## Spring

8. What is Spring's default bean scope?
9. What does singleton mean in Spring?
10. Is a Spring singleton automatically thread-safe?
11. Why should most Spring services be stateless?
12. What problem can mutable instance fields cause in a singleton service?
13. Why doesn't making a request-specific field `final` solve the problem?
14. What is prototype scope?
15. What are request and session scopes?
16. What does `@PostConstruct` do?
17. What does `@PreDestroy` do?
18. What is special about prototype destruction?

---

# Senior Interview Answers

## Is a Spring singleton thread-safe?

> Not automatically. A singleton bean may be accessed concurrently by multiple threads. It is normally safe when it is stateless and does not maintain shared mutable request-specific state.

---

## Why shouldn't a service store the current user/customer in a field?

> Because Spring services are singleton by default, so multiple concurrent requests may share and modify that field, creating race conditions and data corruption. Request-specific data should normally stay in method parameters or local variables.

---

## Does `final` make an object immutable?

> No. `final` prevents reassignment of the variable or reference. If the referenced object is mutable, its internal state may still change.

---

## What is defensive copying?

> Defensive copying creates a separate internal copy of mutable input so external code cannot modify an object's internal state through a shared reference.

---

# Quick Mental Model

```text
JAVA

final variable
→ cannot reassign

final reference
→ reference fixed
→ object may still mutate

immutable
→ state cannot change after construction

mutable field
→ defensive copy
```

```text
SPRING

singleton
→ one per ApplicationContext
→ default

singleton + state
→ concurrency risk

singleton + stateless
→ normal service design

prototype
→ new instance when requested

request
→ one per HTTP request

session
→ one per session
```

```text
LIFECYCLE

instantiate
   ↓
inject
   ↓
@PostConstruct
   ↓
use
   ↓
@PreDestroy
```
