# V0 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83504437
# IDEA :  GREEDY 
# TO CHECK :  DP VS GREEDY 
class Solution(object):
    def canJump(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        reach = 0
        for i, num in enumerate(nums):
            if i > reach:
                return False
            reach = max(reach, i + num)
        return True

# V2 
# Time:  O(n)
# Space: O(1)
class Solution(object):
    # @param A, a list of integers
    # @return a boolean
    def canJump(self, A):
        reachable = 0
        for i, length in enumerate(A):
            if i > reachable:
                break
            reachable = max(reachable, i + length)
        return reachable >= len(A) - 1
