# V0

# V1
# http://bookshadow.com/weblog/2017/12/03/leetcode-delete-and-earn/
# time = O(n + maxn)  # n = len(nums), maxn = max(nums)
# space = O(maxn)
import collections
class Solution(object):
    def deleteAndEarn(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        cnt = collections.Counter(nums)
        maxn = max(nums + [0])
        dp = [0] * (maxn + 10)
        for x in range(1, maxn + 1):
            dp[x] = max(dp[x - 1], dp[x - 2] + cnt[x] * x)
        return dp[maxn]

# V1'
# https://www.jiuzhang.com/solution/delete-and-earn/#tag-highlight-lang-python
# time = O(n)  # n = len(nums), fixed 10001-size DP loop
# space = O(1)  # fixed-size arrays independent of input
class Solution:
    """
    @param nums: a list of integers
    @return: return a integer
    """
    def deleteAndEarn(self, nums):
        # write your code here
        counters = [0 for i in range(10001)]
        for x in nums:
            counters[x] += 1
        
        dp = [0 for i in range(10001)]
        dp[1] = counters[1]
        for i in range(2, 10001):
            dp[i] = max(dp[i - 1], dp[i - 2] + i * counters[i])       
        return dp[10000]

# V2
# time = O(n)
# space = O(1)
class Solution(object):
    def deleteAndEarn(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        vals = [0] * 10001
        for num in nums:
            vals[num] += num
        val_i, val_i_1 = vals[0], 0
        for i in range(1, len(vals)):
            val_i_1, val_i_2 = val_i, val_i_1
            val_i = max(vals[i] + val_i_2, val_i_1)
        return val_i