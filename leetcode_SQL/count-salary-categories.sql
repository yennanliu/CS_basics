/*

1907. Count Salary Categories
https://leetcode.com/problems/count-salary-categories/
Medium

Table: Accounts

+-------------+------+
| Column Name | Type |
+-------------+------+
| account_id  | int  |
| income      | int  |
+-------------+------+
account_id is the primary key (column with unique values) for this table.
Each row contains information about the monthly income for one bank account.

Write a solution to calculate the number of bank accounts for each salary category. The salary categories are:

	- "Low Salary": All the salaries strictly less than $20000.

	- "Average Salary": All the salaries in the inclusive range [$20000, $50000].

	- "High Salary": All the salaries strictly greater than $50000.

The result table must contain all three categories. If there are no accounts in a category, return 0.

Return the result table in any order.

The result format is in the following example.

Example 1:

Input:
Accounts table:
+------------+--------+
| account_id | income |
+------------+--------+
| 3          | 108939 |
| 2          | 12747  |
| 8          | 87709  |
| 6          | 91796  |
+------------+--------+
Output:
+----------------+----------------+
| category       | accounts_count |
+----------------+----------------+
| Low Salary     | 1              |
| Average Salary | 0              |
| High Salary    | 3              |
+----------------+----------------+
Explanation:
Low Salary: Account 2.
Average Salary: No accounts.
High Salary: Accounts 3, 6, and 8.

*/

# V0
# IDEA : ONE COUNT PER CATEGORY, UNION ALL
#
#   Each branch counts its own band, and COUNT(*) over zero matching rows is
#   0, not a missing row -- which is exactly what "return 0 for an empty
#   category" needs. BETWEEN is inclusive on both ends, matching
#   [20000, 50000]. UNION ALL, not UNION: three distinct labels, no dedup.
#
# time = O(n), space = O(1)    -- n = rows in Accounts; three scans, three output rows
SELECT
    'Low Salary' AS category,
    COUNT(*) AS accounts_count
FROM Accounts
WHERE income < 20000
UNION ALL
SELECT
    'Average Salary' AS category,
    COUNT(*) AS accounts_count
FROM Accounts
WHERE income BETWEEN 20000 AND 50000
UNION ALL
SELECT
    'High Salary' AS category,
    COUNT(*) AS accounts_count
FROM Accounts
WHERE income > 50000;

# V1
# IDEA : CATEGORY LIST LEFT JOIN THE CLASSIFIED ACCOUNTS
#
#   Label every account once with CASE WHEN, then LEFT JOIN from a fixed list
#   of the three categories, so a category with no account still gets a row.
#   COUNT(ca.account_id) counts only matched rows (NULL is skipped), giving 0
#   for an empty category; COUNT(*) would wrongly give 1.
#
# time = O(n), space = O(n)    -- n = rows in Accounts; one scan to classify, one group
WITH categories AS (
    SELECT 'Low Salary' AS category
    UNION ALL
    SELECT 'Average Salary'
    UNION ALL
    SELECT 'High Salary'
),
classified_accounts AS (
    SELECT
        account_id,
        CASE
            WHEN income < 20000 THEN 'Low Salary'
            WHEN income > 50000 THEN 'High Salary'
            ELSE 'Average Salary'
        END AS category
    FROM Accounts
)
SELECT
    c.category,
    COUNT(ca.account_id) AS accounts_count   -- COUNT(col) skips the NULL of an unmatched category
FROM categories c
LEFT JOIN classified_accounts ca              -- LEFT: keep a category no account falls into
    ON ca.category = c.category
GROUP BY c.category;
