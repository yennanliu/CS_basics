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


    // V0-1
    // IDEA:  2D Prefix Sum (gemini)
    /**
     *
     * For **LC 1314 (Matrix Block Sum)**,
     * the most efficient approach is to
     * use a **2D Prefix Sum** (also known as a Summed-Area Table).
     *
     * Without prefix sums, calculating the sum for every cell would take $O(M \cdot N \cdot K^2)$. With prefix sums, we can calculate each block sum in $O(1)$ time after an $O(M \cdot N)$ preprocessing step.
     *
     * ### 🛠️ The 2D Prefix Sum Formula
     *
     * For any rectangle defined by $(r1, c1)$ and $(r2, c2)$, the sum is:
     *
     *
     * $$\text{Sum} = P[r2][c2] - P[r1-1][c2] - P[r2][c1-1] + P[r1-1][c1-1]$$
     *
     *
     * *(where $P$ is the prefix sum matrix).*
     *
     *
     * ---
     *
     * ### 🔍 Key Implementation Details
     *
     * 1. **The `+1` Offset**: We use a `pref` matrix of size `(m+1, n+1)`. This allows `pref[0][j]` and `pref[i][0]` to be zero, acting as a "padding" that prevents `IndexOutOfBounds` exceptions when calculating the top or left edges.
     * 2. **Clamping**: `Math.max(0, ...)` and `Math.min(m-1, ...)` ensure that even if $K$ is very large, we don't look outside the actual matrix bounds.
     * 3. **The Subtraction/Addition**: In the formula, we subtract the top and left regions, but that "doubles-subtracts" the top-left corner. We add `pref[r1][c1]` back at the end to correct this.
     *
     * ### 📊 Complexity Analysis
     *
     * | Metric | Complexity | Explanation |
     * | --- | --- | --- |
     * | **Time** | **$O(M \cdot N)$** | One pass to build the prefix sum, one pass to fill the result matrix. |
     * | **Space** | **$O(M \cdot N)$** | To store the prefix sum matrix. |
     *
     *
     */
    public int[][] matrixBlockSum_0_1(int[][] mat, int k) {
        int m = mat.length;
        int n = mat[0].length;

        // 1. Build the 2D Prefix Sum matrix (size m+1 x n+1 for easier boundary handling)
        int[][] pref = new int[m + 1][n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                pref[i + 1][j + 1] = mat[i][j] + pref[i][j + 1] + pref[i + 1][j] - pref[i][j];
            }
        }

        int[][] res = new int[m][n];

        // 2. Calculate sum for each block [i-k, j-k] to [i+k, j+k]
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // Define boundaries and clamp them to matrix size
                int r1 = Math.max(0, i - k);
                int c1 = Math.max(0, j - k);
                int r2 = Math.min(m - 1, i + k);
                int c2 = Math.min(n - 1, j + k);

                // Use prefix sum formula (adjusting for 1-based pref matrix)
                // Area = P[r2+1][c2+1] - P[r1][c2+1] - P[r2+1][c1] + P[r1][c1]
                res[i][j] = pref[r2 + 1][c2 + 1] - pref[r1][c2 + 1] - pref[r2 + 1][c1] + pref[r1][c1];
            }
        }

        return res;
    }


    // V0-2
    // IDEA: GPT
    public int[][] matrixBlockSum_0_2(int[][] mat, int k) {
        int m = mat.length, n = mat[0].length;

        // prefix sum matrix
        int[][] pre = new int[m + 1][n + 1];

        // build prefix sum
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                pre[i][j] = mat[i - 1][j - 1]
                        + pre[i - 1][j]
                        + pre[i][j - 1]
                        - pre[i - 1][j - 1];
            }
        }

        int[][] res = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                int r1 = Math.max(0, i - k);
                int c1 = Math.max(0, j - k);
                int r2 = Math.min(m - 1, i + k);
                int c2 = Math.min(n - 1, j + k);

                res[i][j] = pre[r2 + 1][c2 + 1]
                        - pre[r1][c2 + 1]
                        - pre[r2 + 1][c1]
                        + pre[r1][c1];
            }
        }

        return res;
    }




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
