# Day 30 — JVM + Spring Boot Startup Checkpoint

## Goal

Day 30 ties Month 1 together by following the application lifecycle from JVM startup to a ready Spring Boot HTTP server.

```text
Part A — JVM
process
heap
thread stacks
class loading
class initialization
Metaspace
GC
JIT

Part B — Spring Boot
SpringApplication.run()
ApplicationContext
component scanning
beans
autoconfiguration
embedded Tomcat
Actuator
```

# Part A — JVM

## JVM process

When you run:

```bash
java MyApplication
```

the OS starts a process containing a JVM.

```text
OS
↓
Java process
↓
JVM
├── class loaders
├── heap
├── thread stacks
├── garbage collector
├── JIT
└── native/JVM memory
```

The JVM then:

```text
find main class
↓
load
↓
link
↓
initialize
↓
start main thread
↓
invoke main()
```

## Source vs bytecode

```text
.java
↓ javac
.class bytecode
↓ JVM
execution
```

The JVM executes bytecode, not `.java` source directly.

## JVM vs JRE vs JDK

```text
JVM
→ executes bytecode

JRE
→ runtime environment

JDK
→ development tools + runtime
```

Typical JDK tools include `javac`, `java`, `jcmd`, `jstack`, and `jps`.

## Heap

Objects and arrays are generally heap-managed.

```java
Payment payment = new Payment();
int[] numbers = new int[100];
```

Conceptually:

```text
stack/reference
      |
      v
heap object
```

The heap is shared by threads, which is why shared mutable state creates concurrency concerns.

## Garbage collection

```java
Payment payment = new Payment();
payment = null;
```

If no reachable references remain, the object becomes eligible for GC.

```text
eligible for GC
≠ immediately collected
```

## Thread stacks

Each thread has its own stack.

```text
main thread       → own stack
request thread 1  → own stack
request thread 2  → own stack
```

Method calls create stack frames.

```text
validate()
↓
processPayment()
↓
main()
```

A stack frame contains execution state such as locals, parameters, operand data, and return information.

## Heap vs stack

A better senior-level explanation than “primitives on stack, objects on heap” is:

> Objects and arrays are generally heap-managed, while each thread has stack frames containing method execution state. The JVM/JIT can optimize the actual physical representation.

## StackOverflowError

Unbounded recursion:

```java
static void recurse() {
    recurse();
}
```

eventually causes:

```text
StackOverflowError
```

because each call adds another frame to a finite thread stack.

## Heap OutOfMemoryError

If the JVM cannot satisfy more heap allocations, you may see:

```text
OutOfMemoryError: Java heap space
```

Difference:

```text
StackOverflowError
→ thread stack exhausted

OutOfMemoryError: Java heap space
→ heap allocation failure
```

## Heap flags

```bash
java -Xms512m -Xmx2g MyApplication
```

```text
-Xms
→ initial heap size

-Xmx
→ maximum heap size
```

## Metaspace

Metaspace stores class metadata.

```text
classes
methods
fields
runtime type metadata
```

It is distinct from the normal Java heap and uses native memory.

# Class Loading

## Lifecycle

```text
Loading
↓
Linking
  ├── Verification
  ├── Preparation
  └── Resolution
↓
Initialization
```

### Loading

The JVM obtains the class definition and creates its runtime representation.

### Verification

The JVM checks whether the bytecode is structurally valid.

### Preparation

The JVM prepares class runtime state.

### Resolution

Symbolic references are resolved to runtime classes, methods, and fields.

### Initialization

Static initialization runs.

Example:

```java
public class InitializationDemo {

    static {
        System.out.println("Static block");
    }

    public static void main(String[] args) {
        System.out.println("main()");
    }
}
```

Output:

```text
Static block
main()
```

For the class containing `main()`:

```text
class initialization
↓
static fields / static blocks
↓
main()
```

## Static blocks

Static blocks are useful for one-time class initialization when setup needs several statements or exception handling.

Simple initialization:

```java
static final int MAX_RETRIES = 3;
```

Complex one-time setup:

```java
static final Properties properties = new Properties();

static {
    try {
        properties.load(
            Config.class.getResourceAsStream("/app.properties")
        );
    } catch (IOException ex) {
        throw new ExceptionInInitializerError(ex);
    }
}
```

Rule:

```text
simple initialization
→ field assignment

complex one-time class initialization
→ static block
```

In Spring applications, use Spring lifecycle/configuration mechanisms rather than static blocks for injected services, database connections, or application configuration.

## Class loaders

Useful model:

```text
Bootstrap ClassLoader
↓
Platform ClassLoader
↓
Application ClassLoader
```

Roughly:

```text
Bootstrap
→ core Java runtime

Platform
→ platform libraries/modules

Application
→ application classpath/module path
```

Example:

```java
System.out.println(
    MyClass.class.getClassLoader()
);

System.out.println(
    String.class.getClassLoader()
);
```

`String.class.getClassLoader()` commonly returns `null`, representing bootstrap loading through this API.

## `Class.forName()`

```java
Class<?> clazz =
        Class.forName("java.util.ArrayList");
```

This connects reflection and class loading.

## Missing classes

Simplified:

```text
ClassNotFoundException
→ explicit/dynamic loading cannot find class

NoClassDefFoundError
→ JVM expected a class but cannot use/find/initialize it at runtime
```

## JIT

JIT means Just-In-Time compilation.

```text
bytecode
↓
hot code detected
↓
optimized machine code
```

# JVM Mini-Lab

```java
package mini_lab.day30;

public class JvmLifecycleDemo {

    static {
        System.out.println("1. Class initialized");
    }

    public static void main(String[] args) {

        System.out.println("2. main() started");

        long pid =
                ProcessHandle.current().pid();

        System.out.println(
                "3. JVM process PID=" + pid
        );

        Runtime runtime =
                Runtime.getRuntime();

        System.out.println(
                "4. Max heap MB="
                        + toMb(runtime.maxMemory())
        );

        System.out.println(
                "5. ClassLoader="
                        + JvmLifecycleDemo.class
                        .getClassLoader()
        );

        int localValue = 42;

        System.out.println(
                "Method local value="
                        + localValue
        );

        System.out.println(
                "6. main() finished"
        );
    }

    static long toMb(long bytes) {
        return bytes / 1024 / 1024;
    }
}
```

# Part B — Spring Boot Startup

## Spring Boot `main()`

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        SpringApplication.run(
                Application.class,
                args
        );
    }
}
```

Sequence:

```text
JVM
↓
main()
↓
SpringApplication.run()
↓
Spring Boot startup
```

## What `SpringApplication.run()` does

High-level lifecycle:

```text
prepare environment
↓
create ApplicationContext
↓
discover configuration
↓
register bean definitions
↓
apply autoconfiguration
↓
create beans
↓
start embedded web server
↓
run startup callbacks
↓
application ready
```

## `@SpringBootApplication`

Conceptually combines:

```text
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan
```

Meaning:

```text
@SpringBootConfiguration
→ main Boot configuration

@ComponentScan
→ discover application components

@EnableAutoConfiguration
→ configure infrastructure conditionally
```

# ApplicationContext

The `ApplicationContext` is Spring's main container.

Useful model:

```text
ApplicationContext
=
registry + factory + lifecycle manager
for Spring beans
```

It manages:

```text
bean definitions
bean creation
dependency injection
configuration
lifecycle
proxies
```

Important distinction:

```text
JVM
→ Java runtime

ApplicationContext
→ Spring bean container
```

Every Spring bean is a Java object, but not every Java object is a Spring bean.

# Dependency Injection

Example:

```java
@Service
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(
            PaymentRepository repository) {

        this.repository = repository;
    }
}
```

Spring resolves the object graph:

```text
PaymentRepository bean
↓
PaymentService bean
↓
PaymentController bean
```

# `@Configuration` and `@Bean`

```java
@Configuration
class Day30Config {

