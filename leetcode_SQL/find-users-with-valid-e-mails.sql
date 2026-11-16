/*

1517. Find Users With Valid E-Mails
https://leetcode.com/problems/find-users-with-valid-e-mails/
Easy

Table: Users

+---------------+---------+
| Column Name   | Type    |
+---------------+---------+
| user_id       | int     |
| name          | varchar |
| mail          | varchar |
+---------------+---------+
user_id is the primary key (column with unique values) for this table.
This table contains information of the users signed up in a website. Some e-mails are invalid.

Write a solution to find the users who have valid emails.

A valid e-mail has a prefix name and a domain where:

	- The prefix name is a string that may contain letters (upper or lower case), digits, underscore '_', period '.', and/or dash '-'. The prefix name must start with a letter.

	- The domain must be exactly '@leetcode.com' in lowercase.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Users table:
+---------+-----------+-------------------------+
| user_id | name      | mail                    |
+---------+-----------+-------------------------+
| 1       | Winston   | winston@leetcode.com    |
| 2       | Jonathan  | jonathanisgreat         |
| 3       | Annabelle | bella-@leetcode.com     |
| 4       | Sally     | sally.come@leetcode.com |
| 5       | Marwan    | quarz#2020@leetcode.com |
| 6       | David     | david69@gmail.com       |
| 7       | Shapiro   | .shapo@leetcode.com     |
+---------+-----------+-------------------------+
Output:
+---------+-----------+-------------------------+
| user_id | name      | mail                    |
+---------+-----------+-------------------------+
| 1       | Winston   | winston@leetcode.com    |
| 3       | Annabelle | bella-@leetcode.com     |
| 4       | Sally     | sally.come@leetcode.com |
+---------+-----------+-------------------------+
Explanation:
The mail of user 2 does not have a domain.
The mail of user 5 has the # sign which is not allowed.
The mail of user 6 does not have the leetcode domain.
The mail of user 7 starts with a period.

*/

# V0
# IDEA : ONE ANCHORED, CASE-SENSITIVE REGEX OVER THE WHOLE ADDRESS
#
#   ^[a-zA-Z]            the prefix starts with a letter
#   [a-zA-Z0-9_.-]*      then any letters, digits, '_', '.' or '-'
#   @leetcode[.]com$     then exactly this domain, and nothing after it
#
#   Three details each sink a version that skips them: without `$` the
#   address 'a@leetcode.com.evil' passes; an unescaped '.' in 'leetcode.com'
#   matches any character ('a@leetcodeXcom'); and MySQL's REGEXP follows the
#   column's collation, which is case-insensitive by default, so
#   'a@LEETCODE.COM' would pass. REGEXP_LIKE's 'c' flag makes the match
#   case-sensitive, as the task requires (the domain is lowercase only).
#   A NULL mail makes REGEXP_LIKE return NULL, and WHERE drops the row.
#
# time = O(n * L), space = O(n)    -- n = rows in Users, L = length of mail; one regex match per row
SELECT
    user_id,
    name,
    mail
FROM Users
WHERE REGEXP_LIKE(mail, '^[a-zA-Z][a-zA-Z0-9_.-]*@leetcode[.]com$', 'c');
