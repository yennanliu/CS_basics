"""

1909. Remove One Element to Make the Array Strictly Increasing
Easy

Given a 0-indexed integer array nums, return true if it can be made strictly increasing after removing exactly one element, or false otherwise. If the array is already strictly increasing, return true.

The array nums is strictly increasing if nums[i - 1] < nums[i] for each index (1 <= i < nums.length).


Example 1:

Input: nums = [1,2,10,5,7]
Output: true
Explanation: By removing 10 at index 2 from nums, it becomes [1,2,5,7].
[1,2,5,7] is strictly increasing, so return true.

Example 2:

Input: nums = [2,3,1,2]
Output: false
Explanation:
[3,1,2] is the result of removing the element at index 0.
[2,1,2] is the result of removing the element at index 1.
[2,3,2] is the result of removing the element at index 2.
[2,3,1] is the result of removing the element at index 3.
No resulting array is strictly increasing, so return false.

Example 3:

Input: nums = [1,1,1]
Output: false
Explanation: The result of removing any element is [1,1].
[1,1] is not strictly increasing, so return false.


Constraints:

2 <= nums.length <= 1000
1 <= nums[i] <= 1000

"""

# V0
# IDEA : GREEDY SCAN (only the FIRST bad pair matters)
#
#   walk until the first index i with nums[i] >= nums[i+1].
#   if there is none, the array is already strictly increasing -> True.
#   otherwise the removed element MUST be one of i / i+1 (removing anything
#   else leaves that bad pair adjacent), so we only need 2 checks.
#
#   NOTE : we must "remove exactly one" element, but if the array is already
#          increasing, dropping the last element keeps it increasing -> True.
#
# time = O(n), space = O(1)
class Solution(object):
    def canBeIncreasing(self, nums):
        def check(k):
            # is nums strictly increasing when index k is skipped ?
            prev = None
            for i in range(len(nums)):
                if i == k:
                    continue
                if prev is not None and prev >= nums[i]:
                    return False
                prev = nums[i]
            return True

        i = 0
        while i + 1 < len(nums) and nums[i] < nums[i + 1]:
            i += 1
        return check(i) or check(i + 1)
