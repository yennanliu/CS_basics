"""

3417. Zigzag Grid Traversal With Skip
Easy

You are given an m x n 2D array grid of positive integers.

Your task is to traverse grid in a zigzag pattern while skipping every alternate
cell.

Zigzag pattern traversal is defined as following the below actions:

Start at the top-left cell (0, 0).
Move right within a row until the end of the row is reached.
Drop down to the next row, then traverse left until the beginning of the row is
reached.
Continue alternating between right and left traversal until every row has been
traversed.

Note that you must skip every alternate cell during the traversal.

Return an array of integers result containing, in order, the value of the cells
visited during the zigzag traversal with skips.

Example 1:

Input: grid = [[1,2],[3,4]]

Output: [1,4]

Explanation:

Example 2:

Input: grid = [[2,1],[2,1],[2,1]]

Output: [2,1,2]

Explanation:

Example 3:

Input: grid = [[1,2,3],[4,5,6],[7,8,9]]

Output: [1,3,5,7,9]

Explanation:

Constraints:

2 <= n == grid.length <= 50
2 <= m == grid[i].length <= 50
1 <= grid[i][j] <= 2500

"""

# V0
# IDEA : LINEARISE THE BOUSTROPHEDON ORDER, THEN TAKE EVERY OTHER CELL
#
#   the two rules — snake through the grid, and skip every second cell — act on
#   *the same* sequence, so the cleanest reading is to first flatten the grid in
#   snake order (left to right on even rows, right to left on odd rows) and then
#   keep the entries at even positions of that flattened list.
#
#   doing it in this order avoids the off-by-one trap of trying to express the
#   skip as a parity condition on (i, j): the skip pattern carries across the
#   row boundary, and whether a row starts on a kept or a dropped cell depends
#   on the row width.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def zigzagTraversal(self, grid):
        flat = []
        for i, row in enumerate(grid):
            flat.extend(row if i % 2 == 0 else row[::-1])
        return flat[::2]
