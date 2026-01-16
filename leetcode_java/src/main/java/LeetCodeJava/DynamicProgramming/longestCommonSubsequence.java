package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-common-subsequence/description/

import java.util.Arrays;

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

    // NOTE !!!
    // the `2 POINTERS (SLIDE WINDOW)` is WRONG
    // -> we have no way to know
    // whether `skip or take cur sub str` is a better or worse idea
    // in terms to get a global LCS (longest common subsequence)

    // V0
    // IDEA: BOTTOM UP 2D DP (GEMINI)
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();

        // dp[i][j] stores the LCS of text1[0...i-1] and text2[0...j-1]
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Case 1: Characters match
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    // Diagonal move: 1 + result from both strings being 1 char shorter
                    dp[i][j] = 1 + dp[i - 1][j - 1];
                }
                // Case 2: Characters don't match
                else {
                    // Take the maximum of skipping a char from text1 OR text2
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // The final answer is the LCS of both full strings
        return dp[m][n];
    }

    // V0-0-1
    // IDEA: BOTTOM UP 2D DP (gemini)
    public int longestCommonSubsequence_0_0_1(String text1, String text2) {
        int l1 = text1.length();
        int l2 = text2.length();

        /** NOTE !!!
         *
         *  DP def:
         *
         *  dp[i][j]:
         *    - LCS of text1.substring(0, i)
         *      and text2.substring(0, j)
         *
         */
        /**  NOTE !!!
         *
         *   define dp as `new int[l1 + 1][l2 + 1];`
         *
         *  Array Sizing: You should use new int[l1 + 1][l2 + 1].
         *  This allows you to handle the "empty string" case at
         *  index 0 easily,
         *  preventing IndexOutOfBounds errors when looking at i-1.
         *
         */
        // 1. Size the DP table to length + 1
        // dp[i][j] represents LCS of text1.substring(0, i) and text2.substring(0, j)
        int[][] dp = new int[l1 + 1][l2 + 1];

        // 2. Iterate through both strings
        for (int i = 1; i <= l1; i++) {
            for (int j = 1; j <= l2; j++) {
                /** NOTE !!!
                 *
                 *  since the `dp def` is
                 *       - LCS of text1.substring(0, i) and text2.substring(0, j)
                 *
                 *  -> what we need to compare is:
                 *      the `prev` idx of text1, and text2
                 *      -> e.g. `if (text1.charAt(i - 1) == text2.charAt(j - 1)))
                 *
                 *      and if above is true, update dp by
                 *          dp[i - 1][j - 1] + 1;
                 */
                // Check characters at i-1 and j-1 because DP is 1-indexed
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    // If they match, take the diagonal value and add 1
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    /** NOTE !!!
                     *
                     *    if text1.charAt(i - 1) != text2.charAt(j - 1):
                     *      then dp[i][j] will be the max of
                     *      either 2 possible prev dp
                     *
                     *      -> e.g. max(dp[i - 1][j], dp[i][j - 1])
                     *
                     */
                    // If they don't match, take the best from either the top or left cell
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // 3. The final result is in the bottom-right corner
        return dp[l1][l2];
    }


    // V0-1
    // IDEA: RECURSION (gpt) (TLE)
    public int longestCommonSubsequence_0_1(String text1, String text2) {
        return lcs(text1, text2, 0, 0);
    }

    private int lcs(String s1, String s2, int i, int j) {
        if (i == s1.length() || j == s2.length())
            return 0;

        if (s1.charAt(i) == s2.charAt(j)) {
            return 1 + lcs(s1, s2, i + 1, j + 1);
        } else {
            return Math.max(lcs(s1, s2, i + 1, j), lcs(s1, s2, i, j + 1));
        }
    }

    // V0-2
    // IDEA: TOP DOWN DP (gpt)
    public int longestCommonSubsequence_0_2(String text1, String text2) {
        int[][] memo = new int[text1.length()][text2.length()];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        return dfs(text1, text2, 0, 0, memo);
    }

    private int dfs(String s1, String s2, int i, int j, int[][] memo) {
        if (i == s1.length() || j == s2.length())
            return 0;
        if (memo[i][j] != -1)
            return memo[i][j];

        if (s1.charAt(i) == s2.charAt(j)) {
            memo[i][j] = 1 + dfs(s1, s2, i + 1, j + 1, memo);
        } else {
            memo[i][j] = Math.max(
                    dfs(s1, s2, i + 1, j, memo),
                    dfs(s1, s2, i, j + 1, memo));
        }
        return memo[i][j];
    }

    // V0-3
    // IDEA: BOTTOM UP 2D DP (GPT)
    public int longestCommonSubsequence_0_3(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[m][n];
    }

    //  V0-4
    // IDEA: BOTTOM UP 2D DP WITH SPACE OPTIMIZATION (GPT)
    public int longestCommonSubsequence_0_4(String text1, String text2) {
        int m = text1.length();
        int n = text2.length();

        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    curr[j] = prev[j - 1] + 1;
                } else {
                    curr[j] = Math.max(prev[j], curr[j - 1]);
                }
            }
            int[] tmp = prev;
            prev = curr;
            curr = tmp;
        }

        return prev[n];
    }

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
