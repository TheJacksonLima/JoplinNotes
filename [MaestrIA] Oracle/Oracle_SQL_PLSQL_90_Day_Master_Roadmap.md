# Oracle SQL + PL/SQL — 90-Day Master Roadmap

> **Developer-focused Oracle mastery track.** This roadmap is designed to run alongside the Java/Spring roadmap while React is paused. It emphasizes hands-on Oracle SQL, PL/SQL, performance, high-volume processing, and production troubleshooting rather than DBA administration.

**Cadence:** ~45–75 min per study day • 6 days/week • Sunday off  
**SQL sharpening rule:** most days include at least **20–30 minutes of actual SQL writing**.  
**Completion rule:** a topic is done only when you can **explain + implement + diagnose/review** it without relying on memorized snippets.  
**Session outputs:** detailed Joplin note + concise manual notes + Anki TSV (`Front`, `Back`, `Tags`) + saved SQL/PLSQL lab script.

## Roadmap sources

- Your existing Java/Spring roadmap structure: `TheJacksonLima/JoplinNotes/[MaestrIA] Java_Spring boot/Roadmap.md`
- Oracle Database SQL Language Reference
- Oracle Database PL/SQL Language Reference
- Oracle Database Performance Tuning Guide
- Oracle Database VLDB and Partitioning Guide
- *Oracle SQL by Example* — Alice Rischert (exercise-driven SQL companion)
- *Oracle PL/SQL Programming* — Steven Feuerstein & Bill Pribyl (reference)
- *Expert Oracle Database Architecture* — Thomas Kyte / Darl Kuhn lineage (architecture/performance reference)

## Why this roadmap is structured this way

This plan deliberately starts with **exercise-driven SQL sharpening**, then moves through PL/SQL, optimizer/indexing, partitioning/high-volume loading, and production engineering. It also gives extra weight to the exact advanced topics that surfaced as improvement areas in recent Oracle/ODI technical feedback:

- Global Temporary Tables (GTTs)
- `WITH` / CTEs
- Partitioning types
- Partition pruning
- Partition exchange
- B-tree / bitmap / composite / function-based / local-global indexing
- `PARALLEL` and `APPEND`
- Collection types
- `BULK COLLECT`, `FORALL`, `LIMIT`, bulk exceptions
- REF CURSOR / `SYS_REFCURSOR`
- High-volume batch design, restartability, idempotency and tuning

## Scope

### Hands-on / mastery target

- Oracle SQL query solving
- Advanced SQL / analytics
- PL/SQL packages and APIs
- Collections and bulk processing
- Execution plans and optimizer reasoning
- Index design
- Partitioning and partition maintenance
- High-volume loading
- Transactions, locking and recovery
- Production troubleshooting

### Recognition-only for this cycle

- RAC administration
- Data Guard administration
- RMAN / backup administration
- ASM
- Database installation/patching administration
- Deep CBO internals
- Deep storage-engine internals

The target is **Senior Oracle Developer + Java Backend Engineer**, not Oracle DBA.

## Calendar rule

Day 1 is anchored to **2026-10-06**. Study days run Monday–Saturday; Sundays are rest days and are not numbered. Day 90 lands on **2027-01-18** if no sessions slip. If a session slips, move the sequence forward rather than cramming multiple mastery sessions into one day.

## Practice environment

Keep one permanent local Oracle practice database and one version-controlled lab repository. Recommended schema families:

```text
DEPARTMENT / EMPLOYEE
CUSTOMER / ACCOUNT
ORDERS / ORDER_ITEM
TRANSACTION_STAGE / TRANSACTION / TRANSACTION_ERROR / BATCH_RUN
```

The lab must grow with the roadmap: start tiny for correctness, then generate hundreds of thousands or millions of rows so execution plans, indexing, partitioning and load strategies become meaningful.

## Master index — Day 01 to Day 90

