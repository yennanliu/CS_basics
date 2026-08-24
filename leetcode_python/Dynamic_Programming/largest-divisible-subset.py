# V0  : dev 

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/83027364
"""

DP def
    SORT first: then a <= b with b % a == 0 makes divisibility TRANSITIVE
    along the sorted order, so it becomes an LIS-style chain problem

    dp[i]  : size of the largest divisible subset ENDING at nums[i]

    parent[i]: the predecessor index, used to rebuild the subset

DP eq

     dp[i] = max( dp[j] + 1 )   for j < i with nums[i] % nums[j] == 0


    -> e.g. because the list is sorted, checking only nums[i] % nums[j] is
              enough - every earlier chain member divides nums[j], hence
              divides nums[i] too

     init: dp[i] = 1
     ans = walk `parent` back from argmax(dp), then reverse

"""
# time = O(n^2)
# space = O(n)
class Solution:
    def largestDivisibleSubset(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        if not nums: return []
        N = len(nums)
        nums.sort()
        dp = [0] * N #LDS
        parent = [0] * N
        mx = 0
        mx_index = -1
        for i in range(N):
            for j in range(i - 1, -1 , -1):
                if nums[i] % nums[j] == 0 and dp[i] < dp[j] + 1:
                    dp[i] = dp[j] + 1
                    parent[i] = j
                    if dp[i] > mx:
                        mx = dp[i]
                        mx_index = i
        res = list()
        for k in range(mx + 1):
            res.append(nums[mx_index])
            mx_index = parent[mx_index]
        return res[::-1]

# V2
"""

DP def
    SORT first: then a <= b with b % a == 0 makes divisibility TRANSITIVE
    along the sorted order, so it becomes an LIS-style chain problem

    dp[i]  : size of the largest divisible subset ENDING at nums[i]

    parent[i]: the predecessor index, used to rebuild the subset

DP eq

     dp[i] = max( dp[j] + 1 )   for j < i with nums[i] % nums[j] == 0


    -> e.g. because the list is sorted, checking only nums[i] % nums[j] is
              enough - every earlier chain member divides nums[j], hence
              divides nums[i] too

     init: dp[i] = 1
     ans = walk `parent` back from argmax(dp), then reverse

"""
# time = O(n^2)
# space = O(n)
class Solution(object):
    def largestDivisibleSubset(self, nums):
        """
        :type nums: List[int]
        :rtype: List[int]
        """
        if not nums:
            return []

        nums.sort()
        dp = [1] * len(nums)
        prev = [-1] * len(nums)
        largest_idx = 0
        for i in range(len(nums)):
            for j in range(i):
                if nums[i] % nums[j] == 0:
                    if dp[i] < dp[j] + 1:
                        dp[i] = dp[j] + 1
                        prev[i] = j
            if dp[largest_idx] < dp[i]:
                largest_idx = i

        result = []
        i = largest_idx
        while i != -1:
            result.append(nums[i])
            i = prev[i]
        return result[::-1]