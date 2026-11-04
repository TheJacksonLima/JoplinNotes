# [Day 21] Java Records + Jackson DTOs

## Objective

Day 21 connected two related concepts:

```text
Part A — Java
Records as immutable data carriers

Part B — Spring
Request/response DTO serialization with Jackson
```

The main goal was to evolve the existing Payment application without changing the external API contract.

The key progression was:

```text
Before
PaymentResponse class
        ↓
manual/Lombok DTO

Day 21
PaymentResponse record
        ↓
Jackson
        ↓
JSON
```

And for incoming requests:

```text
JSON
 ↓
Jackson
 ↓
PaymentRequest record
 ↓
PaymentMapper
 ↓
Payment
```

---

# Part A — Java Records

## 1. Why Records Exist

A traditional DTO often needs:

```text
fields
constructor
getters
equals()
hashCode()
toString()
```

For example:

```java
public class PaymentResponse {

    private final BigDecimal amount;
    private final String method;

    public PaymentResponse(
            BigDecimal amount,
            String method) {

        this.amount = amount;
        this.method = method;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }
}
```

A record expresses the same data carrier much more directly:

```java
public record PaymentResponse(
        BigDecimal amount,
        String method
) {}
```

A record is a Java language feature designed for classes whose main purpose is to carry a fixed set of values.

---

## 2. Basic Record Syntax

General form:

```java
public record Name(
        Type component1,
        Type component2
) {}
```

Example:

```java
public record PaymentResponse(
        BigDecimal amount,
        String method
) {}
```

The values inside the parentheses are called:

```text
record components
```

For `PaymentResponse`:

```text
amount
method
```

---

## 3. What Java Generates

For:

```java
public record PaymentResponse(
        BigDecimal amount,
        String method
) {}
```

Java generates behavior equivalent to:

```text
private final fields
canonical constructor
component accessors
equals()
hashCode()
toString()
```

The generated accessors are:

```java
response.amount();
response.method();
```

not:

```java
response.getAmount();
response.getMethod();
```

That distinction is important.

---

## 4. Creating a Record

Records are instantiated normally with `new`:

```java
PaymentResponse response =
        new PaymentResponse(
                new BigDecimal("100.00"),
                "PIX"
        );
```

The generated canonical constructor receives all record components in declaration order.

---

## 5. Record Accessors

Given:

```java
PaymentResponse response =
        new PaymentResponse(
                new BigDecimal("100.00"),
                "PIX"
        );
```

use:

```java
System.out.println(response.amount());
System.out.println(response.method());
```

Expected output:

```text
100.00
PIX
```

---

## 6. Equality

Records generate value-oriented `equals()` and `hashCode()` based on their components.

Example:

```java
PaymentSummary a =
        new PaymentSummary(
                new BigDecimal("250.00"),
                PaymentMethod.PIX
        );

PaymentSummary b =
        new PaymentSummary(
                new BigDecimal("250.00"),
                PaymentMethod.PIX
        );
```

Then:

```java
a.equals(b)
```

is expected to be:

```text
true
```

and their hash codes must also be equal.

Important rule:

```text
equals() == true
→ same hashCode() must be true
```

But:

```text
same hashCode()
→ does NOT prove equals() is true
```

Different objects can have the same hash code because hash collisions are possible.

---

## 7. Equality vs Identity

Two records can be equal without being the same object.

Example:

```java
a.equals(b)  // true
a == b       // false
```

Meaning:

```text
equals()
→ logical/value equality

==
→ object identity
```

So two distinct record instances can contain equal values.

---

## 8. Records and Immutability

Record components are final.

You cannot reassign:

```java
summary.amount = new BigDecimal("1000");
```

and records do not generate setters.

This gives the record immutable component references.

However, that does not automatically mean deep immutability.

---

## 9. Shallow Immutability

Consider:

```java
public record PaymentBatch(
        List<Payment> payments
) {}
```

If we do:

```java
List<Payment> list =
        new ArrayList<>();

PaymentBatch batch =
        new PaymentBatch(list);
```

then without a defensive copy:

```text
list
   └─────────────┐
                 ↓
           same ArrayList
                 ↑
batch.payments() ┘
```

Both references point to the same list object.

So:

```java
list.add(payment);
```

does not call the record constructor again.

It mutates the existing `ArrayList`.

Because the record contains a reference to that same list, the mutation is visible through:

```java
batch.payments();
```

---

## 10. Defensive Copy

To protect the list structure:

```java
public record PaymentBatch(
        List<Payment> payments
) {
    public PaymentBatch {
        payments = List.copyOf(payments);
    }
}
```

Now conceptually:

```text
original list
    ↓
ArrayList A

batch.payments()
    ↓
unmodifiable List B
```

The record now stores a different list.

Changing the original list does not change the list stored in the record.

Important:

```text
final reference
≠ immutable object

record
≠ automatically deep immutable
```

Even with `List.copyOf()`, the elements inside the list may still be mutable.

---

## 11. Compact Constructor

Records support a compact canonical constructor.

Example:

```java
public record PaymentSummary(
        BigDecimal amount,
        PaymentMethod method
) {

    public PaymentSummary {

        if (amount == null) {
            throw new IllegalArgumentException(
                    "Amount cannot be null"
            );
        }

        if (method == null) {
            throw new IllegalArgumentException(
                    "Method cannot be null"
            );
        }
    }
}
```

The compact constructor is useful for:

```text
validation
normalization
defensive copies
```

You do not manually assign:

```java
this.amount = amount;
```

inside a normal compact constructor.

---

## 12. Records Are Final

A record cannot be extended:

```text
record
→ implicitly final
```

A record also extends:

```java
java.lang.Record
```

and cannot extend another custom class.

However, it can implement interfaces.

---

## 13. Records Can Have Methods

A record can define normal methods:

```java
public record PaymentResponse(
        BigDecimal amount,
        String method
) {

    public String display() {
        return method + " - " + amount;
    }
}
```

Records are still classes; they simply have special semantics and restrictions.

---

## 14. Record vs Lombok

Records are a Java language feature.

Lombok is an annotation-processing library.

Conceptually:

```text
Lombok
→ generates boilerplate through annotations

record
→ language-level data carrier
```

For a simple record DTO, annotations such as:

```java
@Data
@AllArgsConstructor
```

are unnecessary.

---

## 15. Record vs Normal Class

A record is a strong fit when the type represents:

```text
API DTO
query result
event payload
command/request data
value-like data carrier
```

A traditional class may be a better fit when the object needs:

```text
complex mutable lifecycle
inheritance
encapsulated state transitions
identity-oriented behavior
```

Records should not replace every class.

---

## 16. Why `PaymentResponse` Is a Good Record

The current architecture separates:

```text
Payment
→ domain model

PaymentResponse
→ API response DTO
```

The DTO exists primarily to carry:

```text
amount
method
```

So it is an excellent candidate for:

```java
public record PaymentResponse(
        BigDecimal amount,
        String method
) {}
```

The domain `Payment` was intentionally left unchanged.

---

## 17. Existing Mapper Remains Compatible

The existing mapper can still create the record:

```java
return new PaymentResponse(
        extractAmount.apply(payment),
        methodNameExtractor.apply(payment)
);
```

This continues to work because the record automatically has the canonical constructor:

```text
PaymentResponse(BigDecimal, String)
```

These method references also remain valid:

```java
Payment::getAmount
Payment::getMethod
```

because they operate on `Payment`, not on `PaymentResponse`.

Only code reading from `PaymentResponse` changes from:

```java
response.getAmount();
```

to:

```java
response.amount();
```

---

# Part B — Jackson + Spring MVC

## 18. Serialization

Serialization means:

```text
Java object
→ JSON
```

Example:

```text
PaymentResponse
→ JSON response
```

If the controller returns:

```java
new PaymentResponse(
        new BigDecimal("500.00"),
        "PIX"
)
```

Jackson can produce:

```json
{
  "amount": 500.00,
  "method": "PIX"
}
```

---

## 19. Deserialization

Deserialization is the opposite:

```text
JSON
→ Java object
```

Example:

```json
{
  "amount": 250.00,
  "method": "PIX"
}
```

becomes:

```text
PaymentRequest
```

with:

```text
amount = 250.00
method = PaymentMethod.PIX
```

---

## 20. Spring MVC + Jackson Flow

