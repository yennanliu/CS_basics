"""

3212. Count Submatrices With Equal Frequency of X and Y
Medium

Given a 2D character matrix grid, where grid[i][j] is either 'X', 'Y', or '.', return the number of submatrices that contain:

grid[0][0]
an equal frequency of 'X' and 'Y'.
at least one 'X'.


Example 1:

Input: grid = [["X","Y","."],["Y",".","."]]
Output: 3
Explanation:

Example 2:

Input: grid = [["X","X"],["X","Y"]]
Output: 0
Explanation:
No submatrix has an equal frequency of 'X' and 'Y'.

Example 3:

Input: grid = [[".","."],[".","."]]
Output: 0
Explanation:
No submatrix has at least one 'X'.


Constraints:

1 <= grid.length, grid[i].length <= 1000
grid[i][j] is either 'X', 'Y', or '.'.

"""

# V0
# IDEA : "CONTAINS grid[0][0]" MEANS IT IS A PREFIX RECTANGLE
#
#   a submatrix holding the top-left cell must start at (0, 0), so it is
#   fully described by its bottom-right corner — one candidate per cell.
#
#   two 2D prefix-sum tables (one counting 'X', one counting 'Y') then answer
#   each candidate in O(1), and the conditions become
#       cntX == cntY   and   cntX > 0
#   (the second clause is the "at least one X" requirement, which also rules
#   out the empty all-dots case).
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def numberOfSubmatrices(self, grid):
        m, n = len(grid), len(grid[0])
        px = [[0] * (n + 1) for _ in range(m + 1)]
        py = [[0] * (n + 1) for _ in range(m + 1)]
        res = 0

        for i in range(m):
            row = grid[i]
            for j in range(n):
                x = 1 if row[j] == 'X' else 0
                y = 1 if row[j] == 'Y' else 0
                px[i + 1][j + 1] = x + px[i][j + 1] + px[i + 1][j] - px[i][j]
                py[i + 1][j + 1] = y + py[i][j + 1] + py[i + 1][j] - py[i][j]
                if px[i + 1][j + 1] and px[i + 1][j + 1] == py[i + 1][j + 1]:
                    res += 1
        return res
