# [Day 10] Java Annotations

## Objective

Understand Java annotations as **metadata**, how they are declared, where they can be used, how long they are retained, and why frameworks such as Spring rely heavily on them.

By the end of this lesson, I should be able to:

- Explain what a Java annotation is
- Explain why an annotation does not execute behavior by itself
- Use built-in annotations such as `@Override`, `@Deprecated`, `@SuppressWarnings`, and `@FunctionalInterface`
- Create a custom annotation
- Use annotation attributes and default values
- Explain `@Target`
- Explain `@Retention`
- Distinguish `SOURCE`, `CLASS`, and `RUNTIME`
- Explain the relationship between annotations and reflection/framework infrastructure
- Explain why Spring uses annotations heavily

---

# 1. What Is an Annotation?

An annotation is **metadata attached to Java code**.

It can describe classes, methods, fields, parameters, constructors, and other program elements.

Example:

```java
@Override
public String toString() {
    return "Customer";
}
```

`@Override` does not replace the method implementation.

It tells the compiler:

> This method is intended to override a method from a parent type.

Mental model:

```text
Java code
   +
metadata
   =
annotated code
```

Examples of metadata:

```text
"This method overrides another method"
"This API is deprecated"
"This class is a Spring service"
"This method should run transactionally"
"This parameter must satisfy a validation rule"
```

---

# 2. Annotations Do Not Execute Behavior by Themselves

Consider:

```java
@MyAnnotation
public void process() {
}
```

The annotation itself does not automatically execute behavior.

Something else must interpret it:

```text
Annotation
   ↓
Compiler / annotation processor / framework / runtime code
   ↓
Interpret metadata
   ↓
Apply behavior
```

This is one of the most important concepts for Spring:

```java
@Service
@Transactional
@Configuration
@Bean
```

The annotation is metadata.

Spring infrastructure reads that metadata and applies behavior.

---

# 3. Built-In Java Annotations

## `@Override`

```java
public class Animal {
    public void speak() {
        System.out.println("Animal");
    }
}
```

```java
public class Dog extends Animal {

    @Override
    public void speak() {
        System.out.println("Dog");
    }
}
```

If the method signature is wrong:

```java
@Override
public void speek() {
}
```

the compiler reports an error.

---

## `@Deprecated`

```java
@Deprecated
public void oldMethod() {
}
```

Modern form:

```java
@Deprecated(
    since = "2.0",
    forRemoval = true
)
public void oldMethod() {
}
```

---

## `@SuppressWarnings`

```java
@SuppressWarnings("unchecked")
public void process() {
}
```

Use carefully. It should mean:

```text
"I understand this warning and intentionally accept it."
```

---

## `@FunctionalInterface`

```java
@FunctionalInterface
public interface Calculator {
    int calculate(int a, int b);
}
```

The compiler enforces a single abstract method.

---

# 4. Where Can Annotations Be Used?

Annotations can target different program elements.

Examples:

```java
@Deprecated
public class LegacyService {
}
```

```java
@Override
public String toString() {
    return "...";
}
```

```java
@MyAnnotation
private String name;
```

```java
public void create(@MyAnnotation String name) {
}
```

Where an annotation is allowed is controlled with:

```java
@Target
```

---

# 5. Creating a Custom Annotation

Basic syntax:

```java
public @interface Important {
}
```

Usage:

```java
@Important
public class PaymentService {
}
```

At this point, the annotation is only metadata.

---

# 6. Annotation Attributes

```java
public @interface Audit {
    String action();
}
```

Usage:

```java
@Audit(action = "CREATE_CUSTOMER")
public void createCustomer() {
}
```

---

# 7. Default Values

```java
public @interface Audit {
    String action();
    boolean enabled() default true;
}
```

Usage:

```java
@Audit(action = "CREATE_CUSTOMER")
public void createCustomer() {
}
```

or:

```java
@Audit(
    action = "CREATE_CUSTOMER",
    enabled = false
)
public void createCustomer() {
}
```

---

