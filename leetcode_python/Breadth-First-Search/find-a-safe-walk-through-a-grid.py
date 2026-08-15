"""

3286. Find a Safe Walk Through a Grid
Medium

You are given an m x n binary matrix grid and an integer health.

You start on the upper-left corner (0, 0) and would like to get to the lower-right corner (m - 1, n - 1).

You can move up, down, left, or right from one cell to another adjacent cell as long as your health remains positive.

Cells (i, j) with grid[i][j] = 1 are considered unsafe and reduce your health by 1.

Return true if you can reach the final cell with a health value of 1 or more, and false otherwise.


Example 1:

Input: grid = [[0,1,0,0,0],[0,1,0,1,0],[0,0,0,1,0]], health = 1
Output: true
Explanation:
The final cell can be reached safely by walking along the gray cells below.

Example 2:

Input: grid = [[0,1,1,0,0,0],[1,0,1,0,0,0],[0,1,1,1,0,1],[0,0,1,0,1,0]], health = 3
Output: false
Explanation:
A minimum of 4 health points is needed to reach the final cell safely.

Example 3:

Input: grid = [[1,1,1],[1,0,1],[1,1,1]], health = 5
Output: true
Explanation:
The final cell can be reached safely by walking along the gray cells below.
Any path that does not go through the cell (1, 1) is unsafe since your health will drop to 0 when reaching the final cell.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 50
2 <= m * n
1 <= health <= m + n
grid[i][j] is either 0 or 1

"""

# V0
# IDEA : 0-1 BFS — MINIMISE THE DAMAGE TAKEN, THEN COMPARE WITH health
#
#   the question "can I arrive with health >= 1" is really "what is the least
#   damage any path takes", so it is a shortest-path problem where stepping
#   onto a 1 costs 1 and onto a 0 costs 0.
#
#   with only two edge weights, a deque replaces the priority queue : a
#   0-cost move goes to the FRONT and a 1-cost move to the back, which keeps
#   the deque sorted by distance.
#
#   the starting cell's own damage counts too, so it seeds the search with
#   grid[0][0].
#
# time = O(m * n), space = O(m * n)
from collections import deque


class Solution(object):
    def findSafeWalk(self, grid, health):
        m, n = len(grid), len(grid[0])
        INF = float('inf')
        dist = [[INF] * n for _ in range(m)]
        dist[0][0] = grid[0][0]

        dq = deque([(0, 0)])
        while dq:
            i, j = dq.popleft()
            d = dist[i][j]
            for di, dj in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                x, y = i + di, j + dj
                if 0 <= x < m and 0 <= y < n:
                    nd = d + grid[x][y]
                    if nd < dist[x][y]:
                        dist[x][y] = nd
                        if grid[x][y]:
                            dq.append((x, y))
                        else:
                            dq.appendleft((x, y))

        return dist[m - 1][n - 1] < health
