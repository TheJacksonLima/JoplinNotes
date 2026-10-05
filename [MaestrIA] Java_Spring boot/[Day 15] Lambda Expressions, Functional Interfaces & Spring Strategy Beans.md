# [Day 15] Lambda Expressions, Functional Interfaces & Spring Strategy Beans

## Objective

Understand how a lambda implements behavior and how Spring can own and inject interchangeable behaviors (the Strategy pattern).

### Java
- What a lambda expression is
- Functional interfaces and `@FunctionalInterface`
- Lambda syntax variations
- Target typing
- Effectively final captured variables
- Lambda vs anonymous class (`this`, scope, bytecode)
- Checked exceptions inside lambdas
- Preview of `java.util.function` (Day 16)

### Spring
- Strategy pattern
- Declaring strategies as `@Bean` lambdas
- Bean names and `@Qualifier`
- Selecting a strategy with a `switch` expression
- Injecting `List<T>` and `Map<String, T>` of beans
- Replacing a `switch` with a strategy map

**Code:** `src/spring/day15_lambdas_strategies`

---

# 1. What Is a Lambda?

A lambda is a **short implementation of a single abstract method**.

Before Java 8, passing behavior required a class:

```java
PaymentStrategy pix = new PaymentStrategy() {
    @Override
    public PaymentStatus pay(Payment payment) {
        return PaymentStatus.APPROVED;
    }
};
```

With a lambda:

```java
PaymentStrategy pix = payment -> PaymentStatus.APPROVED;
```

The lambda has no name and no class declaration. The compiler knows which method it implements from the **target type** (`PaymentStrategy`).

---

# 2. Functional Interface

A functional interface is an interface with **exactly one abstract method**.

```java
@FunctionalInterface
public interface PaymentStrategy {
    PaymentStatus pay(Payment payment);
}
```

Allowed in addition to the single abstract method:

- `default` methods
- `static` methods
- methods that override `Object` methods (`equals`, `toString`, ...)

```java
@FunctionalInterface
public interface PaymentStrategy {
    PaymentStatus pay(Payment payment);

    default boolean supportsRefund() { return false; }   // still functional
}
```

---

# 3. `@FunctionalInterface`

The annotation is **optional**. Any interface with one abstract method can be a lambda target.

What it adds:

- The compiler fails if someone adds a second abstract method.
- It documents intent to readers.

```java
@FunctionalInterface
public interface PaymentStrategy {
    PaymentStatus pay(Payment payment);
    void refund(Payment payment);   // compile error: not a functional interface
}
```

---

# 4. Lambda Syntax

```java
// no parameters
Runnable r = () -> System.out.println("run");

// one parameter, type inferred, parentheses optional
PaymentStrategy s1 = payment -> PaymentStatus.APPROVED;

// explicit type
PaymentStrategy s2 = (Payment payment) -> PaymentStatus.APPROVED;

// block body: needs braces and return
PaymentStrategy s3 = payment -> {
    System.out.println("Processing PIX");
    return PaymentStatus.APPROVED;
};

// two parameters
Comparator<Payment> byAmount = (a, b) -> a.getAmount().compareTo(b.getAmount());
```

Rule of thumb:

```text
single expression → no braces, no return
multiple statements → braces + return
```

---

# 5. Target Typing

A lambda has no type of its own. Its type comes from the context:

```java
PaymentStrategy strategy = payment -> PaymentStatus.APPROVED;   // PaymentStrategy
Function<Payment, PaymentStatus> fn = payment -> PaymentStatus.APPROVED;   // Function
```

Same lambda text, two different types.

This also means:

```java
var x = payment -> PaymentStatus.APPROVED;   // compile error: no target type
```

---

# 6. Effectively Final Captures

A lambda can use local variables from the enclosing method only if they are **final or effectively final** (never reassigned).

```java
BigDecimal limit = new BigDecimal("1000");

PaymentStrategy card = payment ->
        payment.getAmount().compareTo(limit) <= 0
                ? PaymentStatus.APPROVED
                : PaymentStatus.PENDING;

// limit = BigDecimal.TEN;   // would break compilation of the lambda above
```

Why: the lambda captures a **copy of the value**. Allowing reassignment would make it unclear which value the lambda sees, especially if it runs later or on another thread.

Note: fields (`this.x`) are not restricted. Only local variables and parameters.

---

# 7. Lambda vs Anonymous Class

| | Lambda | Anonymous class |
|---|---|---|
| Target | functional interface only | any interface or abstract class |
| `this` | the enclosing instance | the anonymous object itself |
| New scope | no (cannot shadow local vars) | yes |
| State (fields) | none | can have fields |
| Bytecode | `invokedynamic`, no extra `.class` file | a separate `Outer$1.class` |

Interesting detail from your own project: `target/classes` contains `PaymentService$1.class`. That is **not** a lambda. The compiler generates that synthetic class for the `switch` on the `PaymentMethod` enum (a lookup table of ordinals). Lambdas in `PaymentStrategyConfig` do **not** produce `$1` class files.

---

