"""

3552. Grid Teleportation Traversal
Medium

You are given a 2D character grid matrix of size m x n, represented as an array
of strings, where matrix[i][j] represents the cell at the intersection of the ith
row and jth column. Each cell is one of the following:

'.' representing an empty cell.
'#' representing an obstacle.
An uppercase letter ('A'-'Z') representing a teleportation portal.

You start at the top-left cell (0, 0), and your goal is to reach the bottom-right
cell (m - 1, n - 1). You can move from the current cell to any adjacent cell
(up, down, left, right) as long as the destination cell is within the grid bounds
and is not an obstacle.

If you enter a cell containing a portal letter and you haven't used that portal
letter before, you may instantly teleport to any other cell in the grid with the
same letter. This immediate teleportation does not count as a move, but each
portal letter can be used at most once during your journey.

Return the minimum number of moves required to reach the bottom-right cell. If it
is not possible, return -1.


Example 1:

Input: matrix = ["A..",".A.","..."]
Output: 2
Explanation:
Teleport using portal 'A' from (0, 0) to (1, 1).
Move down-right from (1, 1) to (2, 2) in 2 moves.

Example 2:

Input: matrix = [".#...",".#.#.",".#.#.","...#."]
Output: 13
Explanation:
There are no portals, so the answer is the length of the shortest obstacle-free
path from (0, 0) to (3, 4), which is 13 moves.


Constraints:

1 <= m == matrix.length <= 10^3
1 <= n == matrix[i].length <= 10^3
matrix[i][j] is either '#', '.', or an uppercase English letter.
matrix[0][0] is not an obstacle.

"""

# V0
# IDEA : 0-1 BFS — TELEPORTS ARE ZERO-COST EDGES
#
#   walking costs 1 and teleporting costs 0, so a deque-based BFS (push zero
#   cost moves to the front, unit cost moves to the back) keeps the frontier in
#   non-decreasing distance order without a heap.
#
#   "each portal letter can be used at most once" is handled by consuming the
#   whole letter group the first time it is touched: the first time we pop a
#   cell with letter c we release every other c-cell at the same distance, then
#   delete the group so it is never expanded again.  since 0-1 BFS pops cells in
#   distance order, that first touch is at the smallest possible distance and no
#   later use of the letter could beat it.
#
# time = O(m*n), space = O(m*n)
from collections import deque


class Solution(object):
    def minMoves(self, matrix):
        m = len(matrix)
        n = len(matrix[0])
        if matrix[m - 1][n - 1] == '#':
            return -1
        portals = {}
        for i in range(m):
            row = matrix[i]
            for j in range(n):
                c = row[j]
                if 'A' <= c <= 'Z':
                    portals.setdefault(c, []).append((i, j))

        INF = float('inf')
        dist = [[INF] * n for _ in range(m)]
        dist[0][0] = 0
        dq = deque([(0, 0)])
        while dq:
            i, j = dq.popleft()
            d = dist[i][j]
            if i == m - 1 and j == n - 1:
                return d
            c = matrix[i][j]
            if c in portals:
                for (x, y) in portals.pop(c):
                    if d < dist[x][y]:
                        dist[x][y] = d
                        dq.appendleft((x, y))
            for x, y in ((i - 1, j), (i + 1, j), (i, j - 1), (i, j + 1)):
                if 0 <= x < m and 0 <= y < n and matrix[x][y] != '#':
                    if d + 1 < dist[x][y]:
                        dist[x][y] = d + 1
                        dq.append((x, y))
        return -1
