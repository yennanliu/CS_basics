package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximize-palindrome-length-from-subsequences/description/
/**
 * 1771. Maximize Palindrome Length From Subsequences
 * Hard
 *
 * You are given two strings, word1 and word2. You want to construct a string in the
 * following manner:
 *
 * Choose some non-empty subsequence subsequence1 from word1.
 * Choose some non-empty subsequence subsequence2 from word2.
 * Concatenate the subsequences: subsequence1 + subsequence2, to make the string.
 *
 * Return the length of the longest palindrome that can be constructed in the described
 * manner. If no palindromes can be constructed, return 0.
 *
 * A subsequence of a string s is a string that can be made by deleting some (possibly
 * none) characters from s without changing the order of the remaining characters.
 *
 * A palindrome is a string that reads the same forward as well as backward.
 *
 * Example 1:
 *
 * Input: word1 = "cacb", word2 = "cbba"
 * Output: 5
 * Explanation: Choose "ab" from word1 and "cba" from word2 to make "abcba", which is a
 * palindrome.
 *
 * Example 2:
 *
 * Input: word1 = "ab", word2 = "ab"
 * Output: 3
 * Explanation: Choose "ab" from word1 and "a" from word2 to make "aba", which is a
 * palindrome.
 *
 * Example 3:
 *
 * Input: word1 = "aa", word2 = "bb"
 * Output: 0
 * Explanation: You cannot construct a palindrome from the described method, so return 0.
 *
 * Constraints:
 *
 * 1 <= word1.length, word2.length <= 1000
 * word1 and word2 consist of lowercase English letters.
 *
 */
public class MaximizePalindromeLengthFromSubsequences {

    // V0
    // IDEA: LONGEST PALINDROMIC SUBSEQUENCE ON word1 + word2, COUNT ONLY PAIRS THAT CROSS THE BOUNDARY
    /**
     *  DP def (s = word1 + word2):
     *     - dp[i][j] = length of the longest palindromic subsequence of s[i..j]
     *
     *  DP eq:
     *     - s[i] == s[j]: dp[i][j] = dp[i + 1][j - 1] + 2
     *     - otherwise   : dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])
     *
     *  Both subsequences must be non-empty, so a palindrome counts only when its
     *  OUTERMOST matched pair has i in word1 and j in word2. Recording the answer
     *  only at such matches guarantees both halves contribute.
     *
     *  time = O((N + M)^2)
     *  space = O((N + M)^2)
     */
    public int longestPalindrome(String word1, String word2) {
        String s = word1 + word2;
        int n = s.length();
        int boundary = word1.length(); // first index belonging to word2

        int[][] dp = new int[n][n];
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }

        int best = 0;
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                    // the outer pair spans word1 and word2 -> a valid construction
                    if (i < boundary && j >= boundary) {
                        best = Math.max(best, dp[i][j]);
                    }
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        return best;
    }
}
