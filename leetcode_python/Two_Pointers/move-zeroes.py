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


# V1 
class Solution(object):
    def moveZeroes(self, nums):
        return sorted(nums)[::-1] 

# V1' 
class Solution(object):
    def moveZeroes(self, nums):
        return [ x for x in nums if x != 0 ] + [ x for x in nums if x == 0 ] 

# V2 
class Solution(object):
    def moveZeroes(self, nums):
        zero_list = []
        length = len(nums)
        for i in range(length - 1):
            if nums[i] == 0:
                nums.pop(i)
                zero_list.append(0)
                length  = length -1 
        return nums + zero_list


# V3 
# http://bookshadow.com/weblog/2015/09/19/leetcode-move-zeroes/
class Solution(object):
    def moveZeroes(self, nums):
        y = 0
        for x in range(len(nums)):
            if nums[x]:
                nums[x], nums[y] = nums[y], nums[x]
                y += 1


# V4 
class Solution(object):
    def moveZeroes(self, nums):
        """
        :type nums: List[int]
        :rtype: void Do not return anything, modify nums in-place instead.
        """
        pos = 0
        for i in xrange(len(nums)):
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
        for i in xrange(len(nums)):
            if nums[i]:
                nums[pos] = nums[i]
                pos += 1

        for i in xrange(pos, len(nums)):
            nums[i] = 0


# if __name__ == '__main__':
#     s = Solution()
#     r = s.moveZeroes([0, 1, 0, 3, 12])
#     print r
