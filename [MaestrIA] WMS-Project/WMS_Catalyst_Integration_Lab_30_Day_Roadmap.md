# [MaestrIA] WMS — Catalyst Integration Lab (30-Day Roadmap)

> **Independent learning track.** Inspired by the presentation and completion discipline of the existing Java + Spring Boot roadmap, but **not dependent on its Payment codebase or calendar**.

**Purpose:** build functional fluency in warehouse management systems (with special attention to concepts relevant to Aptean Catalyst Edition) while implementing a realistic, standalone REST API and automation simulator in Java.

**Cadence:** ~90 minutes per study session; aim for six sessions per week and keep one rest day. **30 days means 30 study sessions**, normally ~5 calendar weeks. Start dates are intentionally unassigned.

**Completion rule:** a day is done only when you can **explain the warehouse process + implement it + verify it + diagnose at least one failure**. Code and documentation must be committed.

**Outputs:** one evolving Git repository; one detailed Markdown/Joplin note per day; one concise handwritten/typed recap; optional Anki TSV (`Front`, `Back`, `Tags`) and architecture decision records (ADRs).

## Scope and accuracy

The project is an **educational WMS simulator**, not a reverse-engineered or officially documented clone of Aptean Catalyst. Workflows and entity names reflect general WMS concepts. Simulated event names, schemas, status values and rules must be clearly marked as **lab assumptions** unless confirmed by an authorized source. Actual Catalyst behavior and Symbotic contracts may differ by version and deployment.

By Day 30 you should be able to discuss receiving, inventory, directed putaway, allocation, picking, shipping, JPA consistency and integration recovery with engineers and operations staff. The goal is **strong working proficiency**, not full mastery of proprietary Catalyst configuration in 45 hours.

## Technology decisions

| Component | Choice | Why |
|---|---|---|
| Language | Java 21 | Stable modern Java, records, switch, concurrency features |
| Framework | Spring Boot 4.x | REST, DI, transactions, tests; use mutually compatible starters |
| ORM | Jakarta Persistence + Hibernate / Spring Data JPA | Entity modeling and persistence mastery |
| Database | PostgreSQL 17 | Transactions, locking, constraints, SQL inspection |
| Schema changes | Hibernate `ddl-auto=update` only for early throwaway local experiments, then Liquibase + `ddl-auto=validate` | Learn mapping without losing migration discipline |
| Testing | JUnit, Mockito, Spring Boot Test, Testcontainers | Test business behavior against real PostgreSQL |
| Infrastructure | Docker Compose | Repeatable local dependencies |
| Messaging | RabbitMQ **or** Kafka, chosen on Day 27 | Explore async delivery without premature complexity |
| API documentation | OpenAPI | Define and validate boundary contracts |

**Architecture:** modular monolith for the WMS core, plus a separate automation simulator. Avoid microservices until the business model and transaction boundaries are clear.

**Modeling principles:** no public JPA entities in REST request/response payloads; use DTO records; maintain an immutable inventory movement ledger in addition to balance snapshots; establish unique business identifiers and constraints; never rely only on in-memory locks for financial or stock correctness.

## Codebase continuity rule

All 30 sessions evolve the **same** `wms-integration-lab` project. One commit per completed session (e.g., `day-13-putaway-task`), with small tests and notes. Do not copy modules day by day. The previous Java roadmap remains untouched.

```text
wms-integration-lab/
├── README.md
├── compose.yaml
├── docs/
│   ├── architecture/
│   ├── adr/
│   ├── glossary.md
│   ├── assumptions.md
│   └── days/
├── src/main/java/.../wms/
│   ├── shared/
│   ├── catalog/
│   ├── warehouse/
│   ├── inbound/
│   ├── inventory/
│   ├── putaway/
│   ├── outbound/
│   └── integration/
├── src/main/resources/db/changelog/
└── src/test/java/.../wms/
```

## Daily session template (90 minutes)