| Day | Status | Date | Phase | SQL track | Oracle / PL/SQL track | Note |
|---:|---|---|---|---|---|---|
| 01 | ⬜ Upcoming | 2026-10-06 | P1 — SQL Sharpness & Advanced Querying | Oracle Free local environment; SQL Developer/CLI | Schema design and repeatable practice dataset | — |
| 02 | ⬜ Upcoming | 2026-10-07 | P1 — SQL Sharpness & Advanced Querying | SELECT, WHERE, ORDER BY, NULL semantics | Oracle comparison and null behavior | — |
| 03 | ⬜ Upcoming | 2026-10-08 | P1 — SQL Sharpness & Advanced Querying | INNER, LEFT, RIGHT, FULL, self join | Join correctness before performance | — |
| 04 | ⬜ Upcoming | 2026-10-09 | P1 — SQL Sharpness & Advanced Querying | GROUP BY, HAVING, COUNT/SUM/AVG, CASE aggregates | Aggregate correctness | — |
| 05 | ⬜ Upcoming | 2026-10-10 | P1 — SQL Sharpness & Advanced Querying | Scalar, multi-row, correlated subqueries | Query decomposition | — |
| 06 | ⬜ Upcoming | 2026-10-12 | P1 — SQL Sharpness & Advanced Querying | EXISTS/NOT EXISTS, UNION, UNION ALL, INTERSECT, MINUS | Existence and set logic | — |
| 07 | ⬜ Upcoming | 2026-10-13 | P1 — SQL Sharpness & Advanced Querying | Subquery factoring with WITH | Readable multi-step SQL | — |
| 08 | ⬜ Upcoming | 2026-10-14 | P1 — SQL Sharpness & Advanced Querying | INSERT, UPDATE, DELETE, MERGE | Atomic changes and safe testing | — |
| 09 | ⬜ Upcoming | 2026-10-15 | P1 — SQL Sharpness & Advanced Querying | Ranking analytics | Partitioned ranking | — |
| 10 | ⬜ Upcoming | 2026-10-16 | P1 — SQL Sharpness & Advanced Querying | Previous/next-row analytics | Sequence comparison | — |
| 11 | ⬜ Upcoming | 2026-10-17 | P1 — SQL Sharpness & Advanced Querying | SUM/AVG/COUNT OVER; frames | Running and moving calculations | — |
| 12 | ⬜ Upcoming | 2026-10-19 | P1 — SQL Sharpness & Advanced Querying | ROW_NUMBER, ROWID, sequence grouping | Oracle duplicate handling | — |
| 13 | ⬜ Upcoming | 2026-10-20 | P1 — SQL Sharpness & Advanced Querying | DATE/TIMESTAMP, TRUNC, ADD_MONTHS, string functions | Oracle expression fluency | — |
| 14 | ⬜ Upcoming | 2026-10-21 | P1 — SQL Sharpness & Advanced Querying | PIVOT/UNPIVOT, conditional aggregation | Report reshaping | — |
| 15 | ⬜ Upcoming | 2026-10-22 | P1 — SQL Sharpness & Advanced Querying | CONNECT BY, LEVEL, START WITH; recursive-style thinking | Tree traversal | — |
| 16 | ⬜ Upcoming | 2026-10-23 | P1 — SQL Sharpness & Advanced Querying | GTT creation and use | Transaction vs session temporary data | — |
| 17 | ⬜ Upcoming | 2026-10-24 | P1 — SQL Sharpness & Advanced Querying | Mixed advanced SQL | Timed query problem solving | — |
| 18 | ⬜ Upcoming | 2026-10-26 | P1 — SQL Sharpness & Advanced Querying | SQL design and reasoning | Review and refactor | — |
| 19 | ⬜ Upcoming | 2026-10-27 | P2 — PL/SQL Programming & Bulk Processing | SQL vs PL/SQL engines | SGA, PGA, shared pool, buffer cache | — |
| 20 | ⬜ Upcoming | 2026-10-28 | P2 — PL/SQL Programming & Bulk Processing | DECLARE/BEGIN/EXCEPTION/END | %TYPE, %ROWTYPE, records | — |
| 21 | ⬜ Upcoming | 2026-10-29 | P2 — PL/SQL Programming & Bulk Processing | IF/CASE, LOOP/WHILE/FOR | Procedural logic discipline | — |
| 22 | ⬜ Upcoming | 2026-10-30 | P2 — PL/SQL Programming & Bulk Processing | CREATE PROCEDURE; IN/OUT/IN OUT | API contracts | — |
| 23 | ⬜ Upcoming | 2026-10-31 | P2 — PL/SQL Programming & Bulk Processing | Functions in PL/SQL/SQL | Deterministic behavior and caching | — |
| 24 | ⬜ Upcoming | 2026-11-02 | P2 — PL/SQL Programming & Bulk Processing | Package spec/body | Public API, private implementation, state | — |
| 25 | ⬜ Upcoming | 2026-11-03 | P2 — PL/SQL Programming & Bulk Processing | Predefined, user-defined, propagation | RAISE, RAISE_APPLICATION_ERROR | — |
| 26 | ⬜ Upcoming | 2026-11-04 | P2 — PL/SQL Programming & Bulk Processing | OPEN/FETCH/CLOSE; cursor FOR loop | Row iteration | — |
| 27 | ⬜ Upcoming | 2026-11-05 | P2 — PL/SQL Programming & Bulk Processing | Parameterized cursor design | %FOUND/%NOTFOUND/%ROWCOUNT/%ISOPEN | — |
| 28 | ⬜ Upcoming | 2026-11-06 | P2 — PL/SQL Programming & Bulk Processing | Weak/strong REF CURSOR | Returning result sets to clients | — |
| 29 | ⬜ Upcoming | 2026-11-07 | P2 — PL/SQL Programming & Bulk Processing | Associative array, nested table, VARRAY | In-memory data structures | — |
| 30 | ⬜ Upcoming | 2026-11-09 | P2 — PL/SQL Programming & Bulk Processing | Index-by collections | Fast PL/SQL lookup structures | — |
| 31 | ⬜ Upcoming | 2026-11-10 | P2 — PL/SQL Programming & Bulk Processing | SQL-capable collection concepts | Unbounded vs bounded collections | — |
| 32 | ⬜ Upcoming | 2026-11-11 | P2 — PL/SQL Programming & Bulk Processing | Bulk fetching | Context switching and PGA | — |
| 33 | ⬜ Upcoming | 2026-11-12 | P2 — PL/SQL Programming & Bulk Processing | Controlled batch fetching | PGA-safe batching | — |
| 34 | ⬜ Upcoming | 2026-11-13 | P2 — PL/SQL Programming & Bulk Processing | Bulk DML | Reducing PL/SQL-to-SQL switches | — |
| 35 | ⬜ Upcoming | 2026-11-14 | P2 — PL/SQL Programming & Bulk Processing | FORALL SAVE EXCEPTIONS | SQL%BULK_EXCEPTIONS | — |
| 36 | ⬜ Upcoming | 2026-11-16 | P2 — PL/SQL Programming & Bulk Processing | EXECUTE IMMEDIATE; binds | Dynamic structure vs data values | — |
| 37 | ⬜ Upcoming | 2026-11-17 | P3 — Optimizer, Execution Plans & Indexing | Cost-based optimizer | Parsing, optimization, execution | — |
| 38 | ⬜ Upcoming | 2026-11-18 | P3 — Optimizer, Execution Plans & Indexing | Plan reading basics | Access paths and predicates | — |
| 39 | ⬜ Upcoming | 2026-11-19 | P3 — Optimizer, Execution Plans & Indexing | DISPLAY_CURSOR concepts | Estimated vs actual rows | — |
| 40 | ⬜ Upcoming | 2026-11-20 | P3 — Optimizer, Execution Plans & Indexing | Optimizer estimates | DBMS_STATS concepts | — |
| 41 | ⬜ Upcoming | 2026-11-21 | P3 — Optimizer, Execution Plans & Indexing | Column distribution awareness | Skew-sensitive optimization | — |
| 42 | ⬜ Upcoming | 2026-11-23 | P3 — Optimizer, Execution Plans & Indexing | Core Oracle indexing | Range and equality access | — |
| 43 | ⬜ Upcoming | 2026-11-24 | P3 — Optimizer, Execution Plans & Indexing | Multi-column indexes | Predicate order and access paths | — |
| 44 | ⬜ Upcoming | 2026-11-25 | P3 — Optimizer, Execution Plans & Indexing | Indexed expressions | Sargability and expression matching | — |
| 45 | ⬜ Upcoming | 2026-11-26 | P3 — Optimizer, Execution Plans & Indexing | Bitmap index concepts | Read-heavy low-cardinality workloads | — |
| 46 | ⬜ Upcoming | 2026-11-27 | P3 — Optimizer, Execution Plans & Indexing | Nested loop joins | Small driving set + indexed lookup | — |
| 47 | ⬜ Upcoming | 2026-11-28 | P3 — Optimizer, Execution Plans & Indexing | Hash joins, merge joins | Large-set join strategies | — |
| 48 | ⬜ Upcoming | 2026-11-30 | P3 — Optimizer, Execution Plans & Indexing | Hard parse vs soft parse | Shared pool pressure | — |
| 49 | ⬜ Upcoming | 2026-12-01 | P3 — Optimizer, Execution Plans & Indexing | Bind-sensitive plan concepts | Adaptive cursor awareness | — |
| 50 | ⬜ Upcoming | 2026-12-02 | P3 — Optimizer, Execution Plans & Indexing | Optimizer hints | When hints help and when they hide problems | — |
| 51 | ⬜ Upcoming | 2026-12-03 | P3 — Optimizer, Execution Plans & Indexing | Parallel query concepts | Resource trade-offs | — |
| 52 | ⬜ Upcoming | 2026-12-04 | P3 — Optimizer, Execution Plans & Indexing | INSERT /*+ APPEND */ | High-volume load concepts | — |
| 53 | ⬜ Upcoming | 2026-12-05 | P3 — Optimizer, Execution Plans & Indexing | Implicit conversions, functions, OR patterns | Plan-unfriendly SQL | — |
| 54 | ⬜ Upcoming | 2026-12-07 | P3 — Optimizer, Execution Plans & Indexing | Plan reading and index decisions | Evidence-based tuning | — |
| 55 | ⬜ Upcoming | 2026-12-08 | P4 — Partitioning & High-Volume Data Loading | Why partition; partition keys | Large-table manageability and pruning | — |
| 56 | ⬜ Upcoming | 2026-12-09 | P4 — Partitioning & High-Volume Data Loading | RANGE partitions | Date/time data | — |
| 57 | ⬜ Upcoming | 2026-12-10 | P4 — Partitioning & High-Volume Data Loading | Automatic range extension | Rolling time-series tables | — |
| 58 | ⬜ Upcoming | 2026-12-11 | P4 — Partitioning & High-Volume Data Loading | LIST partitions | Discrete business categories | — |
| 59 | ⬜ Upcoming | 2026-12-12 | P4 — Partitioning & High-Volume Data Loading | HASH partitions | Even distribution | — |
| 60 | ⬜ Upcoming | 2026-12-14 | P4 — Partitioning & High-Volume Data Loading | Range-Hash / Range-List concepts | Two-dimensional organization | — |
| 61 | ⬜ Upcoming | 2026-12-15 | P4 — Partitioning & High-Volume Data Loading | Partition elimination | Predicate design | — |
| 62 | ⬜ Upcoming | 2026-12-16 | P4 — Partitioning & High-Volume Data Loading | Partition-aware indexes | Maintenance trade-offs | — |
| 63 | ⬜ Upcoming | 2026-12-17 | P4 — Partitioning & High-Volume Data Loading | EXCHANGE PARTITION | Metadata-oriented high-volume loading | — |
| 64 | ⬜ Upcoming | 2026-12-18 | P4 — Partitioning & High-Volume Data Loading | ADD/SPLIT/MERGE/TRUNCATE/DROP concepts | Lifecycle management | — |
| 65 | ⬜ Upcoming | 2026-12-19 | P4 — Partitioning & High-Volume Data Loading | CREATE TABLE AS SELECT | Fast intermediate data creation | — |
| 66 | ⬜ Upcoming | 2026-12-21 | P4 — Partitioning & High-Volume Data Loading | MERGE at scale | Staging-to-target synchronization | — |
| 67 | ⬜ Upcoming | 2026-12-22 | P4 — Partitioning & High-Volume Data Loading | Parallel INSERT/UPDATE/MERGE concepts | Controlled resource use | — |
| 68 | ⬜ Upcoming | 2026-12-23 | P4 — Partitioning & High-Volume Data Loading | DELETE vs partition operations | Undo/redo and batch strategy | — |
| 69 | ⬜ Upcoming | 2026-12-24 | P4 — Partitioning & High-Volume Data Loading | Intermediate-data architecture | Memory, persistence, restartability | — |
| 70 | ⬜ Upcoming | 2026-12-25 | P4 — Partitioning & High-Volume Data Loading | High-volume architecture | Explain + implement | — |
| 71 | ⬜ Upcoming | 2026-12-26 | P5 — Transactions, Concurrency & Production Engineering | MVCC/read consistency concepts | Undo and statement consistency | — |
| 72 | ⬜ Upcoming | 2026-12-28 | P5 — Transactions, Concurrency & Production Engineering | DML locks | Wait chains | — |
| 73 | ⬜ Upcoming | 2026-12-29 | P5 — Transactions, Concurrency & Production Engineering | Circular lock dependency | ORA-00060 reasoning | — |
| 74 | ⬜ Upcoming | 2026-12-30 | P5 — Transactions, Concurrency & Production Engineering | Pessimistic locking | NOWAIT/SKIP LOCKED concepts | — |
| 75 | ⬜ Upcoming | 2026-12-31 | P5 — Transactions, Concurrency & Production Engineering | COMMIT/ROLLBACK/SAVEPOINT | Business transaction design | — |
| 76 | ⬜ Upcoming | 2027-01-01 | P5 — Transactions, Concurrency & Production Engineering | Safe batch reruns | Checkpoint design | — |
| 77 | ⬜ Upcoming | 2027-01-02 | P5 — Transactions, Concurrency & Production Engineering | DBMS_APPLICATION_INFO concepts; structured logs | Operational visibility | — |
| 78 | ⬜ Upcoming | 2027-01-04 | P5 — Transactions, Concurrency & Production Engineering | AUTHID DEFINER/CURRENT_USER | Privilege model | — |
| 79 | ⬜ Upcoming | 2027-01-05 | P5 — Transactions, Concurrency & Production Engineering | EXECUTE IMMEDIATE + validation | SQL injection prevention | — |
| 80 | ⬜ Upcoming | 2027-01-06 | P5 — Transactions, Concurrency & Production Engineering | Sequence behavior | Surrogate-key generation | — |
| 81 | ⬜ Upcoming | 2027-01-07 | P5 — Transactions, Concurrency & Production Engineering | Job scheduling concepts | Operational batch execution | — |
| 82 | ⬜ Upcoming | 2027-01-08 | P5 — Transactions, Concurrency & Production Engineering | Concurrency, security, recovery | Incident reasoning | — |
| 83 | ⬜ Upcoming | 2027-01-09 | P6 — Capstone, Tuning & Final Evaluation | Schema/API design | Requirements and workload assumptions | — |
| 84 | ⬜ Upcoming | 2027-01-11 | P6 — Capstone, Tuning & Final Evaluation | Exercise dataset | Volume for real plans | — |
| 85 | ⬜ Upcoming | 2027-01-12 | P6 — Capstone, Tuning & Final Evaluation | PL/SQL bulk processing | Validation and error capture | — |
| 86 | ⬜ Upcoming | 2027-01-13 | P6 — Capstone, Tuning & Final Evaluation | Partitioning and indexes | Monthly transaction storage | — |
| 87 | ⬜ Upcoming | 2027-01-14 | P6 — Capstone, Tuning & Final Evaluation | Partition exchange / APPEND | Batch load path | — |
| 88 | ⬜ Upcoming | 2027-01-15 | P6 — Capstone, Tuning & Final Evaluation | Execution plans | Measure before optimize | — |
| 89 | ⬜ Upcoming | 2027-01-16 | P6 — Capstone, Tuning & Final Evaluation | Idempotency and restart | Production incident simulation | — |
| 90 | ⬜ Upcoming | 2027-01-18 | P6 — Capstone, Tuning & Final Evaluation | SQL + PL/SQL + performance | Explain, implement, tune, recover | — |


