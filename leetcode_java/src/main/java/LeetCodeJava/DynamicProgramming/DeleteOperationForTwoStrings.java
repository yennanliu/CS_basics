package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/delete-operation-for-two-strings/

/**
 *  583. Delete Operation for Two Strings
 *  Medium
 *
 *  Given two strings word1 and word2, return the minimum number of steps
 *  required to make word1 and word2 the same.
 *
 *  In one step, you can delete exactly one character in either string.
 *
 *  Example 1:
 *
 *  Input: word1 = "sea", word2 = "eat"
 *  Output: 2
 *  Explanation: You need one step to make "sea" to "ea" and another step to
 *  make "eat" to "ea".
 *
 *  Example 2:
 *
 *  Input: word1 = "leetcode", word2 = "etco"
 *  Output: 4
 *
 *  Constraints:
 *
 *  1 <= word1.length, word2.length <= 500
 *  word1 and word2 consist of only lowercase English letters.
 */
public class DeleteOperationForTwoStrings {

    // V0
    // IDEA: DP (edit distance with delete only)
    //  dp[i][j] = min deletions to make word1[0..i) and word2[0..j) equal
    //   - if chars match : dp[i][j] = dp[i-1][j-1]
    //   - else           : dp[i][j] = 1 + min(dp[i-1][j], dp[i][j-1])
    /**
     * time = O(m * n)
     * space = O(m * n)
     */
    public int minDistance(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // delete everything from word1
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // delete everything from word2
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    // V1
    // IDEA: LCS - answer = m + n - 2 * LCS(word1, word2)
    /**
     * time = O(m * n)
     * space = O(n)
     */
    public int minDistance_1(String word1, String word2) {
        int m = word1.length();
        int n = word2.length();
        int[] prev = new int[n + 1];
        int[] cur = new int[n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    cur[j] = prev[j - 1] + 1;
                } else {
                    cur[j] = Math.max(prev[j], cur[j - 1]);
                }
            }
            int[] tmp = prev;
            prev = cur;
            cur = tmp;
            java.util.Arrays.fill(cur, 0);
        }
        int lcs = prev[n];
        return m + n - 2 * lcs;
    }
}
