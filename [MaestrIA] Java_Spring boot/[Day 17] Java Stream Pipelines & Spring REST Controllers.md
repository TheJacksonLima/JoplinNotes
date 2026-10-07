# [Day 17] Java Stream Pipelines & Spring REST Controllers

## Objective

Understand and apply:

### Java
- Stream pipeline
- Stream source
- Intermediate operations
- Terminal operations
- Lazy evaluation
- `filter()`
- `map()`
- `sorted()`
- `limit()`
- `toList()`
- `forEach()`
- Method references with streams
- Enum comparison
- Stream results vs original collections

### Spring
- `@RestController`
- `@RequestMapping`
- `@GetMapping`
- `@PathVariable`
- Constructor injection
- Returning `List<Payment>` from REST endpoints
- JSON serialization
- Controller vs Service responsibilities
- Protecting internal collections with `List.copyOf()`

---

# 1. What Is a Java Stream?

A Java Stream represents a pipeline for processing data.

A stream is not a collection.

```text
Collection
→ stores data

Stream
→ processes data
```

Example collection:

```java
List<Payment> payments = new ArrayList<>();
```

Create a stream:

```java
payments.stream();
```

Mental model:

```text
List<Payment>
      ↓
   stream()
      ↓
Stream<Payment>
```

The stream does not replace the original collection.

---

# 2. Stream Pipeline

A stream pipeline normally has three parts:

```text
Source
   ↓
Intermediate operations
   ↓
Terminal operation
```

Example:

```java
List<Payment> positivePayments =
        payments.stream()
                .filter(paymentFunctions::isValidAmount)
                .toList();
```

Breaking it down:

```text
payments
→ source

stream()
→ creates stream

filter(...)
→ intermediate operation

toList()
→ terminal operation
```

---

# 3. Intermediate Operations

Intermediate operations normally return another `Stream`.

Examples:

```text
filter()
map()
sorted()
distinct()
limit()
skip()
```

Example:

```java
payments.stream()
        .filter(...)
        .sorted(...)
        .map(...);
```

They build a pipeline.

They do not normally produce the final result themselves.

---

# 4. Terminal Operations

Terminal operations consume the stream and trigger execution.

Examples:

```text
toList()
forEach()
count()
findFirst()
anyMatch()
reduce()
```

Examples used today:

```java
.toList();
```

and:

```java
.forEach(System.out::println);
```

Mental model:

```text
Intermediate
→ build pipeline

Terminal
→ execute pipeline
```

---

# 5. Lazy Evaluation

One of the most important Stream concepts is laziness.

Intermediate operations such as:

```java
filter()
map()
```

are lazy.

Example:

```java
payments.stream()
        .filter(payment -> {
            System.out.println(
                    "Filtering " + payment.getAmount()
            );

            return true;
        });
```

Nothing is printed.

Why?

There is no terminal operation.

The pipeline has been defined but not executed.

When we add:

```java
.toList();
```

the pipeline runs:

```java
payments.stream()
        .filter(payment -> {
            System.out.println(
                    "Filtering " + payment.getAmount()
            );

            return true;
        })
        .toList();
```

Now:

```text
Filtering 100.00
Filtering 500.00
...
```

is printed.

Mental model:

```text
stream()
   ↓
filter()
   ↓
pipeline created

NO terminal operation
→ no processing
```

versus:

```text
stream()
   ↓
filter()
   ↓
toList()
   ↓
pipeline executes
```

---

# 6. `filter()`

`filter()` selects elements.

It receives a:

```java
Predicate<T>
```

Recall from Day 16:

```text
Predicate<T>
T → boolean
```

Example:

```java
List<Payment> positivePayments =
        payments.stream()
                .filter(paymentFunctions::isValidAmount)
                .toList();
```

Only payments for which:

```java
paymentFunctions.isValidAmount(payment)
```

returns:

```text
true
```

remain in the stream.

Mental model:

