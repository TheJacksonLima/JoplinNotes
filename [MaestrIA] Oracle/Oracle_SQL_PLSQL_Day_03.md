# Oracle SQL + PL/SQL — Day 03

**Status:** 🟡 In Progress  
**Phase:** P1 — SQL Interview Sharpness  
**Focus:** `GROUP BY`, `HAVING`, scalar subqueries, correlated subqueries  
**Primary skill:** Query decomposition

---

## Session Objective

The goal of Day 03 is to become comfortable decomposing SQL problems that involve:

- aggregation;
- filtering before vs. after grouping;
- scalar subqueries;
- correlated subqueries;
- rewriting equivalent solutions with different query structures.

The key interview skill is not only writing a valid query, but explaining **why the query shape matches the business requirement**.

---

# 1. GROUP BY

`GROUP BY` collapses multiple detail rows into one result row per group.

Example:

```sql
SELECT
    department_id,
    COUNT(*) AS employee_count,
    SUM(salary) AS total_salary,
    AVG(salary) AS avg_salary,
    MIN(salary) AS min_salary,
    MAX(salary) AS max_salary
FROM employee
GROUP BY department_id;
```

Mental model:

```text
detail rows
    ↓
GROUP BY
    ↓
one result row per group
```

Examples of common aggregate functions:

```text
COUNT()
SUM()
AVG()
MIN()
MAX()
```

## GROUP BY rule

Every expression in the `SELECT` list must normally be either:

```text
1. part of the GROUP BY
or
2. aggregated
```

Example of an invalid query:

```sql
SELECT
    department_id,
    name,
    AVG(salary)
FROM employee
GROUP BY department_id;
```

Oracle cannot decide which employee `name` should represent the department.

Typical error:

```text
ORA-00979: not a GROUP BY expression
```

Correct version for one average per department:

```sql
SELECT
    department_id,
    AVG(salary) AS avg_salary
FROM employee
GROUP BY department_id;
```

Important lesson:

> Do not add columns to `GROUP BY` merely to eliminate ORA-00979.  
> Adding a grouping column changes the meaning and granularity of the query.

---

# 2. WHERE vs HAVING

This distinction must become automatic.

## WHERE

`WHERE` filters **individual rows before grouping**.

Example:

```sql
SELECT
    department_id,
    AVG(salary) AS avg_salary
FROM employee
WHERE status = 'ACTIVE'
GROUP BY department_id;
```

Mental model:

```text
rows
↓
WHERE
↓
GROUP BY
↓
aggregate
```

## HAVING

`HAVING` filters **groups after aggregation**.

Example:

```sql
SELECT
    department_id,
    AVG(salary) AS avg_salary
FROM employee
GROUP BY department_id
HAVING AVG(salary) > 10000;
```

Mental model:

```text
rows
↓
GROUP BY
↓
aggregate
↓
HAVING
```

## WHERE + HAVING together

Example:

```sql
SELECT
    department_id,
    COUNT(*) AS employee_count,
    AVG(salary) AS avg_salary
FROM employee
WHERE status = 'ACTIVE'
GROUP BY department_id
HAVING COUNT(*) >= 2;
```

Interpretation:

```text
1. Keep ACTIVE employees
2. Group them by department
3. Calculate aggregates
4. Keep departments with at least 2 active employees
```

Useful mental execution order:

```text
FROM / JOIN
→ WHERE
→ GROUP BY
→ aggregation
→ HAVING
→ SELECT
→ ORDER BY
```

---

# 3. Scalar Subquery

A scalar subquery returns:

```text
one row
one column
```

Example:

```sql
SELECT AVG(salary)
FROM employee;
```

This can be embedded in another query:

```sql
SELECT
    employee_id,
    name,
    salary
FROM employee
WHERE salary > (
    SELECT AVG(salary)
    FROM employee
);
```

Interpretation:

```text
1. Calculate company average salary
2. Compare each employee salary against that value
```

This is a **non-correlated scalar subquery** because the inner query does not depend on the outer query.

## Important scalar rule

This works:

```sql
WHERE salary > (
    SELECT AVG(salary)
    FROM employee
)
```

because the inner query returns one value.

But this is not a valid scalar comparison if the subquery returns several rows:

```sql
WHERE salary > (
    SELECT AVG(salary)
    FROM employee
    GROUP BY department_id
)
```

Always ask:

```text
Does my subquery return one row or many?
```

---

# 4. Correlated Subquery

A correlated subquery references a value from the outer query.

Example requirement:

> Find employees earning more than the average salary of their own department.

Solution:

```sql
SELECT
    e1.name,
    e1.salary,
    e1.department_id
FROM employee e1
WHERE e1.salary > (
    SELECT AVG(e2.salary)
    FROM employee e2
    WHERE e2.department_id = e1.department_id
);
```

The correlation is:

```sql
e2.department_id = e1.department_id
```

`e1.department_id` belongs to the outer query.

Mental model:

```text
for each employee
    ↓
identify that employee's department
    ↓
calculate that department's average salary
    ↓
compare employee salary against that average
```

---

# 5. Non-Correlated vs Correlated Subquery

## Non-correlated

The inner query can execute independently.

Example:

```sql
SELECT e.name, e.salary
FROM employee e
WHERE e.salary > (
    SELECT AVG(salary)
    FROM employee
);
```

Business meaning:

```text
employee salary
vs.
company average
```

## Correlated

The inner query depends on the current outer-row value.

Example:

```sql
SELECT e.name, e.salary
FROM employee e
WHERE e.salary > (
    SELECT AVG(e2.salary)
    FROM employee e2
    WHERE e2.department_id = e.department_id
);
```

Business meaning:

```text
employee salary
vs.
that employee's department average
```

Interview shorthand:

```text
Non-correlated
→ inner query is independent

Correlated
→ inner query references outer query
```

---

# 6. Query Decomposition

Before writing complex SQL, break the requirement into smaller questions.

Example:

> Employees earning more than their department average.

Think:

```text
1. How do I calculate an average?
2. How do I calculate the average for one department?
3. Where does the department value come from?
4. It comes from the current employee row.
5. Therefore the subquery must be correlated.
```

This approach is more reliable than trying to write the complete SQL immediately.

---

# 7. JOIN + GROUP BY

When aggregating after a join, the relationship must be correct.

Correct example:

```sql
SELECT
    d.name,
    SUM(e.salary) AS total_payroll
FROM employee e
JOIN department d
    ON d.department_id = e.department_id
GROUP BY d.name
HAVING SUM(e.salary) > 30000;
```

Without the join condition:

```sql
SELECT
    d.name,
    SUM(e.salary)
FROM employee e,
     department d
GROUP BY d.name;
```

you create a Cartesian product:

```text
EMPLOYEE × DEPARTMENT
```

and the aggregate becomes incorrect.

Senior habit:

> Verify join cardinality before trusting an aggregate.

---

# 8. Day 03 Exercises

## Exercise 1 — Department salary summary

Return, for every department with employees:

```text
department name
employee count
total payroll
average salary
minimum salary
maximum salary
```

Expected concepts:

```text
JOIN
GROUP BY
COUNT
SUM
AVG
MIN
MAX
```

---

## Exercise 2 — WHERE vs HAVING

Return departments where:

```text
employee status = ACTIVE
and
department has at least 2 active employees
```

Return:

```text
department name
active employee count
average salary
```

Expected reasoning:

```text
status condition → WHERE
count condition  → HAVING
```

---

## Exercise 3 — Company Average

Return:

```text
employee name
salary
```

for employees earning above the company-wide average.

Use a scalar subquery.

Example structure:

```sql
SELECT ...
FROM employee
WHERE salary > (
    SELECT AVG(...)
    FROM employee
);
```

