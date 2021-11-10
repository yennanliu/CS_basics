/*

LeetCode Problem 1194.

Table: Players

+-------------+-------+
| Column Name | Type  |
+-------------+-------+
| player_id   | int   |
| group_id    | int   |
+-------------+-------+
player_id is the primary key of this table.
Each row of this table indicates the group of each player.
Table: Matches

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| match_id      | int     |
| first_player  | int     |
| second_player | int     | 
| first_score   | int     |
| second_score  | int     |
+---------------+---------+

match_id is the primary key of this table.
Each row is a record of a match, first_player and second_player contain the player_id of each match.
first_score and second_score contain the number of points of the first_player and second_player respectively.
You may assume that, in each match, players belongs to the same group.
The winner in each group is the player who scored the maximum total points within the group. In the case of a tie, the lowest player_id wins.

Write an SQL query to find the winner in each group.

The query result format is in the following example:

Players table:
+-----------+------------+
| player_id | group_id   |
+-----------+------------+
| 15        | 1          |
| 25        | 1          |
| 30        | 1          |
| 45        | 1          |
| 10        | 2          |
| 35        | 2          |
| 50        | 2          |
| 20        | 3          |
| 40        | 3          |
+-----------+------------+

Matches table:
+------------+--------------+---------------+-------------+--------------+
| match_id   | first_player | second_player | first_score | second_score |
+------------+--------------+---------------+-------------+--------------+
| 1          | 15           | 45            | 3           | 0            |
| 2          | 30           | 25            | 1           | 2            |
| 3          | 30           | 15            | 2           | 0            |
| 4          | 40           | 20            | 5           | 2            |
| 5          | 35           | 50            | 1           | 1            |
+------------+--------------+---------------+-------------+--------------+

Result table:
+-----------+------------+
| group_id  | player_id  |
+-----------+------------+ 
| 1         | 15         |
| 2         | 35         |
| 3         | 40         |
+-----------+------------+

*/

# V0
# IDEA : UNION ALL
SELECT group_id, 
       player_id 
FROM   (SELECT p.group_id, 
               ps.player_id, 
               Sum(ps.score) AS score 
        FROM   players p INNER JOIN
               (SELECT first_player AS player_id, 
                       first_score  AS score 
                FROM   matches 
                UNION ALL 
                SELECT second_player AS player_id,
                       second_score  AS score
                FROM   matches) ps 
        ON  p.player_id = ps.player_id 
        GROUP  BY ps.player_id 
        ORDER  BY group_id, 
                  score DESC, 
                  player_id) top_scores 
GROUP  BY group_id

# V0'
-- WITH 
-- _1st_score AS ( 
-- SELECT
-- p.player_id,
-- p.group_id,
-- SUM(m.first_score) AS score
-- FROM
-- Players p
-- INNER JOIN Matches m ON m.first_player = p.player_id
-- GROUP BY p.player_id, p.group_id
-- ), 
-- _2nd_score AS ( 
-- SELECT
-- p.player_id,
-- p.group_id,
-- SUM(m.second_score) AS score
-- FROM
-- Players p
-- INNER JOIN Matches m ON m.second_player = p.player_id
-- GROUP BY p.player_id, p.group_id
-- ), _sum_score AS (
-- SELECT
-- _1st_score.player_id,
-- _1st_score.score + _2nd_score.score,
-- _1st_score.group_id
-- FROM
-- _1st_score INNER JOIN _2nd_score ON
-- _1st_score.player_id = _2nd_score.player_id
-- )
-- SELECT
-- group_id,
-- player_id
-- FROM
-- _sum_score
-- WHERE
-- score IN (SELECT group_id, MAX(score) FROM _sum_score GROUP BY  group_id );

# V1
# https://blog.csdn.net/m0_37302219/article/details/105095065
select group_id,player_id from 
(select group_id,player_id,sum((
    case when player_id = first_player then first_score
         when player_id = second_player then second_score
         end
)) as totalScores
from Players p,Matches m
where p.player_id = m.first_player
or p.player_id = m.second_player
group by group_id,player_id
order by group_id,totalScores desc,player_id) as temp
group by group_id
order by group_id,totalScores desc,player_id

# V1'
# https://circlecoder.com/tournament-winners/
select group_id,player_id 
from (
    select sc.group_id group_id, sc.player_id player_id, 
       rank() over (partition by sc.group_id order by sum(sc.score) desc, sc.player_id asc) as rnk 
    from(
        select p.group_id group_id,
         p.player_id player_id ,
         sum(m.first_score) as score
        from players p
        inner join matches m
        on p.player_id = m.first_player
        group by p.group_id,p.player_id
        
        union all
        
        select p.group_id group_id,
         p.player_id player_id ,
        sum(second_score) as score
        from players p
        inner join matches m
        on p.player_id = m.second_player
        group by p.group_id,p.player_id
    ) sc
    group by sc.group_id,sc.player_id
) A 
where rnk = 1

# V2
# Time:  O(m + n + nlogn)
# Space: P(m + n)
SELECT group_id, 
       player_id 
FROM   (SELECT p.group_id, 
               ps.player_id, 
               Sum(ps.score) AS score 
        FROM   players p INNER JOIN
               (SELECT first_player AS player_id, 
                       first_score  AS score 
                FROM   matches 
                UNION ALL 
                SELECT second_player AS player_id,
                       second_score  AS score
                FROM   matches) ps 
        ON  p.player_id = ps.player_id 
        GROUP  BY ps.player_id 
        ORDER  BY group_id, 
                  score DESC, 
                  player_id) top_scores 
GROUP  BY group_id