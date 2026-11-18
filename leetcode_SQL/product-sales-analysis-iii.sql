/*

1070. Product Sales Analysis III
https://leetcode.com/problems/product-sales-analysis-iii/
Medium

Table: Sales

+-------------+-------+
| Column Name | Type  |
+-------------+-------+
| sale_id     | int   |
| product_id  | int   |
| year        | int   |
| quantity    | int   |
| price       | int   |
+-------------+-------+
(sale_id, year) is the primary key (combination of columns with unique values) of this table.
Each row records a sale of a product in a given year.
A product may have multiple sales entries in the same year.
Note that the per-unit price.

Write a solution to find all sales that occurred in the first year each product was sold.

	For each product_id, identify the earliest year it appears in the Sales table.

	Return all sales entries for that product in that year.

Return a table with the following columns: product_id, first_year, quantity, and price.

Return the result in any order.

Example 1:

Input:
Sales table:
+---------+------------+------+----------+-------+
| sale_id | product_id | year | quantity | price |
+---------+------------+------+----------+-------+
| 1       | 100        | 2008 | 10       | 5000  |
| 2       | 100        | 2009 | 12       | 5000  |
| 7       | 200        | 2011 | 15       | 9000  |
+---------+------------+------+----------+-------+

Output:
+------------+------------+----------+-------+
| product_id | first_year | quantity | price |
+------------+------------+----------+-------+
| 100        | 2008       | 10       | 5000  |
| 200        | 2011       | 15       | 9000  |
+------------+------------+----------+-------+

*/

# V0
# IDEA : RANK EACH PRODUCT'S SALES BY YEAR, KEEP RANK 1
#
#   RANK() OVER (PARTITION BY product_id ORDER BY year) gives every sale in a
#   product's earliest year rank 1. RANK, not ROW_NUMBER: a product can have
#   several sales in its first year, and the problem wants all of them --
#   ROW_NUMBER would keep only one.
#
# time = O(n log n), space = O(n)    -- n = rows in Sales; the sort inside each partition sets it
WITH ranked_sales AS (
    SELECT
        product_id,
        year,
        quantity,
        price,
        RANK() OVER (PARTITION BY product_id ORDER BY year) AS year_rank   -- ties share rank 1
    FROM Sales
)
SELECT
    product_id,
    year AS first_year,
    quantity,
    price
FROM ranked_sales
WHERE year_rank = 1;


# V0-1
# IDEA: （Tuple IN 子查詢） (gpt)
SELECT 
    product_id,
    year AS first_year,
    quantity,
    price
FROM sales
WHERE (product_id, year) IN (
    SELECT 
        product_id, 
        MIN(year) AS year
    FROM sales
    GROUP BY product_id
);


# V1
# IDEA : FIRST YEAR PER PRODUCT IN A CTE, JOIN BACK TO SALES
#
#   GROUP BY product_id + MIN(year) finds each product's first year; joining
#   on (product_id, year) brings back every sale in that year, not just one.
#   (Selecting quantity and price next to MIN(year) in one GROUP BY is the
#   classic bug: MySQL rejects it under ONLY_FULL_GROUP_BY, and without that
#   mode it returns an arbitrary row's values, not the first year's.)
#
# time = O(n), space = O(p)    -- n = rows in Sales, p = distinct products; hash group, then hash join
WITH first_year AS (
    SELECT
        product_id,
        MIN(year) AS first_year
    FROM Sales
    GROUP BY product_id
)
SELECT
    s.product_id,
    f.first_year,
    s.quantity,
    s.price
FROM Sales AS s
JOIN first_year AS f
    ON s.product_id = f.product_id
   AND s.year = f.first_year;
