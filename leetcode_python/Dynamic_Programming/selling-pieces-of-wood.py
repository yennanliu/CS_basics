"""

2312. Selling Pieces of Wood
Hard

You are given two integers m and n that represent the height and width of a rectangular piece of wood. You are also given a 2D integer array prices, where prices[i] = [hi, wi, pricei] indicates you can sell a rectangular piece of wood of height hi and width wi for pricei dollars.

To cut a piece of wood, you must make a vertical or horizontal cut across the entire height or width of the piece to split it into two smaller pieces. After cutting a piece of wood into some number of smaller pieces, you can sell pieces according to prices. You may sell multiple pieces of the same shape, and you do not have to sell all the shapes. The grain of the wood makes a difference, so you cannot rotate a piece to swap its height and width.

Return the maximum money you can earn after cutting an m x n piece of wood.

Note that you can cut the piece of wood as many times as you want.


Example 1:

Input: m = 3, n = 5, prices = [[1,4,2],[2,2,7],[2,1,3]]
Output: 19
Explanation: The diagram above shows a possible scenario. It consists of:
- 2 pieces of wood shaped 2 x 2, selling for a price of 2 * 7 = 14.
- 1 piece of wood shaped 2 x 1, selling for a price of 1 * 3 = 3.
- 1 piece of wood shaped 1 x 4, selling for a price of 1 * 2 = 2.
This obtains a total of 14 + 3 + 2 = 19 money earned.
It can be shown that 19 is the maximum amount of money that can be earned.

Example 2:

Input: m = 4, n = 6, prices = [[3,2,10],[1,4,2],[4,1,3]]
Output: 32
Explanation: The diagram above shows a possible scenario. It consists of:
- 3 pieces of wood shaped 3 x 2, selling for a price of 3 * 10 = 30.
- 1 piece of wood shaped 1 x 4, selling for a price of 1 * 2 = 2.
This obtains a total of 30 + 2 = 32 money earned.
It can be shown that 32 is the maximum amount of money that can be earned.
Notice that we cannot rotate the 1 x 4 piece of wood to obtain a 4 x 1 piece of wood.


Constraints:

1 <= m, n <= 200
1 <= prices.length <= 2 * 10^4
prices[i].length == 3
1 <= hi <= m
1 <= wi <= n
1 <= pricei <= 10^6
All the shapes of wood (hi, wi) are pairwise distinct.

"""

# V0
# IDEA : 2D INTERVAL DP (classic "cut a rod", one dimension at a time)
#
#   dp[i][j] = max money obtainable from an i x j block.
#   a block is either sold whole (its listed price, 0 if not listed) or
#   split by ONE full-width / full-height cut:
#       dp[i][j] = max( price[i][j],
#                       max over k in 1..i-1 of dp[k][j] + dp[i-k][j],
#                       max over k in 1..j-1 of dp[i][k] + dp[i][j-k] )
#   every reachable configuration is some sequence of such cuts, and each
#   sub-block is solved independently, so the recurrence is exact.
#
#   NOTE : cuts are symmetric, so k only needs to run to i//2 / j//2.
#          pieces cannot be rotated -> (h, w) and (w, h) are distinct.
#
# time = O(m * n * (m + n)), space = O(m * n)
class Solution(object):
    def sellingWood(self, m, n, prices):
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        for h, w, p in prices:
            dp[h][w] = p

        for i in range(1, m + 1):
            row = dp[i]
            for j in range(1, n + 1):
                best = row[j]
                for k in range(1, i // 2 + 1):
                    cand = dp[k][j] + dp[i - k][j]
                    if cand > best:
                        best = cand
                for k in range(1, j // 2 + 1):
                    cand = row[k] + row[j - k]
                    if cand > best:
                        best = cand
                row[j] = best

        return dp[m][n]
