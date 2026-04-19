package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/maximum-side-length-of-a-square-with-sum-less-than-or-equal-to-threshold/description/
/**
 * 1292. Maximum Side Length of a Square with Sum Less than or Equal to Threshold
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * Given a m x n matrix mat and an integer threshold, return the maximum side-length of a square with a sum less than or equal to threshold or return 0 if there is no such square.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: mat = [[1,1,3,2,4,3,2],[1,1,3,2,4,3,2],[1,1,3,2,4,3,2]], threshold = 4
 * Output: 2
 * Explanation: The maximum side length of square with sum less than or equal to 4 is 2 as shown.
 * Example 2:
 *
 * Input: mat = [[2,2,2,2,2],[2,2,2,2,2],[2,2,2,2,2],[2,2,2,2,2],[2,2,2,2,2]], threshold = 1
 * Output: 0
 *
 *
 * Constraints:
 *
 * m == mat.length
 * n == mat[i].length
 * 1 <= m, n <= 300
 * 0 <= mat[i][j] <= 104
 * 0 <= threshold <= 105
 *
 */
public class MaximumSideLengthOfASquareWithSumLessThanOrEqualToThreshold {

    // V0
//    public int maxSideLength(int[][] mat, int threshold) {
//
//    }


    // V0-1
    // IDEA: 2D PREFIX SUM (fixed by gpt)
    public int maxSideLength_0_1(int[][] mat, int threshold) {
        int m = mat.length;
        int n = mat[0].length;

        // build prefix sum with padding (m+1 x n+1)
        int[][] prefix = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                prefix[i][j] = mat[i - 1][j - 1]
                        + prefix[i - 1][j]
                        + prefix[i][j - 1]
                        - prefix[i - 1][j - 1];
            }
        }

        int maxLen = 0;

        // try all square sizes
        for (int len = 1; len <= Math.min(m, n); len++) {
            for (int i = len; i <= m; i++) {
                for (int j = len; j <= n; j++) {

                    int sum = prefix[i][j]
                            - prefix[i - len][j]
                            - prefix[i][j - len]
                            + prefix[i - len][j - len];

                    if (sum <= threshold) {
                        maxLen = len;
                    }
                }
            }
        }

        return maxLen;
    }

    // V0-2
    // IDEA: 2D Prefix Sum + Greedy (gemini)
    public int maxSideLength_0_2(int[][] mat, int threshold) {
        int m = mat.length;
        int n = mat[0].length;
        // Use n+1, m+1 to avoid boundary checks (0th row/col stay 0)
        int[][] P = new int[m + 1][n + 1];

        // 1. Build 2D Prefix Sum table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                P[i][j] = mat[i - 1][j - 1] + P[i - 1][j] + P[i][j - 1] - P[i - 1][j - 1];
            }
        }

        int maxSide = 0;
        // 2. Greedy search: only check if we can increase the current maxSide
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Can we find a square of length (maxSide + 1) ending at (i, j)?
                int nextSide = maxSide + 1;
                if (i >= nextSide && j >= nextSide) {
                    // Formula to get sum of square with side 'nextSide' ending at (i, j)
                    int r1 = i - nextSide, c1 = j - nextSide;
                    int currentSum = P[i][j] - P[r1][j] - P[i][c1] + P[r1][c1];

                    if (currentSum <= threshold) {
                        maxSide = nextSide;
                    }
                }
            }
        }

        return maxSide;
    }



    // V1-1
    // IDEA: BINARY SEARCH
    // https://leetcode.com/problems/maximum-side-length-of-a-square-with-sum-less-than-or-equal-to-threshold/editorial/
    public int maxSideLength_1_1(int[][] mat, int threshold) {
        int m = mat.length;
        int n = mat[0].length;
        int[][] P = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                P[i][j] = P[i - 1][j] +
                        P[i][j - 1] -
                        P[i - 1][j - 1] +
                        mat[i - 1][j - 1];
            }
        }

        int l = 1;
        int r = Math.min(m, n);
        int ans = 0;
        while (l <= r) {
            int mid = (l + r) / 2;
            boolean find = false;
            for (int i = 1; i <= m - mid + 1; i++) {
                for (int j = 1; j <= n - mid + 1; j++) {
                    int sum = P[i + mid - 1][j + mid - 1] -
                            P[i - 1][j + mid - 1] -
                            P[i + mid - 1][j - 1] +
                            P[i - 1][j - 1];
                    if (sum <= threshold) {
                        find = true;
                        break;
                    }
                }
                if (find)
                    break;
            }
            if (find) {
                ans = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return ans;
    }


    // V1-2
    // IDEA: Enumeration + Optimization
    // https://leetcode.com/problems/maximum-side-length-of-a-square-with-sum-less-than-or-equal-to-threshold/editorial/
    private int getRect(int[][] P, int x1, int y1, int x2, int y2) {
        return P[x2][y2] - P[x1 - 1][y2] - P[x2][y1 - 1] + P[x1 - 1][y1 - 1];
    }

    public int maxSideLength_1_2(int[][] mat, int threshold) {
        int m = mat.length;
        int n = mat[0].length;
        int[][] P = new int[m + 1][n + 1];
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                P[i][j] = P[i - 1][j] +
                        P[i][j - 1] -
                        P[i - 1][j - 1] +
                        mat[i - 1][j - 1];
            }
        }

        int r = Math.min(m, n);
        int ans = 0;
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                for (int c = ans + 1; c <= r; ++c) {
                    if (i + c - 1 <= m &&
                            j + c - 1 <= n &&
                            getRect(P, i, j, i + c - 1, j + c - 1) <= threshold) {
                        ++ans;
                    } else {
                        break;
                    }
                }
            }
        }
        return ans;
    }




    // V2




}
