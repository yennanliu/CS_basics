"""

2882. Drop Duplicate Rows
Easy

DataFrame customers
+-------------+--------+
| Column Name | Type   |
+-------------+--------+
| customer_id | int    |
| name        | object |
| email       | object |
+-------------+--------+

There are some duplicate rows in the DataFrame based on the email column.

Write a solution to remove these duplicate rows and keep only the first occurrence.

The result format is in the following example.


Example 1:

Input:
+-------------+---------+---------------------+
| customer_id | name    | email               |
+-------------+---------+---------------------+
| 1           | Ella    | emily@example.com   |
| 2           | David   | michael@example.com |
| 3           | Zachary | sarah@example.com   |
| 4           | Alice   | john@example.com    |
| 5           | Finn    | john@example.com    |
| 6           | Violet  | alice@example.com   |
+-------------+---------+---------------------+
Output:
+-------------+---------+---------------------+
| customer_id | name    | email               |
+-------------+---------+---------------------+
| 1           | Ella    | emily@example.com   |
| 2           | David   | michael@example.com |
| 3           | Zachary | sarah@example.com   |
| 4           | Alice   | john@example.com    |
| 6           | Violet  | alice@example.com   |
+-------------+---------+---------------------+
Explanation:
Alice (customer_id = 4) and Finn (customer_id = 5) both use john@example.com,
so only the first occurrence of this email is retained.

"""

import pandas as pd

# V0
# IDEA : PANDAS drop_duplicates ON A SUBSET OF COLUMNS
#
#   de-duplication is keyed on `email` ALONE, not on the whole row — the other
#   columns (customer_id, name) differ between the two john@example.com rows,
#   so a plain drop_duplicates() would keep both. Passing subset=["email"]
#   restricts the key to that one column.
#
#   NOTE : `keep="first"` is the pandas default and is exactly what the problem
#          asks for (keep the earliest occurrence), so it can be left implicit.
#          The surviving rows keep their original index labels.
#
# time = O(n), space = O(n)
class Solution(object):
    # NOTE : LeetCode's pandas track submits a bare module-level function
    #        (aliased below); the class keeps this file in line with the
    #        rest of the directory.
    def dropDuplicateEmails(self, customers):
        return customers.drop_duplicates(subset=["email"])


def dropDuplicateEmails(customers):
    return Solution().dropDuplicateEmails(customers)
