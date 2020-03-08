# V0
# IDEA : DP
# define dp as : dp[length][count] 
# length longest length for the increasing sub-array 
# count : count of the sub-array with above case
# DP EQUATION :
# dp[i][0] = max(dp[j][0] + 1), nums[j] < nums[i]
# dp[i][1] = sum(dp[j][1]), nums[j] < nums[i] and dp[j][0] == dp[i][0] - 1
class Solution(object):
    def findNumberOfLIS(self, nums):
        # init dp as dp[length][count] 
        dp, longest = [[1, 1] for _ in range(len(nums))], 1
        for i in range(1, len(nums)):
            curr_longest, count = 1, 0
            for j in range(i):
                # dp[i][0] = max(dp[j][0] + 1), nums[j] < nums[i]
                if nums[j] < nums[i]:
                    curr_longest = max(curr_longest, dp[j][0] + 1)
            for j in range(i):
                # dp[i][1] = sum(dp[j][1]), nums[j] < nums[i] and dp[j][0] == dp[i][0] - 1
                if dp[j][0] == curr_longest - 1 and nums[j] < nums[i]:
                    count += dp[j][1]
            dp[i] = [curr_longest, max(count, dp[i][1])]
            longest = max(longest, curr_longest)
        return sum([item[1] for item in dp if item[0] == longest])

# V1
# https://blog.csdn.net/fuxuemingzhu/article/details/79822126
# IDEA : DP
class Solution(object):
    def findNumberOfLIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        dp, longest = [[1, 1] for _ in range(len(nums))], 1
        for i in range(1, len(nums)):
            curr_longest, count = 1, 0
            for j in range(i):
                if nums[j] < nums[i]:
                    curr_longest = max(curr_longest, dp[j][0] + 1)
            for j in range(i):
                if dp[j][0] == curr_longest - 1 and nums[j] < nums[i]:
                    count += dp[j][1]
            dp[i] = [curr_longest, max(count, dp[i][1])]
            longest = max(longest, curr_longest)
        return sum([item[1] for item in dp if item[0] == longest])
        
# V1'
# https://www.jiuzhang.com/solution/number-of-longest-increasing-subsequence/#tag-highlight-lang-python
class Solution(object):
    def findNumberOfLIS(self, nums):
        ans = [0, 0]
        l = len(nums)
        dp = collections.defaultdict(list)
        for i in range(l):
            dp[i] = [1, 1]
        for i in range(l):
            for j in range(i):
                if nums[i] > nums[j]:
                    if dp[j][0] + 1 > dp[i][0]:
                        dp[i] = [dp[j][0] + 1, dp[j][1]]
                    elif dp[j][0] + 1 == dp[i][0]:
                        dp[i] = [dp[i][0], dp[i][1] + dp[j][1]]
        for i in dp.keys():
            if dp[i][0] > ans[0]:
                ans = [dp[i][0], dp[i][1]]
            elif dp[i][0] == ans[0]:
                ans = [dp[i][0], ans[1] + dp[i][1]]
        return ans[1]

# V2
# Time:  O(n^2)
# Space: O(n)
class Solution(object):
    def findNumberOfLIS(self, nums):
        """
        :type nums: List[int]
        :rtype: int
        """
        result, max_len = 0, 0
        dp = [[1, 1] for _ in range(len(nums))]  # {length, number} pair
        for i in range(len(nums)):
            for j in range(i):
                if nums[i] > nums[j]:
                    if dp[i][0] == dp[j][0]+1:
                        dp[i][1] += dp[j][1]
                    elif dp[i][0] < dp[j][0]+1:
                        dp[i] = [dp[j][0]+1, dp[j][1]]
            if max_len == dp[i][0]:
                result += dp[i][1]
            elif max_len < dp[i][0]:
                max_len = dp[i][0]
                result = dp[i][1]
        return result