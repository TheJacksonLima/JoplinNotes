# Day 25 — I/O and File Operations + Multipart

## Goal

Day 25 connects Java file I/O with Spring MVC multipart upload/download handling.

```text
Part A — Java
Path
Files
BufferedReader / BufferedWriter
try-with-resources

Part B — Spring MVC
MultipartFile
file upload
safe storage
download
```

Core mental model:

```text
Path
→ filesystem location

Files
→ operations on that location

Buffered I/O
→ efficient sequential processing

try-with-resources
→ automatic cleanup

MultipartFile
→ file received through HTTP
```

---

# Part A — Java I/O and File Operations

## `Path`

```java
Path path = Path.of("day25.txt");
```

A `Path` only represents a location. The file does not need to exist.

Useful methods:

```java
path.getFileName();
path.getParent();
path.toAbsolutePath();
path.normalize();
path.isAbsolute();
```

Build platform-independent paths with:

```java
Path path =
        Path.of(
                "data",
                "payments",
                "payment.txt"
        );
```

Use `resolve()`:

```java
Path directory = Path.of("data");

Path file =
        directory.resolve(
                "payment.txt"
        );
```

Mental model:

```text
base path + child = resolved path
```

`normalize()` simplifies redundant path parts such as `.` and `..`.

---

## `Files`

`Path` answers **where**. `Files` answers **what operation**.

Examples:

```java
Files.writeString(path, "Hello");
Files.readString(path);
Files.exists(path);
Files.deleteIfExists(path);
```

Roadmap mini-lab:

```java
Path p = Path.of("day25.txt");

Files.writeString(
        p,
        "RentMap
"
);

System.out.println(
        Files.readString(p)
);

Files.deleteIfExists(p);
```

---

## Writing and Appending

Write:

```java
Files.writeString(
        path,
        "Payment approved"
);
```

Append:

```java
Files.writeString(
        path,
        "Payment 2
",
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
);
```

Important options:

```text
CREATE
CREATE_NEW
APPEND
TRUNCATE_EXISTING
WRITE
```

---

## Directories

Create one directory:

```java
Files.createDirectory(path);
```

Create missing hierarchy:

```java
Files.createDirectories(
        Path.of(
                "data",
                "2026",
                "payments"
        )
);
```

Important:

```text
Files.writeString()
can create the file

but does NOT create
missing parent directories
```

---

## File Inspection

```java
Files.exists(path);
Files.notExists(path);
Files.isDirectory(path);
Files.isRegularFile(path);
Files.size(path);
```

---

## Whole File vs Streaming

Convenient for small files:

```java
String content =
        Files.readString(path);
```

or:

```java
List<String> lines =
        Files.readAllLines(path);
```

For large files, prefer incremental processing:

```text
small file
→ readString / readAllLines

large file
→ BufferedReader / Files.lines()
```

---

## `BufferedReader`

```java
try (BufferedReader reader =
             Files.newBufferedReader(path)) {

    String line;

    while ((line =
            reader.readLine()) != null) {

        System.out.println(line);
    }
}
```

A buffered reader processes text sequentially without loading the whole file.

---

## `BufferedWriter`

```java
try (BufferedWriter writer =
             Files.newBufferedWriter(path)) {

    writer.write(
            "PAY-2026-123456"
    );

    writer.newLine();

    writer.write(
            "PAY-2026-654321"
    );
}
```

---

## Try-with-resources

Resources like readers, writers, streams, sockets, and DB connections must be closed.

Use:

```java
try (BufferedReader reader =
             Files.newBufferedReader(path)) {

    // use reader
}
```

Java closes the resource automatically, even if an exception happens.

Why it works:

```text
resource implements AutoCloseable
→ Java calls close()
```

Multiple resources are supported:

```java
try (
    BufferedReader reader =
            Files.newBufferedReader(input);

    BufferedWriter writer =
            Files.newBufferedWriter(output)
) {

    // use both
}
```

---

## `IOException`

Common causes:

