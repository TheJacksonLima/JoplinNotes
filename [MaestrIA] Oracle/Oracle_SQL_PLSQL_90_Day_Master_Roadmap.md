# Oracle SQL + PL/SQL — 65-Session Senior Developer & Interview Roadmap

> **Interview-focused Oracle track.** This replaces the previous 90-session plan while preserving its strongest material. The goal is to review Oracle deeply enough to perform well in **Senior Oracle Developer, Java + Oracle, and PL/SQL technical interviews** without spending weeks re-learning fundamentals you already know.

**Start date:** **2026-11-11**  
**Projected finish:** **2027-01-25** if no sessions slip  
**Cadence:** 60–90 min per study session • 6 sessions/week • Sunday off  
**Primary objective:** interview performance through **explain + implement + tune/review + production reasoning**  
**SQL sharpening rule:** write SQL in almost every session; do not replace practice with passive reading.  
**Notes rule:** save the lab script every session; consolidate detailed Joplin/manual notes + Anki every 2–3 sessions instead of turning every session into documentation work.

## Why this version is shorter

The original 90-session roadmap had excellent coverage, but it dedicated full sessions to several topics that are already familiar. This version compresses the refresher material and preserves depth where it matters most for senior Oracle interviews:

- WITH / CTE, MERGE, analytics, duplicates and GTTs
- Procedures, functions, packages, exceptions and cursors
- REF CURSOR / SYS_REFCURSOR
- Associative arrays, nested tables and VARRAYs
- BULK COLLECT, LIMIT, FORALL, SAVE EXCEPTIONS
- Execution plans, cardinality, statistics, histograms and join methods
- B-tree, composite, function-based, bitmap, local/global indexes
- Bind variables, hard/soft parse, skew and tuning anti-patterns
- Hints, PARALLEL, APPEND
- Range, interval, list, hash and composite partitioning
- Partition pruning and partition exchange
- Transactions, locks, blocking, deadlocks and SELECT FOR UPDATE
- Restartability, idempotency and production recovery
- Repeated mock interviews and timed coding

## Scope

### Mastery target

- Oracle SQL problem solving
- PL/SQL implementation and code review
- Performance/tuning reasoning
- Index and partition design
- High-volume processing
- Transaction/concurrency behavior
- Production incident recovery
- Technical interview communication

### Recognition-only in this cycle

- RAC administration
- Data Guard administration
- RMAN / backup administration
- ASM
- Database installation/patching
- Deep DBA-only CBO internals

ODI remains a separate track. This roadmap is specifically Oracle SQL + PL/SQL + database-development interview preparation.

## Completion rule

A session is complete only when you can:

~~~text
Explain
   ↓
Implement / solve
   ↓
Break or inspect
   ↓
Tune / review
   ↓
Answer interview follow-ups
   ↓
COMPLETE
~~~

For oral interview practice, answer in English when possible.

## Calendar rule

Day 1 starts on **2026-11-11**. Sundays are rest days and are not numbered. If a session slips, move the sequence forward instead of doubling sessions the next day.

## Phase map

~~~text
Days 01–10  SQL Interview Sharpness
Days 11–22  PL/SQL Core, Collections & Bulk Processing
Days 23–36  Optimizer, Execution Plans & Indexing
Days 37–48  Partitioning & High-Volume Loading
Days 49–55  Transactions & Production Engineering
Days 56–65  Oracle Interview Bootcamp
~~~

## Master index — Day 01 to Day 65

