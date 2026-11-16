/*

610. Triangle Judgement
https://leetcode.com/problems/triangle-judgement/
Easy

Table: Triangle

+-------------+------+
| Column Name | Type |
+-------------+------+
| x           | int  |
| y           | int  |
| z           | int  |
+-------------+------+
In SQL, (x, y, z) is the primary key column for this table.
Each row of this table contains the lengths of three line segments.

Report for every three line segments whether they can form a triangle.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Triangle table:
+----+----+----+
| x  | y  | z  |
+----+----+----+
| 13 | 15 | 30 |
| 10 | 20 | 15 |
+----+----+----+
Output:
+----+----+----+----------+
| x  | y  | z  | triangle |
+----+----+----+----------+
| 13 | 15 | 30 | No       |
| 10 | 20 | 15 | Yes      |
+----+----+----+----------+

*/

# V0
# IDEA : TRIANGLE INEQUALITY IN A CASE WHEN
#
#   Three segments form a triangle exactly when every pair sums to strictly
#   more than the third. Equality (10 + 20 = 30) is a flat line, not a
#   triangle, so the comparison is > rather than >=. Every row gets a label,
#   so there is no WHERE.
#
# time = O(n), space = O(n)    -- n = rows in Triangle; one scan, one output row each
SELECT
    x,
    y,
    z,
    CASE
        WHEN x + y > z
         AND x + z > y
         AND y + z > x
        THEN 'Yes'
        ELSE 'No'
    END AS triangle
FROM Triangle;