| Minutes | Activity |
|---:|---|
| 0–25 | Functional tutorial: process, actors, terms, invariants |
| 25–65 | Runnable Java/Spring mini-lab and API endpoint or test |
| 65–80 | Verify: automated tests, SQL checks, one deliberate failure |
| 80–90 | Explain in English, review questions, Joplin notes and commit |

**Reusable daily note:** `Learning Objectives` · `Functional Tutorial` · `Java/Spring Tutorial` · `Assumptions` · `Runnable Mini-Lab` · `Verify` · `Troubleshooting` · `Review Questions` · `References` · `Done When`.

## Master index — Day 01 to Day 30

| Day | Status | Functional WMS track | Java / Spring / JPA track | Deliverable |
|---:|:---:|---|---|---|
| 01 | ⬜ | WMS Architecture & Project Bootstrap | Spring Boot project, PostgreSQL Docker Compose, modular package boundaries | Run PostgreSQL, start app, create /actuator/health and an architecture diagram |
| 02 | ⬜ | Warehouse, Zones & Locations | @Entity, IDs, @ManyToOne, unique constraints, Hibernate DDL inspection | Create Warehouse, Zone, Location entities and POST/GET location APIs |
| 03 | ⬜ | SKU, UOM & Master Data | Product entity, records as DTOs, Bean Validation, @RestControllerAdvice | Implement product create/read/search endpoints with SKU uniqueness |
| 04 | ⬜ | LPNs & Handling Units | Relationships, enum states, ownership, cascade/orphanRemoval trade-offs | Create LPN and LpnItem |
| 05 | ⬜ | Inventory Positions | InventoryBalance, unique composite key, repositories, projections | Implement GET /inventory?sku=&location= and balance summary |
| 06 | ⬜ | Checkpoint: Master Data & Persistence | @DataJpaTest, Testcontainers PostgreSQL, SQL logging, basic API tests | Write integration tests and publish an ERD |
| 07 | ⬜ | Purchase Orders & ASNs | PurchaseOrder, Asn, line entities, aggregate boundaries | Implement ASN create/read endpoints from synthetic data |
| 08 | ⬜ | Receiving Workflow | ReceivingService, state transitions, transaction boundaries | POST /receipts with ASN, SKU, quantity and LPN |
| 09 | ⬜ | Receiving Exceptions | Domain exceptions, ProblemDetail, validation rules | Implement partial receipts and discrepancy reporting |
| 10 | ⬜ | Stock Ledger & Atomicity | @Transactional, InventoryMovement, dirty checking, constraints | Post receipt movements and update inventory atomically |
| 11 | ⬜ | Lot, Batch & Expiry | Lot entity, date types, composite inventory identity | Record lot and expiration on receipts |
| 12 | ⬜ | Checkpoint: Inbound E2E | Spring Boot integration tests, seeded fixtures | Demo complete inbound REST workflow |
| 13 | ⬜ | Putaway Tasks & Lifecycle | PutawayTask, state machine, optimistic version column | POST /putaway-tasks, GET task, transition endpoints |
| 14 | ⬜ | Putaway Strategies | Strategy pattern, rule interfaces, DI lists/maps | Implement at least two destination-selection strategies |
| 15 | ⬜ | Capacity & Restrictions | Specification-style predicates, validation services | Reject locations exceeding capacity or violating constraints |
| 16 | ⬜ | Inventory Transfers | InventoryMovement ledger, aggregate services, JPA locking | Complete putaway by moving quantities between locations |
| 17 | ⬜ | Concurrent Movements | @Version, PESSIMISTIC_WRITE, @Transactional isolation | Run parallel transfer requests against shared stock |
| 18 | ⬜ | Checkpoint: Putaway Failure Lab | Parameterized tests, recovery workflow, Testcontainers | Simulate five failures and document responses |
| 19 | ⬜ | Sales Orders & Fulfillment | Order entities, request/response DTOs, invariants | POST /orders and GET /orders/{id} |
| 20 | ⬜ | Inventory Allocation | AllocationService, Reservation, transactional updates | Reserve stock for orders by SKU and lot policy |
| 21 | ⬜ | Wave Planning | Sorting, grouping, paginated queries, explicit scheduling | Generate a wave from eligible orders |
| 22 | ⬜ | Picking Tasks | PickTask lifecycle, projections, error handling | Generate and confirm picks for allocated inventory |
| 23 | ⬜ | Packing, Staging & Shipping Readiness | Shipment aggregate, state transitions, DTO validation | Pack items and move completed orders into ready-to-ship state |
| 24 | ⬜ | Checkpoint: Outbound E2E | Integration testing, indexing, EXPLAIN ANALYZE | Implement shipment confirmation and test full outbound path |
| 25 | ⬜ | Catalyst / Automation Boundaries | Ports/adapters, contract-first thinking, HTTP/AsyncAPI overview | Document an assumed Catalyst→automation putaway command |
| 26 | ⬜ | Commands, Acks & HTTP Clients | Spring RestClient, timeouts, correlation IDs, DTO mapping | Send a putaway command to a local automation simulator |
| 27 | ⬜ | Asynchronous Integration | RabbitMQ or Kafka in Docker | Publish PutawayRequested and consume PutawayCompleted |
| 28 | ⬜ | Idempotency, Outbox & Recovery | Inbox deduplication, transactional outbox, retry/DLQ policy | Re-deliver completion events and recover from publish failure |
| 29 | ⬜ | Observability & Troubleshooting | Structured logs, Actuator, metrics, SQL reconciliation queries | Diagnose simulated 'completed physically, WMS pending' incident |
| 30 | ⬜ | Capstone & Architecture Review | End-to-end tests, API docs, diagrams, performance notes | Demo ASN → receive → putaway → allocate → pick → ship + failure recovery |

