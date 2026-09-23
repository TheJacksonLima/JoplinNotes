# [Day 12] Java Enums, Switch Expressions & Spring Profiles

## Objective

Understand and practice:

### Java
- Enums as type-safe finite states
- Enum fields and methods
- Enum comparison
- `values()` and `valueOf()`
- Switch expressions
- Exhaustive enum handling
- `yield`

### Spring
- Spring Profiles
- `@Profile`
- Profile-specific property files
- `@ConditionalOnProperty`
- Selecting bean implementations by environment/configuration

---

# 1. Java Enums

An enum represents a **finite set of valid values**.

Example:

```java
public enum PaymentStatus {
    PENDING,
    APPROVED,
    REJECTED,
    CANCELED,
    REFUNDED
}
```

Instead of using:

```java
String status = "APPROVED";
```

we use:

```java
PaymentStatus status =
    PaymentStatus.APPROVED;
```

This provides type safety.

Mental model:

```text
String
→ arbitrary values

enum
→ controlled set of valid values
```

Typical enum use cases:

```text
PaymentStatus
OrderStatus
UserRole
PropertyType
TransactionType
Environment
```

---

# 2. Enum Is a Real Java Type

An enum is not only a list of constants.

```java
PaymentStatus status;
```

Valid:

```java
status = PaymentStatus.APPROVED;
```

Invalid:

```java
status = "APPROVED";
```

The compiler protects the domain.

---

# 3. Comparing Enums

Enums can safely be compared with:

```java
==
```

Example:

```java
if (status == PaymentStatus.APPROVED) {
    System.out.println("Payment approved");
}
```

This is appropriate because enum constants are predefined singleton-like instances.

---

# 4. Enum Utility Methods

## `values()`

Returns all enum constants:

```java
for (PaymentStatus status :
        PaymentStatus.values()) {

    System.out.println(status);
}
```

## `valueOf()`

Converts the enum constant name into the enum:

```java
PaymentStatus status =
    PaymentStatus.valueOf("APPROVED");
```

Important:

```java
PaymentStatus.valueOf("approved");
```

throws:

```text
IllegalArgumentException
```

because `valueOf()` is case-sensitive.

---

# 5. Enums Can Contain Behavior

Enums can contain:

```text
fields
constructors
methods
business behavior
```

In the Day 12 implementation:

```java
public enum PaymentStatus {

    PENDING,
    APPROVED,
    REJECTED,
    CANCELED,
    REFUNDED;

    public String describe() {

        return switch (this) {

            case PENDING ->
                "Payment pending";

            case APPROVED ->
                "Payment approved";

            case REJECTED ->
                "Payment rejected";

            case CANCELED ->
                "Payment canceled";

            case REFUNDED ->
                "Payment refunded";
        };
    }
}
```

Usage:

```java
PaymentStatus.APPROVED.describe();
```

Result:

```text
Payment approved
```

This is better than passing another `PaymentStatus` into the enum because the behavior belongs to the current enum instance.

---

# 6. Traditional Switch vs Switch Expression

Traditional switch:

```java
switch (status) {

    case APPROVED:
        System.out.println("Approved");
        break;

    case REJECTED:
        System.out.println("Rejected");
        break;
}
```

Problems:

```text
break required
possible fall-through
more verbose
does not naturally return a value
```

Modern Java switch expression:

```java
String message = switch (status) {

    case APPROVED ->
        "Approved";

    case REJECTED ->
        "Rejected";

    default ->
        "Other";
};
```

The switch itself returns a value.

---

# 7. Exhaustive Switch with Enum

Enums work especially well with switch expressions.

Example:

```java
return switch (this) {

    case PENDING ->
        "Payment pending";

    case APPROVED ->
        "Payment approved";

    case REJECTED ->
        "Payment rejected";

    case CANCELED ->
        "Payment canceled";

    case REFUNDED ->
        "Payment refunded";
};
```

No `default` is needed because every enum constant is explicitly handled.

Important advantage:

If a new enum constant is added:

```java
FAILED
```

the compiler can force us to update the switch.

Mental model:

```text
enum
→ finite state space

switch expression
→ exhaustive state handling
```

---

# 8. Why Avoid `default` Sometimes?

Example:

```java
switch (status) {

    case APPROVED -> ...
    case REJECTED -> ...

    default -> ...
}
```

If another enum value is later added, `default` can silently handle it.

That may hide missing business logic.

For domain enums where every state matters, explicit exhaustive handling is often better.

---

# 9. Multiple Case Labels

Cases can share behavior:

```java
String result = switch (status) {

    case APPROVED ->
        "Successful";

    case REJECTED, CANCELED ->
        "Not successful";

    case PENDING ->
        "Processing";

    case REFUNDED ->
        "Refunded";
};
```

