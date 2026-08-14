"""

1155. Number of Dice Rolls With Target Sum
Medium

You have n dice, and each dice has k faces numbered from 1 to k.

Given three integers n, k, and target, return the number of possible ways
(out of the k^n total ways) to roll the dice, so the sum of the face-up numbers
equals target. Since the answer may be too large, return it modulo 10^9 + 7.


Example 1:

Input: n = 1, k = 6, target = 3
Output: 1
Explanation: You throw one die with 6 faces.
There is only one way to get a sum of 3.

Example 2:

Input: n = 2, k = 6, target = 7
Output: 6
Explanation: You throw two dice, each with 6 faces.
There are 6 ways to get a sum of 7: 1+6, 2+5, 3+4, 4+3, 5+2, 6+1.

Example 3:

Input: n = 30, k = 30, target = 500
Output: 222616187
Explanation: The answer must be returned modulo 10^9 + 7.


Constraints:

1 <= n, k <= 30
1 <= target <= 1000

"""

# V0
# IDEA : 2D DP -> rolling 1D array
#
#  DP def:
#    - dp[j] = number of ways to reach sum j with the dice rolled so far
#
#  DP eq:
#    - new_dp[j] = sum( dp[j - h] ) for h in 1..k, j - h >= 0
#
#  init: dp[0] = 1 (0 dice, sum 0), answer = dp[target] after n rounds
# time = O(n * k * target)
# space = O(target)
class Solution(object):
    def numRollsToTarget(self, n, k, target):
        MOD = 10 ** 9 + 7
        dp = [0] * (target + 1)
        dp[0] = 1
        for _ in range(n):
            ndp = [0] * (target + 1)
            for j in range(1, target + 1):
                for h in range(1, k + 1):
                    if j - h < 0:
                        break
                    ndp[j] = (ndp[j] + dp[j - h]) % MOD
            dp = ndp
        return dp[target]
