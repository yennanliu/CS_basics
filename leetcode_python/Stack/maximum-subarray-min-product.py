"""

1856. Maximum Subarray Min-Product
Medium

The min-product of an array is equal to the minimum value in the array multiplied by the array's sum.

For example, the array [3,2,5] (minimum value is 2) has a min-product of 2 * (3+2+5) = 2 * 10 = 20.

Given an array of integers nums, return the maximum min-product of any non-empty subarray of nums. Since the answer may be large, return it modulo 10^9 + 7.

Note that the min-product should be maximized before performing the modulo operation. Testcases are generated such that the maximum min-product without modulo will fit in a 64-bit signed integer.

A subarray is a contiguous part of an array.


Example 1:

Input: nums = [1,2,3,2]
Output: 14
Explanation: The maximum min-product is achieved with the subarray [2,3,2] (minimum value is 2).
2 * (2+3+2) = 2 * 7 = 14.

Example 2:

Input: nums = [2,3,3,1,2]
Output: 18
Explanation: The maximum min-product is achieved with the subarray [3,3] (minimum value is 3).
3 * (3+3) = 3 * 6 = 18.

Example 3:

Input: nums = [3,1,5,6,4,2]
Output: 60
Explanation: The maximum min-product is achieved with the subarray [5,6,4] (minimum value is 4).
4 * (5+6+4) = 4 * 15 = 60.


Constraints:

1 <= nums.length <= 10^5
1 <= nums[i] <= 10^7

"""

# V0
# IDEA : MONOTONIC STACK + PREFIX SUM (largest window where nums[i] is the min)
#
#   an optimal subarray is always MAXIMAL for its minimum : if nums[i] is the
#   minimum, extending left/right while neighbours are >= nums[i] only adds
#   positive numbers to the sum, so the answer is achieved on the widest
#   window in which nums[i] stays the minimum.
#
#   so for every i find :
#     left[i]  = index of previous element STRICTLY smaller  (-1 if none)
#     right[i] = index of next     element STRICTLY smaller  (n  if none)
#   candidate = nums[i] * (pre[right[i]] - pre[left[i] + 1])
#
#   NOTE : use ">=" when popping on one pass and ">" on the other, so equal
#          values are not double-counted / truncated.
#   NOTE : take the modulo only at the very END (problem says maximize first).
#
# time = O(n), space = O(n)
class Solution(object):
    def maxSumMinProduct(self, nums):
        MOD = 10 ** 9 + 7
        n = len(nums)

        left = [-1] * n
        stack = []
        for i in range(n):
            while stack and nums[stack[-1]] >= nums[i]:
                stack.pop()
            if stack:
                left[i] = stack[-1]
            stack.append(i)

        right = [n] * n
        stack = []
        for i in range(n - 1, -1, -1):
            while stack and nums[stack[-1]] > nums[i]:
                stack.pop()
            if stack:
                right[i] = stack[-1]
            stack.append(i)

        # pre[k] = sum of nums[0 .. k-1]
        pre = [0] * (n + 1)
        for i in range(n):
            pre[i + 1] = pre[i] + nums[i]

        res = 0
        for i in range(n):
            total = pre[right[i]] - pre[left[i] + 1]
            res = max(res, nums[i] * total)

        return res % MOD
