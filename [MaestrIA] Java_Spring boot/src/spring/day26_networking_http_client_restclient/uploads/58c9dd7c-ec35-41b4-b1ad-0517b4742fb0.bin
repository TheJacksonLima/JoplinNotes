# Day 24 — Regular Expressions + Bean Validation

## Goal

Day 24 connects Java regex fundamentals with Spring MVC request validation.

```text
Part A — Java Regular Expressions
Part B — Bean Validation in Spring Boot
```

The central idea is:

```text
Regex
→ validates text structure

Bean Validation
→ validates request DTO constraints

Service/domain logic
→ handles business rules
```

---

# Part A — Regular Expressions in Java

## 1. `Pattern` and `Matcher`

`Pattern` is the compiled regex definition.

```java
Pattern pattern = Pattern.compile("\\d+");
```

`Matcher` applies the pattern to a particular input.

```java
Matcher matcher = pattern.matcher("12345");
```

Mental model:

```text
Pattern
→ compiled regex

Matcher
→ pattern + input
```

Useful methods:

```text
matches()
find()
group()
start()
end()
```

---

## 2. CEP Example

Roadmap regex:

```text
\d{5}-?\d{3}
```

Java:

```java
Pattern cep =
        Pattern.compile(
                "\\d{5}-?\\d{3}"
        );

boolean valid =
        cep.matcher("80000-000")
                .matches();
```

Breakdown:

```text
\d{5}
→ five digits

-?
→ optional hyphen

\d{3}
→ three digits
```

Valid:

```text
80000-000
80000000
```

---

## 3. Java Regex Escaping

The regex engine expects:

```text
\d
```

Inside Java source code:

```java
"\\d"
```

Why:

```text
Java String parser
↓
regex parser
```

So:

```text
Regex: \d
Java:  "\\d"
```

The same applies to word boundaries:

```text
Regex: \b
Java:  "\\b"
```

Important: Java's `"\b"` is a backspace character, not a regex word boundary.

---

## 4. Common Regex Symbols

```text
\d     digit
\w     word character
\s     whitespace

[A-Z]  uppercase letter
[0-9]  digit range

*      zero or more
+      one or more
?      zero or one

{3}    exactly 3
{2,5}  between 2 and 5
{2,}   at least 2
```

---

## 5. `matches()` vs `find()`

`matches()` requires the entire input to match.

```java
Pattern pattern =
        Pattern.compile("\\d{3}");

pattern.matcher("123")
        .matches();        // true

pattern.matcher("ABC123XYZ")
        .matches();        // false
```

`find()` searches for a matching substring.

```java
Matcher matcher =
        pattern.matcher("ABC123XYZ");

matcher.find();            // true
```

Mental model:

```text
matches()
→ whole input

find()
→ matching substring
```

---

## 6. Regex Boundaries

Important boundaries:

```text
^   start
$   end
\b  word boundary
```

Example:

```text
^PAY
→ must start with PAY

\d{3}$
→ must end with three digits

\bPIX\b
→ PIX as a whole word
```

Java word-boundary example:

```java
Pattern pix =
        Pattern.compile("\\bPIX\\b");
```

This finds:

```text
Payment PIX approved
```

but not `PIX` inside:

```text
PIXEL
```

With `Matcher.matches()`, explicit `^...$` anchors are often unnecessary because `matches()` already requires the entire input to match.

---

## 7. Capturing Groups

Parentheses create groups.

```java
Pattern pattern =
        Pattern.compile(
                "(\\d{5})-(\\d{3})"
        );

Matcher matcher =
        pattern.matcher("80000-000");
```

After a successful match:

```java
matcher.group(1);   // 80000
matcher.group(2);   // 000
```

`group()` without an index returns the whole match.

---

## 8. Multiple Matches

`find()` can be called repeatedly.

```java
Pattern numbers =
        Pattern.compile("\\d+");

Matcher matcher =
        numbers.matcher(
                "Payment 123 amount 500"
        );

while (matcher.find()) {
    System.out.println(
            matcher.group()
    );
}
```

Output:

```text
123
500
```

`Matcher` maintains state as it moves through the input.

---

## 9. `start()` and `end()`

For each match:

```java
matcher.start();
matcher.end();
```

Meaning:

```text
start()
→ index where match begins

end()
→ index immediately after match
```

Useful for parsing and highlighting text.

---

## 10. Payment Reference Example

Format:

```text
PAY-YYYY-NNNNNN
```

Example:

```text
PAY-2026-123456
```

Regex:

```text
PAY-\d{4}-\d{6}
```

Java:

```java
Pattern paymentReference =
        Pattern.compile(
                "PAY-\\d{4}-\\d{6}"
        );
```

Validation:

```java
paymentReference
        .matcher(
                "PAY-2026-123456"
        )
        .matches();
```

---

## 11. Extract Payment Reference Parts

```java
Pattern reference =
        Pattern.compile(
                "PAY-(\\d{4})-(\\d{6})"
        );

Matcher matcher =
        reference.matcher(
                "PAY-2026-123456"
        );
```

Then:

```java
matcher.group(1);   // 2026
matcher.group(2);   // 123456
```

Regex can therefore be used for both:

```text
validation
+
extraction
```

---

## 12. Reusing a Pattern

When a regex is reused, compile it once:

```java
private static final Pattern
        PAYMENT_REFERENCE_PATTERN =
        Pattern.compile(
                "PAY-\\d{4}-\\d{6}"
        );
```

Then:

```java
PAYMENT_REFERENCE_PATTERN
        .matcher(reference)
        .matches();
```

---

## 13. `String.matches()`

For a simple one-off check:

```java
"80000-000".matches(
        "\\d{5}-?\\d{3}"
);
```

For reused patterns or advanced matching operations, prefer:

```text
Pattern + Matcher
```

---

## 14. Regex vs Business Rules

Regex is good for:

```text
text structure
format
codes
identifiers
```

Business rules belong in Java/domain logic.

Examples that should not become giant regexes:

```text
account must be active
customer must have enough balance
payment must be below risk limit
transaction must be allowed at current settlement time
```

Mental model:

```text
Regex
→ structure

Java/domain/service
→ business meaning
```

---

# Part B — Bean Validation in Spring Boot

## 15. Why Bean Validation?

Without Bean Validation, controllers/services may contain repetitive checks:

```java
if (request.amount() == null) {
    ...
}

if (request.amount().signum() <= 0) {
    ...
}
```

Bean Validation lets input constraints live close to the request DTO.

---

## 16. Dependency

Add:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

Use Jakarta packages with Spring Boot 3+:

```java
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
```

Use:

```text
jakarta.validation
```

not the old:

```text
javax.validation
```

---

## 17. Common Constraints

```text
@NotNull
→ value cannot be null

@NotBlank
→ String cannot be null, empty, or whitespace-only

@NotEmpty
→ String/collection cannot be empty

@Positive
→ value > 0

@PositiveOrZero
→ value >= 0

@Min / @Max
→ numeric limits

@Size
→ string/collection size

@Pattern
→ regex structure

@Email
→ email structure
```

---

## 18. `PaymentRequest`

Example:

```java
public record PaymentRequest(

        @NotNull(
            message =
                "Amount is required"
        )
        @Positive(
            message =
                "Amount must be greater than zero"
        )
        BigDecimal amount,

        @NotNull(
            message =
                "Payment method is required"
        )
        PaymentMethod method,

        @NotBlank(
            message =
                "Reference is required"
        )
        @Pattern(
            regexp =
                "PAY-\\d{4}-\\d{6}",
            message =
                "Reference must follow PAY-YYYY-NNNNNN"
        )
        String reference

) {
}
```

The DTO now represents:

```text
data
+
input constraints
```

---

## 19. `@Pattern` and `null`

Important behavior:

```java
@Pattern(...)
String reference;
```

by itself does not make the field mandatory.

`null` is considered valid by `@Pattern`.

If the field is required:

```java
@NotBlank
@Pattern(...)
String reference;
```

Mental model:

```text
@NotBlank
→ must exist

@Pattern
→ must have correct structure
```

---

## 20. Activating Validation with `@Valid`

Controller:

```java
@PostMapping("/preview")
public PaymentResponse preview(
        @Valid
        @RequestBody
        PaymentRequest request) {

    ...
}
```

`@Valid` triggers Bean Validation after binding/deserialization.

---

## 21. Request Lifecycle

```text
HTTP JSON
       ↓
Jackson
       ↓
PaymentRequest
       ↓
@Valid
       ↓
Bean Validation
       ↓
valid?
  ↙          ↘
yes          no
↓             ↓
Controller   MethodArgumentNotValidException
              ↓
       @RestControllerAdvice
              ↓
       validation error response
              ↓
           400 JSON
```