# 8. Special `value` Attribute

If an annotation defines:

```java
String value();
```

then:

```java
@Role(value = "ADMIN")
```

can be shortened to:

```java
@Role("ADMIN")
```

This convention is common in frameworks.

---

# 9. Meta-Annotations

Important meta-annotations include:

```text
@Target
@Retention
@Documented
@Inherited
@Repeatable
```

For this lesson, the most important are:

```text
@Target
@Retention
```

---

# 10. `@Target`

`@Target` defines where an annotation can be applied.

```java
@Target(ElementType.METHOD)
public @interface Audit {
}
```

Common `ElementType` values:

```text
TYPE
METHOD
FIELD
PARAMETER
CONSTRUCTOR
ANNOTATION_TYPE
```

Multiple targets:

```java
@Target({
    ElementType.TYPE,
    ElementType.METHOD
})
public @interface Important {
}
```

---

# 11. `@Retention`

`@Retention` defines how long annotation metadata survives.

```text
SOURCE
CLASS
RUNTIME
```

## `RetentionPolicy.SOURCE`

```java
@Retention(RetentionPolicy.SOURCE)
```

Available only during compilation.

## `RetentionPolicy.CLASS`

```java
@Retention(RetentionPolicy.CLASS)
```

Stored in the `.class` file, but not normally available through runtime reflection.

This is the default if no retention policy is declared.

## `RetentionPolicy.RUNTIME`

```java
@Retention(RetentionPolicy.RUNTIME)
```

Available while the application is running.

This is critical for frameworks that inspect annotations at runtime.

---

# 12. Complete Custom Annotation

Today's custom annotation:

```java
package org.jfl.Day10_Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

    String action();

    boolean enabled() default true;
}
```

Important imports:

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
```

A compile error around `RetentionPolicy.RUNTIME` usually means the `RetentionPolicy` import is missing.

---

# 13. Example Usage

```java
package org.jfl.Day10_Annotations;

public class PropertyService {

    @Audit(action = "CREATE_PROPERTY")
    public void createProperty() {
        System.out.println("Creating property");
    }

    @Audit(
        action = "DELETE_PROPERTY",
        enabled = false
    )
    public void deleteProperty() {
        System.out.println("Deleting property");
    }
}
```

At this stage, `@Audit` stores metadata only.

---

# 14. Reflection Preview

Reflection is Day 11, but the connection is:

```java
var method = PropertyService.class
    .getMethod("createProperty");

Audit audit = method.getAnnotation(Audit.class);

System.out.println(audit.action());
```

Possible output:

```text
CREATE_PROPERTY
```

Mental model:

```text
@Retention(RUNTIME)
       ↓
metadata survives compilation
       ↓
runtime code can inspect it
```

---

# 15. Why Frameworks Use Annotations

Annotations support declarative programming.

Instead of manually writing:

```java
beginTransaction();

try {
    process();
    commitTransaction();
} catch (Exception e) {
    rollbackTransaction();
}
```

you can declare intent:

```java
@Transactional
public void process() {
}
```

Conceptually:

```text
developer declares intention
        ↓
annotation
        ↓
framework infrastructure
        ↓
behavior applied
```

Important:

```text
@Transactional itself does not start the transaction.

Spring infrastructure interprets the annotation
and implements the transactional behavior.
```

---

# 16. Declarative vs Imperative

## Imperative

```java
openTransaction();
process();
commitTransaction();
```

## Declarative

```java
@Transactional
public void process() {
}
```

Annotations are widely used by Java frameworks to enable declarative configuration.

---

# 17. Annotation vs Interface

```text
interface
→ behavioral contract

annotation
→ metadata
```

Example interface:

```java
public interface PaymentService {
    void pay();
}
```

Example annotation:

```java
public @interface Audited {
}
```

---

# 18. Annotation Processing

Annotations may be processed:

```text
compile time
or
runtime
```

Examples:

```text
@Override
→ compiler

Lombok annotations
→ compile-time annotation processing

