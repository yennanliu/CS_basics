# V0 
# IDEA : DP 
# IDEA :
# dp[0][0] = 1;
# dp[i + 1][x + nums[i]] += dp[i][x];
# dp[i + 1][x - nums[i]] += dp[i][x];
# ( dp[i][j] -> at index = i, # of the way can have sum = j )
class Solution:
    def findTargetSumWays(self, nums, S):
        """
        :type nums: List[int]
        :type S: int
        :rtype: int
        """
        _len = len(nums)
        dp = [collections.defaultdict(int) for _ in range(_len + 1)] 
        dp[0][0] = 1
        for i, num in enumerate(nums):
            for sum, cnt in dp[i].items():
                dp[i + 1][sum + num] += cnt
                dp[i + 1][sum - num] += cnt
        return dp[_len][S]

# V1
# http://bookshadow.com/weblog/2017/01/22/leetcode-target-sum/
# IDEA : DP
import collections
class Solution(object):
    def findTargetSumWays(self, nums, S):
        """
        :type nums: List[int]
        :type S: int
        :rtype: int
        """
        dp = collections.Counter()
        dp[0] = 1
        for n in nums:
            ndp = collections.Counter()
            for sgn in (1, -1):
                for k in dp.keys():
                    ndp[k + n * sgn] += dp[k]
            dp = ndp
        return dp[S]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/80484450
# IDEA : DP
# DP equation 
# -> dp[0][0] = 1;
# -> dp[i + 1][x + nums[i]] += dp[i][x];
# -> dp[i + 1][x - nums[i]] += dp[i][x];
# (x : the "sum" can be built at last positon )
class Solution:
    def findTargetSumWays(self, nums, S):
        """
        :type nums: List[int]
        :type S: int
        :rtype: int
        """
        _len = len(nums)
        dp = [collections.defaultdict(int) for _ in range(_len + 1)] 
        dp[0][0] = 1
        for i, num in enumerate(nums):
            for sum, cnt in dp[i].items():
                dp[i + 1][sum + num] += cnt
                dp[i + 1][sum - num] += cnt
        return dp[_len][S]

# V1''
# IDEA : DFS (TIME OUT ERROR)
class Solution(object):
    def findTargetSumWays(self, nums, S):
        """
        :type nums: List[int]
        :type S: int
        :rtype: int
        """
        def helper(index, acc):
            if index == len(nums):
                if acc == S:
                    return 1
                else:
                    return 0
            return helper(index + 1, acc + nums[index]) + helper(index + 1, acc - nums[index])
        return helper(0, 0)

# V1
# https://blog.csdn.net/xiaoxiaoley/article/details/78968852
# dp[x+y] += dp[y]
class Solution(object):
    def findTargetSumWays(self, nums, S):
        """
        :type nums: List[int]
        :type S: int
        :rtype: int
        """
        if sum(nums)<S:
            return 0
        if (S+sum(nums))%2==1:
            return 0
        target = (S+sum(nums))//2
        dp = [0]*(target+1)
        dp[0] = 1
        for n in nums:
            i = target
            while(i>=n):
                dp[i] = dp[i] + dp[i-n]
                i = i-1
        return dp[target]

# V1'''
# https://zxi.mytechroad.com/blog/dynamic-programming/leetcode-494-target-sum/


# V2
