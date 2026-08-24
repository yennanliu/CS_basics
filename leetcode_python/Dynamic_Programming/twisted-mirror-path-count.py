"""

3665. Twisted Mirror Path Count
Medium

Given an m x n binary grid grid where:

grid[i][j] == 0 represents an empty cell, and
grid[i][j] == 1 represents a mirror.

A robot starts at the top-left corner of the grid (0, 0) and wants to reach
the bottom-right corner (m - 1, n - 1). It can move only right or down. If
the robot attempts to move into a mirror cell, it is reflected before
entering that cell:

If it tries to move right into a mirror, it is turned down and moved into
the cell directly below the mirror.
If it tries to move down into a mirror, it is turned right and moved into
the cell directly to the right of the mirror.

If this reflection would cause the robot to move outside the grid
boundaries, the path is considered invalid and should not be counted.

Return the number of unique valid paths from (0, 0) to (m - 1, n - 1).

Since the answer may be very large, return it modulo 10^9 + 7.

Note: If a reflection moves the robot into a mirror cell, the robot is
immediately reflected again based on the direction it used to enter that
mirror: if it entered while moving right, it will be turned down; if it
entered while moving down, it will be turned right. This process will
continue until either the last cell is reached, the robot moves out of
bounds or the robot moves to a non-mirror cell.


Example 1:

Input: grid = [[0,1,0],[0,0,1],[1,0,0]]
Output: 5
Explanation:

1  (0, 0) -> (0, 1) [M] -> (1, 1) -> (1, 2) [M] -> (2, 2)
2  (0, 0) -> (0, 1) [M] -> (1, 1) -> (2, 1) -> (2, 2)
3  (0, 0) -> (1, 0) -> (1, 1) -> (1, 2) [M] -> (2, 2)
4  (0, 0) -> (1, 0) -> (1, 1) -> (2, 1) -> (2, 2)
5  (0, 0) -> (1, 0) -> (2, 0) [M] -> (2, 1) -> (2, 2)

[M] indicates the robot attempted to enter a mirror cell and instead
reflected.

Example 2:

Input: grid = [[0,0],[0,0]]
Output: 2
Explanation:

1  (0, 0) -> (0, 1) -> (1, 1)
2  (0, 0) -> (1, 0) -> (1, 1)

Example 3:

Input: grid = [[0,1,1],[1,1,0]]
Output: 1
Explanation:

1  (0, 0) -> (0, 1) [M] -> (1, 1) [M] -> (1, 2)

(0, 0) -> (1, 0) [M] -> (1, 1) [M] -> (2, 1) goes out of bounds, so it is
invalid.


Constraints:

m == grid.length
n == grid[i].length
2 <= m, n <= 500
grid[i][j] is either 0 or 1.
grid[0][0] == grid[m - 1][n - 1] == 0

"""

# V0
# IDEA : DP ON *FLOW THROUGH A CELL*, SPLIT BY THE DIRECTION OF TRAVEL
#
#   the reflection chains make "number of paths that stop at (i,j)" awkward,
#   because a mirror is never a stopping place. the fix is to stop tracking
#   where a path RESTS and instead track how much path-flow CROSSES each
#   cell, tagged by the direction the robot is travelling. write
#     in_right[i][j]  = paths entering (i,j) from the left, moving right
#     in_down[i][j]   = paths entering (i,j) from above, moving down
#
#   then one local rule per cell decides everything:
#     empty cell  -- the robot lands, and freely picks its next move, so
#                    both outgoing streams equal the whole incoming flow:
#                    out_right = out_down = in_right + in_down
#     mirror cell -- no choice, only a turn: something entering rightward
#                    leaves downward and vice versa, so
#                    out_right = in_down and out_down = in_right
#   the mirror case is a pure SWAP, which is exactly why arbitrarily long
#   reflection chains need no special handling: each mirror in the chain is
#   just another swap on the way through.
#
#   invalid (out-of-bounds) paths die for free: flow leaving the last row
#   downward or the last column rightward is simply never read again.
#
#   sweeping row by row we only need one row of the two streams, kept as
#   dp[c] = [out_right, out_down] of column c-1. seeding dp[1] = [1, 1]
#   injects the single starting path into (0,0) from "above".
#
"""

DP def
    the reflection chains make "paths that STOP at (i,j)" awkward - a mirror is
    never a resting place. so track the FLOW CROSSING each cell instead,
    tagged by direction of travel:

    in_right[i][j]: paths entering (i,j) from the LEFT,  moving right

    in_down[i][j] : paths entering (i,j) from ABOVE, moving down

DP eq

     EMPTY cell - the robot lands and freely picks its next move, so both
                  outgoing streams equal the whole incoming flow:

        out_right = out_down = in_right + in_down

     MIRROR cell - no choice, only a turn:

        out_right = in_down       and      out_down = in_right


    -> e.g. the mirror case is a pure SWAP, which is exactly why arbitrarily
              long reflection chains need no special handling - each mirror
              is just another swap on the way through

     invalid paths die for free: flow leaving the last row downward or the
     last column rightward is simply never read again

     init: inject the single starting path into (0,0) "from above"
     ans = the flow arriving at (m-1, n-1), mod 10^9 + 7

"""
# time = O(m * n), space = O(n)
class Solution(object):
    def uniquePaths(self, grid):
        MOD = 10 ** 9 + 7
        m, n = len(grid), len(grid[0])

        dp = [[0, 0] for _ in range(n + 1)]
        dp[1] = [1, 1]
        for r in range(m):
            row = grid[r]
            for c in range(n):
                in_right = dp[c][0]
                in_down = dp[c + 1][1]
                if row[c]:
                    dp[c + 1] = [in_down, in_right]
                else:
                    tot = (in_right + in_down) % MOD
                    dp[c + 1] = [tot, tot]
        return dp[n][0]