| Day | Status | Date | Phase | SQL track | Oracle / PL/SQL track |
|---:|---|---|---|---|---|
| 01 | 🟡 Current | 2026-11-11 | P1 — SQL Interview Sharpness | Oracle Free lab; SQL client; practice schema | Repeatable environment + baseline assessment |
| 02 | ⬜ Upcoming | 2026-11-12 | P1 — SQL Interview Sharpness | INNER/OUTER/self joins; NULL; UNION/MINUS | Correct relational reasoning |
| 03 | ⬜ Upcoming | 2026-11-13 | P1 — SQL Interview Sharpness | GROUP BY; HAVING; scalar/correlated subqueries | Query decomposition |
| 04 | ⬜ Upcoming | 2026-11-14 | P1 — SQL Interview Sharpness | WITH; inline views; EXISTS/NOT EXISTS | Readable multi-step SQL |
| 05 | ⬜ Upcoming | 2026-11-16 | P1 — SQL Interview Sharpness | ROW_NUMBER; RANK; DENSE_RANK | Partitioned ranking |
| 06 | ⬜ Upcoming | 2026-11-17 | P1 — SQL Interview Sharpness | LAG/LEAD; SUM/AVG/COUNT OVER | Sequence and running calculations |
| 07 | ⬜ Upcoming | 2026-11-18 | P1 — SQL Interview Sharpness | INSERT/UPDATE/DELETE/MERGE; ROWID | Safe data modification |
| 08 | ⬜ Upcoming | 2026-11-19 | P1 — SQL Interview Sharpness | Dates; strings; NVL/COALESCE; PIVOT; hierarchy | Oracle-specific fluency |
| 09 | ⬜ Upcoming | 2026-11-20 | P1 — SQL Interview Sharpness | Global Temporary Tables; staging | Transaction/session temporary data |
| 10 | ⬜ Upcoming | 2026-11-21 | P1 — SQL Interview Sharpness | Timed mixed SQL | Interview retrieval under pressure |
| 11 | ⬜ Upcoming | 2026-11-23 | P2 — PL/SQL Core, Collections & Bulk Processing | Blocks; variables; %TYPE; %ROWTYPE; records | Procedural foundations |
| 12 | ⬜ Upcoming | 2026-11-24 | P2 — PL/SQL Core, Collections & Bulk Processing | IN/OUT/IN OUT; RETURN | Database API design |
| 13 | ⬜ Upcoming | 2026-11-25 | P2 — PL/SQL Core, Collections & Bulk Processing | Package spec/body; private/public; overloads | Maintainable PL/SQL APIs |
| 14 | ⬜ Upcoming | 2026-11-26 | P2 — PL/SQL Core, Collections & Bulk Processing | Predefined/custom exceptions; propagation | RAISE; RAISE_APPLICATION_ERROR |
| 15 | ⬜ Upcoming | 2026-11-27 | P2 — PL/SQL Core, Collections & Bulk Processing | OPEN/FETCH/CLOSE; cursor FOR loop | Controlled row iteration |
| 16 | ⬜ Upcoming | 2026-11-28 | P2 — PL/SQL Core, Collections & Bulk Processing | Weak and strong cursor variables | Returning result sets |
| 17 | ⬜ Upcoming | 2026-11-30 | P2 — PL/SQL Core, Collections & Bulk Processing | Associative array; nested table; VARRAY | Collection trade-offs |
| 18 | ⬜ Upcoming | 2026-12-01 | P2 — PL/SQL Core, Collections & Bulk Processing | Index-by lookup; SQL collection interaction | Choosing the right collection |
| 19 | ⬜ Upcoming | 2026-12-02 | P2 — PL/SQL Core, Collections & Bulk Processing | Bulk fetching | Context switching + PGA control |
| 20 | ⬜ Upcoming | 2026-12-03 | P2 — PL/SQL Core, Collections & Bulk Processing | Core PL/SQL + cursors + collections | Oral interview checkpoint |
| 21 | ⬜ Upcoming | 2026-12-04 | P2 — PL/SQL Core, Collections & Bulk Processing | Bulk DML | SQL%BULK_EXCEPTIONS |
| 22 | ⬜ Upcoming | 2026-12-05 | P2 — PL/SQL Core, Collections & Bulk Processing | EXECUTE IMMEDIATE; binds | Safe dynamic structure |
| 23 | ⬜ Upcoming | 2026-12-07 | P3 — Optimizer, Execution Plans & Indexing | Cost-based optimizer | Parse → optimize → execute |
| 24 | ⬜ Upcoming | 2026-12-08 | P3 — Optimizer, Execution Plans & Indexing | Plan reading | Access paths; predicates |
| 25 | ⬜ Upcoming | 2026-12-09 | P3 — Optimizer, Execution Plans & Indexing | DISPLAY_CURSOR concepts | Estimated vs actual rows |
| 26 | ⬜ Upcoming | 2026-12-10 | P3 — Optimizer, Execution Plans & Indexing | Optimizer estimates; DBMS_STATS | Data distribution |
| 27 | ⬜ Upcoming | 2026-12-11 | P3 — Optimizer, Execution Plans & Indexing | Skewed distributions | Value-sensitive plans |
| 28 | ⬜ Upcoming | 2026-12-12 | P3 — Optimizer, Execution Plans & Indexing | B-tree; unique; range scan | General-purpose indexing |
| 29 | ⬜ Upcoming | 2026-12-14 | P3 — Optimizer, Execution Plans & Indexing | Leading columns; indexed expressions | Predicate alignment |
| 30 | ⬜ Upcoming | 2026-12-15 | P3 — Optimizer, Execution Plans & Indexing | Bitmap; partition-aware indexes | Workload trade-offs |
| 31 | ⬜ Upcoming | 2026-12-16 | P3 — Optimizer, Execution Plans & Indexing | Nested loops; hash; merge | Choosing by row volume and access |
| 32 | ⬜ Upcoming | 2026-12-17 | P3 — Optimizer, Execution Plans & Indexing | Bind reuse; shared pool | Parse efficiency |
| 33 | ⬜ Upcoming | 2026-12-18 | P3 — Optimizer, Execution Plans & Indexing | Bind-sensitive plans | Skew + reuse tension |
| 34 | ⬜ Upcoming | 2026-12-19 | P3 — Optimizer, Execution Plans & Indexing | Implicit conversions; non-sargable predicates; DISTINCT | Plan-unfriendly SQL |
| 35 | ⬜ Upcoming | 2026-12-21 | P3 — Optimizer, Execution Plans & Indexing | INDEX/FULL/join hints; PARALLEL; APPEND | Controlled optimizer/load influence |
| 36 | ⬜ Upcoming | 2026-12-22 | P3 — Optimizer, Execution Plans & Indexing | Plans; indexes; optimizer | Evidence-based tuning |
| 37 | ⬜ Upcoming | 2026-12-23 | P4 — Partitioning & High-Volume Loading | Partition keys; RANGE | Large-table manageability |
| 38 | ⬜ Upcoming | 2026-12-24 | P4 — Partitioning & High-Volume Loading | INTERVAL; LIST; HASH | Partition strategy selection |
| 39 | ⬜ Upcoming | 2026-12-25 | P4 — Partitioning & High-Volume Loading | Range-hash; range-list | Two-dimensional organization |
| 40 | ⬜ Upcoming | 2026-12-26 | P4 — Partitioning & High-Volume Loading | Partition type selection | Rapid-fire architecture questions |
| 41 | ⬜ Upcoming | 2026-12-28 | P4 — Partitioning & High-Volume Loading | Static/dynamic pruning concepts | Predicate design |
| 42 | ⬜ Upcoming | 2026-12-29 | P4 — Partitioning & High-Volume Loading | LOCAL/GLOBAL | Maintenance vs access needs |
| 43 | ⬜ Upcoming | 2026-12-30 | P4 — Partitioning & High-Volume Loading | EXCHANGE PARTITION | Fast batch loading |
| 44 | ⬜ Upcoming | 2026-12-31 | P4 — Partitioning & High-Volume Loading | ADD/SPLIT/MERGE/TRUNCATE/DROP | Data lifecycle |
| 45 | ⬜ Upcoming | 2027-01-01 | P4 — Partitioning & High-Volume Loading | CTAS; staging; APPEND | High-throughput data movement |
| 46 | ⬜ Upcoming | 2027-01-02 | P4 — Partitioning & High-Volume Loading | Large MERGE; parallel DML | Controlled batch throughput |
| 47 | ⬜ Upcoming | 2027-01-04 | P4 — Partitioning & High-Volume Loading | GTT vs collection vs permanent staging | Restartability + memory + SQL visibility |
| 48 | ⬜ Upcoming | 2027-01-05 | P4 — Partitioning & High-Volume Loading | Partition exchange; pruning; APPEND; MERGE | End-to-end load design |
| 49 | ⬜ Upcoming | 2027-01-06 | P5 — Transactions & Production Engineering | Undo; statement consistency | Oracle transaction model |
| 50 | ⬜ Upcoming | 2027-01-07 | P5 — Transactions & Production Engineering | Row locking; NOWAIT/SKIP LOCKED | Pessimistic coordination |
| 51 | ⬜ Upcoming | 2027-01-08 | P5 — Transactions & Production Engineering | Circular dependencies | ORA-00060 reasoning |
| 52 | ⬜ Upcoming | 2027-01-09 | P5 — Transactions & Production Engineering | COMMIT/ROLLBACK/SAVEPOINT | Business atomicity |
| 53 | ⬜ Upcoming | 2027-01-11 | P5 — Transactions & Production Engineering | Batch keys; checkpoints | Safe reruns |
| 54 | ⬜ Upcoming | 2027-01-12 | P5 — Transactions & Production Engineering | Instrumentation; dynamic SQL safety; AUTHID | Production hygiene |
| 55 | ⬜ Upcoming | 2027-01-13 | P5 — Transactions & Production Engineering | Concurrency; recovery; security | Incident reasoning |
| 56 | ⬜ Upcoming | 2027-01-14 | P6 — Oracle Interview Bootcamp | Timed SQL coding | Fast solution selection |
| 57 | ⬜ Upcoming | 2027-01-15 | P6 — Oracle Interview Bootcamp | Timed PL/SQL coding | Packages/cursors/collections/bulk |
| 58 | ⬜ Upcoming | 2027-01-16 | P6 — Oracle Interview Bootcamp | Plans; stats; access paths | Performance explanation |
| 59 | ⬜ Upcoming | 2027-01-18 | P6 — Oracle Interview Bootcamp | Index selection; sargability | Senior code review |
| 60 | ⬜ Upcoming | 2027-01-19 | P6 — Oracle Interview Bootcamp | Partitioning; pruning; indexes | Architecture explanation |
| 61 | ⬜ Upcoming | 2027-01-20 | P6 — Oracle Interview Bootcamp | GTT; staging; bulk; APPEND; PARALLEL | Design trade-offs |
| 62 | ⬜ Upcoming | 2027-01-21 | P6 — Oracle Interview Bootcamp | Locks; commits; recovery | Production judgment |
| 63 | ⬜ Upcoming | 2027-01-22 | P6 — Oracle Interview Bootcamp | JDBC binds; result sets; transactions | Backend integration recognition |
| 64 | ⬜ Upcoming | 2027-01-23 | P6 — Oracle Interview Bootcamp | All covered areas | 90-minute simulation |
| 65 | ⬜ Upcoming | 2027-01-25 | P6 — Oracle Interview Bootcamp | SQL + PL/SQL + performance + production | Explain, code, tune, recover |

