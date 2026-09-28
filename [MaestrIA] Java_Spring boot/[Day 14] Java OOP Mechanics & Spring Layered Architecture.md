# [Day 14] Java OOP Mechanics & Spring Layered Architecture

## Objective

Review and strengthen the Java OOP concepts that directly influence Spring application design:

### Java
- Access modifiers
- `static`
- Static nested classes vs inner classes
- Method overloading
- Method overriding
- Dynamic binding / runtime dispatch
- Java pass-by-value

### Spring
- Layered architecture
- Controller / Service / Repository responsibilities
- Component boundaries
- Dependency direction
- Interface-based design
- Why Spring services should generally use instance behavior rather than static methods

---

# 1. Access Modifiers

Java provides four access levels:

| Modifier | Same Class | Same Package | Subclass | Everywhere |
|---|---:|---:|---:|---:|
| `private` | ✅ | ❌ | ❌ | ❌ |
| package-private | ✅ | ✅ | limited | ❌ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `public` | ✅ | ✅ | ✅ | ✅ |

Package-private means:

```java
class PaymentValidator {
}
```

No modifier is written.

Core principle:

```text
Expose only what other components actually need.
```

This helps preserve encapsulation and clear class boundaries.

---

# 2. `private`

A private member is accessible only inside the declaring class.

```java
public class PaymentService {

    public void process() {
        validate();
    }

    private void validate() {
        // internal implementation
    }
}
```

The caller can use:

```java
service.process();
```

but cannot call:

```java
service.validate();
```

Mental model:

```text
public
→ external contract

private
→ implementation detail
```

---

# 3. Package-Private

No modifier:

```java
class PriceCalculator {
}
```

means:

```text
visible inside the same package
```

Useful when classes collaborate internally but should not become part of the public API.

---

# 4. `protected`

Example:

```java
public class PaymentProcessor {

    protected void validate() {
    }
}
```

Subclass:

```java
public class CreditCardProcessor
        extends PaymentProcessor {

    public void process() {
        validate();
    }
}
```

`protected` is primarily useful when subclasses genuinely need access to implementation details.

Do not automatically expose members as `protected` simply because inheritance exists.

---

# 5. `public`

A public member can be accessed broadly.

```java
public void process() {
}
```

Public members form part of a class's usable contract.

Changing public APIs can have greater compatibility impact than changing private implementation details.

---

# 6. `static`

A static member belongs to the class rather than to an individual object.

Example:

```java
public class PaymentUtils {

    public static final int MAX_RETRIES = 3;

    public static boolean validAmount(
            double amount) {

        return amount > 0;
    }
}
```

Usage:

```java
PaymentUtils.validAmount(100);
```

No object instance is needed.

Mental model:

```text
instance member
→ belongs to object

static member
→ belongs to class
```

---

# 7. Static Fields

Example:

```java
public class Counter {

    static int count = 0;
}
```

All instances share the same static field.

```java
Counter.count++;
```

There is one shared value for that loaded class.

In server applications, mutable static state is usually dangerous:

```java
static int currentUserId;
```

Potential problems:

```text
shared state
concurrency problems
harder testing
hidden dependencies
global coupling
```

---

# 8. Static Methods and Overriding

Static methods are not overridden in the normal polymorphic sense.

Example:

```java
class Parent {

    static void print() {
        System.out.println("Parent");
    }
}
```

```java
class Child extends Parent {

    static void print() {
        System.out.println("Child");
    }
}
```

Then:

```java
Parent obj = new Child();

obj.print();
```

prints:

```text
Parent
```

This is called:

```text
method hiding
```

not overriding.

Mental model:

```text
instance method override
→ runtime object determines method

static method
→ reference/class determines method
```

---

# 9. Nested Classes

Two important types:

```text
static nested class
inner class
```

---

# 10. Static Nested Class

Example:

```java
public class Payment {

    static class ValidationResult {

        private final boolean valid;

        ValidationResult(boolean valid) {
            this.valid = valid;
        }
    }
}
```

The nested class does not need an instance of the outer class.

Usage conceptually:

```java
Payment.ValidationResult
```

Mental model:

```text
static nested class
→ grouped inside outer class
→ no outer instance required
```

---

# 11. Inner Class

A non-static nested class is tied to an outer instance.

```java
public class Payment {

    private String id;

    class Validator {

        boolean valid() {
            return id != null;
        }
    }
}
```

`Validator` can access fields belonging to the outer `Payment` instance.

Mental model:

```text
static nested class
→ independent of outer object

inner class
→ associated with outer object
```

