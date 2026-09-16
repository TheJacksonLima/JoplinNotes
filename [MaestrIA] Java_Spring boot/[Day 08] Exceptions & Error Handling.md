---
title: '[Day 8] Exceptions & Error Handling'
updated: 2026-09-16 12:27:12Z
created: 2026-09-14 18:10:48Z
latitude: -25.42777520
longitude: -49.27306160
altitude: 0.0000
---

# Java Day 8 --- Exceptions & Error Handling

## Objective

Understand Java exception handling at Senior Java interview level and
apply it correctly in Spring.

## 1. Exception Hierarchy

``` text
Throwable
├── Error
│   ├── OutOfMemoryError
│   └── StackOverflowError
└── Exception
    ├── RuntimeException → unchecked
    └── Other Exception subclasses → checked
```

`Error` usually represents serious JVM/environment problems. `Exception`
represents conditions application code may need to handle.

## 2. Checked vs Unchecked

### Checked

Checked by the compiler. Must be caught or declared with `throws`.

``` java
public void readFile() throws IOException {
    // ...
}
```

Mental model:

``` text
Checked → compiler enforcement → catch OR declare
```

### Unchecked

Extend `RuntimeException`. The compiler does not force catch/declare.

``` java
throw new IllegalArgumentException("Invalid ID");
```

Common examples: `NullPointerException`, `IllegalArgumentException`,
`IllegalStateException`.

In modern Spring applications, domain/business exceptions are frequently
unchecked.

## 3. `throw` vs `throws`

``` text
throw  → actual runtime action
throws → method declaration
```

``` java
throw new IllegalArgumentException("Invalid ID");

public void readFile() throws IOException {
    // ...
}
```

## 4. Exception Propagation

If the current layer cannot meaningfully handle an exception, it can
propagate upward.

``` text
Controller
    ↓
Service
    ↓
Repository
    ↓
Exception
    ↑
Service
    ↑
Controller
    ↑
Global Handler
```

Do not catch an exception merely to immediately rethrow the same
exception.

## 5. Custom Exceptions

Custom exceptions communicate domain meaning.

``` java
public class PropertyNotFoundException extends RuntimeException {
    public PropertyNotFoundException(Long id) {
        super("Property not found with id: " + id);
    }
}
```

Usage:

``` java
return repository.findById(id)
    .orElseThrow(() -> new PropertyNotFoundException(id));
```

## 6. try / catch / finally

``` java
try {
    process();
} catch (IOException e) {
    handle(e);
} finally {
    cleanup();
}
```

Catch when the current layer can meaningfully recover, translate, add
context, or perform necessary handling.

## 7. Try-With-Resources

Resources implementing `AutoCloseable` can be automatically closed.

``` java
try (BufferedReader reader =
         new BufferedReader(new FileReader("properties.csv"))) {
    System.out.println(reader.readLine());
}
```

Examples include readers, streams, JDBC connections, statements, and
result sets.

``` text
try-with-resources → AutoCloseable → automatic cleanup
```

## 8. Spring Exception Handling

Typical REST flow:

``` text
HTTP Request
    ↓
@RestController
    ↓
Service
    ↓
PropertyNotFoundException
    ↓
@ControllerAdvice
    ↓
HTTP 404
```

## 9. `@Transactional` and Exceptions ⭐

Default Spring behavior:

``` text
RuntimeException   → ROLLBACK
Error              → ROLLBACK
Checked Exception  → NO ROLLBACK by default
```

Customize checked-exception rollback:

``` java
@Transactional(rollbackFor = IOException.class)
public void importProperties() throws IOException {
    // ...
}
```

You can also use `noRollbackFor`.

Important: if you catch/swallow an exception inside a transactional
method, do not simply assume normal exception-based rollback will occur.

## 10. Best Practices

-   Do not swallow exceptions.
-   Avoid broad `catch (Exception e)` unless justified.
-   Preserve the original cause when translating exceptions.
-   Do not use exceptions for ordinary control flow.
-   Prefer meaningful domain exceptions over generic `RuntimeException`.
-   Do not add `try/catch` unless the current layer can actually do
    something useful.

## 11. RentMap Practical Exercise

Implement:

``` text
PropertyNotFoundException.java
PropertyService.java
```

Method:

``` java
public Property findProperty(Long id) {
    // TODO
}
```

Requirements: 1. Reject a `null` ID appropriately. 2. Search the
repository. 3. Throw `PropertyNotFoundException` if the property does
not exist. 4. Make it unchecked. 5. Avoid unnecessary `try/catch`.

## 12. Interview Questions

Be able to answer without notes:

1.  Checked vs unchecked exceptions?
2.  `throw` vs `throws`?
3.  When would you create a custom exception?
4.  Why use try-with-resources?
5.  What happens when `RuntimeException` escapes an `@Transactional`
    method?
6.  What happens for a checked exception by default?
7.  When should you catch instead of propagate?

### Interview-ready summary

> Checked exceptions are enforced by the compiler and must be caught or
> declared, while unchecked exceptions extend `RuntimeException` and
> don't have that requirement. In Spring applications, domain exceptions
> are often unchecked. By default, Spring rolls back transactions for
> `RuntimeException` and `Error`, but not for checked exceptions; this
> can be customized with `rollbackFor` and `noRollbackFor`.

------------------------------------------------------------------------

# Next Topic Started --- `final` and Immutability

An immutable object's observable state cannot change after construction.

Typical characteristics: - `private` fields - usually `final` fields -
constructor initialization - no setters - mutable internal state is not
exposed

Canonical example: `String`.

Benefits: - predictable state - easier reasoning - safer concurrency -
useful for stable HashMap/HashSet keys

## Important: `final` reference != immutable object

``` java
private final List<String> neighborhoods;
```

`final` prevents reassignment of the reference, but does not prevent:

``` java
neighborhoods.add("Batel");
```

Mental model:

``` text
final reference → reference cannot change
NOT → referenced object cannot change
```

For true immutability with mutable fields, use defensive-copy techniques
such as `List.copyOf(...)` and avoid exposing mutable internal state.

## Status

### Exceptions

-   [x] Theory
-   [x] Interview questions
-   [ ] RentMap practical implementation/review

### `final` / Immutability

-   [x] Basic concept
-   [x] `final` reference vs mutable object
-   [ ] Defensive copies
-   [ ] Practical exercise
-   [ ] Interview questions

**Next Java session:** finish `final` + Immutability, then move to
Spring Bean Scopes and Lifecycle.