## Phase map

```text
Days 01–18
SQL Sharpness & Advanced Querying

Days 19–36
PL/SQL Programming & Bulk Processing

Days 37–54
Optimizer, Execution Plans & Indexing

Days 55–70
Partitioning & High-Volume Data Loading

Days 71–82
Transactions, Concurrency & Production Engineering

Days 83–90
Capstone, Tuning & Final Evaluation
```

---

# Phase 1 — SQL Sharpness & Advanced Querying

## Day 01 — Local Oracle Lab + Practice Schema ⬜

**Planned date:** 2026-10-06  
**SQL track:** Oracle Free local environment; SQL Developer/CLI  
**Oracle / PL/SQL track:** Schema design and repeatable practice dataset

### Tutorial

Set up a permanent Oracle playground. The goal is to have one database you can break, rebuild, index, partition, and tune repeatedly. Create a dedicated schema and keep every exercise in Git so the lab becomes part of your long-term Oracle toolkit.

### Runnable mini-lab

Start Oracle locally, connect as your practice user, and create `DEPARTMENT`, `EMPLOYEE`, `CUSTOMER`, `ACCOUNT`, `ORDERS`, `ORDER_ITEM`, and `TRANSACTION_STAGE`. Insert at least 20 hand-written rows so you can immediately query them.

### Verify

Reconnect from a fresh client session, list your tables, run one join, and prove you can drop/recreate the schema without touching SYSTEM-owned objects.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 02 — SELECT, Filtering, NULL & Ordering ⬜

**Planned date:** 2026-10-07  
**SQL track:** SELECT, WHERE, ORDER BY, NULL semantics  
**Oracle / PL/SQL track:** Oracle comparison and null behavior

### Tutorial

Refresh the query pipeline before moving into advanced SQL. Pay particular attention to three-valued logic: `NULL` is not equal to anything, including another `NULL`, and predicates involving nulls can become UNKNOWN.

### Runnable mini-lab

Solve five queries on your lab tables: range filters, multiple predicates, `IS NULL`, `IN`, and sorted output. Include one intentionally wrong query using `= NULL`, explain why it fails, then fix it.

### Verify

You can explain logical query processing at a high level and correctly predict how NULL affects `=`, `<>`, `NOT IN`, and boolean predicates.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 03 — JOIN Mastery ⬜

**Planned date:** 2026-10-08  
**SQL track:** INNER, LEFT, RIGHT, FULL, self join  
**Oracle / PL/SQL track:** Join correctness before performance

### Tutorial

A senior Oracle developer should be able to read joins as relationships, not syntax. Refresh matching vs preserving rows, then practice identifying accidental row multiplication caused by incomplete join predicates.

### Runnable mini-lab

Write: employees with departments, all departments including empty ones, customers with and without orders, and a self-join showing employee/manager pairs. Create one bad join that multiplies rows and diagnose it.

### Verify

You can choose the join type from the business requirement and explain why a query duplicates rows.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 04 — GROUP BY, HAVING & Conditional Aggregation ⬜

**Planned date:** 2026-10-09  
**SQL track:** GROUP BY, HAVING, COUNT/SUM/AVG, CASE aggregates  
**Oracle / PL/SQL track:** Aggregate correctness

### Tutorial

Aggregation collapses rows into groups. Keep the distinction sharp: `WHERE` filters rows before grouping, while `HAVING` filters groups after aggregation.

### Runnable mini-lab

Write reports for employee count and average salary per department, departments above a threshold, order totals per customer, and conditional counts such as approved vs rejected transactions using `SUM(CASE...)`.

### Verify

You can explain why every selected non-aggregate expression must be compatible with the grouping and when HAVING is necessary.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 05 — Subqueries & Correlated Subqueries ⬜

**Planned date:** 2026-10-10  
**SQL track:** Scalar, multi-row, correlated subqueries  
**Oracle / PL/SQL track:** Query decomposition

### Tutorial

Use subqueries when the result of one query becomes a condition or value for another. Correlated subqueries are evaluated in relation to the outer row and are common in interview problems and legacy Oracle SQL.

### Runnable mini-lab

Solve: employees above company average, employees above department average, customers whose latest order exceeds a threshold, and departments containing the highest-paid employee.

### Verify

You can identify whether a subquery is correlated and rewrite at least one correlated solution using a join or analytic function.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 06 — EXISTS, NOT EXISTS & Set Operators ⬜

**Planned date:** 2026-10-12  
**SQL track:** EXISTS/NOT EXISTS, UNION, UNION ALL, INTERSECT, MINUS  
**Oracle / PL/SQL track:** Existence and set logic

### Tutorial

`EXISTS` asks whether a matching row exists; it does not need the returned value. Learn the `NOT IN` + NULL trap and practice Oracle's `MINUS` operator.

### Runnable mini-lab

Find customers with orders, customers without orders, employees present in one dataset but not another, and compare `UNION` vs `UNION ALL` row counts.

### Verify

You can explain why `NOT EXISTS` is often safer than `NOT IN` when nulls are possible.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 07 — WITH Clause / CTEs ⬜

**Planned date:** 2026-10-13  
**SQL track:** Subquery factoring with WITH  
**Oracle / PL/SQL track:** Readable multi-step SQL

### Tutorial

A CTE names an intermediate query and can make complex SQL easier to reason about. Treat it primarily as a query-structure tool; do not assume it always materializes.

### Runnable mini-lab

Refactor a three-level nested query into two CTEs. Build a CTE that calculates customer totals and a second that ranks customers. Compare the final query with the equivalent inline-view version.

### Verify

You can explain CTE vs inline view and avoid claiming that a CTE is automatically a temporary table.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 08 — DML, MERGE & Transaction Basics ⬜

**Planned date:** 2026-10-14  
**SQL track:** INSERT, UPDATE, DELETE, MERGE  
**Oracle / PL/SQL track:** Atomic changes and safe testing

### Tutorial

Refresh DML with a production mindset. `MERGE` is especially important for staging-to-target synchronization because it can combine matched updates and not-matched inserts.

### Runnable mini-lab

Create a small staging table and `MERGE` it into a target. Test update-only, insert-only, and mixed batches. Roll back once, then commit once.

### Verify

You can explain what MERGE does and verify exactly which rows changed before committing.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 09 — Analytic Functions I — ROW_NUMBER, RANK, DENSE_RANK ⬜

**Planned date:** 2026-10-15  
**SQL track:** Ranking analytics  
**Oracle / PL/SQL track:** Partitioned ranking

### Tutorial

Analytic functions compute across related rows without collapsing them. `ROW_NUMBER` is unique, `RANK` leaves gaps after ties, and `DENSE_RANK` does not.

### Runnable mini-lab

Solve: global score ranking, highest salary per department, top three distinct salaries per department, and exactly one latest row per customer.

### Verify

Without notes, explain `PARTITION BY` and correctly choose among the three ranking functions.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 10 — Analytic Functions II — LAG & LEAD ⬜

**Planned date:** 2026-10-16  
**SQL track:** Previous/next-row analytics  
**Oracle / PL/SQL track:** Sequence comparison

### Tutorial

`LAG` and `LEAD` let you compare a row with prior or following rows according to a defined order, avoiding many self-joins.

### Runnable mini-lab

Solve: previous transaction amount, day-over-day balance change, consecutive values, and time gap between customer transactions.

### Verify

You can explain why the analytic `ORDER BY` defines what 'previous' and 'next' mean.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 11 — Analytic Functions III — Window Aggregates ⬜

**Planned date:** 2026-10-17  
**SQL track:** SUM/AVG/COUNT OVER; frames  
**Oracle / PL/SQL track:** Running and moving calculations

### Tutorial

Window aggregates preserve detail rows while adding group or running metrics. Learn the difference between partition totals and ordered window frames.

### Runnable mini-lab

Build running account balance, cumulative monthly revenue, moving three-transaction average, and percentage of department payroll.

### Verify

You can explain `ROWS BETWEEN` at a practical level and distinguish a partition total from a running total.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 12 — Duplicates, ROWID & Gaps-and-Islands ⬜

**Planned date:** 2026-10-19  
**SQL track:** ROW_NUMBER, ROWID, sequence grouping  
**Oracle / PL/SQL track:** Oracle duplicate handling