---

# 12. Method Overloading

Overloading means:

```text
same method name
+
different parameter list
```

Example:

```java
public void pay(double amount) {
}
```

```java
public void pay(
        double amount,
        String currency) {
}
```

The compiler determines which method to call.

Therefore:

```text
overloading
→ compile-time polymorphism
```

---

# 13. Return Type Alone Cannot Overload

Invalid:

```java
int calculate() {
    return 1;
}
```

```java
double calculate() {
    return 1.0;
}
```

Both methods have the same parameter list.

Java cannot distinguish overloaded methods using only the return type.

---

# 14. Method Overriding

Overriding happens when a subclass provides another implementation of an inherited instance method.

```java
public class PaymentGateway {

    public void pay() {
        System.out.println("Default");
    }
}
```

```java
public class StripeGateway
        extends PaymentGateway {

    @Override
    public void pay() {
        System.out.println("Stripe");
    }
}
```

Usage:

```java
PaymentGateway gateway =
    new StripeGateway();

gateway.pay();
```

Output:

```text
Stripe
```

This is runtime polymorphism.

---

# 15. Overloading vs Overriding

```text
Overloading
→ same method name
→ different parameters
→ compiler decides
→ compile-time polymorphism

Overriding
→ subclass replaces implementation
→ same method contract
→ runtime object decides
→ runtime polymorphism
```

---

# 16. Dynamic Binding

Example:

```java
PaymentGateway gateway =
    new StripeGateway();
```

Reference type:

```text
PaymentGateway
```

Runtime object:

```text
StripeGateway
```

Calling:

```java
gateway.pay();
```

executes:

```text
StripeGateway.pay()
```

because instance method dispatch depends on the runtime object.

This is:

```text
dynamic binding
dynamic dispatch
runtime polymorphism
```

---

# 17. Dynamic Binding in Spring

This concept appears constantly in Spring applications.

Example:

```java
private final PaymentGateway gateway;
```

Spring might inject:

```text
MockPaymentGateway
```

or:

```text
RealPaymentGateway
```

The service only depends on:

```text
PaymentGateway
```

but:

```java
gateway.pay();
```

executes the concrete implementation.

Therefore:

```text
Spring dependency injection
+
interface abstraction
+
dynamic dispatch
```

work naturally together.

---

# 18. Java Is Always Pass-by-Value

Java is:

> Always pass-by-value.

This is true for both primitives and object references.

---

# 19. Primitive Example

```java
int x = 10;

change(x);
```

```java
static void change(int value) {
    value = 20;
}
```

After the call:

```text
x = 10
```

The primitive value was copied into the parameter.

---

# 20. Objects Are Also Passed by Value

Example:

```java
Property property =
    new Property("Old");

change(property);
```

Method:

```java
static void change(Property p) {

    p.setName("New");
}
```

After the call:

```java
property.getName();
```

returns:

```text
New
```

This does NOT mean Java passed the object by reference.

Java copied the value of the reference.

Conceptually:

```text
caller reference
        \
         → same object
        /
copied method reference
```

Both references point to the same object.

Therefore the method can mutate the shared object.

---

# 21. Reassigning an Object Parameter

Example:

```java
static void replace(Property p) {

    p = new Property("New");
}
```

Caller:

```java
Property property =
    new Property("Old");

replace(property);
```

After the method call:

```java
property.getName();
```

still returns:

```text
Old
```

Why?

Because only the method's copy of the reference was reassigned.

Before:

```text
caller ref ──────→ Old Object
                   ↑
method ref ────────┘
```

Inside `replace()`:

```text
caller ref ──────→ Old Object

method ref ──────→ New Object
```

The caller's variable was never changed.

---

# 22. Correct Interview Explanation — Pass-by-Value

> Java is always pass-by-value. For primitives, the primitive value is copied. For objects, the value being copied is the object reference. Both references can initially point to the same object, which allows the method to mutate that object, but assigning the parameter to another object does not change the caller's reference.

Wrong explanation:

```text
Primitives are passed by value.
Objects are passed by reference.
```

Java does not use pass-by-reference for object parameters.

---

# 23. Spring Layered Architecture

A common Spring application structure is:

```text
Controller
    ↓
Service
    ↓
Repository
```

Additional boundaries can include:

```text
Domain
DTO
Mapper
External client
Gateway
```

The important concept is responsibility, not the number of layers.

---

# 24. Controller Layer

Main responsibility:

```text
HTTP / transport boundary
```

Example:

```java
@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(
            PaymentService service) {

        this.service = service;
    }
}
```