---

# Phase 1 — SQL Interview Sharpness

## Day 01 — Local Oracle Lab + Baseline 🟡

**Planned date:** 2026-11-11  
**SQL track:** Oracle Free lab; SQL client; practice schema  
**Oracle / PL/SQL track:** Repeatable environment + baseline assessment

### Tutorial

Set up or verify the local Oracle database and practice schema. Run a short baseline covering joins, aggregation, analytics and one PL/SQL block so later progress is measurable.

### Runnable mini-lab

Create/verify DEPARTMENT, EMPLOYEE, CUSTOMER, ACCOUNT, ORDERS, ORDER_ITEM, TRANSACTION_STAGE and TRANSACTION tables. Solve 5 baseline queries without notes.

### Verify

You can reconnect from a fresh session, rebuild the lab safely, and list your current SQL/PLSQL weak spots.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 02 — JOINs, NULLs & Set Logic ⬜

**Planned date:** 2026-11-12  
**SQL track:** INNER/OUTER/self joins; NULL; UNION/MINUS  
**Oracle / PL/SQL track:** Correct relational reasoning

### Tutorial

Refresh the SQL mechanics that interviewers assume are automatic. Focus on row preservation, accidental row multiplication and three-valued NULL logic.

### Runnable mini-lab

Solve employees/departments, customers with/without orders, a self-join hierarchy, NOT EXISTS vs NOT IN, and UNION vs UNION ALL.

### Verify

You can choose the join/set operator from the requirement and explain the NOT IN + NULL trap.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 03 — Aggregation, HAVING & Subqueries ⬜

**Planned date:** 2026-11-13  
**SQL track:** GROUP BY; HAVING; scalar/correlated subqueries  
**Oracle / PL/SQL track:** Query decomposition

### Tutorial

Practice collapsing rows correctly and using subqueries only where they improve the solution. Keep WHERE vs HAVING and correlated vs non-correlated subqueries precise.

### Runnable mini-lab

Solve department salary summaries, employees above company average, employees above department average, and departments above a threshold.

### Verify

You can rewrite at least one correlated solution using a join or analytic function and explain the trade-off.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 04 — WITH / CTE, Inline Views & EXISTS ⬜

**Planned date:** 2026-11-14  
**SQL track:** WITH; inline views; EXISTS/NOT EXISTS  
**Oracle / PL/SQL track:** Readable multi-step SQL

### Tutorial

Use subquery factoring to make complex queries easier to reason about, without assuming a CTE is always materialized or faster.

### Runnable mini-lab

Refactor a nested report into two CTEs, then write equivalent inline-view and EXISTS versions where appropriate.

### Verify

You can explain CTE vs inline view vs EXISTS and when each improves clarity.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 05 — Analytic Functions — Ranking ⬜

**Planned date:** 2026-11-16  
**SQL track:** ROW_NUMBER; RANK; DENSE_RANK  
**Oracle / PL/SQL track:** Partitioned ranking

### Tutorial

Analytics are interview staples because they preserve detail rows while calculating across related rows.

### Runnable mini-lab

Solve highest salary per department, top 3 distinct salaries, latest row per customer, and duplicate numbering.

### Verify

Without notes, explain PARTITION BY and choose ROW_NUMBER vs RANK vs DENSE_RANK correctly.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 06 — Analytic Functions — LAG, LEAD & Windows ⬜

**Planned date:** 2026-11-17  
**SQL track:** LAG/LEAD; SUM/AVG/COUNT OVER  
**Oracle / PL/SQL track:** Sequence and running calculations

### Tutorial

Practice previous/next-row comparisons and running metrics without self-joins.

### Runnable mini-lab

Solve previous transaction amount, running balance, moving average, consecutive values, and percentage of department payroll.

### Verify

You can explain how analytic ORDER BY and window frames change the result.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 07 — MERGE, DML & Duplicate Cleanup ⬜

**Planned date:** 2026-11-18  
**SQL track:** INSERT/UPDATE/DELETE/MERGE; ROWID  
**Oracle / PL/SQL track:** Safe data modification

### Tutorial

Review production-style DML, especially staging-to-target MERGE and deliberate duplicate removal.

### Runnable mini-lab

MERGE a staging table into target, roll back once, commit once, then remove duplicate employee rows using ROWID and ROW_NUMBER approaches.

### Verify

You can state exactly which rows changed and why your duplicate strategy preserves one intended row.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 08 — Oracle SQL Functions & Reporting Patterns ⬜

**Planned date:** 2026-11-19  
**SQL track:** Dates; strings; NVL/COALESCE; PIVOT; hierarchy  
**Oracle / PL/SQL track:** Oracle-specific fluency

### Tutorial

Sharpen Oracle-specific syntax that often appears in technical screens and legacy enterprise code.

### Runnable mini-lab

