# [Day 13] Maven, Dependency Management & Spring Boot Autoconfiguration

## Objective

Review the key concepts behind:

### Java / Build Tools
- Maven lifecycle
- Maven phases vs plugin goals
- Dependency scopes
- Transitive dependencies
- Dependency management
- Dependency tree inspection
- Gradle and Bazel recognition

### Spring Boot
- Spring Boot Starters
- Autoconfiguration
- Conditional configuration
- `@ConditionalOnClass`
- `@ConditionalOnMissingBean`
- Spring Boot "backing off"
- Autoconfiguration diagnostics

---

# 1. Maven

Maven is primarily:

```text
Build tool
+
Dependency management tool
+
Project convention system
```

A Maven project is described using:

```text
pom.xml
```

POM:

```text
Project Object Model
```

Example coordinates:

```xml
<groupId>org.jfl</groupId>
<artifactId>day13_maven_autoconfiguration</artifactId>
<version>1.0-SNAPSHOT</version>
```

Mental model:

```text
groupId
→ organization / namespace

artifactId
→ project or module

version
→ artifact version
```

Together they identify an artifact:

```text
org.jfl:day13_maven_autoconfiguration:1.0-SNAPSHOT
```

---

# 2. Maven Project Convention

Typical Maven layout:

```text
src/
├── main/
│   ├── java/
│   └── resources/
└── test/
    ├── java/
    └── resources/
```

Maven follows convention over configuration.

It already knows where to find:

```text
production code
tests
resources
```

---

# 3. Maven Lifecycle

Important default lifecycle phases:

```text
validate
   ↓
compile
   ↓
test
   ↓
package
   ↓
verify
   ↓
install
   ↓
deploy
```

---

# 4. `mvn compile`

```bash
mvn compile
```

Compiles:

```text
src/main/java
```

Typically into:

```text
target/classes
```

---

# 5. `mvn test`

```bash
mvn test
```

Compiles and runs the tests.

Previous required lifecycle phases are executed automatically.

---

# 6. `mvn package`

```bash
mvn package
```

Runs the lifecycle through `package`.

Conceptually:

```text
compile
↓
test
↓
package
```

Produces an artifact such as:

```text
target/application.jar
```

Important:

> Calling a Maven phase also executes the previous phases in that lifecycle.

---

# 7. `mvn install`

```bash
mvn install
```

Builds the artifact and installs it into the local Maven repository.

Typical location:

```text
~/.m2/repository
```

Mental model:

```text
package
→ build artifact

install
→ build artifact + store locally
```

Other projects on the same machine can then resolve that artifact.

---

# 8. `mvn deploy`

```bash
mvn deploy
```

Publishes the artifact to a remote Maven repository.

Examples:

```text
Nexus
Artifactory
GitHub Packages
corporate Maven repository
```

Important:

```text
Maven deploy
!=
deploy application to AWS/Kubernetes
```

Maven deploy means:

```text
publish artifact to artifact repository
```

---

# 9. Clean Lifecycle

Common command:

```bash
mvn clean package
```

`clean` removes previous build output:

```text
target/
```

Then Maven runs the default lifecycle through `package`.

---

# 10. Maven Phase vs Goal

A phase is part of a lifecycle:

```text
compile
test
package
install
```

A goal is a specific action implemented by a plugin.

Example:

```bash
mvn compiler:compile
```

Mental model:

```text
Lifecycle
   ↓
Phase
   ↓
Plugin Goal
```

Normally developers use lifecycle phases:

```bash
mvn package
```

and Maven invokes the appropriate plugin goals.

---

# 11. Maven Dependencies

Example:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>
```

Maven resolves dependencies using repositories.

Conceptually:

```text
local repository
↓
remote repository if necessary
```

---

# 12. Dependency Scopes

Important scopes:

```text
compile
provided
runtime
test
```

## `compile`

Default scope.

Available during:

```text
compile
test
runtime
```

Typical application dependency.

## `test`

Example:

```xml
<scope>test</scope>
```

Available only to testing code.

Examples:

```text
JUnit
Mockito
Spring Boot Test
```

Not part of the normal production classpath.

## `runtime`

Needed while the application runs, but not necessarily to compile application code.

Typical example:

```text
database driver
```

Application code may compile against:

```java
java.sql.Connection
```

while the vendor implementation is only required at runtime.

## `provided`

Available at compile time but expected to be supplied by the runtime environment.

Classic example:

```text
Servlet API
```

when an external servlet container provides it.

---

# 13. Scope Mental Model

```text
compile
→ normal application dependency

test
→ tests only

runtime
→ required at runtime

provided
→ runtime environment provides it
```

---

# 14. Transitive Dependencies

Suppose:

```text
Application
   ↓
Library A
   ↓
Library B
```

Your application directly declares:

```text
Library A
```

but Maven can automatically bring:

```text
Library B
```

Library B is a:

```text
transitive dependency
```

---

# 15. Spring Boot Starter Example

You declare:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

The starter brings related libraries transitively.

Conceptually:

```text
spring-boot-starter-web
        ↓
