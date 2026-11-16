/*

1731. The Number of Employees Which Report to Each Employee
https://leetcode.com/problems/the-number-of-employees-which-report-to-each-employee/
Easy

Table: Employees

+-------------+----------+
| Column Name | Type     |
+-------------+----------+
| employee_id | int      |
| name        | varchar  |
| reports_to  | int      |
| age         | int      |
+-------------+----------+
employee_id is the column with unique values for this table.
This table contains information about the employees and the id of the manager they report to. Some employees do not report to anyone (reports_to is null).

For this problem, we will consider a manager an employee who has at least 1 other employee reporting to them.

Write a solution to report the ids and the names of all managers, the number of employees who report directly to them, and the average age of the reports rounded to the nearest integer.

Return the result table ordered by employee_id.

The result format is in the following example.

Example 1:

Input:
Employees table:
+-------------+---------+------------+-----+
| employee_id | name    | reports_to | age |
+-------------+---------+------------+-----+
| 9           | Hercy   | null       | 43  |
| 6           | Alice   | 9          | 41  |
| 4           | Bob     | 9          | 36  |
| 2           | Winston | null       | 37  |
+-------------+---------+------------+-----+
Output:
+-------------+-------+---------------+-------------+
| employee_id | name  | reports_count | average_age |
+-------------+-------+---------------+-------------+
| 9           | Hercy | 2             | 39          |
+-------------+-------+---------------+-------------+
Explanation: Hercy has 2 people report directly to him, Alice and Bob. Their average age is (41+36)/2 = 38.5, which is 39 after rounding it to the nearest integer.

*/

# V0
# IDEA : SELF JOIN REPORT -> MANAGER, THEN GROUP BY MANAGER
#
#   Each report row joins to the row of the manager it names. An inner JOIN
#   is what makes "manager = has at least one report" hold: an employee
#   nobody reports to has no matching row and never appears, and a report
#   whose reports_to is NULL matches nobody (NULL = id is never TRUE).
#   ROUND(AVG(age)) rounds half up in MySQL, so 38.5 -> 39.
#
# time = O(n log n), space = O(n)    -- n = rows in Employees; hash join + group, sort for the order
SELECT
    mgr.employee_id,
    mgr.name,
    COUNT(emp.employee_id) AS reports_count,
    ROUND(AVG(emp.age)) AS average_age
FROM Employees emp
JOIN Employees mgr
    ON emp.reports_to = mgr.employee_id   -- NULL reports_to never matches: top-level staff drop out
GROUP BY
    mgr.employee_id,
    mgr.name
ORDER BY mgr.employee_id;

# V1
# IDEA : AGGREGATE THE REPORTS FIRST, THEN LOOK UP THE MANAGER'S NAME
#
#   Group the report rows by reports_to to get each manager's count and
#   average age, then join once to fetch the name. The aggregation never
#   needs the name column, so the GROUP BY key is a single id.
#
# time = O(n log n), space = O(n)    -- n = rows in Employees
WITH report_stats AS (
    -- one row per manager id that someone reports to
    SELECT
        reports_to,
        COUNT(*) AS reports_count,
        ROUND(AVG(age)) AS average_age
    FROM Employees
    WHERE reports_to IS NOT NULL      -- the NULL group would be "reports to nobody", not a manager
    GROUP BY reports_to
)
SELECT
    e.employee_id,
    e.name,
    rs.reports_count,
    rs.average_age
FROM report_stats rs
JOIN Employees e
    ON e.employee_id = rs.reports_to
ORDER BY e.employee_id;
