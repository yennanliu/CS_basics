/*

1341. Movie Rating
https://leetcode.com/problems/movie-rating/
Medium

Table: Movies

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| movie_id      | int     |
| title         | varchar |
+---------------+---------+
movie_id is the primary key (column with unique values) for this table.
title is the name of the movie.
Each movie has a unique title.

Table: Users

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| user_id       | int     |
| name          | varchar |
+---------------+---------+
user_id is the primary key (column with unique values) for this table.
The column 'name' has unique values.

Table: MovieRating

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| movie_id      | int     |
| user_id       | int     |
| rating        | int     |
| created_at    | date    |
+---------------+---------+
(movie_id, user_id) is the primary key (column with unique values) for this table.
This table contains the rating of a movie by a user in their review.
created_at is the user's review date.

Write a solution to:

	- Find the name of the user who has rated the greatest number of movies. In case of a tie, return the lexicographically smaller user name.

	- Find the movie name with the highest average rating in February 2020. In case of a tie, return the lexicographically smaller movie name.

The result format is in the following example.

Example 1:

Input:
Movies table:
+-------------+--------------+
| movie_id    |  title       |
+-------------+--------------+
| 1           | Avengers     |
| 2           | Frozen 2     |
| 3           | Joker        |
+-------------+--------------+
Users table:
+-------------+--------------+
| user_id     |  name        |
+-------------+--------------+
| 1           | Daniel       |
| 2           | Monica       |
| 3           | Maria        |
| 4           | James        |
+-------------+--------------+
MovieRating table:
+-------------+--------------+--------------+-------------+
| movie_id    | user_id      | rating       | created_at  |
+-------------+--------------+--------------+-------------+
| 1           | 1            | 3            | 2020-01-12  |
| 1           | 2            | 4            | 2020-02-11  |
| 1           | 3            | 2            | 2020-02-12  |
| 1           | 4            | 1            | 2020-01-01  |
| 2           | 1            | 5            | 2020-02-17  |
| 2           | 2            | 2            | 2020-02-01  |
| 2           | 3            | 2            | 2020-03-01  |
| 3           | 1            | 3            | 2020-02-22  |
| 3           | 2            | 4            | 2020-02-25  |
+-------------+--------------+--------------+-------------+
Output:
+--------------+
| results      |
+--------------+
| Daniel       |
| Frozen 2     |
+--------------+
Explanation:
Daniel and Monica have rated 3 movies ("Avengers", "Frozen 2" and "Joker") but Daniel is smaller lexicographically.
Frozen 2 and Joker have a rating average of 3.5 in February but Frozen 2 is smaller lexicographically.

*/

# V0
# IDEA : TWO TOP-1 QUERIES, STACKED WITH UNION ALL
#
#   Each half is "group, sort by the measure then by name, take one row";
#   the name in ORDER BY is the tie-break the problem asks for.
#   UNION ALL, not UNION: if the top user's name equals the top movie's
#   title, UNION would collapse the two rows into one.
#   February is a half-open range on created_at, so no date function is
#   needed and Feb 29 is included.
#
# time = O(m log m), space = O(m)    -- m = rows in MovieRating; two GROUP BYs and two sorts
WITH top_user AS (
    -- the user with the most ratings; ties -> smaller name
    SELECT u.name AS results
    FROM MovieRating mr
    JOIN Users u
      ON u.user_id = mr.user_id
    GROUP BY u.user_id, u.name
    ORDER BY COUNT(*) DESC, u.name
    LIMIT 1
),
top_movie AS (
    -- the movie with the highest February 2020 average; ties -> smaller title
    SELECT m.title AS results
    FROM MovieRating mr
    JOIN Movies m
      ON m.movie_id = mr.movie_id
    WHERE mr.created_at >= '2020-02-01'
      AND mr.created_at <  '2020-03-01'
    GROUP BY m.movie_id, m.title
    ORDER BY AVG(mr.rating) DESC, m.title
    LIMIT 1
)
SELECT results FROM top_user
UNION ALL
SELECT results FROM top_movie;


# V0-1
# IDEA: UNION ALL + CTE (gpt)
WITH sub_1 AS (
    SELECT
        u.user_id,
        u.name,
        COUNT(*) AS cnt
    FROM Users u
    INNER JOIN MovieRating mr
        ON u.user_id = mr.user_id
    GROUP BY
        u.user_id,
        u.name
    ORDER BY
        cnt DESC,
        u.name ASC
    LIMIT 1
),

sub_2 AS (
    SELECT
        m.movie_id,
        m.title,
        AVG(mr.rating) AS avg_rating
    FROM MovieRating mr
    INNER JOIN Movies m
        ON mr.movie_id = m.movie_id
    WHERE
        mr.created_at >= '2020-02-01'
        AND mr.created_at < '2020-03-01'
    GROUP BY
        m.movie_id,
        m.title
    ORDER BY
        avg_rating DESC,
        m.title ASC
    LIMIT 1
)

SELECT name AS results
FROM sub_1

UNION ALL

SELECT title AS results
FROM sub_2;


# V0-1
# IDEA: UNION ALL + CTE (gemini)
WITH sub_1 AS (
    SELECT
        u.name AS results
    FROM Users u
    INNER JOIN MovieRating mr
        ON u.user_id = mr.user_id
    GROUP BY u.user_id, u.name
    ORDER BY COUNT(mr.movie_id) DESC, u.name ASC
    LIMIT 1
),
sub_2 AS (
    SELECT
        m.title AS results
    FROM MovieRating mr
    INNER JOIN Movies m
        ON mr.movie_id = m.movie_id
    WHERE mr.created_at >= '2020-02-01'
      AND mr.created_at < '2020-03-01'
    GROUP BY m.movie_id, m.title
    ORDER BY AVG(mr.rating) DESC, m.title ASC
    LIMIT 1
)
SELECT results FROM sub_1
UNION ALL
SELECT results FROM sub_2;


# V1
# IDEA : RANK EACH SIDE WITH ROW_NUMBER, KEEP ROW 1
#
#   Same two answers, but "take one" is a window function instead of LIMIT,
#   so each CTE holds every user / movie with its position. ROW_NUMBER, not
#   RANK, because exactly one row must win; the name in the window's ORDER BY
#   breaks ties.
#
# time = O(m log m), space = O(m)    -- m = rows in MovieRating; the windows sort the grouped rows
WITH user_ranked AS (
    SELECT
        u.name,
        ROW_NUMBER() OVER (ORDER BY COUNT(*) DESC, u.name) AS rn
    FROM MovieRating mr
    JOIN Users u
      ON u.user_id = mr.user_id
    GROUP BY u.user_id, u.name
),
movie_ranked AS (
    SELECT
        m.title,
        ROW_NUMBER() OVER (ORDER BY AVG(mr.rating) DESC, m.title) AS rn
    FROM MovieRating mr
    JOIN Movies m
      ON m.movie_id = mr.movie_id
    WHERE mr.created_at >= '2020-02-01'
      AND mr.created_at <  '2020-03-01'
    GROUP BY m.movie_id, m.title
)
SELECT name AS results FROM user_ranked WHERE rn = 1
UNION ALL
SELECT title AS results FROM movie_ranked WHERE rn = 1;
