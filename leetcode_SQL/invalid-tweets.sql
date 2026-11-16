/*

1683. Invalid Tweets
https://leetcode.com/problems/invalid-tweets/
Easy

Table: Tweets

+----------------+---------+
| Column Name    | Type    |
+----------------+---------+
| tweet_id       | int     |
| content        | varchar |
+----------------+---------+
tweet_id is the primary key (column with unique values) for this table.
content consists of alphanumeric characters, '!', or ' ' and no other special characters.
This table contains all the tweets in a social media app.

Write a solution to find the IDs of the invalid tweets. The tweet is invalid if the number of characters used in the content of the tweet is strictly greater than 15.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Tweets table:
+----------+-----------------------------------+
| tweet_id | content                           |
+----------+-----------------------------------+
| 1        | Let us Code                       |
| 2        | More than fifteen chars are here! |
+----------+-----------------------------------+
Output:
+----------+
| tweet_id |
+----------+
| 2        |
+----------+
Explanation:
Tweet 1 has length = 11. It is a valid tweet.
Tweet 2 has length = 33. It is an invalid tweet.

*/

# V0
# IDEA : FILTER ON LENGTH
#
#   A tweet is invalid when its content is longer than 15 characters --
#   "strictly greater", so > 15, not >= 15.
#   In MySQL, LENGTH counts bytes and CHAR_LENGTH counts characters. They agree
#   here because content is limited to alphanumerics, '!' and ' ' (one byte
#   each); on text with multi-byte characters use CHAR_LENGTH(content) > 15.
#
# time = O(n * L), space = O(n)    -- n = rows in Tweets, L = longest content; each row's length is measured once
SELECT tweet_id
FROM Tweets
WHERE LENGTH(content) > 15;
