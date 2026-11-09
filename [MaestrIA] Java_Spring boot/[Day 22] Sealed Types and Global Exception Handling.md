# Day 22 — Sealed Types + Global Exception Handling

## Goal

Day 22 connects two ideas that solve a similar problem at different layers:

```text
Java
sealed hierarchy
→ known failure variants
→ exhaustive handling

Spring
known exceptions
→ global exception handlers
→ consistent HTTP responses
```

The day is split into:

```text
Part A — Sealed Types + Pattern Matching
Part B — Global Exception Handling with Spring MVC
```

---

# Part A — Sealed Types + Pattern Matching

## 1. Why Sealed Types Exist

A normal interface is open:

```java
public interface Failure {
}
```

Any class can implement it:

```java
class NotFound implements Failure {
}

class Invalid implements Failure {
}

class Timeout implements Failure {
}
```

This means the hierarchy can grow freely.

Sometimes that is desirable. But sometimes the domain should have only a known set of valid implementations.

Example:

```text
Failure
├── NotFound
└── Invalid
```

Java lets us express that explicitly:

```java
public sealed interface Failure
        permits NotFound, Invalid {
}
```

Mental model:

```text
normal interface
→ open inheritance

sealed interface
→ controlled inheritance
```

---

## 2. `sealed` and `permits`

Example:

```java
public sealed interface Failure
        permits NotFound, Invalid {
}
```

`sealed` means only approved direct subtypes may extend or implement this type.

`permits` lists those allowed direct subtypes.

---

## 3. Records as Permitted Subtypes

```java
public record NotFound(String message)
        implements Failure {
}
```

```java
public record Invalid(String message)
        implements Failure {
}
```

Records fit sealed hierarchies well because records are implicitly `final`.

---

## 4. `sealed` vs `final`

```text
final
→ no subclasses are allowed

sealed
→ subclasses are allowed, but only selected ones
```

So:

```text
sealed = controlled inheritance
final  = inheritance stops
```

---

## 5. Direct Subtypes Must Define Inheritance Policy

A direct subtype of a sealed type must normally be:

```text
final
sealed
non-sealed
```

### final

```java
public final class NotFound
        implements Failure {
}
```

Meaning: inheritance stops here.

### sealed

```java
public sealed class Invalid
        implements Failure
        permits InvalidAmount, InvalidMethod {
}
```

Meaning: inheritance continues but remains restricted.

### non-sealed

```java
public non-sealed class ExternalFailure
        implements Failure {
}
```

Meaning: inheritance becomes open again below this subtype.

Mental model:

```text
sealed
→ restrict

final
→ stop

non-sealed
→ reopen
```

---

## 6. Pattern Matching with `instanceof`

Old style:

```java
if (failure instanceof NotFound) {
    NotFound notFound = (NotFound) failure;
    System.out.println(notFound.message());
}
```

Modern style:

```java
if (failure instanceof NotFound notFound) {
    System.out.println(notFound.message());
}
```

This combines type check, cast, and typed variable creation.

---

## 7. Pattern Matching with `switch`

Given:

```java
sealed interface Failure
        permits NotFound, Invalid {
}
```

we can write:

```java
static int status(Failure failure) {
    return switch (failure) {
        case NotFound notFound -> 404;
        case Invalid invalid -> 400;
    };
}
```

Each case checks the subtype and binds a typed variable.

---

## 8. Exhaustive `switch`

Because Java knows all permitted subtypes, the compiler can verify that every valid case is handled.

```java
return switch (failure) {
    case NotFound n -> 404;
    case Invalid i -> 400;
};
```

No `default` is required because the switch is exhaustive.

---

## 9. Why Exhaustiveness Matters

Suppose the hierarchy changes:

```java
sealed interface Failure
        permits NotFound, Invalid, Timeout {
}
```

and:

```java
record Timeout(String message)
        implements Failure {
}
```

If the switch still has only:

