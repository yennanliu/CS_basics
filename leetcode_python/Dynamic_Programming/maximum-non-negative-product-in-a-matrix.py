"""

1594. Maximum Non Negative Product in a Matrix
Medium

You are given a m x n matrix grid. Initially, you are located at the top-left corner (0, 0), and in each step, you can only move right or down in the matrix.

Among all possible paths starting from the top-left corner (0, 0) and ending in the bottom-right corner (m - 1, n - 1), find the path with the maximum non-negative product. The product of a path is the product of all integers in the grid cells visited along the path.

Return the maximum non-negative product modulo 10^9 + 7. If the maximum product is negative, return -1.

Notice that the modulo is performed after getting the maximum product.

Example 1:

Input: grid = [[-1,-2,-3],[-2,-3,-3],[-3,-3,-2]]
Output: -1
Explanation: It is not possible to get non-negative product in the path from (0, 0) to (2, 2), so return -1.

Example 2:

Input: grid = [[1,-2,1],[1,-2,1],[3,-4,1]]
Output: 8
Explanation: Maximum non-negative product is shown (1 * 1 * -2 * -4 * 1 = 8).

Example 3:

Input: grid = [[1,3],[0,-4]]
Output: 0
Explanation: Maximum non-negative product is shown (1 * 0 * -4 = 0).

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 15
-4 <= grid[i][j] <= 4

"""

# V0
# IDEA : DP KEEPING BOTH MAX AND MIN (a negative cell flips the two)
#
#   a big NEGATIVE product becomes the best positive one after meeting
#   another negative cell, so each cell has to remember two values :
#     mx[i][j] = best product reaching (i, j)
#     mn[i][j] = worst product reaching (i, j)
#   grid[i][j] >= 0 -> mx from mx, mn from mn
#   grid[i][j] <  0 -> mx from mn, mn from mx
#   NOTE : take the modulo only on the final answer, the comparisons
#          must be done on the true values.
#
"""

DP def
    a big NEGATIVE product becomes the best POSITIVE one after meeting another
    negative cell, so each cell must remember TWO values

    mx[i][j]: the LARGEST  product of a path reaching (i, j)
    mn[i][j]: the SMALLEST product of a path reaching (i, j)

DP eq

     if grid[i][j] >= 0:      # order preserved

        mx[i][j] = max(mx[i-1][j], mx[i][j-1]) * v
        mn[i][j] = min(mn[i-1][j], mn[i][j-1]) * v

     if grid[i][j] <  0:      # order FLIPS

        mx[i][j] = min(mn[i-1][j], mn[i][j-1]) * v
        mn[i][j] = max(mx[i-1][j], mx[i][j-1]) * v


    -> e.g. NOTE !!! take the modulo only on the FINAL answer - the
              comparisons must be done on the true values

     init: mx[0][0] = mn[0][0] = grid[0][0], first row / column cumulative
     ans = -1 if mx[m-1][n-1] < 0 else mx[m-1][n-1] % (10^9 + 7)

"""
# time = O(m * n), space = O(m * n)
class Solution(object):
    def maxProductPath(self, grid):
        MOD = 10 ** 9 + 7
        m, n = len(grid), len(grid[0])
        mx = [[0] * n for _ in range(m)]
        mn = [[0] * n for _ in range(m)]
        mx[0][0] = mn[0][0] = grid[0][0]
        for j in range(1, n):
            mx[0][j] = mn[0][j] = mx[0][j - 1] * grid[0][j]
        for i in range(1, m):
            mx[i][0] = mn[i][0] = mx[i - 1][0] * grid[i][0]

        for i in range(1, m):
            for j in range(1, n):
                v = grid[i][j]
                if v >= 0:
                    mx[i][j] = max(mx[i - 1][j], mx[i][j - 1]) * v
                    mn[i][j] = min(mn[i - 1][j], mn[i][j - 1]) * v
                else:
                    mx[i][j] = min(mn[i - 1][j], mn[i][j - 1]) * v
                    mn[i][j] = max(mx[i - 1][j], mx[i][j - 1]) * v

        best = mx[m - 1][n - 1]
        return -1 if best < 0 else best % MOD
