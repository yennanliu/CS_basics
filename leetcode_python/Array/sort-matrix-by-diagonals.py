"""

3446. Sort Matrix by Diagonals
Medium

You are given an n x n square matrix of integers grid. Return the matrix such
that:

The diagonals in the bottom-left triangle (including the middle diagonal) are
sorted in non-increasing order.
The diagonals in the top-right triangle are sorted in non-decreasing order.

Example 1:

Input: grid = [[1,7,3],[9,8,2],[4,5,6]]

Output: [[8,2,3],[9,6,7],[4,5,1]]

Explanation:

The diagonals with a black arrow (bottom-left triangle) should be sorted in non-
increasing order:

[1, 8, 6] becomes [8, 6, 1].
[9, 5] and [4] remain unchanged.

The diagonals with a blue arrow (top-right triangle) should be sorted in non-
decreasing order:

[7, 2] becomes [2, 7].
[3] remains unchanged.

Example 2:

Input: grid = [[0,1],[1,2]]

Output: [[2,1],[1,0]]

Explanation:

The diagonals with a black arrow must be non-increasing, so [0, 2] is changed to
[2, 0]. The other diagonals are already in the correct order.

Example 3:

Input: grid = [[1]]

Output: [[1]]

Explanation:

Diagonals with exactly one element are already in order, so no changes are
needed.

Constraints:

grid.length == grid[i].length == n
1 <= n <= 10
-10^5 <= grid[i][j] <= 10^5

"""

# V0
# IDEA : GROUP CELLS BY i - j, SORT EACH DIAGONAL IN ITS OWN DIRECTION
#
#   a diagonal that runs down-right is exactly the set of cells sharing the
#   value i - j, so bucketing on that key collects each diagonal in one pass and
#   in top-to-bottom order.
#
#   the sign of the key also tells us which triangle we are in: i - j >= 0 is
#   the bottom-left half including the main diagonal (sort descending), i - j < 0
#   is the top-right half (sort ascending).  writing the sorted bucket back in
#   the same traversal order restores the matrix.
#
# time = O(n^2 log n), space = O(n^2)
from collections import defaultdict


class Solution(object):
    def sortMatrix(self, grid):
        n = len(grid)
        cells = defaultdict(list)
        for i in range(n):
            for j in range(n):
                cells[i - j].append(grid[i][j])
        for key in cells:
            cells[key].sort(reverse=(key >= 0))
        pos = defaultdict(int)
        for i in range(n):
            for j in range(n):
                key = i - j
                grid[i][j] = cells[key][pos[key]]
                pos[key] += 1
        return grid
