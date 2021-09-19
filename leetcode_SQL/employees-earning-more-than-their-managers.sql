/*

https://leetcode.com/problems/employees-earning-more-than-their-managers/description/



The Employee table holds all employees including their managers. Every employee has an Id, and there is also a column for the manager Id.

+----+-------+--------+-----------+
| Id | Name  | Salary | ManagerId |
+----+-------+--------+-----------+
| 1  | Joe   | 70000  | 3         |
| 2  | Henry | 80000  | 4         |
| 3  | Sam   | 60000  | NULL      |
| 4  | Max   | 90000  | NULL      |
+----+-------+--------+-----------+
Given the Employee table, write a SQL query that finds out employees who earn more than their managers. For the above table, Joe is the only employee who earns more than his manager.

+----------+
| Employee |
+----------+
| Joe      |
+----------+

*/ 

/* V0 */
select 
a.Name as Employee  
from 
Employee a, 
Employee b 
where 
b.Id = a.ManagerId 
and 
a.Salary > b.Salary 

/* V0' */
SELECT e.Name AS Employee
FROM Employee e
WHERE e.Salary >
    (SELECT e2.Salary
     FROM Employee e2
     WHERE e.ManagerId = e2.Id )
     
/* V1  */
SELECT a.Name as Employee
FROM Employee a
LEFT JOIN Employee b ON a.ManagerId = b.Id
WHERE a.Salary > b.Salary

/* V2 */
SELECT Name AS Employee 
    FROM Employee e 
    WHERE e.ManagerId IS NOT NULL AND e.Salary > (SELECT Salary 
                          FROM Employee 
                          WHERE e.ManagerId = Id)
                       