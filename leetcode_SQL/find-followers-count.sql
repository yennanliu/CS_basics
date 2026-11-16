/*

1729. Find Followers Count
https://leetcode.com/problems/find-followers-count/
Easy

Table: Followers

+-------------+------+
| Column Name | Type |
+-------------+------+
| user_id     | int  |
| follower_id | int  |
+-------------+------+
(user_id, follower_id) is the primary key (combination of columns with unique values) for this table.
This table contains the IDs of a user and a follower in a social media app where the follower follows the user.

Write a solution that will, for each user, return the number of followers.

Return the result table ordered by user_id in ascending order.

The result format is in the following example.

Example 1:

Input:
Followers table:
+---------+-------------+
| user_id | follower_id |
+---------+-------------+
| 0       | 1           |
| 1       | 0           |
| 2       | 0           |
| 2       | 1           |
+---------+-------------+
Output:
+---------+----------------+
| user_id | followers_count|
+---------+----------------+
| 0       | 1              |
| 1       | 1              |
| 2       | 2              |
+---------+----------------+
Explanation:
The followers of 0 are {1}
The followers of 1 are {0}
The followers of 2 are {0,1}

*/

# V0
# IDEA : GROUP BY USER, COUNT FOLLOWERS, ORDER BY USER
#
#   One row per (user, follower) pair, so counting a user's rows counts their
#   followers. (user_id, follower_id) is the primary key, so no pair repeats
#   and COUNT(follower_id) needs no DISTINCT. The problem asks for user_id
#   ascending, so the ORDER BY is part of the answer.
#
# time = O(n + u log u), space = O(u)    -- n = rows in Followers, u = distinct users; hash group, then sort the u groups
SELECT
    user_id,
    COUNT(follower_id) AS followers_count
FROM Followers
GROUP BY user_id
ORDER BY user_id;
