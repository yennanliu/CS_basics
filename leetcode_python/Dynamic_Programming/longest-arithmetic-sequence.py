"""

1027. Longest Arithmetic Subsequence
Medium

Given an array nums of integers, return the length of the longest arithmetic subsequence in nums.

Note that:

A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.
A sequence seq is arithmetic if seq[i + 1] - seq[i] are all the same value (for 0 <= i < seq.length - 1).


Example 1:

Input: nums = [3,6,9,12]
Output: 4
Explanation: The whole array is an arithmetic sequence with steps of length = 3.

Example 2:

Input: nums = [9,4,7,2,10]
Output: 3
Explanation: The longest arithmetic subsequence is [4,7,10].

Example 3:

Input: nums = [20,1,15,3,10,5,8]
Output: 4
Explanation: The longest arithmetic subsequence is [20,15,10,5].


Constraints:

2 <= nums.length <= 1000
0 <= nums[i] <= 500

"""

# V0
# IDEA : 2D DP over (end index, common difference)
"""

 DP def:
    - dp[i][d] = length of the longest arithmetic subsequence
                 that ENDS at index i and has common difference d

 DP eq:
    - d = nums[i] - nums[j]  for every j < i
    - dp[i][d] = dp[j][d] + 1   (extend the run ending at j)
                 or 2           (start a fresh pair nums[j], nums[i])

 note : d can be negative, so use a dict per index instead of an array
"""
# time = O(n^2)
# space = O(n^2)
class Solution(object):
    def longestArithSeqLength(self, nums):
        n = len(nums)
        if n <= 2:
            return n

        dp = [dict() for _ in range(n)]
        res = 2
        for i in range(1, n):
            for j in range(i):
                d = nums[i] - nums[j]
                # dp[j].get(d, 1) -> if j has no run with diff d,
                # treat (nums[j],) as a run of length 1, so we get 2 here
                dp[i][d] = max(dp[i].get(d, 0), dp[j].get(d, 1) + 1)
                res = max(res, dp[i][d])
        return res
