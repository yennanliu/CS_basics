package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/valid-palindrome-iii/description/
// https://leetcode.ca/2019-03-30-1216-Valid-Palindrome-III/

/**
 * 1216. Valid Palindrome III
 * Hard
 *
 * Given a string s and an integer k, return true if s is a k-palindrome.
 *
 * A string is k-palindrome if it can be transformed into a palindrome by removing at most
 * k characters from it.
 *
 *
 * Example 1:
 *
 * Input: s = "abcdeca", k = 2
 * Output: true
 * Explanation: Remove 'b' and 'e' characters.
 *
 * Example 2:
 *
 * Input: s = "abbababa", k = 1
 * Output: true
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s consists of only lowercase English letters.
 * 1 <= k <= s.length
 *
 */
public class ValidPalindromeIII {

    // V0
    // IDEA: 2D DP (LONGEST PALINDROMIC SUBSEQUENCE, LC 516)
    /**
     * dp[i][j] = length of the longest palindromic subsequence within s[i..j]
     *
     * s[i] == s[j] : dp[i][j] = dp[i+1][j-1] + 2
     * s[i] != s[j] : dp[i][j] = max(dp[i+1][j], dp[i][j-1])
     *
     * min deletions to make s a palindrome = n - LPS(s), so
     * s is a k-palindrome  <=>  n - dp[0][n-1] <= k
     */
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public boolean isValidPalindrome(String s, int k) {
        int n = s.length();
        if (n == 0) {
            return true;
        }
        int[][] dp = new int[n][n];

        // a single char is a palindrome of length 1
        for (int i = 0; i < n; i++) {
            dp[i][i] = 1;
        }

        // i goes backwards so row i+1 is ready when row i is filled
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1] + 2;
                } else {
                    dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        return n - dp[0][n - 1] <= k;
    }

    // V1
    // IDEA: 2D DP ON MIN DELETIONS DIRECTLY, ROLLING 1D ROW
    /**
     * deletions[i][j] = min deletions to turn s[i..j] into a palindrome
     *
     * s[i] == s[j] : deletions[i][j] = deletions[i+1][j-1]
     * s[i] != s[j] : deletions[i][j] = 1 + min(deletions[i+1][j], deletions[i][j-1])
     *
     * base: deletions[i][i] = 0; only row i+1 is ever read, so one row is kept
     */
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public boolean isValidPalindrome_1(String s, int k) {
        int n = s.length();
        if (n <= 1) {
            return true;
        }
        // prev = row i+1
        int[] prev = new int[n];
        for (int i = n - 2; i >= 0; i--) {
            // cur[i] = 0: a single char needs no deletion
            int[] cur = new int[n];
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    // prev[j-1] is deletions[i+1][j-1]
                    cur[j] = prev[j - 1];
                } else {
                    cur[j] = 1 + Math.min(prev[j], cur[j - 1]);
                }
            }
            prev = cur;
        }
        return prev[n - 1] <= k;
    }
}
