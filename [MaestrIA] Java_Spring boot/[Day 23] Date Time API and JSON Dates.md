# Day 23 — Date/Time API + JSON Dates

## Goal

Day 23 connects Java's modern date/time API with Spring MVC and Jackson.

```text
Part A — Java Date/Time API
Part B — Jackson Date/Time Serialization + API Contracts
```

The key principle is:

```text
Choose temporal types by meaning,
not by convenience.
```

Core mapping:

```text
LocalDate      → date only
LocalDateTime  → date + time, no timezone
Instant        → exact point on the global timeline
ZonedDateTime  → date + time + ZoneId / timezone rules
Duration       → elapsed amount of time
```

---

# Part A — Java Date/Time API

## 1. `java.time`

Modern Java uses:

```java
import java.time.*;
```

Important types:

```text
Instant
LocalDate
LocalDateTime
ZonedDateTime
ZoneId
Duration
```

These types are immutable.

```java
LocalDate today = LocalDate.now();
LocalDate tomorrow = today.plusDays(1);
```

`today` is unchanged.

---

## 2. `Instant`

`Instant` represents one exact moment on the global timeline.

```java
Instant now = Instant.now();
```

Typical representation:

```text
2026-11-10T14:00:00Z
```

`Z` means UTC.

Use `Instant` for:

```text
createdAt
updatedAt
processedAt
receivedAt
audit/event timestamps
```

Example:

```java
Instant later = now.plusSeconds(90);
Instant earlier = now.minusSeconds(30);

now.isBefore(later);   // true
now.isAfter(later);    // false
now.equals(later);     // false
```

Mental model:

```text
Instant → exactly when?
```

---

## 3. `LocalDate`

`LocalDate` contains year, month, and day only.

```java
LocalDate today = LocalDate.now();
LocalDate birthday = LocalDate.of(1990, 5, 20);
LocalDate parsed = LocalDate.parse("2026-11-10");
```

Good use cases:

```text
birthday
holiday
invoice date
contract date
due date
```

Mental model:

```text
LocalDate → what date?
```

---

## 4. `LocalDateTime`

`LocalDateTime` contains date + clock time, but **no timezone**.

```java
LocalDateTime local = LocalDateTime.of(
        2026,
        11,
        10,
        15,
        30
);
```

`2026-11-10T15:30` does not tell us whether it means São Paulo, New York, London, etc.

Mental model:

```text
LocalDateTime
→ what local date/time?
→ timezone unknown
```

---

## 5. `ZoneId`

A region timezone is represented by `ZoneId`.

```java
ZoneId saoPaulo = ZoneId.of("America/Sao_Paulo");
```

Prefer region-based identifiers rather than manually subtracting offsets.

Do not do:

```java
time.minusHours(3);
```

for timezone conversion.

---

## 6. `ZonedDateTime`

`ZonedDateTime` contains date + time + `ZoneId` + timezone rules.

```java
ZonedDateTime now = ZonedDateTime.now(
        ZoneId.of("America/Sao_Paulo")
);
```

Mental model:

```text
ZonedDateTime → when, in which region?
```

---

## 7. `Instant` vs `ZonedDateTime`

```text
Instant
→ exact global moment

ZonedDateTime
→ local representation of a moment in a region
```

Convert:

```java
Instant instant = Instant.now();

ZonedDateTime brazil = instant.atZone(
        ZoneId.of("America/Sao_Paulo")
);
```

Reverse:

```java
Instant backToInstant = brazil.toInstant();
```

---

## 8. One Instant, Many Local Representations

```java
Instant instant = Instant.parse("2026-11-10T14:00:00Z");

ZonedDateTime brazil = instant.atZone(
        ZoneId.of("America/Sao_Paulo")
);

ZonedDateTime newYork = instant.atZone(
        ZoneId.of("America/New_York")
);
```

The displayed local times may differ, but:

```java
brazil.toInstant().equals(newYork.toInstant());
```

returns `true`.