## Module map and checkpoints

### Module 1 — WMS Foundations and Domain Modeling

**Goal:** Understand warehouse structure and establish a clean REST/JPA foundation.

**Checkpoint (Day 06):** demonstrate the module's full workflow and pass unit/integration tests.

### Module 2 — Inbound and Receiving

**Goal:** Implement how expected stock becomes controlled warehouse inventory.

**Checkpoint (Day 12):** demonstrate the module's full workflow and pass unit/integration tests.

### Module 3 — Putaway and Inventory Consistency

**Goal:** Explore directed putaway, failure modes and concurrent inventory control.

**Checkpoint (Day 18):** demonstrate the module's full workflow and pass unit/integration tests.

### Module 4 — Outbound, Allocation and Shipping

**Goal:** Implement stock reservation and controlled order fulfillment.

**Checkpoint (Day 24):** demonstrate the module's full workflow and pass unit/integration tests.

### Module 5 — Integration, Automation and Operations

**Goal:** Connect the simulated WMS to an automation adapter and build recovery skills.

**Checkpoint (Day 30):** demonstrate the module's full workflow and pass unit/integration tests.

## Detailed study plan — Day 01 to Day 30

# Module 1 — WMS Foundations and Domain Modeling

## Day 01 — WMS Architecture & Project Bootstrap ⬜

**Functional track:** ERP, WMS, WES, WCS; system-of-record boundaries; receiving-to-shipping overview  
**Java/Spring track:** Spring Boot project, PostgreSQL Docker Compose, modular package boundaries  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **wms architecture & project bootstrap** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** ERP, WMS, WES, WCS; system-of-record boundaries; receiving-to-shipping overview.
- **Technical focus:** Spring Boot project, PostgreSQL Docker Compose, modular package boundaries.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Run PostgreSQL, start app, create /actuator/health and an architecture diagram. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Explain which system owns orders, inventory, and physical execution; document unknowns. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 02 — Warehouse, Zones & Locations ⬜

**Functional track:** Sites, warehouses, zones, aisles, bins, staging, capacity  
**Java/Spring track:** @Entity, IDs, @ManyToOne, unique constraints, Hibernate DDL inspection  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **warehouse, zones & locations** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Sites, warehouses, zones, aisles, bins, staging, capacity.
- **Technical focus:** @Entity, IDs, @ManyToOne, unique constraints, Hibernate DDL inspection.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Create Warehouse, Zone, Location entities and POST/GET location APIs. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Ensure location codes are unique per warehouse; inspect generated SQL. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 03 — SKU, UOM & Master Data ⬜

