/*

1555. Bank Account Summary
Table: Users
+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| user_id      | int     |
| user_name    | varchar |
| credit       | int     |
+--------------+---------+
user_id is the primary key for this table.
Each row of this table contains the current credit information for each user.
Table: Transactions
+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| trans_id      | int     |
| paid_by       | int     |
| paid_to       | int     |
| amount        | int     |
| transacted_on | date    |
+---------------+---------+
trans_id is the primary key for this table.
Each row of this table contains the information about the transaction in the bank.
User with id (paid_by) transfer money to user with id (paid_to).
Leetcode Bank (LCB) helps its coders in making virtual payments. Our bank records all transactions in the table Transaction, we want to find out the current balance of all users and check wheter they have breached their credit limit (If their current credit is less than 0).
Write an SQL query to report.
user_id
user_name
credit, current balance after performing transactions.
credit_limit_breached, check credit_limit ("Yes" or "No")
Return the result table in any order.
The query result format is in the following example.
Users table:
+------------+--------------+-------------+
| user_id    | user_name    | credit      |
+------------+--------------+-------------+
| 1          | Moustafa     | 100         |
| 2          | Jonathan     | 200         |
| 3          | Winston      | 10000       |
| 4          | Luis         | 800         | 
+------------+--------------+-------------+
Transactions table:
+------------+------------+------------+----------+---------------+
| trans_id   | paid_by    | paid_to    | amount   | transacted_on |
+------------+------------+------------+----------+---------------+
| 1          | 1          | 3          | 400      | 2020-08-01    |
| 2          | 3          | 2          | 500      | 2020-08-02    |
| 3          | 2          | 1          | 200      | 2020-08-03    |
+------------+------------+------------+----------+---------------+
Result table:
+------------+------------+------------+-----------------------+
| user_id    | user_name  | credit     | credit_limit_breached |
+------------+------------+------------+-----------------------+
| 1          | Moustafa   | -100       | Yes                   | 
| 2          | Jonathan   | 500        | No                    |
| 3          | Winston    | 9900       | No                    |
| 4          | Luis       | 800        | No                    |
+------------+------------+------------+-----------------------+
Moustafa paid $400 on "2020-08-01" and received $200 on "2020-08-03", credit (100 -400 +200) = -$100
Jonathan received $500 on "2020-08-02" and paid $200 on "2020-08-08", credit (200 +500 -200) = $500
Winston received $400 on "2020-08-01" and paid $500 on "2020-08-03", credit (10000 +400 -500) = $9990
Luis didn't received any transfer, credit = $800

*/

# V0
WITH

tmp AS (
SELECT paid_by AS user, -1 * amount AS amount
FROM Transactions
UNION ALL 
SELECT paid_to AS user, amount
FROM Transactions
),

tmp1 AS (
SELECT user, SUM(amount) AS total FROM tmp
GROUP BY user
),

tmp2 AS (
SELECT a.user_id, a.user_name, (a.credit + IFNULL(b.total, 0)) AS credit
FROM Users AS a
LEFT JOIN tmp1 AS b
ON a.user_id = b.user
GROUP BY a.user_id
)

SELECT user_id, user_name, credit,
CASE WHEN credit >= 0 THEN 'No'
WHEN credit < 0 THEN 'Yes' END AS credit_limit_breached
FROM tmp2;

# V1
# https://zhuanlan.zhihu.com/p/265043876
WITH

tmp AS (
SELECT paid_by AS user, -1 * amount AS amount
FROM Transactions
UNION ALL 
SELECT paid_to AS user, amount
FROM Transactions
),

tmp1 AS (
SELECT user, SUM(amount) AS total FROM tmp
GROUP BY user
),

tmp2 AS (
SELECT a.user_id, a.user_name, (a.credit + IFNULL(b.total, 0)) AS credit
FROM Users AS a
LEFT JOIN tmp1 AS b
ON a.user_id = b.user
GROUP BY a.user_id
)

SELECT user_id, user_name, credit,
CASE WHEN credit >= 0 THEN 'No'
WHEN credit < 0 THEN 'Yes' END AS credit_limit_breached
FROM tmp2;

# V1'
# https://blog.csdn.net/qq_41081716/article/details/108196398

# V1''
# https://medium.com/jen-li-chen-in-data-science/leetcode-sql-f1750acefac6
select user_id as user_id, user_name as user_name,
       min(credit) + sum(amt_debit) + sum(amt_credit) as credit,
       if(min(credit) + sum(amt_debit) + sum(amt_credit) < 0, 'Yes', 'No') as credit_limit_breached
from
(
  select u.user_id, u.user_name,u.credit,
         if (u.user_id = t.paid_by, -amount, 0) as amt_debit,
         if(u.user_id = t.paid_to, amount, 0) as amt_credit
  from
  Users u
  left join Transactions t
  on u.user_id = t.paid_by or u.user_id = t.paid_to
) x
group by 1

# V2
# Time:  O(m + n)
# Space: O(m + n)
SELECT user_id,
       user_name,
       (credit - IFNULL(out_cash, 0) + IFNULL(in_cash, 0)) AS credit,
       IF((credit - IFNULL(out_cash, 0) + IFNULL(in_cash, 0)) < 0, 'Yes', 'No') AS credit_limit_breached
FROM Users users
LEFT JOIN
  (SELECT paid_by,
          SUM(amount) AS out_cash
   FROM TRANSACTION
   GROUP BY paid_by
   ORDER BY NULL) out_tmp ON users.user_id = out_tmp.paid_by
LEFT JOIN
  (SELECT paid_to,
          SUM(amount) AS in_cash
   FROM TRANSACTION
   GROUP BY paid_to
   ORDER BY NULL) in_tmp ON users.user_id = in_tmp.paid_to;