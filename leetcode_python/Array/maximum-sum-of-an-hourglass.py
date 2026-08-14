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
