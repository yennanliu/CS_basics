"""

2884. Modify Columns
Easy

DataFrame employees
+-------------+--------+
| Column Name | Type   |
+-------------+--------+
| name        | object |
| salary      | int    |
+-------------+--------+

A company intends to give its employees a pay rise.

Write a solution to modify the salary column by multiplying each salary by 2.

The result format is in the following example.


Example 1:

Input:
DataFrame employees
+---------+--------+
| name    | salary |
+---------+--------+
| Jack    | 19666  |
| Piper   | 74754  |
| Mia     | 62509  |
| Ulysses | 54866  |
+---------+--------+
Output:
+---------+--------+
| name    | salary |
+---------+--------+
| Jack    | 39332  |
| Piper   | 149508 |
| Mia     | 125018 |
| Ulysses | 109732 |
+---------+--------+
Explanation:
Every salary has been doubled.

"""

import pandas as pd

# V0
# IDEA : VECTORISED COLUMN ARITHMETIC (in-place column assignment)
#
#   a pandas Series multiplies elementwise, so the whole column doubles in one
#   vectorised pass — no apply()/loop needed.
#
#   NOTE : the problem says "modify", i.e. mutate the given DataFrame and return
#          it (the column order / dtype must stay int). Assigning back with
#          employees["salary"] = employees["salary"] * 2 is preferred over the
#          `*=` shorthand: the explicit assignment always writes through to the
#          DataFrame, while in-place ops on a Series view can be a no-op under
#          copy-on-write semantics.
#
# time = O(n), space = O(n)
class Solution(object):
    # NOTE : LeetCode's pandas track submits a bare module-level function
    #        (aliased below); the class keeps this file in line with the
    #        rest of the directory.
    def modifySalaryColumn(self, employees):
        employees["salary"] = employees["salary"] * 2
        return employees


def modifySalaryColumn(employees):
    return Solution().modifySalaryColumn(employees)
