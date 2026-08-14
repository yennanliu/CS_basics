"""

1685. Sum of Absolute Differences in a Sorted Array
Medium

You are given an integer array nums sorted in non-decreasing order.

Build and return an integer array result with the same length as nums such that result[i] is equal
to the summation of absolute differences between nums[i] and all the other elements in the array.

In other words, result[i] is equal to sum(|nums[i]-nums[j]|) where 0 <= j < nums.length and j != i
(0-indexed).


Example 1:

Input: nums = [2,3,5]
Output: [4,3,5]
Explanation: Assuming the arrays are 0-indexed, then
result[0] = |2-2| + |2-3| + |2-5| = 0 + 1 + 3 = 4,
result[1] = |3-2| + |3-3| + |3-5| = 1 + 0 + 2 = 3,
result[2] = |5-2| + |5-3| + |5-5| = 3 + 2 + 0 = 5.

Example 2:

Input: nums = [1,4,6,8,10]
Output: [24,15,13,15,21]


Constraints:

2 <= nums.length <= 10^5
1 <= nums[i] <= nums[i + 1] <= 10^4

"""

# V0
# IDEA : PREFIX SUMS (sorted input kills the absolute value)
#
#   because nums is sorted, for index i :
#     every j < i has nums[j] <= nums[i]  -> |diff| = nums[i] - nums[j]
#     every j > i has nums[j] >= nums[i]  -> |diff| = nums[j] - nums[i]
#
#   so with pre = sum(nums[0..i-1]) and suf = sum(nums[i+1..n-1]) :
#     result[i] = (i * nums[i] - pre) + (suf - (n - 1 - i) * nums[i])
#
#   one left-to-right sweep maintains pre and suf in O(1) each step.
#
# time = O(n), space = O(1) extra (excluding the output)
class Solution(object):
    def getSumAbsoluteDifferences(self, nums):
        n = len(nums)
        total = sum(nums)
        pre = 0
        res = []
        for i, v in enumerate(nums):
            suf = total - pre - v
            res.append((i * v - pre) + (suf - (n - 1 - i) * v))
            pre += v
        return res
