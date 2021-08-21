/*

Table: Sessions

+---------------------+---------+
| Column Name         | Type    |
+---------------------+---------+
| session_id          | int     |
| duration            | int     |
+---------------------+---------+
session_id is the primary key for this table.
duration is the time in seconds that a user has visited the application.
You want to know how long a user visits your application. You need to create bins of “[0-5>”, “[5-10>”, “[10-15>” and “15 minutes or more” and count the number of sessions on it.

Write an SQL query to report the (bin, total) in any order.

The query result format is in the following example.

Sessions table:

+-------------+---------------+
| session_id  | duration      |
+-------------+---------------+
| 1           | 30            |
| 2           | 299           |
| 3           | 340           |
| 4           | 580           |
| 5           | 1000          |
+-------------+---------------+
Result table:

+--------------+--------------+
| bin          | total        |
+--------------+--------------+
| [0-5>        | 3            |
| [5-10>       | 1            |
| [10-15>      | 0            |
| 15 or more   | 1            |
+--------------+--------------+

For session_id 1, 2 and 3 have a duration greater or equal than 0 minutes and less than 5 minutes.
For session_id 4 has a duration greater or equal than 5 minutes and less than 10 minutes.
There are no session with a duration greater or equial than 10 minutes and less than 15 minutes.
For session_id 5 has a duration greater or equal than 15 minutes.

*/


# V0

# V1
# https://code.dennyzhang.com/create-a-session-bar-chart
select '[0-5>' as bin, count(1) as total
from Sessions
where duration>=0 and duration < 300
union
select '[5-10>' as bin, count(1) as total
from Sessions
where duration>=300 and duration < 600
union
select '[10-15>' as bin, count(1) as total
from Sessions
where duration>=600 and duration < 900
union
select '15 or more' as bin, count(1) as total
from Sessions
where duration >= 900

# V2
# Time:  O(n)
# Space: O(1)
SELECT '[0-5>'  AS bin, 
       Count(1) AS total 
FROM   sessions 
WHERE  duration >= 0 
       AND duration < 300 
UNION ALL
SELECT '[5-10>' AS bin, 
       Count(1) AS total 
FROM   sessions 
WHERE  duration >= 300 
       AND duration < 600 
UNION ALL 
SELECT '[10-15>' AS bin, 
       Count(1)  AS total 
FROM   sessions 
WHERE  duration >= 600 
       AND duration < 900 
UNION ALL 
SELECT '15 or more' AS bin, 
       Count(1)     AS total 
FROM   sessions 
WHERE  duration >= 900;

# Time:  O(n)
# Space: O(n)
SELECT
    t2.BIN,
    COUNT(t1.BIN) AS TOTAL
FROM (
SELECT
    CASE 
        WHEN duration/60 BETWEEN 0 AND 5 THEN "[0-5>"
        WHEN duration/60 BETWEEN 5 AND 10 THEN "[5-10>"
        WHEN duration/60 BETWEEN 10 AND 15 THEN "[10-15>"
        WHEN duration/60 >= 15 THEN "15 or more" 
        ELSE NULL END AS BIN
FROM Sessions
) t1 
RIGHT JOIN(
    SELECT "[0-5>"        AS BIN
    UNION ALL
    SELECT "[5-10>"       AS BIN
    UNION ALL
    SELECT "[10-15>"      AS BIN
    UNION ALL
    SELECT "15 or more"   AS BIN
) t2
ON t1.bin = t2.bin
GROUP BY t2.bin
ORDER BY NULL;