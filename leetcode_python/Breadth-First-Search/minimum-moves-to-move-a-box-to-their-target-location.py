"""

1263. Minimum Moves to Move a Box to Their Target Location
Hard

A storekeeper is a game in which the player pushes boxes around in a warehouse
trying to get them to target locations.

The game is represented by an m x n grid of characters grid where each element
is a wall, floor, or box.

Your task is to move the box 'B' to the target position 'T' under the following rules:

The character 'S' represents the player. The player can move up, down, left, right
in grid if it is a floor (empty cell).
The character '.' represents the floor which means a free cell to walk.
The character '#' represents the wall which means an obstacle (impossible to walk there).
There is only one box 'B' and one target cell 'T' in the grid.
The box can be moved to an adjacent free cell by standing next to the box and then
moving in the direction of the box. This is a push.
The player cannot walk through the box.

Return the minimum number of pushes to move the box to the target.
If there is no way to reach the target, return -1.


Example 1:

Input: grid = [["#","#","#","#","#","#"],
               ["#","T","#","#","#","#"],
               ["#",".",".","B",".","#"],
               ["#",".","#","#",".","#"],
               ["#",".",".",".","S","#"],
               ["#","#","#","#","#","#"]]
Output: 3
Explanation: We return only the number of times the box is pushed.

Example 2:

Input: grid = [["#","#","#","#","#","#"],
               ["#","T","#","#","#","#"],
               ["#",".",".","B",".","#"],
               ["#","#","#","#",".","#"],
               ["#",".",".",".","S","#"],
               ["#","#","#","#","#","#"]]
Output: -1

Example 3:

Input: grid = [["#","#","#","#","#","#"],
               ["#","T",".",".","#","#"],
               ["#",".","#","B",".","#"],
               ["#",".",".",".",".","#"],
               ["#",".",".",".","S","#"],
               ["#","#","#","#","#","#"]]
Output: 5
Explanation: push the box down, left, left, up and up.


Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 20
grid contains only characters '.', '#', 'S', 'T', or 'B'.
There is only one character 'S', 'B', and 'T' in the grid.

"""

# V0
# IDEA : 0-1 BFS on state (box position, player position)
#
#   - a player step that does NOT touch the box costs 0
#   - a player step INTO the box (a push) costs 1
#   -> use a deque : cost 0 goes to the front, cost 1 goes to the back,
#      so states still leave the deque in non decreasing cost order
#
# time = O((m * n)^2)
# space = O((m * n)^2)
from collections import deque
class Solution(object):
    def minPushBox(self, grid):
        m, n = len(grid), len(grid[0])
        box = player = target = None
        for i in range(m):
            for j in range(n):
                if grid[i][j] == 'B':
                    box = (i, j)
                elif grid[i][j] == 'S':
                    player = (i, j)
                elif grid[i][j] == 'T':
                    target = (i, j)

        def walkable(i, j):
            return 0 <= i < m and 0 <= j < n and grid[i][j] != '#'

        start = (box[0], box[1], player[0], player[1])
        dist = {start: 0}
        dq = deque([start])
        while dq:
            state = dq.popleft()
            bx, by, px, py = state
            d = dist[state]
            if (bx, by) == target:
                return d
            for dx, dy in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                nx, ny = px + dx, py + dy
                if not walkable(nx, ny):
                    continue
                if (nx, ny) == (bx, by):
                    # push : box slides one more cell in the same direction
                    nbx, nby = bx + dx, by + dy
                    if not walkable(nbx, nby):
                        continue
                    nxt, w = (nbx, nby, nx, ny), 1
                else:
                    nxt, w = (bx, by, nx, ny), 0
                if nxt not in dist or dist[nxt] > d + w:
                    dist[nxt] = d + w
                    if w:
                        dq.append(nxt)
                    else:
                        dq.appendleft(nxt)
        return -1
