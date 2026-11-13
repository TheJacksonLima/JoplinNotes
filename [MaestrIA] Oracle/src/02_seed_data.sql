-- ============================================================
-- Oracle SQL + PL/SQL Lab
-- File: 02_seed_data.sql
-- User: ORACLE_LAB
-- Purpose: Small, deterministic dataset for SQL interview practice
--
-- Assumes 01_schema.sql has already been executed.
-- Designed for:
--   - JOINs
--   - GROUP BY / HAVING
--   - subqueries / EXISTS
--   - analytic functions
--   - hierarchy queries
--   - MERGE
--   - duplicate handling
--   - GTT / staging exercises
--   - PL/SQL practice
-- ============================================================

-- ============================================================
-- CLEAN CURRENT DATA
-- ============================================================

DELETE FROM bank_transaction;
DELETE FROM transaction_stage;
DELETE FROM order_item;
DELETE FROM orders;
DELETE FROM account;
DELETE FROM customer;
DELETE FROM employee;
DELETE FROM department;

COMMIT;

-- ============================================================
-- 1. DEPARTMENTS
-- ============================================================

INSERT INTO department (department_id, name, location)
VALUES (1, 'Engineering', 'Curitiba');

INSERT INTO department (department_id, name, location)
VALUES (2, 'Finance', 'Sao Paulo');

INSERT INTO department (department_id, name, location)
VALUES (3, 'Operations', 'Curitiba');

INSERT INTO department (department_id, name, location)
VALUES (4, 'Sales', 'Rio de Janeiro');

INSERT INTO department (department_id, name, location)
VALUES (5, 'Human Resources', 'Curitiba');

INSERT INTO department (department_id, name, location)
VALUES (6, 'Legal', 'Sao Paulo');

-- Department 6 intentionally has no employees.

-- ============================================================
-- 2. EMPLOYEES
-- ============================================================

-- Top-level managers
INSERT INTO employee (
    employee_id, department_id, manager_id,
    name, email, job_title, salary, hire_date, status
)
VALUES (
    1, 1, NULL,
    'Alice Johnson', 'alice.johnson@lab.com',
    'Engineering Director', 18000,
    DATE '2018-03-12', 'ACTIVE'
);

INSERT INTO employee (
    employee_id, department_id, manager_id,
    name, email, job_title, salary, hire_date, status
)
VALUES (
    2, 2, NULL,
    'Bruno Silva', 'bruno.silva@lab.com',
    'Finance Director', 17500,
    DATE '2017-07-01', 'ACTIVE'
);

INSERT INTO employee (
    employee_id, department_id, manager_id,
    name, email, job_title, salary, hire_date, status
)
VALUES (
    3, 3, NULL,
    'Carla Mendes', 'carla.mendes@lab.com',
    'Operations Director', 16000,
    DATE '2019-01-15', 'ACTIVE'
);

INSERT INTO employee (
    employee_id, department_id, manager_id,
    name, email, job_title, salary, hire_date, status
)
VALUES (
    4, 4, NULL,
    'Diego Costa', 'diego.costa@lab.com',
    'Sales Director', 16500,
    DATE '2018-11-21', 'ACTIVE'
);

INSERT INTO employee (
    employee_id, department_id, manager_id,
    name, email, job_title, salary, hire_date, status
)
VALUES (
    5, 5, NULL,
    'Elena Rocha', 'elena.rocha@lab.com',
    'HR Manager', 12500,
    DATE '2020-05-18', 'ACTIVE'
);

-- Engineering
INSERT INTO employee VALUES (
    6, 1, 1,
    'Felipe Souza', 'felipe.souza@lab.com',
    'Senior Software Engineer', 14500,
    DATE '2020-02-10', 'ACTIVE'
);

INSERT INTO employee VALUES (
    7, 1, 1,
    'Gabriela Lima', 'gabriela.lima@lab.com',
    'Senior Software Engineer', 14500,
    DATE '2021-06-22', 'ACTIVE'
);

INSERT INTO employee VALUES (
    8, 1, 6,
    'Henrique Alves', 'henrique.alves@lab.com',
    'Software Engineer', 10500,
    DATE '2022-08-01', 'ACTIVE'
);

