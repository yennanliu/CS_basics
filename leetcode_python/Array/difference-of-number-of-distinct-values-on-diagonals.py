"""

2711. Difference of Number of Distinct Values on Diagonals
Medium

Given a 2D grid of size m x n, you should find the matrix answer of size m x n.

The cell answer[r][c] is calculated by looking at the diagonal values of the cell grid[r][c]:

Let leftAbove[r][c] be the number of distinct values on the diagonal to the left and above the cell grid[r][c] not including the cell grid[r][c] itself.
Let rightBelow[r][c] be the number of distinct values on the diagonal to the right and below the cell grid[r][c], not including the cell grid[r][c] itself.
Then answer[r][c] = |leftAbove[r][c] - rightBelow[r][c]|.

A matrix diagonal is a diagonal line of cells starting from some cell in either the topmost row or leftmost column and going in the bottom-right direction until the end of the matrix is reached.

For example, for the cell with indices (2, 3):
  - Red-colored cells are left and above the cell.
  - Blue-colored cells are right and below the cell.

Return the matrix answer.


Example 1:

Input: grid = [[1,2,3],[3,1,5],[3,2,1]]
Output: [[1,1,0],[1,0,1],[0,1,1]]
Explanation:
To calculate the answer cells:

answer  | left-above elements    | leftAbove   | right-below elements   | rightBelow  | |leftAbove - rightBelow|
[0][0]  | []                     | 0           | [grid[1][1], grid[2][2]] | |{1, 1}| = 1 | 1
[0][1]  | []                     | 0           | [grid[1][2]]           | |{5}| = 1    | 1
[0][2]  | []                     | 0           | []                     | 0           | 0
[1][0]  | []                     | 0           | [grid[2][1]]           | |{2}| = 1    | 1
[1][1]  | [grid[0][0]]           | |{1}| = 1   | [grid[2][2]]           | |{1}| = 1    | 0
[1][2]  | [grid[0][1]]           | |{2}| = 1   | []                     | 0           | 1
[2][0]  | []                     | 0           | []                     | 0           | 0
[2][1]  | [grid[1][0]]           | |{3}| = 1   | []                     | 0           | 1
[2][2]  | [grid[0][0], grid[1][1]] | |{1, 1}| = 1 | []                   | 0           | 1

Example 2:

Input: grid = [[1]]
Output: [[0]]


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n, grid[i][j] <= 50

"""

# V0
# IDEA : DIAGONAL SWEEP + PREFIX / SUFFIX DISTINCT COUNTS
#
#   every cell belongs to exactly one top-left -> bottom-right diagonal, and
#   the diagonals are the ones starting at (i, 0) for each row i and at (0, j)
#   for each column j > 0. So flatten each diagonal into a list once, then:
#
#     - sweep forward keeping a running set -> pre[t] = #distinct STRICTLY
#       before position t on that diagonal (the "left & above" part)
#     - sweep backward the same way      -> suf[t] = #distinct STRICTLY
#       after position t (the "right & below" part)
#
#   answer at that cell = abs(pre[t] - suf[t]).
#
#   NOTE : the running set must be updated AFTER reading the count for the
#          current cell, since the cell itself is excluded from both sides.
#   NOTE : this is O(m * n) overall - each cell is touched a constant number
#          of times - vs the O(m * n * min(m, n)) naive re-walk per cell.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def differenceOfDistinctValues(self, grid):
        m, n = len(grid), len(grid[0])
        ans = [[0] * n for _ in range(m)]

        starts = [(i, 0) for i in range(m)] + [(0, j) for j in range(1, n)]
        for r, c in starts:
            cells = []
            x, y = r, c
            while x < m and y < n:
                cells.append((x, y))
                x, y = x + 1, y + 1

            k = len(cells)
            pre = [0] * k
            seen = set()
            for t in range(k):
                pre[t] = len(seen)
                seen.add(grid[cells[t][0]][cells[t][1]])

            seen = set()
            for t in range(k - 1, -1, -1):
                x, y = cells[t]
                ans[x][y] = abs(pre[t] - len(seen))
                seen.add(grid[x][y])

        return ans
