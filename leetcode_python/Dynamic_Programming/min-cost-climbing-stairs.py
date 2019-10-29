"""
On a staircase, the i-th step has some non-negative cost cost[i] assigned (0 indexed).

Once you pay the cost, you can either climb one or two steps. You need to find minimum cost to reach the top of the floor, and you can either start from the step with index 0, or the step with index 1.

Example 1:
Input: cost = [10, 15, 20]
Output: 15
Explanation: Cheapest is start on cost[1], pay that cost and go to the top.
Example 2:
Input: cost = [1, 100, 1, 1, 1, 100, 1, 1, 100, 1]
Output: 6
Explanation: Cheapest is start on cost[0], and only step on 1s, skipping cost[3].
Note:
cost will have a length in the range [2, 1000].
Every cost[i] will be an integer in the range [0, 999].
"""

# Time:  O(n)
# Space: O(1)

# On a staircase, the i-th step has some non-negative cost cost[i] assigned (0 indexed).
#
# Once you pay the cost, you can either climb one or two steps.
# You need to find minimum cost to reach the top of the floor,
# and you can either start from the step with index 0, or the step with index 1.
#
# Example 1:
# Input: cost = [10, 15, 20]
# Output: 15
# Explanation: Cheapest is start on cost[1], pay that cost and go to the top.
# Example 2:
# Input: cost = [1, 100, 1, 1, 1, 100, 1, 1, 100, 1]
# Output: 6
# Explanation: Cheapest is start on cost[0], and only step on 1s, skipping cost[3].
# Note:
#  - cost will have a length in the range [2, 1000].
#  - Every cost[i] will be an integer in the range [0, 999].

# V0

# V1
# http://bookshadow.com/weblog/2017/12/17/leetcode-min-cost-climbing-stairs/
# IDEA : DP 
# DP EQUATION:
# dp[x] = min(dp[x - 1], dp[x - 2]) + cost[x]
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