INSERT INTO employee VALUES (
    9, 1, 6,
    'Isabela Martins', 'isabela.martins@lab.com',
    'Software Engineer', 9800,
    DATE '2023-01-09', 'ON_LEAVE'
);

INSERT INTO employee VALUES (
    10, 1, 7,
    'Joao Ribeiro', 'joao.ribeiro@lab.com',
    'Junior Software Engineer', 6500,
    DATE '2024-04-15', 'ACTIVE'
);

-- Finance
INSERT INTO employee VALUES (
    11, 2, 2,
    'Karen Oliveira', 'karen.oliveira@lab.com',
    'Senior Financial Analyst', 13000,
    DATE '2020-09-14', 'ACTIVE'
);

INSERT INTO employee VALUES (
    12, 2, 2,
    'Lucas Ferreira', 'lucas.ferreira@lab.com',
    'Financial Analyst', 9000,
    DATE '2022-02-07', 'ACTIVE'
);

INSERT INTO employee VALUES (
    13, 2, 11,
    'Mariana Santos', 'mariana.santos@lab.com',
    'Financial Analyst', 9000,
    DATE '2023-05-11', 'ACTIVE'
);

-- Operations
INSERT INTO employee VALUES (
    14, 3, 3,
    'Nicolas Pereira', 'nicolas.pereira@lab.com',
    'Operations Supervisor', 11000,
    DATE '2021-03-01', 'ACTIVE'
);

INSERT INTO employee VALUES (
    15, 3, 14,
    'Olivia Gomes', 'olivia.gomes@lab.com',
    'Operations Analyst', 7200,
    DATE '2023-10-02', 'ACTIVE'
);

INSERT INTO employee VALUES (
    16, 3, 14,
    'Paulo Nunes', 'paulo.nunes@lab.com',
    'Operations Analyst', 7200,
    DATE '2024-01-08', 'INACTIVE'
);

-- Sales
INSERT INTO employee VALUES (
    17, 4, 4,
    'Renata Barbosa', 'renata.barbosa@lab.com',
    'Account Executive', 12000,
    DATE '2020-12-03', 'ACTIVE'
);

INSERT INTO employee VALUES (
    18, 4, 4,
    'Samuel Freitas', 'samuel.freitas@lab.com',
    'Account Executive', 12000,
    DATE '2021-12-03', 'ACTIVE'
);

INSERT INTO employee VALUES (
    19, 4, 17,
    'Tatiana Melo', 'tatiana.melo@lab.com',
    'Sales Representative', 7000,
    DATE '2023-02-13', 'ACTIVE'
);

-- HR
INSERT INTO employee VALUES (
    20, 5, 5,
    'Victor Araujo', 'victor.araujo@lab.com',
    'HR Analyst', 8000,
    DATE '2022-07-25', 'ACTIVE'
);

-- ============================================================
-- 3. CUSTOMERS
-- ============================================================

INSERT INTO customer VALUES (
    1, 'Acme Industries', 'contact@acme.com',
    'Curitiba', 'PR', 'ACTIVE',
    TIMESTAMP '2022-01-10 09:00:00'
);

INSERT INTO customer VALUES (
    2, 'Blue Ocean Ltd', 'finance@blueocean.com',
    'Sao Paulo', 'SP', 'ACTIVE',
    TIMESTAMP '2022-03-15 10:30:00'
);

INSERT INTO customer VALUES (
    3, 'Cedar Retail', 'hello@cedar.com',
    'Rio de Janeiro', 'RJ', 'ACTIVE',
    TIMESTAMP '2022-05-20 14:10:00'
);

INSERT INTO customer VALUES (
    4, 'Delta Logistics', 'ops@delta.com',
    'Curitiba', 'PR', 'BLOCKED',
    TIMESTAMP '2022-08-02 08:45:00'
);

INSERT INTO customer VALUES (
    5, 'Evergreen Foods', 'contact@evergreen.com',
    'Florianopolis', 'SC', 'ACTIVE',
    TIMESTAMP '2023-01-11 12:00:00'
);

