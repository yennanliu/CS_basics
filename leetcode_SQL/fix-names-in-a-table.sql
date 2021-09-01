/*

Fix Names In A Table Problem

Description
LeetCode Problem 1667.

Table: Users

+----------------+---------+
| Column Name    | Type    |
+----------------+---------+
| user_id        | int     |
| name           | varchar |
+----------------+---------+
user_id is the primary key for this table.
This table contains the ID and the name of the user. The name consists of only lowercase and uppercase characters.
Write an SQL query to fix the names so that only the first character is uppercase and the rest are lowercase.

Return the result table ordered by user_id.

The query result format is in the following example:

Users table:
+---------+-------+
| user_id | name  |
+---------+-------+
| 1       | aLice |
| 2       | bOB   |
+---------+-------+

Result table:
+---------+-------+
| user_id | name  |
+---------+-------+
| 1       | Alice |
| 2       | Bob   |
+---------+-------+

*/

# V0
select user_id, 
    concat(upper(left(name,1)),lower(substring(name,2))) as name
from Users 
order by user_id

# V1
# https://circlecoder.com/fix-names-in-a-table/
select user_id, 
    concat(upper(left(name,1)),lower(substring(name,2))) as name
from Users 
order by user_id

# V2
# Time:  O(nlogn)
# Space: O(n)
SELECT user_id, CONCAT(UPPER(SUBSTRING(name, 1, 1)), LOWER(SUBSTRING(name, 2))) AS name
FROM Activity
ORDER BY user_id;