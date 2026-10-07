# [Day 16] Standard Functional Interfaces & Spring Collection Injection

## Objective

Understand and apply:

### Java
- `Predicate<T>`
- `Function<T, R>`
- `Consumer<T>`
- `Supplier<T>`
- Functional interface method names
- Simple functional composition
- `BiFunction<T, U, R>` as an extra practical extension

### Spring
- Inject multiple beans of the same type
- `List<T>` injection
- `Map<String, T>` injection
- Difference between `@Qualifier`, `List<T>`, and `Map<String,T>`
- Use a map as a strategy registry
- Select a strategy dynamically

---

# 1. Standard Functional Interfaces

Java provides common functional interfaces in:

```java
java.util.function
```

The four core interfaces for Day 16 are:

```text
Predicate<T>
Function<T,R>
Consumer<T>
Supplier<T>
```

Mental model:

```text
Predicate<T>
T → boolean

Function<T,R>
T → R

Consumer<T>
T → void

Supplier<T>
() → T
```

---

# 2. `Predicate<T>`

A `Predicate<T>` represents a condition or test.

Main method:

```java
boolean test(T value);
```

Example:

```java
Predicate<Payment> validAmount =
        payment ->
                payment.getAmount()
                       .compareTo(BigDecimal.ZERO) > 0;
```

Usage:

```java
boolean valid =
        validAmount.test(payment);
```

Mental model:

```text
Predicate
→ ask a yes/no question
```

Examples:

```text
Is amount positive?
Is customer active?
Is payment valid?
Is transaction above limit?
```

---

# 3. Predicate Composition

Predicates can be combined.

Example:

```java
Predicate<Payment> positive =
        payment ->
                payment.getAmount()
                       .compareTo(BigDecimal.ZERO) > 0;
```

Another:

```java
Predicate<Payment> belowLimit =
        payment ->
                payment.getAmount()
                       .compareTo(
                           new BigDecimal("10000")
                       ) <= 0;
```

Combine:

```java
Predicate<Payment> valid =
        positive.and(belowLimit);
```

Other useful operations:

```text
and()
or()
negate()
```

Example:

```java
Predicate<Payment> invalid =
        valid.negate();
```

---

# 4. `Function<T,R>`

A `Function<T,R>` transforms one value into another.

Main method:

```java
R apply(T value);
```

Example:

```java
Function<Payment, BigDecimal> amountExtractor =
        Payment::getAmount;
```

Usage:

```java
BigDecimal amount =
        amountExtractor.apply(payment);
```

Mental model:

```text
Function
→ transform input into output
```

Type meaning:

```text
Function<T,R>

T
→ input

R
→ output
```

Example:

```text
Payment
→ BigDecimal
```

---

# 5. Correct `Function` Import

Use:

```java
import java.util.function.Function;
```

Do NOT use:

```java
org.springframework.cglib.core.internal.Function
```

Day 16 is specifically about Java's standard functional interfaces.

---

# 6. `Consumer<T>`

A `Consumer<T>` receives a value but does not return a result.

Main method:

```java
void accept(T value);
```

Example:

```java
Consumer<Payment> paymentLogger =
        payment ->
                System.out.println(
                        payment.getMethod()
                        + ": "
                        + payment.getAmount()
                );
```

Usage:

```java
paymentLogger.accept(payment);
```

Mental model:

```text
Consumer
→ consume a value
→ perform an action
→ return nothing
```

Typical uses:

```text
logging
notifications
printing
side effects
```

---

# 7. `Supplier<T>`

A `Supplier<T>` creates or supplies a value without receiving arguments.

Main method:

```java
T get();
```

Example:

```java
Supplier<Payment> paymentSupplier =
        Payment::new;
```

Usage:

```java
Payment payment =
        paymentSupplier.get();
```

Mental model:

```text
Supplier
→ no input
→ produces output
```

Shape:

```text
() → T
```

---

# 8. Supplier Exercise

The implementation:

```java
private final Supplier<Payment> paymentSupplier =
        Payment::new;
```

was exposed through:

```java
public Payment createEmptyPayment() {
    return paymentSupplier.get();
}
```

This creates:

```text
Payment(
    amount = null,
    method = null
)
```

because the no-argument constructor is used.

That behavior is expected.

---

# 9. `BiFunction<T,U,R>` — Extra

