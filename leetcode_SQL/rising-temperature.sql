/*

197. Rising Temperature
https://leetcode.com/problems/rising-temperature/
Easy

Table: Weather

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| id            | int     |
| recordDate    | date    |
| temperature   | int     |
+---------------+---------+
id is the column with unique values for this table.
There are no different rows with the same recordDate.
This table contains information about the temperature on a certain day.

Write a solution to find all dates' id with higher temperatures compared to its previous calendar dates (yesterday).

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Weather table:
+----+------------+-------------+
| id | recordDate | temperature |
+----+------------+-------------+
| 1  | 2015-01-01 | 10          |
| 2  | 2015-01-02 | 25          |
| 3  | 2015-01-03 | 20          |
| 4  | 2015-01-04 | 30          |
+----+------------+-------------+
Output:
+----+
| id |
+----+
| 2  |
| 4  |
+----+
Explanation:
In 2015-01-02, the temperature was higher than the previous day (10 -> 25).
In 2015-01-04, the temperature was higher than the previous day (20 -> 30).

*/

# V0
# IDEA : SELF JOIN EACH DAY TO THE CALENDAR DAY BEFORE IT
#
#   Pair every row (today) with the row whose recordDate is exactly one day
#   earlier (yesterday), then keep the pairs where today is warmer.
#   DATEDIFF compares calendar dates, so a missing day in the table yields no
#   pair -- "previous row" is not the same as "yesterday". Never compare
#   recordDate - 1 as a number: 2015-02-01 - 1 is not 2015-01-31.
#
# time = O(n), space = O(n)    -- n = rows in Weather; hash join on the date (O(n^2) as a nested loop)
SELECT
    today.id
FROM Weather AS today
JOIN Weather AS yesterday
    ON DATEDIFF(today.recordDate, yesterday.recordDate) = 1
WHERE today.temperature > yesterday.temperature;

# V1
# IDEA : LAG() OVER DATE ORDER, THEN CHECK THE GAP IS ONE DAY
#
#   Sort by date and read the previous row's date and temperature with LAG.
#   The previous row is only "yesterday" if the gap is exactly one day, so the
#   date check is still required. The first row has no previous row: LAG gives
#   NULL, DATEDIFF(..., NULL) is NULL, and WHERE drops it.
#
# time = O(n log n), space = O(n)    -- n = rows in Weather; the window sorts by recordDate
WITH with_previous AS (
    SELECT
        id,
        recordDate,
        temperature,
        LAG(recordDate) OVER (ORDER BY recordDate) AS prev_date,
        LAG(temperature) OVER (ORDER BY recordDate) AS prev_temperature
    FROM Weather
)
SELECT
    id
FROM with_previous
WHERE DATEDIFF(recordDate, prev_date) = 1   -- NULL for the first row, so it is dropped
  AND temperature > prev_temperature;
