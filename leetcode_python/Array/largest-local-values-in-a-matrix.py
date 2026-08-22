"""

2373. Largest Local Values in a Matrix
Easy

You are given an n x n integer matrix grid.

Generate an integer matrix maxLocal of size (n - 2) x (n - 2) such that:

maxLocal[i][j] is equal to the largest value of the 3 x 3 matrix in grid centered around row i + 1 and column j + 1.

In other words, we want to find the largest value in every contiguous 3 x 3 matrix in grid.

Return the generated matrix.


Example 1:

Input: grid = [[9,9,8,1],[5,6,2,6],[8,2,6,4],[6,2,2,2]]
Output: [[9,9],[8,6]]
Explanation: The diagram above shows the original matrix and the generated matrix.
Notice that each value in the generated matrix corresponds to the largest value of a contiguous 3 x 3 matrix in grid.

Example 2:

Input: grid = [[1,1,1,1,1],[1,1,1,1,1],[1,1,2,1,1],[1,1,1,1,1],[1,1,1,1,1]]
Output: [[2,2,2],[2,2,2],[2,2,2]]
Explanation: Notice that the 2 is contained within every contiguous 3 x 3 matrix in grid.


Constraints:

n == grid.length == grid[i].length
3 <= n <= 100
1 <= grid[i][j] <= 100

"""

# V0
# IDEA : SLIDE A 3 x 3 WINDOW AND TAKE ITS MAXIMUM
#
#   the output is (n-2) x (n-2), one entry per top-left corner (i, j) with
#   i, j in [0, n-3]. each entry scans its own 9 cells.
#
#   n <= 100, so the 9 * (n-2)^2 work is trivial and no monotonic-deque
#   optimisation is warranted.
#
# time = O(n^2), space = O(n^2) for the output
class Solution(object):
    def largestLocal(self, grid):
        n = len(grid)
        return [[max(grid[i + di][j + dj] for di in range(3) for dj in range(3))
                 for j in range(n - 2)]
                for i in range(n - 2)]


# V0-1
# IDEA : SEPARABLE MAX - SQUEEZE THE ROWS FIRST, THEN THE COLUMNS
#
#   max over a 3 x 3 block = max over 3 vertically stacked 1 x 3 strips.
#   so first collapse every row to its width-3 running maxima
#     rowMax[i][j] = max(grid[i][j .. j+2])
#   then collapse vertically
#     out[i][j] = max(rowMax[i][j], rowMax[i+1][j], rowMax[i+2][j])
#
#   each cell is now touched 3 + 3 = 6 times instead of 9, and the same
#   trick generalises to a k x k window in O(n^2 * k) instead of O(n^2 * k^2).
#
# time = O(n^2)
# space = O(n^2)
class Solution(object):
    def largestLocal(self, grid):
        n = len(grid)
        rowMax = [[max(row[j], row[j + 1], row[j + 2]) for j in range(n - 2)]
                  for row in grid]
        return [[max(rowMax[i][j], rowMax[i + 1][j], rowMax[i + 2][j])
                 for j in range(n - 2)]
                for i in range(n - 2)]


# V0-2
# IDEA : SPARSE-TABLE STYLE DOUBLING - BUILD 2 x 2 MAXIMA, THEN OVERLAP FOUR
#
#   d[i][j] = max of the 2 x 2 block with top-left (i, j).
#   the 3 x 3 block at (i, j) is exactly covered by the four (overlapping)
#   2 x 2 blocks at (i, j), (i, j+1), (i+1, j), (i+1, j+1) - overlap is
#   harmless for max, so
#     out[i][j] = max(d[i][j], d[i][j+1], d[i+1][j], d[i+1][j+1])
#
#   this is the 2D range-max sparse table idea at one doubling level: 4 + 4
#   comparisons per cell, and reusing d for other window sizes is free.
#
# time = O(n^2)
# space = O(n^2)
class Solution(object):
    def largestLocal(self, grid):
        n = len(grid)
        d = [[max(grid[i][j], grid[i][j + 1],
                  grid[i + 1][j], grid[i + 1][j + 1])
              for j in range(n - 1)]
             for i in range(n - 1)]
        return [[max(d[i][j], d[i][j + 1], d[i + 1][j], d[i + 1][j + 1])
                 for j in range(n - 2)]
                for i in range(n - 2)]
