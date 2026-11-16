/*

1978. Employees Whose Manager Left the Company
https://leetcode.com/problems/employees-whose-manager-left-the-company/
Easy

Table: Employees

+-------------+----------+
| Column Name | Type     |
+-------------+----------+
| employee_id | int      |
| name        | varchar  |
| manager_id  | int      |
| salary      | int      |
+-------------+----------+
In SQL, employee_id is the primary key for this table.
This table contains information about the employees, their salary, and the ID of their manager. Some employees do not have a manager (manager_id is null).

Find the IDs of the employees whose salary is strictly less than $30000 and whose manager left the company. When a manager leaves the company, their information is deleted from the Employees table, but the reports still have their manager_id set to the manager that left.

Return the result table ordered by employee_id.

The result format is in the following example.

Example 1:

Input:
Employees table:
+-------------+-----------+------------+--------+
| employee_id | name      | manager_id | salary |
+-------------+-----------+------------+--------+
| 3           | Mila      | 9          | 60301  |
| 12          | Antonella | null       | 31000  |
| 13          | Emery     | null       | 67084  |
| 1           | Kalel     | 11         | 21241  |
| 9           | Mikaela   | null       | 50937  |
| 11          | Joziah    | 6          | 28485  |
+-------------+-----------+------------+--------+
Output:
+-------------+
| employee_id |
+-------------+
| 11          |
+-------------+

Explanation:
The employees with a salary less than $30000 are 1 (Kalel) and 11 (Joziah).
Kalel's manager is employee 11, who is still in the company (Joziah).
Joziah's manager is employee 6, who left the company because there is no row for employee 6 as it was deleted.

*/

# V0
# IDEA : NOT IN AGAINST THE IDS STILL IN THE TABLE
#
#   A manager "left" when manager_id points at an employee_id that no longer
#   exists. The subquery returns employee_id, the primary key, so it never
#   holds a NULL -- that is what keeps NOT IN safe (NOT IN against a list
#   containing a NULL returns no rows). A NULL manager_id already makes NOT IN
#   evaluate to NULL (row dropped); IS NOT NULL says so outright.
#
# time = O(n log n), space = O(n)    -- n = rows in Employees; ids hashed once, one scan, then the sort
SELECT employee_id
FROM Employees
WHERE salary < 30000
  AND manager_id IS NOT NULL          -- no manager is not "manager left"
  AND manager_id NOT IN (
      -- everyone still in the company
      SELECT employee_id
      FROM Employees
  )
ORDER BY employee_id;

# V1
# IDEA : NOT EXISTS (ANTI-JOIN)
#
#   For each low earner, look for a row whose employee_id is their manager_id.
#   None found means the manager left. NOT EXISTS is NULL-safe on its own, but
#   a NULL manager_id also matches no row -- so IS NOT NULL is what keeps the
#   people with no manager out.
#
# time = O(n log n), space = O(n)    -- n = rows in Employees; one PK lookup per row, then the sort
SELECT emp.employee_id
FROM Employees emp
WHERE emp.salary < 30000
  AND emp.manager_id IS NOT NULL      -- a NULL would otherwise count as "manager not found"
  AND NOT EXISTS (
      SELECT 1
      FROM Employees mgr
      WHERE mgr.employee_id = emp.manager_id
  )
ORDER BY emp.employee_id;

# V2
# IDEA : SELF LEFT JOIN, KEEP THE UNMATCHED
#
#   LEFT JOIN each employee to their manager's row; when the manager left,
#   every mgr column comes back NULL. mgr.employee_id IS NULL sits in WHERE on
#   purpose: it is the anti-join test, run after the join has kept every
#   employee.
#
# time = O(n log n), space = O(n)    -- n = rows in Employees; one PK lookup per row, then the sort
SELECT emp.employee_id
FROM Employees emp
LEFT JOIN Employees mgr
       ON mgr.employee_id = emp.manager_id
WHERE emp.salary < 30000
  AND emp.manager_id IS NOT NULL      -- had a manager ...
  AND mgr.employee_id IS NULL         -- ... whose row is gone
ORDER BY emp.employee_id;