### Tutorial

Oracle `ROWID` identifies a physical row location and is useful for controlled duplicate cleanup. Pair it with analytics when the business key is duplicated.

### Runnable mini-lab

Using duplicated `(empid, empname)` rows, write two delete strategies: one based on `ROWID`/`MIN(ROWID)` and another using `ROW_NUMBER()`. Also solve one gaps-and-islands sequence problem.

### Verify

You can delete duplicates while deliberately preserving one row and explain why a business key alone cannot identify which physical duplicate to remove.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 13 — Oracle Date, String & NULL Functions ⬜

**Planned date:** 2026-10-20  
**SQL track:** DATE/TIMESTAMP, TRUNC, ADD_MONTHS, string functions  
**Oracle / PL/SQL track:** Oracle expression fluency

### Tutorial

Refresh the functions that appear constantly in production reporting and ETL SQL. Practice date ranges without wrapping indexed columns unnecessarily.

### Runnable mini-lab

Write month filters, age calculations, month-end dates, normalized names, substring extraction, `NVL`, `COALESCE`, and a sargable date-range query.

### Verify

You avoid `TO_CHAR(date_column,...)` when a direct date range expresses the same filter and preserves better access options.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 14 — PIVOT, UNPIVOT & Reporting SQL ⬜

**Planned date:** 2026-10-21  
**SQL track:** PIVOT/UNPIVOT, conditional aggregation  
**Oracle / PL/SQL track:** Report reshaping

### Tutorial

Oracle supports native PIVOT/UNPIVOT, but conditional aggregation is often more portable and flexible. Know both.

### Runnable mini-lab

Produce monthly order totals as columns using PIVOT, then rewrite using `SUM(CASE...)`. Unpivot a small quarterly table into rows.

### Verify

You can choose between PIVOT and conditional aggregation without treating either as universally better.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 15 — Hierarchical SQL ⬜

**Planned date:** 2026-10-22  
**SQL track:** CONNECT BY, LEVEL, START WITH; recursive-style thinking  
**Oracle / PL/SQL track:** Tree traversal

### Tutorial

Oracle's hierarchical querying is common in organization trees, bill-of-materials, and legacy systems. Learn `START WITH`, `CONNECT BY`, and `LEVEL`.

### Runnable mini-lab

Build a manager hierarchy from `EMPLOYEE(manager_id)`, show depth with `LEVEL`, and return the path from CEO to employee.

### Verify

You can explain parent-child traversal and detect where a cycle would be dangerous.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 16 — Global Temporary Tables (GTTs) ⬜

**Planned date:** 2026-10-23  
**SQL track:** GTT creation and use  
**Oracle / PL/SQL track:** Transaction vs session temporary data

### Tutorial

A Global Temporary Table has a persistent definition while its rows are temporary to the session. `ON COMMIT DELETE ROWS` gives transaction-duration rows; `ON COMMIT PRESERVE ROWS` keeps them for the session.

### Runnable mini-lab

Create both GTT variants. Insert rows, commit, and observe behavior. Compare using a GTT with using a CTE and a PL/SQL collection for multi-step processing.

### Verify

You can answer: 'When would you use a GTT instead of a CTE, collection, or permanent staging table?'

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 17 — SQL Workout — Mixed Set I ⬜

**Planned date:** 2026-10-24  
**SQL track:** Mixed advanced SQL  
**Oracle / PL/SQL track:** Timed query problem solving

### Tutorial

Consolidate the first sixteen days. This is a retrieval day, not a new-content day.

### Runnable mini-lab

Solve eight timed problems: two joins, one aggregation, one correlated subquery, one CTE, one ranking problem, one LAG/LEAD problem, and one duplicate cleanup. Do not use notes for the first attempt.

### Verify

Score each problem Green/Yellow/Red and write one sentence explaining every mistake.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 18 — SQL Phase Assessment ⬜

**Planned date:** 2026-10-26  
**SQL track:** SQL design and reasoning  
**Oracle / PL/SQL track:** Review and refactor

### Tutorial

Close Phase 1 by proving you can write and explain SQL without pattern matching blindly.

### Runnable mini-lab

Take a 60-minute assessment: five SQL problems plus one code-review query containing a bad join, non-sargable predicate, and incorrect NOT IN. Refactor it.

### Verify

At least 80% correct without notes; anything Red becomes a scheduled review item in Phase 2.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

# Phase 2 — PL/SQL Programming & Bulk Processing

## Day 19 — Oracle Process & Memory Model for Developers ⬜

**Planned date:** 2026-10-27  
**SQL track:** SQL vs PL/SQL engines  
**Oracle / PL/SQL track:** SGA, PGA, shared pool, buffer cache

### Tutorial

Learn enough Oracle architecture to reason about PL/SQL performance. SGA is shared instance memory; PGA is private process work memory. SQL and PL/SQL execution can cross engine boundaries.

### Runnable mini-lab

Run a few SQL statements and inspect basic session information available to your account. Write a short diagram connecting shared pool, buffer cache, PGA, SQL engine, and PL/SQL engine.

### Verify

You can explain why a huge PL/SQL collection pressures PGA and why repeated hard parsing affects shared structures.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 20 — PL/SQL Blocks, Variables & Anchored Types ⬜

**Planned date:** 2026-10-28  
**SQL track:** DECLARE/BEGIN/EXCEPTION/END  
**Oracle / PL/SQL track:** %TYPE, %ROWTYPE, records

### Tutorial

Refresh block structure and anchored types so code follows database definitions instead of duplicating datatypes manually.

### Runnable mini-lab

Write an anonymous block that fetches an employee using `%ROWTYPE`, calculates a bonus, and handles no-data and too-many-row cases.

### Verify

You can explain `%TYPE` vs `%ROWTYPE` and compile the block without looking up syntax.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 21 — Control Flow & Loops ⬜

**Planned date:** 2026-10-29  
**SQL track:** IF/CASE, LOOP/WHILE/FOR  
**Oracle / PL/SQL track:** Procedural logic discipline

### Tutorial

PL/SQL is strongest when procedural control wraps set-based SQL rather than replacing it. Learn control flow while resisting unnecessary row-by-row processing.

### Runnable mini-lab

Write examples of IF, CASE, numeric FOR, cursor FOR, and EXIT WHEN. Then rewrite one loop-based aggregate as a single SQL statement.

### Verify

You can identify when procedural looping is unnecessary because SQL can do the work set-wise.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 22 — Procedures & Parameter Modes ⬜

**Planned date:** 2026-10-30  
**SQL track:** CREATE PROCEDURE; IN/OUT/IN OUT  
**Oracle / PL/SQL track:** API contracts

### Tutorial

Procedures encapsulate actions. Parameter modes are part of the contract; OUT and IN OUT should be used deliberately rather than as substitutes for clear return structures.

### Runnable mini-lab

Create `transfer_funds(p_from, p_to, p_amount, p_status OUT)` with validation but no internal COMMIT. Test success and failure.

### Verify

You can explain why transaction ownership often belongs to the caller/service boundary.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 23 — Functions, Determinism & Result Cache ⬜

**Planned date:** 2026-10-31  
**SQL track:** Functions in PL/SQL/SQL  
**Oracle / PL/SQL track:** Deterministic behavior and caching

### Tutorial

Functions return values and may be callable from SQL subject to restrictions. Study `DETERMINISTIC` semantics and the PL/SQL function `RESULT_CACHE` at a practical level.

### Runnable mini-lab

Create a calculation function, call it from SQL, then create a safe result-cache example based on relatively stable lookup data. Observe repeated calls.

### Verify

You can explain function vs procedure and why caching mutable/high-churn data may have limited value.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 24 — Packages & Encapsulation ⬜

**Planned date:** 2026-11-02  
**SQL track:** Package spec/body  
**Oracle / PL/SQL track:** Public API, private implementation, state

### Tutorial

Packages are a major unit of PL/SQL design. Separate public contracts in the specification from implementation details in the body.

### Runnable mini-lab

Create `ACCOUNT_PKG` with public `deposit`, `withdraw`, and `get_balance`; keep validation helpers private. Add one overloaded operation.

### Verify

You can explain package spec vs body and identify what callers can access.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 25 — Exception Handling ⬜

**Planned date:** 2026-11-03  
**SQL track:** Predefined, user-defined, propagation  
**Oracle / PL/SQL track:** RAISE, RAISE_APPLICATION_ERROR

### Tutorial

Handle expected exceptions intentionally and let unexpected failures remain visible. `WHEN OTHERS THEN NULL` is usually a production defect.

### Runnable mini-lab

Create a procedure with `NO_DATA_FOUND`, `DUP_VAL_ON_INDEX`, a custom exception, `RAISE_APPLICATION_ERROR`, and a final handler that logs context and re-raises.

### Verify

You can explain when to handle, translate, or re-raise an exception.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 26 — Explicit Cursors ⬜

**Planned date:** 2026-11-04  
**SQL track:** OPEN/FETCH/CLOSE; cursor FOR loop  
**Oracle / PL/SQL track:** Row iteration

### Tutorial

Explicit cursors expose query iteration. Understand them well even though bulk or set-based processing is often better for large workloads.

### Runnable mini-lab

Implement the same report once using OPEN/FETCH/CLOSE and once using a cursor FOR loop. Compare code and cursor attributes.

### Verify

You can explain implicit vs explicit cursors and the convenience of cursor FOR loops.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 27 — Parameterized Cursors & Cursor Attributes ⬜

