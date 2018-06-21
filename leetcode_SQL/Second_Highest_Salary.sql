

# Second Highest Salary
# https://leetcode.com/problems/second-highest-salary/

/*

Write a SQL query to get the second highest salary from the Employee table.

+----+--------+
| Id | Salary |
+----+--------+
| 1  | 100    |
| 2  | 200    |
| 3  | 300    |
+----+--------+
For example, given the above Employee table, the query should return 200 as the second highest salary. If there is no second highest salary, then the query should return null.

+---------------------+
| SecondHighestSalary |
+---------------------+
| 200                 |
+---------------------+

*/


# V1 


select ifnull(max(Salary),0) AS SecondHighestSalary from Employee a 
where a.Salary < (select max(Salary) from Employee b )


# V2 

SELECT ifnull(a.salary, 0) AS SecondHighestSalary
FROM Employee a
WHERE a.salary <
    (SELECT max(b.salary)
     FROM Employee b)
ORDER BY 1 DESC
LIMIT 1