```text
filter
→ selection

true
→ keep

false
→ discard
```

---

# 7. Day 16 → Day 17 Connection

Day 16 introduced:

```text
Predicate
Function
Consumer
Supplier
```

Day 17 shows where these are heavily used.

### `filter()`

```java
filter(Predicate<T>)
```

### `map()`

```java
map(Function<T,R>)
```

### `forEach()`

```java
forEach(Consumer<T>)
```

So:

```text
Day 16
Functional Interfaces
        ↓
Day 17
Stream API
```

---

# 8. Method Reference With `filter()`

We used:

```java
.filter(paymentFunctions::isValidAmount)
```

This is equivalent to:

```java
.filter(
    payment ->
        paymentFunctions.isValidAmount(payment)
)
```

Why does it work?

`filter()` requires:

```text
Payment → boolean
```

Our method is:

```java
public boolean isValidAmount(Payment payment)
```

which has exactly the same shape:

```text
Payment → boolean
```

Therefore Java can use it as a `Predicate<Payment>`.

---

# 9. `object::method` vs `Class::method`

This:

```java
paymentFunctions::isValidAmount
```

means:

> Call `isValidAmount()` on this specific `paymentFunctions` object.

Equivalent:

```java
payment ->
    paymentFunctions.isValidAmount(payment)
```

But:

```java
PaymentFunctions::isValidAmount
```

would represent an unbound instance method.

Conceptually:

```text
(PaymentFunctions, Payment)
→ boolean
```

That does not match:

```text
Predicate<Payment>
Payment → boolean
```

---

# 10. Example of `Class::instanceMethod`

We also used:

```java
Payment::getAmount
```

This works because:

```java
Payment::getAmount
```

is equivalent to:

```java
payment -> payment.getAmount()
```

Shape:

```text
Payment → BigDecimal
```

which matches:

```java
Function<Payment, BigDecimal>
```

---

# 11. `map()`

`map()` transforms elements.

Example:

```java
List<BigDecimal> amounts =
        payments.stream()
                .map(Payment::getAmount)
                .toList();
```

Before:

```text
Stream<Payment>
```

After:

```text
Stream<BigDecimal>
```

Pipeline:

```text
Payment
   ↓
getAmount()
   ↓
BigDecimal
```

Mental model:

```text
filter
→ select

map
→ transform
```

---

# 12. `filter()` vs `map()`

Example:

```java
.filter(payment ->
        payment.getAmount()
               .compareTo(BigDecimal.ZERO) > 0)
```

asks:

```text
Should this Payment remain?
```

While:

```java
.map(Payment::getAmount)
```

asks:

```text
What should this Payment become?
```

So:

```text
filter
→ selection

map
→ transformation
```

---

# 13. `sorted()`

We sorted payments by amount:

```java
List<Payment> sortedByAmount =
        payments.stream()
                .sorted(
                    Comparator.comparing(
                        Payment::getAmount
                    )
                )
                .toList();
```

This combines:

```text
Stream API
+
Comparator
```

which connects back to our earlier ordering studies.

Mental model:

```text
Payment::getAmount
→ key used for comparison
```

Ascending order is the default here.

For descending order:

```java
Comparator.comparing(
        Payment::getAmount
).reversed()
```

---

# 14. `limit()`

We implemented:

```java
List<Payment> firstTwoPositivePayments =
        payments.stream()
                .filter(
                    paymentFunctions::isValidAmount
                )
                .limit(2)
                .toList();
```

Meaning:

```text
payments
   ↓
keep only positive
   ↓
take first 2 matching elements
   ↓
create result
```

The order of operations matters.

This:

```java
.filter(...)
.limit(2)
```

means:

```text
find positive payments
then take two
```

But:

```java
.limit(2)
.filter(...)
```

means:

```text
take first two payments
then check whether they are positive
```

These are not equivalent.

---

# 15. Lazy Evaluation + `limit()`

Because streams are lazy:

