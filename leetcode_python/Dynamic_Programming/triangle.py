# V0 : DEV 

# V1 
# https://blog.csdn.net/qqxx6661/article/details/78474268
# idea : 
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

# V2 
from functools import reduce
# Time:  O(m * n)
# Space: O(n)

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
