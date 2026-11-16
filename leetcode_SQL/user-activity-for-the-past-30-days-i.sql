/*

1141. User Activity for the Past 30 Days I
https://leetcode.com/problems/user-activity-for-the-past-30-days-i/
Easy

Table: Activity

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| user_id       | int     |
| session_id    | int     |
| activity_date | date    |
| activity_type | enum    |
+---------------+---------+
This table may have duplicate rows.
The activity_type column is an ENUM (category) of type ('open_session', 'end_session', 'scroll_down', 'send_message').
The table shows the user activities for a social media website.
Note that each session belongs to exactly one user.

Write a solution to find the daily active user count for a period of 30 days ending 2019-07-27 inclusively. A user was active on someday if they made at least one activity on that day.

Return the result table in any order.

The result format is in the following example.

Note: Any activity from ('open_session', 'end_session', 'scroll_down', 'send_message') will be considered valid activity for a user to be considered active on a day.

Example 1:

Input:
Activity table:
+---------+------------+---------------+---------------+
| user_id | session_id | activity_date | activity_type |
+---------+------------+---------------+---------------+
| 1       | 1          | 2019-07-20    | open_session  |
| 1       | 1          | 2019-07-20    | scroll_down   |
| 1       | 1          | 2019-07-20    | end_session   |
| 2       | 4          | 2019-07-20    | open_session  |
| 2       | 4          | 2019-07-21    | send_message  |
| 2       | 4          | 2019-07-21    | end_session   |
| 3       | 2          | 2019-07-21    | open_session  |
| 3       | 2          | 2019-07-21    | send_message  |
| 3       | 2          | 2019-07-21    | end_session   |
| 4       | 3          | 2019-06-25    | open_session  |
| 4       | 3          | 2019-06-25    | end_session   |
+---------+------------+---------------+---------------+
Output:
+------------+--------------+
| day        | active_users |
+------------+--------------+
| 2019-07-20 | 2            |
| 2019-07-21 | 2            |
+------------+--------------+
Explanation: Note that we do not care about days with zero active users.

*/

# V0
# IDEA : FILTER TO THE 30-DAY WINDOW, GROUP BY DAY, COUNT DISTINCT USERS
#
#   "30 days ending 2019-07-27 inclusively" is 2019-06-28 .. 2019-07-27, i.e.
#   DATEDIFF('2019-07-27', activity_date) between 0 and 29. Both bounds
#   matter: a bare "< 30" also keeps every date AFTER 2019-07-27 (negative diff).
#   A user logs several activities a day, so COUNT(DISTINCT user_id) counts
#   each user once per day. Filter in WHERE, before grouping, not in HAVING.
#
# time = O(n), space = O(d)    -- n = rows in Activity, d = days in the window (at most 30)
SELECT
    activity_date AS day,
    COUNT(DISTINCT user_id) AS active_users   -- one user, many activities -> counted once
FROM Activity
WHERE DATEDIFF('2019-07-27', activity_date) BETWEEN 0 AND 29
GROUP BY activity_date;

# V1
# IDEA : SAME GROUPING, WINDOW WRITTEN AS A DATE RANGE
#
#   Compare the column itself against two dates instead of computing a diff
#   per row: a bare column in the predicate can use an index on activity_date,
#   DATEDIFF(..., activity_date) cannot. 2019-06-28 is 2019-07-27 minus 29
#   days -- the first day of the inclusive 30-day window; BETWEEN includes
#   both ends.
#
# time = O(n), space = O(d)    -- n = rows in Activity, d = days in the window
SELECT
    activity_date AS day,
    COUNT(DISTINCT user_id) AS active_users
FROM Activity
WHERE activity_date BETWEEN '2019-06-28' AND '2019-07-27'
GROUP BY activity_date;
