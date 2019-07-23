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
-- Write an SQL query that reports for each player and date, how many games played so far by the player. That is, the total number of games played by the player until that date. Check the example for clarity.

-- The query result format is in the following example:

-- Activity table:
-- +-----------+-----------+------------+--------------+
-- | player_id | device_id | event_date | games_played |
-- +-----------+-----------+------------+--------------+
-- | 1         | 2         | 2016-03-01 | 5            |
-- | 1         | 2         | 2016-05-02 | 6            |
-- | 1         | 3         | 2017-06-25 | 1            |
-- | 3         | 1         | 2016-03-02 | 0            |
-- | 3         | 4         | 2018-07-03 | 5            |
-- +-----------+-----------+------------+--------------+

-- Result table:
-- +-----------+------------+---------------------+
-- | player_id | event_date | games_played_so_far |
-- +-----------+------------+---------------------+
-- | 1         | 2016-03-01 | 5                   |
-- | 1         | 2016-05-02 | 11                  |
-- | 1         | 2017-06-25 | 12                  |
-- | 3         | 2016-03-02 | 0                   |
-- | 3         | 2018-07-03 | 5                   |
-- +-----------+------------+---------------------+
-- For the player with id 1, 5 + 6 = 11 games played by 2016-05-02, and 5 + 6 + 1 = 12 games played by 2017-06-25.
-- For the player with id 3, 0 + 5 = 5 games played by 2018-07-03.
-- Note that for each player we only care about the days when the player logged in.

# V0 
SELECT a1.player_id,
       a2.event_date,
       sum(a1.games_played) AS games_played_so_far
FROM Activity AS a1
LEFT JOIN Activity AS a2 ON a1.player_id = a2.player_id
AND a1.event_date <= a2.event_date
GROUP BY 1,
         2

# V1 
# https://code.dennyzhang.com/game-play-analysis-iii
SELECT t1.player_id,
       t1.event_date,
       sum(t2.games_played) AS games_played_so_far
FROM Activity AS t1
INNER JOIN Activity AS t2 ON t1.player_id = t2.player_id
WHERE t1.event_date >= t2.event_date
GROUP BY t1.player_id,
         t1.event_date

# V2 
# Time:  O(nlogn)
# Space: O(n)
SELECT player_id, 
       event_date, 
       @accum := games_played + ( @prev = ( @prev := player_id ) ) * @accum 
       games_played_so_far 
FROM   activity, 
       (SELECT @accum := 0, 
               @prev := -1) init 
ORDER  BY player_id, 
          event_date 