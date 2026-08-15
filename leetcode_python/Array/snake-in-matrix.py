"""

3248. Snake in Matrix
Medium

There is a snake in an n x n matrix grid and can move in four possible directions. Each cell in the grid is identified by the position: grid[i][j] = (i * n) + j.

The snake starts at cell 0 and follows a sequence of commands.

You are given an integer n representing the size of the grid and an array of strings commands where each command[i] is either "UP", "RIGHT", "DOWN", and "LEFT". It's guaranteed that the snake will remain within the grid boundaries throughout its movement.

Return the position of the final cell where the snake ends up after executing commands.


Example 1:

Input: n = 2, commands = ["RIGHT","DOWN"]
Output: 3
Explanation:

Example 2:

Input: n = 3, commands = ["DOWN","RIGHT","UP"]
Output: 1
Explanation:


Constraints:

2 <= n <= 10
1 <= commands.length <= 100
commands consists only of "UP", "RIGHT", "DOWN", and "LEFT".
The input is generated such that the snake will not move outside of the boundaries.

"""

# V0
# IDEA : TRACK (row, col) AND ENCODE AT THE END
#
#   the cell id is i * n + j, so keeping the coordinates and converting once
#   at the end avoids any modular arithmetic mid-walk.
#
#   the statement guarantees the snake never leaves the board, so no bounds
#   checks are needed.
#
# time = O(len(commands)), space = O(1)
class Solution(object):
    def finalPositionOfSnake(self, n, commands):
        moves = {"UP": (-1, 0), "DOWN": (1, 0), "LEFT": (0, -1), "RIGHT": (0, 1)}
        i = j = 0
        for c in commands:
            di, dj = moves[c]
            i += di
            j += dj
        return i * n + j
