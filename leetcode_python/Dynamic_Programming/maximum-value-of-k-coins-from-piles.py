"""

2218. Maximum Value of K Coins From Piles
Hard

There are n piles of coins on a table. Each pile consists of a positive number of coins of assorted denominations.

In one move, you can choose any coin on top of any pile, remove it, and add it to your wallet.

Given a list piles, where piles[i] is a list of integers denoting the composition of the ith pile from top to bottom, and a positive integer k, return the maximum total value of coins you can have in your wallet if you choose exactly k coins optimally.


Example 1:

Input: piles = [[1,100,3],[7,8,9]], k = 2
Output: 101
Explanation:
The above diagram shows the different ways we can choose k coins.
The maximum total we can obtain is 101.

Example 2:

Input: piles = [[100],[100],[100],[100],[100],[100],[1,1,1,1,1,1,700]], k = 7
Output: 706
Explanation:
The maximum total can be obtained if we choose all coins from the last pile.


Constraints:

n == piles.length
1 <= n <= 1000
1 <= piles[i][j] <= 10^5
1 <= k <= sum(piles[i].length) <= 2000

"""

# V0
# IDEA : GROUPED KNAPSACK DP (each pile is one "group", pick a prefix of it)
#
#   coins in a pile can only be taken top-down, so from pile i we take a
#   PREFIX of length h, worth prefix_sum[h].
#
#   f[j] = best value using exactly j picks from the piles seen so far.
#   for each pile : f[j] = max(f[j], f[j - h] + prefix_sum[h]) for 0 <= h <= j
#
#   NOTE : iterate j downward so a pile is used at most once (0/1 knapsack).
#   NOTE : h is also bounded by len(pile) -> total work is O(k * total_coins).
#
"""

DP def
    (GROUPED knapsack - each pile is one group, and you may only take a
     PREFIX of it, since coins come off the top)

    f[j]: best value using exactly j picks from the piles seen so far

    s[h]: prefix sum of a pile = value of its top h coins

DP eq

     for each pile, for j from k DOWN to 1:

        f[j] = max( f[j],  f[j - h] + s[h] )    for 1 <= h <= min(j, len(pile))


    -> e.g. NOTE !!! iterate j DOWNWARD so each pile is used at most once
              (0/1 knapsack ordering)

     h is also bounded by len(pile), so the total work is O(k * total coins)

     init: f = all 0
     ans = f[k]

"""
# time  = O(k * L), L = total number of coins
# space = O(k)
class Solution(object):
    def maxValueOfCoins(self, piles, k):
        f = [0] * (k + 1)

        for pile in piles:
            # prefix sums : s[h] = value of the top h coins
            s = [0]
            for v in pile:
                if len(s) > k:
                    break
                s.append(s[-1] + v)

            for j in range(k, 0, -1):
                best = f[j]
                for h in range(1, min(j, len(s) - 1) + 1):
                    cand = f[j - h] + s[h]
                    if cand > best:
                        best = cand
                f[j] = best

        return f[k]
