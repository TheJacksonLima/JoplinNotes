# [Day 20] Method References & Functional Composition

## Objective

Day 20 focused on two related ideas:

- Java method references and `Function` composition
- Applying those ideas inside the existing Spring Payment codebase

The roadmap scope was:

```text
Java
→ Method references
→ Function.andThen()
→ Function.compose()

Spring
→ Mapper/service composition
```

The goal was not to force functional programming everywhere, but to understand how small compatible functions can be reused and composed into larger transformations.

---

# Part A — Method References

## 1. What is a method reference?

A method reference is a concise form of a lambda when an existing method already matches the required functional interface.

These are equivalent:

```java
Function<Payment, BigDecimal> extractAmount =
        payment -> payment.getAmount();
```

```java
Function<Payment, BigDecimal> extractAmount =
        Payment::getAmount;
```

Both represent:

```text
Payment → BigDecimal
```

Important:

```java
Payment::getAmount
```

does not execute the method immediately. It represents behavior that can be executed later.

---

## 2. Method reference forms

### Static method reference

```java
Function<String, Integer> parser =
        Integer::parseInt;
```

Equivalent:

```java
value -> Integer.parseInt(value)
```

### Bound instance method reference

```java
Predicate<Payment> validPayment =
        paymentFunctions::isValidAmount;
```

Equivalent:

```java
payment -> paymentFunctions.isValidAmount(payment)
```

### Unbound instance method reference

```java
Function<Payment, BigDecimal> extractAmount =
        Payment::getAmount;
```

Equivalent:

```java
payment -> payment.getAmount()
```

### Constructor reference

```java
BiFunction<BigDecimal, PaymentMethod, Payment> creator =
        Payment::new;
```

Equivalent:

```java
(amount, method) -> new Payment(amount, method)
```

---

## 3. Method references depend on the target type

This:

```java
Payment::getAmount
```

works when Java expects:

```java
Function<Payment, BigDecimal>
```

because the shapes match:

```text
Payment → BigDecimal
```

A method reference is valid when the referenced method matches the input/output signature required by the functional interface.

---

# Part A — Functional Composition

## 4. `Function<T,R>`

A Java `Function<T,R>` represents:

```text
T → R
```

Example:

```java
Function<Payment, BigDecimal> extractAmount =
        Payment::getAmount;
```

Another function:

```java
Function<BigDecimal, String> formatAmount =
        amount -> "Amount: " + amount;
```

Since the output of the first function matches the input of the second, they can be composed.

---

## 5. `andThen()`

`andThen()` means:

```text
run this function
THEN
run the next function
```

```java
Function<Payment, String> usingAndThen =
        extractAmount.andThen(formatAmount);
```

Flow:

```text
Payment
   ↓ extractAmount
BigDecimal
   ↓ formatAmount
String
```

Execution happens with:

```java
String result = usingAndThen.apply(payment);
```

Important distinction:

```text
andThen()
→ builds the composed function

apply()
→ executes the function
```

---

## 6. `compose()`

```java
Function<Payment, String> usingCompose =
        formatAmount.compose(extractAmount);
```

Execution order is still:

```text
extractAmount
→ formatAmount
```

So:

```text
f.andThen(g)
```

has the same execution order as:

```text
g.compose(f)
```

Rule:

```text
a.andThen(b)
→ a → b

a.compose(b)
→ b → a
```

Shortcut:

```text
andThen
LEFT → RIGHT

compose
RIGHT → LEFT
```

---

## 7. Function compatibility

Functions compose when the output of one matches the input of the next.

```text
Payment → BigDecimal
BigDecimal → String
```

becomes:

```text
Payment → String
```

Generic form:

```text
A → B
B → C
```

becomes:

```text
A → C
```

Java generics catch incompatible compositions at compile time.

---

## 8. Three-function composition

The fee function was:

```java
Function<BigDecimal, BigDecimal> addFee =
        amount -> amount.add(
                amount.multiply(new BigDecimal("0.10"))
        );
```

Full pipeline:

```java
Function<Payment, String> processorWithFee =
        extractAmount
                .andThen(addFee)
                .andThen(formatAmount);
```

Flow:

```text
Payment
→ BigDecimal
→ BigDecimal (+10%)
→ String
```

Example:

```text
Payment(100.00, PIX)
→ 100.00
→ 110.0000
→ "Amount: 110.0000"
```

Why `110.0000` and not `110`? `BigDecimal.multiply()` adds the scales (decimal places) of both operands: `100.00` (scale 2) × `0.10` (scale 2) = `10.0000` (scale 4), and `add()` keeps the larger scale.

To keep money at two decimals, set the scale explicitly:

```java
Function<BigDecimal, BigDecimal> addFee =
        amount -> amount.add(
                amount.multiply(new BigDecimal("0.10"))
        ).setScale(2, RoundingMode.HALF_EVEN);
```

