"""

1072. Flip Columns For Maximum Number of Equal Rows
Medium

You are given an m x n binary matrix matrix.

You can choose any number of columns in the matrix and flip every cell in that
column (i.e., Change the value of the cell from 0 to 1 or vice versa).

Return the maximum number of rows that have all values equal after some number of flips.


Example 1:

Input: matrix = [[0,1],[1,1]]
Output: 1
Explanation: After flipping no values, 1 row has all values equal.

Example 2:

Input: matrix = [[0,1],[1,0]]
Output: 2
Explanation: After flipping values in the first column, both rows have equal values.

Example 3:

Input: matrix = [[0,0,0],[0,0,1],[1,1,0]]
Output: 2
Explanation: After flipping values in the first two columns, the last two rows have equal values.


Constraints:

m == matrix.length
n == matrix[i].length
1 <= m, n <= 300
matrix[i][j] is either 0 or 1.

"""

# V0
# IDEA : HASH MAP + row "normalization"
#
#  a column flip flips the SAME column in every row, so two rows can be made
#  all-equal together exactly when they are identical OR exact complements.
#
#  -> normalize each row by XOR-ing it with its own first element
#     (so every normalized row starts with 0)
#  -> rows that are identical or complementary collapse to the same key
#  -> answer = size of the biggest bucket
# time = O(m * n)
# space = O(m * n)
from collections import Counter
class Solution(object):
    def maxEqualRowsAfterFlips(self, matrix):
        cnt = Counter()
        for row in matrix:
            key = tuple(x ^ row[0] for x in row)
            cnt[key] += 1
        return max(cnt.values())
