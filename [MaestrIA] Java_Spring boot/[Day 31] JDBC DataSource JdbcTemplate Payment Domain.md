# Day 31 — JDBC + DataSource + JdbcTemplate

## Goal

Day 31 introduces relational database access at two levels:

```text
Part A — Java
raw JDBC lifecycle

Part B — Spring
DataSource
HikariCP
JdbcTemplate
RowMapper
Spring exception translation
```

The project keeps the existing Payment domain.

```text
BEFORE

PaymentRepository
↓
Map<Long, Payment>


DAY 31

PaymentRepository
↓
JdbcTemplate
↓
DataSource
↓
HikariCP
↓
JDBC driver
↓
H2
```

The goal is to understand JDBC first, then use Spring to remove repetitive infrastructure code without losing sight of what is happening underneath.

---

# Current Payment Domain

```java
public class Payment {

    private BigDecimal amount;
    private PaymentMethod method;
}
```

```java
public enum PaymentMethod {
    PIX,
    CREDIT_CARD
}
```

```java
public enum PaymentStatus {
    PENDING,
    APPROVED,
    REJECTED
}
```

Important:

```text
PaymentStatus is not persisted.

PaymentService
+
PaymentStrategy
→ determine PaymentStatus
```

The original repository stores payments in:

```java
Map<Long, Payment>
```

Day 31 preserves the service/controller contract while changing persistence.

---

# Part A — Raw JDBC

## What Is JDBC?

JDBC means:

```text
Java Database Connectivity
```

It is the standard Java API for working with relational databases.

Core types:

```java
Connection
Statement
PreparedStatement
ResultSet
SQLException
DriverManager
```

Day 31 focuses mainly on:

```text
Connection
Statement
ResultSet
```

Prepared statements and transaction boundaries are explored more deeply on Day 32.

---

# JDBC Architecture

```text
Java application
↓
JDBC API
↓
database-specific driver
↓
database
```

Examples:

```text
Oracle
→ Oracle JDBC driver

PostgreSQL
→ PostgreSQL JDBC driver

H2
→ H2 JDBC driver
```

JDBC is the common API. The driver handles communication with the specific database.

---

# H2 In-Memory Database

Example URL:

```text
jdbc:h2:mem:test
```

Breakdown:

```text
jdbc:
→ JDBC URL

h2:
→ H2 driver

mem:
→ in-memory database

test
→ database name
```

---

# JDBC Lifecycle

```text
open Connection
↓
create Statement
↓
execute SQL
↓
obtain ResultSet
↓
read rows
↓
close ResultSet
↓
close Statement
↓
close Connection
```

---

# DriverManager

```java
Connection connection =
        DriverManager.getConnection(
                "jdbc:h2:mem:test"
        );
```

Conceptually:

```text
DriverManager
↓
examines JDBC URL
↓
selects matching driver
↓
opens connection
```

---

# Connection

A `Connection` represents a database connection/session.

Through it you can:

```text
create statements
execute SQL
control transactions
commit
rollback
close/release resources
```

Connections can be expensive because they may involve:

```text
network setup
authentication
database session creation
server-side resources
```

This is why production applications use connection pools.

---

# Statement

```java
Statement statement =
        connection.createStatement();
```

Conceptually:

```text
Connection
↓
Statement
↓
SQL
↓
Database
```

---

# Execute Methods

## `execute()`

Generic SQL execution:

```java
statement.execute(
        "create table payment (...)"
);
```

## `executeQuery()`

Used for SELECT:

```java
ResultSet rs =
        statement.executeQuery(
                "select * from payment"
        );
```

## `executeUpdate()`

Commonly used for:

```text
INSERT
UPDATE
DELETE
```

Example:

```java
int rows =
        statement.executeUpdate(
                "delete from payment where id = 1"
        );
```

---

# ResultSet

A `ResultSet` represents rows returned by a query.

Important:

```text
The cursor starts before the first row.
```

Therefore:

```java
while (rs.next()) {
    // read row
}
```

is the normal iteration pattern.

---

# Reading Columns

```java
long id =
        rs.getLong("id");

BigDecimal amount =
        rs.getBigDecimal("amount");

String method =
        rs.getString("method");
```

---

# Mapping a Payment Manually

```java
Payment payment =
        new Payment(
                rs.getBigDecimal(
                        "amount"
                ),
                PaymentMethod.valueOf(
                        rs.getString(
                                "method"
                        )
                )
        );
```

