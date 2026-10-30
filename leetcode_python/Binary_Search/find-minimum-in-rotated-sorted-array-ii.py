"""

154. Find Minimum in Rotated Sorted Array II
Hard

Suppose an array of length n sorted in ascending order is rotated between 1
and n times. For example, the array nums = [0,1,4,4,5,6,7] might become:

[4,5,6,7,0,1,4] if it was rotated 4 times.
[0,1,4,4,5,6,7] if it was rotated 7 times.

Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results
in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].

Given the sorted rotated array nums that may contain duplicates, return the
minimum element of this array.

You must decrease the overall operation steps as much as possible.

Example 1:

Input: nums = [1,3,5]

Output: 1

Example 2:

Input: nums = [2,2,2,0,1]

Output: 0

Constraints:

n == nums.length
1 <= n <= 5000
-5000 <= nums[i] <= 5000
nums is sorted and rotated between 1 and n times.

Follow up: This problem is similar to Find Minimum in Rotated Sorted Array,
but nums may contain duplicates. Would this affect the runtime complexity?
How and why?

"""

# V0
# IDEA : BINARY SEARCH, COMPARE nums[mid] WITH nums[right] (LC 153 + DUPLICATES)
#
#   Invariant: the minimum is always inside [left, right].
#
#   - nums[mid] > nums[right] -> the rotation point is strictly right of mid,
#                                and mid itself cannot be the min -> left = mid + 1
#   - nums[mid] < nums[right] -> [mid, right] is sorted, so the min is at mid
#                                or left of it -> right = mid
#   - nums[mid] == nums[right] -> cannot tell which side the min is on
#                                (e.g. [3,1,3,3,3] vs [3,3,3,1,3]), but
#                                nums[right] has a copy at mid, so dropping
#                                right never loses the min value -> right -= 1
#
#   Follow up: the `==` branch only shrinks the range by one, so an array like
#   [1,1,1,1,1] degrades to O(n). Duplicates make O(log n) impossible in the
#   worst case.
#
#   Why compare with nums[right], not nums[left]: nums[mid] > nums[left] holds
#   both for [1,2,3,4,5] (min on the left) and [3,4,5,1,2] (min on the right),
#   so it does not say which half to drop.
#
#   e.g. [2,2,2,0,1]: l=0,r=4,mid=2: 2 > 1 -> l=3
#                     l=3,r=4,mid=3: 0 < 1 -> r=3  -> nums[3] = 0
#
# time = O(log n) average, O(n) worst case (all duplicates), space = O(1)
class Solution(object):
    def findMin(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        left = 0
        right = len(nums) - 1

        # stop when one candidate is left: left == right is the min
        while left < right:
            mid = left + (right - left) // 2

            if nums[mid] > nums[right]:
                # the drop happens after mid -> min is in (mid, right]
                left = mid + 1
            elif nums[mid] < nums[right]:
                # [mid, right] is ascending -> min is in [left, mid]
                right = mid
            else:
                # NOTE !!! nums[mid] == nums[right]: side unknown, but
                #          nums[mid] keeps a copy of nums[right]'s value,
                #          so it is safe to drop just the right end
                right -= 1

        return nums[left]
