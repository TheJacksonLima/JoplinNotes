# Day 28 — Logging with SLF4J, Logback and Spring Boot

## Goal

Day 28 focuses on application logging in Java and Spring Boot.

```text
Part A — Java
SLF4J
Logback
log levels
parameterized logging
exception logging
logging best practices

Part B — Spring Boot
logging configuration
root vs package levels
environment-specific logging
file output
console pattern
production logging strategy
```

Core mental model:

```text
application code
↓
SLF4J API
↓
logging implementation
↓
Logback
↓
console / file / external logging system
```

---

# Part A — SLF4J, Logback and Parameterized Logging

## SLF4J

SLF4J means:

```text
Simple Logging Facade for Java
```

It is the logging facade/API your application code uses.

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

private static final Logger log =
        LoggerFactory.getLogger(
                LoggingDemo.class
        );
```

Then:

```java
log.info(
        "Application started"
);
```

Mental model:

```text
SLF4J
→ abstraction / facade
```

---

## Logback

Logback is a logging implementation.

```text
SLF4J
→ abstraction / API

Logback
→ implementation
```

Your application calls SLF4J. Logback processes and outputs the log events.

---

## Why Logging Instead of `System.out.println()`?

A logging framework gives you:

```text
levels
filtering
logger names
configuration
console/file output
exception stack traces
production integrations
```

Prefer:

```java
log.info(
        "Payment created id={}",
        id
);
```

instead of:

```java
System.out.println(
        "Payment created"
);
```

---

## Logger Convention

```java
private static final Logger log =
        LoggerFactory.getLogger(
                PaymentService.class
        );
```

Why:

```text
private
→ internal to class

static
→ one logger reference per class

final
→ reference does not change
```

The logger name typically corresponds to the fully-qualified class name.

---

## Log Levels

Common levels:

```text
TRACE
DEBUG
INFO
WARN
ERROR
```

From most detailed to most severe:

```text
TRACE
↓
DEBUG
↓
INFO
↓
WARN
↓
ERROR
```

### TRACE

```java
log.trace(
        "Entering process() reference={}",
        reference
);
```

Use for extremely detailed execution flow.

### DEBUG

```java
log.debug(
        "Selected strategy={} method={}",
        strategyName,
        method
);
```

Use for troubleshooting and developer diagnostics.

### INFO

```java
log.info(
        "Payment processed id={} status={}",
        paymentId,
        status
);
```

Use for important normal application events.

### WARN

```java
log.warn(
        "Payment reference={} already processed",
        reference
);
```

Use for unusual conditions where the application can continue.

### ERROR

```java
log.error(
        "Failed to process payment id={}",
        paymentId,
        ex
);
```

Use for failed operations or unexpected conditions requiring investigation.

---

# Parameterized Logging

Prefer:

```java
log.info(
        "Payment id={} status={}",
        paymentId,
        status
);
```

Avoid:

```java
log.info(
        "Payment id="
                + paymentId
                + " status="
                + status
);
```

SLF4J uses `{}` placeholders.

Example:

```java
log.debug(
        "Payment reference={} amount={} method={}",
        reference,
        amount,
        method
);
```

Advantages:

```text
cleaner
more readable
less unnecessary string construction
```

If the log level is disabled, parameterized logging can avoid formatting work that string concatenation would perform eagerly.

---

## Expensive Debug Arguments

This method still executes even if DEBUG is disabled:

```java
log.debug(
        "Calculated={}",
        calculateVeryExpensiveDebugValue()
);
```

If argument creation is expensive:

```java
if (log.isDebugEnabled()) {

    log.debug(
            "Calculated={}",
            calculateVeryExpensiveDebugValue()
    );
}
```

Use this selectively, not around every debug statement.

---

# Exception Logging

Avoid:

```java
log.error(
        ex.getMessage()
);
```

Prefer:

```java
log.error(
        "Payment processing failed reference={}",
        reference,
        ex
);
```

Passing the exception object preserves:

```text
exception type
message
stack trace
cause chain
```

---

## Do Not Log the Same Exception at Every Layer

Bad pattern:

```text
repository logs
↓
service logs
↓
controller advice logs
```

One failure becomes multiple duplicate stack traces.

Better:

```text
log where useful context exists
or
at the boundary that owns the failure
```

---

# Expected vs Unexpected Errors

An exception does not automatically mean ERROR.

Example:

```text
PaymentNotFoundException
```

may be an expected application condition.

Depending on context it might be:

```text
DEBUG
INFO
WARN
```

Unexpected failures such as:

```text
database outage
NullPointerException
integration failure
```

may deserve:

```text
ERROR
```

---

# Sensitive Data

Do not casually log:

```text
passwords
tokens
Authorization headers
private keys
full card numbers
security secrets
sensitive personal data
```

Prefer targeted identifiers:

```java
log.info(
        "Payment id={} reference={} status={}",
        payment.getId(),
        payment.getReference(),
        payment.getStatus()
);
```

instead of dumping complete objects.

---

# Lombok `@Slf4j`

If Lombok is available:

```java
@Slf4j
public class PaymentService {

