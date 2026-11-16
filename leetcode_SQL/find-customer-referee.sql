/*

584. Find Customer Referee
Easy

Table: Customer

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| id          | int     |
| name        | varchar |
| referee_id  | int     |
+-------------+---------+
id is the primary key column for this table.
Each row of this table indicates the id of a customer, their name, and the id of the customer who referred them.

Find the names of the customer that are not referred by the customer with id = 2.

Return the result table in any order.

Example 1:

Input:
Customer table:
+----+------+------------+
| id | name | referee_id |
+----+------+------------+
| 1  | Will | null       |
| 2  | Jane | null       |
| 3  | Alex | 2          |
| 4  | Bill | null       |
| 5  | Zack | 1          |
| 6  | Mark | 2          |
+----+------+------------+

Output:
+------+
| name |
+------+
| Will |
| Jane |
| Bill |
| Zack |
+------+

*/

# V0
# IDEA : FILTER WITH AN EXPLICIT NULL BRANCH
#
#   referee_id != 2 evaluates to NULL (not TRUE) when referee_id is NULL, and
#   WHERE keeps only TRUE rows -- so a customer nobody referred would be
#   dropped. The IS NULL branch keeps them.
#
#   e.g. Will (referee_id = null): null != 2 -> NULL -> dropped without the branch
#
# time = O(n), space = O(n)    -- n = rows in Customer; one scan, up to n rows returned
SELECT name
FROM Customer
WHERE referee_id != 2
   OR referee_id IS NULL;   -- NOTE !!! without this line every NULL row is lost

# V1
# IDEA : SET DIFFERENCE WITH NOT IN
#
#   Everyone, minus the customers referred by id 2. The subquery returns ids,
#   and id is the primary key, so it never holds a NULL -- which is what makes
#   NOT IN safe here (NOT IN against a list containing NULL returns no rows).
#
# time = O(n), space = O(n)    -- the subquery's ids are hashed once, then one scan
SELECT name
FROM Customer
WHERE id NOT IN (
    -- the customers referred by id 2
    SELECT id
    FROM Customer
    WHERE referee_id = 2
);