**Functional track:** SKU vs product, units of measure, dimensions, weights, tracking attributes  
**Java/Spring track:** Product entity, records as DTOs, Bean Validation, @RestControllerAdvice  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **sku, uom & master data** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** SKU vs product, units of measure, dimensions, weights, tracking attributes.
- **Technical focus:** Product entity, records as DTOs, Bean Validation, @RestControllerAdvice.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Implement product create/read/search endpoints with SKU uniqueness. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Reject malformed SKUs and duplicate SKUs; keep entities out of API responses. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 04 — LPNs & Handling Units ⬜

**Functional track:** Pallets, cases, totes, LPN/SSCC, parent-child handling units  
**Java/Spring track:** Relationships, enum states, ownership, cascade/orphanRemoval trade-offs  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **lpns & handling units** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Pallets, cases, totes, LPN/SSCC, parent-child handling units.
- **Technical focus:** Relationships, enum states, ownership, cascade/orphanRemoval trade-offs.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Create LPN and LpnItem; assign SKUs to a handling unit. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Reject duplicate LPN codes and inconsistent unit quantities. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 05 — Inventory Positions ⬜

**Functional track:** On-hand, available, reserved, damaged, hold, location-level balance  
**Java/Spring track:** InventoryBalance, unique composite key, repositories, projections  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **inventory positions** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** On-hand, available, reserved, damaged, hold, location-level balance.
- **Technical focus:** InventoryBalance, unique composite key, repositories, projections.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Implement GET /inventory?sku=&location= and balance summary. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Demonstrate available != on-hand; no negative balance. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 06 — Checkpoint: Master Data & Persistence ⬜

**Functional track:** Trace product and unit identity across locations  
**Java/Spring track:** @DataJpaTest, Testcontainers PostgreSQL, SQL logging, basic API tests  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **checkpoint: master data & persistence** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Trace product and unit identity across locations.
- **Technical focus:** @DataJpaTest, Testcontainers PostgreSQL, SQL logging, basic API tests.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Write integration tests and publish an ERD. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Explain mappings and run tests against real PostgreSQL. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

# Module 2 — Inbound and Receiving

## Day 07 — Purchase Orders & ASNs ⬜

**Functional track:** Purchase orders, advanced shipping notices, expected receipts  
**Java/Spring track:** PurchaseOrder, Asn, line entities, aggregate boundaries  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **purchase orders & asns** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Purchase orders, advanced shipping notices, expected receipts.
- **Technical focus:** PurchaseOrder, Asn, line entities, aggregate boundaries.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Implement ASN create/read endpoints from synthetic data. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Reject duplicate line identities and invalid quantities. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 08 — Receiving Workflow ⬜

**Functional track:** Dock check-in, scan, verify, receive, staging  
**Java/Spring track:** ReceivingService, state transitions, transaction boundaries  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **receiving workflow** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Dock check-in, scan, verify, receive, staging.
- **Technical focus:** ReceivingService, state transitions, transaction boundaries.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

POST /receipts with ASN, SKU, quantity and LPN. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Receipt changes state exactly once, with an audit record. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 09 — Receiving Exceptions ⬜

**Functional track:** Short, over, wrong SKU, damaged, rejected, unexpected goods  
**Java/Spring track:** Domain exceptions, ProblemDetail, validation rules  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **receiving exceptions** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Short, over, wrong SKU, damaged, rejected, unexpected goods.
- **Technical focus:** Domain exceptions, ProblemDetail, validation rules.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Implement partial receipts and discrepancy reporting. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Test over-receipt policy; document it as a lab assumption. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 10 — Stock Ledger & Atomicity ⬜

**Functional track:** Stock movements vs balance snapshots; traceability  
**Java/Spring track:** @Transactional, InventoryMovement, dirty checking, constraints  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **stock ledger & atomicity** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Stock movements vs balance snapshots; traceability.
- **Technical focus:** @Transactional, InventoryMovement, dirty checking, constraints.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Post receipt movements and update inventory atomically. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Force an exception and prove the transaction rolls back. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 11 — Lot, Batch & Expiry ⬜

