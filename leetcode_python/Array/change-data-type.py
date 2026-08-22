"""

2886. Change Data Type
Easy

DataFrame students
+-------------+--------+
| Column Name | Type   |
+-------------+--------+
| student_id  | int    |
| name        | object |
| age         | int    |
| grade       | float  |
+-------------+--------+

Write a solution to correct the errors:

The grade column is stored as floats, convert it to integers.

The result format is in the following example.


Example 1:

Input:
DataFrame students:
+------------+------+-----+-------+
| student_id | name | age | grade |
+------------+------+-----+-------+
| 1          | Ava  | 6   | 73.0  |
| 2          | Kate | 15  | 87.0  |
+------------+------+-----+-------+
Output:
+------------+------+-----+-------+
| student_id | name | age | grade |
+------------+------+-----+-------+
| 1          | Ava  | 6   | 73    |
| 2          | Kate | 15  | 87    |
+------------+------+-----+-------+
Explanation:
The data types of the column grade is converted to int.

"""

import pandas as pd

# V0
# IDEA : PANDAS astype ON A SINGLE COLUMN
#
#   Series.astype(int) recasts the whole column in one vectorised pass; only
#   `grade` is touched, so student_id / name / age keep their dtypes.
#
#   NOTE : astype(int) TRUNCATES toward zero rather than rounding, which is fine
#          here because the problem guarantees the floats are whole numbers
#          (73.0, 87.0) that were merely stored in the wrong dtype.
#
# time = O(n), space = O(n)
class Solution(object):
    # NOTE : LeetCode's pandas track submits a bare module-level function
    #        (aliased below); the class keeps this file in line with the
    #        rest of the directory.
    def changeDatatype(self, students):
        students["grade"] = students["grade"].astype(int)
        return students


def changeDatatype(students):
    return Solution().changeDatatype(students)


# V0-1
# IDEA : pd.to_numeric WITH downcast (re-parse the column, not a raw recast)
#
#   to_numeric re-parses the column, and downcast="integer" then picks the
#   SMALLEST integer dtype that still holds every value, so the result is e.g.
#   int8 instead of the platform-wide int64 that astype(int) hands back.
#   handy on wide frames where the memory saving is the point.
#
# time = O(n), space = O(n)
class Solution(object):
    def changeDatatype(self, students):
        students["grade"] = pd.to_numeric(students["grade"], downcast="integer")
        return students


# V0-2
# IDEA : ELEMENT WISE PYTHON CAST VIA apply, RETURNED AS A NEW FRAME
#
#   instead of one vectorised recast, run python's int() on every value and
#   hand the result to DataFrame.assign, which returns a NEW frame rather than
#   mutating the caller's one in place.
#
#   slower than astype (no numpy fast path, one python call per row), but this
#   is the shape you fall back to when the cast needs custom per-value logic.
#
# time = O(n), space = O(n)
class Solution(object):
    def changeDatatype(self, students):
        return students.assign(grade=students["grade"].apply(int))
