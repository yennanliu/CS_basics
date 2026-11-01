package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-deletions-on-a-string/description/

/**
 * 2430. Maximum Deletions on a String
 * Hard
 *
 * You are given a string s consisting of only lowercase English letters. In one operation, you can:
 *
 * Delete the entire string s, or
 * Delete the first i letters of s if the first i letters of s are equal to the following i letters in s, for any i in the range 1 <= i <= s.length / 2.
 *
 * For example, if s = "ababc", then in one operation, you could delete the first two letters of s to get "abc", since the first two letters of s and the following two letters of s are both equal to "ab".
 *
 * Return the maximum number of operations needed to delete all of s.
 *
 *
 * Example 1:
 *
 * Input: s = "abcabcdabc"
 * Output: 2
 * Explanation:
 * - Delete the first 3 letters ("abc") since the next 3 letters are equal. Now, s = "abcdabc".
 * - Delete all the letters.
 * We used 2 operations so we return 2.
 * It can be shown that 2 is the maximum number of operations needed.
 * Note that in the second operation we cannot delete "abc" again because the next occurrence of "abc" is the 4th letter.
 *
 * Example 2:
 *
 * Input: s = "aaabaab"
 * Output: 4
 * Explanation:
 * - Delete the first letter ("a") since the next letter is equal. Now, s = "aabaab".
 * - Delete the first 3 letters ("aab") since the next 3 letters are equal. Now, s = "aab".
 * - Delete the first letter ("a") since the next letter is equal. Now, s = "ab".
 * - Delete all the letters.
 * We used 4 operations so we return 4.
 * It can be shown that 4 is the maximum number of operations needed.
 *
 * Example 3:
 *
 * Input: s = "aaaaa"
 * Output: 5
 * Explanation: In each operation, we can delete the first letter of s.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 4000
 * s consists only of lowercase English letters.
 *
 */
public class MaximumDeletionsOnAString {

    // V0
    // IDEA: SUFFIX DP + LCP TABLE — dp[i] = MAX OPERATIONS ON s[i..]; s[i..i+L) == s[i+L..i+2L) IFF lcp[i][i+L] >= L
    //       (the Python version compares the two halves with a rolling hash; the LCP table does it exactly)
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int deleteString(String s) {
        int n = s.length();

        // lcp[i][j] = length of the longest common prefix of s[i..] and s[j..]
        int[][] lcp = new int[n + 1][n + 1];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = n - 1; j > i; j--) {
                if (s.charAt(i) == s.charAt(j)) {
                    lcp[i][j] = lcp[i + 1][j + 1] + 1;
                }
            }
        }

        // dp[i] = max operations to delete all of s[i..]; deleting it whole is always 1 op
        int[] dp = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            dp[i] = 1;
            // delete the first L letters when they equal the next L letters
            for (int length = 1; i + 2 * length <= n; length++) {
                if (lcp[i][i + length] >= length) {
                    dp[i] = Math.max(dp[i], 1 + dp[i + length]);
                }
            }
        }
        return dp[0];
    }
}