**Functional track:** Lot tracking, FIFO/FEFO, quarantine, product-specific restrictions  
**Java/Spring track:** Lot entity, date types, composite inventory identity  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **lot, batch & expiry** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Lot tracking, FIFO/FEFO, quarantine, product-specific restrictions.
- **Technical focus:** Lot entity, date types, composite inventory identity.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Record lot and expiration on receipts; query expiring stock. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Reject expired or quarantined lot when policy disallows use. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 12 — Checkpoint: Inbound E2E ⬜

**Functional track:** ASN → receipt → staging → balance → discrepancy  
**Java/Spring track:** Spring Boot integration tests, seeded fixtures  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **checkpoint: inbound e2e** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** ASN → receipt → staging → balance → discrepancy.
- **Technical focus:** Spring Boot integration tests, seeded fixtures.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Demo complete inbound REST workflow. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Check movements reconcile to balances for all test scenarios. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

# Module 3 — Putaway and Inventory Consistency

## Day 13 — Putaway Tasks & Lifecycle ⬜

**Functional track:** Manual vs directed putaway; assignment/execution/confirmation  
**Java/Spring track:** PutawayTask, state machine, optimistic version column  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **putaway tasks & lifecycle** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Manual vs directed putaway; assignment/execution/confirmation.
- **Technical focus:** PutawayTask, state machine, optimistic version column.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

POST /putaway-tasks, GET task, transition endpoints. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Disallow illegal status transitions and duplicate completion. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 14 — Putaway Strategies ⬜

**Functional track:** Fixed bin, random, zone, proximity, consolidation  
**Java/Spring track:** Strategy pattern, rule interfaces, DI lists/maps  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **putaway strategies** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Fixed bin, random, zone, proximity, consolidation.
- **Technical focus:** Strategy pattern, rule interfaces, DI lists/maps.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Implement at least two destination-selection strategies. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Explain trade-offs and unit-test deterministic selection. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 15 — Capacity & Restrictions ⬜

**Functional track:** Volume/weight, zones, hazards, product compatibility, blocked locations  
**Java/Spring track:** Specification-style predicates, validation services  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **capacity & restrictions** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Volume/weight, zones, hazards, product compatibility, blocked locations.
- **Technical focus:** Specification-style predicates, validation services.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Reject locations exceeding capacity or violating constraints. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Test boundary conditions and explain rule precedence. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 16 — Inventory Transfers ⬜

**Functional track:** Source→in-transit→destination; location accuracy  
**Java/Spring track:** InventoryMovement ledger, aggregate services, JPA locking  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **inventory transfers** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Source→in-transit→destination; location accuracy.
- **Technical focus:** InventoryMovement ledger, aggregate services, JPA locking.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Complete putaway by moving quantities between locations. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Verify no double counting and auditable before/after states. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 17 — Concurrent Movements ⬜

**Functional track:** Race conditions, oversubscription, optimistic vs pessimistic locks  
**Java/Spring track:** @Version, PESSIMISTIC_WRITE, @Transactional isolation  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **concurrent movements** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Race conditions, oversubscription, optimistic vs pessimistic locks.
- **Technical focus:** @Version, PESSIMISTIC_WRITE, @Transactional isolation.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Run parallel transfer requests against shared stock. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Prove only valid reservations/updates commit; compare locking. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 18 — Checkpoint: Putaway Failure Lab ⬜

**Functional track:** Missing destination, failed task, duplicate confirmation, reconciliation  
**Java/Spring track:** Parameterized tests, recovery workflow, Testcontainers  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **checkpoint: putaway failure lab** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Missing destination, failed task, duplicate confirmation, reconciliation.
- **Technical focus:** Parameterized tests, recovery workflow, Testcontainers.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Simulate five failures and document responses. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Explain how to distinguish physical completion from software acknowledgment. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

# Module 4 — Outbound, Allocation and Shipping