```text
missing file
missing directory
permissions
invalid path
disk problems
locked files
```

Example:

```java
try {
    String content =
            Files.readString(path);

} catch (IOException ex) {
    System.out.println(
            ex.getMessage()
    );
}
```

Prefer precise exception handling over blindly catching `Exception`.

---

## `Files.lines()`

```java
try (Stream<String> lines =
             Files.lines(path)) {

    lines
        .filter(line ->
                line.contains("PAY"))
        .forEach(
                System.out::println
        );
}
```

Important:

```text
Files.lines()
→ Stream<String>
→ backed by an open file
→ use try-with-resources
```

Difference:

```text
readAllLines()
→ loads all lines into memory

Files.lines()
→ lazy stream
```

---

## Payment File Exercise

Create:

```text
data/payments.txt
```

with:

```text
PAY-2026-123456,150.00,PIX
PAY-2026-654321,250.00,CREDIT_CARD
PAY-2026-999999,90.00,PIX
```

Setup:

```java
Path directory =
        Path.of("data");

Files.createDirectories(directory);

Path payments =
        directory.resolve(
                "payments.txt"
        );
```

Write with `BufferedWriter`, read with `BufferedReader`, and filter with `Files.lines()`.

---

## Intentional Breaks

Missing file:

```java
Files.readString(
        Path.of(
                "does-not-exist.txt"
        )
);
```

Observe `NoSuchFileException`.

Missing parent directory:

```java
Path path =
        Path.of(
                "missing",
                "folder",
                "file.txt"
        );

Files.writeString(
        path,
        "hello"
);
```

Fix with:

```java
Files.createDirectories(
        path.getParent()
);
```

---

# Part B — Multipart Upload and Download

## `MultipartFile`

Spring represents an uploaded file with:

```java
org.springframework.web.multipart.MultipartFile
```

Useful methods:

```java
file.getOriginalFilename();
file.getSize();
file.getContentType();
file.isEmpty();
file.getInputStream();
file.getBytes();
```

Mental model:

```text
MultipartFile
→ uploaded HTTP file

Path
→ server filesystem location
```

---

## First Upload Endpoint

```java
@RestController
@RequestMapping("/files")
public class FileController {

    @PostMapping("/upload")
    public String upload(
            @RequestParam("file")
            MultipartFile file) {

        System.out.println(
                file.getOriginalFilename()
        );

        System.out.println(
                file.getSize()
        );

        System.out.println(
                file.getContentType()
        );

        return "File received";
    }
}
```

---

## Request Format

This is **not JSON**.

Use:

```text
multipart/form-data
```

In Insomnia:

```text
Body
→ Form Data / Multipart

Name:
file

Type:
File

Value:
choose a local file
```

The part name:

```text
file
```

must match:

```java
@RequestParam("file")
```

Wrong body mode:

```text
File
```

sends a raw body and may cause:

```text
Current request is not a multipart request
```

Correct flow:

```text
Insomnia
→ multipart/form-data
→ part "file"
→ Spring
→ MultipartFile
```

---

## Basic Validation

```java
if (file.isEmpty()) {
    throw new IllegalArgumentException(
            "File cannot be empty"
    );
}
```

Inspect:

```java
file.getSize();
file.getOriginalFilename();
file.getContentType();
```

`getSize()` is in bytes.

---

## Do Not Trust Original Filename

Do not blindly use:

```java
file.getOriginalFilename()
```

as the server storage path.

Possible malicious input:

```text
../../important-file.txt
```

Risk:

```text
path traversal
```

Basic sanitization:

```java
String originalName =
        file.getOriginalFilename();

String safeName =
        Path.of(originalName)
                .getFileName()
                .toString();
```

Safer approach:

```java
String storedName =
        UUID.randomUUID()
                + ".bin";
```

Mental model:

```text
original filename
→ metadata/display

generated filename
→ physical storage
```

---

## Safe Storage Root

```java
Path uploadDirectory =
        Path.of("uploads");
```

Create:

