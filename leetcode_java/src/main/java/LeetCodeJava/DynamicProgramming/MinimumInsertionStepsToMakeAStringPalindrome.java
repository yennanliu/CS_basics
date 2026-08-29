package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-insertion-steps-to-make-a-string-palindrome/

/**
 *  1312. Minimum Insertion Steps to Make a String Palindrome
 *  Hard
 *
 *  Given a string s. In one step you can insert any character at any index of the string.
 *
 *  Return the minimum number of steps to make s palindrome.
 *
 *  A Palindrome String is one that reads the same backward as well as forward.
 *
 *  Example 1:
 *    Input: s = "zzazz"
 *    Output: 0
 *
 *  Example 2:
 *    Input: s = "mbadm"
 *    Output: 2   ("mbdadbm" or "mdbabdm")
 *
 *  Example 3:
 *    Input: s = "leetcode"
 *    Output: 5   ("leetcodocteel")
 *
 *  Constraints:
 *    1 <= s.length <= 500
 *    s consists of lowercase English letters.
 */
public class MinimumInsertionStepsToMakeAStringPalindrome {

    // V0
    // IDEA: interval DP. dp[i][j] = min insertions to make s[i..j] a palindrome.
    /**
     * time = O(n^2)
     * space = O(n^2)
     */
    public int minInsertions(String s) {
        int n = s.length();
        if (n <= 1) {
            return 0;
        }
        int[][] dp = new int[n][n];

        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i + 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i + 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[0][n - 1];
    }

    // V1
    // IDEA: answer = n - LCS(s, reverse(s)), computed with a rolling 1D array.
    /**
     * time = O(n^2)
     * space = O(n)
     */
    public int minInsertions_1(String s) {
        int n = s.length();
        String r = new StringBuilder(s).reverse().toString();

        int[] prev = new int[n + 1];
        int[] cur = new int[n + 1];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (s.charAt(i - 1) == r.charAt(j - 1)) {
                    cur[j] = prev[j - 1] + 1;
                } else {
                    cur[j] = Math.max(prev[j], cur[j - 1]);
                }
            }
            int[] tmp = prev;
            prev = cur;
            cur = tmp;
        }
        return n - prev[n];
    }

    // V2
    // IDEA: TOP-DOWN MEMOIZATION over the interval (i, j) - the recursive twin of V0
    /**
     * time = O(n^2)
     * space = O(n^2)
     */
    public int minInsertions_2(String s) {
        int n = s.length();
        if (n <= 1) {
            return 0;
        }
        Integer[][] memo = new Integer[n][n];
        return helper_2(s, 0, n - 1, memo);
    }

    private int helper_2(String s, int i, int j, Integer[][] memo) {
        if (i >= j) {
            return 0;
        }
        if (memo[i][j] != null) {
            return memo[i][j];
        }
        int res;
        if (s.charAt(i) == s.charAt(j)) {
            res = helper_2(s, i + 1, j - 1, memo);
        } else {
            res = 1 + Math.min(helper_2(s, i + 1, j, memo), helper_2(s, i, j - 1, memo));
        }
        memo[i][j] = res;
        return res;
    }
}