Spring annotations
→ many are interpreted by framework infrastructure at runtime
```

---

# 19. Connection to Spring

```java
@Service
```

Conceptually:

```text
"This class is a Spring service candidate."
```

```java
@Configuration
```

Conceptually:

```text
"This class contains configuration metadata."
```

```java
@Bean
```

Conceptually:

```text
"The object returned by this method
should be registered as a Spring bean."
```

Spring reads the metadata and performs the behavior.

---

# 20. Practical Exercise

Project structure:

```text
Day10_Annotations/
├── Audit.java
├── PropertyService.java
└── Main.java
```

## `Audit.java`

Requirements:

```text
@Target → METHOD
@Retention → RUNTIME

attributes:
String action()
boolean enabled() default true
```

## `PropertyService.java`

Methods:

```text
createProperty()
deleteProperty()
```

Annotations:

```text
CREATE_PROPERTY
DELETE_PROPERTY
```

One method should use:

```java
enabled = false
```

## `Main.java`

For Day 10, call the methods normally.

Reflection-based discovery is reserved for Day 11.

---

# 21. Interview Questions

1. What is a Java annotation?
2. Does an annotation execute behavior by itself?
3. Who can process Java annotations?
4. What does `@Target` control?
5. What does `@Retention` control?
6. What is the difference between `SOURCE`, `CLASS`, and `RUNTIME`?
7. Why does a framework like Spring need runtime-visible annotations?
8. What is the purpose of annotation attributes?
9. What is special about an attribute named `value`?
10. What is the difference between an annotation and an interface?
11. How are Lombok annotations different from many Spring annotations?
12. Why are annotations useful for declarative programming?

---

# 22. Senior Interview Answer

> Java annotations are metadata attached to program elements such as classes, methods, fields, and parameters. They normally do not execute behavior themselves. Instead, the compiler, annotation processors, or runtime frameworks inspect and act on that metadata. `@Target` controls where an annotation may be used, while `@Retention` controls whether it exists only in source, in the compiled class, or at runtime. Frameworks such as Spring rely heavily on runtime annotation metadata to implement declarative features such as dependency injection, configuration, validation, and transactions.

---

# Quick Mental Model

```text
ANNOTATION
    ↓
metadata
    ↓
does not execute behavior itself
    ↓
must be interpreted
```

```text
@Target
→ WHERE annotation can be used
```

```text
@Retention
→ HOW LONG annotation survives

SOURCE
CLASS
RUNTIME
```

```text
Spring annotation
      ↓
Spring infrastructure
      ↓
behavior
```

---

# Completion Checklist

- [x] Understand annotation as metadata
- [x] Built-in annotations
- [x] Custom annotation syntax
- [x] Annotation attributes
- [x] Default values
- [x] `@Target`
- [x] `@Retention`
- [x] SOURCE / CLASS / RUNTIME
- [x] Understand that annotations do not execute themselves
- [x] Create `Audit` annotation
- [x] Fix/import `RetentionPolicy`
- [ ] Interview round
- [ ] Spring Day 10 track: `@Configuration` + `@Bean`

**Current Day 10 Java status: theory + implementation complete; interview review still pending.**

# [Day 10] Spring `@Configuration` & `@Bean`

## Objective

Understand how Spring creates and manages beans explicitly using:

- `@Configuration`
- `@Bean`
- explicit dependency wiring
- bean naming
- singleton behavior
- `@Bean` vs `@Component`
- third-party bean configuration

By the end of this lesson, I should be able to:

- Explain what `@Configuration` does
- Explain what `@Bean` does
- Explain what object actually becomes the bean
- Explain how dependencies are injected into `@Bean` methods
- Explain `@Bean` vs `@Component`
- Explain why third-party classes are a common use case for `@Bean`
- Explain default bean naming and scope
- Connect explicit bean creation with the `ApplicationContext`

---

# 1. Two Ways to Register Spring Beans

So far, most beans were discovered through component scanning.

Example:

```java
@Service
public class PropertyService {
}
```

Conceptually:

```text
@Component / @Service
        ↓