**Planned date:** 2026-11-05  
**SQL track:** Parameterized cursor design  
**Oracle / PL/SQL track:** %FOUND/%NOTFOUND/%ROWCOUNT/%ISOPEN

### Tutorial

Parameterized cursors make reusable query iteration possible while keeping the query static and bind-friendly.

### Runnable mini-lab

Create a parameterized cursor by department and print employees. Track `%ROWCOUNT`, and intentionally demonstrate `%NOTFOUND` correctly.

### Verify

You can explain each cursor attribute and avoid fetching after end-of-data.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 28 — REF CURSOR & SYS_REFCURSOR ⬜

**Planned date:** 2026-11-06  
**SQL track:** Weak/strong REF CURSOR  
**Oracle / PL/SQL track:** Returning result sets to clients

### Tutorial

REF CURSORs provide handles to query result sets. `SYS_REFCURSOR` is the common weak cursor type used to return data to Java/.NET clients and APIs.

### Runnable mini-lab

Create `get_transactions(p_account_id, p_rc OUT SYS_REFCURSOR)`. Consume it from a PL/SQL block or SQL client. Sketch how JDBC would receive the cursor.

### Verify

You can explain regular cursor vs REF CURSOR and weak vs strong typing.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 29 — Collections Overview ⬜

**Planned date:** 2026-11-07  
**SQL track:** Associative array, nested table, VARRAY  
**Oracle / PL/SQL track:** In-memory data structures

### Tutorial

Know the three collection families and their trade-offs instead of memorizing syntax only.

### Runnable mini-lab

Declare one of each collection type, populate values, iterate them, and test collection methods such as COUNT, FIRST, LAST, EXISTS, EXTEND, DELETE where applicable.

### Verify

You can compare associative array, nested table, and VARRAY from memory.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 30 — Associative Arrays ⬜

**Planned date:** 2026-11-09  
**SQL track:** Index-by collections  
**Oracle / PL/SQL track:** Fast PL/SQL lookup structures

### Tutorial

Associative arrays are flexible PL/SQL collections indexed by integer or string keys and are excellent for in-memory lookup and batch processing.

### Runnable mini-lab

Build an associative array keyed by employee ID and another keyed by code. Use EXISTS before access and delete selected entries.

### Verify

You can explain why associative arrays are useful inside PL/SQL but are not simply database tables.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 31 — Nested Tables & VARRAYs ⬜

**Planned date:** 2026-11-10  
**SQL track:** SQL-capable collection concepts  
**Oracle / PL/SQL track:** Unbounded vs bounded collections

### Tutorial

Nested tables are variable-size collections; VARRAYs have an explicit maximum and preserve order. Understand both PL/SQL use and SQL object-type use at a high level.

### Runnable mini-lab

Create simple SQL collection types, cast/query a nested collection using `TABLE(...)`, and compare with a bounded VARRAY example.

### Verify

You can choose a collection based on bounded size, ordering, SQL interaction, and PL/SQL-only needs.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 32 — BULK COLLECT ⬜

**Planned date:** 2026-11-11  
**SQL track:** Bulk fetching  
**Oracle / PL/SQL track:** Context switching and PGA

### Tutorial

`BULK COLLECT` reduces SQL-to-PL/SQL context switches by fetching batches into collections. The trade-off is PGA memory.

### Runnable mini-lab

Fetch 10,000 rows using a cursor loop and then using BULK COLLECT. Measure elapsed time roughly and inspect collection count.

### Verify

You can explain the performance benefit and why unbounded bulk fetches can be dangerous.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 33 — BULK COLLECT with LIMIT ⬜

**Planned date:** 2026-11-12  
**SQL track:** Controlled batch fetching  
**Oracle / PL/SQL track:** PGA-safe batching

### Tutorial

`LIMIT` lets you process large result sets in manageable batches rather than loading everything into PGA.

### Runnable mini-lab

Process a large synthetic table in batches of 100, 1,000, and 10,000. Record elapsed time and discuss memory/performance trade-offs.

### Verify

You can choose a batch size experimentally rather than assuming one universal number.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 34 — FORALL ⬜

**Planned date:** 2026-11-13  
**SQL track:** Bulk DML  
**Oracle / PL/SQL track:** Reducing PL/SQL-to-SQL switches

### Tutorial

`FORALL` sends collections of DML operations to SQL more efficiently than executing one statement per loop iteration.

### Runnable mini-lab

Fetch keys into a collection and update them using FORALL. Compare with a row-by-row update loop. Include INSERT and DELETE variants.

### Verify

You can explain BULK COLLECT as bulk read and FORALL as bulk DML.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 35 — SAVE EXCEPTIONS & Bulk Error Handling ⬜

**Planned date:** 2026-11-14  
**SQL track:** FORALL SAVE EXCEPTIONS  
**Oracle / PL/SQL track:** SQL%BULK_EXCEPTIONS

### Tutorial

High-volume processing needs error isolation. `SAVE EXCEPTIONS` allows FORALL to continue and report individual failures afterward.

### Runnable mini-lab

Create a batch with deliberately invalid rows, run FORALL SAVE EXCEPTIONS, inspect `SQL%BULK_EXCEPTIONS`, and store failures in an error table.

### Verify

You can recover row-level failure details without losing the entire batch's useful work.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 36 — Dynamic SQL + PL/SQL Phase Assessment ⬜

**Planned date:** 2026-11-16  
**SQL track:** EXECUTE IMMEDIATE; binds  
**Oracle / PL/SQL track:** Dynamic structure vs data values

### Tutorial

Dynamic SQL is appropriate when SQL structure is unknown until runtime, but data values should still be bound. Finish the phase by combining packages, cursors, collections, and error handling.

### Runnable mini-lab

Build a package procedure that accepts a validated table name for a maintenance operation and binds all data values. Then complete a 45-minute PL/SQL assessment covering package, REF CURSOR, collection, and bulk processing.

### Verify

You can explain SQL-injection risk, use `USING` binds, and score at least 80% on the PL/SQL assessment.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

# Phase 3 — Optimizer, Execution Plans & Indexing

## Day 37 — Optimizer Mental Model ⬜

**Planned date:** 2026-11-17  
**SQL track:** Cost-based optimizer  
**Oracle / PL/SQL track:** Parsing, optimization, execution

### Tutorial

Understand the optimizer as a cost-based decision maker using metadata and statistics to choose access paths, join order, and algorithms.

### Runnable mini-lab

Take three equivalent queries and capture their plans. Record what Oracle chose and hypothesize why before checking details.

### Verify

You can distinguish SQL text, parse, optimization, execution plan, and actual execution.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 38 — EXPLAIN PLAN & DBMS_XPLAN ⬜

**Planned date:** 2026-11-18  
**SQL track:** Plan reading basics  
**Oracle / PL/SQL track:** Access paths and predicates

### Tutorial

Learn to read plans from the inside out while focusing on row sources, access paths, join methods, predicates, and estimates.

### Runnable mini-lab

Use `EXPLAIN PLAN` and `DBMS_XPLAN.DISPLAY` on five lab queries. Annotate each operation in plain English.

### Verify

You can describe the plan without treating COST as elapsed time.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 39 — Actual Plans & Runtime Statistics ⬜

**Planned date:** 2026-11-19  
**SQL track:** DISPLAY_CURSOR concepts  
**Oracle / PL/SQL track:** Estimated vs actual rows

### Tutorial

Estimated plans are useful, but tuning improves when you compare estimates with what actually happened. Learn the idea behind runtime row-source statistics and `DBMS_XPLAN.DISPLAY_CURSOR`.

### Runnable mini-lab

For a query you control, capture the executed cursor plan where privileges permit. Compare E-Rows and actual rows or document the limitation if your local edition/setup hides a metric.

### Verify

You understand why a large estimate/actual mismatch often points to statistics or data-distribution issues.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 40 — Cardinality, Selectivity & Statistics ⬜

**Planned date:** 2026-11-20  
**SQL track:** Optimizer estimates  
**Oracle / PL/SQL track:** DBMS_STATS concepts

### Tutorial

Cardinality estimates drive many optimizer decisions. Statistics on tables, columns, and indexes help Oracle estimate row counts and selectivity.

### Runnable mini-lab

Create skewed data, gather statistics, and compare plans for selective vs nonselective predicates. Record estimated rows.

### Verify

You can explain stale statistics and why selectivity matters for access-path choice.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 41 — Histograms & Data Skew ⬜

**Planned date:** 2026-11-21  
**SQL track:** Column distribution awareness  
**Oracle / PL/SQL track:** Skew-sensitive optimization

### Tutorial

Uniform assumptions can be poor when values are highly skewed. Histograms are one mechanism Oracle can use to represent distribution.

### Runnable mini-lab

Create a status column with 99% ACTIVE and 1% BLOCKED. Gather stats using an appropriate method and compare plan estimates for each predicate.

### Verify

You can explain why the same column may need different access strategies for different values.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 42 — B-tree Indexes ⬜

**Planned date:** 2026-11-23  
**SQL track:** Core Oracle indexing  
**Oracle / PL/SQL track:** Range and equality access

### Tutorial

B-tree indexes are the default general-purpose index structure. Learn when they help and the write/storage cost they introduce.

### Runnable mini-lab

Create/drop a B-tree index on a selective column and compare execution plans and logical work for a selective query.

### Verify

You can state why 'index = faster' is not universally true.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 43 — Composite Indexes & Leading Columns ⬜

**Planned date:** 2026-11-24  
**SQL track:** Multi-column indexes  
**Oracle / PL/SQL track:** Predicate order and access paths

### Tutorial

