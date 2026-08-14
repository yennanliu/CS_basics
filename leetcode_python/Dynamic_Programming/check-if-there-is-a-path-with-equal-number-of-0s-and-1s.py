"""

2510. Check if There is a Path With Equal Number of 0's And 1's
Medium

You are given a 0-indexed m x n binary matrix grid. You can move from a cell (row, col) to any of the cells (row + 1, col) or (row, col + 1).

Return true if there is a path from (0, 0) to (m - 1, n - 1) that visits an equal number of 0's and 1's. Otherwise return false.


Example 1:

Input: grid = [[0,1,0,0],[0,1,0,0],[1,0,1,0]]
Output: true
Explanation: The path colored in blue in the above diagram is a valid path because we have 3 cells with a value of 1 and 3 with a value of 0. Since there is a valid path, we return true.

Example 2:

Input: grid = [[1,1,0],[0,0,1],[1,0,0]]
Output: false
Explanation: There is no path in this grid with an equal number of 0's and 1's.


Constraints:

m == grid.length
n == grid[i].length
2 <= m, n <= 100
grid[i][j] is either 0 or 1.

"""

# V0
# IDEA : DP + BITMASK (bitset of reachable "number of 1's")
#
#   every monotone path from (0,0) to (m-1,n-1) visits exactly m + n - 1 cells,
#   so "equal number of 0's and 1's" == "exactly (m + n - 1) / 2 ones".
#
#   let dp[i][j] be a bitmask where bit b is set iff some path (0,0) -> (i,j)
#   collects exactly b ones. then
#
#       dp[i][j] = (dp[i-1][j] | dp[i][j-1]) << grid[i][j]
#
#   because appending cell (i,j) shifts every reachable count up by grid[i][j].
#
#   NOTE : the parity check must come first — if m + n - 1 is odd no path can
#          possibly split evenly, and (m + n - 1) / 2 would not be an integer.
#
# time = O(m * n * (m + n) / 64), space = O(n * (m + n) / 64)
class Solution(object):
    def isThereAPath(self, grid):
        m = len(grid)
        n = len(grid[0])
        total = m + n - 1
        if total % 2 == 1:
            return False
        half = total // 2

        # dp over one row at a time (each entry is a big-int bitmask)
        dp = [0] * n
        for i in range(m):
            for j in range(n):
                if i == 0 and j == 0:
                    reach = 1  # bit 0 set : zero ones collected so far
                else:
                    reach = 0
                    if i > 0:
                        reach |= dp[j]
                    if j > 0:
                        reach |= dp[j - 1]
                dp[j] = reach << grid[i][j]

        return (dp[n - 1] >> half) & 1 == 1