## Day 19 — Sales Orders & Fulfillment ⬜

**Functional track:** Order lifecycle, lines, priorities, cancellation  
**Java/Spring track:** Order entities, request/response DTOs, invariants  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **sales orders & fulfillment** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Order lifecycle, lines, priorities, cancellation.
- **Technical focus:** Order entities, request/response DTOs, invariants.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

POST /orders and GET /orders/{id}. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Reject invalid quantities and unauthorized state transitions. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 20 — Inventory Allocation ⬜

**Functional track:** Available-to-promise, reservation, allocation rules, holds  
**Java/Spring track:** AllocationService, Reservation, transactional updates  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **inventory allocation** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Available-to-promise, reservation, allocation rules, holds.
- **Technical focus:** AllocationService, Reservation, transactional updates.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Reserve stock for orders by SKU and lot policy. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Two orders cannot reserve the same last units. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 21 — Wave Planning ⬜

**Functional track:** Batching tasks, order priority, release criteria, zones  
**Java/Spring track:** Sorting, grouping, paginated queries, explicit scheduling  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **wave planning** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Batching tasks, order priority, release criteria, zones.
- **Technical focus:** Sorting, grouping, paginated queries, explicit scheduling.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Generate a wave from eligible orders. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

No order assigned to two active waves. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 22 — Picking Tasks ⬜

**Functional track:** Discrete/batch/zone picking, short pick, scan confirmation  
**Java/Spring track:** PickTask lifecycle, projections, error handling  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **picking tasks** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Discrete/batch/zone picking, short pick, scan confirmation.
- **Technical focus:** PickTask lifecycle, projections, error handling.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Generate and confirm picks for allocated inventory. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

A short pick is traceable and not silently shipped. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 23 — Packing, Staging & Shipping Readiness ⬜

**Functional track:** Packing, cartons, staging, shipment readiness and holds  
**Java/Spring track:** Shipment aggregate, state transitions, DTO validation  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **packing, staging & shipping readiness** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Packing, cartons, staging, shipment readiness and holds.
- **Technical focus:** Shipment aggregate, state transitions, DTO validation.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Pack items and move completed orders into ready-to-ship state. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Validate packed quantity against picked quantity. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 24 — Checkpoint: Outbound E2E ⬜

**Functional track:** Allocation → pick → pack → ship; cancellations  
**Java/Spring track:** Integration testing, indexing, EXPLAIN ANALYZE  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **checkpoint: outbound e2e** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Allocation → pick → pack → ship; cancellations.
- **Technical focus:** Integration testing, indexing, EXPLAIN ANALYZE.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Implement shipment confirmation and test full outbound path. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Ledger matches shipped quantities; indexed queries make sense. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

# Module 5 — Integration, Automation and Operations

## Day 25 — Catalyst / Automation Boundaries ⬜

**Functional track:** WMS-WES-WCS responsibilities; identify real vs assumed interfaces  
**Java/Spring track:** Ports/adapters, contract-first thinking, HTTP/AsyncAPI overview  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **catalyst / automation boundaries** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** WMS-WES-WCS responsibilities; identify real vs assumed interfaces.
- **Technical focus:** Ports/adapters, contract-first thinking, HTTP/AsyncAPI overview.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Document an assumed Catalyst→automation putaway command. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Label all simulated fields and statuses as invented, not vendor-specific. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 26 — Commands, Acks & HTTP Clients ⬜

**Functional track:** Commands vs facts, acknowledgment vs physical completion  
**Java/Spring track:** Spring RestClient, timeouts, correlation IDs, DTO mapping  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **commands, acks & http clients** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Commands vs facts, acknowledgment vs physical completion.
- **Technical focus:** Spring RestClient, timeouts, correlation IDs, DTO mapping.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Send a putaway command to a local automation simulator. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Handle 2xx, 4xx, 5xx and timeout correctly. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 27 — Asynchronous Integration ⬜

