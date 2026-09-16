---
title: Roadmap
---

# Roadmap — Java + Spring Boot (90 Days, Senior-Level, Parallel Track)

**Objective:** Java Core + Spring Boot to Senior interview level, built as a single 90-day index (Day 01 → Day 90).
**Cadence:** ~1h30/day • 6 days/week • Sunday off.
**Structure:** Java and Spring Boot are studied **in parallel** every day whenever the topics don't have a hard dependency on each other. Where a Spring topic genuinely needs a Java concept that hasn't landed yet, that day leans Java-only (or Spring-only) instead of forcing an artificial pairing.
**Source roadmaps:** [roadmap.sh/java](https://roadmap.sh/java) and [roadmap.sh/spring-boot](https://roadmap.sh/spring-boot) — every row below is mapped to the closest matching node(s) from those two roadmaps.
**Supersedes:** `_old/Plan.md` and `_old/Mês 1 - Plano.md` stay in the repo as history; this file (`Roadmap.md`) is the single source of truth going forward. Update it after each day: flip the status, add the link to that day's note.

**Legend:** ✅ done · 🟡 in progress · ☐ upcoming

**Assumed already known (not scheduled as dedicated days):** basic syntax, variables/data types, conditionals/loops, arrays, strings basics, abstraction/encapsulation/inheritance/interfaces, access specifiers, static keyword basics, packages. These are prerequisite Java-101 material — the existing notes (Day 01 onward) already start well above this level, so the 90 days below go straight into intermediate/advanced/senior territory. If any of these ever feels shaky, slot in a half-day review — it doesn't need its own numbered day.

---

## Progress at a glance

| Month | Days | Focus | Status |
| --- | --- | --- | --- |
| Month 1 | 01–30 | Java Core + Spring Core/Boot fundamentals | 🟡 in progress (9/30 done) |
| Month 2 | 31–60 | Concurrency, Data Access, Security, Testing, Observability | ☐ upcoming |
| Month 3 | 61–90 | Microservices, Cloud, Performance, Capstone project, Final review | ☐ upcoming |

---

## Month 1 — Java Core + Spring Core/Boot Fundamentals (Days 01–30)

### Week 1 — Days 01–06 ✅ complete

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 01 | ✅ | Collections Framework overview (List/Set/Map/Queue/Deque/Stack) | — | java: generic-collections, map, set, queue, dequeue, stack | [Day 01](<[Day 01] Java Collections Framework — Overview.md>) |
| 02 | ✅ | — | Spring Core: IoC, DI, ApplicationContext, Beans | spring-boot: spring-ioc, dependency-injection, terminology | [Day 02](<[Day 02] Spring Core_ IoC, Dependency Injection, B.md>) |
| 03 | ✅ | HashMap & HashSet internals | — | java: map, set | [Day 03](<[Day 03] Java_ HashMap & HashSet.md>) |
| 04 | ✅ | — | Component Scanning & DI (`@Component/@Service/@Repository`, `@Primary/@Qualifier`) | spring-boot: components, configuration | [Day 04](<[Day 04] Spring Component Scanning & Dependency In.md>) |
| 05 | ✅ | `equals()` and `hashCode()` contract | — | java: classes-and-objects (object contract) | [Day 05](<[Day 05] Java_ equals() and hashCode().md>) |
| 06 | ✅ | Comparable, Comparator, TreeMap/TreeSet ordering | — | java: map, set (ordered variants) | [Day 06](<[Day 06] Java Ordering_ Comparable, Comparator, Tr.md>) |

### Week 2 — Days 07–12 🟡 in progress (3/6 done)

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 07 | ✅ | Generics (bounded types, wildcards, PECS, type erasure) | — | java: generic-collections | [Day 07](<[Day 07]  Java Generics.md>) |
| 08 | ✅ | Exceptions & Error Handling (checked/unchecked) | — | java: exception-handling | [Day 08](<[Day 08] Exceptions & Error Handling.md>) |
| 09 | ✅ | `final`, Immutability | Spring Bean Scopes & Lifecycle | java: final-keyword · spring-boot: spring-bean-scope | [Day 09](<[Day 09] final, Immutability, Spring Bean Scopes &.md>) |
| 10 | ☐ | Custom exceptions + try-with-resources (practical) | `@Configuration` + `@Bean` explicit config | java: exception-handling (practical) · spring-boot: configuration | |
| 11 | ☐ | Immutability practice: defensive copying, immutable domain objects | `@Value` + `@ConfigurationProperties` + Profiles | java: final-keyword (applied) · spring-boot: configuration | |
| 12 | ☐ | Week review + mock interview (Generics/Exceptions/Immutability) | Starters + Auto-configuration + Conditional Config | spring-boot: spring-boot-starters, autoconfiguration | |

### Week 3 — Days 13–18 ☐ Functional Java + Spring REST

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 13 | ☐ | Lambda Expressions + Functional Interfaces (`Predicate/Function/Consumer/Supplier`) | `@RestController` + DTOs | java: lambda-expressions, functional-interfaces · spring-boot: spring-mvc | |
| 14 | ☐ | Stream API (`map/filter/flatMap/reduce/Collectors`) + Higher-Order Functions | `@Valid` + Bean Validation | java: stream-api, high-order-functions, functional-composition · spring-boot: spring-mvc | |
| 15 | ☐ | Optionals + Method References | `@ControllerAdvice` + Exception Handling | java: optionals · spring-boot: spring-mvc | |
| 16 | ☐ | Method chaining + coding practice (Two Pointers) | Jackson serialization, HTTP status codes, Pagination | java: method-chaining · spring-boot: spring-mvc | |
| 17 | ☐ | Capstone: small REST API combining lambdas/streams | Servlet fundamentals under Spring MVC | spring-boot: servlet | |
| 18 | ☐ | Week review + mock interview + coding (Sliding Window) | Week review | | |

### Week 4 — Days 19–24 ☐ Modern Java + Spring Boot Internals

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 19 | ☐ | Records | Spring Boot startup flow (`SpringApplication.run` → `ApplicationContext`) | java: record · spring-boot: architecture | |
| 20 | ☐ | Sealed Classes + Pattern Matching + Switch Expressions | Auto-configuration internals | spring-boot: autoconfiguration | |
| 21 | ☐ | Java Memory Model + Object Lifecycle | Embedded Server (Tomcat) | java: java-memory-model, object-lifecycle, lifecycle-of-a-program · spring-boot: embedded-server | |
| 22 | ☐ | Enums + Nested Classes + Initializer Blocks | Actuator basics | java: enums, nested-classes, initializer-block · spring-boot: actuators | |
| 23 | ☐ | Annotations + basic Reflection | Micrometer + basic observability | java: annotations · spring-boot: micrometer | |
| 24 | ☐ | Week review + Java/Spring consolidation + coding review | Week review | | |

### Week 5 — Days 25–30 ☐ Spring Data JPA + Month 1 Evaluation

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 25 | ☐ | JDBC fundamentals + database access | Spring Data JPA intro (entities, repositories) | java: jdbc, database-access · spring-boot: spring-data, spring-data-jpa | |
| 26 | ☐ | Hibernate/ORM concepts | JPA entity lifecycle + relationships (`@OneToMany`, etc.) | java: hibernate · spring-boot: entity-lifecycle, relationships | |
| 27 | ☐ | — | `@Transactional` deep dive · Spring Data JDBC vs JPA | spring-boot: transactions, spring-data-jdbc | |
| 28 | ☐ | JUnit + Mockito fundamentals | Spring Boot Test basics | java: junit, mocking--mockito · spring-boot: testing | |
| 29 | ☐ | — | `MockMvc` + `@MockBean` + `@SpringBootTest` · JPA test slices | spring-boot: mock-mvc, mockbean-annotation, springboottest-annotation, jpa-test | |
| 30 | ☐ | **MONTH 1 EVALUATION** — full interview simulation (Java Core + Spring Core/Boot + Coding + Code Review), same 5-part structure as `Plan.md` | | | |

---

## Month 2 — Concurrency, Data Access, Security, Testing, Observability (Days 31–60)

### Week 6 — Days 31–36 ☐ Concurrency + Spring Security

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 31 | ☐ | Threads: creation, lifecycle, `Runnable`/`Thread` | Spring Security architecture & authentication flow | java: threads · spring-boot: authentication | |
| 32 | ☐ | Concurrency: `synchronized`, locks, race conditions | Authorization, roles | java: concurrency · spring-boot: authorization | |
| 33 | ☐ | `volatile` + Java Memory Model revisit | JWT authentication | java: volatile-keyword · spring-boot: jwt-authentication | |
| 34 | ☐ | Virtual Threads (Project Loom) | OAuth2 basics | java: virtual-threads · spring-boot: oauth2 | |
| 35 | ☐ | Concurrency coding practice + review | Secure a REST endpoint (practical) | | |
| 36 | ☐ | Week review + mock interview (concurrency + security) | | | |

### Week 7 — Days 37–42 ☐ I/O, AOP, Build Tools, Logging

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 37 | ☐ | I/O operations + File operations | Spring AOP: aspects, advice, pointcuts | java: io-operations, file-operations · spring-boot: spring-aop | |
| 38 | ☐ | Networking basics (sockets, HTTP client) | Spring AOP practical (`@Around`, logging aspect) | java: networking | |
| 39 | ☐ | Regular Expressions | Actuator deep dive (custom health indicators) | java: regular-expressions · spring-boot: actuators | |
| 40 | ☐ | Build Tools: Maven vs Gradle | Spring Boot Starters deep dive | java: build-tools, maven, gradle · spring-boot: spring-boot-starters | |
| 41 | ☐ | Logging: SLF4J, Log4j2, Logback | Micrometer + observability practical | java: slf4j, log4j2, logback, logging-frameworks · spring-boot: micrometer | |
| 42 | ☐ | Week review + mock interview | | | |

### Week 8 — Days 43–48 ☐ Testing Deep Dive

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 43 | ☐ | JUnit 5 advanced (parameterized tests, lifecycle) | `@SpringBootTest` deep dive | java: junit · spring-boot: springboottest-annotation, testing | |
| 44 | ☐ | Mockito advanced (stubbing, verification, argument captors) | `@MockBean` advanced | java: mocking--mockito · spring-boot: mockbean-annotation | |
| 45 | ☐ | TestNG overview (compare with JUnit) | `MockMvc` advanced (request builders, matchers) | java: testng · spring-boot: mock-mvc | |
| 46 | ☐ | REST Assured (API testing) | JPA test slices, practical | java: rest-assured · spring-boot: jpa-test | |
| 47 | ☐ | Testing pyramid + TDD kata | | | |
| 48 | ☐ | Week review + mock interview (testing) | | | |

### Week 9 — Days 49–54 ☐ Data Access Deep Dive

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 49 | ☐ | JDBC deep dive (connection pooling, batch ops) | Spring Data relationships advanced (`@OneToMany`/`@ManyToMany`) | java: jdbc · spring-boot: relationships | |
| 50 | ☐ | Hibernate advanced (lazy vs eager, N+1 problem) | `@Transactional` advanced (propagation/isolation) | java: hibernate · spring-boot: transactions | |
| 51 | ☐ | `java.time` (date/time API) | Spring Data JDBC vs JPA tradeoffs | java: date-and-time · spring-boot: spring-data-jdbc | |
| 52 | ☐ | Strings & regex practical | Spring Data MongoDB overview (NoSQL alternative) | java: strings-and-methods · spring-boot: spring-data-mongodb | |
| 53 | ☐ | Data access coding practice (repository pattern kata) | | | |
| 54 | ☐ | Week review + mock interview | | | |

### Week 10 — Days 55–60 ☐ Month 2 Consolidation + Evaluation

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 55 | ☐ | Annotations & reflection deep dive | Spring annotations deep dive (`@Transactional`, `@Async`, custom annotations) | java: annotations · spring-boot: annotations | |
| 56 | ☐ | Modules (JPMS) + Documentation/Javadoc | Spring architecture review | java: modules, documentation, javadoc · spring-boot: architecture | |
| 57 | ☐ | Design patterns via method chaining / builder practice | Servlet + embedded server review | java: method-chaining · spring-boot: servlet, embedded-server | |
| 58 | ☐ | Concurrency + Data Access consolidated review | | | |
| 59 | ☐ | Security + Testing consolidated review + coding practice | | | |
| 60 | ☐ | **MONTH 2 EVALUATION** — interview simulation (Concurrency, Data Access, Security, Testing) + Code Review | | | |

---

## Month 3 — Microservices, Cloud, Performance, Capstone (Days 61–90)

### Week 11 — Days 61–66 ☐ Microservices Foundations

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 61 | ☐ | — | Microservices architecture concepts + Spring Cloud overview | spring-boot: microservices, spring-cloud | |
| 62 | ☐ | — | Service discovery + Eureka practical | spring-boot: eureka | |
| 63 | ☐ | — | Declarative REST clients: Spring Cloud OpenFeign | spring-boot: spring-cloud-open-feign | |
| 64 | ☐ | — | API Gateway patterns: Spring Cloud Gateway | spring-boot: spring-cloud-gateway | |
| 65 | ☐ | — | Resilience patterns: Spring Cloud Circuit Breaker | spring-boot: spring-cloud-circuit-breaker | |
| 66 | ☐ | — | Centralized configuration: Spring Cloud Config | spring-boot: cloud-config | |

### Week 12 — Days 67–72 ☐ Microservices Practical + Security Hardening

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 67 | ☐ | Capstone (part 1): service A + Eureka + Config | | | |
| 68 | ☐ | Capstone (part 2): Feign client + Gateway + Circuit Breaker | | | |
| 69 | ☐ | — | Security hardening: OAuth2 resource server, JWT across services | spring-boot: oauth2, jwt-authentication | |
| 70 | ☐ | Cryptography basics (hashing, encryption) | | java: cryptography | |
| 71 | ☐ | — | Observability across services: tracing basics, Actuator + Micrometer aggregation | | |
| 72 | ☐ | Week review + mock interview (microservices) | | | |

### Week 13 — Days 73–78 ☐ Performance, JVM & Senior-Level Traps

| Day | Status | Java Track | Spring Track | roadmap.sh mapping | Note |
| --- | :-: | --- | --- | --- | --- |
| 73 | ☐ | JVM internals review (memory model, GC tie-in) | Production tuning (thread pools, connection pools) | | |
| 74 | ☐ | Performance testing basics (JMeter overview) + profiling mindset | | java: jmeter | |
| 75 | ☐ | Static vs dynamic binding, method overloading/overriding (senior interview traps) | | java: static-vs-dynamic-binding, method-overloading--overriding | |
| 76 | ☐ | Advanced coding patterns practice (2–3 medium/hard problems) | | | |
| 77 | ☐ | Code review practice: deliberately broken Java/Spring code (bad DI, N+1, poor exception handling, thread-unsafe singletons) | | | |
| 78 | ☐ | Week review + mock interview | | | |

### Week 14 — Days 79–84 ☐ Capstone Project

| Day | Status | Focus | Note |
| --- | :-: | --- | --- |
| 79 | ☐ | Design & entities | Multi-module Spring Boot capstone: domain model + JPA entities |
| 80 | ☐ | Repositories & services | Spring Data JPA repositories + service layer |
| 81 | ☐ | REST controllers & DTOs/validation | `@RestController`, DTOs, `@Valid` |
| 82 | ☐ | Security integration | JWT auth on the capstone API |
| 83 | ☐ | Tests + Actuator | Unit tests (JUnit/Mockito) + `MockMvc` + Actuator |
| 84 | ☐ | Polish + docs + review | Javadoc, README, final cleanup |

### Week 15 — Days 85–90 ☐ Final Review + Program Evaluation

| Day | Status | Focus |
| --- | :-: | --- |
| 85 | ☐ | Full Java Core review (Collections → Modern Java) — self-quiz, no notes |
| 86 | ☐ | Full Spring Core/Boot review (IoC → Auto-configuration → REST) — self-quiz, no notes |
| 87 | ☐ | Full Data/Security/Testing review — self-quiz, no notes |
| 88 | ☐ | Full Microservices/Cloud review — self-quiz, no notes |
| 89 | ☐ | Timed mock interview: 90 minutes, mixed Java + Spring + coding + code review |
| 90 | ☐ | **FINAL PROGRAM EVALUATION** — full 5-part interview simulation + retro (strengths/gaps) → feeds Month 4 (Advanced Spring + Java Concurrency) |

---

## How to use this file going forward

1. Each day, ask for that day's tutorial note (explanation + a runnable code example you can test locally), matching the depth/format of Days 01–09.
2. Once the note exists, flip that row's status to ✅ and add the link, the same way Days 01–09 are linked above.
3. `_old/Plan.md` and `_old/Mês 1 - Plano.md` stay untouched as historical reference for how the plan looked before this restructure.
