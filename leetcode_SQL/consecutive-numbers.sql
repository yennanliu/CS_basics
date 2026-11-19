/*

180. Consecutive Numbers
https://leetcode.com/problems/consecutive-numbers/
Medium

Table: Logs

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| num         | varchar |
+-------------+---------+
In SQL, id is the primary key for this table.
id is an autoincrement column starting from 1.

Find all numbers that appear at least three times consecutively.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Logs table:
+----+-----+
| id | num |
+----+-----+
| 1  | 1   |
| 2  | 1   |
| 3  | 1   |
| 4  | 2   |
| 5  | 1   |
| 6  | 2   |
| 7  | 2   |
+----+-----+
Output:
+-----------------+
| ConsecutiveNums |
+-----------------+
| 1               |
+-----------------+
Explanation: 1 is the only number that appears consecutively for at least three times.

*/

# V0
# IDEA : SELF JOIN THREE COPIES ON id, id + 1, id + 2
#
#   first, second and third are three rows with consecutive ids and the same
#   num, so first.num is a value that appears three times in a row. A run of
#   four or more matches more than once, hence DISTINCT. It relies on ids
#   having no gaps (the table says id is an autoincrement from 1).
#
# time = O(n), space = O(n)    -- n = rows in Logs; each join is a primary-key lookup
SELECT DISTINCT first.num AS ConsecutiveNums
FROM Logs first
JOIN Logs second
    ON second.id = first.id + 1
   AND second.num = first.num
JOIN Logs third
    ON third.id = first.id + 2
   AND third.num = first.num;


# V0-1
# IDEA : SELF JOIN THREE COPIES
SELECT DISTINCT
    l1.num AS ConsecutiveNums
FROM Logs l1
JOIN Logs l2
    ON l1.id = l2.id + 1
    AND l1.num = l2.num
JOIN Logs l3
    ON l1.id = l3.id - 1
    AND l1.num = l3.num;


# V1
# IDEA : WINDOW LAG -- COMPARE EACH ROW WITH THE TWO BEFORE IT
#
#   LAG(num, 1) and LAG(num, 2) over ORDER BY id put the previous two values
#   on each row; a row equal to both is the third of a run of three. Ordering
#   by id rather than id + 1 arithmetic keeps it correct even if ids had
#   gaps. On the first two rows LAG is NULL and num = NULL is never TRUE, so
#   they drop out -- correctly, since neither can end a run of three.
#
# time = O(n log n), space = O(n)    -- n = rows in Logs; the window sorts by id
WITH with_previous AS (
    SELECT
        num,
        LAG(num, 1) OVER (ORDER BY id) AS prev_num,
        LAG(num, 2) OVER (ORDER BY id) AS prev_prev_num
    FROM Logs
)
SELECT DISTINCT num AS ConsecutiveNums
FROM with_previous
WHERE num = prev_num          -- NULL on the first row: dropped
  AND num = prev_prev_num;    -- NULL on the first two rows: dropped