Mental model:

```text
database row
100.00 | PIX
↓
ResultSet
↓
manual mapping
↓
Payment(100.00, PIX)
```

---

# Try-With-Resources

JDBC resources implement `AutoCloseable`.

```java
try (
        Connection connection =
                DriverManager.getConnection(
                        "jdbc:h2:mem:test"
                );

        Statement statement =
                connection.createStatement()
) {

    try (
            ResultSet rs =
                    statement.executeQuery(
                            "select * from payment"
                    )
    ) {

        while (rs.next()) {
            // read rows
        }
    }
}
```

This guarantees cleanup even if an exception occurs.

---

# Why Cleanup Matters

Leaks can cause:

```text
connection leaks
cursor leaks
database resource exhaustion
connection pool exhaustion
```

---

# SQLException

Typical causes:

```text
bad SQL
connection failure
constraint violation
authentication error
table not found
timeout
```

Useful methods:

```java
ex.getMessage();
ex.getSQLState();
ex.getErrorCode();
```

---

# Intentional JDBC Breaks

## Nonexistent table

```sql
select *
from payment_that_does_not_exist
```

## Duplicate primary key

Insert two rows with the same ID.

## Read before `rs.next()`

Incorrect:

```java
ResultSet rs =
        statement.executeQuery(...);

System.out.println(
        rs.getString("method")
);
```

Correct:

```java
if (rs.next()) {

    System.out.println(
            rs.getString("method")
    );
}
```

---

# Part B — DataSource + JdbcTemplate

## Main Transition

Raw JDBC:

```text
DriverManager
↓
Connection
↓
Statement
↓
ResultSet
↓
manual cleanup
```

Spring JDBC:

```text
JdbcTemplate
↓
DataSource
↓
HikariCP
↓
JDBC Connection
↓
driver
↓
database
```

`JdbcTemplate` does not replace JDBC. It runs on top of JDBC.

---

# Required Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

Spring Boot can then autoconfigure:

```text
DataSource
HikariCP
JdbcTemplate
H2 driver
```

---

# DataSource Configuration

Add:

```properties
spring.datasource.url=jdbc:h2:mem:payments
spring.datasource.username=sa
spring.datasource.password=
```

Conceptually:

```text
classpath
+
datasource properties
↓
Spring Boot autoconfiguration
↓
DataSource bean
↓
JdbcTemplate bean
```

---

# DataSource

`DataSource` is the standard JDBC abstraction for obtaining connections.

```java
javax.sql.DataSource
```

Conceptually:

```text
Application
↓
DataSource
↓
Connection
```

---

# HikariCP

Typical Spring Boot architecture:

```text
DataSource
↓
HikariCP
↓
pooled JDBC connections
```

Pooling avoids repeatedly creating/destroying physical DB connections.

---

# Connection Pool Model

```text
Pool

Connection 1
Connection 2
Connection 3
Connection 4
```

A database operation:

```text
borrow connection
↓
execute SQL
↓
connection.close()
↓
return to pool
```

Important:

```text
connection.close()
on a pooled connection
usually means
return to pool
```

not necessarily a physical disconnect.

---

# Inspect the DataSource

```java
@Configuration
public class DataSourceDemo {

    @Bean
    CommandLineRunner inspectDataSource(
            DataSource dataSource) {

        return args -> {

            System.out.println(
                    "DataSource="
                            + dataSource
                            .getClass()
                            .getName()
            );
        };
    }
}
```

Typical implementation:

```text
com.zaxxer.hikari.HikariDataSource
```

---

# Payment Table

The current Payment domain contains:

```text
amount
method
```

The repository supplies the ID.

Use:

```sql
create table payment (
    id bigint primary key,
    amount decimal(19, 2) not null,
    method varchar(30) not null
);
```

Important:

```text
status is not persisted
```

because `PaymentStatus` is calculated by the service/strategy layer.

The request `reference` is also not persisted yet because the existing `Payment` domain does not contain it.

---

# schema.sql

Create:

```text
src/main/resources/schema.sql
```

with:

```sql
create table payment (
    id bigint primary key,
    amount decimal(19, 2) not null,
    method varchar(30) not null
);
```

---

# data.sql

Reproduce the original in-memory repository data:

```sql
insert into payment (id, amount, method)
values (1, 100.00, 'PIX');

insert into payment (id, amount, method)
values (2, 500.00, 'PIX');

insert into payment (id, amount, method)
values (3, 200.00, 'CREDIT_CARD');

insert into payment (id, amount, method)
values (4, 1500.00, 'CREDIT_CARD');

insert into payment (id, amount, method)
values (5, -50.00, 'PIX');
```

---

# JdbcTemplate

`JdbcTemplate` manages repetitive JDBC infrastructure.

Spring handles:

```text
Connection acquisition
Statement creation
ResultSet cleanup
Statement cleanup
Connection release
SQLException translation
```

Application code focuses on:

```text
SQL
parameters
row mapping
```

---

# PaymentRepository with JdbcTemplate

```java
@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepository(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate =
                jdbcTemplate;
    }
}
```

The previous:

```java
Map<Long, Payment>
```

is removed.

---

# Payment Row Mapping

```java
private Payment mapPayment(
        ResultSet rs,
        int rowNum)
        throws SQLException {

    return new Payment(
            rs.getBigDecimal(
                    "amount"
            ),
            PaymentMethod.valueOf(
                    rs.getString(
                            "method"
                    )
            )
    );
}
```

Mental model:

```text
ResultSet row
↓
RowMapper
↓
Payment
```

---

# `findAll()`

```java
public List<Payment> findAll() {

    return jdbcTemplate.query(
            """
            select amount,
                   method
              from payment
             order by id
            """,
            this::mapPayment
    );
}
```

---

# `findById()`

Preserve the current repository contract:

```java
public Optional<Payment> findById(
        long id) {

    List<Payment> payments =
            jdbcTemplate.query(
                    """
                    select amount,
                           method
                      from payment
                     where id = ?
                    """,
                    this::mapPayment,
                    id
            );

    return payments.stream()
            .findFirst();
}
```

The current service remains unchanged:

```java
public Payment findPaymentById(
        Long id) {

    return paymentRepository
            .findById(id)
            .orElseThrow(
                    () ->
                            new PaymentNotFoundException(
                                    id
                            )
            );
}
```

---

# `findPositivePayments()`

```java
public List<Payment>
findPositivePayments() {

    return jdbcTemplate.query(
            """
            select amount,
                   method
              from payment
             where amount > 0
             order by id
            """,
            this::mapPayment
    );
}
```

Expected:

```text
100.00 PIX
500.00 PIX
200.00 CREDIT_CARD
1500.00 CREDIT_CARD
```

The `-50.00 PIX` row is excluded.

---

# `findPaymentsByType()`

```java
public List<Payment>
findPaymentsByType(
        PaymentMethod method) {

    return jdbcTemplate.query(
            """
            select amount,
                   method
              from payment
             where method = ?
             order by id
            """,
            this::mapPayment,
            method.name()
    );
}
```

Examples:

```text
PaymentMethod.PIX
→ "PIX"

PaymentMethod.CREDIT_CARD
→ "CREDIT_CARD"
```

---

# Parameter Binding

```sql
where method = ?
```

The `?` receives a bound value:

```java
method.name()
```

Avoid concatenating values directly into SQL.

Day 32 goes deeper into:

```text
PreparedStatement
parameter binding
transaction boundaries
```

---

# Final PaymentRepository

```java
package org.jfl.repository;

import org.jfl.domain.Payment;
import org.jfl.domain.PaymentMethod;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepository {

    private final JdbcTemplate jdbcTemplate;


    public PaymentRepository(
            JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate =
                jdbcTemplate;
    }


    public List<Payment> findAll() {

        return jdbcTemplate.query(
                """
                select amount,
                       method
                  from payment
                 order by id
                """,
                this::mapPayment
        );
    }


    public Optional<Payment> findById(
            long id) {

        List<Payment> payments =
                jdbcTemplate.query(
                        """
                        select amount,
                               method
                          from payment
                         where id = ?
                        """,
                        this::mapPayment,
                        id
                );

        return payments.stream()
                .findFirst();
    }


    public List<Payment>
    findPositivePayments() {

        return jdbcTemplate.query(
                """
                select amount,
                       method
                  from payment
                 where amount > 0
                 order by id
                """,
                this::mapPayment
        );
    }


    public List<Payment>
    findPaymentsByType(
            PaymentMethod method) {

        return jdbcTemplate.query(
                """
                select amount,
                       method
                  from payment
                 where method = ?
                 order by id
                """,
                this::mapPayment,
                method.name()
        );
    }


    private Payment mapPayment(
            ResultSet rs,
            int rowNum)
            throws SQLException {

        return new Payment(
                rs.getBigDecimal(
                        "amount"
                ),
                PaymentMethod.valueOf(
                        rs.getString(
                                "method"
                        )
                )
        );
    }
}
```

