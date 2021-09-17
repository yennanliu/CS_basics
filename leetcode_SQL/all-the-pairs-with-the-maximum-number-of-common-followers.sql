/*

1951. All the Pairs With the Maximum Number of Common Followers
Level
Medium

Description
Table: Relations

+-------------+------+
| Column Name | Type |
+-------------+------+
| user_id     | int  |
| follower_id | int  |
+-------------+------+
(user_id, follower_id) is the primary key for this table.
Each row of this table indicates that the user with ID follower_id is following the user with ID user_id.
Write an SQL query to find all the pairs of users with the maximum number of common followers. In other words, if the maximum number of common followers between any two users is maxCommon, then you have to return all pairs of users that have maxCommon common followers.

The result table should contain the pairs user1_id and user2_id where user1_id < user2_id.

Return the result table in any order.

The query result format is in the following example:

Relations table:
+---------+-------------+
| user_id | follower_id |
+---------+-------------+
| 1       | 3           |
| 2       | 3           |
| 7       | 3           |
| 1       | 4           |
| 2       | 4           |
| 7       | 4           |
| 1       | 5           |
| 2       | 6           |
| 7       | 5           |
+---------+-------------+

Result table:
+----------+----------+
| user1_id | user2_id |
+----------+----------+
| 1        | 7        |
+----------+----------+

Users 1 and 2 have 2 common followers (3 and 4).
Users 1 and 7 have 3 common followers (3, 4, and 5).
Users 2 and 7 have 2 common followers (3 and 4).
Since the maximum number of common followers between any two users is 3, we return all pairs of users with 3 common followers, which is only the pair (1, 7). We return the pair as (1, 7), not as (7, 1).
Note that we do not have any information about the users that follow users 3, 4, and 5, so we consider them to have 0 followers.

*/

# V0
WITH relation_cte AS
(
    SELECT  a.user_id AS user1_id, 
            b.user_id AS user2_id,
            RANK() OVER(ORDER BY COUNT(*) DESC) AS rnk
    FROM Relations a
    INNER JOIN Relations b
    ON a.follower_id = b.follower_id
    AND a.user_id < b.user_id 
    GROUP BY a.user_id, b.user_id
)
       
SELECT user1_id, user2_id
FROM relation_cte
WHERE rnk = 1;

# V1
# https://leetcode.ca/2021-08-11-1951-All-the-Pairs-With-the-Maximum-Number-of-Common-Followers/
# Write your MySQL query statement below
with r as (select r1.user_id as user1_id, r2.user_id as user2_id, count(*) as cnt
    from Relations r1 join Relations r2 using (follower_id)
    where r1.user_id < r2.user_id
    group by r1.user_id, r2.user_id)
select user1_id, user2_id from r
    where cnt = (select max(cnt) from r);

# V2
# Time:  O(n^3), n is the number of users
# Space: O(n^2)
WITH relation_cte AS
(
    SELECT  a.user_id AS user1_id, 
            b.user_id AS user2_id,
            RANK() OVER(ORDER BY COUNT(*) DESC) AS rnk
    FROM Relations a
    INNER JOIN Relations b
    ON a.follower_id = b.follower_id
    AND a.user_id < b.user_id 
    GROUP BY a.user_id, b.user_id
)
       
SELECT user1_id, user2_id
FROM relation_cte
WHERE rnk = 1;