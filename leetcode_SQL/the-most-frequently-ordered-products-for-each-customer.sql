/*

Description
LeetCode Problem 1596.

Table: Customers

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| customer_id   | int     |
| name          | varchar |
+---------------+---------+
customer_id is the primary key for this table.
This table contains information about the customers.
Table: Orders

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| order_id      | int     |
| order_date    | date    |
| customer_id   | int     |
| product_id    | int     |
+---------------+---------+
order_id is the primary key for this table.
This table contains information about the orders made by customer_id.
No customer will order the same product more than once in a single day.
Table: Products

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| product_id    | int     |
| product_name  | varchar |
| price         | int     |
+---------------+---------+
product_id is the primary key for this table.
This table contains information about the products.
Write an SQL query to find the most frequently ordered product(s) for each customer.

The result table should have the product_id and product_name for each customer_id who ordered at least one order. Return the result table in any order.

The query result format is in the following example:

Customers
+-------------+-------+
| customer_id | name  |
+-------------+-------+
| 1           | Alice |
| 2           | Bob   |
| 3           | Tom   |
| 4           | Jerry |
| 5           | John  |
+-------------+-------+

Orders
+----------+------------+-------------+------------+
| order_id | order_date | customer_id | product_id |
+----------+------------+-------------+------------+
| 1        | 2020-07-31 | 1           | 1          |
| 2        | 2020-07-30 | 2           | 2          |
| 3        | 2020-08-29 | 3           | 3          |
| 4        | 2020-07-29 | 4           | 1          |
| 5        | 2020-06-10 | 1           | 2          |
| 6        | 2020-08-01 | 2           | 1          |
| 7        | 2020-08-01 | 3           | 3          |
| 8        | 2020-08-03 | 1           | 2          |
| 9        | 2020-08-07 | 2           | 3          |
| 10       | 2020-07-15 | 1           | 2          |
+----------+------------+-------------+------------+

Products
+------------+--------------+-------+
| product_id | product_name | price |
+------------+--------------+-------+
| 1          | keyboard     | 120   |
| 2          | mouse        | 80    |
| 3          | screen       | 600   |
| 4          | hard disk    | 450   |
+------------+--------------+-------+
Result table:
+-------------+------------+--------------+
| customer_id | product_id | product_name |
+-------------+------------+--------------+
| 1           | 2          | mouse        |
| 2           | 1          | keyboard     |
| 2           | 2          | mouse        |
| 2           | 3          | screen       |
| 3           | 3          | screen       |
| 4           | 1          | keyboard     |
+-------------+------------+--------------+

Alice (customer 1) ordered the mouse three times and the keyboard one time, so the mouse is the most frquently ordered product for them.
Bob (customer 2) ordered the keyboard, the mouse, and the screen one time, so those are the most frquently ordered products for them.
Tom (customer 3) only ordered the screen (two times), so that is the most frquently ordered product for them.
Jerry (customer 4) only ordered the keyboard (one time), so that is the most frquently ordered product for them.
John (customer 5) did not order anything, so we do not include them in the result table.

*/


# V0
### NOTE : rank() CAN ORDER BY count
select t.customer_id, t.product_id, p.product_name
from(
    select customer_id, product_id, 
        rank() over(partition by customer_id order by count(product_id) desc) as rank_p
    from orders
    group by customer_id,product_id
    order by customer_id) as t 
left join products p on t.product_id = p.product_id
where rank_p = 1

# V1
# https://zhuanlan.zhihu.com/p/265222438
WITH

tmp AS (
SELECT a.customer_id, b.product_id, c.product_name,
COUNT(b.order_id) OVER(PARTITION BY a.customer_id, b.product_id) AS freq
FROM Customers AS a
JOIN Orders AS b
ON a.customer_id = b.customer_id
JOIN Products AS c
ON b.product_id = c.product_id
),

tmp1 AS (
SELECT customer_id, product_id, product_name, freq,
DENSE_RANK() OVER(PARTITION BY customer_id ORDER BY freq DESC) AS rnk
FROM tmp
)

SELECT DISTINCT customer_id, product_id, product_name FROM tmp1
WHERE rnk = 1;

# V1'
# https://circlecoder.com/the-most-frequently-ordered-products-for-each-customer/
select t.customer_id, t.product_id, p.product_name
from(
    select customer_id, product_id, 
        rank() over(partition by customer_id order by count(product_id) desc) as rank_p
    from orders
    group by customer_id,product_id
    order by customer_id) as t 
left join products p on t.product_id = p.product_id
where rank_p = 1

# V2
# Time:  O(n)
# Space: O(n)
WITH product_count_cte AS
  (SELECT c.customer_id,
          o.product_id,
          p.product_name,
          count(c.customer_id) AS product_cnt
   FROM Customers c,
        Orders o,
        Products p
   WHERE c.customer_id = o.customer_id
     AND o.product_id = p.product_id
   GROUP BY c.customer_id,
            o.product_id
   ORDER BY NULL)
, max_product_count_cte AS
  (SELECT customer_id,
          max(product_cnt) AS product_cnt
   FROM product_count_cte
   GROUP BY customer_id
   ORDER BY NULL)

SELECT a.customer_id,
       a.product_id,
       a.product_name
FROM product_count_cte a
INNER JOIN max_product_count_cte b
ON a.customer_id = b.customer_id AND
   a.product_cnt = b.product_cnt;