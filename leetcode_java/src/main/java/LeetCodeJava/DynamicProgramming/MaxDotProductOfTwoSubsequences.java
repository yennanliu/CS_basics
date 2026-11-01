package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/max-dot-product-of-two-subsequences/description/

import java.util.Arrays;

/**
 * 1458. Max Dot Product of Two Subsequences
 * Hard
 *
 * Given two arrays nums1 and nums2.
 *
 * Return the maximum dot product between non-empty subsequences of nums1 and nums2
 * with the same length.
 *
 * A subsequence of an array is a new array which is formed from the original array
 * by deleting some (can be none) of the characters without disturbing the relative
 * positions of the remaining characters. (ie, [2,3,5] is a subsequence of
 * [1,2,3,4,5] while [1,5,3] is not).
 *
 *
 * Example 1:
 *
 * Input: nums1 = [2,1,-2,5], nums2 = [3,0,-6]
 * Output: 18
 * Explanation: Take subsequence [2,-2] from nums1 and subsequence [3,-6] from nums2.
 * Their dot product is (2*3 + (-2)*(-6)) = 18.
 *
 * Example 2:
 *
 * Input: nums1 = [3,-2], nums2 = [2,-6,7]
 * Output: 21
 * Explanation: Take subsequence [3] from nums1 and subsequence [7] from nums2.
 * Their dot product is (3*7) = 21.
 *
 * Example 3:
 *
 * Input: nums1 = [-1,-1], nums2 = [1,1]
 * Output: -1
 * Explanation: Take subsequence [-1] from nums1 and subsequence [1] from nums2.
 * Their dot product is -1.
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length, nums2.length <= 500
 * -1000 <= nums1[i], nums2[i] <= 1000
 *
 */
public class MaxDotProductOfTwoSubsequences {

    // sentinel for "no non-empty pairing yet"; small enough that adding a product never overflows
    private static final int NEG = Integer.MIN_VALUE / 2;

    // V0
    // IDEA: 2D DP (LCS-LIKE)
    //       dp[i][j] = best dot product of a NON-EMPTY pairing from nums1[:i] and nums2[:j]
    //       dp[i][j] = max( dp[i-1][j],                       skip nums1[i-1]
    //                       dp[i][j-1],                       skip nums2[j-1]
    //                       max(0, dp[i-1][j-1]) + product )  pair them up
    //       the max(0, ...) drops a negative prefix, i.e. starts a fresh pairing with just this
    //       pair; that keeps the all-negative answer right (it must be non-empty, so not 0).
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int maxDotProduct(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;

        // row 0 / column 0 = an empty prefix: no valid pairing
        int[][] dp = new int[m + 1][n + 1];
        for (int[] row : dp) {
            Arrays.fill(row, NEG);
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int product = nums1[i - 1] * nums2[j - 1];
                int pairThem = Math.max(0, dp[i - 1][j - 1]) + product;
                int skipOne = Math.max(dp[i - 1][j], dp[i][j - 1]);
                dp[i][j] = Math.max(skipOne, pairThem);
            }
        }
        return dp[m][n];
    }

    // V1
    // IDEA: SAME DP, ROLLING 1D ARRAYS (row i only reads row i-1)
    /**
     * time = O(M * N)
     * space = O(N)
     */
    public int maxDotProduct_1(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;

        int[] prev = new int[n + 1];
        Arrays.fill(prev, NEG);

        for (int i = 1; i <= m; i++) {
            int[] cur = new int[n + 1];
            cur[0] = NEG;
            for (int j = 1; j <= n; j++) {
                int product = nums1[i - 1] * nums2[j - 1];
                int pairThem = Math.max(0, prev[j - 1]) + product;
                int skipOne = Math.max(prev[j], cur[j - 1]);
                cur[j] = Math.max(skipOne, pairThem);
            }
            prev = cur;
        }
        return prev[n];
    }
}