Typical responsibilities:

```text
receive HTTP request
validate request structure
convert input
call service/use case
return HTTP response
```

Avoid putting core business logic into controllers.

---

# 25. Service Layer

The service layer represents application use cases and business orchestration.

Example:

```java
@Service
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentGateway gateway;

    public PaymentService(
            PaymentRepository repository,
            PaymentGateway gateway) {

        this.repository = repository;
        this.gateway = gateway;
    }
}
```

Typical responsibilities:

```text
business rules
orchestration
transaction boundaries
use-case execution
coordination between dependencies
```

Example:

```java
public Payment process(
        Payment payment) {

    validate(payment);

    PaymentStatus status =
        gateway.pay(payment);

    payment.updateStatus(status);

    return repository.save(payment);
}
```

---

# 26. Repository Layer

The repository handles persistence concerns.

Example:

```java
@Repository
public class PaymentRepository {

    public Payment save(
            Payment payment) {

        return payment;
    }
}
```

Typical responsibilities:

```text
save data
load data
queries
persistence abstraction
```

Avoid placing HTTP concerns in the repository.

Avoid placing unrelated business orchestration there.

---

# 27. Layer Responsibilities

Core mental model:

```text
Controller
→ HTTP / transport

Service
→ business use case / orchestration

Repository
→ persistence
```

The annotations themselves do not create good architecture.

This:

```java
@RestController
@Service
@Repository
```

only describes Spring components.

Good boundaries still depend on design.

---

# 28. Bad Layering

Example:

```text
Controller
    ↓
Repository
```

This is not automatically wrong for every trivial CRUD application, but in non-trivial domains it often causes:

```text
business logic in controller
persistence knowledge in web layer
tight coupling
harder testing
poor boundaries
```

---

# 29. Too Many Layers Is Also Bad

This is not automatically better:

```text
Controller
→ Service
→ Manager
→ Helper
→ Processor
→ Utility
→ Repository
```

Architecture is not measured by number of classes.

Good architecture means:

```text
clear responsibility
clear ownership
clear boundaries
clear dependency direction
```

---

# 30. Component Boundaries

A component boundary defines:

```text
what a component is responsible for
what it is allowed to know
what it exposes
what it hides
```

Example interface:

```java
public interface PaymentGateway {

    PaymentStatus pay(
        Payment payment
    );
}
```

`PaymentService` can depend on:

```text
PaymentGateway
```

without knowing:

```text
Stripe SDK
HTTP implementation
JSON format
API keys
network details
```

The concrete implementation owns those details.

---

# 31. Dependency Direction

A useful conceptual direction is:

```text
Controller
    ↓
Service
    ↓
Domain / abstractions
    ↓
Repository / external adapters
```

The service should generally not depend directly on web concepts such as:

```text
ResponseEntity
HTTP 404
HttpServletRequest
JSON
```

Those belong near the HTTP boundary.

---

# 32. DTO Boundary

External representations should not automatically become internal domain models.

Conceptually:

```text
HTTP Request DTO
      ↓
Controller / mapping
      ↓
Domain / application model
```

Mental model:

```text
external representation
!=
internal domain model
```

DTOs and records will be studied more deeply later in the roadmap.

---

# 33. Static Methods in Spring Services

Example:

```java
@Service
public class PaymentService {

    public static void process() {
    }
}
```

This usually works against Spring's object-management model.

Static methods belong to the class rather than the bean instance.

Spring primarily manages:

```text
bean instances
dependencies
lifecycle
proxies
```

Prefer:

```java
@Service
public class PaymentService {

    private final PaymentGateway gateway;

    public void process() {
    }
}
```

This becomes especially important for Spring features such as:

```text
@Transactional
@Async
AOP
```

which commonly rely on managed bean instances and proxies.

---

# 34. Java → Spring Connection

Today's Java topics directly support Spring architecture:

```text
Access modifiers
→ protect component boundaries

static
→ understand class vs bean instance

overriding
→ interchangeable implementations

dynamic binding
→ interface-based dependency injection

pass-by-value
→ understand object mutation

layered architecture
→ organize responsibilities correctly
```

---

# 35. Practical Exercise

Suggested project:

```text
day14_oop_layers
```

Structure:

```text
controller/
    PaymentController.java

service/
    PaymentService.java

repository/
    PaymentRepository.java

domain/
    Payment.java
    PaymentStatus.java

gateway/
    PaymentGateway.java
    MockPaymentGateway.java
```

---

# 36. PaymentGateway

