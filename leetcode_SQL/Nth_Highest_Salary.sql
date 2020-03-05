-- Nth Highest Salary
-- https://leetcode.com/problems/nth-highest-salary/description/

/*

Write a SQL query to get the nth highest salary from the Employee table.

+----+--------+
| Id | Salary |
+----+--------+
| 1  | 100    |
| 2  | 200    |
| 3  | 300    |
+----+--------+
For example, given the above Employee table, the nth highest salary where n = 2 is 200. If there is no nth highest salary, then the query should return null.

+------------------------+
| getNthHighestSalary(2) |
+------------------------+
| 200                    |
+------------------------+

*/

-- V0 
-- IDEA : MYSQL PROCEDURE
-- DEMO 
-- mysql> delimiter //
-- mysql> CREATE PROCEDURE getNthHighestSalary( N INT) 
--     -> BEGIN
--     -> 
--     ->      # Write your MySQL query statement below.
--     ->      SELECT MAX(Salary) /*This is the outer query part */
--     ->             FROM Employee Emp1
--     ->             WHERE (N-1) = ( /* Subquery starts here */
--     ->                  SELECT COUNT(DISTINCT(Emp2.Salary))
--     ->                         FROM Employee Emp2
--     ->                         WHERE Emp2.Salary > Emp1.Salary); 
--     -> END//
-- Query OK, 0 rows affected (0.00 sec)

-- mysql> use local_dev;
-- Reading table information for completion of table and column names
-- You can turn off this feature to get a quicker startup with -A

-- Database changed
-- mysql> call getNthHighestSalary(2);
-- +-------------+
-- | MAX(Salary) |
-- +-------------+
-- |         200 |
-- +-------------+
-- 1 row in set (0.00 sec)

-- Query OK, 0 rows affected (0.00 sec)

-- mysql> call getNthHighestSalary(1);
-- +-------------+
-- | MAX(Salary) |
-- +-------------+
-- |         300 |
-- +-------------+
-- 1 row in set (0.00 sec)

-- Query OK, 0 rows affected (0.00 sec)

-- mysql> call getNthHighestSalary(3);
-- +-------------+
-- | MAX(Salary) |
-- +-------------+
-- |         100 |
-- +-------------+
-- 1 row in set (0.00 sec)

-- Query OK, 0 rows affected (0.00 sec)

-- mysql>

delimiter //
CREATE PROCEDURE getNthHighestSalary( N INT) 
BEGIN

     -- Write your MySQL query statement below.
     SELECT MAX(Salary) /*This is the outer query part */
            FROM Employee Emp1
            WHERE (N-1) = ( /* Subquery starts here */
                 SELECT COUNT(DISTINCT(Emp2.Salary))
                        FROM Employee Emp2
                        WHERE Emp2.Salary > Emp1.Salary); 
END//


-- V1 
-- https://github.com/kamyu104/LeetCode/blob/master/MySQL/nth-highest-salary.sql
-- Time:  O(n^2)
-- Space: O(n)
delimiter //
CREATE FUNCTION getNthHighestSalary(N INT) RETURNS INT
BEGIN
  RETURN (
     -- Write your MySQL query statement below.
     SELECT MAX(Salary) /*This is the outer query part */
            FROM Employee Emp1
            WHERE (N-1) = ( /* Subquery starts here */
                 SELECT COUNT(DISTINCT(Emp2.Salary))
                        FROM Employee Emp2
                        WHERE Emp2.Salary > Emp1.Salary)
  );
END//
