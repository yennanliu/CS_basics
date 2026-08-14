"""

1411. Number of Ways to Paint N x 3 Grid
Hard

You have a grid of size n x 3 and you want to paint each cell of the grid with
exactly one of the three colors: Red, Yellow, or Green while making sure that no
two adjacent cells have the same color (i.e., no two cells that share vertical or
horizontal sides have the same color).

Given n the number of rows of the grid, return the number of ways you can paint
this grid. As the answer may grow large, the answer must be computed modulo
10^9 + 7.


Example 1:

Input: n = 1
Output: 12
Explanation: There are 12 possible way to paint the grid as shown.

Example 2:

Input: n = 5000
Output: 30228214


Constraints:

n == grid.length
1 <= n <= 5000

"""

# V0
# IDEA : DP on ROW PATTERN TYPE (only 2 shapes matter)
#
#   A legal row of 3 cells is either
#     - "ABA" type (ends match)  -> 6 such rows  (3 * 2)
#     - "ABC" type (all distinct)-> 6 such rows  (3!)
#
#   Counting the legal successors of each shape:
#     ABA -> 3 x ABA + 2 x ABC
#     ABC -> 2 x ABA + 2 x ABC
#
#   DP eq:
#     f0' = 3*f0 + 2*f1     (f0 = #ways ending in an ABA row)
#     f1' = 2*f0 + 2*f1     (f1 = #ways ending in an ABC row)
#
# time = O(n)
# space = O(1)
class Solution(object):
    def numOfWays(self, n):
        MOD = 10 ** 9 + 7
        f0 = f1 = 6
        for _ in range(n - 1):
            f0, f1 = (3 * f0 + 2 * f1) % MOD, (2 * f0 + 2 * f1) % MOD
        return (f0 + f1) % MOD

# V1
# IDEA : STATE COMPRESSION DP over all 3^3 = 27 row colorings
#        (slower, but generalises to a grid with more columns)
# time = O(n * 27^2)
# space = O(27)
class Solution(object):
    def numOfWays(self, n):
        MOD = 10 ** 9 + 7

        def row_ok(x):
            last = -1
            for _ in range(3):
                if x % 3 == last:
                    return False
                last = x % 3
                x //= 3
            return True

        def pair_ok(x, y):
            for _ in range(3):
                if x % 3 == y % 3:
                    return False
                x //= 3
                y //= 3
            return True

        valid = [x for x in range(27) if row_ok(x)]
        nxt = {x: [y for y in valid if pair_ok(x, y)] for x in valid}

        f = {x: 1 for x in valid}
        for _ in range(n - 1):
            g = {x: 0 for x in valid}
            for x in valid:
                for y in nxt[x]:
                    g[y] = (g[y] + f[x]) % MOD
            f = g
        return sum(f.values()) % MOD
