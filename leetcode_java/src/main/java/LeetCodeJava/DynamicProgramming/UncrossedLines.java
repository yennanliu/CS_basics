package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/uncrossed-lines/description/

/**
 * 1035. Uncrossed Lines
 * Medium
 *
 * You are given two integer arrays nums1 and nums2. We write the integers of nums1
 * and nums2 (in the order they are given) on two separate horizontal lines.
 *
 * We may draw connecting lines: a straight line connecting two numbers nums1[i] and
 * nums2[j] such that:
 *
 * nums1[i] == nums2[j], and
 * the line we draw does not intersect any other connecting (non-horizontal) line.
 *
 * Note that a connecting line cannot intersect even at the endpoints
 * (i.e., each number can only belong to one connecting line).
 *
 * Return the maximum number of connecting lines we can draw in this way.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,4,2], nums2 = [1,2,4]
 * Output: 2
 * Explanation: We can draw 2 uncrossed lines as in the diagram.
 * We cannot draw 3 uncrossed lines, because the line from nums1[1] = 4 to nums2[2] = 4
 * will intersect the line from nums1[2] = 2 to nums2[1] = 2.
 *
 * Example 2:
 *
 * Input: nums1 = [2,5,1,2,5], nums2 = [10,5,2,1,5,2]
 * Output: 3
 *
 * Example 3:
 *
 * Input: nums1 = [1,3,7,1,7,5], nums2 = [1,9,2,5,1]
 * Output: 2
 *
 *
 * Constraints:
 *
 * 1 <= nums1.length, nums2.length <= 500
 * 1 <= nums1[i], nums2[j] <= 2000
 *
 */
public class UncrossedLines {

    // V0
    // IDEA: 2D DP (THIS IS EXACTLY "LONGEST COMMON SUBSEQUENCE", LC 1143)
    /**
     * Non-crossing lines == the matched pairs keep the same relative order
     * in both arrays == a common subsequence.
     *
     * dp[i][j] = max lines using the first i of nums1 and the first j of nums2
     *
     * if nums1[i-1] == nums2[j-1] : dp[i][j] = dp[i-1][j-1] + 1
     * else                        : dp[i][j] = max(dp[i-1][j], dp[i][j-1])
     *
     * base: dp[i][0] = dp[0][j] = 0
     */
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int maxUncrossedLines(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    // draw a line between the two last numbers
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    // skip the last number of one side
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    // V1
    // IDEA: SAME LCS DP WITH A ROLLING 1D ROW -> O(N) SPACE
    /**
     * time = O(M * N)
     * space = O(N)
     */
    public int maxUncrossedLines_1(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        // prev = row i-1 of the 2D table
        int[] prev = new int[n + 1];

        for (int i = 1; i <= m; i++) {
            int[] cur = new int[n + 1];
            for (int j = 1; j <= n; j++) {
                if (nums1[i - 1] == nums2[j - 1]) {
                    cur[j] = prev[j - 1] + 1;
                } else {
                    cur[j] = Math.max(prev[j], cur[j - 1]);
                }
            }
            prev = cur;
        }
        return prev[n];
    }
}