```java
Files.createDirectories(
        uploadDirectory
);
```

Resolve target:

```java
Path root =
        uploadDirectory
                .toAbsolutePath()
                .normalize();

Path target =
        root.resolve(storedName)
                .normalize();

if (!target.startsWith(root)) {
    throw new IllegalArgumentException(
            "Invalid file path"
    );
}
```

Core rule:

```text
resolved path
must remain inside
configured storage root
```

---

## Store with `Files.copy()`

```java
try (InputStream input =
             file.getInputStream()) {

    Files.copy(
            input,
            target,
            StandardCopyOption.REPLACE_EXISTING
    );
}
```

Flow:

```text
MultipartFile
↓
InputStream
↓
Files.copy()
↓
Path
```

---

## `getBytes()` vs `getInputStream()`

```text
getBytes()
→ loads entire upload into byte[]

getInputStream()
→ stream-based processing
```

For larger files, streaming is preferable.

---

## Storage Service

```java
@Service
public class FileStorageService {

    private final Path uploadDirectory =
            Path.of("uploads");

    public String store(
            MultipartFile file)
            throws IOException {

        Files.createDirectories(
                uploadDirectory
        );

        String storedName =
                UUID.randomUUID()
                        + ".bin";

        Path target =
                uploadDirectory
                        .resolve(storedName)
                        .normalize();

        try (InputStream input =
                     file.getInputStream()) {

            Files.copy(
                    input,
                    target,
                    StandardCopyOption
                            .REPLACE_EXISTING
            );
        }

        return storedName;
    }
}
```

Separation:

```text
Controller
→ HTTP / MultipartFile / response

Service
→ Path / Files / filesystem
```

---

## Upload Size Limits

Spring Boot:

```properties
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

Application-level rule:

```java
long maxSize =
        5 * 1024 * 1024;

