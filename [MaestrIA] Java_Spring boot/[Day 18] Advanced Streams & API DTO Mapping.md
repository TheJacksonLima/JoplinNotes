# [Day 18] Advanced Streams & API DTO Mapping

## Objective

Today we expanded the same Payment application from Days 16 and 17.

### Java
- `reduce()`
- `Collectors.groupingBy()`
- `Collectors.toMap()`
- duplicate-key handling with merge functions
- `flatMap()`

### Spring / API
- separate domain objects from API DTOs
- create `PaymentResponse`
- create `PaymentMapper`
- map `Payment -> PaymentResponse`
- map `List<Payment> -> List<PaymentResponse>`
- refactor REST endpoints to return DTOs instead of domain objects

```text
Day 16
Functional Interfaces
    ↓
Day 17
Basic Stream pipelines + REST
    ↓
Day 18
Advanced Stream operations + DTO mapping
```

---

# Part A — Advanced Stream Operations

## 1. `reduce()`

`reduce()` combines many stream elements into one result.

```text
many values
    ↓
reduce()
    ↓
one value
```

Example:

```java
BigDecimal totalAmount = listPayments.stream()
        .map(Payment::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
```

Pipeline:

```text
List<Payment>
      ↓
stream()
      ↓
map(Payment::getAmount)
      ↓
Stream<BigDecimal>
      ↓
reduce()
      ↓
BigDecimal
```

### Identity value

In:

```java
.reduce(BigDecimal.ZERO, BigDecimal::add)
```

`BigDecimal.ZERO` is the identity.

For addition:

```text
0 + x = x
```

### Accumulator

This:

```java
BigDecimal::add
```

is equivalent to:

```java
(a, b) -> a.add(b)
```

---

## 2. Total Payment Amount

Using:

```text
PIX          100.00
PIX          500.00
CREDIT_CARD  200.00
CREDIT_CARD  1500.00
PIX          -50.00
```

Implementation:

```java
BigDecimal totalAmount = listPayments.stream()
        .map(Payment::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
```

Expected:

```text
2250.00
```

---

## 3. Total Positive Payment Amount

Our implementation:

```java
BigDecimal totalPositiveAmount = listPayments.stream()
        .map(Payment::getAmount)
        .filter(amount -> amount.compareTo(BigDecimal.ZERO) > 0)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
```

Expected:

```text
2300.00
```

An alternative that reuses our existing validation method:

```java
BigDecimal totalPositiveAmount = listPayments.stream()
        .filter(paymentFunctions::isValidAmount)
        .map(Payment::getAmount)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
```

---

# 4. `groupingBy()`

`groupingBy()` groups multiple elements under a classifier key.

Implementation:

```java
Map<PaymentMethod, List<Payment>> groupedPayments =
        listPayments.stream()
                .collect(
                        Collectors.groupingBy(Payment::getMethod)
                );
```

Result type:

```java
Map<PaymentMethod, List<Payment>>
```

Conceptually:

```text
PIX
→ [100, 500, -50]

CREDIT_CARD
→ [200, 1500]
```

The classifier:

```java
Payment::getMethod
```

has the shape:

```text
Payment → PaymentMethod
```

The returned `PaymentMethod` becomes the map key.

---

# 5. `collect()`

Today we used another terminal operation:

```java
collect(...)
```

Compare:

```text
toList()
→ simple list result

collect(...)
→ configurable accumulation
```

Examples:

```java
collect(Collectors.groupingBy(...))
```

and:

```java
collect(Collectors.toMap(...))
```

---

# 6. `toMap()`

Basic form:

```java
Collectors.toMap(
        keyMapper,
        valueMapper
)
```

For our domain:

```java
Collectors.toMap(
        Payment::getMethod,
        Payment::getAmount
)
```

But the data contains duplicate payment methods:

```text
PIX → 100
PIX → 500
PIX → -50
```

A map can only have one value per key.

Without a merge function:

```java
Map<PaymentMethod, BigDecimal> amountPerPayment =
        listPayments.stream()
                .collect(
                        Collectors.toMap(
                                Payment::getMethod,
                                Payment::getAmount
                        )
                );
```

the stream fails with a duplicate-key error.

Typical result:

```text
IllegalStateException: Duplicate key
```

---

# 7. Merge Function

We fixed it with:

```java
Map<PaymentMethod, BigDecimal> amountPerPayment =
        listPayments.stream()
                .collect(
                        Collectors.toMap(
                                Payment::getMethod,
                                Payment::getAmount,
                                BigDecimal::add
                        )
                );
```

