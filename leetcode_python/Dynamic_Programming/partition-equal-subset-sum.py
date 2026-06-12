"""

416. Partition Equal Subset Sum
Medium

Given a non-empty array nums containing only positive integers, find if the array can be partitioned into two subsets such that the sum of elements in both subsets is equal.



Example 1:

Input: nums = [1,5,11,5]
Output: true
Explanation: The array can be partitioned as [1, 5, 5] and [11].
Example 2:

Input: nums = [1,2,3,5]
Output: false
Explanation: The array cannot be partitioned into equal sum subsets.
 

Constraints:

1 <= nums.length <= 200
1 <= nums[i] <= 100

"""



# VO
# IDEA: 1D DP
"""

DP def:

dp[s]:
    Whether it is `possible` to `pick` some numbers 
    from the processed elements 
    so that their `sum` equals s.


     -> dp[s] = True if we can form sum s


DP eq:
   
    dp[s] = dp[s] or dp[s - num]



    ->

    Don't take num
         - dp[s] remains what it was.
    Take num
         - If we could previously make s - num,
           then we can now make s.


"""
class Solution(object):
    def canPartition(self, nums):
        # Calculate the total sum of all numbers.
        total = sum(nums)

        # If total is odd, it cannot be split into two equal integers.
        # Example:
        # total = 11
        # 11 / 2 = 5.5
        # Impossible to partition into two subsets with equal sum.
        if total % 2:
            return False

        # We only need to find a subset whose sum is half of total.
        # If one subset is target, the remaining numbers automatically
        # sum to target as well.
        target = total // 2

        # dp[s] means:
        # "Can we form sum s using some of the numbers processed so far?"
        #
        # Example:
        # dp[5] == True
        # => there exists a subset with sum = 5
        dp = [False] * (target + 1)

        # Base case:
        # Sum 0 is always achievable by choosing nothing.
        dp[0] = True

        # Process each number one by one.
        for num in nums:

            # NOTE !!! move `backward`
            # it's the standard setting of `0/1 Knapsack`
            # to avoid `duplicated pick up`
            # example:
            # nums = [3]
            # target = 6


            # Traverse `backward` to ensure each number is used at MOST once.
            #
            # Why backward?
            #
            # Suppose num = 3
            #
            # If we go forward:
            #   dp[3] becomes True from dp[0]
            #   then dp[6] may immediately use the updated dp[3]
            #
            # This would effectively use the same 3 twice.
            #
            # Going backward prevents this problem.
            for s in range(target, num - 1, -1):

                # Two choices:
                #
                # 1. Don't take num
                #    dp[s]
                #
                # 2. Take num
                #    Need a previous subset that formed (s - num)
                #    => dp[s - num]
                #
                # Transition:
                # dp[s] = dp[s] OR dp[s - num]
                #
                # Example:
                # num = 5
                # s = 11
                #
                # If dp[6] is True,
                # then adding 5 gives sum 11,
                # so dp[11] becomes True.
                dp[s] = dp[s] or dp[s - num]

        # If target is achievable,
        # we can split the array into 
        # two equal-sum subsets.
        return dp[target]


# V0-1
# IDEA: 1D DP
class Solution(object):
    def canPartition(self, nums):
        # Calculate the grand total of all integers within the input array.
        total = sum(nums)
        
        # ODD CHECK: In Python, non-zero integers evaluate to True. If 'total % 2' is 1,
        # it means the total sum is odd. An odd sum cannot be divided into two equal integer halves.
        if total % 2:
            return False
            
        # Define the exact target subset sum value we need to find (half of the total).
        target = total // 2
        
        # Initialize a 1D DP table of size (target + 1) filled with False.
        # dp[s] will represent whether a subset sum of exactly 's' can be formed.
        dp = [False] * (target + 1)
        
        # BASE CASE: A subset sum of 0 is always possible by choosing an empty subset.
        dp[0] = True
        
        # Loop through each individual number in our input list.
        for num in nums:
            
            # CRITICAL LOOP: Scan BACKWARDS from 'target' down to the value of 'num'.
            # We must go backwards to prevent using the current 'num' multiple times 
            # in the same iteration (which would accidentally mimic an infinite-item Unbounded Knapsack).
            for s in range(target, num - 1, -1):
                
                # TRANSITION EQUATION: A sum of 's' can be formed if:
                # 1. It was ALREADY possible to form 's' without this number (dp[s] is already True), OR
                # 2. It was possible to form the remainder sum 's - num' before introducing this number.
                dp[s] = dp[s] or dp[s - num]
                
        # Return the final boolean state tracking whether the exact target half sum was achieved.
        return dp[target]



# V1
# IDEA : brute force (TLE)
# https://leetcode.com/problems/partition-equal-subset-sum/solution/
class Solution:
    def canPartition(self, nums: List[int]) -> bool:
        def dfs(nums: List[int], n: int, subset_sum: int) -> bool:
            # Base cases
            if subset_sum == 0:
                return True
            if n == 0 or subset_sum < 0:
                return False
            result = (dfs(nums, n - 1, subset_sum - nums[n - 1])
                    or dfs(nums, n - 1, subset_sum))
            return result

        # find sum of array elements
        total_sum = sum(nums)

        # if total_sum is odd, it cannot be partitioned into equal sum subsets
        if total_sum % 2 != 0:
            return False

        subset_sum = total_sum // 2
        n = len(nums)
        return dfs(nums, n - 1, subset_sum)

# V1'
# IDEA : Top Down Dynamic Programming - Memoization
# https://leetcode.com/problems/partition-equal-subset-sum/solution/
class Solution:
    def canPartition(self, nums: List[int]) -> bool:
        @lru_cache(maxsize=None)
        def dfs(nums: Tuple[int], n: int, subset_sum: int) -> bool:
            # Base cases
            if subset_sum == 0:
                return True
            if n == 0 or subset_sum < 0:
                return False
            result = (dfs(nums, n - 1, subset_sum - nums[n - 1])
                    or dfs(nums, n - 1, subset_sum))
            return result

        # find sum of array elements
        total_sum = sum(nums)

        # if total_sum is odd, it cannot be partitioned into equal sum subsets
        if total_sum % 2 != 0:
            return False

        subset_sum = total_sum // 2
        n = len(nums)
        return dfs(tuple(nums), n - 1, subset_sum)

# V1''
# IDEA : Bottom Up Dynamic Programming
# https://leetcode.com/problems/partition-equal-subset-sum/solution/
class Solution:
    def canPartition(self, nums: List[int]) -> bool:
        # find sum of array elements
        total_sum = sum(nums)

        # if total_sum is odd, it cannot be partitioned into equal sum subsets
        if total_sum % 2 != 0:
            return False
        subset_sum = total_sum // 2
        n = len(nums)

        # construct a dp table of size (n+1) x (subset_sum + 1)
        dp = [[False] * (subset_sum + 1) for _ in range(n + 1)]
        dp[0][0] = True
        for i in range(1, n + 1):
            curr = nums[i - 1]
            for j in range(subset_sum + 1):
                if j < curr:
                    dp[i][j] = dp[i - 1][j]
                else:
                    dp[i][j] = dp[i - 1][j] or dp[i - 1][j - curr]
        return dp[n][subset_sum]

# V1'''
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

# V1'''''
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