INSERT INTO customer VALUES (
    6, 'Futura Tech', 'admin@futuratech.com',
    'Curitiba', 'PR', 'ACTIVE',
    TIMESTAMP '2023-03-04 16:15:00'
);

INSERT INTO customer VALUES (
    7, 'Global Health', 'billing@globalhealth.com',
    'Sao Paulo', 'SP', 'ACTIVE',
    TIMESTAMP '2023-07-19 11:50:00'
);

INSERT INTO customer VALUES (
    8, 'Horizon Media', 'accounts@horizon.com',
    'Porto Alegre', 'RS', 'INACTIVE',
    TIMESTAMP '2023-09-30 09:20:00'
);

INSERT INTO customer VALUES (
    9, 'Iron Works', 'sales@ironworks.com',
    'Curitiba', 'PR', 'ACTIVE',
    TIMESTAMP '2024-02-05 13:40:00'
);

INSERT INTO customer VALUES (
    10, 'Jupiter Consulting', 'office@jupiter.com',
    'Sao Paulo', 'SP', 'ACTIVE',
    TIMESTAMP '2024-05-22 15:00:00'
);

-- Customer 10 intentionally has no account/orders initially.

-- ============================================================
-- 4. ACCOUNTS
-- ============================================================

INSERT INTO account VALUES (
    1, 1, 'ACC-1001', 'CHECKING', 25000, 'ACTIVE',
    TIMESTAMP '2022-01-10 10:00:00'
);

INSERT INTO account VALUES (
    2, 1, 'ACC-1002', 'SAVINGS', 80000, 'ACTIVE',
    TIMESTAMP '2022-01-10 10:10:00'
);

INSERT INTO account VALUES (
    3, 2, 'ACC-2001', 'CHECKING', 15000, 'ACTIVE',
    TIMESTAMP '2022-03-15 11:00:00'
);

INSERT INTO account VALUES (
    4, 3, 'ACC-3001', 'CHECKING', 7000, 'ACTIVE',
    TIMESTAMP '2022-05-20 15:00:00'
);

INSERT INTO account VALUES (
    5, 4, 'ACC-4001', 'CHECKING', 1200, 'BLOCKED',
    TIMESTAMP '2022-08-02 09:00:00'
);

INSERT INTO account VALUES (
    6, 5, 'ACC-5001', 'SAVINGS', 34000, 'ACTIVE',
    TIMESTAMP '2023-01-11 12:30:00'
);

INSERT INTO account VALUES (
    7, 6, 'ACC-6001', 'CHECKING', 54000, 'ACTIVE',
    TIMESTAMP '2023-03-04 17:00:00'
);

INSERT INTO account VALUES (
    8, 6, 'ACC-6002', 'INVESTMENT', 125000, 'ACTIVE',
    TIMESTAMP '2023-03-04 17:05:00'
);

INSERT INTO account VALUES (
    9, 7, 'ACC-7001', 'CHECKING', 22000, 'ACTIVE',
    TIMESTAMP '2023-07-19 12:30:00'
);

INSERT INTO account VALUES (
    10, 8, 'ACC-8001', 'CHECKING', 0, 'CLOSED',
    TIMESTAMP '2023-09-30 10:00:00'
);

INSERT INTO account VALUES (
    11, 9, 'ACC-9001', 'CHECKING', 9100, 'ACTIVE',
    TIMESTAMP '2024-02-05 14:00:00'
);

INSERT INTO account VALUES (
    12, 9, 'ACC-9002', 'SAVINGS', 15000, 'ACTIVE',
    TIMESTAMP '2024-02-05 14:10:00'
);

-- ============================================================
-- 5. ORDERS
-- ============================================================

INSERT INTO orders VALUES (
    1, 1, DATE '2025-01-10', 'COMPLETED', 2500
);

INSERT INTO orders VALUES (
    2, 1, DATE '2025-02-15', 'COMPLETED', 1800
);

INSERT INTO orders VALUES (
    3, 2, DATE '2025-02-20', 'SHIPPED', 4200
);

INSERT INTO orders VALUES (
    4, 3, DATE '2025-03-01', 'CANCELLED', 600
);