---

# Existing Layers Stay Unchanged

```text
PaymentController
→ unchanged

PaymentService
→ unchanged

PaymentMapper
→ unchanged

Payment domain
→ unchanged

PaymentRepository implementation
→ changed
```

This is repository abstraction working correctly.

---

# Full Request Flow

For:

```text
GET /payments/1
```

the flow is:

```text
PaymentController
↓
PaymentService.findPaymentById(1)
↓
PaymentRepository.findById(1)
↓
JdbcTemplate.query(...)
↓
DataSource
↓
HikariCP
↓
borrow Connection
↓
H2
↓
ResultSet
↓
mapPayment()
↓
Payment
↓
PaymentMapper
↓
PaymentResponse
↓
JSON
```

---

# Existing Endpoint Verification

```text
GET /payments
→ 5 payments
```

```text
GET /payments/1
→ 100.00 PIX
```

```text
GET /payments/positive
→ 4 payments
```

```text
GET /payments/type/PIX
→ 100.00 PIX
→ 500.00 PIX
→ -50.00 PIX
```

```text
GET /payments/type/CREDIT_CARD
→ 200.00 CREDIT_CARD
→ 1500.00 CREDIT_CARD
```

---

# What JdbcTemplate Does Underneath

```text
JdbcTemplate
↓
DataSource.getConnection()
↓
borrow pooled connection
↓
create statement
↓
execute SQL
↓
ResultSet
↓
RowMapper
↓
close ResultSet
↓
close statement
↓
release connection
↓
return to pool
```

The JDBC lifecycle from Part A still exists. Spring manages the boilerplate.

---

# Spring Exception Translation

Raw JDBC:

```text
SQLException
```

Spring JDBC:

```text
SQLException
↓
Spring translation
↓
DataAccessException
```

`DataAccessException` is unchecked.

This means repository methods do not need to declare `throws SQLException` for normal Spring JDBC operations.

---

# Intentional Breaks

## Wrong table

Change:

```sql
from payment
```

to:

```sql
from payments_wrong
```

Then call:

```text
GET /payments
```

Observe Spring's translated data-access exception.

## Invalid enum value

Temporarily insert:

```sql
insert into payment
values (6, 100.00, 'DEBIT_CARD');
```

The DB accepts the `VARCHAR`, but this fails:

```java
PaymentMethod.valueOf(
        "DEBIT_CARD"
);
```

Lesson:

```text
database representation
must match
Java domain representation
```

## Remove H2

Temporarily remove the H2 dependency.

This changes:

```text
classpath
↓
Boot autoconfiguration conditions
↓
DataSource setup
```

This connects Day 31 to Day 30.

---

# Connection Pool Exhaustion

If the pool has:

```text
10 connections
```

and all are leaked:

```text
connection 1 → leaked
...
connection 10 → leaked
```

the next request may wait and eventually timeout.

This is:

```text
connection pool exhaustion
```

`JdbcTemplate` reduces accidental leaks by managing JDBC resource lifecycle.

---

# Hikari Pool Size

Example:

```properties
spring.datasource.hikari.maximum-pool-size=10
```

Do not assume larger is always better.

Pool sizing depends on:

```text
database capacity
query duration
application concurrency
number of app instances
```

---

# JDBC vs JdbcTemplate vs JPA

```text
JDBC
→ low-level DB API

JdbcTemplate
→ JDBC helper
→ explicit SQL
→ less boilerplate

Spring Data JDBC
→ repository abstraction

JPA
→ persistence specification

Hibernate
→ common JPA implementation
```

---

# Interview Questions

## What is JDBC?

JDBC is the standard Java API for relational database access.

## What is the lifecycle of a JDBC query?

Obtain a `Connection`, create a statement, execute SQL, obtain a `ResultSet`, iterate/map rows, then close the result set, statement and connection.

## What is DataSource?