component scanning
        ↓
Spring discovers class
        ↓
bean registered
```

Spring also allows explicit bean registration:

```java
@Configuration
@Bean
```

Conceptually:

```text
@Configuration
      ↓
configuration class
      ↓
@Bean methods
      ↓
objects created explicitly
      ↓
Spring registers/manages them
```

---

# 2. `@Configuration`

Example:

```java
@Configuration
public class AppConfig {
}
```

This tells Spring:

> This class contains bean configuration.

It is a Java-based configuration source for the Spring container.

Mental model:

```text
@Configuration
→ class containing instructions
  for Spring bean creation
```

---

# 3. `@Bean`

Example:

```java
@Configuration
public class AppConfig {

    @Bean
    public PropertyFormatter propertyFormatter() {
        return new PropertyFormatter();
    }
}
```

The important point is:

> The object returned by the method becomes the Spring bean.

Conceptually:

```text
@Bean method
     ↓
creates object
     ↓
returns object
     ↓
Spring registers object
     ↓
ApplicationContext manages it
```

---

# 4. Bean Name

By default, the bean name is the method name.

Example:

```java
@Bean
public PropertyFormatter propertyFormatter() {
    return new PropertyFormatter();
}
```

Default bean name:

```text
propertyFormatter
```

It can be customized:

```java
@Bean("formatter")
public PropertyFormatter propertyFormatter() {
    return new PropertyFormatter();
}
```

Now the bean name is:

```text
formatter
```

---

# 5. Dependency Injection with `@Bean`

Suppose:

```java
public class PropertyService {

    private final PropertyFormatter formatter;

    public PropertyService(PropertyFormatter formatter) {
        this.formatter = formatter;
    }
}
```

Configuration:

```java
@Configuration
public class AppConfig {

    @Bean
    public PropertyFormatter propertyFormatter() {
        return new PropertyFormatter();
    }

    @Bean
    public PropertyService propertyService(
            PropertyFormatter formatter) {

        return new PropertyService(formatter);
    }
}
```

Spring sees this parameter:

```java
PropertyFormatter formatter
```

and resolves the matching bean from the container.

Mental model:

```text
PropertyFormatter bean
        ↓
ApplicationContext
        ↓
PropertyService bean
        ↓
dependency injected
```

---

# 6. Prefer Parameter Injection in `@Bean` Methods

Instead of:

```java
@Bean
public PropertyService propertyService() {
    return new PropertyService(propertyFormatter());
}
```

prefer:

```java
@Bean
public PropertyService propertyService(
        PropertyFormatter formatter) {

    return new PropertyService(formatter);
}
```

Why?

Because the dependency is explicit in the method signature.

You can immediately see:

```text
PropertyService
depends on
PropertyFormatter
```

This is clearer and easier to reason about.

---

# 7. `@Bean` vs `@Component`

This is one of the most important interview questions.

## `@Component`

Example:

```java
@Component
public class PropertyFormatter {
}
```

Typical use:

```text
class belongs to my application
+
Spring can discover it through scanning
```

---

## `@Bean`

Example:

```java
@Configuration
public class AppConfig {

    @Bean
    public PropertyFormatter propertyFormatter() {
        return new PropertyFormatter();
    }
}
```

Typical use:

```text
explicit construction/configuration
```

Especially useful when:

```text
I do not own the class
```

---

# 8. Third-Party Class Example

Suppose a library provides:

```java
ThirdPartyClient
```

You cannot modify its source code to add:

```java
@Component
```

So:

```java
@Configuration
public class ClientConfig {

    @Bean
    public ThirdPartyClient thirdPartyClient() {
        return new ThirdPartyClient();
    }
}
```

Spring can now inject it elsewhere.

This is a classic use case for `@Bean`.

---

# 9. Custom Configuration Example

Suppose:

```java
public class PropertyApiClient {

    private final String baseUrl;
    private final int timeout;

