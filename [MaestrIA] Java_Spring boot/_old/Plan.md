---
title: Plan
updated: 2026-09-14 18:04:00Z
created: 2026-09-14 17:13:19Z
latitude: -25.42777520
longitude: -49.27306160
altitude: 0.0000
---

Yes. Since the original calendar no longer matches the actual pace, I would **shift the calendar rather than compress the remaining material**.

The key change is that **Week 2 becomes Sep 14–20**, because Generics is complete but Exceptions, Spring configuration, bean lifecycle/scopes, and coding still need to be properly closed. That moves the Month 1 evaluation to **October 5**.

# Month 1 — Java Core + Spring Boot

**Objective:** Strengthen Java Core and Spring Boot fundamentals to Senior Java interview level.
**Updated period:** **Aug 31 → Oct 5, 2026**
**Workload:** ~1h30/day • 6 days/week • Sunday off
**Rule:** A topic is complete only when you can **explain it + implement it + answer interview questions about it**.

| ✓  | Period                    | Java                                                                                                                                                   | Spring Boot                                                                                                                                               | Coding / Practice                                | Weekly Goal                                                                 |
| -- | ------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------ | --------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------ | --------------------------------------------------------------------------- |
| ✅  | **Week 1 — Aug 31–Sep 6** | Collections; ArrayList vs LinkedList; HashMap internals; HashSet; TreeMap/TreeSet; `equals/hashCode`; Comparable/Comparator; Big-O                     | IoC; DI; ApplicationContext; Beans; Component Scanning; stereotypes; Constructor Injection; `@Primary` / `@Qualifier`                                     | Arrays; HashMap; HashSet; Comparator             | Explain Collections, HashMap and DI at interview level                      |
| 🟡 | **Week 2 — Sep 14–20**    | Generics; bounded types; wildcards; PECS; type erasure; checked/unchecked exceptions; custom exceptions; try-with-resources; `final`; immutability     | Bean scopes/lifecycle; `@Configuration`; `@Bean`; `@Value`; `@ConfigurationProperties`; Profiles; Starters; Auto-configuration; Conditional Configuration | HashMap/Set + Two Pointers — **2 problems**      | Understand Java type safety/error handling and Spring configuration/startup |
| ☐  | **Week 3 — Sep 21–27**    | Lambdas; Functional Interfaces; Predicate; Function; Consumer; Supplier; Streams; `map/filter/flatMap/reduce`; Collectors; Optional; Method References | REST; `@RestController`; DTOs; Validation; `@Valid`; `@ControllerAdvice`; Exception Handling; Jackson; HTTP Status; Pagination                            | Two Pointers + Sliding Window — **2–3 problems** | Build and review a small REST API                                           |
| ☐  | **Week 4 — Sep 28–Oct 4** | Records; Sealed Classes; Pattern Matching; Switch Expressions; important Java 17/21 features; Java Core review                                         | Spring Boot startup; Auto-configuration internals; Embedded Server; Actuator; Logging; Profiles; basic Observability; review                              | **2 problems + review**                          | Consolidate Java/Spring and prepare for evaluation                          |
| ☐  | **Evaluation — Oct 5**    | Java Core                                                                                                                                              | Spring Core / Boot                                                                                                                                        | Coding + Code Review                             | **Complete Month 1 evaluation**                                             |

## Current position — September 14

We are **here**:

```text
Week 1
████████████████████  COMPLETE ✅

Week 2
████████████░░░░░░░░  IN PROGRESS 🟡

Generics              ✅
Exceptions theory     ✅
Exceptions practical  ← NEXT
final / immutability  pending
Spring configuration  pending
Bean scopes/lifecycle pending
Coding                pending
```

So we're **not starting Week 3 yet**.

## Updated Week 2 — Sep 14–20

This is how I'd structure this week from where we actually are.

| Day            | Java                                                                                 | Spring                                                             | Practice                                 |
| -------------- | ------------------------------------------------------------------------------------ | ------------------------------------------------------------------ | ---------------------------------------- |
| **Mon Sep 14** | Exceptions: checked/unchecked, custom exceptions, try-with-resources, best practices | `@Transactional` exception behavior as related interview knowledge | Exception exercise + interview questions |
| **Tue Sep 15** | `final` + Immutability                                                               | Bean scopes + Bean lifecycle                                       | Small immutable-domain exercise          |
| **Wed Sep 16** | Java review: Generics + Exceptions                                                   | `@Configuration` + `@Bean`                                         | Build explicit bean configuration        |
| **Thu Sep 17** | —                                                                                    | `@Value` + `@ConfigurationProperties` + Profiles                   | External configuration exercise          |
| **Fri Sep 18** | —                                                                                    | Starters + Auto-configuration + Conditional Configuration          | Inspect/debug Boot auto-configuration    |
| **Sat Sep 19** | Java/Spring Week 2 review                                                            | Spring configuration review                                        | 2 coding problems + mock interview       |
| **Sun Sep 20** | **OFF**                                                                              | **OFF**                                                            | **OFF**                                  |

This also gives us a useful Saturday checkpoint:

> If you can't explain **PECS, checked vs unchecked, immutability, bean scopes, `@Bean`, Profiles and auto-configuration** without the notes, Week 2 stays open.

---

# Week 3 — Sep 21–27

## Functional Java + Spring REST

This will be a substantial week.

