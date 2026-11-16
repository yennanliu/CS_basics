/*

620. Not Boring Movies
https://leetcode.com/problems/not-boring-movies/
Easy

Table: Cinema

+----------------+----------+
| Column Name    | Type     |
+----------------+----------+
| id             | int      |
| movie          | varchar  |
| description    | varchar  |
| rating         | float    |
+----------------+----------+
id is the primary key (column with unique values) for this table.
Each row contains information about the name of a movie, its genre, and its rating.
rating is a 2 decimal places float in the range [0, 10]

Write a solution to report the movies with an odd-numbered ID and a description that is not "boring".

Return the result table ordered by rating in descending order.

The result format is in the following example.

Example 1:

Input:
Cinema table:
+----+------------+-------------+--------+
| id | movie      | description | rating |
+----+------------+-------------+--------+
| 1  | War        | great 3D    | 8.9    |
| 2  | Science    | fiction     | 8.5    |
| 3  | irish      | boring      | 6.2    |
| 4  | Ice song   | Fantacy     | 8.6    |
| 5  | House card | Interesting | 9.1    |
+----+------------+-------------+--------+
Output:
+----+------------+-------------+--------+
| id | movie      | description | rating |
+----+------------+-------------+--------+
| 5  | House card | Interesting | 9.1    |
| 1  | War        | great 3D    | 8.9    |
+----+------------+-------------+--------+
Explanation:
We have three movies with odd-numbered IDs: 1, 3, and 5. The movie with ID = 3 is boring so we do not include it in the answer.

*/

# V0
# IDEA : FILTER ON TWO PREDICATES, THEN SORT
#
#   Odd id is id % 2 = 1; "not boring" is description != 'boring'. Both are
#   row-level filters, so one WHERE does it -- no grouping needed.
#   The task asks for rating descending, so ORDER BY is part of the answer.
#
# time = O(n log n), space = O(n)    -- n = rows in Cinema; one scan, then a sort of the survivors
SELECT
    id,
    movie,
    description,
    rating
FROM Cinema
WHERE id % 2 = 1                  -- odd-numbered id (MOD(id, 2) = 1 is the same test)
  AND description != 'boring'     -- a NULL description would be dropped by != (NULL is not TRUE)
ORDER BY rating DESC;
