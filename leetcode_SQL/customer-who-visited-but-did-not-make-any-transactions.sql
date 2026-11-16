/*

1581. Customer Who Visited but Did Not Make Any Transactions
https://leetcode.com/problems/customer-who-visited-but-did-not-make-any-transactions/
Easy

Table: Visits

+-------------+---------+
| Column Name | Type    |
+-------------+---------+
| visit_id    | int     |
| customer_id | int     |
+-------------+---------+
visit_id is the column with unique values for this table.
This table contains information about the customers who visited the mall.

Table: Transactions

+----------------+---------+
| Column Name    | Type    |
+----------------+---------+
| transaction_id | int     |
| visit_id       | int     |
| amount         | int     |
+----------------+---------+
transaction_id is column with unique values for this table.
This table contains information about the transactions made during the visit_id.

Write a solution to find the IDs of the users who visited without making any transactions and the number of times they made these types of visits.

Return the result table sorted in any order.

The result format is in the following example.

Example 1:

Input:
Visits table:
+----------+-------------+
| visit_id | customer_id |
+----------+-------------+
| 1        | 23          |
| 2        | 9           |
| 4        | 30          |
| 5        | 54          |
| 6        | 96          |
| 7        | 54          |
| 8        | 54          |
+----------+-------------+
Transactions table:
+----------------+----------+--------+
| transaction_id | visit_id | amount |
+----------------+----------+--------+
| 2              | 5        | 310    |
| 3              | 5        | 300    |
| 9              | 5        | 200    |
| 12             | 1        | 910    |
| 13             | 2        | 970    |
+----------------+----------+--------+
Output:
+-------------+----------------+
| customer_id | count_no_trans |
+-------------+----------------+
| 54          | 2              |
| 30          | 1              |
| 96          | 1              |
+-------------+----------------+
Explanation:
Customer with id = 23 visited the mall once and made one transaction during the visit with id = 12.
Customer with id = 9 visited the mall once and made one transaction during the visit with id = 13.
Customer with id = 30 visited the mall once and did not make any transactions.
Customer with id = 54 visited the mall three times. During 2 visits they did not make any transactions, and during one visit they made 3 transactions.
Customer with id = 96 visited the mall once and did not make any transactions.
As we can see, users with IDs 30 and 96 visited the mall one time without making any transactions. Also, user 54 visited the mall twice and did not make any transactions.

*/

# V0
# IDEA : LEFT JOIN ... WHERE RIGHT SIDE IS NULL (ANTI JOIN), THEN GROUP BY
#
#   LEFT JOIN Transactions onto Visits: a visit with no transaction keeps one row
#   whose t columns are all NULL. Filtering on t.transaction_id IS NULL keeps
#   exactly those visits -- the WHERE tests the right table on purpose, because
#   here only the unmatched rows are wanted. Then count those visits per customer.
#
# time = O(n + m), space = O(n + m)    -- n = rows in Visits, m = rows in Transactions; hash join + hash group
SELECT
    v.customer_id,
    COUNT(v.visit_id) AS count_no_trans
FROM Visits AS v
LEFT JOIN Transactions AS t
    ON v.visit_id = t.visit_id
WHERE t.transaction_id IS NULL   -- the visit matched no transaction
GROUP BY v.customer_id;

# V1
# IDEA : SET DIFFERENCE WITH NOT IN
#
#   Keep the visits whose id is not among the visit ids that have a transaction.
#   NOT IN returns no rows at all if the subquery yields a NULL, so the subquery
#   filters NULL visit_ids out explicitly -- the schema does not promise none.
#
# time = O(n + m), space = O(m)    -- the subquery's visit ids are hashed once, then one scan of Visits
SELECT
    customer_id,
    COUNT(visit_id) AS count_no_trans
FROM Visits
WHERE visit_id NOT IN (
    -- visits that made at least one transaction
    SELECT visit_id
    FROM Transactions
    WHERE visit_id IS NOT NULL   -- one NULL here would make NOT IN return nothing
)
GROUP BY customer_id;

# V2
# IDEA : NOT EXISTS (CORRELATED ANTI JOIN)
#
#   For each visit, ask "is there any transaction for this visit?". NOT EXISTS
#   only looks for a matching row, so a NULL visit_id in Transactions never
#   matches and cannot empty the result -- NULL-safe without an extra filter.
#
# time = O(n + m), space = O(m)    -- the optimiser runs it as a hash anti join
SELECT
    v.customer_id,
    COUNT(v.visit_id) AS count_no_trans
FROM Visits AS v
WHERE NOT EXISTS (
    SELECT 1
    FROM Transactions AS t
    WHERE t.visit_id = v.visit_id
)
GROUP BY v.customer_id;
