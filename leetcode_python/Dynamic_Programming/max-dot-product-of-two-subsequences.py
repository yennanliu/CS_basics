"""

1458. Max Dot Product of Two Subsequences
Hard

Given two arrays nums1 and nums2.

Return the maximum dot product between non-empty subsequences of nums1 and nums2
with the same length.

A subsequence of an array is a new array which is formed from the original array
by deleting some (can be none) of the characters without disturbing the relative
positions of the remaining characters. (ie, [2,3,5] is a subsequence of
[1,2,3,4,5] while [1,5,3] is not).


Example 1:

Input: nums1 = [2,1,-2,5], nums2 = [3,0,-6]
Output: 18
Explanation: Take subsequence [2,-2] from nums1 and subsequence [3,-6] from nums2.
Their dot product is (2*3 + (-2)*(-6)) = 18.

Example 2:

Input: nums1 = [3,-2], nums2 = [2,-6,7]
Output: 21
Explanation: Take subsequence [3] from nums1 and subsequence [7] from nums2.
Their dot product is (3*7) = 21.

Example 3:

Input: nums1 = [-1,-1], nums2 = [1,1]
Output: -1
Explanation: Take subsequence [-1] from nums1 and subsequence [1] from nums2.
Their dot product is -1.


Constraints:

1 <= nums1.length, nums2.length <= 500
-1000 <= nums1[i], nums2[i] <= 1000

"""

# V0
# IDEA: 2D DP (LCS-like)
#
#  DP def:
#    - dp[i][j] = best dot product using a NON-EMPTY pairing taken from
#                 nums1[:i] and nums2[:j]
#
#  DP eq (v = nums1[i-1] * nums2[j-1]):
#    - dp[i][j] = max( dp[i-1][j],                     # skip nums1[i-1]
#                      dp[i][j-1],                     # skip nums2[j-1]
#                      max(0, dp[i-1][j-1]) + v )      # pair them up
#
#  NOTE !!! the `max(0, dp[i-1][j-1])` is the whole trick:
#    - dropping a negative prefix = starting a brand-new pairing with just this
#      one pair, which is what makes the all-negative answer come out right
#      (e.g. nums1=[-1,-1], nums2=[1,1] -> -1, NOT 0, since it must be non-empty)
#
# time = O(m * n)
# space = O(m * n)
class Solution(object):
    def maxDotProduct(self, nums1, nums2):
        m, n = len(nums1), len(nums2)

        # NEG marks "no valid (non-empty) pairing yet"
        NEG = float('-inf')
        dp = [[NEG] * (n + 1) for _ in range(m + 1)]

        for i in range(1, m + 1):
            for j in range(1, n + 1):
                v = nums1[i - 1] * nums2[j - 1]
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1], max(0, dp[i - 1][j - 1]) + v)

        return int(dp[m][n])


# V1
# IDEA: same DP, rolling 1D array
"""

DP def
    dp[i][j]: best dot product using a NON-EMPTY pairing taken from

              nums1[:i] and nums2[:j]

              -> -inf marks "no valid (non-empty) pairing yet"

DP eq

     with v = nums1[i-1] * nums2[j-1]:

     dp[i][j] = max(
                   dp[i-1][j],                  # skip nums1[i-1]
                   dp[i][j-1],                  # skip nums2[j-1]
                   max(0, dp[i-1][j-1]) + v     # PAIR them up
                )


    -> e.g. NOTE !!! the max(0, dp[i-1][j-1]) is the whole trick - dropping
              a negative prefix means starting a BRAND NEW pairing with just
              this one pair, which is what makes the all-negative case right

         nums1 = [-1,-1], nums2 = [1,1] -> -1, NOT 0 (it must be non-empty)

     ans = dp[m][n]

"""
# time = O(m * n)
# space = O(n)
class Solution(object):
    def maxDotProduct(self, nums1, nums2):
        m, n = len(nums1), len(nums2)
        NEG = float('-inf')

        prev = [NEG] * (n + 1)
        for i in range(1, m + 1):
            cur = [NEG] * (n + 1)
            for j in range(1, n + 1):
                v = nums1[i - 1] * nums2[j - 1]
                cur[j] = max(prev[j], cur[j - 1], max(0, prev[j - 1]) + v)
            prev = cur

        return int(prev[n])