---

## Exercise 4 — Department Average

Completed solution:

```sql
SELECT
    e1.name,
    e1.salary,
    e1.department_id
FROM employee e1
WHERE e1.salary > (
    SELECT AVG(e2.salary)
    FROM employee e2
    WHERE e2.department_id = e1.department_id
);
```

Status:

```text
✅ Correct
```

Key reason:

```text
The inner query references e1.department_id from the outer query.
```

---

## Exercise 5 — Department Threshold

Completed solution:

```sql
SELECT
    d.name,
    SUM(e.salary) AS total_payroll
FROM employee e
JOIN department d
    ON d.department_id = e.department_id
GROUP BY d.name
HAVING SUM(e.salary) > 30000;
```

Status:

```text
✅ Correct
```

Important correction made during the session:

The first version omitted the join condition and therefore produced a Cartesian product.

---

## Exercise 6 — Break / Inspect ORA-00979

Broken query:

```sql
SELECT
    department_id,
    name,
    AVG(salary)
FROM employee
GROUP BY department_id;
```

Reason for failure:

```text
The query requests one row per department,
but NAME has multiple possible values inside the department.

Oracle cannot arbitrarily choose one employee name.
```

Correct query for average salary per department:

```sql
SELECT
    department_id,
    AVG(salary) AS avg_salary
FROM employee
GROUP BY department_id;
```

Status:

```text
✅ Break / inspect requirement completed
```

---

## Exercise 7 — Rewrite the Correlated Query

Still to complete.

Requirement:

> Rewrite Exercise 4 using `JOIN + aggregated inline view`.

Target idea:

```text
1. Build a result containing:
   department_id + average salary

2. Join that result to EMPLOYEE

3. Compare employee salary against department average
```

Do not use an analytic function yet; analytic functions are covered later in the roadmap.

---

# 9. Book Reading for Day 03

## Oracle SQL by Example — Chapter 6

**Chapter 6 — Aggregate Functions, GROUP BY, and HAVING Clauses**

Primary section:

```text
Lab 6.2 — The GROUP BY and HAVING Clauses
Pages 271–282
```

Focus on:

```text
GROUP BY
Grouping by multiple columns
ORA-00979
HAVING
WHERE vs HAVING
Order of clauses
Exercises
```

## Oracle SQL by Example — Chapter 8

### Lab 8.1 — Simple Subqueries

Recommended:

```text
Pages 324–331
```

Focus on:

```text
scalar subqueries
single-row vs multiple-row results
query decomposition
```

### Lab 8.2 — Correlated Subqueries

Recommended:

```text
Pages 339–346
```

Focus on:

```text
correlated subqueries
outer-query references
logical row-by-row reasoning
subquery vs join trade-offs
```

---

# 10. Interview Memory

```text
GROUP BY
→ collapses detail rows into groups

WHERE
→ filters rows before grouping

HAVING
→ filters groups after aggregation

Scalar subquery
→ one row + one column

Non-correlated subquery
→ independent from outer query

Correlated subquery
→ references outer query

ORA-00979
→ selected expression is neither grouped nor aggregated

JOIN before aggregation
→ verify cardinality

Complex SQL
→ decompose the requirement first
```

---

# 11. Current Day 03 Status

```text
GROUP BY                         ✅
WHERE vs HAVING                 ✅
Scalar subquery                 ✅
Correlated subquery             ✅
Department-average exercise     ✅
HAVING threshold exercise       ✅
ORA-00979 break/inspect         ✅
JOIN cardinality correction     ✅
Exercise 7 rewrite              ⬜
Interview knowledge check       ⬜
```

**Day 03 Status: 🟡 In Progress**

---

# Next Step

Complete:

```text
Exercise 7
→ correlated subquery rewritten as JOIN + aggregated inline view
```

Then perform the short Day 03 interview check.
