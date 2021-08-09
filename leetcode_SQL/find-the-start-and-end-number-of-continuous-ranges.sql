/*

Table: Logs

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| log_id        | int     |
+---------------+---------+
id is the primary key for this table.
Each row of this table contains the ID in a log Table.
Since some IDs have been removed from Logs. Write an SQL query to find the start and end number of continuous ranges in table Logs.

Order the result table by start_id.

The query result format is in the following example:

Logs table:
+------------+
| log_id     |
+------------+
| 1          |
| 2          |
| 3          |
| 7          |
| 8          |
| 10         |
+------------+

Result table:
+------------+--------------+
| start_id   | end_id       |
+------------+--------------+
| 1          | 3            |
| 7          | 8            |
| 10         | 10           |
+------------+--------------+
The result table should contain all ranges in table Logs.
From 1 to 3 is contained in the table.
From 4 to 6 is missing in the table
From 7 to 8 is contained in the table.
Number 9 is missing in the table.
Number 10 is contained in the table.

*/


# V0
SELECT Min(log_id) AS start_id, 
       Max(log_id) AS end_id 
FROM   (SELECT log_id, 
               @rank := CASE 
                          WHEN @prev = log_id - 1 THEN @rank 
                          ELSE @rank + 1 
                        end    AS rank, 
               @prev := log_id AS prev
        FROM   logs AS A, 
               (SELECT @rank := 0, 
                       @prev :=- 1) AS B) C 
GROUP  BY C.rank 
ORDER  BY NULL; 

# V1
# https://code.dennyzhang.com/find-the-start-and-end-number-of-continuous-ranges
select min(log_id) as START_ID, max(log_id) as END_ID
from (select log_id,
        @rank := case when @prev = log_id-1 then @rank else @rank+1 end as rank,
        @prev := log_id as prev
    from Logs,
       (select @rank:=0, @prev:=-1) as rows) as tt
group by rank
order by START_ID

# V1'
# https://code.dennyzhang.com/find-the-start-and-end-number-of-continuous-ranges
## find the starting sequence: 1, 7, 10
## find the ending sequence: 3, 8, 10
## merge them as one table
select start_id, min(end_id) as end_id
from (select t1.log_id as start_id
    from logs as t1 left join logs as t2
    on t1.log_id-1 = t2.log_id
    where t2.log_id is null) tt_start join
    (select t1.log_id as end_id
    from logs as t1 left join logs as t2
    on t1.log_id+1 = t2.log_id
    where t2.log_id is null) tt_end
where start_id<=end_id
group by start_id

# V2
# Time:  O(n)
# Space: O(n)
SELECT Min(log_id) AS start_id, 
       Max(log_id) AS end_id 
FROM   (SELECT log_id, 
               @rank := CASE 
                          WHEN @prev = log_id - 1 THEN @rank 
                          ELSE @rank + 1 
                        end    AS rank, 
               @prev := log_id AS prev
        FROM   logs AS A, 
               (SELECT @rank := 0, 
                       @prev :=- 1) AS B) C 
GROUP  BY C.rank 
ORDER  BY NULL; 