"""

3116. Kth Smallest Amount With Single Denomination Combination
Hard

You are given an integer array coins representing coins of different denominations and an integer k.

You have an infinite number of coins of each denomination. However, you are not allowed to combine coins of different denominations.

Return the kth smallest amount that can be made using these coins.


Example 1:

Input: coins = [3,6,9], k = 3
Output: 9
Explanation: The given coins can make the following amounts:
Coin 3 produces multiples of 3: 3, 6, 9, 12, 15, etc.
Coin 6 produces multiples of 6: 6, 12, 18, 24, etc.
Coin 9 produces multiples of 9: 9, 18, 27, 36, etc.
All of the coins combined produce: 3, 6, 9, 12, 15, etc.

Example 2:

Input: coins = [5,2], k = 7
Output: 12
Explanation: The given coins can make the following amounts:
Coin 5 produces multiples of 5: 5, 10, 15, 20, etc.
Coin 2 produces multiples of 2: 2, 4, 6, 8, 10, 12, etc.
All of the coins combined produce: 2, 4, 5, 6, 8, 10, 12, 14, 15, etc.


Constraints:

1 <= coins.length <= 15
1 <= coins[i] <= 25
1 <= k <= 2 * 10^9

"""

# V0
# IDEA : BINARY SEARCH THE AMOUNT, COUNT WITH INCLUSION-EXCLUSION
#
#   "no mixing denominations" means the reachable amounts are exactly the
#   union of the multiple-sets of each coin. how many of them are <= x is
#   therefore a union cardinality, which inclusion-exclusion gives :
#
#       |union| = sum over non-empty subsets S of
#                     (-1)^(|S|+1) * floor(x / lcm(S))
#
#   with at most 15 coins that is 32767 subsets, each an O(1) division once
#   the subset's lcm is known.
#
#   the count is non-decreasing in x, so binary search for the smallest x
#   whose count reaches k — and that x is itself reachable, because the count
#   only increases at reachable amounts.
#
#   the upper bound min(coins) * k is safe : that coin alone already produces
#   k amounts by then.
#
# time = O(2^n * log(min(coins) * k)), space = O(2^n)
class Solution(object):
    def findKthSmallest(self, coins, k):

        def gcd(a, b):
            while b:
                a, b = b, a % b
            return a

        n = len(coins)
        # precompute (lcm, sign) for every non-empty subset
        subsets = []
        for mask in range(1, 1 << n):
            lcm = 1
            bits = 0
            for i in range(n):
                if (mask >> i) & 1:
                    bits += 1
                    lcm = lcm // gcd(lcm, coins[i]) * coins[i]
            subsets.append((lcm, 1 if bits % 2 else -1))

        def count(x):
            return sum(sign * (x // lcm) for lcm, sign in subsets)

        lo, hi = 1, min(coins) * k
        while lo < hi:
            mid = (lo + hi) // 2
            if count(mid) >= k:
                hi = mid
            else:
                lo = mid + 1
        return lo
