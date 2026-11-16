/*

570. Managers with at Least 5 Direct Reports
https://leetcode.com/problems/managers-with-at-least-5-direct-reports/
Medium

Table: Employee

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| name        | varchar |
| department  | varchar |
| managerId   | int     |
+-------------+---------+
id is the primary key (column with unique values) for this table.
Each row of this table indicates the name of an employee, their department, and the id of their manager.
If managerId is null, then the employee does not have a manager.
No employee will be the manager of themself.

Write a solution to find managers with at least five direct reports.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Employee table:
+-----+-------+------------+-----------+
| id  | name  | department | managerId |
+-----+-------+------------+-----------+
| 101 | John  | A          | null      |
| 102 | Dan   | A          | 101       |
| 103 | James | A          | 101       |
| 104 | Amy   | A          | 101       |
| 105 | Anne  | A          | 101       |
| 106 | Ron   | B          | 101       |
+-----+-------+------------+-----------+
Output:
+------+
| name |
+------+
| John |
+------+

*/

# V0
# IDEA : COUNT REPORTS PER managerId, THEN JOIN BACK FOR THE NAME
#
#   A manager's direct reports are the rows whose managerId is that manager's
#   id, so GROUP BY managerId + HAVING COUNT(*) >= 5 finds the manager ids.
#   Joining those ids back to Employee gets the names, and drops a managerId
#   that matches no employee. Employees with managerId NULL form a NULL group,
#   which never matches in the join (NULL = id is never TRUE), so it is dropped.
#
# time = O(n), space = O(n)    -- n = rows in Employee; hash group by managerId, then hash join
WITH big_managers AS (
    SELECT
        managerId
    FROM Employee
    GROUP BY managerId
    HAVING COUNT(*) >= 5
)
SELECT
    e.name
FROM Employee AS e
JOIN big_managers AS bm
    ON e.id = bm.managerId;   -- the NULL managerId group never matches

# V1
# IDEA : SELF JOIN MANAGER TO EACH REPORT, GROUP BY MANAGER
#
#   Join Employee (as the manager) to Employee (as the report) on
#   report.managerId = mgr.id: each manager gets one row per direct report.
#   Group by the manager's id -- not the name, since two managers may share a
#   name -- and keep groups of five or more.
#
# time = O(n), space = O(n)    -- n = rows in Employee; hash join + hash group
SELECT
    mgr.name
FROM Employee AS mgr
JOIN Employee AS report
    ON report.managerId = mgr.id
GROUP BY
    mgr.id,
    mgr.name
HAVING COUNT(*) >= 5;
