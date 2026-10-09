# [Day 19] Java Optional & Spring Not-Found Flow

## Objective

Today we learned how Java `Optional` models the presence or absence of a value and how that concept fits naturally into a Spring repository/service flow.

The Java focus was:

- `Optional.of()`
- `Optional.ofNullable()`
- `Optional.empty()`
- `Optional.map()`
- `Optional.flatMap()`
- `orElse()`
- `orElseGet()`
- `orElseThrow()`

The Spring/application focus was:

- introduce `PaymentRepository`
- move in-memory storage out of `PaymentService`
- return `Optional<Payment>` from repository lookup
- let the service decide what "not found" means
- throw `PaymentNotFoundException`
- add `GET /payments/{id}`
- preserve the Day 18 DTO mapping architecture

Progression:

```text
Day 17
Controller → Service

Day 18
Controller → Service → Mapper → DTO

Day 19
Controller
    ↓
Service
    ↓
Repository
    ↓
Optional<Payment>
    ↓
orElseThrow()
    ↓
Payment
    ↓
Mapper
    ↓
PaymentResponse
```

---

# Part A — Java Optional

## 1. Why `Optional` Exists

Without `Optional`, a method may return `null`:

```java
Payment findById(long id);
```

The caller then has to remember to check:

```java
Payment payment = findById(10);

if (payment != null) {
    // use payment
}
```

If the caller forgets, this may happen:

```text
NullPointerException
```

With `Optional`:

```java
Optional<Payment> findById(long id);
```

the method signature explicitly says:

```text
Payment may exist
or
Payment may not exist
```

Mental model:

```text
Optional<T>

contains T
or
contains no value
```

---

## 2. `Optional.of()`

Use `Optional.of()` when the value is guaranteed to be non-null.

```java
Optional<String> method =
        Optional.of("PIX");
```

If the value is `null`:

```java
Optional.of(null);
```

the result is:

```text
NullPointerException
```

Rule:

```text
Optional.of(value)
→ value must not be null
```

---

## 3. `Optional.ofNullable()`

Use when the value may be null:

```java
String value = maybeNull();

Optional<String> optional =
        Optional.ofNullable(value);
```

If `value` is not null:

```text
Optional[value]
```

If it is null:

```text
Optional.empty
```

Rule:

```text
of()
→ definitely non-null

ofNullable()
→ value may be null
```

---

## 4. `Optional.empty()`

Represents absence explicitly:

```java
Optional<Payment> payment =
        Optional.empty();
```

This means:

```text
there is no Payment
```

---

## 5. Best Use of Optional

A strong default rule:

```text
Optional
→ good as a return type

Optional fields
→ usually avoid

Optional parameters
→ usually avoid
```

The key use case is:

```java
Optional<Payment> findById(long id);
```

because a lookup may legitimately find nothing.

---

## 6. `map()`

`map()` transforms a value if it exists.

Example:

```java
Optional<Payment> payment = ...;

Optional<BigDecimal> amount =
        payment.map(Payment::getAmount);
```

Type transition:

```text
Optional<Payment>
        ↓
map(Payment::getAmount)
        ↓
Optional<BigDecimal>
```

If a Payment exists:

```text
Payment
→ getAmount()
→ BigDecimal
```

If the Optional is empty:

```text
Optional.empty
→ mapper is not executed
→ Optional.empty
```

---

## 7. Stream `map()` vs Optional `map()`

The concept is the same:

```text
Stream<Payment>
→ map
→ Stream<BigDecimal>
```

versus:

```text
Optional<Payment>
→ map
→ Optional<BigDecimal>
```

Difference:

```text
Stream
→ zero to many values

Optional
→ zero or one value
```

---

## 8. `flatMap()`

`flatMap()` is useful when the mapping function already returns an `Optional`.

Example helper:

```java
private static Optional<String> getPaymentMethod(
        Payment payment) {

    return Optional.ofNullable(
            payment.getMethod()
    ).map(Enum::name);
}
```

Using `map()`:

```java
Optional<Optional<String>> methodWithMap =
        payment.map(Main::getPaymentMethod);
```

Result:

```text
Optional<Optional<String>>
```

Using `flatMap()`:

```java
Optional<String> methodWithFlatMap =
        payment.flatMap(Main::getPaymentMethod);
```

Result:

```text
Optional<String>
```

Mental model:

```text
map
T → R

flatMap
T → Optional<R>
then flatten
```

---

## 9. `map()` vs `flatMap()` Rule

Use `map()` when the mapper returns a normal value:

```text
Payment → BigDecimal
```