### Java

We move into functional Java:

```text
Lambdas
    ↓
Functional Interfaces
    ├── Predicate<T>
    ├── Function<T,R>
    ├── Consumer<T>
    └── Supplier<T>
    ↓
Streams
    ├── map
    ├── filter
    ├── flatMap
    ├── reduce
    └── Collectors
    ↓
Optional
    ↓
Method References
```

We already touched:

```java
.mapToDouble(Property::getPrice)
.forEach(destination::add)
```

during Generics, but that was only exposure. **Streams has not been marked complete.**

### Spring

We'll build a small API rather than studying annotations in isolation:

```text
HTTP Request
     ↓
@RestController
     ↓
DTO
     ↓
@Valid
     ↓
Service
     ↓
Response DTO
     ↓
Jackson
     ↓
HTTP Response
```

Then introduce:

```text
@ControllerAdvice
@ExceptionHandler
HTTP status codes
Pagination
```

The exception knowledge we're learning now will connect directly to this.

### Coding

Patterns:

```text
Two Pointers
Sliding Window
```

Target: **2–3 problems**, understood deeply rather than just submitted successfully.

---

# Week 4 — Sep 28–Oct 4

## Modern Java + Spring Boot Internals

### Java

```text
Records
Sealed Classes
Pattern Matching
Switch Expressions
Java 17/21 features
Java Core consolidation
```

We'll focus on being able to answer questions such as:

> Why would you use a record instead of a regular class?

> What problem do sealed classes solve?

> How does pattern matching improve traditional `instanceof` code?

And connect them to actual backend code rather than memorizing feature lists.

### Spring Boot

This is where we go beneath the annotations:

```text
main()
  ↓
SpringApplication.run()
  ↓
ApplicationContext
  ↓
Configuration
  ↓
Component scanning
  ↓
Auto-configuration
  ↓
Bean creation
  ↓
Embedded server
  ↓
Application ready
```

Topics:

```text
Spring Boot startup
Auto-configuration internals
Embedded Tomcat/server
Actuator
Logging
Profiles
Basic observability
```

This should connect all the Spring concepts from Weeks 1–3.

---

# October 5 — Month 1 Evaluation

This shouldn't be another study day.

I'll treat it as an **interview simulation / diagnostic**.

### Part 1 — Java Core

Questions covering:

```text
Collections
HashMap
equals/hashCode
Comparable/Comparator
Generics
PECS
Exceptions
Immutability
Functional interfaces
Streams
Optional
Modern Java
```

You'll answer **without notes**.

### Part 2 — Spring

```text
IoC / DI
Beans
Component scanning
Scopes
Lifecycle
@Configuration
@Bean
Profiles
ConfigurationProperties
Auto-configuration
REST
Validation
Exception handling
Boot startup
Actuator
```

Again, interview style: **one question at a time**.

### Part 3 — Coding

One problem in roughly the **easy/medium → medium** range using one of the patterns we've studied.

You explain your reasoning while solving it.

### Part 4 — Code Review

I'll give you deliberately problematic Java/Spring code.

You'll identify things such as:

```text
Incorrect DI
Bad exception handling
Mutable state
equals/hashCode bugs
Collection misuse
Poor REST design
Transaction problems
Bad Optional usage
Stream misuse
```

### Part 5 — Result

Not simply pass/fail.

We'll classify each area:

```text
🟢 Strong
🟡 Needs reinforcement
🔴 Gap
```

That result becomes the input for **Month 2 — Advanced Spring + Java Concurrency**.

---

# Updated Month 1 Checklist

| Competency           | Completion Criteria                                                  | Status |
| -------------------- | -------------------------------------------------------------------- | -----: |
| Java Collections     | Explain and correctly choose List/Set/Map implementations            |      ✅ |
| HashMap              | Explain hashing, buckets, collisions and `equals/hashCode`           |      ✅ |
| Generics             | Explain generic types, invariance, wildcards and PECS                |      ✅ |
| Exceptions           | Explain + implement checked/unchecked/custom/resource handling       |     🟡 |
| Immutability         | Design and explain an immutable Java object                          |     🟡 |
| Functional Java      | Comfortably use lambdas, functional interfaces, Streams and Optional |      ☐ |
| Modern Java          | Understand Records, Sealed Classes and Pattern Matching              |      ☐ |
| Spring Core          | Explain IoC and DI independently of annotations                      |      ✅ |
| Spring Beans         | Understand discovery, creation, scopes, lifecycle and injection      |     🟡 |
| Spring Configuration | Use configuration classes, properties and profiles                   |      ☐ |
| Spring Boot          | Explain startup, starters and auto-configuration                     |      ☐ |
| REST                 | Build API with DTOs, validation and exception handling               |      ☐ |
| Coding               | Solve studied patterns without relying on memorized solutions        |     🟡 |
| Code Review          | Identify Java/Spring design and implementation problems              |      ☐ |
| Interview            | Explain concepts clearly in English without notes                    |     🟡 |

One other thing I'd keep separate: **React does not change this calendar.** Your React/TypeScript study remains a secondary afternoon track. Missing React Day 2 doesn't cause us to delay Java/Spring; you'll finish that React block tonight as planned.

So our immediate position is very clear: **today's theory on Exceptions is done, but Exceptions is not yet checked off. Next is the practical exception exercise, followed by interview questions.**
