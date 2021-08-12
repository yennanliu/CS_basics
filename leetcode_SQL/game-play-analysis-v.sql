-- 1097.Game Play Analysis V
-- Table: Activity

-- +--------------+---------+
-- | Column Name  | Type    |
-- +--------------+---------+
-- | player_id    | int     |
-- | device_id    | int     |
-- | event_date   | date    |
-- | games_played | int     |
-- +--------------+---------+
-- (player_id, event_date) is the primary key of this table.
-- This table shows the activity of players of some game.
-- Each row is a record of a player who logged in and played a number of games (possibly 0) before logging out on some day using some device.
 

-- We define the install date of a player to be the first login day of that player.

-- We also define day 1 retention of some date X to be the number of players whose install date is X and they logged back in on the day right after X, divided by the number of players whose install date is X, rounded to 2 decimal places.

-- Write an SQL query that reports for each install date, the number of players that installed the game on that day and the day 1 retention.

-- The query result format is in the following example:

-- Activity table:
-- +-----------+-----------+------------+--------------+
-- | player_id | device_id | event_date | games_played |
-- +-----------+-----------+------------+--------------+
-- | 1         | 2         | 2016-03-01 | 5            |
-- | 1         | 2         | 2016-03-02 | 6            |
-- | 2         | 3         | 2017-06-25 | 1            |
-- | 3         | 1         | 2016-03-01 | 0            |
-- | 3         | 4         | 2016-07-03 | 5            |
-- +-----------+-----------+------------+--------------+

-- Result table:
-- +------------+----------+----------------+
-- | install_dt | installs | Day1_retention |
-- +------------+----------+----------------+
-- | 2016-03-01 | 2        | 0.50           |
-- | 2017-06-25 | 1        | 0.00           |
-- +------------+----------+----------------+
-- Player 1 and 3 installed the game on 2016-03-01 but only player 1 logged back in on 2016-03-02 so the day 1 retention of 2016-03-01 is 1 / 2 = 0.50
-- Player 2 installed the game on 2017-06-25 but didn't log back in on 2017-06-26 so the day 1 retention of 2017-06-25 is 0 / 1 = 0.00

# V0
select t1.install_date as install_dt, count(t1.install_date) as installs,
    round(count(t2.event_date) / count(*), 2) as Day1_retention
from (
    select player_id, min(event_date) as install_date
    from Activity
    group by 1
) t1
left join Activity t2 
on date_add(t1.install_date, interval 1 day) = t2.event_date
    and t1.player_id = t2.player_id
group by 1

# V1
# https://circlecoder.com/game-play-analysis-V/
select t1.install_date as install_dt, count(t1.install_date) as installs,
    round(count(t2.event_date) / count(*), 2) as Day1_retention
from (
    select player_id, min(event_date) as install_date
    from Activity
    group by 1
) t1
left join Activity t2 
on date_add(t1.install_date, interval 1 day) = t2.event_date
    and t1.player_id = t2.player_id
group by 1
order by 1

# V1'
# https://blog.csdn.net/weixin_43329319/article/details/97616108
select install_dt,installs,round(ifnull(jude,0)/installs,2) as Day1_retention
from (
    
select event_date as install_dt,count(player_id) as installs  
from
(select  player_id,event_date
from Activity
group by player_id
order by player_id,event_date)a 
group by event_date)c

left join

 (select a1.event_date,count(a1.player_id) as jude
from Activity a1,Activity a2
where a1.player_id=a2.player_id and datediff(a2.event_date,a1.event_date)=1
group by a1.event_date)b

on c.install_dt=b.event_date
group by install_dt
order by install_dt

# V2
# Time:  O(n^2)
# Space: O(n)
SELECT install_dt, 
       Count(player_id)                             AS installs, 
       Round(Count(next_day) / Count(player_id), 2) AS Day1_retention 
FROM   (SELECT a.player_id, 
               a.install_dt, 
               b.event_date AS next_day 
        FROM   (SELECT player_id, 
                       Min(event_date) AS install_dt 
                FROM   activity 
                GROUP  BY player_id) AS a 
               LEFT JOIN activity AS b 
                      ON Datediff(b.event_date, a.install_dt) = 1
                         AND a.player_id = b.player_id ) AS t 
GROUP  BY install_dt; 