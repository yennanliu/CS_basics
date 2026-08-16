"""

2883. Drop Missing Data
Easy

DataFrame students
+-------------+--------+
| Column Name | Type   |
+-------------+--------+
| student_id  | int    |
| name        | object |
| age         | int    |
+-------------+--------+

There are some rows having missing values in the name column.

Write a solution to remove the rows with missing values.

The result format is in the following example.


Example 1:

Input:
+------------+---------+-----+
| student_id | name    | age |
+------------+---------+-----+
| 32         | Piper   | 5   |
| 217        | None    | 19  |
| 779        | Georgia | 20  |
| 849        | Willow  | 14  |
+------------+---------+-----+
Output:
+------------+---------+-----+
| student_id | name    | age |
+------------+---------+-----+
| 32         | Piper   | 5   |
| 779        | Georgia | 20  |
| 849        | Willow  | 14  |
+------------+---------+-----+
Explanation:
Student with id 217 has an empty value in the name column, so it will be removed.

"""

import pandas as pd

# V0
# IDEA : PANDAS dropna RESTRICTED TO THE `name` COLUMN
#
#   only missing values in `name` should drop a row — a NaN sitting in
#   student_id or age must NOT. dropna(subset=["name"]) scopes the null test
#   to that single column.
#
#   NOTE : in an object-dtype column, the Python `None` shown in the example is
#          what pandas treats as missing, so both `None` and `NaN` are caught.
#          The equivalent boolean-mask form is students[students["name"].notna()].
#
# time = O(n), space = O(n)
class Solution(object):
    # NOTE : LeetCode's pandas track submits a bare module-level function
    #        (aliased below); the class keeps this file in line with the
    #        rest of the directory.
    def dropMissingData(self, students):
        return students.dropna(subset=["name"])


def dropMissingData(students):
    return Solution().dropMissingData(students)
