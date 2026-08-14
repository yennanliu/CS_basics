"""

1810. Minimum Path Cost in a Hidden Grid
Medium

This is an interactive problem.

There is a robot in a hidden grid, and you are trying to get it from its starting cell to the target cell in this grid. The grid is of size m x n, and each cell in the grid is either empty or blocked. It is guaranteed that the starting cell and the target cell are different, and neither of them is blocked.

Each cell has a cost that you need to pay each time you move to the cell. The starting cell's cost is not applied before the robot moves.

You want to find the minimum total cost to move the robot to the target cell. However, you do not know the grid's dimensions, the starting cell, nor the target cell. You are only allowed to ask queries to the GridMaster object.

The GridMaster class has the following functions:

boolean canMove(char direction) Returns true if the robot can move in that direction. Otherwise, it returns false.
int move(char direction) Moves the robot in that direction and returns the cost of moving to that cell. If this move would move the robot to a blocked cell or off the grid, the move will be ignored, the robot will remain in the same position, and the function will return -1.
boolean isTarget() Returns true if the robot is currently on the target cell. Otherwise, it returns false.

Note that direction in the above functions should be a character from {'U','D','L','R'}, representing the directions up, down, left, and right, respectively.

Return the minimum total cost to get the robot from its initial starting cell to the target cell. If there is no valid path between the cells, return -1.

Custom testing:

The test input is read as a 2D matrix grid of size m x n and four integers r1, c1, r2, and c2 where:

grid[i][j] == 0 indicates that the cell (i, j) is blocked.
grid[i][j] >= 1 indicates that the cell (i, j) is empty and grid[i][j] is the cost to move to that cell.
(r1, c1) is the starting cell of the robot.
(r2, c2) is the target cell of the robot.

Remember that you will not have this information in your code.


Example 1:

Input: grid = [[2,3],[1,1]], r1 = 0, c1 = 1, r2 = 1, c2 = 0
Output: 2
Explanation: One possible interaction is described below:
The robot is initially standing on cell (0, 1), denoted by the 3.
- master.canMove('U') returns false.
- master.canMove('D') returns true.
- master.canMove('L') returns true.
- master.canMove('R') returns false.
- master.move('L') moves the robot to the cell (0, 0) and returns 2.
- master.isTarget() returns false.
- master.canMove('U') returns false.
- master.canMove('D') returns true.
- master.canMove('L') returns false.
- master.canMove('R') returns true.
- master.move('D') moves the robot to the cell (1, 0) and returns 1.
- master.isTarget() returns true.
- master.move('L') doesn't move the robot and returns -1.
- master.move('R') moves the robot to the cell (1, 1) and returns 1.
We now know that the target is the cell (1, 0), and the minimum total cost to reach it is 2.

Example 2:

Input: grid = [[0,3,1],[3,4,2],[1,2,0]], r1 = 2, c1 = 0, r2 = 0, c2 = 2
Output: 9
Explanation: The minimum cost path is (2,0) -> (2,1) -> (1,1) -> (1,2) -> (0,2).

Example 3:

Input: grid = [[1,0],[0,1]], r1 = 0, c1 = 0, r2 = 1, c2 = 1
Output: -1
Explanation: There is no path from the robot to the target cell.


Constraints:

1 <= n, m <= 100
m == grid.length
n == grid[i].length
0 <= grid[i][j] <= 100

"""

# V0
# IDEA : DFS TO EXPLORE (map the hidden grid) + DIJKSTRA ON THE MAP
#
#   two independent phases:
#
#   1) DFS with backtracking. the grid is at most 100 x 100 and we do not know
#      where we start, so allocate a 200 x 200 board and put the robot at the
#      centre (100, 100). every time we step into an unvisited neighbour we
#      record its cost, recurse, then step BACK with the opposite direction so
#      the robot is restored -- this is what makes the walk a valid DFS.
#      while walking, isTarget() tells us the target coordinates.
#
#   2) now the costs are fully known, so it is a plain weighted shortest path:
#      Dijkstra from the start cell (entering a cell pays that cell's cost;
#      the start cell itself is free).
#
#   NOTE : the recursion depth can reach ~10^4, so raise the limit.
#   NOTE : -1 in `g` means "blocked or never reached" -> unusable.
#
# time = O(m*n log(m*n)), space = O(m*n)
import sys
import heapq

# """
# This is GridMaster's API interface.
# You should not implement it, or speculate about its implementation
# """
# class GridMaster(object):
#     def canMove(self, direction):
#         pass
#     def move(self, direction):
#         pass
#     def isTarget(self):
#         pass

class Solution(object):
    def findShortestPath(self, master):
        sys.setrecursionlimit(30000)

        N = 200
        SX = SY = 100
        DIRS = [(-1, 0, 'U'), (0, 1, 'R'), (1, 0, 'D'), (0, -1, 'L')]
        BACK = {'U': 'D', 'D': 'U', 'L': 'R', 'R': 'L'}

        g = [[-1] * N for _ in range(N)]
        g[SX][SY] = 0
        target = [-1, -1]

        def dfs(x, y):
            if master.isTarget():
                target[0], target[1] = x, y
            for dx, dy, d in DIRS:
                nx, ny = x + dx, y + dy
                if 0 <= nx < N and 0 <= ny < N and g[nx][ny] == -1:
                    if master.canMove(d):
                        g[nx][ny] = master.move(d)
                        dfs(nx, ny)
                        master.move(BACK[d])

        dfs(SX, SY)
        if target[0] == -1:
            return -1

        dist = [[-1] * N for _ in range(N)]
        pq = [(0, SX, SY)]
        while pq:
            w, x, y = heapq.heappop(pq)
            if dist[x][y] != -1:
                continue
            dist[x][y] = w
            if (x, y) == (target[0], target[1]):
                return w
            for dx, dy, _ in DIRS:
                nx, ny = x + dx, y + dy
                if 0 <= nx < N and 0 <= ny < N and g[nx][ny] != -1 and dist[nx][ny] == -1:
                    heapq.heappush(pq, (w + g[nx][ny], nx, ny))
        return -1