```java
payments.stream()
        .filter(...)
        .limit(2)
        .toList();
```

may stop processing once two matching elements have been found.

It does not necessarily need to process the entire source.

This is an example of short-circuiting behavior.

---

# 16. Enum Comparison

During the PIX filtering exercise, we initially used:

```java
p.getMethod()
 .compareTo(PaymentMethod.PIX) > 0
```

This was incorrect for equality.

For enums, `compareTo()` compares their declaration order.

Example:

```java
public enum PaymentMethod {
    PIX,
    CREDIT_CARD
}
```

Conceptually:

```text
PIX
ordinal = 0

CREDIT_CARD
ordinal = 1
```

So:

```java
CREDIT_CARD.compareTo(PIX)
```

returns a positive value because `CREDIT_CARD` comes after `PIX`.

It does NOT mean:

```text
is this PIX?
```

---

# 17. Correct Enum Equality

For enum equality use:

```java
==
```

Example:

```java
List<Payment> pixPayments =
        payments.stream()
                .filter(
                    p ->
                        p.getMethod()
                        == PaymentMethod.PIX
                )
                .toList();
```

Mental model:

```text
Enum equality
→ ==

Enum ordering
→ compareTo()
```

Another benefit:

```java
p.getMethod() == PaymentMethod.PIX
```

does not throw if `getMethod()` is `null`.

It simply evaluates to `false`.

But:

```java
p.getMethod().compareTo(...)
```

would throw a `NullPointerException`.

---

# 18. `toList()`

Example:

```java
List<Payment> result =
        payments.stream()
                .filter(...)
                .toList();
```

`Stream.toList()` returns an unmodifiable list.

So:

```java
result.add(...)
```

is not allowed.

Mental model:

```text
stream pipeline
   ↓
toList()
   ↓
unmodifiable result list
```

---

# 19. Original Collection Is Not Modified

This:

```java
List<Payment> positive =
        payments.stream()
                .filter(...)
                .toList();
```

does not remove elements from:

```java
payments
```

Instead:

```text
payments
→ source

positive
→ result
```

The original collection remains.

---

# 20. Part A Exercises Completed

We created:

```java
List<Payment> payments
```

with payments such as:

```text
PIX          100.00
PIX          500.00
CREDIT_CARD  200.00
CREDIT_CARD  1500.00
PIX          -50.00
```

Then implemented:

### Positive Payments

```java
payments.stream()
        .filter(paymentFunctions::isValidAmount)
        .toList();
```

### PIX Payments

```java
payments.stream()
        .filter(
            p -> p.getMethod()
                 == PaymentMethod.PIX
        )
        .toList();
```

### Amount Extraction

```java
payments.stream()
        .map(Payment::getAmount)
        .toList();
```

### Sorting

```java
payments.stream()
        .sorted(
            Comparator.comparing(
                Payment::getAmount
            )
        )
        .toList();
```

### Lazy Evaluation

Without terminal operation:

```java
payments.stream()
        .filter(payment -> {
            System.out.println(
                    "Filtering "
                    + payment.getAmount()
            );

            return true;
        });
```

Nothing executes.

With:

```java
.toList()
```

the pipeline executes.

### First Two Positive Payments

```java
payments.stream()
        .filter(paymentFunctions::isValidAmount)
        .limit(2)
        .toList();
```

---

# Part B — Spring MVC

# 21. Spring Web

For a REST API, the project uses:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

This provides Spring MVC infrastructure.

---

# 22. `@RestController`

We created:

```java
@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService service;
}
```

`@RestController` tells Spring:

```text
this class handles HTTP requests
+
return values are written
to the HTTP response body
```

Conceptually:

```text
@Controller
+
@ResponseBody
```

---

# 23. `@RequestMapping`

We used:

```java
@RequestMapping("/payments")
```

This establishes the base path:

```text
/payments
```

Every endpoint inside the controller starts from that path.

