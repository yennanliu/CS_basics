/*

595. Big Countries
https://leetcode.com/problems/big-countries/
Easy

Table: World

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| name        | varchar |
| continent   | varchar |
| area        | int     |
| population  | int     |
| gdp         | bigint  |
+-------------+---------+
name is the primary key (column with unique values) for this table.
Each row of this table gives information about the name of a country, the continent to which it belongs, its area, the population, and its GDP value.

A country is big if:

	- it has an area of at least three million (i.e., 3000000 km2), or

	- it has a population of at least twenty-five million (i.e., 25000000).

Write a solution to find the name, population, and area of the big countries.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
World table:
+-------------+-----------+---------+------------+--------------+
| name        | continent | area    | population | gdp          |
+-------------+-----------+---------+------------+--------------+
| Afghanistan | Asia      | 652230  | 25500100   | 20343000000  |
| Albania     | Europe    | 28748   | 2831741    | 12960000000  |
| Algeria     | Africa    | 2381741 | 37100000   | 188681000000 |
| Andorra     | Europe    | 468     | 78115      | 3712000000   |
| Angola      | Africa    | 1246700 | 20609294   | 100990000000 |
+-------------+-----------+---------+------------+--------------+
Output:
+-------------+------------+---------+
| name        | population | area    |
+-------------+------------+---------+
| Afghanistan | 25500100   | 652230  |
| Algeria     | 37100000   | 2381741 |
+-------------+------------+---------+

*/

# V0
# IDEA : FILTER WITH OR, INCLUSIVE BOUNDS
#
#   A country is big if either condition holds, so one scan with OR.
#   "At least" means >=: a country with exactly 3000000 km2 or exactly
#   25000000 people is big.
#
# time = O(n), space = O(n)    -- n = rows in World; one scan, up to n rows returned
SELECT
    name,
    population,
    area
FROM World
WHERE area >= 3000000
   OR population >= 25000000;

# V1
# IDEA : UNION OF TWO FILTERS
#
#   One SELECT per condition, then UNION. UNION (not UNION ALL) removes the
#   duplicate when a country meets both conditions; name is the primary key, so
#   no two distinct countries collapse into one row. Worth knowing because on a
#   table with an index on each column, two index range scans can beat one
#   full scan with OR.
#
# time = O(n), space = O(n)    -- two scans of World, plus the dedup of up to n rows
SELECT
    name,
    population,
    area
FROM World
WHERE area >= 3000000

UNION

SELECT
    name,
    population,
    area
FROM World
WHERE population >= 25000000;
