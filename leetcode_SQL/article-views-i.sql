/*

1148. Article Views I
https://leetcode.com/problems/article-views-i/
Easy

Table: Views

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| article_id    | int     |
| author_id     | int     |
| viewer_id     | int     |
| view_date     | date    |
+---------------+---------+
There is no primary key (column with unique values) for this table, the table may have duplicate rows.
Each row of this table indicates that some viewer viewed an article (written by some author) on some date.
Note that equal author_id and viewer_id indicate the same person.

Write a solution to find all the authors that viewed at least one of their own articles.

Return the result table sorted by id in ascending order.

The result format is in the following example.

Example 1:

Input:
Views table:
+------------+-----------+-----------+------------+
| article_id | author_id | viewer_id | view_date  |
+------------+-----------+-----------+------------+
| 1          | 3         | 5         | 2019-08-01 |
| 1          | 3         | 6         | 2019-08-02 |
| 2          | 7         | 7         | 2019-08-01 |
| 2          | 7         | 6         | 2019-08-02 |
| 4          | 7         | 1         | 2019-07-22 |
| 3          | 4         | 4         | 2019-07-21 |
| 3          | 4         | 4         | 2019-07-21 |
+------------+-----------+-----------+------------+
Output:
+------+
| id   |
+------+
| 4    |
| 7    |
+------+

*/

# V0
# IDEA : SELF-VIEW FILTER, DISTINCT, SORT
#
#   An author viewed their own article when author_id = viewer_id on the same
#   row. The table has duplicate rows and an author may view several of their
#   articles, so DISTINCT collapses each author to one row. The task asks for
#   ascending id, hence ORDER BY.
#
# time = O(n log n), space = O(n)    -- n = rows in Views; one scan, then dedup and sort the matches
SELECT DISTINCT author_id AS id
FROM Views
WHERE author_id = viewer_id
ORDER BY id;

# V1
# IDEA : GROUP BY INSTEAD OF DISTINCT
#
#   Grouping the matching rows by author gives one row per author -- the same
#   result as DISTINCT, and the form to reach for once a count or other
#   aggregate per author is needed.
#
# time = O(n log n), space = O(n)    -- n = rows in Views; one scan, then group and sort
SELECT author_id AS id
FROM Views
WHERE author_id = viewer_id
GROUP BY author_id
ORDER BY id;