    public void process() {

        log.info(
                "Processing payment"
        );
    }
}
```

This generates the logger field for you.

You should still understand `Logger` and `LoggerFactory`.

---

# Part A Mini-Lab

```java
package mini_lab.day28;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

public class LoggingDemo {

    private static final Logger log =
            LoggerFactory.getLogger(
                    LoggingDemo.class
            );

    public static void main(String[] args) {

        Long paymentId = 42L;

        String reference =
                "PAY-2026-123456";

        BigDecimal amount =
                new BigDecimal(
                        "150.00"
                );

        String method =
                "PIX";

        log.trace(
                "Entering payment processing"
        );

        log.debug(
                "Payment reference={} amount={} method={}",
                reference,
                amount,
                method
        );

        log.info(
                "Payment processed id={} reference={}",
                paymentId,
                reference
        );

        log.warn(
                "Demo warning for payment id={}",
                paymentId
        );
    }
}
```

With normal Spring Boot defaults you will typically see INFO and WARN, but not TRACE or DEBUG.

---

# Part B — Spring Boot Logging Configuration

## Spring Boot Defaults

A typical Spring Boot application uses:

```text
SLF4J
+
Logback
```

Spring Boot usually configures logging automatically.

The effective root level is commonly:

```text
INFO
```

So:

```text
TRACE → hidden
DEBUG → hidden
INFO  → visible
WARN  → visible
ERROR → visible
```

---

## Root Logging Level

In `application.properties`:

```properties
logging.level.root=INFO
```

If you use:

```properties
logging.level.root=DEBUG
```

you may get very verbose output from Spring and third-party libraries.

---

## Package-Specific Logging

Prefer targeted configuration:

```properties
logging.level.root=INFO
logging.level.org.jfl=DEBUG
```

Then:

```text
framework / third-party
→ INFO+

org.jfl
→ DEBUG+
```

For the mini-lab package:

```properties
logging.level.mini_lab.day28=DEBUG
```

---

## Logger Hierarchy

Example package structure:

```text
org.jfl
├── controller
├── service
└── repository
```

Specific configuration:

```properties
logging.level.org.jfl.service=DEBUG
```

Even more specific:

```properties
logging.level.org.jfl.service.PaymentService=TRACE
```

Mental model:

```text
root
└── org.jfl
    └── service
        └── PaymentService
```

More specific settings can override broader ones.

---

# Main Verification Exercise

Use:

```java
private static final Logger log =
        LoggerFactory.getLogger(
                PaymentService.class
        );

