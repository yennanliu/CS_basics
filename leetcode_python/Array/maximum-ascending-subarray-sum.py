"""

1800. Maximum Ascending Subarray Sum
Easy

Given an array of positive integers nums, return the maximum possible sum of an strictly increasing subarray in nums.

A subarray is defined as a contiguous sequence of numbers in an array.

Example 1:

Input: nums = [10,20,30,5,10,50]
Output: 65
Explanation: [5,10,50] is the ascending subarray with the maximum sum of 65.

Example 2:

Input: nums = [10,20,30,40,50]
Output: 150
Explanation: [10,20,30,40,50] is the ascending subarray with the maximum sum of 150.

Example 3:

Input: nums = [12,17,15,13,10,11,12]
Output: 33
Explanation: [10,11,12] is the ascending subarray with the maximum sum of 33.

Constraints:

1 <= nums.length <= 100
1 <= nums[i] <= 100

"""

# V0
# IDEA : RUNNING SUM OF THE CURRENT ASCENDING RUN
#
#   keep the sum of the ascending run that ends at i. when nums[i] > nums[i-1]
#   the run continues, otherwise a new run starts at nums[i].
#   the answer is the largest run sum ever seen.
#
# time = O(n), space = O(1)
class Solution(object):
    def maxAscendingSum(self, nums):
        res = cur = 0
        for i in range(len(nums)):
            if i > 0 and nums[i] > nums[i - 1]:
                cur += nums[i]
            else:
                cur = nums[i]
            res = max(res, cur)
        return res