Use `flatMap()` when the mapper already returns an Optional:

```text
Payment → Optional<String>
```

This avoids:

```text
Optional<Optional<T>>
```

---

## 10. `orElse()`

`orElse()` provides a default value.

Example:

```java
Optional<Payment> p4 = ...;

PaymentMethod method = p4
        .map(Payment::getMethod)
        .orElse(PaymentMethod.PIX);
```

Type flow:

```text
Optional<Payment>
        ↓
map(Payment::getMethod)
        ↓
Optional<PaymentMethod>
        ↓
orElse(PaymentMethod.PIX)
        ↓
PaymentMethod
```

Important type rule:

```text
Optional<T>.orElse(...)
→ fallback must be T
```

---

## 11. `orElse()` Is Eager

Suppose:

```java
Payment result =
        existingPayment.orElse(
                createFallbackPayment()
        );
```

Even if `existingPayment` already has a value, the fallback expression is evaluated before `orElse()` receives it.

Mental model:

```text
orElse(value)
→ fallback value is evaluated eagerly
```

---

## 12. `orElseGet()`

`orElseGet()` receives a `Supplier<T>`.

```java
Payment result =
        existingPayment.orElseGet(
                Main::createFallbackPayment
        );
```

If the Optional contains a value:

```text
fallback supplier is NOT executed
```

If the Optional is empty:

```text
fallback supplier executes
```

Rule:

```text
orElseGet(supplier)
→ fallback is lazy
```

This matters when the fallback involves:

```text
database call
network call
expensive computation
object creation
```

---

## 13. `orElse()` vs `orElseGet()`

```text
orElse(value)
→ value already evaluated

orElseGet(supplier)
→ compute fallback only if needed
```

---

## 14. `orElseThrow()`

`orElseThrow()` turns absence into an exception.

```java
Optional<Payment> payment =
        Optional.empty();

Payment result = payment.orElseThrow(
        PaymentNotFoundException::new
);
```

With an ID:

```java
long id = 999L;

Payment result = payment.orElseThrow(
        () -> new PaymentNotFoundException(id)
);
```

Result:

```text
Payment not found: 999
```

---

# Part A Quick Mental Model

```text
Optional.of()
→ definitely non-null
```

```text
Optional.ofNullable()
→ value may be null
```

```text
Optional.empty()
→ explicit absence
```

```text
map()
→ transform contained value
```

```text
flatMap()
→ transform when mapper already returns Optional
```

```text
orElse()
→ eager fallback
```

```text
orElseGet()
→ lazy fallback
```

```text
orElseThrow()
→ absence becomes exception
```

---

# Part B — Repository + Not-Found Flow

## 15. Why Introduce a Repository?

Before Day 19, `PaymentService` both stored and processed payments.

That mixed:

```text
data access
+
application/business logic
```

We separated those responsibilities.

```text
PaymentRepository
→ storage/access

PaymentService
→ application/business logic

PaymentController
→ HTTP

PaymentMapper
→ domain → DTO
```

---

## 16. `PaymentRepository`

We introduced:

```java
@Repository
public class PaymentRepository {
}
```

`@Repository` is a Spring stereotype for data-access components.

The repository owns the in-memory storage:

```java
private final Map<Long, Payment> payments =
        new HashMap<>();
```

Conceptually:

```text
1 → Payment(100, PIX)
2 → Payment(500, PIX)
3 → Payment(200, CREDIT_CARD)
4 → Payment(1500, CREDIT_CARD)
5 → Payment(-50, PIX)
```

---

## 17. Why `Map<Long, Payment>`?

Our current `Payment` does not contain an ID.

Instead of expanding the domain just for this lesson, the repository uses IDs as map keys.

This lets us practice lookup semantics without introducing database/JPA complexity.

---

## 18. `findAll()`

Repository responsibility:

```java
public List<Payment> findAll()
```

A clean implementation:

```java
return new ArrayList<>(
        payments.values()
);
```

Because:

```text
Map.values()
→ Collection<Payment>
```

---

## 19. `findById()`

The key method:

```java
public Optional<Payment> findById(long id) {
    return Optional.ofNullable(
            payments.get(id)
    );
}
```

Why `ofNullable()`?

Because:

```java
payments.get(id)
```

returns:

```text
Payment
or
null
```

if the key does not exist.

Flow:

```text
payments.get(id)
        ↓
Payment or null
        ↓
Optional.ofNullable(...)
        ↓
Optional<Payment>
```

---

## 20. Repository Should Not Decide the Error Policy

The repository's responsibility is:

