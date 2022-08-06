"""

64. Minimum Path Sum
Medium

Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right, which minimizes the sum of all numbers along its path.

Note: You can only move either down or right at any point in time.

 

Example 1:


Input: grid = [[1,3,1],[1,5,1],[4,2,1]]
Output: 7
Explanation: Because the path 1 → 3 → 1 → 1 → 1 minimizes the sum.
Example 2:

Input: grid = [[1,2,3],[4,5,6]]
Output: 12
 

Constraints:

m == grid.length
n == grid[i].length
1 <= m, n <= 200
0 <= grid[i][j] <= 100

"""

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

# V1'
# IDEA : BRUTE FORCE
# https://leetcode.com/problems/minimum-path-sum/solution/
# JAVA
# public class Solution {
#     public int calculate(int[][] grid, int i, int j) {
#         if (i == grid.length || j == grid[0].length) return Integer.MAX_VALUE;
#         if (i == grid.length - 1 && j == grid[0].length - 1) return grid[i][j];
#         return grid[i][j] + Math.min(calculate(grid, i + 1, j), calculate(grid, i, j + 1));
#     }
#     public int minPathSum(int[][] grid) {
#         return calculate(grid, 0, 0);
#     }
# }

# V1''
# IDEA : DP 2D
# https://leetcode.com/problems/minimum-path-sum/solution/
# JAVA
# public class Solution {
#     public int minPathSum(int[][] grid) {
#         int[][] dp = new int[grid.length][grid[0].length];
#         for (int i = grid.length - 1; i >= 0; i--) {
#             for (int j = grid[0].length - 1; j >= 0; j--) {
#                 if(i == grid.length - 1 && j != grid[0].length - 1)
#                     dp[i][j] = grid[i][j] +  dp[i][j + 1];
#                 else if(j == grid[0].length - 1 && i != grid.length - 1)
#                     dp[i][j] = grid[i][j] + dp[i + 1][j];
#                 else if(j != grid[0].length - 1 && i != grid.length - 1)
#                     dp[i][j] = grid[i][j] + Math.min(dp[i + 1][j], dp[i][j + 1]);
#                 else
#                     dp[i][j] = grid[i][j];
#             }
#         }
#         return dp[0][0];
#     }
# }

# V1'''
# IDEA : DP 1D
# https://leetcode.com/problems/minimum-path-sum/solution/
# JAVA
#  public class Solution {
#     public int minPathSum(int[][] grid) {
#         int[] dp = new int[grid[0].length];
#         for (int i = grid.length - 1; i >= 0; i--) {
#             for (int j = grid[0].length - 1; j >= 0; j--) {
#                 if(i == grid.length - 1 && j != grid[0].length - 1)
#                     dp[j] = grid[i][j] +  dp[j + 1];
#                 else if(j == grid[0].length - 1 && i != grid.length - 1)
#                     dp[j] = grid[i][j] + dp[j];
#                 else if(j != grid[0].length - 1 && i != grid.length - 1)
#                     dp[j] = grid[i][j] + Math.min(dp[j], dp[j + 1]);
#                 else
#                     dp[j] = grid[i][j];
#             }
#         }
#         return dp[0];
#     }
# }

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