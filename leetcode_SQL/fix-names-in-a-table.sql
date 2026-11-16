/*

1667. Fix Names in a Table
https://leetcode.com/problems/fix-names-in-a-table/
Easy

Table: Users

+----------------+---------+
| Column Name    | Type    |
+----------------+---------+
| user_id        | int     |
| name           | varchar |
+----------------+---------+
user_id is the primary key (column with unique values) for this table.
This table contains the ID and the name of the user. The name consists of only lowercase and uppercase characters.

Write a solution to fix the names so that only the first character is uppercase and the rest are lowercase.

Return the result table ordered by user_id.

The result format is in the following example.

Example 1:

Input:
Users table:
+---------+-------+
| user_id | name  |
+---------+-------+
| 1       | aLice |
| 2       | bOB   |
+---------+-------+
Output:
+---------+-------+
| user_id | name  |
+---------+-------+
| 1       | Alice |
| 2       | Bob   |
+---------+-------+

*/

# V0
# IDEA : UPPER THE FIRST CHARACTER, LOWER THE REST, CONCAT
#
#   LEFT(name, 1) is the first character and SUBSTRING(name, 2) is everything
#   after it (SUBSTRING is 1-based, and on a one-letter name it returns '').
#   UPPER / LOWER each half, then glue them back with CONCAT. The alias must be
#   `name` again, because LeetCode compares column names.
#
# time = O(n log n), space = O(n)    -- n = rows in Users; one scan, then the sort for ORDER BY user_id
SELECT
    user_id,
    CONCAT(UPPER(LEFT(name, 1)), LOWER(SUBSTRING(name, 2))) AS name
FROM Users
ORDER BY user_id;
