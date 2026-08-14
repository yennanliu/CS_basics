"""

2338. Count the Number of Ideal Arrays
Hard

You are given two integers n and maxValue, which are used to describe an ideal array.

A 0-indexed integer array arr of length n is considered ideal if the following conditions hold:

Every arr[i] is a value from 1 to maxValue, for 0 <= i < n.
Every arr[i] is divisible by arr[i - 1], for 0 < i < n.

Return the number of distinct ideal arrays of length n. Since the answer may be very large, return it modulo 10^9 + 7.


Example 1:

Input: n = 2, maxValue = 5
Output: 10
Explanation: The following are the possible ideal arrays:
- Arrays starting with the value 1 (5 arrays): [1,1], [1,2], [1,3], [1,4], [1,5]
- Arrays starting with the value 2 (2 arrays): [2,2], [2,4]
- Arrays starting with the value 3 (1 array): [3,3]
- Arrays starting with the value 4 (1 array): [4,4]
- Arrays starting with the value 5 (1 array): [5,5]
There are a total of 5 + 2 + 1 + 1 + 1 = 10 distinct ideal arrays.

Example 2:

Input: n = 5, maxValue = 3
Output: 11
Explanation: The following are the possible ideal arrays:
- Arrays starting with the value 1 (9 arrays):
   - With no other distinct values (1 array): [1,1,1,1,1]
   - With 2nd distinct value 2 (4 arrays): [1,1,1,1,2], [1,1,1,2,2], [1,1,2,2,2], [1,2,2,2,2]
   - With 2nd distinct value 3 (4 arrays): [1,1,1,1,3], [1,1,1,3,3], [1,1,3,3,3], [1,3,3,3,3]
- Arrays starting with the value 2 (1 array): [2,2,2,2,2]
- Arrays starting with the value 3 (1 array): [3,3,3,3,3]
There are a total of 9 + 1 + 1 = 11 distinct ideal arrays.


Constraints:

2 <= n <= 10^4
1 <= maxValue <= 10^4

"""

# V0
# IDEA : SPLIT THE ARRAY INTO ITS *DISTINCT VALUES* AND WHERE THEY CHANGE
#
#   an ideal array is a non-decreasing divisor chain, so it is determined by
#     (a) the sequence of DISTINCT values  v1 | v2 | ... | vk  (each a proper
#         multiple of the previous, all <= maxValue), and
#     (b) which of the n - 1 gaps between consecutive positions are the k - 1
#         "step up" points — C(n - 1, k - 1) ways
#
#   so count the chains by length first :
#       dp[k][v] = number of strict divisor chains of length k ending at v
#       dp[1][v] = 1 ;  dp[k][m] += dp[k-1][d] for every multiple m = 2d, 3d, ...
#   which is a sieve-style pass per k.
#
#   k is bounded by log2(maxValue) (each step at least doubles), so at most
#   14 layers for maxValue <= 10^4.
#
#   answer = sum over k of  (sum_v dp[k][v]) * C(n - 1, k - 1)
#
# time = O(log(maxValue) * maxValue * log(maxValue)), space = O(log(maxValue) * maxValue)
from math import comb


class Solution(object):
    def idealArrays(self, n, maxValue):
        MOD = 10 ** 9 + 7
        K = maxValue.bit_length()          # a chain can never be longer

        dp = [[0] * (maxValue + 1) for _ in range(K + 1)]
        for v in range(1, maxValue + 1):
            dp[1][v] = 1
        for k in range(2, K + 1):
            for d in range(1, maxValue + 1):
                if dp[k - 1][d] == 0:
                    continue
                for m in range(2 * d, maxValue + 1, d):
                    dp[k][m] = (dp[k][m] + dp[k - 1][d]) % MOD

        res = 0
        for k in range(1, K + 1):
            chains = sum(dp[k]) % MOD
            if chains == 0:
                continue
            res = (res + chains * comb(n - 1, k - 1)) % MOD
        return res
