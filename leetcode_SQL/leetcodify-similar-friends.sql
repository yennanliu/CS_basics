/*

1919. Leetcodify Similar Friends
Level
Hard

Description
Table: Listens

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| user_id     | int     |
| song_id     | int     |
| day         | date    |
+-------------+---------+
There is no primary key for this table. It may contain duplicates.
Each row of this table indicates that the user user_id listened to the song song_id on the day day.
Table: Friendship

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| user1_id      | int     |
| user2_id      | int     |
+---------------+---------+
(user1_id, user2_id) is the primary key for this table.
Each row of this table indicates that the users user1_id and user2_id are friends.
Note that user1_id < user2_id.
Write an SQL query to report the similar friends of Leetcodify users. A user x and user y are similar friends if:

Users x and y are friends, and
Users x and y listened to the same three or more different songs on the same day.
Return the result table in any order. Note that you must return the similar pairs of friends the same way they were represented in the input (i.e., always user1_id < user2_id).

The query result format is in the following example:

Listens table:
+---------+---------+------------+
| user_id | song_id | day        |
+---------+---------+------------+
| 1       | 10      | 2021-03-15 |
| 1       | 11      | 2021-03-15 |
| 1       | 12      | 2021-03-15 |
| 2       | 10      | 2021-03-15 |
| 2       | 11      | 2021-03-15 |
| 2       | 12      | 2021-03-15 |
| 3       | 10      | 2021-03-15 |
| 3       | 11      | 2021-03-15 |
| 3       | 12      | 2021-03-15 |
| 4       | 10      | 2021-03-15 |
| 4       | 11      | 2021-03-15 |
| 4       | 13      | 2021-03-15 |
| 5       | 10      | 2021-03-16 |
| 5       | 11      | 2021-03-16 |
| 5       | 12      | 2021-03-16 |
+---------+---------+------------+

Friendship table:
+----------+----------+
| user1_id | user2_id |
+----------+----------+
| 1        | 2        |
| 2        | 4        |
| 2        | 5        |
+----------+----------+

Result table:
+----------+----------+
| user1_id | user2_id |
+----------+----------+
| 1        | 2        |
+----------+----------+

Users 1 and 2 are friends, and they listened to songs 10, 11, and 12 on the same day. They are similar friends.
Users 1 and 3 listened to songs 10, 11, and 12 on the same day, but they are not friends.
Users 2 and 4 are friends, but they did not listen to the same three different songs.
Users 2 and 5 are friends and listened to songs 10, 11, and 12, but they did not listen to them on the same day.

*/

# V0
SELECT DISTINCT a.user_id user1_id,
                b.user_id user2_id
FROM   friendship c
       INNER JOIN listens a ON c.user1_id = a.user_id
       INNER JOIN listens b ON c.user2_id = b.user_id AND a.day = b.day AND a.song_id = b.song_id
GROUP  BY c.user1_id,
          c.user2_id,
          a.day
HAVING Count(DISTINCT a.song_id) >= 3
ORDER  BY NULL;

# V1
# https://leetcode.ca/2021-08-02-1919-Leetcodify-Similar-Friends/
# Write your MySQL query statement below
select distinct user1_id, user2_id
    from (
        select user1_id, user2_id, day, count(song_id)
        from (
            select a.user_id as user1_id, b.user_id as user2_id, a.song_id, a.day
            from (
                select distinct * from Listens
            ) as a
            inner join (
                select distinct * from Listens
            ) as b
            on a.song_id = b.song_id and a.day = b.day and a.user_id != b.user_id
        ) as c
        group by user1_id, user2_id, day
        having count(song_id) >= 3
    ) as d
    inner join (
        select user1_id as u1, user2_id as u2
        from Friendship
        union all
        select user2_id as u1, user1_id as u2
        from Friendship
    ) as e
    on d.user1_id = e.u1 and d.user2_id = e.u2 and d.user1_id < d.user2_id;

# V2
# Time:  O(n * l), n is the number of users, l is the number of listens
# Space: O(n * l)
SELECT DISTINCT a.user_id user1_id,
                b.user_id user2_id
FROM   friendship c
       INNER JOIN listens a ON c.user1_id = a.user_id
       INNER JOIN listens b ON c.user2_id = b.user_id AND a.day = b.day AND a.song_id = b.song_id
GROUP  BY c.user1_id,
          c.user2_id,
          a.day
HAVING Count(DISTINCT a.song_id) >= 3
ORDER  BY NULL;