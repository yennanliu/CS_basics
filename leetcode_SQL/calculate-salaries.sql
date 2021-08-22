/*

Table Salaries:

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| company_id    | int     |
| employee_id   | int     |
| employee_name | varchar |
| salary        | int     |
+---------------+---------+
(company_id, employee_id) is the primary key for this table.
This table contains the company id, the id, the name and the salary for an employee.
Write an SQL query to find the salaries of the employees after applying taxes.

The tax rate is calculated for each company based on the following criteria:

0% If the max salary of any employee in the company is less than 1000$.
24% If the max salary of any employee in the company is in the range [1000, 10000] inclusive.
49% If the max salary of any employee in the company is greater than 10000$.
Return the result table in any order. Round the salary to the nearest integer.

The query result format is in the following example:

Salaries table:
+------------+-------------+---------------+--------+
| company_id | employee_id | employee_name | salary |
+------------+-------------+---------------+--------+
| 1          | 1           | Tony          | 2000   |
| 1          | 2           | Pronub        | 21300  |
| 1          | 3           | Tyrrox        | 10800  |
| 2          | 1           | Pam           | 300    |
| 2          | 7           | Bassem        | 450    |
| 2          | 9           | Hermione      | 700    |
| 3          | 7           | Bocaben       | 100    |
| 3          | 2           | Ognjen        | 2200   |
| 3          | 13          | Nyancat       | 3300   |
| 3          | 15          | Morninngcat   | 1866   |
+------------+-------------+---------------+--------+

Result table:
+------------+-------------+---------------+--------+
| company_id | employee_id | employee_name | salary |
+------------+-------------+---------------+--------+
| 1          | 1           | Tony          | 1020   |
| 1          | 2           | Pronub        | 10863  |
| 1          | 3           | Tyrrox        | 5508   |
| 2          | 1           | Pam           | 300    |
| 2          | 7           | Bassem        | 450    |
| 2          | 9           | Hermione      | 700    |
| 3          | 7           | Bocaben       | 76     |
| 3          | 2           | Ognjen        | 1672   |
| 3          | 13          | Nyancat       | 2508   |
| 3          | 15          | Morninngcat   | 5911   |
+------------+-------------+---------------+--------+
For company 1, Max salary is 21300. Employees in company 1 have taxes = 49%
For company 2, Max salary is 700. Employees in company 2 have taxes = 0%
For company 3, Max salary is 7777. Employees in company 3 have taxes = 24%
The salary after taxes = salary - (taxes percentage / 100) * salary
For example, Salary for Morninngcat (3, 15) after taxes = 7777 - 7777 * (24 / 100) = 7777 - 1866.48 = 5910.52, which is rounded to 5911.

*/


# V0
# NOTE : the salary_max logic (24% If the max salary of any employee in the company is in the range [1000, 10000] inclusive.)
select Salaries.company_id, Salaries.employee_id, Salaries.employee_name, 
    round(case when salary_max<1000 then Salaries.salary
               when salary_max>=1000 and salary_max<=10000 then Salaries.salary * 0.76
               else Salaries.salary * 0.51 end, 0) as salary
from Salaries inner join (
    select company_id, max(salary) as salary_max
    from Salaries
    group by company_id) as t
on Salaries.company_id = t.company_id

# V1
# https://code.dennyzhang.com/calculate-salarieso
select Salaries.company_id, Salaries.employee_id, Salaries.employee_name, 
    round(case when salary_max<1000 then Salaries.salary
               when salary_max>=1000 and salary_max<=10000 then Salaries.salary * 0.76
               else Salaries.salary * 0.51 end, 0) as salary
from Salaries inner join (
    select company_id, max(salary) as salary_max
    from Salaries
    group by company_id) as t
on Salaries.company_id = t.company_id

# V2
# Time:  O(m + n)
# Space: O(m + n)
SELECT s.company_id,
       s.employee_id,
       s.employee_name,
       ROUND(s.salary * t.rate) salary
FROM Salaries s
INNER JOIN
  (SELECT company_id,
          CASE
              WHEN MAX(salary) < 1000 THEN 1.0
              WHEN MAX(salary) <= 10000 THEN 0.76
              ELSE 0.51
          END AS rate
   FROM Salaries
   GROUP BY company_id
   ORDER BY NULL) t ON s.company_id = t.company_id;