```java
case NotFound n -> 404;
case Invalid i -> 400;
```

the compiler detects that the switch is incomplete.

Add:

```java
case Timeout t -> 504;
```

Core idea:

```text
domain hierarchy changes
→ compiler finds incomplete handling
```

---

## 10. Why an Unnecessary `default` Can Hide Problems

```java
return switch (failure) {
    case NotFound n -> 404;
    case Invalid i -> 400;
    default -> 500;
};
```

If a new subtype is added later, the code may still compile.

With an exhaustive sealed switch and no unnecessary `default`:

```text
new subtype
→ compile error
→ developer must explicitly handle it
```

---

## 11. Pattern Matching with Subtype-Specific Data

```java
static String describe(Failure failure) {
    return switch (failure) {
        case NotFound n ->
                "Not found: " + n.message();

        case Invalid i ->
                "Invalid: " + i.message();
    };
}
```

The pattern variable has the specific subtype, so subtype-specific data is directly available.

---

## 12. Different Variant Shapes

```java
public sealed interface PaymentResult
        permits Approved, Declined {
}
```

```java
public record Approved(
        String transactionId
) implements PaymentResult {
}
```

```java
public record Declined(
        String reason,
        int code
) implements PaymentResult {
}
```

Mapper:

```java
public class PaymentResultMapper {

    public static String describe(
            PaymentResult result) {

        return switch (result) {
            case Approved approved ->
                    "Approved: "
                            + approved.transactionId();

            case Declined declined ->
                    "Declined "
                            + declined.code()
                            + ": "
                            + declined.reason();
        };
    }
}
```

---

## 13. Enum vs Sealed Hierarchy

```text
enum
→ fixed set of values/constants

sealed hierarchy
→ fixed set of types
```

Example:

```text
PaymentMethod
→ PIX
→ CREDIT_CARD
```

versus:

```text
PaymentResult
├── Approved(transactionId)
└── Declined(reason, code)
```

---

## 14. Payment Failure Example

```java
public sealed interface PaymentFailure
        permits PaymentMissing, InvalidPayment {
}
```

```java
public record PaymentMissing(Long id)
        implements PaymentFailure {
}
```

```java
public record InvalidPayment(String reason)
        implements PaymentFailure {
}
```

Hierarchy:

```text
PaymentFailure
├── PaymentMissing(id)
└── InvalidPayment(reason)
```

---

## 15. Mapping Failure to Status

```java
public static int status(
        PaymentFailure failure) {

    return switch (failure) {
        case PaymentMissing missing -> 404;
        case InvalidPayment invalid -> 400;
    };
}
```

---

## 16. Mapping Failure to Message

```java
public static String message(
        PaymentFailure failure) {

    return switch (failure) {
        case PaymentMissing missing ->
                "Payment not found: "
                        + missing.id();

        case InvalidPayment invalid ->
                "Invalid payment: "
                        + invalid.reason();
    };
}
```

---

## 17. Pattern Guards with `when`

```java
public static String describe(
        PaymentFailure failure) {

    return switch (failure) {

        case InvalidPayment invalid
                when invalid.reason().isBlank() ->
                "Invalid payment with no reason";

        case InvalidPayment invalid ->
                "Invalid payment: "
                        + invalid.reason();

        case PaymentMissing missing ->
                "Payment not found: "
                        + missing.id();
    };
}
```

A guard means:

```text
match the type
+
check an extra condition
```

Put the more specific guarded pattern before the broader pattern.

---

## 18. Handling `null`

```java
return switch (failure) {
    case null -> 500;
    case PaymentMissing missing -> 404;
    case InvalidPayment invalid -> 400;
};
```

For normal application code, avoid passing `null` unless it is intentionally part of the design.

---

## 19. Intentional Break Exercise

First create:

```java
public record PaymentTimeout(
        String message
) implements PaymentFailure {
}
```

without adding it to `permits`.

This should fail.

Then update:

