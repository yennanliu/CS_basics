"""

2577. Minimum Time to Visit a Cell In a Grid
Hard

You are given a m x n matrix grid consisting of non-negative integers where grid[row][col]
represents the minimum time required to be able to visit the cell (row, col), which means you
can visit the cell (row, col) only when the time you visit it is greater than or equal to
grid[row][col].

You are standing in the top-left cell of the matrix in the 0th second, and you must move to any
adjacent cell in the four directions: up, down, left, and right. Each move you make takes 1 second.

Return the minimum time required in which you can visit the bottom-right cell of the matrix.
If you cannot visit the bottom-right cell, then return -1.


Example 1:

Input: grid = [[0,1,3,2],[5,1,2,5],[4,3,8,6]]
Output: 7
Explanation: One of the paths that we can take is the following:
- at t = 0, we are on the cell (0,0).
- at t = 1, we move to the cell (0,1). It is possible because grid[0][1] <= 1.
- at t = 2, we move to the cell (1,1). It is possible because grid[1][1] <= 2.
- at t = 3, we move to the cell (1,2). It is possible because grid[1][2] <= 3.
- at t = 4, we move to the cell (1,1). It is possible because grid[1][1] <= 4.
- at t = 5, we move to the cell (1,2). It is possible because grid[1][2] <= 5.
- at t = 6, we move to the cell (1,3). It is possible because grid[1][3] <= 6.
- at t = 7, we move to the cell (2,3). It is possible because grid[2][3] <= 7.
The final time is 7. It can be shown that it is the minimum time possible.

Example 2:

Input: grid = [[0,2,4],[3,2,1],[1,0,4]]
Output: -1
Explanation: There is no path from the top left to the bottom-right cell.


Constraints:

m == grid.length
n == grid[i].length
2 <= m, n <= 1000
4 <= m * n <= 10^5
0 <= grid[i][j] <= 10^5
grid[0][0] == 0

"""

# V0
# IDEA : DIJKSTRA (BFS + MIN HEAP) + PARITY WAITING TRICK
#
#   we can never stand still, but we CAN bounce between two already-visited
#   cells to burn time. one bounce costs 2 seconds, so from a cell reached at
#   time t we may arrive at a neighbour at any time t + 1, t + 3, t + 5, ...
#   i.e. any time >= t + 1 with the SAME PARITY as t + 1.
#
#   NOTE : so if t + 1 < grid[x][y] we do not just wait until grid[x][y] — we
#          must land on a time of the right parity:
#              nt = grid[x][y] + ((grid[x][y] - (t + 1)) % 2)
#          (python's % is non-negative, so this adds 0 or 1 exactly right)
#
#   NOTE : the bouncing trick needs a second cell to bounce off. If BOTH
#          grid[0][1] > 1 and grid[1][0] > 1 we cannot make even the first move
#          at t = 1, and we are stuck forever -> return -1. This is the ONLY
#          unreachable case; once one first step exists the whole grid opens up.
#
#   after that special case, run Dijkstra with a min-heap over (time, r, c) —
#   edge "weights" are not uniform (waiting inflates them), so plain BFS is not
#   enough.
#
# time = O(m * n * log(m * n)), space = O(m * n)
import heapq
class Solution(object):
    def minimumTime(self, grid):
        m, n = len(grid), len(grid[0])
        # if we cannot even take the very first step, we can never move at all
        if grid[0][1] > 1 and grid[1][0] > 1:
            return -1

        INF = float('inf')
        dist = [[INF] * n for _ in range(m)]
        dist[0][0] = 0
        pq = [(0, 0, 0)]
        dirs = ((-1, 0), (1, 0), (0, -1), (0, 1))

        while pq:
            t, i, j = heapq.heappop(pq)
            if i == m - 1 and j == n - 1:
                return t
            if t > dist[i][j]:
                continue
            for di, dj in dirs:
                x, y = i + di, j + dj
                if 0 <= x < m and 0 <= y < n:
                    nt = t + 1
                    if nt < grid[x][y]:
                        # wait by bouncing; keep parity of (t + 1)
                        nt = grid[x][y] + (grid[x][y] - nt) % 2
                    if nt < dist[x][y]:
                        dist[x][y] = nt
                        heapq.heappush(pq, (nt, x, y))
        return -1
