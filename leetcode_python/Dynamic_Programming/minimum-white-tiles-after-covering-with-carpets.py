"""

2209. Minimum White Tiles After Covering With Carpets
Hard

You are given a 0-indexed binary string floor, which represents the colors of tiles on a floor:

floor[i] = '0' denotes that the ith tile of the floor is colored black.
On the other hand, floor[i] = '1' denotes that the ith tile of the floor is colored white.

You are also given numCarpets and carpetLen. You have numCarpets black carpets, each of length carpetLen tiles. Cover the tiles with the given carpets such that the number of white tiles still visible is minimum. Carpets may overlap one another.

Return the minimum number of white tiles still visible.


Example 1:

Input: floor = "10110101", numCarpets = 2, carpetLen = 2
Output: 2
Explanation:
The figure above shows one way of covering the tiles with the carpets such that only 2 white tiles are visible.
No other way of covering the tiles with the carpets can leave less than 2 white tiles visible.

Example 2:

Input: floor = "11111", numCarpets = 2, carpetLen = 3
Output: 0
Explanation:
The figure above shows one way of covering the tiles with the carpets such that no white tiles are visible.
Note that the carpets are able to overlap one another.


Constraints:

1 <= carpetLen <= floor.length <= 1000
floor[i] is either '0' or '1'.
1 <= numCarpets <= 1000

"""

# V0
# IDEA : DP ON (PREFIX LENGTH, CARPETS USED)
#
#   dp[i][j] = fewest white tiles visible among the first i tiles using at
#              most j carpets. at tile i - 1 there are two choices :
#
#       leave it uncovered  -> dp[i-1][j] + (floor[i-1] == '1')
#       end a carpet here   -> dp[max(0, i - carpetLen)][j-1]
#         (the carpet blankets tiles [i - carpetLen, i - 1], so everything
#          under it contributes nothing)
#
#   placing a carpet so that it ENDS at the current tile loses nothing —
#   sliding it further right could only be handled by a later i — so the two
#   options cover every optimal layout, overlaps included.
#
"""

DP def
    dp[j][i]: FEWEST white tiles still visible among the first i tiles

              using at most j carpets

DP eq

     at tile i-1 there are two choices:

        leave it UNCOVERED -> dp[j][i-1] + (floor[i-1] == '1')

        END a carpet here  -> dp[j-1][max(0, i - carpetLen)]
                              (the carpet blankets [i-carpetLen, i-1], so
                               everything under it contributes nothing)

     dp[j][i] = min of the two


    -> e.g. placing a carpet so that it ENDS at the current tile loses
              nothing - sliding it further right would be handled by a later
              i - so these two options cover every optimal layout, overlaps
              included

     init: dp[0][i] = number of '1' in floor[:i]
     ans = dp[numCarpets][n]

"""
# time = O(n * numCarpets), space = O(n * numCarpets)
class Solution(object):
    def minimumWhiteTiles(self, floor, numCarpets, carpetLen):
        n = len(floor)
        # dp[j][i] : j carpets, first i tiles
        dp = [[0] * (n + 1) for _ in range(numCarpets + 1)]
        for i in range(1, n + 1):
            dp[0][i] = dp[0][i - 1] + (1 if floor[i - 1] == '1' else 0)

        for j in range(1, numCarpets + 1):
            for i in range(1, n + 1):
                skip = dp[j][i - 1] + (1 if floor[i - 1] == '1' else 0)
                cover = dp[j - 1][max(0, i - carpetLen)]
                dp[j][i] = min(skip, cover)
        return dp[numCarpets][n]
