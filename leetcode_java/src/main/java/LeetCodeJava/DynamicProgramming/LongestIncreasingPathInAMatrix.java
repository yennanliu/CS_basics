package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-increasing-path-in-a-matrix/description/

import java.util.LinkedList;
import java.util.Queue;

/**
 * 329. Longest Increasing Path in a Matrix
 * Hard
 * Topics
 * Companies
 * Given an m x n integers matrix, return the length of the longest increasing path in matrix.
 *
 * From each cell, you can either move in four directions: left, right, up, or down. You may not move diagonally or move outside the boundary (i.e., wrap-around is not allowed).
 *
 *
 *
 * Example 1:
 *
 *
 * Input: matrix = [[9,9,4],[6,6,8],[2,1,1]]
 * Output: 4
 * Explanation: The longest increasing path is [1, 2, 6, 9].
 * Example 2:
 *
 *
 * Input: matrix = [[3,4,5],[3,2,6],[2,2,1]]
 * Output: 4
 * Explanation: The longest increasing path is [3, 4, 5, 6]. Moving diagonally is not allowed.
 * Example 3:
 *
 * Input: matrix = [[1]]
 * Output: 1
 *
 *
 * Constraints:
 *
 * m == matrix.length
 * n == matrix[i].length
 * 1 <= m, n <= 200
 * 0 <= matrix[i][j] <= 231 - 1
 *
 *
 */
public class LongestIncreasingPathInAMatrix {

    // V0
//    public int longestIncreasingPath(int[][] matrix) {
//
//    }

    /** NOTE !!! (WRONG code, just for reference)
     *
     *   below is WRONG,
     *   -> we CAN'T do without DFS, by checking
     *   the max increase path on 4 directions move
     */
//    public int longestIncreasingPath(int[][] matrix) {
//        // edge
//
//        int l = matrix.length;
//        int w = matrix[0].length;
//
//        // ???
//        int[][] dp = new int[l][w]; // ??
//        // init
//        for(int x = 0; x < w; x++){
//            dp[0][x] = 1;
//        }
//        for(int y = 0; y < l; y++){
//            dp[y][0] = 1;
//        }
//
//        // ???
//        int maxIncreaseLen = 1;
//        for(int y = 0; y < l; y++){
//            for(int x = 0; x < w; x++){
//                // ????
//                if ( matrix[y][x] > matrix[y-1][x] || matrix[y][x] > matrix[y][x-1] ){
//                    dp[y][x] = Math.max(dp[y-1][x] , dp[y][x-1]) + 1;
//                    maxIncreaseLen = Math.max(maxIncreaseLen, dp[y][x]);
//                }
//            }
//        }
//
//        return maxIncreaseLen;
//    }


    // V0-1
    // IDEA: DFS + DP (Memoization) (gemini)
    // 4 directions: Up, Down, Left, Right
    private static final int[][] DIRECTIONS = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
    /** NOTE !!!
     *
     *  memo is our DP (Memoization)
     *
     *  ----
     *
     *  NOTE!!!
     *
     *   memo[r][c]  is the result of dfs call e.g. cache
     */
    private int[][] memo;

