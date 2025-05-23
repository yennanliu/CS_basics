package LeetCodeJava.Array;

// https://leetcode.com/problems/regular-expression-matching/description/
/**
 * 10. Regular Expression Matching
 * Solved
 * Hard
 * Topics
 * Companies
 * Given an input string s and a pattern p, implement regular expression matching with support for '.' and '*' where:
 *
 * '.' Matches any single character.​​​​
 * '*' Matches zero or more of the preceding element.
 * The matching should cover the entire input string (not partial).
 *
 *
 *
 * Example 1:
 *
 * Input: s = "aa", p = "a"
 * Output: false
 * Explanation: "a" does not match the entire string "aa".
 * Example 2:
 *
 * Input: s = "aa", p = "a*"
 * Output: true
 * Explanation: '*' means zero or more of the preceding element, 'a'. Therefore, by repeating 'a' once, it becomes "aa".
 * Example 3:
 *
 * Input: s = "ab", p = ".*"
 * Output: true
 * Explanation: ".*" means "zero or more (*) of any character (.)".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 20
 * 1 <= p.length <= 20
 * s contains only lowercase English letters.
 * p contains only lowercase English letters, '.', and '*'.
 * It is guaranteed for each appearance of the character '*', there will be a previous valid character to match.
 *
 *
 */
public class RegularExpressionMatching {

    // V0
//    public boolean isMatch(String s, String p) {
//
//    }

    // V0-1
    // IDEA: 2D DP (gpt)
    // time: O(M*N), space: O(M*N)
    public boolean isMatch_0_1(String s, String p) {
        int m = s.length();
        int n = p.length();

        // dp[i][j] = true if s[0..i-1] matches p[0..j-1]
        boolean[][] dp = new boolean[m + 1][n + 1];

        // Empty string matches empty pattern
        dp[0][0] = true;

        // Handle patterns like a*, a*b*, a*b*c* for empty string
        for (int j = 2; j <= n; j++) {
            if (p.charAt(j - 1) == '*' && dp[0][j - 2]) {
                dp[0][j] = true;
            }
        }

        // Fill DP table
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char sc = s.charAt(i - 1);
                char pc = p.charAt(j - 1);

                if (pc == '.' || pc == sc) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (pc == '*') {
                    char prevPattern = p.charAt(j - 2);
                    // Zero occurrence of preceding char
                    dp[i][j] = dp[i][j - 2];

                    // One or more occurrence if char matches
                    if (prevPattern == '.' || prevPattern == sc) {
                        dp[i][j] |= dp[i - 1][j];
                    }
                }
            }
        }

        return dp[m][n];
    }

    // V1-1
    // https://neetcode.io/problems/regular-expression-matching
    // IDEA: RECURSION
    // time: O(2^(M+N)), space: O(M+N)
    public boolean isMatch_1_1(String s, String p) {
        int m = s.length(), n = p.length();
        return dfs(0, 0, s, p, m, n);
    }

    private boolean dfs(int i, int j, String s, String p, int m, int n) {
        if (j == n) return i == m;

        boolean match = i < m && (s.charAt(i) == p.charAt(j) ||
                p.charAt(j) == '.');
        if (j + 1 < n && p.charAt(j + 1) == '*') {
            return dfs(i, j + 2, s, p, m, n) ||
                    (match && dfs(i + 1, j, s, p, m, n));
        }

        if (match) {
            return dfs(i + 1, j + 1, s, p, m, n);
        }

        return false;
    }

    // V1-2
    // https://neetcode.io/problems/regular-expression-matching
    // IDEA: DP (TOP DOWN)
    // time: O(M*N), space: O(M*N)
    private Boolean[][] dp;

    public boolean isMatch_1_2(String s, String p) {
        int m = s.length(), n = p.length();
        dp = new Boolean[m + 1][n + 1];
        return dfs_1_2(0, 0, s, p, m, n);
    }

    private boolean dfs_1_2(int i, int j, String s, String p, int m, int n) {
        if (j == n) {
            return i == m;
        }
        if (dp[i][j] != null) {
            return dp[i][j];
        }

        boolean match = i < m && (s.charAt(i) == p.charAt(j) ||
                p.charAt(j) == '.');
        if (j + 1 < n && p.charAt(j + 1) == '*') {
            dp[i][j] = dfs_1_2(i, j + 2, s, p, m, n) ||
                    (match && dfs_1_2(i + 1, j, s, p, m, n));
        } else {
            dp[i][j] = match && dfs_1_2(i + 1, j + 1, s, p, m, n);
        }

        return dp[i][j];
    }

    // V1-3
    // https://neetcode.io/problems/regular-expression-matching
    // IDEA: DP (BOTTOM UP)
    // time: O(M*N), space: O(M*N)
    public boolean isMatch_1_3(String s, String p) {
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[m][n] = true;

        for (int i = m; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                boolean match = i < m && (s.charAt(i) == p.charAt(j) ||
                        p.charAt(j) == '.');

                if ((j + 1) < n && p.charAt(j + 1) == '*') {
                    dp[i][j] = dp[i][j + 2];
                    if (match) {
                        dp[i][j] = dp[i + 1][j] || dp[i][j];
                    }
                } else if (match) {
                    dp[i][j] = dp[i + 1][j + 1];
                }
            }
        }

        return dp[0][0];
    }


    // V1-4
    // https://neetcode.io/problems/regular-expression-matching
    // IDEA: DP (SPACE OPTIMIZED)
    // time: O(M*N), space: O(N)
    public boolean isMatch_1_4(String s, String p) {
        boolean[] dp = new boolean[p.length() + 1];
        dp[p.length()] = true;

        for (int i = s.length(); i >= 0; i--) {
            boolean[] nextDp = new boolean[p.length() + 1];
            nextDp[p.length()] = (i == s.length());

            for (int j = p.length() - 1; j >= 0; j--) {
                boolean match = i < s.length() &&
                        (s.charAt(i) == p.charAt(j) ||
                                p.charAt(j) == '.');

                if (j + 1 < p.length() && p.charAt(j + 1) == '*') {
                    nextDp[j] = nextDp[j + 2];
                    if (match) {
                        nextDp[j] |= dp[j];
                    }
                } else if (match) {
                    nextDp[j] = dp[j + 1];
                }
            }

            dp = nextDp;
        }

        return dp[0];
    }


    // V1-5
    // https://neetcode.io/problems/regular-expression-matching
    // IDEA: DP (OPTIMAL)
    // time: O(M*N), space: O(M*N)
    public boolean isMatch_1_5(String s, String p) {
        boolean[] dp = new boolean[p.length() + 1];
        dp[p.length()] = true;

        for (int i = s.length(); i >= 0; i--) {
            boolean dp1 = dp[p.length()];
            dp[p.length()] = (i == s.length());

            for (int j = p.length() - 1; j >= 0; j--) {
                boolean match = i < s.length() &&
                        (s.charAt(i) == p.charAt(j) ||
                                p.charAt(j) == '.');
                boolean res = false;
                if (j + 1 < p.length() && p.charAt(j + 1) == '*') {
                    res = dp[j + 2];
                    if (match) {
                        res |= dp[j];
                    }
                } else if (match) {
                    res = dp1;
                }
                dp1 = dp[j];
                dp[j] = res;
            }
        }

        return dp[0];
    }


    // V2

}
