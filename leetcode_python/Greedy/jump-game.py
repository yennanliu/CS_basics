# V0 
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

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83504437
# IDEA :  GREEDY 
# -> 1) WE DONT HAVE TO CARE ABOUT THE PRIOR STEPS, BUT ONLY SELECT THE CURRENT BEST STEP, THEN MOVE FORWARD
# -> 2) ONLY FOCUS ON THE INDEX REACHED BY "MAX JUMP", SINCE ALL INDEX WITHIN "MAX REACH" CAN ALSO BE REACHED

# TO CHECK :  DP VS GREEDY 
# DEMO :
# In [9]: nums=[2,3,1,1,4]
#    ...: 
#    ...: s=Solution()
#    ...: r=s.canJump(nums)
#    ...: print (r)
#    ...: 
# 0 2 0
# 1 3 2
# 2 1 4
# 3 1 4
# 4 4 4
# True
class Solution(object):
    def canJump(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        reach = 0
        for i, num in enumerate(nums):
            # DON'T HAVE TO CARE PRIOR STEP, 
            # -> IMAGE EACH INDEX IS REACHABLE FIRST, 
            # -> THEN VALIDATE IF IT'S POSSBILE FROM PRIOR STEP TO THIS INDEX 
            # so if i > reach, means it's not possbile form prior step to this index -> return false
            if i > reach:
                return False
            reach = max(reach, i + num)
        return True

### Test case
s=Solution()
assert s.canJump([2,3,1,1,4]) ==True
assert s.canJump([3,2,1,0,4]) ==False
assert s.canJump([]) ==True
assert s.canJump([1]) ==True
assert s.canJump([-1]) ==False
assert s.canJump([-1, -2]) ==False
assert s.canJump([-1, 2,3]) ==False
assert s.canJump([ 0 for _ in range(10)]) ==False

# V1'
# https://leetcode.com/problems/jump-game/discuss/593108/python-O(N)-solution-Space%3AO(1)
class Solution(object):
    def canJump(self, nums):
        lstidx = len(nums)-1
        for i in range(lstidx-1,-1,-1):
            if i+nums[i] >= lstidx:
                lstidx = i
        return not lstidx

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
