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

# V0 
# IDEA : SLICE (in place)
class Solution(object):
    def rotate(self, nums, k):
        k = k % len(nums)
        nums[:k], nums[k:] = nums[-k:], nums[:len(nums)-k]

# V0'
# IDEA : SLICE
class Solution(object):
    def rotate(self, nums, k):
        k = k % len(nums) # since the rotate operation is cyclic. i.e. if len(nums)=7, k=17 -> rotate(17) = rotate(17%7) = rotate(3)
        nums[:k], nums[k:] = nums[len(nums)-k:], nums[:len(nums)-k]

# V1 
# https://blog.csdn.net/coder_orz/article/details/52052767
# IDEA : SLICE 
class Solution(object):
    def rotate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        k = k % len(nums) # since the rotate operation is cyclic. i.e. if len(nums)=7, k=17 -> rotate(17) = rotate(17%7) = rotate(3)
        nums[:k], nums[k:] = nums[len(nums)-k:], nums[:len(nums)-k]
        # for test cases
        return nums

### Test case
s=Solution()
assert s.rotate([1,2,3,4,5,6,7], 3) == [5, 6, 7, 1, 2, 3, 4]
assert s.rotate([1,2,3,4,5,6,7], 4) == [4, 5, 6, 7, 1, 2, 3]
assert s.rotate([1,2,3,4,5,6,7], 5) == [3, 4, 5, 6, 7, 1, 2]
assert s.rotate([1], 0) == [1]
assert s.rotate([1], 1) == [1]
assert s.rotate([1], 2) == [1]

# V1'
# https://blog.csdn.net/coder_orz/article/details/52052767
class Solution(object):
    def rotate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        old_nums = nums[:]
        for i in range(len(nums)):
            nums[(i + k) % len(nums)] = old_nums[i]

# V1''
# https://blog.csdn.net/coder_orz/article/details/52052767
class Solution(object):
    def rotate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        k = k % len(nums)
        self.reversePart(nums, 0, len(nums)-k-1)
        self.reversePart(nums, len(nums)-k, len(nums)-1)
        self.reversePart(nums, 0, len(nums)-1)

    def reversePart(self, nums, start, end):
        while start < end:
            nums[start], nums[end] = nums[end], nums[start]
            start, end = start+1, end-1

# V1'''
# https://blog.csdn.net/coder_orz/article/details/52052767
class Solution(object):
    def rotate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        k, start, n = k % len(nums), 0, len(nums)
        while k % n != 0 and n > 0:
            for i in range(k):
                nums[start + i], nums[len(nums) - k + i] = nums[len(nums) - k + i], nums[start + i]
            start, n = start + k, n - k
            k = k % n

# V2 
class Solution:
    def rotate(self, nums, k):
        for epoch in range(k):
            nums = [nums[-1]] + nums
            nums = nums[:-1]
        return nums 

# V3 
# Time:  O(n)
# Space: O(1)
class Solution(object):
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

# Time:  O(n)
# Space: O(1)
from fractions import gcd
class Solution2(object):
    """
    :type nums: List[int]
    :type k: int
    :rtype: void Do not return anything, modify nums in-place instead.
    """

    def rotate(self, nums, k):
        def apply_cycle_permutation(k, offset, cycle_len, nums):
            tmp = nums[offset]
            for i in range(1, cycle_len):
                nums[(offset + i * k) % len(nums)], tmp = tmp, nums[(offset + i * k) % len(nums)]
            nums[offset] = tmp

        k %= len(nums)
        num_cycles = gcd(len(nums), k)
        cycle_len = len(nums) / num_cycles
        for i in range(num_cycles):
            apply_cycle_permutation(k, i, cycle_len, nums)


# Time:  O(n)
# Space: O(1)
class Solution3(object):
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


# Time:  O(n)
# Space: O(n)
class Solution4(object):
    """
    :type nums: List[int]
    :type k: int
    :rtype: void Do not return anything, modify nums in-place instead.
    """
    def rotate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        nums[:] = nums[len(nums) - k:] + nums[:len(nums) - k]
        

# Time:  O(k * n)
# Space: O(1)
class Solution5(object):
    """
    :type nums: List[int]
    :type k: int
    :rtype: void Do not return anything, modify nums in-place instead.
    """
    def rotate(self, nums, k):
        while k > 0:
            nums.insert(0, nums.pop())
            k -= 1