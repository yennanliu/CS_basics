"""

3176. Find the Maximum Length of a Good Subsequence I
Medium

You are given an integer array nums and a non-negative integer k. A sequence of integers seq is called good if there are at most k indices i in the range [0, seq.length - 2] such that seq[i] != seq[i + 1].

Return the maximum possible length of a good subsequence of nums.


Example 1:

Input: nums = [1,2,1,1,3], k = 2
Output: 4
Explanation:
The maximum length subsequence is [1,2,1,1,3].

Example 2:

Input: nums = [1,2,3,4,5,1], k = 0
Output: 2
Explanation:
The maximum length subsequence is [1,2,3,4,5,1].


Constraints:

1 <= nums.length <= 500
1 <= nums[i] <= 10^9
0 <= k <= min(nums.length, 25)

"""

# V0
# IDEA : DP OVER (LAST INDEX TAKEN, CHANGES SPENT)
#
#   dp[i][j] = longest good subsequence that ENDS at index i having used j
#   "seq[t] != seq[t+1]" transitions.
#
#   extending from an earlier index t costs a change only when the values
#   differ :
#       nums[t] == nums[i]  ->  dp[i][j] = max(dp[i][j], dp[t][j] + 1)
#       nums[t] != nums[i]  ->  dp[i][j] = max(dp[i][j], dp[t][j-1] + 1)
#
#   n is only 500 and k at most 25, so the O(n^2 * k) table is about 6
#   million steps. the sequel (LC 3177) raises n to 5000 and needs the
#   per-value trick that drops the inner loop.
#
# time = O(n^2 * k), space = O(n * k)
class Solution(object):
    def maximumLength(self, nums, k):
        n = len(nums)
        dp = [[1] * (k + 1) for _ in range(n)]
        res = 1
        for i in range(n):
            for t in range(i):
                if nums[t] == nums[i]:
                    for j in range(k + 1):
                        if dp[t][j] + 1 > dp[i][j]:
                            dp[i][j] = dp[t][j] + 1
                else:
                    for j in range(1, k + 1):
                        if dp[t][j - 1] + 1 > dp[i][j]:
                            dp[i][j] = dp[t][j - 1] + 1
            res = max(res, max(dp[i]))
        return res
