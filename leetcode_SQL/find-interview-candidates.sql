/*

LeetCode Problem 1811.

Table: Contests

+--------------+------+
| Column Name  | Type |
+--------------+------+
| contest_id   | int  |
| gold_medal   | int  |
| silver_medal | int  |
| bronze_medal | int  |
+--------------+------+
contest_id is the primary key for this table.
This table contains the LeetCode contest ID and the user IDs of the gold, silver, and bronze medalists.
It is guaranteed that any consecutive contests have consecutive IDs and that no ID is skipped.
Table: Users

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| user_id     | int     |
| mail        | varchar |
| name        | varchar |
+-------------+---------+
user_id is the primary key for this table.
This table contains information about the users.
Write an SQL query to report the name and the mail of all interview candidates. A user is an interview candidate if at least one of these two conditions is true:

The user won any medal in three or more consecutive contests. The user won the gold medal in three or more different contests (not necessarily consecutive). Return the result table in any order.

The query result format is in the following example:

Contests table:
+------------+------------+--------------+--------------+
| contest_id | gold_medal | silver_medal | bronze_medal |
+------------+------------+--------------+--------------+
| 190        | 1          | 5            | 2            |
| 191        | 2          | 3            | 5            |
| 192        | 5          | 2            | 3            |
| 193        | 1          | 3            | 5            |
| 194        | 4          | 5            | 2            |
| 195        | 4          | 2            | 1            |
| 196        | 1          | 5            | 2            |
+------------+------------+--------------+--------------+

Users table:
+---------+--------------------+-------+
| user_id | mail               | name  |
+---------+--------------------+-------+
| 1       | sarah@leetcode.com | Sarah |
| 2       | bob@leetcode.com   | Bob   |
| 3       | alice@leetcode.com | Alice |
| 4       | hercy@leetcode.com | Hercy |
| 5       | quarz@leetcode.com | Quarz |
+---------+--------------------+-------+

Result table:
+-------+--------------------+
| name  | mail               |
+-------+--------------------+
| Sarah | sarah@leetcode.com |
| Bob   | bob@leetcode.com   |
| Alice | alice@leetcode.com |
| Quarz | quarz@leetcode.com |
+-------+--------------------+

Sarah won 3 gold medals (190, 193, and 196), so we include her in the result table.
Bob won a medal in 3 consecutive contests (190, 191, and 192), so we include him in the result table.
    - Note that he also won a medal in 3 other consecutive contests (194, 195, and 196).
Alice won a medal in 3 consecutive contests (191, 192, and 193), so we include her in the result table.
Quarz won a medal in 5 consecutive contests (190, 191, 192, 193, and 194), so we include them in the result table.

*/

# V0
select name, mail
from Users
where user_id in (
    select gold_medal as user_id
    from Contests
    group by gold_medal
    having count(gold_medal) >= 3
    union all
    (with contest_and_medal as (
        select contest_id, gold_medal as user_id
        from Contests
        union all
        select contest_id, silver_medal as user_id
        from Contests
        union all
        select contest_id, bronze_medal as user_id
        from Contests)
     select distinct c1.user_id
     from contest_and_medal c1,
          contest_and_medal c2,
          contest_and_medal c3
     where c1.contest_id - c2.contest_id = 1
       and c2.contest_id - c3.contest_id = 1
       and c1.user_id = c2.user_id
       and c1.user_id = c3.user_id))

# V1
# https://circlecoder.com/find-interview-candidates/
select name, mail
from Users
where user_id in (
    select gold_medal as user_id
    from Contests
    group by gold_medal
    having count(gold_medal) >= 3
    union all
    (with contest_and_medal as (
        select contest_id, gold_medal as user_id
        from Contests
        union all
        select contest_id, silver_medal as user_id
        from Contests
        union all
        select contest_id, bronze_medal as user_id
        from Contests)
     select distinct c1.user_id
     from contest_and_medal c1,
          contest_and_medal c2,
          contest_and_medal c3
     where c1.contest_id - c2.contest_id = 1
       and c2.contest_id - c3.contest_id = 1
       and c1.user_id = c2.user_id
       and c1.user_id = c3.user_id))

# V2
# Time:  O(nlogn)
# Space: O(n)
WITH winners_cte AS
  ((SELECT gold_medal AS winner, contest_id
    FROM contests)
   UNION ALL
   (SELECT silver_medal AS winner, contest_id
    FROM contests)
   UNION ALL
   (SELECT bronze_medal AS winner, contest_id
    FROM contests)),
     consecutive_winners_cte AS
  (SELECT winner, contest_id, row_number() OVER (PARTITION BY winner ORDER BY contest_id) AS row_num
   FROM winners_cte),
     candidates_cte AS
  ((SELECT winner AS user_id
    FROM consecutive_winners_cte
    GROUP BY winner, contest_id - row_num
    HAVING count(1) >= 3
    ORDER BY NULL)
   UNION
   (SELECT gold_medal AS user_id
    FROM contests
    GROUP BY gold_medal
    HAVING count(1) >= 3
    ORDER BY NULL))
  
SELECT u.name, u.mail
FROM users u
INNER JOIN candidates_cte c ON u.user_id = c.user_id;