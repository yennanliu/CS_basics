# V1  : dev 

# class Solution(object):
#     def minMoves(self, nums):
#     	nums = nums.sorted()
#     	count = 0 
#     	while sum(nums) != len(nums)*(nums[-1]):
#     		nums



# V2 
# https://blog.csdn.net/u012814856/article/details/72710519
# idea :
# this is a math problem:
# let's say we need "m" moves then can get all number as x 
# so we will following equeations below: 
# "sum" : current sum before any move, "n" : number of elements in the list
# sum +  m * (n-1)  = x * n   --- (1)
# x = minNum + (n-1)  --- (2)
# so we will get the infal relation as below 
# sum = n * minNum + m --- (final)
# and this the  answer of this problem :    m = sum - n * minNum 
class Solution(object):
    def minMoves(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return sum(nums) - len(nums) * min(nums)


# V3 
# Time:  O(n)
# Space: O(1)

class Solution(object):
    def minMoves(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return sum(nums) - len(nums) * min(nums)
