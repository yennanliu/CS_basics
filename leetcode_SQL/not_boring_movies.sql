/*

620. Not Boring Movies
Easy

SQL Schema
Table: Cinema

+----------------+----------+
| Column Name    | Type     |
+----------------+----------+
| id             | int      |
| movie          | varchar  |
| description    | varchar  |
| rating         | float    |
+----------------+----------+
id is the primary key for this table.
Each row contains information about the name of a movie, its genre, and its rating.
rating is a 2 decimal places float in the range [0, 10]
 

Write an SQL query to report the movies with an odd-numbered ID and a description that is not "boring".

Return the result table in descending order by rating.

The query result format is in the following example:

 

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

Result table:
+----+------------+-------------+--------+
| id | movie      | description | rating |
+----+------------+-------------+--------+
| 5  | House card | Interesting | 9.1    |
| 1  | War        | great 3D    | 8.9    |
+----+------------+-------------+--------+

We have three movies with odd-numbered ID: 1, 3, and 5. The movie with ID = 3 is boring so we don't include it in the answer.

*/

# V0
select *
from cinema
where id % 2 = 1 and description != 'boring'
order by rating DESC

# V1
# https://leetcode.com/problems/not-boring-movies/solution/
select *
from cinema
where mod(id, 2) = 1 and description != 'boring'
order by rating DESC

# V2