---

# 24. Constructor Injection

The controller contains:

```java
private final PaymentService service;
```

and uses Lombok:

```java
@AllArgsConstructor
```

Lombok generates conceptually:

```java
public PaymentController(
        PaymentService service) {

    this.service = service;
}
```

Spring sees this constructor and injects the `PaymentService` bean.

Mental model:

```text
Spring
   ↓
finds PaymentController
   ↓
constructor needs PaymentService
   ↓
finds PaymentService bean
   ↓
injects it
```

---

# 25. Controller vs Service

Important architecture rule:

```text
Controller
→ HTTP concerns

Service
→ business/application logic
```

Our controller delegates:

```java
@GetMapping
public List<Payment> getPayments() {
    return service.findAll();
}
```

The controller does not build or filter the collection itself.

---

# 26. `GET /payments`

Endpoint:

```java
@GetMapping
public List<Payment> getPayments() {
    return service.findAll();
}
```

Request:

```text
GET /payments
```

Flow:

```text
HTTP
 ↓
PaymentController
 ↓
PaymentService.findAll()
 ↓
List<Payment>
 ↓
Spring MVC
 ↓
JSON
```

---

# 27. Returning a Typed Collection

Our controller returns:

```java
List<Payment>
```

rather than:

```java
List
```

The type information clearly expresses:

```text
this endpoint returns
a collection of Payment objects
```

Avoid raw collections.

Prefer:

```java
List<Payment>
```

---

# 28. JSON Serialization

When the controller returns:

```java
List<Payment>
```

Spring MVC uses a configured message converter, normally Jackson for JSON.

Conceptually:

```text
List<Payment>
      ↓
Jackson
      ↓
JSON
```

Example result:

```json
[
  {
    "amount": 100.00,
    "method": "PIX"
  },
  {
    "amount": 500.00,
    "method": "PIX"
  }
]
```

---

# 29. `GET /payments/positive`

Controller:

```java
@GetMapping("/positive")
public List<Payment> getPositivePayments() {
    return service.findPositivePayments();
}
```

Service:

```java
public List<Payment> findPositivePayments() {

    return payments.stream()
            .filter(
                paymentFunctions::isValidAmount
            )
            .toList();
}
```

Flow:

```text
GET /payments/positive
        ↓
PaymentController
        ↓
PaymentService
        ↓
stream()
        ↓
filter()
        ↓
toList()
        ↓
List<Payment>
        ↓
JSON
```

This directly connects the Stream lesson to Spring MVC.

---

# 30. Filtering by Payment Type

We added:

```java
public List<Payment> findPaymentsByType(
        PaymentMethod method) {

    return payments.stream()
            .filter(
                p -> p.getMethod() == method
            )
            .toList();
}
```

Important:

The parameter is:

```java
PaymentMethod
```

not:

```java
PaymentStrategy
```

because:

```text
Payment.getMethod()
→ PaymentMethod
```

while:

```text
PaymentStrategy
→ behavior used to process payment
```

These represent different concepts.

---

# 31. `PaymentMethod` vs `PaymentStrategy`

```text
PaymentMethod
→ data / classification

PIX
CREDIT_CARD
```

```text
PaymentStrategy
→ behavior

pixStrategy
cardStrategy
```

So:

```java
p.getMethod() == method
```

makes sense.

But:

```java
p.getMethod() == strategy
```

does not because the types are unrelated.

---

# 32. `@PathVariable`

Controller:

```java
@GetMapping("/type/{method}")
public List<Payment> getPaymentsByType(
        @PathVariable PaymentMethod method) {

    return service.findPaymentsByType(method);
}
```

Example request:

```text
GET /payments/type/PIX
```

Spring extracts:

```text
PIX
```

from the path and converts it into:

```java
PaymentMethod.PIX
```

Then calls:

```java
findPaymentsByType(
    PaymentMethod.PIX
)
```

---

# 33. Why `{method}` Is Required

