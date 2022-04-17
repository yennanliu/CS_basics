"""

325. Maximum Size Subarray Sum Equals k
Medium

Given an integer array nums and an integer k, return the maximum length of a subarray that sums to k. If there is not one, return 0 instead.

 

Example 1:

Input: nums = [1,-1,5,-2,3], k = 3
Output: 4
Explanation: The subarray [1, -1, 5, -2] sums to 3 and is the longest.
Example 2:

Input: nums = [-2,-1,2,1], k = 1
Output: 2
Explanation: The subarray [-1, 2] sums to 1 and is the longest.
 

Constraints:

1 <= nums.length <= 2 * 105
-104 <= nums[i] <= 104
-109 <= k <= 109

"""

# V0 
# time complexity : O(N) | space complexity : O(N)
# IDEA : HASH TBALE
# -> have a var acc keep sum of all item in nums,
# -> and use dic collect acc and its index
# -> since we want to find nums[i:j] = k  -> so it's a 2 sum problem now
# -> i.e. if acc - k in dic => there must be a solution (i,j) of  nums[i:j] = k  
# -> return the max result 
# -> ### acc DEMO : given array a = [1,2,3,4,5] ###
# -> acc_list = [1,3,6,10,15]
# -> so sum(a[1:3]) = 9 = acc_list[3] - acc_list[1-1] = 10 - 1 = 9 
class Solution(object):
    def maxSubArrayLen(self, nums, k):

        result, acc = 0, 0
        # NOTE !!! we init dic as {0:-1} ({sum:idx})
        dic = {0: -1}

        for i in range(len(nums)):
            acc += nums[i]
            if acc not in dic:
                ### NOTE : we save idx as dict value
                dic[acc] = i
            ### acc - x = k -> so x = acc - k, that's why we check if acc - x in the dic or not
            if acc - k in dic:
                result = max(result, i - dic[acc-k])
        return result

# V0'
# IDEA : BRUTE FORCE 
# time complexity : O(N^2) | space complexity : O(N)
class Solution(object):
    def maxSubArrayLen(self, nums, k):
        # O(n^2)
        tmp = []
        for i in range(len(nums)):
            for j in range(i, len(nums)):
                if sum(nums[i:j]) == k:
                    tmp.append(j-i)
        return 0 if len(tmp) == 0 else max(tmp)

# V1
# IDEA :  Prefix Sum + Hash Map
# https://leetcode.com/problems/maximum-size-subarray-sum-equals-k/solution/
class Solution:
    def maxSubArrayLen(self, nums: List[int], k: int) -> int:
        prefix_sum = longest_subarray = 0
        indices = {}
        
        for i, num in enumerate(nums):
            prefix_sum += num
            
            # Check if all of the numbers seen so far sum to k.
            if prefix_sum == k:
                longest_subarray = i + 1
                
            # If any subarray seen so far sums to k, then
            # update the length of the longest_subarray. 
            if prefix_sum - k in indices:
                longest_subarray = max(longest_subarray, i - indices[prefix_sum - k])
                
            # Only add the current prefix_sum index pair to the 
            # map if the prefix_sum is not already in the map.
            if prefix_sum not in indices:
                indices[prefix_sum] = i
        
        return longest_subarray

# V1'
# http://www.voidcn.com/article/p-pbvzylrp-qp.html
# https://www.cnblogs.com/lightwindy/p/9760070.html
# IDEA : DICT 
# STEPS :
# -> HAVE A dic RECORD THE SUMS AND THEIR INDEX 
# -> GO THROUGH ALL ELEMENTS IN nums AND KEEP DOING ACCUMULATED SUM : acc
# -> IF acc not in dict : dic[acc] = i (add sum and it index to dict)
# -> IF acc - k in dic : result = max(result, i - dic[acc-k])
# acc + sth = k ---> acc - k = sth, so if acc - k already in dict ---> acc + sth = k MUST EXIST
# SO IF ABOVE CASE EXIST, THEN WE DO max(result, i - dic[acc-k]) : TO RETURN THE MAX LENGTH OF SUB ARRAY
# i - dic[acc-k] : GET THE LENGTH BETWEEN i and sth (sth = dic[acc-k] )
class Solution(object):
    def maxSubArrayLen(self, nums, k):

        result, acc = 0, 0
        dic = {0: -1}

        for i in range(len(nums)):
            acc += nums[i]
            if acc not in dic:
                dic[acc] = i
            if acc - k in dic:
                result = max(result, i - dic[acc-k])
        return result

# V1''
# https://www.jiuzhang.com/solution/maximum-size-subarray-sum-equals-k/#tag-highlight-lang-python
class Solution:
    """
    @param nums: an array
    @param k: a target value
    @return: the maximum length of a subarray that sums to k
    """
    def maxSubArrayLen(self, nums, k):
        # Write your code here
        m = {}
        ans = 0
        m[k] = 0
        n = len(nums)
        sum = [0 for i in range(n + 1)]
        for i in range(1, n + 1):
            sum[i] = sum[i - 1] + nums[i - 1]  # use this trick to "record" accumulated sum, avoid using double loop 
            if sum[i] in m:
                ans = max(ans, i - m[sum[i]])
            if sum[i] + k not in m:
                m[sum[i] + k] = i
        return ans
        
# V2 
# Time:  O(n)
# Space: O(n)
class Solution(object):
    def maxSubArrayLen(self, nums, k):
        """
        :type nums: List[int]
        :type k: int
        :rtype: int
        """
        sums = {}
        cur_sum, max_len = 0, 0
        for i in range(len(nums)):
            cur_sum += nums[i]
            if cur_sum == k:
                max_len = i + 1
            elif cur_sum - k in sums:
                max_len = max(max_len, i - sums[cur_sum - k])
            if cur_sum not in sums:
                sums[cur_sum] = i  # Only keep the smallest index.
        return max_len
