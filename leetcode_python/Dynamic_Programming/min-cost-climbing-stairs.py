"""

746. Min Cost Climbing Stairs
Solved
Easy
Topics
premium lock icon
Companies
Hint
You are given an integer array cost where cost[i] is the cost of ith step on a staircase. Once you pay the cost, you can either climb one or two steps.

You can either start from the step with index 0, or the step with index 1.

Return the minimum cost to reach the top of the floor.

 

Example 1:

Input: cost = [10,15,20]
Output: 15
Explanation: You will start at index 1.
- Pay 15 and climb two steps to reach the top.
The total cost is 15.
Example 2:

Input: cost = [1,100,1,1,1,100,1,1,100,1]
Output: 6
Explanation: You will start at index 0.
- Pay 1 and climb two steps to reach index 2.
- Pay 1 and climb two steps to reach index 4.
- Pay 1 and climb two steps to reach index 6.
- Pay 1 and climb one step to reach index 7.
- Pay 1 and climb two steps to reach index 9.
- Pay 1 and climb one step to reach the top.
The total cost is 6.
 

Constraints:

2 <= cost.length <= 1000
0 <= cost[i] <= 999


"""


# V0
"""

DP def
    dp[i]: MIN cost to REACH step i

           (you pay cost[i] only when you STEP ON i and move on)

DP eq

     dp[i] = min(
                dp[i-1] + cost[i-1],      # arrive by a 1-step from i-1
                dp[i-2] + cost[i-2]       # arrive by a 2-step from i-2
             )


    -> e.g. only dp[i-1] and dp[i-2] are read -> two rolling variables,
              O(1) space

     init: dp[0] = dp[1] = 0     # you may start at step 0 OR step 1, free
     ans = dp[n]                 # "the top" is one past the last step

"""
class Solution(object):
    def minCostClimbingStairs(self, cost):
        """
        :type cost: List[int]
        :rtype: int
        """
        pass


# V0-1
# IDEA: 1D DP (gpt)
"""
NOTE !!!


DP def:

    dp[i] = minimum cost to reach step i.


DP eq:

    dp[i] = min(dp[i-1] + cost[i-1],
            dp[i-2] + cost[i-2])
    

----


1. ONLY need 1 dp

2. we can start from either idx=0 or 1, so

->

    dp[0] = 0
    dp[1] = 0


"""
class Solution(object):
    def minCostClimbingStairs(self, cost):
        """
        :type cost: List[int]
        :rtype: int
        """
        n = len(cost)

        dp = [0] * (n + 1)

        # We can start at step 0 or step 1 for free
        dp[0] = 0
        dp[1] = 0

        for i in range(2, n + 1):
            dp[i] = min(
                dp[i - 1] + cost[i - 1],
                dp[i - 2] + cost[i - 2]
            )

        return dp[n]



# V0-2
# IDEA: 1D DP (gemini)
"""

DP def
    dp[i]: MIN cost to REACH step i

           (you pay cost[i] only when you STEP ON i and move on)

DP eq

     dp[i] = min(
                dp[i-1] + cost[i-1],      # arrive by a 1-step from i-1
                dp[i-2] + cost[i-2]       # arrive by a 2-step from i-2
             )


    -> e.g. only dp[i-1] and dp[i-2] are read -> two rolling variables,
              O(1) space

     init: dp[0] = dp[1] = 0     # you may start at step 0 OR step 1, free
     ans = dp[n]                 # "the top" is one past the last step

"""
class Solution(object):
    def minCostClimbingStairs(self, cost):
        """
        :type cost: List[int]
        :rtype: int
        """
        n = len(cost)
        # dp[i] = minimum cost to reach step i
        dp = [0] * (n + 1)

        # Base cases: Starting at step 0 or step 1 incurs 0 cost
        dp[0] = 0
        dp[1] = 0

        # Fill table up to step n (the top of the staircase)
        for i in range(2, n + 1):
            dp[i] = min(dp[i - 1] + cost[i - 1], dp[i - 2] + cost[i - 2])

        return dp[n]


