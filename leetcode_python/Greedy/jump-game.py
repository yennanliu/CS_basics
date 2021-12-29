"""

55. Jump Game
Medium

You are given an integer array nums. You are initially positioned at the array's first index, and each element in the array represents your maximum jump length at that position.

Return true if you can reach the last index, or false otherwise.

 

Example 1:

Input: nums = [2,3,1,1,4]
Output: true
Explanation: Jump 1 step from index 0 to 1, then 3 steps to the last index.
Example 2:

Input: nums = [3,2,1,0,4]
Output: false
Explanation: You will always arrive at index 3 no matter what. Its maximum jump length is 0, which makes it impossible to reach the last index.
 

Constraints:

1 <= nums.length <= 104
0 <= nums[i] <= 105

"""

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
# DEMO 1):
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
#
# DEMO 2):
#     ...: nums = [3,2,1,0,4]
#     ...:
#     ...: s = Solution()
#     ...: r = s.canJump(nums)
#     ...: print (r)
# i = 0 num = 3 reach = 0
# i = 1 num = 2 reach = 3
# i = 2 num = 1 reach = 3
# i = 3 num = 0 reach = 3
# False
class Solution(object):
    def canJump(self, nums):
        reach = 0
        for i, num in enumerate(nums):
            # DON'T HAVE TO CARE PRIOR STEP, 
            # -> IMAGE EACH INDEX IS REACHABLE FIRST, 
            # -> THEN VALIDATE IF IT'S POSSBILE FROM PRIOR STEP TO THIS INDEX 
            # so if i > reach, means it's not possbile form prior step to reach this index -> return false
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