A standard JDBC abstraction for obtaining connections. In Spring applications it commonly sits over a connection pool.

## Why use DataSource instead of DriverManager directly?

It supports centralized configuration, pooling, Spring transaction integration and better resource management.

## What is JdbcTemplate?

A Spring JDBC helper that manages repetitive infrastructure such as acquiring/releasing connections, statement execution, result-set cleanup and exception translation.

## Does JdbcTemplate replace JDBC?

No. It runs on top of JDBC.

## What is RowMapper?

A mapper that converts one JDBC `ResultSet` row into a Java object such as `Payment`.

## What happens when a pooled connection is closed?

It is generally returned to the pool rather than physically disconnected.

## What happens to SQLException in Spring JDBC?

Spring translates it into its unchecked `DataAccessException` hierarchy.

## Why is the repository abstraction useful here?

Because `PaymentService` and `PaymentController` stay unchanged even though persistence changes from an in-memory `HashMap` to H2/JdbcTemplate.

---

# Concise Manual Notes

```text
DAY 31
JDBC + DATASOURCE + JDBCTEMPLATE


PART A

Java
↓
JDBC
↓
driver
↓
DB


Connection
↓
Statement
↓
SQL
↓
ResultSet


ResultSet:
starts before first row

rs.next()
→ move cursor


try-with-resources
→ closes JDBC resources


PART B

BEFORE:

PaymentRepository
↓
Map<Long,Payment>


AFTER:

PaymentRepository
↓
JdbcTemplate
↓
DataSource
↓
HikariCP
↓
JDBC
↓
H2


payment table:

id BIGINT PK
amount DECIMAL
method VARCHAR


PaymentStatus
NOT persisted


DataSource
= gets Connections


HikariCP
= connection pool


close()
on pooled connection
→ returns to pool


JdbcTemplate handles:

Connection lifecycle
Statement lifecycle
ResultSet cleanup
exception translation


Application provides:

SQL
parameters
mapping


RowMapper:

DB row
↓
Payment


findAll()
→ SELECT all

findById()
→ WHERE id = ?

findPositivePayments()
→ WHERE amount > 0

findPaymentsByType()
→ WHERE method = ?


SQLException
↓
DataAccessException


IMPORTANT:

JdbcTemplate
runs ON TOP OF JDBC


Controller unchanged
Service unchanged
Repository implementation changed
```

---

# Day 31 Completion Checklist

## Part A

- [ ] Explain JDBC
- [ ] Explain JDBC driver
- [ ] Explain `Connection`
- [ ] Explain `Statement`
- [ ] Explain `ResultSet`
- [ ] Explain `rs.next()`
- [ ] Explain `execute()`
- [ ] Explain `executeQuery()`
- [ ] Explain `executeUpdate()`
- [ ] Use try-with-resources
- [ ] Explain `SQLException`
- [ ] Run raw H2 JDBC mini-lab
- [ ] Intentionally break SQL or a constraint

## Part B

- [ ] Add `spring-boot-starter-jdbc`
- [ ] Add H2 runtime dependency
- [ ] Configure `DataSource`
- [ ] Explain `DataSource`
- [ ] Explain HikariCP
- [ ] Explain connection pooling
- [ ] Explain pooled `connection.close()`
- [ ] Create `schema.sql`
- [ ] Create `data.sql`
- [ ] Replace `Map<Long,Payment>` with `JdbcTemplate`
- [ ] Implement `findAll()`
- [ ] Implement `findById()`
- [ ] Implement `findPositivePayments()`
- [ ] Implement `findPaymentsByType()`
- [ ] Implement Payment row mapping
- [ ] Explain `RowMapper`
- [ ] Explain `DataAccessException`
- [ ] Verify existing Payment endpoints
- [ ] Intentionally break one SQL query or enum mapping
- [ ] Explain why controller/service stayed unchanged

---

# Final Mental Model

```text
HTTP request
↓
PaymentController
↓
PaymentService
↓
PaymentRepository
↓
JdbcTemplate
↓
DataSource
↓
HikariCP
↓
JDBC Connection
↓
H2
↓
ResultSet
↓
RowMapper
↓
Payment
↓
PaymentMapper
↓
PaymentResponse
↓
JSON
```

Most important Day 31 lesson:

```text
Raw JDBC exposes the lifecycle.

JdbcTemplate manages the lifecycle.

The database concepts remain the same.
```
