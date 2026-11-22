/*

1633. Percentage of Users Attended a Contest
https://leetcode.com/problems/percentage-of-users-attended-a-contest/
Easy

Table: Users

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| user_id     | int     |
| user_name   | varchar |
+-------------+---------+
user_id is the primary key (column with unique values) for this table.
Each row of this table contains the name and the id of a user.

Table: Register

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| contest_id  | int     |
| user_id     | int     |
+-------------+---------+
(contest_id, user_id) is the primary key (combination of columns with unique values) for this table.
Each row of this table contains the id of a user and the contest they registered into.

Write a solution to find the percentage of the users registered in each contest rounded to two decimals.

Return the result table ordered by percentage in descending order. In case of a tie, order it by contest_id in ascending order.

The result format is in the following example.

Example 1:

Input:
Users table:
+---------+-----------+
| user_id | user_name |
+---------+-----------+
| 6       | Alice     |
| 2       | Bob       |
| 7       | Alex      |
+---------+-----------+
Register table:
+------------+---------+
| contest_id | user_id |
+------------+---------+
| 215        | 6       |
| 209        | 2       |
| 208        | 2       |
| 210        | 6       |
| 208        | 6       |
| 209        | 7       |
| 209        | 6       |
| 215        | 7       |
| 208        | 7       |
| 210        | 2       |
| 207        | 2       |
| 210        | 7       |
+------------+---------+
Output:
+------------+------------+
| contest_id | percentage |
+------------+------------+
| 208        | 100.0      |
| 209        | 100.0      |
| 210        | 100.0      |
| 215        | 66.67      |
| 207        | 33.33      |
+------------+------------+
Explanation:
All the users registered in contests 208, 209, and 210. The percentage is 100% and we sort them in the answer table by contest_id in ascending order.
Alice and Alex registered in contest 215 and the percentage is ((2/3) * 100) = 66.67%
Bob registered in contest 207 and the percentage is ((1/3) * 100) = 33.33%

*/

# V0
# IDEA : COUNT PER CONTEST OVER A SCALAR TOTAL
#
#   The denominator is the same for every contest -- the number of users --
#   so it is one scalar subquery, not a join. (contest_id, user_id) is the
#   primary key, so COUNT(user_id) per contest already counts distinct users.
#   Ties on percentage are broken by contest_id ascending, as the task asks.
#
# time = O(m + n + k log k), space = O(k)    -- m = rows in Register, n = rows in Users, k = contests (the sort)
SELECT
    r.contest_id,
    ROUND(COUNT(r.user_id) * 100 / (SELECT COUNT(*) FROM Users), 2) AS percentage
FROM Register AS r
GROUP BY r.contest_id
ORDER BY
    percentage DESC,
    r.contest_id ASC;


# V0-1
# IDEA: CTE + ROUND (gpt)
WITH user_cnt AS (
    SELECT COUNT(*) AS cnt
    FROM Users
)

SELECT
    contest_id,
    ROUND(
        COUNT(*) * 100.0 / (SELECT cnt FROM user_cnt),
        2
    ) AS percentage
FROM Register
GROUP BY contest_id
ORDER BY percentage DESC, contest_id ASC;



# V1
# IDEA : TOTAL AS A ONE-ROW CTE, CROSS JOINED IN
#
#   Same arithmetic, with the denominator named: total_users is a one-row
#   table, so CROSS JOIN attaches it to every Register row without
#   multiplying rows. (The old V0 cross-joined the whole Users table and used
#   COUNT(DISTINCT ...) to undo the blow-up -- correct, but O(m * n) rows.)
#
# time = O(m + n + k log k), space = O(k)    -- m = rows in Register, n = rows in Users, k = contests
WITH total_users AS (
    SELECT COUNT(*) AS user_count
    FROM Users
)
SELECT
    r.contest_id,
    ROUND(COUNT(r.user_id) * 100 / t.user_count, 2) AS percentage
FROM Register AS r
CROSS JOIN total_users AS t
GROUP BY
    r.contest_id,
    t.user_count
ORDER BY
    percentage DESC,
    r.contest_id ASC;
