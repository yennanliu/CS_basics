/*

Number Of Calls Between Two Persons Problem


Description
LeetCode Problem 1699.

Table: Calls

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| from_id     | int     |
| to_id       | int     |
| duration    | int     |
+-------------+---------+
This table does not have a primary key, it may contain duplicates.
This table contains the duration of a phone call between from_id and to_id.
from_id != to_id
Write an SQL query to report the number of calls and the total call duration between each pair of distinct persons (person1, person2) where person1 < person2.

Return the result table in any order.

The query result format is in the following example:

Calls table:
+---------+-------+----------+
| from_id | to_id | duration |
+---------+-------+----------+
| 1       | 2     | 59       |
| 2       | 1     | 11       |
| 1       | 3     | 20       |
| 3       | 4     | 100      |
| 3       | 4     | 200      |
| 3       | 4     | 200      |
| 4       | 3     | 499      |
+---------+-------+----------+

Result table:
+---------+---------+------------+----------------+
| person1 | person2 | call_count | total_duration |
+---------+---------+------------+----------------+
| 1       | 2       | 2          | 70             |
| 1       | 3       | 1          | 20             |
| 3       | 4       | 4          | 999            |
+---------+---------+------------+----------------+
Users 1 and 2 had 2 calls and the total duration is 70 (59 + 11).
Users 1 and 3 had 1 call and the total duration is 20.
Users 3 and 4 had 4 calls and the total duration is 999 (100 + 200 + 200 + 499).

*/

# V0
# IDEA : cte + union all
WITH cte AS
  (SELECT *
   FROM Calls c1
   UNION ALL 
   SELECT 
    to_id,
    from_id,
    duration
   FROM Calls c2)
SELECT from_id AS person1,
       to_id AS person2,
       COUNT(1) AS call_count,
       SUM(duration) AS total_duration
FROM cte
WHERE to_id > from_id
GROUP BY person1,
         person2

# V1
# https://circlecoder.com/number-of-calls-between-two-persons/
select from_id as person1,to_id as person2,
    count(duration) as call_count, sum(duration) as total_duration
from (select * 
      from Calls 
      
      union all
      
      select to_id, from_id, duration 
      from Calls) t1
where from_id < to_id
group by person1, person2

# V1'
# https://leetcode.ca/2020-07-25-1699-Number-of-Calls-Between-Two-Persons/
WITH caller as (
    select from_id as person1, to_id as person2, duration
    from Calls
    UNION ALL
    select to_id as person1, from_id as person2, duration
    from Calls
),

unique_caller as (
    select person1, person2, duration
    from caller
    where person1 < person2
)

select
    person1, person2, count(*) as call_count, sum(duration) as total_duration
from unique_caller
group by person1, person2

# V2
# Time:  O(n)
# Space: O(n)
SELECT LEAST(from_id,to_id) as person1,
       GREATEST(from_id,to_id) as person2,
       COUNT(*) as call_count,
       SUM(duration) as total_duration
FROM Calls
GROUP BY person1, person2
ORDER BY NULL;