Write sargable date filters, month-end calculations, normalized text, conditional aggregation/PIVOT, and one CONNECT BY hierarchy.

### Verify

You avoid wrapping indexed date columns unnecessarily and can explain CONNECT BY at a practical level.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 09 — GTTs & Intermediate Data Choices ⬜

**Planned date:** 2026-11-20  
**SQL track:** Global Temporary Tables; staging  
**Oracle / PL/SQL track:** Transaction/session temporary data

### Tutorial

GTTs were a real interview gap, so this session is hands-on. Remember: the definition persists; rows are temporary and session-private.

### Runnable mini-lab

Create ON COMMIT DELETE ROWS and PRESERVE ROWS GTTs. Compare GTT vs CTE vs PL/SQL collection vs permanent staging for 3 scenarios.

### Verify

You can answer when to use each intermediate-data mechanism and explain commit behavior.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 10 — SQL Mock Interview #1 ⬜

**Planned date:** 2026-11-21  
**SQL track:** Timed mixed SQL  
**Oracle / PL/SQL track:** Interview retrieval under pressure

### Tutorial

No new theory. Prove that core and advanced SQL are available from memory.

### Runnable mini-lab

60 minutes: 6 SQL problems + 10 rapid-fire questions covering joins, EXISTS, CTE, analytics, MERGE, duplicates and GTTs.

### Verify

Score each area Green/Yellow/Red. Any Red topic becomes a review item in later sessions.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

# Phase 2 — PL/SQL Core, Collections & Bulk Processing

## Day 11 — PL/SQL Core Refresher ⬜

**Planned date:** 2026-11-23  
**SQL track:** Blocks; variables; %TYPE; %ROWTYPE; records  
**Oracle / PL/SQL track:** Procedural foundations

### Tutorial

Compress the basics into one session. The goal is fluency, not relearning syntax.

### Runnable mini-lab

Write one anonymous block using anchored types, a record, IF/CASE and a small loop; then replace one unnecessary loop with set-based SQL.

### Verify

You can explain SQL vs PL/SQL and when procedural logic is justified.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 12 — Procedures & Functions ⬜

**Planned date:** 2026-11-24  
**SQL track:** IN/OUT/IN OUT; RETURN  
**Oracle / PL/SQL track:** Database API design

### Tutorial

Review callable program units with emphasis on clean contracts and transaction ownership.

### Runnable mini-lab

Create transfer_funds plus a lookup function. Do not commit inside the reusable transfer routine.

### Verify

You can explain procedure vs function and why callers often own the transaction boundary.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 13 — Packages & Encapsulation ⬜

**Planned date:** 2026-11-25  
**SQL track:** Package spec/body; private/public; overloads  
**Oracle / PL/SQL track:** Maintainable PL/SQL APIs

### Tutorial

Packages are central to serious PL/SQL design and common in interviews.

### Runnable mini-lab

Build ACCOUNT_PKG with public deposit/withdraw/get_balance and private validation helpers. Add one overload.

### Verify

You can explain specification vs body and package state at interview level.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 14 — Exception Handling ⬜

**Planned date:** 2026-11-26  
**SQL track:** Predefined/custom exceptions; propagation  
**Oracle / PL/SQL track:** RAISE; RAISE_APPLICATION_ERROR

### Tutorial

Production PL/SQL should make failures visible and meaningful, not swallow them.

### Runnable mini-lab

Handle NO_DATA_FOUND, DUP_VAL_ON_INDEX and a custom business exception. Add logging context and re-raise unexpected errors.

### Verify

You can explain why WHEN OTHERS THEN NULL is dangerous and when to translate/re-raise.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 15 — Explicit & Parameterized Cursors ⬜

**Planned date:** 2026-11-27  
**SQL track:** OPEN/FETCH/CLOSE; cursor FOR loop  
**Oracle / PL/SQL track:** Controlled row iteration

### Tutorial

Know cursors well, while remembering that set-based or bulk processing is often better at scale.

### Runnable mini-lab

Implement the same query using explicit cursor mechanics and a cursor FOR loop. Add a parameterized department cursor.

### Verify

You can explain implicit vs explicit vs parameterized cursors and cursor attributes.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 16 — REF CURSOR / SYS_REFCURSOR ⬜

**Planned date:** 2026-11-28  
**SQL track:** Weak and strong cursor variables  
**Oracle / PL/SQL track:** Returning result sets

### Tutorial

REF CURSOR was another interview gap. Focus on how a caller receives a dynamic result set.

### Runnable mini-lab

Create get_transactions(account_id, OUT SYS_REFCURSOR) and consume it from your client. Sketch JDBC CallableStatement usage conceptually.

### Verify

You can explain regular cursor vs REF CURSOR and weak vs strong typing.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 17 — Collections — Mental Model ⬜

**Planned date:** 2026-11-30  
**SQL track:** Associative array; nested table; VARRAY  
**Oracle / PL/SQL track:** Collection trade-offs

### Tutorial

Know the three collection families by behavior, not memorized definitions.

### Runnable mini-lab

Declare and populate each type, use collection methods, and write a comparison table for indexing, size, SQL visibility and ordering.

### Verify

You can compare all three in 60–90 seconds without notes.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 18 — Associative Arrays + Nested Tables/VARRAYs ⬜

**Planned date:** 2026-12-01  
**SQL track:** Index-by lookup; SQL collection interaction  
**Oracle / PL/SQL track:** Choosing the right collection

### Tutorial

Go deeper on the differences that interviewers probe after the definition question.

### Runnable mini-lab

Build an employee lookup with associative arrays, query a nested-table SQL type using TABLE(...), and create one bounded VARRAY example.

### Verify

Given a scenario, you can justify the collection choice.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 19 — BULK COLLECT + LIMIT ⬜

**Planned date:** 2026-12-02  
**SQL track:** Bulk fetching  
**Oracle / PL/SQL track:** Context switching + PGA control

### Tutorial

Bulk fetching reduces SQL↔PL/SQL context switches, but collections consume PGA. LIMIT is about bounded batching, not a magic number.

### Runnable mini-lab

Process a large cursor using LIMIT 100, 1000 and 10000; record elapsed time and batch counts.

### Verify

You can explain speed vs PGA trade-offs and why loading millions of rows at once is risky.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 20 — PL/SQL Checkpoint Mock ⬜

**Planned date:** 2026-12-03  
**SQL track:** Core PL/SQL + cursors + collections  
**Oracle / PL/SQL track:** Oral interview checkpoint

### Tutorial

This is the first dedicated PL/SQL retrieval checkpoint.

### Runnable mini-lab

