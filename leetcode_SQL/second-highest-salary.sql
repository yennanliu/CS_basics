/*

176. Second Highest Salary
https://leetcode.com/problems/second-highest-salary/
Medium

Table: Employee

+-------------+------+
| Column Name | Type |
+-------------+------+
| id          | int  |
| salary      | int  |
+-------------+------+
id is the primary key (column with unique values) for this table.
Each row of this table contains information about the salary of an employee.

Write a solution to find the second highest distinct salary from the Employee table. If there is no second highest salary, return null (return None in Pandas).

The result format is in the following example.

Example 1:

Input:
Employee table:
+----+--------+
| id | salary |
+----+--------+
| 1  | 100    |
| 2  | 200    |
| 3  | 300    |
+----+--------+
Output:
+---------------------+
| SecondHighestSalary |
+---------------------+
| 200                 |
+---------------------+

Example 2:

Input:
Employee table:
+----+--------+
| id | salary |
+----+--------+
| 1  | 100    |
+----+--------+
Output:
+---------------------+
| SecondHighestSalary |
+---------------------+
| null                |
+---------------------+

*/

# V0
# IDEA : MAX OF EVERYTHING BELOW THE MAX
#
#   The second highest distinct salary is the largest salary strictly below
#   the highest one. Ties at the top do not matter: `<` skips every copy of
#   the max. When no salary is below the max (one row, or all equal), the
#   WHERE leaves no rows and MAX() over an empty set is NULL -- exactly the
#   null the task asks for. An aggregate with no GROUP BY always returns one
#   row, so the answer is never an empty table.
#
# time = O(n), space = O(1)    -- n = rows in Employee; one scan for the inner MAX, one for the outer
SELECT MAX(salary) AS SecondHighestSalary
FROM Employee
WHERE salary < (
    SELECT MAX(salary)
    FROM Employee
);

# V1
# IDEA : DISTINCT + ORDER BY DESC + LIMIT 1 OFFSET 1, WRAPPED AS A SCALAR
#
#   Sort the distinct salaries high to low and skip the first. DISTINCT is
#   what makes it the second highest *distinct* salary (100, 100 has none).
#   On its own, LIMIT returns an empty table when there is no second row;
#   wrapping it as a scalar subquery in an outer SELECT turns "no row" into
#   one row holding NULL. This shape generalises to the Nth highest.
#
# time = O(n log n), space = O(n)    -- n = rows in Employee; the sort of the distinct salaries
SELECT (
    SELECT DISTINCT salary
    FROM Employee
    ORDER BY salary DESC
    LIMIT 1 OFFSET 1
) AS SecondHighestSalary;

# V2
# IDEA : DENSE_RANK, THEN PICK RANK 2
#
#   DENSE_RANK gives tied salaries the same rank with no gap, so rank 2 is
#   the second highest distinct salary (RANK would skip to 3 after a tie at
#   the top, and ROW_NUMBER would split the tie). MAX() over the rank-2 rows
#   collapses ties to one value and returns NULL when there is no rank 2.
#
# time = O(n log n), space = O(n)    -- n = rows in Employee; the window sorts by salary
WITH ranked AS (
    SELECT
        salary,
        DENSE_RANK() OVER (ORDER BY salary DESC) AS salary_rank
    FROM Employee
)
SELECT MAX(salary) AS SecondHighestSalary
FROM ranked
WHERE salary_rank = 2;
