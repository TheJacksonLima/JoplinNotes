# Oracle SQL + PL/SQL — Day 1

**Status:** ✅ Complete  
**Date:** 2026-11-12  
**Phase:** SQL Core + PL/SQL Foundations  
**Main goals:** SQL vs PL/SQL, relational model, PK/FK, transactions, first PL/SQL block, `SELECT INTO`, set-based SQL.

---

## 1. SQL vs PL/SQL

### SQL

SQL is **declarative**.

You describe **what** data or result you want, and Oracle decides **how** to retrieve or modify it.

```sql
SELECT name, salary
FROM employee
WHERE salary > 10000;
```

### PL/SQL

PL/SQL is Oracle's **procedural** extension to SQL.

It adds programming constructs such as:

- Variables
- Conditions
- Loops
- Exception handling
- Procedures
- Functions
- Packages
- Collections

PL/SQL can execute SQL directly inside procedural code.

### Mental model

```text
SQL    -> What data/result do I want?
PL/SQL -> What logic or sequence of operations do I need?
```

---

## 2. Relational Model

### Primary Key

A primary key uniquely identifies a row.

Example:

```text
EMPLOYEE
----------------
employee_id  PK
name
salary
```

A surrogate key such as `employee_id` is useful because it has no business meaning and usually does not need to change.

### Foreign Key

A foreign key references a key in another table.

Example:

```text
EMPLOYEE.department_id
        |
        v
DEPARTMENT.department_id
```

Purpose:

- Creates relationships between tables
- Enforces referential integrity
- Prevents rows from referencing nonexistent parent records

---

## 3. Important Terminology

### Normalization

Normalization organizes data into related tables and reduces unnecessary duplication.

### Referential Integrity

Referential integrity ensures that a foreign key points to a valid row in the referenced table.

### Surrogate Key

An artificial identifier with no business meaning.

Example:

```text
department_id = 7
```

### Natural / Business Key

A value with business meaning.

Example:

```text
department_name = 'HR'
```

### Magic Number / Hard-coded ID

Avoid assuming that a specific numeric ID always represents the same business entity.

Bad:

```sql
INSERT INTO employee (
    name,
    salary,
    department_id
)
VALUES (
    'Robert',
    8000,
    7
);
```

Better:

```sql
INSERT INTO employee (
    name,
    salary,
    department_id
)
VALUES (
    'Robert',
    8000,
    (
        SELECT department_id
        FROM department
        WHERE name = 'HR'
    )
);
```

Why this is safer:

- IDs may differ between DEV, QA, UAT, and PROD.
- The SQL resolves the row using a business value instead of assuming a surrogate key value.

---

## 4. Basic JOIN

```sql
SELECT e.name,
       e.salary,
       d.name AS department_name
FROM employee e
JOIN department d
    ON d.department_id = e.department_id
WHERE e.salary > 10000
ORDER BY e.name;
```

### Key concepts

```text
JOIN  -> combines related tables
ON    -> defines the relationship
WHERE -> filters rows
```

---

## 5. Transactions

### COMMIT

```sql
COMMIT;
```

Makes the current transaction's changes permanent.

### ROLLBACK

```sql
ROLLBACK;
```

Undoes uncommitted changes.

Important:

```text
INSERT
COMMIT
ROLLBACK
```

The rollback cannot undo the already committed insert.

---

## 6. PL/SQL Block Structure

```sql
DECLARE
    -- variables and constants
BEGIN
    -- executable statements
EXCEPTION
    -- error handling
END;
/
```

### Sections

```text
DECLARE   -> local variables and constants
BEGIN     -> executable code
EXCEPTION -> error handling
END       -> closes the block
```

`DECLARE` and `EXCEPTION` are optional depending on the block.

Minimal example:

```sql
BEGIN
    DBMS_OUTPUT.PUT_LINE('Oracle mastery - Day 1');
END;
/
```

---

## 7. SELECT INTO

`SELECT INTO` retrieves SQL results into PL/SQL variables.

```sql
DECLARE
    l_employee_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO l_employee_count
    FROM employee;

    DBMS_OUTPUT.PUT_LINE(
        'Employees: ' || l_employee_count
    );
END;
/
```

Mental model:

```text
SQL result
   |
   v
INTO
   |
   v
PL/SQL variable
```

Example with two variables:

```sql
DECLARE
    l_employee_count   NUMBER;
    l_department_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO l_employee_count
    FROM employee;

    SELECT COUNT(*)
    INTO l_department_count
    FROM department;

    DBMS_OUTPUT.PUT_LINE(
        'Employees: ' || l_employee_count
    );

    DBMS_OUTPUT.PUT_LINE(
        'Departments: ' || l_department_count
    );
END;
/
```

