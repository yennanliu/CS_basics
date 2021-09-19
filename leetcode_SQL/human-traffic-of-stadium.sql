/*

SQL Schema
Table: Stadium

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| id            | int     |
| visit_date    | date    |
| people        | int     |
+---------------+---------+
visit_date is the primary key for this table.
Each row of this table contains the visit date and visit id to the stadium with the number of people during the visit.
No two rows will have the same visit_date, and as the id increases, the dates increase as well.
 

Write an SQL query to display the records with three or more rows with consecutive id's, and the number of people is greater than or equal to 100 for each.

Return the result table ordered by visit_date in ascending order.

The query result format is in the following example.

 

Stadium table:
+------+------------+-----------+
| id   | visit_date | people    |
+------+------------+-----------+
| 1    | 2017-01-01 | 10        |
| 2    | 2017-01-02 | 109       |
| 3    | 2017-01-03 | 150       |
| 4    | 2017-01-04 | 99        |
| 5    | 2017-01-05 | 145       |
| 6    | 2017-01-06 | 1455      |
| 7    | 2017-01-07 | 199       |
| 8    | 2017-01-09 | 188       |
+------+------------+-----------+

Result table:
+------+------------+-----------+
| id   | visit_date | people    |
+------+------------+-----------+
| 5    | 2017-01-05 | 145       |
| 6    | 2017-01-06 | 1455      |
| 7    | 2017-01-07 | 199       |
| 8    | 2017-01-09 | 188       |
+------+------------+-----------+
The four rows with ids 5, 6, 7, and 8 have consecutive ids and each of them has >= 100 people attended. Note that row 8 was included even though the visit_date was not the next day after row 7.
The rows with ids 2 and 3 are not included because we need at least three consecutive ids.
Accepted

*/

# V0
select distinct t1.*
from stadium t1, stadium t2, stadium t3
where (t1.people >= 100) and (t2.people >= 100) and (t3.people >= 100) and 
    ((t1.id - t2.id = 1 and t1.id - t3.id = 2 and t2.id - t3.id =1) or
    (t2.id - t1.id = 1 and t2.id - t3.id = 2 and t1.id - t3.id =1) or
    (t3.id - t2.id = 1 and t2.id - t1.id =1 and t3.id - t1.id = 2))
order by t1.id

# V1
# https://circlecoder.com/human-traffic-of-stadium/
select distinct t1.*
from stadium t1, stadium t2, stadium t3
where (t1.people >= 100) and (t2.people >= 100) and (t3.people >= 100) and 
    ((t1.id - t2.id = 1 and t1.id - t3.id = 2 and t2.id - t3.id =1) or
    (t2.id - t1.id = 1 and t2.id - t3.id = 2 and t1.id - t3.id =1) or
    (t3.id - t2.id = 1 and t2.id - t1.id =1 and t3.id - t1.id = 2))
order by t1.id

# V1'
# https://medium.com/data-science-for-kindergarten/leetcode-mysql-601-human-traffic-of-stadium-9bbfb30240de
# Write your MySQL query statement below
SELECT distinct tb1.id as id, tb1.visit_date, tb1.people
FROM Stadium as tb1, Stadium as tb2, Stadium as tb3 
WHERE (tb1.people >= 100 and tb2.people >= 100 and tb3.people >= 100) and
      (
      (tb1.id+1=tb2.id and tb1.id+2=tb3.id) or
      (tb1.id-1=tb2.id and tb1.id+1=tb3.id) or
       tb1.id-1=tb2.id and tb1.id-2=tb3.id
      )
ORDER BY id

# V1''
# https://blog.csdn.net/qq_44186838/article/details/118713154#NO11__601__210

# V2