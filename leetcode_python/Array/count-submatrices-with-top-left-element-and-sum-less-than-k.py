"""

3070. Count Submatrices with Top-Left Element and Sum Less Than k
Medium

You are given a 0-indexed integer matrix grid and an integer k.

Return the number of submatrices that contain the top-left element of the grid, and have a sum less than or equal to k.


Example 1:

Input: grid = [[7,6,3],[6,6,1]], k = 18
Output: 4
Explanation: There are only 4 submatrices, shown in the image above, that contain the top-left element of grid, and have a sum less than or equal to 18.

Example 2:

Input: grid = [[7,2,9],[1,5,0],[2,6,6]], k = 20
Output: 6
Explanation: There are only 6 submatrices, shown in the image above, that contain the top-left element of grid, and have a sum less than or equal to 20.


Constraints:

m == grid.length
n == grid[i].length
1 <= n, m <= 1000
0 <= grid[i][j] <= 1000
1 <= k <= 10^9

"""

# V0
# IDEA : A SUBMATRIX CONTAINING (0,0) *IS* A PREFIX RECTANGLE
#
#   "contains the top-left element" forces the submatrix to start at (0, 0),
#   so it is fully described by its bottom-right corner (i, j) — one
#   candidate per cell.
#
#   its sum is the 2D prefix sum at that corner, built with the usual
#       pre[i+1][j+1] = grid[i][j] + pre[i][j+1] + pre[i+1][j] - pre[i][j]
#   so the answer is simply how many corners have prefix sum <= k.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def countSubmatrices(self, grid, k):
        m, n = len(grid), len(grid[0])
        pre = [[0] * (n + 1) for _ in range(m + 1)]
        res = 0
        for i in range(m):
            for j in range(n):
                pre[i + 1][j + 1] = (grid[i][j] + pre[i][j + 1]
                                     + pre[i + 1][j] - pre[i][j])
                if pre[i + 1][j + 1] <= k:
                    res += 1
        return res


# V0-1
# IDEA : ROLLING COLUMN SUMS — SAME COUNT IN O(n) SPACE
#
#   the whole (m+1) x (n+1) table is never needed at once. keep only
#       col[j] = sum of column j over the rows processed so far,
#   then a running left-to-right sum of col[] gives, at column j, exactly the
#   sum of the rectangle (0,0)-(i,j).
#
# time = O(m * n), space = O(n)
class Solution(object):
    def countSubmatrices(self, grid, k):
        n = len(grid[0])
        col = [0] * n
        res = 0
        for row in grid:
            run = 0
            for j in range(n):
                col[j] += row[j]
                run += col[j]
                if run <= k:
                    res += 1
        return res


# V0-2
# IDEA : BRUTE FORCE — RE-ADD EVERY CANDIDATE RECTANGLE
#
#   the baseline that the prefix sum replaces : for each bottom-right corner
#   (i, j), sum the rectangle (0,0)-(i,j) from scratch. O(m^2 * n^2), only
#   viable on tiny inputs, kept to show what the prefix table buys.
#
# time = O(m^2 * n^2), space = O(1)
class Solution(object):
    def countSubmatrices(self, grid, k):
        m, n = len(grid), len(grid[0])
        res = 0
        for i in range(m):
            for j in range(n):
                total = 0
                for r in range(i + 1):
                    for c in range(j + 1):
                        total += grid[r][c]
                if total <= k:
                    res += 1
        return res
