# V1 
from random import randint
import random 
class Solution(object):

	def __init__(self, nums):
		"""
		:type nums: List[int]
		:type numsSize: int
		"""
		self.__nums = nums

	def pick(self, target):
		# if len(self.__nums) == 1 or self.__nums.count(target) == 1:
		# 	return 0
		# else:
		indexes = [ k for k, v in enumerate(self.__nums) if v == target ]
		# print ('self.__nums :', self.__nums)
		# print ('indexes :', indexes)
		# print ('randint(0, len(indexes)) :', randint(0, len(indexes)))
		return indexes[randint(0, len(indexes)-1)]
        
        
# V2 
# https://blog.csdn.net/fuxuemingzhu/article/details/79540576
class Solution(object):

    def __init__(self, nums):
        """
        
        :type nums: List[int]
        :type numsSize: int
        """
        self.nums = nums

    def pick(self, target):
        """
        :type target: int
        :rtype: int
        """
        idxs = []
        for i, num in enumerate(self.nums):
            if num == target:
                idxs.append(i)
        return idxs[random.randint(0, len(idxs) - 1)]


# V3
# Time:  O(n)
# Space: O(1)
from random import randint

class Solution(object):

    def __init__(self, nums):
        """
        :type nums: List[int]
        :type numsSize: int
        """
        self.__nums = nums

    def pick(self, target):
        """
        :type target: int
        :rtype: int
        """
        reservoir = -1
        n = 0
        for i in range(len(self.__nums)):
            if self.__nums[i] != target:
                continue
            reservoir = i if randint(1, n+1) == 1 else reservoir
            n += 1
        return reservoir