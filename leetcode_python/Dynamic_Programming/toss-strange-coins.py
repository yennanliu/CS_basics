"""

1230. Toss Strange Coins
Medium

You have some coins. The i-th coin has a probability prob[i] of facing heads when tossed.

Return the probability that the number of coins facing heads equals target if you toss
every coin exactly once.

Example 1:

Input: prob = [0.4], target = 1
Output: 0.40000

Example 2:

Input: prob = [0.5,0.5,0.5,0.5,0.5], target = 0
Output: 0.03125


Constraints:

1 <= prob.length <= 1000
0 <= prob[i] <= 1
0 <= target <= prob.length
Answers will be accepted as correct if they are within 10^-5 of the correct answer.

"""

# V0
# IDEA: 1D DP (rolled from the 2D "first i coins, j heads" table)
"""
 DP def:
    - dp[j] = probability of getting exactly j heads among the coins seen so far

 DP eq (adding coin with head probability p):
    - dp[j] = dp[j] * (1 - p) + dp[j - 1] * p        (j > 0)
    - dp[0] = dp[0] * (1 - p)

 NOTE !!! loop j BACKWARD, so dp[j - 1] is still the previous coin's value
 Init: dp[0] = 1 (zero coins -> zero heads with probability 1)
"""
# time = O(n * target)
# space = O(target)
class Solution(object):
    def probabilityOfHeads(self, prob, target):
        dp = [0.0] * (target + 1)
        dp[0] = 1.0

        for p in prob:
            for j in range(target, -1, -1):
                dp[j] = dp[j] * (1 - p) + (dp[j - 1] * p if j > 0 else 0.0)

        return dp[target]
