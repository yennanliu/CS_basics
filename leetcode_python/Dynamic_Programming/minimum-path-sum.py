# V0 : DEV 

# V1 
# https://blog.csdn.net/xx_123_1_rj/article/details/84571645
# IDEA : 
# DP : dp[i][j] = dp[i][j] + min(dp[i-1][j], dp[i][j-1])
# DEMO 
#    ...: grid = [
#    ...:     [1, 3, 1],
#    ...:     [1, 5, 1],
#    ...:     [4, 2, 1]]
#    ...: solu = Solution()
#    ...: print(solu.minPathSum(grid))
#    ...:     
# grid : [[1, 4, 5], [2, 7, 6], [6, 8, 7]]
# 7
class Solution:
    def minPathSum(self, grid):

        if not grid: return None
        m, n = len(grid), len(grid[0])  # row, column 

        for i in range(1, m):  ###  deal with boundary (→)  -- the 1st row, since the 1st row can only be visited via →. so we can fill them first at beginning 
            grid[i][0] += grid[i-1][0]

        for j in range(1, n):  ### deal with boundary (↓) -- the 1st column, since the 1st column can only be visited via ↓. so we can fill them first at beginning  
            grid[0][j] += grid[0][j-1]

        for i in range(1, m):  # get dp[i][j] += min(dp[i-1][j], dp[i][j-1])
            for j in range(1, n):
                grid[i][j] += min(grid[i-1][j], grid[i][j-1])
        return grid[-1][-1]

# if __name__ == '__main__':
#     grid = [
#         [1, 3, 1],
#         [1, 5, 1],
#         [4, 2, 1]]
#     solu = Solution()
#     print(solu.minPathSum(grid))

# V2 
# Time:  O(m * n)
# Space: O(m + n)
class Solution(object):
    # @param grid, a list of lists of integers
    # @return an integer
    def minPathSum(self, grid):
        sum = list(grid[0])
        for j in range(1, len(grid[0])):
            sum[j] = sum[j - 1] + grid[0][j]

        for i in range(1, len(grid)):
            sum[0] += grid[i][0]
            for j in range(1, len(grid[0])):
                sum[j] = min(sum[j - 1], sum[j]) + grid[i][j]

        return sum[-1]