The third argument is the merge function.

Conceptually:

```text
PIX → 100
PIX → 500
100 + 500 → 600

PIX → -50
600 + -50 → 550
```

Final:

```text
PIX          → 550.00
CREDIT_CARD  → 1700.00
```

---

# 8. `groupingBy()` vs `toMap()`

```text
groupingBy()
→ one key can naturally contain many elements
```

Example:

```text
PIX
→ [Payment, Payment, Payment]
```

```text
toMap()
→ one mapped value per key
→ duplicate key requires merge rule
```

Example:

```text
PIX
→ 550.00
```

---

# 9. `flatMap()`

`flatMap()` flattens nested collections/streams.

Starting type:

```java
List<List<Payment>>
```

Target:

```java
List<Payment>
```

Example:

```java
List<List<Payment>> batches = List.of(batch1, batch2);

List<Payment> allBatch =
        batches.stream()
                .flatMap(List::stream)
                .toList();
```

Type transition:

```text
Stream<List<Payment>>
        ↓
flatMap(List::stream)
        ↓
Stream<Payment>
        ↓
toList()
        ↓
List<Payment>
```

---

# 10. `map()` vs `flatMap()`

With:

```java
List<List<Payment>> batches;
```

Using:

```java
batches.stream()
       .map(List::stream)
```

produces:

```text
Stream<Stream<Payment>>
```

Using:

```java
batches.stream()
       .flatMap(List::stream)
```

produces:

```text
Stream<Payment>
```

Mental model:

```text
map
T → R

flatMap
T → Stream<R>
then flatten
```

---

# Part A Quick Mental Model

```text
reduce()
many → one
```

```text
groupingBy()
one key → many objects
```

```text
toMap()
one key → one mapped value
duplicate key → merge rule
```

```text
flatMap()
nested → flat
```

---

# Part B — Domain → DTO Mapping

## 11. Why Not Return Domain Objects Directly?

In Day 17, the controller returned:

```java
@GetMapping
public List<Payment> getPayments() {
    return service.findAll();
}
```

That directly couples the API contract to the internal domain model.

```text
Payment
    ↓
Controller
    ↓
Jackson
    ↓
JSON
```

If `Payment` changes internally, the API may change unintentionally.

---

# 12. Domain Object vs DTO

```text
Payment
→ internal/domain representation

PaymentResponse
→ external/API representation
```

DTO means:

```text
Data Transfer Object
```

Its purpose is to represent data crossing a boundary.

For us:

```text
Spring application
    ↓
HTTP API
    ↓
client
```

---

# 13. Why DTOs Matter

A DTO lets us control what the API exposes.

Suppose `Payment` later gets:

```java
private PaymentStatus status;
private String internalTransactionId;
```

Returning `Payment` directly could expose those fields.

A DTO can continue exposing only:

```java
amount
method
```

Benefits:

- protects internal fields
- reduces coupling
- gives explicit API contracts
- lets domain and API evolve independently
- supports field renaming/transformation

---

# 14. `PaymentResponse`

Our Day 18 DTO:

```java
@Data
@AllArgsConstructor
public class PaymentResponse {

    private BigDecimal amount;
    private String method;
}
```

Important difference:

Domain:

```java
PaymentMethod method;
```

DTO:

```java
String method;
```

Mapping:

```text
PaymentMethod.PIX
        ↓
.name()
        ↓
"PIX"
```

So the DTO is not just a blind copy of the domain.

---

# 15. Why Not Use a Record Yet?

A modern version could be:

```java
record PaymentResponse(
        BigDecimal amount,
        String method
) {}
```

But records are scheduled for Day 21.

For Day 18 we intentionally keep a normal class so the lesson remains focused on DTO mapping.

---

# 16. `PaymentMapper`

We created:

```java
@Component
public class PaymentMapper {
}
```

Its responsibility is:

```text
Payment
→ PaymentResponse
```

Implementation:

```java
public PaymentResponse toResponse(Payment payment) {
    return new PaymentResponse(
            payment.getAmount(),
            payment.getMethod().name()
    );
}
```

This maps:

```text
Payment.amount
→ PaymentResponse.amount

Payment.method
→ PaymentResponse.method
```

---

# 17. Why a Mapper?

Without a mapper, every controller endpoint could duplicate:

```java
new PaymentResponse(
        payment.getAmount(),
        payment.getMethod().name()
)
```

The mapper centralizes that transformation.

```text
PaymentMapper
→ owns mapping responsibility
```

Benefits:

