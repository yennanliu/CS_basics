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
SELECT question_id AS survey_log
FROM
(SELECT question_id,
        SUM(case when action="answer" THEN 1 ELSE 0 END) AS num_answer,
        SUM(case when action="show" THEN 1 ELSE 0 END) AS num_show,    
    FROM survey_log
    GROUP BY question_id
) AS tbl
ORDER BY (num_answer / num_show) DESC
LIMIT 1

# V0'
# IDEA : CTE + CASE
WITH _show AS (question_id,
               SUM(CASE
                       WHEN action = "show" THEN 1
                       ELSE 0
                   END) AS SHOW,
               survey_log
               GROUP BY question_id),
     _answer AS (question_id,
                 SUM(CASE
                         WHEN action = "answer" THEN 1
                         ELSE 0
                     END) AS answer,
                 survey_log
                 GROUP BY question_id),
     _count AS (COUNT(question_id) AS _count,
                survey_log)
SELECT _answer.answer / _show.show
FROM _show
INNER JOIN _answer ON _show.question_id = _answer.question_id
ORDER BY 1 DESC
LIMIT 1;


# V1
# https://zhuanlan.zhihu.com/p/258327104
SELECT question_id AS survey_log
FROM
(SELECT question_id,
        SUM(case when action="answer" THEN 1 ELSE 0 END) AS num_answer,
        SUM(case when action="show" THEN 1 ELSE 0 END) AS num_show,    
    FROM survey_log
    GROUP BY question_id
) AS tbl
ORDER BY (num_answer / num_show) DESC
LIMIT 1

# V1'
# https://zhuanlan.zhihu.com/p/258327104
SELECT question_id AS 'survey_log'
FROM survey_log
GROUP BY question_id
ORDER BY COUNT(answer_id) / COUNT(IF(action = 'show', 1, NULL)) DESC
LIMIT 1;

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