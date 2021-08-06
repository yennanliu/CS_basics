/*

Table: Friendship

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| user1_id      | int     |
| user2_id      | int     |
+---------------+---------+
(user1_id, user2_id) is the primary key for this table.
Each row of this table indicates that there is a friendship relation between user1_id and user2_id.
Table: Likes

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| user_id     | int     |
| page_id     | int     |
+-------------+---------+
(user_id, page_id) is the primary key for this table.
Each row of this table indicates that user_id likes page_id.
Write an SQL query to recommend pages to the user with user_id = 1 using the pages that your friends liked. It should not recommend pages you already liked.

Return result table in any order without duplicates.

The query result format is in the following example:

Friendship table:
+----------+----------+
| user1_id | user2_id |
+----------+----------+
| 1        | 2        |
| 1        | 3        |
| 1        | 4        |
| 2        | 3        |
| 2        | 4        |
| 2        | 5        |
| 6        | 1        |
+----------+----------+
 
Likes table:
+---------+---------+
| user_id | page_id |
+---------+---------+
| 1       | 88      |
| 2       | 23      |
| 3       | 24      |
| 4       | 56      |
| 5       | 11      |
| 6       | 33      |
| 2       | 77      |
| 3       | 77      |
| 6       | 88      |
+---------+---------+

Result table:
+------------------+
| recommended_page |
+------------------+
| 23               |
| 24               |
| 56               |
| 33               |
| 77               |
+------------------+
User one is friend with users 2, 3, 4 and 6.
Suggested pages are 23 from user 2, 24 from user 3, 56 from user 3 and 33 from user 6.
Page 77 is suggested from both user 2 and user 3.
Page 88 is not suggested because user 1 already likes it.

*/

# V0
SELECT DISTINCT page_id AS recommended_page 
FROM   likes l2 
WHERE  NOT EXISTS (SELECT *
                   FROM   likes l1 
                   WHERE  l1.user_id = 1 
                          AND l1.page_id = l2.page_id) 
       AND user_id IN (SELECT user2_id AS friend_id 
                       FROM   friendship 
                       WHERE  user1_id = 1 
                       UNION 
                       SELECT user1_id AS friend_id 
                       FROM   friendship 
                       WHERE  user2_id = 1) 
                       
# V0'
WITH user1_like AS
  (SELECT l.page_id
   FROM Friendship f
   INNER JOIN Likes l ON f.user1_id = l.user_id
   WHERE f.user2_id = '1' ),
     user2_like AS
  (SELECT l.page_id
   FROM Friendship f
   INNER JOIN Likes l ON f.user2_id = l.user_id
   WHERE f.user1_id = '1' )
SELECT DISTINCT page_id AS recommended_page
FROM
  (SELECT (page_id)
   FROM user1_like
   UNION ALL SELECT (page_id)
   FROM user2_like
   WHERE page_id NOT IN
       (SELECT (page_id)
        FROM Likes
        WHERE user_id = '1') ) sub

# V1
# https://code.dennyzhang.com/page-recommendations
select distinct page_id as recommended_page
from Likes
where user_id in (select user2_id
    from Friendship
    where user1_id=1
    union
    select user1_id
    from Friendship
    where user2_id=1)
    and page_id not in
        (select page_id
        from Likes
        where user_id=1)

# V2
# Time:  O(m + n)
# Space: O(m)
SELECT DISTINCT page_id AS recommended_page 
FROM   likes l2 
WHERE  NOT EXISTS (SELECT *
                   FROM   likes l1 
                   WHERE  l1.user_id = 1 
                          AND l1.page_id = l2.page_id) 
       AND user_id IN (SELECT user2_id AS friend_id 
                       FROM   friendship 
                       WHERE  user1_id = 1 
                       UNION 
                       SELECT user1_id AS friend_id 
                       FROM   friendship 
                       WHERE  user2_id = 1) 