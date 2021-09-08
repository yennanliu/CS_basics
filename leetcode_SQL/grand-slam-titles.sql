/*

1783.(Medium)Grand Slam Titles

Table: Players

+----------------+---------+
| Column Name    | Type    |
+----------------+---------+
| player_id      | int     |
| player_name    | varchar |
+----------------+---------+
player_id is the primary key for this table.
Each row in this table contains the name and the ID of a tennis player.
Table: Championships

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| year          | int     |
| Wimbledon     | int     |
| Fr_open       | int     |
| US_open       | int     |
| Au_open       | int     |
+---------------+---------+
year is the primary key for this table.
Each row of this table containts the IDs of the players who won one each tennis tournament of the grand slam.
Write an SQL query to report the number of grand slam tournaments won by each player. Do not include the players who did not win any tournament.

Return the result table in any order.

The query result format is in the following example:

Players table:
+-----------+-------------+
| player_id | player_name |
+-----------+-------------+
| 1         | Nadal       |
| 2         | Federer     |
| 3         | Novak       |
+-----------+-------------+
​
Championships table:
+------+-----------+---------+---------+---------+
| year | Wimbledon | Fr_open | US_open | Au_open |
+------+-----------+---------+---------+---------+
| 2018 | 1         | 1       | 1       | 1       |
| 2019 | 1         | 1       | 2       | 2       |
| 2020 | 2         | 1       | 2       | 2       |
+------+-----------+---------+---------+---------+
​
Result table:
+-----------+-------------+-------------------+
| player_id | player_name | grand_slams_count |
+-----------+-------------+-------------------+
| 2         | Federer     | 5                 |
| 1         | Nadal       | 7                 |
+-----------+-------------+-------------------+
​
Player 1 (Nadal) won 7 titles: Wimbledon (2018, 2019), Fr_open (2018, 2019, 2020), US_open (2018), and Au_open (2018).
Player 2 (Federer) won 5 titles: Wimbledon (2020), US_open (2019, 2020), and Au_open (2019, 2020).
Player 3 (Novak) did not win anything, we did not include them in the result table.

*/

# V0
# IDEA : UNION ALL
WITH cte
     AS (SELECT wimbledon AS id
         FROM   championships
         UNION ALL
         SELECT fr_open AS id
         FROM   championships
         UNION ALL
         SELECT us_open AS id
         FROM   championships
         UNION ALL
         SELECT au_open AS id
         FROM   championships)
SELECT player_id,
       player_name,
       Count(*) AS grand_slams_count
FROM   players
       INNER JOIN cte
               ON players.player_id = cte.id
GROUP  BY 1, 2
ORDER  BY NULL; 

# V1
# https://zqt0.gitbook.io/leetcode/sql/1783.grand-slam-titles
select player_id, Players.player_name, count(1) grand_slams_count
from 
(
    select year,'Wimbledon' AS tournament, Wimbledon AS 'player_id' from Championships
    union
    select year,'Fr_open' AS tournament, Fr_open AS 'player_id' from Championships
    union
    select year,'US_open' AS tournament, US_open AS 'player_id'from Championships
    union 
    select year,'Au_open' AS tournament, Au_open  AS 'player_id'from Championships
) t inner join Players using(player_id)
group by player_id

# V1'
# https://zqt0.gitbook.io/leetcode/sql/1783.grand-slam-titles
select  player_id, player_name,
    sum(player_id = Wimbledon) + 
    sum(player_id = Fr_open) + 
    sum(player_id = US_open) + 
    sum(player_id = Au_open) grand_slams_count
from Players cross join Championships
group by player_id
having grand_slams_count>0

# V2
# Time:  O(n)
# Space: O(n)
WITH cte
     AS (SELECT wimbledon AS id
         FROM   championships
         UNION ALL
         SELECT fr_open AS id
         FROM   championships
         UNION ALL
         SELECT us_open AS id
         FROM   championships
         UNION ALL
         SELECT au_open AS id
         FROM   championships)
SELECT player_id,
       player_name,
       Count(*) AS grand_slams_count
FROM   players
       INNER JOIN cte
               ON players.player_id = cte.id
GROUP  BY 1, 2
ORDER  BY NULL; 