When a Spring MVC controller returns a Java object:

```text
Controller
    ↓
Spring MVC
    ↓
HTTP message conversion
    ↓
Jackson
    ↓
JSON
```

The controller does not need to manually convert the object to a JSON string.

Avoid:

```java
return "{\"amount\":100,\"method\":\"PIX\"}";
```

Prefer:

```java
return new PaymentResponse(...);
```

and let Jackson serialize it.

---

## 21. Why Records Work With Jackson

A response record:

```java
public record PaymentResponse(
        BigDecimal amount,
        String method
) {}
```

contains explicit components:

```text
amount
method
```

Jackson can use those components as JSON properties.

So:

```text
record component
amount
```

maps naturally to:

```json
"amount"
```

and:

```text
record component
method
```

maps naturally to:

```json
"method"
```

No setters are required for serialization.

---

## 22. Existing GET Response

An existing endpoint such as:

```java
@GetMapping("/{id}")
public PaymentResponse getPaymentById(
        @PathVariable Long id) {

    Payment payment =
            service.findPaymentById(id);

    return mapper.toResponse(payment);
}
```

now returns a record.

The flow is:

```text
GET /payments/{id}
        ↓
PaymentController
        ↓
PaymentService
        ↓
Payment
        ↓
PaymentMapper
        ↓
PaymentResponse record
        ↓
Jackson
        ↓
JSON
```

The API contract remains conceptually unchanged.

---

## 23. Collection Serialization

The controller can also return:

```java
List<PaymentResponse>
```

The mapper already contains:

```java
return payments.stream()
        .map(this::toResponse)
        .toList();
```

Jackson serializes:

```text
List<PaymentResponse>
```

as a JSON array:

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

## 24. Request Record

For incoming JSON, Day 21 introduced:

```java
package org.jfl.dto;

import org.jfl.domain.PaymentMethod;
import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod method
) {}
```

This is the API request model.

---

## 25. `@RequestBody`

Spring uses:

```java
@RequestBody
```

to indicate that the HTTP request body should be converted into the method parameter.

Example:

```java
@PostMapping("/preview")
public PaymentResponse preview(
        @RequestBody PaymentRequest paymentRequest) {
    ...
}
```

The flow is:

```text
HTTP request body
        ↓
@RequestBody
        ↓
Jackson
        ↓
PaymentRequest
```

---

## 26. Request Record Does Not Need Setters

A record has a canonical constructor.

So Jackson can conceptually do:

```text
read JSON values
→ call canonical constructor
→ create PaymentRequest
```

There is no need for:

```java
setAmount()
setMethod()
```

This is a key advantage of records for immutable request DTOs.

---

## 27. Reading Request Components

Because `PaymentRequest` is a record:

```java
paymentRequest.amount();
paymentRequest.method();
```

not:

```java
paymentRequest.getAmount();
paymentRequest.getMethod();
```

---

## 28. Request DTO → Domain Mapping

The API DTO should not become the domain model.

The intended boundary is:

```text
PaymentRequest
    ↓
PaymentMapper
    ↓
Payment
```

The mapper should provide something like:

```java
public Payment toDomain(
        PaymentRequest request) {

    return new Payment(
            request.amount(),
            request.method()
    );
}
```

This keeps:

```text
API model
!=
domain model
```

and prevents the controller from exposing or accepting the internal domain type directly.

---

## 29. POST `/payments/preview`

The Day 21 exercise added:

```java
@PostMapping("/preview")
public PaymentResponse preview(
        @RequestBody PaymentRequest paymentRequest) {

    Payment payment =
            mapper.toDomain(paymentRequest);

    return mapper.toResponse(payment);
}
```

This endpoint intentionally does not persist anything.

Its purpose is to demonstrate the entire conversion pipeline.

---

## 30. Full POST Flow

```text
CLIENT JSON

{
  "amount": 250.00,
  "method": "PIX"
}

        ↓
Spring MVC
        ↓
Jackson deserialization
        ↓
PaymentRequest record
        ↓
PaymentMapper.toDomain()
        ↓
Payment
        ↓
PaymentMapper.toResponse()
        ↓
PaymentResponse record
        ↓
Jackson serialization
        ↓
CLIENT JSON
```

