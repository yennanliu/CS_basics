"""

2444. Count Subarrays With Fixed Bounds
Hard

You are given an integer array nums and two integers minK and maxK.

A fixed-bound subarray of nums is a subarray that satisfies the following conditions:

The minimum value in the subarray is equal to minK.
The maximum value in the subarray is equal to maxK.

Return the number of fixed-bound subarrays.

A subarray is a contiguous part of an array.


Example 1:

Input: nums = [1,3,5,2,7,5], minK = 1, maxK = 5
Output: 2
Explanation: The fixed-bound subarrays are [1,3,5] and [1,3,5,2].

Example 2:

Input: nums = [1,1,1,1], minK = 1, maxK = 1
Output: 10
Explanation: Every subarray of nums is a fixed-bound subarray. There are 10 possible subarrays.


Constraints:

2 <= nums.length <= 10^5
1 <= nums[i], minK, maxK <= 10^6

"""

# V0
# IDEA : THREE POINTERS (last bad index + last minK index + last maxK index)
#
#   fix the right end i. a subarray [l..i] is valid iff
#     - it contains no value outside [minK, maxK]  -> l > bad, the last index
#       holding an out-of-range value
#     - it contains at least one minK              -> l <= lastMin
#     - it contains at least one maxK              -> l <= lastMax
#   so the valid l's are (bad, min(lastMin, lastMax)] and their count is
#     max(0, min(lastMin, lastMax) - bad)
#
#   NOTE : all three pointers start at -1, which makes the empty / not-yet-seen
#          cases fall out for free.
#
# time = O(n), space = O(1)
class Solution(object):
    def countSubarrays(self, nums, minK, maxK):
        res = 0
        bad = last_min = last_max = -1
        for i, v in enumerate(nums):
            if v < minK or v > maxK:
                bad = i
            if v == minK:
                last_min = i
            if v == maxK:
                last_max = i
            cnt = min(last_min, last_max) - bad
            if cnt > 0:
                res += cnt
        return res
