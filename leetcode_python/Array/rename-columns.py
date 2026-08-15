"""

2885. Rename Columns
Easy

DataFrame students
+-------------+--------+
| Column Name | Type   |
+-------------+--------+
| id          | int    |
| first       | object |
| last        | object |
| age         | int    |
+-------------+--------+

Write a solution to rename the columns as follows:

- id to student_id
- first to first_name
- last to last_name
- age to age_in_years

The result format is in the following example.


Example 1:

Input:
+----+---------+----------+-----+
| id | first   | last     | age |
+----+---------+----------+-----+
| 1  | Mason   | King     | 6   |
| 2  | Ava     | Wright   | 7   |
| 3  | Taylor  | Hall     | 16  |
| 4  | Georgia | Thompson | 18  |
| 5  | Thomas  | Moore    | 10  |
+----+---------+----------+-----+
Output:
+------------+------------+-----------+--------------+
| student_id | first_name | last_name | age_in_years |
+------------+------------+-----------+--------------+
| 1          | Mason      | King      | 6            |
| 2          | Ava        | Wright    | 7            |
| 3          | Taylor     | Hall      | 16           |
| 4          | Georgia    | Thompson  | 18           |
| 5          | Thomas     | Moore     | 10           |
+------------+------------+-----------+--------------+
Explanation:
The column names are changed accordingly.

"""

import pandas as pd

# V0
# IDEA : PANDAS rename WITH AN OLD -> NEW COLUMN MAPPING
#
#   DataFrame.rename(columns=mapping) relabels the column axis without touching
#   any of the data, so both column order and row order are preserved.
#
#   NOTE : rename is a LOOKUP, not a positional rewrite — that matters here
#          because assigning students.columns = [...] would silently mis-label
#          the frame if the incoming column order ever differed.
#
# time = O(m) for m columns, space = O(m)
class Solution(object):
    # NOTE : LeetCode's pandas track submits a bare module-level function
    #        (aliased below); the class keeps this file in line with the
    #        rest of the directory.
    def renameColumns(self, students):
        return students.rename(
            columns={
                "id": "student_id",
                "first": "first_name",
                "last": "last_name",
                "age": "age_in_years",
            }
        )


def renameColumns(students):
    return Solution().renameColumns(students)
