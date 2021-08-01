/*

1211. Queries Quality and Percentage
Table: Queries

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| query_name  | varchar |
| result      | varchar |
| position    | int     |
| rating      | int     |
+-------------+---------+
There is no primary key for this table, it may have duplicate rows.
This table contains information collected from some queries on a database.
The position column has a value from 1 to 500.
The rating column has a value from 1 to 5. Query with rating less than 3 is a poor query.
 

We define query quality as:

The average of the ratio between query rating and its position.

We also define poor query percentage as:

The percentage of all queries with rating less than 3.

Write an SQL query to find each query_name, the quality and poor_query_percentage.

Both quality and poor_query_percentage should be rounded to 2 decimal places.

The query result format is in the following example:

Queries table:
+------------+-------------------+----------+--------+
| query_name | result            | position | rating |
+------------+-------------------+----------+--------+
| Dog        | Golden Retriever  | 1        | 5      |
| Dog        | German Shepherd   | 2        | 5      |
| Dog        | Mule              | 200      | 1      |
| Cat        | Shirazi           | 5        | 2      |
| Cat        | Siamese           | 3        | 3      |
| Cat        | Sphynx            | 7        | 4      |
+------------+-------------------+----------+--------+

Result table:
+------------+---------+-----------------------+
| query_name | quality | poor_query_percentage |
+------------+---------+-----------------------+
| Dog        | 2.50    | 33.33                 |
| Cat        | 0.66    | 33.33                 |
+------------+---------+-----------------------+

Dog queries quality is ((5 / 1) + (5 / 2) + (1 / 200)) / 3 = 2.50
Dog queries poor_ query_percentage is (1 / 3) * 100 = 33.33

Cat queries quality equals ((2 / 5) + (3 / 3) + (4 / 7)) / 3 = 0.66
Cat queries poor_ query_percentage is (1 / 3) * 100 = 33.33

*/

# V0
select query_name, 
round(avg(rating/position),2) quality, 
round(( sum (CASE WHEN rating >= 3 THEN 0 ELSE 1 END ) /count(*) ) * 100, 2) poor_query_percentage
from Queries
GROUP BY query_name;

# V1
# https://blog.csdn.net/Hello_JavaScript/article/details/104712391
SELECT query_name, ROUND(AVG(unavg_quality), 2) AS quality, 
    ROUND(AVG(poor_query) * 100, 2) AS poor_query_percentage
FROM
(
SELECT query_name, rating, rating/position AS unavg_quality, 
    IF(rating >= 3, 0, 1) AS poor_query
FROM Queries
) AS temp
GROUP BY query_name;

# V1'
# https://blog.csdn.net/Hello_JavaScript/article/details/104712391
select query_name, round(avg(rating/position),2) quality, 
round((sum(if(rating<3,1,0))/count(*))*100,2) poor_query_percentage
from Queries
GROUP BY query_name;

# V2
# Time:  O(n)
# Space: O(n)
SELECT query_name,
       ROUND(AVG(rating * 100 / POSITION)) / 100 AS quality,
       ROUND(SUM(rating < 3) * 100 / COUNT(*), 2) AS poor_query_percentage
FROM Queries
GROUP BY query_name
ORDER BY NULL;