---

## 22. Valid Request

```json
{
  "amount": 150.00,
  "method": "PIX",
  "reference": "PAY-2026-123456"
}
```

Flow:

```text
Jackson ✓
Validation ✓
Controller ✓
```

---

## 23. Invalid Amount

```json
{
  "amount": -50,
  "method": "PIX",
  "reference": "PAY-2026-123456"
}
```

Fails:

```java
@Positive
```

Flow:

```text
Jackson ✓
Bean Validation ✗
Controller not executed
```

---

## 24. Invalid Reference

```json
{
  "amount": 100,
  "method": "PIX",
  "reference": "INVALID"
}
```

Fails:

```java
@Pattern(...)
```

---

## 25. `MethodArgumentNotValidException`

When validation fails for:

```java
@Valid @RequestBody
```

Spring MVC typically raises:

```java
MethodArgumentNotValidException
```

This connects Day 24 to Day 22's global exception handling.

---

## 26. `BindingResult` and `FieldError`

From the exception:

```java
ex.getBindingResult()
```

Field-level errors:

```java
ex.getBindingResult()
        .getFieldErrors()
```

A `FieldError` exposes:

```java
fieldError.getField()
fieldError.getDefaultMessage()
```

Example:

```text
reference
→ Reference must follow PAY-YYYY-NNNNNN
```

---

## 27. Validation Error DTO

Example:

```java
public record ValidationErrorResponse(
        Instant timestamp,
        int status,
        String error,
        Map<String, String> fieldErrors,
        String path
) {
}
```

Possible JSON:

```json
{
  "timestamp": "2026-11-11T12:00:00Z",
  "status": 400,
  "error": "Validation Failed",
  "fieldErrors": {
    "amount": "Amount must be greater than zero",
    "reference": "Reference must follow PAY-YYYY-NNNNNN"
  },
  "path": "/payments/preview"
}
```

---

## 28. Building the Field Error Map

```java
Map<String, String> errors =
        new LinkedHashMap<>();

for (FieldError fieldError :
        ex.getBindingResult()
                .getFieldErrors()) {

    errors.put(
            fieldError.getField(),
            fieldError.getDefaultMessage()
    );
}
```

---

## 29. Global Validation Handler

```java
@ExceptionHandler(
        MethodArgumentNotValidException.class
)
public ResponseEntity<ValidationErrorResponse>
        handleValidation(
                MethodArgumentNotValidException ex,
                HttpServletRequest request) {

    Map<String, String> errors =
            new LinkedHashMap<>();

    for (FieldError fieldError :
            ex.getBindingResult()
                    .getFieldErrors()) {

        errors.put(
                fieldError.getField(),
                fieldError.getDefaultMessage()
        );
    }

    ValidationErrorResponse response =
            new ValidationErrorResponse(
                    Instant.now(),
                    400,
                    "Validation Failed",
                    errors,
                    request.getRequestURI()
            );

    return ResponseEntity
            .badRequest()
            .body(response);
}
```

---

## 30. Jackson Errors vs Validation Errors

### Invalid JSON syntax

Example:

```json
{
  "amount":
}
```

Problem:

```text
JSON parsing/deserialization
```

### Wrong Java representation

Example:

```json
{
  "method": "BITCOIN"
}
```

If `PaymentMethod` has no `BITCOIN`, Jackson cannot deserialize the enum.

Problem:

```text
Jackson conversion
```

### Valid Java object, invalid constraints

Example:

```json
{
  "amount": -100,
  "method": "PIX",
  "reference": "BAD"
}
```

Problem:

```text
Bean Validation
```

Mental model:

```text
Jackson
→ can JSON become Java?

Bean Validation
→ is the Java request acceptable?
```

---

## 31. Bean Validation vs Business Validation

Good Bean Validation rules:

```text
required field
positive amount
string length
regex structure
email syntax
```

Business rules belong in service/domain logic:

```text
customer has enough balance
account is active
risk limit allows transaction
payment is allowed in current workflow
```

Mental model:

```text
Bean Validation
→ request contract

Service/domain
→ business rules
```

---

# Intentional Break Exercises

## Break 1 — Remove `@Valid`

Remove `@Valid` from the controller and send invalid input.

Observe that DTO constraints are no longer triggered as expected for the request body.

Restore `@Valid`.

---

## Break 2 — Remove `@NotBlank`

Leave only:

```java
@Pattern(...)
String reference;
```