30 min rapid-fire + 30 min coding: packages, exceptions, cursors, REF CURSOR, collection differences and BULK COLLECT/LIMIT.

### Verify

Any answer requiring notes is marked Yellow even if technically correct.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 21 — FORALL + SAVE EXCEPTIONS ⬜

**Planned date:** 2026-12-04  
**SQL track:** Bulk DML  
**Oracle / PL/SQL track:** SQL%BULK_EXCEPTIONS

### Tutorial

FORALL complements BULK COLLECT by reducing PL/SQL→SQL switches for repeated DML. Production bulk code also needs row-level error handling.

### Runnable mini-lab

Compare row-by-row DML with FORALL. Inject bad rows and capture failures with SAVE EXCEPTIONS into an error table.

### Verify

You can explain BULK COLLECT vs FORALL and recover individual error details.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 22 — Dynamic SQL + PL/SQL Phase Assessment ⬜

**Planned date:** 2026-12-05  
**SQL track:** EXECUTE IMMEDIATE; binds  
**Oracle / PL/SQL track:** Safe dynamic structure

### Tutorial

Dynamic SQL belongs where structure is genuinely runtime-dependent; bind data values and validate identifiers.

### Runnable mini-lab

Build a safe dynamic report with whitelisted object/sort identifiers and bound values. Finish with a package/collection/bulk-processing code review.

### Verify

You can identify SQL injection risk and score at least 80% on the phase assessment.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

# Phase 3 — Optimizer, Execution Plans & Indexing

## Day 23 — Optimizer Mental Model ⬜

**Planned date:** 2026-12-07  
**SQL track:** Cost-based optimizer  
**Oracle / PL/SQL track:** Parse → optimize → execute

### Tutorial

Understand what the optimizer is deciding before trying to tune individual SQL statements.

### Runnable mini-lab

Capture plans for three equivalent queries and predict access path/join choices before reading the plan.

### Verify

You can distinguish cost from elapsed time and explain why the optimizer needs statistics.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 24 — EXPLAIN PLAN & DBMS_XPLAN ⬜

**Planned date:** 2026-12-08  
**SQL track:** Plan reading  
**Oracle / PL/SQL track:** Access paths; predicates

### Tutorial

Learn a consistent plan-reading method: row sources, access paths, join methods, estimated rows and predicates.

### Runnable mini-lab

Use EXPLAIN PLAN + DBMS_XPLAN.DISPLAY on 5 queries and annotate each operation in plain English.

### Verify

You can read a plan without declaring every FULL TABLE SCAN bad.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 25 — Executed Plans & Runtime Statistics ⬜

**Planned date:** 2026-12-09  
**SQL track:** DISPLAY_CURSOR concepts  
**Oracle / PL/SQL track:** Estimated vs actual rows

### Tutorial

Tuning improves when you compare estimates with runtime reality where your privileges/environment allow it.

### Runnable mini-lab

Capture an executed cursor plan and compare E-Rows/A-Rows where available. Create one case with a large estimate mismatch.

### Verify

You can explain what an estimate mismatch suggests and what you would investigate next.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 26 — Cardinality, Selectivity & Statistics ⬜

**Planned date:** 2026-12-10  
**SQL track:** Optimizer estimates; DBMS_STATS  
**Oracle / PL/SQL track:** Data distribution

### Tutorial

Cardinality estimates drive join order, access path and join algorithm choices.

### Runnable mini-lab

Create selective/nonselective predicates, gather stats, and compare plans before/after stats refresh.

### Verify

You can explain stale stats, selectivity and cardinality in practical terms.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 27 — Histograms & Skew ⬜

**Planned date:** 2026-12-11  
**SQL track:** Skewed distributions  
**Oracle / PL/SQL track:** Value-sensitive plans

### Tutorial

A column can be low-cardinality yet highly skewed; distribution matters.

### Runnable mini-lab

Create 99% ACTIVE / 1% BLOCKED data, gather statistics and compare estimates/plans for each value.

### Verify

You can explain why different values may deserve different plans.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 28 — B-tree Indexes & Access Paths ⬜

**Planned date:** 2026-12-12  
**SQL track:** B-tree; unique; range scan  
**Oracle / PL/SQL track:** General-purpose indexing

### Tutorial

Indexes are workload structures with read benefits and DML/storage costs.

### Runnable mini-lab

Create/drop indexes on selective columns and compare FULL, INDEX RANGE SCAN and UNIQUE SCAN cases.

### Verify

You can explain why an index may be ignored and why more indexes are not automatically better.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 29 — Composite + Function-Based Indexes ⬜

**Planned date:** 2026-12-14  
**SQL track:** Leading columns; indexed expressions  
**Oracle / PL/SQL track:** Predicate alignment

### Tutorial

Index column order and query expressions directly affect usable access paths.

### Runnable mini-lab

Test (customer_id, transaction_date) with different predicates. Add UPPER(name) function-based index and compare.

### Verify

You can explain the leading-column principle and sargability without overclaiming absolute rules.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 30 — Bitmap + Local/Global Index Concepts ⬜

**Planned date:** 2026-12-15  
**SQL track:** Bitmap; partition-aware indexes  
**Oracle / PL/SQL track:** Workload trade-offs

### Tutorial

Bitmap indexes fit read-heavy analytic workloads better than concurrent OLTP. Local/global indexing will matter again in partitioning.

### Runnable mini-lab

Create a disposable bitmap example and write a B-tree vs bitmap decision table. Review local vs global terminology.

### Verify

You can explain bitmap DML/concurrency concerns at a high level.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 31 — Join Methods ⬜

**Planned date:** 2026-12-16  
**SQL track:** Nested loops; hash; merge  
**Oracle / PL/SQL track:** Choosing by row volume and access

### Tutorial

Interviewers often ask which join algorithm fits which workload; answer with conditions, not slogans.

### Runnable mini-lab

Create selective and broad joins and observe nested-loop vs hash choices. Document when merge join can appear.

### Verify

You can compare the three major join methods and the concept of a driving row source.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 32 — Bind Variables, Hard/Soft Parse ⬜

**Planned date:** 2026-12-17  
**SQL track:** Bind reuse; shared pool  
**Oracle / PL/SQL track:** Parse efficiency

### Tutorial

Bind variables are primarily about reusable SQL/cursor execution and reduced hard parsing, not convenience.

### Runnable mini-lab

Execute literal vs bind-style repeated statements and inspect available cursor information.

### Verify

You can explain hard vs soft parse and why bind reuse matters.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 33 — Bind Peeking & Adaptive Cursor Concepts ⬜

**Planned date:** 2026-12-18  
**SQL track:** Bind-sensitive plans  
**Oracle / PL/SQL track:** Skew + reuse tension