# 8. Checked Exceptions in Lambdas

A lambda may only throw checked exceptions that the functional interface method declares.

```java
PaymentStrategy s = payment -> {
    Thread.sleep(100);   // compile error: InterruptedException not declared by pay()
    return PaymentStatus.APPROVED;
};
```

Options:

- Catch inside the lambda and wrap in an unchecked exception.
- Declare `throws` in your own functional interface (only when callers really must handle it).

---

# 9. Preview: `java.util.function`

Java already ships generic functional interfaces (Day 16 goes deeper):

| Interface | Method | Shape |
|---|---|---|
| `Function<T,R>` | `apply` | T → R |
| `Predicate<T>` | `test` | T → boolean |
| `Consumer<T>` | `accept` | T → void |
| `Supplier<T>` | `get` | () → T |
| `UnaryOperator<T>` | `apply` | T → T |

`PaymentStrategy` is structurally a `Function<Payment, PaymentStatus>`. A custom interface is still better here because the name documents the domain concept, and Spring can inject it by a clear type.

---

# 10. Strategy Pattern

Strategy = define a family of interchangeable behaviors behind one interface, and choose one at runtime.

```text
PaymentService
    └── PaymentStrategy (interface)
            ├── PIX strategy
            └── Credit card strategy
```

The service does not know how each payment method works. It only knows how to pick one and call `pay()`.

---

# 11. Strategies as `@Bean` Lambdas

From your project:

```java
@Configuration
public class PaymentStrategyConfig {

    @Bean("pixStrategy")
    public PaymentStrategy pixStrategy() {
        return payment -> {
            System.out.println("Processing PIX");
            return PaymentStatus.APPROVED;
        };
    }

    @Bean("cardStrategy")
    public PaymentStrategy cardStrategy() {
        return payment -> {
            System.out.println("Processing credit card");
            return PaymentStatus.PENDING;
        };
    }
}
```

Key points:

- A lambda has no class to annotate with `@Component`, so it must be registered through a `@Bean` method.
- The bean type is `PaymentStrategy`.
- The bean name defaults to the **method name**, so `@Bean("pixStrategy")` is redundant here. Plain `@Bean` gives the same name.

---

# 12. Bean Names and `@Qualifier`

There are two beans of type `PaymentStrategy`. Injecting one by type alone is ambiguous:

```text
NoUniqueBeanDefinitionException: expected single matching bean but found 2: pixStrategy, cardStrategy
```

`@Qualifier` selects by name:

```java
public PaymentService(@Qualifier("pixStrategy") PaymentStrategy pix,
                      @Qualifier("cardStrategy") PaymentStrategy card) { ... }
```

Bug you hit in this lab:

```java
@Bean("pixStragegy")   // typo in the bean name
...
@Qualifier("pixStrategy")   // asks for a name that does not exist
```

Result: `NoSuchBeanDefinitionException`. Bean names are plain strings, so typos are only caught at startup.

Second bug you hit: `PaymentService` had no `@Service`, so `context.getBean(PaymentService.class)` failed. A class is only a bean if Spring is told about it (stereotype annotation or `@Bean` method).

---

# 13. Selecting with a `switch` Expression

```java
public PaymentStatus process(Payment payment) {
    PaymentStrategy strategy = switch (payment.getMethod()) {
        case PIX -> pix;
        case CREDIT_CARD -> card;
    };
    return strategy.pay(payment);
}
```

Good:

- The `switch` over an enum is exhaustive: no `default` needed, and adding a new enum constant causes a compile error here.

Limitation:

- Adding a new payment method requires editing `PaymentService` (a new field, a new constructor parameter, a new `case`). This violates the Open/Closed Principle.

---

# 14. Injecting `List<T>`

Spring can inject **all** beans of a type:

```java
@Service
public class PaymentService {
    private final List<PaymentStrategy> strategies;

    public PaymentService(List<PaymentStrategy> strategies) {
        this.strategies = strategies;
    }
}
```

Order follows `@Order` / `Ordered` if present, otherwise registration order.

Useful for "run all" behavior (validators, pipeline steps). For "pick one", each strategy needs to say what it supports.

---

# 15. Injecting `Map<String, T>`

```java
public PaymentService(Map<String, PaymentStrategy> strategies) { ... }
```

Spring fills the map as:

```text
"pixStrategy"  → PIX lambda
"cardStrategy" → card lambda
```

Keys are bean names. It works, but string keys are fragile, as your typo showed.

---

# 16. Better: A Typed Strategy Map

Build an `EnumMap` keyed by the domain enum:

```java
@Configuration
public class PaymentStrategyConfig {

    @Bean
    public Map<PaymentMethod, PaymentStrategy> paymentStrategies() {
        Map<PaymentMethod, PaymentStrategy> map = new EnumMap<>(PaymentMethod.class);
        map.put(PaymentMethod.PIX, payment -> PaymentStatus.APPROVED);
        map.put(PaymentMethod.CREDIT_CARD, payment -> PaymentStatus.PENDING);
        return Map.copyOf(map);
    }
}
```

