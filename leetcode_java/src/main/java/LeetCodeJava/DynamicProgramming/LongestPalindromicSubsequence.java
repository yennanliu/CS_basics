package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/longest-palindromic-subsequence/description/

import java.util.Arrays;

/**
 * 516. Longest Palindromic Subsequence
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Given a string s, find the longest palindromic subsequence's length in s.
 *
 * A subsequence is a sequence that can be derived from another sequence by deleting some or no elements without changing the order of the remaining elements.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "bbbab"
 * Output: 4
 * Explanation: One possible longest palindromic subsequence is "bbbb".
 * Example 2:
 *
 * Input: s = "cbbd"
 * Output: 2
 * Explanation: One possible longest palindromic subsequence is "bb".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s consists only of lowercase English letters.
 *
 *
 */
public class LongestPalindromicSubsequence {

    // V0
//    public int longestPalindromeSubseq(String s) {
//
//    }

    // V0-1
    // IDEA: 2D DP (gemini)
    public int longestPalindromeSubseq_0_1(String s) {
        int n = s.length();
        // dp[i][j] stores the LPS length for s[i...j]
        int[][] dp = new int[n][n];

        // Iterate from the end to the beginning for the starting index
        for (int i = n - 1; i >= 0; i--) {
            dp[i][i] = 1; // Base case: single character

            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    // Match found: inner sequence + 2
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    // No match: best of either skipping i or skipping j
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }

        // The answer is the LPS for the entire string
        return dp[0][n - 1];
    }

    // V0-2
    // IDEA: 2D DP (GPT)
    public int longestPalindromeSubseq_0_2(String s) {
        if (s == null || s.length() == 0) {
            return 0;
        }

        int n = s.length();
        int[][] dp = new int[n][n];

        // Base case: single character
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }

        // Fill the DP table
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = 2 + dp[i + 1][j - 1];
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }

        return dp[0][n - 1];
    }


    // V1
    // IDEA: DP
    // https://leetcode.ca/2017-04-29-516-Longest-Palindromic-Subsequence/
    public int longestPalindromeSubseq_1(String s) {
        int n = s.length();
        int[][] dp = new int[n][n];
        for (int i = 0; i < n; ++i) {
            dp[i][i] = 1;
        }
        for (int j = 1; j < n; ++j) {
            for (int i = j - 1; i >= 0; --i) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[0][n - 1];
    }


    // V2
    // IDEA: DP
    // https://leetcode.com/problems/longest-palindromic-subsequence/solutions/3414715/easy-solutions-in-java-python-and-c-look-fl62/
    public int longestPalindromeSubseq_2(String s) {
        // Get the length of the input string
        int n = s.length();
        // Initialize a 2D array to store the length of the longest palindromic subsequence
        int[][] dp = new int[n][n];

        // Iterate over the substrings in reverse order to fill in the dp table bottom-up
        for (int i = n - 1; i >= 0; i--) {
            // Base case: the longest palindromic subsequence of a single character is 1
            dp[i][i] = 1;
            for (int j = i + 1; j < n; j++) {
                // If the two characters match, the longest palindromic subsequence can be extended by two
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = 2 + dp[i + 1][j - 1];
                } else {
                    // Otherwise, we take the maximum of the two possible substrings
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }

        // The length of the longest palindromic subsequence is in the top-right corner of the dp table
        return dp[0][n - 1];
    }


    // V3
    // IDEA: ARRAY OP
    // https://leetcode.com/problems/longest-palindromic-subsequence/solutions/3414839/java-easy-image-explaination-dp-beginner-r69e/
    public int longestPalindromeSubseq_3(String s) {
        /* If the two ends of a string are the same, then they must be included in the longest palindrome subsequence. Otherwise, both ends cannot be included in the longest palindrome subsequence. */

        int size = s.length();
        if (size == 0 || s == null)
            return 0;
        int[][] dp = new int[size][size];
        for (int[] row : dp)
            Arrays.fill(row, -1);
        return helper(s, 0, s.length() - 1, dp);
    }



    private int helper(String s, int left, int right, int[][] cache) {
        if (left == right)
            return 1;

        if (left > right)
            return 0;

        if (cache[left][right] != -1)
            return cache[left][right];

        if (s.charAt(left) == s.charAt(right)) {
            cache[left][right] = 2 + helper(s, left + 1, right - 1, cache);
            return cache[left][right];
        }

        int leftIncrement = helper(s, left + 1, right, cache);
        int rightIncrement = helper(s, left, right - 1, cache);
        cache[left][right] = Math.max(leftIncrement, rightIncrement);
        return cache[left][right];

    }

    // V4-1
    // IDEA:
    // https://leetcode.com/problems/longest-palindromic-subsequence/


    // V4-2
    // IDEA:
    // https://leetcode.com/problems/longest-palindromic-subsequence/

    // V4-3
    // IDEA:
    // https://leetcode.com/problems/longest-palindromic-subsequence/

    // V4-4
    // IDEA: Bottom Up Tabulation with space optimization
    // https://leetcode.com/problems/longest-palindromic-subsequence/
    public int longestPalindromeSubseq_4_4(String s) {
        int[] dp = new int[s.length()];
        for (int i = s.length() - 1; i >= 0; i--) {
            dp[i] = 1;
            int pre = 0;
            for (int j = i + 1; j < s.length(); j++) {
                int tmp = dp[j];
                if (s.charAt(i) == s.charAt(j)) {
                    dp[j] = pre + 2;
                } else {
                    dp[j] = Math.max(dp[j], dp[j - 1]);
                }
                pre = tmp;
            }
        }

        return dp[s.length() - 1];
    }



}