---

# 10. `yield`

When a switch branch needs multiple statements:

```java
double fee = switch (status) {

    case APPROVED -> 0.0;

    case PENDING -> {

        System.out.println(
            "Calculating fee"
        );

        double result = 10.0;

        yield result;
    }

    default -> 5.0;
};
```

Important:

```text
return
→ returns from method

yield
→ returns value from switch branch
```

---

# 11. Day 12 Payment Domain

The implementation uses:

```java
public interface PaymentGateway {

    PaymentStatus pay();
}
```

This allows different gateway implementations while keeping the same contract.

Flow:

```text
PaymentGateway
      ↓
pay()
      ↓
PaymentStatus
      ↓
describe()
      ↓
switch expression
```

---

# 12. PaymentService

The service depends on the interface:

```java
@Service
public class PaymentService {

    private final PaymentGateway gateway;

    public PaymentService(
            PaymentGateway gateway) {

        this.gateway = gateway;
    }

    public void process() {

        PaymentStatus status =
            gateway.pay();

        System.out.println(
            status.describe()
        );
    }
}
```

This demonstrates:

```text
constructor injection
+
interface abstraction
+
enum result
+
switch expression
```

---

# 13. Spring Profiles

Profiles allow Spring to activate different beans or configuration depending on the environment.

Common examples:

```text
dev
test
prod
```

Mental model:

```text
same application
+
different environment
=
different configuration / beans
```

---

# 14. Activating a Profile

Example:

```properties
spring.profiles.active=dev
```

Now the active profile is:

```text
dev
```

---

# 15. `@Profile`

Development gateway:

```java
@Component
@Profile("dev")
public class MockPaymentGateway
        implements PaymentGateway {

    @Override
    public PaymentStatus pay() {

        System.out.println(
            "Pay via MockPaymentGateway"
        );

        return PaymentStatus.APPROVED;
    }
}
```

Production gateway:

```java
@Component
@Profile("prod")
public class RealPaymentGateway
        implements PaymentGateway {

    @Override
    public PaymentStatus pay() {

        System.out.println(
            "Pay via RealPaymentGateway"
        );

        return PaymentStatus.PENDING;
    }
}
```

Spring decides which implementation becomes a bean based on the active profile.

---

# 16. Dependency Injection with Profiles

`PaymentService` depends only on:

```java
PaymentGateway
```

It does not know whether the implementation is:

```text
MockPaymentGateway
or
RealPaymentGateway
```

Spring decides that.

Flow:

```text
spring.profiles.active
        ↓
@Profile
        ↓
Spring selects bean
        ↓
PaymentGateway
        ↓
PaymentService
```

This is an important application of Dependency Inversion.

---

# 17. Profile-Specific Property Files

Spring Boot supports profile-specific configuration:

```text
application.properties
application-dev.properties
application-prod.properties
```

Current example:

```properties
# application.properties

spring.profiles.active=dev
```

Production-specific configuration:

```properties
# application-prod.properties

service.kafka.enabled=true
```

When:

```text
prod
```

is active, Spring loads the production-specific properties.

Mental model:

```text
application.properties
→ base configuration

application-prod.properties
→ overrides/additions for prod
```

---

# 18. `@ConditionalOnProperty`

`@ConditionalOnProperty` creates a bean only when a configuration condition matches.

Current Day 12 implementation:

```java
@Component
@Profile("prod")
@ConditionalOnProperty(
    prefix = "service.kafka",
    name = "enabled",
    havingValue = "true"
)
public class RealPaymentGateway
        implements PaymentGateway {
}
```

Corresponding property:

```properties
service.kafka.enabled=true
```

Full property name:

```text
prefix
+
name
=
service.kafka.enabled
```

---

# 19. Combining `@Profile` and `@ConditionalOnProperty`

The production gateway currently requires both conditions:

```text
@Profile("prod")
AND
service.kafka.enabled=true
```

So Spring creates `RealPaymentGateway` only if both conditions match.

Flow:

```text
prod active?
     ↓ yes

service.kafka.enabled=true?
     ↓ yes

RealPaymentGateway bean created
```

If one condition fails:

```text
RealPaymentGateway is not created
```

---

# 20. Important Consequence

Because `PaymentService` requires:

```java
PaymentGateway
```

if no `PaymentGateway` bean exists, Spring cannot create `PaymentService`.

Example:

```text
active profile = prod

service.kafka.enabled = false
```

Then:

```text
MockPaymentGateway
→ disabled because profile != dev

RealPaymentGateway
→ disabled because property != true
```

Result:

```text
no PaymentGateway bean
        ↓
PaymentService dependency missing
        ↓
application startup failure
```

This demonstrates an important Spring principle:

> Conditional beans affect the entire dependency graph.

---

