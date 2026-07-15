# https://leetcode.ca/all/370.html


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
# IDEA: PREFIX SUM (GPT)
class Solution(object):
    def getModifiedArray(self, length, updates):
        diff = [0] * length

        # Record range updates
        for start, end, inc in updates:
            diff[start] += inc

            if end + 1 < length:
                diff[end + 1] -= inc

        # Prefix sum
        res = [0] * length
        running = 0

        """
        NOTE !!!


        1. we update `running` first
        2. then we update res
        """
        for i in range(length):
            running += diff[i]
            res[i] = running

        return res


# V0-1
# IDEA: PREFIX SUM (GEMINI)
class Solution(object):
    def getModifiedArray(self, length, updates):
        """
        :type length: int
        :type updates: List[List[int]]
        :rtype: List[int]
        """
        res = [0] * length
        
        # 1. Build the difference array
        for start, end, inc in updates:
            # Add 'inc' at the start index
            res[start] += inc
            
            # Subtract 'inc' right after the end index (if it's within bounds)
            if end + 1 < length:
                res[end + 1] -= inc
                
        # 2. Compute the prefix sum to apply the updates across the ranges
        current_sum = 0
        for i in range(length):
            current_sum += res[i]
            res[i] = current_sum
            
        return res


# V0
# IDEA: DIFF ARRAY + PREFIX SUM (GPT)
# time = O(n + k)
# space = O(n)
class Solution(object):
    def getModifiedArray(self, length, updates):
        # ------------------------------------------------------------------
        # Create a difference array.
        #
        # Instead of updating every element in the range [start, end],
        # we only record where the increment starts and where it ends.
        #
        # Time Complexity:
        #   O(k), where k = number of updates
        #
        # Space Complexity:
        #   O(n)
        # ------------------------------------------------------------------
        diff = [0] * length

        # ------------------------------------------------------------------
        # Process every update.
        #
        # Each update has the format:
        #   [start, end, increment]
        #
        # Example:
        #   [1, 3, 2]
        #
        # Means:
        #   Add 2 to every index from 1 through 3 (inclusive).
        #
        # Difference array trick:
        #
        #   diff[start] += increment
        #       -> Marks where the increment begins.
        #
        #   diff[end + 1] -= increment
        #       -> Marks where the increment stops.
        #
        # We only subtract if (end + 1) is still inside the array.
        # ------------------------------------------------------------------
        for start, end, increment in updates:
            diff[start] += increment

            if end + 1 < length:
                diff[end + 1] -= increment

        # ------------------------------------------------------------------
        # Build the final result by taking the prefix sum of diff.
        #
        # The running prefix represents the value that should appear
        # at the current index.
        # ------------------------------------------------------------------
        result = [0] * length
        prefix_sum = 0

        # ------------------------------------------------------------------
        # Convert the difference array back into the actual array.
        #
        # Example:
        #
        # diff = [0, 2, 0, 0, -2]
        #
        # prefix_sum progression:
        #
        # i=0 -> 0
        # i=1 -> 2
        # i=2 -> 2
        # i=3 -> 2
        # i=4 -> 0
        #
        # Final result:
        # [0, 2, 2, 2, 0]
        # ------------------------------------------------------------------
        for i in range(length):
            prefix_sum += diff[i]
            result[i] = prefix_sum

        # ------------------------------------------------------------------
        # Return the fully updated array.
        # ------------------------------------------------------------------
        return result


# V0-1
# IDEA: DIFF ARRAY + PREFIX SUM (GEMINI)
# time = O(n + k)
# space = O(n)
class Solution(object):
    def getModifiedArray(self, length, updates):
        """
        :type length: int
        :type updates: List[List[int]]
        :rtype: List[int]
        """
        # Create our difference/modifier array
        diff_arr = [0] * length
        
        # 1. Apply the range boundaries for each update in O(1) time
        for start, end, inc in updates:
            diff_arr[start] += inc
            
            # FIX: Place the negative cancel marker at 'end + 1' 
            # so that index 'end' still safely receives the increment.
            if end + 1 < length:
                diff_arr[end + 1] -= inc
        
        # 2. Accumulate the values prefix-style to build the final array
        res = [0] * length
        running_prefix_sum = 0
        
        for i in range(length):
            running_prefix_sum += diff_arr[i]
            res[i] = running_prefix_sum
            
        return res


# V0
# IDEA : double loop -> 2 single loops,  prefix sum
# time = O(n + k)
# space = O(n)
class Solution(object):
    def getModifiedArray(self, length, updates):
        # edge case
        if not length:
            return
        if length and not updates:
            return [0 for i in range(length)]
        # init res
        res = [0 for i in range(length)]
        # get cumsum on start and end idx
        # then go through res, adjust the sum
        for _start, _end, val in updates:
            res[_start] += val
            if _end+1 < length:
                res[_end+1] -= val

        #print ("res = " + str(res))
        cur = 0
        for i in range(len(res)):
            cur += res[i]
            res[i] = cur
        #print ("--> res = " + str(res))
        return res

# V0'
# IDEA : double loop -> 2 single loops,  prefix sum
# time = O(n + k)
# space = O(n)
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
# time = O(n + k)
# space = O(n)
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
# time = O(n + k)
# space = O(n)
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
# time = O(n + k)
# space = O(n)
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
# time = O(n + k)
# space = O(n)
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
# time = O(n + k)
# space = O(n)
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
# time = O(n + k)
# space = O(n + k)
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
# time = O(k + n)
# space = O(1)
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