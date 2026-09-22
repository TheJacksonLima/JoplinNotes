# [Day 11] Java Reflection

## Objective

Understand Java Reflection at a practical interview-ready level:

- What reflection is
- What `Class<?>` represents
- How to obtain a `Class` object
- How to inspect methods and fields
- How runtime annotations are discovered
- How reflection connects to Day 10 annotations
- Why frameworks such as Spring use reflection
- Main tradeoffs and limitations

---

# 1. What Is Reflection?

Reflection is Java's ability to inspect information about classes and their members at runtime.

With reflection, code can inspect:

- class names
- methods
- fields
- constructors
- annotations
- modifiers
- parameter types
- return types

Mental model:

```text
Normal Java
→ use an object

Reflection
→ inspect the type/object itself at runtime
```

Example:

```java
Class<?> clazz = PropertyService.class;
```

---

# 2. `Class<?>`

Every Java type has a runtime representation through:

```java
java.lang.Class
```

Example:

```java
Class<PropertyService> clazz =
    PropertyService.class;
```

Useful methods:

```java
clazz.getName();
clazz.getSimpleName();
```

Example:

```text
getName()
→ org.jfl.Day11.PropertyService

getSimpleName()
→ PropertyService
```

---

# 3. Three Common Ways to Obtain a `Class`

## Using `.class`

Use when the type is known directly:

```java
Class<PropertyService> clazz =
    PropertyService.class;
```

## Using `getClass()`

Use from an existing object:

```java
PropertyService service =
    new PropertyService();

Class<?> clazz =
    service.getClass();
```

## Using `Class.forName()`

Use when the class name is known dynamically:

```java
Class<?> clazz =
    Class.forName(
        "org.jfl.Day11.PropertyService"
    );
```

Mental model:

```text
PropertyService.class
→ type known directly

object.getClass()
→ runtime type of an existing object

Class.forName(...)
→ resolve a class dynamically by name
```

---

# 4. Inspecting Methods

Example class:

```java
public class PropertyService {

    public void createProperty() {
    }

    public void deleteProperty(Long id) {
    }
}
```

Reflection:

```java
Class<PropertyService> clazz =
    PropertyService.class;

for (Method method :
        clazz.getDeclaredMethods()) {

    System.out.println(
        method.getName()
    );
}
```

Import:

```java
import java.lang.reflect.Method;
```

Possible output:

```text
createProperty
deleteProperty
```

---

# 5. `getMethods()` vs `getDeclaredMethods()`

```java
clazz.getMethods();
```

Returns:

```text
public methods
+
inherited public methods
```

```java
clazz.getDeclaredMethods();
```

Returns:

```text
methods declared directly in the class
+
private/protected/package/public methods
```

Mental shortcut:

```text
getMethods()
→ public + inherited

getDeclaredMethods()
→ declared here
```

---

# 6. Inspecting Method Metadata

Reflection can inspect:

- method name
- parameter count
- parameter types
- return type
- annotations
- modifiers
- declared exceptions

Example:

```java
for (Method method :
        clazz.getDeclaredMethods()) {

    System.out.println(
        method.getName()
        + " parameters: "
        + method.getParameterCount()
    );
}
```

---

# 7. Inspecting Fields

Example:

```java
public class Property {

    private Long id;
    private String title;
    private double price;
}
```

Reflection:

```java
Class<Property> clazz =
    Property.class;

for (Field field :
        clazz.getDeclaredFields()) {

    System.out.println(
        field.getName()
        + " : "
        + field.getType().getSimpleName()
    );
}
```

Import:

```java
import java.lang.reflect.Field;
```

Possible output:

```text
id : Long
title : String
price : double
```

---

# 8. Private Fields and Reflection

Private members can still appear in metadata returned by:

```java
getDeclaredFields()
getDeclaredMethods()
```

But inspecting metadata and accessing private values are different things.

Example:

```java
Field field =
    Property.class
        .getDeclaredField("title");
```

Older/common examples may use:

```java
field.setAccessible(true);
```

then:

