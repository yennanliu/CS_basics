package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/paths-in-matrix-whose-sum-is-divisible-by-k/description/

/**
 * 2435. Paths in Matrix Whose Sum Is Divisible by K
 * Hard
 *
 * You are given a 0-indexed m x n integer matrix grid and an integer k. You are currently at
 * position (0, 0) and you want to reach position (m - 1, n - 1) moving only down or right.
 *
 * Return the number of paths where the sum of the elements on the path is divisible by k.
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: grid = [[5,2,4],[3,0,5],[0,7,2]], k = 3
 * Output: 2
 * Explanation: There are two paths where the sum of the elements on the path is divisible by k.
 * The first path has a sum of 5 + 2 + 4 + 5 + 2 = 18 which is divisible by 3.
 * The second path has a sum of 5 + 3 + 0 + 5 + 2 = 15 which is divisible by 3.
 *
 * Example 2:
 *
 * Input: grid = [[0,0]], k = 5
 * Output: 1
 * Explanation: The path has a sum of 0 + 0 = 0 which is divisible by 5.
 *
 * Example 3:
 *
 * Input: grid = [[7,3,4,9],[2,3,6,2],[2,3,7,0]], k = 1
 * Output: 10
 * Explanation: Every integer is divisible by 1 so the sum of the elements on every possible path is divisible by k.
 *
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 5 * 10^4
 * 1 <= m * n <= 5 * 10^4
 * 0 <= grid[i][j] <= 100
 * 1 <= k <= 50
 *
 */
public class PathsInMatrixWhoseSumIsDivisibleByK {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP ON (CELL, REMAINDER) - CARRY THE RUNNING SUM MOD k AS AN EXTRA STATE, KEEP ONLY THE PREVIOUS ROW
    /**
     * time = O(M * N * K)
     * space = O(N * K)
     */
    public int numberOfPaths(int[][] grid, int k) {
        int rows = grid.length;
        int cols = grid[0].length;

        // prev[c][r] = number of paths reaching (row - 1, c) whose sum % k == r
        int[][] prev = new int[cols][k];

        for (int r = 0; r < rows; r++) {
            int[][] cur = new int[cols][k];
            for (int c = 0; c < cols; c++) {
                int value = grid[r][c] % k;

                // base case: the start cell is a path of one element
                if (r == 0 && c == 0) {
                    cur[0][value] = 1;
                    continue;
                }

                // dp[r][c][(rem + value) % k] += dp[r-1][c][rem] + dp[r][c-1][rem]
                for (int rem = 0; rem < k; rem++) {
                    long ways = 0;
                    if (r > 0) {
                        ways += prev[c][rem];          // came from above
                    }
                    if (c > 0) {
                        ways += cur[c - 1][rem];       // came from the left
                    }
                    int newRem = (rem + value) % k;
                    cur[c][newRem] = (int) ((cur[c][newRem] + ways) % MOD);
                }
            }
            prev = cur;
        }

        return prev[cols - 1][0];
    }
}
