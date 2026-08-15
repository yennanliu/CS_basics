"""

2787. Ways to Express an Integer as Sum of Powers
Medium

Given two positive integers n and x.

Return the number of ways n can be expressed as the sum of the xth power of unique positive integers, in other words, the number of sets of unique integers [n1, n2, ..., nk] where n = n1^x + n2^x + ... + nk^x.

Since the result can be very large, return it modulo 10^9 + 7.

For example, if n = 160 and x = 3, one way to express n is n = 2^3 + 3^3 + 5^3.


Example 1:

Input: n = 10, x = 2
Output: 1
Explanation: We can express n as the following: n = 3^2 + 1^2 = 10.
It can be shown that it is the only way to express 10 as the sum of the 2nd power of unique integers.

Example 2:

Input: n = 4, x = 1
Output: 2
Explanation: We can express n in the following ways:
- n = 4^1 = 4.
- n = 3^1 + 1^1 = 4.


Constraints:

1 <= n <= 300
1 <= x <= 5

"""

# V0
# IDEA : 0/1 KNAPSACK (COUNT SUBSETS SUMMING TO n)
#
#   each base integer i contributes the item weight i^x and may be used AT
#   MOST ONCE ("unique positive integers") -> classic 0/1 knapsack counting.
#
#   dp[j] = number of subsets of the bases processed so far whose powers
#           sum to exactly j.   dp[0] = 1 (the empty subset).
#
#   NOTE : the inner loop MUST run downward (j from n to k). Iterating upward
#          would let the same base be reused, which turns this into unbounded
#          knapsack and over-counts.
#
#   NOTE : we only need bases i with i^x <= n, so the loop stops as soon as
#          i^x exceeds n (for x = 5 and n = 300 that is just i <= 3).
#
#   NOTE : take the answer mod 1e9+7 as we go.
#
# time = O(n * n^(1/x)), space = O(n)
class Solution(object):
    def numberOfWays(self, n, x):
        MOD = 10 ** 9 + 7
        dp = [0] * (n + 1)
        dp[0] = 1
        i = 1
        while i ** x <= n:
            k = i ** x
            for j in range(n, k - 1, -1):
                dp[j] = (dp[j] + dp[j - k]) % MOD
            i += 1
        return dp[n]
