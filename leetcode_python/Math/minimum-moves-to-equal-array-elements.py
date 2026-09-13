"""

453. Minimum Moves to Equal Array Elements
Medium

Given an integer array nums of size n, return the minimum number of moves required to make all array elements equal.

In one move, you can increment n - 1 elements of the array by 1.

Example 1:

Input: nums = [1,2,3]
Output: 3
Explanation: Only three moves are needed (remember each move increments two elements):
[1,2,3]  =>  [2,3,3]  =>  [3,4,3]  =>  [4,4,4]

Example 2:

Input: nums = [1,1,1]
Output: 0

Constraints:

n == nums.length
1 <= nums.length <= 10^5
-10^9 <= nums[i] <= 10^9
The answer is guaranteed to fit in a 32-bit integer.

"""

# V0

# V1 : dev

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
# time = O(n)
# space = O(1)
class Solution(object):
    def minMoves(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return sum(nums) - len(nums) * min(nums)


# V3
# time = O(n)
# space = O(1)

class Solution(object):
    def minMoves(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        return sum(nums) - len(nums) * min(nums)