---

## 8. Set-Based SQL vs Row-by-Row PL/SQL

Prefer set-based SQL when a single SQL statement can solve the problem.

Example:

```sql
UPDATE employee
SET salary = salary * 1.05
WHERE department_id = 10;
```

This is generally better than looping through employees and updating one row at a time.

Why:

- One SQL operation can process the whole set.
- Oracle's optimizer can optimize the operation.
- Less context switching between the SQL engine and PL/SQL engine.
- Less procedural overhead.
- Usually faster and simpler.

### Senior-level rule

> Prefer set-based SQL when the operation can naturally be expressed as a single SQL statement. Use PL/SQL when you need procedural logic that SQL alone cannot express cleanly.

Later topics that build on this:

- `BULK COLLECT`
- `FORALL`
- Collections
- Context switching
- High-volume DML

---

## 9. Day 1 Lab

### Department table

```sql
CREATE TABLE department (
    department_id NUMBER GENERATED BY DEFAULT AS IDENTITY,
    name          VARCHAR2(100) NOT NULL,

    CONSTRAINT pk_department
        PRIMARY KEY (department_id),

    CONSTRAINT uk_department_name
        UNIQUE (name)
);
```

### Employee table

```sql
CREATE TABLE employee (
    employee_id   NUMBER GENERATED BY DEFAULT AS IDENTITY,
    name          VARCHAR2(100) NOT NULL,
    salary        NUMBER(10,2),
    department_id NUMBER,

    CONSTRAINT pk_employee
        PRIMARY KEY (employee_id),

    CONSTRAINT fk_employee_department
        FOREIGN KEY (department_id)
        REFERENCES department(department_id),

    CONSTRAINT ck_employee_salary
        CHECK (salary >= 0)
);
```

### Sample data

```sql
INSERT INTO department (name)
VALUES ('Engineering');

INSERT INTO department (name)
VALUES ('Finance');

INSERT INTO department (name)
VALUES ('Operations');

COMMIT;
```

```sql
INSERT INTO employee (name, salary, department_id)
VALUES ('John', 12000, 1);

INSERT INTO employee (name, salary, department_id)
VALUES ('Maria', 15000, 1);

INSERT INTO employee (name, salary, department_id)
VALUES ('Ana', 11000, 2);

INSERT INTO employee (name, salary, department_id)
VALUES ('Carlos', 9000, 3);

COMMIT;
```

---

## 10. Referential Integrity Test

This should fail if department `999` does not exist:

```sql
INSERT INTO employee (
    name,
    salary,
    department_id
)
VALUES (
    'Invalid Employee',
    5000,
    999
);
```

Expected type of error:

```text
ORA-02291: integrity constraint violated - parent key not found
```

Meaning:

```text
EMPLOYEE.department_id
must reference
DEPARTMENT.department_id
```

---

## 11. Day 1 Knowledge Check

You should now be able to explain:

1. What is the difference between SQL and PL/SQL?
2. What is a primary key?
3. What is a foreign key?
4. What is referential integrity?
5. What is the difference between normalization and referential integrity?
6. What is a surrogate key?
7. What is a natural/business key?
8. Why should hard-coded IDs be avoided?
9. What does `COMMIT` do?
10. What does `ROLLBACK` do?
11. What are the roles of `DECLARE`, `BEGIN`, `EXCEPTION`, and `END`?
12. What does `SELECT INTO` do?
13. Why is set-based SQL usually preferable to row-by-row PL/SQL?

---

## 12. Interview Memory Version

```text
SQL = declarative
PL/SQL = procedural

PK = unique row
FK = relationship + referential integrity

Normalization = reduce duplication / organize relations
Referential integrity = FK must reference valid row

Surrogate key = artificial ID
Natural key = business meaning
Avoid magic IDs

COMMIT = make permanent
ROLLBACK = undo uncommitted work

DECLARE
BEGIN
EXCEPTION
END

SELECT ... INTO -> SQL result into PL/SQL variable

Prefer set-based SQL over row-by-row processing
```

---

## 13. Day 1 Completion

- [x] Oracle container running
- [x] SQL vs PL/SQL understood
- [x] PK/FK concepts understood
- [x] Referential integrity tested
- [x] JOIN written
- [x] `WHERE` filtering corrected
- [x] Avoided hard-coded department ID
- [x] `COMMIT` / `ROLLBACK` explained
- [x] Anonymous PL/SQL block written
- [x] `SELECT INTO` used
- [x] Set-based SQL explained
- [x] Knowledge check completed

**Day 1 — COMPLETE ✅**
