# Time:  O(n^2)
# Space: O(n)
# 
# The Employee table holds all employees. Every employee has an Id, and there is also a column for the department Id.
# 
# +----+-------+--------+--------------+
# | Id | Name  | Salary | DepartmentId |
# +----+-------+--------+--------------+
# | 1  | Joe   | 70000  | 1            |
# | 2  | Henry | 80000  | 2            |
# | 3  | Sam   | 60000  | 2            |
# | 4  | Max   | 90000  | 1            |
# | 5  | Janet | 69000  | 1            |
# | 6  | Randy | 85000  | 1            |
# +----+-------+--------+--------------+
# The Department table holds all departments of the company.
# 
# +----+----------+
# | Id | Name     |
# +----+----------+
# | 1  | IT       |
# | 2  | Sales    |
# +----+----------+
# Write a SQL query to find employees who earn the top three salaries in each of the department. For the above tables, your SQL query should return the following rows.
# 
# +------------+----------+--------+
# | Department | Employee | Salary |
# +------------+----------+--------+
# | IT         | Max      | 90000  |
# | IT         | Randy    | 85000  |
# | IT         | Joe      | 70000  |
# | Sales      | Henry    | 80000  |
# | Sales      | Sam      | 60000  |
# +------------+----------+--------+

# V0 : IDEA : count distinct + where < n 
SELECT d.Name AS Department,
       e.Name AS Employee,
       e.Salary AS Salary
FROM Employee e
INNER JOIN Department d ON e.DepartmentId = d.Id
WHERE
    (SELECT count(DISTINCT e2.Salary) AS cnt
     FROM Employee e2
     WHERE e2.Salary > e.Salary
       AND e2.DepartmentId = e.DepartmentId) < 3

# V0' : IDEA : DENSE_RANK
SELECT Department,
       Employee,
       Salary
FROM
  (SELECT d.Name AS Department,
          e.Name AS Employee,
          e.Salary AS Salary,
          DENSE_RANK() OVER (PARTITION BY d.Name
                             ORDER BY e.Salary DESC) AS dept_sal_rank
   FROM Employee e
   INNER JOIN Department d ON e.DepartmentId = d.Id) sub
WHERE dept_sal_rank <= 3

# V1
# https://leetcode.com/problems/department-top-three-salaries/solution/
SELECT
    d.Name AS 'Department', e1.Name AS 'Employee', e1.Salary
FROM
    Employee e1
        JOIN
    Department d ON e1.DepartmentId = d.Id
WHERE
    3 > (SELECT
            COUNT(DISTINCT e2.Salary)
        FROM
            Employee e2
        WHERE
            e2.Salary > e1.Salary
                AND e1.DepartmentId = e2.DepartmentId
        )
;

# V1''
# IDEA : DENSE_RANK
SELECT a.Department, a.Employee, a.Salary FROM
(
Select b.Name as Department,a.Name as Employee,a.salary As Salary,
DENSE_RANK() over (PARTITION BY b.Name ORDER BY a.Salary DESC)  as dept_sal_rank
from Employee a
INNER JOIN Department b ON a.DepartmentId = b.Id ) a

where a.dept_sal_Rank <=3

# V2
# Write your MySQL query statement below
SELECT D.Name AS Department, E.Name AS Employee, E.Salary AS Salary 
FROM Employee E INNER JOIN Department D ON E.DepartmentId = D.Id 
WHERE (SELECT COUNT(DISTINCT(Salary)) FROM Employee 
       WHERE DepartmentId = E.DepartmentId AND Salary > E.Salary) < 3
ORDER by E.DepartmentId, E.Salary DESC;