INSERT INTO orders VALUES (
    5, 3, DATE '2025-03-12', 'COMPLETED', 3100
);

INSERT INTO orders VALUES (
    6, 5, DATE '2025-04-05', 'COMPLETED', 5500
);

INSERT INTO orders VALUES (
    7, 6, DATE '2025-04-18', 'PROCESSING', 1250
);

INSERT INTO orders VALUES (
    8, 6, DATE '2025-05-02', 'COMPLETED', 8700
);

INSERT INTO orders VALUES (
    9, 7, DATE '2025-05-17', 'PENDING', 2300
);

INSERT INTO orders VALUES (
    10, 9, DATE '2025-06-03', 'COMPLETED', 4600
);

INSERT INTO orders VALUES (
    11, 9, DATE '2025-06-21', 'COMPLETED', 1500
);

INSERT INTO orders VALUES (
    12, 2, DATE '2025-07-04', 'COMPLETED', 3200
);

-- Customers 4, 8, and 10 intentionally have no orders.

-- ============================================================
-- 6. ORDER ITEMS
-- ============================================================

INSERT INTO order_item VALUES (1, 1, 'Laptop', 2, 1000);
INSERT INTO order_item VALUES (2, 1, 'Mouse', 5, 100);

INSERT INTO order_item VALUES (3, 2, 'Monitor', 2, 700);
INSERT INTO order_item VALUES (4, 2, 'Keyboard', 4, 100);

INSERT INTO order_item VALUES (5, 3, 'Server', 1, 3500);
INSERT INTO order_item VALUES (6, 3, 'SSD', 7, 100);

INSERT INTO order_item VALUES (7, 4, 'Office Chair', 2, 300);

INSERT INTO order_item VALUES (8, 5, 'Tablet', 5, 500);
INSERT INTO order_item VALUES (9, 5, 'Case', 6, 100);

INSERT INTO order_item VALUES (10, 6, 'Printer', 5, 1000);
INSERT INTO order_item VALUES (11, 6, 'Cable', 10, 50);

INSERT INTO order_item VALUES (12, 7, 'Router', 5, 200);
INSERT INTO order_item VALUES (13, 7, 'Cable', 5, 50);

INSERT INTO order_item VALUES (14, 8, 'Workstation', 3, 2500);
INSERT INTO order_item VALUES (15, 8, 'Monitor', 2, 600);

INSERT INTO order_item VALUES (16, 9, 'Phone', 4, 500);
INSERT INTO order_item VALUES (17, 9, 'Headset', 3, 100);

INSERT INTO order_item VALUES (18, 10, 'Industrial Sensor', 20, 200);
INSERT INTO order_item VALUES (19, 10, 'Cable', 20, 30);

INSERT INTO order_item VALUES (20, 11, 'Tool Kit', 3, 500);

INSERT INTO order_item VALUES (21, 12, 'Laptop', 3, 1000);
INSERT INTO order_item VALUES (22, 12, 'Mouse', 2, 100);

-- ============================================================
-- 7. BANK TRANSACTIONS
--
-- Multiple transactions per account and repeated values are
-- intentional for analytic-function practice.
-- ============================================================

INSERT INTO bank_transaction VALUES (
    1, 1, 'CREDIT', 5000,
    TIMESTAMP '2025-01-05 09:00:00',
    'Customer payment', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    2, 1, 'DEBIT', 1200,
    TIMESTAMP '2025-01-08 10:00:00',
    'Supplier payment', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    3, 1, 'DEBIT', 450,
    TIMESTAMP '2025-01-15 15:30:00',
    'Office expense', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    4, 1, 'CREDIT', 3200,
    TIMESTAMP '2025-02-01 11:00:00',
    'Customer payment', 'CRM'
);

INSERT INTO bank_transaction VALUES (
    5, 2, 'CREDIT', 10000,
    TIMESTAMP '2025-01-10 08:00:00',
    'Investment return', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    6, 2, 'DEBIT', 2500,
    TIMESTAMP '2025-02-10 14:00:00',
    'Transfer out', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    7, 3, 'CREDIT', 7000,
    TIMESTAMP '2025-01-20 09:30:00',
    'Invoice payment', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    8, 3, 'DEBIT', 7000,
    TIMESTAMP '2025-01-21 09:30:00',
    'Supplier payment', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    9, 4, 'CREDIT', 1500,
    TIMESTAMP '2025-02-03 10:00:00',
    'Retail payment', 'POS'
);

