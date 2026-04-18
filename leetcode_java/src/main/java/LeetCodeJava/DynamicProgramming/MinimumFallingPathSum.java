package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-falling-path-sum/description/

import java.util.Arrays;

/**
 *  931. Minimum Falling Path Sum
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given an n x n array of integers matrix, return the minimum sum of any falling path through matrix.
 *
 * A falling path starts at any element in the first row and chooses the element in the next row that is either directly below or diagonally left/right. Specifically, the next element from position (row, col) will be (row + 1, col - 1), (row + 1, col), or (row + 1, col + 1).
 *
 *
 *
 * Example 1:
 *
 *
 * Input: matrix = [[2,1,3],[6,5,4],[7,8,9]]
 * Output: 13
 * Explanation: There are two falling paths with a minimum sum as shown.
 * Example 2:
 *
 *
 * Input: matrix = [[-19,57],[-40,-5]]
 * Output: -59
 * Explanation: The falling path with a minimum sum is shown.
 *
 *
 * Constraints:
 *
 * n == matrix.length == matrix[i].length
 * 1 <= n <= 100
 * -100 <= matrix[i][j] <= 100
 *
 *
 *
 */
public class MinimumFallingPathSum {

    // V0
    // IDEA: 2D DP + BOUNDARY CHECK (gpt)
    public int minFallingPathSum(int[][] matrix) {

        int n = matrix.length;

        /** NOTE !!!
         *
         *   DP def:
         *
         *    dp[i][j] = minimum `sum` of a `falling path` that ends at
         *               cell (i, j)
         *
         */
        int[][] dp = new int[n][n];

        /** NOTE !!! we ONLY need to init 1st row */
        // init first row
        for (int x = 0; x < n; x++) {
            dp[0][x] = matrix[0][x];
        }

        /** NOTE !!! below init is NOT needed */
//        for(int y = 0; y < n; y++){
//            dp[y][0] += matrix[y][0];
//        }


        // fill dp
        /** NOTE !!!
         *
         *   1. double loop
         *   2. y starts from 1
         *   3. x starts from 0
         *
         */
        for (int y = 1; y < n; y++) {
            // NOTE !!! x start from 0
            for (int x = 0; x < n; x++) {

                /** NOTE !!!
                 *
                 *   we use 3 var to make code clean
                 *   -> up, left, right
                 */
                int up = dp[y - 1][x];
                /** NOTE !!!
                 *
                 *  need to handel `out of boundary case`
                 *  , use Integer.MAX_VALUE as default val
                 */
                int left = (x > 0) ? dp[y - 1][x - 1] : Integer.MAX_VALUE;
                /** NOTE !!!
                 *
                 *  need to handel `out of boundary case`
                 *  , use Integer.MAX_VALUE as default val
                 */
                int right = (x < n - 1) ? dp[y - 1][x + 1] : Integer.MAX_VALUE;


                /** NOTE !!!
                 *
                 *  dp[y][x] is the min(left, right, up) + matrix[y][x]
                 */
                dp[y][x] = Math.min(up, Math.min(left, right)) + matrix[y][x];
            }
        }

        // get answer from last row
        int minPath = Integer.MAX_VALUE;
        /** NOTE !!!
         *
         *  we loop bottom cell for getting `min path`
         *  e.g. y = n-1, and x = [0, n]
         */
        for (int x = 0; x < n; x++) {
            minPath = Math.min(minPath, dp[n - 1][x]);
        }

        return minPath;
    }


