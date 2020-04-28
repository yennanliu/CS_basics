# 120. Triangle
# Medium
#
# Given a triangle, find the minimum path sum from top to bottom. Each step you may move to adjacent numbers on the row below.
#
# For example, given the following triangle
#
# [
#      [2],
#     [3,4],
#    [6,5,7],
#   [4,1,8,3]
# ]
# The minimum path sum from top to bottom is 11 (i.e., 2 + 3 + 5 + 1 = 11).
#
# Note:
#
# Bonus point if you are able to do this using only O(n) extra space, where n is the total number of rows in the triangle.


# V0

# V1 
# https://blog.csdn.net/qqxx6661/article/details/78474268
# IDEA : DP 
# dp[j] = triangle[i][j] + min(dp[j], dp[j + 1])
class Solution(object):
    def minimumTotal(self, triangle):
        """
        :type triangle: List[List[int]]
        :rtype: int
        """
        n = len(triangle)
        dp = triangle[-1]
        for i in range(n - 2, -1, -1):
            for j in range(i + 1):  # start from the second last digit 
                dp[j] = triangle[i][j] + min(dp[j], dp[j + 1])
        print(dp)
        return dp[0]

# V1'
# https://blog.csdn.net/fuxuemingzhu/article/details/82883187
# IDEA : DP
# minpath[k][i] = min( minpath[k+1][i], minpath[k+1][i+1]) + triangle[k][i]
# For the kth level:
# minpath[i] = min( minpath[i], minpath[i+1]) + triangle[k][i];
class Solution(object):
    def minimumTotal(self, triangle):
        """
        :type triangle: List[List[int]]
        :rtype: int
        """
        n = len(triangle)
        dp = triangle[-1]
        for layer in range(n - 2, -1, -1):
            for i in range(layer + 1):
                dp[i] = min(dp[i], dp[i + 1]) + triangle[layer][i]
        return dp[0]

# V2 
# Time:  O(m * n)
# Space: O(n)
from functools import reduce
class Solution(object):
    # @param triangle, a list of lists of integers
    # @return an integer
    def minimumTotal(self, triangle):
        if not triangle:
            return 0
        cur = triangle[0] + [float("inf")]
        for i in range(1, len(triangle)):
            next = []
            next.append(triangle[i][0] + cur[0])
            for j in range(1, i + 1):
                next.append(triangle[i][j] + min(cur[j - 1], cur[j]))
            cur = next + [float("inf")]
        return reduce(min, cur)
