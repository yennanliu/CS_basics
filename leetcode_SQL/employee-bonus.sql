/*

577. Employee Bonus
https://leetcode.com/problems/employee-bonus/
Easy

Table: Employee

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| empId       | int     |
| name        | varchar |
| supervisor  | int     |
| salary      | int     |
+-------------+---------+
empId is the column with unique values for this table.
Each row of this table indicates the name and the ID of an employee in addition to their salary and the id of their manager.

Table: Bonus

+-------------+------+
| Column Name | Type |
+-------------+------+
| empId       | int  |
| bonus       | int  |
+-------------+------+
empId is the column of unique values for this table.
empId is a foreign key (reference column) to empId from the Employee table.
Each row of this table contains the id of an employee and their respective bonus.

Write a solution to report the name and bonus amount of each employee who satisfies either of the following:

	- The employee has a bonus less than 1000.

	- The employee did not get any bonus.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Employee table:
+-------+--------+------------+--------+
| empId | name   | supervisor | salary |
+-------+--------+------------+--------+
| 3     | Brad   | null       | 4000   |
| 1     | John   | 3          | 1000   |
| 2     | Dan    | 3          | 2000   |
| 4     | Thomas | 3          | 4000   |
+-------+--------+------------+--------+
Bonus table:
+-------+-------+
| empId | bonus |
+-------+-------+
| 2     | 500   |
| 4     | 2000  |
+-------+-------+
Output:
+------+-------+
| name | bonus |
+------+-------+
| Brad | null  |
| John | null  |
| Dan  | 500   |
+------+-------+

*/

# V0
# IDEA : LEFT JOIN, THEN KEEP bonus < 1000 OR NO BONUS
#
#   Employees with no Bonus row must survive, so Bonus is LEFT JOINed and an
#   employee without one gets b.bonus = NULL. NULL < 1000 is NULL (not TRUE),
#   so the IS NULL branch is what keeps them -- without it, this WHERE on the
#   right table would turn the LEFT JOIN back into an inner join.
#
# time = O(n + m), space = O(n + m)    -- n = rows in Employee, m = rows in Bonus; hash join
SELECT
    e.name,
    b.bonus
FROM Employee AS e
LEFT JOIN Bonus AS b   -- LEFT: an employee with no bonus row must still appear
    ON e.empId = b.empId
WHERE b.bonus < 1000
   OR b.bonus IS NULL;  -- no Bonus row: NULL < 1000 alone would drop it
