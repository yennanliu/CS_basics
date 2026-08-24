"""

3148. Maximum Difference Score in a Grid
Medium

You are given an m x n matrix grid consisting of positive integers. You can move from a cell in the matrix to any other cell that is either to the bottom or to the right (not necessarily adjacent). The score of a move from a cell with the value c1 to a cell with the value c2 is c2 - c1.

You can start at any cell, and you have to make at least one move.

Return the maximum total score you can achieve.


Example 1:

Input: grid = [[9,5,7,3],[8,9,6,1],[6,7,14,3],[2,5,3,1]]
Output: 9
Explanation: We start at the cell (0, 1), and we perform the following moves:
- Move from the cell (0, 1) to (2, 1) with a score of 7 - 5 = 2.
- Move from the cell (2, 1) to (2, 2) with a score of 14 - 7 = 7.
The total score is 2 + 7 = 9.

Example 2:

Input: grid = [[4,3,2],[3,2,1]]
Output: -1
Explanation: We start at the cell (0, 0), and we perform one move: (0, 0) to (0, 1). The score is 3 - 4 = -1.


Constraints:

m == grid.length
n == grid[i].length
2 <= m, n <= 1000
4 <= m * n <= 10^5
1 <= grid[i][j] <= 10^5

"""

# V0
# IDEA : A MULTI-MOVE PATH TELESCOPES — ONLY THE ENDPOINTS MATTER
#
#   the scores along a path sum as
#       (c2 - c1) + (c3 - c2) + ... = last - first
#   so intermediate cells cancel out entirely, and the task is just : pick a
#   start cell and a later (down-right) end cell maximising end - start.
#
#   so for each cell, the best answer ending there is
#       grid[i][j] - (minimum value strictly above-left of it)
#   and that running minimum propagates with
#       best[i][j] = min(grid[i][j], best[i-1][j], best[i][j-1])
#
#   "at least one move" is what makes the minimum STRICT — a cell may not
#   pair with itself, hence reading the neighbours' minima, never its own.
#
"""

DP def
    a multi-move path TELESCOPES:
        (c2 - c1) + (c3 - c2) + ... = last - first
    so intermediate cells cancel out and the task is just "pick a start cell
    and a later (down-right) end cell maximising end - start"

    best[i][j]: MINIMUM grid value in the up-left region reaching (i, j)

DP eq

     reach      = min( best[i-1][j], best[i][j-1] )     # STRICTLY above/left

     res        = max( res, grid[i][j] - reach )

     best[i][j] = min( grid[i][j], reach )


    -> e.g. "at least one move" is what makes the minimum STRICT - a cell may
              not pair with itself, hence reading the NEIGHBOURS' minima and
              never its own

     init: best = +inf, res = -inf
     ans = res     (can be negative)

"""
# time = O(m * n), space = O(n)
class Solution(object):
    def maxScore(self, grid):
        m, n = len(grid), len(grid[0])
        INF = float('inf')
        best = [INF] * n                   # min value seen in the up-left region
        res = float('-inf')

        for i in range(m):
            row = grid[i]
            new = [INF] * n
            left = INF                     # min over this row so far, up-left region
            for j in range(n):
                reach = min(best[j], left)  # strictly above or strictly left
                if reach != INF:
                    res = max(res, row[j] - reach)
                new[j] = min(row[j], reach)
                left = new[j]
            best = new
        return res
