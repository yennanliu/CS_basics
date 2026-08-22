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


# V0-1
# IDEA : BRUTE FORCE (walk both diagonal rays out of every cell)
#
#   for each cell, step up-left collecting values into a set until falling off
#   the grid, then step down-right into a second set, and take the absolute
#   difference of the two set sizes. The cell itself is never added, which is
#   exactly the "not including grid[r][c]" clause.
#
#   each cell walks up to min(m, n) steps in each direction, so this is the
#   O(m * n * min(m, n)) version - fine at m, n <= 50, and the obvious oracle
#   for the linear sweeps.
#
# time = O(m * n * min(m, n)), space = O(min(m, n))
class Solution(object):
    def differenceOfDistinctValues(self, grid):
        m, n = len(grid), len(grid[0])
        ans = [[0] * n for _ in range(m)]

        for r in range(m):
            for c in range(n):
                left_above = set()
                x, y = r - 1, c - 1
                while x >= 0 and y >= 0:
                    left_above.add(grid[x][y])
                    x, y = x - 1, y - 1

                right_below = set()
                x, y = r + 1, c + 1
                while x < m and y < n:
                    right_below.add(grid[x][y])
                    x, y = x + 1, y + 1

                ans[r][c] = abs(len(left_above) - len(right_below))
        return ans


# V0-2
# IDEA : GROUP BY (r - c) + SHRINKING MULTISET, ONE PASS PER DIAGONAL
#
#   cells on the same top-left -> bottom-right diagonal all share the key
#   r - c, so a defaultdict keyed on r - c collects the diagonals without any
#   "find the starting cell" logic.
#
#   then a single forward pass per diagonal, carrying TWO structures :
#     - `left`  : a set that GROWS behind the cursor
#     - `right` : a Counter (multiset) of everything ahead, seeded with the
#                 whole diagonal, from which the current cell is removed
#                 before it is read
#   len(right) is the number of distinct values still ahead, so no prefix
#   array and no backward pass are needed - the multiset is what makes the
#   "distinct count of a shrinking suffix" query O(1).
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def differenceOfDistinctValues(self, grid):
        from collections import Counter, defaultdict

        m, n = len(grid), len(grid[0])
        ans = [[0] * n for _ in range(m)]

        diag = defaultdict(list)
        for r in range(m):
            for c in range(n):
                diag[r - c].append((r, c))

        for cells in diag.values():
            right = Counter(grid[x][y] for x, y in cells)
            left = set()
            for x, y in cells:
                v = grid[x][y]
                right[v] -= 1
                if right[v] == 0:
                    del right[v]
                ans[x][y] = abs(len(left) - len(right))
                left.add(v)
        return ans
