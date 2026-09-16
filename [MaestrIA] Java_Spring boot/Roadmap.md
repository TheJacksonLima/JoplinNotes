# Java + Spring Boot — 90-Day Master Roadmap

> **Source of truth from Day 10 onward.** `Plan.md` and `Mês 1 - Plano.md` remain untouched as historical references.

**Cadence:** ~1h30 per study day • 6 days/week • Sunday off  
**Completion rule:** a topic is done only when you can **explain + implement + answer interview questions / review code**.  
**Session outputs:** detailed Joplin note + concise manual notes + Anki TSV (`Front`, `Back`, `Tags`).

## Roadmap sources

- [roadmap.sh — Java](https://roadmap.sh/java)
- [roadmap.sh — Java PDF](https://roadmap.sh/pdfs/roadmaps/java.pdf)
- [roadmap.sh — Spring Boot](https://roadmap.sh/spring-boot)
- [roadmap.sh — Spring Boot PDF](https://roadmap.sh/pdfs/roadmaps/spring-boot.pdf)

## What changed from the original plan

The original Month 1 plan was strong on Collections, Generics, exceptions, functional Java, modern Java, Spring Core/configuration, REST, and basic Boot internals. The roadmap.sh comparison shows several important areas that were missing or underrepresented for a Senior Java/Spring interview path:

- **Java gaps added:** OOP edge cases (static/dynamic binding, pass-by-value), annotations/reflection, I/O/files, Date/Time, regex, networking/HTTP, modules/classpath, Maven/build internals, JDBC, Hibernate/JPA, testing, logging, cryptography, threads/executors, synchronization/atomics, `volatile`, Java Memory Model, `CompletableFuture`, virtual threads, JVM memory/GC and profiling.
- **Spring gaps added:** AOP/proxies, Data JDBC/JPA/MongoDB, entity lifecycle/relationships, transaction propagation/isolation/locking, testing slices (`@SpringBootTest`, MockMvc, JPA tests), Security (authentication/authorization/JWT/OAuth2), microservices, OpenFeign/HTTP clients, Gateway, Cloud Config, Circuit Breaker, Eureka, Micrometer/Actuator and production observability.
- **Ordering improved:** Java and Spring now run in parallel whenever concepts reinforce each other: annotations ↔ configuration, records ↔ DTO/Jackson, exceptions ↔ transactions, generics ↔ repositories, threads/JMM ↔ singleton services and `@Async`, cryptography ↔ Security/JWT, networking ↔ service clients/gateway, logging ↔ Actuator/Micrometer.
- **Alternatives are recognition-only:** you should recognize Gradle/Bazel, TestNG/JMeter/Cucumber, Log4j2/TinyLog, EBean and non-Spring web frameworks, but the hands-on path stays focused on Maven, JUnit/Mockito, SLF4J/Logback, JDBC/JPA, and Spring Boot because that matches the target role.

## Calendar rule

Day 10 is anchored to **2026-09-16**. Study days run Monday–Saturday; Sundays are rest days and are not numbered. If a session slips, move the sequence forward instead of cramming two sessions into one day.

## Master index — Day 01 to Day 90

| Day | Status | Date | Java track | Spring track | roadmap.sh mapping | Note |
|---:|---|---|---|---|---|---|
| 01 | ✅ Done | completed | Collections: List/Set/Map/Queue/Deque, ArrayList vs LinkedList, Big-O | — | Java: Collections; Spring: — | [Java Collections Framework — Overview](./%5BDay%2001%5D%20Java%20Collections%20Framework%20%E2%80%94%20Overview.md) |
| 02 | ✅ Done | completed | — | IoC, DI, ApplicationContext, Beans, constructor injection | Java: Dependency Injection; Spring: Introduction → Dependency Injection / Spring IoC | [Spring Core — IoC & Dependency Injection](./%5BDay%2002%5D%20Spring%20Core_%20IoC%2C%20Dependency%20Injection%2C%20B.md) |
| 03 | ✅ Done | completed | HashMap internals, buckets, collisions, HashSet | — | Java: Collections → Map / Set; Spring: — | [HashMap & HashSet](./%5BDay%2003%5D%20Java_%20HashMap%20%26%20HashSet.md) |
| 04 | ✅ Done | completed | — | Component scanning, stereotypes, @Primary, @Qualifier | Java: Annotations; Spring: Introduction → Annotations / Spring Bean Scope | [Spring Component Scanning](./%5BDay%2004%5D%20Spring%20Component%20Scanning%20%26%20Dependency%20In.md) |
| 05 | ✅ Done | completed | Object equality, hashCode contract, mutable-key trap | — | Java: Object Oriented Programming / Collections; Spring: — | [equals() & hashCode()](./%5BDay%2005%5D%20Java_%20equals%28%29%20and%20hashCode%28%29.md) |
| 06 | ✅ Done | completed | Comparable, Comparator, TreeSet, TreeMap | — | Java: Collections; Spring: — | [Java Ordering](./%5BDay%2006%5D%20Java%20Ordering_%20Comparable%2C%20Comparator%2C%20Tr.md) |
| 07 | ✅ Done | completed | Generic classes/methods, bounds, wildcards, invariance, PECS, erasure | — | Java: Collections → Generic Collections; Spring: — | [Java Generics](./%5BDay%2007%5D%20%20Java%20Generics.md) |
| 08 | ✅ Done | completed | Checked/unchecked, custom exceptions, try-with-resources | @Transactional rollback behavior (supplemental) | Java: Exception Handling / I/O Operations; Spring: Transactions | [Exceptions & Error Handling](./%5BDay%2008%5D%20Exceptions%20%26%20Error%20Handling.md) |
| 09 | ✅ Done | completed | final, immutable objects, defensive copies, HashMap key stability | singleton/prototype/request/session, @PostConstruct, @PreDestroy | Java: Object Oriented Programming → Final Keyword; Spring: Introduction → Spring Bean Scope | [final, Immutability, Bean Scopes & Lifecycle](./%5BDay%2009%5D%20final%2C%20Immutability%2C%20Spring%20Bean%20Scopes%20%26.md) |
| 10 | ✅ Done | completed | Annotations: what metadata is and why frameworks use it | @Configuration, @Bean, explicit bean creation and dependencies | Java: Object Oriented Programming → Annotations; Spring: Introduction → Configuration / Dependency Injection | [Java Annotations & Spring `@Configuration`/`@Bean`](./%5BDay%2010%5D%20Annotations.md) |
| 11 | ⬜ Upcoming | 2026-09-17 | Reflection basics: Class, fields, methods, runtime metadata | @Value and @ConfigurationProperties | Java: Object Oriented Programming → Annotations (reflection support); Spring: Introduction → Configuration | — |
| 12 | ⬜ Upcoming | 2026-09-18 | Enums and switch expressions for finite states | Profiles, @Profile, @ConditionalOnProperty | Java: Object Oriented Programming → Enums / Switch Expressions; Spring: Introduction → Configuration / Autoconfiguration | — |
| 13 | ⬜ Upcoming | 2026-09-19 | Maven lifecycle, dependency scopes, transitive dependencies; recognize Gradle/Bazel | Spring Boot Starters and autoconfiguration | Java: Build Tools → Maven / Gradle / Bazel; Spring: Spring Boot Starters / Autoconfiguration | — |
| 14 | ⬜ Upcoming | 2026-09-21 | Access modifiers, static, nested classes, overloading/overriding, dynamic binding, pass-by-value | Layered Spring architecture and component boundaries | Java: Object Oriented Programming → Basics / More about OOP; Spring: Introduction → Architecture | — |
| 15 | ⬜ Upcoming | 2026-09-22 | Lambda expressions and functional interfaces | Inject multiple strategy beans and select behavior cleanly | Java: Lambda Expressions / Functional Programming → Functional Interfaces; Spring: Introduction → Dependency Injection | — |
| 16 | ⬜ Upcoming | 2026-09-23 | Predicate, Function, Consumer, Supplier | Inject `List<T>` / `Map<String,T>` of beans | Java: Functional Programming → Functional Interfaces / Functional Composition; Spring: Introduction → Dependency Injection | — |
| 17 | ⬜ Upcoming | 2026-09-24 | Stream pipeline, lazy intermediate operations, terminal operations | @RestController returning a typed collection | Java: Functional Programming → Stream API; Spring: Spring MVC | — |
| 18 | ⬜ Upcoming | 2026-09-25 | flatMap, reduce, groupingBy, toMap | Map domain objects to API DTOs | Java: Functional Programming → Stream API; Spring: Spring MVC → Components | — |
| 19 | ⬜ Upcoming | 2026-09-26 | Optional creation, map/flatMap/orElse/orElseGet/orElseThrow | Service/repository not-found handling | Java: Optionals; Spring: Spring Data / Spring MVC | — |
| 20 | ⬜ Upcoming | 2026-09-28 | Method references, andThen/compose | Mapper/service composition | Java: Functional Programming → Functional Composition; Spring: Spring MVC → Components | — |
| 21 | ⬜ Upcoming | 2026-09-29 | Records as immutable data carriers | Request/response DTO serialization with Jackson | Java: Object Oriented Programming → Record; Spring: Spring MVC | — |
| 22 | ⬜ Upcoming | 2026-09-30 | Sealed classes/interfaces and pattern matching | @ControllerAdvice and @ExceptionHandler | Java: Object Oriented Programming → Sealed Types / Pattern Matching; Spring: Spring MVC | — |
| 23 | ⬜ Upcoming | 2026-10-01 | Instant, LocalDate, LocalDateTime, ZonedDateTime, Duration | Jackson date/time serialization and API contracts | Java: Date and Time; Spring: Spring MVC | — |
| 24 | ⬜ Upcoming | 2026-10-02 | Pattern/Matcher and regex boundaries | @Valid, @Pattern, validation errors | Java: Regular Expressions; Spring: Spring MVC | — |
| 25 | ⬜ Upcoming | 2026-10-03 | Path, Files, buffered I/O, try-with-resources | Multipart upload/download endpoint | Java: I/O Operations / File Operations; Spring: Spring MVC | — |
| 26 | ⬜ Upcoming | 2026-10-05 | URI, HTTP client, timeouts, status handling | Spring RestClient/WebClient fundamentals | Java: Networking; Spring: Spring MVC / Microservices | — |
| 27 | ⬜ Upcoming | 2026-10-06 | Java modules/classpath/JAR packaging | Embedded server and Spring Boot executable JAR | Java: Modules / Build Tools; Spring: Embedded Server / Spring Boot | — |
| 28 | ⬜ Upcoming | 2026-10-07 | Logging abstraction vs implementation, parameterized logs | Boot logging levels and configuration | Java: Logging Frameworks → SLF4J / Logback / Log4j2; Spring: Spring Boot | — |
| 29 | ⬜ Upcoming | 2026-10-08 | Dynamic proxies and method interception concept | AOP, @Aspect, cross-cutting concerns | Java: Object Oriented Programming → Annotations / Dynamic Binding; Spring: Introduction → Spring AOP | — |
| 30 | ⬜ Upcoming | 2026-10-09 | JVM process, heap/stack/class loading overview | SpringApplication startup, ApplicationContext, autoconfiguration report, Actuator intro | Java: Lifecycle of a Program / Java Memory Model (preview); Spring: Spring Boot / Autoconfiguration / Actuators / Embedded Server | — |
| 31 | ⬜ Upcoming | 2026-10-10 | JDBC connection/statement/result-set lifecycle | Spring DataSource and JdbcTemplate | Java: Database Access → JDBC; Spring: Spring Data JDBC / Spring Data | — |
| 32 | ⬜ Upcoming | 2026-10-12 | PreparedStatement, parameter binding, transaction boundaries | Spring Data JDBC repositories | Java: Database Access → JDBC; Spring: Spring Data JDBC | — |
| 33 | ⬜ Upcoming | 2026-10-13 | POJO identity and object mapping concepts | @Entity, @Id, repository basics | Java: Database Access → Hibernate / Spring Data JPA; Spring: Hibernate / Spring Data JPA | — |
| 34 | ⬜ Upcoming | 2026-10-14 | Object reachability and lifecycle review | Transient, managed, detached, removed; persistence context | Java: Object Oriented Programming → Object Lifecycle; Spring: Entity Lifecycle / Hibernate | — |
| 35 | ⬜ Upcoming | 2026-10-15 | Set/List semantics in aggregates | @OneToMany, @ManyToOne, ownership | Java: Collections; Spring: Relationships / Spring Data JPA | — |
| 36 | ⬜ Upcoming | 2026-10-16 | equals/hashCode rules revisited for persisted identity | Fetch types, lazy proxies, N+1 query problem | Java: Object Oriented Programming / Collections; Spring: Hibernate / Relationships | — |
| 37 | ⬜ Upcoming | 2026-10-17 | Generic interfaces and type bounds revisited | CrudRepository/JpaRepository generics and derived queries | Java: Collections → Generic Collections; Spring: Spring Data / Spring Data JPA | — |
| 38 | ⬜ Upcoming | 2026-10-19 | Comparator review and stable ordering | Pageable, Page, Sort | Java: Collections; Spring: Spring Data JPA | — |
| 39 | ⬜ Upcoming | 2026-10-20 | Exception propagation review | @Transactional propagation and rollback rules | Java: Exception Handling; Spring: Transactions | — |
| 40 | ⬜ Upcoming | 2026-10-21 | Thread states, race conditions, Executor basics | Transaction isolation, optimistic/pessimistic locking | Java: Concurrency → Threads; Spring: Transactions / Spring Data JPA | — |
| 41 | ⬜ Upcoming | 2026-10-22 | Executors, task submission, lifecycle | Spring @Async and TaskExecutor | Java: Concurrency → Threads; Spring: Introduction → Task Execution (terminology) / Spring Boot | — |
| 42 | ⬜ Upcoming | 2026-10-23 | synchronized, Lock, AtomicInteger | Singleton services under concurrent requests | Java: Concurrency → Threads; Spring: Introduction → Spring Bean Scope | — |
| 43 | ⬜ Upcoming | 2026-10-24 | Visibility, happens-before, volatile vs atomicity | Memory visibility in singleton beans | Java: Concurrency → volatile keyword / Java Memory Model; Spring: Introduction → Spring Bean Scope | — |
| 44 | ⬜ Upcoming | 2026-10-26 | CompletableFuture pipelines and exception handling | Compose independent service calls asynchronously | Java: Concurrency → Threads / Functional Programming; Spring: Microservices | — |
| 45 | ⬜ Upcoming | 2026-10-27 | Virtual thread model and blocking I/O tradeoffs | Spring Boot virtual-thread support | Java: Concurrency → Virtual Threads; Spring: Spring Boot | — |
| 46 | ⬜ Upcoming | 2026-10-28 | ScheduledExecutorService | Spring @Scheduled | Java: Concurrency → Threads; Spring: Introduction → Task Execution (terminology) | — |
| 47 | ⬜ Upcoming | 2026-10-29 | Assertions, lifecycle, parameterized tests | Unit-test Spring services without loading a context | Java: Testing → Unit Testing → JUnit; Spring: Testing | — |
| 48 | ⬜ Upcoming | 2026-10-30 | Mocks, stubs, verification, test doubles | Mock repository dependencies in service tests | Java: Testing → Mocking → Mockito; Spring: Testing | — |
| 49 | ⬜ Upcoming | 2026-10-31 | Integration-test principles | @SpringBootTest, context loading | Java: Testing → Integration Testing; Spring: Testing → @SpringBootTest Annotation | — |
| 50 | ⬜ Upcoming | 2026-11-02 | HTTP contract assertions | @WebMvcTest and MockMvc | Java: Testing → Integration Testing; Spring: Testing → Mock MVC | — |
| 51 | ⬜ Upcoming | 2026-11-03 | Database test isolation | @DataJpaTest; real database with Testcontainers | Java: Testing → Integration Testing; Spring: Testing → JPA Test | — |
| 52 | ⬜ Upcoming | 2026-11-04 | End-to-end test thinking; recognize REST Assured/JMeter/Cucumber roles | Run a real Boot server and exercise HTTP | Java: Testing → REST Assured / JMeter / Cucumber-JVM; Spring: Testing | — |
| 53 | ⬜ Upcoming | 2026-11-05 | Hashing vs encryption, salts, secure random | Spring Security introduction and PasswordEncoder | Java: Cryptography; Spring: Spring Security | — |
| 54 | ⬜ Upcoming | 2026-11-06 | Identity/authentication concepts | Spring Security filter chain and authentication | Java: Cryptography / Web Frameworks; Spring: Spring Security → Authentication | — |
| 55 | ⬜ Upcoming | 2026-11-07 | Roles vs permissions; least privilege | @PreAuthorize and authorization rules | Java: Object Oriented Programming → Interfaces (policy abstraction); Spring: Spring Security → Authorization | — |
| 56 | ⬜ Upcoming | 2026-11-09 | Base64URL, signatures, claims, expiration | JWT resource-server authentication | Java: Cryptography; Spring: Spring Security → JWT Authentication | — |
| 57 | ⬜ Upcoming | 2026-11-10 | Protocol roles: resource owner/client/resource server/authorization server | OAuth2 resource server / OIDC login | Java: Networking / Cryptography; Spring: Spring Security → OAuth2 | — |
| 58 | ⬜ Upcoming | 2026-11-11 | HTTP/browser security boundaries | Spring Security CORS/CSRF configuration | Java: Networking; Spring: Spring Security | — |
| 59 | ⬜ Upcoming | 2026-11-12 | Annotations/proxies review | Test authenticated/authorized MVC methods | Java: Annotations / Dynamic Binding; Spring: Testing / Spring Security | — |
| 60 | ⬜ Upcoming | 2026-11-13 | Review Java Core, functional Java, concurrency, testing | Review configuration, MVC, data, transactions, security | Java: Multiple roadmap sections; Spring: Multiple roadmap sections | — |
| 61 | ⬜ Upcoming | 2026-11-14 | Packages/modules, cohesion and coupling | Microservice responsibilities and tradeoffs | Java: Modules / Object Oriented Programming; Spring: Microservices | — |
| 62 | ⬜ Upcoming | 2026-11-16 | Networking failure modes and timeouts | Declarative/service-to-service HTTP client | Java: Networking; Spring: Spring Cloud Open Feign / Microservices | — |
| 63 | ⬜ Upcoming | 2026-11-17 | DNS/service naming concepts | Eureka/service discovery concepts | Java: Networking; Spring: Spring Cloud → Eureka | — |
| 64 | ⬜ Upcoming | 2026-11-18 | Configuration precedence and environment variables | Spring Cloud Config | Java: Build Tools / Modules (configuration delivery); Spring: Spring Cloud → Cloud Config | — |
| 65 | ⬜ Upcoming | 2026-11-19 | Request/response abstractions | Spring Cloud Gateway routes and filters | Java: Networking; Spring: Spring Cloud → Spring Cloud Gateway | — |
| 66 | ⬜ Upcoming | 2026-11-20 | Exception classification and retry-safe operations | Spring Cloud Circuit Breaker / Resilience4j | Java: Exception Handling; Spring: Spring Cloud → Spring Cloud Circuit Breaker | — |
| 67 | ⬜ Upcoming | 2026-11-21 | Semaphores and bounded resources | Resilience patterns around remote calls | Java: Concurrency → Threads; Spring: Spring Cloud Circuit Breaker / Microservices | — |
| 68 | ⬜ Upcoming | 2026-11-23 | Observer-style decoupling and event objects | ApplicationEventPublisher and @EventListener | Java: Object Oriented Programming → Interfaces; Spring: Introduction → Terminology (event model) | — |
| 69 | ⬜ Upcoming | 2026-11-24 | Serialization, keys, partitions, ordering | Event-driven microservice integration | Java: Networking / Functional Programming (applied); Spring: Microservices (ecosystem extension) | — |
| 70 | ⬜ Upcoming | 2026-11-25 | Set/map deduplication patterns applied | Idempotent consumers, retry topics, dead-letter handling | Java: Collections → Set / Map; Spring: Microservices (ecosystem extension) | — |
| 71 | ⬜ Upcoming | 2026-11-26 | HashMap cache semantics, eviction tradeoffs | Spring Cache / @Cacheable | Java: Collections → Map; Spring: Spring Boot ecosystem / Microservices | — |
| 72 | ⬜ Upcoming | 2026-11-27 | Serialization/key design | External cache for multi-instance applications | Java: Database Access (NoSQL adjacent); Spring: Microservices / related Redis roadmap | — |
| 73 | ⬜ Upcoming | 2026-11-28 | Counters/timers as quantitative feedback | Micrometer meters and Actuator metrics | Java: Logging Frameworks / Java runtime observation; Spring: Spring Cloud → Micrometer / Actuators | — |
| 74 | ⬜ Upcoming | 2026-11-30 | Thread/context propagation concepts | Actuator health/info, Micrometer tracing concepts | Java: Concurrency / Logging Frameworks; Spring: Actuators / Micrometer | — |
| 75 | ⬜ Upcoming | 2026-12-01 | Enums/records for explicit API contracts | Spring MVC response status and ProblemDetail | Java: Object Oriented Programming → Record / Enum; Spring: Spring MVC | — |
| 76 | ⬜ Upcoming | 2026-12-02 | Predicate composition | Spring Data JPA Specifications / dynamic queries | Java: Functional Programming → Functional Composition; Spring: Spring Data JPA | — |
| 77 | ⬜ Upcoming | 2026-12-03 | Document modeling and Java records/classes | Mongo repositories and document mapping | Java: Database Access; Spring: Spring Data MongoDB | — |
| 78 | ⬜ Upcoming | 2026-12-04 | Interfaces/adapters and substitution | Spring Data JDBC vs JPA vs MongoDB | Java: Database Access → JDBC / Hibernate / Spring Data JPA; Spring: Spring Data JDBC / JPA / MongoDB | — |
| 79 | ⬜ Upcoming | 2026-12-05 | Fakes/stubs at network boundaries | WireMock/MockWebServer style client tests | Java: Testing → Integration Testing; Spring: Testing / Microservices | — |
| 80 | ⬜ Upcoming | 2026-12-07 | Resource lifecycle in tests | Spring Boot + PostgreSQL/Kafka containers | Java: Testing → Integration Testing; Spring: Testing | — |
| 81 | ⬜ Upcoming | 2026-12-08 | Heap, stack, metaspace, allocation, GC basics | Diagnose memory behavior in Boot services | Java: Java Memory Model / Lifecycle of a Program; Spring: Spring Boot | — |
| 82 | ⬜ Upcoming | 2026-12-09 | Measure CPU/allocation before optimizing | Actuator metrics + Java Flight Recorder workflow | Java: Java runtime / performance; Spring: Actuators / Micrometer | — |
| 83 | ⬜ Upcoming | 2026-12-10 | Queues, backpressure, bounded executors | Protect Spring services from overload | Java: Collections → Queue / Concurrency; Spring: Microservices / Spring Boot | — |
| 84 | ⬜ Upcoming | 2026-12-11 | Input trust boundaries and secure coding | Security headers, validation, authz, secrets | Java: Cryptography / Networking; Spring: Spring Security | — |
| 85 | ⬜ Upcoming | 2026-12-12 | JAR/runtime process model | Containerize a Spring Boot application | Java: Build Tools; Spring: Spring Boot / related Docker roadmap | — |
| 86 | ⬜ Upcoming | 2026-12-14 | Environment variables and process configuration | Boot config precedence, profiles, secret injection | Java: Build Tools / Java runtime; Spring: Introduction → Configuration / Spring Cloud Config | — |
| 87 | ⬜ Upcoming | 2026-12-15 | Interfaces, dependency direction, immutability | Organize Spring adapters around domain/application boundaries | Java: Object Oriented Programming; Spring: Architecture / Dependency Injection | — |
| 88 | ⬜ Upcoming | 2026-12-16 | Data structures, concurrency, caching, API contracts | Compose MVC, data, cache, security, observability, messaging | Java: Multiple Java sections; Spring: Multiple Spring Boot sections | — |
| 89 | ⬜ Upcoming | 2026-12-17 | Java Core/concurrency/JVM/coding review | Spring Core/MVC/Data/Security/Microservices review | Java: All covered sections; Spring: All covered sections | — |
| 90 | ⬜ Upcoming | 2026-12-18 | Java evaluation: core, functional, concurrency, JVM, persistence, testing | Spring evaluation: core, Boot, MVC, Data, Security, testing, microservices | Java: All covered sections; Spring: All covered sections | — |

## Completed sessions — Day 01 to Day 09

### Day 01 — [Java Collections Framework — Overview](./%5BDay%2001%5D%20Java%20Collections%20Framework%20%E2%80%94%20Overview.md) ✅

- **Java:** Collections: List/Set/Map/Queue/Deque, ArrayList vs LinkedList, Big-O
- **Spring:** —
- **roadmap.sh:** Java — Collections; Spring — —

### Day 02 — [Spring Core — IoC & Dependency Injection](./%5BDay%2002%5D%20Spring%20Core_%20IoC%2C%20Dependency%20Injection%2C%20B.md) ✅

- **Java:** —
- **Spring:** IoC, DI, ApplicationContext, Beans, constructor injection
- **roadmap.sh:** Java — Dependency Injection; Spring — Introduction → Dependency Injection / Spring IoC

### Day 03 — [HashMap & HashSet](./%5BDay%2003%5D%20Java_%20HashMap%20%26%20HashSet.md) ✅

- **Java:** HashMap internals, buckets, collisions, HashSet
- **Spring:** —
- **roadmap.sh:** Java — Collections → Map / Set; Spring — —

### Day 04 — [Spring Component Scanning](./%5BDay%2004%5D%20Spring%20Component%20Scanning%20%26%20Dependency%20In.md) ✅

- **Java:** —
- **Spring:** Component scanning, stereotypes, @Primary, @Qualifier
- **roadmap.sh:** Java — Annotations; Spring — Introduction → Annotations / Spring Bean Scope

### Day 05 — [equals() & hashCode()](./%5BDay%2005%5D%20Java_%20equals%28%29%20and%20hashCode%28%29.md) ✅

- **Java:** Object equality, hashCode contract, mutable-key trap
- **Spring:** —
- **roadmap.sh:** Java — Object Oriented Programming / Collections; Spring — —

### Day 06 — [Java Ordering](./%5BDay%2006%5D%20Java%20Ordering_%20Comparable%2C%20Comparator%2C%20Tr.md) ✅

- **Java:** Comparable, Comparator, TreeSet, TreeMap
- **Spring:** —
- **roadmap.sh:** Java — Collections; Spring — —

### Day 07 — [Java Generics](./%5BDay%2007%5D%20%20Java%20Generics.md) ✅

- **Java:** Generic classes/methods, bounds, wildcards, invariance, PECS, erasure
- **Spring:** —
- **roadmap.sh:** Java — Collections → Generic Collections; Spring — —

### Day 08 — [Exceptions & Error Handling](./%5BDay%2008%5D%20Exceptions%20%26%20Error%20Handling.md) ✅

- **Java:** Checked/unchecked, custom exceptions, try-with-resources
- **Spring:** @Transactional rollback behavior (supplemental)
- **roadmap.sh:** Java — Exception Handling / I/O Operations; Spring — Transactions

### Day 09 — [final, Immutability, Bean Scopes & Lifecycle](./%5BDay%2009%5D%20final%2C%20Immutability%2C%20Spring%20Bean%20Scopes%20%26.md) ✅

- **Java:** final, immutable objects, defensive copies, HashMap key stability
- **Spring:** singleton/prototype/request/session, @PostConstruct, @PreDestroy
- **roadmap.sh:** Java — Object Oriented Programming → Final Keyword; Spring — Introduction → Spring Bean Scope

---

# Detailed plan — Day 10 to Day 90

All Spring snippets assume the existing Spring Boot study project. Add snippets under a package scanned by the application, add the named starter/dependency when needed, and run with `./mvnw spring-boot:run`. Plain Java snippets can be copied into a matching `DayXX.java` and run directly.

## Day 10 — @Configuration and @Bean ✅

**Planned date:** 2026-09-16  
**Java track:** Annotations: what metadata is and why frameworks use it  
**Spring track:** @Configuration, @Bean, explicit bean creation and dependencies  
**roadmap.sh mapping:** Java — Object Oriented Programming → Annotations; Spring Boot — Introduction → Configuration / Dependency Injection
**Note:** [Java Annotations & Spring `@Configuration`/`@Bean`](./%5BDay%2010%5D%20Annotations.md)

### Tutorial

Java annotations attach metadata to program elements; Spring reads that metadata to build the application context. `@Configuration` marks a class that declares bean definitions, while `@Bean` tells Spring to manage the object returned by a method. This is especially useful for third-party classes or when construction needs explicit logic.

### Runnable mini-lab

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    GreetingService greetingService() {
        return new GreetingService("Hello from @Bean");
    }
}

class GreetingService {
    private final String message;
    GreetingService(String message) { this.message = message; }
    String message() { return message; }
}
```

### Verify

Add the file under a scanned package, inject `GreetingService` into a `CommandLineRunner`, run the app, and print `message()`.

### Done when

- [x] Explain the concept in English without notes.
- [x] Run/implement the mini-lab and intentionally change or break one thing.
- [x] Answer 3–5 interview questions or review a short code sample.
- [x] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 11 — External Configuration and Property Binding ⬜

**Planned date:** 2026-09-17  
**Java track:** Reflection basics: Class, fields, methods, runtime metadata  
**Spring track:** @Value and @ConfigurationProperties  
**roadmap.sh mapping:** Java — Object Oriented Programming → Annotations (reflection support); Spring Boot — Introduction → Configuration

### Tutorial

Reflection lets Java inspect types and metadata at runtime; frameworks use it heavily for binding and wiring. In Spring Boot, `@Value` works for a small number of values, while `@ConfigurationProperties` is preferable for grouped, typed configuration.

### Runnable mini-lab

```java
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rentmap")
public record RentMapProperties(String city, int maxResults) {}

// application.properties
// rentmap.city=Curitiba
// rentmap.max-results=100
```

### Verify

Enable configuration-properties scanning, inject `RentMapProperties`, print the values, then change `application.properties` and rerun.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 12 — Profiles and Conditional Configuration ⬜

**Planned date:** 2026-09-18  
**Java track:** Enums and switch expressions for finite states  
**Spring track:** Profiles, @Profile, @ConditionalOnProperty  
**roadmap.sh mapping:** Java — Object Oriented Programming → Enums / Switch Expressions; Spring Boot — Introduction → Configuration / Autoconfiguration

### Tutorial

Enums model a closed set of states and pair naturally with modern switch expressions. Spring profiles and conditional beans solve a similar configuration problem at runtime: choose which implementation exists based on environment or properties.

### Runnable mini-lab

```java
enum Environment { DEV, PROD }

public class Day12 {
    public static void main(String[] args) {
        Environment env = Environment.DEV;
        String url = switch (env) {
            case DEV -> "http://localhost:8080";
            case PROD -> "https://api.example.com";
        };
        System.out.println(url);
    }
}
```

### Verify

Run the class, then create two Spring beans with `@Profile("dev")` and `@Profile("prod")` and start the app with each profile.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 13 — Maven, Starters and Autoconfiguration ⬜

**Planned date:** 2026-09-19  
**Java track:** Maven lifecycle, dependency scopes, transitive dependencies; recognize Gradle/Bazel  
**Spring track:** Spring Boot Starters and autoconfiguration  
**roadmap.sh mapping:** Java — Build Tools → Maven / Gradle / Bazel; Spring Boot — Spring Boot Starters / Autoconfiguration

### Tutorial

Maven turns source code plus a dependency graph into a reproducible build. Spring Boot starters provide curated dependency sets, while autoconfiguration creates sensible beans when required classes and conditions are present.

### Runnable mini-lab

```java
// Run in the project root:
// ./mvnw dependency:tree
// ./mvnw clean test
// ./mvnw package

// Then inspect META-INF/MANIFEST.MF inside the generated Boot JAR.
public class Day13 {
    public static void main(String[] args) {
        System.out.println("Build with Maven, then run the packaged JAR");
    }
}
```

### Verify

Run the three Maven commands, identify which starter brings Jackson/Tomcat, then run `java -jar target/<app>.jar`.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 14 — OOP Gap Audit + Spring Architecture ⬜

**Planned date:** 2026-09-21  
**Java track:** Access modifiers, static, nested classes, overloading/overriding, dynamic binding, pass-by-value  
**Spring track:** Layered Spring architecture and component boundaries  
**roadmap.sh mapping:** Java — Object Oriented Programming → Basics / More about OOP; Spring Boot — Introduction → Architecture

### Tutorial

These OOP topics are foundational interview material even for senior engineers. Java is always pass-by-value, including object references; dynamic dispatch chooses overridden instance methods at runtime. Spring layers should reflect responsibilities rather than become annotation-only folders.

### Runnable mini-lab

```java
class Animal { void speak() { System.out.println("animal"); } }
class Dog extends Animal { @Override void speak() { System.out.println("dog"); } }

public class Day14 {
    static void reassign(Dog d) { d = new Dog(); }
    public static void main(String[] args) {
        Animal a = new Dog();
        a.speak();
        Dog d = new Dog();
        reassign(d);
        d.speak();
    }
}
```

### Verify

Run it and explain why both calls print `dog`, and why `reassign` does not replace the caller variable.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 15 — Lambdas and Strategy Injection ⬜

**Planned date:** 2026-09-22  
**Java track:** Lambda expressions and functional interfaces  
**Spring track:** Inject multiple strategy beans and select behavior cleanly  
**roadmap.sh mapping:** Java — Lambda Expressions / Functional Programming → Functional Interfaces; Spring Boot — Introduction → Dependency Injection

### Tutorial

A lambda is an implementation of a functional interface. Spring dependency injection and the Strategy pattern combine well: Spring owns implementations, while Java functions can express small behavior transformations without extra classes.

### Runnable mini-lab

```java
import java.util.function.Function;

public class Day15 {
    public static void main(String[] args) {
        Function<Double, Double> addTax = price -> price * 1.10;
        System.out.println(addTax.apply(100.0));
    }
}
```

### Verify

Run it, then create two Spring `PricingStrategy` beans and inject `List<PricingStrategy>` into a service.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 16 — Core Functional Interfaces + Bean Collections ⬜

**Planned date:** 2026-09-23  
**Java track:** Predicate, Function, Consumer, Supplier  
**Spring track:** Inject `List<T>` / `Map<String,T>` of beans  
**roadmap.sh mapping:** Java — Functional Programming → Functional Interfaces / Functional Composition; Spring Boot — Introduction → Dependency Injection

### Tutorial

The four standard interfaces cover most everyday functional code: test, transform, consume, and supply. Spring can inject every bean implementing an interface, which is useful for pipelines and strategy registries.

### Runnable mini-lab

```java
import java.util.function.*;

public class Day16 {
    public static void main(String[] args) {
        Predicate<Integer> positive = n -> n > 0;
        Function<Integer, Integer> twice = n -> n * 2;
        Consumer<Integer> print = System.out::println;
        Supplier<Integer> start = () -> 21;
        if (positive.test(start.get())) print.accept(twice.apply(start.get()));
    }
}
```

### Verify

Run it; then inject `Map<String, NotificationService>` in Spring and print the bean names.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 17 — Streams: map/filter + REST Collection Endpoint ⬜

**Planned date:** 2026-09-24  
**Java track:** Stream pipeline, lazy intermediate operations, terminal operations  
**Spring track:** @RestController returning a typed collection  
**roadmap.sh mapping:** Java — Functional Programming → Stream API; Spring Boot — Spring MVC

### Tutorial

A stream describes a data-processing pipeline without mutating the source collection. Pair it with a REST endpoint by filtering domain objects and mapping them to response DTOs before serialization.

### Runnable mini-lab

```java
import java.util.List;

public class Day17 {
    public static void main(String[] args) {
        var prices = List.of(900, 1500, 2500, 4000);
        var result = prices.stream().filter(p -> p >= 2000).map(p -> "R$ " + p).toList();
        System.out.println(result);
    }
}
```

### Verify

Run it, then expose the same transformation from a `GET /properties` controller endpoint.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 18 — flatMap, reduce, Collectors + DTO Mapping ⬜

**Planned date:** 2026-09-25  
**Java track:** flatMap, reduce, groupingBy, toMap  
**Spring track:** Map domain objects to API DTOs  
**roadmap.sh mapping:** Java — Functional Programming → Stream API; Spring Boot — Spring MVC → Components

### Tutorial

`flatMap` flattens nested structures, `reduce` combines elements into one result, and collectors build richer aggregate structures. In Spring APIs, these operations are useful for DTO transformation and grouping without leaking entities.

### Runnable mini-lab

```java
import java.util.List;

public class Day18 {
    public static void main(String[] args) {
        var groups = List.of(List.of(1,2), List.of(3,4));
        int sum = groups.stream().flatMap(List::stream).reduce(0, Integer::sum);
        System.out.println(sum);
    }
}
```

### Verify

Run it, then map a `Property` list to `PropertyResponse` records with `stream().map(...)`.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 19 — Optional + Not-Found Flow ⬜

**Planned date:** 2026-09-26  
**Java track:** Optional creation, map/flatMap/orElse/orElseGet/orElseThrow  
**Spring track:** Service/repository not-found handling  
**roadmap.sh mapping:** Java — Optionals; Spring Boot — Spring Data / Spring MVC

### Tutorial

`Optional` models presence or absence explicitly; it is most useful as a return type, not as a replacement for every nullable field. In a service layer, `orElseThrow` makes repository absence become a domain-specific failure.

### Runnable mini-lab

```java
import java.util.Optional;

public class Day19 {
    public static void main(String[] args) {
        Optional<String> city = Optional.of("Curitiba");
        System.out.println(city.map(String::toUpperCase).orElse("UNKNOWN"));
    }
}
```

### Verify

Run it; then implement `repository.findById(id).orElseThrow(PropertyNotFoundException::new)` in your Spring service.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 20 — Method References and Functional Composition ⬜

**Planned date:** 2026-09-28  
**Java track:** Method references, andThen/compose  
**Spring track:** Mapper/service composition  
**roadmap.sh mapping:** Java — Functional Programming → Functional Composition; Spring Boot — Spring MVC → Components

### Tutorial

Method references are concise lambda forms when an existing method already matches the target functional interface. Function composition lets small transformations remain testable and reusable.

### Runnable mini-lab

```java
import java.util.function.Function;

public class Day20 {
    public static void main(String[] args) {
        Function<String,String> trim = String::trim;
        Function<String,String> upper = String::toUpperCase;
        System.out.println(trim.andThen(upper).apply("  batel  "));
    }
}
```

### Verify

Run it, then extract a Spring DTO mapper and use method references in a stream.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 21 — Records + Jackson DTOs ⬜

**Planned date:** 2026-09-29  
**Java track:** Records as immutable data carriers  
**Spring track:** Request/response DTO serialization with Jackson  
**roadmap.sh mapping:** Java — Object Oriented Programming → Record; Spring Boot — Spring MVC

### Tutorial

Records remove boilerplate for immutable data carriers and are excellent API DTOs when their semantics fit. Jackson supports records directly in modern Spring Boot, so JSON maps naturally to record components.

### Runnable mini-lab

```java
public record PropertyResponse(long id, String title, double price) {}

class Day21 {
    public static void main(String[] args) {
        var dto = new PropertyResponse(1L, "Batel apartment", 3500);
        System.out.println(dto);
    }
}
```

### Verify

Run it, then return `PropertyResponse` from a controller and inspect the JSON with curl.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 22 — Sealed Types + Global Exception Handling ⬜

**Planned date:** 2026-09-30  
**Java track:** Sealed classes/interfaces and pattern matching  
**Spring track:** @ControllerAdvice and @ExceptionHandler  
**roadmap.sh mapping:** Java — Object Oriented Programming → Sealed Types / Pattern Matching; Spring Boot — Spring MVC

### Tutorial

Sealed hierarchies make permitted subtypes explicit, which improves exhaustive reasoning. A Spring global exception handler is another closed mapping problem: translate domain failures into consistent HTTP responses.

### Runnable mini-lab

```java
sealed interface Failure permits NotFound, Invalid {}
record NotFound(String message) implements Failure {}
record Invalid(String message) implements Failure {}

public class Day22 {
    static int status(Failure f) {
        return switch (f) { case NotFound n -> 404; case Invalid i -> 400; };
    }
    public static void main(String[] args) { System.out.println(status(new NotFound("x"))); }
}
```

### Verify

Run it, then create a `@RestControllerAdvice` mapping your two exception types to 404 and 400.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 23 — Date/Time API + JSON Dates ⬜

**Planned date:** 2026-10-01  
**Java track:** Instant, LocalDate, LocalDateTime, ZonedDateTime, Duration  
**Spring track:** Jackson date/time serialization and API contracts  
**roadmap.sh mapping:** Java — Date and Time; Spring Boot — Spring MVC

### Tutorial

Choose time types by semantics: `Instant` for a point on the global timeline, `LocalDate` for date-only values, and zoned types when region rules matter. API contracts should avoid ambiguous timestamps.

### Runnable mini-lab

```java
import java.time.*;

public class Day23 {
    public static void main(String[] args) {
        Instant now = Instant.now();
        System.out.println(now);
        System.out.println(Duration.between(now, now.plusSeconds(90)).toSeconds());
    }
}
```

### Verify

Run it, then expose an `Instant createdAt` field from a Spring DTO and inspect its JSON representation.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 24 — Regular Expressions + Bean Validation ⬜

**Planned date:** 2026-10-02  
**Java track:** Pattern/Matcher and regex boundaries  
**Spring track:** @Valid, @Pattern, validation errors  
**roadmap.sh mapping:** Java — Regular Expressions; Spring Boot — Spring MVC

### Tutorial

Regex is useful for structural validation, but complex business rules belong in code. Bean Validation keeps input constraints close to DTO contracts and lets Spring reject invalid requests before service logic executes.

### Runnable mini-lab

```java
import java.util.regex.Pattern;

public class Day24 {
    public static void main(String[] args) {
        var cep = Pattern.compile("\\d{5}-?\\d{3}");
        System.out.println(cep.matcher("80000-000").matches());
    }
}
```

### Verify

Run it, then put `@Pattern` on a request DTO and POST one valid and one invalid payload.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 25 — I/O and File Operations + Multipart ⬜

**Planned date:** 2026-10-03  
**Java track:** Path, Files, buffered I/O, try-with-resources  
**Spring track:** Multipart upload/download endpoint  
**roadmap.sh mapping:** Java — I/O Operations / File Operations; Spring Boot — Spring MVC

### Tutorial

Modern Java file APIs prefer `Path` and `Files`. Spring MVC can receive multipart files, but production code should enforce size/type constraints and avoid trusting original filenames.

### Runnable mini-lab

```java
import java.nio.file.*;

public class Day25 {
    public static void main(String[] args) throws Exception {
        Path p = Path.of("day25.txt");
        Files.writeString(p, "RentMap\n");
        System.out.println(Files.readString(p));
        Files.deleteIfExists(p);
    }
}
```

### Verify

Run it; then create a Spring `MultipartFile` endpoint that prints file size and original name.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 26 — Networking and Java HTTP Client + RestClient ⬜

**Planned date:** 2026-10-05  
**Java track:** URI, HTTP client, timeouts, status handling  
**Spring track:** Spring RestClient/WebClient fundamentals  
**roadmap.sh mapping:** Java — Networking; Spring Boot — Spring MVC / Microservices

### Tutorial

Backend services communicate over unreliable networks, so every client call needs explicit timeout and error semantics. Java has a built-in HTTP client; Spring adds higher-level clients that integrate with its ecosystem.

### Runnable mini-lab

```java
import java.net.URI;
import java.net.http.*;

public class Day26 {
    public static void main(String[] args) throws Exception {
        var client = HttpClient.newHttpClient();
        var req = HttpRequest.newBuilder(URI.create("https://example.com")).GET().build();
        var res = client.send(req, HttpResponse.BodyHandlers.ofString());
        System.out.println(res.statusCode());
    }
}
```

### Verify

Run it with internet access; in your Spring app, repeat using `RestClient` against a local endpoint.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 27 — Modules, Classpath and Executable JAR ⬜

**Planned date:** 2026-10-06  
**Java track:** Java modules/classpath/JAR packaging  
**Spring track:** Embedded server and Spring Boot executable JAR  
**roadmap.sh mapping:** Java — Modules / Build Tools; Spring Boot — Embedded Server / Spring Boot

### Tutorial

The JVM must resolve classes from modules or the classpath before execution. Spring Boot packages dependencies and a launcher into an executable JAR, usually running an embedded server instead of deploying a WAR.

### Runnable mini-lab

```java
public class Day27 {
    public static void main(String[] args) {
        System.out.println(Day27.class.getModule().getName());
        System.out.println(System.getProperty("java.class.path"));
    }
}
```

### Verify

Compile/run it, then package your Boot project and inspect `BOOT-INF/classes` and `BOOT-INF/lib` inside the JAR.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 28 — Logging: SLF4J/Logback + Spring Logging ⬜

**Planned date:** 2026-10-07  
**Java track:** Logging abstraction vs implementation, parameterized logs  
**Spring track:** Boot logging levels and configuration  
**roadmap.sh mapping:** Java — Logging Frameworks → SLF4J / Logback / Log4j2; Spring Boot — Spring Boot

### Tutorial

Use a logging facade such as SLF4J and avoid string concatenation in hot paths. Spring Boot defaults to Logback and lets you configure levels per package through properties.

### Runnable mini-lab

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Day28 {
    private static final Logger log = LoggerFactory.getLogger(Day28.class);
    public static void main(String[] args) { log.info("Property id={}", 42); }
}
```

### Verify

Run inside the Boot project, then set your package to DEBUG and verify a debug message appears.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 29 — Reflection/Proxies + Spring AOP ⬜

**Planned date:** 2026-10-08  
**Java track:** Dynamic proxies and method interception concept  
**Spring track:** AOP, @Aspect, cross-cutting concerns  
**roadmap.sh mapping:** Java — Object Oriented Programming → Annotations / Dynamic Binding; Spring Boot — Introduction → Spring AOP

### Tutorial

AOP separates cross-cutting concerns such as timing, logging, or authorization from business logic. Spring commonly implements advice through proxies, so understanding proxy boundaries explains many `@Transactional` and `@Async` surprises.

### Runnable mini-lab

```java
import java.lang.reflect.*;

interface Ping { String ping(); }
public class Day29 {
    public static void main(String[] args) {
        Ping p = (Ping) Proxy.newProxyInstance(Day29.class.getClassLoader(), new Class[]{Ping.class},
            (proxy, method, a) -> "intercepted");
        System.out.println(p.ping());
    }
}
```

### Verify

Run it, then write a Spring `@Aspect` that times one service method.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 30 — JVM + Spring Boot Startup Checkpoint ⬜

**Planned date:** 2026-10-09  
**Java track:** JVM process, heap/stack/class loading overview  
**Spring track:** SpringApplication startup, ApplicationContext, autoconfiguration report, Actuator intro  
**roadmap.sh mapping:** Java — Lifecycle of a Program / Java Memory Model (preview); Spring Boot — Spring Boot / Autoconfiguration / Actuators / Embedded Server

### Tutorial

Tie Month 1 together by following the process from `main` to a ready HTTP server. At this point you should be able to explain which objects the JVM creates, which objects Spring manages, and why Boot chooses particular autoconfigurations.

### Runnable mini-lab

```java
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class Day30Config {
    @Bean CommandLineRunner names(org.springframework.context.ApplicationContext ctx) {
        return args -> System.out.println("Bean count=" + ctx.getBeanDefinitionCount());
    }
}
```

### Verify

Run with `--debug`, inspect the condition evaluation report, then call `/actuator/health` after adding the actuator starter.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 31 — JDBC + DataSource ⬜

**Planned date:** 2026-10-10  
**Java track:** JDBC connection/statement/result-set lifecycle  
**Spring track:** Spring DataSource and JdbcTemplate  
**roadmap.sh mapping:** Java — Database Access → JDBC; Spring Boot — Spring Data JDBC / Spring Data

### Tutorial

JDBC is the foundation beneath higher-level persistence frameworks. Learn it first so connection management, prepared statements, transactions, and result mapping are not hidden magic.

### Runnable mini-lab

```java
import java.sql.*;

public class Day31 {
    public static void main(String[] args) throws Exception {
        try (Connection c = DriverManager.getConnection("jdbc:h2:mem:test");
             Statement s = c.createStatement()) {
            s.execute("create table property(id int primary key, title varchar(100))");
            s.execute("insert into property values(1,'Batel')");
            try (ResultSet rs = s.executeQuery("select * from property")) { while (rs.next()) System.out.println(rs.getString("title")); }
        }
    }
}
```

### Verify

Add H2 as a test/runtime dependency, run it, then reproduce the query with `JdbcTemplate`.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 32 — Prepared Statements + Spring Data JDBC ⬜

**Planned date:** 2026-10-12  
**Java track:** PreparedStatement, parameter binding, transaction boundaries  
**Spring track:** Spring Data JDBC repositories  
**roadmap.sh mapping:** Java — Database Access → JDBC; Spring Boot — Spring Data JDBC

### Tutorial

Prepared statements separate SQL structure from data and reduce injection risk. Spring Data JDBC offers repository convenience while keeping a simpler aggregate model than JPA.

### Runnable mini-lab

```java
// JDBC core idea:
try (var ps = connection.prepareStatement("select title from property where id = ?")) {
    ps.setLong(1, 1L);
    try (var rs = ps.executeQuery()) {
        if (rs.next()) System.out.println(rs.getString(1));
    }
}
```

### Verify

Run against your H2 setup, then define a Spring Data JDBC `CrudRepository<Property, Long>` and save/find one record.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 33 — JPA/Hibernate Entity Basics ⬜

**Planned date:** 2026-10-13  
**Java track:** POJO identity and object mapping concepts  
**Spring track:** @Entity, @Id, repository basics  
**roadmap.sh mapping:** Java — Database Access → Hibernate / Spring Data JPA; Spring Boot — Hibernate / Spring Data JPA

### Tutorial

JPA defines the persistence API; Hibernate is the common implementation. Entities are managed objects with identity and lifecycle rules, so they should not be treated exactly like ordinary DTOs.

### Runnable mini-lab

```java
import jakarta.persistence.*;

@Entity
class PropertyEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    protected PropertyEntity() {}
    PropertyEntity(String title) { this.title = title; }
}
```

### Verify

Add JPA + H2, create a `JpaRepository`, save one entity in a `CommandLineRunner`, and print its generated id.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 34 — Object Lifecycle + JPA Entity Lifecycle ⬜

**Planned date:** 2026-10-14  
**Java track:** Object reachability and lifecycle review  
**Spring track:** Transient, managed, detached, removed; persistence context  
**roadmap.sh mapping:** Java — Object Oriented Programming → Object Lifecycle; Spring Boot — Entity Lifecycle / Hibernate

### Tutorial

The persistence context is an identity map and change-tracking boundary. Understanding managed vs detached state explains dirty checking, lazy loading, and why changes may persist without an explicit update call.

### Runnable mini-lab

```java
@jakarta.transaction.Transactional
void rename(Long id) {
    var p = repository.findById(id).orElseThrow();
    p.setTitle("Updated"); // managed entity; dirty checking persists this
}
```

### Verify

Create the method, call it, then query the database and verify the title changed without calling `save` inside the method.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 35 — Collections + JPA Relationships ⬜

**Planned date:** 2026-10-15  
**Java track:** Set/List semantics in aggregates  
**Spring track:** @OneToMany, @ManyToOne, ownership  
**roadmap.sh mapping:** Java — Collections; Spring Boot — Relationships / Spring Data JPA

### Tutorial

Relationship mapping is not just annotation syntax: ownership determines which side writes the foreign key. Choose `List` vs `Set` based on domain semantics and understand the SQL generated by each operation.

### Runnable mini-lab

```java
@jakarta.persistence.Entity
class Building {
    @jakarta.persistence.Id @jakarta.persistence.GeneratedValue Long id;
    @jakarta.persistence.OneToMany(mappedBy = "building", cascade = jakarta.persistence.CascadeType.ALL)
    java.util.List<Unit> units = new java.util.ArrayList<>();
}
@jakarta.persistence.Entity
class Unit {
    @jakarta.persistence.Id @jakarta.persistence.GeneratedValue Long id;
    @jakarta.persistence.ManyToOne Building building;
}
```

### Verify

Persist one building with two units and inspect the generated SQL and foreign keys.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 36 — Entity Equality + Lazy/Eager and N+1 ⬜

**Planned date:** 2026-10-16  
**Java track:** equals/hashCode rules revisited for persisted identity  
**Spring track:** Fetch types, lazy proxies, N+1 query problem  
**roadmap.sh mapping:** Java — Object Oriented Programming / Collections; Spring Boot — Hibernate / Relationships

### Tutorial

JPA entities complicate equality because generated IDs do not exist before persistence. Lazy associations can reduce unnecessary data but also cause N+1 queries when iterated without the right fetch strategy.

### Runnable mini-lab

```java
// Enable SQL logging, then execute:
var buildings = buildingRepository.findAll();
buildings.forEach(b -> System.out.println(b.getUnits().size()));
// Count the SQL statements before and after using a fetch join/entity graph.
```

### Verify

Run with Hibernate SQL logging enabled and compare query counts before/after a fetch join.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 37 — Generic Repository Patterns + Spring Data ⬜

**Planned date:** 2026-10-17  
**Java track:** Generic interfaces and type bounds revisited  
**Spring track:** CrudRepository/JpaRepository generics and derived queries  
**roadmap.sh mapping:** Java — Collections → Generic Collections; Spring Boot — Spring Data / Spring Data JPA

### Tutorial

Spring Data repository types are a practical example of Java generics at framework scale. Type parameters communicate entity/id types and allow the framework to generate implementations safely.

### Runnable mini-lab

```java
interface Named { String name(); }
interface Repository<T extends Named, ID> { java.util.Optional<T> findById(ID id); }

record City(String name) implements Named {}
```

### Verify

Compile the generic example, then inspect your `JpaRepository<PropertyEntity, Long>` methods and add one derived query.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 38 — Ordering + Pagination/Sorting ⬜

**Planned date:** 2026-10-19  
**Java track:** Comparator review and stable ordering  
**Spring track:** Pageable, Page, Sort  
**roadmap.sh mapping:** Java — Collections; Spring Boot — Spring Data JPA

### Tutorial

Pagination is only reliable with deterministic ordering. Spring Data models page requests separately from repository queries, making limit/offset and sort intent explicit.

### Runnable mini-lab

```java
// Spring Data example:
var page = repository.findAll(org.springframework.data.domain.PageRequest.of(0, 20,
    org.springframework.data.domain.Sort.by("title").ascending()));
System.out.println(page.getTotalElements());
```

### Verify

Seed >20 rows, request page 0 and page 1, and verify stable ordering.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 39 — Transactions: Propagation and Rollback ⬜

**Planned date:** 2026-10-20  
**Java track:** Exception propagation review  
**Spring track:** @Transactional propagation and rollback rules  
**roadmap.sh mapping:** Java — Exception Handling; Spring Boot — Transactions

### Tutorial

A transaction is a unit of work with atomicity and isolation guarantees. Spring applies transaction behavior through proxies; unchecked exceptions roll back by default, and propagation determines how nested service calls share or create transactions.

### Runnable mini-lab

```java
@org.springframework.transaction.annotation.Transactional
public void createThenFail() {
    repository.save(new PropertyEntity("temporary"));
    throw new IllegalStateException("force rollback");
}
```

### Verify

Run the method, catch the exception outside the service, and verify the inserted row does not exist.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 40 — Concurrency Fundamentals + Isolation/Locking ⬜

**Planned date:** 2026-10-21  
**Java track:** Thread states, race conditions, Executor basics  
**Spring track:** Transaction isolation, optimistic/pessimistic locking  
**roadmap.sh mapping:** Java — Concurrency → Threads; Spring Boot — Transactions / Spring Data JPA

### Tutorial

Concurrency bugs and database concurrency bugs are different layers of the same coordination problem. Learn thread-level races first, then see how isolation and locking protect rows across requests/processes.

### Runnable mini-lab

```java
public class Day40 {
    private static int counter = 0;
    public static void main(String[] args) throws Exception {
        Thread a = new Thread(() -> { for (int i=0;i<100_000;i++) counter++; });
        Thread b = new Thread(() -> { for (int i=0;i<100_000;i++) counter++; });
        a.start(); b.start(); a.join(); b.join();
        System.out.println(counter);
    }
}
```

### Verify

Run it several times and observe lost updates; then add a JPA `@Version` field and reproduce an optimistic-lock conflict.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 41 — ExecutorService + @Async ⬜

**Planned date:** 2026-10-22  
**Java track:** Executors, task submission, lifecycle  
**Spring track:** Spring @Async and TaskExecutor  
**roadmap.sh mapping:** Java — Concurrency → Threads; Spring Boot — Introduction → Task Execution (terminology) / Spring Boot

### Tutorial

Executors separate task submission from thread management. Spring `@Async` uses an executor behind a proxy, so method visibility and self-invocation rules matter.

### Runnable mini-lab

```java
import java.util.concurrent.*;
public class Day41 {
    public static void main(String[] args) throws Exception {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            System.out.println(executor.submit(() -> "done").get());
        }
    }
}
```

### Verify

Run it; then enable `@EnableAsync`, annotate a service method with `@Async`, and print the executing thread name.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 42 — Synchronization, Locks and Atomics + Stateless Beans ⬜

**Planned date:** 2026-10-23  
**Java track:** synchronized, Lock, AtomicInteger  
**Spring track:** Singleton services under concurrent requests  
**roadmap.sh mapping:** Java — Concurrency → Threads; Spring Boot — Introduction → Spring Bean Scope

### Tutorial

Use the smallest synchronization primitive that matches the problem. In Spring, avoiding shared mutable state is usually better than adding locks to singleton services.

### Runnable mini-lab

```java
import java.util.concurrent.atomic.AtomicInteger;
public class Day42 {
    public static void main(String[] args) throws Exception {
        var c = new AtomicInteger();
        Thread a = new Thread(() -> { for(int i=0;i<100_000;i++) c.incrementAndGet(); });
        Thread b = new Thread(() -> { for(int i=0;i<100_000;i++) c.incrementAndGet(); });
        a.start(); b.start(); a.join(); b.join();
        System.out.println(c.get());
    }
}
```

### Verify

Run it and compare with Day 40; explain why stateless services avoid this class of bug.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 43 — volatile and the Java Memory Model ⬜

**Planned date:** 2026-10-24  
**Java track:** Visibility, happens-before, volatile vs atomicity  
**Spring track:** Memory visibility in singleton beans  
**roadmap.sh mapping:** Java — Concurrency → volatile keyword / Java Memory Model; Spring Boot — Introduction → Spring Bean Scope

### Tutorial

`volatile` provides visibility and ordering guarantees, not compound-operation atomicity. The Java Memory Model explains when writes by one thread are guaranteed visible to another.

### Runnable mini-lab

```java
public class Day43 {
    static volatile boolean running = true;
    public static void main(String[] args) throws Exception {
        Thread t = new Thread(() -> { while (running) { Thread.onSpinWait(); } System.out.println("stopped"); });
        t.start(); Thread.sleep(100); running = false; t.join();
    }
}
```

### Verify

Run it, then explain why `volatile int counter; counter++` is still not thread-safe.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 44 — CompletableFuture + Async Composition ⬜

**Planned date:** 2026-10-26  
**Java track:** CompletableFuture pipelines and exception handling  
**Spring track:** Compose independent service calls asynchronously  
**roadmap.sh mapping:** Java — Concurrency → Threads / Functional Programming; Spring Boot — Microservices

### Tutorial

CompletableFuture represents a value that may arrive later and supports functional composition. Use it when parallelism reduces latency and you can define timeout/error behavior clearly.

### Runnable mini-lab

```java
import java.util.concurrent.*;
public class Day44 {
    public static void main(String[] args) {
        var a = CompletableFuture.supplyAsync(() -> 20);
        var b = CompletableFuture.supplyAsync(() -> 22);
        System.out.println(a.thenCombine(b, Integer::sum).join());
    }
}
```

### Verify

Run it, then combine two fake Spring service calls and add `orTimeout` plus exception handling.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 45 — Virtual Threads + Spring Boot ⬜

**Planned date:** 2026-10-27  
**Java track:** Virtual thread model and blocking I/O tradeoffs  
**Spring track:** Spring Boot virtual-thread support  
**roadmap.sh mapping:** Java — Concurrency → Virtual Threads; Spring Boot — Spring Boot

### Tutorial

Virtual threads make thread-per-request style scalable for blocking workloads by making threads cheap. They do not make CPU-bound work faster and do not remove database connection-pool limits.

### Runnable mini-lab

```java
import java.util.concurrent.*;
public class Day45 {
    public static void main(String[] args) throws Exception {
        try (var ex = Executors.newVirtualThreadPerTaskExecutor()) {
            var f = ex.submit(() -> { Thread.sleep(100); return Thread.currentThread(); });
            System.out.println(f.get().isVirtual());
        }
    }
}
```

### Verify

Run on Java 21+, then enable Boot virtual threads and inspect request thread names.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 46 — Scheduling ⬜

**Planned date:** 2026-10-28  
**Java track:** ScheduledExecutorService  
**Spring track:** Spring @Scheduled  
**roadmap.sh mapping:** Java — Concurrency → Threads; Spring Boot — Introduction → Task Execution (terminology)

### Tutorial

Scheduling is about when a task runs; async execution is about where it runs. Spring `@Scheduled` is convenient, but production jobs also need idempotency, observability, and multi-instance coordination.

### Runnable mini-lab

```java
import java.util.concurrent.*;
public class Day46 {
    public static void main(String[] args) throws Exception {
        var ex = Executors.newSingleThreadScheduledExecutor();
        ex.schedule(() -> System.out.println("ran"), 200, TimeUnit.MILLISECONDS);
        ex.shutdown(); ex.awaitTermination(1, TimeUnit.SECONDS);
    }
}
```

### Verify

Run it; then create a Spring `@Scheduled(fixedDelay = 5000)` method and verify repeated execution.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 47 — JUnit Fundamentals ⬜

**Planned date:** 2026-10-29  
**Java track:** Assertions, lifecycle, parameterized tests  
**Spring track:** Unit-test Spring services without loading a context  
**roadmap.sh mapping:** Java — Testing → Unit Testing → JUnit; Spring Boot — Testing

### Tutorial

Unit tests should isolate behavior and run fast. A Spring service is still a Java class, so most service logic can be tested with plain JUnit without `@SpringBootTest`.

### Runnable mini-lab

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
class PriceCalculatorTest {
    @Test void addsTenPercent() { assertEquals(110.0, 100.0 * 1.10, 0.001); }
}
```

### Verify

Run `./mvnw test`; then write a unit test for one of your services with no Spring annotations.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 48 — Mockito ⬜

**Planned date:** 2026-10-30  
**Java track:** Mocks, stubs, verification, test doubles  
**Spring track:** Mock repository dependencies in service tests  
**roadmap.sh mapping:** Java — Testing → Mocking → Mockito; Spring Boot — Testing

### Tutorial

Mockito is useful at boundaries, but over-mocking implementation details makes tests brittle. Mock collaborators, drive behavior through the public API, and verify outcomes first.

### Runnable mini-lab

```java
@org.junit.jupiter.api.Test
void findsProperty() {
    var repo = org.mockito.Mockito.mock(PropertyRepository.class);
    org.mockito.Mockito.when(repo.findById(1L)).thenReturn(java.util.Optional.of(new Property("Batel")));
    var service = new PropertyService(repo);
    org.junit.jupiter.api.Assertions.assertEquals("Batel", service.find(1L).title());
}
```

### Verify

Adapt class names to your project and run the test; add one not-found test.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 49 — @SpringBootTest and Integration Boundaries ⬜

**Planned date:** 2026-10-31  
**Java track:** Integration-test principles  
**Spring track:** @SpringBootTest, context loading  
**roadmap.sh mapping:** Java — Testing → Integration Testing; Spring Boot — Testing → @SpringBootTest Annotation

### Tutorial

`@SpringBootTest` loads a broad application context and is useful when configuration/integration matters. It is slower than unit tests, so use it deliberately instead of by default.

### Runnable mini-lab

```java
@org.springframework.boot.test.context.SpringBootTest
class ContextTest {
    @org.junit.jupiter.api.Test
    void contextLoads() {}
}
```

### Verify

Run it, then intentionally break one bean dependency and observe how context startup catches the wiring error.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 50 — Spring MVC Testing ⬜

**Planned date:** 2026-11-02  
**Java track:** HTTP contract assertions  
**Spring track:** @WebMvcTest and MockMvc  
**roadmap.sh mapping:** Java — Testing → Integration Testing; Spring Boot — Testing → Mock MVC

### Tutorial

MVC slice tests load the web layer without the whole system. They are good for routes, JSON shape, status codes, validation, and exception-handler behavior.

### Runnable mini-lab

```java
@org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest(PropertyController.class)
class PropertyControllerTest {
    @org.springframework.beans.factory.annotation.Autowired org.springframework.test.web.servlet.MockMvc mvc;
    @org.junit.jupiter.api.Test void health() throws Exception {
        mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/properties"))
           .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isOk());
    }
}
```

### Verify

Adapt dependencies with mocks and run the test.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 51 — JPA Tests + Testcontainers ⬜

**Planned date:** 2026-11-03  
**Java track:** Database test isolation  
**Spring track:** @DataJpaTest; real database with Testcontainers  
**roadmap.sh mapping:** Java — Testing → Integration Testing; Spring Boot — Testing → JPA Test

### Tutorial

Repository tests should verify mappings and SQL behavior, not only mock repository methods. `@DataJpaTest` gives a focused persistence slice; Testcontainers can replace an in-memory database with the same engine used in production.

### Runnable mini-lab

```java
@org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
class PropertyRepositoryTest {
    @org.springframework.beans.factory.annotation.Autowired PropertyRepository repo;
    @org.junit.jupiter.api.Test void saves() {
        var saved = repo.save(new PropertyEntity("Centro"));
        org.junit.jupiter.api.Assertions.assertNotNull(saved.getId());
    }
}
```

### Verify

Run with H2 first, then add a PostgreSQL Testcontainer later and compare behavior.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 52 — REST Integration Testing ⬜

**Planned date:** 2026-11-04  
**Java track:** End-to-end test thinking; recognize REST Assured/JMeter/Cucumber roles  
**Spring track:** Run a real Boot server and exercise HTTP  
**roadmap.sh mapping:** Java — Testing → REST Assured / JMeter / Cucumber-JVM; Spring Boot — Testing

### Tutorial

End-to-end API tests cross more boundaries and therefore cost more to run and debug. Keep a smaller number focused on critical flows, while unit/slice tests cover most branches.

### Runnable mini-lab

```java
// With the app running:
// curl -i http://localhost:8080/properties
// curl -i -X POST http://localhost:8080/properties \
//   -H 'Content-Type: application/json' \
//   -d '{"title":"Batel","price":3500}'
public class Day52 { public static void main(String[] a){ System.out.println("Run HTTP smoke tests above"); } }
```

### Verify

Automate the same flow using REST Assured or your preferred integration-test client.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 53 — Cryptography Basics + Password Hashing ⬜

**Planned date:** 2026-11-05  
**Java track:** Hashing vs encryption, salts, secure random  
**Spring track:** Spring Security introduction and PasswordEncoder  
**roadmap.sh mapping:** Java — Cryptography; Spring Boot — Spring Security

### Tutorial

Passwords should be verified through slow password hashes, not reversible encryption. Spring Security provides `PasswordEncoder` abstractions so application code does not implement cryptography itself.

### Runnable mini-lab

```java
import java.security.*;
import java.util.HexFormat;
public class Day53 {
    public static void main(String[] args) throws Exception {
        byte[] digest = MessageDigest.getInstance("SHA-256").digest("demo".getBytes());
        System.out.println(HexFormat.of().formatHex(digest));
    }
}
```

### Verify

Run it to understand hashing, then use Spring `BCryptPasswordEncoder` and compare two hashes of the same password.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 54 — Authentication + SecurityFilterChain ⬜

**Planned date:** 2026-11-06  
**Java track:** Identity/authentication concepts  
**Spring track:** Spring Security filter chain and authentication  
**roadmap.sh mapping:** Java — Cryptography / Web Frameworks; Spring Boot — Spring Security → Authentication

### Tutorial

Authentication answers “who are you?” Spring Security processes requests through a filter chain before controller code, so understanding filter order and authentication objects is more useful than memorizing annotations.

### Runnable mini-lab

```java
@org.springframework.context.annotation.Bean
org.springframework.security.web.SecurityFilterChain security(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
    return http.authorizeHttpRequests(a -> a.requestMatchers("/public/**").permitAll().anyRequest().authenticated())
        .httpBasic(org.springframework.security.config.Customizer.withDefaults()).build();
}
```

### Verify

Start the app and compare responses to `/public/x` and a protected endpoint with and without credentials.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 55 — Authorization + Method Security ⬜

**Planned date:** 2026-11-07  
**Java track:** Roles vs permissions; least privilege  
**Spring track:** @PreAuthorize and authorization rules  
**roadmap.sh mapping:** Java — Object Oriented Programming → Interfaces (policy abstraction); Spring Boot — Spring Security → Authorization

### Tutorial

Authorization answers “what may this identity do?” Keep coarse route rules in the filter chain and use method-level rules when domain/service authorization needs a stronger boundary.

### Runnable mini-lab

```java
@org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
public void deleteProperty(long id) {
    repository.deleteById(id);
}
```

### Verify

Enable method security, call as ADMIN and USER, and verify only ADMIN succeeds.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 56 — JWT Fundamentals ⬜

**Planned date:** 2026-11-09  
**Java track:** Base64URL, signatures, claims, expiration  
**Spring track:** JWT resource-server authentication  
**roadmap.sh mapping:** Java — Cryptography; Spring Boot — Spring Security → JWT Authentication

### Tutorial

A JWT is a signed token, not an encrypted secret by default. Verify issuer, audience, expiry, and signature; never trust claims before validation.

### Runnable mini-lab

```java
import java.util.Base64;
public class Day56 {
    public static void main(String[] args) {
        String header = Base64.getUrlEncoder().withoutPadding().encodeToString("{\"alg\":\"none\"}".getBytes());
        System.out.println(header);
    }
}
```

### Verify

Run to see Base64URL encoding, then configure a Spring resource server with a real signing key/issuer in your lab.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 57 — OAuth2 / OIDC ⬜

**Planned date:** 2026-11-10  
**Java track:** Protocol roles: resource owner/client/resource server/authorization server  
**Spring track:** OAuth2 resource server / OIDC login  
**roadmap.sh mapping:** Java — Networking / Cryptography; Spring Boot — Spring Security → OAuth2

### Tutorial

OAuth2 delegates authorization; OpenID Connect adds identity on top. Senior interviews often test the flow and trust boundaries more than configuration syntax.

### Runnable mini-lab

```java
public class Day57 {
    public static void main(String[] args) {
        System.out.println("Client -> Authorization Server -> access token -> Resource Server");
    }
}
```

### Verify

Draw the authorization-code flow, then configure a local/provider-backed resource server and inspect authenticated claims.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 58 — CSRF, CORS, Sessions vs Tokens ⬜

**Planned date:** 2026-11-11  
**Java track:** HTTP/browser security boundaries  
**Spring track:** Spring Security CORS/CSRF configuration  
**roadmap.sh mapping:** Java — Networking; Spring Boot — Spring Security

### Tutorial

CORS is a browser-origin policy; CSRF is an attack that abuses ambient credentials. Whether CSRF protection is needed depends on how authentication credentials are sent, not on whether the frontend is React.

### Runnable mini-lab

```java
@org.springframework.context.annotation.Bean
org.springframework.web.cors.CorsConfigurationSource cors() {
    var c = new org.springframework.web.cors.CorsConfiguration();
    c.setAllowedOrigins(java.util.List.of("http://localhost:5173"));
    c.setAllowedMethods(java.util.List.of("GET","POST"));
    var s = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
    s.registerCorsConfiguration("/**", c);
    return s;
}
```

### Verify

Run React/local curl requests from allowed and disallowed origins and inspect response headers.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 59 — Security Tests + Proxy Boundaries ⬜

**Planned date:** 2026-11-12  
**Java track:** Annotations/proxies review  
**Spring track:** Test authenticated/authorized MVC methods  
**roadmap.sh mapping:** Java — Annotations / Dynamic Binding; Spring Boot — Testing / Spring Security

### Tutorial

Security is behavior and should be tested like any other contract. Verify allowed and denied cases, not only happy-path authentication.

### Runnable mini-lab

```java
@org.springframework.security.test.context.support.WithMockUser(roles = "USER")
@org.junit.jupiter.api.Test
void userCannotDelete() throws Exception {
    mvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/properties/1"))
       .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers.status().isForbidden());
}
```

### Verify

Run alongside an ADMIN variant that expects success.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 60 — Midpoint Review + Code Review ⬜

**Planned date:** 2026-11-13  
**Java track:** Review Java Core, functional Java, concurrency, testing  
**Spring track:** Review configuration, MVC, data, transactions, security  
**roadmap.sh mapping:** Java — Multiple roadmap sections; Spring Boot — Multiple roadmap sections

### Tutorial

This is a retrieval day, not a lecture day. Explain concepts without notes, solve one coding problem, and review one intentionally flawed Spring service/controller/repository stack.

### Runnable mini-lab

```java
public class Day60 {
    public static void main(String[] args) {
        System.out.println("Checkpoint: explain, code, review — no new API today");
    }
}
```

### Verify

Do a 45-minute mock interview + 30-minute coding problem + 15-minute code review.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 61 — Microservices Fundamentals + Module Boundaries ⬜

**Planned date:** 2026-11-14  
**Java track:** Packages/modules, cohesion and coupling  
**Spring track:** Microservice responsibilities and tradeoffs  
**roadmap.sh mapping:** Java — Modules / Object Oriented Programming; Spring Boot — Microservices

### Tutorial

A microservice is an independently deployable boundary, not simply a small Spring application. Start from business capabilities and data ownership; distributed systems add latency, partial failure, and operational cost.

### Runnable mini-lab

```java
public class Day61 {
    public static void main(String[] args) {
        System.out.println("Property Catalog owns property metadata; Search consumes its API/events");
    }
}
```

### Verify

Sketch two RentMap service boundaries and state which service owns each table/event.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 62 — HTTP, DNS, Timeouts + OpenFeign/RestClient ⬜

**Planned date:** 2026-11-16  
**Java track:** Networking failure modes and timeouts  
**Spring track:** Declarative/service-to-service HTTP client  
**roadmap.sh mapping:** Java — Networking; Spring Boot — Spring Cloud Open Feign / Microservices

### Tutorial

Remote calls can timeout, fail, retry, or return partial data. Explicit connect/read timeouts and clear error translation are prerequisites before adding retries.

### Runnable mini-lab

```java
@org.springframework.web.service.annotation.HttpExchange("/properties")
interface PropertyClient {
    @org.springframework.web.service.annotation.GetExchange("/{id}")
    PropertyResponse find(@org.springframework.web.bind.annotation.PathVariable long id);
}
```

### Verify

Create a local second endpoint/service, call it through a Spring HTTP interface or RestClient, then stop the target and observe failure.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 63 — Service Discovery ⬜

**Planned date:** 2026-11-17  
**Java track:** DNS/service naming concepts  
**Spring track:** Eureka/service discovery concepts  
**roadmap.sh mapping:** Java — Networking; Spring Boot — Spring Cloud → Eureka

### Tutorial

Service discovery maps a logical service name to healthy instances. In Kubernetes this is often provided by cluster DNS; Eureka is a Spring Cloud approach worth understanding for existing systems.

### Runnable mini-lab

```java
public class Day63 {
    public static void main(String[] args) {
        System.out.println(java.net.InetAddress.getLoopbackAddress().getHostName());
    }
}
```

### Verify

Run the Java lookup, then create a small Eureka lab or document how Kubernetes Service DNS would solve the same problem.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 64 — Centralized Configuration ⬜

**Planned date:** 2026-11-18  
**Java track:** Configuration precedence and environment variables  
**Spring track:** Spring Cloud Config  
**roadmap.sh mapping:** Java — Build Tools / Modules (configuration delivery); Spring Boot — Spring Cloud → Cloud Config

### Tutorial

Centralized configuration decouples deployable artifacts from environment-specific settings. You still need versioning, secure secret handling, and a strategy for config-server failure.

### Runnable mini-lab

```java
public class Day64 {
    public static void main(String[] args) {
        System.out.println(System.getenv().getOrDefault("RENTMAP_CITY", "Curitiba"));
    }
}
```

### Verify

Run with and without `RENTMAP_CITY`, then externalize one Spring property and document precedence.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 65 — API Gateway ⬜

**Planned date:** 2026-11-19  
**Java track:** Request/response abstractions  
**Spring track:** Spring Cloud Gateway routes and filters  
**roadmap.sh mapping:** Java — Networking; Spring Boot — Spring Cloud → Spring Cloud Gateway

### Tutorial

A gateway centralizes edge routing and cross-cutting policies, but it should not become a second business-logic layer. Use filters for concerns such as correlation IDs, rate limits, and authentication handoff.

### Runnable mini-lab

```java
// application.yml concept:
// spring.cloud.gateway.routes[0].id=properties
// spring.cloud.gateway.routes[0].uri=http://localhost:8081
// spring.cloud.gateway.routes[0].predicates[0]=Path=/properties/**
public class Day65 { public static void main(String[] a){ System.out.println("Start gateway + property service, then curl /properties"); } }
```

### Verify

Run two local apps and verify the gateway forwards a request to the property service.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 66 — Retries and Circuit Breakers ⬜

**Planned date:** 2026-11-20  
**Java track:** Exception classification and retry-safe operations  
**Spring track:** Spring Cloud Circuit Breaker / Resilience4j  
**roadmap.sh mapping:** Java — Exception Handling; Spring Boot — Spring Cloud → Spring Cloud Circuit Breaker

### Tutorial

Retries help transient failures only when operations are safe to repeat. A circuit breaker stops repeated calls to an unhealthy dependency and gives it time to recover.

### Runnable mini-lab

```java
public class Day66 {
    static int attempts;
    static String flaky() { if (++attempts < 3) throw new RuntimeException("temporary"); return "ok"; }
    public static void main(String[] args) {
        for (int i=0;i<3;i++) try { System.out.println(flaky()); break; } catch (RuntimeException e) { System.out.println(e.getMessage()); }
    }
}
```

### Verify

Run it, then replace the manual loop with a Resilience4j retry/circuit-breaker around a local failing endpoint.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 67 — Timeouts, Bulkheads and Rate Limits ⬜

**Planned date:** 2026-11-21  
**Java track:** Semaphores and bounded resources  
**Spring track:** Resilience patterns around remote calls  
**roadmap.sh mapping:** Java — Concurrency → Threads; Spring Boot — Spring Cloud Circuit Breaker / Microservices

### Tutorial

Bulkheads limit how much of your system one failing dependency can consume. Timeouts bound latency; rate limits bound load. These controls are often more important than retries.

### Runnable mini-lab

```java
import java.util.concurrent.Semaphore;
public class Day67 {
    public static void main(String[] args) {
        var bulkhead = new Semaphore(2);
        System.out.println(bulkhead.tryAcquire());
        System.out.println(bulkhead.tryAcquire());
        System.out.println(bulkhead.tryAcquire());
    }
}
```

### Verify

Run it and explain the third result; configure a small bulkhead/rate-limit lab around a Spring endpoint.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 68 — Application Events ⬜

**Planned date:** 2026-11-23  
**Java track:** Observer-style decoupling and event objects  
**Spring track:** ApplicationEventPublisher and @EventListener  
**roadmap.sh mapping:** Java — Object Oriented Programming → Interfaces; Spring Boot — Introduction → Terminology (event model)

### Tutorial

In-process events decouple producers from listeners but still share one process and deployment. They are useful for side effects that do not belong in the main business method.

### Runnable mini-lab

```java
record PropertyCreated(long id) {}

@org.springframework.context.event.EventListener
public void on(PropertyCreated event) {
    System.out.println("Created " + event.id());
}
```

### Verify

Publish the event from a service and verify a listener runs; then discuss transaction timing.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 69 — Kafka Fundamentals ⬜

**Planned date:** 2026-11-24  
**Java track:** Serialization, keys, partitions, ordering  
**Spring track:** Event-driven microservice integration  
**roadmap.sh mapping:** Java — Networking / Functional Programming (applied); Spring Boot — Microservices (ecosystem extension)

### Tutorial

Kafka partitions provide ordered logs and scalable consumption. Keys influence partition selection, so choose them based on the ordering boundary you need.

### Runnable mini-lab

```java
record PropertyEvent(long id, String type) {}
public class Day69 {
    public static void main(String[] args) {
        var event = new PropertyEvent(42L, "CREATED");
        System.out.println(event);
    }
}
```

### Verify

Run a local Kafka/Redpanda broker, publish one keyed event, and consume it from a Spring listener.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 70 — Idempotency, Retries and DLQ ⬜

**Planned date:** 2026-11-25  
**Java track:** Set/map deduplication patterns applied  
**Spring track:** Idempotent consumers, retry topics, dead-letter handling  
**roadmap.sh mapping:** Java — Collections → Set / Map; Spring Boot — Microservices (ecosystem extension)

### Tutorial

At-least-once delivery means duplicates are normal. Consumers should make repeated processing safe using stable event IDs and a persistence-backed idempotency strategy.

### Runnable mini-lab

```java
import java.util.*;
public class Day70 {
    public static void main(String[] args) {
        Set<String> processed = new HashSet<>();
        for (String id : List.of("e1","e1","e2")) if (processed.add(id)) System.out.println("process " + id);
    }
}
```

### Verify

Run it, then explain why an in-memory set is insufficient across restarts/multiple instances and design a DB-backed equivalent.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 71 — Caching ⬜

**Planned date:** 2026-11-26  
**Java track:** HashMap cache semantics, eviction tradeoffs  
**Spring track:** Spring Cache / @Cacheable  
**roadmap.sh mapping:** Java — Collections → Map; Spring Boot — Spring Boot ecosystem / Microservices

### Tutorial

Caching trades freshness and memory for lower latency/load. Define key, TTL/eviction, invalidation, and failure behavior before adding `@Cacheable`.

### Runnable mini-lab

```java
@org.springframework.cache.annotation.Cacheable("properties")
public PropertyResponse find(long id) {
    System.out.println("database hit");
    return loadFromDatabase(id);
}
```

### Verify

Enable caching, call the method twice with the same id, and verify the expensive path runs once.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 72 — Redis and Distributed Cache ⬜

**Planned date:** 2026-11-27  
**Java track:** Serialization/key design  
**Spring track:** External cache for multi-instance applications  
**roadmap.sh mapping:** Java — Database Access (NoSQL adjacent); Spring Boot — Microservices / related Redis roadmap

### Tutorial

An external cache is shared across application instances, unlike an in-process map. Treat Redis as a network dependency with serialization, TTL, and outage considerations.

### Runnable mini-lab

```java
public class Day72 {
    public static void main(String[] args) {
        String key = "property:%d".formatted(42L);
        System.out.println(key);
    }
}
```

### Verify

Run Redis locally, write/read one property through Spring Data Redis or `RedisTemplate`, then set a TTL.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 73 — Metrics with Micrometer ⬜

**Planned date:** 2026-11-28  
**Java track:** Counters/timers as quantitative feedback  
**Spring track:** Micrometer meters and Actuator metrics  
**roadmap.sh mapping:** Java — Logging Frameworks / Java runtime observation; Spring Boot — Spring Cloud → Micrometer / Actuators

### Tutorial

Logs explain individual events; metrics summarize system behavior over time. Micrometer is Spring’s instrumentation facade and lets you publish counters, timers, gauges, and distribution summaries.

### Runnable mini-lab

```java
@org.springframework.stereotype.Component
class SearchMetrics {
    private final io.micrometer.core.instrument.Counter counter;
    SearchMetrics(io.micrometer.core.instrument.MeterRegistry registry) {
        counter = registry.counter("rentmap.searches");
    }
    void searched() { counter.increment(); }
}
```

### Verify

Call `searched()`, then query `/actuator/metrics/rentmap.searches`.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 74 — Tracing, Correlation IDs and Production Actuator ⬜

**Planned date:** 2026-11-30  
**Java track:** Thread/context propagation concepts  
**Spring track:** Actuator health/info, Micrometer tracing concepts  
**roadmap.sh mapping:** Java — Concurrency / Logging Frameworks; Spring Boot — Actuators / Micrometer

### Tutorial

Tracing connects work across services using trace/span identifiers. Correlation IDs in logs are useful, but distributed tracing provides structured causal relationships across remote calls.

### Runnable mini-lab

```java
public class Day74 {
    public static void main(String[] args) {
        String correlationId = java.util.UUID.randomUUID().toString();
        System.out.println("correlationId=" + correlationId);
    }
}
```

### Verify

Run it, then add a request filter that puts a correlation ID into MDC and verify it appears in logs.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 75 — API Design: Status, Errors, Versioning ⬜

**Planned date:** 2026-12-01  
**Java track:** Enums/records for explicit API contracts  
**Spring track:** Spring MVC response status and ProblemDetail  
**roadmap.sh mapping:** Java — Object Oriented Programming → Record / Enum; Spring Boot — Spring MVC

### Tutorial

Good APIs make success and failure semantics predictable. Use HTTP status codes consistently, return machine-readable errors, and avoid versioning until compatibility requirements force it.

### Runnable mini-lab

```java
record ApiError(String code, String message) {}
public class Day75 {
    public static void main(String[] args) { System.out.println(new ApiError("PROPERTY_NOT_FOUND", "No property")); }
}
```

### Verify

Return a Spring `ProblemDetail` or typed error body from your global handler and verify 404/400 responses.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 76 — Filtering, Pagination and Specifications ⬜

**Planned date:** 2026-12-02  
**Java track:** Predicate composition  
**Spring track:** Spring Data JPA Specifications / dynamic queries  
**roadmap.sh mapping:** Java — Functional Programming → Functional Composition; Spring Boot — Spring Data JPA

### Tutorial

Dynamic search is naturally compositional: each optional filter contributes a predicate. Spring Data Specifications let you build database-side predicates instead of loading everything and filtering in Java.

### Runnable mini-lab

```java
java.util.function.Predicate<Integer> min = n -> n >= 1000;
java.util.function.Predicate<Integer> max = n -> n <= 3000;
public class Day76 {
    public static void main(String[] a) { System.out.println(min.and(max).test(2500)); }
}
```

### Verify

Fix placement if needed in your IDE, run the predicate composition, then implement two optional JPA filters with Specifications.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 77 — Spring Data MongoDB ⬜

**Planned date:** 2026-12-03  
**Java track:** Document modeling and Java records/classes  
**Spring track:** Mongo repositories and document mapping  
**roadmap.sh mapping:** Java — Database Access; Spring Boot — Spring Data MongoDB

### Tutorial

Document databases favor aggregate-oriented data shapes and different query/index tradeoffs than relational databases. Learn enough to choose intentionally rather than treating MongoDB as “JPA without joins.”

### Runnable mini-lab

```java
@org.springframework.data.mongodb.core.mapping.Document("properties")
record PropertyDocument(@org.springframework.data.annotation.Id String id, String title, java.util.List<String> amenities) {}
```

### Verify

Run MongoDB locally, save/find one document with `MongoRepository`, and inspect the stored JSON/BSON shape.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 78 — Persistence Strategy Comparison ⬜

**Planned date:** 2026-12-04  
**Java track:** Interfaces/adapters and substitution  
**Spring track:** Spring Data JDBC vs JPA vs MongoDB  
**roadmap.sh mapping:** Java — Database Access → JDBC / Hibernate / Spring Data JPA; Spring Boot — Spring Data JDBC / JPA / MongoDB

### Tutorial

Choose persistence technology by domain/query needs, not popularity. JDBC gives explicit SQL, JPA gives unit-of-work/object graph management, and MongoDB favors document aggregates.

### Runnable mini-lab

```java
interface PropertyStore { java.util.Optional<String> findTitle(long id); }
class InMemoryStore implements PropertyStore {
    public java.util.Optional<String> findTitle(long id) { return java.util.Optional.of("Batel"); }
}
public class Day78 { public static void main(String[] a){ System.out.println(new InMemoryStore().findTitle(1).orElseThrow()); } }
```

### Verify

Run it and write a short ADR choosing the default RentMap persistence approach with two alternatives.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 79 — Testing Remote Dependencies ⬜

**Planned date:** 2026-12-05  
**Java track:** Fakes/stubs at network boundaries  
**Spring track:** WireMock/MockWebServer style client tests  
**roadmap.sh mapping:** Java — Testing → Integration Testing; Spring Boot — Testing / Microservices

### Tutorial

Client code should be tested against HTTP behavior, not by mocking every internal method. A stub server lets you verify status handling, payload mapping, timeout behavior, and retries.

### Runnable mini-lab

```java
public class Day79 {
    public static void main(String[] args) {
        System.out.println("Stub server scenario: 200 JSON, 404, 500, slow response");
    }
}
```

### Verify

Use WireMock or MockWebServer to create those four responses and verify your Spring client behavior.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 80 — Full Integration with Testcontainers ⬜

**Planned date:** 2026-12-07  
**Java track:** Resource lifecycle in tests  
**Spring track:** Spring Boot + PostgreSQL/Kafka containers  
**roadmap.sh mapping:** Java — Testing → Integration Testing; Spring Boot — Testing

### Tutorial

Testcontainers gives disposable real infrastructure and reduces differences between test and production engines. Keep containers at the right scope so the suite stays trustworthy and reasonably fast.

### Runnable mini-lab

```java
@org.testcontainers.junit.jupiter.Testcontainers
class ContainerSmokeTest {
    @org.testcontainers.junit.jupiter.Container
    static org.testcontainers.containers.PostgreSQLContainer<?> db =
        new org.testcontainers.containers.PostgreSQLContainer<>("postgres:17-alpine");
}
```

### Verify

Run one repository integration test against the container and inspect the JDBC URL used by the test.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 81 — JVM Memory and Garbage Collection ⬜

**Planned date:** 2026-12-08  
**Java track:** Heap, stack, metaspace, allocation, GC basics  
**Spring track:** Diagnose memory behavior in Boot services  
**roadmap.sh mapping:** Java — Java Memory Model / Lifecycle of a Program; Spring Boot — Spring Boot

### Tutorial

Senior backend work benefits from distinguishing object allocation problems from leaks and from understanding GC pauses at a high level. You do not need to memorize collectors before you can read heap/GC signals intelligently.

### Runnable mini-lab

```java
public class Day81 {
    public static void main(String[] args) {
        Runtime r = Runtime.getRuntime();
        System.out.println("maxMB=" + r.maxMemory()/1024/1024);
        System.out.println("freeMB=" + r.freeMemory()/1024/1024);
    }
}
```

### Verify

Run with different `-Xmx` values and compare reported max memory.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 82 — Profiling and JFR ⬜

**Planned date:** 2026-12-09  
**Java track:** Measure CPU/allocation before optimizing  
**Spring track:** Actuator metrics + Java Flight Recorder workflow  
**roadmap.sh mapping:** Java — Java runtime / performance; Spring Boot — Actuators / Micrometer

### Tutorial

Optimization starts with measurement. JFR/JDK Mission Control can show CPU, allocations, locks, and GC while Actuator exposes application-level metrics.

### Runnable mini-lab

```java
public class Day82 {
    public static void main(String[] args) {
        long sum = 0; for (int i=0;i<100_000_000;i++) sum += i;
        System.out.println(sum);
    }
}
// Run: java -XX:StartFlightRecording=filename=day82.jfr,duration=10s Day82
```

### Verify

Record the program, open the JFR file, and identify the hottest method.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 83 — Production Concurrency Patterns ⬜

**Planned date:** 2026-12-10  
**Java track:** Queues, backpressure, bounded executors  
**Spring track:** Protect Spring services from overload  
**roadmap.sh mapping:** Java — Collections → Queue / Concurrency; Spring Boot — Microservices / Spring Boot

### Tutorial

Unbounded concurrency converts load spikes into memory/latency failures. Bounded queues and explicit rejection/backpressure policies make overload visible and controlled.

### Runnable mini-lab

```java
import java.util.concurrent.*;
public class Day83 {
    public static void main(String[] args) {
        var ex = new ThreadPoolExecutor(1,1,0,TimeUnit.SECONDS,new ArrayBlockingQueue<>(1));
        ex.execute(() -> sleep()); ex.execute(() -> sleep());
        try { ex.execute(() -> sleep()); } catch (RejectedExecutionException e) { System.out.println("rejected"); }
        ex.shutdown();
    }
    static void sleep(){ try { Thread.sleep(200); } catch(Exception ignored){} }
}
```

### Verify

Run it and explain why the third task can be rejected; relate this to protecting remote dependencies.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 84 — API Security / OWASP Review ⬜

**Planned date:** 2026-12-11  
**Java track:** Input trust boundaries and secure coding  
**Spring track:** Security headers, validation, authz, secrets  
**roadmap.sh mapping:** Java — Cryptography / Networking; Spring Boot — Spring Security

### Tutorial

Security is layered: authenticate, authorize, validate, encode output, protect secrets, and keep dependencies patched. Treat every network input as untrusted even when another internal service sent it.

### Runnable mini-lab

```java
public class Day84 {
    public static void main(String[] args) {
        String userInput = "../../etc/passwd";
        System.out.println("Never concatenate untrusted input into paths/SQL: " + userInput);
    }
}
```

### Verify

Review one endpoint against injection, broken access control, mass assignment, and secret leakage; add one concrete fix.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 85 — Packaging and Docker ⬜

**Planned date:** 2026-12-12  
**Java track:** JAR/runtime process model  
**Spring track:** Containerize a Spring Boot application  
**roadmap.sh mapping:** Java — Build Tools; Spring Boot — Spring Boot / related Docker roadmap

### Tutorial

A container packages the application runtime environment, not application state. Keep images small/reproducible and configuration external.

### Runnable mini-lab

```dockerfile
# Dockerfile
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

### Verify

Build the Boot JAR, `docker build -t rentmap-api .`, run it with a port mapping, and call `/actuator/health`.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 86 — Configuration Precedence and Secrets ⬜

**Planned date:** 2026-12-14  
**Java track:** Environment variables and process configuration  
**Spring track:** Boot config precedence, profiles, secret injection  
**roadmap.sh mapping:** Java — Build Tools / Java runtime; Spring Boot — Introduction → Configuration / Spring Cloud Config

### Tutorial

Configuration belongs outside the artifact when it varies by environment. Secrets should come from a secret store/environment mechanism, never from Git or logs.

### Runnable mini-lab

```java
public class Day86 {
    public static void main(String[] args) {
        String db = System.getenv("DB_URL");
        System.out.println(db == null ? "DB_URL not set" : "DB_URL configured");
    }
}
```

### Verify

Run once without and once with `DB_URL`; then verify which Spring property source wins when the same value exists in file and environment.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 87 — Architecture: SOLID, DDD, Hexagonal Boundaries ⬜

**Planned date:** 2026-12-15  
**Java track:** Interfaces, dependency direction, immutability  
**Spring track:** Organize Spring adapters around domain/application boundaries  
**roadmap.sh mapping:** Java — Object Oriented Programming; Spring Boot — Architecture / Dependency Injection

### Tutorial

Framework annotations should not define your domain architecture. Use interfaces and dependency direction so core business rules are testable without HTTP/database infrastructure.

### Runnable mini-lab

```java
interface PropertyRepositoryPort { java.util.Optional<Property> find(long id); }
record Property(long id, String title) {}
class FindProperty {
    private final PropertyRepositoryPort repo;
    FindProperty(PropertyRepositoryPort repo){ this.repo=repo; }
    Property execute(long id){ return repo.find(id).orElseThrow(); }
}
```

### Verify

Compile the core classes with a fake repository and prove the use case works without Spring.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 88 — System Design Exercise — RentMap Backend ⬜

**Planned date:** 2026-12-16  
**Java track:** Data structures, concurrency, caching, API contracts  
**Spring track:** Compose MVC, data, cache, security, observability, messaging  
**roadmap.sh mapping:** Java — Multiple Java sections; Spring Boot — Multiple Spring Boot sections

### Tutorial

Design the backend as a system, not a list of technologies. State requirements first, estimate scale, define API/data ownership, then add cache/events/resilience only where they solve a concrete problem.

### Runnable mini-lab

```java
public class Day88 {
    public static void main(String[] args) {
        System.out.println("Design: API -> Service -> Postgres/PostGIS; Redis cache; Kafka ingestion; Actuator/Micrometer");
    }
}
```

### Verify

Produce a one-page design with APIs, data model, critical sequence, failure modes, and three tradeoffs.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 89 — Full Mock Interview + Code Review ⬜

**Planned date:** 2026-12-17  
**Java track:** Java Core/concurrency/JVM/coding review  
**Spring track:** Spring Core/MVC/Data/Security/Microservices review  
**roadmap.sh mapping:** Java — All covered sections; Spring Boot — All covered sections

### Tutorial

Simulate the real interview: explain concepts aloud, solve a coding task under time, then review flawed production-style code. The goal is retrieval and judgment, not learning new annotations.

### Runnable mini-lab

```java
public class Day89 {
    public static void main(String[] args) {
        System.out.println("90-minute mock: 30 theory + 30 coding + 30 code review");
    }
}
```

### Verify

Do the session without notes. Record gaps as red/yellow/green and only review after the timer ends.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

## Day 90 — Final Evaluation and Next 90 Days ⬜

**Planned date:** 2026-12-18  
**Java track:** Java evaluation: core, functional, concurrency, JVM, persistence, testing  
**Spring track:** Spring evaluation: core, Boot, MVC, Data, Security, testing, microservices  
**roadmap.sh mapping:** Java — All covered sections; Spring Boot — All covered sections

### Tutorial

Day 90 is a diagnostic, not another content day. Demonstrate the plan’s completion rule: explain, implement, debug, review, and make architecture decisions without relying on memorized snippets.

### Runnable mini-lab

```java
public class Day90 {
    public static void main(String[] args) {
        System.out.println("Final: explain + implement + debug + review + design");
    }
}
```

### Verify

Score each domain Strong / Needs Reinforcement / Gap, then create the next plan from evidence rather than preference.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Run/implement the mini-lab and intentionally change or break one thing.
- [ ] Answer 3–5 interview questions or review a short code sample.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 90 completion rubric

| Area | Strong | Needs reinforcement | Gap |
|---|:---:|:---:|:---:|
| Java Core & OOP | ☐ | ☐ | ☐ |
| Collections & Generics | ☐ | ☐ | ☐ |
| Functional Java | ☐ | ☐ | ☐ |
| Exceptions & I/O | ☐ | ☐ | ☐ |
| Concurrency & JMM | ☐ | ☐ | ☐ |
| JVM & Performance | ☐ | ☐ | ☐ |
| JDBC/JPA/Hibernate | ☐ | ☐ | ☐ |
| Spring Core & Boot | ☐ | ☐ | ☐ |
| Spring MVC & API design | ☐ | ☐ | ☐ |
| Transactions | ☐ | ☐ | ☐ |
| Testing | ☐ | ☐ | ☐ |
| Spring Security | ☐ | ☐ | ☐ |
| Microservices & Spring Cloud | ☐ | ☐ | ☐ |
| Observability | ☐ | ☐ | ☐ |
| Coding / Code Review / Interview | ☐ | ☐ | ☐ |

The next 90-day plan should be built from this rubric, prioritizing real gaps rather than repeating broad introductory courses.