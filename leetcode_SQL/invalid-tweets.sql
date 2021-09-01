/*

Invalid Tweets Problem

Description
LeetCode Problem 1683.

Table: Tweets

+----------------+---------+
| Column Name    | Type    |
+----------------+---------+
| tweet_id       | int     |
| content        | varchar |
+----------------+---------+
tweet_id is the primary key for this table.
This table contains all the tweets in a social media app.
Write an SQL query to find the IDs of the invalid tweets. The tweet is invalid if the number of characters used in the content of the tweet is strictly greater than 15.

Return the result table in any order.

The query result format is in the following example:

Tweets table:
+----------+----------------------------------+
| tweet_id | content                          |
+----------+----------------------------------+
| 1        | Vote for Biden                   |
| 2        | Let us make America great again! |
+----------+----------------------------------+

Result table:
+----------+
| tweet_id |
+----------+
| 2        |
+----------+
Tweet 1 has length = 14. It is a valid tweet.
Tweet 2 has length = 32. It is an invalid tweet.

*/

# V0
select tweet_id
from Tweets
where length(content) > 15;

# V1
# https://circlecoder.com/invalid-tweets/
select tweet_id
from Tweets
where length(content) > 15;

# V2
# Time:  O(n)
# Space: O(n)
SELECT tweet_id
FROM Tweets
WHERE CHAR_LENGTH(content) > 15;