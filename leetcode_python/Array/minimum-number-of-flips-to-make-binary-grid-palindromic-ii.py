"""

3240. Minimum Number of Flips to Make Binary Grid Palindromic II
Medium

You are given an m x n binary matrix grid.

A row or column is considered palindromic if its values read the same forward and backward.

You can flip any number of cells in grid from 0 to 1, or from 1 to 0.

Return the minimum number of cells that need to be flipped to make all rows and columns palindromic, and the total number of 1's in grid divisible by 4.


Example 1:

Input: grid = [[1,0,0],[0,1,0],[0,0,1]]
Output: 3
Explanation:

Example 2:

Input: grid = [[0,1],[0,1],[0,0]]
Output: 2
Explanation:

Example 3:

Input: grid = [[1],[1]]
Output: 2
Explanation:


Constraints:

m == grid.length
n == grid[i].length
1 <= m * n <= 2 * 10^5

"""

# V0
# IDEA : GROUPS OF FOUR ARE FREE OF THE MOD-4 RULE — ONLY THE MIDDLES MATTER
#
#   rows AND columns palindromic ties four cells together :
#       (i, j), (i, n-1-j), (m-1-i, j), (m-1-i, n-1-j)
#   they must all agree, costing min(ones, 4 - ones) flips, and afterwards
#   they hold 0 or 4 ones — either way divisible by 4, so such groups never
#   affect the parity rule.
#
#   what is left is the middle row (odd m) and middle column (odd n), where
#   cells pair up TWO at a time and so contribute 0 or 2 ones :
#       a mismatched pair costs 1 and its final value is our choice
#       a matched 1-1 pair is free but already contributes 2
#   with `ones2` such 1-1 pairs, the running total is 2*ones2 mod 4, which is
#   wrong exactly when ones2 is odd. fixing it is free if some mismatched
#   pair exists (settle it on 1-1 instead of 0-0); otherwise one 1-1 pair
#   must be zeroed at a cost of 2.
#
#   finally the very centre cell (both m and n odd) has no partner, so it
#   must be 0 — everything else is even.
#
# time = O(m * n), space = O(1)
class Solution(object):
    def minFlips(self, grid):
        m, n = len(grid), len(grid[0])
        cost = 0

        # quadruples
        for i in range(m // 2):
            for j in range(n // 2):
                ones = (grid[i][j] + grid[i][n - 1 - j]
                        + grid[m - 1 - i][j] + grid[m - 1 - i][n - 1 - j])
                cost += min(ones, 4 - ones)

        ones2 = 0        # mirrored pairs already holding two 1s
        diff = 0         # mirrored pairs that disagree

        if m % 2:        # middle row
            i = m // 2
            for j in range(n // 2):
                a, b = grid[i][j], grid[i][n - 1 - j]
                if a != b:
                    diff += 1
                elif a == 1:
                    ones2 += 1

        if n % 2:        # middle column
            j = n // 2
            for i in range(m // 2):
                a, b = grid[i][j], grid[m - 1 - i][j]
                if a != b:
                    diff += 1
                elif a == 1:
                    ones2 += 1

        cost += diff
        if ones2 % 2:
            if diff == 0:
                cost += 2                     # zero out one 1-1 pair
            # else: settle a mismatched pair on 1-1 instead, same cost

        if m % 2 and n % 2:
            cost += grid[m // 2][n // 2]      # the centre must end up 0

        return cost
