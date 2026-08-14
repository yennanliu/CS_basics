"""

2596. Check Knight Tour Configuration
Medium

There is a knight on an n x n chessboard. In a valid configuration, the knight starts at the top-left cell of the board and visits every cell on the board exactly once.

You are given an n x n integer matrix grid consisting of distinct integers from the range [0, n * n - 1] where grid[row][col] indicates that the cell (row, col) is the grid[row][col]th cell that the knight visited. The moves are 0-indexed.

Return true if grid represents a valid configuration of the knight's movements or false otherwise.

Note that a valid knight move consists of moving two squares vertically and one square horizontally, or two squares horizontally and one square vertically.


Example 1:

Input: grid = [[0,11,16,5,20],[17,4,19,10,15],[12,1,8,21,6],[3,18,23,14,9],[24,13,2,7,22]]
Output: true
Explanation: The above diagram represents the grid. It can be shown that it is a valid configuration.

Example 2:

Input: grid = [[0,3,6],[5,8,1],[2,7,4]]
Output: false
Explanation: The above diagram represents the grid. The 8th move of the knight is not valid considering its position after the 7th move.


Constraints:

n == grid.length == grid[i].length
3 <= n <= 7
0 <= grid[row][col] < n * n
All integers in grid are unique.

"""

# V0
# IDEA : INVERT THE GRID INTO A VISIT ORDER, THEN CHECK EACH STEP
#
#   grid maps cell -> visit time; we want the opposite. Build pos[t] = (r, c),
#   the cell visited at step t, then walk t = 1..n*n-1 and verify that every
#   consecutive pair differs by a legal knight jump: {|dr|, |dc|} == {1, 2}.
#
#   NOTE : the tour must START at the top-left, so grid[0][0] != 0 is an
#          immediate false -- the per-step check alone would not catch a tour
#          that is otherwise legal but begins elsewhere.
#   NOTE : values are guaranteed distinct and inside [0, n*n), so pos is fully
#          filled and no cell is visited twice.
#
# time = O(n^2), space = O(n^2)
class Solution(object):
    def checkValidGrid(self, grid):
        if grid[0][0] != 0:
            return False
        n = len(grid)
        pos = [None] * (n * n)
        for r in range(n):
            for c in range(n):
                pos[grid[r][c]] = (r, c)
        for t in range(1, n * n):
            r1, c1 = pos[t - 1]
            r2, c2 = pos[t]
            dr, dc = abs(r1 - r2), abs(c1 - c2)
            if sorted([dr, dc]) != [1, 2]:
                return False
        return True
