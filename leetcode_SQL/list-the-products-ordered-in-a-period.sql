/*

1327. List the Products Ordered in a Period
https://leetcode.com/problems/list-the-products-ordered-in-a-period/
Easy

Table: Products

+------------------+---------+
| Column Name      | Type    |
+------------------+---------+
| product_id       | int     |
| product_name     | varchar |
| product_category | varchar |
+------------------+---------+
product_id is the primary key (column with unique values) for this table.
This table contains data about the company's products.

Table: Orders

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| product_id    | int     |
| order_date    | date    |
| unit          | int     |
+---------------+---------+
This table may have duplicate rows.
product_id is a foreign key (reference column) to the Products table.
unit is the number of products ordered in order_date.

Write a solution to get the names of products that have at least 100 units ordered in February 2020 and their amount.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Products table:
+-------------+-----------------------+------------------+
| product_id  | product_name          | product_category |
+-------------+-----------------------+------------------+
| 1           | Leetcode Solutions    | Book             |
| 2           | Jewels of Stringology | Book             |
| 3           | HP                    | Laptop           |
| 4           | Lenovo                | Laptop           |
| 5           | Leetcode Kit          | T-shirt          |
+-------------+-----------------------+------------------+
Orders table:
+--------------+--------------+----------+
| product_id   | order_date   | unit     |
+--------------+--------------+----------+
| 1            | 2020-02-05   | 60       |
| 1            | 2020-02-10   | 70       |
| 2            | 2020-01-18   | 30       |
| 2            | 2020-02-11   | 80       |
| 3            | 2020-02-17   | 2        |
| 3            | 2020-02-24   | 3        |
| 4            | 2020-03-01   | 20       |
| 4            | 2020-03-04   | 30       |
| 4            | 2020-03-04   | 60       |
| 5            | 2020-02-25   | 50       |
| 5            | 2020-02-27   | 50       |
| 5            | 2020-03-01   | 50       |
+--------------+--------------+----------+
Output:
+--------------------+---------+
| product_name       | unit    |
+--------------------+---------+
| Leetcode Solutions | 130     |
| Leetcode Kit       | 100     |
+--------------------+---------+
Explanation:
Products with product_id = 1 is ordered in February a total of (60 + 70) = 130.
Products with product_id = 2 is ordered in February a total of 80.
Products with product_id = 3 is ordered in February a total of (2 + 3) = 5.
Products with product_id = 4 was not ordered in February 2020.
Products with product_id = 5 is ordered in February a total of (50 + 50) = 100.

*/

# V0
# IDEA : JOIN, FILTER TO FEBRUARY 2020, GROUP BY PRODUCT, HAVING SUM >= 100
#
#   The WHERE keeps only February 2020 orders before grouping, so a
#   product's March units never count toward its total. A half-open range
#   (>= Feb 1, < Mar 1) needs no knowledge of how many days February has
#   and can use an index on order_date; LEFT(order_date, 7) = '2020-02'
#   gives the same rows but cannot. HAVING filters on the per-product sum.
#   Group by product_id (not the name): two products may share a name.
#
# time = O(n + m), space = O(m)    -- n = rows in Products, m = rows in Orders; hash join and one pass to sum per product
SELECT
    p.product_name,
    SUM(o.unit) AS unit
FROM Orders AS o
JOIN Products AS p
    ON o.product_id = p.product_id
WHERE o.order_date >= '2020-02-01'
  AND o.order_date < '2020-03-01'
GROUP BY
    p.product_id,
    p.product_name
HAVING SUM(o.unit) >= 100;

# V1
# IDEA : AGGREGATE THE ORDERS FIRST IN A CTE, THEN JOIN FOR THE NAME
#
#   Sum February's units per product_id on Orders alone, keep the totals of
#   at least 100, and only then join Products to turn each id into a name.
#   The join touches only the qualifying products instead of every order row.
#
# time = O(n + m), space = O(m)    -- n = rows in Products, m = rows in Orders; one pass to sum, then a join on the few survivors
WITH february_units AS (
    -- total units each product sold in February 2020
    SELECT
        product_id,
        SUM(unit) AS unit
    FROM Orders
    WHERE order_date >= '2020-02-01'
      AND order_date < '2020-03-01'
    GROUP BY product_id
    HAVING SUM(unit) >= 100
)
SELECT
    p.product_name,
    f.unit
FROM february_units AS f
JOIN Products AS p
    ON f.product_id = p.product_id;
