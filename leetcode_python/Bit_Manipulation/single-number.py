"""
# python 3 
# https://leetcode.com/problems/single-number/description/

# solution 
# https://leetcode.com/articles/single-number/

Given a non-empty array of integers, every element appears twice except for one. Find that single one.

Note:

Your algorithm should have a linear runtime complexity. Could you implement it without using extra memory?

Example 1:

Input: [2,2,1]
Output: 1
Example 2:

Input: [4,1,2,1,2]
Output: 4
"""

# V0 
import collections 
class Solution(object):
    def singleNumber(self, nums):
        num_count = collections.Counter(nums)
        return [x for x in num_count if num_count[x] == 1] 

# V0'
# IDEA : BIT XOR 
# IDEA 
# Solution with XOR #
# Recall the following two properties of XOR:
# It returns zero if we take XOR of two same numbers.
# It returns the same number if we XOR with zero.
# So we can XOR all the numbers in the input; 
# -> duplicate numbers will zero out each other and we will be left with the single number.
class Solution(object):
    def singleNumber(self, nums):
        a = 0
        for i in nums:
            a ^= i
        return a

# V1
# https://blog.csdn.net/qq_20141867/article/details/82314035
class Solution(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return 2 * sum(set(nums)) - sum(nums)
        
# V1'
# https://www.jiuzhang.com/solution/single-number/#tag-highlight-lang-python
class Solution:
    """
   @param A : an integer array
   @return : a integer
   """
    def singleNumber(self, A):
        # write your code here
        ans = 0;
        for x in A:
            ans = ans ^ x
        return ans

# V1 
class Solution:
	def singleNumber(self, nums):
		no_repeat_array=[]
		for index, item in enumerate(nums):
			if item in no_repeat_array:
				no_repeat_array.remove(item)
			else:
				no_repeat_array.append(item)
		return no_repeat_array.pop()

# V2 
# to know the non-repeat element 
# 2∗(a+b+c)−(a+a+b+b+c)=c
class Solution(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return 2 * sum(set(nums)) - sum(nums)

# V3 
# XOR logic 
# https://www.programiz.com/python-programming/operators
# x ^ 0 = x 
# x ^ x = 0
class Solution(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        a = 0
        for i in nums:
            a ^= i
        return a
