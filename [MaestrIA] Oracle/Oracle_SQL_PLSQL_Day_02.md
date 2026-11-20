# Oracle SQL + PL/SQL — Day 02

**Status:** ✅ Complete  
**Completed:** 2026-11-16  
**Phase:** P1 — SQL Interview Sharpness  
**Focus:** JOINs, NULLs, Set Logic, and Join Cardinality

---

## 1. INNER JOIN

An `INNER JOIN` returns only rows that satisfy the join condition.

```sql
SELECT
    e.name,
    e.job_title,
    d.name AS department_name,
    d.location
FROM employee e
JOIN department d
    ON d.department_id = e.department_id;
```

Mental model:

```text
INNER JOIN = only matching rows survive
```

Before joining tables, identify:
- which tables are involved;
- which columns connect them;
- the relationship cardinality;
- whether the join can multiply rows.

---

## 2. LEFT JOIN

A `LEFT JOIN` preserves all rows from the table on the left.

```sql
SELECT
    d.name AS department_name,
    e.name AS employee_name
FROM department d
LEFT JOIN employee e
    ON e.department_id = d.department_id
ORDER BY d.department_id;
```

If a department has no employee:

```text
LEGAL | NULL
```

Mental model:

```text
LEFT JOIN = preserve the left side
```

Important distinction:

```text
employee LEFT JOIN department
→ preserves all employees

department LEFT JOIN employee
→ preserves all departments
```

---

## 3. NULL and Three-Valued Logic

SQL uses:

```text
TRUE
FALSE
UNKNOWN
```

`NULL` represents an unknown or missing value.

Therefore:

```sql
NULL = NULL
```

does not evaluate to `TRUE`; it evaluates to `UNKNOWN`.

Use:

```sql
WHERE manager_id IS NULL
```

instead of:

```sql
WHERE manager_id = NULL
```

---

## 4. Self JOIN

A self-join joins a table to itself.

```text
EMPLOYEE.MANAGER_ID
        ↓
EMPLOYEE.EMPLOYEE_ID
```

```sql
SELECT
    e.name AS employee_name,
    m.name AS manager_name
FROM employee e
LEFT JOIN employee m
    ON m.employee_id = e.manager_id;
```

Aliases represent different logical roles:

```text
e = employee
m = manager
```

Using `LEFT JOIN` preserves top-level managers whose `manager_id` is `NULL`.

---

## 5. NOT IN and the NULL Trap

```sql
SELECT employee_id, name
FROM employee
WHERE employee_id NOT IN (
    SELECT manager_id
    FROM employee
);
```

If the subquery contains `NULL`, Oracle may need to evaluate something conceptually like:

```text
employee_id != 1
AND employee_id != 2
AND employee_id != NULL
```

But comparison with `NULL` yields `UNKNOWN`.

Mental model:

```text
NOT IN + NULL = dangerous
```

Safer variant:

```sql
SELECT employee_id, name
FROM employee
WHERE employee_id NOT IN (
    SELECT manager_id
    FROM employee
    WHERE manager_id IS NOT NULL
);
```

---

## 6. NOT EXISTS

For employees who manage nobody:

```sql
SELECT
    e.employee_id,
    e.name
FROM employee e
WHERE NOT EXISTS (
    SELECT 1
    FROM employee subordinate
    WHERE subordinate.manager_id = e.employee_id
);
```

Mental model:

```text
NOT EXISTS = no matching row exists
```

---

## 7. UNION vs UNION ALL

### UNION

Combines result sets and removes duplicates.

```sql
SELECT location
FROM department
UNION
SELECT city
FROM customer;
```

```text
UNION = combine + deduplicate
```

### UNION ALL

Combines result sets and keeps duplicates.

```sql
SELECT location
FROM department
UNION ALL
SELECT city
FROM customer;
```

```text
UNION ALL = combine + preserve duplicates
```

Rule of thumb:

```text
Need duplicate removal?
YES -> UNION
NO  -> UNION ALL
```

---

## 8. MINUS

Oracle `MINUS` returns rows from the first result set that do not appear in the second.

```text
A MINUS B
= rows in A that are not in B
```

Example:

```sql
SELECT customer_id
FROM customer
MINUS
SELECT customer_id
FROM orders;
```

Order matters:

```text
A MINUS B != B MINUS A
```

---

## 9. Customers With No Orders Using LEFT JOIN

```sql
SELECT
    c.customer_id,
    c.name
FROM customer c
LEFT JOIN orders o
    ON o.customer_id = c.customer_id
WHERE o.customer_id IS NULL;
```

Mental model:

```text
1. Preserve all customers
2. Join matching orders
3. Keep only rows where no order matched
```

---

## 10. Accidental Row Multiplication

```sql
SELECT
    c.customer_id,
    c.name,
    a.account_id,
    o.order_id
FROM customer c
JOIN account a
    ON a.customer_id = c.customer_id
JOIN orders o
    ON o.customer_id = c.customer_id
WHERE c.customer_id = 1;
```

If a customer has:

```text
2 accounts
3 orders
```

the result can contain:

```text
2 × 3 = 6 rows
```

because every account can pair with every order.

This matters with:

```sql
SUM(...)
COUNT(...)
AVG(...)
```

because duplicated rows can produce incorrect aggregates.

Senior SQL habit:

> Understand join cardinality before aggregating.

---

## 11. ON vs WHERE With LEFT JOIN

This can accidentally remove unmatched rows:

```sql
SELECT
    d.name,
    e.name
FROM department d
LEFT JOIN employee e
    ON e.department_id = d.department_id
WHERE e.status = 'ACTIVE';
```

If the requirement is to preserve all departments but include only active employees when present:

```sql
SELECT
    d.name,
    e.name
FROM department d
LEFT JOIN employee e
    ON e.department_id = d.department_id
   AND e.status = 'ACTIVE';
```

Mental model:

```text
ON    = controls matching
WHERE = filters the final result
```

---

## 12. Day 02 Lab Solutions

### Exercise 1 — INNER JOIN

```sql
SELECT
    e.name,
    e.job_title,
    d.name AS department_name,
    d.location
FROM employee e
JOIN department d
    ON d.department_id = e.department_id;
```

### Exercise 2 — LEFT JOIN

```sql
SELECT
    d.name AS department_name,
    e.name AS employee_name
FROM department d
LEFT JOIN employee e
    ON e.department_id = d.department_id
ORDER BY d.department_id;
```

### Exercise 3 — Self JOIN

```sql
SELECT
    e.name AS employee_name,
    m.name AS manager_name
FROM employee e
LEFT JOIN employee m
    ON m.employee_id = e.manager_id;
```

### Exercise 4A — Customers With No Orders Using MINUS

```sql
SELECT customer_id
FROM customer
MINUS
SELECT customer_id
FROM orders;
```

### Exercise 4B — Customers With No Orders Using LEFT JOIN

```sql
SELECT
    c.customer_id,
    c.name
FROM customer c
LEFT JOIN orders o
    ON o.customer_id = c.customer_id
WHERE o.customer_id IS NULL;
```

### Exercise 5 — Employees Who Manage Nobody

```sql
SELECT
    e.employee_id,
    e.name
FROM employee e
WHERE NOT EXISTS (
    SELECT 1
    FROM employee subordinate
    WHERE subordinate.manager_id = e.employee_id
);
```

### Exercise 6 — UNION / UNION ALL

```sql
SELECT location
FROM department
UNION
SELECT city
FROM customer;
```

```sql
SELECT location
FROM department
UNION ALL
SELECT city
FROM customer;
```

### Exercise 7 — Row Multiplication

```sql
SELECT
    c.customer_id,
    c.name,
    a.account_id,
    o.order_id
FROM customer c
JOIN account a
    ON a.customer_id = c.customer_id
JOIN orders o
    ON o.customer_id = c.customer_id
WHERE c.customer_id = 1;
```

---

## 13. Interview Memory Version

```text
INNER JOIN
= only matching rows

LEFT JOIN
= preserve the left side

NULL
= unknown

NULL = NULL
= UNKNOWN, not TRUE

Self JOIN
= same table joined using aliases

NOT IN + NULL
= dangerous

NOT EXISTS
= no matching row exists

UNION
= combine + deduplicate

UNION ALL
= combine + keep duplicates

MINUS
= rows in first set not in second

1:N + 1:N
= possible row multiplication

ON
= controls matching

WHERE
= filters final result
```

---

## 14. Completion Checklist

- [x] INNER JOIN
- [x] LEFT JOIN
- [x] Self JOIN
- [x] NULL / three-valued logic
- [x] `IS NULL`
- [x] `NOT IN` + `NULL` trap
- [x] `NOT EXISTS`
- [x] `UNION`
- [x] `UNION ALL`
- [x] `MINUS`
- [x] Row multiplication
- [x] Join cardinality reasoning
- [x] Exercises completed
- [x] Notes generated

**Day 02 Status: ✅ COMPLETE**

---

## Next Session

### Day 03 — GROUP BY, HAVING & Subqueries

Topics:

```text
GROUP BY
HAVING
aggregate functions
scalar subqueries
correlated subqueries
query decomposition
```
