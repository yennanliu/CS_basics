"""

3651. Minimum Cost Path with Teleportations
Hard

You are given a m x n 2D integer array grid and an integer k. You start at
the top-left cell (0, 0) and your goal is to reach the bottom-right cell
(m - 1, n - 1).

There are two types of moves available:

Normal move: You can move right or down from your current cell (i, j), i.e.
you can move to (i, j + 1) (right) or (i + 1, j) (down). The cost is the
value of the destination cell.
Teleportation: You can teleport from any cell (i, j), to any cell (x, y)
such that grid[x][y] <= grid[i][j]; the cost of this move is 0. You may
teleport at most k times.

Return the minimum total cost to reach cell (m - 1, n - 1) from (0, 0).


Example 1:

Input: grid = [[1,3,3],[2,5,4],[4,3,5]], k = 2
Output: 7
Explanation:
Initially we are at (0, 0) and cost is 0.

Current Position   Move                 New Position   Total Cost
(0, 0)             Move Down            (1, 0)         0 + 2 = 2
(1, 0)             Move Right           (1, 1)         2 + 5 = 7
(1, 1)             Teleport to (2, 2)   (2, 2)         7 + 0 = 7

The minimum cost to reach bottom-right cell is 7.

Example 2:

Input: grid = [[1,2],[2,3],[3,4]], k = 1
Output: 9
Explanation:
Initially we are at (0, 0) and cost is 0.

Current Position   Move          New Position   Total Cost
(0, 0)             Move Down     (1, 0)         0 + 2 = 2
(1, 0)             Move Right    (1, 1)         2 + 3 = 5
(1, 1)             Move Down     (2, 1)         5 + 4 = 9

The minimum cost to reach bottom-right cell is 9.


Constraints:

2 <= m, n <= 80
m == grid.length
n == grid[i].length
0 <= grid[i][j] <= 10^4
0 <= k <= 10

"""

# V0
# IDEA : ONE MONOTONE DP PER TELEPORT COUNT, LINKED BY A SUFFIX MINIMUM
#
#   teleports are the only thing that breaks the right/down monotonicity,
#   and there are at most k = 10 of them -- so slice the problem by "how
#   many teleports have been spent". within a slice nothing teleports, and
#   the usual grid DP applies:
#       f[i][j] = min(arrive[i][j], min(f[i-1][j], f[i][j-1]) + grid[i][j])
#   where arrive[i][j] is the cost of *materialising* at (i, j) by teleport
#   (or, in slice 0, the start).
#
#   the link between slices is the interesting part. a teleport into (i, j)
#   may come from ANY cell whose value is >= grid[i][j], so
#       arrive_{t+1}[i][j] = min{ f_t[x][y] : grid[x][y] >= grid[i][j] }
#   which naively is O((mn)^2). but the condition only depends on the cell's
#   VALUE, so sorting the cells by value descending and sweeping a running
#   minimum computes all of them in one pass -- taking care to fold in a
#   whole equal-value group before reading it back, since the comparison is
#   >= and not >.
#
#   the answer is the best bottom-right value over all slices; spending
#   fewer teleports is always allowed, so no extra "at most" bookkeeping is
#   needed.
#
# time = O(k * m * n * log(m * n)), space = O(m * n)
class Solution(object):
    def minCost(self, grid, k):
        m, n = len(grid), len(grid[0])
        INF = float('inf')

        cells = sorted(((grid[i][j], i, j) for i in range(m) for j in range(n)),
                       key=lambda c: -c[0])

        arrive = [[INF] * n for _ in range(m)]
        arrive[0][0] = 0
        best = INF
        for _ in range(k + 1):
            f = [[INF] * n for _ in range(m)]
            for i in range(m):
                row, arow, prev = f[i], arrive[i], f[i - 1] if i else None
                grow = grid[i]
                for j in range(n):
                    v = arow[j]
                    step = INF
                    if i:
                        step = prev[j]
                    if j and row[j - 1] < step:
                        step = row[j - 1]
                    if step < INF and step + grow[j] < v:
                        v = step + grow[j]
                    row[j] = v
            if f[m - 1][n - 1] < best:
                best = f[m - 1][n - 1]

            # suffix minimum of f over cells sorted by value descending
            nxt = [[INF] * n for _ in range(m)]
            run = INF
            idx = 0
            total = m * n
            while idx < total:
                val = cells[idx][0]
                end = idx
                while end < total and cells[end][0] == val:
                    x, y = cells[end][1], cells[end][2]
                    if f[x][y] < run:
                        run = f[x][y]
                    end += 1
                for t in range(idx, end):
                    nxt[cells[t][1]][cells[t][2]] = run
                idx = end
            arrive = nxt
        return best