```java
public interface PaymentGateway {

    PaymentStatus pay(
        Payment payment
    );
}
```

---

# 37. MockPaymentGateway

```java
@Component
public class MockPaymentGateway
        implements PaymentGateway {

    @Override
    public PaymentStatus pay(
            Payment payment) {

        return PaymentStatus.APPROVED;
    }
}
```

This demonstrates:

```text
interface abstraction
+
overriding
+
dynamic binding
```

---

# 38. PaymentRepository

A simple repository is enough for this exercise:

```java
@Repository
public class PaymentRepository {

    public Payment save(
            Payment payment) {

        System.out.println(
            "Saving payment"
        );

        return payment;
    }
}
```

No database/JPA is required today.

---

# 39. PaymentService Requirements

Implement:

```text
constructor injection
public process(...)
private validation helper
call PaymentGateway
update PaymentStatus
save through repository
```

The service should contain application/business orchestration.

It should not contain:

```text
HTTP status codes
ResponseEntity
request parsing
```

---

# 40. PaymentController Requirements

Controller should:

```text
receive request
call PaymentService
return response
```

It should not contain the payment business flow itself.

---

# 41. Pass-by-Value Experiment

Create:

```java
static void replace(
        Payment payment) {

    payment =
        new Payment(...);
}
```

Then:

```java
Payment original = ...;

replace(original);
```

The caller still references the original object.

Now:

```java
static void mutate(
        Payment payment) {

    payment.setStatus(
        PaymentStatus.APPROVED
    );
}
```

The mutation is visible to the caller because both reference values point to the same object.

Mental model:

```text
replace parameter
→ caller reference unchanged

mutate referenced object
→ caller sees mutation
```

Both behaviors are consistent with:

```text
Java is always pass-by-value.
```

---

# 42. Interview Questions

1. What are Java's access modifiers?
2. What is package-private?
3. Static member vs instance member?
4. Can static methods be overridden?
5. Static nested class vs inner class?
6. What is method overloading?
7. What is method overriding?
8. Overloading vs overriding?
9. What is dynamic binding?
10. Why does `PaymentGateway gateway = new StripeGateway()` call the Stripe implementation?
11. Is Java pass-by-value or pass-by-reference?
12. Why can a method mutate an object when Java uses pass-by-value?
13. Why can't parameter reassignment replace the caller's object reference?
14. What belongs in a controller?
15. What belongs in a service?
16. What belongs in a repository?
17. What is a component boundary?
18. Why should Spring services generally avoid static business methods?

---

# 43. Senior Interview Answers

## Overloading vs Overriding

> Overloading means using the same method name with different parameter lists, and method selection occurs at compile time. Overriding means a subclass provides another implementation of an inherited instance method, and the runtime object determines which implementation executes.

## Dynamic Binding

> Dynamic binding means that an overridden instance method is selected based on the runtime type of the object rather than only the declared reference type. This is what allows code to depend on an interface while executing the concrete injected implementation.

## Pass-by-Value

> Java is always pass-by-value. With object parameters, Java copies the reference value. The copied reference can point to the same object and mutate it, but reassigning the parameter does not change the caller's reference.

## Layered Architecture

> A controller should primarily handle transport concerns, a service should coordinate application use cases and business rules, and a repository should handle persistence. The goal is to keep responsibilities and dependencies clear rather than simply adding layers.

---

# Quick Mental Model

```text
Overloading
→ compile time
```

```text
Overriding
→ runtime
```

```text
static method
→ hidden, not overridden
```

```text
dynamic binding
→ runtime object determines overridden method
```

```text
Java
→ always pass-by-value
```

```text
object argument
→ copied reference value
→ same underlying object
```

```text
Controller
→ HTTP

Service
→ business/use case

Repository
→ persistence
```

```text
Good boundary
→ expose what is needed
→ hide implementation details
```

---

# Day 14 Completion Checklist

## Java

- [x] Review access modifiers
- [x] Understand `static`
- [x] Understand static method hiding
- [x] Understand static nested vs inner classes
- [x] Understand overloading
- [x] Understand overriding
- [x] Understand dynamic binding
- [x] Understand Java pass-by-value
- [x] Understand object mutation vs parameter reassignment

## Spring

- [x] Understand Controller responsibility
- [x] Understand Service responsibility
- [x] Understand Repository responsibility
- [x] Understand component boundaries
- [x] Understand dependency direction
- [x] Connect interfaces and dynamic binding to DI
- [x] Understand why static business methods are usually inappropriate in Spring services

**Day 14 theory: complete.**