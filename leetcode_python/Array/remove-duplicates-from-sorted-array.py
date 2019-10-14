# Time:  O(n)
# Space: O(1)
#
# Given a sorted array, remove the duplicates in place such that each element appear only once and return the new length.
#
# Do not allocate extra space for another array, you must do this in place with constant memory.
#
# For example,
# Given input array A = [1,1,2],
#
# Your function should return length = 2, and A is now [1,2].
#

# V0
class Solution:
    # @param a list of integers
    # @return an integer
    def removeDuplicates(self, A):
        if len(A) == 0:
            return 0
        j = 0
        for i in range(0, len(A)):
            if A[i] != A[j]:
                A[i], A[j+1] = A[j+1], A[i]
                j = j + 1
        return j+1

# V1 
# https://www.cnblogs.com/zuoyuan/p/3779816.html
# IDEA : TWO POINTER 
# IDEA : use an index j, when i go through the array,
# if A[i] != A[j], then swap A[i] and A[j+1] and do j=j+1 (A[i], A[j+1] = A[j+1], A[i])
# then i keep going through the array 
# DEMO
# A = [1,1,1,2,3]
# s = Solution()
# s.removeDuplicates(A)
# [1, 1, 1, 2, 3]
# [1, 1, 1, 2, 3]
# [1, 1, 1, 2, 3]
# [1, 2, 1, 1, 3]
# [1, 2, 3, 1, 1]
class Solution:
    # @param a list of integers
    # @return an integer
    def removeDuplicates(self, A):
        if len(A) == 0:
            return 0
        j = 0
        for i in range(0, len(A)):
            if A[i] != A[j]:
                A[i], A[j+1] = A[j+1], A[i]
                j = j + 1
        return j+1

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51589013
# IDEA : TWO POINTER 
class Solution(object):
    def removeDuplicates(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if len(nums)==0:
            return 0
        cur = 0
        for i in range(1, len(nums)):
            if nums[i] != nums[cur]:
                cur += 1
                nums[cur] = nums[i]
        return cur+1

# V1'' 
# https://blog.csdn.net/coder_orz/article/details/51589013
# IDEA : COUNTER     
class Solution(object):
    def removeDuplicates(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        count = 0
        for i in range(1, len(nums)):
            if nums[i] == nums[i-1]:
                count += 1
            else:
                nums[i-count] = nums[i]
        return len(nums) - count

# V1'''
# https://www.jiuzhang.com/solution/remove-duplicates-from-sorted-array/#tag-highlight-lang-python
class Solution:
    """
    @param A: a list of integers
    @return an integer
    """
    def removeDuplicates(self, A):
        # write your code here
        if A == []:
            return 0
        index = 0
        for i in range(1, len(A)):
            if A[index] != A[i]:
                index += 1
                A[index] = A[i]
        return index + 1

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param a list of integers
    # @return an integer
    def removeDuplicates(self, A):
        if not A:
            return 0

        last = 0
        for i in range(len(A)):
            if A[last] != A[i]:
                last += 1
                A[last] = A[i]
        return last + 1