    public int longestIncreasingPath_0_1(int[][] matrix) {
        if (matrix == null || matrix.length == 0)
            return 0;

        int rows = matrix.length;
        int cols = matrix[0].length;
        memo = new int[rows][cols];
        int maxLen = 0;

        // Start DFS from every single cell
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maxLen = Math.max(maxLen, dfs(matrix, i, j));
            }
        }
        return maxLen;
    }


    /** NOTE !!!
     *
     *  we don't pass memo as a dfs param,
     *  but set it as global var, so dfs can access and operate it still
     */
    /**  NOTE !!!
     *
     *  memo[r][c]  is the result of dfs call e.g. cache
     *
     *  Yes, exactly! `memo[r][c]`
     *  is the **cache** that stores the
     *  "best possible answer" starting
     *  from that specific coordinate.
     *
     * Without it, the algorithm would be **exponential** ()
     * because it would recalculate the same paths over and over again.
     * With it, the algorithm becomes **linear** ().
     *
     * ### 🧠 How the Cache Works
     *
     * Think of `memo[r][c]` as a notebook where you write down the answer to the question: *"If I am standing at cell , what is the longest path I can possibly take from here?"*
     *
     * 1. **Check the notebook:** Before doing any work, the DFS checks `if (memo[r][c] != 0)`. If there’s a number there, it just returns it instantly.
     * 2. **Do the work:** If the notebook is empty (`0`), the DFS looks at all 4 neighbors.
     * 3. **Write it down:** Once it finds the longest path from that cell, it saves it: `memo[r][c] = max`.
     *
     * ---
     *
     * ### 🔍 Execution Trace Example
     *
     * Imagine a small matrix: `[[1, 2], [4, 3]]`
     *
     * * **Step 1:** You call `dfs(0, 0)` for the value **1**.
     * * **Step 2:** **1** sees **2** is larger, so it calls `dfs(0, 1)`.
     * * **Step 3:** **2** sees **3** is larger, so it calls `dfs(1, 1)`.
     * * **Step 4:** **3** has no larger neighbors. It returns **1**.
     * * `memo[1][1]` is now **1**.
     *
     *
     * * **Step 5:** Back in **2**'s call, it takes **3**'s result (1) and adds 1.
     * * `memo[0][1]` is now **2**.
     *
     *
     * * **Step 6:** Back in **1**'s call, it takes **2**'s result (2) and adds 1.
     * * `memo[0][0]` is now **3**.
     *
     *
     *
     * Now, if another path (like starting from **4**) ever tries to look at **1**, **2**, or **3**, it doesn't have to recalculate anything! It just reads the `memo` table.
     *
     * ---
     *
     * ### 📊 DP vs. Memoization
     *
     * | Concept | Approach | Analogy |
     * | --- | --- | --- |
     * | **Tabulation (Iterative)** | Bottom-Up (starting from the smallest subproblems) | Building a house from the foundation up. |
     * | **Memoization (Recursive)** | Top-Down (starting from the big problem and breaking it down) | Writing down the answer to a math problem so you don't have to solve it again. |
     *
     * **In this specific problem, Memoization is much easier because the "order" of the foundation isn't obvious (it depends on which numbers are smaller).**
     *
     */
    private int dfs(int[][] matrix, int r, int c) {
        // If we've already calculated this cell, return the cached result
        if (memo[r][c] != 0)
            return memo[r][c];

        /** NOTE !!!
         *
         *  we init a `local max len` called max
         *
         *   1. we can maintain a `local max len` within DFS call
         *   2. we can update cache (memo) with max val
         *   3. we can return max as the DFS return value
         */
        // Every cell is a path of at least length 1
        int max = 1;

        for (int[] dir : DIRECTIONS) {
            int nr = r + dir[0];
            int nc = c + dir[1];

            // Check boundaries AND if the neighbor is strictly INCREASING
            if (nr >= 0 && nr < matrix.length && nc >= 0 && nc < matrix[0].length
                    && matrix[nr][nc] > matrix[r][c]) {

                /** NOTE !!!
                 *
                 *  1. tmp len: len
                 *  2. update local max val (max)
                 */
                int len = 1 + dfs(matrix, nr, nc);
                max = Math.max(max, len);
            }
        }

        /** NOTE !!!
         *
         *  update cache (dp) with `max` val
         */
        // Cache the result before returning
        memo[r][c] = max;
        /** NOTE !!!
         *
         *  return `max` as DFS result
         */
        return max;
    }


    // V0-2
    // IDEA:  (DFS + Memoization) (gpt)
    private static final int[][] DIRECTIONS_0_2 = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
    private int[][] memo_0_2;
    private int rows, cols;

    public int longestIncreasingPath_0_2(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
            return 0;

        rows = matrix.length;
        cols = matrix[0].length;
        memo_0_2 = new int[rows][cols];

        /**
         *  NOTE !!!!
         *
         *   via below trick, we can get MAX val on every single `dir` choice
         *   (e.g. move `left or right or up or down)
         *
         *   and compare with maxLen, so we can get maxLen path length
         *   of the `initial cell`
         *
         */
        int maxLen = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maxLen = Math.max(maxLen, dfs_0_1(matrix, i, j, Integer.MIN_VALUE));
            }
        }

        // NOTE !!! below is WRONG (which sum `all path len` per different dirs)
        /**
         *
         * 1): Recursive sum of all paths (your version)
         *
         *
         * return 1 + getMaxIncreaseLen(...) +
         *            getMaxIncreaseLen(...) +
         *            getMaxIncreaseLen(...) +
         *            getMaxIncreaseLen(...);
         *
         *  -> This returns the sum of ALL increasing paths starting from
         *     four directions.
         *
         */
        return maxLen;
    }

    private int dfs_0_1(int[][] matrix, int row, int col, int prevVal) {
        // Out of bounds or not increasing
        if (row < 0 || col < 0 || row >= rows || col >= cols || matrix[row][col] <= prevVal) {
            return 0;
        }

        // Already computed
        if (memo_0_2[row][col] > 0) {
            return memo_0_2[row][col];
        }

        /**
         *  https://youtu.be/wCc_nd-GiEc?feature=shared&t=958
         *
         *
         * NOTE !!!  below code is similar as :
         *
         *  max = Math.max(max, dfs_0_1(matrix, row + 1, col, matrix[row + 1][col]));
         *  max = Math.max(max, dfs_0_1(matrix, row - 1, col, matrix[row - 1][col]));
         *  max = Math.max(max, dfs_0_1(matrix, row, col + 1, matrix[row][col + 1]));
         *  max = Math.max(max, dfs_0_1(matrix, row, col - 1, matrix[row + 1][col - 1]));
         *
         */
        int max = 0;
        for (int[] dir : DIRECTIONS_0_2) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            max = Math.max(max, dfs_0_1(matrix, newRow, newCol, matrix[row][col]));
        }

        memo_0_2[row][col] = 1 + max;
        return memo_0_2[row][col];
    }


    // V1-1
    // https://neetcode.io/problems/longest-increasing-path-in-matrix
    // IDEA: Recursion
    int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    private int dfs(int[][] matrix, int r, int c, int prevVal) {
        int ROWS = matrix.length, COLS = matrix[0].length;
        if (r < 0 || r >= ROWS || c < 0 ||
                c >= COLS || matrix[r][c] <= prevVal) {
            return 0;
        }

        int res = 1;
        for (int[] d : directions) {
            res = Math.max(res, 1 + dfs(matrix, r + d[0],
                    c + d[1], matrix[r][c]));
        }
        return res;
    }

    public int longestIncreasingPath_1_1(int[][] matrix) {
        int ROWS = matrix.length, COLS = matrix[0].length;
        int LIP = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                LIP = Math.max(LIP, dfs(matrix, r, c, Integer.MIN_VALUE));
            }
        }
        return LIP;
    }

    // V1-2
    // https://neetcode.io/problems/longest-increasing-path-in-matrix
    // IDEA: Dynamic Programming (Top-Down)
    //int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
    int[][] dp;
    
    private int dfs_1_2(int[][] matrix, int r, int c, int prevVal) {
        int ROWS = matrix.length, COLS = matrix[0].length;
        if (r < 0 || r >= ROWS || c < 0 ||
                c >= COLS || matrix[r][c] <= prevVal) {
            return 0;
        }
        if (dp[r][c] != -1) return dp[r][c];

        int res = 1;
        for (int[] d : directions) {
            res = Math.max(res, 1 + dfs_1_2(matrix, r + d[0],
                    c + d[1], matrix[r][c]));
        }
        return dp[r][c] = res;
    }

    public int longestIncreasingPath_1_2(int[][] matrix) {
        int ROWS = matrix.length, COLS = matrix[0].length;
        int LIP = 0;
        dp = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                dp[i][j] = -1;
            }
        }
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                LIP = Math.max(LIP, dfs_1_2(matrix, r, c, Integer.MIN_VALUE));
            }
        }
        return LIP;
    }

    // V1-3
    // https://neetcode.io/problems/longest-increasing-path-in-matrix
    // IDEA:  Topological Sort (Kahn's Algorithm)
    public int longestIncreasingPath_1_3(int[][] matrix) {
        int ROWS = matrix.length, COLS = matrix[0].length;
        int[][] indegree = new int[ROWS][COLS];
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int r = 0; r < ROWS; ++r) {
            for (int c = 0; c < COLS; ++c) {
                for (int[] d : directions) {
                    int nr = r + d[0], nc = c + d[1];
                    if (nr >= 0 && nr < ROWS && nc >= 0 &&
                            nc < COLS && matrix[nr][nc] < matrix[r][c]) {
                        indegree[r][c]++;
                    }
                }
            }
        }

        Queue<int[]> q = new LinkedList<>();
        for (int r = 0; r < ROWS; ++r) {
            for (int c = 0; c < COLS; ++c) {
                if (indegree[r][c] == 0) {
                    q.offer(new int[]{r, c});
                }
            }
        }

        int LIS = 0;
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; ++i) {
                int[] node = q.poll();
                int r = node[0], c = node[1];
                for (int[] d : directions) {
                    int nr = r + d[0], nc = c + d[1];
                    if (nr >= 0 && nr < ROWS && nc >= 0 &&
                            nc < COLS && matrix[nr][nc] > matrix[r][c]) {
                        if (--indegree[nr][nc] == 0) {
                            q.offer(new int[]{nr, nc});
                        }
                    }
                }
            }
            LIS++;
        }
        return LIS;
    }


    // V2



}
