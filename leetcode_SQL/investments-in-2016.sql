/*

585. Investments in 2016
https://leetcode.com/problems/investments-in-2016/
Medium

Table: Insurance

+-------------+-------+
| Column Name | Type  |
+-------------+-------+
| pid         | int   |
| tiv_2015    | float |
| tiv_2016    | float |
| lat         | float |
| lon         | float |
+-------------+-------+
pid is the primary key (column with unique values) for this table.
Each row of this table contains information about one policy where:
pid is the policyholder's policy ID.
tiv_2015 is the total investment value in 2015 and tiv_2016 is the total investment value in 2016.
lat is the latitude of the policy holder's city. It's guaranteed that lat is not NULL.
lon is the longitude of the policy holder's city. It's guaranteed that lon is not NULL.

Write a solution to report the sum of all total investment values in 2016 tiv_2016, for all policyholders who:

	- have the same tiv_2015 value as one or more other policyholders, and

	- are not located in the same city as any other policyholder (i.e., the (lat, lon) attribute pairs must be unique).

Round tiv_2016 to two decimal places.

The result format is in the following example.

Example 1:

Input:
Insurance table:
+-----+----------+----------+-----+-----+
| pid | tiv_2015 | tiv_2016 | lat | lon |
+-----+----------+----------+-----+-----+
| 1   | 10       | 5        | 10  | 10  |
| 2   | 20       | 20       | 20  | 20  |
| 3   | 10       | 30       | 20  | 20  |
| 4   | 10       | 40       | 40  | 40  |
+-----+----------+----------+-----+-----+
Output:
+----------+
| tiv_2016 |
+----------+
| 45.00    |
+----------+
Explanation:
The first record in the table, like the last record, meets both of the two criteria.
The tiv_2015 value 10 is the same as the third and fourth records, and its location is unique.

The second record does not meet any of the two criteria. Its tiv_2015 is not like any other policyholders and its location is the same as the third record, which makes the third record fail, too.
So, the result is the sum of tiv_2016 of the first and last record, which is 45.

*/

# V0
# IDEA : WINDOW COUNTS PER GROUP, THEN FILTER
#
#   Two conditions, each a group size: how many policies share my tiv_2015
#   (must be > 1), and how many share my (lat, lon) (must be exactly 1).
#   COUNT(*) OVER (PARTITION BY ...) puts both sizes on every row without
#   collapsing it, so one outer WHERE checks both. lat / lon are guaranteed
#   NOT NULL, so no NULL lands in a location partition.
#
# time = O(n log n), space = O(n)    -- n = rows in Insurance; each window partitions (sorts) n rows
WITH counted AS (
    SELECT
        tiv_2016,
        COUNT(*) OVER (PARTITION BY tiv_2015) AS same_tiv_2015,
        COUNT(*) OVER (PARTITION BY lat, lon) AS same_location
    FROM Insurance
)
SELECT ROUND(SUM(tiv_2016), 2) AS tiv_2016
FROM counted
WHERE same_tiv_2015 > 1      -- shares tiv_2015 with at least one other policy
  AND same_location = 1;     -- no other policy in the same city

# V1
# IDEA : IN AGAINST TWO GROUP BY / HAVING SUBQUERIES
#
#   The same two group sizes, written as sets: the tiv_2015 values that occur
#   more than once, and the (lat, lon) pairs that occur exactly once. A row
#   qualifies when its value is in the first set and its pair in the second.
#   Neither subquery can return a NULL here, so IN behaves.
#
# time = O(n log n), space = O(n)    -- n = rows in Insurance; two GROUP BYs, then one scan
SELECT ROUND(SUM(tiv_2016), 2) AS tiv_2016
FROM Insurance
WHERE tiv_2015 IN (
          -- tiv_2015 values shared by two or more policies
          SELECT tiv_2015
          FROM Insurance
          GROUP BY tiv_2015
          HAVING COUNT(*) > 1
      )
  AND (lat, lon) IN (
          -- locations held by exactly one policy
          SELECT lat, lon
          FROM Insurance
          GROUP BY lat, lon
          HAVING COUNT(*) = 1
      );
