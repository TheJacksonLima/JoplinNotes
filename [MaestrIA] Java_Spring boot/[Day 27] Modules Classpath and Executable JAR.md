# Day 27 — Modules, Classpath and Executable JAR

## Goal

Day 27 connects Java runtime class loading with Spring Boot packaging and embedded server execution.

```text
Part A — Java
modules
classpath
JAR packaging

Part B — Spring Boot
embedded server
executable JAR
BOOT-INF/classes
BOOT-INF/lib
Spring Boot launcher
```

Core mental model:

```text
.java source
↓
javac
↓
.class bytecode
↓
classpath / module path
↓
JVM resolves classes
↓
application runs
```

For Spring Boot:

```text
application classes
+
dependency JARs
+
Spring Boot launcher
+
embedded server
↓
executable JAR
↓
java -jar app.jar
```

---

# Part A — Modules, Classpath and JAR Packaging

## Source Code vs Bytecode

Java source:

```text
Hello.java
```

is compiled into:

```text
Hello.class
```

Flow:

```text
.java
↓ javac
.class
↓ JVM
execution
```

The JVM runs bytecode, not source code.

---

## What Is the Classpath?

The classpath is the set of locations where the JVM searches for classes and resources.

Examples:

```text
target/classes
out/
spring-core.jar
jackson-databind.jar
postgresql.jar
```

Mental model:

```text
classpath
→ search path for Java classes/resources
```

Run with:

```bash
java -cp out mini_lab.day27.ModulesClasspathJarDemo
```

`-cp` and `-classpath` are equivalent.

---

## Package Name and Class Location

If the source declares:

```java
package mini_lab.day27;
```

then the compiled class corresponds to:

```text
mini_lab/day27/ModulesClasspathJarDemo.class
```

The JVM searches from the classpath root.

---

## Roadmap Mini-Lab

```java
package mini_lab.day27;

public class ModulesClasspathJarDemo {

    public static void main(String[] args) {

        Module module =
                ModulesClasspathJarDemo.class
                        .getModule();

        System.out.println(
                "Module: "
                        + module
        );

        System.out.println(
                "Module name: "
                        + module.getName()
        );

        System.out.println(
                "Named module: "
                        + module.isNamed()
        );

        System.out.println(
                "Classpath:"
        );

        System.out.println(
                System.getProperty(
                        "java.class.path"
                )
        );
    }
}
```

Typical non-modular result:

```text
Module name: null
Named module: false
```

This means the code is running in the:

```text
unnamed module
```

using the traditional classpath model.

---

## Java Platform Module System — JPMS

Java 9 introduced JPMS.

A named module is described with:

```text
module-info.java
```

Example:

```java
module payment.app {

    requires java.sql;
}
```

`requires` means:

```text
what my module needs
```

Example:

```java
module payment.domain {

    exports org.jfl.domain;
}
```

`exports` means:

```text
what my module exposes
```

Summary:

```text
requires
→ dependency

exports
→ package exposure
```

---

## Classpath vs Module Path

Traditional Java:

```bash
java -cp out ...
```

uses the classpath.

Modular Java can use:

```bash
java --module-path ...
```

Conceptually:

```text
classpath
→ traditional classes/JARs

module path
→ named modules
```

In many Spring Boot applications, the traditional classpath model is still common.

---

## Why IDEs Hide the Classpath

IntelliJ, Maven, Gradle, and Spring Boot do not eliminate the classpath.

They manage it for you.

A successful IDE run still relies on the JVM being given access to all required classes and dependency JARs.

---

## Runtime Class Problems

A program can compile and still fail at runtime if dependencies differ between compile-time and runtime.

Useful operational distinction:

```text
ClassNotFoundException
→ code/classloader explicitly tried to load a class
  and could not find it

NoClassDefFoundError
→ JVM needed a class at runtime
  but it was unavailable or failed to initialize
```

---

# JAR Packaging

## What Is a JAR?

JAR means:

```text
Java ARchive
```

It can contain:

```text
.class files
resources
metadata
```

Inspect a JAR with:

```bash
jar tf application.jar
```

A JAR is not automatically an executable application.

---

## Manual Compile and JAR Exercise

Example class:

```java
package mini_lab.day27;

public class SimpleJarDemo {

    public static void main(
            String[] args) {

        System.out.println(
                "Running from JAR"
        );
    }
}
```

Compile:

```bash
javac -d out   src/mini_lab/day27/SimpleJarDemo.java
```

Create a normal JAR:

```bash
jar --create     --file day27.jar     -C out .
```

