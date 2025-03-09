package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-common-subsequence/description/
/**
 * 1143. Longest Common Subsequence
 * Medium
 * Topics
 * Companies
 * Hint
 * Given two strings text1 and text2, return the length of their longest common subsequence. If there is no common subsequence, return 0.
 *
 * A subsequence of a string is a new string generated from the original string with some characters (can be none) deleted without changing the relative order of the remaining characters.
 *
 * For example, "ace" is a subsequence of "abcde".
 * A common subsequence of two strings is a subsequence that is common to both strings.
 *
 *
 *
 * Example 1:
 *
 * Input: text1 = "abcde", text2 = "ace"
 * Output: 3
 * Explanation: The longest common subsequence is "ace" and its length is 3.
 * Example 2:
 *
 * Input: text1 = "abc", text2 = "abc"
 * Output: 3
 * Explanation: The longest common subsequence is "abc" and its length is 3.
 * Example 3:
 *
 * Input: text1 = "abc", text2 = "def"
 * Output: 0
 * Explanation: There is no such common subsequence, so the result is 0.
 *
 *
 * Constraints:
 *
 * 1 <= text1.length, text2.length <= 1000
 * text1 and text2 consist of only lowercase English characters.
 *
 *
 */
public class longestCommonSubsequence {

    // V0
//    public int longestCommonSubsequence(String text1, String text2) {
//
//    }

    // V1-1
    // https://neetcode.io/problems/longest-common-subsequence
    // IDEA: RECURSION
    public int longestCommonSubsequence_1_1(String text1, String text2) {
        return dfs(text1, text2, 0, 0);
    }

    private int dfs(String text1, String text2, int i, int j) {
        if (i == text1.length() || j == text2.length()) {
            return 0;
        }
        if (text1.charAt(i) == text2.charAt(j)) {
            return 1 + dfs(text1, text2, i + 1, j + 1);
        }
        return Math.max(dfs(text1, text2, i + 1, j),
                dfs(text1, text2, i, j + 1));
    }

    // V1-2
    // https://neetcode.io/problems/longest-common-subsequence
    // IDEA:  Dynamic Programming (Top-Down)
    private int[][] memo;

    public int longestCommonSubsequence_1_2(String text1, String text2) {
        memo = new int[text1.length()][text2.length()];
        for (int i = 0; i < text1.length(); i++) {
            for (int j = 0; j < text2.length(); j++) {
                memo[i][j] = -1;
            }
        }
        return dfs_1_2(text1, text2, 0, 0);
    }

    private int dfs_1_2(String text1, String text2, int i, int j) {
        if (i == text1.length() || j == text2.length()) {
            return 0;
        }
        if (memo[i][j] != -1) {
            return memo[i][j];
        }
        if (text1.charAt(i) == text2.charAt(j)) {
            memo[i][j] = 1 + dfs_1_2(text1, text2, i + 1, j + 1);
        } else {
            memo[i][j] = Math.max(dfs(text1, text2, i + 1, j),
                    dfs_1_2(text1, text2, i, j + 1));
        }
        return memo[i][j];
    }


    // V1-3
    // https://neetcode.io/problems/longest-common-subsequence
    // IDEA: Dynamic Programming (Bottom-Up)
    public int longestCommonSubsequence_1_3(String text1, String text2) {
        int[][] dp = new int[text1.length() + 1][text2.length() + 1];

        for (int i = text1.length() - 1; i >= 0; i--) {
            for (int j = text2.length() - 1; j >= 0; j--) {
                if (text1.charAt(i) == text2.charAt(j)) {
                    dp[i][j] = 1 + dp[i + 1][j + 1];
                } else {
                    dp[i][j] = Math.max(dp[i][j + 1], dp[i + 1][j]);
                }
            }
        }

        return dp[0][0];
    }

    // V1-4
    // https://neetcode.io/problems/longest-common-subsequence
    // IDEA: Dynamic Programming (Space Optimized)
    public int longestCommonSubsequence_1_4(String text1, String text2) {
        if (text1.length() < text2.length()) {
            String temp = text1;
            text1 = text2;
            text2 = temp;
        }

        int[] prev = new int[text2.length() + 1];
        int[] curr = new int[text2.length() + 1];

        for (int i = text1.length() - 1; i >= 0; i--) {
            for (int j = text2.length() - 1; j >= 0; j--) {
                if (text1.charAt(i) == text2.charAt(j)) {
                    curr[j] = 1 + prev[j + 1];
                } else {
                    curr[j] = Math.max(curr[j + 1], prev[j]);
                }
            }
            int[] temp = prev;
            prev = curr;
            curr = temp;
        }

        return prev[0];
    }


    // V1-5
    // https://neetcode.io/problems/longest-common-subsequence
    // IDEA: Dynamic Programming (Optimal)
    public int longestCommonSubsequence_1_5(String text1, String text2) {
        if (text1.length() < text2.length()) {
            String temp = text1;
            text1 = text2;
            text2 = temp;
        }

        int[] dp = new int[text2.length() + 1];

        for (int i = text1.length() - 1; i >= 0; i--) {
            int prev = 0;
            for (int j = text2.length() - 1; j >= 0; j--) {
                int temp = dp[j];
                if (text1.charAt(i) == text2.charAt(j)) {
                    dp[j] = 1 + prev;
                } else {
                    dp[j] = Math.max(dp[j], dp[j + 1]);
                }
                prev = temp;
            }
        }

        return dp[0];
    }


    // V2
    // IDEA: DP
    // https://leetcode.com/problems/longest-common-subsequence/solutions/4622129/beats-100-dynamic-programming-cjavapytho-dqsy/
    public int longestCommonSubsequence_2(String text1, String text2) {
        // Lengths of the input strings
        int length1 = text1.length();
        int length2 = text2.length();

        // Create a 2D array to store the lengths of longest common subsequences
        // for all subproblems, initialized with zero
        int[][] dp = new int[length1 + 1][length2 + 1];

        // Build the dp array from the bottom up
        for (int i = 1; i <= length1; ++i) {
            for (int j = 1; j <= length2; ++j) {
                // If characters match, take diagonal value and add 1
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                }
                // If characters do not match, take the maximum value from
                // the left (dp[i][j-1]) or above (dp[i-1][j])
                else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        // The bottom-right cell contains the length of the longest
        // common subsequence of text1 and text2
        return dp[length1][length2];
    }

    // V3
    // IDEA: DP
    // https://leetcode.com/problems/longest-common-subsequence/solutions/6027437/video-dynamic-programming-solution-by-ni-9sc6/
    public int longestCommonSubsequence_3(String text1, String text2) {
        int[] dp = new int[text1.length()];
        int longest = 0;

        for (char c : text2.toCharArray()) {
            int curLength = 0;
            for (int i = 0; i < dp.length; i++) {
                if (curLength < dp[i]) {
                    curLength = dp[i];
                } else if (c == text1.charAt(i)) {
                    dp[i] = curLength + 1;
                    longest = Math.max(longest, curLength + 1);
                }
            }
        }

        return longest;
    }

}