```java
public sealed interface PaymentFailure
        permits PaymentMissing,
                InvalidPayment,
                PaymentTimeout {
}
```

Now leave your switch unchanged. The compiler should complain that it is no longer exhaustive.

Fix it with:

```java
case PaymentTimeout timeout -> 504;
```

Core lesson:

```text
sealed hierarchy changes
→ exhaustive switch must change too
```

---

## 20. Part A Interview Questions

### What is a sealed class or interface?

A sealed class or interface restricts which classes may directly extend or implement it. The allowed direct subtypes are controlled by the hierarchy.

### What is the difference between `sealed`, `final`, and `non-sealed`?

```text
sealed
→ inheritance allowed but restricted

final
→ inheritance stops

non-sealed
→ inheritance becomes open again
```

### Why are sealed types useful with switch pattern matching?

Because the compiler knows the permitted subtypes and can verify whether a switch covers every possible case.

### What is pattern matching for `instanceof`?

It combines the type check and cast into one operation.

### Why use a sealed hierarchy instead of an enum?

Enums model a fixed set of values. Sealed hierarchies model a fixed set of types that may carry different data and behavior.

---

# Part B — Global Exception Handling in Spring MVC

## 21. The Problem

The service already throws:

```java
public Payment findPaymentById(Long id) {
    return paymentRepository.findById(id)
            .orElseThrow(
                    () -> new PaymentNotFoundException(id)
            );
}
```

If payment `999` does not exist:

```text
GET /payments/999
        ↓
Controller
        ↓
Service
        ↓
Repository
        ↓
Optional.empty()
        ↓
PaymentNotFoundException
```

We do not want every controller method to contain its own `try/catch`.

---

## 22. Separation of Responsibilities

The service should know:

```text
payment not found
```

The web layer should know:

```text
404 Not Found
```

So:

```text
Service
→ throws meaningful application exception

Spring MVC advice
→ converts exception into HTTP response
```

---

## 23. `@ExceptionHandler`

```java
@ExceptionHandler(PaymentNotFoundException.class)
public ResponseEntity<String> handlePaymentNotFound(
        PaymentNotFoundException ex) {

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ex.getMessage());
}
```

Meaning:

```text
PaymentNotFoundException
        ↓
handler method
        ↓
404 Not Found
```

---

## 24. Local vs Global Exception Handling

An `@ExceptionHandler` can be placed in a controller, but then it only applies locally.

For multiple controllers, centralized handling avoids duplication.

---

## 25. `@ControllerAdvice`

```java
@ControllerAdvice
public class GlobalExceptionHandler {
}
```

This allows controller-related concerns, especially exception handling, to be shared across controllers.

---

## 26. `@RestControllerAdvice`

For REST APIs:

```java
@RestControllerAdvice
```

Mental model:

```text
@RestControllerAdvice
≈ @ControllerAdvice + @ResponseBody
```

Returned objects are serialized to the HTTP response body.

---

## 27. First Global Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            PaymentNotFoundException.class
    )
    public ResponseEntity<String>
            handlePaymentNotFound(
                    PaymentNotFoundException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }
}
```

---

## 28. Controller Remains Clean

```java
@GetMapping("/{id}")
public PaymentResponse getPaymentById(
        @PathVariable Long id) {

    Payment payment =
            service.findPaymentById(id);

    return mapper.toResponse(payment);
}
```

No repeated `try/catch` is required.

---

## 29. Consistent JSON Error

```java
public record ApiError(
        int status,
        String error,
        String message,
        String path
) {
}
```

This is a good fit for a record because it is a fixed data carrier.

---

## 30. Why No Timestamp Yet?

Day 23 covers Java date/time and Jackson date/time serialization.

So keep Day 22 focused on exception mapping.

---

## 31. Returning `ApiError`

```java
@ExceptionHandler(
        PaymentNotFoundException.class
)
public ResponseEntity<ApiError>
        handlePaymentNotFound(
                PaymentNotFoundException ex,
                HttpServletRequest request) {

    ApiError error = new ApiError(
            404,
            "Not Found",
            ex.getMessage(),
            request.getRequestURI()
    );

    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(error);
}
```

Expected JSON:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Payment not found: 999",
  "path": "/payments/999"
}
```