Run using the classpath:

```bash
java -cp day27.jar   mini_lab.day27.SimpleJarDemo
```

Lesson:

```text
JAR
→ can be a classpath container
```

---

## Executable JAR

Create:

```bash
jar --create     --file day27-executable.jar     --main-class mini_lab.day27.SimpleJarDemo     -C out .
```

Run:

```bash
java -jar day27-executable.jar
```

The JAR manifest contains an entry point such as:

```text
Main-Class: mini_lab.day27.SimpleJarDemo
```

---

## `java -cp` vs `java -jar`

Classpath execution:

```bash
java -cp day27.jar   mini_lab.day27.SimpleJarDemo
```

You provide:

```text
classpath + main class
```

Executable JAR:

```bash
java -jar day27-executable.jar
```

The manifest tells Java which main class to use.

---

## Dependencies and Runtime Classpath

A normal application JAR may depend on other JARs.

Example:

```text
application.jar
spring.jar
jackson.jar
postgresql.jar
```

Concept:

```text
your JAR
+
dependency JARs
=
runtime classpath
```

This is the packaging problem Spring Boot simplifies.

---

## Maven Connection

```text
pom.xml
↓
Maven resolves dependencies
↓
downloads JARs
↓
builds classpaths
↓
compiles/tests/packages
```

---

## Intentional Breaks

Wrong classpath:

```bash
java -cp something-else   mini_lab.day27.SimpleJarDemo
```

Wrong fully-qualified name:

```bash
java -cp out SimpleJarDemo
```

Correct:

```bash
java -cp out   mini_lab.day27.SimpleJarDemo
```

Normal JAR without a main manifest:

```bash
java -jar normal.jar
```

may fail with:

```text
no main manifest attribute
```

But this can still work:

```bash
java -cp normal.jar   mini_lab.day27.SimpleJarDemo
```

Key lesson:

```text
valid JAR
≠ executable JAR
```

---

# Part B — Spring Boot Executable JAR and Embedded Server

## Traditional Web Deployment

Traditional model:

```text
application
↓
WAR
↓
external server
↓
Tomcat / WebLogic / JBoss
```

---

## Spring Boot Model

Typical Boot model:

```text
application
+
embedded server
↓
executable JAR
↓
java -jar application.jar
```

The server is part of the application runtime.

---

## Embedded Server

With:

```text
spring-boot-starter-web
```

Spring Boot normally includes embedded Tomcat.

Conceptually:

```text
Java process
├── JVM
├── Spring Boot
├── application
└── Tomcat
```

`SpringApplication.run(...)` starts the Spring context and embedded server.

---

## Package the Spring Boot Project

Maven:

```bash
mvn clean package
```

or:

```bash
./mvnw clean package
```

Windows:

```powershell
.\mvnw.cmd clean package
```

Result:

```text
target/
└── application-0.0.1-SNAPSHOT.jar
```

---

# Spring Boot JAR Structure

Inspect:

```bash
jar tf target/application.jar
```

Typical structure:

```text
META-INF/
META-INF/MANIFEST.MF

BOOT-INF/
BOOT-INF/classes/
BOOT-INF/lib/

Spring Boot loader classes
```

---

## `BOOT-INF/classes`

Contains your application classes and resources.

Example:

```text
BOOT-INF/classes/
├── org/jfl/Application.class
├── org/jfl/controller/
├── org/jfl/service/
├── org/jfl/domain/
├── mini_lab/day27/
└── application.properties
```

Mental model:

```text
BOOT-INF/classes
→ my code and resources
```

---

## `BOOT-INF/lib`

Contains dependency JARs.

Examples:

```text
spring-web-....jar
spring-context-....jar
spring-boot-....jar
jackson-databind-....jar
tomcat-embed-core-....jar
```

Mental model:

```text
BOOT-INF/lib
→ dependency JARs
```

---

## Find Application Classes

```bash
jar tf target/application.jar   | grep "BOOT-INF/classes"   | head -20
```

---

## Find Dependencies

```bash
jar tf target/application.jar   | grep "BOOT-INF/lib"   | head -20
```

---

## Find Embedded Tomcat

```bash
jar tf target/application.jar   | grep tomcat
```

Expected dependency names include:

```text
tomcat-embed-core
tomcat-embed-websocket
tomcat-embed-el
```

This proves the web server is packaged as an application dependency.

---

## Find Jackson

```bash
jar tf target/application.jar   | grep jackson
```

These dependencies support Java object ↔ JSON mapping.

---

## Why Spring Boot Needs a Launcher