- no duplicated conversion code
- clear responsibility
- easier API changes
- domain stays independent from DTOs

---

# 18. Mapping Collections

Our mapper also converts:

```text
List<Payment>
```

into:

```text
List<PaymentResponse>
```

Implementation:

```java
public List<PaymentResponse> toResponseList(
        List<Payment> payments) {

    return payments.stream()
            .map(this::toResponse)
            .toList();
}
```

Type transition:

```text
List<Payment>
      ↓
stream()
      ↓
Stream<Payment>
      ↓
map(this::toResponse)
      ↓
Stream<PaymentResponse>
      ↓
toList()
      ↓
List<PaymentResponse>
```

---

# 19. Why `this::toResponse` Works

Method:

```java
PaymentResponse toResponse(Payment payment)
```

has the shape:

```text
Payment → PaymentResponse
```

`map()` expects:

```text
Function<T,R>
```

Therefore:

```text
Function<Payment, PaymentResponse>
```

matches perfectly.

This:

```java
.map(this::toResponse)
```

is equivalent to:

```java
.map(payment -> this.toResponse(payment))
```

Connection:

```text
Day 16
Function<T,R>
    ↓
Day 17
map()
    ↓
Day 18
DTO mapping
```

---

# 20. Service Remains Domain-Oriented

We kept:

```java
public List<Payment> findAll()
```

instead of changing the service to return:

```java
List<PaymentResponse>
```

Reason:

```text
PaymentService
→ domain/application logic

PaymentMapper
→ representation transformation

PaymentController
→ HTTP/API
```

The service should not need to know about REST DTOs.

---

# 21. Controller Before Day 18

```java
@GetMapping
public List<Payment> getPayments() {
    return service.findAll();
}
```

The domain object was directly exposed.

---

# 22. Controller After Day 18

```java
@GetMapping
public List<PaymentResponse> getPayments(){

    List<Payment> payments = service.findAll();

    return mapper.toResponseList(payments);
}
```

Flow:

```text
GET /payments
      ↓
PaymentController
      ↓
PaymentService.findAll()
      ↓
List<Payment>
      ↓
PaymentMapper.toResponseList()
      ↓
List<PaymentResponse>
      ↓
Jackson
      ↓
JSON
```

---

# 23. Positive Payments Endpoint

```java
@GetMapping("/positive")
public List<PaymentResponse> getPositivePayments(){

    List<Payment> payments =
            service.findPositivePayments();

    return mapper.toResponseList(payments);
}
```

Responsibility:

```text
filtering
→ PaymentService

mapping
→ PaymentMapper

HTTP
→ PaymentController
```

---

# 24. Payment Type Endpoint

```java
@GetMapping("/type/{method}")
public List<PaymentResponse> getPaymentsByType(
        @PathVariable PaymentMethod method){

    List<Payment> payments =
            service.findPaymentsByType(method);

    return mapper.toResponseList(payments);
}
```

Flow:

```text
GET /payments/type/PIX
        ↓
@PathVariable
        ↓
PaymentMethod.PIX
        ↓
PaymentService
        ↓
List<Payment>
        ↓
PaymentMapper
        ↓
List<PaymentResponse>
```

---

# 25. Why Not Map Directly in the Controller?

Technically this works:

```java
return service.findAll()
        .stream()
        .map(payment ->
                new PaymentResponse(
                        payment.getAmount(),
                        payment.getMethod().name()
                )
        )
        .toList();
```

But the conversion would be duplicated across endpoints.

Prefer a dedicated mapper:

```java
mapper.toResponseList(...)
```

---

# 26. Why Not Put `toResponse()` Inside `Payment`?

Avoid:

```java
payment.toResponse();
```

because then the domain object knows about the API DTO.

Prefer:

```text
Payment
does not know PaymentResponse

PaymentMapper
knows both
```

This keeps the domain less coupled to transport concerns.

---

# 27. DTOs Can Hide or Transform Fields

Domain might contain:

```java
UUID id;
BigDecimal amount;
PaymentMethod method;
String fraudToken;
```

DTO could expose only:

```java
BigDecimal amount;
String method;
```

DTOs may:

- hide fields
- rename fields
- transform enum values
- combine fields
- expose a smaller contract
- format values differently

---

# 28. Day 18 Architecture

```text
                HTTP
                 ↓
        PaymentController
          ↓             ↓
 PaymentService    PaymentMapper
      ↓                 ↑
 List<Payment>           │
      └──────────────────┘
                 ↓
      List<PaymentResponse>
                 ↓
               Jackson
                 ↓
                JSON
```

