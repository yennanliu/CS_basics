"""
Given a binary array, find the maximum length of a CONTIGUOUS subarray with equal number of 0 and 1.

Example 1:
Input: [0,1]
Output: 2
Explanation: [0, 1] is the longest CONTIGUOUS subarray with equal number of 0 and 1.
Example 2:
Input: [0,1,0]
Output: 2
Explanation: [0, 1] (or [1, 0]) is a longest CONTIGUOUS  subarray with equal number of 0 and 1.
Note: The length of the given binary array will not exceed 50,000.

"""

# V0
# IDEA : SET UP A DICT, cur_sum, ans 
# -> TO SAVE THE LENGTH OF SUB ARRAY WHEN COUNT OF 0 = COUNT OF 1, AND UPDATE cur_sum, ans  BY CASES 
# -> RETURN THE MAX OF THE ans 
class Solution:
    def findMaxLength(self, nums):
        index_sum = {}
        cur_sum = 0
        ans = 0
        for i in range(len(nums)):
            if nums[i] == 0: 
                cur_sum -= 1
            else: 
                cur_sum += 1
            if cur_sum == 0: 
                ans = i+1
            elif cur_sum in index_sum: 
                ans = max(ans, i-index_sum[cur_sum])
            if cur_sum not in index_sum: 
                index_sum[cur_sum] = i
        return ans

# V0' 
class Solution(object):
    def findMaxLength(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        result, count = 0, 0
        lookup = {0: -1}
        for i, num in enumerate(nums):
            count += 1 if num == 1 else -1
            if count in lookup:
                result = max(result, i - lookup[count])
            else:
                lookup[count] = i

        return result

# V1
# https://www.jiuzhang.com/solution/contiguous-array/#tag-highlight-lang-python
# IDEA : SET UP A DICT, cur_sum, ans 
# -> TO SAVE THE LENGTH OF SUB ARRAY WHEN COUNT OF 0 = COUNT OF 1, AND UPDATE cur_sum, ans  BY CASES 
# -> RETURN THE MAX OF THE ans 
class Solution:
    """
    @param nums: a binary array
    @return: the maximum length of a contiguous subarray
    """
    def findMaxLength(self, nums):
        index_sum = {}
        cur_sum = 0
        ans = 0
        for i in range(len(nums)):
            if nums[i] == 0: 
                cur_sum -= 1
            else: 
                cur_sum += 1
            if cur_sum == 0: 
                ans = i+1
            elif cur_sum in index_sum: 
                ans = max(ans, i-index_sum[cur_sum])
            if cur_sum not in index_sum: 
                index_sum[cur_sum] = i
        return ans

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/82667054
# https://kingsfish.github.io/2017/07/13/Leetcode-525-Contiguous-Array/
class Solution:
    def findMaxLength(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        print(nums)
        total_sum = 0
        index_map = dict()
        index_map[0] = -1
        res = 0        
        for i, num in enumerate(nums):
            if num == 0:
                total_sum -= 1
            else:
                total_sum += 1
            if total_sum in index_map:
                res = max(res, i - index_map[total_sum])
            else:
                index_map[total_sum] = i
        return res
        
# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def findMaxLength(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        result, count = 0, 0
        lookup = {0: -1}
        for i, num in enumerate(nums):
            count += 1 if num == 1 else -1
            if count in lookup:
                result = max(result, i - lookup[count])
            else:
                lookup[count] = i

        return result