```java
Object value = field.get(property);
```

Important:

Reflection can weaken normal encapsulation and modern Java module boundaries may restrict reflective access.

Use it carefully.

---

# 9. Connecting Reflection to Day 10 Annotations

Day 10:

```text
Annotation
→ metadata
```

Day 11:

```text
Reflection
→ inspect runtime metadata
```

Example annotation:

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

    String action();

    boolean enabled() default true;
}
```

Example usage:

```java
public class PropertyService {

    @Audit(action = "CREATE_PROPERTY")
    public void createProperty() {
    }

    @Audit(
        action = "DELETE_PROPERTY",
        enabled = false
    )
    public void deleteProperty() {
    }
}
```

---

# 10. Reading Runtime Annotations

Because the annotation uses:

```java
@Retention(RetentionPolicy.RUNTIME)
```

it can be discovered through reflection.

Example:

```java
Class<PropertyService> clazz =
    PropertyService.class;

for (Method method :
        clazz.getDeclaredMethods()) {

    if (method.isAnnotationPresent(
            Audit.class)) {

        Audit audit =
            method.getAnnotation(
                Audit.class
            );

        System.out.println(
            method.getName()
            + " → "
            + audit.action()
        );
    }
}
```

Possible output:

```text
createProperty → CREATE_PROPERTY
deleteProperty → DELETE_PROPERTY
```

---

# 11. Reading Annotation Attributes

Example:

```java
System.out.println(
    "Action: " + audit.action()
);

System.out.println(
    "Enabled: " + audit.enabled()
);
```

Possible output:

```text
Action: CREATE_PROPERTY
Enabled: true

Action: DELETE_PROPERTY
Enabled: false
```

Important connection:

```text
@Retention(RUNTIME)
        ↓
annotation survives compilation
        ↓
Reflection can inspect it
```

---

# 12. The Annotation Still Does Not Execute Behavior

Example:

```java
@Audit(action = "CREATE_PROPERTY")
```

does not automatically log anything.

The flow is:

```text
@Audit
   ↓
metadata

Reflection
   ↓
reads metadata

application/framework code
   ↓
acts on metadata
```

Example:

```java
if (audit.enabled()) {

    System.out.println(
        "AUDIT: " + audit.action()
    );
}
```

---

# 13. Invoking Methods with Reflection

Reflection can also execute methods dynamically.

Normal call:

```java
service.createProperty();
```

Reflection:

```java
PropertyService service =
    new PropertyService();

Method method =
    PropertyService.class
        .getDeclaredMethod(
            "createProperty"
        );

method.invoke(service);
```

This allows frameworks to invoke methods without having hard-coded compile-time references to every application class.

---

# 14. Why Frameworks Use Reflection

Frameworks such as Spring cannot know all application classes in advance.

Conceptually, Spring can:

```text
scan classes
    ↓
inspect annotations
    ↓
discover components
    ↓
inspect constructors
    ↓
create objects
    ↓
inject dependencies
```

For example:

```java
@Service
public class PropertyService {
}
```

Conceptually:

```text
Spring infrastructure
      ↓
inspect class metadata
      ↓
recognize @Service
      ↓
register bean definition
      ↓
create/manage bean
```

---

# 15. Common Reflection Use Cases

Reflection is commonly used by:

```text
Spring
Hibernate
Jackson
JUnit
dependency injection containers
ORMs
serialization libraries
testing frameworks
plugin systems
```

---

# 16. Tradeoffs

## Reduced compile-time safety

Normal Java:

```java
service.createProperty();
```

Compiler verifies the method.

Reflection:

```java
clazz.getMethod("creatProperty");
```

A typo may fail only at runtime.

## Runtime exceptions

Common examples:

```text
ClassNotFoundException
NoSuchMethodException
NoSuchFieldException
IllegalAccessException
InvocationTargetException
```

## Readability

Normal code:

```java
service.createProperty();
```

is clearer than:

```java
method.invoke(service);
```

Reflection should be used when dynamic behavior is actually needed.

## Encapsulation

Reflection can inspect or potentially access private members, which can make code more fragile.

## Performance

Reflection may add runtime overhead compared with direct calls.

Better interview wording:

> Reflection can have additional runtime overhead and reduced compile-time safety compared with direct calls, so it should be used when dynamic behavior justifies it.

Avoid saying:

```text
"Reflection is always slow."
```

---

# 17. Reflection and Generics

Because of type erasure:

```java
List<String>
List<Integer>
```

normally have the same runtime class.

Example:

```java
System.out.println(
    new ArrayList<String>().getClass()
        ==
    new ArrayList<Integer>().getClass()
);
```

Result:

```text
true
```

Connection:

```text
Generics
→ mostly compile-time typing

