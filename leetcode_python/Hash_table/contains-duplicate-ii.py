"""

Given an array of integers and an integer k, find out whether there are two distinct indices i and j in the array such that nums[i] = nums[j] and the absolute difference between i and j is at most k.

Example 1:

Input: nums = [1,2,3,1], k = 3
Output: true
Example 2:

Input: nums = [1,0,1,1], k = 1
Output: true
Example 3:

Input: nums = [1,2,3,1,2,3], k = 2
Output: false


"""

# Time:  O(n)
# Space: O(n)
#
# Given an array of integers and an integer k, return true if
# and only if there are two distinct indices i and j in the array
# such that nums[i] = nums[j] and the difference between i and j is at most k.
#

# V0 

# V1 
# https://blog.csdn.net/coder_orz/article/details/51674266
# IDEA : HASH TABLE 
class Solution(object):
    def containsNearbyDuplicate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: bool
        """
        num_map = {}
        for i in range(len(nums)):
            if nums[i] in num_map and i - num_map[nums[i]] <= k:
                return True
            else:
                num_map[nums[i]] = i
        return False

# V1' 
# https://blog.csdn.net/coder_orz/article/details/51674266
# IDEA : SET 
# IDEA : SET OPERATION
# In [12]: window = set([1,3,4,])
#     ...: 
# In [13]: window
# Out[13]: {1, 3, 4}
# In [14]: window.discard(1)
# In [15]: window
# Out[15]: {3, 4}
# In [16]: window.discard(3)
# In [17]: window
# Out[17]: {4}
class Solution(object):
    def containsNearbyDuplicate(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: bool
        """
        window = set([])
        for i in range(len(nums)):
            if i > k:
                window.discard(nums[i-k-1])
            if nums[i] in window:
                return True
            else:
                window.add(nums[i])
        return False

# V1'
# https://www.jiuzhang.com/solution/contains-duplicate-ii/#tag-highlight-lang-python
class Solution:
    """
    @param nums: the given array
    @param k: the given number
    @return:  whether there are two distinct indices i and j in the array such that nums[i] = nums[j] and the absolute difference between i and j is at most k
    """
    def containsNearbyDuplicate(self, nums, k):
        # Write your code here

        dic = {}
        for index, value in enumerate(nums):
            if value in dic and index - dic[value] <= k:
                return True
            dic[value] = index
        return False

# V2 
class Solution:
    # @param {integer[]} nums
    # @param {integer} k
    # @return {boolean}
    def containsNearbyDuplicate(self, nums, k):
        lookup = {}
        for i, num in enumerate(nums):
            if num not in lookup:
                lookup[num] = i
            else:
                # It the value occurs before, check the difference.
                if i - lookup[num] <= k:
                    return True
                # Update the index of the value.
                lookup[num] = i
        return False
