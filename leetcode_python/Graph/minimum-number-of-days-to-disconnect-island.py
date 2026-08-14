"""

1568. Minimum Number of Days to Disconnect Island
Medium

You are given an m x n binary grid grid where 1 represents land and 0 represents water. An island is a maximal 4-directionally (horizontal or vertical) connected group of 1's.

The grid is said to be connected if we have exactly one island, otherwise is said disconnected.

In one day, we are allowed to change any single land cell (1) into a water cell (0).

Return the minimum number of days to disconnect the grid.

Example 1:

Input: grid = [[0,1,1,0],[0,1,1,0],[0,0,0,0]]

Output: 2
Explanation: We need at least 2 days to get a disconnected grid.
Change land grid[1][1] and grid[0][2] to water and get 2 disconnected island.

Example 2:

Input: grid = [[1,1]]
Output: 2
Explanation: Grid of full water is also disconnected ([[1,1]] -> [[0,0]]), 0 islands.

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 30
grid[i][j] is either 0 or 1.

"""

# V0
# IDEA : BRUTE FORCE + DFS (the answer is always 0, 1 or 2)
#
#   any island has a corner cell whose 2 neighbours can be removed, so
#   2 days always suffice -> we only have to test 0 and 1.
#     - already disconnected (island count != 1) -> 0
#     - try flipping each single land cell, re-count -> 1 if it works
#     - otherwise -> 2
#   NOTE : "disconnected" includes the 0-island case (all water).
#
# time = O((m * n)^2), space = O(m * n)
class Solution(object):
    def minDays(self, grid):
        m, n = len(grid), len(grid[0])

        def count():
            seen = [[False] * n for _ in range(m)]
            total = 0
            for i in range(m):
                for j in range(n):
                    if grid[i][j] == 1 and not seen[i][j]:
                        total += 1
                        seen[i][j] = True
                        stack = [(i, j)]
                        while stack:
                            x, y = stack.pop()
                            for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                                a, b = x + dx, y + dy
                                if 0 <= a < m and 0 <= b < n \
                                        and grid[a][b] == 1 and not seen[a][b]:
                                    seen[a][b] = True
                                    stack.append((a, b))
            return total

        if count() != 1:
            return 0
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1:
                    grid[i][j] = 0
                    ok = count() != 1
                    grid[i][j] = 1
                    if ok:
                        return 1
        return 2