    // V0-1
    // IDEA: DP (gpt)
    public int minFallingPathSum_0_1(int[][] matrix) {
        int n = matrix.length;

        /** NOTE !!!
         *
         *  DP def:
         *
         *   - dp[i][j] = minimum path sum to
         *                reach `cell` (i, j)
         *
         *
         *  -------------
         *
         *  2. dp size = n !!!!
         *
         */
        int[][] dp = new int[n][n];

        // base case: first row
        for (int j = 0; j < n; j++) {
            dp[0][j] = matrix[0][j];
        }

        // fill DP
        /** NOTE !!!
         *
         *   1. both loop move `forward`
         *      -> e.g. from 1 -> n, or  0 -> n
         */
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n; j++) {
                /** NOTE !!!
                 *
                 *    DP eq:
                 *
                 *     dp[i][j] = matrix[i][j] + min(
                 *        dp[i-1][j],        // up
                 *        dp[i-1][j-1],      // up-left
                 *        dp[i-1][j+1]       // up-right
                 *      )
                 *
                 */
                int up = dp[i - 1][j];
                int left = (j > 0) ? dp[i - 1][j - 1] : Integer.MAX_VALUE;
                int right = (j < n - 1) ? dp[i - 1][j + 1] : Integer.MAX_VALUE;

                dp[i][j] = matrix[i][j] + Math.min(up, Math.min(left, right));
            }
        }

        // result = min of last row
        int res = Integer.MAX_VALUE;
        for (int j = 0; j < n; j++) {
            res = Math.min(res, dp[n - 1][j]);
        }

        return res;
    }


    // V0-2
    // IDEA: DP (GEMINI)
    public int minFallingPathSum_0_2(int[][] matrix) {
        int n = matrix.length;

        // 1. Start from the second row (y = 1) and accumulate sums
        for (int y = 1; y < n; y++) {
            for (int x = 0; x < n; x++) {
                // Potential parents from the row above
                int straight = matrix[y - 1][x];
                int left = (x > 0) ? matrix[y - 1][x - 1] : Integer.MAX_VALUE;
                int right = (x < n - 1) ? matrix[y - 1][x + 1] : Integer.MAX_VALUE;

                // Update current cell with the minimum possible path sum
                matrix[y][x] += Math.min(straight, Math.min(left, right));
            }
        }

        // 2. The answer is the minimum value in the last row
        int minPath = Integer.MAX_VALUE;
        for (int x = 0; x < n; x++) {
            minPath = Math.min(minPath, matrix[n - 1][x]);
        }

        return minPath;
    }



    // V1



    // V2
    // IDEA: DP
    // https://leetcode.com/problems/minimum-falling-path-sum/solutions/4589812/beats-9930-users-cjavapythonjavascript-e-0cg6/
    public int minFallingPathSum_2(int[][] A) {
        int m = A.length;
        int n = A[0].length;

        if (m == 1 || n == 1)
            return A[0][0];

        int[][] dp = new int[m][n];
        for (int[] row : dp) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        int ans = Integer.MAX_VALUE;

        for (int i = 0; i < A.length; ++i) {
            ans = Math.min(ans, minFallingPathSum(A, 0, i, dp));
        }

        return ans;
    }

    private int minFallingPathSum(int[][] A, int row, int col, int[][] dp) {
        int m = A.length;
        int n = A[0].length;

        if (dp[row][col] != Integer.MAX_VALUE)
            return dp[row][col];

        if (row == m - 1)
            return dp[row][col] = A[row][col];

        int left = Integer.MAX_VALUE, right = Integer.MAX_VALUE;

        if (col > 0)
            left = minFallingPathSum(A, row + 1, col - 1, dp);

        int straight = minFallingPathSum(A, row + 1, col, dp);

        if (col < n - 1)
            right = minFallingPathSum(A, row + 1, col + 1, dp);

        dp[row][col] = Math.min(left, Math.min(straight, right)) + A[row][col];

        return dp[row][col];
    }



    // V3
    // IDEA: DP
    // https://leetcode.com/problems/minimum-falling-path-sum/solutions/4589826/beats-9956-users-c-java-python-javascrip-vbcx/
    public int minFallingPathSum_3(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        int[][] dp = new int[n][m];

        for (int j = 0; j < m; j++) {
            dp[0][j] = matrix[0][j];
        }

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int ld = Integer.MAX_VALUE, rd = Integer.MAX_VALUE;
                int up = matrix[i][j] + dp[i - 1][j];

                if (j - 1 >= 0) {
                    ld = matrix[i][j] + dp[i - 1][j - 1];
                }
                if (j + 1 < m) {
                    rd = matrix[i][j] + dp[i - 1][j + 1];
                }

                dp[i][j] = Math.min(up, Math.min(ld, rd));
            }
        }

        int mini = dp[n - 1][0];
        for (int j = 1; j < m; j++) {
            mini = Math.min(mini, dp[n - 1][j]);
        }
        return mini;
    }





}