Result: `"Amount: 110.00"`.

---

## 9. BigDecimal arithmetic

`BigDecimal` is an object, so arithmetic operators such as `*` cannot be used directly.

Invalid:

```java
amount * BigDecimal.valueOf(0.1)
```

Use methods such as:

```java
add()
subtract()
multiply()
divide()
```

For decimal monetary constants, string construction avoids binary floating-point surprises:

```java
new BigDecimal("0.10")
```

---

## 10. Using a composed function in a Stream

```java
Function<Payment, String> processor =
        extractAmount.andThen(formatAmount);

List<String> formattedAmounts =
        listPayments.stream()
                .map(processor)
                .toList();
```

This connects earlier topics:

```text
Day 16 → Function<T,R>
Day 17 → Stream.map()
Day 20 → Function composition
```

---

## 11. Payment method composition

```java
Function<Payment, PaymentMethod> extractMethod =
        Payment::getMethod;

Function<PaymentMethod, String> methodName =
        Enum::name;

Function<Payment, String> paymentMethodName =
        extractMethod.andThen(methodName);
```

Flow:

```text
Payment
→ PaymentMethod
→ String
```

Example:

```text
Payment
→ CREDIT_CARD
→ "CREDIT_CARD"
```

---

# Part B — Spring Mapper Composition

## 12. Mapper responsibility

`PaymentMapper` converts the internal domain object to the API DTO:

```text
Payment
→ PaymentResponse
```

Collection mapping already used a method reference:

```java
return payments.stream()
        .map(this::toResponse)
        .toList();
```

`toResponse(Payment)` matches:

```java
Function<Payment, PaymentResponse>
```

---

## 13. Final `PaymentMapper` composition

The mapper uses:

```java
private final Function<Payment, PaymentMethod> extractMethod =
        Payment::getMethod;

private final Function<Payment, BigDecimal> extractAmount =
        Payment::getAmount;

private final Function<PaymentMethod, String> methodName =
        Enum::name;

private final Function<Payment, String> methodNameExtractor =
        extractMethod.andThen(methodName);
```

The key transformation is:

```text
Payment
→ PaymentMethod
→ String
```

Then:

```java
public PaymentResponse toResponse(Payment payment) {
    return new PaymentResponse(
            extractAmount.apply(payment),
            methodNameExtractor.apply(payment)
    );
}
```

The API behavior remains unchanged. This is a refactor, not a behavior change.

---

## 14. Method reference inside the mapper stream

```java
public List<PaymentResponse> toResponseList(
        List<Payment> payments) {

    return payments.stream()
            .map(this::toResponse)
            .toList();
}
```

Equivalent lambda:

```java
.map(payment -> this.toResponse(payment))
```

Method reference:

```java
.map(this::toResponse)
```

The method reference is cleaner because there is no additional lambda logic.

---

## 15. Do not overuse `Function`

Functional composition should improve:

```text
clarity
reusability
testability
```

It should not be used just because it is available.

For example:

```java
payment.getAmount()
```

is often clearer than:

```java
extractAmount.apply(payment)
```

The functional version was useful here because Day 20 specifically studies composition.

---

# Part B — Service Composition

## 16. Existing service methods

The service already had:

```java
public Payment findPaymentById(Long id)
```

Shape:

```text
Long → Payment
```

and:

```java
public PaymentStatus process(Payment payment)
```

Shape:

```text
Payment → PaymentStatus
```

These are naturally compatible:

```text
Long → Payment
Payment → PaymentStatus
```

Therefore:

```text
Long → PaymentStatus
```

---

## 17. `processById()`

Final implementation:

```java
public PaymentStatus processById(Long id) {
    Function<Long, Payment> findPayment =
            this::findPaymentById;

    Function<Payment, PaymentStatus> processPayment =
            this::process;

    Function<Long, PaymentStatus> findAndProcess =
            findPayment.andThen(processPayment);

    return findAndProcess.apply(id);
}
```

Flow:

```text
Long
 ↓
findPaymentById
 ↓
Payment
 ↓
process
 ↓
PaymentStatus
```

Final type:

```java
Function<Long, PaymentStatus>
```

---

## 18. Relationship with Day 19

Day 19 introduced:

```java
public Payment findPaymentById(Long id) {
    return paymentRepository.findById(id)
            .orElseThrow(
                    () -> new PaymentNotFoundException(id)
            );
}
```

Day 20 does not replace that behavior. It composes it with `process()`.

Full flow:

```text
Long
 ↓
findPaymentById()
 ↓
repository.findById()
 ↓
Optional<Payment>
 ↓
orElseThrow()
 ↓
Payment
 ↓
process()
 ↓
PaymentStatus
```