### Tutorial

One reusable SQL statement may not have one ideal plan when bind values have radically different selectivity.

### Runnable mini-lab

Use skewed data to reason about ACTIVE vs BLOCKED plans and inspect behavior supported by your local version.

### Verify

You can describe the problem without claiming behavior is identical across all Oracle releases.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 34 — SQL Tuning Anti-Patterns ⬜

**Planned date:** 2026-12-19  
**SQL track:** Implicit conversions; non-sargable predicates; DISTINCT  
**Oracle / PL/SQL track:** Plan-unfriendly SQL

### Tutorial

Many tuning wins come from fixing SQL before adding indexes or hints.

### Runnable mini-lab

Refactor 5 intentionally bad queries and compare before/after plans where meaningful.

### Verify

You can identify common plan-hostile SQL patterns quickly in code review.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 35 — Hints, PARALLEL & APPEND ⬜

**Planned date:** 2026-12-21  
**SQL track:** INDEX/FULL/join hints; PARALLEL; APPEND  
**Oracle / PL/SQL track:** Controlled optimizer/load influence

### Tutorial

Hints are tools, not magic. Learn optimizer hints and the two advanced hints already seen in interviews.

### Runnable mini-lab

Test INDEX/FULL and one join hint; test PARALLEL and APPEND in a disposable high-volume lab where supported.

### Verify

You can explain when Oracle may ignore a hint, resource trade-offs of PARALLEL, and direct-path intent of APPEND.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 36 — Performance Mock Interview #2 ⬜

**Planned date:** 2026-12-22  
**SQL track:** Plans; indexes; optimizer  
**Oracle / PL/SQL track:** Evidence-based tuning

### Tutorial

No new theory: turn performance knowledge into concise interview answers.

### Runnable mini-lab

Review 3 plans, recommend or reject indexes, explain stats/cardinality, then answer 15 rapid-fire tuning questions.

### Verify

You can defend every recommendation with evidence rather than 'indexes are faster'.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

# Phase 4 — Partitioning & High-Volume Loading

## Day 37 — Partitioning Fundamentals + Range ⬜

**Planned date:** 2026-12-23  
**SQL track:** Partition keys; RANGE  
**Oracle / PL/SQL track:** Large-table manageability

### Tutorial

Partitioning is both a manageability and pruning tool. Start from workload and retention needs.

### Runnable mini-lab

Create monthly range partitions for transactions and query one month. Inspect partition start/stop in the plan.

### Verify

You can explain partitioning vs indexing and why date-based range is common.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 38 — Interval, List & Hash Partitioning ⬜

**Planned date:** 2026-12-24  
**SQL track:** INTERVAL; LIST; HASH  
**Oracle / PL/SQL track:** Partition strategy selection

### Tutorial

Know the major Oracle partitioning types and what problem each solves.

### Runnable mini-lab

Create small interval, list and hash examples and insert enough rows to inspect placement.

### Verify

Given a workload, you can choose among range/interval/list/hash and justify it.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 39 — Composite Partitioning ⬜

**Planned date:** 2026-12-25  
**SQL track:** Range-hash; range-list  
**Oracle / PL/SQL track:** Two-dimensional organization

### Tutorial

Composite partitioning combines lifecycle/query boundaries with secondary distribution or categories.

### Runnable mini-lab

Create a range-hash transaction table by month/account and inspect pruning/distribution.

### Verify

You can name a real reason to use composite partitioning instead of adding complexity by default.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 40 — Partitioning Checkpoint Mock ⬜

**Planned date:** 2026-12-26  
**SQL track:** Partition type selection  
**Oracle / PL/SQL track:** Rapid-fire architecture questions

### Tutorial

Pause early in this phase because partitioning was a known gap.

### Runnable mini-lab

Answer 20 short scenarios selecting partition type/key and explaining trade-offs. Include time series, region and evenly distributed IDs.

### Verify

You can answer core partitioning questions without notes in under 90 seconds each.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 41 — Partition Pruning ⬜

**Planned date:** 2026-12-28  
**SQL track:** Static/dynamic pruning concepts  
**Oracle / PL/SQL track:** Predicate design

### Tutorial

Pruning matters only if Oracle can exclude irrelevant partitions based on predicates and plan structure.

### Runnable mini-lab

Compare pruning-friendly date predicates with intentionally awkward variants and inspect PSTART/PSTOP.

### Verify

You can identify pruning in a plan and explain why a predicate enables or hinders it.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 42 — Local vs Global Partitioned Indexes ⬜

**Planned date:** 2026-12-29  
**SQL track:** LOCAL/GLOBAL  
**Oracle / PL/SQL track:** Maintenance vs access needs

### Tutorial

Partition-aware index design is a common follow-up to partitioning questions.

### Runnable mini-lab

Create local and global indexes on disposable partitioned tables, then perform a maintenance operation and inspect consequences.

### Verify

You can explain why local indexes simplify partition maintenance and when global access may still be needed.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 43 — Partition Exchange ⬜

**Planned date:** 2026-12-30  
**SQL track:** EXCHANGE PARTITION  
**Oracle / PL/SQL track:** Fast batch loading

### Tutorial

Partition exchange is one of the highest-value advanced Oracle/ETL interview topics from your feedback.

### Runnable mini-lab

Build a compatible standalone staging table, load/validate it, exchange it with a target partition and verify row locations afterward.

### Verify

You can explain 'metadata-oriented swap' plus compatibility/validation requirements.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 44 — Partition Maintenance ⬜

**Planned date:** 2026-12-31  
**SQL track:** ADD/SPLIT/MERGE/TRUNCATE/DROP  
**Oracle / PL/SQL track:** Data lifecycle

### Tutorial

Partitioning can turn massive row-level retention work into partition-level maintenance.

### Runnable mini-lab

Add a future partition and truncate/drop an old one on lab data; review split/merge concepts.

### Verify

You can explain when partition maintenance is preferable to millions of DELETEs.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 45 — CTAS, Staging & Direct-Path Loads ⬜

**Planned date:** 2027-01-01  
**SQL track:** CTAS; staging; APPEND  
**Oracle / PL/SQL track:** High-throughput data movement

### Tutorial

High-volume Oracle development often combines staging, CTAS, direct-path insert, statistics and partition operations.

### Runnable mini-lab

Create transformed staging data with CTAS, gather stats, then load a target with conventional vs APPEND insert.

### Verify

You can describe the load path and recovery implications rather than just quoting syntax.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 46 — MERGE at Scale + Parallel DML ⬜

