/*

Table: Movies

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| movie_id      | int     |
| title         | varchar |
+---------------+---------+
movie_id is the primary key for this table.
title is the name of the movie.
Table: Users

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| user_id       | int     |
| name          | varchar |
+---------------+---------+
user_id is the primary key for this table.
Table: Movie_Rating

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| movie_id      | int     |
| user_id       | int     |
| rating        | int     |
| created_at    | date    |
+---------------+---------+
(movie_id, user_id) is the primary key for this table.
This table contains the rating of a movie by a user in their review.
created_at is the user's review date. 
Write the following SQL query:

Find the name of the user who has rated the greatest number of the movies.
In case of a tie, return lexicographically smaller user name.
Find the movie name with the highest average rating as of Feb 2020.
In case of a tie, return lexicographically smaller movie name..
Query is returned in 2 rows, the query result format is in the folowing example:

Movie table:
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

Movie_Rating table:
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

Result table:
+--------------+
| results      |
+--------------+
| Daniel       |
| Frozen 2     |
+--------------+

Daniel and Maria have rated 3 movies ("Avengers", "Frozen 2" and "Joker") but Daniel is smaller lexicographically.
Frozen 2 and Joker have a rating average of 3.5 in February but Frozen 2 is smaller lexicographically.

*/

# V0

# V1
# https://code.dennyzhang.com/movie-rating
(select name as results
from Movie_Rating inner join Users
on Movie_Rating.user_id = Users.user_id
group by Movie_Rating.user_id
order by count(1) desc, name
limit 1)
union all
(select title as results
from Movie_Rating inner join Movies
on Movie_Rating.movie_id = Movies.movie_id
where left(created_at, 7) = "2020-02"
group by Movie_Rating.movie_id
order by avg(rating) desc, title
limit 1
)

# V2
# Time:  O(nlogn), n is the number of movie_rating
# Space: O(n)
(SELECT b.name AS results 
 FROM   (SELECT a.user_id, 
                Count(*) AS cnt 
         FROM   movie_rating a 
         GROUP  BY a.user_id) a 
        INNER JOIN users b 
               ON a.user_id = b.user_id 
 ORDER  BY a.cnt DESC, 
           b.name ASC 
 LIMIT  1) 
UNION ALL 
(SELECT b.movie_name 
 FROM   (SELECT a.movie_name, 
                Avg(a.rating) AS max_rating 
         FROM   (SELECT a.rating,
                        b.title AS movie_name 
                 FROM   movie_rating a 
                        INNER JOIN movies b 
                                ON a.movie_id = b.movie_id 
                 WHERE  a.created_at BETWEEN '2020-02-01' AND '2020-02-29') a 
         GROUP  BY a.movie_name) b 
 ORDER  BY b.max_rating DESC, 
           b.movie_name ASC 
 LIMIT  1); 