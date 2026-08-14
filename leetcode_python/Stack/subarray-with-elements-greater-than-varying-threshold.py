"""

2334. Subarray With Elements Greater Than Varying Threshold
Hard

You are given an integer array nums and an integer threshold.

Find any subarray of nums of length k such that every element in the subarray is greater than threshold / k.

Return the size of any such subarray. If there is no such subarray, return -1.

A subarray is a contiguous non-empty sequence of elements within an array.


Example 1:

Input: nums = [1,3,4,3,1], threshold = 6
Output: 3
Explanation: The subarray [3,4,3] has a size of 3, and every element is greater than 6 / 3 = 2.
Note that this is the only valid subarray.

Example 2:

Input: nums = [6,5,6,5,8], threshold = 7
Output: 1
Explanation: The subarray [8] has a size of 1, and 8 > 7 / 1 = 7. So 1 is returned.
Note that the subarray [6,5] has a size of 2, and every element is greater than 7 / 2 = 3.5.
Similarly, the subarrays [6,5,6], [6,5,6,5], [6,5,6,5,8] also satisfy the given conditions.
Therefore, 2, 3, 4, or 5 may also be returned.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i], threshold <= 10^9

"""

# V0
# IDEA : "EVERY ELEMENT > threshold / k" IS REALLY "THE MINIMUM > threshold / k"
#
#   so it is enough to consider, for each index i, the WIDEST window in which
#   nums[i] is the minimum — bounded by the previous and next strictly
#   smaller elements. a monotonic stack finds both in one pass.
#
#   with that window of length  len = right[i] - left[i] - 1, the condition
#       nums[i] > threshold / len
#   is checked as  nums[i] * len > threshold  to stay in exact integers.
#
#   testing only these maximal windows is sufficient : any valid subarray is
#   contained in the maximal window of its own minimum, and shrinking a
#   window only makes the required bound larger.
#
# time = O(n), space = O(n)
class Solution(object):
    def validSubarraySize(self, nums, threshold):
        n = len(nums)

        left = [-1] * n            # previous strictly smaller
        stack = []
        for i in range(n):
            while stack and nums[stack[-1]] >= nums[i]:
                stack.pop()
            left[i] = stack[-1] if stack else -1
            stack.append(i)

        right = [n] * n            # next strictly smaller
        stack = []
        for i in range(n - 1, -1, -1):
            while stack and nums[stack[-1]] >= nums[i]:
                stack.pop()
            right[i] = stack[-1] if stack else n
            stack.append(i)

        for i in range(n):
            width = right[i] - left[i] - 1
            if nums[i] * width > threshold:
                return width
        return -1
