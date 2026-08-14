"""

1752. Check if Array Is Sorted and Rotated
Easy

Given an array nums, return true if the array was originally sorted in non-decreasing order, then rotated some number of positions (including zero). Otherwise, return false.

There may be duplicates in the original array.

Note: An array A rotated by x positions results in an array B of the same length such that B[i] == A[(i+x) % A.length] for every valid index i.

Example 1:

Input: nums = [3,4,5,1,2]
Output: true
Explanation: [1,2,3,4,5] is the original sorted array.
You can rotate the array by x = 2 positions to begin on the element of value 3: [3,4,5,1,2].

Example 2:

Input: nums = [2,1,3,4]
Output: false
Explanation: There is no sorted array once rotated that can make nums.

Example 3:

Input: nums = [1,2,3]
Output: true
Explanation: [1,2,3] is the original sorted array.
You can rotate the array by x = 0 positions (i.e. no rotation) to make nums.

Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 100

"""

# V0
# IDEA : COUNT THE "DROPS" ON THE CIRCULAR ARRAY (a rotation has at most one)
#
#   a non-decreasing array has 0 indices i with nums[i - 1] > nums[i];
#   rotating it introduces exactly 1 (the rotation point). so the array is a
#   rotated sorted array iff the cyclic drop count is <= 1.
#   NOTE : the wrap-around pair (nums[-1], nums[0]) must be counted as well,
#          which python's negative indexing gives us for free at i = 0.
#
# time = O(n), space = O(1)
class Solution(object):
    def check(self, nums):
        drops = 0
        for i in range(len(nums)):
            if nums[i - 1] > nums[i]:
                drops += 1
        return drops <= 1