A Boot JAR contains nested dependency JARs:

```text
application.jar
└── BOOT-INF/lib/
    ├── spring.jar
    ├── jackson.jar
    └── tomcat.jar
```

The normal JVM classpath does not simply load every nested JAR automatically.

Spring Boot uses a launcher that understands this layout.

Flow:

```text
java -jar app.jar
↓
Spring Boot launcher
↓
loads BOOT-INF/classes
↓
loads BOOT-INF/lib
↓
starts application
```

---

## Inspect `MANIFEST.MF`

```bash
unzip -p target/application.jar   META-INF/MANIFEST.MF
```

Conceptually:

```text
Main-Class:
org.springframework.boot.loader.launch.JarLauncher

Start-Class:
org.jfl.Application
```

---

## `Main-Class` vs `Start-Class`

In a Spring Boot executable JAR:

```text
Main-Class
→ Spring Boot launcher

Start-Class
→ application main class
```

Flow:

```text
java -jar app.jar
↓
JarLauncher
↓
BOOT-INF/classes + BOOT-INF/lib
↓
Start-Class
↓
SpringApplication.run(...)
```

---

## Run the Packaged Application

```bash
java -jar   target/application.jar
```

At this point IntelliJ is not required to run the app.

The executable JAR itself starts Spring Boot and embedded Tomcat.

---

## Run on Another Port

```bash
java -jar   target/application.jar   --server.port=9090
```

Lesson:

```text
same artifact
+
different runtime configuration
```

This is an important deployment principle.

---

## Same Artifact Across Environments

Prefer:

```text
same JAR
+
environment-specific configuration
```

instead of producing different code builds for DEV, QA, and PROD.

---

## Maven Is Build-Time

Maven:

```text
resolve dependencies
compile
test
package
```

JVM:

```text
run application
```

Once packaged, a Boot JAR normally needs a compatible Java runtime, not Maven, to execute.

---

## JAR vs WAR

Spring Boot executable JAR:

```text
embedded server
→ java -jar
```

Traditional WAR:

```text
external server
→ deploy WAR
```

Spring Boot can support WAR packaging when needed, but executable JAR is the common model.

---

## Intentional Break — Port Conflict

If IntelliJ is already running on port `8081` and you start the packaged JAR on the same port, the second process should fail.

Fix:

```bash
java -jar target/application.jar   --server.port=9090
```

Now:

```text
Process A → 8081
Process B → 9090
```

---

## Connection to Docker

Example:

```dockerfile
FROM eclipse-temurin:21-jre

COPY target/app.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]
```

Concept:

```text
Boot JAR
↓
Java runtime image
↓
container
```

Embedded Tomcat means the container does not need a separately installed Tomcat.

---

## Connection to Kubernetes

Kubernetes sees:

```text
container
↓
process
↓
listening port
```

Inside:

```text
java -jar app.jar
↓
embedded Tomcat
↓
application port
```

---

## Client Library vs External Service

The JAR can contain client libraries:

```text
JDBC driver
Jackson
Spring
```

But it does not contain the external services themselves:

```text
Oracle
PostgreSQL server
Kafka
Redis
SQS
external APIs
```

Important:

```text
client library
≠ external infrastructure
```

---

# Verification Commands

Package:

```bash
./mvnw clean package
```

Inspect application classes:

```bash
jar tf target/YOUR_JAR.jar   | grep "BOOT-INF/classes"   | head -20
```

Inspect dependencies:

```bash
jar tf target/YOUR_JAR.jar   | grep "BOOT-INF/lib"   | head -20
```

Find Tomcat:

```bash
jar tf target/YOUR_JAR.jar   | grep tomcat
```

Inspect manifest:

```bash
unzip -p target/YOUR_JAR.jar   META-INF/MANIFEST.MF
```

Run:

```bash
java -jar target/YOUR_JAR.jar
```

Run another port:

```bash
java -jar target/YOUR_JAR.jar   --server.port=9090
```

---

# Interview Questions

## What is the Java classpath?

The classpath is the set of directories and JAR files where the JVM searches for classes and resources.

## `.class` file vs JAR?

A `.class` file contains JVM bytecode for a compiled Java type. A JAR is an archive that can contain many classes, resources, and metadata.

## Classpath vs module path?

Classpath is the traditional mechanism for making classes and JARs available. Module path is used by JPMS for named modules and explicit module dependencies.

## What do `requires` and `exports` mean?

```text
requires
→ module dependency

exports
→ package exposed to other modules
```

## Why can code compile but fail with `NoClassDefFoundError`?

