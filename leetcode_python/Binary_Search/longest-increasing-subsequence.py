# V0
# IDEA : DP 
# DP equation : 
#               -> dp[i] = max(1, dp[j] + 1), while i > j and nums[i] > nums[i]
#               -> dp[0] = 1 
# dp[idx] : the longest lengh of the sub-array at index = idx
# PROCESS : 
# Start from dp[0], then go through all nums
# and update dp on the same time
# then return max(dp) which is the longest lengh of the sub-array  
# time complexity : O(n^2), space complexity : O(n)
class Solution:
    def lengthOfLIS(self, nums):
        # write your code here
        if nums is None or not nums:
            return 0
        dp = [1] * len(nums)
        for i in range(len(nums)):
            for j in range(i):
                if nums[i] > nums[j]:
                    dp[i] = max(dp[i], dp[j]+1)
        return max(dp)

# V0'
# IDEA : DP 
# time complexity : O(n^2), space complexity : O(n)
class Solution(object):
    def lengthOfLIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums: return 0
        dp = [0] * len(nums)
        dp[0] = 1
        for i in range(1, len(nums)):
            tmax = 1
            for j in range(0, i):
                if nums[i] > nums[j]:
                    tmax = max(tmax, dp[j] + 1)
            dp[i] = tmax
        return max(dp) 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79820919
# IDEA : DP 
class Solution(object):
    def lengthOfLIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        if not nums: return 0
        dp = [0] * len(nums)
        dp[0] = 1
        for i in range(1, len(nums)):
            tmax = 1
            for j in range(0, i):
                if nums[i] > nums[j]:
                    tmax = max(tmax, dp[j] + 1)
            dp[i] = tmax
        return max(dp)

# V1'
# https://www.jiuzhang.com/solution/longest-increasing-subsequence/#tag-highlight-lang-python
# IDEA : DP
class Solution:
    """
    @param nums: The integer array
    @return: The length of LIS (longest increasing subsequence)
    """
    def longestIncreasingSubsequence(self, nums):
        # write your code here
        if nums is None or not nums:
            return 0
        dp = [1] * len(nums)
        for curr, val in enumerate(nums):
            for prev in range(curr):
                if nums[prev] < val:
                    dp[curr] = max(dp[curr], dp[prev] + 1)
        return max(dp)

# V1''
# https://www.jiuzhang.com/solution/longest-increasing-subsequence/#tag-highlight-lang-python
# IDEA : DP + binary search
# time complexity : O(nlogn), space complexity : O(n)
class Solution:
    """
    @param nums: The integer array
    @return: The length of LIS (longest increasing subsequence)
    """
    def longestIncreasingSubsequence(self, nums):
        if nums is None or not nums:
            return 0
        
        # state: dp[i] : longest length of sequence from left to right till i 
        
        # initialization: dp[0..n-1] = 1
        dp = [1] * len(nums)
        
        # prev[i] :  the best solution of dp[i] is from which dp[j] 
        prev = [-1] * len(nums)
        
        # function dp[i] = max{dp[j] + 1},  j < i and nums[j] < nums[i]
        for i in range(len(nums)):
            for j in range(i):
                if nums[j] < nums[i] and dp[i] < dp[j] + 1:
                    dp[i] = dp[j] + 1
                    prev[i] = j
        
        # answer: max(dp[0..n-1])
        longest, last = 0, -1
        for i in range(len(nums)):
            if dp[i] > longest:
                longest = dp[i]
                last = i
        
        path = []
        while last != -1:
            path.append(nums[last])
            last = prev[last]
        print(path[::-1])       
        return longest

# V1'''
# https://www.jiuzhang.com/solution/longest-increasing-subsequence/#tag-highlight-lang-python
class Solution:
    """
    @param nums: An integer array
    @return: The length of LIS (longest increasing subsequence)
    """
    def longestIncreasingSubsequence(self, nums):
        if nums is None or len(nums) == 0:
            return 0
        
        n = len(nums)
        f = [0] * n         
        g = [0] * (n + 1)   
        
        lis = f[0] = 1
        g[1] = nums[0]
        
        for i in range(1, n):            
            if nums[i] > g[lis]: 
                f[i] = lis
                lis += 1
                g[lis] = nums[i]
            else:
                l, r =  1, lis 
                while l != r:
                    mid = (l + r) // 2 
                    if g[mid] < nums[i]:
                        l = mid + 1 
                    else:
                        r = mid
                f[i] = l
                g[l] = min(g[l], nums[i])
        return lis

# V2 
# Time:  O(nlogn)
# Space: O(n)
class Solution(object):
    def lengthOfLIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        LIS = []
        def insert(target):
            left, right = 0, len(LIS) - 1
            # Find the first index "left" which satisfies LIS[left] >= target
            while left <= right:
                mid = left + (right - left) // 2
                if LIS[mid] >= target:
                    right = mid - 1
                else:
                    left = mid + 1
            # If not found, append the target.
            if left == len(LIS):
                LIS.append(target)
            else:
                LIS[left] = target

        for num in nums:
            insert(num)

        return len(LIS)

# Time:  O(n^2)
# Space: O(n)
# Traditional DP solution.
class Solution2(object):
    def lengthOfLIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        dp = []  # dp[i]: the length of LIS ends with nums[i]
        for i in range(len(nums)):
            dp.append(1)
            for j in range(i):
                if nums[j] < nums[i]:
                    dp[i] = max(dp[i], dp[j] + 1)
        return max(dp) if dp else 0