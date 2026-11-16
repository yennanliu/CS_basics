/*

550. Game Play Analysis IV
https://leetcode.com/problems/game-play-analysis-iv/
Medium

Table: Activity

+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| player_id    | int     |
| device_id    | int     |
| event_date   | date    |
| games_played | int     |
+--------------+---------+
(player_id, event_date) is the primary key (combination of columns with unique values) of this table.
This table shows the activity of players of some games.
Each row is a record of a player who logged in and played a number of games (possibly 0) before logging out on someday using some device.

Write a solution to report the fraction of players that logged in again on the day after the day they first logged in, rounded to 2 decimal places. In other words, you need to determine the number of players who logged in on the day immediately following their initial login, and divide it by the number of total players.

The result format is in the following example.

Example 1:

Input:
Activity table:
+-----------+-----------+------------+--------------+
| player_id | device_id | event_date | games_played |
+-----------+-----------+------------+--------------+
| 1         | 2         | 2016-03-01 | 5            |
| 1         | 2         | 2016-03-02 | 6            |
| 2         | 3         | 2017-06-25 | 1            |
| 3         | 1         | 2016-03-02 | 0            |
| 3         | 4         | 2018-07-03 | 5            |
+-----------+-----------+------------+--------------+
Output:
+-----------+
| fraction  |
+-----------+
| 0.33      |
+-----------+
Explanation:
Only the player with id 1 logged back in after the first day he had logged in so the answer is 1/3 = 0.33

*/

# V0
# IDEA : FIRST LOGIN PER PLAYER, LEFT JOIN THE NEXT DAY, COUNT THE MATCHES
#
#   Step 1: MIN(event_date) per player is the first login (one row per player,
#   so COUNT(*) over it is the total player count).
#   Step 2: LEFT JOIN Activity on "same player, exactly one day later". The
#   primary key (player_id, event_date) allows at most one match per player.
#   Step 3: COUNT(a.player_id) skips the NULLs of unmatched players, so it
#   counts only the players who came back the next day.
#
# time = O(n), space = O(p)    -- n = rows in Activity, p = players; one GROUP BY, one hash join
WITH first_login AS (
    SELECT
        player_id,
        MIN(event_date) AS first_date
    FROM Activity
    GROUP BY player_id
)
SELECT
    ROUND(COUNT(a.player_id) / COUNT(*), 2) AS fraction   -- COUNT(col) skips NULL, COUNT(*) does not
FROM first_login AS f
LEFT JOIN Activity AS a                                    -- LEFT: players who never returned stay in the denominator
    ON  a.player_id = f.player_id
    AND DATEDIFF(a.event_date, f.first_date) = 1;          -- date arithmetic; first_date + 1 is integer math in MySQL

# V1
# IDEA : WINDOW MIN ON EVERY ROW, THEN ONE PASS
#
#   MIN(event_date) OVER (PARTITION BY player_id) puts each player's first
#   login on every one of their rows, so "logged in the day after" becomes a
#   row-level test. At most one row per player can pass it (primary key),
#   so the SUM of the test is the number of returning players.
#
# time = O(n log n), space = O(n)    -- n = rows in Activity; the window partitions (sorts) by player
WITH with_first AS (
    SELECT
        player_id,
        event_date,
        MIN(event_date) OVER (PARTITION BY player_id) AS first_date
    FROM Activity
)
SELECT
    ROUND(
        SUM(CASE WHEN DATEDIFF(event_date, first_date) = 1 THEN 1 ELSE 0 END)
            / COUNT(DISTINCT player_id),
        2
    ) AS fraction
FROM with_first;