```text
different local clock time
≠ different instant
```

---

## 9. `LocalDateTime + ZoneId`

```java
LocalDateTime local = LocalDateTime.of(
        2026,
        11,
        10,
        15,
        30
);

ZonedDateTime zoned = local.atZone(
        ZoneId.of("America/Sao_Paulo")
);
```

Mental model:

```text
LocalDateTime + ZoneId = ZonedDateTime
```

---

## 10. `Duration`

`Duration` represents elapsed time.

```java
Instant start = Instant.now();
Instant end = start.plusSeconds(90);

Duration duration = Duration.between(start, end);

System.out.println(duration.toSeconds());
```

Output:

```text
90
```

Mental model:

```text
Instant  → point on timeline
Duration → distance between timeline points
```

Useful for processing time, request latency, timeouts, and job duration.

---

## 11. Negative Duration

Order matters:

```java
Duration.between(start, end)
```

means `end - start`.

Reversing them can produce a negative duration.

```java
Duration negative = Duration.between(end, start);
```

---

## 12. Invalid Dates

Java temporal types validate real calendar values.

```java
LocalDate.of(2026, 2, 30);
```

throws a `DateTimeException`.

---

## 13. Type Selection Guide

```text
Birthday                   → LocalDate
Payment created timestamp  → Instant
Meeting tied to a region   → ZonedDateTime
Local schedule             → LocalDateTime may fit
Processing duration        → Duration
```

The core rule:

```text
Choose by semantics.
```

---

# Part B — Jackson Date/Time + API Contracts

## 14. Serialization and Deserialization

```text
Serialization
Java → JSON

Deserialization
JSON → Java
```

This applies to temporal values too.

An `Instant` may serialize as:

```json
"2026-11-10T14:00:00Z"
```

---

## 15. `Instant createdAt` in a DTO

For backend event timestamps, `Instant` is usually a strong fit.

```java
public record PaymentResponse(
        BigDecimal amount,
        String method,
        Instant createdAt
) {
}
```

For the exercise, the mapper may temporarily use `Instant.now()`:

```java
public PaymentResponse toResponse(Payment payment) {
    return new PaymentResponse(
            payment.getAmount(),
            payment.getMethod().name(),
            Instant.now()
    );
}
```

Production note:

```text
Instant.now() in mapper
≠ actual creation time
```

In a real application, `createdAt` normally comes from the domain/entity or persistence layer.

---

## 16. Jackson Serialization

Spring MVC/Jackson handles:

```text
PaymentResponse
        ↓
Jackson
        ↓
JSON
```

Example:

```json
{
  "amount": 250.00,
  "method": "PIX",
  "createdAt": "2026-11-10T14:00:00Z"
}
```

The `Z` makes the timestamp globally unambiguous.

---

## 17. Why `Instant` Is Better Than `LocalDateTime` for `createdAt`

Compare:

```json
"createdAt": "2026-11-10T14:00:00Z"
```

with:

```json
"createdAt": "2026-11-10T14:00:00"
```

The first identifies one exact moment. The second does not tell us the timezone.

```text
createdAt → normally Instant
```

---

## 18. Keep Strong Java Types

Avoid:

```java
String createdAt;
```

Prefer:

```java
Instant createdAt;
```

Rule:

```text
inside Java
→ temporal type

HTTP boundary
→ let Jackson serialize it
```

---

## 19. Deserializing an `Instant`

Request DTO:

```java
public record PaymentTimeRequest(
        Instant processedAt
) {
}
```

Temporary educational endpoint:

```java
@PostMapping("/time")
public PaymentTimeRequest time(
        @RequestBody PaymentTimeRequest request) {

    return request;
}
```

Request:

```json
{
  "processedAt": "2026-11-10T14:00:00Z"
}
```

Flow:

```text
JSON
↓
Jackson
↓
Instant
↓
Controller
```

---

## 20. Invalid Temporal JSON

```json
{
  "processedAt": "not-a-date"
}
```

Jackson cannot convert that value into an `Instant`.