**Planned date:** 2027-01-02  
**SQL track:** Large MERGE; parallel DML  
**Oracle / PL/SQL track:** Controlled batch throughput

### Tutorial

Scale changes the trade-offs of MERGE and DML. Matching indexes, data volume and resource windows matter.

### Runnable mini-lab

Load a delta table, MERGE with/without supporting key index, and experiment with parallel DML only in the disposable lab.

### Verify

You can explain why parallel DML belongs in controlled batch workloads.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 47 — Intermediate Data Architecture ⬜

**Planned date:** 2027-01-04  
**SQL track:** GTT vs collection vs permanent staging  
**Oracle / PL/SQL track:** Restartability + memory + SQL visibility

### Tutorial

Revisit intermediate-data choices now that you understand bulk PL/SQL and high-volume SQL.

### Runnable mini-lab

Choose a design for 10k lookup values, 5M intermediate rows, and a restartable overnight batch. Implement one.

### Verify

You can justify the choice using volume, PGA, SQL visibility, cross-statement reuse and restart needs.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 48 — High-Volume Load Assessment ⬜

**Planned date:** 2027-01-05  
**SQL track:** Partition exchange; pruning; APPEND; MERGE  
**Oracle / PL/SQL track:** End-to-end load design

### Tutorial

Design a realistic batch rather than discussing each feature in isolation.

### Runnable mini-lab

Design a 5M-row monthly load: stage, validate, transform, load/exchange, index/stats, recover and validate. Explain alternatives.

### Verify

Your design covers performance, correctness, observability and safe restart.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

# Phase 5 — Transactions & Production Engineering

## Day 49 — Read Consistency & MVCC ⬜

**Planned date:** 2027-01-06  
**SQL track:** Undo; statement consistency  
**Oracle / PL/SQL track:** Oracle transaction model

### Tutorial

Oracle read consistency is a core production concept and differentiates good Oracle answers from generic database answers.

### Runnable mini-lab

Use two sessions: update without commit in A, query in B, then commit/rollback and observe visibility.

### Verify

You can explain why readers generally see a consistent committed view rather than another session's uncommitted changes.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 50 — Locks, Blocking & SELECT FOR UPDATE ⬜

**Planned date:** 2027-01-07  
**SQL track:** Row locking; NOWAIT/SKIP LOCKED  
**Oracle / PL/SQL track:** Pessimistic coordination

### Tutorial

Blocking is normal coordination; prolonged blocking is the problem. SELECT FOR UPDATE is intentional pessimistic locking.

### Runnable mini-lab

Use two sessions to block on one row, then test NOWAIT or SKIP LOCKED on a job-claim example.

### Verify

You can distinguish normal blocking from a design/performance problem.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 51 — Deadlocks ⬜

**Planned date:** 2027-01-08  
**SQL track:** Circular dependencies  
**Oracle / PL/SQL track:** ORA-00060 reasoning

### Tutorial

Deadlock is not simply 'a long lock'. It is a cycle of dependencies.

### Runnable mini-lab

Create a controlled two-row/two-session deadlock, capture the error, then fix it by consistent lock ordering.

### Verify

You can explain blocking vs deadlock in one sentence each.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 52 — Commit Boundaries & Savepoints ⬜

**Planned date:** 2027-01-09  
**SQL track:** COMMIT/ROLLBACK/SAVEPOINT  
**Oracle / PL/SQL track:** Business atomicity

### Tutorial

Commit strategy should model the business transaction and recovery plan rather than arbitrary row counts.

### Runnable mini-lab

Build a transfer workflow with a savepoint and test full rollback vs rollback-to-savepoint.

### Verify

You can defend the transaction boundary and explain why 'commit every N rows' is not automatically correct.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 53 — Restartability & Idempotency ⬜

**Planned date:** 2027-01-11  
**SQL track:** Batch keys; checkpoints  
**Oracle / PL/SQL track:** Safe reruns

### Tutorial

Interviewers value production judgment: what happens after 60% of a batch committed and the process failed?

### Runnable mini-lab

Build a batch-run/checkpoint table, force a mid-batch failure, rerun and prove no duplicates occur.

### Verify

Your recovery answer starts with what committed, restart point, idempotency and validation.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 54 — Logging, Security & Rights ⬜

**Planned date:** 2027-01-12  
**SQL track:** Instrumentation; dynamic SQL safety; AUTHID  
**Oracle / PL/SQL track:** Production hygiene

### Tutorial

Combine the production concerns that need recognition but not a full DBA track.

### Runnable mini-lab

Add batch logging/module context, validate dynamic identifiers, bind data values, and review definer vs invoker rights.

### Verify

You can distinguish audit vs diagnostic logging and state the bind-data/validate-identifiers rule.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 55 — Production Mock Interview #3 ⬜

**Planned date:** 2027-01-13  
**SQL track:** Concurrency; recovery; security  
**Oracle / PL/SQL track:** Incident reasoning

### Tutorial

Close the engineering phase with real failure scenarios.

### Runnable mini-lab

Work through 5 incidents: blocking payment, deadlock, half-loaded batch, SQL injection risk and runaway bulk collection.

### Verify

For each: diagnose → impact → safe recovery → validation → prevention.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

# Phase 6 — Oracle Interview Bootcamp

## Day 56 — SQL Interview Bootcamp ⬜

**Planned date:** 2027-01-14  
**SQL track:** Timed SQL coding  
**Oracle / PL/SQL track:** Fast solution selection

### Tutorial

Switch fully into interview mode. No broad new content.

### Runnable mini-lab

45 min coding: joins/subqueries, analytics, duplicates and MERGE; 30 min explanation/code review.

### Verify

You can solve medium SQL questions cleanly without overengineering.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 57 — PL/SQL Interview Bootcamp ⬜

**Planned date:** 2027-01-15  
**SQL track:** Timed PL/SQL coding  
**Oracle / PL/SQL track:** Packages/cursors/collections/bulk

### Tutorial

Practice producing syntactically credible PL/SQL while narrating trade-offs.

### Runnable mini-lab

Implement one package routine, one SYS_REFCURSOR routine and one bulk-processing task under time.

### Verify

You can code and explain without relying on templates.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 58 — Optimizer Rapid-Fire ⬜

**Planned date:** 2027-01-16  
**SQL track:** Plans; stats; access paths  
**Oracle / PL/SQL track:** Performance explanation

### Tutorial

Make tuning answers short, structured and evidence-based.

### Runnable mini-lab

Review 4 unfamiliar plans and answer 20 rapid-fire questions on cardinality, access paths, joins, stats and binds.

### Verify

You consistently start with plan/evidence rather than guessing.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 59 — Indexing & SQL Review Drill ⬜

