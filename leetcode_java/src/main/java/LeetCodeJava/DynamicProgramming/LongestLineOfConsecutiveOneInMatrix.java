package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-line-of-consecutive-one-in-matrix/

/**
 *  562. Longest Line of Consecutive One in Matrix
 *  Medium
 *
 *  Given an m x n binary matrix mat, return the length of the longest line of
 *  consecutive ones in the matrix.
 *
 *  The line could be horizontal, vertical, diagonal, or anti-diagonal.
 *
 *  Example 1:
 *
 *  Input: mat = [[0,1,1,0],[0,1,1,0],[0,0,0,1]]
 *  Output: 3
 *
 *  Example 2:
 *
 *  Input: mat = [[1,1,1,1],[0,1,1,0],[0,0,0,1]]
 *  Output: 4
 *
 *  Constraints:
 *
 *  m == mat.length
 *  n == mat[i].length
 *  1 <= m, n <= 10^4
 *  1 <= m * n <= 10^4
 *  mat[i][j] is either 0 or 1.
 */
public class LongestLineOfConsecutiveOneInMatrix {

    // V0
    // IDEA: DP with 4 directions per cell
    //  dp[i][j][0] = horizontal run ending at (i,j)
    //  dp[i][j][1] = vertical
    //  dp[i][j][2] = diagonal      (\)
    //  dp[i][j][3] = anti-diagonal (/)
    /**
     * time = O(m * n)
     * space = O(m * n)
     */
    public int longestLine(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return 0;
        }
        int m = mat.length;
        int n = mat[0].length;
        int[][][] dp = new int[m][n][4];
        int res = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    continue;
                }
                for (int k = 0; k < 4; k++) {
                    dp[i][j][k] = 1;
                }
                // horizontal : left neighbor
                if (j - 1 >= 0) {
                    dp[i][j][0] += dp[i][j - 1][0];
                }
                // vertical : up neighbor
                if (i - 1 >= 0) {
                    dp[i][j][1] += dp[i - 1][j][1];
                }
                // diagonal (\) : up-left neighbor
                if (i - 1 >= 0 && j - 1 >= 0) {
                    dp[i][j][2] += dp[i - 1][j - 1][2];
                }
                // anti-diagonal (/) : up-right neighbor
                if (i - 1 >= 0 && j + 1 < n) {
                    dp[i][j][3] += dp[i - 1][j + 1][3];
                }
                for (int k = 0; k < 4; k++) {
                    res = Math.max(res, dp[i][j][k]);
                }
            }
        }
        return res;
    }
}
