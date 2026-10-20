"""

1277. Count Square Submatrices with All Ones
Medium

Given a m * n matrix of ones and zeros, return how many square submatrices
have all ones.

Example 1:

Input: matrix =
[
  [0,1,1,1],
  [1,1,1,1],
  [0,1,1,1]
]
Output: 15
Explanation:
There are 10 squares of side 1.
There are 4 squares of side 2.
There is  1 square of side 3.
Total number of squares = 10 + 4 + 1 = 15.

Example 2:

Input: matrix =
[
  [1,0,1],
  [1,1,0],
  [1,1,0]
]
Output: 7
Explanation:
There are 6 squares of side 1.
There is 1 square of side 2.
Total number of squares = 6 + 1 = 7.

Constraints:

1 <= arr.length <= 300
1 <= arr[0].length <= 300
0 <= arr[i][j] <= 1

"""

# V0
# IDEA : 2D DP (SAME RECURRENCE AS LC 221 MAXIMAL SQUARE)
#
#   dp[i][j] = side of the LARGEST all-ones square whose bottom-right corner
#              is cell (i-1, j-1)   (dp is padded by one row / col of 0s)
#
#   A square of side k ending at (i, j) exists exactly when squares of side
#   k-1 end at its top, left and top-left neighbours, so
#
#       dp[i][j] = min(top, left, top_left) + 1    if matrix cell is 1
#                = 0                                otherwise
#
#   Why SUM dp gives the count: if the largest square ending at a cell has
#   side k, then squares of side 1, 2, ..., k all end there too -> k squares,
#   each counted once (by its own bottom-right corner).
#
#   e.g. dp value 3 at a cell -> one 1x1, one 2x2, one 3x3 end there -> +3
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def countSquares(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: int
        """
        # edge case: empty grid has no squares
        if not matrix or not matrix[0]:
            return 0

        rows = len(matrix)
        cols = len(matrix[0])

        # extra 0 row / col on top and left, so i-1 / j-1 never go out of range
        dp = [[0] * (cols + 1) for _ in range(rows + 1)]

        total = 0
        for i in range(1, rows + 1):
            for j in range(1, cols + 1):
                if matrix[i - 1][j - 1] == 1:
                    top = dp[i - 1][j]
                    left = dp[i][j - 1]
                    top_left = dp[i - 1][j - 1]
                    # NOTE !!! the SMALLEST neighbour limits how far the square can grow
                    dp[i][j] = min(top, left, top_left) + 1
                    # every side 1..dp[i][j] is a distinct square ending here
                    total += dp[i][j]

        return total


# V0-1
# IDEA : 1D ROLLING DP (SPACE-OPTIMISED V0)
#
#   Row i only reads row i-1, so keep one row. Before dp[j] is overwritten it
#   still holds the value from the row above ("top"); dp[j-1] already holds
#   this row's value ("left"); the old dp[j-1] from the row above ("top_left")
#   is saved in a variable before it gets overwritten.
#
# time = O(m * n), space = O(n)
class Solution2(object):
    def countSquares(self, matrix):
        """
        :type matrix: List[List[int]]
        :rtype: int
        """
        if not matrix or not matrix[0]:
            return 0

        rows = len(matrix)
        cols = len(matrix[0])

        # dp[j] = largest square side ending at column j-1 of the current row
        dp = [0] * (cols + 1)

        total = 0
        for i in range(1, rows + 1):
            # dp[0] is the padding column, so the top-left of column 1 is 0
            top_left = 0
            for j in range(1, cols + 1):
                # dp[j] still holds the previous row's value -> it is "top"
                top = dp[j]
                if matrix[i - 1][j - 1] == 1:
                    left = dp[j - 1]
                    dp[j] = min(top, left, top_left) + 1
                    total += dp[j]
                else:
                    # a 0 cell ends no square, and must reset the stale value
                    dp[j] = 0
                # this row's "top" becomes the next column's "top_left"
                top_left = top

        return total