**Planned date:** 2027-01-18  
**SQL track:** Index selection; sargability  
**Oracle / PL/SQL track:** Senior code review

### Tutorial

Interviews may give you code rather than a direct definition question.

### Runnable mini-lab

Review 10 query/index scenarios and decide: add index, change index, rewrite SQL, update stats, or do nothing.

### Verify

You can defend 'do nothing / full scan is fine' when appropriate.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 60 — Partitioning Rapid-Fire ⬜

**Planned date:** 2027-01-19  
**SQL track:** Partitioning; pruning; indexes  
**Oracle / PL/SQL track:** Architecture explanation

### Tutorial

Make the advanced area that previously surprised you one of your strongest interview sections.

### Runnable mini-lab

Answer 25 rapid-fire questions: types, pruning, local/global indexes, exchange, maintenance and key selection.

### Verify

You can explain partition exchange and pruning clearly in under 90 seconds.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 61 — High-Volume ETL / Batch Drill ⬜

**Planned date:** 2027-01-20  
**SQL track:** GTT; staging; bulk; APPEND; PARALLEL  
**Oracle / PL/SQL track:** Design trade-offs

### Tutorial

Combine Oracle developer and ETL thinking into one interview scenario.

### Runnable mini-lab

Design two solutions for a 10M-row nightly load: set-based/partition strategy and PL/SQL bulk strategy. Explain which you prefer and why.

### Verify

You can choose between SQL, PL/SQL bulk, GTT/staging and partition operations based on requirements.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 62 — Transactions & Incident Drill ⬜

**Planned date:** 2027-01-21  
**SQL track:** Locks; commits; recovery  
**Oracle / PL/SQL track:** Production judgment

### Tutorial

Practice the questions that distinguish senior production experience from syntax knowledge.

### Runnable mini-lab

Answer scenarios on duplicate requests, blocked rows, deadlocks, partial commits, retries and SELECT FOR UPDATE.

### Verify

Your answers explicitly cover correctness, concurrency and recovery.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 63 — Java + Oracle Boundary Review ⬜

**Planned date:** 2027-01-22  
**SQL track:** JDBC binds; result sets; transactions  
**Oracle / PL/SQL track:** Backend integration recognition

### Tutorial

Your main profile is Java + Oracle, so be ready for boundary questions without turning this into a Java roadmap.

### Runnable mini-lab

Explain PreparedStatement/binds, calling SYS_REFCURSOR, transaction ownership, connection pooling and why N+1/database round trips matter.

### Verify

You can connect Oracle behavior to Java backend decisions clearly.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 64 — Full Mock Interview #1 + Gap Repair ⬜

**Planned date:** 2027-01-23  
**SQL track:** All covered areas  
**Oracle / PL/SQL track:** 90-minute simulation

### Tutorial

Simulate a realistic senior Oracle technical interview and immediately turn misses into a small repair list.

### Runnable mini-lab

90 minutes: 25 SQL, 20 PL/SQL, 20 tuning, 15 partitioning/high-volume, 10 production. Then repair the 3 weakest answers.

### Verify

No new broad topics are added. Only evidence-based gap repair.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

## Day 65 — Final Oracle Interview Evaluation ⬜

**Planned date:** 2027-01-25  
**SQL track:** SQL + PL/SQL + performance + production  
**Oracle / PL/SQL track:** Explain, code, tune, recover

### Tutorial

The roadmap ends with demonstrated interview readiness, not completion by calendar.

### Runnable mini-lab

Run a final 100-minute mock plus the completion rubric. Record Strong / Needs Reinforcement / Gap and produce a one-page last-minute review sheet.

### Verify

You can answer core questions concisely, code under time, read a plan, discuss partition/load trade-offs, and handle production scenarios.

### Interview drill

- Explain the topic aloud in 60–90 seconds.
- Answer 3–5 follow-up questions without notes.
- For coding/tuning sessions, narrate your reasoning while solving.

### Done when

- [ ] Explain the concept in English without notes.
- [ ] Complete the mini-lab or timed exercise.
- [ ] Intentionally change/break/inspect at least one thing where applicable.
- [ ] Save the SQL/PLSQL script or interview notes.
- [ ] Record any weak point as Green / Yellow / Red.

---

# Final completion rubric

| Area | Strong | Needs reinforcement | Gap |
|---|:---:|:---:|:---:|
| JOINs / aggregation / subqueries / EXISTS | ☐ | ☐ | ☐ |
| CTE / WITH / MERGE / GTT | ☐ | ☐ | ☐ |
| Analytic functions | ☐ | ☐ | ☐ |
| Procedures / functions / packages | ☐ | ☐ | ☐ |
| Exceptions / cursors / REF CURSOR | ☐ | ☐ | ☐ |
| Collection types | ☐ | ☐ | ☐ |
| BULK COLLECT / LIMIT / FORALL | ☐ | ☐ | ☐ |
| Dynamic SQL / binds / security | ☐ | ☐ | ☐ |
| Execution plans / DBMS_XPLAN | ☐ | ☐ | ☐ |
| Cardinality / stats / histograms | ☐ | ☐ | ☐ |
| Indexing | ☐ | ☐ | ☐ |
| Join methods | ☐ | ☐ | ☐ |
| Bind variables / hard-soft parse | ☐ | ☐ | ☐ |
| Hints / PARALLEL / APPEND | ☐ | ☐ | ☐ |
| Partitioning types | ☐ | ☐ | ☐ |
| Partition pruning | ☐ | ☐ | ☐ |
| Local / global indexes | ☐ | ☐ | ☐ |
| Partition exchange / maintenance | ☐ | ☐ | ☐ |
| Transactions / locks / deadlocks | ☐ | ☐ | ☐ |
| Restartability / idempotency | ☐ | ☐ | ☐ |
| Production troubleshooting | ☐ | ☐ | ☐ |
| Timed SQL coding | ☐ | ☐ | ☐ |
| Timed PL/SQL coding | ☐ | ☐ | ☐ |
| Oral interview explanations | ☐ | ☐ | ☐ |

## Interview-readiness gate

Do not judge readiness by having reached Day 65. You are ready when you can reliably do all four:

~~~text
1. Solve SQL under time pressure
2. Write and explain PL/SQL
3. Read a plan and justify tuning decisions
4. Reason safely about high-volume and production failures
~~~

## Maintenance after Day 65

Keep Oracle sharp with 3 short sessions/week:

~~~text
Session A → SQL challenge
Session B → execution plan / index / partition scenario
Session C → PL/SQL or production incident question
~~~

20–30 minutes is enough for maintenance once the main roadmap is complete.
