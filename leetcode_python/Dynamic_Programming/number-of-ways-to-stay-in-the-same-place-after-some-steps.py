"""

1269. Number of Ways to Stay in the Same Place After Some Steps
Hard

You have a pointer at index 0 in an array of size arrLen. At each step,
you can move 1 position to the left, 1 position to the right in the array,
or stay in the same place (The pointer should not be placed outside the array
at any time).

Given two integers steps and arrLen, return the number of ways such that your
pointer is still at index 0 after exactly steps steps.
Since the answer may be too large, return it modulo 10^9 + 7.


Example 1:

Input: steps = 3, arrLen = 2
Output: 4
Explanation: There are 4 differents ways to stay at index 0 after 3 steps.
Right, Left, Stay
Stay, Right, Left
Right, Stay, Left
Stay, Stay, Stay

Example 2:

Input: steps = 2, arrLen = 4
Output: 2
Explanation: There are 2 differents ways to stay at index 0 after 2 steps
Right, Left
Stay, Stay

Example 3:

Input: steps = 4, arrLen = 2
Output: 8


Constraints:

1 <= steps <= 500
1 <= arrLen <= 10^6

"""

# V0
# IDEA : 1D DP (rolling array)
#
#  dp[j] = number of ways to be at index j after the steps done so far
#  dp_new[j] = dp[j] + dp[j - 1] + dp[j + 1]
#
#  KEY PRUNE : we must come back to 0, so we can never go further than
#              steps // 2 -> cap the width at min(arrLen, steps // 2 + 1)
#
"""

DP def
    dp[j]: number of ways to be at index j after the steps taken so far

DP eq

     dp_new[j] = dp[j]        # stay
               + dp[j-1]      # arrive from the left
               + dp[j+1]      # arrive from the right


    -> e.g. KEY PRUNE: we must come BACK to 0, so the pointer can never go
              further right than steps // 2

         width = min(arrLen, steps // 2 + 1)

     init: dp[0] = 1
     ans = dp[0] after `steps` rounds, mod 10^9 + 7

"""
# time = O(steps * min(arrLen, steps))
# space = O(min(arrLen, steps))
class Solution(object):
    def numWays(self, steps, arrLen):
        MOD = 10 ** 9 + 7
        width = min(arrLen, steps // 2 + 1)
        dp = [0] * width
        dp[0] = 1
        for _ in range(steps):
            nxt = [0] * width
            for j in range(width):
                cur = dp[j]
                if cur:
                    nxt[j] = (nxt[j] + cur) % MOD
                    if j > 0:
                        nxt[j - 1] = (nxt[j - 1] + cur) % MOD
                    if j + 1 < width:
                        nxt[j + 1] = (nxt[j + 1] + cur) % MOD
            dp = nxt
        return dp[0] % MOD
