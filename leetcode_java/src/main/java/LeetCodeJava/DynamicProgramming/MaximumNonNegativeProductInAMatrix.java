package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-non-negative-product-in-a-matrix/description/

/**
 * 1594. Maximum Non Negative Product in a Matrix
 * Medium
 *
 * You are given a m x n matrix grid. Initially, you are located at the top-left corner (0, 0), and in each step, you can only move right or down in the matrix.
 *
 * Among all possible paths starting from the top-left corner (0, 0) and ending in the bottom-right corner (m - 1, n - 1), find the path with the maximum non-negative product. The product of a path is the product of all integers in the grid cells visited along the path.
 *
 * Return the maximum non-negative product modulo 10^9 + 7. If the maximum product is negative, return -1.
 *
 * Notice that the modulo is performed after getting the maximum product.
 *
 * Example 1:
 *
 * Input: grid = [[-1,-2,-3],[-2,-3,-3],[-3,-3,-2]]
 * Output: -1
 * Explanation: It is not possible to get non-negative product in the path from (0, 0) to (2, 2), so return -1.
 *
 * Example 2:
 *
 * Input: grid = [[1,-2,1],[1,-2,1],[3,-4,1]]
 * Output: 8
 * Explanation: Maximum non-negative product is shown (1 * 1 * -2 * -4 * 1 = 8).
 *
 * Example 3:
 *
 * Input: grid = [[1,3],[0,-4]]
 * Output: 0
 * Explanation: Maximum non-negative product is shown (1 * 0 * -4 = 0).
 *
 * Constraints:
 *
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 15
 * -4 <= grid[i][j] <= 4
 *
 */
public class MaximumNonNegativeProductInAMatrix {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP TRACKING BOTH MAX AND MIN PRODUCT (like Maximum Product Subarray)
    //       a negative cell flips signs, so the most negative product so far can become the
    //       largest one. For each cell keep
    //         maxProd[i][j] / minProd[i][j] = largest / smallest product of a path to (i, j)
    //       value >= 0: max = max(up, left) * value, min = min(up, left) * value
    //       value <  0: max = min(up, left) * value, min = max(up, left) * value
    //       |product| <= 4^30 < 2^63, so long never overflows; take the modulo only at the end.
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int maxProductPath(int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        long[][] maxProd = new long[rows][cols];
        long[][] minProd = new long[rows][cols];

        maxProd[0][0] = grid[0][0];
        minProd[0][0] = grid[0][0];
        // first row and first column have a single path each
        for (int j = 1; j < cols; j++) {
            maxProd[0][j] = maxProd[0][j - 1] * grid[0][j];
            minProd[0][j] = maxProd[0][j];
        }
        for (int i = 1; i < rows; i++) {
            maxProd[i][0] = maxProd[i - 1][0] * grid[i][0];
            minProd[i][0] = maxProd[i][0];
        }

        for (int i = 1; i < rows; i++) {
            for (int j = 1; j < cols; j++) {
                long value = grid[i][j];
                long bestBefore = Math.max(maxProd[i - 1][j], maxProd[i][j - 1]);
                long worstBefore = Math.min(minProd[i - 1][j], minProd[i][j - 1]);
                if (value >= 0) {
                    maxProd[i][j] = bestBefore * value;
                    minProd[i][j] = worstBefore * value;
                } else {
                    // multiplying by a negative swaps the roles of max and min
                    maxProd[i][j] = worstBefore * value;
                    minProd[i][j] = bestBefore * value;
                }
            }
        }

        long best = maxProd[rows - 1][cols - 1];
        return best < 0 ? -1 : (int) (best % MOD);
    }
}
