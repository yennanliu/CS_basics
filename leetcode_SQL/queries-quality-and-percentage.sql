/*

1211. Queries Quality and Percentage
https://leetcode.com/problems/queries-quality-and-percentage/
Easy

Table: Queries

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| query_name  | varchar |
| result      | varchar |
| position    | int     |
| rating      | int     |
+-------------+---------+
This table may have duplicate rows.
This table contains information collected from some queries on a database.
The position column has a value from 1 to 500.
The rating column has a value from 1 to 5. Query with rating less than 3 is a poor query.

We define query quality as:

The average of the ratio between query rating and its position.

We also define poor query percentage as:

The percentage of all queries with rating less than 3.

Write a solution to find each query_name, the quality and poor_query_percentage.

Both quality and poor_query_percentage should be rounded to 2 decimal places.

When rounding to two decimal places, values exactly halfway between two possible results are rounded away from zero. For example, 0.125 is rounded to 0.13.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Queries table:
+------------+-------------------+----------+--------+
| query_name | result            | position | rating |
+------------+-------------------+----------+--------+
| Dog        | Golden Retriever  | 1        | 5      |
| Dog        | German Shepherd   | 2        | 5      |
| Dog        | Mule              | 200      | 1      |
| Cat        | Shirazi           | 5        | 2      |
| Cat        | Siamese           | 3        | 3      |
| Cat        | Sphynx            | 7        | 4      |
+------------+-------------------+----------+--------+
Output:
+------------+---------+-----------------------+
| query_name | quality | poor_query_percentage |
+------------+---------+-----------------------+
| Dog        | 2.50    | 33.33                 |
| Cat        | 0.66    | 33.33                 |
+------------+---------+-----------------------+
Explanation:
Dog queries quality is ((5 / 1) + (5 / 2) + (1 / 200)) / 3 = 2.50
Dog queries poor_ query_percentage is (1 / 3) * 100 = 33.33

Cat queries quality equals ((2 / 5) + (3 / 3) + (4 / 7)) / 3 = 0.66
Cat queries poor_ query_percentage is (1 / 3) * 100 = 33.33

*/

# V0
# IDEA : TWO AGGREGATES PER query_name
#
#   quality is the average of rating / position, so AVG over the per-row ratio
#   (MySQL's / is decimal division, so 1 / 200 is 0.005, not 0).
#   poor_query_percentage counts rating < 3 with a CASE and divides by the
#   group's row count. The table may hold duplicate rows; each one counts.
#
# time = O(n), space = O(g)    -- n = rows in Queries, g = distinct query_name groups
SELECT
    query_name,
    ROUND(AVG(rating / position), 2) AS quality,
    ROUND(
        SUM(CASE WHEN rating < 3 THEN 1 ELSE 0 END) * 100 / COUNT(*),
        2
    ) AS poor_query_percentage
FROM Queries
WHERE query_name IS NOT NULL      -- GROUP BY would make NULL a group of its own; a nameless row is not a query
GROUP BY query_name;

# V1
# IDEA : A PERCENTAGE IS THE AVERAGE OF A 0 / 100 INDICATOR
#
#   Map each row to 100 when it is poor and 0 when not; the group's AVG of
#   that is the poor percentage directly, with no separate COUNT(*) divide.
#
# time = O(n), space = O(g)    -- n = rows in Queries, g = distinct query_name groups
SELECT
    query_name,
    ROUND(AVG(rating / position), 2) AS quality,
    ROUND(AVG(CASE WHEN rating < 3 THEN 100 ELSE 0 END), 2) AS poor_query_percentage
FROM Queries
WHERE query_name IS NOT NULL      -- same guard as V0
GROUP BY query_name;
