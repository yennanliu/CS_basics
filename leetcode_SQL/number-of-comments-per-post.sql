/*

+---------------+----------+
| Column Name   | Type     |
+---------------+----------+
| sub_id        | int      |
| parent_id     | int      |
+---------------+----------+
There is no primary key for this table, it may have duplicate rows.
Each row can be a post or comment on the post.
parent_id is null for posts.
parent_id for comments is sub_id for another post in the table.
 

Write an SQL query to find number of comments per each post.

Result table should contain post_id and its corresponding number_of_comments, and must be sorted by post_id in ascending order.

Submissions may contain duplicate comments. You should count the number of unique comments per post.

Submissions may contain duplicate posts. You should treat them as one post.

The query result format is in the following example:

Submissions table:
+---------+------------+
| sub_id  | parent_id  |
+---------+------------+
| 1       | Null       |
| 2       | Null       |
| 1       | Null       |
| 12      | Null       |
| 3       | 1          |
| 5       | 2          |
| 3       | 1          |
| 4       | 1          |
| 9       | 1          |
| 10      | 2          |
| 6       | 7          |
+---------+------------+

Result table:
+---------+--------------------+
| post_id | number_of_comments |
+---------+--------------------+
| 1       | 3                  |
| 2       | 2                  |
| 12      | 0                  |
+---------+--------------------+

The post with id 1 has three comments in the table with id 3, 4 and 9. The comment with id 3 is repeated in the table, we counted it only once.
The post with id 2 has two comments in the table with id 5 and 10.
The post with id 12 has no comments in the table.
The comment with id 6 is a comment on a deleted post with id 7 so we ignored it.

*/

# V0
SELECT
s1.sub_id AS post_id,
NULLIF(COUNT(DISTINCT s2.sub_id), 0) AS number_of_comments
FROM
Submissions s1
LEFT JOIN
Submissions s2
ON s1.post_id = s2.parent_id
WHERE  a.parent_id IS NULL 
GROUP BY s1.sub_id
ORDER BY number_of_comments DESC;

# V1
# https://www.programmersought.com/article/11894736817/
select t1.sub_id as post_id,count(t2.parent_id) as number_of_comments from
(
    select distinct sub_id from Submissions s1
    where parent_id is null) t1 left join 
    (
        select distinct sub_id,parent_id from Submissions s2) t2 on t1.sub_id=t2.parent_id
        group by post_id
        order by post_id

# V2
# Time:  O(n)
# Space: O(n)
SELECT a.sub_id                 AS post_id, 
       Count(DISTINCT b.sub_id) AS number_of_comments 
FROM   submissions a 
       LEFT JOIN submissions b 
              ON a.sub_id = b.parent_id 
WHERE  a.parent_id IS NULL 
GROUP  BY a.sub_id 
ORDER  BY NULL 