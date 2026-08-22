"""

3643. Flip Square Submatrix Vertically
Easy

You are given an m x n integer matrix grid, and three integers x, y, and k.

The integers x and y represent the row and column indices of the top-left
corner of a square submatrix and the integer k represents the size (side
length) of the square submatrix.

Your task is to flip the submatrix by reversing the order of its rows
vertically.

Return the updated matrix.

Example 1:

Input: grid = [[1,2,3,4],[5,6,7,8],[9,10,11,12],[13,14,15,16]], x = 1, y = 0, k = 3
Output: [[1,2,3,4],[13,14,15,8],[9,10,11,12],[5,6,7,16]]
Explanation:
The diagram above shows the grid before and after the transformation.

Example 2:

Input: grid = [[3,4,2,3],[2,3,4,2]], x = 0, y = 2, k = 2
Output: [[3,4,4,2],[2,3,2,3]]
Explanation:
The diagram above shows the grid before and after the transformation.

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 50
1 <= grid[i][j] <= 100
0 <= x < m
0 <= y < n
1 <= k <= min(m - x, n - y)

"""

# V0
# IDEA : SWAP MIRRORED ROW SLICES IN PLACE
#
#   a vertical flip only permutes whole rows of the window: row x+i trades
#   places with row x+k-1-i, and the columns [y, y+k) travel unchanged.
#
#   so walk i over the top half only and swap the two k-wide slices; the
#   middle row of an odd-sized window is its own mirror and stays put.
#
# time = O(k^2), space = O(1)
class Solution(object):
    def reverseSubmatrix(self, grid, x, y, k):
        for i in range(k // 2):
            top = x + i
            bot = x + k - 1 - i
            for j in range(y, y + k):
                grid[top][j], grid[bot][j] = grid[bot][j], grid[top][j]
        return grid


# V0-1
# IDEA : COPY THE WINDOW OUT, REVERSE IT, PASTE IT BACK
#
#   lift the k x k window out as its own list of row slices. a vertical
#   flip IS a reversal of that list, so reverse it and write the rows back
#   with slice assignment -- no mirrored-index arithmetic anywhere.
#
# time = O(k^2)
# space = O(k^2)
class Solution(object):
    def reverseSubmatrix(self, grid, x, y, k):
        block = [grid[r][y:y + k] for r in range(x, x + k)]
        block.reverse()
        for i in range(k):
            grid[x + i][y:y + k] = block[i]
        return grid


# V0-2
# IDEA : REBUILD THE GRID FROM A COORDINATE MAP (NO MUTATION AT ALL)
#
#   state the answer as a pure function of the source cell : a cell inside
#   the window reads from its mirror row 2*x + k - 1 - r (same column), and
#   every cell outside reads from itself. materialising that map builds a
#   fresh matrix and leaves the input untouched, which is what you want when
#   the caller still needs the original grid.
#
# time = O(m * n)
# space = O(m * n)
class Solution(object):
    def reverseSubmatrix(self, grid, x, y, k):
        m, n = len(grid), len(grid[0])
        res = []
        for r in range(m):
            row = []
            for c in range(n):
                if x <= r < x + k and y <= c < y + k:
                    row.append(grid[2 * x + k - 1 - r][c])
                else:
                    row.append(grid[r][c])
            res.append(row)
        return res