This would be incorrect:

```java
@GetMapping("/type")
public List<Payment> find(
        @PathVariable PaymentMethod method)
```

because:

```text
/type
```

contains no path variable.

For `@PathVariable`, the route must contain:

```text
{variable}
```

Correct:

```java
@GetMapping("/type/{method}")
```

---

# 34. Path Variable Enum Conversion

These work:

```text
GET /payments/type/PIX

GET /payments/type/CREDIT_CARD
```

because Spring can convert the path string to the corresponding enum constant.

By default, enum conversion is generally case-sensitive.

So:

```text
/payments/type/PIX
```

works while:

```text
/payments/type/pix
```

normally does not without custom conversion.

An unknown value such as `pix` or `BOLETO` fails the conversion, and Spring MVC responds with:

```text
400 Bad Request
```

The service method is never called.

---

# 35. `@PathVariable` vs `@RequestParam`

Path variable:

```text
/payments/type/PIX
```

```java
@PathVariable PaymentMethod method
```

Query parameter:

```text
/payments/type?method=PIX
```

would use:

```java
@RequestParam PaymentMethod method
```

For our implementation we chose:

```text
/payments/type/PIX
```

with:

```java
@PathVariable
```

---

# 36. In-Memory Payments in the Service

For this exercise, `PaymentService` owns an in-memory collection:

```java
private final List<Payment> payments =
        new ArrayList<>();
```

Payments are initialized in the service constructor.

This avoids introducing a database during Day 17.

The focus today is:

```text
Streams
+
Spring MVC
```

not persistence.

---

# 37. Protecting the Internal List

Initial implementation:

```java
public List<Payment> findAll() {
    return payments;
}
```

works, but exposes the service's internal mutable collection.

Another class could do:

```java
service.findAll().clear();
```

and modify the internal service state.

A safer implementation is:

```java
public List<Payment> findAll() {
    return List.copyOf(payments);
}
```

---

# 38. Why `List.copyOf()` Is Better

With:

```java
return payments;
```

the caller gets the actual internal list.

Conceptually:

```text
PaymentService
      ↓
internal payments list
      ↓
caller gets same list
```

The caller can modify its structure.

With:

```java
return List.copyOf(payments);
```

the caller gets an unmodifiable copy.

So operations such as:

```java
result.add(...)
result.remove(...)
result.clear()
```

are not allowed.

This helps preserve encapsulation.

Mental model:

```text
return payments
→ exposes mutable internal structure
```

```text
return List.copyOf(payments)
→ protects internal list structure
```

---

# 39. `List.copyOf()` Is Shallow

Important nuance:

```java
List.copyOf(payments)
```

protects the list structure.

It does NOT create deep copies of the `Payment` objects.

Conceptually:

```text
Original list
   ↓
Payment A
Payment B
Payment C

Copied list
   ↓
same Payment A
same Payment B
same Payment C
```

Therefore:

```java
result.clear();
```

cannot modify the service list.

But if `Payment` is mutable:

```java
result.get(0)
      .setAmount(
          new BigDecimal("9999.00")
      );
```

may still mutate the same `Payment` object referenced internally.

So:

```text
List.copyOf()
→ collection immutability

NOT
→ deep object immutability
```

---

# 40. Final Controller

Conceptually:

```java
@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @GetMapping
    public List<Payment> getPayments() {
        return service.findAll();
    }

    @GetMapping("/positive")
    public List<Payment> getPositivePayments() {
        return service.findPositivePayments();
    }

    @GetMapping("/type/{method}")
    public List<Payment> getPaymentsByType(
            @PathVariable PaymentMethod method) {

        return service.findPaymentsByType(method);
    }
}
```

---

# 41. Final Service Methods

```java
public List<Payment> findAll() {
    return List.copyOf(payments);
}
```

```java
public List<Payment> findPositivePayments() {

    return payments.stream()
            .filter(
                paymentFunctions::isValidAmount
            )
            .toList();
}
```

