"""

3426. Manhattan Distances of All Arrangements of Pieces
Hard

You are given three integers m, n, and k.

There is a rectangular grid of size m × n containing k identical pieces. Return
the sum of Manhattan distances between every pair of pieces over all valid
arrangements of pieces.

A valid arrangement is a placement of all k pieces on the grid with at most one
piece per cell.

Since the answer may be very large, return it modulo 10^9 + 7.

The Manhattan Distance between two cells (x_i, y_i) and (x_j, y_j) is |x_i -
x_j| + |y_i - y_j|.

Example 1:

Input: m = 2, n = 2, k = 2

Output: 8

Explanation:

The valid arrangements of pieces on the board are:

In the first 4 arrangements, the Manhattan distance between the two pieces is 1.
In the last 2 arrangements, the Manhattan distance between the two pieces is 2.

Thus, the total Manhattan distance across all valid arrangements is 1 + 1 + 1 +
1 + 2 + 2 = 8.

Example 2:

Input: m = 1, n = 4, k = 3

Output: 20

Explanation:

The valid arrangements of pieces on the board are:

The first and last arrangements have a total Manhattan distance of 1 + 1 + 2 =
4.
The middle two arrangements have a total Manhattan distance of 1 + 2 + 3 = 6.

The total Manhattan distance between all pairs of pieces across all arrangements
is 4 + 6 + 6 + 4 = 20.

Constraints:

1 <= m, n <= 10^5
2 <= m * n <= 10^5
2 <= k <= m * n

"""

# V0
# IDEA : SWAP THE SUMMATION ORDER — EVERY CELL PAIR IS SEEN THE SAME NUMBER OF TIMES
#
#   instead of "for each arrangement, sum over pairs", count "for each pair of
#   cells, how many arrangements put pieces on both".  the pieces are identical
#   and the remaining k - 2 of them go anywhere among the other m*n - 2 cells,
#   so that count is C(mn - 2, k - 2) — the same constant for every pair.  the
#   whole answer is therefore that constant times
#
#     S = sum over unordered cell pairs of |x1 - x2| + |y1 - y2|.
#
#   S splits by coordinate.  for the row part, two cells in rows i and j
#   contribute |i - j| and there are n*n such cell pairs for each ordered row
#   pair, so
#     row part = n^2 * sum_{i<j} (j - i) = n^2 * sum_{d=1}^{m-1} d*(m-d)
#              = n^2 * C(m+1, 3),
#   (same-row pairs contribute 0 and drop out).  the column part is the mirror
#   image, m^2 * C(n+1, 3).
#
# time = O(mn) for the factorial table, space = O(mn)
class Solution(object):
    def distanceSum(self, m, n, k):
        MOD = 10 ** 9 + 7
        total = m * n
        fact = [1] * (total + 1)
        for i in range(1, total + 1):
            fact[i] = fact[i - 1] * i % MOD
        inv = [1] * (total + 1)
        inv[total] = pow(fact[total], MOD - 2, MOD)
        for i in range(total, 0, -1):
            inv[i - 1] = inv[i] * i % MOD

        def comb(a, b):
            if b < 0 or b > a:
                return 0
            return fact[a] * inv[b] % MOD * inv[a - b] % MOD

        def tri(t):
            # C(t+1, 3) = sum_{d=1}^{t-1} d * (t - d)
            return (t - 1) * t % MOD * (t + 1) % MOD * pow(6, MOD - 2, MOD) % MOD

        S = (n * n % MOD * tri(m) + m * m % MOD * tri(n)) % MOD
        return S * comb(total - 2, k - 2) % MOD