```text
found
or
not found
```

So:

```java
repository.findById(999L)
```

returns:

```text
Optional.empty()
```

The repository should not decide that absence is an application error.

That decision belongs to the service.

---

## 21. `PaymentNotFoundException`

We created:

```java
public class PaymentNotFoundException
        extends RuntimeException {

    public PaymentNotFoundException(Long id) {
        super("Payment not found: " + id);
    }
}
```

This creates a meaningful application-level failure:

```text
Payment not found: 999
```

---

## 22. Service-Level Decision

Repository returns:

```java
Optional<Payment>
```

The service needs:

```java
Payment
```

So:

```java
public Payment findPaymentById(Long id) {
    return paymentRepository.findById(id)
            .orElseThrow(
                    () ->
                        new PaymentNotFoundException(id)
            );
}
```

Flow:

```text
Optional<Payment>
        ↓
orElseThrow(...)
        ↓
Payment
```

---

## 23. Why `orElseThrow()` Belongs in the Service

The repository says:

```text
I found it
or
I didn't
```

The service says:

```text
for this use case,
missing payment means error
```

Another use case could treat absence differently.

For example:

```text
missing
→ use default
```

or:

```text
missing
→ create new object
```

So the repository stays neutral.

---

## 24. Service Delegation

Other service methods now delegate to the repository:

```java
public List<Payment> findAll() {
    return paymentRepository.findAll();
}
```

```java
public List<Payment> findPositivePayments() {
    return paymentRepository.findPositivePayments();
}
```

```java
public List<Payment> findPaymentsByType(
        PaymentMethod method) {

    return paymentRepository
            .findPaymentsByType(method);
}
```

The service no longer owns the in-memory data.

---

## 25. Enum Comparison

When filtering by `PaymentMethod`, prefer:

```java
payment.getMethod() == method
```

instead of comparing enum names as strings.

Mental model:

```text
Enum equality
→ ==
```

---

## 26. Keep Stream Knowledge From Previous Days

Repository filtering can still use Streams:

```java
public List<Payment> findPositivePayments() {

    return payments.values()
            .stream()
            .filter(payment ->
                    payment.getAmount()
                            .compareTo(
                                    BigDecimal.ZERO
                            ) > 0
            )
            .toList();
}
```

and:

```java
public List<Payment> findPaymentsByType(
        PaymentMethod method) {

    return payments.values()
            .stream()
            .filter(payment ->
                    payment.getMethod() == method
            )
            .toList();
}
```

The project keeps evolving cumulatively.

---

## 27. New Endpoint — `GET /payments/{id}`

We added:

```text
GET /payments/{id}
```

Example:

```text
GET /payments/2
```

Controller shape:

```java
@GetMapping("/{id}")
public PaymentResponse getPaymentById(
        @PathVariable Long id) {

    Payment payment =
            service.findPaymentById(id);

    return mapper.toResponse(payment);
}
```

---

## 28. Successful Lookup Flow

```text
GET /payments/2
        ↓
PaymentController
        ↓
PaymentService.findPaymentById(2)
        ↓
PaymentRepository.findById(2)
        ↓
Optional<Payment>
        ↓
orElseThrow()
        ↓
Payment
        ↓
PaymentMapper
        ↓
PaymentResponse
        ↓
JSON
```

---

## 29. Missing Lookup Flow

```text
GET /payments/999
        ↓
PaymentController
        ↓
PaymentService
        ↓
PaymentRepository
        ↓
Optional.empty()
        ↓
orElseThrow()
        ↓
PaymentNotFoundException
```

At this stage, the important lesson is the application flow.

---

## 30. Why No `@ControllerAdvice` Yet?

Today:

```text
absence
→ Optional
→ service
→ meaningful exception
```

A later roadmap lesson will handle:

```text
PaymentNotFoundException
→ @ControllerAdvice
→ HTTP 404
```

We intentionally keep those concerns separate.

---

## 31. Day 18 Design Was Preserved

The controller still returns:

```java
PaymentResponse
```

not:

```java
Payment
```

So Day 19 adds repository and Optional handling without removing the Day 18 DTO boundary.

Current architecture:

```text
HTTP
 ↓
PaymentController
 ↓
PaymentService
 ↓
PaymentRepository
 ↓
Optional<Payment>
 ↓
orElseThrow()
 ↓
Payment
 ↓
PaymentMapper
 ↓
PaymentResponse
 ↓
JSON
```

---

# Interview Questions

## What problem does `Optional` solve?

> `Optional` makes the presence or absence of a return value explicit in the API. It is especially useful for lookup methods where not finding a value is a valid outcome.