**Functional track:** Eventual consistency, events, out-of-order and redelivery  
**Java/Spring track:** RabbitMQ or Kafka in Docker; listeners; consumer boundaries  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **asynchronous integration** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Eventual consistency, events, out-of-order and redelivery.
- **Technical focus:** RabbitMQ or Kafka in Docker; listeners; consumer boundaries.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Publish PutawayRequested and consume PutawayCompleted. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Record correlation and avoid using transport delivery as business success. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 28 — Idempotency, Outbox & Recovery ⬜

**Functional track:** Duplicate messages, retries, exactly-once business effects  
**Java/Spring track:** Inbox deduplication, transactional outbox, retry/DLQ policy  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **idempotency, outbox & recovery** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Duplicate messages, retries, exactly-once business effects.
- **Technical focus:** Inbox deduplication, transactional outbox, retry/DLQ policy.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Re-deliver completion events and recover from publish failure. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

One physical movement produces one logical inventory effect. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 29 — Observability & Troubleshooting ⬜

**Functional track:** Incident triage: stuck tasks, divergence, timeouts, missing acks  
**Java/Spring track:** Structured logs, Actuator, metrics, SQL reconciliation queries  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **observability & troubleshooting** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Incident triage: stuck tasks, divergence, timeouts, missing acks.
- **Technical focus:** Structured logs, Actuator, metrics, SQL reconciliation queries.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Diagnose simulated 'completed physically, WMS pending' incident. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Create runbook with detection, containment, investigation, reconciliation. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Day 30 — Capstone & Architecture Review ⬜

**Functional track:** Full lifecycle; explain decisions to ops and engineers  
**Java/Spring track:** End-to-end tests, API docs, diagrams, performance notes  
**Planned date:** _TBD_  
**Actual completion:** _TBD_

### Learning objectives

Understand **capstone & architecture review** in a WMS workflow, identify its inputs/outputs and invariants, and explain the technical mechanism used in today's implementation.

### Tutorial

- **Warehouse context:** Full lifecycle; explain decisions to ops and engineers.
- **Technical focus:** End-to-end tests, API docs, diagrams, performance notes.
- **Boundary question:** Which system owns the data or decision? Record unknown assumptions in `docs/assumptions.md`.

### Runnable mini-lab

Demo ASN → receive → putaway → allocate → pick → ship + failure recovery. Build on yesterday's code; commit only after the test or API demonstration works.

### Verify

Explain architecture in English, pass acceptance tests, list known gaps. Inspect SQL/logs where applicable and write down the expected vs actual outcome.

### Troubleshooting drill

Deliberately introduce one realistic defect (invalid state, missing reference, duplicate request, timeout, failed transaction or competing update). Explain impact, diagnosis, fix and how a test prevents recurrence.

### Review questions

1. What physical warehouse event does this API action represent?
2. What is the system of record for the affected data?
3. Which domain invariant must remain true after failure or retry?
4. What SQL or log evidence confirms successful execution?
5. How would you explain the failure to a warehouse operator and to a Java engineer?

### Done when

- [ ] Explain the functional concept in English without notes.
- [ ] Implement and run the mini-lab using synthetic data.
- [ ] Pass automated tests and inspect important generated SQL.
- [ ] Intentionally break one scenario; record diagnosis and fix.
- [ ] Answer 3–5 review questions or complete a short code review.
- [ ] Update detailed Joplin note, concise recap, and commit.

---

## Resources (prioritized)

### Functional / WMS
- **Gwynne Richards, _Warehouse Management_** — receiving, storage, putaway, picking and shipping. Read the sections matching each module; chapter numbers differ by edition.
- **Aptean Catalyst Edition datasheet** — product capability overview, **not** an internal implementation manual: https://assets.ctfassets.net/grb5fvwhwnyo/5YcMGpJpP8t5KoggHVwpF3/60d09196c7c1719266d846ce9d2ea45a/Aptean-WMS-Catalyst-Edition-Datasheet-en.pdf
- **Aptean AppCentral Connect** — request product-specific training and guides through authorized company access: https://appcentral-docs.apteancloud.dev/AppCentral/connect-overview/
- **Symbotic** — public automation concepts, not a substitute for your company's interface control documents: https://www.symbotic.com/
- **GS1 standards** — GTIN, GLN, SSCC and logistics identification: https://www.gs1.org/standards

