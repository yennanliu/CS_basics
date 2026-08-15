"""

3346. Maximum Frequency of an Element After Performing Operations I
Medium

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
1 <= nums[i] <= 10^5
0 <= k <= 10^5
0 <= numOperations <= nums.length


"""

# V0
# IDEA : FIX THE TARGET VALUE — THEN IT IS "ALREADY THERE" PLUS "CAN BE NUDGED"
#
#   for a chosen target v the final frequency is
#       (elements already equal to v)  +  min(numOperations, elements within
#                                             k of v that are not already v)
#   because an element in [v-k, v+k] can be moved onto v with one operation,
#   and only numOperations of them may be moved.
#
#   the best v is always some nums[i] or a boundary nums[i] +- k — moving the
#   target between those points never adds anybody — so those are the only
#   candidates worth testing.
#
#   sorting once turns "how many lie in [v-k, v+k]" into two binary searches,
#   and "how many equal v" into another pair.
#
# time = O(n log n), space = O(n)
import bisect


class Solution(object):
    def maxFrequency(self, nums, k, numOperations):
        arr = sorted(nums)
        n = len(arr)

        candidates = set()
        for v in arr:
            candidates.add(v)
            candidates.add(v - k)
            candidates.add(v + k)

        best = 0
        for v in candidates:
            lo = bisect.bisect_left(arr, v - k)
            hi = bisect.bisect_right(arr, v + k)
            inside = hi - lo
            same = bisect.bisect_right(arr, v) - bisect.bisect_left(arr, v)
            movable = inside - same
            total = same + min(numOperations, movable)
            if total > best:
                best = total
        return best
