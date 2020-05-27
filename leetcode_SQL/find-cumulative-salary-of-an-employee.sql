-- Leetcode 579. Find Cumulative Salary of an Employee

-- drop table employees2

-- Create table employees2(Id int,[Month] int,salary int);

-- insert into employees2 values(1,1,20);
-- insert into employees2 values(2,1,20);
-- insert into employees2 values(1,2,30);
-- insert into employees2 values(2,2,30);
-- insert into employees2 values(3,2,40);
-- insert into employees2 values(1,3,40);
-- insert into employees2 values(3,3,60);
-- insert into employees2 values(1,4,60);
-- insert into employees2 values(3,4,70);

-- The Employee table holds the salary information in a year.

-- Write a SQL to get the cumulative sum of an employees salary over a period of 3 months but exclude the most recent month.

-- The result should be displayed by 'Id' ascending, and then by 'Month' descending.

-- Example
-- Input

-- | Id | Month | Salary |
-- |----|-------|--------|
-- | 1  | 1     | 20     |
-- | 2  | 1     | 20     |
-- | 1  | 2     | 30     |
-- | 2  | 2     | 30     |
-- | 3  | 2     | 40     |
-- | 1  | 3     | 40     |
-- | 3  | 3     | 60     |
-- | 1  | 4     | 60     |
-- | 3  | 4     | 70     |
-- Output

-- | Id | Month | Salary |
-- |----|-------|--------|
-- | 1  | 3     | 90     |
-- | 1  | 2     | 50     |
-- | 1  | 1     | 20     |
-- | 2  | 1     | 20     |
-- | 3  | 3     | 100    |
-- | 3  | 2     | 40     |
--  

-- Explanation

-- Employee '1' has 3 salary records for the following 3 months except the most recent month '4': salary 40 for month '3', 30 for month '2' and 20 for month '1'
-- So the cumulative sum of salary of this employee over 3 months is 90(40+30+20), 50(30+20) and 20 respectively.

-- | Id | Month | Salary |
-- |----|-------|--------|
-- | 1  | 3     | 90     |
-- | 1  | 2     | 50     |
-- | 1  | 1     | 20     |
-- Employee '2' only has one salary record (month '1') except its most recent month '2'.

-- | Id | Month | Salary |
-- |----|-------|--------|
-- | 2  | 1     | 20     |
--  

-- Employ '3' has two salary records except its most recent pay month '4': month '3' with 60 and month '2' with 40. So the cumulative salary is as following.

-- | Id | Month | Salary |
-- |----|-------|--------|
-- | 3  | 3     | 100    |
-- | 3  | 2     | 40     |
 

-- BUILD SQLITE TABLE 
DROP TABLE employee;

CREATE TABLE employee(
id INTEGER,
month INTEGER, 
salary INTEGER
);

INSERT INTO employee(id, month, salary)
VALUES
(1,1,20),
(2,1,20),
(1,2,30),
(2,2,30),
(3,2,40),
(1,3,40),
(3,3,60),
(1,4,60),
(3,4,70)
;

-- V0
-- IDEA : CTE
-- SQLITE
WITH e3 AS
  (SELECT id,
          MAX(MONTH) AS last_month
   FROM employee
   GROUP BY id)
SELECT e1.id,
       e2.month,
       sum(e1.salary)
FROM employee e1
INNER JOIN employee e2 ON e1.id = e2.id
AND e1.month <= e2.month
INNER JOIN e3 ON e2.id = e3.id
AND e2.month < e3.last_month
GROUP BY 1,
         2
ORDER BY 1,
         2 DESC;

-- V0'
-- IDEA : WINDOW FUNC
-- MYSQL
select distinct e1.id, e1.month, 
sum(e1.salary) over(partition by e1.id order by e1.month)
from employee e1,
(select id, max(month) month from employee group by id) e2
where e1.id = e2.id and e1.month < e2.month
order by e1.ID;

-- V1
-- https://leetcode.com/articles/find-cumulative-salary-of-an-employee/
SELECT
    E1.id,
    E1.month,
    (IFNULL(E1.salary, 0) + IFNULL(E2.salary, 0) + IFNULL(E3.salary, 0)) AS Salary
FROM
    (SELECT
        id, MAX(month) AS month
    FROM
        Employee
    GROUP BY id
    HAVING COUNT(*) > 1) AS maxmonth
        LEFT JOIN
    Employee E1 ON (maxmonth.id = E1.id
        AND maxmonth.month > E1.month)
        LEFT JOIN
    Employee E2 ON (E2.id = E1.id
        AND E2.month = E1.month - 1)
        LEFT JOIN
    Employee E3 ON (E3.id = E1.id
        AND E3.month = E1.month - 2)
ORDER BY id ASC , month DESC;

-- V1'
-- https://blog.csdn.net/betty1121/article/details/80829515
select distinct e1.id, e1.month, 
sum(e1.salary) over(partition by e1.id order by e1.month)
from employee e1,
(select id, max(month) month from employee group by id) e2
where e1.id = e2.id and e1.month < e2.month
order by e1.ID

-- V1''
-- https://blog.csdn.net/chelseajcole/article/details/81532054
with cumulativeSalary as(
select e1.Id,e1.Month,
isnull(e1.salary,0)+ISNULL(e2.salary,0)+ISNULL(e3.salary,0) as CumulativeSalary
from employees2 e1 
left join employees2 e2 on e1.Id=e2.Id and e2.Month=e1.Month-1
left join employees2 e3 on e3.Id=e2.Id and e3.Month=e2.Month-1
),MostRecentMonth as
(
select id,max(Month) as MaxMonth from employees2 group by Id having(count(*)>1)
)
select c.Id,c.Month,c.CumulativeSalary from cumulativeSalary c join MostRecentMonth m on c.Id=m.Id and m.MaxMonth>c.Month
order by Id asc, Month desc

-- V1'''
-- https://blog.csdn.net/chelseajcole/article/details/81532054
-- IDEA : WINDOW FUNC
SELECT id, month, Salary
FROM
(
SELECT  id, 
        month, 
		-- Every 3 months. ROWS 2 PRECEDING indicates the number of rows or values to precede the current row (1 + 2)
        SUM(salary) OVER(PARTITION BY id  ORDER BY month ROWS 2 PRECEDING) as Salary, 
        DENSE_RANK() OVER(PARTITION BY id ORDER by month DESC) month_no
FROM Employee
)  src
--  exclude the most recent month
where month_no > 1
ORDER BY id , month desc

-- V2
