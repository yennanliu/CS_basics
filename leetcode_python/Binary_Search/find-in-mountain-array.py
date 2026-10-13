"""

1095. Find in Mountain Array
Hard

(This problem is an interactive problem.)

You may recall that an array arr is a mountain array if and only if:

arr.length >= 3
There exists some i with 0 < i < arr.length - 1 such that:
arr[0] < arr[1] < ... < arr[i - 1] < arr[i]
arr[i] > arr[i + 1] > ... > arr[arr.length - 1]

Given a mountain array mountainArr, return the minimum index such that
mountainArr.get(index) == target. If such an index does not exist, return -1.

You cannot access the mountain array directly. You may only access the array
using a MountainArray interface:

MountainArray.get(k) returns the element of the array at index k (0-indexed).
MountainArray.length() returns the length of the array.

Submissions making more than 100 calls to MountainArray.get will be judged
Wrong Answer. Also, any solutions that attempt to circumvent the judge will
result in disqualification.

Example 1:

Input: mountainArr = [1,2,3,4,5,3,1], target = 3

Output: 2

Explanation:

3 exists in the array, at index=2 and index=5. Return the minimum index, which is 2.

Example 2:

Input: mountainArr = [0,1,2,4,2,1], target = 3

Output: -1

Explanation:

3 does not exist in the array, so we return -1.

Constraints:

3 <= mountainArr.length() <= 10^4
0 <= target <= 10^9
0 <= mountainArr.get(index) <= 10^9

"""

# """
# This is MountainArray's API interface.
# You should not implement it, or speculate about its implementation
# """
# class MountainArray(object):
#    def get(self, index):
#        """
#        :type index: int
#        :rtype int
#        """
#
#    def length(self):
#        """
#        :rtype int
#        """

# V0
# IDEA : BINARY SEARCH x 3 (FIND PEAK -> SEARCH ASCENDING SIDE -> SEARCH DESCENDING SIDE)
#
#   A mountain is two sorted arrays glued at the peak: the left side is strictly
#   increasing, the right side strictly decreasing. So:
#     1) find the peak (LC 852): if arr[mid] < arr[mid + 1] we are on the
#        uphill slope and the peak is to the right, else it is mid or left of it
#     2) plain binary search on [0, peak] (ascending)
#     3) only if not found, binary search on [peak + 1, n - 1] (descending)
#   Searching the LEFT side first is what makes the answer the MINIMUM index.
#
#   e.g. [1,2,3,4,5,3,1], target 3 -> peak = 4 -> left side [1,2,3,4,5] finds idx 2
#        (idx 5 on the right is never reached)
#
#   Call budget: 3 searches * ~log2(10^4) ~ 14 steps, <= 2 gets per peak step
#   -> well under 100. A linear scan would need up to 10^4 gets -> Wrong Answer.
#
# time = O(log n), space = O(1)
class Solution(object):
    def findInMountainArray(self, target, mountain_arr):
        """
        :type target: integer
        :type mountain_arr: MountainArray
        :rtype: integer
        """
        n = mountain_arr.length()

        peak = self.find_peak(mountain_arr, n)

        # left side first: any hit here is smaller than any hit on the right
        idx = self.search_ascending(mountain_arr, target, 0, peak)
        if idx != -1:
            return idx

        return self.search_descending(mountain_arr, target, peak + 1, n - 1)

    def find_peak(self, mountain_arr, n):
        """Return the index of the mountain's peak."""
        # the peak is never at either end, so search [1, n - 2]
        left = 1
        right = n - 2
        # invariant: the peak is always inside [left, right]
        while left < right:
            mid = (left + right) // 2
            # NOTE !!! compare with mid + 1 (never out of range, since mid < right <= n - 2):
            #          still climbing -> the peak is strictly to the right of mid
            if mountain_arr.get(mid) < mountain_arr.get(mid + 1):
                left = mid + 1
            # going downhill -> mid itself may be the peak, so keep it
            else:
                right = mid
        return left

    def search_ascending(self, mountain_arr, target, left, right):
        """Standard binary search on the increasing slice [left, right]."""
        while left <= right:
            mid = (left + right) // 2
            val = mountain_arr.get(mid)
            if val == target:
                return mid
            elif val < target:
                # target is bigger -> it lies to the right on an uphill slope
                left = mid + 1
            else:
                right = mid - 1
        return -1

    def search_descending(self, mountain_arr, target, left, right):
        """Binary search on the decreasing slice [left, right] (comparisons flipped)."""
        while left <= right:
            mid = (left + right) // 2
            val = mountain_arr.get(mid)
            if val == target:
                return mid
            elif val > target:
                # values shrink going right -> a smaller target lies to the right
                left = mid + 1
            else:
                right = mid - 1
        return -1