`BiFunction` was not part of the core Day 16 roadmap, but it came up naturally during the exercise.

A normal `Function` has:

```text
T → R
```

A `BiFunction` has:

```text
(T, U) → R
```

For payment creation:

```text
BigDecimal
+
PaymentMethod
→
Payment
```

This maps naturally to:

```java
BiFunction<
        BigDecimal,
        PaymentMethod,
        Payment
>
```

Example:

```java
private final BiFunction<
        BigDecimal,
        PaymentMethod,
        Payment
> paymentCreator = Payment::new;
```

Usage:

```java
Payment payment =
        paymentCreator.apply(
                new BigDecimal("100.00"),
                PaymentMethod.PIX
        );
```

Main method:

```java
apply(T, U)
```

Important:

```text
BiFunction
→ useful extra knowledge
→ not one of the four required Day 16 interfaces
```

---

# 10. Core Interface Method Names

Memorize:

```text
Predicate<T>
→ test()

Function<T,R>
→ apply()

Consumer<T>
→ accept()

Supplier<T>
→ get()

BiFunction<T,U,R>
→ apply(T,U)
```

---

# 11. `PaymentFunctions`

The practical component grouped the functional-interface examples.

Conceptually:

```java
@Component
public class PaymentFunctions {

    private final Predicate<Payment> validAmount = ...;

    private final Function<Payment, BigDecimal> amountExtractor = ...;

    private final Consumer<Payment> paymentLogger = ...;

    private final Supplier<Payment> paymentSupplier = ...;

    private final BiFunction<
            BigDecimal,
            PaymentMethod,
            Payment
    > paymentCreator = ...;
}
```

Public methods delegate to the functional interfaces:

```java
public boolean isValidAmount(Payment payment) {
    return validAmount.test(payment);
}
```

```java
public BigDecimal extractAmount(Payment payment) {
    return amountExtractor.apply(payment);
}
```

```java
public void logPayment(Payment payment) {
    paymentLogger.accept(payment);
}
```

```java
public Payment createEmptyPayment() {
    return paymentSupplier.get();
}
```

```java
public Payment createPayment(
        BigDecimal value,
        PaymentMethod method) {

    return paymentCreator.apply(
            value,
            method
    );
}
```

---

# 12. Why `@Component`?

`PaymentFunctions` was annotated with:

```java
@Component
```

This makes it a Spring-managed bean.

Spring can then create it and make it available through dependency injection or the application context.

Example:

```java
PaymentFunctions paymentFunctions =
        context.getBean(
            PaymentFunctions.class
        );
```

For this exercise, `@Component` was appropriate because we wanted to practice Spring DI.

---

# 13. Part A Practical Flow

The application exercised:

```text
Predicate
→ validate amount

Function
→ extract amount

Consumer
→ log payment

Supplier
→ create empty payment

BiFunction
→ create initialized payment
```

Example:

```java
Payment payment =
        paymentFunctions.createPayment(
                new BigDecimal("100.00"),
                PaymentMethod.PIX
        );
```

Then:

```java
paymentFunctions.isValidAmount(payment);

paymentFunctions.extractAmount(payment);

paymentFunctions.logPayment(payment);
```

Part A result:

```text
Predicate ✅
Function ✅
Consumer ✅
Supplier ✅
BiFunction ✅ extra
```

---

# 14. Custom Interface vs Standard Functional Interface

Day 15 created:

```java
@FunctionalInterface
public interface PaymentStrategy {

    PaymentStatus pay(Payment payment);
}
```

Technically, this could be represented as:

```java
Function<Payment, PaymentStatus>
```

because both have the shape:

```text
Payment → PaymentStatus
```

However:

```java
PaymentStrategy
```

has stronger domain meaning.

Compare:

```java
strategy.pay(payment);
```

with:

```java
function.apply(payment);
```

Rule:

```text
generic behavior
→ java.util.function interfaces

important domain concept
→ custom functional interface may be clearer
```

---

# 15. Spring Multiple-Bean Injection

Suppose Spring has multiple beans implementing:

```java
PaymentStrategy
```

For example:

```text
pixStrategy
cardStrategy
```

Day 15 solved this using:

```java
@Qualifier
```

Example:

```java
@Qualifier("pixStrategy")
PaymentStrategy pix
```

