select e.DEPARTMENT_ID,e.SALARY
from EMPLOYEE e;
--ORA-00979: not a GROUP BY expression
SELECT department_id, job_title, AVG(salary)
FROM employee
GROUP BY department_id;

SELECT department_id, job_title, AVG(salary)
FROM employee
GROUP BY department_id;

--2. WHERE vs HAVING
--WHERE eliminates rows; HAVING eliminates groups.
SELECT
    department_id,
    AVG(salary)
FROM employee
WHERE status = 'ACTIVE'
GROUP BY department_id;

SELECT
    department_id,
    AVG(salary) AS avg_salary
FROM employee
GROUP BY department_id
HAVING AVG(salary) > 10000;

--Find employees earning more than the average salary of their own department

SELECT E1.NAME,E1.SALARY,E1.DEPARTMENT_ID
FROM EMPLOYEE E1
WHERE
E1.SALARY >
(
    SELECT AVG(E2.SALARY)
    FROM EMPLOYEE E2
    WHERE E2.DEPARTMENT_ID = E1.DEPARTMENT_ID

 )

--Exercise 1 — Department salary summary

select e.DEPARTMENT_ID,
       COUNT(*) as employee_count,
       SUM(e.SALARY) as total_salary,
       AVG(e.SALARY) as avg_salary,
       MIN(e.SALARY) as min_salary,
       MAX(e.SALARY) as max_salary
from EMPLOYEE e
GROUP BY e.DEPARTMENT_ID;


--Exercise 2 — WHERE vs HAVING
SELECT avg(SALARY) FROM EMPLOYEE
GROUP BY DEPARTMENT_ID;

SELECT D.NAME,COUNT(E.EMPLOYEE_ID),AVG(E.SALARY)
FROM EMPLOYEE E,
     DEPARTMENT D
WHERE E.STATUS = 'ACTIVE'
GROUP BY D.NAME
HAVING COUNT(*) > 2

--Exercise 3 — Company average
SELECT E1.NAME,E1.SALARY
FROM EMPLOYEE E1
WHERE
E1.SALARY >
(
    SELECT AVG(E2.SALARY)
    FROM EMPLOYEE E2


 );

--Exercise 4 — Department average

SELECT E1.NAME,E1.SALARY,E1.DEPARTMENT_ID
FROM EMPLOYEE E1
WHERE
E1.SALARY >
(
    SELECT AVG(E2.SALARY)
    FROM EMPLOYEE E2
    WHERE E2.DEPARTMENT_ID = E1.DEPARTMENT_ID

 );

--Exercise 5 — Department threshold

SELECT D.NAME,SUM(E.SALARY)
FROM EMPLOYEE E
JOIN DEPARTMENT D
    on D.DEPARTMENT_ID = e.DEPARTMENT_ID
GROUP BY D.NAME
HAVING SUM(E.SALARY) > 30000

--Exercise 6 — Break it intentionally
SELECT
    department_id,
    name,
    AVG(salary)
FROM employee
GROUP BY department_id;