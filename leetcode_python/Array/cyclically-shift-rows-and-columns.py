"""

4052. Cyclically Shift Rows and Columns
Easy

You are given an integer n, a 2D integer array grid of size n x n,
and two integer arrays rowShift and colShift, each of length n, where:

rowShift[i] represents the number of positions to cyclically shift
the i-th row of grid to the left.

colShift[j] represents the number of positions to cyclically shift
the j-th column of grid upward.

First, cyclically shift each row according to rowShift, then cyclically
shift each column of the resulting grid according to colShift.

Return the resulting grid after performing all the shifts.

A cyclic left shift of a row by k positions moves the element at column j
to column (j - k + n) % n. All other rows remain unchanged.

A cyclic upward shift of a column by k positions moves the element at row i
to row (i - k + n) % n. All other columns remain unchanged.


Example 1:

Input: n = 2, grid = [[1,2],[3,4]], rowShift = [1,0], colShift = [0,1]

Output: [[2,4],[3,1]]

Explanation:

row 0 shifts left by 1 -> [2,1], row 1 stays -> [3,4], so the grid is [[2,1],[3,4]].
col 0 stays -> [2,3], col 1 shifts up by 1 -> [4,1], giving [[2,4],[3,1]].

Example 2:

Input: n = 3, grid = [[1,2,3],[4,5,6],[7,8,9]], rowShift = [1,2,0], colShift = [2,2,1]

Output: [[7,8,5],[2,3,9],[6,4,1]]

Explanation:

after the row shifts the grid is [[2,3,1],[6,4,5],[7,8,9]]
(row 0 left by 1, row 1 left by 2, row 2 unchanged).

then col 0 = [2,6,7] up by 2 -> [7,2,6], col 1 = [3,4,8] up by 2 -> [8,3,4],
col 2 = [1,5,9] up by 1 -> [5,9,1].


Constraints:

1 <= n == grid.length == grid[i].length <= 10

1 <= grid[i][j] <= 100

rowShift.length == colShift.length == n

0 <= rowShift[i], colShift[i] < n

"""

# V0
# IDEA : BRUTE FORCE (SIMULATE THE 2 PASSES), WRITE INTO A FRESH GRID
#
#   the statement hands us the "push" form directly :
#
#     row i shifted LEFT by k  ->  the value at (i, j) lands at (i, (j - k + n) % n)
#     col j shifted UP   by k  ->  the value at (i, j) lands at ((i - k + n) % n, j)
#
#   so each pass is just "read every cell, write it where the rule says".
#
# NOTE !!! the destination MUST be a NEW grid, not the one being read
#
#   shifting a row in place overwrites cells that have not been read yet :
#
#     [1,2,3] left by 1, in place :
#        grid[0] = grid[1] -> [2,2,3]   <-- the original 2 is gone
#        grid[1] = grid[2] -> [2,3,3]
#        grid[2] = grid[0] -> [2,3,2]   <-- reads the ALREADY OVERWRITTEN grid[0]
#
#   and the 2 passes are ordered (rows first, then columns), so the column pass
#   reads the grid the row pass produced - never the original one.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def cyclicShift(self, n, grid, rowShift, colShift):
        """
        :type n: int
        :type grid: List[List[int]]
        :type rowShift: List[int]
        :type colShift: List[int]
        :rtype: List[List[int]]
        """
        # edge
        if not grid or not grid[0]:
            return grid

        # pass 1) shift every ROW left
        after_row = [[0] * n for _ in range(n)]

        for i in range(n):
            # NOTE !!! i is the ROW INDEX, so the shift is rowShift[i]
            k = rowShift[i]
            for j in range(n):
                after_row[i][(j - k + n) % n] = grid[i][j]

        # pass 2) shift every COLUMN of the result upward
        res = [[0] * n for _ in range(n)]

        for j in range(n):
            k = colShift[j]
            for i in range(n):
                res[(i - k + n) % n][j] = after_row[i][j]

        return res


# V0-1
# IDEA : COMPOSE THE 2 SHIFTS INTO ONE INDEX FORMULA (SINGLE PASS, NO TMP GRID)
#
#   instead of PUSHING values forward twice, PULL each answer cell once :
#
#     res[i][j] comes from after_row[(i + colShift[j]) % n][j]
#                          ^ the up-shift read backwards
#
#     and after_row[r][j] comes from grid[r][(j + rowShift[r]) % n]
#                                          ^ the left-shift read backwards
#
#   substituting r = (i + colShift[j]) % n :
#
#     res[i][j] = grid[r][(j + rowShift[r]) % n]
#
# NOTE !!! rowShift is indexed by r (the SOURCE row), not by i - the column pass
#          moved the cell, so the row it came from is the row whose shift applied.
#
#   justified over V0 : one pass and NO intermediate grid (O(1) extra space
#   beyond the output), at the cost of having to get the composition right.
#
# time = O(n^2), space = O(1) extra (excluding the returned grid)
class Solution2(object):
    def cyclicShift(self, n, grid, rowShift, colShift):
        """
        :type n: int
        :type grid: List[List[int]]
        :type rowShift: List[int]
        :type colShift: List[int]
        :rtype: List[List[int]]
        """
        # edge
        if not grid or not grid[0]:
            return grid

        res = [[0] * n for _ in range(n)]

        for i in range(n):
            for j in range(n):
                r = (i + colShift[j]) % n
                res[i][j] = grid[r][(j + rowShift[r]) % n]

        return res