Expected response:

```json
{
  "amount": 250.00,
  "method": "PIX"
}
```

---

## 31. Enum Deserialization

`PaymentRequest` uses:

```java
PaymentMethod method
```

If the enum contains:

```text
PIX
CREDIT_CARD
```

then JSON such as:

```json
{
  "amount": 900.00,
  "method": "CREDIT_CARD"
}
```

can be converted to:

```text
PaymentMethod.CREDIT_CARD
```

An unknown value such as:

```json
{
  "amount": 250.00,
  "method": "BITCOIN"
}
```

cannot be mapped to a valid `PaymentMethod`.

That is a deserialization failure.

---

## 32. Jackson Is Not Validation

Jackson's job is:

```text
JSON ↔ Java conversion
```

It does not guarantee that the data is valid according to business rules.

For example:

```json
{
  "amount": -500,
  "method": "PIX"
}
```

may still deserialize successfully.

That does not mean the payment is valid.

Validation is a separate concern and was intentionally not introduced on Day 21.

---

## 33. Missing Properties

A request such as:

```json
{
  "method": "PIX"
}
```

is useful to observe because it demonstrates that:

```text
deserialization
!=
validation
```

The absence of a business-required field does not automatically mean Jackson itself is responsible for rejecting it.

Validation is handled separately in later roadmap topics.

---

## 34. Compact Constructors Participate in Deserialization

If a request record contains a compact constructor:

```java
public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod method
) {

    public PaymentRequest {
        if (amount == null) {
            throw new IllegalArgumentException(
                    "Amount cannot be null"
            );
        }
    }
}
```

Jackson must construct the record.

So the constructor logic participates in object creation during deserialization.

Conceptually:

```text
JSON
↓
Jackson
↓
record constructor
↓
PaymentRequest
```

If the constructor throws, record construction fails.

---

## 35. `ObjectMapper`

Jackson's central API is:

```java
ObjectMapper
```

Conceptually:

```text
ObjectMapper
→ Java ↔ JSON
```

Manual examples include:

```java
objectMapper.writeValueAsString(response);
```

and:

```java
objectMapper.readValue(
        json,
        PaymentRequest.class
);
```

But normal Spring MVC controllers do not need to call `ObjectMapper` manually.

Spring's HTTP message conversion integrates Jackson automatically.

---

## 36. `@JsonProperty`

Jackson can customize JSON property names using:

```java
@JsonProperty
```

Example:

```java
public record PaymentResponse(
        @JsonProperty("payment_amount")
        BigDecimal amount,
        String method
) {}
```

This could produce:

```json
{
  "payment_amount": 100.00,
  "method": "PIX"
}
```

Day 21 did not require this customization.

The simple mapping:

```text
amount → amount
method → method
```

was intentionally kept.

---

# Debugging From Day 21

## 37. HTTP 405 — Method Not Allowed

A POST request was initially sent to:

```text
POST /payments
```

but the controller's POST mapping was:

```java
@PostMapping("/preview")
```

combined with:

```java
@RequestMapping("/payments")
```

So the correct endpoint was:

```text
POST /payments/preview
```

The `405 Method Not Allowed` response meant:

```text
Spring recognized the path
but the requested HTTP method was not mapped there
```

This is different from:

```text
404 Not Found
```

which would normally mean the requested route itself was not found.

---

# Architecture After Day 21

## 38. GET Flow

```text
PaymentRepository
      ↓
PaymentService
      ↓
Payment
      ↓
PaymentMapper
      ↓
PaymentResponse record
      ↓
Jackson
      ↓
JSON
```

---

## 39. POST Flow

```text
JSON
 ↓
Jackson
 ↓
PaymentRequest record
 ↓
PaymentMapper
 ↓
Payment
 ↓
PaymentMapper
 ↓
PaymentResponse record
 ↓
Jackson
 ↓
JSON
```

---

# Interview Questions

## What is a Java record?

> A record is a Java language construct for classes whose main purpose is to transparently carry a fixed set of values. Java automatically provides component accessors, a canonical constructor, `equals()`, `hashCode()`, and `toString()`.

## Are records immutable?

