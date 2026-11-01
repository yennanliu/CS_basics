package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-paths-with-the-given-xor-value/description/
/**
 * 3393. Count Paths With the Given XOR Value
 * Medium
 *
 * You are given a 2D integer array grid with size m x n. You are also given an integer k.
 *
 * Your task is to calculate the number of paths you can take from the top-left cell (0, 0) to the
 * bottom-right cell (m - 1, n - 1) satisfying the following constraints:
 *
 * You can either move to the right or down. Formally, from the cell (i, j) you may move to the
 * cell (i, j + 1) or to the cell (i + 1, j) if the target cell exists.
 * The XOR of all the numbers on the path must be equal to k.
 *
 * Return the total number of such paths.
 *
 * Since the answer can be very large, return the result modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: grid = [[2, 1, 5], [7, 10, 0], [12, 6, 4]], k = 11
 * Output: 3
 * Explanation:
 * The 3 paths are:
 * (0, 0) -> (1, 0) -> (2, 0) -> (2, 1) -> (2, 2)
 * (0, 0) -> (1, 0) -> (1, 1) -> (1, 2) -> (2, 2)
 * (0, 0) -> (0, 1) -> (1, 1) -> (2, 1) -> (2, 2)
 *
 * Example 2:
 *
 * Input: grid = [[1, 3, 3, 3], [0, 3, 3, 2], [3, 0, 1, 1]], k = 2
 * Output: 5
 *
 * Example 3:
 *
 * Input: grid = [[1, 1, 1, 2], [3, 0, 3, 2], [3, 0, 2, 2]], k = 10
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= m == grid.length <= 300
 * 1 <= n == grid[r].length <= 300
 * 0 <= grid[r][c] < 16
 * 0 <= k < 16
 *
 */
public class CountPathsWithTheGivenXORValue {

    private static final int MOD = 1_000_000_007;
    // every cell value is < 16, so any running XOR fits in 4 bits
    private static final int XOR_STATES = 16;

    // V0
    // IDEA: GRID DP WITH THE RUNNING XOR (16 STATES) AS AN EXTRA DIMENSION
    /**
     *  DP def:
     *     - dp[i][j][x] = number of right/down paths from (0, 0) to (i, j) whose XOR is x
     *  DP eq:
     *     - dp[i][j][x ^ grid[i][j]] += dp[i-1][j][x] + dp[i][j-1][x]
     *  Init: dp[0][0][grid[0][0]] = 1
     *  Answer: dp[m-1][n-1][k]
     *
     *  time = O(M * N * 16)
     *  space = O(M * N * 16)
     */
    public int countPathsWithXorValue(int[][] grid, int k) {
        int rows = grid.length;
        int cols = grid[0].length;
        long[][][] dp = new long[rows][cols][XOR_STATES];
        dp[0][0][grid[0][0]] = 1;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int cellValue = grid[r][c];
                for (int xor = 0; xor < XOR_STATES; xor++) {
                    // a path with XOR `xor` before this cell has XOR `xor ^ cellValue` after it
                    int newXor = xor ^ cellValue;
                    if (r > 0) {
                        dp[r][c][newXor] = (dp[r][c][newXor] + dp[r - 1][c][xor]) % MOD;
                    }
                    if (c > 0) {
                        dp[r][c][newXor] = (dp[r][c][newXor] + dp[r][c - 1][xor]) % MOD;
                    }
                }
            }
        }
        return (int) dp[rows - 1][cols - 1][k];
    }
}
