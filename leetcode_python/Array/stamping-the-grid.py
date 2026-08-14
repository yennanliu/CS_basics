"""

2132. Stamping the Grid
Hard

You are given an m x n binary matrix grid where each cell is either 0 (empty) or 1 (occupied).

You are then given stamps of size stampHeight x stampWidth. We want to fit the stamps such that they follow the given restrictions and requirements:

Cover all the empty cells.
Do not cover any of the occupied cells.
We can put as many stamps as we want.
Stamps can overlap with each other.
Stamps are not allowed to be rotated.
Stamps must stay completely inside the grid.

Return true if it is possible to fit the stamps while following the given restrictions and requirements. Otherwise, return false.


Example 1:

Input: grid = [[1,0,0,0],[1,0,0,0],[1,0,0,0],[1,0,0,0],[1,0,0,0]], stampHeight = 4, stampWidth = 3
Output: true
Explanation: We have two overlapping stamps (labeled 1 and 2 in the image) that are able to cover all the empty cells.

Example 2:

Input: grid = [[1,0,0,0],[0,1,0,0],[0,0,1,0],[0,0,0,1]], stampHeight = 2, stampWidth = 2
Output: false
Explanation: There is no way to fit the stamps onto all the empty cells without the stamps going outside the grid.


Constraints:

m == grid.length
n == grid[r].length
1 <= m, n <= 10^5
1 <= m * n <= 2 * 10^5
grid[r][c] is either 0 or 1.
1 <= stampHeight, stampWidth <= 10^5

"""

# V0
# IDEA : 2D PREFIX SUM (where can a stamp go?) + 2D DIFFERENCE ARRAY (what is covered?)
#
#   stamps may overlap freely, so the greedy answer is "place a stamp
#   EVERYWHERE it legally fits, then check nothing is left uncovered".
#
#   pass 1 — prefix sums of grid : a stamp with its top-left at (i, j) is
#            legal iff the rectangle sum over its footprint is 0.
#   pass 2 — for each legal position, mark its rectangle in a 2D DIFFERENCE
#            array (4 O(1) updates instead of filling the rectangle).
#   pass 3 — accumulate the difference array back into coverage counts, and
#            fail if any empty cell has coverage 0.
#
#   NOTE : both auxiliary grids are (m+1) x (n+1) so the boundary arithmetic
#          needs no special-casing.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def possibleToStamp(self, grid, stampHeight, stampWidth):
        m, n = len(grid), len(grid[0])

        # prefix[i+1][j+1] = sum of grid[0..i][0..j]
        prefix = [[0] * (n + 1) for _ in range(m + 1)]
        for i in range(m):
            for j in range(n):
                prefix[i + 1][j + 1] = (grid[i][j] + prefix[i][j + 1]
                                        + prefix[i + 1][j] - prefix[i][j])

        diff = [[0] * (n + 2) for _ in range(m + 2)]
        for i in range(m - stampHeight + 1):
            for j in range(n - stampWidth + 1):
                x, y = i + stampHeight, j + stampWidth       # exclusive corner
                occupied = (prefix[x][y] - prefix[i][y]
                            - prefix[x][j] + prefix[i][j])
                if occupied == 0:
                    diff[i][j] += 1
                    diff[i][y] -= 1
                    diff[x][j] -= 1
                    diff[x][y] += 1

        # accumulate the difference array into coverage counts
        for i in range(m):
            for j in range(n):
                cover = diff[i][j]
                if i:
                    cover += diff[i - 1][j]
                if j:
                    cover += diff[i][j - 1]
                if i and j:
                    cover -= diff[i - 1][j - 1]
                diff[i][j] = cover
                if grid[i][j] == 0 and cover == 0:
                    return False
        return True
