"""

3347. Maximum Frequency of an Element After Performing Operations II
Hard

You are given an integer array nums and two integers k and numOperations.

You must perform an operation numOperations times on nums, where in each operation you:

Select an index i that was not selected in any previous operations.
Add an integer in the range [-k, k] to nums[i].

Return the maximum possible frequency of any element in nums after performing the operations.


Example 1:

Input: nums = [1,4,5], k = 1, numOperations = 2
Output: 2
Explanation:
We can achieve a maximum frequency of two by:
Adding 0 to nums[1], after which nums becomes [1, 4, 5].
Adding -1 to nums[2], after which nums becomes [1, 4, 4].

Example 2:

Input: nums = [5,11,20,20], k = 5, numOperations = 1
Output: 2
Explanation:
We can achieve a maximum frequency of two by:
Adding 0 to nums[1].


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^9
0 <= k <= 10^9
0 <= numOperations <= nums.length

"""

# V0
# IDEA : SAME CANDIDATE ARGUMENT AS LC 3346 — THE VALUES JUST GET BIGGER
#
#   for a target v the reachable frequency is
#       (count already equal to v) + min(numOperations, others within k)
#   and the optimum is attained at some nums[i] or at a window edge
#   nums[i] +- k, so only 3n candidates need testing.
#
#   the sequel lifts nums[i] and k to 10^9, which rules out any array indexed
#   by value; sorting plus binary search stays value-agnostic and is what
#   makes it scale.
#
# time = O(n log n), space = O(n)
import bisect


class Solution(object):
    def maxFrequency(self, nums, k, numOperations):
        arr = sorted(nums)

        candidates = set()
        for v in arr:
            candidates.add(v)
            candidates.add(v - k)
            candidates.add(v + k)

        best = 0
        for v in candidates:
            lo = bisect.bisect_left(arr, v - k)
            hi = bisect.bisect_right(arr, v + k)
            same = bisect.bisect_right(arr, v) - bisect.bisect_left(arr, v)
            movable = (hi - lo) - same
            total = same + min(numOperations, movable)
            if total > best:
                best = total
        return best
