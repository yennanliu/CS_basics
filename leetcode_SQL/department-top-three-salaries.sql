/*

185. Department Top Three Salaries
https://leetcode.com/problems/department-top-three-salaries/
Hard

Table: Employee

+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| id           | int     |
| name         | varchar |
| salary       | int     |
| departmentId | int     |
+--------------+---------+
id is the primary key (column with unique values) for this table.
departmentId is a foreign key (reference column) of the ID from the Department table.
Each row of this table indicates the ID, name, and salary of an employee. It also contains the ID of their department.

Table: Department

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| name        | varchar |
+-------------+---------+
id is the primary key (column with unique values) for this table.
Each row of this table indicates the ID of a department and its name.

A company's executives are interested in seeing who earns the most money in each of the company's departments. A high earner in a department is an employee who has a salary in the top three unique salaries for that department.

Write a solution to find the employees who are high earners in each of the departments.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Employee table:
+----+-------+--------+--------------+
| id | name  | salary | departmentId |
+----+-------+--------+--------------+
| 1  | Joe   | 85000  | 1            |
| 2  | Henry | 80000  | 2            |
| 3  | Sam   | 60000  | 2            |
| 4  | Max   | 90000  | 1            |
| 5  | Janet | 69000  | 1            |
| 6  | Randy | 85000  | 1            |
| 7  | Will  | 70000  | 1            |
+----+-------+--------+--------------+
Department table:
+----+-------+
| id | name  |
+----+-------+
| 1  | IT    |
| 2  | Sales |
+----+-------+
Output:
+------------+----------+--------+
| Department | Employee | Salary |
+------------+----------+--------+
| IT         | Max      | 90000  |
| IT         | Joe      | 85000  |
| IT         | Randy    | 85000  |
| IT         | Will     | 70000  |
| Sales      | Henry    | 80000  |
| Sales      | Sam      | 60000  |
+------------+----------+--------+
Explanation:
In the IT department:
- Max earns the highest unique salary
- Both Randy and Joe earn the second-highest unique salary
- Will earns the third-highest unique salary

In the Sales department:
- Henry earns the highest salary
- Sam earns the second-highest salary
- There is no third-highest salary as there are only two employees

Constraints:

	- There are no employees with the exact same name, salary and department.

*/

# V0
# IDEA : CORRELATED COUNT OF DISTINCT HIGHER SALARIES
#
#   An employee is in their department's top three unique salaries exactly
#   when fewer than 3 distinct salaries in that department are higher than
#   theirs. COUNT(DISTINCT ...) is what makes ties share a place: two people
#   on 85000 both see one higher salary (90000).
#   INNER JOIN Department: an employee whose departmentId has no row is not
#   in any department to report.
#
# time = O(n^2), space = O(n)    -- n = rows in Employee; the subquery rescans a department per employee
SELECT
    d.name   AS Department,
    e.name   AS Employee,
    e.salary AS Salary
FROM Employee e
JOIN Department d
  ON d.id = e.departmentId
WHERE (
    -- distinct salaries in the same department above this one
    SELECT COUNT(DISTINCT higher.salary)
    FROM Employee higher
    WHERE higher.departmentId = e.departmentId
      AND higher.salary > e.salary
) < 3;

# V1
# IDEA : DENSE_RANK PER DEPARTMENT
#
#   Rank salaries inside each department, highest first, and keep ranks 1-3.
#   DENSE_RANK, not RANK: after two people tie at 2nd, the next salary must
#   be 3rd, not 4th -- "top three unique salaries" counts values, not people.
#   Partition by departmentId (the key), not by department name.
#
# time = O(n log n), space = O(n)    -- n = rows in Employee; the window sorts each department
WITH ranked AS (
    SELECT
        e.name,
        e.salary,
        e.departmentId,
        DENSE_RANK() OVER (PARTITION BY e.departmentId
                           ORDER BY e.salary DESC) AS salary_rank
    FROM Employee e
)
SELECT
    d.name   AS Department,
    r.name   AS Employee,
    r.salary AS Salary
FROM ranked r
JOIN Department d
  ON d.id = r.departmentId
WHERE r.salary_rank <= 3;