Send:

```json
{
  "reference": null
}
```

Observe that `@Pattern` alone does not reject `null`.

Restore `@NotBlank`.

---

## Break 3 — Change the Regex

Change:

```text
PAY-\d{4}-\d{6}
```

to:

```text
PAY-\d{3}-\d{6}
```

Then test:

```text
PAY-2026-123456
```

It should fail.

---

# Interview Questions

## What is the difference between `Pattern` and `Matcher`?

`Pattern` is the compiled regex definition. `Matcher` applies that pattern to a specific input and maintains matching state.

## What is the difference between `matches()` and `find()`?

`matches()` requires the entire input to match. `find()` searches for matching substrings.

## Why do Java regex strings use double backslashes?

Because Java String escaping happens before regex parsing. `"\\d"` is required so the regex engine receives `\d`.

## What are regex boundaries?

```text
^  → start
$  → end
\b → word boundary
```

## What does `@Valid` do?

It triggers Bean Validation for the request DTO after binding/deserialization.

## What is the difference between `@Pattern` and `@NotBlank`?

`@Pattern` checks structure against a regex. `@NotBlank` ensures a String exists and contains non-whitespace text.

## What happens when `@Valid @RequestBody` fails?

Spring MVC raises `MethodArgumentNotValidException`, which can be handled globally with `@RestControllerAdvice` and `@ExceptionHandler`.

## Jackson failure vs Bean Validation failure?

Jackson failure means JSON cannot be converted into the required Java type. Bean Validation failure means the Java object exists but violates declared constraints.

## Should all business rules use Bean Validation?

No. Bean Validation is best for request/structural constraints. State-dependent business rules belong in domain/service logic.

---

# Concise Manual Notes

```text
DAY 24

PART A — REGEX

Pattern
= compiled regex

Matcher
= pattern + input

matches()
= whole input

find()
= substring search

group()
= matched value

group(n)
= captured group

start()/end()
= match indexes

COMMON:
\d  digit
\w  word char
\s  whitespace
+   1+
*   0+
?   0/1
{3} exactly 3

BOUNDARIES:
^   start
$   end
\b  word boundary

JAVA ESCAPING:
regex: \d
Java:  "\\d"

CEP:
\\d{5}-?\\d{3}

Payment reference:
PAY-\\d{4}-\\d{6}

Regex
→ structure

Java/domain
→ business rules


PART B — VALIDATION

Dependency:
spring-boot-starter-validation

Use:
jakarta.validation.*

@NotNull
= not null

@NotBlank
= non-null/non-empty/non-whitespace

@Positive
= > 0

@Pattern
= regex constraint

Controller:
@Valid
@RequestBody
PaymentRequest request

Flow:
JSON
→ Jackson
→ DTO
→ @Valid
→ Bean Validation

Invalid DTO:
→ MethodArgumentNotValidException

Global handling:
@RestControllerAdvice
@ExceptionHandler(...)

BindingResult
→ validation result

getFieldErrors()
→ field problems

IMPORTANT:
@Pattern alone allows null

Use:
@NotBlank + @Pattern

Jackson error:
JSON → Java fails

Validation error:
Java object exists
but constraints fail

Bean Validation
→ request contract

Service/domain
→ business rules
```

---

# Day 24 Completion Checklist

## Part A

- [x] Understand `Pattern`
- [x] Understand `Matcher`
- [x] Use `matches()`
- [x] Use `find()`
- [x] Use `group()`
- [x] Use capturing groups
- [x] Use `start()` and `end()`
- [x] Understand Java regex escaping
- [x] Understand `^`, `$`, and `\b`
- [x] Validate CEP
- [x] Validate payment reference
- [x] Extract groups

## Part B

- [ ] Add `spring-boot-starter-validation`
- [ ] Add constraints to `PaymentRequest`
- [ ] Add `@Valid` to controller
- [ ] Test valid request
- [ ] Test invalid amount
- [ ] Test invalid reference
- [ ] Test multiple invalid fields
- [ ] Handle `MethodArgumentNotValidException`
- [ ] Return field-specific validation errors
- [ ] Remove `@Valid` and observe behavior
- [ ] Remove `@NotBlank` and test null reference

---

# Final Mental Model

```text
Regex
→ is the text structurally correct?

Jackson
→ can JSON become Java?

Bean Validation
→ does the Java request satisfy API constraints?

Service/domain
→ is the request allowed by business rules?
```