```java
public List<Payment> findPaymentsByType(
        PaymentMethod method) {

    return payments.stream()
            .filter(
                p -> p.getMethod() == method
            )
            .toList();
}
```

---

# 42. Final Application Architecture

```text
                HTTP
                 ↓
        PaymentController
                 ↓
          PaymentService
          /      |      \
         /       |       \
    findAll   positive   byType
                 ↓
             Streams
                 ↓
          List<Payment>
                 ↓
       Spring MVC / Jackson
                 ↓
               JSON
```

---

# 43. Controller Responsibility

The controller should remain thin.

Good:

```java
@GetMapping("/positive")
public List<Payment> getPositivePayments() {
    return service.findPositivePayments();
}
```

Less desirable:

```java
@GetMapping("/positive")
public List<Payment> getPositivePayments() {

    return payments.stream()
            .filter(...)
            .toList();
}
```

because now the controller owns application logic.

Keep:

```text
Controller
→ HTTP

Service
→ use case / processing
```

---

# 44. Domain Object vs DTO

For Day 17, returning:

```java
List<Payment>
```

is intentionally acceptable.

However:

```text
Domain object
!= necessarily API contract
```

in a production application.

Day 18 introduces:

```text
Domain object
    ↓
DTO mapping
    ↓
API response
```

So do not solve that problem prematurely today.

---

# 45. Port Configuration

If port `8080` is unavailable, Spring Boot can be configured using:

```properties
server.port=8081
```

in:

```text
src/main/resources/application.properties
```

Then:

```text
GET http://localhost:8081/payments
```

---

# 46. Interview Questions

Be able to answer:

1. What is a Java Stream?
2. What is the difference between a Stream and a Collection?
3. What are the three parts of a stream pipeline?
4. What is an intermediate operation?
5. What is a terminal operation?
6. Why are intermediate stream operations lazy?
7. What functional interface does `filter()` receive?
8. What functional interface does `map()` receive?
9. What functional interface does `forEach()` receive?
10. What is the difference between `filter()` and `map()`?
11. What does `limit()` do?
12. Why does operation order matter in a stream pipeline?
13. Why should enums usually be compared with `==`?
14. Why was `compareTo()` incorrect for checking `PIX`?
15. What does `paymentFunctions::isValidAmount` mean?
16. What does `Payment::getAmount` mean?
17. What does `@RestController` do?
18. What does `@RequestMapping` do?
19. What does `@GetMapping` do?
20. How does Spring convert `PIX` in the URL into `PaymentMethod.PIX`?
21. What is the difference between `@PathVariable` and `@RequestParam`?
22. Why keep stream filtering in the service instead of the controller?
23. Why is `List.copyOf(payments)` safer than returning `payments` directly?
24. Does `List.copyOf()` make the objects inside the list immutable?

---

# 47. Senior Interview Answers

## What is a Stream pipeline?

> A Stream pipeline consists of a source, zero or more intermediate operations, and a terminal operation. Intermediate operations such as `filter`, `map`, and `sorted` are lazy and normally execute only when a terminal operation triggers the pipeline.

## Stream vs Collection

> A collection stores data, while a stream represents a pipeline for processing data. Streams are single-use (calling another operation on a consumed stream throws `IllegalStateException`) and do not themselves own the underlying elements.

## `filter()` vs `map()`

> `filter()` selects elements using a `Predicate<T>`, while `map()` transforms elements using a `Function<T,R>`.

## Why are Streams lazy?

> Laziness allows Java to avoid unnecessary work and optimize pipeline execution. For example, a pipeline using `filter()` and `limit(2)` can stop after finding two matching elements rather than processing the entire source.

## What does `paymentFunctions::isValidAmount` mean?

> It is a bound instance method reference. It refers to the `isValidAmount` method on the specific `paymentFunctions` object. Because the method accepts a `Payment` and returns `boolean`, it can be used where a `Predicate<Payment>` is required.