    @Bean
    Clock clock() {
        return Clock.systemUTC();
    }
}
```

`@Configuration` marks a configuration class.

`@Bean` tells Spring to register the returned object as a managed bean.

# CommandLineRunner

A `CommandLineRunner` runs after the Spring context is initialized.

```java
@Bean
CommandLineRunner runner() {

    return args ->
            System.out.println(
                    "Spring is running"
            );
}
```

Roadmap mini-lab:

```java
@Configuration
class Day30Config {

    @Bean
    CommandLineRunner names(
            ApplicationContext ctx) {

        return args ->
                System.out.println(
                        "Bean count="
                                + ctx.getBeanDefinitionCount()
                );
    }
}
```

The exact count does not matter. The point is to observe that Boot creates many infrastructure beans in addition to your own.

# Autoconfiguration

Spring Boot uses conditions based on:

```text
classpath
properties
existing beans
application type
```

Mental model:

```text
classpath
+
properties
+
existing beans
+
application type
↓
conditions
↓
autoconfiguration decisions
```

Common conditional concepts:

```text
@ConditionalOnClass
@ConditionalOnMissingBean
@ConditionalOnProperty
```

Examples:

```text
required class exists
→ configuration may apply

bean missing
→ Boot may provide default

property enabled
→ feature may activate
```

# Condition Evaluation Report

Run Spring Boot with:

```text
--debug
```

Then inspect:

```text
CONDITIONS EVALUATION REPORT
```

Conceptually:

```text
Positive match
→ condition satisfied

Negative match
→ condition not satisfied
```

This is useful when troubleshooting why Boot did or did not configure something.

# Embedded Tomcat

With a normal `spring-boot-starter-web` application, Boot commonly starts embedded Tomcat.

```text
JVM
↓
Spring Boot
↓
embedded Tomcat
↓
HTTP server
```

Tomcat is not part of the JVM. It is included as Java dependencies and runs inside the same JVM process.

# Port conflict experiment

If port 8080 is already in use, application startup can fail.

Change:

```properties
server.port=8081
```

or:

```bash
java -jar app.jar --server.port=8081
```

This demonstrates that Spring Boot still relies on normal OS networking.

# Actuator

Add:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

Then call:

```text
GET /actuator/health
```

Typical response:

```json
{
  "status": "UP"
}
```

Distinction:

```text
/payments
→ business API

/actuator/health
→ operational API
```

# Bean Definition vs Bean Instance

```text
BeanDefinition
↓
Spring container
↓
actual Java bean instance
```

A bean definition is metadata describing how Spring should create/manage a bean.

A bean instance is the actual Java object.

# Day 29 Connection — Proxies

Spring can wrap beans in proxies during container processing.

```text
ApplicationContext
→ target bean
→ proxy created when needed
→ consumer receives proxy
```

This is how features such as AOP and `@Transactional` can work.

# Day 28 Connection — Logging

Startup messages such as:

```text
Starting Application
Tomcat initialized
Tomcat started
Started Application
```

map to real phases in the Spring Boot lifecycle.

# Day 27 Connection — Executable JAR

```text
java -jar app.jar
↓
Boot launcher
↓
main()
↓
SpringApplication.run()
↓
ApplicationContext
↓
Tomcat
↓
HTTP server
```

# JAR vs WAR

## JAR

JAR means:

```text
Java ARchive
```

A JAR can be a Java library or application.

For Spring Boot, an executable JAR commonly packages:

```text
application classes
dependencies
Boot launcher
embedded server dependencies
```

Run:

```bash
java -jar app.jar
```

Flow:

```text
JVM
↓
Spring Boot
↓
embedded Tomcat
↓
application
```

## WAR

WAR means:

```text
Web Application ARchive
```

Traditionally used for web applications deployed into an external servlet container/application server:

```text
payment.war
↓
Tomcat / WebLogic / JBoss
↓
application
```

Practical comparison:

```text
JAR
→ standalone Java archive
→ common modern Spring Boot deployment
→ embedded server
→ java -jar app.jar

