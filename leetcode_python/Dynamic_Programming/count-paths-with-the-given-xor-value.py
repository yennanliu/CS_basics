"""

3393. Count Paths With the Given XOR Value
Medium

You are given a 2D integer array grid with size m x n. You are also given an
integer k.

Your task is to calculate the number of paths you can take from the top-left
cell (0, 0) to the bottom-right cell (m - 1, n - 1) satisfying the following
constraints:

You can either move to the right or down. Formally, from the cell (i, j) you may
move to the cell (i, j + 1) or to the cell (i + 1, j) if the target cell exists.
The XOR of all the numbers on the path must be equal to k.

Return the total number of such paths.

Since the answer can be very large, return the result modulo 10^9 + 7.

Example 1:

Input: grid = [[2, 1, 5], [7, 10, 0], [12, 6, 4]], k = 11

Output: 3

Explanation:

The 3 paths are:

(0, 0) -> (1, 0) -> (2, 0) -> (2, 1) -> (2, 2)
(0, 0) -> (1, 0) -> (1, 1) -> (1, 2) -> (2, 2)
(0, 0) -> (0, 1) -> (1, 1) -> (2, 1) -> (2, 2)

Example 2:

Input: grid = [[1, 3, 3, 3], [0, 3, 3, 2], [3, 0, 1, 1]], k = 2

Output: 5

Explanation:

The 5 paths are:

(0, 0) -> (1, 0) -> (2, 0) -> (2, 1) -> (2, 2) -> (2, 3)
(0, 0) -> (1, 0) -> (1, 1) -> (2, 1) -> (2, 2) -> (2, 3)
(0, 0) -> (1, 0) -> (1, 1) -> (1, 2) -> (1, 3) -> (2, 3)
(0, 0) -> (0, 1) -> (1, 1) -> (1, 2) -> (2, 2) -> (2, 3)
(0, 0) -> (0, 1) -> (0, 2) -> (1, 2) -> (2, 2) -> (2, 3)

Example 3:

Input: grid = [[1, 1, 1, 2], [3, 0, 3, 2], [3, 0, 2, 2]], k = 10

Output: 0

Constraints:

1 <= m == grid.length <= 300
1 <= n == grid[r].length <= 300
0 <= grid[r][c] < 16
0 <= k < 16

"""

# V0
# IDEA : GRID DP OVER THE 4-BIT XOR STATE
#
#   every cell value is < 16, so the XOR of any prefix of a path is itself a
#   4-bit number.  that means the *whole* history of a path collapses into one
#   of only 16 states, and two partial paths reaching the same cell with the
#   same running XOR are interchangeable for the rest of the walk.
#
#   so dp[i][j][v] = number of ways to reach (i, j) with running XOR v.
#   a step into (i, j) turns a predecessor state v into v ^ grid[i][j], and
#   XOR is its own inverse so the map v -> v ^ g is a bijection on the 16
#   states — no counts are ever merged or lost.
#
#   only the previous row plus the already-finished part of the current row is
#   needed, so one rolling row of 16-slot buckets is enough.
#
# time = O(m * n * 16), space = O(n * 16)
class Solution(object):
    def countPathsWithXorValue(self, grid, k):
        MOD = 10 ** 9 + 7
        m, n = len(grid), len(grid[0])
        prev = [[0] * 16 for _ in range(n)]
        cur = [[0] * 16 for _ in range(n)]
        for i in range(m):
            for j in range(n):
                g = grid[i][j]
                c = cur[j]
                for v in range(16):
                    c[v] = 0
                if i == 0 and j == 0:
                    c[g] = 1
                    continue
                if i > 0:
                    p = prev[j]
                    for v in range(16):
                        if p[v]:
                            w = v ^ g
                            c[w] = (c[w] + p[v]) % MOD
                if j > 0:
                    p = cur[j - 1]
                    for v in range(16):
                        if p[v]:
                            w = v ^ g
                            c[w] = (c[w] + p[v]) % MOD
            prev, cur = cur, prev
        return prev[n - 1][k] % MOD
