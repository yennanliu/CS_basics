# V0 :DEV 

# V1 
# https://blog.csdn.net/XX_123_1_RJ/article/details/81583698
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

# V2 
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
