/*

626. Exchange Seats
https://leetcode.com/problems/exchange-seats/
Medium

Table: Seat

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| student     | varchar |
+-------------+---------+
id is the primary key (unique value) column for this table.
Each row of this table indicates the name and the ID of a student.
The ID sequence always starts from 1 and increments continuously.

Write a solution to swap the seat id of every two consecutive students. If the number of students is odd, the id of the last student is not swapped.

Return the result table ordered by id in ascending order.

The result format is in the following example.

Example 1:

Input:
Seat table:
+----+---------+
| id | student |
+----+---------+
| 1  | Abbot   |
| 2  | Doris   |
| 3  | Emerson |
| 4  | Green   |
| 5  | Jeames  |
+----+---------+
Output:
+----+---------+
| id | student |
+----+---------+
| 1  | Doris   |
| 2  | Abbot   |
| 3  | Green   |
| 4  | Emerson |
| 5  | Jeames  |
+----+---------+
Explanation:
Note that if the number of students is odd, there is no need to change the last one's seat.

*/

# V0
# IDEA : RENUMBER WITH CASE, USING THE ROW COUNT
#
#   Keep each student and change their id instead: an odd id moves to id + 1,
#   an even id to id - 1. The one exception is the last student when the
#   count is odd -- there is no id + 1 to swap with, so they keep their id.
#   The count comes from a one-row CTE joined to every seat.
#
# time = O(n log n), space = O(n)    -- n = rows in Seat; one count, one scan, sort by the new id
WITH seat_count AS (
    SELECT COUNT(*) AS total
    FROM Seat
)
SELECT
    CASE
        WHEN s.id % 2 = 1 AND s.id = c.total THEN s.id      -- odd last seat: stays
        WHEN s.id % 2 = 1                    THEN s.id + 1  -- odd: swaps with the next
        ELSE s.id - 1                                       -- even: swaps with the previous
    END AS id,
    s.student
FROM Seat s
CROSS JOIN seat_count c
ORDER BY id;

# V1
# IDEA : WINDOW FUNCTIONS LEAD / LAG
#
#   Keep the ids and swap the names instead: an odd seat takes the next
#   student (LEAD), an even seat takes the previous one (LAG). LEAD is NULL
#   on the last row, so COALESCE keeps the odd last student in place.
#
# time = O(n log n), space = O(n)    -- n = rows in Seat; the window sorts by id
SELECT
    id,
    CASE
        WHEN id % 2 = 1 THEN COALESCE(LEAD(student) OVER (ORDER BY id), student)  -- NULL past the last row
        ELSE LAG(student) OVER (ORDER BY id)
    END AS student
FROM Seat
ORDER BY id;

# V2
# IDEA : SELF LEFT JOIN TO THE PARTNER SEAT
#
#   Each seat's partner is id + 1 (odd) or id - 1 (even). LEFT JOIN so the odd
#   last seat, whose partner does not exist, survives with a NULL partner;
#   COALESCE then keeps its own student.
#
# time = O(n log n), space = O(n)    -- n = rows in Seat; one PK lookup per seat, then the sort
SELECT
    s.id,
    COALESCE(partner.student, s.student) AS student
FROM Seat s
LEFT JOIN Seat partner
       ON partner.id = CASE
                           WHEN s.id % 2 = 1 THEN s.id + 1
                           ELSE s.id - 1
                       END
ORDER BY s.id;
