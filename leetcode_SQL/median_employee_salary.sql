/*

Question

The Employee table holds all employees. The employee table has three columns: Employee Id, Company Name, and Salary.
+-----+------------+--------+
|Id   | Company    | Salary |
+-----+------------+--------+
|1    | A          | 2341   |
|2    | A          | 341    |
|3    | A          | 15     |
|4    | A          | 15314  |
|5    | A          | 451    |
|6    | A          | 513    |
|7    | B          | 15     |
|8    | B          | 13     |
|9    | B          | 1154   |
|10   | B          | 1345   |
|11   | B          | 1221   |
|12   | B          | 234    |
|13   | C          | 2345   |
|14   | C          | 2645   |
|15   | C          | 2645   |
|16   | C          | 2652   |
|17   | C          | 65     |
+-----+------------+--------+
Write a SQL query to find the median salary of each company. Bonus points if you can solve it without using any built-in SQL functions.
+-----+------------+--------+
|Id   | Company    | Salary |
+-----+------------+--------+
|5    | A          | 451    |
|6    | A          | 513    |
|12   | B          | 234    |
|9    | B          | 1154   |
|14   | C          | 2645   |
+-----+------------+--------+

*/


# V0

# V1
# https://circlecoder.com/median-employee-salary/
select t1.Id, t1.Company, t1.Salary
from (select Id, Company, Salary, 
      dense_rank() over (partition by Company order by Salary, Id) as Sort, 
      count(1) over (partition by Company) / 2.0 as Counts
from Employee ) t1
where Sort between Counts and Counts + 1

# V1'
# https://medium.com/learn-from-data/leetcode-569-median-employee-salary-d9653d82048c
SELECT id, company, salary
*,
COUNT() OVER (PARTITION BY company) cnt
ROW_NUMBER OVER (PARTITION BY company ORDER BY salary) rnum
FROM employee e
WHERE (cnt/2) <= rnum AND rnum<= (cnt/2) + 1
ORDER BY company, salary

# V1''
# https://blog.csdn.net/chelseajcole/article/details/81503406

# V1'''
# https://blog.csdn.net/chelseajcole/article/details/81503406

# V2