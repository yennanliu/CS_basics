/*

Table: Employees

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| employee_id   | int     |
| employee_name | varchar |
| manager_id    | int     |
+---------------+---------+
employee_id is the primary key for this table.
Each row of this table indicates that the employee with ID employee_id and name employee_name reports his work to his/her direct manager with manager_id
The head of the company is the employee with employee_id = 1.
Write an SQL query to find employee_id of all employees that directly or indirectly report their work to the head of the company.

The indirect relation between managers will not exceed 3 managers as the company is small.

Return result table in any order without duplicates.

The query result format is in the following example:

Employees table:
+-------------+---------------+------------+
| employee_id | employee_name | manager_id |
+-------------+---------------+------------+
| 1           | Boss          | 1          |
| 3           | Alice         | 3          |
| 2           | Bob           | 1          |
| 4           | Daniel        | 2          |
| 7           | Luis          | 4          |
| 8           | Jhon          | 3          |
| 9           | Angela        | 8          |
| 77          | Robert        | 1          |
+-------------+---------------+------------+

Result table:
+-------------+
| employee_id |
+-------------+
| 2           |
| 77          |
| 4           |
| 7           |
+-------------+

The head of the company is the employee with employee_id 1.
The employees with employee_id 2 and 77 report their work directly to the head of the company.
The employee with employee_id 4 report his work indirectly to the head of the company 4 --> 2 --> 1. 
The employee with employee_id 7 report his work indirectly to the head of the company 7 --> 4 --> 2 --> 1.
The employees with employee_id 3, 8 and 9 don't report their work to head

*/

# V0
SELECT e1.employee_id 
FROM   employees e1 
       LEFT JOIN employees e2 
              ON e1.manager_id = e2.employee_id 
       LEFT JOIN employees e3 
              ON e2.manager_id = e3.employee_id 
WHERE  e3.manager_id = 1 
       AND e1.employee_id != 1 

# V0'
WITH 
  _2nd AS
  (SELECT employee_id
   FROM Employees
   WHERE manager_id = 1 ),
  _3rd AS
  (SELECT employee_id
   FROM Employees
   WHERE manager_id IN
       (SELECT employee_id
        FROM _2nd) )
SELECT DISTINCT employee_id
FROM
  (SELECT employee_id
   FROM Employees
   WHERE manager_id = 1
   UNION ALL SELECT employee_id
   FROM _2nd
   UNION ALL SELECT employee_id
   FROM _3rd) sub;

# V1
# https://code.dennyzhang.com/all-people-report-to-the-given-manager
## t3: directly report to employee_id 1
## t2: directly report to t3
## t1: directly report to t2
select t1.employee_id
from Employees as t1 inner join Employees as t2
on t1.manager_id = t2.employee_id
join Employees as t3
on t2.manager_id = t3.employee_id
where t3.manager_id = 1 and t1.employee_id != 1 

# V1'
# https://code.dennyzhang.com/all-people-report-to-the-given-manager
select distinct employee_id
from (
select employee_id
from Employees
where manager_id in
(select employee_id
from Employees
where manager_id in
    (select employee_id
    from Employees
    where manager_id = 1))
union
select employee_id
from Employees
where manager_id in
    (select employee_id
    from Employees
    where manager_id = 1)
union
select employee_id
    from Employees
    where manager_id = 1) as t
where employee_id != 1

# V2
# Time:  O(n)
# Space: O(n)
SELECT e1.employee_id 
FROM   employees e1 
       LEFT JOIN employees e2 
              ON e1.manager_id = e2.employee_id 
       LEFT JOIN employees e3 
              ON e2.manager_id = e3.employee_id 
WHERE  e3.manager_id = 1 
       AND e1.employee_id != 1 