Spring MVC
JSON support
logging
web server dependencies
other web infrastructure
```

You do not manually declare every library.

---

# 16. Inspecting Dependencies

Important command:

```bash
mvn dependency:tree
```

Useful for investigating:

```text
transitive dependencies
dependency conflicts
unexpected versions
duplicate libraries
```

Conceptual output:

```text
application
└── spring-boot-starter-web
    ├── spring-web
    ├── spring-webmvc
    └── ...
```

---

# 17. Dependency Conflicts

Example:

```text
Application
├── Library A
│   └── Library X 1.0
│
└── Library B
    └── Library X 2.0
```

Maven must choose a version.

Important concept:

```text
nearest dependency wins
```

Dependency mediation determines the selected version.

Do not rely blindly on mediation when version compatibility matters.

---

# 18. Excluding a Transitive Dependency

Example:

```xml
<dependency>
    <groupId>example</groupId>
    <artifactId>library-a</artifactId>

    <exclusions>
        <exclusion>
            <groupId>example</groupId>
            <artifactId>library-x</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

Use exclusions deliberately.

Common reasons:

```text
version conflict
replacement implementation
unwanted library
security issue
```

---

# 19. `dependencyManagement`

Example:

```xml
<dependencyManagement>
    <dependencies>

        <dependency>
            <groupId>example</groupId>
            <artifactId>library-x</artifactId>
            <version>2.0</version>
        </dependency>

    </dependencies>
</dependencyManagement>
```

Important:

> `dependencyManagement` does not automatically add a dependency.

It manages how a dependency should be resolved if it is used.

Then:

```xml
<dependency>
    <groupId>example</groupId>
    <artifactId>library-x</artifactId>
</dependency>
```

can inherit the managed version.

Mental model:

```text
dependencies
→ add/use dependency

dependencyManagement
→ manage dependency versions/configuration
```

---

# 20. Spring Boot Dependency Management

Spring Boot provides managed versions for compatible libraries.

Using the Spring Boot parent:

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>...</version>
</parent>
```

allows many dependencies to omit explicit versions.

Boot manages compatible versions for libraries such as:

```text
Spring Framework
Jackson
logging libraries
embedded server
testing libraries
```

Benefit:

```text
tested dependency combinations
+
less manual version management
```

---

# 21. Gradle

Gradle is another build system.

Common files:

```text
build.gradle
build.gradle.kts
```

Uses:

```text
Groovy DSL
or
Kotlin DSL
```

Typical commands:

```bash
./gradlew build
./gradlew test
```

Recognition-level comparison:

```text
Maven
→ XML
→ lifecycle-oriented

Gradle
→ DSL
→ task-oriented
```

No deep Gradle study required for this roadmap stage.

---

# 22. Bazel

Bazel is another build system.

Often used with:

```text
large repositories
monorepos
reproducible builds
explicit dependencies
build caching
```

Typical files:

```text
BUILD
BUILD.bazel
MODULE.bazel
WORKSPACE
```

Recognition only for this roadmap.

---

# 23. Spring Boot Starters

A Spring Boot Starter is primarily a convenient dependency descriptor.

Example:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

Mental model:

```text
Starter
→ curated dependency bundle
```

Examples:

```text
spring-boot-starter
spring-boot-starter-web
spring-boot-starter-data-jpa
spring-boot-starter-security
spring-boot-starter-test
```

---

# 24. Starter vs Autoconfiguration

Important distinction:

```text
Starter
→ dependency setup

Autoconfiguration
→ Spring configuration / bean setup
```

They often work together but are not the same thing.

Mental model:

```text
Starter
→ puts libraries on classpath

Autoconfiguration
→ reacts to classpath/environment
→ configures sensible defaults
```

---

# 25. Spring Boot Autoconfiguration

Autoconfiguration attempts to configure the application automatically based on:

```text
classpath
existing beans
application properties
environment
```

Example concept:

```text
Web classes present?
        ↓
yes

Web application detected?
        ↓
yes

No user replacement?
        ↓
configure web infrastructure
```

---

# 26. `@SpringBootApplication`

Typical application:

```java
@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(
            Main.class,
            args
        );
    }
}
```

Conceptually, `@SpringBootApplication` includes functionality related to:

```text
configuration
component scanning
autoconfiguration
```

One important component is:

```java
@EnableAutoConfiguration
```

Usually this annotation is not written separately.

---

# 27. Conditional Autoconfiguration

Spring Boot uses conditional annotations extensively.

Important examples:

```text
@ConditionalOnClass
@ConditionalOnMissingBean
@ConditionalOnBean
@ConditionalOnProperty
```

---

# 28. `@ConditionalOnClass`

Conceptually:

```java
@ConditionalOnClass(SomeLibrary.class)
```

means:

```text
If this class exists on the classpath,
this autoconfiguration may become active.
```

This connects Maven directly to Spring Boot.

```text
Maven dependency
        ↓
class enters classpath
        ↓
Spring condition matches
        ↓
