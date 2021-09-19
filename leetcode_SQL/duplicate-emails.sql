/* 

https://leetcode.com/problems/duplicate-emails/description/

Write a SQL query to find all duplicate emails in a table named Person.

+----+---------+
| Id | Email   |
+----+---------+
| 1  | a@b.com |
| 2  | c@d.com |
| 3  | a@b.com |
+----+---------+
For example, your query should return the following for the above table:

+---------+
| Email   |
+---------+
| a@b.com |
+---------+

*/ 

/* V0 */
select
Email
from
Person
group by Email
having count(Email) > 1

/* V1 */
SELECT Email
FROM
  (SELECT Email,
          count(*)
   FROM Person a
   GROUP BY Email
   HAVING count(*) > 1) sub

/* V2 */ 
SELECT Email FROM Person GROUP BY Email HAVING COUNT(*) > 1
