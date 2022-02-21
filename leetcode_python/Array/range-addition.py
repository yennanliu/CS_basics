"""

370. Range Addition
Medium

You are given an integer length and an array updates where updates[i] = [startIdxi, endIdxi, inci].

You have an array arr of length length with all zeros, and you have some operation to apply on arr. In the ith operation, you should increment all the elements arr[startIdxi], arr[startIdxi + 1], ..., arr[endIdxi] by inci.

Return arr after applying all the updates.

 

Example 1:


Input: length = 5, updates = [[1,3,2],[2,4,3],[0,2,-2]]
Output: [-2,0,3,5,3]
Example 2:

Input: length = 10, updates = [[2,4,6],[5,6,8],[1,9,-4]]
Output: [0,-4,2,2,2,4,4,-4,-4,-4]
 

Constraints:

1 <= length <= 105
0 <= updates.length <= 104
0 <= startIdxi <= endIdxi < length
-1000 <= inci <= 1000

"""

# V0
# IDEA : double loop -> 2 single loops,  prefix sum
class Solution(object):
    def getModifiedArray(self, length, updates):
        # NOTE : we init res with (len+1)
        res = [0] * (length + 1)
        """
        NOTE !!!

        -> We split double loop into 2 single loops
        -> Steps)
            step 1) go through updates,  add val to start and end idx in res
            step 2) go through res, maintain an aggregated sum (sum) and add it to res[i]
                e.g. res[i], sum = res[i] + sum, res[i] + sum
        """
        for start, end, val in updates:
            res[start] += val
            res[end+1] -= val
        
        sum = 0
        for i in range(0, length):
            res[i], sum = res[i] + sum, res[i] + sum
        
        # NOTE : we return res[0:-1]
        return res[0:-1]

# V0'
# IDEA : double loop -> 2 single loops,  prefix sum
class Solution(object):
    def getModifiedArray(self, length, updates):
        ret = [0] * (length + 1)
        for start, end, val in updates:
            ret[start] += val
            ret[end+1] -= val
        
        sum = 0
        for i in range(0, length):
            ret[i], sum = ret[i] + sum, ret[i] + sum
        
        return ret[0:-1]

# V0
# IDEA : naive (TLE)
# class Solution(object):
#     def getModifiedArray(self, length, updates):
#         # edge case
#         if not updates:
#             return [0 for i in range(length)]
#
#         res = [0 for i in range(length)]
#
#         for start, end, diff in updates:
#             for i in range(start, end+1):
#                 res[i] += diff
#         return res

# V1
# IDEA : double loop -> 2 single loops,  prefix sum
# https://leetcode.com/problems/range-addition/discuss/84239/python-code
class Solution(object):
    def getModifiedArray(self, length, updates):
        ret = [0] * (length + 1)
        for start, end, val in updates:
            ret[start] += val
            ret[end+1] -= val
        
        sum = 0
        for i in range(0, length):
            ret[i], sum = ret[i] + sum, ret[i] + sum
        
        return ret[0:-1]

# V1'
# https://leetcode.com/problems/range-addition/discuss/84228/Python-solution-with-detailed-explanation
# IDEA
# Checkpointing updates
# We store updates at borders including start and end+1. A positive update at start and negative update at end+1.
# We do a final calculation by taking a cumulative sum.
class Solution(object):
    def getModifiedArray(self, length, updates):
        nums = [0]*length
        for update in updates:
            s,e,v = update[0], update[1], update[2]
            nums[s] = nums[s] + v
            if e < length-1:
                nums[e+1] = nums[e+1] - v
        for i in range(1, length):
            nums[i] = nums[i-1] + nums[i]
        return nums

# V1''
# https://leetcode.com/problems/range-addition/discuss/846584/Python-O(n-%2B-k)
class Solution:
    def getModifiedArray(self, length, updates):
        result = [0] * (length + 1)
        for i in range(len(updates)):
            result[updates[i][0]] += updates[i][2]
            result[updates[i][1] + 1] += -updates[i][2]
        for i in range(1, len(result)): result[i] += result[i - 1]
        return result[:-1]

# V1'''
# https://leetcode.com/problems/range-addition/discuss/84241/Python-Simple-Solution
class Solution(object):
    def getModifiedArray(self, length, updates):
        nums = [0 for _ in range(length)]
        for update in updates:
            i, j, k = update
            nums[i] += k
            if j+1 < length:
                nums[j+1] -= k
        for i in range(1,length):
            nums[i] += nums[i-1]
        return nums

# V1''''
# https://leetcode.com/problems/range-addition/discuss/239463/Python-solution
# IDEA
# We initialize two dictionaries start and end, which maps the index of the array to the increment value that starts and ends at index, respectively. Then we initialize the result res = [0]*length, and the increment value at the current index Inc = 0. We iterate i in range(length), we modify Inc according to the two dictionaries start and end, and add Inc to res[i]. Finally, we return res.
# Time complexity: O(length+len(updates)), space complexity: O(length+len(updates)).
class Solution:
    def getModifiedArray(self, length, updates):
        
        start = collections.defaultdict(int)
        end = collections.defaultdict(int)
        for update in updates:
            sIdx, eIdx, val = update
            start[sIdx] += val
            end[eIdx] += val
            
        res = [0]*length
        Inc = 0  
        for i in range(length):
            if i in start:
                Inc += start[i]
            res[i] += Inc
            if i in end:
                Inc -= end[i]
        return res

# V1'''''
# https://blog.csdn.net/qq508618087/article/details/51864853
# DEV 

# V1'''''''
# IDEA :  Naïve Approach
# https://leetcode.com/problems/range-addition/solution/
# C++
# vector<int> getModifiedArray(int length, vector<vector<int> > updates)
# {
#     vector<int> result(length, 0);
#
#     for (auto& tuple : updates) {
#         int start = tuple[0], end = tuple[1], val = tuple[2];
#
#         for (int i = start; i <= end; i++) {
#             result[i] += val;
#         }
#     }
#
#     return result;
# }

# V1'''''''''
# IDEA : Range Caching
# https://leetcode.com/problems/range-addition/solution/
# C++
# vector<int> getModifiedArray(int length, vector<vector<int> > updates)
# {
#     vector<int> result(length, 0);
#
#     for (auto& tuple : updates) {
#         int start = tuple[0], end = tuple[1], val = tuple[2];
#
#         result[start] += val;
#         if (end < length - 1)
#             result[end + 1] -= val;
#     }
#
#     // partial_sum applies the following operation (by default) for the parameters {x[0], x[n], y[0]}:
#     // y[0] = x[0]
#     // y[1] = y[0] + x[1]
#     // y[2] = y[1] + x[2]
#     // ...  ...  ...
#     // y[n] = y[n-1] + x[n]
#
#     partial_sum(result.begin(), result.end(), result.begin());
#
#     return result;
# }


# V2 
# Time:  O(k + n)
# Space: O(1)
class Solution(object):
    def getModifiedArray(self, length, updates):
        """
        :type length: int
        :type updates: List[List[int]]
        :rtype: List[int]
        """
        result = [0] * length
        for update in updates:
            result[update[0]] += update[2]
            if update[1]+1 < length:
                result[update[1]+1] -= update[2]
        for i in range(1, length):
            result[i] += result[i-1]
        return result