# Day 29 — Reflection, Dynamic Proxies and Spring AOP

## Goal

Day 29 connects Java dynamic proxies and reflection with Spring AOP.

```text
Part A — Java
Dynamic proxies
InvocationHandler
Method interception
Reflection
Proxy boundaries

Part B — Spring
AOP
@Aspect
@Around
Pointcuts
ProceedingJoinPoint
Cross-cutting concerns
Self-invocation
```

Core model:

```text
caller
↓
proxy
↓
interceptor / advice
↓
target
```

This helps explain Spring features such as `@Transactional`, `@Async`, `@Cacheable`, and AOP.

---

# Part A — Java Dynamic Proxies

## Normal Call

```text
caller
↓
target
```

Example:

```java
public interface PaymentProcessor {

    String process(Long paymentId);
}
```

```java
public class DefaultPaymentProcessor
        implements PaymentProcessor {

    @Override
    public String process(Long paymentId) {

        System.out.println(
                "Processing payment id="
                        + paymentId
        );

        return "APPROVED";
    }
}
```

---

## What Is a Proxy?

A proxy stands between caller and target:

```text
caller
↓
proxy
↓
target
```

It can intercept calls before delegating to the real object.

Typical cross-cutting concerns:

```text
logging
timing
transactions
authorization
metrics
caching
auditing
```

---

## JDK Dynamic Proxy

Java provides:

```java
java.lang.reflect.Proxy
java.lang.reflect.InvocationHandler
```

Example:

```java
PaymentProcessor target =
        new DefaultPaymentProcessor();

PaymentProcessor proxy =
        (PaymentProcessor)
        Proxy.newProxyInstance(
                PaymentProcessor.class
                        .getClassLoader(),

                new Class[]{
                        PaymentProcessor.class
                },

                (
                        proxyObject,
                        method,
                        arguments
                ) -> {

                    System.out.println(
                            "Before: "
                                    + method.getName()
                    );

                    Object result =
                            method.invoke(
                                    target,
                                    arguments
                            );

                    System.out.println(
                            "After: "
                                    + method.getName()
                    );

                    return result;
                }
        );
```

Call:

```java
String result =
        proxy.process(42L);
```

Flow:

```text
proxy.process(42)
↓
InvocationHandler
↓
before
↓
method.invoke(target,args)
↓
DefaultPaymentProcessor.process(42)
↓
after
↓
return result
```

---

## `Proxy.newProxyInstance()`

Conceptually:

```java
Proxy.newProxyInstance(
        classLoader,
        interfaces,
        invocationHandler
);
```

Meaning:

```text
classLoader
→ how classes are loaded

interfaces
→ interfaces implemented by proxy

invocationHandler
→ interception behavior
```

---

## `InvocationHandler`

The handler receives:

```java
(proxy, method, args) -> ...
```

`method` is a:

```java
java.lang.reflect.Method
```

Useful methods:

```java
method.getName();
method.getReturnType();
method.getDeclaringClass();
```

---

## `method.invoke()`

```java
method.invoke(
        target,
        arguments
);
```

uses reflection to invoke the intercepted method on the real target.

Conceptually:

```text
method = process
target = DefaultPaymentProcessor
args = [42]
```

is equivalent to calling:

```java
target.process(42L);
```

dynamically.

---

## Timing Proxy

```java
PaymentProcessor proxy =
        (PaymentProcessor)
        Proxy.newProxyInstance(
                PaymentProcessor.class
                        .getClassLoader(),

                new Class[]{
                        PaymentProcessor.class
                },

                (
                        proxyObject,
                        method,
                        arguments
                ) -> {

                    long start =
                            System.nanoTime();

                    try {

                        return method.invoke(
                                target,
                                arguments
                        );

                    } finally {

                        long elapsed =
                                System.nanoTime()
                                        - start;

                        System.out.println(
                                method.getName()
                                        + " took "
                                        + elapsed
                                        + " ns"
                        );
                    }
                }
        );
```

Now:

```text
target
→ business logic

proxy
→ timing
```

---

## Proxy Boundary

This is one of the most important concepts.

```java
proxy.process(42L);
```

is intercepted.

```java
target.process(42L);
```

is not.

```text
proxy.method()
→ interception

target.method()
→ direct call
→ no interception
```

This becomes critical in Spring.

---

## JDK Proxy Limitation

JDK dynamic proxies are interface-based.

```text
JDK dynamic proxy
→ proxies interfaces
```

Spring can also create class-based proxies, but the Java mini-lab uses an interface.

---

## Intentional Break

Replace:

```java
proxy.process(42L);
```

with:

```java
target.process(42L);
```

Observe that the interceptor output disappears.

Lesson:

```text
interception only happens
when the call crosses the proxy
```