Composite indexes support multi-column access, and column order shapes which predicates can efficiently drive the index.

### Runnable mini-lab

Create `(customer_id, transaction_date)` and test customer-only, date-only, and combined predicates. Record plan differences.

### Verify

You can explain the leading-column principle without presenting it as an absolute optimizer law.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 44 — Function-Based Indexes ⬜

**Planned date:** 2026-11-25  
**SQL track:** Indexed expressions  
**Oracle / PL/SQL track:** Sargability and expression matching

### Tutorial

Function-based indexes can support predicates on expressions such as `UPPER(name)`. Prefer rewriting predicates naturally when possible before adding complexity.

### Runnable mini-lab

Compare `UPPER(last_name)=...` before/after a function-based index. Also rewrite a date function predicate as a range and compare.

### Verify

You can identify when an expression prevents a normal index from being useful.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 45 — Bitmap Indexes ⬜

**Planned date:** 2026-11-26  
**SQL track:** Bitmap index concepts  
**Oracle / PL/SQL track:** Read-heavy low-cardinality workloads

### Tutorial

Bitmap indexes can be efficient for low-cardinality dimensions in read-heavy warehouse workloads but are usually poor choices for high-concurrency OLTP DML.

### Runnable mini-lab

On a disposable lab table, create a low-cardinality column and inspect a bitmap-index plan. Contrast conceptually with a B-tree under frequent updates.

### Verify

You can explain bitmap vs B-tree using workload characteristics, not only cardinality.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 46 — Join Methods — Nested Loops ⬜

**Planned date:** 2026-11-27  
**SQL track:** Nested loop joins  
**Oracle / PL/SQL track:** Small driving set + indexed lookup

### Tutorial

Nested loops are often effective when a small outer row set can probe an indexed inner source efficiently.

### Runnable mini-lab

Build a selective join and inspect whether nested loops appears. Change the predicate to return many more rows and observe optimizer changes.

### Verify

You can explain the driving-row concept and why nested loops can become expensive at scale.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 47 — Join Methods — Hash & Merge ⬜

**Planned date:** 2026-11-28  
**SQL track:** Hash joins, merge joins  
**Oracle / PL/SQL track:** Large-set join strategies

### Tutorial

Hash joins are common for large equality joins; merge joins sort/merge compatible row sources and can fit other patterns.

### Runnable mini-lab

Create two medium/large lab tables and compare join plans under selective and broad predicates. Record hash vs nested-loop choices.

### Verify

You can compare the three major join methods at interview level.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 48 — Bind Variables & Cursor Reuse ⬜

**Planned date:** 2026-11-30  
**SQL track:** Hard parse vs soft parse  
**Oracle / PL/SQL track:** Shared pool pressure

### Tutorial

Bind variables allow the same SQL text to be reused across different values, reducing parsing/optimization overhead and shared-pool churn.

### Runnable mini-lab

Execute repeated literal queries and a bind-based equivalent. Observe available cursor information or simply compare generated SQL text and explain reuse.

### Verify

You can explain why binds matter beyond 'making variables easier to change'.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 49 — Bind Peeking & Skew ⬜

**Planned date:** 2026-12-01  
**SQL track:** Bind-sensitive plan concepts  
**Oracle / PL/SQL track:** Adaptive cursor awareness

### Tutorial

At parse time, bind values and skew can influence plan selection. Learn the problem without diving too deeply into optimizer internals.

### Runnable mini-lab

Using your skewed status table, reason about whether one plan fits ACTIVE and BLOCKED equally well. Inspect plans available in your environment.

### Verify

You can describe the bind-peeking problem without claiming one behavior is guaranteed in every Oracle release.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 50 — Hints Fundamentals ⬜

**Planned date:** 2026-12-02  
**SQL track:** Optimizer hints  
**Oracle / PL/SQL track:** When hints help and when they hide problems

### Tutorial

Hints influence optimizer choices but should not replace statistics, schema design, and SQL understanding. Incorrect or inapplicable hints may be ignored.

### Runnable mini-lab

Use `/*+ INDEX */`, `/*+ FULL */`, and one join-method hint on disposable queries. Compare plans and note whether Oracle honored each hint.

### Verify

You treat hints as controlled tools, not magic comments.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 51 — PARALLEL Hint ⬜

**Planned date:** 2026-12-03  
**SQL track:** Parallel query concepts  
**Oracle / PL/SQL track:** Resource trade-offs

### Tutorial

Parallel execution can reduce elapsed time for large operations by consuming more CPU, I/O, and process resources. It is usually inappropriate for tiny queries.

### Runnable mini-lab

Test `/*+ PARALLEL(t,4) */` on a sufficiently large local table if your edition/environment supports useful parallel execution. Compare plan annotations.

### Verify

You can explain why parallelism trades resource consumption for potential latency improvement.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 52 — APPEND Hint & Direct-Path Insert ⬜

**Planned date:** 2026-12-04  
**SQL track:** INSERT /*+ APPEND */  
**Oracle / PL/SQL track:** High-volume load concepts

### Tutorial

Direct-path insert is important for warehouse/batch loading. APPEND can bypass conventional insert paths and has transaction/space/concurrency implications.

### Runnable mini-lab

Create a staging table and compare conventional `INSERT INTO ... SELECT` with `INSERT /*+ APPEND */ ... SELECT` in a disposable target. Inspect plans/behavior available locally.

### Verify

You can explain APPEND as a bulk-load strategy rather than a generic INSERT accelerator.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 53 — SQL Tuning Anti-Patterns ⬜

**Planned date:** 2026-12-05  
**SQL track:** Implicit conversions, functions, OR patterns  
**Oracle / PL/SQL track:** Plan-unfriendly SQL

### Tutorial

Many performance problems start in SQL formulation: implicit datatype conversions, functions on indexed columns, broad OR conditions, unnecessary DISTINCT, or fetching columns you do not need.

### Runnable mini-lab

Take five intentionally poor queries and rewrite them. Capture before/after plans where meaningful.

### Verify

You can identify at least five common SQL formulation problems before reaching for a hint.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 54 — Performance Phase Assessment ⬜

**Planned date:** 2026-12-07  
**SQL track:** Plan reading and index decisions  
**Oracle / PL/SQL track:** Evidence-based tuning

### Tutorial

Finish Phase 3 with an evidence-first tuning exercise.

### Runnable mini-lab

Given three slow queries and schemas, propose changes only after reading the plans. Include one case where the correct answer is 'keep the full table scan'.

### Verify

You can defend each tuning decision using cardinality, selectivity, access paths, or workload—not intuition alone.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

# Phase 4 — Partitioning & High-Volume Data Loading

## Day 55 — Partitioning Fundamentals ⬜

**Planned date:** 2026-12-08  
**SQL track:** Why partition; partition keys  
**Oracle / PL/SQL track:** Large-table manageability and pruning

### Tutorial

Partitioning divides one logical table into partitions and can improve manageability and query efficiency when predicates align with partition keys.

### Runnable mini-lab

Create a simple range-partitioned transaction table by date and load several months of data.

### Verify

You can explain partitioning vs indexing and why partitioning is not horizontal sharding.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 56 — Range Partitioning ⬜

**Planned date:** 2026-12-09  
**SQL track:** RANGE partitions  
**Oracle / PL/SQL track:** Date/time data

### Tutorial

Range partitioning is a natural fit for time-based transactional and warehouse data.

### Runnable mini-lab

Create monthly partitions, query one month, and inspect partition information and plan partition-start/stop information.

### Verify

You can choose sensible boundaries and explain MAXVALUE usage.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 57 — Interval Partitioning ⬜

**Planned date:** 2026-12-10  
**SQL track:** Automatic range extension  
**Oracle / PL/SQL track:** Rolling time-series tables

### Tutorial

Interval partitioning extends range partitioning by automatically creating future interval partitions as data arrives.

### Runnable mini-lab

Create an interval-partitioned table by month, insert data for a new month, and inspect the automatically created partition.

### Verify

You can explain range vs interval partitioning.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 58 — List Partitioning ⬜

**Planned date:** 2026-12-11  
**SQL track:** LIST partitions  
**Oracle / PL/SQL track:** Discrete business categories

### Tutorial

List partitioning groups explicit values such as region, channel, or business line.

### Runnable mini-lab

Partition a small table by region codes and test pruning using an equality predicate.

### Verify

You can identify when list partitioning is more natural than range.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 59 — Hash Partitioning ⬜

**Planned date:** 2026-12-12  
**SQL track:** HASH partitions  
**Oracle / PL/SQL track:** Even distribution

### Tutorial

Hash partitioning distributes rows across a fixed number of partitions using a hash key, useful when there is no meaningful range/list split but distribution matters.

### Runnable mini-lab

Create a hash-partitioned account-events table and inspect row distribution by partition.

### Verify

You can explain hash partitioning without confusing it with application-level sharding.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 60 — Composite Partitioning ⬜

**Planned date:** 2026-12-14  
**SQL track:** Range-Hash / Range-List concepts  
**Oracle / PL/SQL track:** Two-dimensional organization

### Tutorial

Composite partitioning first partitions by one strategy and subpartitions by another, combining manageability and distribution goals.

### Runnable mini-lab

Create a range-hash table for monthly transactions distributed by account ID. Query one month/account combination and inspect pruning.

### Verify

You can state a real reason to choose composite partitioning instead of doing it for complexity's sake.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 61 — Partition Pruning ⬜

**Planned date:** 2026-12-15  
**SQL track:** Partition elimination  
**Oracle / PL/SQL track:** Predicate design

### Tutorial

