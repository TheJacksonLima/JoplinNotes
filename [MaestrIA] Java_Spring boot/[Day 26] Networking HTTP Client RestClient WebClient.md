# Day 26 — Networking, Java HTTP Client, RestClient and WebClient

## Goal

Day 26 focuses on outbound HTTP communication from Java and Spring applications.

```text
Part A — Java
URI
HttpClient
HttpRequest
HttpResponse
timeouts
status handling

Part B — Spring
RestClient
JSON ↔ DTO mapping
HTTP error handling
WebClient fundamentals
```

Core idea:

```text
remote HTTP call
→ network can fail
→ server can be slow
→ server can return 4xx / 5xx
→ client must handle each case explicitly
```

---

# Part A — Java Networking and `HttpClient`

## `URI`

Java uses `java.net.URI` to represent a resource location.

```java
URI uri =
        URI.create(
                "http://localhost:8081/payments/1"
        );
```

Useful parts:

```java
uri.getScheme();
uri.getHost();
uri.getPort();
uri.getPath();
```

Example:

```text
http://localhost:8081/payments/1

scheme → http
host   → localhost
port   → 8081
path   → /payments/1
```

Mental model:

```text
URI
→ identifies where the resource is
```

---

## `HttpClient`

Java provides:

```java
java.net.http.HttpClient
```

Create:

```java
HttpClient client =
        HttpClient.newBuilder()
                .connectTimeout(
                        Duration.ofSeconds(3)
                )
                .build();
```

Mental model:

```text
HttpClient
→ sends HTTP requests
```

Prefer reusing a client instead of creating one for every request.

---

## `HttpRequest`

A request contains:

```text
URI
HTTP method
headers
body
timeout
```

GET example:

```java
HttpRequest request =
        HttpRequest.newBuilder(uri)
                .timeout(
                        Duration.ofSeconds(3)
                )
                .header(
                        "Accept",
                        "application/json"
                )
                .GET()
                .build();
```

---

## `HttpResponse`

Send:

```java
HttpResponse<String> response =
        client.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );
```

Response contains:

```text
status
headers
body
```

Access:

```java
response.statusCode();
response.headers();
response.body();
```

---

## `BodyHandlers`

```java
HttpResponse.BodyHandlers.ofString()
```

means:

```text
response body
→ String
```

Therefore:

```java
HttpResponse<String>
```

Other examples:

```java
BodyHandlers.ofByteArray();
BodyHandlers.ofFile(...);
BodyHandlers.discarding();
```

---

## HTTP Status Handling

Examples:

```text
200 → OK
201 → Created
204 → No Content

400 → Bad Request
401 → Unauthorized
403 → Forbidden
404 → Not Found

500 → Internal Server Error
503 → Service Unavailable
```

Important:

```text
client.send(...)
```

does not throw just because the server returned `404` or `500`.

Those are valid HTTP responses.

---

## Transport Failure vs HTTP Failure

### HTTP failure

```text
400
404
500
503
```

Meaning:

```text
communication succeeded
server returned an error response
```

### Transport/network failure

```text
connection refused
DNS failure
connection reset
timeout
```

Meaning:

```text
communication did not complete normally
```

Mental model:

```text
404 / 500
→ HTTP response

IOException / timeout
→ transport problem
```

---

## Successful Status Helper

```java
static boolean isSuccessful(
        int status) {

    return status >= 200
            && status < 300;
}
```

Usage:

```java
if (isSuccessful(
        response.statusCode())) {

    System.out.println(
            response.body()
    );

} else {

    System.out.println(
            "Request failed: "
                    + response.statusCode()
    );
}
```

---

## Timeouts

### Connect timeout

```java
HttpClient client =
        HttpClient.newBuilder()
                .connectTimeout(
                        Duration.ofSeconds(3)
                )
                .build();
```

Meaning:

```text
how long to wait
to establish a connection
```

### Request timeout

```java
HttpRequest request =
        HttpRequest.newBuilder(uri)
                .timeout(
                        Duration.ofSeconds(3)
                )
                .GET()
                .build();
```

