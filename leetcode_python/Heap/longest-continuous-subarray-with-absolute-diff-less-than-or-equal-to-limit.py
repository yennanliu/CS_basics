"""

1438. Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit
Medium

Given an array of integers nums and an integer limit, return the size of the
longest non-empty subarray such that the absolute difference between any two
elements of this subarray is less than or equal to limit.

Example 1:

Input: nums = [8,2,4,7], limit = 4

Output: 2

Explanation: All subarrays are:
[8] with maximum absolute diff |8-8| = 0 <= 4.
[8,2] with maximum absolute diff |8-2| = 6 > 4.
[8,2,4] with maximum absolute diff |8-2| = 6 > 4.
[8,2,4,7] with maximum absolute diff |8-2| = 6 > 4.
[2] with maximum absolute diff |2-2| = 0 <= 4.
[2,4] with maximum absolute diff |2-4| = 2 <= 4.
[2,4,7] with maximum absolute diff |2-7| = 5 > 4.
[4] with maximum absolute diff |4-4| = 0 <= 4.
[4,7] with maximum absolute diff |4-7| = 3 <= 4.
[7] with maximum absolute diff |7-7| = 0 <= 4.
Therefore, the size of the longest subarray is 2.

Example 2:

Input: nums = [10,1,2,4,7,2], limit = 5

Output: 4

Explanation: The subarray [2,4,7,2] is the longest since the maximum absolute
diff is |2-7| = 5 <= 5.

Example 3:

Input: nums = [4,2,2,2,4,4,2,2], limit = 0

Output: 3

Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
0 <= limit <= 10^9

"""

# V0
class Solution(object):
    def longestSubarray(self, nums, limit):
        """
        :type nums: List[int]
        :type limit: int
        :rtype: int
        """
        pass


# V0-1
# IDEA : SLIDE WINDOW + 2 MONOTONIC DEQUES (max deque decreasing, min deque increasing)
#
#   "every pair diff <= limit"  <=>  max(window) - min(window) <= limit,
#   so the window only needs its max and min, each in O(1) amortized.
#   max_q keeps a decreasing run of values: anything smaller than a newer
#   value can never be the window max again, so it is popped for good.
#   min_q is the mirror. Front of each deque = current max / min.
#
#   e.g. nums = [10,1,2,4,7,2], limit = 5
#        r=5 -> window [2,4,7,2], max_q = [7,2], min_q = [2,2] -> 7-2 = 5 ok -> len 4
#
# time = O(n), space = O(n)
from collections import deque


class Solution(object):
    def longestSubarray(self, nums, limit):
        """
        :type nums: List[int]
        :type limit: int
        :rtype: int
        """
        # edge
        if not nums:
            return 0

        max_q = deque()  # decreasing
        min_q = deque()  # increasing
        l = 0
        res = 0

        for r in range(len(nums)):
            x = nums[r]
            # NOTE !!! strict `<` / `>` : equal values must STAY, otherwise
            # moving `l` past one copy would drop the max/min still in the window
            while max_q and max_q[-1] < x:
                max_q.pop()
            max_q.append(x)
            while min_q and min_q[-1] > x:
                min_q.pop()
            min_q.append(x)

            # shrink until the window is valid again
            while max_q[0] - min_q[0] > limit:
                if nums[l] == max_q[0]:
                    max_q.popleft()
                if nums[l] == min_q[0]:
                    min_q.popleft()
                l += 1

            res = max(res, r - l + 1)

        return res


# V1
# IDEA : SLIDE WINDOW + MAX HEAP + MIN HEAP (lazy deletion by index)
#
#   Same window invariant, but max / min come from two heaps of (val, idx).
#   Entries whose idx < l are stale; they are only popped when they surface
#   at the top, which is the only time they could give a wrong answer.
#   When the window is invalid, the offending extreme is the older of the two
#   tops, so jump l just past it.
#
# time = O(n log n), space = O(n)
import heapq


class Solution2(object):
    def longestSubarray(self, nums, limit):
        """
        :type nums: List[int]
        :type limit: int
        :rtype: int
        """
        if not nums:
            return 0

        max_h = []  # (-val, idx)
        min_h = []  # (val, idx)
        l = 0
        res = 0

        for r, x in enumerate(nums):
            heapq.heappush(max_h, (-x, r))
            heapq.heappush(min_h, (x, r))

            while -max_h[0][0] - min_h[0][0] > limit:
                l = min(max_h[0][1], min_h[0][1]) + 1
                while max_h[0][1] < l:
                    heapq.heappop(max_h)
                while min_h[0][1] < l:
                    heapq.heappop(min_h)

            res = max(res, r - l + 1)

        return res