This preserves the cumulative roadmap design.

---

## 19. Layering decision

Do not move DTO mapping into `PaymentService` just because the types can be composed.

Keep responsibilities separated:

```text
Controller
→ HTTP concerns

Service
→ application/business logic

Repository
→ data access

Mapper
→ domain → API DTO
```

A technically possible composition is not automatically a good architectural decision.

---

## 20. Intentionally breaking composition

Given:

```java
Function<Payment, PaymentMethod> extractMethod =
        Payment::getMethod;

Function<PaymentMethod, String> methodName =
        Enum::name;
```

Correct flow:

```text
Payment → PaymentMethod → String
```

Trying to reverse incompatible functions produces a compile-time type error.

Core rule:

```text
A → B
B → C
✅ composable
```

but:

```text
A → B
C → D
❌ not necessarily composable
```

---

# Interview Questions

## What is a method reference?

> A method reference is a concise form of a lambda when an existing method already matches the required functional interface.

## What is the difference between `andThen()` and `compose()`?

> `andThen()` runs the current function first and the next one second. `compose()` runs the supplied function first and the current function second.

```text
f.andThen(g)
→ f → g

f.compose(g)
→ g → f
```

## What must be true for two functions to compose?

> The output type of the first function must be compatible with the input type of the second function.

```text
Function<A,B>
Function<B,C>
→ Function<A,C>
```

## Does `andThen()` execute immediately?

> No. It creates a new composed function. Execution occurs when `apply()` is called.

## Why does `.map(this::toResponse)` compile?

> Because `toResponse(Payment)` has the same shape as `Function<Payment, PaymentResponse>`.

## Why is `Long → Payment → PaymentStatus` a good service composition example?

> `findPaymentById(Long)` returns exactly the type that `process(Payment)` requires, so their contracts naturally compose.

## Should every method be represented as a `Function`?

> No. Composition is useful when it improves clarity, reuse, or testability. Direct method calls are often simpler.

---

# Common Traps

## Wrong `Function` import

Use:

```java
import java.util.function.Function;
```

## Forgetting `apply()`

Defining:

```java
Function<Payment, String> f = ...;
```

creates behavior. Execute it with:

```java
f.apply(payment);
```

## Printing the function itself

Wrong for executing the transformation:

```java
System.out.println(paymentMethodName);
```

Correct:

```java
System.out.println(
        paymentMethodName.apply(payment)
);
```

## Wrong composition direction

```text
andThen → LEFT → RIGHT
compose → RIGHT → LEFT
```

---

# Senior-Level Takeaway

Functional composition is useful when the program already contains small operations with clear input/output contracts.

Think in transformations:

```text
Payment → BigDecimal
Payment → PaymentMethod
PaymentMethod → String
Long → Payment
Payment → PaymentStatus
```

Then ask:

```text
Does the output of one function match the input of the next?
```

If yes, they can often be composed.

But composition is a tool, not a goal. Prefer simple direct code whenever it communicates the intent more clearly.

---

# Final Day 20 Architecture

```text
HTTP Request
    ↓
PaymentController
    ↓
PaymentService
    ↓
PaymentRepository
```

Service composition:

```text
Long
→ Payment
→ PaymentStatus
```

DTO mapping:

```text
Payment
→ PaymentMethod
→ String
```

Collection mapping:

```text
List<Payment>
→ Stream<Payment>
→ map(this::toResponse)
→ List<PaymentResponse>
```

---

# Day 20 Completion Checklist

## Part A — Java

- [x] Understand method references
- [x] Recognize static, bound instance, unbound instance and constructor references
- [x] Use `Function<T,R>`
- [x] Use `andThen()`
- [x] Use `compose()`
- [x] Explain execution order
- [x] Compose three functions
- [x] Use a composed function inside `Stream.map()`
- [x] Understand type compatibility
- [x] Use `BigDecimal` correctly in the fee exercise

## Part B — Spring

- [x] Compose mapper transformations
- [x] Use a method reference inside mapper stream
- [x] Preserve `PaymentResponse`
- [x] Compose service methods
- [x] Implement `processById(Long id)`
- [x] Preserve Day 19 not-found behavior
- [x] Keep service independent from API DTOs
- [x] Clean up naming and unused imports

---

# Final Mental Model

```text
Method reference
→ reuse existing behavior
```

```text
Function<A,B>
+
Function<B,C>
=
Function<A,C>
```

```text
f.andThen(g)
→ f → g
```

```text
f.compose(g)
→ g → f
```

```text
andThen()/compose()
→ build behavior

apply()
→ execute behavior
```

Payment examples:

```text
Payment
→ PaymentMethod
→ String
```

```text
Long
→ Payment
→ PaymentStatus
```

That is the core of Day 20.
