"""

862. Shortest Subarray with Sum at Least K
Hard

Given an integer array nums and an integer k, return the length of the shortest
non-empty subarray of nums with a sum of at least k. If there is no such subarray,
return -1.

A subarray is a contiguous part of an array.


Example 1:

Input: nums = [1], k = 1
Output: 1

Example 2:

Input: nums = [1,2], k = 4
Output: -1

Example 3:

Input: nums = [2,-1,2], k = 3
Output: 3


Constraints:

1 <= nums.length <= 10^5
-10^5 <= nums[i] <= 10^5
1 <= k <= 10^9

"""

# V0
# IDEA : PREFIX SUM + MONOTONIC DEQUE
#
#   With negative numbers the classic sliding window breaks (shrinking the
#   window can INCREASE the sum), so we work on prefix sums instead:
#
#       sum(nums[j..i-1]) = prefix[i] - prefix[j]
#
#   We want the smallest (i - j) with prefix[i] - prefix[j] >= k.
#
#   Keep a deque of candidate indices j with STRICTLY INCREASING prefix values:
#
#     - pop from the BACK while prefix[back] >= prefix[i]:
#       a later index with a smaller-or-equal prefix is always at least as good
#       (shorter subarray AND bigger sum), so the older one is useless.
#
#     - pop from the FRONT while prefix[i] - prefix[front] >= k:
#       record the length; that front can never give a shorter answer for a
#       later i, so it is safe to discard.
#
#   Every index enters and leaves the deque at most once -> linear time.
#
# time  = O(n)
# space = O(n)
from collections import deque
class Solution(object):
    def shortestSubarray(self, nums, k):
        n = len(nums)

        # prefix[i] = sum of the first i elements
        prefix = [0] * (n + 1)
        for i, v in enumerate(nums):
            prefix[i + 1] = prefix[i] + v

        ans = n + 1
        q = deque()          # indices into prefix, prefix values increasing

        for i in range(n + 1):
            cur = prefix[i]

            # keep the deque increasing
            while q and prefix[q[-1]] >= cur:
                q.pop()

            # the front already satisfies the target -> shortest for this i
            while q and cur - prefix[q[0]] >= k:
                ans = min(ans, i - q.popleft())

            q.append(i)

        return ans if ans <= n else -1
