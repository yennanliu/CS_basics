"""

1696. Jump Game VI
Medium

You are given a 0-indexed integer array nums and an integer k.

You are initially standing at index 0. In one move, you can jump at most k steps forward without
going outside the boundaries of the array. That is, you can jump from index i to any index in the
range [i + 1, min(n - 1, i + k)] inclusive.

You want to reach the last index of the array (index n - 1). Your score is the sum of all nums[j]
for each index j you visited in the array.

Return the maximum score you can get.


Example 1:

Input: nums = [1,-1,-2,4,-7,3], k = 2
Output: 7
Explanation: You can choose your jumps forming the subsequence [1,-1,4,3]. The sum is 7.

Example 2:

Input: nums = [10,-5,-2,4,0,3], k = 3
Output: 17
Explanation: You can choose your jumps forming the subsequence [10,4,3]. The sum is 17.

Example 3:

Input: nums = [1,-5,-20,4,-1,3,-6,-3], k = 2
Output: 0


Constraints:

1 <= nums.length, k <= 10^5
-10^4 <= nums[i] <= 10^4

"""

# V0
# IDEA : DP + MONOTONIC DEQUE (sliding-window MAXIMUM over the last k states)
#
#   dp[i] = nums[i] + max(dp[i-k] ... dp[i-1])
#
#   the naive max scan is O(nk); instead keep a deque of indices whose dp values
#   are strictly DECREASING:
#     - front = the best reachable predecessor, pop it once it falls out of the
#       window (i - front > k)
#     - before pushing i, pop every tail with dp <= dp[i]: it is both older and
#       worse, so it can never be the window max again
#
#   NOTE : values may be negative, so we cannot greedily jump to the max -- every
#          landing spot's score is forced, which is what makes this a DP.
#
# time = O(n), space = O(n)
from collections import deque
class Solution(object):
    def maxResult(self, nums, k):
        n = len(nums)
        dp = [0] * n
        dp[0] = nums[0]
        dq = deque([0])
        for i in range(1, n):
            while dq and i - dq[0] > k:
                dq.popleft()
            dp[i] = nums[i] + dp[dq[0]]
            while dq and dp[dq[-1]] <= dp[i]:
                dq.pop()
            dq.append(i)
        return dp[n - 1]