WAR
→ web application archive
→ traditionally external server
→ deploy to Tomcat/WebLogic/JBoss
```

Spring Boot can still produce WAR files when external-container deployment is required.

# Interview Questions

## What happens when you run a Java application?

The OS starts a JVM process. The JVM loads, links and initializes the main class, creates the main thread, invokes `main()`, and manages memory, class loading, garbage collection and JIT compilation.

## Heap vs stack?

The heap is shared JVM memory primarily used for objects and arrays. Each thread has its own stack containing method frames and execution state.

## What causes `StackOverflowError`?

Usually excessive recursion or method-call depth exhausts the finite thread stack.

## What causes `OutOfMemoryError: Java heap space`?

The JVM cannot satisfy another heap allocation within the available heap.

## What is a class loader?

A JVM component responsible for locating and loading class definitions into the runtime.

## What are the class-loading phases?

```text
Loading
Linking
  Verification
  Preparation
  Resolution
Initialization
```

## When does a static block run?

During class initialization. For the class containing `main()`, static initialization happens before `main()` executes.

## What does `SpringApplication.run()` do?

It bootstraps the Spring Boot application by preparing the environment, creating the `ApplicationContext`, applying configuration/autoconfiguration, creating beans, starting the embedded server for a web application, and executing startup callbacks.

## What is `ApplicationContext`?

Spring's container for bean definitions, bean creation, dependency injection, lifecycle and application infrastructure.

## What is Spring Boot autoconfiguration?

Conditional application configuration based on the classpath, properties, existing beans and application type.

## What is Actuator?

Spring Boot's operational/management feature set, including endpoints such as `/actuator/health`.

## JAR vs WAR?

A JAR is a general Java archive and is commonly used by Spring Boot as a standalone executable application with an embedded server. A WAR is a web application archive traditionally deployed into an external servlet container/application server.

# Concise Manual Notes

```text
DAY 30

JVM:

OS
↓
JVM process
↓
class loading
↓
initialization
↓
main()


HEAP:
objects/arrays
shared by threads
GC

STACK:
one per thread
method frames

StackOverflowError
→ stack exhausted

OutOfMemoryError
→ heap allocation failure


-Xms
→ initial heap

-Xmx
→ max heap


METASPACE:
class metadata


CLASS LOADING:

Loading
↓
Linking
 verification
 preparation
 resolution
↓
Initialization


STATIC BLOCK:

runs during class initialization

main class:
static block
→ before main()


Spring Boot:

main()
↓
SpringApplication.run()
↓
ApplicationContext
↓
component scan
↓
autoconfiguration
↓
beans
↓
embedded Tomcat
↓
READY


@SpringBootApplication
≈
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan


ApplicationContext
= Spring container


JVM
→ Java runtime

ApplicationContext
→ bean lifecycle/DI


AUTOCONFIG:

classpath
+
properties
+
existing beans
+
app type


--debug
→ Condition Evaluation Report


ACTUATOR:

/actuator/health


JAR:
standalone
embedded server
java -jar app.jar

WAR:
web archive
external Tomcat/WebLogic/JBoss
```

# Day 30 Completion Checklist

- [ ] Explain JVM process
- [ ] Explain heap
- [ ] Explain thread stacks
- [ ] Explain stack frames
- [ ] Explain GC eligibility
- [ ] Explain StackOverflowError
- [ ] Explain heap OutOfMemoryError
- [ ] Explain `-Xms` / `-Xmx`
- [ ] Explain Metaspace
- [ ] Explain class-loading phases
- [ ] Explain static blocks
- [ ] Explain class loaders
- [ ] Explain JIT
- [ ] Run JVM lifecycle mini-lab
- [ ] Explain `SpringApplication.run()`
- [ ] Explain `@SpringBootApplication`
- [ ] Explain `ApplicationContext`
- [ ] Explain Java object vs Spring bean
- [ ] Explain dependency injection
- [ ] Explain `@Configuration`
- [ ] Explain `@Bean`
- [ ] Run bean-count mini-lab
- [ ] Explain autoconfiguration
- [ ] Run with `--debug`
- [ ] Inspect Condition Evaluation Report
- [ ] Explain embedded Tomcat
- [ ] Add Actuator
- [ ] Call `/actuator/health`
- [ ] Explain JAR vs WAR
