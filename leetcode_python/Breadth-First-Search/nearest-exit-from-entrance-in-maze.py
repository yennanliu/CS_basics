"""

1926. Nearest Exit from Entrance in Maze
Medium

You are given an m x n matrix maze (0-indexed) with empty cells (represented as '.') and walls (represented as '+'). You are also given the entrance of the maze, where entrance = [entrancerow, entrancecol] denotes the row and column of the cell you are initially standing at.

In one step, you can move one cell up, down, left, or right. You cannot step into a cell with a wall, and you cannot step outside the maze. Your goal is to find the nearest exit from the entrance. An exit is defined as an empty cell that is at the border of the maze. The entrance does not count as an exit.

Return the number of steps in the shortest path from the entrance to the nearest exit, or -1 if no such path exists.


Example 1:

Input: maze = [["+","+",".","+"],[".",".",".","+"],["+","+","+","."]], entrance = [1,2]
Output: 1
Explanation: There are 3 exits in this maze at [1,0], [0,2], and [2,3].
Initially, you are at the entrance cell [1,2].
- You can reach [1,0] by moving 2 steps left.
- You can reach [0,2] by moving 1 step up.
It is impossible to reach [2,3] from the entrance.
Thus, the nearest exit is [0,2], which is 1 step away.

Example 2:

Input: maze = [["+","+","+"],[".",".","."],["+","+","+"]], entrance = [1,0]
Output: 2
Explanation: There is 1 exit in this maze at [1,2].
[1,0] does not count as an exit since it is the entrance cell.
Initially, you are at the entrance cell [1,0].
- You can reach [1,2] by moving 2 steps right.
Thus, the nearest exit is [1,2], which is 2 steps away.

Example 3:

Input: maze = [[".","+"]], entrance = [0,0]
Output: -1
Explanation: There are no exits in this maze.


Constraints:

maze.length == m
maze[i].length == n
1 <= m, n <= 100
maze[i][j] is either '.' or '+'.
entrance.length == 2
0 <= entrancerow < m
0 <= entrancecol < n
entrance will always be an empty cell.

"""

# V0
# IDEA : BFS LEVEL BY LEVEL (unweighted grid -> first border cell reached wins)
#
#   start at the entrance, expand one ring at a time; the first empty cell we
#   step onto that lies on the border is the nearest exit.
#
#   NOTE : the entrance is NOT an exit, so we test the border condition only on
#          cells we MOVE INTO, never on the start cell.
#   NOTE : mark visited by turning the cell into a wall ('+') -> O(1) space
#          on top of the queue, and no cell is ever enqueued twice.
#
# time = O(m * n), space = O(m * n)
from collections import deque
class Solution(object):
    def nearestExit(self, maze, entrance):
        m, n = len(maze), len(maze[0])
        sr, sc = entrance
        maze[sr][sc] = '+'
        q = deque([(sr, sc)])
        steps = 0
        while q:
            steps += 1
            for _ in range(len(q)):
                i, j = q.popleft()
                for di, dj in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                    x, y = i + di, j + dj
                    if 0 <= x < m and 0 <= y < n and maze[x][y] == '.':
                        if x == 0 or x == m - 1 or y == 0 or y == n - 1:
                            return steps
                        maze[x][y] = '+'
                        q.append((x, y))
        return -1
