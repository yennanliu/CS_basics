"""

2832. Maximal Range That Each Element Is Maximum in It
Medium

You are given a 0-indexed array nums of distinct integers.

Let us define a 0-indexed array ans of the same length as nums in the following way:

ans[i] is the maximum length of a subarray nums[l..r], such that the maximum element in that subarray is equal to nums[i].

Return the array ans.

Note that a subarray is a contiguous part of the array.


Example 1:

Input: nums = [1,5,4,3,6]
Output: [1,4,2,1,5]
Explanation: For nums[0] the longest subarray in which 1 is the maximum is nums[0..0] so ans[0] = 1.
For nums[1] the longest subarray in which 5 is the maximum is nums[0..3] so ans[1] = 4.
For nums[2] the longest subarray in which 4 is the maximum is nums[2..3] so ans[2] = 2.
For nums[3] the longest subarray in which 3 is the maximum is nums[3..3] so ans[3] = 1.
For nums[4] the longest subarray in which 6 is the maximum is nums[0..4] so ans[4] = 5.

Example 2:

Input: nums = [1,2,3,4,5]
Output: [1,2,3,4,5]
Explanation: For nums[i] the longest subarray in which it's the maximum is nums[0..i] so ans[i] = i + 1.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^5
All elements in nums are distinct.

"""

# V0
# IDEA : MONOTONIC STACK (previous greater / next greater element)
#
#   The widest window in which nums[i] is the maximum stretches from just
#   after the previous strictly-greater element to just before the next
#   strictly-greater one. Call those boundaries left[i] and right[i]
#   (-1 and n when none exists); then
#
#       ans[i] = right[i] - left[i] - 1
#
#   NOTE : nums[i] must be the max of the window, so the window may freely
#          contain SMALLER elements — it is blocked only by greater ones.
#          Since all values are distinct, "greater" and "greater or equal"
#          coincide and there is no tie-breaking subtlety to worry about.
#
#   Both boundary arrays come from one decreasing monotonic stack each:
#   scan left to right for the previous greater element, right to left for
#   the next greater element. Every index is pushed and popped once.
#
#   NOTE : iterative and O(n) — n reaches 10^5, so no recursion.
#
# time = O(n), space = O(n)
class Solution(object):
    def maximumLengthOfRanges(self, nums):
        n = len(nums)
        left = [-1] * n   # index of previous greater element
        right = [n] * n   # index of next greater element

        stack = []
        for i in range(n):
            while stack and nums[stack[-1]] < nums[i]:
                stack.pop()
            if stack:
                left[i] = stack[-1]
            stack.append(i)

        stack = []
        for i in range(n - 1, -1, -1):
            while stack and nums[stack[-1]] < nums[i]:
                stack.pop()
            if stack:
                right[i] = stack[-1]
            stack.append(i)

        return [right[i] - left[i] - 1 for i in range(n)]
