"""

Given an array, rotate the array to the right by k steps, where k is non-negative.

Example 1:

Input: [1,2,3,4,5,6,7] and k = 3
Output: [5,6,7,1,2,3,4]
Explanation:
rotate 1 steps to the right: [7,1,2,3,4,5,6]
rotate 2 steps to the right: [6,7,1,2,3,4,5]
rotate 3 steps to the right: [5,6,7,1,2,3,4]
Example 2:

Input: [-1,-100,3,99] and k = 2
Output: [3,99,-1,-100]
Explanation: 
rotate 1 steps to the right: [99,-1,-100,3]
rotate 2 steps to the right: [3,99,-1,-100]
Note:

Try to come up as many solutions as you can, there are at least 3 different ways to solve this problem.
Could you do it in-place with O(1) extra space?

"""

# Time:  O(n)
# Space: O(1)
#
# Rotate an array of n elements to the right by k steps.
#
# For example, with n = 7 and k = 3, the array [1,2,3,4,5,6,7] is rotated to [5,6,7,1,2,3,4].
#
# Note:
# Try to come up as many solutions as you can, there are at least 3 different ways to solve this problem.
#


# V1 
class Solution:
    def rotate(self, nums, k):
        for epoch in range(k):
            nums = [nums[-1]] + nums
            nums = nums[:-1]
        return nums 


# V2 


class Solution:
    """
    :type nums: List[int]
    :type k: int
    :rtype: void Do not return anything, modify nums in-place instead.
    """

    def rotate(self, nums, k):
        k %= len(nums)
        self.reverse(nums, 0, len(nums))
        self.reverse(nums, 0, k)
        self.reverse(nums, k, len(nums))

    def reverse(self, nums, start, end):
        while start < end:
            nums[start], nums[end - 1] = nums[end - 1], nums[start]
            start += 1
            end -= 1

    def rotate2(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        nums[:] = nums[len(nums) - k:] + nums[:len(nums) - k]


from fractions import gcd


class Solution2:
    """
    :type nums: List[int]
    :type k: int
    :rtype: void Do not return anything, modify nums in-place instead.
    """

    def rotate(self, nums, k):
        k %= len(nums)
        num_cycles = gcd(len(nums), k)
        cycle_len = len(nums) / num_cycles
        for i in range(num_cycles):
            self.apply_cycle_permutation(k, i, cycle_len, nums)

    def apply_cycle_permutation(self, k, offset, cycle_len, nums):
        tmp = nums[offset]
        for i in range(1, cycle_len):
            nums[(offset + i * k) % len(nums)], tmp = tmp, nums[(offset + i * k) % len(nums)]
        nums[offset] = tmp


class Solution3:
    """
    :type nums: List[int]
    :type k: int
    :rtype: void Do not return anything, modify nums in-place instead.
    """

    def rotate(self, nums, k):
        count = 0
        start = 0
        while count < len(nums):
            curr = start
            prev = nums[curr]
            while True:
                idx = (curr + k) % len(nums)
                nums[idx], prev = prev, nums[idx]
                curr = idx
                count += 1
                if start == curr:
                    break
            start += 1



            









