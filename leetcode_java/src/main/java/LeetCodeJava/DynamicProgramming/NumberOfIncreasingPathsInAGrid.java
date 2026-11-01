package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-increasing-paths-in-a-grid/description/

import java.util.*;

/**
 * 2328. Number of Increasing Paths in a Grid
 * Hard
 *
 * You are given an m x n integer matrix grid, where you can move from a cell to any adjacent cell in all 4 directions.
 *
 * Return the number of strictly increasing paths in the grid such that you can start from any cell and end at any cell. Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * Two paths are considered different if they do not have exactly the same sequence of visited cells.
 *
 *
 * Example 1:
 *
 * Input: grid = [[1,1],[3,4]]
 * Output: 8
 * Explanation: The strictly increasing paths are:
 * - Paths with length 1: [1], [1], [3], [4].
 * - Paths with length 2: [1 -> 3], [1 -> 4], [3 -> 4].
 * - Paths with length 3: [1 -> 3 -> 4].
 * The total number of paths is 4 + 3 + 1 = 8.
 *
 * Example 2:
 *
 * Input: grid = [[1],[2]]
 * Output: 3
 * Explanation: The strictly increasing paths are:
 * - Paths with length 1: [1], [2].
 * - Paths with length 2: [1 -> 2].
 * The total number of paths is 2 + 1 = 3.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 1000
 * 1 <= m * n <= 10^5
 * 1 <= grid[i][j] <= 10^5
 *
 */
public class NumberOfIncreasingPathsInAGrid {

    private static final int MOD = 1_000_000_007;
    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    // V0
    // IDEA: PROCESS CELLS IN INCREASING VALUE ORDER — dp[i][j] = STRICTLY INCREASING PATHS ENDING AT (i, j)
    /**
     * time = O(M * N * log(M * N))
     * space = O(M * N)
     */
    public int countPaths(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // cells sorted by value, so every smaller neighbour is final before it is read
        List<int[]> cells = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells.add(new int[]{grid[i][j], i, j});
            }
        }
        cells.sort((a, b) -> Integer.compare(a[0], b[0]));

        // every cell alone is a path of length 1
        long[][] dp = new long[rows][cols];
        long total = 0;

        for (int[] cell : cells) {
            int i = cell[1];
            int j = cell[2];
            dp[i][j] = 1;
            // extend every path ending at a strictly smaller neighbour
            for (int[] direction : DIRECTIONS) {
                int r = i + direction[0];
                int c = j + direction[1];
                if (r >= 0 && r < rows && c >= 0 && c < cols && grid[r][c] < grid[i][j]) {
                    dp[i][j] = (dp[i][j] + dp[r][c]) % MOD;
                }
            }
            total = (total + dp[i][j]) % MOD;
        }
        return (int) total;
    }

    // V1
    // IDEA: MEMOISED DFS — paths(i, j) = 1 + SUM OF paths(NEIGHBOUR) OVER STRICTLY LARGER NEIGHBOURS
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int countPaths_1(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        // memo[i][j] = increasing paths starting at (i, j); 0 = not computed (every answer is >= 1)
        long[][] memo = new long[rows][cols];
        long total = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                total = (total + pathsFrom(grid, i, j, memo)) % MOD;
            }
        }
        return (int) total;
    }

    /** Number of strictly increasing paths starting at (i, j). */
    private long pathsFrom(int[][] grid, int i, int j, long[][] memo) {
        if (memo[i][j] != 0) {
            return memo[i][j];
        }
        // the cell alone counts as one path
        long paths = 1;
        for (int[] direction : DIRECTIONS) {
            int r = i + direction[0];
            int c = j + direction[1];
            if (r >= 0 && r < grid.length && c >= 0 && c < grid[0].length && grid[r][c] > grid[i][j]) {
                paths = (paths + pathsFrom(grid, r, c, memo)) % MOD;
            }
        }
        memo[i][j] = paths;
        return paths;
    }
}
