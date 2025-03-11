package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/distinct-subsequences/description/

import java.util.Arrays;

/**
 * 115. Distinct Subsequences
 * Hard
 * Topics
 * Companies
 * Given two strings s and t, return the number of distinct subsequences of s which equals t.
 *
 * The test cases are generated so that the answer fits on a 32-bit signed integer.
 *
 *
 *
 * Example 1:
 *
 * Input: s = "rabbbit", t = "rabbit"
 * Output: 3
 * Explanation:
 * As shown below, there are 3 ways you can generate "rabbit" from s.
 * rabbbit
 * rabbbit
 * rabbbit
 * Example 2:
 *
 * Input: s = "babgbag", t = "bag"
 * Output: 5
 * Explanation:
 * As shown below, there are 5 ways you can generate "bag" from s.
 * babgbag
 * babgbag
 * babgbag
 * babgbag
 * babgbag
 *
 *
 * Constraints:
 *
 * 1 <= s.length, t.length <= 1000
 * s and t consist of English letters.
 *
 *
 */
public class DistinctSubsequences {

    // V0
//    public int numDistinct(String s, String t) {
//
//    }

    // V1-1
    // https://neetcode.io/problems/count-subsequences
    // IDEA:  RECURSION
    public int numDistinct_1_1(String s, String t) {
        if (t.length() > s.length()) {
            return 0;
        }
        return dfs(s, t, 0, 0);
    }

    private int dfs(String s, String t, int i, int j) {
        if (j == t.length()) {
            return 1;
        }
        if (i == s.length()) {
            return 0;
        }

        int res = dfs(s, t, i + 1, j);
        if (s.charAt(i) == t.charAt(j)) {
            res += dfs(s, t, i + 1, j + 1);
        }
        return res;
    }


    // V1-2
    // https://neetcode.io/problems/count-subsequences
    // IDEA: Dynamic Programming (Top-Down)
    private int[][] dp;

    public int numDistinct_1_2(String s, String t) {
        int m = s.length(), n = t.length();
        if (n > m) return 0;
        dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            Arrays.fill(dp[i], -1);
        }
        return dfs_1_2(s, t, 0, 0);
    }

    private int dfs_1_2(String s, String t, int i, int j) {
        if (j == t.length()) return 1;
        if (i == s.length()) return 0;
        if (dp[i][j] != -1) return dp[i][j];

        int res = dfs_1_2(s, t, i + 1, j);
        if (s.charAt(i) == t.charAt(j)) {
            res += dfs_1_2(s, t, i + 1, j + 1);
        }
        dp[i][j] = res;
        return res;
    }

    // V1-3
    // https://neetcode.io/problems/count-subsequences
    // IDEA:  Dynamic Programming (Bottom-Up)
    public int numDistinct_1_3(String s, String t) {
        int m = s.length(), n = t.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][n] = 1;
        }

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                dp[i][j] = dp[i + 1][j];
                if (s.charAt(i) == t.charAt(j)) {
                    dp[i][j] += dp[i + 1][j + 1];
                }
            }
        }

        return dp[0][0];
    }


    // V1-4
    // https://neetcode.io/problems/count-subsequences
    // IDEA: Dynamic Programming (Space Optimized)
    public int numDistinct_1_4(String s, String t) {
        int m = s.length(), n = t.length();
        int[] dp = new int[n + 1];
        int[] nextDp = new int[n + 1];

        dp[n] = nextDp[n] = 1;
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                nextDp[j] = dp[j];
                if (s.charAt(i) == t.charAt(j)) {
                    nextDp[j] += dp[j + 1];
                }
            }
            dp = nextDp.clone();
        }

        return dp[0];
    }


    // V1-5
    // https://neetcode.io/problems/count-subsequences
    // IDEA: Dynamic Programming (Optimal)
    public int numDistinct_1_5(String s, String t) {
        int m = s.length(), n = t.length();
        int[] dp = new int[n + 1];

        dp[n] = 1;
        for (int i = m - 1; i >= 0; i--) {
            int prev = 1;
            for (int j = n - 1; j >= 0; j--) {
                int res = dp[j];
                if (s.charAt(i) == t.charAt(j)) {
                    res += prev;
                }

                prev = dp[j];
                dp[j] = res;
            }
        }

        return dp[0];
    }

    // V2
}
