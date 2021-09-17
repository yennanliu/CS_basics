/*

1965. Employees With Missing Information
Level
Easy

Description
Table: Employees

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| employee_id | int     |
| name        | varchar |
+-------------+---------+
employee_id is the primary key for this table.
Each row of this table indicates the name of the employee whose ID is employee_id.
Table: Salaries

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| employee_id | int     |
| salary      | int     |
+-------------+---------+
employee_id is the primary key for this table.
Each row of this table indicates the salary of the employee whose ID is employee_id.
Write an SQL query to report the IDs of all the employees with missing information. The information of an employee is missing if:

The employee’s name is missing, or
The employee’s salary is missing.
Return the result table ordered by employee_id in ascending order.

The query result format is in the following example:

Employees table:
+-------------+----------+
| employee_id | name     |
+-------------+----------+
| 2           | Crew     |
| 4           | Haven    |
| 5           | Kristian |
+-------------+----------+
Salaries table:
+-------------+--------+
| employee_id | salary |
+-------------+--------+
| 5           | 76071  |
| 1           | 22517  |
| 4           | 63539  |
+-------------+--------+

Result table:
+-------------+
| employee_id |
+-------------+
| 1           |
| 2           |
+-------------+

Employees 1, 2, 4, and 5 are working at this company.
The name of employee 1 is missing.
The salary of employee 2 is missing.

*/

# V0
select e.employee_id
    from Employees e
    left join Salaries s
    ON s.employee_id = e.employee_id
    where s.salary is null
union all
select s.employee_id
    from Employees e
    right join Salaries s
    ON s.employee_id = e.employee_id
    where e.name is null
order by employee_id;

# V1
# https://leetcode.ca/2021-08-16-1965-Employees-With-Missing-Information/
# Write your MySQL query statement below
select e.employee_id
    from Employees e
    left join Salaries s
    using(employee_id)
    where salary is null
union all
select s.employee_id
    from Employees e
    right join Salaries s
    using(employee_id)
    where name is null
order by employee_id;

# V2
# Time:  O(nlogn)
# Space: O(n)
WITH all_employee_info_cte AS
(
    SELECT employee_id FROM Employees
    UNION ALL
    SELECT employee_id FROM Salaries
)

SELECT employee_id
FROM all_employee_info_cte
GROUP BY employee_id
HAVING COUNT(*) != 2
ORDER BY 1;

# V2'
# Time:  O(nlogn)
# Space: O(n)
WITH all_employee_id_cte AS
(
    SELECT employee_id FROM Employees
    UNION
    SELECT employee_id FROM Salaries
),
complete_employee_id_cte AS
(
    SELECT a.employee_id 
    FROM Employees a
    INNER JOIN Salaries b
    ON a.employee_id = b.employee_id
)

SELECT employee_id
FROM all_employee_id_cte a
WHERE NOT EXISTS (SELECT 1 FROM complete_employee_id_cte b WHERE a.employee_id = b.employee_id)
ORDER BY 1;

# V2''
# Time:  O(nlogn)
# Space: O(n)
WITH all_employee_info_cte AS
(
    (
        SELECT a.employee_id, a.name, b.salary
        FROM Employees a
        LEFT JOIN Salaries b 
        ON a.employee_id = b.employee_id
    )
    UNION
    (
        SELECT b.employee_id, a.name, b.salary
        FROM Employees a
        RIGHT JOIN Salaries b 
        ON a.employee_id = b.employee_id
    )
)

SELECT employee_id
FROM all_employee_info_cte tmp
WHERE name IS NULL OR salary IS NULL
ORDER BY 1;