public PaymentResponse findById(
        Long id) {

    log.trace(
            "TRACE - find payment id={}",
            id
    );

    log.debug(
            "DEBUG - find payment id={}",
            id
    );

    log.info(
            "INFO - find payment id={}",
            id
    );

    log.warn(
            "WARN - find payment id={}",
            id
    );

    // existing logic...
}
```

### Run with INFO

```properties
logging.level.org.jfl=INFO
```

Expected:

```text
TRACE ❌
DEBUG ❌
INFO  ✅
WARN  ✅
ERROR ✅
```

### Run with DEBUG

```properties
logging.level.org.jfl=DEBUG
```

Expected:

```text
TRACE ❌
DEBUG ✅
INFO  ✅
WARN  ✅
ERROR ✅
```

### Run with TRACE

```properties
logging.level.org.jfl=TRACE
```

Expected:

```text
TRACE ✅
DEBUG ✅
INFO  ✅
WARN  ✅
ERROR ✅
```

This proves logging behavior can change through configuration without changing Java source.

---

# Why Not Root DEBUG in Production?

Try:

```properties
logging.level.root=DEBUG
```

You may see much more output from:

```text
Spring
Tomcat
Jackson
Hibernate
other libraries
```

Better:

```properties
logging.level.root=INFO
logging.level.org.jfl=DEBUG
```

Mental model:

```text
root DEBUG
→ noisy

package DEBUG
→ focused diagnostics
```

---

# Environment-Specific Logging

Development:

```properties
logging.level.org.jfl=DEBUG
```

Production:

```properties
logging.level.org.jfl=INFO
```

Possible files:

```text
application.properties
application-dev.properties
application-prod.properties
```

Concept:

```text
same code
+
different environment config
=
different logging behavior
```

---

# File Logging

Spring Boot can write logs to a file:

```properties
logging.file.name=logs/payment-app.log
```

Possible output:

```text
logs/payment-app.log
```

For Day 28, understanding this capability is enough.

---

# Console Pattern

Example:

```properties
logging.pattern.console=%d{HH:mm:ss} %-5level %logger{36} - %msg%n
```

This controls things such as:

```text
timestamp
level
logger
message
```

You do not need to memorize the pattern syntax.

---

# Controller Logging

Example:

```java
@GetMapping("/{id}")
public PaymentResponse findById(
        @PathVariable Long id) {

    log.debug(
            "Received request to find payment id={}",
            id
    );

    return paymentService.findById(id);
}
```

Useful context:

```text
operation
identifier
request flow
```

---

# Service Logging

Example:

```java
public PaymentResponse process(
        PaymentRequest request) {

    log.info(
            "Processing payment reference={} method={}",
            request.reference(),
            request.method()
    );

    PaymentResponse response =
            processPayment(request);

    log.info(
            "Payment processed reference={} status={}",
            response.reference(),
            response.status()
    );

    return response;
}
```

---

# Integration with `@RestControllerAdvice`

This connects Day 28 to Day 22.

Example:

```java
@ExceptionHandler(Exception.class)
public ResponseEntity<ApiError>
handleUnexpected(
        Exception ex,
        HttpServletRequest request) {

    log.error(
            "Unexpected error path={}",
            request.getRequestURI(),
            ex
    );

    // build API response
}
```

Flow:

```text
unexpected exception
↓
log useful context
↓
convert to API response
```

Expected business errors such as 404s do not always deserve ERROR.

---

# Logging Configuration Without Recompiling

You can change:

```properties
logging.level.org.jfl=INFO
```

to:

```properties
logging.level.org.jfl=DEBUG
```

without changing Java source.

Important principle:

```text
logging verbosity
belongs in configuration
not business logic
```

---

# Interview Questions

## SLF4J vs Logback?

SLF4J is the logging facade/API. Logback is the implementation that processes and writes log events.

## Why parameterized logging?

It improves readability and can avoid unnecessary message formatting when the log level is disabled.

## What are the common log levels?

```text
TRACE
DEBUG
INFO
WARN
ERROR
```

## How should exceptions be logged?

Pass the exception object so the stack trace and cause chain are preserved.

```java
log.error(
        "Payment failed id={}",
        id,
        ex
);
```

## Why not use `System.out.println()`?

A logging framework provides levels, filtering, hierarchy, configuration, multiple output destinations, and production integration.

## How do you enable DEBUG for one Spring package?

```properties
logging.level.org.jfl=DEBUG
```

## Why not enable root DEBUG in production?

Because it can produce excessive output from your application and third-party libraries, making logs noisy and expensive.

## Can log levels change without recompiling?

Yes. Logging levels are configuration and can change independently of Java source.

## Root logger vs package logger?

Root defines the default threshold. Package/class configuration can override it for that logger hierarchy.

---

# Concise Manual Notes

```text
DAY 28 — LOGGING