```java
@Service
public class PaymentService {
    private final Map<PaymentMethod, PaymentStrategy> strategies;

    public PaymentService(Map<PaymentMethod, PaymentStrategy> strategies) {
        this.strategies = strategies;
    }

    public PaymentStatus process(Payment payment) {
        PaymentStrategy strategy = strategies.get(payment.getMethod());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported method: " + payment.getMethod());
        }
        return strategy.pay(payment);
    }
}
```

Trade-off vs the `switch`:

| | `switch` | Strategy map |
|---|---|---|
| Missing strategy detected | compile time | runtime |
| Adding a method | edit service | edit config only |
| Best when | few, stable options | options grow or come from config |

Alternative for class-based strategies: add `PaymentMethod supports()` to the interface and build the map from `List<PaymentStrategy>`.

---

# 17. Lambda Bean vs Class Bean

| Lambda `@Bean` | Class `@Component` |
|---|---|
| Very short behavior | Real logic, several dependencies |
| No extra file | Own file, easy to unit test |
| Hard to name in stack traces | Clear class name in logs |

Senior guideline: lambdas for small behavior, classes once a strategy needs its own dependencies (gateway client, repository, logger) or tests.

---

# 18. Java → Spring Connection

```text
functional interface
→ the contract a strategy bean implements

lambda
→ a compact bean implementation inside @Bean

target typing
→ bean type = the functional interface

effectively final
→ strategies should be stateless, like singleton beans

List<T> / Map<K,T> injection
→ register new behavior without editing the consumer
```

---

# 19. Practical Exercise (from Roadmap Verify)

1. Run the mini-lab:

```java
Function<Double, Double> addTax = price -> price * 1.10;
System.out.println(addTax.apply(100.0));
```

2. Create `PricingStrategy` as a functional interface.
3. Register two beans (e.g. `regularPricing`, `blackFridayPricing`).
4. Inject `List<PricingStrategy>` into a service and print each result.
5. Break something on purpose: remove `@Qualifier`, or add a second abstract method to the interface, and read the error.

---

# 20. Interview Questions

1. What is a lambda expression?
2. What is a functional interface?
3. Is `@FunctionalInterface` required? What does it do?
4. Can a functional interface have default methods?
5. What is target typing?
6. Why must captured local variables be effectively final?
7. What does `this` mean inside a lambda vs an anonymous class?
8. How do lambdas handle checked exceptions?
9. What is the Strategy pattern?
10. How do you register a lambda as a Spring bean?
11. What happens when two beans of the same type exist and you inject one?
12. How do you inject all implementations of an interface?
13. What keys does Spring use when injecting `Map<String, T>`?
14. `switch` vs strategy map: trade-offs?
15. When would you use a class instead of a lambda for a strategy?

---

# 21. Senior Interview Answers

## Lambda and Functional Interface

> A lambda is an anonymous implementation of the single abstract method of a functional interface. The lambda itself has no type; the compiler infers it from the target type. `@FunctionalInterface` is optional but makes the compiler enforce the single-abstract-method rule.

## Effectively Final

> Lambdas capture the values of local variables, not the variables themselves. Requiring them to be effectively final avoids confusing semantics and race conditions when the lambda runs later or on another thread.

## Strategy with Spring

> I define the behavior as an interface and let Spring own the implementations. The consumer receives either a specific bean via `@Qualifier`, or a collection or map of all implementations, so new behavior can be added without changing the consumer. For tiny behaviors I use lambdas in `@Bean` methods; when a strategy has dependencies or needs its own tests, I make it a class.

## Multiple Beans of the Same Type

> Injecting one bean by type when several exist throws `NoUniqueBeanDefinitionException`. I resolve it with `@Qualifier`, `@Primary`, or by injecting `List<T>` or `Map<String, T>` and selecting at runtime.

---

# Quick Mental Model

```text
functional interface
→ exactly one abstract method
```

```text
lambda
→ implementation of that one method
→ type comes from the target
```

```text
captured local variable
→ must be effectively final
```

```text
lambda this
→ enclosing object
```

```text
Strategy
→ one interface, many interchangeable behaviors
```

```text
2 beans of same type + inject 1
→ NoUniqueBeanDefinitionException
→ @Qualifier / @Primary / List / Map
```

```text
Map<String, T> injection
→ keys are bean names
```

---

# Day 15 Completion Checklist

## Java

- [x] Explain what a lambda is
- [x] Explain functional interfaces and `@FunctionalInterface`
- [x] Use each lambda syntax form
- [x] Explain target typing
- [x] Explain effectively final captures
- [x] Compare lambda vs anonymous class
- [x] Handle a checked exception inside a lambda

## Spring

- [x] Register strategies as `@Bean` lambdas
- [x] Resolve multiple beans with `@Qualifier`
- [x] Select a strategy with a `switch` expression
- [x] Inject `List<PricingStrategy>` (roadmap Verify)
- [x] Refactor to a `Map<PaymentMethod, PaymentStrategy>`
- [x] Explain `switch` vs strategy map trade-offs
