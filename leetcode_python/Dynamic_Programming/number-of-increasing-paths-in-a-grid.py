"""

2328. Number of Increasing Paths in a Grid
Hard

You are given an m x n integer matrix grid, where you can move from a cell to any adjacent cell in all 4 directions.

Return the number of strictly increasing paths in the grid such that you can start from any cell and end at any cell. Since the answer may be very large, return it modulo 10^9 + 7.

Two paths are considered different if they do not have exactly the same sequence of visited cells.


Example 1:

Input: grid = [[1,1],[3,4]]
Output: 8
Explanation: The strictly increasing paths are:
- Paths with length 1: [1], [1], [3], [4].
- Paths with length 2: [1 -> 3], [1 -> 4], [3 -> 4].
- Paths with length 3: [1 -> 3 -> 4].
The total number of paths is 4 + 3 + 1 = 8.

Example 2:

Input: grid = [[1],[2]]
Output: 3
Explanation: The strictly increasing paths are:
- Paths with length 1: [1], [2].
- Paths with length 2: [1 -> 2].
The total number of paths is 2 + 1 = 3.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 1000
1 <= m * n <= 10^5
1 <= grid[i][j] <= 10^5

"""

# V0
# IDEA : PROCESS THE CELLS IN ASCENDING VALUE ORDER — NO RECURSION NEEDED
#
#   dp[c] = number of strictly increasing paths ENDING at cell c :
#       dp[c] = 1 + sum of dp[nb] over neighbours with a SMALLER value
#   (the 1 is the length-one path consisting of c alone).
#
#   the answer is the sum of dp over every cell, since each path is counted
#   exactly once at its final cell.
#
#   the dependency is always "smaller value first", so sorting the cells by
#   value and sweeping in that order fills the table with no memoised DFS —
#   which matters here because m * n can be 10^5 and a recursive chain could
#   be that deep.
#
# time = O(m * n log(m * n)), space = O(m * n)
class Solution(object):
    def countPaths(self, grid):
        MOD = 10 ** 9 + 7
        m, n = len(grid), len(grid[0])

        cells = sorted(((grid[i][j], i, j) for i in range(m) for j in range(n)))
        dp = [[1] * n for _ in range(m)]

        res = 0
        for val, i, j in cells:
            for di, dj in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                x, y = i + di, j + dj
                if 0 <= x < m and 0 <= y < n and grid[x][y] < val:
                    dp[i][j] = (dp[i][j] + dp[x][y]) % MOD
            res = (res + dp[i][j]) % MOD
        return res
