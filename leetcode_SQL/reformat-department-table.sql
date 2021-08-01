/*

SQL Schema
Table: Department

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| id            | int     |
| revenue       | int     |
| month         | varchar |
+---------------+---------+
(id, month) is the primary key of this table.
The table has information about the revenue of each department per month.
The month has values in ["Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"].
 

Write an SQL query to reformat the table such that there is a department id column and a revenue column for each month.

The query result format is in the following example:

Department table:
+------+---------+-------+
| id   | revenue | month |
+------+---------+-------+
| 1    | 8000    | Jan   |
| 2    | 9000    | Jan   |
| 3    | 10000   | Feb   |
| 1    | 7000    | Feb   |
| 1    | 6000    | Mar   |
+------+---------+-------+

Result table:
+------+-------------+-------------+-------------+-----+-------------+
| id   | Jan_Revenue | Feb_Revenue | Mar_Revenue | ... | Dec_Revenue |
+------+-------------+-------------+-------------+-----+-------------+
| 1    | 8000        | 7000        | 6000        | ... | null        |
| 2    | 9000        | null        | null        | ... | null        |
| 3    | null        | 10000       | null        | ... | null        |
+------+-------------+-------------+-------------+-----+-------------+

Note that the result table has 13 columns (1 for the department id + 12 for the months).

*/

# V0
select 
    id,
       sum(case when month = 'jan' then revenue else null end) as Jan_Revenue,
       sum(case when month = 'feb' then revenue else null end) as Feb_Revenue,
       sum(case when month = 'mar' then revenue else null end) as Mar_Revenue,
       sum(case when month = 'apr' then revenue else null end) as Apr_Revenue,
       sum(case when month = 'may' then revenue else null end) as May_Revenue,
       sum(case when month = 'jun' then revenue else null end) as Jun_Revenue,
       sum(case when month = 'jul' then revenue else null end) as Jul_Revenue,
       sum(case when month = 'aug' then revenue else null end) as Aug_Revenue,
       sum(case when month = 'sep' then revenue else null end) as Sep_Revenue,
       sum(case when month = 'oct' then revenue else null end) as Oct_Revenue,
       sum(case when month = 'nov' then revenue else null end) as Nov_Revenue,
       sum(case when month = 'dec' then revenue else null end) as Dec_Revenue
from department
group by id
order by id asc

# V1
# https://cakebytheoceanluo.github.io/2020/03/13/SQL-Leetcode-1179-reformat-department-table/
select 
    id,
       sum(case when month = 'jan' then revenue else null end) as Jan_Revenue,
       sum(case when month = 'feb' then revenue else null end) as Feb_Revenue,
       sum(case when month = 'mar' then revenue else null end) as Mar_Revenue,
       sum(case when month = 'apr' then revenue else null end) as Apr_Revenue,
       sum(case when month = 'may' then revenue else null end) as May_Revenue,
       sum(case when month = 'jun' then revenue else null end) as Jun_Revenue,
       sum(case when month = 'jul' then revenue else null end) as Jul_Revenue,
       sum(case when month = 'aug' then revenue else null end) as Aug_Revenue,
       sum(case when month = 'sep' then revenue else null end) as Sep_Revenue,
       sum(case when month = 'oct' then revenue else null end) as Oct_Revenue,
       sum(case when month = 'nov' then revenue else null end) as Nov_Revenue,
       sum(case when month = 'dec' then revenue else null end) as Dec_Revenue
from department
group by id
order by id asc

# V2
# Time:  O(n)
# Space: O(n)
SELECT id, 
       SUM(IF(month = 'Jan', revenue, NULL)) AS Jan_Revenue, 
       SUM(IF(month = 'Feb', revenue, NULL)) AS Feb_Revenue, 
       SUM(IF(month = 'Mar', revenue, NULL)) AS Mar_Revenue, 
       SUM(IF(month = 'Apr', revenue, NULL)) AS Apr_Revenue, 
       SUM(IF(month = 'May', revenue, NULL)) AS May_Revenue, 
       SUM(IF(month = 'Jun', revenue, NULL)) AS Jun_Revenue, 
       SUM(IF(month = 'Jul', revenue, NULL)) AS Jul_Revenue, 
       SUM(IF(month = 'Aug', revenue, NULL)) AS Aug_Revenue, 
       SUM(IF(month = 'Sep', revenue, NULL)) AS Sep_Revenue, 
       SUM(IF(month = 'Oct', revenue, NULL)) AS Oct_Revenue, 
       SUM(IF(month = 'Nov', revenue, NULL)) AS Nov_Revenue, 
       SUM(IF(month = 'Dec', revenue, NULL)) AS Dec_Revenue 
FROM   department 
GROUP  BY id;