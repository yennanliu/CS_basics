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


# V0-1
# IDEA : BRUTE FORCE — UN-ROTATE BY EVERY OFFSET AND TEST FOR NON DECREASING
#
#   n <= 100, so we can just try each of the n candidate originals,
#   nums[x:] + nums[:x], and accept if any one of them is sorted.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def check(self, nums):
        n = len(nums)
        for x in range(n):
            rotated = nums[x:] + nums[:x]
            if all(rotated[i] <= rotated[i + 1] for i in range(n - 1)):
                return True
        return False


# V0-2
# IDEA : nums MUST BE A LENGTH n WINDOW OF sorted(nums) CONCATENATED TWICE
#
#   every rotation of a sorted array S appears as a length n window of S + S,
#   and every length n window of S + S is a rotation of S — so the two are
#   equivalent. sort a copy, double it, and search for nums as a sublist.
#   duplicates need no special casing : sorting pins the multiset.
#
# time = O(n^2), space = O(n)
class Solution(object):
    def check(self, nums):
        n = len(nums)
        doubled = sorted(nums) * 2
        return any(doubled[i:i + n] == nums for i in range(n))
