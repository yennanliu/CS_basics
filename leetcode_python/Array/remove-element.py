# Time:  O(n)
# Space: O(1)
#
# Given an array and a value, remove all instances of that value in place and return the new length.
#
# The order of elements can be changed. It doesn't matter what you leave beyond the new length.
#

# V0 
# class Solution(object):
#     def removeElement(self, nums, val):
#         import collection
#         num_count = collection.Counter(nums)
#         return len(nums) - num_count[val]

# V1 
# https://blog.csdn.net/coder_orz/article/details/51578854
# IDEA : DOUBLE POINTER 
class Solution(object):
    def removeElement(self, nums, val):
        length = 0
        for i in range(len(nums)):
            if nums[i] != val:
                nums[length] = nums[i]
                length += 1
        return length

# test case
s = Solution()
assert s.removeElement([3,2,2,3], 2) == 2
assert s.removeElement([3,2,2,3], 3) == 2
assert s.removeElement([], 3) == 0
assert s.removeElement([1], 3) == 1
assert s.removeElement([3], 3) == 0
assert s.removeElement([_ for _ in range(100)], 3) == 99

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51578854
class Solution(object):
    def removeElement(self, nums, val):
        """
        :type nums: List[int]
        :type val: int
        :rtype: int
        """
        rm_index = []
        for i in range(len(nums)):
            if nums[i] == val:
                rm_index.append(i)
        last = len(nums) - 1
        for i in rm_index:
            while last >= 0 and nums[last] == val:
                last -= 1
            if last < 0:
                break
            nums[i] = nums[last]
            last -= 1
        return len(nums) - len(rm_index)

# V1'' 
# https://blog.csdn.net/coder_orz/article/details/51578854
class Solution(object):
    def removeElement(self, nums, val):
        """
        :type nums: List[int]
        :type val: int
        :rtype: int
        """
        length, i = len(nums), 0 
        while i < length:
            if nums[i] == val:
                nums[i] = nums[length - 1]
                length -= 1
            else:
                i += 1
        return length

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param    A       a list of integers
    # @param    elem    an integer, value need to be removed
    # @return an integer
    def removeElement(self, A, elem):
        i, last = 0, len(A) - 1
        while i <= last:
            if A[i] == elem:
                A[i], A[last] = A[last], A[i]
                last -= 1
            else:
                i += 1
        return last + 1