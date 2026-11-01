package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/shortest-common-supersequence/description/

/**
 * 1092. Shortest Common Supersequence
 * Hard
 *
 * Given two strings str1 and str2, return the shortest string that has both str1
 * and str2 as subsequences. If there are multiple valid strings, return any of them.
 *
 * A string s is a subsequence of string t if deleting some number of characters
 * from t (possibly 0) results in the string s.
 *
 *
 * Example 1:
 *
 * Input: str1 = "abac", str2 = "cab"
 * Output: "cabac"
 * Explanation:
 * str1 = "abac" is a subsequence of "cabac" because we can delete the first "c".
 * str2 = "cab" is a subsequence of "cabac" because we can delete the last "ac".
 * The answer provided is the shortest such string that satisfies these properties.
 *
 * Example 2:
 *
 * Input: str1 = "aaaaaaaa", str2 = "aaaaaaaa"
 * Output: "aaaaaaaa"
 *
 *
 * Constraints:
 *
 * 1 <= str1.length, str2.length <= 1000
 * str1 and str2 consist of lowercase English letters.
 *
 */
public class ShortestCommonSupersequence {

    // V0
    // IDEA: LCS (2D DP) + BACKTRACK THE DP TABLE
    /**
     * dp[i][j] = length of the LCS of str1[0..i) and str2[0..j)
     *
     * if str1[i-1] == str2[j-1] : dp[i][j] = dp[i-1][j-1] + 1
     * else                      : dp[i][j] = max(dp[i-1][j], dp[i][j-1])
     *
     * Then walk back from (m, n): a char in the LCS is emitted once, a char not in
     * the LCS is emitted from whichever string it belongs to.
     */
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public String shortestCommonSupersequence(String str1, String str2) {
        int m = str1.length();
        int n = str2.length();

        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // backtrack from the bottom-right corner; the result is built in reverse
        StringBuilder reversed = new StringBuilder();
        int i = m;
        int j = n;
        while (i > 0 || j > 0) {
            if (i == 0) {
                // str1 exhausted: the rest of str2 is copied
                j--;
                reversed.append(str2.charAt(j));
            } else if (j == 0) {
                // str2 exhausted: the rest of str1 is copied
                i--;
                reversed.append(str1.charAt(i));
            } else if (dp[i][j] == dp[i - 1][j]) {
                // str1[i-1] is not part of the LCS
                i--;
                reversed.append(str1.charAt(i));
            } else if (dp[i][j] == dp[i][j - 1]) {
                // str2[j-1] is not part of the LCS
                j--;
                reversed.append(str2.charAt(j));
            } else {
                // a matched LCS char: emit it only once
                i--;
                j--;
                reversed.append(str1.charAt(i));
            }
        }
        return reversed.reverse().toString();
    }
}