## Why use `==` with enums?

> Enum constants are singleton instances managed by the JVM, so identity comparison using `==` is safe and idiomatic. `compareTo()` represents declaration ordering, not equality intent.

## What does `@RestController` do?

> `@RestController` marks a Spring MVC controller whose handler method return values are written to the HTTP response body. For objects and typed collections, Spring normally uses Jackson through its message converters to serialize them as JSON.

## Why return `List.copyOf(payments)`?

> Returning the internal mutable list exposes the service's collection state to callers. `List.copyOf()` returns an unmodifiable copy, preventing callers from adding, removing, or clearing elements from the service's internal list.

---

# 48. Quick Mental Model

## Streams

```text
Collection
   ↓
stream()
   ↓
filter(Predicate)
   ↓
map(Function)
   ↓
sorted()
   ↓
limit()
   ↓
terminal operation
   ↓
result
```

## Functional Interfaces

```text
filter
→ Predicate<T>
→ T → boolean
```

```text
map
→ Function<T,R>
→ T → R
```

```text
forEach
→ Consumer<T>
→ T → void
```

## Laziness

```text
Intermediate operation
→ lazy

Terminal operation
→ executes pipeline
```

## Enum

```text
Equality
→ ==

Ordering
→ compareTo()
```

## Spring MVC

```text
HTTP request
      ↓
@RestController
      ↓
@GetMapping
      ↓
PaymentService
      ↓
List<Payment>
      ↓
Jackson
      ↓
JSON
```

## Layering

```text
Controller
→ HTTP

Service
→ processing/business logic
```

## Collection Protection

```text
return payments
→ exposes internal mutable list
```

```text
return List.copyOf(payments)
→ protects internal list structure
```

---

# 49. Day 17 Completion Checklist

## Java Streams

- [x] Understand Stream vs Collection
- [x] Understand source
- [x] Understand stream pipeline
- [x] Understand intermediate operations
- [x] Understand terminal operations
- [x] Understand lazy evaluation
- [x] Use `filter()`
- [x] Use `map()`
- [x] Use `sorted()`
- [x] Use `limit()`
- [x] Use `toList()`
- [x] Use `forEach()`
- [x] Connect `Predicate` with `filter`
- [x] Connect `Function` with `map`
- [x] Connect `Consumer` with `forEach`
- [x] Use method references
- [x] Understand enum `==` vs `compareTo()`

## Spring MVC

- [x] Add Spring Web
- [x] Create `@RestController`
- [x] Use `@RequestMapping`
- [x] Use `@GetMapping`
- [x] Use constructor injection
- [x] Return `List<Payment>`
- [x] Create `/payments`
- [x] Create `/payments/positive`
- [x] Create `/payments/type/{method}`
- [x] Use `@PathVariable`
- [x] Understand automatic enum conversion
- [x] Keep stream logic in Service
- [x] Understand JSON serialization
- [x] Protect internal collection with `List.copyOf()`

## Exercises

- [x] Filter positive payments
- [x] Filter PIX payments
- [x] Map payments to amounts
- [x] Sort payments by amount
- [x] Demonstrate lazy evaluation
- [x] Take first two positive payments
- [x] Expose all payments through REST
- [x] Expose positive payments through REST
- [x] Filter REST result by payment method

---

# Day 17 Status

```text
Part A — Streams
✅ COMPLETE

Part B — Spring MVC / REST
✅ COMPLETE

Day 17 implementation
✅ COMPLETE
```

## Next Roadmap Topic — Day 18

```text
Java
→ flatMap
→ reduce
→ groupingBy
→ toMap

Spring
→ map domain objects to API DTOs
```

This builds directly on today's pipeline:

```text
Day 17
Stream<Payment>
        ↓
filter / map
        ↓
List<Payment>

Day 18
advanced collection transformations
        +
domain → DTO mapping
```
