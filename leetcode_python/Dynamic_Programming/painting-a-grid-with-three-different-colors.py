"""

1931. Painting a Grid With Three Different Colors
Hard

You are given two integers m and n. Consider an m x n grid where each cell is initially white. You can paint each cell red, green, or blue. All cells must be painted.

Return the number of ways to color the grid with no two adjacent cells having the same color. Since the answer can be very large, return it modulo 10^9 + 7.


Example 1:

Input: m = 1, n = 1
Output: 3
Explanation: The three possible colorings are shown in the image above.

Example 2:

Input: m = 1, n = 2
Output: 6
Explanation: The six possible colorings are shown in the image above.

Example 3:

Input: m = 5, n = 5
Output: 580986


Constraints:

1 <= m <= 5
1 <= n <= 1000

"""

# V0
# IDEA : BITMASK / BASE-3 STATE COMPRESSION DP OVER COLUMNS
#
#   m <= 5, so a whole column is one base-3 number with m digits (< 243).
#   step 1 : keep only the columns that are internally valid (no two vertically
#            adjacent digits equal).
#   step 2 : precompute, for each valid column x, the list of valid columns y
#            that may sit immediately to its left (every digit differs).
#   step 3 : f[x] = number of ways to paint the first i columns ending with x.
#            f_next[x] = sum over compatible y of f[y].
#
#   NOTE : only ~48 of the 243 column patterns survive step 1 for m = 5, so the
#          transition table is tiny and the n-loop is the only long one.
#
"""

DP def
    m <= 5, so a whole COLUMN is one base-3 number with m digits (< 243)

    valid : the columns that are internally legal
            (no two VERTICALLY adjacent digits equal)

    prev_ok[x]: the valid columns y that may sit immediately LEFT of x
                (every digit differs)

    f[x]: number of ways to paint the first i columns ENDING with column x

DP eq

     f_next[x] = sum over y in prev_ok[x] of f[y]


    -> e.g. only ~48 of the 243 column patterns survive for m = 5, so the
              transition table is tiny and only the n-loop is long

     init: f[x] = 1 for every valid x  (the first column)
     ans = sum(f) after n columns, mod 10^9 + 7

"""
# time = O(3^(2m) + n * V^2) with V = number of valid columns
# space = O(V^2)
class Solution(object):
    def colorTheGrid(self, m, n):
        MOD = 10 ** 9 + 7

        def digits(x):
            out = []
            for _ in range(m):
                out.append(x % 3)
                x //= 3
            return out

        valid = []
        cols = {}
        for x in range(3 ** m):
            d = digits(x)
            if all(d[i] != d[i + 1] for i in range(m - 1)):
                valid.append(x)
                cols[x] = d

        prev_ok = {}
        for x in valid:
            dx = cols[x]
            prev_ok[x] = [y for y in valid
                          if all(dx[i] != cols[y][i] for i in range(m))]

        f = dict((x, 1) for x in valid)
        for _ in range(n - 1):
            g = {}
            for x in valid:
                total = 0
                for y in prev_ok[x]:
                    total += f[y]
                g[x] = total % MOD
            f = g
        return sum(f.values()) % MOD
