# Wiggle Sort
# Given an unsorted array nums, reorder it in-place such that nums[0] <= nums[1] >= nums[2] <= nums[3]....
# For example, given nums = [3, 5, 2, 1, 6, 4], one possible answer is [1, 6, 2, 5, 3, 4].

# V0 
class Solution:
    def wiggleSort(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        for i in range(len(nums)-1):
            if i%2 == 1 and nums[i] < nums[i+1]:
                nums[i], nums[i+1] = nums[i+1], nums[i]
            elif i%2 == 0 and nums[i] > nums[i+1]:
                nums[i], nums[i+1] = nums[i+1], nums[i]

# V1
# https://blog.csdn.net/danspace1/article/details/86593393
# IDEA : GO THROUGH THE LIST
# FOR EVEN INDEX : SWAP n, n+1 IF nums(i) > nums(i+1)
# FOR ODD INDEX : SWAP n, n+1 IF nums(i) < nums(i+1)
class Solution:
    def wiggleSort(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        for i in range(len(nums)-1):
            if i%2 == 1 and nums[i]< nums[i+1]:
                nums[i], nums[i+1] = nums[i+1], nums[i]
            elif i%2 == 0 and nums[i]> nums[i+1]:
                nums[i], nums[i+1] = nums[i+1], nums[i]

# V1''
# https://www.jiuzhang.com/solution/wiggle-sort/#tag-highlight-lang-python
class Solution:
    """
    @param: nums: A list of integers
    @return: nothing
    """
    def wiggleSort(self, nums):
        if not nums:
            return  

        for i in range(1, len(nums)):
            should_swap = nums[i] < nums[i - 1] if i % 2 else nums[i] > nums[i - 1]
            if should_swap:
                nums[i], nums[i - 1] = nums[i - 1], nums[i]

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def wiggleSort(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        for i in range(1, len(nums)):
            if ((i % 2) and nums[i - 1] > nums[i]) or \
                (not (i % 2) and nums[i - 1] < nums[i]):
                # Swap unordered elements.
                nums[i - 1], nums[i] = nums[i], nums[i - 1]

# V3 
# time: O(nlogn)
# space: O(n)
class Solution2(object):
    def wiggleSort(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        nums.sort()
        med = (len(nums) - 1) // 2
        nums[::2], nums[1::2] = nums[med::-1], nums[:med:-1]
