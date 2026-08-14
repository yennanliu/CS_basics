"""

1866. Number of Ways to Rearrange Sticks With K Sticks Visible
Hard

There are n uniquely-sized sticks whose lengths are integers from 1 to n. You want to arrange the sticks such that exactly k sticks are visible from the left. A stick is visible from the left if there are no longer sticks to the left of it.

For example, if the sticks are arranged [1,3,2,5,4], then the sticks with lengths 1, 3, and 5 are visible from the left.

Given n and k, return the number of such arrangements. Since the answer may be large, return it modulo 10^9 + 7.


Example 1:

Input: n = 3, k = 2
Output: 3
Explanation: [1,3,2], [2,3,1], and [2,1,3] are the only arrangements such that exactly 2 sticks are visible.

Example 2:

Input: n = 5, k = 5
Output: 1
Explanation: [1,2,3,4,5] is the only arrangement such that all 5 sticks are visible.

Example 3:

Input: n = 20, k = 11
Output: 647427950
Explanation: There are 647427950 (mod 10^9 + 7) ways to rearrange the sticks such that exactly 11 sticks are visible.


Constraints:

1 <= n <= 1000
1 <= k <= n

"""

# V0
# IDEA : DP (unsigned Stirling numbers of the FIRST kind)
#
#   dp[i][j] = arrangements of i sticks with exactly j visible from the left.
#   condition on where the SHORTEST stick (length 1) is placed :
#     - it is FIRST     -> nothing hides it, so it IS visible, and the
#                          other i-1 sticks must show j-1  -> dp[i-1][j-1]
#     - it is ANYWHERE  -> some longer stick sits to its left, so it is
#       ELSE               invisible and does not change the visible count;
#                          there are (i-1) slots for it     -> (i-1)*dp[i-1][j]
#   (only the relative order matters, so "the other i-1 sticks" behave
#    exactly like an instance of size i-1.)
#
#   dp[i][j] = dp[i-1][j-1] + (i-1) * dp[i-1][j],  dp[0][0] = 1
#
#   NOTE : only the previous row is needed -> roll it into a 1D array,
#          iterating j DOWNWARD so dp[j-1] is still the old row's value.
#
# time = O(n * k), space = O(k)
class Solution(object):
    def rearrangeSticks(self, n, k):
        MOD = 10 ** 9 + 7
        dp = [0] * (k + 1)
        dp[0] = 1

        for i in range(1, n + 1):
            for j in range(min(i, k), 0, -1):
                dp[j] = (dp[j] * (i - 1) + dp[j - 1]) % MOD
            dp[0] = 0

        return dp[k]
