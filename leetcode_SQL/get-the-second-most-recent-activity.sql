/*

SQL Schema
Table: UserActivity

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| username      | varchar |
| activity      | varchar |
| startDate     | Date    |
| endDate       | Date    |
+---------------+---------+
This table does not contain primary key.
This table contain information about the activity performed of each user in a period of time.
A person with username performed a activity from startDate to endDate.
Write an SQL query to show the second most recent activity of each user.

If the user only has one activity, return that one.

A user can’t perform more than one activity at the same time. Return the result table in any order.

The query result format is in the following example:

UserActivity table:
+------------+--------------+-------------+-------------+
| username   | activity     | startDate   | endDate     |
+------------+--------------+-------------+-------------+
| Alice      | Travel       | 2020-02-12  | 2020-02-20  |
| Alice      | Dancing      | 2020-02-21  | 2020-02-23  |
| Alice      | Travel       | 2020-02-24  | 2020-02-28  |
| Bob        | Travel       | 2020-02-11  | 2020-02-18  |
+------------+--------------+-------------+-------------+

Result table:
+------------+--------------+-------------+-------------+
| username   | activity     | startDate   | endDate     |
+------------+--------------+-------------+-------------+
| Alice      | Dancing      | 2020-02-21  | 2020-02-23  |
| Bob        | Travel       | 2020-02-11  | 2020-02-18  |
+------------+--------------+-------------+-------------+

The most recent activity of Alice is Travel from 2020-02-24 to 2020-02-28, before that she was dancing from 2020-02-21 to 2020-02-23.
Bob only has one record, we just take that one.

*/

# V0

# V1
# https://code.dennyzhang.com/get-the-second-most-recent-activity
(select *
from UserActivity
group by username
having count(1) = 1)
union
(select a.*
from UserActivity as a left join UserActivity as b
on a.username = b.username and a.endDate < b.endDate
group by a.username, a.endDate
having count(b.endDate) = 1)

# V2
# Time:  O(nlogn)
# Space: O(n)
SELECT *
FROM UserActivity
GROUP BY username
HAVING COUNT(1) = 1

UNION ALL

SELECT a.username,
       a.activity,
       a.startDate,
       a.endDate
FROM
  (SELECT @accu := (CASE
                        WHEN username = @prev THEN @accu + 1
                        ELSE 1
                    END) AS n,
          @prev := username AS username,
          activity,
          startDate,
          endDate
   FROM
        (SELECT @accu := 0, @prev := 0) AS init,
        UserActivity AS u
   ORDER BY username,
            endDate DESC) AS a
WHERE n = 2;