SLF4J
= facade / abstraction

Logback
= implementation

FLOW:
application
↓
SLF4J
↓
Logback
↓
console/file


LOGGER:

private static final Logger log =
  LoggerFactory.getLogger(
    MyClass.class
  );


LEVELS:

TRACE
DEBUG
INFO
WARN
ERROR


TRACE
= very detailed

DEBUG
= troubleshooting

INFO
= normal important events

WARN
= unusual but continues

ERROR
= failed operation


PARAMETERIZED:

GOOD:

log.info(
 "Payment id={} status={}",
 id,
 status
);

AVOID:

log.info(
 "Payment id=" + id
);


EXCEPTIONS:

GOOD:

log.error(
 "Failed id={}",
 id,
 ex
);

BAD:

log.error(
 ex.getMessage()
);


DO NOT LOG:

passwords
tokens
Authorization headers
card data
secrets


SPRING BOOT:

default usually INFO

logging.level.root=INFO

logging.level.org.jfl=DEBUG

package-specific
beats broad config


INFO:
TRACE ❌
DEBUG ❌
INFO  ✅
WARN  ✅
ERROR ✅

DEBUG:
TRACE ❌
DEBUG ✅
INFO  ✅
WARN  ✅
ERROR ✅


avoid:
logging.level.root=DEBUG
in production


profiles:

dev → DEBUG
prod → INFO


file:

logging.file.name=
logs/payment-app.log


same code
+
different logging config
```

---

# Day 28 Completion Checklist

## Part A

- [ ] Explain SLF4J
- [ ] Explain Logback
- [ ] Explain facade vs implementation
- [ ] Create logger with `LoggerFactory`
- [ ] Explain TRACE
- [ ] Explain DEBUG
- [ ] Explain INFO
- [ ] Explain WARN
- [ ] Explain ERROR
- [ ] Use parameterized logging
- [ ] Explain why concatenation is discouraged
- [ ] Log exception with stack trace
- [ ] Explain `isDebugEnabled()`
- [ ] Explain expected vs unexpected failures
- [ ] Explain sensitive-data risks
- [ ] Compare logging vs `System.out.println()`

## Part B

- [ ] Explain Spring Boot default logging
- [ ] Configure `logging.level.root`
- [ ] Configure package-specific logging
- [ ] Run INFO level test
- [ ] Run DEBUG level test
- [ ] Run TRACE level test
- [ ] Intentionally enable root DEBUG
- [ ] Observe framework noise
- [ ] Restore targeted logging
- [ ] Explain logger hierarchy
- [ ] Explain environment-specific logging
- [ ] Configure file logging
- [ ] Understand console patterns
- [ ] Add logging to a Spring service
- [ ] Add unexpected error logging to `@RestControllerAdvice`
- [ ] Explain why logging config does not require recompiling

---

# Final Mental Model

```text
application
↓
SLF4J
↓
Logback
↓
configuration
↓
console/file
```

Log filtering:

```text
configured INFO
↓
TRACE hidden
DEBUG hidden
INFO visible
WARN visible
ERROR visible
```

Spring Boot strategy:

```text
root = INFO
+
targeted package = DEBUG
```

rather than:

```text
root = DEBUG everywhere
```
