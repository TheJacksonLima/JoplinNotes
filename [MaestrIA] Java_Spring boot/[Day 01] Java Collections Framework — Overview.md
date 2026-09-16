---
title: '[Day 01] Java Collections Framework — Overview'
updated: 2026-08-31 17:56:22Z
created: 2026-08-26 15:05:41Z
latitude: -25.42016050
longitude: -49.27616110
altitude: 0.0000
---

# Java Collections Framework — Overview
# Day 1 — Java Collections Framework

## 1. Java Collections Framework

The **Java Collections Framework (JCF)** provides interfaces and implementations for storing, accessing, and manipulating groups of objects.

The main structure is:

```text
Iterable
   │
Collection
   ├── List
   │    ├── ArrayList
   │    └── LinkedList
   │
   ├── Set
   │    ├── HashSet
   │    ├── LinkedHashSet
   │    └── TreeSet
   │
   └── Queue
        ├── PriorityQueue
        └── Deque
             └── ArrayDeque

Map  ← does NOT extend Collection
 ├── HashMap
 ├── LinkedHashMap
 └── TreeMap
```

### Important distinction

`Map` belongs to the Java Collections Framework, but it **does not extend `Collection`**.

The reason is conceptual:

* `Collection<E>` represents a group of individual elements.
* `Map<K,V>` represents associations between **keys and values**.

Another common interview question is the difference between `Collection` and `Collections`:

* `Collection` is an **interface**.
* `Collections` is a **utility class** containing static methods such as `sort()`, `reverse()`, `shuffle()`, and `min()`.

---

# 2. List

A `List` represents an **ordered collection**.

Use a `List` when:

* order matters;
* duplicates are allowed;
* index-based access may be required.

Example:

```java
List<String> names = new ArrayList<>();

names.add("John");
names.add("Mary");
names.add("John");

System.out.println(names.get(0));
```

Output:

```text
John
```

The duplicate `"John"` is valid.

The two most common implementations are:

* `ArrayList`
* `LinkedList`

---

# 3. ArrayList

An `ArrayList` is implemented using a **dynamically resizable array**.

Conceptually:

```text
[A][B][C][D][ ][ ]
```

Because the elements are stored using an array, accessing an element by index is very efficient:

```java
list.get(500);
```

The position can be calculated directly.

Therefore:

```text
get(index) → O(1)
```

### Insertion and removal

Adding an element to the end is normally:

```text
O(1) amortized
```

However, inserting or removing an element from the beginning or middle may require shifting elements.

Example:

```text
BEFORE

[A][B][C][D]

remove(0)

[B][C][D][ ]
 ←  ←  ←
```

Therefore:

```text
insert/remove middle → O(n)
```

### ArrayList complexity

| Operation            |     Complexity |
| -------------------- | -------------: |
| `get(index)`         |           O(1) |
| `set(index)`         |           O(1) |
| `add()` at end       | O(1) amortized |
| `contains()`         |           O(n) |
| search               |           O(n) |
| insert/remove middle |           O(n) |

### What does amortized O(1) mean?

Most calls to:

```java
list.add(value);
```

are O(1).

Eventually, the internal array becomes full. `ArrayList` then needs to allocate a larger array and copy the existing elements.

That particular operation is O(n), but it happens only occasionally.

Across many insertions, the average cost per insertion is considered:

> **Amortized O(1)**

---

# 4. LinkedList

`LinkedList` is implemented as a **doubly linked list**.

Conceptually:

```text
null ← [A] ⇄ [B] ⇄ [C] ⇄ [D] → null
```

Each node stores:

```text
previous ← [ value ] → next
```

Unlike `ArrayList`, elements are not accessed directly using an array index.

For example:

```java
list.get(500);
```

requires traversing nodes until the requested position is reached.

Therefore:

```text
get(index) → O(n)
```

### Important interview trap

A common statement is:

> "LinkedList is faster than ArrayList for insertion and deletion."

This is incomplete.

If you **already have the relevant node**, connecting or disconnecting nodes can be O(1).

But if you first need to find position `500`, locating that position is O(n).

`ArrayList` also generally has better memory locality and lower memory overhead.

Therefore, as a practical default:

> **Prefer ArrayList unless you have a specific reason to use LinkedList.**

### ArrayList vs LinkedList

| Operation       | ArrayList | LinkedList |
| --------------- | --------: | ---------: |
| `get(index)`    |      O(1) |       O(n) |
| search          |      O(n) |       O(n) |
| add at end      |     O(1)* |       O(1) |
| remove by index |      O(n) |     O(n)** |
| memory overhead |     Lower |     Higher |

* amortized
** locating the node may require traversal

---

# 5. Set

A `Set` represents a collection of **unique elements**.

Example:

```java
Set<String> technologies = new HashSet<>();

technologies.add("Java");
technologies.add("React");
technologies.add("Java");
```

The set contains only:

```text
Java
React
```

Use a `Set` when:

* uniqueness matters;
* you need to know whether an element already exists;
* duplicates should not be stored.

Typical questions that suggest using a Set are:

> "Have I already seen this value?"

> "Does this value already exist?"

> "Are there any duplicates?"

---

# 6. HashSet

`HashSet` uses **hashing** to efficiently store and locate elements.

Average complexity:

| Operation    | Complexity |
| ------------ | ---------: |
| `add()`      |       O(1) |
| `contains()` |       O(1) |
| `remove()`   |       O(1) |

Example:

```java
Set<Integer> numbers = new HashSet<>();

numbers.add(10);
numbers.add(20);

if (numbers.contains(10)) {
    System.out.println("Found");
}
```

A useful detail is that:

```java
set.add(value);
```

returns `false` if the element is already present.

This allows code such as:

```java
boolean containsDuplicate(int[] nums) {
    Set<Integer> seen = new HashSet<>();

    for (int num : nums) {
        if (!seen.add(num)) {
            return true;
        }
    }

    return false;
}
```

The deeper relationship between `HashSet`, hashing, `hashCode()` and `equals()` is a separate topic we will cover later this week.

---

# 7. Map

A `Map` stores **key-value associations**:

```text
KEY → VALUE
```

Example:

```java
Map<String, Integer> ages = new HashMap<>();

ages.put("John", 30);
ages.put("Mary", 25);
```

Conceptually:

```text
John → 30
Mary → 25
```

Keys are unique.

Values do not have to be unique.

If you execute:

```java
ages.put("John", 31);
```

you don't create another `"John"` entry.

The existing value is replaced:

```text
John → 31
Mary → 25
```

---

# 8. HashMap

`HashMap` is one of the most important data structures for both real Java development and coding interviews.

Average complexity:

| Operation       | Complexity |
| --------------- | ---------: |
| `put()`         |       O(1) |
| `get()`         |       O(1) |
| `remove()`      |       O(1) |
| `containsKey()` |       O(1) |

For example:

```java
Map<Long, User> usersById = new HashMap<>();

usersById.put(user.getId(), user);

User user = usersById.get(123L);
```

The lookup is **O(1) on average**.

### Why is this important?

Suppose we have:

```java
List<User> users;
```

Finding a particular user may require:

```java
for (User user : users) {
    if (user.getId().equals(id)) {
        return user;
    }
}
```

Worst case:

```text
O(n)
```

With:

```java
Map<Long, User>
```

we can use:

```java
usersById.get(id);
```

Average case:

```text
O(1)
```

Recognizing opportunities like this is extremely important in coding interviews.

We will study **HashMap internals** separately:

```text
hashCode
   ↓
hash
   ↓
bucket
   ↓
collision
   ↓
equals
```

---

# 9. Queue

A `Queue` commonly represents **FIFO**:

> **First In, First Out**

Conceptually:

```text
IN → [A][B][C][D] → OUT
```

Example:

```java
Queue<String> queue = new ArrayDeque<>();

queue.offer("A");
queue.offer("B");
queue.offer("C");

System.out.println(queue.poll());
```

Output:

```text
A
```

Important operations:

```java
offer() // insert
poll()  // retrieve and remove
peek()  // retrieve without removing
```

Queues are commonly used for:

* task processing;
* messaging;
* scheduling;
* buffering;
* Breadth-First Search (BFS).

---

# 10. Deque

`Deque` means:

> **Double-Ended Queue**

