"""

3239. Minimum Number of Flips to Make Binary Grid Palindromic I
Medium

You are given an m x n binary matrix grid.

A row or column is considered palindromic if its values read the same forward and backward.

You can flip any number of cells in grid from 0 to 1, or from 1 to 0.

Return the minimum number of cells that need to be flipped to make either all rows palindromic or all columns palindromic.


Example 1:

Input: grid = [[1,0,0],[0,0,0],[0,0,1]]
Output: 2
Explanation:
Flipping the highlighted cells makes all the rows palindromic.

Example 2:

Input: grid = [[0,1],[0,1],[0,0]]
Output: 1
Explanation:
Flipping the highlighted cell makes all the columns palindromic.

Example 3:

Input: grid = [[1],[0]]
Output: 0
Explanation:
All rows are already palindromic.


Constraints:

m == grid.length
n == grid[i].length
1 <= m * n <= 2 * 10^5

"""

# V0
# IDEA : COUNT THE MISMATCHED MIRROR PAIRS, ONCE PER ORIENTATION
#
#   making one row palindromic means every mirrored pair inside it must
#   agree, and a disagreeing pair costs exactly one flip (change either
#   side). the pairs are independent, so the row cost is just the number of
#   mismatched pairs across all rows — and the column cost is the same count
#   taken down the columns.
#
#   the task allows satisfying EITHER orientation, so the answer is the
#   smaller of the two totals.
#
#   odd-length lines have a middle cell with no partner, which never costs
#   anything.
#
# time = O(m * n), space = O(1)
class Solution(object):
    def minFlips(self, grid):
        m, n = len(grid), len(grid[0])

        row_cost = 0
        for i in range(m):
            for j in range(n // 2):
                if grid[i][j] != grid[i][n - 1 - j]:
                    row_cost += 1

        col_cost = 0
        for j in range(n):
            for i in range(m // 2):
                if grid[i][j] != grid[m - 1 - i][j]:
                    col_cost += 1

        return min(row_cost, col_cost)
