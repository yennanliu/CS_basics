"""

2428. Maximum Sum of an Hourglass
Medium

You are given an m x n integer matrix grid.

We define an hourglass as a part of the matrix with the following form:

Return the maximum sum of the elements of an hourglass.

Note that an hourglass cannot be rotated and must be entirely contained within the matrix.


Example 1:

Input: grid = [[6,2,1,3],[4,2,1,5],[9,2,8,7],[4,1,2,9]]
Output: 30
Explanation: The cells shown above represent the hourglass with the maximum sum: 6 + 2 + 1 + 2 + 9 + 2 + 8 = 30.

Example 2:

Input: grid = [[1,2,3],[4,5,6],[7,8,9]]
Output: 35
Explanation: There is only one hourglass in the matrix, so we return 35.


Constraints:

m == grid.length
n == grid[i].length
3 <= m, n <= 150
0 <= grid[i][j] <= 10^6

"""

# V0
# IDEA : SLIDE THE FIXED 7-CELL SHAPE OVER EVERY 3 x 3 WINDOW
#
#   the hourglass is the full top row, the middle cell, and the full bottom
#   row of a 3 x 3 block — i.e. a 3 x 3 sum minus the middle row's two side
#   cells.
#
#   the shape cannot rotate, so anchoring on the top-left corner (i, j) with
#   i <= m-3 and j <= n-3 enumerates them all.
#
# time = O(m * n), space = O(1)
class Solution(object):
    def maxSum(self, grid):
        m, n = len(grid), len(grid[0])
        res = 0
        for i in range(m - 2):
            for j in range(n - 2):
                total = (sum(grid[i][j:j + 3])
                         + grid[i + 1][j + 1]
                         + sum(grid[i + 2][j:j + 3]))
                res = max(res, total)
        return res


# V0-1
# IDEA : 2D PREFIX SUM — 3 x 3 BLOCK MINUS THE MIDDLE ROW'S SIDE CELLS
#
#   an hourglass anchored at (i, j) is the whole 3 x 3 block with two cells
#   removed :
#       hourglass = block(i, j) - grid[i + 1][j] - grid[i + 1][j + 2]
#
#   with a standard (m+1) x (n+1) prefix table any rectangle is 4 lookups, so
#   the block costs O(1) no matter how big the shape gets — the same code
#   generalises to a k x k window by changing one constant.
#
# time = O(m * n), space = O(m * n)
class Solution(object):
    def maxSum(self, grid):
        m, n = len(grid), len(grid[0])
        pre = [[0] * (n + 1) for _ in range(m + 1)]
        for i in range(m):
            for j in range(n):
                pre[i + 1][j + 1] = (grid[i][j] + pre[i][j + 1]
                                     + pre[i + 1][j] - pre[i][j])
        res = 0
        for i in range(m - 2):
            for j in range(n - 2):
                block = (pre[i + 3][j + 3] - pre[i][j + 3]
                         - pre[i + 3][j] + pre[i][j])
                cur = block - grid[i + 1][j] - grid[i + 1][j + 2]
                if cur > res:
                    res = cur
        return res


# V0-2
# IDEA : ROLLING WIDTH-3 ROW WINDOWS, ONLY THREE ROWS HELD AT A TIME
#
#   the top and bottom bars are plain 3-cell row runs, so sweep each row once
#   with a sliding window (add the entering cell, drop the leaving one) to get
#       win[j] = grid[i][j] + grid[i][j + 1] + grid[i][j + 2]
#   then an hourglass is win_top[j] + grid[mid][j + 1] + win_bottom[j].
#
#   nothing is ever re-summed and no full table is kept : only the window rows
#   of the last three grid rows live at once, so the extra memory is O(n)
#   instead of O(m * n).
#
# time = O(m * n), space = O(n)
class Solution(object):
    def maxSum(self, grid):
        m, n = len(grid), len(grid[0])
        res = 0
        win = []                          # window sums of the last <= 3 rows
        for i in range(m):
            row = grid[i]
            cur = row[0] + row[1] + row[2]
            w = [cur]
            for j in range(3, n):
                cur += row[j] - row[j - 3]
                w.append(cur)
            win.append(w)
            if len(win) > 3:
                win.pop(0)
            if len(win) == 3:
                mid = grid[i - 1]         # rows i-2, i-1, i -> middle is i-1
                for j in range(n - 2):
                    total = win[0][j] + mid[j + 1] + win[2][j]
                    if total > res:
                        res = total
        return res
