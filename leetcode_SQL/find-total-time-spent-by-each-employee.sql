/*

Find Total Time Spent By Each Employee Problem

Description
LeetCode Problem 1741.

Table: Employees

+-------------+------+
| Column Name | Type |
+-------------+------+
| emp_id      | int  |
| event_day   | date |
| in_time     | int  |
| out_time    | int  |
+-------------+------+
(emp_id, event_day, in_time) is the primary key of this table.
The table shows the employees' entries and exits in an office.
event_day is the day at which this event happened and in_time is the minute at which the employee entered the office and out_time is the time at which he got outnumbered from 1 to 1440.
It's guaranteed that no two events on the same day intersect in time.
Write an SQL query to calculate the total time in minutes spent by each employee on each day at the office. Note that within one day, an employee can enter and leave more than once.

Return the result table in any order.

The query result format is in the following example:

Employees table:
+--------+------------+---------+----------+
| emp_id | event_day  | in_time | out_time |
+--------+------------+---------+----------+
| 1      | 2020-11-28 | 4       | 32       |
| 1      | 2020-11-28 | 55      | 200      |
| 1      | 2020-12-03 | 1       | 42       |
| 2      | 2020-11-28 | 3       | 33       |
| 2      | 2020-12-09 | 47      | 74       |
+--------+------------+---------+----------+
Result table:
+------------+--------+------------+
| day        | emp_id | total_time |
+------------+--------+------------+
| 2020-11-28 | 1      | 173        |
| 2020-11-28 | 2      | 30         |
| 2020-12-03 | 1      | 41         |
| 2020-12-09 | 2      | 27         |
+------------+--------+------------+
Employee 1 has three events two on day 2020-11-28 with a total of (32 - 4) + (200-55) = 173 and one on day 2020-12-03 with a total of (42 - 1) = 41.
Employee 2 has two events one on day 2020-11-28 with a total of (33-3) = 30 and one on day 2020-12-09 with a total of (74 - 47) = 27.

*/

# V0
select event_day as day, emp_id, sum(out_time - in_time) as total_time
from Employees
group by day, emp_id

# V1
# https://circlecoder.com/find-total-time-spent-by-each-employee/
select event_day as day, emp_id, sum(out_time - in_time) as total_time
from Employees
group by day, emp_id

# V2
# Time:  O(nlogn)
# Space: O(n)
SELECT event_day               AS day,
       emp_id,
       Sum(out_time - in_time) AS total_time
FROM   employees
GROUP  BY 1, 2
ORDER  BY 1, 2;