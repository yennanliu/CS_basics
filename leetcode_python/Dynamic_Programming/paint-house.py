# There are a row of n houses, each house can be painted with one of the three colors: red, blue or green. The cost of painting each house with a certain color is different. You have to paint all the houses such that no two adjacent houses have the same color.

# The cost of painting each house with a certain color is represented by a n x 3 cost matrix. For example, costs[0][0] is the cost of painting house 0 with color red; costs[1][2] is the cost of painting house 1 with color green, and so on… Find the minimum cost to paint all houses.

# Note:
# All costs are positive integers.

# Example:

# Input: [[17,2,17],[16,16,5],[14,3,19]]
# Output: 10
# Explanation: Paint house 0 into blue, paint house 1 into green, paint house 2 into blue.
# Minimum cost: 2 + 5 + 3 = 10.


# V0 : DEV 

# V1 :  
# https://blog.csdn.net/danspace1/article/details/87854823
# DP state equation : dp[i][j] = dp[i][j] + min(dp[i - 1][(j + 1) % 3], dp[i - 1][(j + 2) % 3])
# class Solution:
#     def minCost(self, costs: 'List[List[int]]') -> 'int':
#         red, blue, green = 0, 0, 0
#         for r, b, g in costs:
#             red, blue, green = min(blue, green) + r, min(red, green) + b, min(red, blue) + g            
  
# # V1' 
# class Solution:
#     def minCost(self, costs: 'List[List[int]]') -> 'int':
#         # base case
#         if not costs: return 0
        
#         dp = costs[0][:]
#         for i in range(1, len(costs)):
#             # get the previous minimum cost
#             pre = dp[:]
#             for j in range(len(costs[0])):
#                 dp[j] = costs[i][j] + min(pre[:j] + pre[j+1:])
                
#         return min(dp)

# V2 
# Time:  O(n)
# Space: O(1)

class Solution(object):
    def minCost(self, costs):
        """
        :type costs: List[List[int]]
        :rtype: int
        """
        if not costs:
            return 0

        min_cost = [costs[0], [0, 0, 0]]

        n = len(costs)
        for i in range(1, n):
            min_cost[i % 2][0] = costs[i][0] + \
                                 min(min_cost[(i - 1) % 2][1], min_cost[(i - 1) % 2][2])
            min_cost[i % 2][1] = costs[i][1] + \
                                 min(min_cost[(i - 1) % 2][0], min_cost[(i - 1) % 2][2])
            min_cost[i % 2][2] = costs[i][2] + \
                                 min(min_cost[(i - 1) % 2][0], min_cost[(i - 1) % 2][1])

        return min(min_cost[(n - 1) % 2])

# Time:  O(n)
# Space: O(n)
class Solution2(object):
    def minCost(self, costs):
        """
        :type costs: List[List[int]]
        :rtype: int
        """
        if not costs:
            return 0

        n = len(costs)
        for i in range(1, n):
            costs[i][0] += min(costs[i - 1][1], costs[i - 1][2])
            costs[i][1] += min(costs[i - 1][0], costs[i - 1][2])
            costs[i][2] += min(costs[i - 1][0], costs[i - 1][1])

        return min(costs[n - 1])
