---
title: "[Day 02] Spring Core: IoC, Dependency Injection, Beans, and\_`ApplicationContex"
updated: 2026-09-01 15:34:31Z
created: 2026-09-01 15:33:51Z
latitude: -25.42016050
longitude: -49.27616110
altitude: 0.0000
---

\[Day 02\] Spring Core: IoC, Dependency Injection, Beans, and `ApplicationContext`.

## Official Spring documentation

Start here:

[Spring — Introduction to the IoC Container and Beans](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html?utm_source=chatgpt.com)

Then read:

[Spring — Container Overview / ApplicationContext](https://docs.spring.io/spring-framework/reference/core/beans/basics.html?utm_source=chatgpt.com)

And finally:

[Spring — Dependency Injection](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html?utm_source=chatgpt.com)

You **do not need to read the entire IoC chapter today**. Those three sections are enough.

* * *

# Day 2 — Spring Core

## 1\. Inversion of Control — IoC

Without Spring, a class might create its own dependencies:

```java
public class OrderService {

    private final PaymentService paymentService;

    public OrderService() {
        this.paymentService = new PaymentService();
    }
}
```

`OrderService` controls the creation of `PaymentService`.

This creates strong coupling:

```text
OrderService
     │
     └── creates → PaymentService
```

With **Inversion of Control**, that responsibility is moved outside the class.

```java
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Now `OrderService` says:

> "I need a PaymentService, but I don't care who creates it."

The Spring container takes responsibility for creating, configuring, and connecting these objects. That's the "inversion": application objects no longer control the creation/location of their dependencies. ([Home](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html?utm_source=chatgpt.com "Introduction to the Spring IoC Container and Beans :: Spring Framework"))

### Interview definition

A good concise answer:

> **Inversion of Control is a design principle where control over object creation and dependency management is transferred from application code to a container or framework.**

* * *

# 2\. Dependency Injection — DI

Dependency Injection is a **technique used to implement IoC**.

This distinction is important:

```text
IoC
│
│ broader principle
│
└── Dependency Injection
        │
        └── technique used by Spring
```

With DI, an object declares what it needs, and the container provides those dependencies.

Spring supports dependencies through constructors, factory methods and properties/setters. ([Home](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html?utm_source=chatgpt.com "Introduction to the Spring IoC Container and Beans :: Spring Framework"))

For example:

```java
@Service
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Spring sees:

```text
OrderService
     │
     │ requires
     ▼
PaymentService
```

and provides the appropriate `PaymentService` bean.

* * *

# 3\. Constructor Injection

This is the style I want you to be comfortable with:

```java
@Service
public class PaymentService {

    public void processPayment() {
        // ...
    }
}
```

and:

```java
@Service
public class OrderService {

    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

Conceptually Spring does something similar to:

```java
PaymentService paymentService =
        new PaymentService();

OrderService orderService =
        new OrderService(paymentService);
```

Except that **Spring controls this process**.

The dependency graph becomes:

```text
ApplicationContext

PaymentService Bean
       │
       │ injected
       ▼
OrderService Bean
```

Spring resolves dependencies as it creates the beans and builds the dependency graph. ([Home](https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html?utm_source=chatgpt.com "Dependency Injection :: Spring Framework"))

* * *

# 4\. Why Constructor Injection?

There are several advantages.

### Explicit dependencies

You immediately know what the class needs:

```java
public OrderService(
    PaymentService paymentService,
    InventoryService inventoryService
)
```

The constructor communicates:

```text
OrderService requires:

PaymentService
InventoryService
```

### Immutability

Dependencies can be `final`:

```java
private final PaymentService paymentService;
```

### Testability

You don't need Spring to unit test the class:

```java
PaymentService paymentService = mock(PaymentService.class);

OrderService service =
        new OrderService(paymentService);
```

This is a very important point:

> **Dependency Injection is not only about Spring.**

Your classes become easier to test because their dependencies can be supplied externally.

* * *

# 5\. What is a Spring Bean?

A **bean is simply an object managed by the Spring IoC container**. Spring instantiates, assembles and manages those objects. ([Home](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html?utm_source=chatgpt.com "Introduction to the Spring IoC Container and Beans :: Spring Framework"))

For example:

```java
@Service
public class PaymentService {
}
```

Spring can discover this class and create an instance.

That instance becomes a:

> **Spring Bean**

A bean is not some special Java object.

Conceptually:

```text
Normal Java Object
       +
managed by Spring
       =
Spring Bean
```

This distinction is important.

* * *

# 6\. What is ApplicationContext?

`ApplicationContext` represents the Spring IoC container.

It is responsible for **instantiating, configuring and assembling beans** based on configuration metadata. ([Home](https://docs.spring.io/spring-framework/reference/core/beans/basics.html?utm_source=chatgpt.com "Container Overview :: Spring Framework"))

Think of it as:

```text
              ApplicationContext
                     │
         ┌───────────┼───────────┐
         ▼           ▼           ▼
    OrderService  PaymentService Repository
       Bean           Bean          Bean
         │             │
         └─────────────┘
          dependencies
```

`ApplicationContext` extends the capabilities of `BeanFactory` and adds features such as event publication, internationalization support and easier integration with Spring AOP. ([Home](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html?utm_source=chatgpt.com "Introduction to the Spring IoC Container and Beans :: Spring Framework"))

For interviews, this is usually enough:

> **ApplicationContext is Spring's IoC container. It creates, configures, manages, and wires application beans and their dependencies.**

* * *

# 7\. BeanFactory vs ApplicationContext

You should at least recognize this distinction.

```text
BeanFactory
     │
     └── basic IoC container functionality

ApplicationContext
     │
     ├── BeanFactory functionality
     ├── events
     ├── internationalization
     ├── AOP integration
     └── other application features
```

`ApplicationContext` is a sub-interface of `BeanFactory`, and Spring's documentation recommends using `ApplicationContext` for normal application development. ([Home](https://docs.spring.io/spring-framework/reference/core/beans/introduction.html?utm_source=chatgpt.com "Introduction to the Spring IoC Container and Beans :: Spring Framework"))

Don't spend much time memorizing `BeanFactory` APIs today.

* * *

# 8\. How Everything Connects

This is the most important mental model from today's lesson:

```text
        IoC
         │
         │ principle
         ▼
Dependency Injection
         │
         │ implemented by
         ▼
   Spring Container
         │
         ▼
 ApplicationContext
         │
         │ manages
         ▼
       Beans
         │
         │ dependencies between
         ▼
       Beans
```

For example:

```text
ApplicationContext
       │
       ├── creates PaymentService
       │
       ├── creates OrderService
       │
       └── injects PaymentService
                into
             OrderService
```

That's today's core concept.

* * *

# 9\. One Important Distinction

Don't answer an interview question like:

> "Dependency Injection is `@Autowired`."

That's too implementation-specific.

DI exists without annotations and without Spring.

This is Dependency Injection:

```java
public OrderService(PaymentService paymentService) {
    this.paymentService = paymentService;
}
```

Spring is simply the framework/container responsible for providing the dependency.

`@Autowired`, component scanning, `@Bean`, etc. are mechanisms Spring can use to determine how the objects should be wired.

We'll get deeper into those mechanisms next.

* * *

# 10\. What You Need to Know for Interviews

By the end of today's session, you should comfortably answer:

**1\. What is Inversion of Control?**

> Control over creating and managing dependencies is transferred from application objects to a container/framework.

**2\. What is Dependency Injection?**

> A technique where an object's dependencies are provided externally rather than created by the object itself.

**3\. IoC vs DI?**

> IoC is the broader design principle; DI is a technique for implementing IoC.

**4\. What is a Spring Bean?**

> An object instantiated, configured and managed by the Spring IoC container.

**5\. What is ApplicationContext?**

> Spring's IoC container responsible for creating, configuring, managing and wiring beans.

**6\. Why constructor injection?**

> Explicit dependencies, easier testing, support for immutable dependencies, and classes that don't need to depend directly on Spring APIs.

* * *

## 🎯 Today's success criterion

The most important thing is that you can explain this flow without memorizing definitions:

```text
My OrderService needs PaymentService.

I don't instantiate PaymentService inside OrderService.

I declare the dependency through the constructor.

Spring's ApplicationContext manages both objects as beans.

When Spring creates OrderService,
it resolves and injects PaymentService.

This is Dependency Injection,
which is one way Spring implements IoC.
```

If you can explain that clearly in your own words, **you understand the core of today's topic**.

After studying it, we should do what we did with Collections: I'll give you a **small practical Spring exercise**, and then we can run the **Day 2 interview in English**.