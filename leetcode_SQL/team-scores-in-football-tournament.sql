/*

Table: Teams

+---------------+----------+
| Column Name   | Type     |
+---------------+----------+
| team_id       | int      |
| team_name     | varchar  |
+---------------+----------+
team_id is the primary key of this table.
Each row of this table represents a single football team.
 

Table: Matches

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| match_id      | int     |
| host_team     | int     |
| guest_team    | int     | 
| host_goals    | int     |
| guest_goals   | int     |
+---------------+---------+
match_id is the primary key of this table.
Each row is a record of a finished match between two different teams. 
Teams host_team and guest_team are represented by their IDs in the teams table (team_id) and they scored host_goals and guest_goals goals respectively.
 

You would like to compute the scores of all teams after all matches. Points are awarded as follows:

A team receives three points if they win a match (Score strictly more goals than the opponent team).
A team receives one point if they draw a match (Same number of goals as the opponent team).
A team receives no points if they lose a match (Score less goals than the opponent team).
Write an SQL query that selects the team_id, team_name and num_points of each team in the tournament after all described matches. Result table should be ordered by num_points (decreasing order). In case of a tie, order the records by team_id (increasing order).

The query result format is in the following example:

Teams table:
+-----------+--------------+
| team_id   | team_name    |
+-----------+--------------+
| 10        | Leetcode FC  |
| 20        | NewYork FC   |
| 30        | Atlanta FC   |
| 40        | Chicago FC   |
| 50        | Toronto FC   |
+-----------+--------------+

Matches table:
+------------+--------------+---------------+-------------+--------------+
| match_id   | host_team    | guest_team    | host_goals  | guest_goals  |
+------------+--------------+---------------+-------------+--------------+
| 1          | 10           | 20            | 3           | 0            |
| 2          | 30           | 10            | 2           | 2            |
| 3          | 10           | 50            | 5           | 1            |
| 4          | 20           | 30            | 1           | 0            |
| 5          | 50           | 30            | 1           | 0            |
+------------+--------------+---------------+-------------+--------------+

Result table:
+------------+--------------+---------------+
| team_id    | team_name    | num_points    |
+------------+--------------+---------------+
| 10         | Leetcode FC  | 7             |
| 20         | NewYork FC   | 3             |
| 50         | Toronto FC   | 3             |
| 30         | Atlanta FC   | 1             |
| 40         | Chicago FC   | 0             |
+------------+--------------+---------------+

*/

# V0
WITH _guest AS
  (SELECT team_id,
          SUM(CASE
                  WHEN guest_goals > host_goals THEN 3
                  ELSE guest_goals = host_goals THEN 1
                  ELSE 0
              END) AS guest_sum
   FROM Matches m
   INNER JOIN Teams t ON t.team_id = m.guest_team
   GROUP BY team_id),
     _host AS
  (SELECT team_id,
          SUM(CASE
                  WHEN host_goals > guest_goals THEN 3
                  ELSE host_goals > guest_goals THEN 1
                  ELSE 0
              END) AS host_sum
   FROM Matches m
   INNER JOIN Teams t ON t.team_id = m.host_team
   GROUP BY team_id)
SELECT t.team_id,
       t.team_name,
       /* NOTICE HERE !!!  NEED TO CONSIDER IF NULL POINT (some teams have NO any tournament)*/
       SUM(IFNULL(g.guest_sum + t.host_sum, 0)) AS num_points
FROM Teams t
/* NOTE : LEFT JOIN ! (some teams have NO any tournament) */
LEFT JOIN _guest g ON g.team_id = t.team_id
/* NOTE : LEFT JOIN ! (some teams have NO any tournament) */
LEFT JOIN _host h ON h.team_id = t.team_id
ORDER BY g.guest_sum + t.host_sum DESC;

# V1
# https://www.cnblogs.com/onePunchCoder/p/11619433.html
SELECT
  t.team_id,
  t.team_name,
  SUM(IFNULL(points,0)) AS num_points
FROM
teams t LEFT JOIN
(
SELECT
  host_team AS team_id,
  IF(
    host_goals > guest_goals,
    3,
    IF(host_goals = guest_goals, 1, 0)
  ) AS points
FROM
  matches
UNION ALL
SELECT
  guest_team AS team_id,
  IF(
    host_goals > guest_goals,
    0,
    IF(host_goals = guest_goals, 1, 3)
  ) AS points
FROM
  matches
)temp ON
t.team_id = temp.team_id
GROUP BY team_id
ORDER BY num_points DESC ,team_id ASC ;

# V1'
# https://code.dennyzhang.com/team-scores-in-football-tournament
select Teams.team_id, Teams.team_name, 
    sum(case when team_id=host_team and host_goals>guest_goals then 3 else 0 end) +
    sum(case when team_id=host_team and host_goals=guest_goals then 1 else 0 end) +
    sum(case when team_id=guest_team and host_goals<guest_goals then 3 else 0 end) +
    sum(case when team_id=guest_team and host_goals=guest_goals then 1 else 0 end) as num_points
from Teams left join Matches
on Teams.team_id = Matches.host_team or Teams.team_id = Matches.guest_team
group by Teams.team_id
order by num_points desc, Teams.team_id asc

# V1''
# https://code.dennyzhang.com/team-scores-in-football-tournament
select Teams.team_id, Teams.team_name, sum(if(isnull(num_points), 0, num_points)) as num_points
from Teams left join
    (
        select host_team as team_id,
            sum(case when host_goals>guest_goals then 3 
                     when host_goals=guest_goals then 1
                     else 0 end) as num_points
        from Matches
        group by host_team
        union all
        select guest_team as team_id,
            sum(case when host_goals<guest_goals then 3 
                     when host_goals=guest_goals then 1
                     else 0 end) as num_points
        from Matches
        group by guest_team
    ) as tt
on Teams.team_id = tt.team_id
group by Teams.team_id
order by num_points desc, Teams.team_id asc


# V1'''
# https://code.dennyzhang.com/team-scores-in-football-tournament
select Teams.team_id, Teams.team_name, ifnull(sum(num_points), 0) as num_points
from Teams left join
    (
        select host_team as team_id,
            sum(case when host_goals>guest_goals then 3 
                     when host_goals=guest_goals then 1
                     else 0 end) as num_points
        from Matches
        group by host_team
        union all
        select guest_team as team_id,
            sum(case when host_goals<guest_goals then 3 
                     when host_goals=guest_goals then 1
                     else 0 end) as num_points
        from Matches
        group by guest_team
    ) as tt
on Teams.team_id = tt.team_id
group by Teams.team_id
order by num_points desc, Teams.team_id asc

# V2
# Time:  O(nlogn)
# Space: O(n)
SELECT t.team_id,
       t.team_name,
       IFNULL(SUM(m.points), 0) AS num_points
FROM TEAMS t
LEFT JOIN
 (SELECT host_team AS team_id,
         CASE
             WHEN host_goals > guest_goals THEN 3
             WHEN host_goals = guest_goals THEN 1
             ELSE 0
         END AS points
  FROM Matches
  UNION ALL
  SELECT guest_team AS team_id,
         CASE
             WHEN host_goals < guest_goals THEN 3
             WHEN host_goals = guest_goals THEN 1
             ELSE 0
           END AS points
  FROM Matches) m
ON t.team_id = m.team_id
GROUP BY t.team_id
ORDER BY num_points DESC,
         team_id;