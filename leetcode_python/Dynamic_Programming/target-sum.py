"""

494. Target Sum
Solved
Medium
Topics
premium lock icon
Companies
You are given an integer array nums and an integer target.

You want to build an expression out of nums by adding one of the symbols '+' and '-' before each integer in nums and then concatenate all the integers.

For example, if nums = [2, 1], you can add a '+' before 2 and a '-' before 1 and concatenate them to build the expression "+2-1".
Return the number of different expressions that you can build, which evaluates to target.

 

Example 1:

Input: nums = [1,1,1,1,1], target = 3
Output: 5
Explanation: There are 5 ways to assign symbols to make the sum of nums be target 3.
-1 + 1 + 1 + 1 + 1 = 3
+1 - 1 + 1 + 1 + 1 = 3
+1 + 1 - 1 + 1 + 1 = 3
+1 + 1 + 1 - 1 + 1 = 3
+1 + 1 + 1 + 1 - 1 = 3
Example 2:

Input: nums = [1], target = 1
Output: 1
 

Constraints:

1 <= nums.length <= 20
0 <= nums[i] <= 1000
0 <= sum(nums[i]) <= 1000
-1000 <= target <= 1000
 

"""

# V0
# IDEA: 1D DP (0/1 knapsack), LC 416
"""
NOTE !!!


Let:

P = numbers assigned +
N = numbers assigned -

Then:

P−N=target

and

P+N=total

Adding them:

2P=total+target

Therefore:

P= (total+target) / 2


"""
# time = O(n * s), n = len(nums), s = subset_target = (total + target) // 2
# space = O(s)
class Solution(object):
    def findTargetSumWays(self, nums, target):
        total = sum(nums)

        # Impossible cases
        if abs(target) > total:
            return 0

        if (total + target) % 2 != 0:
            return 0

        subset_target = (total + target) // 2

        # dp[s] = number of ways to make sum s
        dp = [0] * (subset_target + 1)

        # One way to make sum 0
        dp[0] = 1

        """
        NOTE !!!


        for DP (0/1 knapsack)
        -> we loop
           `num`, then `target`

        -> 

           for num in nums:
                for x in range(target, num - 1, -1):
                        ...
        """
        for num in nums:
            # backward because each number can be used once
            for s in range(subset_target, num - 1, -1):
                dp[s] += dp[s - num]

        return dp[subset_target]


# V0-1
# IDEA: 1D DP (0/1 knapsack), LC 416
# time = O(n * s), n = len(nums), s = subset_target = (target + total_sum) // 2
# space = O(s)
class Solution(object):
    def findTargetSumWays(self, nums, target):
        """
        :type nums: List[int]
        :type target: int
        :rtype: int
        """
        total_sum = sum(nums)
        
        # ABSOLUTE BOUNDS GUARD: If the target is physically out of reach 
        # even if all numbers were positive, return 0.
        if abs(target) > total_sum:
            return 0
            
        # PARITY GUARD: (target + total_sum) must be even and non-negative.
        if (target + total_sum) % 2 == 1 or (target + total_sum) < 0:
            return 0
            
        # CRITICAL FIX: Calculate the accurate modified subset sum target
        subset_target = (target + total_sum) // 2
        
        # CRITICAL FIX: Initialize a 1D DP counting table of size (subset_target + 1).
        # dp[s] stores the total number of unique ways to form a subset sum equal to 's'.
        dp = [0] * (subset_target + 1)
        
        # BASE CASE: There is exactly 1 way to form a subset sum of 0 (using an empty subset).
        dp[0] = 1
        
        # Outer loop: Evaluate each number in the array one by one
        for num in nums:
            # Inner loop: Scan BACKWARDS from our subset_target down to 'num'.
            # Moving backward prevents the same number from being reused multiple times.
            for s in range(subset_target, num - 1, -1):
                # Accumulate the total pathways. The number of ways to reach sum 's' 
                # increases by the number of ways we could reach the remainder 's - num'.
                dp[s] += dp[s - num]
                
        # Return the total ways recorded at our exact target location
        return dp[subset_target]


# V0 
# IDEA : DP 
# IDEA :
# dp[0][0] = 1;
# dp[i + 1][x + nums[i]] += dp[i][x];
# dp[i + 1][x - nums[i]] += dp[i][x];
# ( dp[i][j] -> at index = i, # of the way can have sum = j )
# time = O(n * sum), n = len(nums), sum = sum(nums)  # distinct sums per layer bounded by O(sum)
# space = O(n * sum)
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
# time = O(n * sum), n = len(nums), sum = sum(nums)
# space = O(sum)
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
# time = O(n * sum), n = len(nums), sum = sum(nums)
# space = O(n * sum)
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
# time = O(2^n), n = len(nums)
# space = O(n)
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
# time = O(n * s), n = len(nums), s = target = (S + sum(nums)) // 2
# space = O(s)
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