# V1
# http://bookshadow.com/weblog/2017/12/17/leetcode-min-cost-climbing-stairs/
# IDEA : DP 
# DP EQUATION:
# dp[x] = min(dp[x - 1], dp[x - 2]) + cost[x]
"""

DP def
    dp[i]: MIN cost to REACH step i

           (you pay cost[i] only when you STEP ON i and move on)

DP eq

     dp[i] = min(
                dp[i-1] + cost[i-1],      # arrive by a 1-step from i-1
                dp[i-2] + cost[i-2]       # arrive by a 2-step from i-2
             )


    -> e.g. only dp[i-1] and dp[i-2] are read -> two rolling variables,
              O(1) space

     init: dp[0] = dp[1] = 0     # you may start at step 0 OR step 1, free
     ans = dp[n]                 # "the top" is one past the last step

"""
# time = O(n)
# space = O(n)
class Solution(object):
    def minCostClimbingStairs(self, cost):
        """
        :type cost: List[int]
        :rtype: int
        """
        size = len(cost)
        dp = [cost[0], cost[1]]
        for x in range(2, size):
            dp.append(min(dp[x - 1], dp[x - 2]) + cost[x])
        return min(dp[-1], dp[-2])

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/79188612
"""

DP def
    dp[i]: MIN cost to REACH step i

           (you pay cost[i] only when you STEP ON i and move on)

DP eq

     dp[i] = min(
                dp[i-1] + cost[i-1],      # arrive by a 1-step from i-1
                dp[i-2] + cost[i-2]       # arrive by a 2-step from i-2
             )


    -> e.g. only dp[i-1] and dp[i-2] are read -> two rolling variables,
              O(1) space

     init: dp[0] = dp[1] = 0     # you may start at step 0 OR step 1, free
     ans = dp[n]                 # "the top" is one past the last step

"""
# time = O(n)
# space = O(n)
class Solution(object):
    def minCostClimbingStairs(self, cost):
        """
        :type cost: List[int]
        :rtype: int
        """
        costed = [0, 0]
        for i in range(2, len(cost)):
            costed.append(min(costed[i - 1] + cost[i - 1], costed[i - 2] + cost[i - 2]))
        return min(costed[-1] + cost[-1], costed[-2] + cost[-2])

# V1''
# https://blog.csdn.net/fuxuemingzhu/article/details/79188612
"""

DP def
    dp[i]: MIN cost to REACH step i

           (you pay cost[i] only when you STEP ON i and move on)

DP eq

     dp[i] = min(
                dp[i-1] + cost[i-1],      # arrive by a 1-step from i-1
                dp[i-2] + cost[i-2]       # arrive by a 2-step from i-2
             )


    -> e.g. only dp[i-1] and dp[i-2] are read -> two rolling variables,
              O(1) space

     init: dp[0] = dp[1] = 0     # you may start at step 0 OR step 1, free
     ans = dp[n]                 # "the top" is one past the last step

"""
# time = O(n)
# space = O(n)
class Solution:
    def minCostClimbingStairs(self, cost):
        """
        :type cost: List[int]
        :rtype: int
        """
        N = len(cost)
        cost.append(0)
        dp = [0] * (N + 1)
        dp[0] = cost[0]
        dp[1] = cost[1]
        for i in range(2, N + 1):
            dp[i] = min(dp[i - 1], dp[i - 2]) + cost[i]
        return dp[-1]

# V1'''
# https://www.jiuzhang.com/solution/min-cost-climbing-stairs/#tag-highlight-lang-python
# IDEA : DP
# DP EQUATION :
# dp[i] = min(dp[i-1] + cost[i-1],dp[i-2] + cost[i-2])
"""

DP def
    dp[i]: MIN cost to REACH step i

           (you pay cost[i] only when you STEP ON i and move on)

DP eq

     dp[i] = min(
                dp[i-1] + cost[i-1],      # arrive by a 1-step from i-1
                dp[i-2] + cost[i-2]       # arrive by a 2-step from i-2
             )


    -> e.g. only dp[i-1] and dp[i-2] are read -> two rolling variables,
              O(1) space

     init: dp[0] = dp[1] = 0     # you may start at step 0 OR step 1, free
     ans = dp[n]                 # "the top" is one past the last step

"""
# time = O(n)
# space = O(1)
class Solution:
    """
    @param cost: an array
    @return: minimum cost to reach the top of the floor
    """
    def minCostClimbingStairs(self, cost):
        # Write your code here
        a, b = 0, 0
        for i in range(2, len(cost) + 1):
            c = min(a + cost[i - 2], b + cost[i - 1])
            a, b = b, c
        return b

# V2
"""

DP def
    dp[i]: MIN cost to REACH step i

           (you pay cost[i] only when you STEP ON i and move on)

DP eq

     dp[i] = min(
                dp[i-1] + cost[i-1],      # arrive by a 1-step from i-1
                dp[i-2] + cost[i-2]       # arrive by a 2-step from i-2
             )


    -> e.g. only dp[i-1] and dp[i-2] are read -> two rolling variables,
              O(1) space

     init: dp[0] = dp[1] = 0     # you may start at step 0 OR step 1, free
     ans = dp[n]                 # "the top" is one past the last step

"""
# time = O(n)
# space = O(1)
class Solution(object):
    def minCostClimbingStairs(self, cost):
        """
        :type cost: List[int]
        :rtype: int
        """
        dp = [0] * 3
        for i in reversed(range(len(cost))):
            dp[i%3] = cost[i] + min(dp[(i+1)%3], dp[(i+2)%3])
        return min(dp[0], dp[1])
