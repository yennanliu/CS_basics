# VO 
# IDEA : DP 
# dp[x+y] = dp[x+y] + dp[x] (for all x in range(target + 1), for all y in nums)
# -> dp[x] : # of ways can make sum = x from sub-nums 
class Solution(object):
    def combinationSum4(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        dp = [0] * (target + 1)
        dp[0] = 1
        for x in range(target + 1):
            for y in nums:
                if x + y <= target:
                    dp[x + y] += dp[x]
        return dp[target]

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/79343825
class Solution(object):
    def combinationSum4(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        dp = [0] * (target + 1)
        dp[0] = 1
        for i in range(1, target + 1):
            for num in nums:
                if i >= num:
                    dp[i] += dp[i - num]
        return dp.pop()

# V1'
# IDEA : DP 
# http://bookshadow.com/weblog/2016/07/25/leetcode-combination-sum-iv/
class Solution(object):
    def combinationSum4(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        dp = [0] * (target + 1)
        dp[0] = 1
        for x in range(target + 1):
            for y in nums:
                if x + y <= target:
                    dp[x + y] += dp[x]
        return dp[target]

# V1''
# https://www.hrwhisper.me/leetcode-combination-sum-iv/
# IDEA DP 
# dp[i] += dp[i-num]
class Solution(object):
    def combinationSum4(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        dp = [1] + [0] * target
        for i in xrange(1, target + 1):
            for x in nums:
                if i >= x:
                    dp[i] += dp[i - x]
        return dp[target]

# V1''
# https://www.hrwhisper.me/leetcode-combination-sum-iv/
# IDEA : DP
# dp[i+num] += dp[i]
class Solution(object):
    def combinationSum4(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        dp = [1] + [0] * target
        for i in xrange(target + 1):
            for x in nums:
                if i + x <= target:
                    dp[i + x] += dp[i]
        return dp[target]

# V2 
# Time:  O(nlon + n * t), t is the value of target.
# Space: O(t)
class Solution(object):
    def combinationSum4(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        dp = [0] * (target+1)
        dp[0] = 1
        nums.sort()

        for i in range(1, target+1):
            for j in range(len(nums)):
                if nums[j] <= i:
                    dp[i] += dp[i - nums[j]]
                else:
                    break
        return dp[target]