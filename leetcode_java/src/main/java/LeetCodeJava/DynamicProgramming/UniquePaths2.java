package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/unique-paths-ii/description/
/**
 * 63. Unique Paths II
 * Medium
 * Topics
 * Companies
 * Hint
 * You are given an m x n integer array grid. There is a robot initially located at the top-left corner (i.e., grid[0][0]). The robot tries to move to the bottom-right corner (i.e., grid[m - 1][n - 1]). The robot can only move either down or right at any point in time.
 *
 * An obstacle and space are marked as 1 or 0 respectively in grid. A path that the robot takes cannot include any square that is an obstacle.
 *
 * Return the number of possible unique paths that the robot can take to reach the bottom-right corner.
 *
 * The testcases are generated so that the answer will be less than or equal to 2 * 109.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: obstacleGrid = [[0,0,0],[0,1,0],[0,0,0]]
 * Output: 2
 * Explanation: There is one obstacle in the middle of the 3x3 grid above.
 * There are two ways to reach the bottom-right corner:
 * 1. Right -> Right -> Down -> Down
 * 2. Down -> Down -> Right -> Right
 * Example 2:
 *
 *
 * Input: obstacleGrid = [[0,1],[0,0]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * m == obstacleGrid.length
 * n == obstacleGrid[i].length
 * 1 <= m, n <= 100
 * obstacleGrid[i][j] is 0 or 1.
 *
 */
public class UniquePaths2 {

    // V0
//    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
//
//    }

    // V1
    // https://www.youtube.com/watch?v=pGMsrvt0fpk
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0063-unique-paths-ii.java
    public int uniquePathsWithObstacles_1(int[][] grid) {
        return dfs(
                grid,
                0,
                0,
                grid.length,
                grid[0].length,
                new int[grid.length][grid[0].length]
        );
    }

    public int dfs(int[][] grid, int i, int j, int m, int n, int[][] dp) {
        if (i < 0 || j < 0 || i >= m || j >= n || grid[i][j] == 1) {
            return 0;
        }
        if (i == m - 1 && j == n - 1) {
            dp[i][j] = 1;
            return dp[i][j];
        }
        if (dp[i][j] != 0) return dp[i][j];
        int right = dfs(grid, i, j + 1, m, n, dp);
        int left = dfs(grid, i + 1, j, m, n, dp);
        dp[i][j] = right + left;
        return dp[i][j];
    }

    // V2
    // https://leetcode.com/problems/unique-paths-ii/solutions/6106874/video-simple-solution-by-niits-cni4/
    public int uniquePathsWithObstacles_2(int[][] obstacleGrid) {
        if (obstacleGrid == null || obstacleGrid[0][0] == 1) {
            return 0;
        }

        int rows = obstacleGrid.length;
        int cols = obstacleGrid[0].length;
        int[] dp = new int[cols];
        dp[0] = 1;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (obstacleGrid[r][c] == 1) {
                    dp[c] = 0;
                } else {
                    if (c > 0) {
                        dp[c] += dp[c - 1];
                    }
                }
            }
        }

        return dp[cols - 1];
    }

    // V3
    // https://leetcode.com/problems/unique-paths-ii/solutions/23395/java-simple-and-clean-dp-solution-easy-t-s64f/
    public int uniquePathsWithObstacles_3(int[][] obstacleGrid) {
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        int[][] path = new int[m][n];

        for (int i = 0; i < m; i++) {
            if (obstacleGrid[i][0] == 1) {
                path[i][0] = 0;
                //on the first column, if there is an obstacle, the rest are blocked.
                //no need to continue.
                break;
            } else
                path[i][0] = 1;
        }

        for (int j = 0; j < n; j++) {
            if (obstacleGrid[0][j] == 1) {
                path[0][j] = 0;
                //First row, once obstacle found, the rest are blocked.
                break;
            } else
                path[0][j] = 1;
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 1)
                    path[i][j] = 0;
                else
                    path[i][j] = path[i - 1][j] + path[i][j - 1];
            }
        }
        return path[m - 1][n - 1];
    }

}
