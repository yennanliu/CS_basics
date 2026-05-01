package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-square-submatrices-with-all-ones/description/

import java.util.Arrays;

/**
 *
 * 1277. Count Square Submatrices with All Ones
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a m * n matrix of ones and zeros, return how many square submatrices have all ones.
 *
 *
 *
 * Example 1:
 *
 * Input: matrix =
 * [
 *   [0,1,1,1],
 *   [1,1,1,1],
 *   [0,1,1,1]
 * ]
 * Output: 15
 * Explanation:
 * There are 10 squares of side 1.
 * There are 4 squares of side 2.
 * There is  1 square of side 3.
 * Total number of squares = 10 + 4 + 1 = 15.
 * Example 2:
 *
 * Input: matrix =
 * [
 *   [1,0,1],
 *   [1,1,0],
 *   [1,1,0]
 * ]
 * Output: 7
 * Explanation:
 * There are 6 squares of side 1.
 * There is 1 square of side 2.
 * Total number of squares = 6 + 1 = 7.
 *
 *
 * Constraints:
 *
 * 1 <= arr.length <= 300
 * 1 <= arr[0].length <= 300
 * 0 <= arr[i][j] <= 1
 *
 *
 */
public class CountSquareSubmatricesWithAllOnes {

    // V0
//    public int countSquares(int[][] matrix) {
//
//    }


    // V1-1
    // IDEA: Bottom-up Approach
    // https://leetcode.com/problems/count-square-submatrices-with-all-ones/editorial/
    public int countSquares_1_1(int[][] matrix) {
        int row = matrix.length, col = matrix[0].length;
        int[][] dp = new int[row + 1][col + 1];
        int ans = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (matrix[i][j] == 1) {
                    dp[i + 1][j + 1] = Math.min(
                            Math.min(dp[i][j + 1], dp[i + 1][j]),
                            dp[i][j]) +
                            1;
                    ans += dp[i + 1][j + 1];
                }
            }
        }
        return ans;
    }


    // V1-2
    // IDEA: Top-Down Dynamic Programming
    // https://leetcode.com/problems/count-square-submatrices-with-all-ones/editorial/
    public int solve(int i, int j, int[][] grid, int[][] dp) {
        // If the cell lies outside the grid, return 0.
        if (i >= grid.length || j >= grid[0].length) {
            return 0;
        }
        if (grid[i][j] == 0) {
            return 0;
        }

        // If we have already visited this cell, return the memoized value.
        if (dp[i][j] != -1) {
            return dp[i][j];
        }

        // Find the answer for the cell to the right of the current cell.
        int right = solve(i, j + 1, grid, dp);
        // Find the answer for the cell to the diagonal of the current cell.
        int diagonal = solve(i + 1, j + 1, grid, dp);
        // Find the answer for the cell below the current cell.
        int below = solve(i + 1, j, grid, dp);

        return dp[i][j] = 1 + Math.min(right, Math.min(diagonal, below));
    }

    public int countSquares_1_2(int[][] grid) {
        int ans = 0;
        int[][] dp = new int[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                Arrays.fill(dp[i], -1);
            }
        }
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                ans += solve(i, j, grid, dp);
            }
        }

        return ans;
    }



    // V1-3
    // IDEA: Optimized Dynamic Programming
    // https://leetcode.com/problems/count-square-submatrices-with-all-ones/editorial/
    public int countSquares_1_3(int[][] matrix) {
        int row = matrix.length, col = matrix[0].length, result = 0, prev = 0;
        int[] dp = new int[col + 1];

        for (int i = 1; i <= row; i++) {
            for (int j = 1; j <= col; j++) {
                if (matrix[i - 1][j - 1] == 1) {
                    int temp = dp[j];
                    dp[j] = 1 + Math.min(prev, Math.min(dp[j - 1], dp[j]));
                    prev = temp;
                    result += dp[j];
                } else {
                    dp[j] = 0;
                }
            }
        }
        return result;
    }




    // V2


    // V3




}
