"""

1293. Shortest Path in a Grid with Obstacles Elimination
Hard

You are given an m x n integer matrix grid where each cell is either 0 (empty)
or 1 (obstacle). You can move up, down, left, or right from and to an empty cell
in one step.

Return the minimum number of steps to walk from the upper left corner (0, 0)
to the lower right corner (m - 1, n - 1) given that you can eliminate at most k
obstacles. If it is not possible to find such walk return -1.


Example 1:

Input: grid = [[0,0,0],[1,1,0],[0,0,0],[0,1,1],[0,0,0]], k = 1
Output: 6
Explanation:
The shortest path without eliminating any obstacle is 10.
The shortest path with one obstacle elimination at position (3,2) is 6.
Such path is (0,0) -> (0,1) -> (0,2) -> (1,2) -> (2,2) -> (3,2) -> (4,2).

Example 2:

Input: grid = [[0,1,1],[1,1,1],[1,0,0]], k = 1
Output: -1
Explanation: We need to eliminate at least two obstacles to find such a walk.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 40
1 <= k <= m * n
grid[i][j] is either 0 or 1.
grid[0][0] == grid[m - 1][n - 1] == 0

"""

# V0
# IDEA: BFS on the 3D STATE (row, col, remaining_k)
#
#  -> plain BFS on (row, col) is not enough:
#     reaching a cell with MORE eliminations left may be better later.
#     so the visited set must be keyed by (row, col, k_left).
#
# time = O(m * n * k)
# space = O(m * n * k)
from collections import deque
class Solution(object):
    def shortestPath(self, grid, k):
        m, n = len(grid), len(grid[0])

        # edge : the Manhattan path is always walkable if k is big enough
        if k >= m + n - 3:
            return m + n - 2

        q = deque([(0, 0, k, 0)])   # (row, col, k_left, steps)
        visited = {(0, 0, k)}

        while q:
            i, j, kk, step = q.popleft()
            if i == m - 1 and j == n - 1:
                return step
            for dx, dy in [(0, 1), (0, -1), (1, 0), (-1, 0)]:
                x, y = i + dx, j + dy
                if not (0 <= x < m and 0 <= y < n):
                    continue
                """
                NOTE !!!

                the new remaining quota is `kk - grid[x][y]`
                -> stepping on an obstacle (1) costs one elimination
                """
                nk = kk - grid[x][y]
                if nk >= 0 and (x, y, nk) not in visited:
                    visited.add((x, y, nk))
                    q.append((x, y, nk, step + 1))

        return -1
