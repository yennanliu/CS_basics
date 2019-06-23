# VO 

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79787425
# DFS 
class Solution:
    def canPartition(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        _sum = sum(nums)
        div, mod = divmod(_sum, 2)
        if mod or max(nums) > div: return False
        nums.sort(reverse = True)
        target = [div] * 2
        return self.dfs(nums, 0, target)
    
    def dfs(self, nums, index, target):
        for i in range(2):
            if target[i] >= nums[index]:
                target[i] -= nums[index]
                if target[i] == 0 or self.dfs(nums, index + 1, target): return True
                target[i] += nums[index]
        return False

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79787425
# dp[j] = dp[j] || dp[j - nums[i]]
class Solution(object):
    def canPartition(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        sums = sum(nums)
        if sums & 1: return False
        nset = set([0])
        for n in nums:
            for m in nset.copy():
                nset.add(m + n)
        return sums / 2 in nset

# V2 
# Time:  O(n * s), s is the sum of nums
# Space: O(s)

class Solution(object):
    def canPartition(self, nums):
        """
        :type nums: List[int]
        :rtype: bool
        """
        s = sum(nums)
        if s % 2:
            return False

        dp = [False] * (s/2 + 1)
        dp[0] = True
        for num in nums:
            for i in reversed(xrange(1, len(dp))):
                if num <= i:
                    dp[i] = dp[i] or dp[i - num]
        return dp[-1]
