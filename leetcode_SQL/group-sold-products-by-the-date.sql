/*

1484. Group Sold Products By The Date
https://leetcode.com/problems/group-sold-products-by-the-date/
Easy

Table: Activities

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| sell_date   | date    |
| product     | varchar |
+-------------+---------+
There is no primary key (column with unique values) for this table. It may contain duplicates.
Each row of this table contains the product name and the date it was sold in a market.

Write a solution to find for each date the number of different products sold and their names.

The sold products names for each date should be sorted lexicographically.

Return the result table ordered by sell_date.

The result format is in the following example.

Example 1:

Input:
Activities table:
+------------+------------+
| sell_date  | product     |
+------------+------------+
| 2020-05-30 | Headphone  |
| 2020-06-01 | Pencil     |
| 2020-06-02 | Mask       |
| 2020-05-30 | Basketball |
| 2020-06-01 | Bible      |
| 2020-06-02 | Mask       |
| 2020-05-30 | T-Shirt    |
+------------+------------+
Output:
+------------+----------+------------------------------+
| sell_date  | num_sold | products                     |
+------------+----------+------------------------------+
| 2020-05-30 | 3        | Basketball,Headphone,T-shirt |
| 2020-06-01 | 2        | Bible,Pencil                 |
| 2020-06-02 | 1        | Mask                         |
+------------+----------+------------------------------+
Explanation:
For 2020-05-30, Sold items were (Headphone, Basketball, T-shirt), we sort them lexicographically and separate them by a comma.
For 2020-06-01, Sold items were (Pencil, Bible), we sort them lexicographically and separate them by a comma.
For 2020-06-02, the Sold item is (Mask), we just return it.

(Note: LeetCode's own example spells the product 'T-Shirt' in the input and
'T-shirt' in the output. The query returns the stored value, so the local
smoke test reports a FAIL on that one cell; the grids are left as LeetCode
publishes them.)

*/

# V0
# IDEA : GROUP BY DATE, COUNT(DISTINCT) + GROUP_CONCAT(DISTINCT ... ORDER BY)
#
#   One row per sell_date. The table may repeat a (date, product) pair (Mask
#   twice on 2020-06-02), so both the count and the list need DISTINCT.
#   GROUP_CONCAT's own ORDER BY sorts the names inside each group, and
#   SEPARATOR ',' (MySQL's default, written out) joins them with no space.
#   SQLite rejects the SEPARATOR keyword, so the smoke test reports V0 as
#   SKIP; V1 is the same plan in portable syntax.
#
# time = O(n log n), space = O(n)    -- n = rows in Activities; the grouping and the per-date sort of names
SELECT
    sell_date,
    COUNT(DISTINCT product) AS num_sold,
    GROUP_CONCAT(DISTINCT product ORDER BY product SEPARATOR ',') AS products
FROM Activities
GROUP BY sell_date
ORDER BY sell_date;

# V1
# IDEA : DE-DUPLICATE FIRST IN A CTE, THEN COUNT(*) AND GROUP_CONCAT
#
#   Removing the duplicate (date, product) rows up front means each group
#   holds every product once, so COUNT(*) is the distinct count and the
#   concat needs no DISTINCT. GROUP_CONCAT(... ORDER BY ...) without
#   SEPARATOR runs on MySQL (default ',') and on SQLite 3.44+.
#
# time = O(n log n), space = O(n)    -- n = rows in Activities; the DISTINCT, the grouping and the per-date sort
WITH daily_products AS (
    -- each product once per day
    SELECT DISTINCT
        sell_date,
        product
    FROM Activities
)
SELECT
    sell_date,
    COUNT(*) AS num_sold,
    GROUP_CONCAT(product ORDER BY product) AS products
FROM daily_products
GROUP BY sell_date
ORDER BY sell_date;
