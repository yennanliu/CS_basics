# https://leetcode.com/problems/missing-number/description/
# Time:  O(n)
# Space: O(1)
#
# Given an array containing n distinct numbers taken from
# 0, 1, 2, ..., n, find the one that is missing from the array.
#
# For example,
# Given nums = [0, 1, 3] return 2.
#
# Note:
# Your algorithm should run in linear runtime complexity.
# Could you implement it using only constant extra space complexity?
#

# V0 
class Solution(object):
    def missingNumber(self, nums):
        return sum(range(len(nums)+1)) - sum(nums)

# V1 
# http://bookshadow.com/weblog/2015/08/24/leetcode-missing-number/
class Solution(object):
    def missingNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        n = len(nums)
        return n * (n + 1) / 2 - sum(nums)

# V1' 
# http://bookshadow.com/weblog/2015/08/24/leetcode-missing-number/
# IDEA : BIT MANIPULATION 
# IDEA : XOR 
# https://stackoverflow.com/questions/14526584/what-does-the-xor-operator-do/14526640
# XOR is a binary operation, it stands for "exclusive or", that is to say the resulting bit evaluates to one if only exactly one of the bits is set.
# "XOR" is same as "^"
# a | b | a ^ b
# --|---|------
# 0 | 0 | 0
# 0 | 1 | 1
# 1 | 0 | 1
# 1 | 1 | 0
class Solution(object):
    def missingNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        a = reduce(operator.xor, nums)
        b = reduce(operator.xor, range(len(nums) + 1))
        return a ^ b

# V2 
class Solution(object):
    def missingNumber(self, nums):
        return sum(range(len(nums)+1)) - sum(nums)

# V3 
import operator
from functools import reduce
class Solution(object):
    def missingNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return reduce(operator.xor, nums, \
                      reduce(operator.xor, range(len(nums) + 1)))
