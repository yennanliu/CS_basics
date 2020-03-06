-- LeetCode 578. Get Highest Answer Rate Question

-- Get the highest answer rate question from a table survey_log with these columns: uid, action, question_id, answer_id, q_num, timestamp.

-- uid means user id; action has these kind of values: "show", "answer", "skip"; answer_id is not null when action column is "answer", while are null for "show" and "skip"; q_num is the numeral/order of the question in current session.

-- Write a sql to identify the question which has the highest answer rate.

-- Example:

-- Input:
-- +------+-----------+--------------+------------+-----------+------------+
-- | uid  | action    | question_id  | answer_id  | q_num     | timestamp  |
-- +------+-----------+--------------+------------+-----------+------------+
-- | 5    | show      | 285          | null       | 1         | 123        |
-- | 5    | answer    | 285          | 124124     | 1         | 124        |
-- | 5    | show      | 369          | null       | 2         | 125        |
-- | 5    | skip      | 369          | null       | 2         | 126        |
-- +------+-----------+--------------+------------+-----------+------------+
-- Output:
-- +-------------+
-- | survey_log  |
-- +-------------+
-- |    285      |
-- +-------------+
-- Explanation:
-- question 285 has answer rate 1/1, while question 369 has 0/1 answer rate, so output 285.
-- Note: The highest answer rate meaning is: answer number's ratio in show number in the same question.

# V0
# NOTE : THERE IS NO "FULL OUTER JOIN" IN MYSQL
#        SO WE USE "RIGHT JOIN" + "LEFT JOIN" AS WORKAROUND HERE
## Neet to validate
SELECT question_id AS survey_log FROM
    (SELECT question_id, COUNT(*) AS answer_count
    FROM survey_log
    WHERE ACTION = 'answer'
    GROUP BY question_id) a
    RIGHT JOIN
    (SELECT question_id, COUNT(*) AS action_count
    FROM survey_log
    GROUP BY question_id) b
    USING on a.question_id = b.question_id
    LEFT JOIN
    (SELECT question_id, COUNT(*) AS show_count
    FROM survey_log
    WHERE ACTION = 'show'
    GROUP BY question_id) c
    USING on b.question_id = c.question_id
ORDER BY answer_count / show_count DESC
LIMIT 1

-- V1 
-- http://bookshadow.com/weblog/2017/05/09/leetcode-get-highest-answer-rate-question/
-- Write your MySQL query statement below
SELECT question_id AS survey_log FROM
    (SELECT question_id, COUNT(*) AS answer_count
    FROM survey_log
    WHERE ACTION = 'answer'
    GROUP BY question_id) a
    RIGHT JOIN
    (SELECT question_id, COUNT(*) AS action_count
    FROM survey_log
    GROUP BY question_id) b
    USING (question_id)
    LEFT JOIN
    (SELECT question_id, COUNT(*) AS show_count
    FROM survey_log
    WHERE ACTION = 'show'
    GROUP BY question_id) c
    USING (question_id)
ORDER BY answer_count / show_count DESC
LIMIT 1


# V2