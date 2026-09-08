"""

2017. Grid Game
Medium

You are given a 0-indexed 2D array grid of size 2 x n, where grid[r][c]
represents the number of points at position (r, c) on the matrix.

Two robots are playing a game on this matrix.

Both robots initially start at (0, 0) and want to reach (1, n-1). Each robot
may only move to the right ((r, c) -> (r, c + 1)) or down ((r, c) -> (r + 1, c)).

At the start of the game, the first robot moves from (0, 0) to (1, n-1),
collecting all the points from the cells on its path. For all cells (r, c)
traversed on the path, grid[r][c] is set to 0. Then, the second robot moves
from (0, 0) to (1, n-1), collecting the points on its path. Note that their
paths may intersect with one another.

The first robot wants to minimize the number of points collected by the second
robot. In contrast, the second robot wants to maximize the number of points
collected. If both robots play optimally, return the number of points collected
by the second robot.

Example 1:

Input: grid = [[2,5,4],[1,5,1]]

Output: 4

Explanation:

the first robot turns down at column 1, so it takes 2 + 5 + 5 + 1 and zeroes
those cells. what is left for the second robot is 4 on the top row and 1 on the
bottom row, and one path cannot pick up both -> it takes 4.

Example 2:

Input: grid = [[3,3,1],[8,5,2]]

Output: 4

Explanation:

the first robot turns down at column 0 (3 + 8 + 5 + 2), leaving 3 + 1 on the
top row for the second robot.

Example 3:

Input: grid = [[1,3,1,15],[1,3,3,1]]

Output: 7

Explanation:

the first robot turns down at the last column, leaving 1 + 3 + 3 on the bottom
row for the second robot.

Constraints:

grid.length == 2
n == grid[r].length
1 <= n <= 5 * 10^4
1 <= grid[r][c] <= 10^5

"""

# V0
# IDEA: PREFIX (BOTTOM) + SUFFIX (TOP) SUM OVER THE TURNING COLUMN
#
#   a path in a 2 x n grid goes right along row 0, drops ONCE, then goes right
#   along row 1 -- so a path IS just its turning column i.
#
#   robot 1 turning at i zeroes row0[0..i] and row1[i..n-1]. that leaves robot 2
#   exactly two untouched blocks, and they sit on OPPOSITE sides of i :
#
#     top    row0[i+1 ... n-1]     (right of the drop)
#     bottom row1[0   ... i-1]     (left  of the drop)
#
#   robot 2 also drops once, so it can only ever reach ONE of the two blocks --
#   and since every point is >= 1, it takes that block whole (drop at n-1 for
#   the top, at 0 for the bottom). so its best is max(top, bottom), and the
#   answer is the i that minimises that max.
#
#   e.g. grid = [[2,5,4],[1,5,1]], i = 1 -> top = 4, bottom = 1 -> 4  (the min)
#
# time = O(n), space = O(1)
class Solution(object):
    def gridGame(self, grid):
        """
        :type grid: List[List[int]]
        :rtype: int
        """
        # edge
        if not grid or not grid[0]:
            return 0

        top = sum(grid[0])       # will shrink to row0[i+1 ... n-1]
        bottom = 0               # will grow  to row1[0   ... i-1]
        res = float('inf')

        for i in range(len(grid[0])):
            # NOTE !!! column i is on robot 1's own path, so it belongs to
            #          NEITHER block : drop it from top BEFORE the compare,
            #          and add it to bottom only AFTER
            top -= grid[0][i]
            res = min(res, max(top, bottom))
            bottom += grid[1][i]

        return res
