"""

3205. Maximum Array Hopping Score I
Medium
🔒 (premium)

Given an array nums, you have to get the maximum score starting from index 0 and hopping until you reach the last element of the array.

In each hop, you can jump from index i to an index j > i, and you get a score of (j - i) * nums[j].

Return the maximum score you can get.


Example 1:

Input: nums = [1,5,8]
Output: 16
Explanation:
There are two possible ways to reach the last element:
0 -> 1 -> 2 with a score of (1 - 0) * 5 + (2 - 1) * 8 = 13.
0 -> 2 with a score of (2 - 0) * 8 = 16.

Example 2:

Input: nums = [4,5,2,8,9,1,3]
Output: 42
Explanation:
We can do the hopping 0 -> 4 -> 6 with a score of (4 - 0) * 9 + (6 - 4) * 3 = 42.


Constraints:

2 <= nums.length <= 10^3
1 <= nums[i] <= 10^5

"""

# V0
# IDEA : PLAIN O(n^2) DP OVER THE LANDING INDEX
#
#   dp[i] = best score achievable when standing on index i, so
#       dp[i] = max over j < i of  dp[j] + (i - j) * nums[i]
#   and the answer is dp[n-1] since the walk must finish on the last element.
#
#   n is only 1000 here, so the quadratic scan is fine; the sequel (LC 3221)
#   raises it to 10^5 and needs the suffix-maximum greedy instead.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def maxScore(self, nums):
        n = len(nums)
        dp = [0] * n
        for i in range(1, n):
            best = float('-inf')
            for j in range(i):
                cand = dp[j] + (i - j) * nums[i]
                if cand > best:
                    best = cand
            dp[i] = best
        return dp[n - 1]
