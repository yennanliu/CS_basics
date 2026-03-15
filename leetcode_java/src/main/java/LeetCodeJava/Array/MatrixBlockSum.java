package LeetCodeJava.Array;

// https://leetcode.com/problems/matrix-block-sum/description/
/**
 *  1314. Matrix Block Sum
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a m x n matrix mat and an integer k, return a matrix answer where each answer[i][j] is the sum of all elements mat[r][c] for:
 *
 * i - k <= r <= i + k,
 * j - k <= c <= j + k, and
 * (r, c) is a valid position in the matrix.
 *
 *
 * Example 1:
 *
 * Input: mat = [[1,2,3],[4,5,6],[7,8,9]], k = 1
 * Output: [[12,21,16],[27,45,33],[24,39,28]]
 * Example 2:
 *
 * Input: mat = [[1,2,3],[4,5,6],[7,8,9]], k = 2
 * Output: [[45,45,45],[45,45,45],[45,45,45]]
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n, k <= 100
 * 1 <= mat[i][j] <= 100
 *
 *
 */
public class MatrixBlockSum {

    // V0
//    public int[][] matrixBlockSum(int[][] mat, int k) {
//
//    }

    // V1
    // IDEA: DP + PREFIX SUM
    // https://leetcode.com/problems/matrix-block-sum/solutions/838172/java-prefix-sum-by-aksharkashyap-x7sd/
    public int[][] matrixBlockSum_1(int[][] mat, int K) {
        int m = mat.length, n = mat[0].length;
        int dp[][] = new int[m + 1][n + 1];
        int ans[][] = new int[m][n];
        computePrefix(dp, mat, m, n);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int r1 = Math.max(0, i - K), c1 = Math.max(0, j - K);
                int r2 = Math.min(m - 1, i + K), c2 = Math.min(n - 1, j + K);
                ans[i][j] = rangeSum(dp, r1, c1, r2, c2);
            }
        }
        return ans;
    }

    void computePrefix(int[][] dp, int[][] mat, int m, int n) {
        for (int i = 1; i <= m; i++)
            for (int j = 1; j <= n; j++)
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1] - dp[i - 1][j - 1] + mat[i - 1][j - 1];
    }

    int rangeSum(int[][] dp, int r1, int c1, int r2, int c2) {
        r1++;
        c1++;
        r2++;
        c2++;
        return dp[r2][c2] - dp[r1 - 1][c2] - dp[r2][c1 - 1] + dp[r1 - 1][c1 - 1];
    }

    // V2
    // https://leetcode.com/problems/matrix-block-sum/solutions/4488101/easy-java-solution-based-on-prefix-sum-b-a1vw/
    public int[][] matrixBlockSum_2(int[][] mat, int k) {
        int n = mat.length, m = mat[0].length;
        int[][] pref = new int[n + 1][m + 1];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                pref[i + 1][j + 1] = mat[i][j] + pref[i][j + 1] + pref[i + 1][j] - pref[i][j];
            }
        }

        int[][] ans = new int[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                int x1 = Math.max(0, i - k), y1 = Math.max(0, j - k);
                int x2 = Math.min(mat.length - 1, i + k), y2 = Math.min(mat[0].length - 1, j + k);

                ans[i][j] = pref[x2 + 1][y2 + 1] - pref[x1][y2 + 1] - pref[x2 + 1][y1] + pref[x1][y1];
            }
        }

        return ans;
    }

    // V3
    // https://leetcode.com/problems/matrix-block-sum/solutions/1484196/java-fast-solution-omn-by-zihaohan-uev0/
    public int[][] matrixBlockSum_3(int[][] mat, int k) {
        int m = mat.length;
        int n = mat[0].length;
        int[][] sum = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) {
                    sum[i][j] = mat[i][j];
                } else if (i == 0) {
                    sum[0][j] = sum[0][j - 1] + mat[i][j];
                } else if (j == 0) {
                    sum[i][0] = sum[i - 1][j] + mat[i][j];
                } else {
                    sum[i][j] = mat[i][j] + sum[i][j - 1] + sum[i - 1][j] - sum[i - 1][j - 1];
                }
            }
        }
        int[][] result = new int[m][n];
        for (int i = 0; i < m; i++) {
            int iLow = Math.max(i - k, 0);
            int iHigh = Math.min(i + k, m - 1);
            for (int j = 0; j < n; j++) {
                int jLow = Math.max(j - k, 0);
                int jHigh = Math.min(j + k, n - 1);
                if (iLow == 0 && jLow == 0) {
                    result[i][j] = sum[iHigh][jHigh];
                } else if (iLow == 0) {
                    result[i][j] = sum[iHigh][jHigh] - sum[iHigh][jLow - 1];
                } else if (jLow == 0) {
                    result[i][j] = sum[iHigh][jHigh] - sum[iLow - 1][jHigh];
                } else {
                    result[i][j] = sum[iHigh][jHigh] - sum[iHigh][jLow - 1] - sum[iLow - 1][jHigh]
                            + sum[iLow - 1][jLow - 1];
                }
            }
        }
        return result;
    }




    // V4


}
