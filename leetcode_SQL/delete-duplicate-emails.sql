/*

196. Delete Duplicate Emails
https://leetcode.com/problems/delete-duplicate-emails/
Easy

Table: Person

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| email       | varchar |
+-------------+---------+
id is the primary key (column with unique values) for this table.
Each row of this table contains an email. The emails will not contain uppercase letters.

Write a solution to delete all duplicate emails, keeping only one unique email with the smallest id.

For SQL users, please note that you are supposed to write a DELETE statement and not a SELECT one.

For Pandas users, please note that you are supposed to modify Person in place.

After running your script, the answer shown is the Person table. The driver will first compile and run your piece of code and then show the Person table. The final order of the Person table does not matter.

The result format is in the following example.

Example 1:

Input:
Person table:
+----+------------------+
| id | email            |
+----+------------------+
| 1  | john@example.com |
| 2  | bob@example.com  |
| 3  | john@example.com |
+----+------------------+
Output:
+----+------------------+
| id | email            |
+----+------------------+
| 1  | john@example.com |
| 2  | bob@example.com  |
+----+------------------+
Explanation: john@example.com is repeated two times. We keep the row with the smallest Id = 1.

*/

# V0
# IDEA : SELF JOIN DELETE -- DROP EVERY ROW THAT HAS A SAME-EMAIL ROW WITH A SMALLER ID
#
#   Join Person to itself on email. A row `dup` that finds a partner `keep`
#   with the same email and a smaller id is not the smallest-id copy, so it
#   goes. The smallest-id row of each email never finds such a partner and
#   stays. MySQL's multi-table DELETE (DELETE dup FROM ... JOIN ...) has no
#   SQLite spelling, so the smoke test reports it as SKIP.
#
#   The task is a DELETE; the SELECT after it only shows the table that is
#   left, which is what LeetCode prints and what the smoke test compares.
#
# time = O(n^2), space = O(1)    -- n = rows in Person; the self join can pair every row with every other (one shared email); O(n log n) with an index on email
DELETE dup
FROM Person AS dup
JOIN Person AS keep
    ON dup.email = keep.email
   AND dup.id > keep.id;        -- a same-email row with a smaller id exists

SELECT
    id,
    email
FROM Person;

# V1
# IDEA : KEEP THE MIN(id) OF EACH EMAIL, DELETE THE REST
#
#   GROUP BY email gives each email's smallest id: the rows to keep. Delete
#   every id not in that set. MySQL refuses a subquery that reads the table
#   being deleted from (error 1093), so the keep-list is wrapped in a derived
#   table (keep_ids), which MySQL materialises before the delete starts.
#   NOT IN is NULL-safe here: id is the primary key, so MIN(id) is never NULL.
#
# time = O(n log n), space = O(n)    -- n = rows in Person; the GROUP BY on email, plus the materialised keep-list
DELETE FROM Person
WHERE id NOT IN (
    SELECT keep_id
    FROM (
        -- the smallest id of each email: the one row per email that survives
        SELECT MIN(id) AS keep_id
        FROM Person
        GROUP BY email
    ) AS keep_ids
);

SELECT
    id,
    email
FROM Person;
