/*

Table: Friends

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| id            | int     |
| name          | varchar |
| activity      | varchar |
+---------------+---------+
id is the id of the friend and primary key for this table.
name is the name of the friend.
activity is the name of the activity which the friend takes part in.
Table: Activities

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| id            | int     |
| name          | varchar |
+---------------+---------+
id is the primary key for this table.
name is the name of the activity.
Write an SQL query to find the names of all the activities with neither maximum, nor minimum number of participants.

Return the result table in any order. Each activity in table Activities is performed by any person in the table Friends.

The query result format is in the following example:

Friends table:

+------+--------------+---------------+
| id   | name         | activity      |
+------+--------------+---------------+
| 1    | Jonathan D.  | Eating        |
| 2    | Jade W.      | Singing       |
| 3    | Victor J.    | Singing       |
| 4    | Elvis Q.     | Eating        |
| 5    | Daniel A.    | Eating        |
| 6    | Bob B.       | Horse Riding  |
+------+--------------+---------------+

Activities table:
+------+--------------+
| id   | name         |
+------+--------------+
| 1    | Eating       |
| 2    | Singing      |
| 3    | Horse Riding |
+------+--------------+

Result table:
+--------------+
| results      |
+--------------+
| Singing      |
+--------------+

Eating activity is performed by 3 friends, maximum number of participants, (Jonathan D. , Elvis Q. and Daniel A.)
Horse Riding activity is performed by 1 friend, minimum number of participants, (Bob B.)
Singing is performed by 2 friends (Victor J. and Jade W.)

*/

# V0
WITH cte AS
(Select activity, count(*) AS cnt FROM Friends GROUP BY activity)
SELECT
activity
FROM
cte
WHERE 
cnt NOT IN (SELECT max(cnt) FROM cte)
AND
cnt NOT IN (SELECT min(cnt) FROM cte)

# V1
# https://code.dennyzhang.com/activity-participants
with cte as
(Select count(*) as cnt, activity from Friends group by activity)

Select activity from cte
where cnt not in 
    (Select max(cnt) from cte
    union all
    Select min(cnt) from cte)

# V2
# Time:  O(n) 
# Space: O(n) 
SELECT activity 
FROM   friends 
GROUP  BY activity 
HAVING Count(1) NOT IN (SELECT Max(counts) 
                        FROM   (SELECT Count(1) AS counts 
                                FROM   friends 
                                GROUP  BY activity 
                                ORDER  BY NULL) a 
                        UNION ALL 
                        SELECT Min(counts) 
                        FROM   (SELECT Count(1) AS counts 
                                FROM   friends 
                                GROUP  BY activity 
                                ORDER  BY NULL) b) 
ORDER  BY NULL; 