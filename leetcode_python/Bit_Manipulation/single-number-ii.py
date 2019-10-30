# # https://leetcode.com/problems/single-number-ii/description/
# # https://github.com/yennanliu/LeetCode/blob/master/Python/single-number-ii.py
# Given a non-empty array of integers, every element appears three times except for one, which appears exactly once. Find that single one.
# Note:
# Your algorithm should have a linear runtime complexity. Could you implement it without using extra memory?
# Example 1:
# Input: [2,2,3,2]
# Output: 3
# Example 2:
# Input: [0,1,0,1,0,1,99]
# Output: 99


# V0 
class Solution:
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # for [a,a,b,a] array
        # 3*(a+b) - (a+a+b+a) = 2b
        return int((3*(sum(set(nums))) - sum(nums))//2)

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79554959
class Solution(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        res = 0
        for i in range(32):
            cnt = 0
            mask = 1 << i
            for num in nums:
                if num & mask:
                    cnt += 1
            if cnt % 3 == 1:
                res |= mask
        if res >= 2 ** 31:
            res -= 2 ** 32
        return res

# V1'
class Solution:
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        # for [a,a,b,a] array
        # 3*(a+b) - (a+a+b+a) = 2b
        return int((3*(sum(set(nums))) - sum(nums))/2)
 
# V2 
import collections
class Solution3(object):
    def singleNumber(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return list((collections.Counter(list(set(nums)) * 3) - collections.Counter(nums)).keys())[0]
