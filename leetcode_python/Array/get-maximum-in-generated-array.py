"""

1646. Get Maximum in Generated Array
Easy

You are given an integer n. A 0-indexed integer array nums of length n + 1 is generated in the following way:

nums[0] = 0
nums[1] = 1
nums[2 * i] = nums[i] when 2 <= 2 * i <= n
nums[2 * i + 1] = nums[i] + nums[i + 1] when 2 <= 2 * i + 1 <= n

Return the maximum integer in the array nums.


Example 1:

Input: n = 7
Output: 3
Explanation: According to the given rules:
  nums[0] = 0
  nums[1] = 1
  nums[(1 * 2) = 2] = nums[1] = 1
  nums[(1 * 2) + 1 = 3] = nums[1] + nums[2] = 1 + 1 = 2
  nums[(2 * 2) = 4] = nums[2] = 1
  nums[(2 * 2) + 1 = 5] = nums[2] + nums[3] = 1 + 2 = 3
  nums[(3 * 2) = 6] = nums[3] = 2
  nums[(3 * 2) + 1 = 7] = nums[3] + nums[4] = 2 + 1 = 3
Hence, nums = [0,1,1,2,1,3,2,3], and the maximum is max(0,1,1,2,1,3,2,3) = 3.

Example 2:

Input: n = 2
Output: 1
Explanation: According to the given rules, nums = [0,1,1]. The maximum is max(0,1,1) = 1.

Example 3:

Input: n = 3
Output: 2
Explanation: According to the given rules, nums = [0,1,1,2]. The maximum is max(0,1,1,2) = 2.


Constraints:

0 <= n <= 100

"""

# V0
# IDEA : SIMULATION (build the array left to right, every rule looks back)
#
#   rewrite both rules in terms of i alone:
#     i even -> nums[i] = nums[i // 2]
#     i odd  -> nums[i] = nums[i // 2] + nums[i // 2 + 1]
#   both referenced indices are < i (for i >= 2), so a single forward pass
#   already has them computed.
#
#   NOTE : n = 0 and n = 1 must be answered directly -- the array is too
#          short for the loop to run.
#
# time = O(n), space = O(n)
class Solution(object):
    def getMaximumGenerated(self, n):
        if n < 2:
            return n
        nums = [0] * (n + 1)
        nums[1] = 1
        for i in range(2, n + 1):
            half = i // 2
            if i % 2 == 0:
                nums[i] = nums[half]
            else:
                nums[i] = nums[half] + nums[half + 1]
        return max(nums)
