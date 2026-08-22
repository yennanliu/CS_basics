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


# V0-1
# IDEA : BUILD THE SET OF BAD LABELS WITH isna(), THEN DROP THEM BY LABEL
#
#   the inverse framing of V0 : instead of SELECTING the rows to keep, compute
#   the index labels of the offending rows (`index[...isna()]`) and hand them to
#   `DataFrame.drop`, which removes rows by label.
#
#   worth knowing because it composes : the same label set can be reused (e.g.
#   logged, or dropped from a second frame that shares the index), which a
#   boolean-mask selection cannot hand you.
#
# time = O(n), space = O(n)
class Solution(object):
    def dropMissingData(self, students):
        bad = students.index[students["name"].isna()]
        return students.drop(index=bad)


# V0-2
# IDEA : EXPLICIT PYTHON SCAN OVER THE COLUMN, THEN SELECT BY LABEL
#
#   no pandas null-handling primitive : iterate the `name` column, test each
#   value with `pd.isna` (which catches both the `None` of an object column and
#   float `NaN`), collect the labels that survive, and materialise once via
#   `.loc`.
#
#   NOTE : the explicit predicate is the point -- swap `pd.isna(v)` for
#          `pd.isna(v) or v == ""` and empty strings drop too, something
#          `dropna` will never do for you.
#
# time = O(n), space = O(n)
class Solution(object):
    def dropMissingData(self, students):
        keep = [label for label, v in students["name"].items()
                if not pd.isna(v)]
        return students.loc[keep]
