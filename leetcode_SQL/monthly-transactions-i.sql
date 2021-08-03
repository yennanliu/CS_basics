/*

1193. Monthly Transactions I
Table: Transactions

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| id            | int     |
| country       | varchar |
| state         | enum    |
| amount        | int     |
| trans_date    | date    |
+---------------+---------+

id is the primary key of this table.
The table has information about incoming transactions.
The state column is an enum of type ["approved", "declined"].
 

Write an SQL query to find for each month and country, the number of transactions and their total amount, the number of approved transactions and their total amount.

The query result format is in the following example:

Transactions table:
+------+---------+----------+--------+------------+
| id   | country | state    | amount | trans_date |
+------+---------+----------+--------+------------+
| 121  | US      | approved | 1000   | 2018-12-18 |
| 122  | US      | declined | 2000   | 2018-12-19 |
| 123  | US      | approved | 2000   | 2019-01-01 |
| 124  | DE      | approved | 2000   | 2019-01-07 |
+------+---------+----------+--------+------------+

Result table:
+----------+---------+-------------+----------------+--------------------+-----------------------+
| month    | country | trans_count | approved_count | trans_total_amount | approved_total_amount |
+----------+---------+-------------+----------------+--------------------+-----------------------+
| 2018-12  | US      | 2           | 1              | 3000               | 1000                  |
| 2019-01  | US      | 1           | 1              | 2000               | 2000                  |
| 2019-01  | DE      | 1           | 1              | 2000               | 2000                  |
+----------+---------+-------------+----------------+--------------------+-----------------------+

*/

# V0
SELECT
Date_format(trans_date, '%Y-%m') AS month,
country AS country,
count(id) AS trans_count,
SUM(case WHEN state = 'approved' THEN 1 ELSE 0 END) AS approved_count,
SUM(amount) AS trans_total_amount,
SUM(case WHEN state = 'approved' THEN amount ELSE 0 END) AS approved_total_amount
FROM
transactions
GROUP BY
Date_format(trans_date, '%Y-%m') ,
country;

# V1
# https://www.programmersought.com/article/92805114112/
SELECT DATE_FORMAT(trans_date, '%Y-%m') AS month, country
       , COUNT(1) AS trans_count
       , COUNT(if(state = 'approved', 1, NULL)) AS approved_count
       , SUM(amount) AS trans_total_amount
       , SUM(if(state = 'approved', amount, 0)) AS approved_total_amount
FROM Transactions
GROUP BY month, country;

# V2
# Time:  O(n)
# Space: O(n)
SELECT Date_format(trans_date, '%Y-%m')       AS month, 
       country, 
       Count(id)                              AS trans_count, 
       Count(IF(state = 'approved', 1, NULL)) AS approved_count, 
       SUM(amount)                            AS trans_total_amount, 
       SUM(IF(state = 'approved', amount, 0)) AS approved_total_amount 
FROM   transactions 
GROUP  BY Date_format(trans_date, '%Y-%m'), 
          country 
ORDER BY NULL