# V0  

# V1 
# https://blog.csdn.net/fuxuemingzhu/article/details/83154114
# IDEA : DP
class Solution(object):
    def uniquePathsWithObstacles(self, obstacleGrid):
        """
        :type obstacleGrid: List[List[int]]
        :rtype: int
        """
        m, n = len(obstacleGrid), len(obstacleGrid[0])
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if obstacleGrid[i - 1][j - 1] == 1:
                    dp[i][j] = 0
                else:
                    if i == j == 1:
                        dp[i][j] = 1
                    else:
                        dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
        return dp[m][n]

# V1' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83154114
# IDEA : DP
class Solution(object):
    def uniquePathsWithObstacles(self, obstacleGrid):
        """
        :type obstacleGrid: List[List[int]]
        :rtype: int
        """
        m, n = len(obstacleGrid), len(obstacleGrid[0])
        dp = [[0] * n for _ in range(m)]
        if obstacleGrid[0][0] == 0:
            dp[0][0] = 1
        for i in range(m):
            for j in range(n):
                if obstacleGrid[i][j] == 1:
                    dp[i][j] = 0
                else:
                    if i != 0:
                        dp[i][j] += dp[i - 1][j]
                    if j != 0:
                        dp[i][j] += dp[i][j - 1]
        return dp[m - 1][n - 1]

# V1'' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83154114
# IDEA : DP
class Solution(object):
    def uniquePathsWithObstacles(self, obstacleGrid):
        """
        :type obstacleGrid: List[List[int]]
        :rtype: int
        """
        m, n = len(obstacleGrid), len(obstacleGrid[0])
        dp = [[0] * n for _ in range(m)]
        if obstacleGrid[0][0] == 0:
            dp[0][0] = 1
        for i in range(m):
            for j in range(n):
                if obstacleGrid[i][j] == 0:
                    if i == j == 0:
                        continue
                    else:
                        dp[i][j] = dp[i - 1][j] + dp[i][j - 1]
        return dp[m - 1][n - 1]

# V1''' 
# https://blog.csdn.net/fuxuemingzhu/article/details/83154114
# IDEA : MEMORY SEARCH 
class Solution(object):
    def uniquePathsWithObstacles(self, obstacleGrid):
        """
        :type obstacleGrid: List[List[int]]
        :rtype: int
        """
        m, n = len(obstacleGrid), len(obstacleGrid[0])
        memo = [[0] * n for _ in range(m)]
        return self.dfs(m - 1, n - 1, obstacleGrid, memo)
    
    def dfs(self, m, n, obstacleGrid, memo): # methods of postion m, n
        if obstacleGrid[m][n] == 1:
            return 0
        if m < 0 or n < 0:
            return 0
        if m == n == 0:
            return 1
        if memo[m][n]:
            return memo[m][n]
        up = self.dfs(m - 1, n, obstacleGrid, memo)
        left = self.dfs(m, n - 1, obstacleGrid, memo)
        memo[m][n] = up + left
        return memo[m][n]

# V2
# https://blog.csdn.net/XX_123_1_RJ/article/details/81583698
# IDEA : DP 
# idea :
# dp[i][j] = 0  # if grid[i][j] == 1
# dp[i][j] = dp[i-1][j] + dp[i][j-1]  # if grid[i][j] == 0
class Solution:
    def uniquePathsWithObstacles(self, obstacleGrid):
        n, m = len(obstacleGrid), len(obstacleGrid[0])
        dp = [[0]*m for _ in range(n)]  # initize dp
        dp[0][0] = 1 if obstacleGrid[0][0] == 0 else 0
        for i in range(n):
            for j in range(m):
                if obstacleGrid[i][j] == 0:
                    if i + 1 < n:  # update the grid below 
                        dp[i + 1][j] += dp[i][j]
                    if j + 1 < m:  # update the grid at right hand side  
                        dp[i][j + 1] += dp[i][j]
                else:  # if there is  obstacle, mark it as 0 
                    dp[i][j] = 0
        return dp[n-1][m-1]
# if __name__ == '__main__':
#     obstacleGrid = [[0, 0, 0], [0, 1, 0], [0, 0, 0]]
#     solu =Solution()
#     print(solu.uniquePathsWithObstacles(obstacleGrid))

# V3 
# Time:  O(m * n)
# Space: O(m + n)
class Solution(object):
    # @param obstacleGrid, a list of lists of integers
    # @return an integer
    def uniquePathsWithObstacles(self, obstacleGrid):
        """
        :type obstacleGrid: List[List[int]]
        :rtype: int
        """
        m, n = len(obstacleGrid), len(obstacleGrid[0])

        ways = [0]*n
        ways[0] = 1
        for i in range(m):
            if obstacleGrid[i][0] == 1:
                ways[0] = 0
            for j in range(n):
                if obstacleGrid[i][j] == 1:
                    ways[j] = 0
                elif j>0:
                    ways[j] += ways[j-1]
        return ways[-1]