> Record component references are final, so they cannot be reassigned. However, records provide shallow immutability. If a component refers to a mutable object such as a list, that object can still mutate unless a defensive copy is made.

## What is the difference between a record accessor and a JavaBean getter?

For:

```java
record PaymentResponse(
    BigDecimal amount,
    String method
) {}
```

the accessors are:

```java
amount()
method()
```

not:

```java
getAmount()
getMethod()
```

## Why are records a good fit for DTOs?

> DTOs normally represent a fixed set of values and do not require a mutable lifecycle. Records make that intent explicit and remove boilerplate while providing value-oriented semantics.

## What is serialization?

> Serialization converts a Java object into an external representation such as JSON.

```text
Java → JSON
```

## What is deserialization?

> Deserialization converts external data such as JSON into a Java object.

```text
JSON → Java
```

## Why does Jackson work with records without setters?

> A record declares its complete construction contract through its components and canonical constructor. Jackson can use that structure when deserializing and can use component accessors when serializing.

## What does `@RequestBody` do?

> It tells Spring MVC to convert the HTTP request body into the declared Java parameter using the configured HTTP message converters, typically Jackson for JSON.

## Is Jackson responsible for business validation?

> No. Jackson's primary responsibility is data conversion between JSON and Java types. Business and Bean Validation constraints are separate concerns.

## Why use `PaymentRequest` instead of accepting `Payment` directly?

> A request DTO keeps the public API contract separate from the internal domain model, allowing both to evolve independently and avoiding accidental exposure of domain internals.

---

# Senior-Level Takeaways

## Record Equality Is Value-Oriented

```text
a.equals(b)
→ compares record components

a == b
→ compares object identity
```

Two record instances can be equal while still being different objects.

## Same Hash Code Does Not Prove Equality

```text
equals true
→ same hashCode required
```

but:

```text
same hashCode
→ equals may still be false
```

because hash collisions are possible.

## Records Are Shallowly Immutable

```text
record component reference
→ final
```

but:

```text
mutable object referenced by component
→ may still mutate
```

Use defensive copies when needed:

```java
payments = List.copyOf(payments);
```

## DTO Refactor Can Preserve API Contract

Internally:

```text
class DTO
→ record DTO
```

Externally:

```json
{
  "amount": 500.00,
  "method": "PIX"
}
```

can remain unchanged.

That is an important example of changing implementation without changing the API contract.

## Keep Boundaries Clear

```text
JSON
↓
Request DTO
↓
Mapper
↓
Domain
↓
Service
```

and:

```text
Domain
↓
Mapper
↓
Response DTO
↓
JSON
```

Do not let Jackson or API DTO concerns leak unnecessarily into domain/service logic.

---

# Day 21 Completion Checklist

## Part A — Records

- [x] Understand why records exist
- [x] Create a basic record
- [x] Understand record components
- [x] Use generated accessors
- [x] Understand generated `equals()`
- [x] Understand generated `hashCode()`
- [x] Understand generated `toString()`
- [x] Distinguish equality from identity
- [x] Understand shallow immutability
- [x] Use `List.copyOf()` for defensive copying
- [x] Understand compact constructors
- [x] Convert `PaymentResponse` to a record
- [x] Preserve existing mapper construction

## Part B — Jackson + Spring MVC

- [x] Understand serialization
- [x] Understand deserialization
- [x] Return `PaymentResponse` record from controller
- [x] Serialize `List<PaymentResponse>`
- [x] Create `PaymentRequest` record
- [x] Use `@RequestBody`
- [x] Map request DTO to domain
- [x] Add `POST /payments/preview`
- [x] Understand enum deserialization
- [x] Distinguish Jackson conversion from validation
- [x] Debug HTTP 405 caused by wrong POST path

---

# Final Mental Model

```text
record
→ concise fixed data carrier
```

```text
record components
→ final references
→ shallow immutability
```

```text
class getter
getAmount()

record accessor
amount()
```

```text
Serialization
Java → JSON
```

```text
Deserialization
JSON → Java
```

```text
GET

Payment
→ PaymentResponse record
→ Jackson
→ JSON
```

```text
POST

JSON
→ Jackson
→ PaymentRequest record
→ Payment
→ PaymentResponse record
→ Jackson
→ JSON
```

That is the core of Day 21.
