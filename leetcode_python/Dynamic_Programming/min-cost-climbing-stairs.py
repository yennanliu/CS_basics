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
class Solution(object):
    def minCostClimbingStairs(self, cost):
        """
        :type cost: List[int]
        :rtype: int
        """
        pass


# V0-1


# V0-2 


# V1
# http://bookshadow.com/weblog/2017/12/17/leetcode-min-cost-climbing-stairs/
# IDEA : DP 
# DP EQUATION:
# dp[x] = min(dp[x - 1], dp[x - 2]) + cost[x]
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