# 21. `@Profile` vs `@ConditionalOnProperty`

## `@Profile`

Think:

```text
Which environment am I running in?
```

Examples:

```text
dev
test
prod
```

Use:

```java
@Profile("prod")
```

---

## `@ConditionalOnProperty`

Think:

```text
Is this configuration or capability enabled?
```

Example:

```properties
service.kafka.enabled=true
```

Use:

```java
@ConditionalOnProperty(...)
```

Mental model:

```text
@Profile
→ environment condition

@ConditionalOnProperty
→ configuration/property condition
```

---

# 22. Spring Boot Main Class

The Day 12 application starts a real Spring context:

```java
@SpringBootApplication
public class Main {

    public static void main(
            String[] args)
            throws Exception {

        var ctx =
            SpringApplication.run(
                Main.class,
                args
            );

        ctx.getBean(
            PaymentService.class
        ).process();
    }
}
```

This verifies that:

```text
component scanning works
profiles work
conditional beans work
constructor injection works
```

---

# 23. Current Dev Execution

With:

```properties
spring.profiles.active=dev
```

Spring chooses:

```text
MockPaymentGateway
```

Execution:

```text
PaymentService
        ↓
MockPaymentGateway.pay()
        ↓
PaymentStatus.APPROVED
        ↓
describe()
        ↓
Payment approved
```

Expected output:

```text
Pay via MockPaymentGateway
Payment approved
```

---

# 24. Current Prod Execution

If:

```properties
spring.profiles.active=prod
```

and:

```properties
service.kafka.enabled=true
```

Spring chooses:

```text
RealPaymentGateway
```

Execution:

```text
PaymentService
        ↓
RealPaymentGateway.pay()
        ↓
PaymentStatus.PENDING
        ↓
describe()
        ↓
Payment pending
```

Expected output:

```text
Pay via RealPaymentGateway
Payment pending
```

---

# 25. Key Interview Concepts

## Why use enum instead of String?

Enum provides:

```text
type safety
finite valid values
compiler support
better domain modeling
```

---

## Why use switch expressions?

They provide:

```text
clearer syntax
return values
no accidental fall-through
exhaustiveness with enums
```

---

## Why avoid `default` with domain enums?

Because explicit cases allow the compiler to reveal newly added enum values that have not been handled.

---

## What does `@Profile` do?

It makes bean registration conditional on the active Spring profile.

---

## What does `@ConditionalOnProperty` do?

It makes bean registration conditional on a configuration property.

---

## Difference between them?

```text
@Profile
→ environment

@ConditionalOnProperty
→ specific configuration condition
```

---

# 26. Senior-Level Nuances

### Enum

Enum constants are predefined instances.

They can contain:

```text
state
methods
constructors
behavior
```

---

### Switch Expression

A switch expression:

```java
return switch (...) {
    ...
};
```

returns a value.

Arrow syntax:

```java
case VALUE -> ...
```

does not fall through.

---

### Profiles

Profiles are useful for environment differences but should not become a replacement for every feature flag.

---

### Conditional Beans

Conditional beans influence dependency injection.

If a required bean is excluded by a condition, dependent beans may fail to initialize.

---

# Quick Mental Model

```text
Enum
→ finite valid states
```

```text
Switch expression
→ exhaustive state handling
```

```text
@Profile
→ environment-based bean selection
```

```text
@ConditionalOnProperty
→ property-based bean selection
```

```text
PaymentGateway
        ↓
profile chooses implementation
        ↓
pay()
        ↓
PaymentStatus
        ↓
switch expression
```

---

# Day 12 Practical Implementation

Implemented:

```text
PaymentStatus enum
    ↓
describe() using switch(this)

PaymentGateway interface
    ↓
MockPaymentGateway
    @Profile("dev")

RealPaymentGateway
    @Profile("prod")
    @ConditionalOnProperty(...)

PaymentService
    ↓
constructor-injected PaymentGateway
    ↓
PaymentStatus result

Spring Boot Main
    ↓
starts context
    ↓
gets PaymentService
    ↓
process()
```

---

# Completion Checklist

## Java

- [x] Understand enum use cases
- [x] Understand enum type safety
- [x] Compare enums with `==`
- [x] Understand enum methods
- [x] Understand switch expressions
- [x] Understand exhaustive enum switches
- [x] Understand why avoiding `default` can be useful
- [x] Understand `yield`
- [x] Implement enum behavior with `switch (this)`

## Spring

- [x] Understand Spring Profiles
- [x] Use `@Profile`
- [x] Create dev/prod bean implementations
- [x] Use profile-specific property files
- [x] Use `@ConditionalOnProperty`
- [x] Inject profile-selected bean through interface
- [x] Run implementation through Spring Boot context

**Day 12 practical implementation: complete.**