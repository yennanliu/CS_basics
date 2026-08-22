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


# V0-1
# IDEA : BRUTE FORCE -- TRY EVERY ROW AS THE ROW WE INSIST ON MAKING UNIFORM
#
#  the flip set is fully determined once we decide WHICH row must come out
#  all-equal (flip exactly the columns where that row holds 1), so only m
#  candidate flip sets are worth trying. under a candidate, another row
#  becomes uniform iff it matches the chosen row in every column or
#  disagrees with it in every column.
# time = O(m^2 * n)
# space = O(1)
class Solution(object):
    def maxEqualRowsAfterFlips(self, matrix):
        n = len(matrix[0])
        best = 0
        for base in matrix:
            cnt = 0
            for row in matrix:
                same = all(row[j] == base[j] for j in range(n))
                opp = all(row[j] != base[j] for j in range(n))
                if same or opp:
                    cnt += 1
            best = max(best, cnt)
        return best


# V0-2
# IDEA : PACK EACH ROW INTO AN INTEGER, PAIR A MASK WITH ITS COMPLEMENT
#
#  with n <= 300 a row is just a (big) python int bitmask. equal rows share
#  a mask and complementary rows have masks that XOR to the all-ones value,
#  so no normalisation pass is needed : count the masks, then read off
#  cnt[mask] + cnt[full ^ mask] and keep the biggest.
# time = O(m * n)
# space = O(m)
class Solution(object):
    def maxEqualRowsAfterFlips(self, matrix):
        n = len(matrix[0])
        full = (1 << n) - 1
        cnt = {}
        for row in matrix:
            mask = 0
            for v in row:
                mask = (mask << 1) | v
            cnt[mask] = cnt.get(mask, 0) + 1
        return max(c + cnt.get(full ^ mask, 0) for mask, c in cnt.items())
