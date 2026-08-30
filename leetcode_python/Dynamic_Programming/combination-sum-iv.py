"""

377. Combination Sum IV
Solved
Medium
Topics
premium lock icon
Companies
Given an array of distinct integers nums and a target integer target, return the number of possible combinations that add up to target.

The test cases are generated so that the answer can fit in a 32-bit integer.

 

Example 1:

Input: nums = [1,2,3], target = 4
Output: 7
Explanation:
The possible combination ways are:
(1, 1, 1, 1)
(1, 1, 2)
(1, 2, 1)
(1, 3)
(2, 1, 1)
(2, 2)
(3, 1)
Note that different sequences are counted as different combinations.
Example 2:

Input: nums = [9], target = 3
Output: 0
 

Constraints:

1 <= nums.length <= 200
1 <= nums[i] <= 1000
All the elements of nums are unique.
1 <= target <= 1000
 

Follow up: What if negative numbers are allowed in the given array? How does it change the problem? What limitation we need to add to the question to allow negative numbers?


"""

# V0
# IDEA: 1D DP (`combination` pattern)
# https://yennj12.js.org/CS_basics/cheatsheets/knapsack.html#loop-order-combinations-vs-permutations
# https://github.com/yennanliu/CS_basics/issues/103
"""
NOTE !!!


the `loop` ordering

-> LC 518 VS 377


- Unbounded Knapsack — Count
    
    ```
    for num in nums:
        for s in range(num, target + 1):
            dp[s] += dp[s - num]
    ```


- Combination count (order DOES matter)


   ```
    for j in range(target + 1):
        for coin in nums:
            if j - coin >= 0:
                dp[j] += dp[j - coin]
   ```

"""
class Solution(object):
    def combinationSum4(self, nums, target):

        # dp[i] = number of combinations to build sum i
        # where ORDER MATTERS
        dp = [0] * (target + 1)

        # One way to build 0:
        # choose nothing
        dp[0] = 1

        # Build every target from small -> large
        for i in range(1, target + 1):

            for num in nums:

                if i >= num:
                    dp[i] += dp[i - num]

        return dp[target]


# V0-0-1
# IDEA: 1D DP (`combination` pattern)
# https://yennj12.js.org/CS_basics/cheatsheets/knapsack.html#loop-order-combinations-vs-permutations
# https://github.com/yennanliu/CS_basics/issues/103
class Solution(object):
    def combinationSum4(self, nums, target):
        # edge
        if target == 0:
            return 1

        if not nums or len(nums) == 0:
            return 0

        if len(nums) == 1:
            return 1 if target % nums[0] == 0 else 0

        dp = [0] * (target + 1)

        dp[0] = 1

        for j in range(target + 1):
            for coin in nums:
                if j - coin >= 0:
                    dp[j] += dp[j - coin]


        return dp[target]


# VO 
# IDEA : DP 
# dp[x+y] = dp[x+y] + dp[x] (for all x in range(target + 1), for all y in nums)
# -> dp[x] : # of ways can make sum = x from sub-nums 
# time = O(target * n), n = len(nums)
# space = O(target)
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
"""

DP def
    dp[i]: number of ORDERED combinations (permutations count separately)

           of nums that add up to i

           -> target is the OUTER loop, which is what makes order matter

DP eq

     for i in range(1, target + 1):
         for num in nums:
             if i >= num:

                 dp[i] += dp[i - num]


    -> e.g.
         dp[x + y] += dp[x]     for every x, and every y in nums

         (1,3) and (3,1) are counted as 2 different combinations

     init: dp[0] = 1
     ans = dp[target]

"""
# time = O(target * n), n = len(nums)
# space = O(target)
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
"""

DP def
    dp[i]: number of ORDERED combinations (permutations count separately)

           of nums that add up to i

           -> target is the OUTER loop, which is what makes order matter

DP eq

     for i in range(1, target + 1):
         for num in nums:
             if i >= num:

                 dp[i] += dp[i - num]


    -> e.g.
         dp[x + y] += dp[x]     for every x, and every y in nums

         (1,3) and (3,1) are counted as 2 different combinations

     init: dp[0] = 1
     ans = dp[target]

"""
# time = O(target * n), n = len(nums)
# space = O(target)
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
"""

DP def
    dp[i]: number of ORDERED combinations (permutations count separately)

           of nums that add up to i

           -> target is the OUTER loop, which is what makes order matter

DP eq

     for i in range(1, target + 1):
         for num in nums:
             if i >= num:

                 dp[i] += dp[i - num]


    -> e.g.
         dp[x + y] += dp[x]     for every x, and every y in nums

         (1,3) and (3,1) are counted as 2 different combinations

     init: dp[0] = 1
     ans = dp[target]

"""
# time = O(target * n), n = len(nums)
# space = O(target)
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
"""

DP def
    dp[i]: number of ORDERED combinations (permutations count separately)

           of nums that add up to i

           -> target is the OUTER loop, which is what makes order matter

DP eq

     for i in range(1, target + 1):
         for num in nums:
             if i >= num:

                 dp[i] += dp[i - num]


    -> e.g.
         dp[x + y] += dp[x]     for every x, and every y in nums

         (1,3) and (3,1) are counted as 2 different combinations

     init: dp[0] = 1
     ans = dp[target]

"""
# time = O(target * n), n = len(nums)
# space = O(target)
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
"""

DP def
    dp[i]: number of ORDERED combinations (permutations count separately)

           of nums that add up to i

           -> target is the OUTER loop, which is what makes order matter

DP eq

     for i in range(1, target + 1):
         for num in nums:
             if i >= num:

                 dp[i] += dp[i - num]


    -> e.g.
         dp[x + y] += dp[x]     for every x, and every y in nums

         (1,3) and (3,1) are counted as 2 different combinations

     init: dp[0] = 1
     ans = dp[target]

"""
# time = O(target * n), n = len(nums)
# space = O(target)
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