---

## 32. `ResponseEntity<T>`

`ResponseEntity<T>` represents:

```text
HTTP status
+
HTTP headers
+
HTTP body
```

Example:

```text
ResponseEntity<ApiError>
→ status 404
→ ApiError body
```

---

## 33. `HttpStatus`

Prefer:

```java
HttpStatus.NOT_FOUND
HttpStatus.BAD_REQUEST
HttpStatus.INTERNAL_SERVER_ERROR
```

instead of hard-coded numeric status values when building responses.

---

## 34. Handler Method Parameters

The exception parameter:

```java
PaymentNotFoundException ex
```

gives access to:

```java
ex.getMessage()
```

The request parameter:

```java
HttpServletRequest request
```

lets us obtain:

```java
request.getRequestURI()
```

for the error response path.

---

## 35. Full Exception Flow

```text
Request
   ↓
Controller
   ↓
Service
   ↓
throws exception
   ↓
Spring MVC
   ↓
@RestControllerAdvice
   ↓
@ExceptionHandler
   ↓
ResponseEntity<ApiError>
   ↓
Jackson
   ↓
JSON + HTTP status
```

---

## 36. Second Mapping — 400 Bad Request

The service already contains an `IllegalArgumentException` path:

```java
if (strategy == null) {
    throw new IllegalArgumentException(
            "Strategy not found: "
                    + strategyName
    );
}
```

Map it with:

```java
@ExceptionHandler(
        IllegalArgumentException.class
)
public ResponseEntity<ApiError>
        handleIllegalArgument(
                IllegalArgumentException ex,
                HttpServletRequest request) {

    ApiError error = new ApiError(
            400,
            "Bad Request",
            ex.getMessage(),
            request.getRequestURI()
    );

    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(error);
}
```

---

## 37. Full Handler Example

```java
package org.jfl.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.jfl.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
            PaymentNotFoundException.class
    )
    public ResponseEntity<ApiError>
            handlePaymentNotFound(
                    PaymentNotFoundException ex,
                    HttpServletRequest request) {

        ApiError error = new ApiError(
                404,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(
            IllegalArgumentException.class
    )
    public ResponseEntity<ApiError>
            handleIllegalArgument(
                    IllegalArgumentException ex,
                    HttpServletRequest request) {

        ApiError error = new ApiError(
                400,
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
```

---

## 38. Why Not Catch Exceptions in the Service?

Avoid swallowing application failures:

```java
try {
    ...
} catch (PaymentNotFoundException ex) {
    return null;
}
```

Problems:

```text
failure information is lost
null becomes ambiguous
error handling becomes inconsistent
```

Also avoid returning `ResponseEntity` from the service because HTTP is a web-layer concern.

---

## 39. Why Not Put `HttpStatus` Inside the Exception?

Keep the exception focused:

```java
public class PaymentNotFoundException
        extends RuntimeException {

    public PaymentNotFoundException(Long id) {
        super("Payment not found: " + id);
    }
}
```

Mental model:

```text
PaymentNotFoundException
→ application concern

404
→ HTTP concern
```

The advice maps one to the other.

---

## 40. Specific vs Generic Handlers

Specific handler:

```java
@ExceptionHandler(
        PaymentNotFoundException.class
)
```

Generic handler:

```java
@ExceptionHandler(Exception.class)
```

For Day 22, focus on explicit mappings rather than a catch-all.

---

## 41. Why 404?

For:

```text
GET /payments/999
```

the endpoint exists, but the resource does not.

So:

```text
404 Not Found
```

is correct.

---

## 42. Why 400?

