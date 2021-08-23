/*

Table: Customers

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| customer_id   | int     |
| name          | varchar |
| country       | varchar |
+---------------+---------+
customer_id is the primary key for this table.
This table contains information of the customers in the company.
Table: Product

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| product_id    | int     |
| description   | varchar |
| price         | int     |
+---------------+---------+
product_id is the primary key for this table.
This table contains information of the products in the company.
price is the product cost.
Table: Orders

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| order_id      | int     |
| customer_id   | int     |
| product_id    | int     |
| order_date    | date    |
| quantity      | int     |
+---------------+---------+
order_id is the primary key for this table.
This table contains information on customer orders.
customer_id is the id of the customer who bought "quantity" products with id "product_id".
Order_date is the date in format ('YYYY-MM-DD') when the order was shipped.
Write an SQL query to report the customer_id and customer_name of customers who have spent at least $100 in each month of June and July 2020.

Return the result table in any order.

The query result format is in the following example.

Customers
+--------------+-----------+-------------+
| customer_id  | name      | country     |
+--------------+-----------+-------------+
| 1            | Winston   | USA         |
| 2            | Jonathan  | Peru        |
| 3            | Moustafa  | Egypt       |
+--------------+-----------+-------------+

Product
+--------------+-------------+-------------+
| product_id   | description | price       |
+--------------+-------------+-------------+
| 10           | LC Phone    | 300         |
| 20           | LC T-Shirt  | 10          |
| 30           | LC Book     | 45          |
| 40           | LC Keychain | 2           |
+--------------+-------------+-------------+

Orders
+--------------+-------------+-------------+-------------+-----------+
| order_id     | customer_id | product_id  | order_date  | quantity  |
+--------------+-------------+-------------+-------------+-----------+
| 1            | 1           | 10          | 2020-06-10  | 1         |
| 2            | 1           | 20          | 2020-07-01  | 1         |
| 3            | 1           | 30          | 2020-07-08  | 2         |
| 4            | 2           | 10          | 2020-06-15  | 2         |
| 5            | 2           | 40          | 2020-07-01  | 10        |
| 6            | 3           | 20          | 2020-06-24  | 2         |
| 7            | 3           | 30          | 2020-06-25  | 2         |
| 9            | 3           | 30          | 2020-05-08  | 3         |
+--------------+-------------+-------------+-------------+-----------+

Result table:
+--------------+------------+
| customer_id  | name       |  
+--------------+------------+
| 1            | Winston    |
+--------------+------------+ 
Winston spent $300 (300 * 1) in June and $100 ( 10 * 1 + 45 * 2) in July 2020.
Jonathan spent $600 (300 * 2) in June and $20 ( 2 * 10) in July 2020.
Moustafa spent $110 (10 * 2 + 45 * 2) in June and $0 in July 2020.

*/

# V0
select o.customer_id, c.name
from Customers c, Product p, Orders o
where c.customer_id = o.customer_id and p.product_id = o.product_id
group by o.customer_id
having 
(
    sum(case when o.order_date like '2020-06%' then o.quantity*p.price else 0 end) >= 100
    and
    sum(case when o.order_date like '2020-07%' then o.quantity*p.price else 0 end) >= 100
)

# V1
# https://zhuanlan.zhihu.com/p/264975229
SELECT customer_id, name FROM Customers
WHERE customer_id IN (
SELECT a.customer_id FROM Orders AS a
JOIN Product AS b
ON a.product_id = b.product_id
WHERE a.order_date BETWEEN '2020-06-01' AND '2020-06-30'
GROUP BY a.customer_id
HAVING SUM(a.quantity * b.price) >= 100
)
AND customer_id IN (
SELECT a.customer_id FROM Orders AS a
JOIN Product AS b
ON a.product_id = b.product_id
WHERE a.order_date BETWEEN '2020-07-01' AND '2020-07-31'
GROUP BY a.customer_id
HAVING SUM(a.quantity * b.price) >= 100
);

# V1'
# https://cloud.tencent.com/developer/article/1787716
select o.customer_id, c.name
from Customers c, Product p, Orders o
where c.customer_id = o.customer_id and p.product_id = o.product_id
group by o.customer_id
having 
(
    sum(case when o.order_date like '2020-06%' then o.quantity*p.price else 0 end) >= 100
    and
    sum(case when o.order_date like '2020-07%' then o.quantity*p.price else 0 end) >= 100
)

# V2
# Time:  O(n)
# Space: O(n)
SELECT a.customer_id,
       a.name
FROM Customers AS a
INNER JOIN
  (SELECT *
   FROM Orders
   WHERE order_date BETWEEN "2020-06-01" AND "2020-07-31" ) AS b
ON a.customer_id = b.customer_id
INNER JOIN Product AS c
ON b.product_id = c.product_id
GROUP BY a.customer_id
HAVING SUM(CASE
               WHEN LEFT(b.order_date, 7) = "2020-06" THEN c.price * b.quantity
               ELSE 0
           END) >= 100
AND    SUM(CASE
               WHEN LEFT(b.order_date, 7) = "2020-07" THEN c.price * b.quantity
               ELSE 0
           END) >= 100
ORDER BY NULL;