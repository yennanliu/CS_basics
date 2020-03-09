# Time:  O(n)
# Space: O(1)
#
# Follow up for "Remove Duplicates":
# What if duplicates are allowed at most twice?
#
# For example,
# Given sorted array A = [1,1,1,2,2,3],
#
# Your function should return length = 5, and A is now [1,1,2,2,3].
#

# V0 
# IDEA : TWO POINTER
# TO NOTE : have to the list IN PLACE
# DEMO
# nums = [1,1,1,2,2,3]
# [1, 1, 1, 2, 2, 3]
# [1, 1, 1, 2, 2, 3]
# [1, 1, 1, 2, 2, 3]
# [1, 1, 1, 2, 2, 3]
# [1, 1, 2, 2, 2, 3]
# [1, 1, 2, 2, 2, 3]
# 5
class Solution(object):
    def removeDuplicates(self, nums):
        i = 0
        for n in nums:
            if i < 2 or n != nums[i - 2]: # i < 2 : for dealing with i < 2 cases (i.e. i=0, i=1, i=2)
                # modify the list IN PLACE
                nums[i] = n
                i += 1
        return i

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/82829709
class Solution(object):
    def removeDuplicates(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        i = 0
        for n in nums:
            if i < 2 or n != nums[i - 2]: # i < 2 : for dealing with i < 2 cases (i.e. i=0, i=1, i=2)
                nums[i] = n
                i += 1
        return i

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82829709
# NEET TO DOUBLE CHECK / FIX 
# class Solution(object):
#     def removeDuplicates(self, nums):
#         """
#         :type nums: List[int]
#         :rtype: int
#         """
#         N = len(nums)
#         if N <= 1: return N
#         left, right = 0, 1
#         while right < N:
#             while right < N and nums[right] == nums[left]:
#                 right += 1
#             left += 1
#             if right < N:
#                 nums[left] = nums[right]
#         return left

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param a list of integers
    # @return an integer
    def removeDuplicates(self, A):
        if not A:
            return 0
        last, i, same = 0, 1, False
        while i < len(A):
            if A[last] != A[i] or not same:
                same = A[last] == A[i]
                last += 1
                A[last] = A[i]
            i += 1
        return last + 1