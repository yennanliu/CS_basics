"""

2319. Check if Matrix Is X-Matrix
Easy

A square matrix is said to be an X-Matrix if both of the following conditions hold:

All the elements in the diagonals of the matrix are non-zero.
All other elements are 0.

Given a 2D integer array grid of size n x n representing a square matrix, return true if grid is an X-Matrix. Otherwise, return false.


Example 1:

Input: grid = [[2,0,0,1],[0,3,1,0],[0,5,2,0],[4,0,0,2]]
Output: true
Explanation: Refer to the diagram above.
An X-Matrix should have the green elements (diagonals) be non-zero and the red elements be 0.
Thus, grid is an X-Matrix.

Example 2:

Input: grid = [[5,7,0],[0,3,1],[0,5,0]]
Output: false
Explanation: Refer to the diagram above.
An X-Matrix should have the green elements (diagonals) be non-zero and the red elements be 0.
Thus, grid is not an X-Matrix.


Constraints:

n == grid.length == grid[i].length
3 <= n <= 100
0 <= grid[i][j] <= 10^5

"""

# V0
# IDEA : DIRECT SCAN (a cell is on the X iff i == j or i + j == n - 1)
#
#   walk every cell once:
#     - on a diagonal -> the value must be non-zero
#     - off both diagonals -> the value must be zero
#   any violation ends the scan immediately.
#
#   NOTE : for odd n the centre cell satisfies both conditions at once;
#          the "on diagonal" branch already covers it correctly.
#
# time = O(n^2), space = O(1)
class Solution(object):
    def checkXMatrix(self, grid):
        n = len(grid)
        for i in range(n):
            for j in range(n):
                on_x = (i == j or i + j == n - 1)
                if on_x:
                    if grid[i][j] == 0:
                        return False
                elif grid[i][j] != 0:
                    return False
        return True


# V0-1
# IDEA : SET OF NON-ZERO CELLS == SET OF X CELLS
#
#   collect the coordinates of every non-zero entry, and compare that set with
#   the coordinate set of the two diagonals. One set equality settles BOTH
#   rules at once :
#     - a MISSING diagonal coordinate -> some diagonal cell was 0
#     - an EXTRA coordinate           -> some off-diagonal cell was non-zero
#   the odd-n centre cell is deduplicated by the set itself.
#
# time = O(n^2)
# space = O(n^2)
class Solution(object):
    def checkXMatrix(self, grid):
        n = len(grid)
        nonzero = {(i, j)
                   for i in range(n) for j in range(n) if grid[i][j] != 0}
        x_cells = ({(i, i) for i in range(n)}
                   | {(i, n - 1 - i) for i in range(n)})
        return nonzero == x_cells


# V0-2
# IDEA : PER-ROW ARITHMETIC (exploit grid[i][j] >= 0)
#
#   row i meets the X in at most two places : (i, i) and (i, n-1-i).
#   Since every entry is non-negative, "every other cell of the row is 0" is
#   exactly "sum(row) equals the sum of those one/two X entries".
#   So each row needs only :
#     - both X entries non-zero, and
#     - sum(row) == that expected total
#   -> no per-cell branching, just two lookups plus one row sum.
#
# time = O(n^2)
# space = O(1)
class Solution(object):
    def checkXMatrix(self, grid):
        n = len(grid)
        for i, row in enumerate(grid):
            a, b = i, n - 1 - i
            if row[a] == 0 or row[b] == 0:
                return False
            expected = row[a] if a == b else row[a] + row[b]
            if sum(row) != expected:
                return False
        return True