---

# Part B — Spring AOP

## What Is AOP?

AOP means:

```text
Aspect-Oriented Programming
```

Its purpose is to separate cross-cutting concerns from business logic.

Example:

```text
PaymentService
→ business logic

TimingAspect
→ timing logic
```

---

## Dependency

Typical dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

---

## Target Service

```java
@Service
public class PaymentProcessingService {

    public String process(
            Long paymentId) {

        System.out.println(
                "Processing payment id="
                        + paymentId
        );

        return "APPROVED";
    }
}
```

---

## `@Aspect`

```java
@Aspect
@Component
public class PaymentTimingAspect {
}
```

Meaning:

```text
@Component
→ Spring bean

@Aspect
→ contains AOP logic
```

---

## `@Around`

Timing requires behavior before and after the target method.

```java
@Around(
        "execution(* org.jfl.service.PaymentProcessingService.process(..))"
)
public Object measureExecutionTime(
        ProceedingJoinPoint joinPoint)
        throws Throwable {

    long start =
            System.nanoTime();

    try {

        return joinPoint.proceed();

    } finally {

        long elapsed =
                System.nanoTime()
                        - start;

        System.out.println(
                "Execution time="
                        + elapsed
                        + "ns"
        );
    }
}
```

Flow:

```text
before
↓
joinPoint.proceed()
↓
target
↓
after
```

---

## Advice Types

```text
@Before
→ before target method

@After
→ after target method

@AfterReturning
→ after successful return

@AfterThrowing
→ after exception

@Around
→ wraps full invocation
```

For Day 29, `@Around` is the key one.

---

## Pointcut

Example:

```java
execution(
    * org.jfl.service.PaymentProcessingService.process(..)
)
```

Breakdown:

```text
execution(...)
→ method execution

*
→ any return type

PaymentProcessingService
→ target class

process
→ method name

(..)
→ any arguments
```

A pointcut defines:

```text
where advice should run
```

---

## `ProceedingJoinPoint`

Useful methods:

```java
joinPoint.proceed();
joinPoint.getArgs();
joinPoint.getSignature();
joinPoint.getTarget();
```

Example:

```java
joinPoint
        .getSignature()
        .getName();
```

---

## Part A vs Part B

Part A:

```java
method.invoke(
        target,
        args
);
```

Part B:

```java
joinPoint.proceed();
```

Mental model:

```text
manual proxy
→ method.invoke()

Spring AOP
→ joinPoint.proceed()
```

---

## Final Timing Aspect

```java
@Aspect
@Component
public class PaymentTimingAspect {

    private static final Logger log =
            LoggerFactory.getLogger(
                    PaymentTimingAspect.class
            );

    @Around(
            "execution(* org.jfl.service.PaymentProcessingService.process(..))"
    )
    public Object measureExecutionTime(
            ProceedingJoinPoint joinPoint)
            throws Throwable {

        long start =
                System.nanoTime();

        try {

            log.debug(
                    "Entering method={}",
                    joinPoint
                            .getSignature()
                            .getName()
            );

            return joinPoint.proceed();

        } finally {

            long elapsed =
                    System.nanoTime()
                            - start;

            log.info(
                    "Method={} executionTime={}ns",
                    joinPoint
                            .getSignature()
                            .getName(),
                    elapsed
            );
        }
    }
}
```

---

# Proxy Boundaries and Self-Invocation

## External Call

```text
Controller
↓
Spring proxy
↓
PaymentService
```

Advice can execute because the call crosses the proxy.

---

## Self-Invocation

Example:

```java
@Service
public class PaymentProcessingService {

    public String start(
            Long id) {

        return process(id);
    }

    public String process(
            Long id) {

        return "APPROVED";
    }
}
```

Inside `start()`:

```java
process(id);
```

is effectively:

```java
this.process(id);
```

Flow:

```text
Controller
↓
proxy
↓
start()
↓
real service
↓
this.process()
```

The internal call may bypass the proxy.

Therefore advice may not run for that internal method call.

---

## Why This Matters

The same proxy-boundary issue helps explain behavior with:

```text
@Transactional
@Async
@Cacheable
Spring AOP
```

Example:

```java
@Service
public class PaymentService {

    public void start() {

        process();
    }

    @Transactional
    public void process() {
    }
}
```

The self-invocation can bypass transaction interception.

---

# Cross-Cutting Concerns

Good AOP candidates:

```text
timing
logging
transactions
security
metrics
caching
auditing
```

Poor AOP candidate:

```text
core business rules
```

Example:

```text
if amount > 10000
→ special approval rule
```

That belongs in normal business logic.

---

# Intentional Breaks

## Remove `proceed()`

Replace:

```java
return joinPoint.proceed();
```

with:

```java
return "INTERCEPTED";
```

The target method no longer runs.

Lesson:

```text
@Around can control
whether the target executes
```

---

## Wrong Pointcut

Use:

```java
@Around(
    "execution(* org.jfl.service.PaymentProcessingService.foo(..))"
)
```

The target still runs, but the advice does not.

Reason:

```text
pointcut matched nothing
```

---

## Self-Invocation Test

Call:

```java
paymentProcessingService.start(id);
```

where:

```java
start()
→ process()
```

If the pointcut targets only `process()`, the internal call may bypass the proxy.

---

# Interview Questions

## What is a dynamic proxy?

A runtime-generated object that implements one or more interfaces and intercepts method calls through an invocation handler.

## What is `InvocationHandler`?

It defines what happens when a method on a JDK dynamic proxy is invoked.

## What does `method.invoke(target,args)` do?

It uses reflection to invoke the intercepted method on the real target object.

## Why are proxies useful?

They allow cross-cutting behavior such as logging, timing, transactions, security, metrics, and caching to be applied around business methods.

## What is AOP?

Aspect-Oriented Programming separates cross-cutting concerns from core business logic.

## What is an aspect?

A component containing cross-cutting behavior and the rules describing where that behavior applies.

## What is advice?

The code executed when a pointcut matches.

## What is a pointcut?

A rule that defines which method executions should be intercepted.

## What does `ProceedingJoinPoint.proceed()` do?

It continues execution to the actual target method.

## Why can self-invocation break Spring AOP?

Because Spring AOP is proxy-based. A direct call through `this` can bypass the proxy.

## Why can `@Transactional` fail on self-invocation?

Because transaction interception is commonly proxy-based, and an internal direct call can skip the interceptor.

---

# Concise Manual Notes

```text
DAY 29 — PROXIES + AOP

PART A

NORMAL:
caller
↓
target

PROXY:
caller
↓
proxy
↓
target

Proxy.newProxyInstance(
 classLoader,
 interfaces,
 handler
)

InvocationHandler:
(proxy, method, args)

Method
= reflection object

method.invoke(
 target,
 args
)
→ real method

JDK dynamic proxy
= interface-based

IMPORTANT:

proxy.method()
→ intercepted

target.method()
→ NOT intercepted


PART B

AOP
= Aspect-Oriented Programming

@Aspect
= AOP component

@Component
= Spring bean

@Around
= before + target + after

ProceedingJoinPoint
= intercepted invocation

joinPoint.proceed()
= execute target

POINTCUT:

execution(
 * org.jfl.service.
 PaymentProcessingService.
 process(..)
)

*
= any return type

process
= method

(..)
= any args

ADVICE:

@Before
@After
@AfterReturning
@AfterThrowing
@Around

PART A:
method.invoke(target,args)

PART B:
joinPoint.proceed()

PROXY BOUNDARY:

external call
→ proxy
→ intercepted

this.method()
→ may bypass proxy

AFFECTS:

@Transactional
@Async
@Cacheable
Spring AOP

GOOD AOP:
timing
logging
security
transactions
metrics
caching

BAD AOP:
core business rules
```

---

# Day 29 Completion Checklist

## Part A

- [ ] Explain proxy
- [ ] Explain `Proxy.newProxyInstance()`
- [ ] Explain `InvocationHandler`
- [ ] Explain `Method`
- [ ] Build dynamic proxy
- [ ] Call target through proxy
- [ ] Use `method.invoke()`
- [ ] Implement timing interceptor
- [ ] Explain interface-based JDK proxies
- [ ] Call target directly and observe no interception
- [ ] Explain proxy boundary

## Part B

- [ ] Add/use Spring AOP
- [ ] Create `@Aspect`
- [ ] Create `@Around`
- [ ] Explain pointcut
- [ ] Explain advice
- [ ] Explain `ProceedingJoinPoint`
- [ ] Use `joinPoint.proceed()`
- [ ] Time a service method
- [ ] Use SLF4J inside aspect
- [ ] Remove `proceed()` intentionally
- [ ] Break pointcut intentionally
- [ ] Test self-invocation
- [ ] Explain self-invocation
- [ ] Connect to `@Transactional`
- [ ] Connect to `@Async`
- [ ] Connect to `@Cacheable`
- [ ] Explain why AOP suits cross-cutting concerns

---

# Final Mental Model

Java:

```text
caller
↓
JDK proxy
↓
InvocationHandler
↓
method.invoke(target,args)
↓
target
```

Spring:

```text
caller
↓
Spring proxy
↓
@Aspect / @Around
↓
joinPoint.proceed()
↓
target
```

Most important rule:

```text
proxy.method()
→ interception

target.method()
or this.method()
→ may bypass interception
```