---

# Interview Questions

## What does `reduce()` do?

> `reduce()` combines stream elements into a single result using an accumulator. For example, a stream of payment amounts can be reduced into one total amount.

## What is the identity value in `reduce()`?

> The identity is the initial neutral value for the reduction. For addition, zero is the natural identity because `0 + x = x`.

## What does `groupingBy()` return?

> `groupingBy()` groups elements by a classifier. In our example, grouping payments by method returns `Map<PaymentMethod, List<Payment>>`.

## What happens if `toMap()` receives duplicate keys?

> Without a merge function, duplicate keys normally cause an `IllegalStateException`. A merge function defines how values with the same key should be combined.

## `groupingBy()` vs `toMap()`?

> `groupingBy()` naturally groups multiple elements under one key, while `toMap()` creates one mapped value per key and requires a merge rule when keys repeat.

## `map()` vs `flatMap()`?

> `map()` transforms each element into another value. `flatMap()` transforms each element into a stream and flattens the nested streams into one stream.

## Why use DTOs instead of returning domain objects?

> DTOs separate the external API contract from the internal domain model. This reduces coupling, prevents accidental exposure of internal fields, and lets both representations evolve independently.

## Why use a mapper?

> A mapper centralizes transformation logic between domain objects and DTOs, avoids duplicated conversion code, and keeps domain objects independent from API representations.

## Why does `map(this::toResponse)` work?

> `toResponse(Payment)` has the shape `Payment -> PaymentResponse`, which matches `Function<Payment, PaymentResponse>`, the type required by `Stream.map()`.

## Why keep `PaymentService` returning `Payment`?

> The service stays focused on domain/application logic. DTO conversion belongs at the API boundary, so the service does not depend on transport representations.

---

# Senior-Level Notes

## `reduce()` vs `collect()`

```text
reduce
→ combine values into one result
```

Example:

```text
payment amounts
→ total
```

```text
collect
→ accumulate elements into a structured result
```

Example:

```text
payments
→ Map<PaymentMethod, List<Payment>>
```

## DTO does not mean "copy every field"

The DTO should represent the API contract, not mirror the domain mechanically.

## Mapping belongs at a boundary

Current design:

```text
Service
→ domain

Mapper
→ transformation

Controller
→ HTTP
```

---

# Quick Mental Model

```text
reduce()
many → one
```

```text
groupingBy()
T
 ↓ classifier
K
 ↓
Map<K, List<T>>
```

```text
toMap()
T
 ↓
key mapper
+
value mapper
+
merge function if needed
 ↓
Map<K,V>
```

```text
flatMap()
nested → flat
```

```text
Payment
    ↓
PaymentMapper
    ↓
PaymentResponse
    ↓
JSON
```

---

# Day 18 Completion Checklist

## Java

- [x] Understand `reduce()`
- [x] Use identity value
- [x] Use accumulator
- [x] Calculate total payment amount
- [x] Calculate total positive amount
- [x] Understand `collect()`
- [x] Use `groupingBy()`
- [x] Use `toMap()`
- [x] Reproduce duplicate-key problem
- [x] Fix duplicate keys with merge function
- [x] Understand `flatMap()`
- [x] Understand `map()` vs `flatMap()`
- [x] Flatten `List<List<Payment>>`

## Spring / API

- [x] Create `PaymentResponse`
- [x] Keep DTO separate from domain package
- [x] Create `PaymentMapper`
- [x] Make mapper a Spring `@Component`
- [x] Map `Payment -> PaymentResponse`
- [x] Map `List<Payment> -> List<PaymentResponse>`
- [x] Inject mapper into controller
- [x] `GET /payments` returns DTOs
- [x] `GET /payments/positive` returns DTOs
- [x] `GET /payments/type/{method}` returns DTOs
- [x] Keep `PaymentService` domain-oriented
- [x] Understand why DTO != domain object
- [x] Understand mapper responsibility

---

# Day 18 Status

```text
Part A — Advanced Streams
✅ COMPLETE

Part B — DTO Mapping / API Boundary
✅ COMPLETE

Day 18 Implementation
✅ COMPLETE
```

---

# Next Roadmap Topic — Day 19

```text
Java
→ Optional creation
→ map
→ flatMap
→ orElse
→ orElseGet
→ orElseThrow

Spring
→ Service/repository not-found handling
```

Natural progression:

```text
Day 18
Payment → DTO
        ↓
Day 19
find something that may not exist
        ↓
Optional<T>
        ↓
clean not-found handling
```
