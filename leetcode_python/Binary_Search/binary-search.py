"""

704. Binary Search
Easy

Given an array of integers nums which is sorted in ascending order, and an integer
target, write a function to search target in nums. If target exists, then return
its index. Otherwise, return -1.

You must write an algorithm with O(log n) runtime complexity.


Example 1:

Input: nums = [-1,0,3,5,9,12], target = 9
Output: 4
Explanation: 9 exists in nums and its index is 4

Example 2:

Input: nums = [-1,0,3,5,9,12], target = 2
Output: -1
Explanation: 2 does not exist in nums so return -1


Constraints:

1 <= nums.length <= 10^4
-10^4 < nums[i], target < 10^4
All the integers in nums are unique.
nums is sorted in ascending order.

"""

# V0
# IDEA : BINARY SEARCH (the textbook closed-interval template)
#
#   the invariant is : if target is in nums at all, it is inside nums[l..r].
#   every step throws away the half that cannot hold it, so the interval
#   shrinks to nothing only when target is genuinely absent.
#
#   e.g. nums = [-1,0,3,5,9,12], target = 9
#        l=0 r=5 mid=2 (3)  -> 3 < 9  -> l = 3
#        l=3 r=5 mid=4 (9)  -> hit    -> 4
#
#   NOTE !!! `while l <= r` (not `<`) together with `r = mid - 1` : with a
#            half-open loop the single-element interval l == r is never tested
#            and a target sitting there is reported missing.
#
# time = O(log n), space = O(1)
class Solution(object):
    def search(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        # edge
        if not nums:
            return -1

        l, r = 0, len(nums) - 1
        while l <= r:
            # written this way (not (l + r) // 2) so it cannot overflow in
            # languages with fixed-width ints -- the habit is the point
            mid = l + (r - l) // 2
            if nums[mid] == target:
                return mid
            elif nums[mid] < target:
                l = mid + 1
            else:
                r = mid - 1

        return -1
