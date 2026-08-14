"""

2664. The Knight's Tour
Medium

Given two positive integers m and n which are the height and width of a 0-indexed 2D-array board, a pair of positive integers (r, c) which is the starting position of the knight on the board.

Your task is to find an order of movements for the knight, in a manner that every cell of the board gets visited exactly once (the starting cell is considered visited and you shouldn't visit it again).

Return the array board in which the cells' values show the order of visiting the cell starting from 0 (the initial place of the knight).

Note that a knight can move from cell (r1, c1) to cell (r2, c2) if 0 <= r2 <= m - 1 and 0 <= c2 <= n - 1 and min(abs(r1 - r2), abs(c1 - c2)) = 1 and max(abs(r1 - r2), abs(c1 - c2)) = 2.


Example 1:

Input: m = 1, n = 1, r = 0, c = 0
Output: [[0]]
Explanation: There is only 1 cell and the knight is initially on it so there is only a 0 inside the 1x1 grid.

Example 2:

Input: m = 3, n = 4, r = 0, c = 0
Output: [[0,3,6,9],[11,8,1,4],[2,5,10,7]]
Explanation: By the following order of movements we can visit the entire board.
(0,0)->(1,2)->(2,0)->(0,1)->(1,3)->(2,1)->(0,2)->(2,3)->(1,1)->(0,3)->(2,2)->(1,0)


Constraints:

1 <= m, n <= 5
0 <= r <= m - 1
0 <= c <= n - 1
The inputs will be generated such that there exists at least one possible order of movements with the given condition

"""

# V0
# IDEA : BACKTRACKING (DFS) OVER THE 8 KNIGHT MOVES
#
#   the board is at most 5 x 5 = 25 cells, so a plain "try every move, undo
#   on failure" DFS is affordable -- the branching factor of a knight is at
#   most 8, but the "visit each cell exactly once" rule prunes almost all
#   of the tree immediately.
#
#   g[i][j] holds the step number at which the knight lands on (i, j), or
#   -1 when the cell is still unvisited. That single array doubles as the
#   answer AND as the visited marker -- no separate seen set needed.
#
#   dfs(i, j):
#     - if g[i][j] == m*n - 1 we just placed the LAST step number, so every
#       cell has been filled -> a full tour exists, latch `done` and unwind
#       WITHOUT undoing anything.
#     - otherwise try each of the 8 jumps to an in-bounds, unvisited (x, y);
#       stamp g[x][y] = g[i][j] + 1, recurse, and if `done` propagate up.
#       else reset g[x][y] = -1 and try the next direction.
#
#   NOTE : the `done` flag is what freezes the board. Without it the
#          unwinding would erase the very tour we just found.
#
#   NOTE : the termination test is on the STEP NUMBER (m*n - 1), not on a
#          visited counter -- they are the same thing here because step
#          numbers are assigned consecutively.
#
#   NOTE : m*n == 1 is handled for free: g[r][c] == 0 == m*n - 1 on entry,
#          so dfs returns immediately with [[0]].
#
#   the problem guarantees a tour exists, so `done` always ends up True.
#
# time = O(8^(m*n)) worst case (tiny in practice), space = O(m*n)
class Solution(object):
    def tourOfKnight(self, m, n, r, c):
        dirs = [(-2, -1), (-1, 2), (2, 1), (1, -2),
                (-2, 1), (1, 2), (2, -1), (-1, -2)]
        g = [[-1] * n for _ in range(m)]
        g[r][c] = 0
        last = m * n - 1
        self.done = False

        def dfs(i, j):
            if g[i][j] == last:
                self.done = True
                return
            for a, b in dirs:
                x, y = i + a, j + b
                if 0 <= x < m and 0 <= y < n and g[x][y] == -1:
                    g[x][y] = g[i][j] + 1
                    dfs(x, y)
                    if self.done:
                        return
                    g[x][y] = -1

        dfs(r, c)
        return g