    public PropertyApiClient(
            String baseUrl,
            int timeout) {

        this.baseUrl = baseUrl;
        this.timeout = timeout;
    }
}
```

Configuration:

```java
@Configuration
public class ApiConfig {

    @Bean
    public PropertyApiClient propertyApiClient() {

        return new PropertyApiClient(
            "https://api.example.com",
            5000
        );
    }
}
```

Later, hard-coded values can be replaced using:

```text
@Value
@ConfigurationProperties
Profiles
```

Those are future topics.

---

# 10. `@Bean` Objects Are Normal Spring Beans

Once registered, a bean created using `@Bean` can be:

```text
injected
scoped
proxied
lifecycle-managed
used by other beans
```

Example:

```java
@Bean
public PropertyService propertyService(
        PropertyFormatter formatter) {

    return new PropertyService(formatter);
}
```

Elsewhere:

```java
@RestController
public class PropertyController {

    private final PropertyService service;

    public PropertyController(
            PropertyService service) {

        this.service = service;
    }
}
```

Once registered, Spring manages it regardless of whether it came from:

```text
@Service
```

or:

```text
@Bean
```

---

# 11. Default Scope of `@Bean`

By default:

```java
@Bean
public PropertyFormatter propertyFormatter() {
    return new PropertyFormatter();
}
```

is singleton scoped.

Mental model:

```text
one bean instance
per bean definition
per ApplicationContext
```

This connects directly to Day 9.

---

# 12. Multiple Beans of the Same Type

Example:

```java
@Bean
public NotificationService emailService() {
    return new EmailNotificationService();
}

@Bean
public NotificationService smsService() {
    return new SmsNotificationService();
}
```

Now Spring has:

```text
NotificationService
├── emailService
└── smsService
```

Injection becomes ambiguous unless resolved.

Use:

```text
@Primary
```

or:

```text
@Qualifier
```

Example:

```java
@Bean
@Primary
public NotificationService emailService() {
    return new EmailNotificationService();
}
```

or:

```java
public OrderService(
    @Qualifier("smsService")
    NotificationService notificationService
) {
}
```

This connects back to Day 4.

---

# 13. `@Bean` + Lifecycle

Beans created with `@Bean` still participate in Spring lifecycle management.

Example:

```java
@Bean(
    initMethod = "init",
    destroyMethod = "close"
)
public ExternalClient externalClient() {
    return new ExternalClient();
}
```

This is useful when:

```text
I do not own the class
+
I cannot add @PostConstruct / @PreDestroy
```

Another strong use case for `@Bean`.

---

# 14. `new` vs Spring-Managed Bean

Important distinction:

```java
new PropertyService(...)
```

creates a normal Java object.

It is not automatically managed by Spring.

Spring features such as:

```text
dependency injection
AOP proxies
@Transactional
lifecycle callbacks
```

may not apply.

Mental model:

```text
new
→ you manage object

Spring bean
→ container manages object
```

---

# 15. `@Bean` vs `@Autowired`

Different responsibilities:

```text
@Bean
→ creates/registers bean

@Autowired
→ injects existing bean
```

With constructor injection, `@Autowired` is often unnecessary when there is only one constructor.

---

# 16. Connection to Java Annotations

Today's Java track established:

```text
annotation = metadata
```

Now apply that to Spring.

```java
@Configuration
```

means conceptually:

```text
"This class contains configuration metadata."
```

```java
@Bean
```

means:

```text
"Register the returned object as a Spring bean."
```

Then:

```text
Spring infrastructure
        ↓
reads metadata
        ↓
creates/registers beans
```

The annotations themselves do not perform the behavior.

---

# 17. Runnable Example

## `PropertyFormatter.java`

```java
package org.jfl.Day10_Configuration;

public class PropertyFormatter {

    public String format(
            String title,
            double price) {

        return title + " - R$ " + price;
    }
}
```

## `PropertyService.java`

```java
package org.jfl.Day10_Configuration;

public class PropertyService {

    private final PropertyFormatter formatter;

