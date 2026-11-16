/*

1789. Primary Department for Each Employee
https://leetcode.com/problems/primary-department-for-each-employee/
Easy

Table: Employee

+---------------+---------+
| Column Name   |  Type   |
+---------------+---------+
| employee_id   | int     |
| department_id | int     |
| primary_flag  | varchar |
+---------------+---------+
(employee_id, department_id) is the primary key (combination of columns with unique values) for this table.
employee_id is the id of the employee.
department_id is the id of the department to which the employee belongs.
primary_flag is an ENUM (category) of type ('Y', 'N'). If the flag is 'Y', the department is the primary department for the employee. If the flag is 'N', the department is not the primary.

Employees can belong to multiple departments. When the employee joins other departments, they need to decide which department is their primary department. Note that when an employee belongs to only one department, their primary column is 'N'.

Write a solution to report all the employees with their primary department. For employees who belong to one department, report their only department.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Employee table:
+-------------+---------------+--------------+
| employee_id | department_id | primary_flag |
+-------------+---------------+--------------+
| 1           | 1             | N            |
| 2           | 1             | Y            |
| 2           | 2             | N            |
| 3           | 3             | N            |
| 4           | 2             | N            |
| 4           | 3             | Y            |
| 4           | 4             | N            |
+-------------+---------------+--------------+
Output:
+-------------+---------------+
| employee_id | department_id |
+-------------+---------------+
| 1           | 1             |
| 2           | 1             |
| 3           | 3             |
| 4           | 3             |
+-------------+---------------+
Explanation:
- The Primary department for employee 1 is 1.
- The Primary department for employee 2 is 1.
- The Primary department for employee 3 is 3.
- The Primary department for employee 4 is 3.

*/

# V0
# IDEA : FILTER -- THE 'Y' ROW, OR THE ONLY ROW
#
#   An employee's answer is either the department flagged 'Y', or, when they
#   belong to exactly one department, that department (its flag is 'N').
#   The subquery lists the single-department employees; employee_id is part
#   of the primary key, so it is never NULL and IN is safe.
#
# time = O(n), space = O(n)    -- n = rows in Employee; one group pass, one scan
SELECT
    employee_id,
    department_id
FROM Employee
WHERE primary_flag = 'Y'
   OR employee_id IN (
        -- employees who belong to exactly one department
        SELECT employee_id
        FROM Employee
        GROUP BY employee_id
        HAVING COUNT(*) = 1
   );

# V1
# IDEA : UNION OF THE TWO CASES
#
#   Case 1: the rows flagged 'Y'. Case 2: employees with one department,
#   whose only department is MIN(department_id) of a one-row group (an
#   aggregate keeps it valid under ONLY_FULL_GROUP_BY). UNION drops the
#   duplicate if a one-department employee were ever flagged 'Y'.
#
# time = O(n), space = O(n)    -- n = rows in Employee
SELECT
    employee_id,
    department_id
FROM Employee
WHERE primary_flag = 'Y'
UNION
SELECT
    employee_id,
    MIN(department_id) AS department_id
FROM Employee
GROUP BY employee_id
HAVING COUNT(*) = 1;

# V2
# IDEA : WINDOW COUNT PER EMPLOYEE
#
#   COUNT(*) OVER (PARTITION BY employee_id) puts each employee's department
#   count on every one of their rows, so one filter handles both cases
#   without a second pass over the table.
#
# time = O(n log n), space = O(n)    -- n = rows in Employee; the window sorts by employee_id
WITH with_dept_count AS (
    SELECT
        employee_id,
        department_id,
        primary_flag,
        COUNT(*) OVER (PARTITION BY employee_id) AS dept_count
    FROM Employee
)
SELECT
    employee_id,
    department_id
FROM with_dept_count
WHERE primary_flag = 'Y'
   OR dept_count = 1;