Meaning:

```text
how long to wait
for the request operation
```

Important principle:

```text
network calls must be bounded
```

Without bounded waiting:

```text
slow dependency
↓
blocked threads
↓
requests accumulate
↓
resource exhaustion
↓
cascading failure
```

---

## `send()` vs `sendAsync()`

```java
client.send(...)
```

is synchronous.

```text
thread
↓
send()
↓
wait
↓
response/failure
↓
continue
```

`sendAsync()` returns a `CompletableFuture`.

For Day 26 we focused on `send()`.

---

## Headers

```java
HttpRequest request =
        HttpRequest.newBuilder(uri)
                .header(
                        "Accept",
                        "application/json"
                )
                .GET()
                .build();
```

Important:

```text
Accept
→ response format I want

Content-Type
→ request format I send
```

---

## POST JSON with `HttpClient`

```java
String json = """
        {
          "amount": 100,
          "method": "PIX",
          "reference": "PAY-2026-123456"
        }
        """;
```

Request:

```java
HttpRequest request =
        HttpRequest.newBuilder(
                URI.create(
                        "http://localhost:8081/payments/preview"
                )
        )
        .header(
                "Content-Type",
                "application/json"
        )
        .header(
                "Accept",
                "application/json"
        )
        .POST(
                HttpRequest.BodyPublishers
                        .ofString(json)
        )
        .build();
```

`BodyPublishers.ofString(json)` means:

```text
request body
→ String
```

---

## Exceptions

### `HttpTimeoutException`

```java
catch (HttpTimeoutException ex) {
    System.err.println(
            "Request timed out"
    );
}
```

### `IOException`

```java
catch (IOException ex) {
    System.err.println(
            "Network failure: "
                    + ex.getMessage()
    );
}
```

### `InterruptedException`

```java
catch (InterruptedException ex) {

    Thread.currentThread()
            .interrupt();

    System.err.println(
            "Request interrupted"
    );
}
```

Do not silently swallow interruption.

---

## Exception Ordering

`HttpTimeoutException` extends `IOException`.

So this is correct:

```java
catch (HttpTimeoutException ex) {
    ...
}
catch (IOException ex) {
    ...
}
```

Specific exceptions come before general ones.

---

## Client Timeout vs HTTP 504

These are different.

```text
client-side timeout
→ application waited too long
→ exception
```

```text
504 Gateway Timeout
→ server/proxy returned HTTP 504
```

---

# Part B — Spring `RestClient`

## Why `RestClient`?

Java `HttpClient` works with lower-level pieces such as:

```text
JSON String
HttpRequest
BodyPublisher
HttpResponse<String>
```

Spring `RestClient` is higher level.

Example:

```java
PaymentResponse payment =
        restClient
                .get()
                .uri(
                        "/payments/{id}",
                        1L
                )
                .retrieve()
                .body(
                        PaymentResponse.class
                );
```

Now the result is a `PaymentResponse` directly.

---

## Creating `RestClient`

For the mini-lab:

```java
RestClient restClient =
        RestClient.builder()
                .baseUrl(
                        "http://localhost:8081"
                )
                .build();
```

Using a base URL avoids repeating the host/port for every request.

---

## GET with `RestClient`

```java
PaymentResponse payment =
        restClient
                .get()
                .uri(
                        "/payments/{id}",
                        1L
                )
                .accept(
                        MediaType.APPLICATION_JSON
                )
                .retrieve()
                .body(
                        PaymentResponse.class
                );
```

Flow:

```text
GET /payments/1
↓
HTTP response
↓
JSON
↓
Jackson
↓
PaymentResponse
```

---

## `.retrieve()`

```java
.retrieve()
```

executes the request and begins response processing.

Then:

```java
.body(PaymentResponse.class)
```

deserializes the response body into `PaymentResponse`.

---

## Jackson Integration

Server side:

```text
PaymentResponse
↓
Jackson
↓
JSON
```

Client side:

```text
JSON
↓
Jackson
↓
PaymentResponse
```

---

## POST with Java DTO

