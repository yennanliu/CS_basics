"""

1444. Number of Ways of Cutting a Pizza
Hard

Given a rectangular pizza represented as a rows x cols matrix containing the following characters: 'A' (an apple) and '.' (empty cell) and given the integer k. You have to cut the pizza into k pieces using k-1 cuts.

For each cut you choose the direction: vertical or horizontal, then you choose a cut position at the cell boundary and cut the pizza into two pieces. If you cut the pizza vertically, give the left part of the pizza to a person. If you cut the pizza horizontally, give the upper part of the pizza to a person. Give the last piece of pizza to the last person.

Return the number of ways of cutting the pizza such that each piece contains at least one apple. Since the answer can be a huge number, return this modulo 10^9 + 7.


Example 1:

Input: pizza = ["A..","AAA","..."], k = 3
Output: 3
Explanation: The figure above shows the three ways to cut the pizza. Note that pieces must contain at least one apple.

Example 2:

Input: pizza = ["A..","AA.","..."], k = 3
Output: 1

Example 3:

Input: pizza = ["A..","A..","..."], k = 1
Output: 1


Constraints:

1 <= rows, cols <= 50
rows == pizza.length
cols == pizza[i].length
1 <= k <= 10
pizza consists of characters 'A' and '.' only.

"""

# V0
# IDEA : 2D PREFIX SUM + MEMOIZED SEARCH (state = remaining bottom-right block)
#
#   every cut hands away the top or the left strip, so whatever is left is
#   always the sub-rectangle (i, j) .. (m-1, n-1) -> the state is just
#   (i, j, cuts_left).
#   dfs(i, j, c) = number of ways to make c more cuts on that block:
#     - c == 0 : the block itself must hold an apple -> 1 or 0
#     - else   : try every horizontal row x > i / vertical col y > j; the
#                strip given away must contain an apple, then recurse on
#                the rest.
#   s[i][j] = apples in the top-left i x j rectangle answers every
#   "does this strip have an apple" test in O(1).
#
"""

DP def
    every cut hands away the TOP or the LEFT strip, so whatever remains is
    always the sub-rectangle (i, j)..(m-1, n-1) -> the state is just
    (i, j, cuts left)

    dfs(i, j, c): number of ways to make c more cuts on that block

    s[i][j]     : apples in the top-left i x j rectangle (2D prefix sum)

DP eq

     c == 0 : the block itself must hold an apple -> 1 or 0

     else   : dfs(i, j, c) = sum over rows x > i with apples(i, j, x, n) > 0
                                of dfs(x, j, c-1)

                           + sum over cols y > j with apples(i, j, m, y) > 0
                                of dfs(i, y, c-1)


    -> e.g. the strip GIVEN AWAY must contain an apple; the prefix sum
              answers each such test in O(1)

     ans = dfs(0, 0, k-1) % (10^9 + 7)

"""
# time = O(m * n * k * (m + n)), space = O(m * n * k)
from functools import lru_cache
class Solution(object):
    def ways(self, pizza, k):
        MOD = 10 ** 9 + 7
        m, n = len(pizza), len(pizza[0])

        # s[i][j] = apples in rows [0, i) and cols [0, j)
        s = [[0] * (n + 1) for _ in range(m + 1)]
        for i in range(m):
            for j in range(n):
                s[i + 1][j + 1] = (
                    s[i][j + 1] + s[i + 1][j] - s[i][j] + (1 if pizza[i][j] == 'A' else 0)
                )

        def apples(r1, c1, r2, c2):
            # apples in rows [r1, r2) and cols [c1, c2)
            return s[r2][c2] - s[r1][c2] - s[r2][c1] + s[r1][c1]

        @lru_cache(None)
        def dfs(i, j, c):
            if c == 0:
                return 1 if apples(i, j, m, n) > 0 else 0
            res = 0
            for x in range(i + 1, m):
                if apples(i, j, x, n) > 0:          # top strip has an apple
                    res += dfs(x, j, c - 1)
            for y in range(j + 1, n):
                if apples(i, j, m, y) > 0:          # left strip has an apple
                    res += dfs(i, y, c - 1)
            return res % MOD

        res = dfs(0, 0, k - 1)
        dfs.cache_clear()
        return res