```text
deserialization error
≠ Bean Validation error
```

Bean Validation belongs to Day 24.

---

## 21. Ambiguous Timestamp Input

Compare:

```text
2026-11-10T14:00:00
```

with:

```text
2026-11-10T14:00:00Z
```

The first has no global timezone context. The second identifies an exact instant.

API contracts should avoid ambiguous timestamps when the field represents a global event.

---

## 22. `LocalDate` in JSON

```java
public record CustomerResponse(
        String name,
        LocalDate birthday
) {
}
```

JSON:

```json
{
  "name": "John",
  "birthday": "1990-05-20"
}
```

This is appropriate because a birthday is a date-only concept.

---

## 23. `LocalDateTime` in JSON

```java
public record ExampleResponse(
        LocalDateTime createdAt
) {
}
```

JSON may look like:

```json
{
  "createdAt": "2026-11-10T14:00:00"
}
```

The representation is valid, but the contract may be semantically ambiguous.

Jackson cannot add timezone information that the Java type does not contain.

---

## 24. `ZonedDateTime` in JSON

Use `ZonedDateTime` when region rules genuinely matter.

Always inspect the actual JSON contract instead of assuming how the zone will be represented.

```text
Do not assume date/time JSON.
Inspect it.
```

---

## 25. Adding Timestamp to `ApiError`

Day 22 introduced:

```java
public record ApiError(
        int status,
        String error,
        String message,
        String path
) {
}
```

Day 23 can evolve it to:

```java
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path
) {
}
```

Handler example:

```java
ApiError error = new ApiError(
        Instant.now(),
        404,
        "Not Found",
        ex.getMessage(),
        request.getRequestURI()
);
```

Expected JSON:

```json
{
  "timestamp": "2026-11-10T17:45:22.123Z",
  "status": 404,
  "error": "Not Found",
  "message": "Payment not found: 999",
  "path": "/payments/999"
}
```

---

## 26. Backend vs Presentation Layer

A common architecture is:

```text
Backend/API
→ Instant

UI / presentation
→ convert Instant to user's ZoneId
```

Example:

```java
instant.atZone(
        ZoneId.of("America/Sao_Paulo")
);
```

The backend exposes the global fact. The presentation layer chooses the local representation.

---

## 27. Avoid Manual Timezone Arithmetic

Do not do:

```java
time.minusHours(3);
```

Use `ZoneId` / `ZonedDateTime` instead.

Timezone rules may change and may vary historically.

---

## 28. `@JsonFormat`

Jackson supports custom formatting with `@JsonFormat`, for example:

```java
@JsonFormat(pattern = "yyyy-MM-dd")
```

But use custom formats only when the API contract requires them.

For Day 23, prefer:

```text
correct Java type
+
standard ISO-style representation
```

---

# Part B Exercises

## Exercise 1 — `PaymentResponse`

Add `Instant createdAt` to `PaymentResponse`, update the mapper, call `GET /payments` and `GET /payments/{id}`, and inspect the JSON.

## Exercise 2 — `ApiError`

Add `Instant timestamp` to `ApiError`, request a missing payment, and inspect the serialized timestamp.

## Exercise 3 — Deserialization

Create:

```java
record PaymentTimeRequest(
        Instant processedAt
) {
}
```

Send:

```json
{
  "processedAt": "2026-11-10T14:00:00Z"
}
```

Verify that Jackson creates an `Instant`.

## Exercise 4 — Break Deserialization

Send:

```json
{
  "processedAt": "not-a-date"
}
```

Observe Jackson conversion failure.

## Exercise 5 — Compare with `LocalDateTime`

Temporarily serialize `LocalDateTime.now()` and inspect the JSON.

Ask:

```text
Can another system determine the exact global moment?
```

Answer:

```text
No, not without timezone context.
```

---

# Interview Questions

## What is the difference between `Instant` and `LocalDateTime`?