Partition pruning allows Oracle to avoid partitions that cannot contain qualifying rows. It is one of the main performance reasons to align partition keys with common filters.

### Runnable mini-lab

Write pruning-friendly date predicates and intentionally non-friendly variants. Compare plan partition ranges.

### Verify

You can identify whether a plan prunes and explain how the predicate enabled or prevented it.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 62 — Local vs Global Partitioned Indexes ⬜

**Planned date:** 2026-12-16  
**SQL track:** Partition-aware indexes  
**Oracle / PL/SQL track:** Maintenance trade-offs

### Tutorial

Local indexes align index partitions with table partitions and simplify many maintenance operations; global indexes span table partitions and support other access needs.

### Runnable mini-lab

Create one local and one global index on disposable partitioned tables. Perform a partition maintenance operation and inspect index state/structure.

### Verify

You can explain local vs global index trade-offs at developer level.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 63 — Partition Exchange ⬜

**Planned date:** 2026-12-17  
**SQL track:** EXCHANGE PARTITION  
**Oracle / PL/SQL track:** Metadata-oriented high-volume loading

### Tutorial

Partition exchange swaps a compatible standalone table with a target partition, enabling extremely fast batch-loading patterns because conventional row-by-row movement is avoided.

### Runnable mini-lab

Create a staging table compatible with one target partition, load it, validate row ranges, and perform `ALTER TABLE ... EXCHANGE PARTITION ...`. Verify where the rows are afterward.

### Verify

You can explain why compatibility, constraints, and validation matter before exchange.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 64 — Partition Maintenance ⬜

**Planned date:** 2026-12-18  
**SQL track:** ADD/SPLIT/MERGE/TRUNCATE/DROP concepts  
**Oracle / PL/SQL track:** Lifecycle management

### Tutorial

Partitioning is also a data-lifecycle tool. Learn the maintenance operations commonly used in rolling-window systems.

### Runnable mini-lab

On a disposable table, add a future partition and truncate/drop an old one. If supported in your setup, experiment with split/merge.

### Verify

You can describe how partition maintenance can replace massive DELETE operations in some designs.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 65 — CTAS & Staging Patterns ⬜

**Planned date:** 2026-12-19  
**SQL track:** CREATE TABLE AS SELECT  
**Oracle / PL/SQL track:** Fast intermediate data creation

### Tutorial

CTAS is a powerful set-based way to materialize transformed data, often useful in ETL and migration workflows.

### Runnable mini-lab

Build a transformed staging table with CTAS, gather stats, index it, and compare with inserting row-by-row.

### Verify

You can explain when CTAS is appropriate and why it is not always a permanent-data design.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 66 — High-Volume MERGE ⬜

**Planned date:** 2026-12-21  
**SQL track:** MERGE at scale  
**Oracle / PL/SQL track:** Staging-to-target synchronization

### Tutorial

MERGE is common in batch integration but can become expensive when source/target matching is poorly indexed or the change set is huge.

### Runnable mini-lab

Load a staging delta, index join keys, run MERGE, inspect the plan, and compare with a case missing the supporting index.

### Verify

You can identify the join condition as a key performance driver.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 67 — Parallel DML ⬜

**Planned date:** 2026-12-22  
**SQL track:** Parallel INSERT/UPDATE/MERGE concepts  
**Oracle / PL/SQL track:** Controlled resource use

### Tutorial

Parallel DML can accelerate large changes but requires intentional session/statement configuration and increases system resource consumption.

### Runnable mini-lab

In a disposable lab and only if supported, enable parallel DML, run a large insert/update with parallel hints, then disable it. Record behavior.

### Verify

You can explain why parallel DML belongs in controlled batch windows rather than ordinary OLTP requests.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 68 — Large Deletes & Data Lifecycle ⬜

**Planned date:** 2026-12-23  
**SQL track:** DELETE vs partition operations  
**Oracle / PL/SQL track:** Undo/redo and batch strategy

### Tutorial

Deleting millions of rows can generate substantial undo/redo and lock work. Sometimes partition lifecycle operations or chunking are safer designs.

### Runnable mini-lab

Compare deleting old rows from an unpartitioned table with truncating/dropping an old partition in a partitioned copy. Do this only on lab data.

### Verify

You can choose between DML cleanup and partition maintenance based on retention design.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 69 — GTT vs Staging vs Collection — Revisited ⬜

**Planned date:** 2026-12-24  
**SQL track:** Intermediate-data architecture  
**Oracle / PL/SQL track:** Memory, persistence, restartability

### Tutorial

Revisit temporary-data choices now that you understand bulk PL/SQL and high-volume SQL. The right tool depends on row volume, cross-statement reuse, SQL visibility, restart requirements, and memory.

### Runnable mini-lab

For three scenarios—10k lookup values, 5M intermediate rows, restartable overnight batch—choose collection, GTT, or permanent staging and justify each.

### Verify

You can make the choice from requirements rather than habit.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 70 — Partitioning & Load Phase Assessment ⬜

**Planned date:** 2026-12-25  
**SQL track:** High-volume architecture  
**Oracle / PL/SQL track:** Explain + implement

### Tutorial

Close Phase 4 by designing a realistic monthly transaction load.

### Runnable mini-lab

Design: stage 5M rows, validate, load a monthly partition, index appropriately, and make it queryable. Use partition exchange or direct-path insert where appropriate and explain the choice.

### Verify

Your design includes pruning, index implications, statistics, and recovery considerations.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

# Phase 5 — Transactions, Concurrency & Production Engineering

## Day 71 — Oracle Read Consistency & Transactions ⬜

**Planned date:** 2026-12-26  
**SQL track:** MVCC/read consistency concepts  
**Oracle / PL/SQL track:** Undo and statement consistency

### Tutorial

Oracle readers generally do not block writers in the same way as simplistic lock models suggest; read consistency relies heavily on undo and SCNs.

### Runnable mini-lab

Open two sessions. Update rows without committing in one and query them from the other. Observe what the second session sees.

### Verify

You can explain consistent reads at a developer level without claiming uncommitted changes are visible.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 72 — Row Locking & Blocking ⬜

**Planned date:** 2026-12-28  
**SQL track:** DML locks  
**Oracle / PL/SQL track:** Wait chains

### Tutorial

DML locks protect modified rows until transaction completion. Blocking is normal coordination; the problem is prolonged or unexpected blocking.

### Runnable mini-lab

Use two sessions to update the same row and observe the wait. Commit/rollback the blocker and observe release.

### Verify

You can distinguish blocker vs blocked session and explain why premature commits are not the universal fix.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 73 — Deadlocks ⬜

**Planned date:** 2026-12-29  
**SQL track:** Circular lock dependency  
**Oracle / PL/SQL track:** ORA-00060 reasoning

### Tutorial

A deadlock is a cycle: session A needs something held by B while B needs something held by A. Oracle detects the cycle and raises an error for one statement.

### Runnable mini-lab

Create a controlled two-row deadlock in two lab sessions. Capture the error and then rewrite operation ordering to avoid it.

### Verify

You can explain blocking vs deadlock in one sentence each.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 74 — SELECT FOR UPDATE ⬜

**Planned date:** 2026-12-30  
**SQL track:** Pessimistic locking  
**Oracle / PL/SQL track:** NOWAIT/SKIP LOCKED concepts

### Tutorial

`SELECT ... FOR UPDATE` reserves selected rows for later modification. Variants such as NOWAIT or SKIP LOCKED support different concurrency workflows.

### Runnable mini-lab

Build a simple job-claim table. Claim rows with `FOR UPDATE`, then test NOWAIT or SKIP LOCKED in another session.

### Verify

You can explain when pessimistic locking is appropriate and its concurrency cost.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 75 — Commit Boundaries & Savepoints ⬜

**Planned date:** 2026-12-31  
**SQL track:** COMMIT/ROLLBACK/SAVEPOINT  
**Oracle / PL/SQL track:** Business transaction design

### Tutorial

Commit boundaries should model business atomicity and recovery, not simply reduce lock time. Savepoints offer partial rollback inside a transaction.

### Runnable mini-lab

Write a three-step transfer workflow with a savepoint before an optional operation. Test full rollback and rollback-to-savepoint.

### Verify

You can explain why 'commit every N rows' needs workload/recovery justification.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 76 — Restartability & Idempotency ⬜

**Planned date:** 2027-01-01  
**SQL track:** Safe batch reruns  
**Oracle / PL/SQL track:** Checkpoint design

### Tutorial

Production batch design must answer what happens after partial success. Idempotency, batch keys, staging status, and checkpoints are core tools.

### Runnable mini-lab

Create a batch-run table and load process with a unique business/batch key. Force failure halfway, rerun, and prove no duplicate target rows appear.

### Verify

You can describe exactly how your process decides what is safe to reprocess.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 77 — Logging & Instrumentation ⬜

**Planned date:** 2027-01-02  
**SQL track:** DBMS_APPLICATION_INFO concepts; structured logs  
**Oracle / PL/SQL track:** Operational visibility

### Tutorial

Production PL/SQL should expose enough context to diagnose failures without dumping sensitive data. Instrument batch/module/action and capture meaningful error context.

### Runnable mini-lab

Add a batch log table and module/action instrumentation where available. Record start/end, counts, error code, and safe context.

### Verify

You can distinguish business audit data from diagnostic logging.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 78 — Invoker vs Definer Rights ⬜

**Planned date:** 2027-01-04  
**SQL track:** AUTHID DEFINER/CURRENT_USER  
**Oracle / PL/SQL track:** Privilege model

### Tutorial

