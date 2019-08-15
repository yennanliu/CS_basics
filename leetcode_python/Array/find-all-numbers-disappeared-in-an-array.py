"""
Given an array of integers where 1 ≤ a[i] ≤ n (n = size of array), some elements appear twice and others appear once.

Find all the elements of [1, n] inclusive that do not appear in this array.

Could you do it without extra space and in O(n) runtime? You may assume the returned list does not count as extra space.

Example:

Input:
[4,3,2,7,8,2,3,1]

Output:
[5,6]
"""

# V0

# V1
# https://blog.csdn.net/nxhyd/article/details/72322608
# IDEA : SET 
# In [23]: x=set([1,3,4,5])
# In [24]: y=set(i for i in range(1, max(x)+1))
# In [25]: y
# Out[25]: {1, 2, 3, 4, 5}
# In [26]: list(y-x)
# Out[26]: [2]
class Solution(object):
    def findDisappearedNumbers(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        n = set(nums)
        new = set(range(1, len(nums) + 1))
        return list(new - n)   # works on "set" data structure 

# V1'
# http://bookshadow.com/weblog/2016/11/01/leetcode-find-all-numbers-disappeared-in-an-array/
class Solution(object):
    def findDisappearedNumbers(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        for n in nums:
            nums[abs(n) - 1] = -abs(nums[abs(n) - 1])         # lebel n with minus (-) if it already exists in the array 
        return [i + 1 for i, n in enumerate(nums) if n > 0]   # go through the array to find the n not exists there (with +)

# V2
# Time:  O(n)
# Space: O(1)
class Solution(object):
    def findDisappearedNumbers(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        for i in range(len(nums)):
            if nums[abs(nums[i]) - 1] > 0:
                nums[abs(nums[i]) - 1] *= -1

        result = []
        for i in range(len(nums)):
            if nums[i] > 0:
                result.append(i+1)
            else:
                nums[i] *= -1
        return result

    def findDisappearedNumbers2(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        return list(set(range(1, len(nums) + 1)) - set(nums))

    def findDisappearedNumbers3(self, nums):
        for i in range(len(nums)):
            index = abs(nums[i]) - 1
            nums[index] = - abs(nums[index])

        return [i + 1 for i in range(len(nums)) if nums[i] > 0]
