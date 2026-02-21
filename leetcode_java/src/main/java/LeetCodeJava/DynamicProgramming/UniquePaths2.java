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
    // IDEA: DP, LC 62 (gemini)
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if (obstacleGrid == null || obstacleGrid.length == 0 || obstacleGrid[0][0] == 1) {
            return 0;
        }

        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];

        // 1. Initialize First Column
        // Once we hit an obstacle, all subsequent cells in this column stay 0
        for (int y = 0; y < m; y++) {
            /** NOTE !!!
             *
             *  we BREAK the loop directly if face an obstacle.
             *  since it's NOT possible to proceed (in y-axis direction)
             */
            if (obstacleGrid[y][0] == 1){
                break;
            }
            dp[y][0] = 1;
        }

        // 2. Initialize First Row
        // Once we hit an obstacle, all subsequent cells in this row stay 0
        for (int x = 0; x < n; x++) {
            /** NOTE !!!
             *
             *  we BREAK the loop directly if face an obstacle.
             *  since it's NOT possible to proceed (in x-axis direction)
             */
            if (obstacleGrid[0][x] == 1){
                break;
            }
            dp[0][x] = 1;
        }

        // 3. Fill the DP table
        for (int y = 1; y < m; y++) {
            for (int x = 1; x < n; x++) {
                /** NOTE !!!
                 *
                 *   ONLY if cell at (x,y) is NOT obstacle,
                 *   then we update the choice (dp table at (x,y))
                 */
                // If current cell is an obstacle, dp[y][x] remains 0
                if (obstacleGrid[y][x] == 0) {
                    dp[y][x] = dp[y - 1][x] + dp[y][x - 1];
                }
            }
        }

        return dp[m - 1][n - 1];
    }


    // V0-0-1
    // IDEA: DP, LC 62 (gpt)
    /**
     * time = O(M * N)
     * space = O(N)
     */
    public int uniquePathsWithObstacles_0_0_1(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        int[][] dp = new int[m][n];

        // If starting point has obstacle, no paths
        if (obstacleGrid[0][0] == 1)
            return 0;

        dp[0][0] = 1; // start

        // Fill first column
        for (int i = 1; i < m; i++) {
            dp[i][0] = (obstacleGrid[i][0] == 0 && dp[i - 1][0] == 1) ? 1 : 0;
        }

    // Fill first row
    /**  NOTE !!!
     *
     *
     * Yes — that loop is filling the first row of the DP table,
     * which needs special handling because it only has access to cells from the left.
     *
     * Let me break it down and then show you the space-optimized version.
     *
     * ⸻
     *
     * 🔍 Why this line is needed:
     *
     * dp[0][j] = (obstacleGrid[0][j] == 0 && dp[0][j-1] == 1) ? 1 : 0;
     *
     * This means:
     * 	•	If there’s no obstacle at obstacleGrid[0][j]
     * 	•	AND the previous cell in the same row (dp[0][j-1]) has a valid path (i.e. 1)
     * 	•	Then this cell also gets 1 (reachable)
     *
     * Once we hit an obstacle in that row, all cells to the right will be 0.
     *
     *
     */
    for (int j = 1; j < n; j++) {
            dp[0][j] = (obstacleGrid[0][j] == 0 && dp[0][j - 1] == 1) ? 1 : 0;
        }

        // Fill rest of the grid
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                /**
                 *  NOTE !!! below - obstacle handling
                 *
                 */
                if (obstacleGrid[i][j] == 0) {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                } else {
                    dp[i][j] = 0;
                }
            }
        }

        return dp[m - 1][n - 1];
    }


    // V0-0-2
    // IDEA: DP, LC 62 (GPT)
    public int uniquePathsWithObstacles_0_0_2(int[][] obstacleGrid) {

        if (obstacleGrid == null || obstacleGrid.length == 0
                || obstacleGrid[0].length == 0) {
            return 0;
        }

        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        // If start OR end is blocked → no path
        if (obstacleGrid[0][0] == 1 || obstacleGrid[m - 1][n - 1] == 1) {
            return 0;
        }

        int[][] dp = new int[m][n];

        dp[0][0] = 1;

        // First column
        for (int i = 1; i < m; i++) {
            if (obstacleGrid[i][0] == 0) {
                dp[i][0] = dp[i - 1][0];
            } else {
                dp[i][0] = 0;
            }
        }

        // First row
        for (int j = 1; j < n; j++) {
            if (obstacleGrid[0][j] == 0) {
                dp[0][j] = dp[0][j - 1];
            } else {
                dp[0][j] = 0;
            }
        }

        // Fill rest
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {

                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }

        return dp[m - 1][n - 1];
    }



    // V0-1
    // IDEA: DFS (fixed by gpt) (TLE)
    int pathCnt = 0;

    /**
     * time = O(M * N)
     * space = O(N)
     */
    public int uniquePathsWithObstacles_0_1(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        if (m == 0 || n == 0 || obstacleGrid[0][0] == 1)
            return 0;

        pathCnt = 0; // reset before each run
        boolean[][] visited = new boolean[m][n];

        uniquePathDFS(0, 0, obstacleGrid, visited);

        return pathCnt;
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public void uniquePathDFS(int x, int y, int[][] obstacleGrid, boolean[][] visited) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        // out of bounds or obstacle or already visited
        if (x < 0 || x >= n || y < 0 || y >= m || visited[y][x] || obstacleGrid[y][x] == 1) {
            return;
        }

        // reached the destination
        if (x == n - 1 && y == m - 1) {
            pathCnt += 1;
            return;
        }

        visited[y][x] = true;

        // Only move right or down
        uniquePathDFS(x + 1, y, obstacleGrid, visited);
        uniquePathDFS(x, y + 1, obstacleGrid, visited);

        visited[y][x] = false; // backtrack
    }

    // V0-2
    // IDEA: DP (gpt)
    /**
     * time = O(M * N)
     * space = O(N)
     */
    public int uniquePathsWithObstacles_0_2(int[][] obstacleGrid) {
        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;

        int[][] dp = new int[m][n];

        // If starting point has obstacle, no paths
        if (obstacleGrid[0][0] == 1)
            return 0;

        dp[0][0] = 1; // start

        // Fill first column
        for (int i = 1; i < m; i++) {
            dp[i][0] = (obstacleGrid[i][0] == 0 && dp[i - 1][0] == 1) ? 1 : 0;
        }

        // Fill first row
        for (int j = 1; j < n; j++) {
            dp[0][j] = (obstacleGrid[0][j] == 0 && dp[0][j - 1] == 1) ? 1 : 0;
        }

        // Fill rest of the grid
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (obstacleGrid[i][j] == 0) {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                } else {
                    dp[i][j] = 0;
                }
            }
        }

        return dp[m - 1][n - 1];
    }

    // V1
    // https://www.youtube.com/watch?v=pGMsrvt0fpk
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0063-unique-paths-ii.java
    /**
     * time = O(M * N)
     * space = O(N)
     */
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

    /**
     * time = O(N)
     * space = O(N)
     */
    public int dfs(int[][] grid, int i, int j, int m, int n, int[][] dp) {
        if (i < 0 || j < 0 || i >= m || j >= n || grid[i][j] == 1) {
            return 0;
        }
        if (i == m - 1 && j == n - 1) {
            dp[i][j] = 1;
            return dp[i][j];
        }
        if (dp[i][j] != 0) return dp[i][j];
        /**
         *  NOTE !!! below
         *
         *  we get `right cnt` and `left cnt` via dfs first,
         *  then current sum = left + right
         *  ...
         *  and so on
         */
        int right = dfs(grid, i, j + 1, m, n, dp);
        int left = dfs(grid, i + 1, j, m, n, dp);
        dp[i][j] = right + left;
        return dp[i][j];
    }

    // V2
    // https://leetcode.com/problems/unique-paths-ii/solutions/6106874/video-simple-solution-by-niits-cni4/
    /**
     * time = O(M * N)
     * space = O(N)
     */
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
    /**
     * time = O(M * N)
     * space = O(N)
     */
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