## `Optional.of()` vs `ofNullable()`?

> `Optional.of()` requires a non-null value and throws `NullPointerException` if null is passed. `ofNullable()` returns an empty Optional when the value is null.

## `map()` vs `flatMap()` in Optional?

> `map()` is used when the mapping function returns a normal value. `flatMap()` is used when the mapping function already returns an Optional, preventing nested `Optional<Optional<T>>`.

## `orElse()` vs `orElseGet()`?

> `orElse()` evaluates its fallback eagerly, while `orElseGet()` receives a Supplier and evaluates the fallback only if the Optional is empty.

## Why use `orElseThrow()` in the service?

> The repository reports presence or absence with `Optional`. The service applies application semantics and decides that absence for this use case should become a `PaymentNotFoundException`.

## Why should the repository return `Optional<Payment>` instead of null?

> The method signature explicitly communicates that a result may be absent and removes the hidden null contract.

## Why not throw `PaymentNotFoundException` directly in the repository?

> The repository should focus on data access and report whether data exists. The service should decide what absence means for the application use case.

## Why preserve `PaymentResponse` in the controller?

> The DTO keeps the external API contract separated from the internal domain model, even as repository and service behavior evolve.

---

# Senior-Level Notes

## Optional Is Primarily a Return-Type Tool

Useful default:

```text
Optional as return type
→ often good

Optional fields
→ usually avoid

Optional parameters
→ usually avoid
```

Use it when absence is a meaningful possibility.

---

## Avoid `optional.get()`

This:

```java
optional.get();
```

can throw:

```text
NoSuchElementException
```

if the Optional is empty.

Prefer:

```text
map()
flatMap()
orElse()
orElseGet()
orElseThrow()
```

because they explicitly describe absence handling.

---

## Don't Wrap Collections in Optional Without a Good Reason

For:

```java
findAll()
```

prefer:

```java
List<Payment>
```

and return:

```text
[]
```

when no elements exist.

Usually avoid:

```java
Optional<List<Payment>>
```

because an empty list already represents "no results."

---

## Optional and Collections Represent Different Cardinality

```text
Optional<T>
→ zero or one
```

```text
List<T>
→ zero to many
```

---

# Quick Mental Model

```text
Optional.of()
→ definitely present
```

```text
Optional.ofNullable()
→ maybe present
```

```text
Optional.empty()
→ absent
```

```text
map()
Optional<T>
→ Optional<R>
```

```text
flatMap()
Optional<T>
→ mapper returns Optional<R>
→ Optional<R>
```

```text
orElse()
→ eager fallback
```

```text
orElseGet()
→ lazy fallback
```

```text
orElseThrow()
→ absence becomes exception
```

Spring:

```text
Repository
→ Optional<Payment>

Service
→ orElseThrow()

Controller
→ PaymentMapper

API
→ PaymentResponse
```

---

# Day 19 Completion Checklist

## Java Optional

- [x] Understand why `Optional` exists
- [x] Use `Optional.of()`
- [x] Use `Optional.ofNullable()`
- [x] Use `Optional.empty()`
- [x] Use `map()`
- [x] Use `flatMap()`
- [x] Understand nested Optional
- [x] Use `orElse()`
- [x] Use `orElseGet()`
- [x] Observe eager vs lazy fallback evaluation
- [x] Use `orElseThrow()`
- [x] Understand `Optional<T>` vs `List<T>`

## Spring / Architecture

- [x] Create `PaymentRepository`
- [x] Use `@Repository`
- [x] Move in-memory payment storage out of service
- [x] Use `Map<Long, Payment>`
- [x] Implement `findAll()`
- [x] Implement `findById()` returning `Optional<Payment>`
- [x] Create `PaymentNotFoundException`
- [x] Inject repository into service
- [x] Use `orElseThrow()` in service
- [x] Pass missing ID to custom exception
- [x] Add `GET /payments/{id}`
- [x] Preserve DTO mapping
- [x] Keep controller/service/repository responsibilities separated

---

# Day 19 Status

```text
Part A — Java Optional
✅ COMPLETE

Part B — Repository + Not-Found Flow
✅ COMPLETE

Day 19 Implementation
✅ COMPLETE
```

---

# Next Roadmap Topic — Day 20

```text
Java
→ Method references
→ andThen()
→ compose()

Spring
→ Mapper/service composition
```

Natural progression:

```text
Day 19
Optional-based lookup
        ↓
Day 20
functional composition
        ↓
reusable mapping / service transformations
```
