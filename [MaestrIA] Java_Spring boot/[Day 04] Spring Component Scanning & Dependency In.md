---
title: '[Day 04] Spring Component Scanning & Dependency Injection'
updated: 2026-09-03 13:25:50Z
created: 2026-09-03 13:21:59Z
latitude: -25.42016050
longitude: -49.27616110
altitude: 0.0000
---

\[Day 04\] Spring Component Scanning & Dependency Injection

## 📚 Read these today

Start with these official Spring docs, in this order:

1.  **Classpath Scanning and Managed Components** — this is the main reading for today. Focus on `@Component`, stereotype annotations, component scanning, and auto-detection. ([Home](https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html?utm_source=chatgpt.com "Classpath Scanning and Managed Components :: Spring Framework"))  
    [Spring — Classpath Scanning and Managed Components](https://docs.spring.io/spring-framework/reference/core/beans/classpath-scanning.html?utm_source=chatgpt.com)
    
2.  **Using `@Autowired`** — focus specifically on constructor injection and the fact that a single constructor doesn't require `@Autowired`. ([Home](https://docs.spring.io/spring-framework/reference/7.1/core/beans/annotation-config/autowired.html?utm_source=chatgpt.com "Using @Autowired :: Spring Framework"))  
    [Spring — Using @Autowired](https://docs.spring.io/spring-framework/reference/7.1/core/beans/annotation-config/autowired.html?utm_source=chatgpt.com)
    
3.  **`@Primary`** — short reading. Understand how Spring chooses one candidate when multiple beans match the requested type. ([Home](https://docs.spring.io/spring-framework/reference/7.1/core/beans/annotation-config/autowired-primary.html?utm_source=chatgpt.com "Fine-tuning Annotation-based Autowiring with @Primary or @Fallback :: Spring Framework"))  
    [Spring — @Primary](https://docs.spring.io/spring-framework/reference/7.1/core/beans/annotation-config/autowired-primary.html?utm_source=chatgpt.com)
    
4.  **`@Qualifier`** — understand how it narrows the candidates when multiple beans have the same type. ([Home](https://docs.spring.io/spring-framework/reference/7.1/core/beans/annotation-config/autowired-qualifiers.html?utm_source=chatgpt.com "Fine-tuning Annotation-based Autowiring with Qualifiers :: Spring Framework"))  
    [Spring — @Qualifier](https://docs.spring.io/spring-framework/reference/7.1/core/beans/annotation-config/autowired-qualifiers.html?utm_source=chatgpt.com)
    
5.  **Spring Boot package structure** — short but important. It explains why we normally put the `@SpringBootApplication` class in the root package. ([Home](https://docs.spring.io/spring-boot/reference/using/structuring-your-code.html?utm_source=chatgpt.com "Structuring Your Code :: Spring Boot"))  
    [Spring Boot — Structuring Your Code](https://docs.spring.io/spring-boot/reference/using/structuring-your-code.html?utm_source=chatgpt.com)
    

I wouldn't spend more than **25–30 minutes reading**. The implementation and interview explanation are more important.

* * *

# What we're studying today

### 1\. Component Scanning

Yesterday we said:

> A bean is an object managed by the `ApplicationContext`.

Today's question is:

> **How does the ApplicationContext know which classes should become beans?**

One answer is **component scanning**.

Suppose your project looks like:

```text
com.jfl.app
│
├── Application.java
├── service
│   └── OrderService.java
│
└── notification
    └── EmailNotificationService.java
```

And:

```java
@SpringBootApplication
public class Application {
}
```

`@SpringBootApplication` includes component-scanning behavior. By convention, Spring scans from the package containing your application class downward through its subpackages. That's one reason Spring Boot recommends placing the main application class in a root package. ([Home](https://docs.spring.io/spring-boot/reference/using/structuring-your-code.html?utm_source=chatgpt.com "Structuring Your Code :: Spring Boot"))

It discovers:

```java
@Service
public class OrderService {
}
```

and registers it with the container.

The mental model is:

```text
Application starts
       ↓
Component scanning
       ↓
Find candidate components
       ↓
Register BeanDefinitions
       ↓
Create/manage beans
       ↓
Resolve dependencies
```

One nuance for your senior-level understanding: **scanning and bean instantiation aren't exactly the same step**. Scanning discovers candidates and registers bean definitions; the container subsequently creates the bean instances.

* * *

# 2\. Stereotype annotations

These are all related:

```java
@Component
@Service
@Repository
@Controller
```

`@Component` is the generic stereotype. `@Service`, `@Repository`, and `@Controller` are specialized stereotypes for particular application layers. ([Home](https://docs.spring.io/spring/reference/6.2/core/beans/classpath-scanning.html?utm_source=chatgpt.com "Classpath Scanning and Managed Components :: Spring Framework"))

Think:

```text
@Component
    │
    ├── @Service
    ├── @Repository
    └── @Controller
```

### `@Component`

Generic Spring-managed component.

```java
@Component
public class TokenGenerator {
}
```

Use it when the class doesn't naturally belong to one of the more specific layers.

### `@Service`

Business/service layer:

```java
@Service
public class OrderService {
}
```

Technically Spring could discover it with `@Component`, but `@Service` communicates the role of the class.

### `@Repository`

Persistence/data-access layer:

```java
@Repository
public class OrderRepository {
}
```

There's an important difference here.

`@Repository` isn't **only** documentation. Spring uses it as a marker for persistence exception translation, allowing technology-specific persistence exceptions to be translated into Spring's data-access exception hierarchy. ([Home](https://docs.spring.io/spring/reference/6.2/core/beans/classpath-scanning.html?utm_source=chatgpt.com "Classpath Scanning and Managed Components :: Spring Framework"))

That's a good senior-interview detail.

### `@Controller`

Presentation/web layer:

```java
@Controller
public class OrderController {
}
```

We'll study controllers properly when we reach Spring MVC/REST.

* * *

# 3\. Constructor Injection

We've already practiced this:

```java
@Service
public class OrderService {

    private final NotificationService notificationService;

    public OrderService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
}
```

Today we're connecting it to the container.

Spring sees:

```text
OrderService requires NotificationService
                 ↓
Search ApplicationContext
                 ↓
Find matching bean
                 ↓
Inject it
```

And because there's only one constructor, you don't need:

```java
@Autowired
```

Spring will use that constructor automatically. ([Home](https://docs.spring.io/spring-framework/reference/7.1/core/beans/annotation-config/autowired.html?utm_source=chatgpt.com "Using @Autowired :: Spring Framework"))

* * *

# 4\. Multiple implementations

This is where today's exercise gets interesting.

We're going to change your previous example from:

```text
NotificationService
        ↑
EmailNotificationService
```

to:

```text
          NotificationService
             ↑         ↑
             │         │
          Email       SMS
```

Both:

```java
@Service
public class EmailNotificationService
        implements NotificationService {
}
```

and:

```java
@Service
public class SmsNotificationService
        implements NotificationService {
}
```

are beans.

Now Spring encounters:

```java
public OrderService(NotificationService notificationService)
```

and effectively asks:

```text
I need NotificationService.

Candidate #1: EmailNotificationService
Candidate #2: SmsNotificationService

Which one?
```

That's the ambiguity we discussed previously.

* * *

# 5\. `@Primary`

One solution is:

```java
@Service
@Primary
public class EmailNotificationService
        implements NotificationService {
}
```

You're telling Spring:

> If several beans match this type, prefer this one.

Spring documents `@Primary` specifically as a way to give one candidate preference when multiple beans can satisfy a single-valued dependency. ([Home](https://docs.spring.io/spring-framework/reference/7.1/core/beans/annotation-config/autowired-primary.html?utm_source=chatgpt.com "Fine-tuning Annotation-based Autowiring with @Primary or @Fallback :: Spring Framework"))

So:

```text
NotificationService

Email → @Primary
SMS

       ↓

OrderService
       ↓
Email selected
```

* * *

# 6\. `@Qualifier`

Sometimes you don't want a global default.

For example:

```java
public OrderService(
    @Qualifier("emailNotificationService")
    NotificationService notificationService
) {
    this.notificationService = notificationService;
}
```

Now you're explicitly narrowing the candidates at this injection point. ([Home](https://docs.spring.io/spring-framework/reference/7.1/core/beans/annotation-config/autowired-qualifiers.html?utm_source=chatgpt.com "Fine-tuning Annotation-based Autowiring with Qualifiers :: Spring Framework"))

Mental distinction:

```text
@Primary
→ "Use this implementation by default."

@Qualifier
→ "At THIS injection point, I want this candidate."
```

This is an important interview distinction.

* * *

# Today's practical exercise

We're going to use the code you already built rather than start another project.

Current:

```text
NotificationService
       ↑
EmailNotificationService
       ↑
OrderService
```

You'll:

**First**, add:

```java
SmsNotificationService
```

implementing:

```java
NotificationService
```

and annotate it appropriately.

**Second**, start the application **without `@Primary` or `@Qualifier`**.

I actually want you to see Spring fail.

**Third**, read the exception and identify why dependency resolution failed.

**Fourth**, fix it using `@Primary`.

**Fifth**, remove `@Primary` and fix it using `@Qualifier`.

That gives us the full learning cycle:

```text
Understand
   ↓
Implement
   ↓
Break it
   ↓
Read exception
   ↓
Fix it
   ↓
Explain why
```

Afterward we'll do a short voice interview covering today's material.

For now, I'd start with **the component-scanning documentation above**. Once you've read that first page, come back and we'll discuss what Spring is actually doing during scanning before you modify the project.