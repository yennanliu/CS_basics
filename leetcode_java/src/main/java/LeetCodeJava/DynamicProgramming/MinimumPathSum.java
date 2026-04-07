package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-path-sum/description/

import java.util.Arrays;
import java.util.PriorityQueue;

/**
 * 64. Minimum Path Sum
 * Medium
 * Topics
 * Companies
 * Given a m x n grid filled with non-negative numbers, find a path from top left to bottom right, which minimizes the sum of all numbers along its path.
 * <p>
 * Note: You can only move either down or right at any point in time.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * <p>
 * Input: grid = [[1,3,1],[1,5,1],[4,2,1]]
 * Output: 7
 * Explanation: Because the path 1 → 3 → 1 → 1 → 1 minimizes the sum.
 * Example 2:
 * <p>
 * Input: grid = [[1,2,3],[4,5,6]]
 * Output: 12
 * <p>
 * <p>
 * Constraints:
 * <p>
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 200
 * 0 <= grid[i][j] <= 200
 */
public class MinimumPathSum {


    /**
     * NOTE !!!  LC 64 VS LC 1631
     * <p>
     * <p>
     * ✅ Leetcode 64: Minimum Path Sum
     * <p>
     * Key property:
     * •	You can only move right or down.
     * •	The cost is accumulative: you sum values along the path.
     * •	Since you can’t revisit a cell from a different direction, you don’t need visited.
     * •	DP is perfect here. Every cell is updated once with the best possible value from top or left.
     * <p>
     * ✅ No visited needed:
     * •	Each cell is filled once.
     * •	You never have to worry about improving a previous path.
     * •	No cycles. No need to guard against revisiting.
     * <p>
     * ⸻
     * <p>
     * 🔁 Leetcode 1631: Path With Minimum Effort
     * <p>
     * Key property:
     * •	You can move in all four directions (up/down/left/right).
     * •	Cost is not additive, it’s based on the maximum absolute height difference between steps.
     * •	You might find a better path to a cell after already visiting it.
     * •	This is Dijkstra-style, but the edge weight is non-linear (max of step costs).
     * <p>
     * ✅ visited is needed here:
     * •	You must revisit nodes if a better path is found.
     * •	To avoid processing worse paths, you mark nodes as visited once the minimum effort to reach them is finalized.
     * •	Without visited, you could end up adding multiple paths for the same cell and wasting computation.
     * <p>
     * ⸻
     * <p>
     * 🔍 Summary:
     * <p>
     * Problem	Move Directions	Cost Definition	Can revisit cells with better cost?	Needs visited?
     * Minimum Path Sum (64)	Right + Down only	Sum of grid values	❌ No	❌ No
     * Path With Minimum Effort (1631)	All 4 directions	Max of step differences	✅ Yes	✅ Yes
     */

    // V0
    //    public int minPathSum(int[][] grid) {
    //
    //    }

    // V0-0-1
    // 2D DP (fixed by gemini)
    public int minPathSum(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        // 1. Initialize DP table (or use grid in-place to save space)
        int[][] dp = new int[m][n];

        // 2. Base Case: Starting point
        dp[0][0] = grid[0][0];

        // 3. Initialize First Column (only one way: from above)
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }

        // 4. Initialize First Row (only one way: from left)
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j - 1] + grid[0][j];
        }

        // 5. Fill the rest of the table
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                // Min cost to reach (i, j) = grid[i][j] + min(above, left)
                dp[i][j] = grid[i][j] + Math.min(dp[i - 1][j], dp[i][j - 1]);
            }
        }

        return dp[m - 1][n - 1];
    }

    // V0-1
    // IDEA: MIN PQ + BFS (fixed by gpt)

    /**
     * time = O(N)
     * space = O(N)
     */
    public int minPathSum_0_1(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }

        int m = grid.length;
        int n = grid[0].length;

        // Min-heap by cost
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> Integer.compare(a[2], b[2]));
        pq.offer(new int[]{0, 0, grid[0][0]});

        boolean[][] visited = new boolean[m][n];
        int[][] dirs = new int[][]{{0, 1}, {1, 0}};

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int row = cur[0];
            int col = cur[1];
            int cost = cur[2];

            if (row == m - 1 && col == n - 1) {
                return cost;
            }

            if (visited[row][col]) {
                continue;
            }
            visited[row][col] = true;

            for (int[] dir : dirs) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];

                if (newRow < m && newCol < n) {
                    pq.offer(new int[]{newRow, newCol, cost + grid[newRow][newCol]});
                }
            }
        }

        return -1; // shouldn't reach here if input is valid
    }

    // V1
    // https://www.youtube.com/watch?v=pGMsrvt0fpk
    // https://github.com/neetcode-gh/leetcode/blob/main/java%2F0064-minimum-path-sum.java

    /**
     * time = O(N)
     * space = O(N)
     */
    public int minPathSum_1(int[][] grid) {
        int m = grid.length - 1;
        int n = grid[0].length - 1;
        int[][] dp = new int[m + 1][n + 1];
        for (int[] arr : dp) {
            Arrays.fill(arr, -1);
        }
        return helper(grid, m, n, dp);
    }

    /**
     * time = O(N)
     * space = O(N)
     */
    public int helper(int[][] grid, int m, int n, int[][] dp) {
        if (m == 0 && n == 0) return grid[0][0];
        if (m == 0) {
            dp[m][n] = grid[m][n] + helper(grid, m, n - 1, dp);
            return dp[m][n];
        }
        if (n == 0) {
            dp[m][n] = grid[m][n] + helper(grid, m - 1, n, dp);
            return dp[m][n];
        }
        if (dp[m][n] != -1) return dp[m][n];
        /**
         *  DP equation:
         *
         *   grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);
         */
        dp[m][n] =
                grid[m][n] +
                        Math.min(helper(grid, m, n - 1, dp), helper(grid, m - 1, n, dp));
        return dp[m][n];
    }

    // V2
    // https://leetcode.com/problems/minimum-path-sum/solutions/3345656/pythonjava-csimple-solutioneasy-to-under-occy/

    /**
     * time = O(N)
     * space = O(N)
     */
    public int minPathSum_2(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;

        for (int i = 1; i < m; i++) {
            grid[i][0] += grid[i - 1][0];
        }

        for (int j = 1; j < n; j++) {
            grid[0][j] += grid[0][j - 1];
        }

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                /**
                 *  DP equation:
                 *
                 *   grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);
                 */
                grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);
            }
        }

        return grid[m - 1][n - 1];
    }

    // V3-1
    // https://leetcode.com/problems/minimum-path-sum/solutions/3345863/image-explanation-recursion-dp-4-methods-pyw1/

    /**
     * time = O(N)
     * space = O(N)
     */
    public int minPathSum_3_1(int[][] grid) {
        int m = grid.length, n = grid[0].length;

        for (int i = 1; i < m; i++)
            grid[i][0] += grid[i - 1][0];

        for (int j = 1; j < n; j++)
            grid[0][j] += grid[0][j - 1];

        for (int i = 1; i < m; i++)
            for (int j = 1; j < n; j++)
                grid[i][j] += Math.min(grid[i - 1][j], grid[i][j - 1]);

        return grid[m - 1][n - 1];
    }

    // V3-2
    // https://leetcode.com/problems/minimum-path-sum/solutions/3345863/image-explanation-recursion-dp-4-methods-pyw1/

    /**
     * time = O(N)
     * space = O(N)
     */
    public int minPathSum_3_2(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] cur = new int[m];
        cur[0] = grid[0][0];

        for (int i = 1; i < m; i++)
            cur[i] = cur[i - 1] + grid[i][0];

        for (int j = 1; j < n; j++) {
            cur[0] += grid[0][j];
            for (int i = 1; i < m; i++)
                cur[i] = Math.min(cur[i - 1], cur[i]) + grid[i][j];
        }
        return cur[m - 1];
    }




}
