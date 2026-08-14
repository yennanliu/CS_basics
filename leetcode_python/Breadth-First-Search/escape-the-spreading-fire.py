"""

2258. Escape the Spreading Fire
Hard

You are given a 0-indexed 2D integer array grid of size m x n which represents a field. Each cell has one of three values:

0 represents grass,
1 represents fire,
2 represents a wall that you and fire cannot pass through.

You are situated in the top-left cell, (0, 0), and you want to travel to the safehouse at the bottom-right cell, (m - 1, n - 1). Every minute, you may move to an adjacent grass cell. After your move, every fire cell will spread to all adjacent cells that are not walls.

Return the maximum number of minutes that you can stay in your initial position before moving while still safely reaching the safehouse. If this is impossible, return -1. If you can always reach the safehouse regardless of the minutes stayed, return 10^9.

Note that even if the fire spreads to the safehouse immediately after you have reached it, it will be counted as safely reaching the safehouse.

A cell is adjacent to another cell if the former is directly north, east, south, or west of the latter (i.e., their sides are touching).


Example 1:

Input: grid = [[0,2,0,0,0,0,0],[0,0,0,2,2,1,0],[0,2,0,0,1,2,0],[0,0,2,2,2,0,2],[0,0,0,0,0,0,0]]
Output: 3
Explanation: The figure above shows the scenario where you stay in the initial position for 3 minutes.
You will still be able to safely reach the safehouse.
Staying for more than 3 minutes will not allow you to safely reach the safehouse.

Example 2:

Input: grid = [[0,0,0,0],[0,1,2,0],[0,2,0,0]]
Output: -1
Explanation: The figure above shows the scenario where you immediately move towards the safehouse.
Fire will spread to any cell you move towards and it is impossible to safely reach the safehouse.
Thus, -1 is returned.

Example 3:

Input: grid = [[0,0,0],[2,2,0],[1,2,0]]
Output: 1000000000
Explanation: The figure above shows the initial grid.
Notice that the fire is contained by walls and you will always be able to safely reach the safehouse.
Thus, 10^9 is returned.


Constraints:

m == grid.length
n == grid[i].length
2 <= m, n <= 300
4 <= m * n <= 2 * 10^4
grid[i][j] is either 0, 1, or 2.
grid[0][0] == grid[m - 1][n - 1] == 0

"""

# V0
# IDEA : BFS THE FIRE ONCE, THEN BINARY SEARCH THE WAITING TIME
#
#   step 1 : multi-source BFS from every fire cell gives fire_time[r][c], the
#            minute the fire arrives (INF where it never does).
#
#   step 2 : "can I still escape after waiting t minutes?" is MONOTONE in t —
#            waiting longer never helps — so binary search t.
#
#   the feasibility BFS starts at (0, 0) at minute t and may enter a cell only
#   while it is strictly ahead of the flames :
#       arrival < fire_time[cell]
#   with ONE exception at the safehouse, where arrival <= fire_time is fine
#   (the statement allows the fire to reach it the moment you do).
#
#   if waiting m * n minutes still works, the fire can never reach the path,
#   so the answer is 10^9.
#
# time = O(m * n * log(m * n)), space = O(m * n)
from collections import deque


class Solution(object):
    def maximumMinutes(self, grid):
        m, n = len(grid), len(grid[0])
        INF = float('inf')
        DIRS = ((1, 0), (-1, 0), (0, 1), (0, -1))

        # how long until the fire reaches each cell
        fire_time = [[INF] * n for _ in range(m)]
        q = deque()
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 1:
                    fire_time[i][j] = 0
                    q.append((i, j))
        while q:
            r, c = q.popleft()
            for dr, dc in DIRS:
                x, y = r + dr, c + dc
                if (0 <= x < m and 0 <= y < n and grid[x][y] == 0
                        and fire_time[x][y] == INF):
                    fire_time[x][y] = fire_time[r][c] + 1
                    q.append((x, y))

        def can_escape(wait):
            if fire_time[0][0] <= wait:
                return False
            seen = [[False] * n for _ in range(m)]
            seen[0][0] = True
            q = deque([(0, 0, wait)])
            while q:
                r, c, t = q.popleft()
                for dr, dc in DIRS:
                    x, y = r + dr, c + dc
                    if not (0 <= x < m and 0 <= y < n) or grid[x][y] != 0 or seen[x][y]:
                        continue
                    if x == m - 1 and y == n - 1:
                        if t + 1 <= fire_time[x][y]:
                            return True
                        continue
                    if t + 1 < fire_time[x][y]:
                        seen[x][y] = True
                        q.append((x, y, t + 1))
            return False

        limit = m * n
        if can_escape(limit):
            return 10 ** 9
        if not can_escape(0):
            return -1

        lo, hi = 0, limit
        while lo < hi:
            mid = (lo + hi + 1) // 2
            if can_escape(mid):
                lo = mid
            else:
                hi = mid - 1
        return lo