This means:

```text
give me ONE specific bean
```

Day 16 introduces two other possibilities:

```text
List<PaymentStrategy>
```

and:

```text
Map<String, PaymentStrategy>
```

---

# 16. `@Qualifier` vs `List<T>` vs `Map<String,T>`

Core mental model:

```text
@Qualifier
→ ONE specific bean
```

```text
List<T>
→ ALL beans of type T
```

```text
Map<String,T>
→ ALL beans of type T
→ key = bean name
→ value = bean instance
```

This distinction is one of the most important concepts from Day 16.

---

# 17. `List<PaymentStrategy>` Injection

A component was created:

```java
@Component
public class StrategyInspector {

    private final List<PaymentStrategy> strategies;

    public StrategyInspector(
            List<PaymentStrategy> strategies) {

        this.strategies = strategies;
    }

    public void printStrategies() {
        strategies.forEach(
                System.out::println
        );
    }
}
```

The important point:

> We did NOT manually call the constructor.

Spring did it.

---

# 18. How Spring Builds `List<T>`

Spring sees:

```java
@Component
public class StrategyInspector
```

Then inspects the constructor:

```java
StrategyInspector(
    List<PaymentStrategy> strategies
)
```

Spring asks:

```text
Which beans in the ApplicationContext
are assignable to PaymentStrategy?
```

It finds:

```text
pixStrategy
cardStrategy
```

Then conceptually builds:

```text
List<PaymentStrategy>

[
    pixStrategy,
    cardStrategy
]
```

and calls the constructor automatically.

Flow:

```text
Spring starts
    ↓
component scanning
    ↓
finds StrategyInspector
    ↓
examines constructor
    ↓
needs List<PaymentStrategy>
    ↓
finds all PaymentStrategy beans
    ↓
creates list
    ↓
injects list
```

This is Spring dependency injection.

---

# 19. Getting `StrategyInspector`

Because Spring created the component:

```java
StrategyInspector inspector =
        context.getBean(
            StrategyInspector.class
        );
```

Then:

```java
inspector.printStrategies();
```

This retrieves the already-created Spring bean.

It does NOT manually create:

```java
new StrategyInspector(...)
```

---

# 20. Lambda Strategy Output

The strategy implementations were created as lambdas.

Therefore printing them directly produced output similar to:

```text
PaymentStrategyConfig$$Lambda/...@...
```

This is expected.

There are no normal classes such as:

```text
PixPaymentStrategy
CardPaymentStrategy
```

The runtime creates lambda implementations.

The important observation was:

```text
2 PaymentStrategy beans
→ Spring injected both
→ List size = 2
```

---

# 21. `List<T>` Use Cases

Injecting:

```java
List<T>
```

is useful when you need:

```text
all implementations
processing chains
validators
handlers
plugins
strategies
processors
```

Example:

```java
List<PaymentValidator>
```

could allow every validator to execute.

---

# 22. Ordering Warning

Do not depend accidentally on collection order.

If ordering matters, Spring provides mechanisms such as:

```java
@Order
```

or:

```java
Ordered
```

Mental model:

```text
List<T>
→ gives all matching beans

Business logic should not rely
on accidental ordering.
```

---

# 23. `Map<String, PaymentStrategy>`

The second Part B exercise replaced individual strategy fields with:

```java
Map<String, PaymentStrategy>
```

Service:

```java
@Service
public class PaymentService {

    private final Map<
            String,
            PaymentStrategy
    > strategies;

    public PaymentService(
            Map<String, PaymentStrategy>
                    strategies) {

        this.strategies = strategies;
    }
}
```

Spring automatically creates something conceptually equivalent to:

```text
{
    "pixStrategy"
        → PIX strategy bean,

    "cardStrategy"
        → credit-card strategy bean
}
```

---

# 24. Map Keys

For:

```java
Map<String, PaymentStrategy>
```

Spring uses:

```text
key
→ bean name

value
→ bean instance
```

Therefore:

```java
@Bean("pixStrategy")
```

creates a map entry conceptually like:

```text
"pixStrategy" → PaymentStrategy
```

and:

```java
@Bean("cardStrategy")
```

creates:

```text
"cardStrategy" → PaymentStrategy
```

---

# 25. Refactoring `PaymentService`

Day 15:

```java
private final PaymentStrategy pix;
private final PaymentStrategy card;
```

Constructor:

```java
PaymentService(
    @Qualifier("pixStrategy")
    PaymentStrategy pix,

    @Qualifier("cardStrategy")
    PaymentStrategy card
)
```

Day 16:

```java
private final Map<
        String,
        PaymentStrategy
> strategies;
```

Constructor:

```java
public PaymentService(
        Map<String, PaymentStrategy>
                strategies) {

    this.strategies = strategies;
}
```

No individual:

```text
pix field
card field
@Qualifier
```

is required.

---

# 26. Strategy Selection From Map

The implementation determines the bean name from the domain state:

```java
String strategyName =
        switch (payment.getMethod()) {

            case PIX ->
                "pixStrategy";

            case CREDIT_CARD ->
                "cardStrategy";
        };
```

Then retrieves:

```java
PaymentStrategy strategy =
        strategies.get(strategyName);
```

Finally:

```java
return strategy.pay(payment);
```

Flow:

```text
Payment
   ↓
PaymentMethod
   ↓
switch
   ↓
bean name
   ↓
Map<String, PaymentStrategy>
   ↓
PaymentStrategy
   ↓
pay(payment)
   ↓
PaymentStatus
```

---

# 27. Missing Strategy Handling

Calling:

```java
strategies.get(strategyName)
```

may return:

```text
null
```

Therefore the implementation protects against a later:

```text
NullPointerException
```

using:

```java
if (strategy == null) {

    throw new IllegalArgumentException(
            "Strategy not found: "
            + strategyName
    );
}
```

Then:

```java
return strategy.pay(payment);
```

This produces a much clearer failure.

---

# 28. Strategy Registry

The map effectively becomes a:

```text
strategy registry
```

Conceptually:

```text
PaymentService
     ↓
Map<String, PaymentStrategy>
     ↓
strategy registry
```

Compared with Day 15:

```text
PaymentService
├── pix
└── card
```

Day 16 becomes:

```text
PaymentService
└── strategies
    ├── pixStrategy
    └── cardStrategy
```

This reduces the number of explicit strategy dependencies in the service.

---

# 29. Extensibility Benefit

Suppose a new bean is added:

```java
@Bean("paypalStrategy")
public PaymentStrategy paypalStrategy() {
    ...
}
```

Spring automatically includes it in:

```java
Map<String, PaymentStrategy>
```

The constructor does not need another parameter.

Day 15 style:

```text
new strategy
→ new field
→ new @Qualifier
→ constructor change
```

Day 16 map style:

```text
new strategy bean
→ automatically enters registry
```

Selection logic may still need to know how to choose it.

---

# 30. Important Design Tradeoff

The current implementation uses strings:

```java
"pixStrategy"
"cardStrategy"
```

This creates coupling between:

```text
domain selection logic
```

and:

```text
Spring bean names
```

For this exercise, this is intentional because it clearly demonstrates:

```text
Map<String,T>
→ bean-name lookup
```

In a more mature design, possible improvements include:

```text
enum → strategy mapping
strategy metadata
registry abstraction
supports(method) approach
```

But these were intentionally not required for Day 16.

---

# 31. Day 15 → Day 16 Evolution

Day 15:

```text
PaymentStrategy
        ↓
multiple beans
        ↓
@Qualifier
        ↓
specific strategy fields
```

Day 16:

```text
multiple PaymentStrategy beans
        ↓
List<PaymentStrategy>
or
Map<String, PaymentStrategy>
```

Meaning:

```text
@Qualifier
→ choose one

List<T>
→ receive all

Map<String,T>
→ receive all + identify by bean name
```

---

# 32. Full Day 16 Architecture

```text
PaymentStrategyConfig
│
├── pixStrategy
└── cardStrategy
        │
        ↓
Spring ApplicationContext
        │
        ├───────────────┐
        ↓               ↓
List<PaymentStrategy>   Map<String,PaymentStrategy>
        ↓               ↓
StrategyInspector       PaymentService
                            ↓
                     strategy lookup
                            ↓
                     strategy.pay()
```

Alongside:

```text
PaymentFunctions
│
├── Predicate
├── Function
├── Consumer
├── Supplier
└── BiFunction [extra]
```

---

# 33. Interview Questions

Be ready to answer:

1. What does `Predicate<T>` represent?
2. What method does `Predicate` expose?
3. What does `Function<T,R>` represent?
4. What is the difference between `Function` and `Consumer`?
5. What does `Supplier<T>` do?
6. What is the difference between `Supplier` and `Function`?
7. What does `BiFunction<T,U,R>` represent?
8. When would you use a custom functional interface instead of `Function<T,R>`?
9. What happens if multiple Spring beans implement the same interface?
10. What does `@Qualifier` solve?
11. What happens if a constructor requests `List<MyInterface>`?
12. What happens if a constructor requests `Map<String,MyInterface>`?
13. What are the keys in that map?
14. When would you prefer `List<T>`?
15. When would you prefer `Map<String,T>`?
16. Why is the map useful for strategy implementations?
17. What happens if the requested strategy name does not exist?

---

# 34. Senior Interview Answers

## Predicate vs Function vs Consumer vs Supplier

> `Predicate<T>` tests a value and returns a boolean. `Function<T,R>` transforms an input into an output. `Consumer<T>` receives a value and performs an action without returning a result. `Supplier<T>` produces a value without requiring an input.

---

## What happens when Spring injects `List<T>`?

> Spring finds all beans assignable to `T`, creates a collection containing those bean instances, and injects that collection into the dependency.

---

## What happens when Spring injects `Map<String,T>`?

> Spring finds all beans assignable to `T` and injects them into a map where each key is the Spring bean name and each value is the corresponding bean instance.

---

## `@Qualifier` vs `List<T>`

> `@Qualifier` selects a specific bean when multiple candidates exist. `List<T>` intentionally asks Spring for all beans matching that type.

---

## Why use `Map<String, PaymentStrategy>`?

> It creates a simple strategy registry. The service can select a strategy dynamically by bean name rather than keeping one field and qualifier for every strategy implementation.

---

# Quick Mental Model

```text
Predicate<T>
→ test()
→ T → boolean
```

```text
Function<T,R>
→ apply()
→ T → R
```

```text
Consumer<T>
→ accept()
→ T → void
```

```text
Supplier<T>
→ get()
→ () → T
```

```text
BiFunction<T,U,R>
→ apply(T,U)
→ (T,U) → R
```

Spring:

```text
@Qualifier
→ ONE bean
```

```text
List<T>
→ ALL beans of type T
```

```text
Map<String,T>
→ ALL beans
→ key = bean name
→ value = bean instance
```

Strategy architecture:

```text
PaymentMethod
      ↓
strategy bean name
      ↓
Map<String, PaymentStrategy>
      ↓
PaymentStrategy
      ↓
pay()
```

---

# Day 16 Completion Checklist

## Java

- [x] Understand `Predicate<T>`
- [x] Use `test()`
- [x] Understand `Function<T,R>`
- [x] Use `apply()`
- [x] Understand `Consumer<T>`
- [x] Use `accept()`
- [x] Understand `Supplier<T>`
- [x] Use `get()`
- [x] Understand basic predicate composition
- [x] Use correct `java.util.function` imports
- [x] Understand custom interface vs standard functional interface
- [x] Use `BiFunction<T,U,R>` as an extra exercise

## Spring — List Injection

- [x] Create `StrategyInspector`
- [x] Inject `List<PaymentStrategy>`
- [x] Understand that Spring creates the list automatically
- [x] Understand that constructor is called by Spring
- [x] Retrieve the Spring-managed inspector
- [x] Iterate over injected strategies

## Spring — Map Injection

- [x] Inject `Map<String, PaymentStrategy>`
- [x] Understand bean name as map key
- [x] Remove individual strategy fields
- [x] Remove individual `@Qualifier`s
- [x] Select strategy by bean name
- [x] Handle missing strategy
- [x] Execute selected strategy

## Practical

- [x] `PaymentFunctions`
- [x] Predicate validation
- [x] Function amount extraction
- [x] Consumer logging
- [x] Supplier creation
- [x] BiFunction payment creation
- [x] `StrategyInspector`
- [x] `List<PaymentStrategy>` injection
- [x] `Map<String, PaymentStrategy>` strategy registry
- [x] `PaymentService` refactor

## Remaining

- [ ] Interview round
- [ ] Manual notes
- [ ] Anki cards

**Day 16 practical implementation: complete.**