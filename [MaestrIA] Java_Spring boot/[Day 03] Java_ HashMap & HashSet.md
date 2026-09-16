---
title: '[Day 03] Java: HashMap & HashSet'
updated: 2026-09-03 13:25:58Z
created: 2026-09-02 15:36:49Z
latitude: -25.42016050
longitude: -49.27616110
altitude: 0.0000
---

### \[Day 03\] Java: HashMap & HashSet

**1\. HashMap fundamentals**

- Key/value structure
    
- `put()`, `get()`, `remove()`, `containsKey()`
    
- Average `O(1)` operations
    
- Why keys should generally be immutable
    

**2\. HashMap internals — main focus**

- `hashCode()`
    
- Hash calculation
    
- Buckets
    
- Bucket/index selection
    
- Collisions
    
- How `equals()` is used
    
- Replacing an existing value vs a real collision
    
- Java 8+ treeification / red-black trees
    
- Resizing, capacity and load factor
    

Mental model:

```text
key
 ↓
hashCode()
 ↓
hash
 ↓
bucket
 ↓
compare keys
 ↓
equals()
```

**3\. `equals()` / `hashCode()` contract**

The important rules:

```text
a.equals(b) == true
        ↓
a.hashCode() == b.hashCode()
```

But:

```text
same hashCode
     ✗
does NOT imply equals()
```

And this is exactly the part you just answered correctly in the mock interview.

**4\. HashSet internals**

- Uniqueness
    
- `add()`, `contains()`, `remove()`
    
- Average `O(1)`
    
- Internally backed by a `HashMap`
    
- Why `HashSet` also depends on `equals()` + `hashCode()`
    

**5\. Complexity**

You should finish today comfortable with:

| Operation | Average | Worst case |
| --- | ---: | ---: |
| `HashMap.get()` | O(1) | O(log n)\* |
| `HashMap.put()` | O(1) | O(log n)\* |
| `HashSet.contains()` | O(1) | O(log n)\* |
| `HashSet.add()` | O(1) | O(log n)\* |

\* For treeified collision buckets in modern Java; there are implementation details/edge cases we'll discuss rather than memorizing this blindly.

**6\. Coding practice**

Today we'll do **2 HackerRank-style exercises**:

```text
Problem 1 → HashSet
Problem 2 → HashMap / frequency counting
```

The objective isn't just solving them. After each one I'll ask you:

> Why did you choose this data structure?  
> What's the time complexity?  
> What's the space complexity?

### Today's target

By the end of Day 3, you should be able to confidently answer this interview question:

> **"Explain how HashMap works internally in Java, including hashCode, equals, collisions and time complexity."**

We already started that with `map.put("Java", 10)`. I suggest we continue from there and go deeper into **what a bucket actually contains and what happens during a collision**.