PL/SQL units can execute under definer or invoker privilege models. This affects security and reusable database APIs.

### Runnable mini-lab

Create a small controlled example with two users if practical, or document the privilege flow if your local setup is single-user.

### Verify

You can explain why privilege model is a design choice, not only syntax.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 79 — Dynamic SQL Security ⬜

**Planned date:** 2027-01-05  
**SQL track:** EXECUTE IMMEDIATE + validation  
**Oracle / PL/SQL track:** SQL injection prevention

### Tutorial

Dynamic object names cannot generally be represented as ordinary bind values, so validate/whitelist structural input and bind actual data values.

### Runnable mini-lab

Create a safe report procedure with a whitelisted sort column and bound filter values. Attempt a malicious input and verify rejection.

### Verify

You can state: bind data values; validate dynamic identifiers.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 80 — Sequences, Identity & Concurrency ⬜

**Planned date:** 2027-01-06  
**SQL track:** Sequence behavior  
**Oracle / PL/SQL track:** Surrogate-key generation

### Tutorial

Oracle sequences are concurrency-friendly unique-number generators but do not promise gapless business numbering. Identity columns build on sequence-like mechanisms.

### Runnable mini-lab

Create a sequence with CACHE, insert rows from two sessions, rollback some inserts, and observe gaps.

### Verify

You can explain why gaps are normal and why `MAX(id)+1` is unsafe under concurrency.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 81 — DBMS_SCHEDULER for Developers ⬜

**Planned date:** 2027-01-07  
**SQL track:** Job scheduling concepts  
**Oracle / PL/SQL track:** Operational batch execution

### Tutorial

Learn enough Scheduler to create and inspect a developer-owned recurring or one-time database job without becoming a DBA.

### Runnable mini-lab

Create a safe one-time job that calls a logging procedure, run it, inspect status, then drop it.

### Verify

You can explain when database scheduling is appropriate vs an external enterprise scheduler.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 82 — Production Engineering Assessment ⬜

**Planned date:** 2027-01-08  
**SQL track:** Concurrency, security, recovery  
**Oracle / PL/SQL track:** Incident reasoning

### Tutorial

Close Phase 5 with scenario-driven evaluation.

### Runnable mini-lab

Work through: blocked payment update, deadlock, failed half-loaded batch, SQL injection risk, and runaway bulk collection. Give diagnosis and corrective design for each.

### Verify

You can answer each scenario with evidence, impact, safe recovery, and prevention.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

# Phase 6 — Capstone, Tuning & Final Evaluation

## Day 83 — Capstone Design — Financial Transaction System ⬜

**Planned date:** 2027-01-09  
**SQL track:** Schema/API design  
**Oracle / PL/SQL track:** Requirements and workload assumptions

### Tutorial

Start the final capstone by defining workload before technology. Model customers, accounts, staged transactions, final transactions, errors, and batch runs.

### Runnable mini-lab

Write `architecture.md` plus DDL for `CUSTOMER`, `ACCOUNT`, `TRANSACTION_STAGE`, `TRANSACTION`, `TRANSACTION_ERROR`, and `BATCH_RUN`.

### Verify

Every index/partition key has a stated query or lifecycle reason.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 84 — Capstone Data Generator ⬜

**Planned date:** 2027-01-11  
**SQL track:** Exercise dataset  
**Oracle / PL/SQL track:** Volume for real plans

### Tutorial

Performance skills need enough data for optimizer choices to matter. Generate a realistic skewed dataset with dates, account IDs, statuses, and amounts.

### Runnable mini-lab

Generate at least 500k–1M transactions locally if your machine handles it comfortably; otherwise choose the largest repeatable size that does.

### Verify

You can rebuild the dataset with one script and describe its skew/distribution.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 85 — Capstone Bulk Load Package ⬜

**Planned date:** 2027-01-12  
**SQL track:** PL/SQL bulk processing  
**Oracle / PL/SQL track:** Validation and error capture

### Tutorial

Build a package that validates staged rows in batches and writes valid/error outcomes with controlled PGA usage.

### Runnable mini-lab

Implement BULK COLLECT LIMIT + FORALL SAVE EXCEPTIONS or a set-based alternative where better. Record batch counts.

### Verify

You can justify where PL/SQL bulk processing is used and where plain SQL is superior.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 86 — Capstone Partitioned Target ⬜

**Planned date:** 2027-01-13  
**SQL track:** Partitioning and indexes  
**Oracle / PL/SQL track:** Monthly transaction storage

### Tutorial

Partition the final transaction table to match time-based access and retention. Add appropriate local/global indexes only for demonstrated access patterns.

### Runnable mini-lab

Rebuild/load the target as range/interval partitioned, verify pruning, and document index choices.

### Verify

A month-filter query prunes and an account lookup uses a sensible access path.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 87 — Capstone High-Speed Monthly Load ⬜

**Planned date:** 2027-01-14  
**SQL track:** Partition exchange / APPEND  
**Oracle / PL/SQL track:** Batch load path

### Tutorial

Implement one high-throughput load path and measure it rather than assuming the advanced feature wins.

### Runnable mini-lab

Load a prepared monthly staging table using partition exchange when compatible, or compare APPEND direct-path load. Gather stats afterward.

### Verify

You can explain prerequisites, concurrency/recovery implications, and why you chose the method.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 88 — Capstone SQL Tuning ⬜

**Planned date:** 2027-01-15  
**SQL track:** Execution plans  
**Oracle / PL/SQL track:** Measure before optimize

### Tutorial

Tune three real capstone queries using plans and statistics. At least one should remain a full scan if that is genuinely optimal.

### Runnable mini-lab

Tune: account history, monthly aggregate, and high-value anomaly query. Record before/after plan and elapsed metrics available locally.

### Verify

Every change has measured evidence and no cargo-cult hints.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 89 — Capstone Failure & Recovery Drill ⬜

**Planned date:** 2027-01-16  
**SQL track:** Idempotency and restart  
**Oracle / PL/SQL track:** Production incident simulation

### Tutorial

Force realistic failures and prove the system can recover without duplicate or silently missing financial transactions.

### Runnable mini-lab

Inject invalid data and a mid-batch failure. Restart from the documented checkpoint, validate row counts/balances, and inspect logs/error table.

### Verify

You can explain recovery as: diagnose → impact → safe restart → validate.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

## Day 90 — Final Oracle Mastery Evaluation ⬜

**Planned date:** 2027-01-18  
**SQL track:** SQL + PL/SQL + performance  
**Oracle / PL/SQL track:** Explain, implement, tune, recover

### Tutorial

Day 90 is an evidence-based diagnostic. Demonstrate that you can solve SQL, design PL/SQL, read plans, make indexing/partitioning decisions, and troubleshoot production failures.

### Runnable mini-lab

Do a 90-minute final: 25 min SQL problems, 20 min PL/SQL coding, 20 min tuning/plan review, 15 min partition/load design, 10 min production incident explanation.

### Verify

Score every domain Strong / Needs Reinforcement / Gap and build the next 90-day plan only from actual weaknesses.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab and intentionally change or break one thing.
- [ ] Solve at least 2–3 SQL/PLSQL exercises or review one production-style code/query sample.
- [ ] Save the working SQL/PLSQL script in the lab repository.
- [ ] Generate the detailed Joplin note, manual-note version, and Anki TSV.

---

# Day 90 completion rubric

| Area | Strong | Needs reinforcement | Gap |
|---|:---:|:---:|:---:|
| Core SQL syntax & joins | ☐ | ☐ | ☐ |
| Aggregation / subqueries / EXISTS | ☐ | ☐ | ☐ |
| CTE / WITH / MERGE / GTT | ☐ | ☐ | ☐ |
| Analytic functions | ☐ | ☐ | ☐ |
| PL/SQL blocks / procedures / functions | ☐ | ☐ | ☐ |
| Packages & exception handling | ☐ | ☐ | ☐ |
| Cursors & REF CURSOR | ☐ | ☐ | ☐ |
| Collections | ☐ | ☐ | ☐ |
| BULK COLLECT / FORALL / LIMIT | ☐ | ☐ | ☐ |
| Dynamic SQL & security | ☐ | ☐ | ☐ |
| Execution plans / DBMS_XPLAN | ☐ | ☐ | ☐ |
| Cardinality / stats / selectivity | ☐ | ☐ | ☐ |
| Indexing | ☐ | ☐ | ☐ |
| Bind variables / parsing | ☐ | ☐ | ☐ |
| Hints / PARALLEL / APPEND | ☐ | ☐ | ☐ |
| Partitioning types | ☐ | ☐ | ☐ |
| Partition pruning | ☐ | ☐ | ☐ |
| Partition exchange / maintenance | ☐ | ☐ | ☐ |
| Transactions / locking / deadlocks | ☐ | ☐ | ☐ |
| Restartability / idempotency | ☐ | ☐ | ☐ |
| Production troubleshooting | ☐ | ☐ | ☐ |
| Capstone architecture & tuning | ☐ | ☐ | ☐ |

The next 90-day plan should be built from this rubric. Do not repeat broad introductory material that already scores Strong.

## Long-term maintenance after Day 90

Keep SQL sharp with a small permanent workout:

```text
Monday    → joins / subqueries
Tuesday   → GROUP BY / analytics
Wednesday → CTE / EXISTS / MERGE
Thursday  → execution-plan / index problem
Friday    → mixed SQL challenge
Saturday  → PL/SQL / batch / performance lab
```

A maintenance session can be only 20–30 minutes. The point is to keep writing SQL continuously instead of periodically relearning syntax.
