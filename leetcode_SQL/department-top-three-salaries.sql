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

# V0 : DEV 
# TODO : use mysql procedure, i.e. similiar as # 0177 Nth Highest Salary

# V1 
# Write your MySQL query statement below
SELECT D.Name AS Department, E.Name AS Employee, E.Salary AS Salary 
FROM Employee E INNER JOIN Department D ON E.DepartmentId = D.Id 
WHERE (SELECT COUNT(DISTINCT(Salary)) FROM Employee 
       WHERE DepartmentId = E.DepartmentId AND Salary > E.Salary) < 3
ORDER by E.DepartmentId, E.Salary DESC;