```java
PaymentRequest request =
        new PaymentRequest(
                new BigDecimal("100.00"),
                PaymentMethod.PIX,
                "PAY-2026-123456"
        );
```

Send:

```java
PaymentResponse response =
        restClient
                .post()
                .uri(
                        "/payments/preview"
                )
                .contentType(
                        MediaType.APPLICATION_JSON
                )
                .accept(
                        MediaType.APPLICATION_JSON
                )
                .body(request)
                .retrieve()
                .body(
                        PaymentResponse.class
                );
```

Important comparison:

```text
Java HttpClient
→ manually create JSON String
→ BodyPublishers.ofString(json)

Spring RestClient
→ .body(paymentRequest)
→ Jackson serializes automatically
```

---

## HTTP Error Handling with `RestClient`

With Java `HttpClient`:

```text
404
→ normal HttpResponse
```

With:

```java
restClient
        .get()
        .retrieve()
```

4xx and 5xx are treated as errors by default.

Example:

```java
try {

    PaymentResponse payment =
            restClient
                    .get()
                    .uri(
                            "/payments/{id}",
                            9999999999L
                    )
                    .retrieve()
                    .body(
                            PaymentResponse.class
                    );

} catch (
        RestClientResponseException ex) {

    System.err.println(
            "Status: "
                    + ex.getStatusCode()
    );

    System.err.println(
            "Body: "
                    + ex.getResponseBodyAsString()
    );
}
```

Key difference:

```text
HttpClient
404 → inspect status manually

RestClient.retrieve()
404 → exception by default
```

---

## Bean Validation + RestClient

Invalid request:

```java
PaymentRequest invalidRequest =
        new PaymentRequest(
                new BigDecimal("-100.00"),
                PaymentMethod.PIX,
                "INVALID"
        );
```

Possible flow:

```text
Day 24 Bean Validation
↓
400 Bad Request
↓
RestClientResponseException
```

---

## `ResponseEntity`

Sometimes we want status and headers too.

```java
ResponseEntity<PaymentResponse> response =
        restClient
                .get()
                .uri(
                        "/payments/{id}",
                        1L
                )
                .retrieve()
                .toEntity(
                        PaymentResponse.class
                );
```

Access:

```java
response.getStatusCode();
response.getHeaders();
response.getBody();
```

Difference:

```text
body(...)
→ body only

toEntity(...)
→ status + headers + body
```

---

## `onStatus()`

Customize status handling:

```java
PaymentResponse payment =
        restClient
                .get()
                .uri(
                        "/payments/{id}",
                        9999999999L
                )
                .retrieve()
                .onStatus(
                        status ->
                                status.value() == 404,

                        (
                                request,
                                response
                        ) -> {

                            throw new IllegalStateException(
                                    "Payment not found"
                            );
                        }
                )
                .body(
                        PaymentResponse.class
                );
```

Concept:

```text
remote HTTP 404
↓
application exception
```

Useful for translating:

```text
HTTP semantics
↓
application/domain semantics
```

---

## Same Project, Two Processes

The API can run while the mini-lab client runs separately.

```text
Process 1
Spring Boot API
→ localhost:8081

Process 2
RestClientDemo
→ outbound HTTP call
→ localhost:8081
```

This is perfectly valid for the Day 26 lab.

---

# WebClient Fundamentals

## `WebClient`

Spring also provides `WebClient`.

Comparison:

| | RestClient | WebClient |
|---|---|---|
| Style | Imperative | Reactive |
| Blocking model | Synchronous | Designed non-blocking |
| Typical return | `PaymentResponse` | `Mono<PaymentResponse>` |
| Natural fit | Spring MVC | Spring WebFlux |
| Complexity | Lower | Higher |

For the current Payment application:

```text
Spring MVC
+
imperative code
→ RestClient is the simpler fit
```

---

## Minimal `WebClient`

```java
WebClient webClient =
        WebClient.builder()
                .baseUrl(
                        "http://localhost:8081"
                )
                .build();
```

GET:

```java
Mono<PaymentResponse> payment =
        webClient
                .get()
                .uri(
                        "/payments/{id}",
                        1L
                )
                .retrieve()
                .bodyToMono(
                        PaymentResponse.class
                );
```

---

## `Mono<T>` and `Flux<T>`

Simple mental model:

```text
Mono<T>
→ 0 or 1 value

Flux<T>
→ 0 to many values
```

Deep Reactor programming is outside Day 26 scope.

---

## Avoid `.block()` Everywhere

You can technically do:

```java
PaymentResponse payment =
        webClient
                .get()
                .uri("/payments/1")
                .retrieve()
                .bodyToMono(
                        PaymentResponse.class
                )
                .block();
```

But this turns a reactive call back into a blocking call.

If the application is already synchronous and imperative, `RestClient` is usually clearer.

---

## `RestTemplate`

Older Spring applications may use:

```java
RestTemplate
```

Mental model:

```text
RestTemplate
→ older synchronous Spring client

RestClient
→ modern fluent synchronous API

WebClient
→ reactive/non-blocking API
```

---

# Exercises

## Exercise 1 — GET Existing Payment

```java
PaymentResponse payment =
        restClient
                .get()
                .uri(
                        "/payments/{id}",
                        1L
                )
                .retrieve()
                .body(
                        PaymentResponse.class
                );
```

Observe:

```text
JSON
→ PaymentResponse
```

---

## Exercise 2 — GET Missing Payment

Call:

```text
/payments/9999999999
```

Observe:

```text
RestClient.retrieve()
→ 404
→ exception
```

Compare with:

```text
HttpClient
→ 404 returned as HttpResponse
```

---

## Exercise 3 — POST Preview

```java
PaymentRequest request =
        new PaymentRequest(
                new BigDecimal("100"),
                PaymentMethod.PIX,
                "PAY-2026-123456"
        );
```

Send:

```java
restClient
        .post()
        .uri("/payments/preview")
        .contentType(
                MediaType.APPLICATION_JSON
        )
        .body(request)
        .retrieve()
        .body(
                PaymentResponse.class
        );
```

Observe:

```text
Java DTO
→ Jackson
→ JSON
```

---

## Exercise 4 — Invalid POST

```java
new PaymentRequest(
        new BigDecimal("-100"),
        PaymentMethod.PIX,
        "INVALID"
);
```

Expected:

```text
400 Bad Request
```

Observe:

```text
Bean Validation
↓
HTTP 400
↓
RestClient exception
```

---

## Exercise 5 — `ResponseEntity`

Use:

```java
.retrieve()
.toEntity(
        PaymentResponse.class
);
```

Observe:

```text
status
headers
body
```

---

## Exercise 6 — `onStatus()`

Translate:

```text
404
↓
custom Java/application exception
```

---

# Interview Questions

## What is the difference between `HttpClient`, `HttpRequest`, and `HttpResponse`?

`HttpClient` sends requests. `HttpRequest` represents the request configuration. `HttpResponse` represents the returned status, headers, and body.

## What is the difference between HTTP 500 and `IOException`?

HTTP 500 means communication succeeded and the server returned an error response. `IOException` indicates a transport/I/O failure.

## Why should HTTP calls have timeouts?

Remote dependencies can become slow or unavailable. Timeouts bound waiting time and reduce resource exhaustion and cascading failures.

## What is the difference between `send()` and `sendAsync()`?

`send()` is synchronous. `sendAsync()` returns a `CompletableFuture` and supports asynchronous processing.

## Why restore the interrupt flag after `InterruptedException`?

Because higher layers may need to know the thread was interrupted. Swallowing interruption can break cancellation and coordination.

## Why use `RestClient` instead of Java `HttpClient` in a Spring application?

`RestClient` is a higher-level Spring-integrated abstraction that can automatically serialize and deserialize DTOs using Spring message converters and Jackson.

## What happens on 404 with `RestClient.retrieve()`?

By default, 4xx and 5xx responses are treated as errors and result in a `RestClientResponseException` subtype.

## `body()` vs `toEntity()`?

