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
# IDEA : HashMap
#     -> SET UP A DICT,
#     -> FIND MAX SUB ARRAY LENGH WHEN COUNT(0) == COUNT(1)
#     -> (WHEN cur in _dict, THERE IS THE COUNT(0) == COUNT(1) CASE)
# explaination : https://leetcode.com/problems/contiguous-array/discuss/99655/python-on-solution-with-visual-explanation
class Solution(object):
    def findMaxLength(self, nums):
        r = 0
        cur = 0
        ### NOTE : WE NEED INIT DICT LIKE BELOW
        # https://blog.csdn.net/fuxuemingzhu/article/details/82667054
        _dict = {0:-1}
        for k, v in enumerate(nums):
            if v == 1:
                cur += 1
            else:
                cur -= 1
            if cur in _dict:
                r = max(r, k - _dict[cur])
            else:
                _dict[cur] = k
        return r

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

# V0''
# IDEA : BRUTE FROCE (Time Limit Exceeded)
class Solution:
    def findMaxLength(self, nums):
        r = 0
        for i in range(len(nums)):
            for j in range(i+1, len(nums)):
                tmp = sum(nums[i:j+1])
                if tmp == (j-i+1) / 2:
                    _r = j-i+1
                else:
                    _r = 0
                r = max(r, _r)
        return r

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
# https://leetcode.com/problems/contiguous-array/discuss/99655/python-on-solution-with-visual-explanation
class Solution(object):
    def findMaxLength(self, nums):
        count = 0
        max_length=0
        table = {0: 0}
        for index, num in enumerate(nums, 1):
            if num == 0:
                count -= 1
            else:
                count += 1
            
            if count in table:
                max_length = max(max_length, index - table[count])
            else:
                table[count] = index
        
        return max_length

# V1''
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