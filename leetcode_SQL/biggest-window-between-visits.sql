/*
Biggest Window Between Visits Problem

Description
LeetCode Problem 1709.

Table: UserVisits

+-------------+------+
| Column Name | Type |
+-------------+------+
| user_id     | int  |
| visit_date  | date |
+-------------+------+
This table does not have a primary key.
This table contains logs of the dates that users vistied a certain retailer.
Assume today’s date is ‘2021-1-1’.

Write an SQL query that will, for each user_id, find out the largest window of days between each visit and the one right after it (or today if you are considering the last visit).

Return the result table ordered by user_id.

The query result format is in the following example:

UserVisits table:
+---------+------------+
| user_id | visit_date |
+---------+------------+
| 1       | 2020-11-28 |
| 1       | 2020-10-20 |
| 1       | 2020-12-3  |
| 2       | 2020-10-5  |
| 2       | 2020-12-9  |
| 3       | 2020-11-11 |
+---------+------------+
Result table:
+---------+---------------+
| user_id | biggest_window|
+---------+---------------+
| 1       | 39            |
| 2       | 65            |
| 3       | 51            |
+---------+---------------+
For the first user, the windows in question are between dates:
    - 2020-10-20 and 2020-11-28 with a total of 39 days. 
    - 2020-11-28 and 2020-12-3 with a total of 5 days. 
    - 2020-12-3 and 2021-1-1 with a total of 29 days.
Making the biggest window the one with 39 days.
For the second user, the windows in question are between dates:
    - 2020-10-5 and 2020-12-9 with a total of 65 days.
    - 2020-12-9 and 2021-1-1 with a total of 23 days.
Making the biggest window the one with 65 days.
For the third user, the only window in question is between dates 2020-11-11 and 2021-1-1 with a total of 51 days.

*/

# V0
### IDEA : lead func + coalesce + datediff
# https://riptutorial.com/sql/example/27455/lag-and-lead
WITH cte1 AS
  (SELECT user_id,
          coalesce(lead(visit_date) OVER (PARTITION BY user_id
                                          ORDER BY visit_date), '2021-01-01') AS lead_visit_date,
          visit_date
   FROM UserVisits),
     cte2 AS
  (SELECT user_id,
          max(datediff(lead_visit_date, visit_date)) AS max_diff
   FROM cte1
   GROUP BY user_id)
SELECT *
FROM cte2
ORDER BY user_id

# V0'
WITH cte1 AS
  (SELECT user_id,
          IFNULL(lead(visit_date) OVER (PARTITION BY user_id
                                          ORDER BY visit_date), '2021-01-01') AS lead_visit_date,
          visit_date
   FROM UserVisits),
     cte2 AS
  (SELECT user_id,
          max(datediff(lead_visit_date, visit_date)) AS max_diff
   FROM cte1
   GROUP BY user_id)
SELECT *
FROM cte2
ORDER BY user_id

# V1
# https://circlecoder.com/biggest-window-between-visits/
select user_id, max(diff) as biggest_window
from
(
    select user_id, 
       datediff(coalesce(lead(visit_date) over (partition by user_id order by visit_date), '2021-01-01'), visit_date) as diff
    from userVisits
) t
group by user_id
order by user_id

# V2
# Time:  O(nlogn)
# Space: O(n)
SELECT
    user_id,
    MAX(DATEDIFF(visit_date_after, visit_date)) AS biggest_window
FROM (
    SELECT
        user_id,
        visit_date,
        IFNULL(
            LEAD(visit_date, 1) OVER (
                PARTITION BY user_id
                ORDER BY visit_date
            ),
            '2021-1-1'
        ) AS visit_date_after
    FROM UserVisits
) tmp
GROUP BY 1
ORDER BY NULL;