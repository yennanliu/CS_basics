package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-ascii-delete-sum-for-two-strings/

/**
 *  712. Minimum ASCII Delete Sum for Two Strings
 *  Medium
 *
 *  Given two strings s1 and s2, return the lowest ASCII sum of deleted
 *  characters to make two strings equal.
 *
 *  Example 1:
 *    Input: s1 = "sea", s2 = "eat"
 *    Output: 231
 *    Explanation: Deleting "s" from "sea" adds the ASCII value of "s" (115)
 *                 to the sum. Deleting "t" from "eat" adds 116 to the sum.
 *                 At the end, both strings are equal, and 115 + 116 = 231 is
 *                 the minimum sum possible to achieve this.
 *
 *  Example 2:
 *    Input: s1 = "delete", s2 = "leet"
 *    Output: 403
 *
 *  Constraints:
 *    - 1 <= s1.length, s2.length <= 1000
 *    - s1 and s2 consist of lowercase English letters.
 */
public class MinimumASCIIDeleteSumForTwoStrings {

    // V0
    // IDEA: 2D DP (edit-distance shape) - dp[i][j] = min delete sum for s1[0..i) and s2[0..j)
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public int minimumDeleteSum(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            dp[i][0] = dp[i - 1][0] + s1.charAt(i - 1);
        }
        for (int j = 1; j <= n; j++) {
            dp[0][j] = dp[0][j - 1] + s2.charAt(j - 1);
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(
                            dp[i - 1][j] + s1.charAt(i - 1),
                            dp[i][j - 1] + s2.charAt(j - 1));
                }
            }
        }
        return dp[m][n];
    }

    // V1
    // IDEA: same recurrence, rolling 1D array to cut space
    /**
     * time = O(M * N)
     * space = O(N)
     */
    public int minimumDeleteSum_1(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[] prev = new int[n + 1];
        for (int j = 1; j <= n; j++) {
            prev[j] = prev[j - 1] + s2.charAt(j - 1);
        }

        for (int i = 1; i <= m; i++) {
            int[] cur = new int[n + 1];
            cur[0] = prev[0] + s1.charAt(i - 1);
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    cur[j] = prev[j - 1];
                } else {
                    cur[j] = Math.min(prev[j] + s1.charAt(i - 1),
                            cur[j - 1] + s2.charAt(j - 1));
                }
            }
            prev = cur;
        }
        return prev[n];
    }
}
