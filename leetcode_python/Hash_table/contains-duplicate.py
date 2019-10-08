"""
Given an array of integers, find if the array contains any duplicates.

Your function should return true if any value appears at least twice in the array, and it should return false if every element is distinct.

Example 1:

Input: [1,2,3,1]
Output: true
Example 2:

Input: [1,2,3,4]
Output: false
Example 3:

Input: [1,1,1,3,3,4,3,2,4,2]
Output: true
"""
# Time:  O(n)
# Space: O(n)
#
# Given an array of integers, find if the array contains any duplicates.
# Your function should return true if any value appears at least twice in the array,
# and it should return false if every element is distinct.
#

# V0 

# V1 
class Solution(object):
    def containsDuplicate(self, nums):
        if len(set(nums)) != len(nums):
            return True 
        else:
            return False 

# V1'
# https://www.jiuzhang.com/solution/contains-duplicate/#tag-highlight-lang-python
class Solution:
    """
    @param nums: the given array
    @return: if any value appears at least twice in the array
    """
    def containsDuplicate(self, nums):
        # Write your code here
        hashset = {}
        for num in nums:
            if num in hashset:
                return True
            hashset[num] = True
        return False
        
# V2 
class Solution:
    # @param {integer[]} nums
    # @return {boolean}
    def containsDuplicate(self, nums):
        return len(nums) > len(set(nums))