`body()` returns only the deserialized body. `toEntity()` returns status, headers, and body.

## Why use `onStatus()`?

To customize HTTP status handling and translate remote HTTP errors into application-specific exceptions.

## `RestClient` vs `WebClient`?

`RestClient` is synchronous and imperative. `WebClient` is reactive and designed for non-blocking workflows.

---

# Concise Manual Notes

```text
DAY 26 — NETWORKING

PART A

URI
= resource address

HttpClient
= sends requests

HttpRequest
= URI + method + headers + body + timeout

HttpResponse
= status + headers + body

GET:
newBuilder(uri)
.GET()
.build()

SEND:
client.send(
 request,
 BodyHandlers.ofString()
)

2xx success
4xx client/request error
5xx server error

IMPORTANT:

404 / 500
= HTTP response
= communication worked

IOException
= network/transport failure

TIMEOUTS:
connectTimeout(...)
request.timeout(...)

network calls must be bounded

Accept
= what I want back

Content-Type
= what I send

send()
= synchronous

sendAsync()
= async

Exceptions:
HttpTimeoutException
IOException
InterruptedException

InterruptedException:
Thread.currentThread().interrupt()


PART B

RestClient
= synchronous Spring HTTP client

GET:
restClient
 .get()
 .uri(...)
 .retrieve()
 .body(PaymentResponse.class)

POST:
restClient
 .post()
 .uri(...)
 .body(paymentRequest)
 .retrieve()
 .body(PaymentResponse.class)

HttpClient:
manual JSON String

RestClient:
Java DTO directly

Jackson:
PaymentRequest → JSON
JSON → PaymentResponse

retrieve():
4xx / 5xx
→ exception by default

RestClientResponseException:
status + error body

body(...)
→ body only

toEntity(...)
→ status + headers + body

onStatus(...)
→ custom HTTP error mapping

RestClient
= synchronous / imperative

WebClient
= reactive / non-blocking

Mono<T>
= 0 or 1

Flux<T>
= 0..N

Spring MVC
→ RestClient

Spring WebFlux
→ WebClient
```

---

# Day 26 Completion Checklist

## Part A

- [x] Explain `URI`
- [x] Create `HttpClient`
- [x] Build `HttpRequest`
- [x] Read `HttpResponse`
- [x] Send GET
- [x] Read status code
- [x] Read body
- [x] Add `Accept`
- [x] Configure connection timeout
- [x] Configure request timeout
- [x] Explain HTTP error vs network failure
- [x] Handle `HttpTimeoutException`
- [x] Handle `IOException`
- [x] Handle `InterruptedException`
- [x] Restore interrupt flag
- [ ] Run POST JSON
- [ ] Intentionally test wrong port

## Part B

- [x] Create `RestClient`
- [x] Configure base URL
- [x] Understand GET DTO mapping
- [x] Understand Jackson serialization/deserialization
- [x] Understand `retrieve()`
- [x] Understand default 4xx/5xx handling
- [x] Understand `RestClientResponseException`
- [x] Understand `ResponseEntity`
- [x] Understand `onStatus()`
- [x] Compare `RestClient` vs `WebClient`
- [x] Understand `Mono` and `Flux` at a high level
- [ ] Run valid GET
- [ ] Run missing GET
- [ ] Run valid POST
- [ ] Run invalid POST
- [ ] Run `toEntity()` exercise
- [ ] Run `onStatus()` exercise

---

# Final Mental Model

```text
Java HttpClient:

URI
↓
HttpRequest
↓
HttpClient.send()
↓
HttpResponse<String>
↓
inspect status manually
```

Spring `RestClient`:

```text
Java DTO
↓
Jackson
↓
HTTP request
↓
remote API
↓
HTTP response
↓
Jackson
↓
Java DTO
```

Error model:

```text
HttpClient
404 / 500
→ normal HttpResponse

RestClient.retrieve()
404 / 500
→ exception by default
```

Architecture choice:

```text
Spring MVC / imperative
→ RestClient

Spring WebFlux / reactive
→ WebClient
```
