/*

1204. Last Person to Fit in the Bus
https://leetcode.com/problems/last-person-to-fit-in-the-bus/
Medium

Table: Queue

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| person_id   | int     |
| person_name | varchar |
| weight      | int     |
| turn        | int     |
+-------------+---------+
person_id column contains unique values.
This table has the information about all people waiting for a bus.
The person_id and turn columns will contain all numbers from 1 to n, where n is the number of rows in the table.
turn determines the order of which the people will board the bus, where turn=1 denotes the first person to board and turn=n denotes the last person to board.
weight is the weight of the person in kilograms.

There is a queue of people waiting to board a bus. However, the bus has a weight limit of 1000 kilograms, so there may be some people who cannot board.

Write a solution to find the person_name of the last person that can fit on the bus without exceeding the weight limit. The test cases are generated such that the first person does not exceed the weight limit.

Note that only one person can board the bus at any given turn.

The result format is in the following example.

Example 1:

Input:
Queue table:
+-----------+-------------+--------+------+
| person_id | person_name | weight | turn |
+-----------+-------------+--------+------+
| 5         | Alice       | 250    | 1    |
| 4         | Bob         | 175    | 5    |
| 3         | Alex        | 350    | 2    |
| 6         | John Cena   | 400    | 3    |
| 1         | Winston     | 500    | 6    |
| 2         | Marie       | 200    | 4    |
+-----------+-------------+--------+------+
Output:
+-------------+
| person_name |
+-------------+
| John Cena   |
+-------------+
Explanation: The folowing table is ordered by the turn for simplicity.
+------+----+-----------+--------+--------------+
| Turn | ID | Name      | Weight | Total Weight |
+------+----+-----------+--------+--------------+
| 1    | 5  | Alice     | 250    | 250          |
| 2    | 3  | Alex      | 350    | 600          |
| 3    | 6  | John Cena | 400    | 1000         | (last person to board)
| 4    | 2  | Marie     | 200    | 1200         | (cannot board)
| 5    | 4  | Bob       | 175    | ___          |
| 6    | 1  | Winston   | 500    | ___          |
+------+----+-----------+--------+--------------+

*/

# V0
# IDEA : RUNNING SUM WINDOW, LAST TURN WITHIN 1000
#
#   SUM(weight) OVER (ORDER BY turn) is the total weight on the bus after each
#   person boards. The people who fit are those whose running total is
#   <= 1000 (exactly 1000 still fits); the answer is the one with the highest
#   turn. turn is unique, so the default RANGE frame equals ROWS and
#   ORDER BY turn DESC LIMIT 1 has no tie.
#
# time = O(n log n), space = O(n)    -- n = rows in Queue; the window sorts by turn
WITH boarding AS (
    SELECT
        person_name,
        turn,
        SUM(weight) OVER (ORDER BY turn) AS total_weight
    FROM Queue
)
SELECT person_name
FROM boarding
WHERE total_weight <= 1000
ORDER BY turn DESC
LIMIT 1;

# V1
# IDEA : CORRELATED SUBQUERY FOR THE RUNNING TOTAL
#
#   For each person, sum the weights of everyone who boards at or before
#   their turn. Same filter and pick as V0, without a window function -- the
#   answer on a MySQL older than 8.0.
#
# time = O(n^2), space = O(n)    -- n = rows in Queue; one subquery scan per row
SELECT q.person_name
FROM Queue q
WHERE (
    -- total weight once q has boarded
    SELECT SUM(earlier.weight)
    FROM Queue earlier
    WHERE earlier.turn <= q.turn
) <= 1000
ORDER BY q.turn DESC
LIMIT 1;

# V2
# IDEA : SELF JOIN ON EARLIER TURNS, GROUP BY + HAVING
#
#   Join each person to everyone at or before their turn, group by the
#   person, and keep the groups whose summed weight is within the limit.
#   The same running total as V1, written as a join instead of a subquery.
#
# time = O(n^2), space = O(n^2)    -- n = rows in Queue; the join produces up to n^2 / 2 pairs
SELECT q.person_name
FROM Queue q
JOIN Queue earlier
    ON earlier.turn <= q.turn
GROUP BY
    q.person_id,
    q.person_name,
    q.turn
HAVING SUM(earlier.weight) <= 1000
ORDER BY q.turn DESC
LIMIT 1;
