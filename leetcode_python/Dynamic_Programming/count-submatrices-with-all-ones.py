"""

1504. Count Submatrices With All Ones
Medium

Given an m x n binary matrix mat, return the number of submatrices that have all ones.


Example 1:

Input: mat = [[1,0,1],[1,1,0],[1,1,0]]
Output: 13
Explanation:
There are 6 rectangles of side 1x1.
There are 2 rectangles of side 1x2.
There are 3 rectangles of side 2x1.
There is 1 rectangle of side 2x2.
There is 1 rectangle of side 3x1.
Total number of rectangles = 6 + 2 + 3 + 1 + 1 = 13.

Example 2:

Input: mat = [[0,1,1,0],[0,1,1,1],[1,1,1,0]]
Output: 24
Explanation:
There are 8 rectangles of side 1x1.
There are 5 rectangles of side 1x2.
There are 2 rectangles of side 1x3.
There are 4 rectangles of side 2x1.
There are 2 rectangles of side 2x2.
There are 2 rectangles of side 3x1.
There is 1 rectangle of side 3x2.
Total number of rectangles = 8 + 5 + 2 + 4 + 2 + 2 + 1 = 24.


Constraints:

1 <= m, n <= 150
mat[i][j] is either 0 or 1.

"""

# V0
# IDEA : ROW WIDTH DP + ENUMERATE BOTTOM RIGHT CORNER
#
#   step 1) g[i][j] = how many consecutive 1s end at (i, j) going LEFT
#           g[i][j] = 0                if mat[i][j] == 0
#                   = g[i][j-1] + 1    otherwise
#
#   step 2) fix (i, j) as the BOTTOM RIGHT corner, then walk up k = i, i-1, ...
#           the widest submatrix whose bottom right corner is (i, j)
#           and whose top row is k has width min(g[k][j] ... g[i][j]).
#           each distinct width contributes exactly 1 submatrix per top row,
#           so just accumulate that running min.
"""

DP def
    g[i][j]: how many consecutive 1s END at (i, j) going LEFT

             -> the "width" available on row i at column j

DP eq

     g[i][j] = 0                  if mat[i][j] == 0

     g[i][j] = g[i][j-1] + 1      otherwise


    -> e.g. then fix (i, j) as the BOTTOM RIGHT corner and walk up
              k = i, i-1, ... keeping a RUNNING MIN of the widths:

         w = min(g[k][j] .. g[i][j])
         res += w          # w submatrices with top row k

         (break as soon as w == 0)

     ans = res

"""
# time = O(m^2 * n)
# space = O(m * n)
class Solution(object):
    def numSubmat(self, mat):
        m, n = len(mat), len(mat[0])

        # g[i][j] : run of 1s ending at (i, j), extending to the left
        g = [[0] * n for _ in range(m)]
        for i in range(m):
            for j in range(n):
                if mat[i][j]:
                    g[i][j] = g[i][j - 1] + 1 if j else 1

        res = 0
        for i in range(m):
            for j in range(n):
                # NOTE !!! `w` is the running min of the widths above
                w = float('inf')
                for k in range(i, -1, -1):
                    w = min(w, g[k][j])
                    if w == 0:
                        break
                    res += w
        return res


# V1
# IDEA : MONOTONIC STACK (per column, O(m*n))
#
#   scan row by row. for the current row, keep a stack of columns with
#   increasing g[i][j]. dp[j] = number of all-ones submatrices whose
#   bottom right corner is (i, j).
#
#       - pop every column p with g[i][p] >= g[i][j]
#       - let `prev` be the column left on top of the stack
#       - dp[j] = dp[prev] + g[i][j] * (j - prev)
#
#   because the columns in (prev, j] are all capped by g[i][j].
"""

DP def
    g[i][j]: how many consecutive 1s END at (i, j) going LEFT

             -> the "width" available on row i at column j

DP eq

     g[i][j] = 0                  if mat[i][j] == 0

     g[i][j] = g[i][j-1] + 1      otherwise


    -> e.g. then fix (i, j) as the BOTTOM RIGHT corner and walk up
              k = i, i-1, ... keeping a RUNNING MIN of the widths:

         w = min(g[k][j] .. g[i][j])
         res += w          # w submatrices with top row k

         (break as soon as w == 0)

     ans = res

"""
# time = O(m * n)
# space = O(n)
class Solution(object):
    def numSubmat(self, mat):
        m, n = len(mat), len(mat[0])

        g = [0] * n
        res = 0
        for i in range(m):
            for j in range(n):
                g[j] = g[j] + 1 if mat[i][j] else 0

            stack = []          # holds column indices, increasing g
            dp = [0] * n
            for j in range(n):
                while stack and g[stack[-1]] >= g[j]:
                    stack.pop()
                if stack:
                    prev = stack[-1]
                    dp[j] = dp[prev] + g[j] * (j - prev)
                else:
                    dp[j] = g[j] * (j + 1)
                stack.append(j)
                res += dp[j]

        return res