autoconfiguration becomes eligible
```

---

# 29. `@ConditionalOnMissingBean`

Conceptually:

```java
@ConditionalOnMissingBean
```

means:

```text
Create a default bean
only when the application has not supplied one.
```

This is central to Spring Boot's:

```text
back-off behavior
```

---

# 30. Spring Boot Backing Off

Spring Boot often provides defaults only when the application has not explicitly provided its own configuration.

Mental model:

```text
No user bean
→ Boot creates default

User bean exists
→ Boot backs off
```

This provides:

```text
convenient defaults
+
customizability
```

---

# 31. Autoconfiguration Is Not Magic

Weak explanation:

```text
Spring magically configures everything.
```

Better explanation:

> Spring Boot autoconfiguration uses conditional configuration based on the classpath, application properties, environment, and existing beans to register sensible default beans.

---

# 32. Maven + Spring Boot Connection

This is the core concept of Day 13.

```text
pom.xml
   ↓
Maven dependencies
   ↓
direct + transitive dependencies
   ↓
runtime classpath
   ↓
Spring Boot examines classpath
   ↓
conditions match
   ↓
autoconfiguration activates
```

Example:

```text
spring-boot-starter-web
        ↓
web dependencies added
        ↓
web classes appear on classpath
        ↓
web autoconfiguration conditions match
        ↓
Spring Boot configures web infrastructure
```

---

# 33. Autoconfiguration Diagnostics

Autoconfiguration can be inspected.

Example:

```properties
debug=true
```

Spring Boot can then provide a condition evaluation report.

Important concepts:

```text
Positive matches
Negative matches
ConditionalOnClass
ConditionalOnMissingBean
ConditionalOnProperty
```

The important point:

```text
Autoconfiguration
→ conditional
→ inspectable
```

---

# 34. Useful Maven Commands

```bash
mvn clean
```

Removes previous build output.

```bash
mvn compile
```

Compiles main source code.

```bash
mvn test
```

Runs tests.

```bash
mvn package
```

Creates the artifact.

```bash
mvn install
```

Installs artifact into local repository.

```bash
mvn dependency:tree
```

Displays dependency graph.

```bash
mvn help:effective-pom
```

Displays the effective Maven configuration after inheritance and management are applied.

---

# 35. Interview Traps

## Does `dependencyManagement` add dependencies?

No.

```text
dependencyManagement
→ manage version/configuration

dependencies
→ add dependency
```

## Does `mvn package` only execute package?

No.

It executes earlier lifecycle phases as well.

## Are Spring Boot Starters and Autoconfiguration the same thing?

No.

```text
Starter
→ dependencies

Autoconfiguration
→ configuration
```

## Does adding a starter enable every possible configuration?

No.

Autoconfiguration is still conditional.

## Does Spring Boot stop you from providing custom configuration?

No.

Boot commonly backs off when user-defined beans are present.

---

# 36. Senior Interview Answer — Maven

> Maven is a build and dependency-management tool based on conventions and lifecycles. The default lifecycle includes phases such as compile, test, package, install, and deploy. Maven also resolves direct and transitive dependencies and supports dependency scopes and version management through mechanisms such as dependencyManagement.

---

# 37. Senior Interview Answer — Spring Boot Autoconfiguration

> Spring Boot autoconfiguration inspects the classpath, environment, application properties, and existing beans and conditionally creates sensible default configuration. Starters make commonly related dependencies available, while autoconfiguration reacts to those dependencies and application conditions. Boot typically backs off when the application provides its own bean.

---

# Quick Mental Model

```text
Maven
→ build + dependency management
```

```text
compile → test → package → install → deploy
```

```text
dependency
→ directly declared

transitive dependency
→ dependency of a dependency
```

```text
dependencyManagement
→ manage versions
→ does NOT add dependency
```

```text
Starter
→ dependencies
```

```text
Autoconfiguration
→ conditional configuration
```

```text
@ConditionalOnClass
→ classpath condition
```

```text
@ConditionalOnMissingBean
→ Boot default unless user provides one
```

```text
Maven dependency
→ classpath
→ Boot condition
→ autoconfiguration
```

---

# Day 13 Completion Checklist

## Maven

- [x] Review Maven lifecycle
- [x] Understand compile/test/package/install/deploy
- [x] Understand phase vs plugin goal
- [x] Review dependency scopes
- [x] Understand transitive dependencies
- [x] Understand dependency mediation
- [x] Understand exclusions
- [x] Understand `dependencyManagement`
- [x] Know `mvn dependency:tree`
- [x] Recognize Gradle
- [x] Recognize Bazel

## Spring Boot

- [x] Understand Spring Boot Starters
- [x] Distinguish starter from autoconfiguration
- [x] Understand classpath-based autoconfiguration
- [x] Understand `@ConditionalOnClass`
- [x] Understand `@ConditionalOnMissingBean`
- [x] Understand Boot back-off behavior
- [x] Understand the condition evaluation report

## Session Decision

- [x] Theory reviewed
- [x] Topic already familiar from previous experience
- [x] Hands-on implementation intentionally skipped

**Day 13 status: review complete.**