Reflection
→ runtime metadata
```

---

# 18. Practical Exercise

Reuse the Day 10 `Audit` concept.

## `Audit.java`

```java
package org.jfl.Day11_Reflection;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

    String action();

    boolean enabled() default true;
}
```

## `PropertyService.java`

```java
package org.jfl.Day11_Reflection;

public class PropertyService {

    @Audit(action = "CREATE_PROPERTY")
    public void createProperty() {
        System.out.println("Property created");
    }

    @Audit(
        action = "DELETE_PROPERTY",
        enabled = false
    )
    public void deleteProperty() {
        System.out.println("Property deleted");
    }

    public void listProperties() {
        System.out.println("Listing properties");
    }
}
```

## `Main.java`

Implement:

```java
package org.jfl.Day11_Reflection;

import java.lang.reflect.Method;

public class Main {

    public static void main(String[] args) {

        Class<PropertyService> clazz =
            PropertyService.class;

        // TODO:
        // iterate through methods
        // detect @Audit
        // retrieve annotation
        // print action + enabled

    }
}
```

Expected output:

```text
createProperty
action = CREATE_PROPERTY
enabled = true

deleteProperty
action = DELETE_PROPERTY
enabled = false
```

`listProperties()` should not appear because it does not have `@Audit`.

---

# 19. Interview Questions

1. What is reflection in Java?
2. What does `Class<?>` represent?
3. What is the difference between `.class`, `getClass()` and `Class.forName()`?
4. What is the difference between `getMethods()` and `getDeclaredMethods()`?
5. How do you inspect annotations at runtime?
6. Why must an annotation use `RetentionPolicy.RUNTIME` to be found through reflection?
7. Why do frameworks such as Spring use reflection?
8. What are the main disadvantages of reflection?
9. Can reflection inspect private members?
10. How does type erasure affect what reflection sees for generic types?

---

# 20. Senior Interview Answer

> Reflection is Java's ability to inspect and interact with classes, methods, fields, constructors, and runtime annotations dynamically. Frameworks such as Spring, Hibernate, and Jackson use it because they cannot know every application type ahead of time. Reflection enables dynamic discovery and invocation, but it comes with tradeoffs such as reduced compile-time safety, more runtime exceptions, additional complexity, and possible runtime overhead.

---

# Quick Mental Model

```text
Reflection
→ inspect runtime type information
```

```text
Class<?>
→ runtime representation of a type
```

```text
getDeclaredMethods()
→ methods declared in this class
```

```text
@Retention(RUNTIME)
→ annotation visible to reflection
```

```text
Annotation
→ metadata

Reflection
→ reads metadata

Framework
→ acts on metadata
```

---

# Completion Checklist

- [x] Understand reflection concept
- [x] Understand `Class<?>`
- [x] Understand `.class`, `getClass()`, `Class.forName()`
- [x] Inspect methods
- [x] Inspect fields
- [x] Understand `getMethods()` vs `getDeclaredMethods()`
- [x] Read runtime annotations
- [x] Connect Reflection to Day 10 annotations
- [x] Understand why frameworks use reflection
- [x] Understand main tradeoffs
- [ ] Complete practical `Main` implementation
- [ ] Interview round
- [ ] Day 11 Spring track: `@Value` and `@ConfigurationProperties`

**Current Day 11 Java status: theory complete; practical implementation and interview review pending.**