Elements can be inserted and removed from both ends.

```text
← [A][B][C] →
```

A `Deque` can therefore behave as either:

* Queue — FIFO
* Stack — LIFO

Example using it as a stack:

```java
Deque<Integer> stack = new ArrayDeque<>();

stack.push(10);
stack.push(20);
stack.push(30);

System.out.println(stack.pop());
```

Output:

```text
30
```

This follows:

> **LIFO — Last In, First Out**

For modern Java code, `ArrayDeque` is generally preferred over the legacy `Stack` class.

---

# 11. Choosing the Correct Data Structure

The most important skill is not memorizing every implementation.

You should be able to look at a problem and select an appropriate data structure.

A simple decision tree:

```text
Do I need KEY → VALUE?
        │
       YES
        ↓
       Map


Do I only need values?
        │
        ↓
Are duplicates allowed?
        │
   ┌────┴────┐
  YES        NO
   ↓          ↓
 List        Set


Do I need processing order?
        │
        ↓
      Queue
```

Examples:

### Transaction history

```java
List<Transaction>
```

Why?

Because order matters and multiple transactions can contain similar data.

### Unique email addresses

```java
Set<String>
```

Why?

Because duplicate emails should not exist.

### User ID → User

```java
Map<Long, User>
```

Why?

Because the ID naturally acts as a key and provides efficient lookup.

### Tasks waiting for processing

```java
Queue<Task>
```

Why?

Because tasks are generally processed in arrival order.

---

# 12. Complexity Cheat Sheet

These are the main complexities worth remembering at this stage:

| Structure     |           Access/Search |      Add |   Remove |
| ------------- | ----------------------: | -------: | -------: |
| ArrayList     | index O(1), search O(n) |    O(1)* |     O(n) |
| LinkedList    |                    O(n) |   O(1)** |   O(1)** |
| HashSet       |                O(1) avg | O(1) avg | O(1) avg |
| HashMap       |                O(1) avg | O(1) avg | O(1) avg |
| TreeSet       |                O(log n) | O(log n) | O(log n) |
| TreeMap       |                O(log n) | O(log n) | O(log n) |
| PriorityQueue |                       — | O(log n) | O(log n) |

* amortized at the end
** when the relevant node is already known; finding it may be O(n)

---

# 13. Key Interview Concepts

For Day 1, you should be able to answer these questions:

### Collections Framework

**What is the Java Collections Framework?**

A set of interfaces, implementations, and algorithms for storing and manipulating groups of objects.

### Collection vs Collections

```text
Collection  → interface
Collections → utility class
```

### Does Map extend Collection?

No.

`Collection` represents individual elements, while `Map` represents key-value associations.

### List vs Set vs Map

```text
List → ordered, duplicates allowed

Set → unique elements

Map → key-value associations
```

### ArrayList vs LinkedList

```text
ArrayList
→ dynamic array
→ O(1) random access
→ usually the default List implementation

LinkedList
→ doubly linked list
→ O(n) random access
→ efficient node insertion/removal when the node is already known
```

### When should you use a Set?

When uniqueness or fast membership checking matters.

### When should you use a Map?

When data naturally has a key-value relationship or efficient lookup by key is required.

---

# 14. Day 1 Practice

Create small examples using:

```java
List<String>
Set<String>
Map<String, Integer>
Queue<String>
Deque<String>
```

Then choose the best data structure for each scenario:

```text
1. Transaction history
2. Unique email addresses
3. User ID → User
4. Processing queue
5. Recently visited pages
```

For each one, don't just implement it.

Explain:

> **Why did I choose this data structure?**

That explanation is what turns basic Collections knowledge into **interview-ready knowledge**.

## Day 1 Success Criteria

By the end of today's Java study, you should confidently explain:

```text
Collections Framework
       │
       ├── List
       │    └── ArrayList vs LinkedList
       │
       ├── Set
       │    └── HashSet
       │
       ├── Queue / Deque
       │
       └── Map
            └── HashMap

+ basic Big-O
+ choosing the correct structure
```

**Not required today:** deep `HashMap` internals, buckets, collisions, treeification, and the `equals()`/`hashCode()` contract. Those deserve their own focused session later this week.
