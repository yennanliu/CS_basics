"""

3592. Inverse Coin Change
Medium

You are given a 1-indexed integer array numWays, where numWays[i] represents the number of ways to select a total amount i using an infinite supply of some fixed coin denominations. Each denomination is a positive integer with value at most numWays.length.

However, the exact coin denominations have been lost. Your task is to recover the set of denominations that could have resulted in the given numWays array.

Return a sorted array containing unique integers which represents this set of denominations.

If no such set exists, return an empty array.


Example 1:

Input: numWays = [0,1,0,2,0,3,0,4,0,5]
Output: [2,4,6]
Explanation:

Amount | Ways | Explanation
1      | 0    | There is no way to select coins with total value 1.
2      | 1    | The only way is [2].
3      | 0    | There is no way to select coins with total value 3.
4      | 2    | The ways are [2, 2] and [4].
5      | 0    | There is no way to select coins with total value 5.
6      | 3    | The ways are [2, 2, 2], [2, 4], and [6].
7      | 0    | There is no way to select coins with total value 7.
8      | 4    | The ways are [2, 2, 2, 2], [2, 2, 4], [2, 6], and [4, 4].
9      | 0    | There is no way to select coins with total value 9.
10     | 5    | The ways are [2, 2, 2, 2, 2], [2, 2, 2, 4], [2, 4, 4],
       |      | [2, 2, 6], and [4, 6].

Example 2:

Input: numWays = [1,2,2,3,4]
Output: [1,2,5]
Explanation:

Amount | Ways | Explanation
1      | 1    | The only way is [1].
2      | 2    | The ways are [1, 1] and [2].
3      | 2    | The ways are [1, 1, 1] and [1, 2].
4      | 3    | The ways are [1, 1, 1, 1], [1, 1, 2], and [2, 2].
5      | 4    | The ways are [1, 1, 1, 1, 1], [1, 1, 1, 2], [1, 2, 2], and [5].

Example 3:

Input: numWays = [1,2,3,4,15]
Output: []
Explanation: No set of denomination satisfies this array.


Constraints:

1 <= numWays.length <= 100
0 <= numWays[i] <= 2 * 10^8

"""

# V0
# IDEA : REBUILD THE COIN-CHANGE DP LEFT TO RIGHT AND READ OFF MISSING COINS
#
#   sweep amounts in increasing order maintaining the usual unbounded
#   knapsack table for the coins discovered so far. when we reach amount i,
#   every coin smaller than i has already been accounted for, so the table
#   value dp[i] is final apart from the contribution of a coin worth exactly
#   i — which would add exactly 1 way (the single-coin multiset).
#
#   therefore dp[i] must equal numWays[i] (no coin i) or numWays[i] - 1
#   (coin i exists). anything else is inconsistent and the answer is [].
#
"""

DP def
    rebuild the ordinary coin-change table LEFT TO RIGHT and read the missing
    coins off it

    dp[a]: number of ways to make amount a using the coins DISCOVERED SO FAR

           (unbounded knapsack, exactly like LC 518)

DP eq

     when the sweep reaches amount i, every coin smaller than i is already
     accounted for, so dp[i] is final EXCEPT for a coin worth exactly i -
     which would add exactly 1 way (the single-coin multiset). therefore:

        dp[i] == numWays[i]        -> coin i does NOT exist

        dp[i] + 1 == numWays[i]    -> coin i EXISTS: record it and fold it in
                                      dp[a] += dp[a - i]  for a in [i, n]

        anything else              -> inconsistent -> return []


    -> e.g. ans = the recorded coins, in increasing order

"""
# time = O(n^2), space = O(n)
class Solution(object):
    def findCoins(self, numWays):
        n = len(numWays)
        dp = [0] * (n + 1)
        dp[0] = 1
        coins = []
        for i in range(1, n + 1):
            want = numWays[i - 1]
            if dp[i] == want:
                continue
            if dp[i] + 1 != want:
                return []
            coins.append(i)
            for a in range(i, n + 1):
                dp[a] += dp[a - i]
        return coins
