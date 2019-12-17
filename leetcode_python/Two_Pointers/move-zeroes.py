# Time:  O(n)
# Space: O(1)
# Given an array nums, write a function to move all 0's
# to the end of it while maintaining the relative order
# of the non-zero elements.
#
# For example, given nums = [0, 1, 0, 3, 12], after
# calling your function, nums should be [1, 3, 12, 0, 0].
#
# Note:
# You must do this in-place without making a copy of the array.
# Minimize the total number of operations.

# V0 
class Solution(object):
    def moveZeroes(self, nums):
        return [ x for x in nums if x != 0 ] + [ x for x in nums if x == 0 ] 
# V0'
# DEMO 
# In [59]:  array  = [9,0,0,1,5, 11, 7, 0 ]
# In [60]: sol = Solution().moveZeroes(array)
# [9, 0, 0, 1, 5, 11, 7, 0]
# [9, 0, 0, 1, 5, 11, 7, 0]
# [9, 0, 0, 1, 5, 11, 7, 0]
# [9, 0, 0, 1, 5, 11, 7, 0]
# [9, 1, 0, 0, 5, 11, 7, 0]
# [9, 1, 5, 0, 0, 11, 7, 0]
# [9, 1, 5, 11, 0, 0, 7, 0]
# [9, 1, 5, 11, 7, 0, 0, 0]
# In [61]: sol
# Out[61]: [9, 1, 5, 11, 7, 0, 0, 0]
class Solution(object):
    def moveZeroes(self, nums):
        y = 0
        for x in range(len(nums)):
            if nums[x] != 0:
                nums[x], nums[y] = nums[y], nums[x]
                y += 1
        return nums 

# V1' 
class Solution(object):
    def moveZeroes(self, nums):
        return [ x for x in nums if x != 0 ] + [ x for x in nums if x == 0 ] 

# V1''
# https://www.jiuzhang.com/solution/move-zeroes/#tag-highlight-lang-python
class Solution:
    """
    @param nums: an integer array
    @return: nothing
    """
    def moveZeroes(self, nums):
        left, right = 0, 0
        while right < len(nums):
            if nums[right] != 0:
                if left != right:
                    nums[left] = nums[right]
                left += 1
            right += 1
            
        while left < len(nums):
            if nums[left] != 0:
                nums[left] = 0
            left += 1
            
# V2 
class Solution(object):
    def moveZeroes(self, nums):
        non_zero_list = []
        length = len(nums)
        for i in range(length ):
            print (i)
            if nums[i] != 0:
                non_zero_list.append(nums[i])
        return non_zero_list + [0]*( length - len(non_zero_list))

# V3 
# http://bookshadow.com/weblog/2015/09/19/leetcode-move-zeroes/
class Solution(object):
    def moveZeroes(self, nums):
        y = 0
        for x in range(len(nums)):
            if nums[x] != 0:
                nums[x], nums[y] = nums[y], nums[x]
                y += 1
        return nums 

# V4 
class Solution(object):
    def moveZeroes(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        pos = 0
        for i in range(len(nums)):
            if nums[i]:
                nums[i], nums[pos] = nums[pos], nums[i]
                pos += 1

    def moveZeroes2(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        nums.sort(cmp=lambda a, b: 0 if b else -1)

# V5 
class Solution2(object):
    def moveZeroes(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        pos = 0
        for i in range(len(nums)):
            if nums[i]:
                nums[pos] = nums[i]
                pos += 1

        for i in range(pos, len(nums)):
            nums[i] = 0
# if __name__ == '__main__':
#     s = Solution()
#     r = s.moveZeroes([0, 1, 0, 3, 12])
#     print r
