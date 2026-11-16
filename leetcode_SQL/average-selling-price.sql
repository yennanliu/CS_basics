/*

1251. Average Selling Price
https://leetcode.com/problems/average-selling-price/
Easy

Table: Prices

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| product_id    | int     |
| start_date    | date    |
| end_date      | date    |
| price         | int     |
+---------------+---------+
(product_id, start_date, end_date) is the primary key (combination of columns with unique values) for this table.
Each row of this table indicates the price of the product_id in the period from start_date to end_date.
For each product_id there will be no two overlapping periods. That means there will be no two intersecting periods for the same product_id.

Table: UnitsSold

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| product_id    | int     |
| purchase_date | date    |
| units         | int     |
+---------------+---------+
This table may contain duplicate rows.
Each row of this table indicates the date, units, and product_id of each product sold.

Write a solution to find the average selling price for each product. average_price should be rounded to 2 decimal places. If a product does not have any sold units, its average selling price is assumed to be 0.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Prices table:
+------------+------------+------------+--------+
| product_id | start_date | end_date   | price  |
+------------+------------+------------+--------+
| 1          | 2019-02-17 | 2019-02-28 | 5      |
| 1          | 2019-03-01 | 2019-03-22 | 20     |
| 2          | 2019-02-01 | 2019-02-20 | 15     |
| 2          | 2019-02-21 | 2019-03-31 | 30     |
+------------+------------+------------+--------+
UnitsSold table:
+------------+---------------+-------+
| product_id | purchase_date | units |
+------------+---------------+-------+
| 1          | 2019-02-25    | 100   |
| 1          | 2019-03-01    | 15    |
| 2          | 2019-02-10    | 200   |
| 2          | 2019-03-22    | 30    |
+------------+---------------+-------+
Output:
+------------+---------------+
| product_id | average_price |
+------------+---------------+
| 1          | 6.96          |
| 2          | 16.96         |
+------------+---------------+
Explanation:
Average selling price = Total Price of Product / Number of products sold.
Average selling price for product 1 = ((100 * 5) + (15 * 20)) / 115 = 6.96
Average selling price for product 2 = ((200 * 15) + (30 * 30)) / 230 = 16.96

*/

# V0
# IDEA : LEFT JOIN ON THE PRICE PERIOD, WEIGHTED AVERAGE, NULL -> 0
#
#   Each sale is priced by the one Prices row whose period contains its
#   purchase_date, so the date range belongs in the JOIN condition.
#   Average price = SUM(units * price) / SUM(units), a weighted average --
#   not AVG(price), which would ignore how many units sold at each price.
#   LEFT JOIN from Prices keeps a product that never sold; its SUMs are NULL,
#   NULL / NULL is NULL, and COALESCE turns that into the required 0.
#
# time = O(n + m), space = O(n + m)    -- n = rows in Prices, m = rows in UnitsSold; hash join on product_id, one group per product
SELECT
    p.product_id,
    COALESCE(ROUND(SUM(u.units * p.price) / SUM(u.units), 2), 0) AS average_price
FROM Prices AS p
LEFT JOIN UnitsSold AS u
    ON  u.product_id = p.product_id
    -- the range goes in ON, not WHERE: a WHERE on u would drop the unsold
    -- products and turn this back into an inner join
    AND u.purchase_date BETWEEN p.start_date AND p.end_date
GROUP BY p.product_id;
