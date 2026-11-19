/*

1164. Product Price at a Given Date
https://leetcode.com/problems/product-price-at-a-given-date/
Medium

Table: Products

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| product_id    | int     |
| new_price     | int     |
| change_date   | date    |
+---------------+---------+
(product_id, change_date) is the primary key (combination of columns with unique values) of this table.
Each row of this table indicates that the price of some product was changed to a new price at some date.

Initially, all products have price 10.

Write a solution to find the prices of all products on the date 2019-08-16.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Products table:
+------------+-----------+-------------+
| product_id | new_price | change_date |
+------------+-----------+-------------+
| 1          | 20        | 2019-08-14  |
| 2          | 50        | 2019-08-14  |
| 1          | 30        | 2019-08-15  |
| 1          | 35        | 2019-08-16  |
| 2          | 65        | 2019-08-17  |
| 3          | 20        | 2019-08-18  |
+------------+-----------+-------------+
Output:
+------------+-------+
| product_id | price |
+------------+-------+
| 2          | 50    |
| 1          | 35    |
| 3          | 10    |
+------------+-------+

*/


/* 

->

找出每個 Product 在指定日期 2019-08-16 的價格。
如果該 Product 在指定日期之前沒有任何價格異動紀錄，預設價格為 10。

*/


# V0
# IDEA : LATEST CHANGE ON OR BEFORE THE DATE, LEFT JOIN, DEFAULT 10 (GPT)
WITH ranked AS (
    SELECT
        product_id,
        new_price,
        ROW_NUMBER() OVER (
            PARTITION BY product_id
            ORDER BY change_date DESC
        ) AS rn
    FROM Products
    WHERE change_date <= '2019-08-16'
),
all_products AS (
    SELECT DISTINCT product_id
    FROM Products
)
SELECT
    p.product_id,
    COALESCE(r.new_price, 10) AS price
FROM all_products p
LEFT JOIN ranked r
    ON p.product_id = r.product_id
    AND r.rn = 1;



# V0-0-1
# IDEA : LATEST CHANGE ON OR BEFORE THE DATE, LEFT JOIN, DEFAULT 10
#
#   last_change keeps, per product, the latest change_date <= 2019-08-16
#   (a change made ON the date counts). Every product is listed once, then
#   LEFT JOINed to that change and to the row holding its price. A product
#   whose changes all come after the date has no match, its new_price is
#   NULL, and COALESCE turns that into the initial price 10.
#
# time = O(n), space = O(n)    -- n = rows in Products; one group pass and two hash joins
WITH last_change AS (
    -- the latest change on or before the date, per product
    SELECT
        product_id,
        MAX(change_date) AS change_date
    FROM Products
    WHERE change_date <= '2019-08-16'
    GROUP BY product_id
),
all_products AS (
    SELECT DISTINCT product_id
    FROM Products
)
SELECT
    ap.product_id,
    COALESCE(p.new_price, 10) AS price   -- NULL = no change by the date: still the initial 10
FROM all_products ap
LEFT JOIN last_change lc               -- LEFT: keep products with no change by the date
    ON lc.product_id = ap.product_id
LEFT JOIN Products p
    ON p.product_id = lc.product_id
   AND p.change_date = lc.change_date;

# V1
# IDEA : UNION OF THE TWO CASES
#
#   Case 1: products changed by the date take the price of their latest such
#   change. Case 2: products never changed by the date are still at 10.
#   The two sets of products are disjoint, so UNION ALL loses nothing.
#   product_id is never NULL, so NOT IN is safe here.
#
# time = O(n), space = O(n)    -- n = rows in Products
SELECT
    product_id,
    new_price AS price
FROM Products
WHERE (product_id, change_date) IN (
    SELECT
        product_id,
        MAX(change_date)
    FROM Products
    WHERE change_date <= '2019-08-16'
    GROUP BY product_id
)
UNION ALL
SELECT DISTINCT
    product_id,
    10 AS price
FROM Products
WHERE product_id NOT IN (
    SELECT product_id
    FROM Products
    WHERE change_date <= '2019-08-16'
);

# V2
# IDEA : ROW_NUMBER OVER THE CHANGES BY THE DATE, NEWEST FIRST
#
#   Rank each product's changes on or before the date from newest to oldest;
#   rank 1 is the price on the date. ROW_NUMBER is safe because
#   (product_id, change_date) is the primary key, so there are no ties.
#   Products with no such change are added back through the LEFT JOIN.
#
# time = O(n log n), space = O(n)    -- n = rows in Products; the window sorts each product's dates
WITH ranked_changes AS (
    SELECT
        product_id,
        new_price,
        ROW_NUMBER() OVER (
            PARTITION BY product_id
            ORDER BY change_date DESC
        ) AS change_rank
    FROM Products
    WHERE change_date <= '2019-08-16'
),
all_products AS (
    SELECT DISTINCT product_id
    FROM Products
)
SELECT
    ap.product_id,
    COALESCE(rc.new_price, 10) AS price   -- NULL = no change by the date: still the initial 10
FROM all_products ap
LEFT JOIN ranked_changes rc
    ON rc.product_id = ap.product_id
   AND rc.change_rank = 1;               -- in ON, not WHERE: a WHERE would drop the unmatched products