if (file.getSize() > maxSize) {
    throw new IllegalArgumentException(
            "File is too large"
    );
}
```

---

## Content Type

```java
file.getContentType()
```

Examples:

```text
text/csv
application/pdf
image/png
```

Important:

```text
client-provided content type
is NOT proof of actual content
```

The same applies to filename extensions.

---

# Downloading

## Download Flow

```text
filesystem
↓
Resource
↓
Spring MVC
↓
HTTP response
```

Use:

```java
org.springframework.core.io.Resource
```

Example:

```java
FileSystemResource
```

---

## Simple Download Endpoint

```java
@GetMapping("/{name}")
public ResponseEntity<Resource> download(
        @PathVariable String name) {

    Path path =
            uploadDirectory
                    .resolve(name)
                    .normalize();

    Resource resource =
            new FileSystemResource(path);

    return ResponseEntity.ok()
            .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=""
                            + name
                            + """
            )
            .contentType(
                    MediaType.APPLICATION_OCTET_STREAM
            )
            .body(resource);
}
```

`Content-Disposition: attachment` tells the client this is downloadable content.

`application/octet-stream` means generic binary data.

---

## Download Security

A request such as:

```text
GET /files/../../something
```

must never escape the storage root.

Use the same rule as upload:

```text
fixed storage root
+
requested name
↓
resolve
↓
normalize
↓
verify still inside root
```

Check existence:

```java
if (!Files.exists(path)
        || !Files.isRegularFile(path)) {

    throw new FileNotFoundException(
            name
    );
}
```

This can later map to `404 Not Found`.

---

# Interview Questions

## Path vs Files?

`Path` represents a filesystem location. `Files` performs static file and directory operations on paths.

## Why try-with-resources?

It automatically closes `AutoCloseable` resources, even if an exception occurs.

## `readString()` vs `BufferedReader`?

`readString()` is convenient for small files. `BufferedReader` is better for sequential processing without loading the entire file.

## `readAllLines()` vs `Files.lines()`?

`readAllLines()` loads everything into memory. `Files.lines()` returns a lazy stream backed by the file.

## What is `MultipartFile`?

Spring's abstraction for a file received in a multipart HTTP request.

## Why not trust `getOriginalFilename()`?

Because it is client-controlled and can contain unsafe path information or collide with existing filenames.

## `getBytes()` vs `getInputStream()`?

`getBytes()` materializes the entire file into memory. `getInputStream()` supports streaming.

## How do you prevent path traversal?

Use a fixed storage root, resolve and normalize paths, verify the result remains inside the root, and prefer server-generated storage names.

## How can Spring limit upload size?

With:

```properties
spring.servlet.multipart.max-file-size
spring.servlet.multipart.max-request-size
```

plus application-level rules when needed.

---

# Concise Manual Notes

```text
DAY 25 — FILE I/O + MULTIPART

PART A

Path
= filesystem location

Files
= operations

Path.of()
resolve()
normalize()
toAbsolutePath()

Files:
writeString()
readString()
readAllLines()
lines()
exists()
size()
isRegularFile()
createDirectories()
deleteIfExists()
copy()
move()

Small file:
readString/readAllLines

Large file:
BufferedReader
Files.lines()

BufferedReader
= sequential buffered read

BufferedWriter
= sequential buffered write

try-with-resources:
try (resource) { ... }

→ automatic close
→ even on exception

AutoCloseable
→ close()

Files.lines()
→ Stream<String>
→ close it

IOException
= common I/O exception

writeString()
can create FILE
but not missing parent folders


PART B

MultipartFile
= uploaded HTTP file

@RequestParam("file")
MultipartFile file

Request:
multipart/form-data

NOT JSON
NOT raw File body

Useful:
isEmpty()
getOriginalFilename()
getSize()
getContentType()
getInputStream()
getBytes()

FLOW:
HTTP multipart
→ MultipartFile
→ validate
→ safe Path
→ Files.copy()
→ filesystem

DO NOT TRUST:
original filename

Risks:
path traversal
filename collision

Prefer:
server-generated name

root.resolve(name)
    .normalize()

verify path stays
inside root

Store:
try (InputStream in =
     file.getInputStream()) {

    Files.copy(
       in,
       target,
       REPLACE_EXISTING
    );
}

Upload limits:
max-file-size
max-request-size

DOWNLOAD:
Path
→ Resource
→ ResponseEntity<Resource>

Content-Disposition:
attachment

application/octet-stream
= generic binary
```

---

# Day 25 Completion Checklist

## Part A

- [ ] Explain `Path` vs `Files`
- [ ] Create relative and absolute paths
- [ ] Use `resolve()` and `normalize()`
- [ ] Create directories
- [ ] Write/read files
- [ ] Append to files
- [ ] Use `BufferedReader`
- [ ] Use `BufferedWriter`
- [ ] Explain try-with-resources
- [ ] Explain `AutoCloseable`
- [ ] Use `Files.lines()` safely
- [ ] Understand `IOException`
- [ ] Break on missing file
- [ ] Break on missing parent directory

## Part B

- [x] Send multipart request from Insomnia
- [x] Receive `MultipartFile`
- [x] Use field name `file`
- [x] Confirm `multipart/form-data`
- [ ] Inspect filename/size/content type
- [ ] Reject empty upload
- [ ] Generate safe server-side filename
- [ ] Create upload directory
- [ ] Store with `InputStream` + `Files.copy()`
- [ ] Use try-with-resources
- [ ] Add upload size limits
- [ ] Add download endpoint
- [ ] Return Spring `Resource`
- [ ] Protect against path traversal
- [ ] Verify file existence before download

---

# Final Mental Model

```text
Java filesystem:

Path
→ location

Files
→ operation

Buffered I/O
→ incremental processing

try-with-resources
→ safe cleanup
```

HTTP upload:

```text
multipart/form-data
        ↓
MultipartFile
        ↓
validate
        ↓
safe normalized Path
        ↓
Files.copy()
        ↓
filesystem
```

Download:

```text
filesystem Path
        ↓
Resource
        ↓
ResponseEntity<Resource>
        ↓
HTTP download
```
