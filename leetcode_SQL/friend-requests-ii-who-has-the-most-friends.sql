/*

602. Friend Requests II: Who Has the Most Friends
https://leetcode.com/problems/friend-requests-ii-who-has-the-most-friends/
Medium

Table: RequestAccepted

+----------------+---------+
| Column Name    | Type    |
+----------------+---------+
| requester_id   | int     |
| accepter_id    | int     |
| accept_date    | date    |
+----------------+---------+
(requester_id, accepter_id) is the primary key (combination of columns with unique values) for this table.
This table contains the ID of the user who sent the request, the ID of the user who received the request, and the date when the request was accepted.

Write a solution to find the people who have the most friends and the most friends number.

The test cases are generated so that only one person has the most friends.

The result format is in the following example.

Example 1:

Input:
RequestAccepted table:
+--------------+-------------+-------------+
| requester_id | accepter_id | accept_date |
+--------------+-------------+-------------+
| 1            | 2           | 2016/06/03  |
| 1            | 3           | 2016/06/08  |
| 2            | 3           | 2016/06/08  |
| 3            | 4           | 2016/06/09  |
+--------------+-------------+-------------+
Output:
+----+-----+
| id | num |
+----+-----+
| 3  | 3   |
+----+-----+
Explanation:
The person with id 3 is a friend of people 1, 2, and 4, so he has three friends in total, which is the most number than any others.

Follow up: In the real world, multiple people could have the same most number of friends. Could you find all these people in this case?

*/

# V0
# IDEA : UNION ALL BOTH SIDES, COUNT, TAKE THE TOP
#
#   An accepted request makes both people one friend richer, so stack the
#   requester column on the accepter column and count each id. UNION ALL,
#   not UNION: UNION would collapse every repeat of an id into one row and
#   every count would be 1. LIMIT 1 is enough because the problem promises a
#   single winner.
#
# time = O(n log n), space = O(n)    -- n = rows in RequestAccepted; 2n ids grouped, then sorted
WITH all_ids AS (
    SELECT requester_id AS id FROM RequestAccepted
    UNION ALL
    SELECT accepter_id  AS id FROM RequestAccepted
),
friend_counts AS (
    SELECT id, COUNT(*) AS num
    FROM all_ids
    GROUP BY id
)
SELECT id, num
FROM friend_counts
ORDER BY num DESC
LIMIT 1;

# V1
# IDEA : RANK THE COUNTS (FOLLOW-UP: EVERY PERSON TIED FOR THE MOST)
#
#   Same counts, then RANK() over num descending and keep rank 1. RANK, not
#   ROW_NUMBER, so every person tied at the top shares rank 1 -- which is
#   the follow-up's answer, and the same single row when there is no tie.
#
# time = O(n log n), space = O(n)    -- n = rows in RequestAccepted; the window sorts the counts
WITH all_ids AS (
    SELECT requester_id AS id FROM RequestAccepted
    UNION ALL
    SELECT accepter_id  AS id FROM RequestAccepted
),
friend_counts AS (
    SELECT
        id,
        COUNT(*)                             AS num,
        RANK() OVER (ORDER BY COUNT(*) DESC) AS rnk
    FROM all_ids
    GROUP BY id
)
SELECT id, num
FROM friend_counts
WHERE rnk = 1;
