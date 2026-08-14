"""

1425. Constrained Subsequence Sum
Hard

Given an integer array nums and an integer k, return the maximum sum of a
non-empty subsequence of that array such that for every two consecutive
integers in the subsequence, nums[i] and nums[j], where i < j, the condition
j - i <= k is satisfied.

A subsequence of an array is obtained by deleting some number of elements
(can be zero) from the array, leaving the remaining elements in their
original order.


Example 1:

Input: nums = [10,2,-10,5,20], k = 2
Output: 37
Explanation: The subsequence is [10, 2, 5, 20].

Example 2:

Input: nums = [-1,-2,-3], k = 1
Output: -1
Explanation: The subsequence must be non-empty, so we choose the largest number.

Example 3:

Input: nums = [10,-2,-10,-5,20], k = 2
Output: 23
Explanation: The subsequence is [10, -2, -5, 20].


Constraints:

1 <= k <= nums.length <= 10^5
-10^4 <= nums[i] <= 10^4

"""

# V0
# IDEA : DP + MONOTONIC DEQUE (sliding window maximum)
#
#  DP def:
#     - dp[i] = max sum of a valid subsequence that ENDS at index i
#
#  DP eq:
#     - dp[i] = nums[i] + max(0, max(dp[i-k] ... dp[i-1]))
#       (the `max(0, ...)` means "start a fresh subsequence at i")
#
#  -> the inner max is a sliding window maximum of width k,
#     so keep a deque of indices whose dp values are DECREASING
#
# time = O(n)
# space = O(n)
from collections import deque
class Solution(object):
    def constrainedSubsetSum(self, nums, k):
        n = len(nums)
        dp = [0] * n
        # dq holds indices, dp[dq[0]] is the window max
        dq = deque()
        res = float('-inf')

        for i in range(n):
            # NOTE !!! drop indices that fell out of the window [i-k, i-1]
            while dq and dq[0] < i - k:
                dq.popleft()

            best_prev = dp[dq[0]] if dq else 0
            dp[i] = nums[i] + max(0, best_prev)
            res = max(res, dp[i])

            # keep the deque decreasing on dp value
            while dq and dp[dq[-1]] <= dp[i]:
                dq.pop()
            dq.append(i)

        return res


# V1
# IDEA : DP + MAX HEAP (lazy deletion)
#
#  -> same recurrence, but the window max comes from a heap;
#     stale entries (index < i - k) are popped lazily
#
# time = O(n log n)
# space = O(n)
import heapq
class Solution(object):
    def constrainedSubsetSum(self, nums, k):
        n = len(nums)
        dp = [0] * n
        heap = []  # (-dp[j], j)
        res = float('-inf')

        for i in range(n):
            while heap and heap[0][1] < i - k:
                heapq.heappop(heap)

            best_prev = -heap[0][0] if heap else 0
            dp[i] = nums[i] + max(0, best_prev)
            res = max(res, dp[i])
            heapq.heappush(heap, (-dp[i], i))

        return res
