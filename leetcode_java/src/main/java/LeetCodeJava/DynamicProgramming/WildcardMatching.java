package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/wildcard-matching/description/

/**
 * 44. Wildcard Matching
 * Hard
 *
 * Given an input string (s) and a pattern (p), implement wildcard pattern matching with
 * support for '?' and '*' where:
 *
 * '?' Matches any single character.
 * '*' Matches any sequence of characters (including the empty sequence).
 * The matching should cover the entire input string (not partial).
 *
 *
 * Example 1:
 *
 * Input: s = "aa", p = "a"
 * Output: false
 * Explanation: "a" does not match the entire string "aa".
 *
 * Example 2:
 *
 * Input: s = "aa", p = "*"
 * Output: true
 * Explanation: '*' matches any sequence.
 *
 * Example 3:
 *
 * Input: s = "cb", p = "?a"
 * Output: false
 * Explanation: '?' matches 'c', but the second letter is 'a', which does not match 'b'.
 *
 *
 * Constraints:
 *
 * 0 <= s.length, p.length <= 2000
 * s contains only lowercase English letters.
 * p contains only lowercase English letters, '?' or '*'.
 *
 */
public class WildcardMatching {

    // V0
    // IDEA: 2D DP — dp[i][j] = s[0..i) is fully matched by p[0..j)
    //   p[j-1] == '*'                    -> dp[i][j] = dp[i][j-1]   ('*' matches nothing)
    //                                                  || dp[i-1][j] ('*' eats one more char of s)
    //   p[j-1] == '?' or == s[i-1]       -> dp[i][j] = dp[i-1][j-1] (both advance by one)
    //   base: dp[0][0] = true; dp[0][j] = true iff p[0..j) is all '*'
    /**
     * time = O(M * N)
     * space = O(M * N)
     */
    public boolean isMatch(String s, String p) {
        int m = s.length();
        int n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];

        // empty string matches empty pattern
        dp[0][0] = true;

        // an empty string is matched only by a leading run of '*'
        for (int j = 1; j <= n; j++) {
            if (p.charAt(j - 1) != '*') {
                break;
            }
            dp[0][j] = true;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char patternChar = p.charAt(j - 1);

                if (patternChar == '*') {
                    // '*' matches the empty sequence, or swallows s[i-1] and stays available
                    dp[i][j] = dp[i][j - 1] || dp[i - 1][j];
                } else if (patternChar == '?' || patternChar == s.charAt(i - 1)) {
                    // a single-character match: both strings advance by one
                    dp[i][j] = dp[i - 1][j - 1];
                }
                // otherwise the characters differ and dp[i][j] stays false
            }
        }

        return dp[m][n];
    }

    // V1
    // IDEA: GREEDY TWO POINTERS WITH BACKTRACK TO THE LAST '*'
    //   remember where the last '*' was and which s-position it started at; on a mismatch,
    //   let that '*' eat one more character of s and retry from just after it.
    //   O(1) space, but harder to get right in an interview than V0.
    /**
     * time = O(M * N) worst case, O(M + N) typical
     * space = O(1)
     */
    public boolean isMatch_1(String s, String p) {
        int sIdx = 0;
        int pIdx = 0;
        // position of the last '*' seen in p, and the s-position it was matched against
        int starIdx = -1;
        int starMatchIdx = -1;

        while (sIdx < s.length()) {
            if (pIdx < p.length() && (p.charAt(pIdx) == '?' || p.charAt(pIdx) == s.charAt(sIdx))) {
                // characters match: advance both
                sIdx++;
                pIdx++;
            } else if (pIdx < p.length() && p.charAt(pIdx) == '*') {
                // record the '*', first try letting it match nothing
                starIdx = pIdx;
                starMatchIdx = sIdx;
                pIdx++;
            } else if (starIdx != -1) {
                // mismatch: backtrack, the last '*' eats one more character of s
                starMatchIdx++;
                sIdx = starMatchIdx;
                pIdx = starIdx + 1;
            } else {
                // mismatch and no '*' to fall back on
                return false;
            }
        }

        // s is used up: whatever is left of p must be all '*'
        while (pIdx < p.length() && p.charAt(pIdx) == '*') {
            pIdx++;
        }

        return pIdx == p.length();
    }
}
