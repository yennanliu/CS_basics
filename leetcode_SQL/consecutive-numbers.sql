/*

Write a SQL query to find all numbers that appear at least three times consecutively.

+----+-----+
| Id | Num |
+----+-----+
| 1  |  1  |
| 2  |  1  |
| 3  |  1  |
| 4  |  2  |
| 5  |  1  |
| 6  |  2  |
| 7  |  2  |
+----+-----+
For example, given the above Logs table, 1 is the only number that appears consecutively for at least three times.

+-----------------+
| ConsecutiveNums |
+-----------------+
| 1               |
+-----------------+

# https://leetcode.com/problems/consecutive-numbers/description/
# https://github.com/kamyu104/LeetCode/blob/master/MySQL/consecutive-numbers.sql

/* V1 */
SELECT DISTINCT b.Num AS ConsecutiveNums
FROM Logs a
INNER JOIN Logs b ON a.Id +1 = b.Id
INNER JOIN Logs c ON b.Id +1 = c.Id
WHERE a.Num = b.Num
  AND c.Num = b.Num

/* V2 */
# join 3 same table to grab 2nd element 
# (if there are at least consecutively 3 elements)
SELECT DISTINCT a.Num AS ConsecutiveNums 
FROM Logs a,
     Logs b,
     Logs c
WHERE a.Num = b.Num
  AND b.Id +1 = a.Id
  AND c.Num = a.Num
  AND c.Id = a.Id +1

/* V3  */
SELECT DISTINCT(Num) AS ConsecutiveNums
FROM (
    SELECT
    Num,
    @counter := IF(@prev = Num, @counter + 1, 1) AS how_many_cnt_in_a_row,
    @prev := Num
    FROM Logs y, (SELECT @counter:=1, @prev:=NULL) vars
) sq
WHERE how_many_cnt_in_a_row >= 3
