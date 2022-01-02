"""

525. Contiguous Array
Medium

Given a binary array nums, return the maximum length of a contiguous subarray with an equal number of 0 and 1.

 

Example 1:

Input: nums = [0,1]
Output: 2
Explanation: [0, 1] is the longest contiguous subarray with an equal number of 0 and 1.
Example 2:

Input: nums = [0,1,0]
Output: 2
Explanation: [0, 1] (or [1, 0]) is a longest contiguous subarray with equal number of 0 and 1.
 

Constraints:

1 <= nums.length <= 105
nums[i] is either 0 or 1.

"""

# V0
# IDEA : HashMap
#     -> SET UP A DICT,
#     -> FIND MAX SUB ARRAY LENGH WHEN COUNT(0) == COUNT(1)
#     -> (WHEN cur in _dict, THERE IS THE COUNT(0) == COUNT(1) CASE)
# explaination : https://leetcode.com/problems/contiguous-array/discuss/99655/python-on-solution-with-visual-explanation
class Solution(object):
    def findMaxLength(self, nums):
        # edge case
        if len(nums) <= 1:
            return 0
        if len(nums) == 2:
            if nums.count(0) == nums.count(1):
                return 2
            else:
                return 0

        # init hash map like below (idx=0, no solution)
        d = {0:-1}
        tmp = 0
        res = 0
        for k, v in enumerate(nums):
            if v == 1:
                tmp += 1
            else:
                tmp -= 1
            if tmp in d:
                res = max(res, k - d[tmp])
            else:
                d[tmp] = k
        return res

# V0'
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

# V0''
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

# V0'''
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

# V1''''
# JAVA
# https://leetcode.com/problems/contiguous-array/solution/
# IDEA : Using Extra Array 
# public class Solution {
#
#     public int findMaxLength(int[] nums) {
#         int[] arr = new int[2 * nums.length + 1];
#         Arrays.fill(arr, -2);
#         arr[nums.length] = -1;
#         int maxlen = 0, count = 0;
#         for (int i = 0; i < nums.length; i++) {
#             count = count + (nums[i] == 0 ? -1 : 1);
#             if (arr[count + nums.length] >= -1) {
#                 maxlen = Math.max(maxlen, i - arr[count + nums.length]);
#             } else {
#                 arr[count + nums.length] = i;
#             }
#
#         }
#         return maxlen;
#     }
# }

# V1''''''
# https://leetcode.com/problems/contiguous-array/solution/
# JAVA
# IDEA : HashMap
# public class Solution {
#
#     public int findMaxLength(int[] nums) {
#         Map<Integer, Integer> map = new HashMap<>();
#         map.put(0, -1);
#         int maxlen = 0, count = 0;
#         for (int i = 0; i < nums.length; i++) {
#             count = count + (nums[i] == 1 ? 1 : -1);
#             if (map.containsKey(count)) {
#                 maxlen = Math.max(maxlen, i - map.get(count));
#             } else {
#                 map.put(count, i);
#             }
#         }
#         return maxlen;
#     }
# }


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