### Java / persistence / integration
- Spring Boot Reference: https://docs.spring.io/spring-boot/reference/
- Spring Data JPA Reference: https://docs.spring.io/spring-data/jpa/reference/
- Hibernate ORM documentation: https://hibernate.org/orm/documentation/
- PostgreSQL documentation: https://www.postgresql.org/docs/17/
- Testcontainers for Java: https://java.testcontainers.org/
- Spring Framework REST clients: https://docs.spring.io/spring-framework/reference/integration/rest-clients.html
- Spring for Apache Kafka (if Kafka chosen): https://docs.spring.io/spring-kafka/reference/
- RabbitMQ tutorials (if RabbitMQ chosen): https://www.rabbitmq.com/tutorials

**Access-dependent research:** ask your Medline/Catalyst team for authorized configuration guides, integration contracts, message catalogs, data dictionaries, and operational runbooks. Never publish proprietary payloads, credentials, production data, internal diagrams or customer information in the lab repository.
## Capstone acceptance criteria (Day 30)

- [ ] Bootstrap via documented Docker Compose + Spring Boot commands.
- [ ] Register warehouse, locations, SKU, LPN and tracked inventory.
- [ ] Receive a synthetic ASN (including a partial receipt and discrepancy).
- [ ] Direct putaway according to a defined rule; reject an invalid destination.
- [ ] Complete inventory movements with an immutable ledger and reconciled balances.
- [ ] Demonstrate optimistic or pessimistic locking under concurrent requests.
- [ ] Allocate, pick, pack and ship a synthetic order without overselling.
- [ ] Send a putaway command to a simulator and process asynchronous confirmation.
- [ ] Reprocess duplicate/out-of-order delivery without duplicate stock effect.
- [ ] Recover from a simulated timeout or failed publish using a written runbook.
- [ ] Pass representative REST, service, repository and end-to-end integration tests.
- [ ] Present ERD, sequence diagram, architecture boundaries, key ADRs and remaining assumptions.

## Suggested API progression (illustrative, not Catalyst APIs)

| Module | Example endpoints |
|---|---|
| Catalog | `POST /api/products`, `GET /api/products/{id}` |
| Warehouse | `POST /api/warehouses`, `POST /api/locations` |
| Handling units | `POST /api/lpns`, `GET /api/lpns/{id}` |
| Inventory | `GET /api/inventory`, `GET /api/inventory/movements` |
| Inbound | `POST /api/asns`, `POST /api/receipts` |
| Putaway | `POST /api/putaway-tasks`, `POST /api/putaway-tasks/{id}/complete` |
| Outbound | `POST /api/orders`, `POST /api/allocations`, `POST /api/shipments/{id}/confirm` |
| Automation simulator | `POST /simulator/putaway-commands`, `POST /api/automation/events` |

## Initial setup checklist

- [ ] Create standalone Git repository `wms-integration-lab`.
- [ ] Generate Java 21 + compatible Spring Boot starter dependencies (Web, Validation, Data JPA, Actuator, PostgreSQL, Testing).
- [ ] Add Docker Compose PostgreSQL with a persistent development volume.
- [ ] Set development-only Hibernate SQL logging; protect credentials through environment variables.
- [ ] Use `ddl-auto=update` only in a disposable local schema for Day 1–2 experimentation; move to Liquibase and `validate` before accumulating lasting data.
- [ ] Decide explicit timezone, identifiers, quantity precision and inventory tracking policies.
- [ ] Keep a `docs/assumptions.md` file: every undocumented Catalyst/Symbotic behavior remains an assumption.

## What to study after Day 30

Go deeper on Catalyst-specific configuration (if authorized), real interface specifications, location/lot rules, operational metrics, performance testing, security, replenishment, cycle counts, wave optimization, cartonization, carrier integration and disaster recovery. These are deliberately not promises of full coverage within 30 sessions.