INSERT INTO bank_transaction VALUES (
    10, 4, 'DEBIT', 300,
    TIMESTAMP '2025-02-05 13:10:00',
    'Refund', 'POS'
);

INSERT INTO bank_transaction VALUES (
    11, 5, 'DEBIT', 500,
    TIMESTAMP '2025-02-07 16:00:00',
    'Blocked account test', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    12, 6, 'CREDIT', 6000,
    TIMESTAMP '2025-03-01 09:00:00',
    'Wholesale payment', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    13, 6, 'DEBIT', 1100,
    TIMESTAMP '2025-03-03 09:00:00',
    'Food supplier', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    14, 7, 'CREDIT', 15000,
    TIMESTAMP '2025-03-05 10:00:00',
    'Software contract', 'CRM'
);

INSERT INTO bank_transaction VALUES (
    15, 7, 'DEBIT', 2000,
    TIMESTAMP '2025-03-07 15:00:00',
    'Cloud invoice', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    16, 8, 'CREDIT', 25000,
    TIMESTAMP '2025-03-10 10:30:00',
    'Investment contribution', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    17, 8, 'CREDIT', 25000,
    TIMESTAMP '2025-04-10 10:30:00',
    'Investment contribution', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    18, 8, 'CREDIT', 25000,
    TIMESTAMP '2025-05-10 10:30:00',
    'Investment contribution', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    19, 9, 'CREDIT', 8000,
    TIMESTAMP '2025-04-01 12:00:00',
    'Health contract', 'CRM'
);

INSERT INTO bank_transaction VALUES (
    20, 9, 'DEBIT', 1900,
    TIMESTAMP '2025-04-03 12:00:00',
    'Medical equipment', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    21, 11, 'CREDIT', 4000,
    TIMESTAMP '2025-05-01 09:00:00',
    'Industrial sale', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    22, 11, 'DEBIT', 600,
    TIMESTAMP '2025-05-02 09:00:00',
    'Raw material', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    23, 12, 'CREDIT', 3000,
    TIMESTAMP '2025-05-03 09:00:00',
    'Savings contribution', 'MOBILE'
);

INSERT INTO bank_transaction VALUES (
    24, 12, 'CREDIT', 3000,
    TIMESTAMP '2025-06-03 09:00:00',
    'Savings contribution', 'MOBILE'
);

INSERT INTO bank_transaction VALUES (
    25, 12, 'CREDIT', 3000,
    TIMESTAMP '2025-07-03 09:00:00',
    'Savings contribution', 'MOBILE'
);

INSERT INTO bank_transaction VALUES (
    26, 1, 'REFUND', 200,
    TIMESTAMP '2025-02-10 10:00:00',
    'Supplier refund', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    27, 3, 'PAYMENT', 900,
    TIMESTAMP '2025-03-15 09:00:00',
    'Utility payment', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    28, 7, 'TRANSFER', 5000,
    TIMESTAMP '2025-04-15 15:30:00',
    'Internal transfer', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    29, 8, 'TRANSFER', 5000,
    TIMESTAMP '2025-04-15 15:31:00',
    'Internal transfer', 'ERP'
);

INSERT INTO bank_transaction VALUES (
    30, 9, 'REFUND', 450,
    TIMESTAMP '2025-04-20 12:10:00',
    'Vendor refund', 'ERP'
);

-- ============================================================
-- 8. TRANSACTION STAGE
--
-- Intentionally contains:
--   - duplicates
--   - missing account
--   - invalid transaction type
--   - negative amount
--
-- This table has no constraints on purpose.
-- ============================================================

INSERT INTO transaction_stage VALUES (
    1001, 1, 'CREDIT', 1200,
    TIMESTAMP '2025-08-01 09:00:00',
    'LEGACY',
    TIMESTAMP '2025-08-02 01:00:00'
);

