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


# V0-1
# IDEA : ROLLING COLUMN COUNTS — SAME O(1) LOOKUP IN O(n) SPACE
#
#   the full m*n prefix tables are never needed at once: while walking row i
#   we only need the counts for rows 0..i. keep, per column j,
#       colX[j] / colY[j] = how many 'X' / 'Y' in column j among rows 0..i,
#   and a running left-to-right sum of those columns. after column j that
#   running sum IS the count over the rectangle (0,0)-(i,j).
#
# time = O(m * n), space = O(n)
class Solution(object):
    def numberOfSubmatrices(self, grid):
        n = len(grid[0])
        colX = [0] * n
        colY = [0] * n
        res = 0
        for row in grid:
            runX = runY = 0
            for j in range(n):
                if row[j] == 'X':
                    colX[j] += 1
                elif row[j] == 'Y':
                    colY[j] += 1
                runX += colX[j]
                runY += colY[j]
                if runX and runX == runY:
                    res += 1
        return res


# V0-2
# IDEA : BRUTE FORCE — RECOUNT EVERY CANDIDATE RECTANGLE FROM SCRATCH
#
#   the baseline the prefix sums replace: for each bottom-right corner (i, j)
#   re-scan the whole rectangle (0,0)-(i,j) and tally 'X' / 'Y'.
#   O(m^2 * n^2) — only usable on tiny grids, kept to show what the prefix
#   tables buy.
#
# time = O(m^2 * n^2), space = O(1)
class Solution(object):
    def numberOfSubmatrices(self, grid):
        m, n = len(grid), len(grid[0])
        res = 0
        for i in range(m):
            for j in range(n):
                cntX = cntY = 0
                for r in range(i + 1):
                    for c in range(j + 1):
                        if grid[r][c] == 'X':
                            cntX += 1
                        elif grid[r][c] == 'Y':
                            cntY += 1
                if cntX and cntX == cntY:
                    res += 1
        return res