`Instant` represents an exact point on the global timeline. `LocalDateTime` represents a date and clock time without timezone or offset information, so it does not uniquely identify a global moment.

## When would you use `LocalDate`?

For date-only concepts such as birthdays, holidays, invoice dates, or due dates.

## What is `ZonedDateTime`?

A date/time together with a `ZoneId`, including region timezone rules, so it can be mapped to a global instant.

## What is `Duration`?

An elapsed amount of time between temporal points.

## Which type would you normally use for `createdAt`?

Usually `Instant`, because `createdAt` represents one exact event on the global timeline.

## How does Spring Boot serialize `Instant`?

Spring MVC uses Jackson's Java time support to serialize it into a JSON timestamp representation, typically ISO-8601 style with UTC/offset information.

## Why should a DTO use `Instant` instead of `String`?

Because `Instant` preserves temporal semantics and type safety inside Java. Jackson should handle conversion at the HTTP boundary.

## What happens if JSON contains an invalid `Instant`?

Jackson cannot deserialize it into an `Instant`, so request conversion fails before normal controller processing continues.

## Is a Jackson deserialization error the same as Bean Validation?

No.

```text
Jackson
→ converts JSON to Java types

Bean Validation
→ validates constraints
```

Validation is covered on Day 24.

---

# Senior-Level Takeaways

```text
LocalDate      → date only
LocalDateTime  → local date/time, no zone
Instant        → exact global moment
ZonedDateTime  → moment tied to region rules
Duration       → elapsed time
```

Avoid ambiguous API timestamps:

```text
2026-11-10T14:00:00Z
→ globally unambiguous

2026-11-10T14:00:00
→ timezone unknown
```

Keep temporal types inside Java and convert/serialize only at boundaries.

---

# Concise Manual Notes

```text
DAY 23 — DATE/TIME + JSON

PART A

LocalDate
= date only

LocalDateTime
= date + time
= NO timezone
= globally ambiguous

Instant
= exact global point
= good for createdAt / audit

ZonedDateTime
= date + time + ZoneId
= timezone rules

Duration
= elapsed time

Conversions:
instant.atZone(zone)
zoned.toInstant()

LocalDateTime + ZoneId
→ ZonedDateTime

Choose type by SEMANTICS.

PART B

Jackson:
Java → JSON
JSON → Java

createdAt
→ Instant

Example:
"2026-11-10T14:00:00Z"

Z = UTC / exact instant

Keep:
Instant createdAt

Not:
String createdAt

LocalDate JSON:
"2026-11-10"

LocalDateTime:
no zone → can be ambiguous

ApiError:
Instant timestamp
status
error
message
path

Backend:
Instant

Presentation:
Instant → ZoneId

Invalid date string
→ Jackson deserialization error

Deserialization
≠ validation
```

---

# Day 23 Completion Checklist

## Part A

- [x] Explain `Instant`
- [x] Explain `LocalDate`
- [x] Explain `LocalDateTime`
- [x] Explain `ZonedDateTime`
- [x] Explain `Duration`
- [x] Convert `Instant` → `ZonedDateTime`
- [x] Convert `ZonedDateTime` → `Instant`
- [x] Calculate `Duration`
- [x] Observe negative duration
- [x] Observe invalid `LocalDate`

## Part B

- [ ] Add `Instant createdAt` to a response DTO
- [ ] Inspect serialized JSON
- [ ] Add `Instant timestamp` to `ApiError`
- [ ] Inspect error JSON
- [ ] Deserialize ISO timestamp into `Instant`
- [ ] Break deserialization with invalid date input
- [ ] Compare `Instant` with `LocalDateTime` ambiguity
- [ ] Explain why `createdAt` normally fits `Instant`

---

# Final Mental Model

```text
LocalDate
→ what date?

LocalDateTime
→ what local date/time?

Instant
→ exactly when?

ZonedDateTime
→ when, in which region?

Duration
→ how long?
```

At the API boundary:

```text
strong Java temporal type
        ↓
Jackson
        ↓
clear JSON contract
```
