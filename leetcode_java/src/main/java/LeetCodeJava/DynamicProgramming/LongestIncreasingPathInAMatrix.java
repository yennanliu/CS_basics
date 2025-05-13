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

    // V0-1
    // IDEA:  (DFS + Memoization) (gpt)
    private static final int[][] DIRECTIONS = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
    private int[][] memo;
    private int rows, cols;

    public int longestIncreasingPath_0_1(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
            return 0;

        rows = matrix.length;
        cols = matrix[0].length;
        memo = new int[rows][cols];

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
         *
         *
         *  -> This returns the sum of ALL increasing paths starting from
         *     four directions.
         *
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
        if (memo[row][col] > 0) {
            return memo[row][col];
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
        for (int[] dir : DIRECTIONS) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            max = Math.max(max, dfs_0_1(matrix, newRow, newCol, matrix[row][col]));
        }

        memo[row][col] = 1 + max;
        return memo[row][col];
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
