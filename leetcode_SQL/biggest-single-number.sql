/*

619. Biggest Single Number
https://leetcode.com/problems/biggest-single-number/
Easy

Table: MyNumbers

+-------------+------+
| Column Name | Type |
+-------------+------+
| num         | int  |
+-------------+------+
This table may contain duplicates (In other words, there is no primary key for this table in SQL).
Each row of this table contains an integer.

A single number is a number that appeared only once in the MyNumbers table.

Find the largest single number. If there is no single number, report null.

The result format is in the following example.

Example 1:

Input:
MyNumbers table:
+-----+
| num |
+-----+
| 8   |
| 8   |
| 3   |
| 3   |
| 1   |
| 4   |
| 5   |
| 6   |
+-----+
Output:
+-----+
| num |
+-----+
| 6   |
+-----+
Explanation: The single numbers are 1, 4, 5, and 6.
Since 6 is the largest single number, we return it.

*/

# V0
# IDEA : SINGLES IN A CTE, THEN MAX OVER THEM
#
#   GROUP BY num + HAVING COUNT(*) = 1 keeps the numbers that appear once.
#   MAX() is an aggregate with no GROUP BY, so it always returns exactly one
#   row -- and over an empty set that row is NULL, which is exactly the
#   "report null" the problem asks for when there is no single number.
#
# time = O(n), space = O(n)    -- n = rows in MyNumbers; one grouping pass, then one scan of the singles
WITH single_numbers AS (
    SELECT num
    FROM MyNumbers
    GROUP BY num
    HAVING COUNT(*) = 1
)
SELECT MAX(num) AS num   -- empty CTE -> one row holding NULL
FROM single_numbers;

# V1
# IDEA : ORDER THE SINGLES DESC, TAKE ONE, WRAPPED AS A SCALAR SUBQUERY
#
#   ORDER BY num DESC LIMIT 1 picks the largest single, but on its own it
#   returns zero rows when there is none. Wrapping it as a scalar subquery
#   in the SELECT list turns "no row" into NULL, so the result always has
#   one row.
#
# time = O(n log n), space = O(n)    -- n = rows in MyNumbers; the sort of the singles sets it
SELECT (
    SELECT num
    FROM MyNumbers
    GROUP BY num
    HAVING COUNT(*) = 1
    ORDER BY num DESC
    LIMIT 1
) AS num;   -- scalar subquery: no row -> NULL