    public PropertyService(
            PropertyFormatter formatter) {

        this.formatter = formatter;
    }

    public void printProperty() {

        String result =
            formatter.format(
                "Apartment in Batel",
                3500.0
            );

        System.out.println(result);
    }
}
```

## `AppConfig.java`

```java
package org.jfl.Day10_Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public PropertyFormatter propertyFormatter() {
        return new PropertyFormatter();
    }

    @Bean
    public PropertyService propertyService(
            PropertyFormatter formatter) {

        return new PropertyService(formatter);
    }
}
```

## `Main.java`

```java
package org.jfl.Day10_Configuration;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {

    public static void main(String[] args) {

        ApplicationContext context =
            new AnnotationConfigApplicationContext(
                AppConfig.class
            );

        PropertyService service =
            context.getBean(
                PropertyService.class
            );

        service.printProperty();
    }
}
```

Expected output:

```text
Apartment in Batel - R$ 3500.0
```

---

# 18. Verify Singleton Behavior

```java
PropertyService service1 =
    context.getBean(PropertyService.class);

PropertyService service2 =
    context.getBean(PropertyService.class);

System.out.println(service1 == service2);
```

Expected:

```text
true
```

Why?

Because singleton is the default Spring bean scope.

---

# 19. Interview Review

## Question 1

### What is an annotation?

Improved answer:

> An annotation is metadata attached to Java code. It does not normally execute behavior by itself. Instead, the compiler, annotation processors, or frameworks inspect that metadata and act on it.

Important correction:

```text
annotation
≠
interface
```

An annotation does not define behavior like an interface.

---

## Question 2

### SOURCE vs CLASS vs RUNTIME

```text
SOURCE
→ discarded during compilation

CLASS
→ stored in .class file
→ not normally available through runtime reflection

RUNTIME
→ available at runtime
→ frameworks can inspect it
```

---

## Question 3

### Why does Spring need RUNTIME retention?

Answer:

> Spring needs annotations to remain available at runtime so its infrastructure can inspect classes and methods and apply behavior such as component discovery, configuration, dependency injection, transactions, and other framework features.

---

## Question 4

### `@Component` vs `@Bean`

Strong answer:

> `@Component` is typically used on classes owned by the application and discovered through component scanning. `@Bean` is used inside configuration classes to explicitly construct and configure objects, especially third-party classes or objects requiring custom setup.

---

# 20. Interview Questions to Keep Reviewing

1. What does `@Configuration` do?
2. What does `@Bean` do?
3. What object becomes the bean?
4. What is the default bean name?
5. What is the default bean scope?
6. How does one `@Bean` depend on another?
7. `@Bean` vs `@Component`?
8. Why use `@Bean` for third-party classes?
9. What happens when I manually use `new`?
10. Can `@Bean` participate in lifecycle management?
11. How do `@Primary` and `@Qualifier` relate to multiple beans?

---

# Senior Interview Answer

> `@Configuration` marks a class that defines Spring configuration, while `@Bean` marks a method whose returned object should be registered and managed by the Spring container. `@Component` relies on component scanning and is typically used for application-owned classes, whereas `@Bean` gives explicit control over object construction and is especially useful for third-party classes or custom configuration.

---

# Day 10 Spring Mental Model

```text
@Configuration
      ↓
configuration class
      ↓
@Bean
      ↓
object created
      ↓
ApplicationContext
      ↓
Spring-managed bean
      ↓
dependency injection
```

---

# Completion Checklist

* [x] Understand `@Configuration`
* [x] Understand `@Bean`
* [x] Understand returned object becomes the bean
* [x] Understand bean naming
* [x] Understand dependency injection in `@Bean` methods
* [x] Understand `@Bean` vs `@Component`
* [x] Understand third-party class use case
* [x] Understand default singleton scope
* [x] Connect `@Primary` / `@Qualifier`
* [x] Connect `@Bean` with lifecycle
* [x] Complete interview review

**Day 10 Spring track: COMPLETE ✅**

```
