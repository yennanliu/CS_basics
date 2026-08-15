"""

2733. Neither Minimum nor Maximum
Easy

Given an integer array nums containing distinct positive integers, find and return any number from the array that is neither the minimum nor the maximum value in the array, or -1 if there is no such number.

Return the selected integer.


Example 1:

Input: nums = [3,2,1,4]
Output: 2
Explanation: In this example, the minimum value is 1 and the maximum value is 4. Therefore, either 2 or 3 can be valid answers.

Example 2:

Input: nums = [1,2]
Output: -1
Explanation: Since there is no number in nums that is neither the maximum nor the minimum, we cannot select a number that satisfies the given condition. Therefore, there is no answer.

Example 3:

Input: nums = [2,1,3]
Output: 2
Explanation: Since 2 is neither the maximum nor the minimum value in nums, it is the only valid answer.


Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 100
All values in nums are distinct

"""

# V0
# IDEA : MIN / MAX THEN FIRST SURVIVOR
#
#   grab the global min and max, then return the first element that is
#   neither. Since the values are DISTINCT, exactly one element equals the min
#   and one equals the max, so any survivor is a valid answer.
#
#   NOTE : len(nums) <= 2 has no survivor -> -1 falls out naturally (for
#          len == 1 the single element is both the min and the max).
#
# time = O(n), space = O(1)
class Solution(object):
    def findNonMinOrMax(self, nums):
        lo, hi = min(nums), max(nums)
        for x in nums:
            if x != lo and x != hi:
                return x
        return -1
