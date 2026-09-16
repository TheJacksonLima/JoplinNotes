---
title: '[Day 05] Java: equals() and hashCode()'
updated: 2026-09-08 15:16:17Z
created: 2026-09-08 15:13:51Z
latitude: -25.42777520
longitude: -49.27306160
altitude: 0.0000
---

## 1. Reference Equality vs Logical Equality

For objects, `==` compares references:

``` java  
Customer c1 = new Customer(1L, "john@test.com");  
Customer c2 = new Customer(1L, "john@test.com");

c1 == c2; // false  
```

They are two different objects in memory.

`equals()` is used for logical equality:

``` java  
c1.equals(c2);  
```

Without overriding `equals()`, the implementation inherited from  
`Object` uses object identity, so two different instances are not  
considered equal.

------------------------------------------------------------------------

## 2. Overriding `equals()`

We override `equals()` to define what logical equality means for our  
domain.

Example:

``` java  
@Override  
public boolean equals(Object o) {  
   if (o == null || getClass() != o.getClass()) return false;

   Customer customer = (Customer) o;

return Objects.equals(id, customer.id)  
        && Objects.equals(email, customer.email);  
}  
```

Here two `Customer` objects are equal when:

- `id` is equal  
- `email` is equal

The fields used by `equals()` are a domain/design decision.

For example, if Customer ID alone represents identity, equality could  
instead be based only on ID.

------------------------------------------------------------------------

## 3. `equals()` Contract

A correct `equals()` implementation must be:

- **Reflexive:** `x.equals(x)` is `true`  
- **Symmetric:** if `x.equals(y)`, then `y.equals(x)`  
- **Transitive:** if `x` equals `y` and `y` equals `z`, then `x`  
    equals `z`  
- **Consistent:** repeated calls should return the same result if  
    objects don't change  
- **Non-null:** `x.equals(null)` is `false`

------------------------------------------------------------------------

## 4. `hashCode()`

`hashCode()` returns an integer used by hash-based collections such as:

- `HashMap`  
- `HashSet`

Simplified HashMap/HashSet lookup:

``` text  
object/key  
    ↓  
hashCode()  
    ↓  
  hash  
    ↓  
 bucket  
    ↓  
equals()  
    ↓  
correct object  
```

`hashCode()` helps find the candidate location.

`equals()` determines logical equality when necessary.

------------------------------------------------------------------------

## 5. `equals()` / `hashCode()` Contract

**Critical rule:**

If:

``` java  
a.equals(b) == true  
```

then:

``` java  
a.hashCode() == b.hashCode()  
```

**must also be true.**

However, the reverse is **not** required:

``` java  
a.hashCode() == b.hashCode()  
```

does not mean:

``` java  
a.equals(b) == true  
```

Two different objects can have the same hash code.

This is called a **hash collision**.

Summary:

``` text  
equals() true  
     ↓  
same hashCode() REQUIRED

same hashCode()  
     ↓  
equals() may be true or false  
```

------------------------------------------------------------------------

## 6. Why Override Both?

If we override `equals()` but not `hashCode()`:

``` java  
c1.equals(c2); // true

c1.hashCode(); // e.g. 123  
c2.hashCode(); // e.g. 987  
```

This violates the contract.

Hash-based collections may behave incorrectly because logically equal  
objects may be searched for using different hashes/buckets.

> **Rule:** When overriding `equals()`, override `hashCode()`  
> consistently as well.

------------------------------------------------------------------------

## 7. `HashSet` Example

Without `equals()` / `hashCode()` overrides:

``` java  
Customer c1 = new Customer(1L, "john@test.com");  
Customer c2 = new Customer(1L, "john@test.com");

Set&lt;Customer&gt; customers = new HashSet<>();

customers.add(c1);  
customers.add(c2);

System.out.println(customers.size()); // 2  
```

They are different object instances and default equality does not  
consider their fields.

With properly implemented `equals()` / `hashCode()`:

``` java  
System.out.println(customers.size()); // 1  
```

Conceptually:

``` text  
add(c2)  
   ↓  
hashCode()  
   ↓  
candidate bucket  
   ↓  
compare with c1  
   ↓  
c1.equals(c2)  
   ↓  
  true  
   ↓  
duplicate → don't add  
```

This connects directly to the fact that `HashSet` is backed by a  
`HashMap`.

------------------------------------------------------------------------

## 8. Hash Collisions

Two different objects may have the same hash:

``` java  
a.hashCode() == b.hashCode(); // true  
a.equals(b); // false  
```

This is valid.

The collection uses equality checks to distinguish objects when  
hashes/buckets collide.

A collision does **not** mean the objects are equal.

------------------------------------------------------------------------

## 9. Mutable `HashMap` Keys

Using mutable objects as `HashMap` keys can be dangerous.

Example:

``` java  
Customer customer = new Customer(1L);

map.put(customer, "data");  
```

Suppose:

``` text  
id = 1  
   ↓  
hash A  
   ↓  
Bucket 3  
```

Now mutate a field used by `hashCode()`:

``` java  
customer.setId(2L);  
```

The new state might produce:

``` text  
id = 2  
   ↓  
hash B  
   ↓  
Bucket 8  
```

But the existing entry was stored according to the **original hash**.

It is not automatically moved.

Therefore:

``` java  
map.get(customer);  
```

may calculate the new hash, search according to it, and fail to find the  
existing entry.

> **Rule:** `HashMap` keys should preferably be immutable with respect  
> to fields used by `equals()` and `hashCode()`.

------------------------------------------------------------------------

## 10. `Objects` Utility Class

Java provides:

``` java  
java.util.Objects  
```

Common methods:

``` java  
Objects.equals(a, b);  
Objects.hash(id, email);  
```

Example:

``` java  
@Override  
public int hashCode() {  
    return Objects.hash(id, email);  
}  
```

------------------------------------------------------------------------

# Interview Cheat Sheet

## `==` vs `equals()`

For objects:

- `==` → reference identity  
- `equals()` → logical equality

## `equals()` / `hashCode()` contract

If two objects are equal according to `equals()`, they **must** have the  
same `hashCode()`.

Same `hashCode()` does **not** imply `equals()`.

## Hash collision

Different objects can generate the same hash code.

The collection uses `equals()` to distinguish them.

## Why override both?

Overriding `equals()` without a consistent `hashCode()` breaks the  
contract and can cause incorrect behavior in `HashMap` / `HashSet`.

## Mutable `HashMap` keys

Changing a field involved in `equals()` / `hashCode()` after insertion  
can change the key's hash. The entry remains stored according to the  
original hash, causing future lookups to fail.

## Mental Model

``` text  
hashCode()  
    ↓  
find candidate bucket  
    ↓  
equals()  
    ↓  
determine logical equality  
```