"""

1. Two Sum
Easy

Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.

You may assume that each input would have exactly one solution, and you may not use the same element twice.

You can return the answer in any order.

 

Example 1:

Input: nums = [2,7,11,15], target = 9
Output: [0,1]
Explanation: Because nums[0] + nums[1] == 9, we return [0, 1].
Example 2:

Input: nums = [3,2,4], target = 6
Output: [1,2]
Example 3:

Input: nums = [3,3], target = 6
Output: [0,1]
 

Constraints:

2 <= nums.length <= 104
-109 <= nums[i] <= 109
-109 <= target <= 109
Only one valid answer exists.
 

Follow-up: Can you come up with an algorithm that is less than O(n2) time complexity?

"""

# V0 
# IDEA : HASH TABLE 
# DEMO :
# In [2]: class Solution(object):
#    ...:     def twoSum(self, nums, target):
#    ...:         """
#    ...:         :type nums: List[int]
#    ...:         :type target: int
#    ...:         :rtype: List[int]
#    ...:         """
#    ...:         lookup = {}
#    ...:         for i, num in enumerate(nums):
#    ...:             if target - num in lookup:
#    ...:                 return [lookup[target - num], i]
#    ...:             lookup[num] = i
#    ...:             print (lookup)
#    ...: 
#    ...: nums = [2, 0, 100, 3,2, 9 ,7, 11, 15]
#    ...: target = 9
#    ...: Solution().twoSum(nums, target)
#    ...: 
# {2: 0}
# {2: 0, 0: 1}
# {2: 0, 0: 1, 100: 2}
# {2: 0, 0: 1, 100: 2, 3: 3}
# {2: 4, 0: 1, 100: 2, 3: 3}
# Out[2]: [1, 5]
class Solution(object):
    def twoSum(self, nums, target):
        lookup = {}
        for i, num in enumerate(nums):
            if target - num in lookup:
                return [lookup[target - num], i]
            lookup[num] = i
        return [-1, -1]

# V0'
# IDEA : dict
class Solution(object):
    def twoSum(self, nums, target):
        d = {}
        for i in range(len(nums)):
            if target - nums[i] in d:
                return [d[target - nums[i]], i]
            else:
                if nums[i] not in d:
                    d[nums[i]] = i

# V1 
# https://blog.csdn.net/coder_orz/article/details/52039233
# IDEA : LINEAR SCAN
class Solution(object):
    def twoSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[int]
        """
        for i in range(len(nums) - 1):
            for j in range(i+1, len(nums)):
                if nums[i] + nums[j] == target:
                    return [i, j]
# V1'
# https://www.jiuzhang.com/solution/two-sum/#tag-highlight-lang-python
class Solution(object):
    def twoSum(self, nums, target):
        # hash for index-value mapping 
        hash = {}
        # loop over nums, and sync it with hash 
        for i in range(len(nums)):
            if target - nums[i] in hash:
                return [hash[target - nums[i]], i]
            hash[nums[i]] = i
        # in case if there is no solution (two sum)
        return [-1, -1]

# V2 
# example :
# nums = [3,3] , target = 6 -> return [0, 1]
# nums = [0,3,3] , target = 6 -> return [1, 2]
class Solution(object):
    def twoSum(self, nums, target):
        for i,num in enumerate(nums):
            if target - num in nums[i+1:]:
                return [nums.index(num),nums[i+1:].index( target - num) +i+1 ]
            else:
                pass

# V3 
class Solution(object):
    def twoSum(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[int]
        """
        lookup = {}
        for i, num in enumerate(nums):
            if target - num in lookup:
                return [lookup[target - num], i]
            lookup[num] = i

    def twoSum2(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: List[int]
        """
        for i in nums:
            j = target - i
            tmp_nums_start_index = nums.index(i) + 1
            tmp_nums = nums[tmp_nums_start_index:]
            if j in tmp_nums:
                return [nums.index(i), tmp_nums_start_index + tmp_nums.index(j)]