`400 Bad Request` is appropriate when the request is invalid from the API/client perspective.

Conceptually:

```text
invalid request/input
→ 400
```

---

## 43. Connection Between Part A and Part B

Part A:

```java
return switch (failure) {
    case PaymentMissing missing -> 404;
    case InvalidPayment invalid -> 400;
};
```

Part B:

```text
PaymentNotFoundException
→ @ExceptionHandler
→ 404

IllegalArgumentException
→ @ExceptionHandler
→ 400
```

Same conceptual pattern:

```text
known failure type
→ known response
```

Difference:

```text
Part A
→ Java type-system mapping

Part B
→ Spring MVC HTTP mapping
```

---

## 44. Testing the 404 Handler

```bash
curl -i http://localhost:8081/payments/999999
```

Expected:

```text
HTTP/1.1 404
```

with JSON similar to:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Payment not found: 999999",
  "path": "/payments/999999"
}
```

---

## 45. Test the Happy Path

Call an existing payment ID.

Expected:

```text
200 OK
```

with normal `PaymentResponse`.

The global handler should not affect successful requests.

---

## 46. Intentional Break

Temporarily remove or comment:

```java
@ExceptionHandler(
        PaymentNotFoundException.class
)
```

Call a missing payment again.

Compare Spring's default error response with your controlled `ApiError` contract.

Then restore the handler.

---

## 47. Do Not Add Validation Yet

Avoid today:

```java
@Valid
@NotNull
@Positive
@Pattern
```

That belongs to Day 24.

---

## 48. Do Not Add Date/Time Yet

Avoid today:

```java
Instant
LocalDateTime
ZonedDateTime
```

inside `ApiError`.

That belongs to Day 23.

---

# Interview Questions

## What does `@ExceptionHandler` do?

It associates an exception type with a method that handles that exception during Spring MVC request processing.

## What does `@ControllerAdvice` do?

It provides global controller-related behavior such as centralized exception handling across multiple controllers.

## What is `@RestControllerAdvice`?

It is the REST-oriented version of controller advice and includes response-body behavior.

```text
@RestControllerAdvice
≈ @ControllerAdvice + @ResponseBody
```

## Why not catch exceptions in every controller?

Because it duplicates error-handling logic, mixes concerns, and makes consistent API responses harder to maintain.

## Why should the service not return `ResponseEntity`?

Because `ResponseEntity` is an HTTP concern. The service should remain independent from the transport layer.

---

# Day 22 Final Mental Model

## Part A

```text
sealed hierarchy
        ↓
known permitted subtypes
        ↓
pattern matching
        ↓
exhaustive switch
        ↓
compiler protects completeness
```

## Part B

```text
request
   ↓
controller
   ↓
service
   ↓
throws exception
   ↓
@RestControllerAdvice
   ↓
@ExceptionHandler
   ↓
ResponseEntity<ApiError>
   ↓
JSON + HTTP status
```

---

# Concise Manual Notes

```text
DAY 22

PART A — SEALED TYPES

sealed
= controlled inheritance

permits
= allowed direct subtypes

Direct subtype:
final      → stop
sealed     → restrict again
non-sealed → reopen

record = implicitly final

instanceof pattern:
if (x instanceof Type t)

switch pattern:
case Type t -> ...

sealed + switch
= compiler checks exhaustiveness

new subtype
→ exhaustive switch may stop compiling

enum
= fixed values

sealed hierarchy
= fixed types


PART B — SPRING EXCEPTION HANDLING

Service:
throws application exception

Controller:
no duplicated try/catch

@RestControllerAdvice
= global REST controller advice

@ExceptionHandler
= maps exception type to handler method

PaymentNotFoundException
→ 404 Not Found

IllegalArgumentException
→ 400 Bad Request

ResponseEntity<T>
= status + headers + body

ApiError record:
status
error
message
path

Keep:
service → application/domain concerns
advice  → HTTP concerns
```
