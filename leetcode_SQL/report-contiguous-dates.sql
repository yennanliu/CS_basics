/*

Table: Failed

+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| fail_date    | date    |
+--------------+---------+
Primary key for this table is fail_date.
Failed table contains the days of failed tasks.
Table: Succeeded

+--------------+---------+
| Column Name  | Type    |
+--------------+---------+
| success_date | date    |
+--------------+---------+
Primary key for this table is success_date.
Succeeded table contains the days of succeeded tasks.
A system is running one task every day. Every task is independent of the previous tasks. The tasks can fail or succeed.

Write an SQL query to generate a report of period_state for each continuous interval of days in the period from 2019-01-01 to 2019-12-31.

period_state is ‘failed’ if tasks in this interval failed or ‘succeeded’ if tasks in this interval succeeded. Interval of days are retrieved as start_date and end_date.

Order result by start_date.

The query result format is in the following example:

Failed table:
+-------------------+
| fail_date         |
+-------------------+
| 2018-12-28        |
| 2018-12-29        |
| 2019-01-04        |
| 2019-01-05        |
+-------------------+

Succeeded table:
+-------------------+
| success_date      |
+-------------------+
| 2018-12-30        |
| 2018-12-31        |
| 2019-01-01        |
| 2019-01-02        |
| 2019-01-03        |
| 2019-01-06        |
+-------------------+

Result table:
+--------------+--------------+--------------+
| period_state | start_date   | end_date     |
+--------------+--------------+--------------+
| succeeded    | 2019-01-01   | 2019-01-03   |
| failed       | 2019-01-04   | 2019-01-05   |
| succeeded    | 2019-01-06   | 2019-01-06   |
+--------------+--------------+--------------+

The report ignored the system state in 2018 as we care about the system in the period 2019-01-01 to 2019-12-31.
From 2019-01-01 to 2019-01-03 all tasks succeeded and the system state was "succeeded".
From 2019-01-04 to 2019-01-05 all tasks failed and system state was "failed".
From 2019-01-06 to 2019-01-06 all tasks succeeded and system state was "succeeded".

*/


# V0


# V1
# https://blog.csdn.net/betty1121/article/details/108977451
with cte as (select fail_date as cal_date, 'failed' as state
from Failed
union all
select success_date as cal_date, 'succeeded' as state
from Succeeded)
select state as period_state,
       min(cal_date) as start_date,
       max(cal_date) as end_date
from
(select state, cal_date, rank() over (partition by state order by cal_date) as ranking, rank() over (order by cal_date) as id
from cte 
where cal_date between '2019-01-01' and '2019-12-31') t
group by state, (id - ranking)
order by 2

# V1'
# https://blog.csdn.net/betty1121/article/details/108977451
SELECT "failed" AS statues, fail_date AS date_list
FROM failed
WHERE fail_date BETWEEN "2019-01-01" AND "2019-12-31"
UNION ALL
SELECT "succeeded" AS statues, success_date
FROM Succeeded
WHERE success_date BETWEEN "2019-01-01" AND "2019-12-31"
ORDER BY date_list

# V1''
# https://blog.csdn.net/betty1121/article/details/108977451
SELECT s1,
CASE WHEN s1 != s2 THEN d1
     WHEN s2 IS NULL THEN d1 END AS end_date,
     @rank := @rank +1 AS rnk
FROM (SELECT t1.statues s1, t1.date_list d1, t2.statues s2, t2.date_list d2
FROM tmp t1 LEFT JOIN tmp t2
ON DATEDIFF(t1.date_list, t2.date_list) = -1
ORDER BY d1) t, (SELECT @rank := 0) r
HAVING end_date IS NOT NULL

# V1'''
# https://blog.csdn.net/betty1121/article/details/108977451
SELECT s1,
CASE WHEN s1 != s2 THEN d1
     WHEN s2 IS NULL THEN d1 END AS start_date,
@rank1 := @rank1 +1 AS rnk
FROM (SELECT t1.statues s1, t1.date_list d1, t2.statues s2, t2.date_list d2
FROM tmp t1 LEFT JOIN tmp t2
ON DATEDIFF(t1.date_list, t2.date_list) = 1
ORDER BY d1) t2, (SELECT @rank1 := 0) r
HAVING start_date IS NOT NULL

# V1''''
# https://code.dennyzhang.com/report-contiguous-dates
select period_state, min(date) as start_date, max(date) as end_date
from (
    select period_state, date,
         @rank := case when @prev = period_state then @rank else @rank+1 end as rank,
         @prev := period_state as prev
    from (
        select 'failed' as period_state, fail_date as date
        from Failed
        where fail_date between '2019-01-01' and '2019-12-31'
        union
        select 'succeeded' as period_state, success_date as date
        from Succeeded
        where success_date between '2019-01-01' and '2019-12-31') as t, 
        (select @rank:=0, @prev:='') as rows
    order by date asc) as tt
group by rank
order by rank

# V2
# Time:  O(nlogn)
# Space: O(n)
SELECT state     AS period_state, 
       Min(date) AS start_date, 
       Max(date) AS end_date 
FROM   (SELECT state, 
               date, 
               @rank := CASE 
                          WHEN @prev = state THEN @rank 
                          ELSE @rank + 1 
                        end   AS rank, 
               @prev := state AS prev 
        FROM   (SELECT * 
                FROM   (SELECT fail_date AS date, 
                               "failed"  AS state 
                        FROM   failed 
                        UNION ALL 
                        SELECT success_date AS date, 
                               "succeeded"  AS state 
                        FROM   succeeded) a 
                WHERE  date BETWEEN '2019-01-01' AND '2019-12-31' 
                ORDER  BY date ASC) b, 
               (SELECT @rank := 0, 
                       @prev := "unknown") c) d 
GROUP  BY d.rank 
ORDER  BY start_date ASC 