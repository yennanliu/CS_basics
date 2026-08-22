"""

3142. Check if Grid Satisfies Conditions
Easy

You are given a 2D matrix grid of size m x n. You need to check if each cell grid[i][j] is:

Equal to the cell below it, i.e. grid[i][j] == grid[i + 1][j] (if it exists).
Different from the cell to its right, i.e. grid[i][j] != grid[i][j + 1] (if it exists).

Return true if all the cells satisfy these conditions, otherwise, return false.


Example 1:

Input: grid = [[1,0,2],[1,0,2]]
Output: true
Explanation:
All the cells in the grid satisfy the conditions.

Example 2:

Input: grid = [[1,1,1],[0,0,0]]
Output: false
Explanation:
All cells in the first row are equal.

Example 3:

Input: grid = [[1],[2],[3]]
Output: false
Explanation:
Cells in the first column have different values.


Constraints:

1 <= n, m <= 10
0 <= grid[i][j] <= 9

"""

# V0
# IDEA : TWO LOCAL CHECKS PER CELL, GUARDED BY THE BOUNDS
#
#   "equal below" and "different to the right" only ever compare neighbours,
#   so a single sweep suffices — the conditions are vacuous on the last row
#   and last column respectively.
#
# time = O(m * n), space = O(1)
class Solution(object):
    def satisfiesConditions(self, grid):
        m, n = len(grid), len(grid[0])
        for i in range(m):
            for j in range(n):
                if i + 1 < m and grid[i][j] != grid[i + 1][j]:
                    return False
                if j + 1 < n and grid[i][j] == grid[i][j + 1]:
                    return False
        return True


# V0-1
# IDEA : COLUMN AGGREGATION WITH set (via zip(*grid)) + ROW WISE ADJACENCY
#
#   chained equality down a column means the column holds ONE distinct value,
#   i.e. len(set(col)) == 1 — an aggregate test rather than a pairwise one.
#   zip(*grid) transposes the grid and hands us those columns directly; the
#   "different to the right" rule stays pairwise, via zip(row, row[1:]).
#
# time = O(m * n), space = O(m + n)
class Solution(object):
    def satisfiesConditions(self, grid):
        for col in zip(*grid):
            if len(set(col)) > 1:
                return False
        for row in grid:
            if any(a == b for a, b in zip(row, row[1:])):
                return False
        return True


# V0-2
# IDEA : REDUCE TO "EVERY ROW EQUALS ROW 0", THEN ONE ADJACENCY SCAN
#
#   grid[i][j] == grid[i + 1][j] chained down every column forces ALL rows to
#   be identical to grid[0]. so compare whole rows with == (one shot per row),
#   and once that holds the "different to the right" rule only has to be
#   checked on grid[0] — every other row is a copy of it.
#
# time = O(m * n), space = O(1)
class Solution(object):
    def satisfiesConditions(self, grid):
        first = grid[0]
        for i in range(1, len(grid)):
            if grid[i] != first:
                return False
        for j in range(len(first) - 1):
            if first[j] == first[j + 1]:
                return False
        return True
