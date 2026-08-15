"""

3173. Bitwise OR of Adjacent Elements
Easy
🔒 (premium)

Given an array nums of length n, return an array answer of length n - 1 such that answer[i] = nums[i] | nums[i + 1] where | is the bitwise OR operation.


Example 1:

Input: nums = [1,3,7,15]
Output: [3,7,15]

Example 2:

Input: nums = [8,4,2]
Output: [12,6]

Example 3:

Input: nums = [5,4,9,11]
Output: [5,13,11]


Constraints:

2 <= nums.length <= 100
0 <= nums[i] <= 100

"""

# V0
# IDEA : ZIP THE ARRAY WITH ITS OWN TAIL
#
#   zip(nums, nums[1:]) walks exactly the n-1 adjacent pairs, so the answer
#   is one comprehension.
#
# time = O(n), space = O(n)
class Solution(object):
    def orArray(self, nums):
        return [a | b for a, b in zip(nums, nums[1:])]
