"""

1210. Minimum Moves to Reach Target with Rotations
Hard

In an n*n grid, there is a snake that spans 2 cells and starts moving from the top left corner
at (0, 0) and (0, 1). The grid has empty cells represented by zeros and blocked cells represented
by ones. The snake wants to reach the lower right corner at (n-1, n-2) and (n-1, n-1).

In one move the snake can:

Move one cell to the right if there are no blocked cells there.
This move keeps the horizontal/vertical position of the snake as it is.

Move down one cell if there are no blocked cells there.
This move keeps the horizontal/vertical position of the snake as it is.

Rotate clockwise if it's in a horizontal position and the two cells under it are both empty.
In that case the snake moves from (r, c) and (r, c+1) to (r, c) and (r+1, c).

Rotate counterclockwise if it's in a vertical position and the two cells to its right are both empty.
In that case the snake moves from (r, c) and (r+1, c) to (r, c) and (r, c+1).

Return the minimum number of moves to reach the target.

If there is no way to reach the target, return -1.


Example 1:

Input: grid = [[0,0,0,0,0,1],
               [1,1,0,0,1,0],
               [0,0,0,0,1,1],
               [0,0,1,0,1,0],
               [0,1,1,0,0,0],
               [0,1,1,0,0,0]]
Output: 11
Explanation:
One possible solution is [right, right, rotate clockwise, right, down, down, down, down,
rotate counterclockwise, right, down].

Example 2:

Input: grid = [[0,0,1,1,1,1],
               [0,0,0,0,1,1],
               [1,1,0,0,0,1],
               [1,1,1,0,0,1],
               [1,1,1,0,0,1],
               [1,1,1,0,0,0]]
Output: 9


Constraints:

2 <= n <= 100
0 <= grid[i][j] <= 1
It is guaranteed that the snake starts at empty cells.

"""

# V0
# IDEA : BFS on state = (row, col, orientation)
#
#   the snake is fully described by its "anchor" cell (r, c) plus a flag :
#     d = 0 -> horizontal, body = (r, c) and (r, c + 1)
#     d = 1 -> vertical  , body = (r, c) and (r + 1, c)
#
#   every move costs 1, so plain BFS from (0, 0, 0) gives the minimum.
#   transitions (all require the newly occupied cells to be inside + empty):
#     horizontal: right -> (r, c+1, 0)   needs grid[r][c+2]
#                 down  -> (r+1, c, 0)   needs grid[r+1][c], grid[r+1][c+1]
#                 CW    -> (r, c, 1)     needs grid[r+1][c], grid[r+1][c+1]
#     vertical  : right -> (r, c+1, 1)   needs grid[r][c+1], grid[r+1][c+1]
#                 down  -> (r+1, c, 1)   needs grid[r+2][c]
#                 CCW   -> (r, c, 0)     needs grid[r][c+1], grid[r+1][c+1]
#
#   NOTE : a rotation and a translation can share the same required-empty
#          cells but land on different states, so both must be pushed.
#   goal state = (n-1, n-2, 0).
#
# time = O(n^2), space = O(n^2)
from collections import deque
class Solution(object):
    def minimumMoves(self, grid):
        n = len(grid)

        def free(r, c):
            return 0 <= r < n and 0 <= c < n and grid[r][c] == 0

        start = (0, 0, 0)
        target = (n - 1, n - 2, 0)
        q = deque([start])
        seen = set([start])
        steps = 0

        while q:
            for _ in range(len(q)):
                r, c, d = q.popleft()
                if (r, c, d) == target:
                    return steps

                nxts = []
                if d == 0:
                    if free(r, c + 2):
                        nxts.append((r, c + 1, 0))
                    if free(r + 1, c) and free(r + 1, c + 1):
                        nxts.append((r + 1, c, 0))
                        nxts.append((r, c, 1))
                else:
                    if free(r + 2, c):
                        nxts.append((r + 1, c, 1))
                    if free(r, c + 1) and free(r + 1, c + 1):
                        nxts.append((r, c + 1, 1))
                        nxts.append((r, c, 0))

                for st in nxts:
                    if st not in seen:
                        seen.add(st)
                        q.append(st)
            steps += 1

        return -1