INSERT INTO transaction_stage VALUES (
    1002, 2, 'DEBIT', 300,
    TIMESTAMP '2025-08-01 09:05:00',
    'LEGACY',
    TIMESTAMP '2025-08-02 01:00:00'
);

INSERT INTO transaction_stage VALUES (
    1003, 7, 'CREDIT', 8000,
    TIMESTAMP '2025-08-01 09:10:00',
    'CRM',
    TIMESTAMP '2025-08-02 01:00:00'
);

-- Exact duplicate
INSERT INTO transaction_stage VALUES (
    1003, 7, 'CREDIT', 8000,
    TIMESTAMP '2025-08-01 09:10:00',
    'CRM',
    TIMESTAMP '2025-08-02 01:00:00'
);

INSERT INTO transaction_stage VALUES (
    1004, 9999, 'CREDIT', 1000,
    TIMESTAMP '2025-08-01 09:15:00',
    'LEGACY',
    TIMESTAMP '2025-08-02 01:00:00'
);

INSERT INTO transaction_stage VALUES (
    1005, 3, 'UNKNOWN', 500,
    TIMESTAMP '2025-08-01 09:20:00',
    'LEGACY',
    TIMESTAMP '2025-08-02 01:00:00'
);

INSERT INTO transaction_stage VALUES (
    1006, 4, 'DEBIT', -250,
    TIMESTAMP '2025-08-01 09:25:00',
    'POS',
    TIMESTAMP '2025-08-02 01:00:00'
);

INSERT INTO transaction_stage VALUES (
    1007, NULL, 'CREDIT', 900,
    TIMESTAMP '2025-08-01 09:30:00',
    'LEGACY',
    TIMESTAMP '2025-08-02 01:00:00'
);

-- ============================================================
-- COMMIT
-- ============================================================

COMMIT;

-- ============================================================
-- VERIFY ROW COUNTS
-- ============================================================

SELECT 'DEPARTMENT' AS table_name, COUNT(*) AS row_count
FROM department
UNION ALL
SELECT 'EMPLOYEE', COUNT(*)
FROM employee
UNION ALL
SELECT 'CUSTOMER', COUNT(*)
FROM customer
UNION ALL
SELECT 'ACCOUNT', COUNT(*)
FROM account
UNION ALL
SELECT 'ORDERS', COUNT(*)
FROM orders
UNION ALL
SELECT 'ORDER_ITEM', COUNT(*)
FROM order_item
UNION ALL
SELECT 'BANK_TRANSACTION', COUNT(*)
FROM bank_transaction
UNION ALL
SELECT 'TRANSACTION_STAGE', COUNT(*)
FROM transaction_stage
ORDER BY table_name;

-- ============================================================
-- EXPECTED COUNTS
-- ============================================================
--
-- ACCOUNT              12
-- BANK_TRANSACTION     30
-- CUSTOMER             10
-- DEPARTMENT            6
-- EMPLOYEE             20
-- ORDERS               12
-- ORDER_ITEM           22
-- TRANSACTION_STAGE     8
--
-- ============================================================
-- QUICK DATA-QUALITY / EXERCISE CHECKS
-- ============================================================

-- Departments without employees
SELECT d.department_id, d.name
FROM department d
LEFT JOIN employee e
    ON e.department_id = d.department_id
WHERE e.employee_id IS NULL;

-- Customers without orders
SELECT c.customer_id, c.name
FROM customer c
WHERE NOT EXISTS (
    SELECT 1
    FROM orders o
    WHERE o.customer_id = c.customer_id
);

-- Duplicate staging records
SELECT
    source_transaction_id,
    account_id,
    transaction_type,
    amount,
    transaction_date,
    COUNT(*) AS duplicate_count
FROM transaction_stage
GROUP BY
    source_transaction_id,
    account_id,
    transaction_type,
    amount,
    transaction_date
HAVING COUNT(*) > 1;

-- Salary ties for ranking exercises
SELECT
    department_id,
    salary,
    COUNT(*) AS employee_count
FROM employee
GROUP BY department_id, salary
HAVING COUNT(*) > 1
ORDER BY department_id, salary DESC;

-- ============================================================
-- END
-- ============================================================