The required class may have been available during compilation but missing or unavailable at runtime.

## What is an embedded server?

An embedded server is packaged as an application dependency and started by the application itself. In Spring Boot MVC, Tomcat is commonly embedded.

## What is inside a Spring Boot executable JAR?

```text
BOOT-INF/classes
→ application classes/resources

BOOT-INF/lib
→ dependency JARs

META-INF/MANIFEST.MF
→ metadata

Spring Boot loader
→ starts the application
```

## Why does Spring Boot need a launcher?

Because Boot executable JARs contain nested dependency JARs. The launcher understands the Boot JAR layout, loads the application classes and dependencies, and starts the configured application class.

## `Main-Class` vs `Start-Class`?

```text
Main-Class
→ Spring Boot launcher

Start-Class
→ actual application main class
```

## JAR vs WAR?

A Spring Boot executable JAR usually contains an embedded server and runs directly with `java -jar`. A WAR is traditionally deployed to an external server.

## Do you need Maven to run a Spring Boot JAR?

No. Maven is used to build the application. The packaged JAR runs with a compatible Java runtime.

---

# Concise Manual Notes

```text
DAY 27 — MODULES / CLASSPATH / JAR

PART A

.java
↓ javac
.class
↓ JVM

classpath
= locations JVM searches
for classes/resources

java -cp out package.ClassName

JPMS:
module-info.java

requires
= dependencies

exports
= exposed packages

classpath
→ traditional classes/JARs

module path
→ named modules

unnamed module:
getName() = null
isNamed() = false

JAR
= Java Archive

jar tf file.jar
→ inspect contents

normal JAR
≠ automatically executable

executable JAR:
MANIFEST.MF
Main-Class

java -jar app.jar

ClassNotFoundException
→ requested class not found

NoClassDefFoundError
→ needed class unavailable
  at runtime


PART B

Traditional:
WAR
→ external Tomcat/WebLogic

Spring Boot:
app
+
embedded server
→ executable JAR

mvn clean package
→ target/app.jar

BOOT-INF/classes
→ my code/resources

BOOT-INF/lib
→ dependencies

Main-Class
→ Boot launcher

Start-Class
→ my application

java -jar app.jar
↓
manifest
↓
launcher
↓
BOOT-INF/classes
↓
BOOT-INF/lib
↓
Start-Class
↓
Spring Boot
↓
embedded Tomcat

Maven
= build time

JVM
= runtime

same JAR
+
different config

--server.port=9090

JAR
→ common Boot packaging

WAR
→ external server deployment
```

---

# Day 27 Completion Checklist

## Part A

- [ ] Explain `.java` vs `.class`
- [ ] Explain classpath
- [ ] Run with `java -cp`
- [ ] Explain package / fully-qualified class name
- [ ] Run module/classpath mini-lab
- [ ] Explain unnamed module
- [ ] Explain `module-info.java`
- [ ] Explain `requires`
- [ ] Explain `exports`
- [ ] Explain classpath vs module path
- [ ] Explain JAR structure
- [ ] Build simple JAR
- [ ] Run JAR through classpath
- [ ] Build executable JAR
- [ ] Explain `MANIFEST.MF`
- [ ] Break with wrong classpath
- [ ] Break with wrong class name
- [ ] Break non-executable JAR with `java -jar`

## Part B

- [ ] Package Spring Boot with Maven
- [ ] Inspect `target`
- [ ] Inspect `BOOT-INF/classes`
- [ ] Inspect `BOOT-INF/lib`
- [ ] Find embedded Tomcat JARs
- [ ] Find Jackson JARs
- [ ] Inspect `MANIFEST.MF`
- [ ] Explain `Main-Class`
- [ ] Explain `Start-Class`
- [ ] Explain Spring Boot launcher
- [ ] Run with `java -jar`
- [ ] Run on different port
- [ ] Trigger port conflict intentionally
- [ ] Explain executable JAR vs WAR
- [ ] Explain Maven build-time vs JVM runtime

---

# Final Mental Model

```text
Java:

.java
↓
.class
↓
directory/JAR
↓
classpath/module path
↓
JVM
```

Spring Boot:

```text
source
↓
Maven
↓
target/classes
↓
Spring Boot repackaging
↓
executable JAR
```

Inside:

```text
BOOT-INF/classes
→ application

BOOT-INF/lib
→ dependencies

Boot launcher
→ runtime loader
```

Execution:

```text
java -jar app.jar
↓
MANIFEST
↓
Boot launcher
↓
Start-Class
↓
Spring Boot
↓
embedded Tomcat
```
