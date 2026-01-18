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

    // V0-1
    // IDEA: 2D DP (gemini)
    /**
     * To solve **LeetCode 115: Distinct Subsequences**,
     * we use 2D Dynamic Programming.
     * 
     * The goal is to
     * find how many ways we can form string
     * `t` by picking characters from string `s`
     * while maintaining their relative order.
     *
     * ### 💡 The DP Logic
     *
     * 1. **State Definition**: `dp[i][j]` represents the number of distinct subsequences of `s.substring(0, i)` which equal `t.substring(0, j)`.
     * 2. **Base Cases**:
     * * If `t` is empty (`j = 0`), there is **1** way to form it (by deleting all characters of `s`). So, `dp[i][0] = 1`.
     * * If `s` is empty but `t` is not (`i = 0, j > 0`), there are **0** ways. So, `dp[0][j] = 0`.
     *
     *
     * 3. **Transition**:
     * * **Case 1: Characters Match (`s[i-1] == t[j-1]`)**
     * We have two choices:
     * 1. Use `s[i-1]` to match `t[j-1]`: The result is `dp[i-1][j-1]`.
     * 2. Don't use `s[i-1]` (even though it matches): The result is `dp[i-1][j]`.
     * *Equation*: `dp[i][j] = dp[i-1][j-1] + dp[i-1][j]`
     *
     *
     * * **Case 2: Characters Don't Match**
     * We have no choice but to skip `s[i-1]`.
     * *Equation*: `dp[i][j] = dp[i-1][j]`
     *
     * ---
     *
     * ### 🔍 Visualizing the Transition
     *
     * Imagine `s = "rabbbit"` and `t = "rabbit"`.
     * When we are at the third 'b' in `s` matching the second 'b' in `t`:
     *
     * 1. **Use it**: We look at how many ways "rab" (from `s`) made "ra" (from `t`).
     * 2. **Skip it**: We look at how many ways "rab" (from `s`) already made "rab" (from `t`) using earlier characters.
     *
     * ### 📊 Complexity Analysis
     *
     * * **Time Complexity**: , where  is the length of `s` and  is the length of `t`.
     * * **Space Complexity**:  for the 2D array.
     *
     */
    public int numDistinct_0_1(String s, String t) {
        int slen = s.length();
        int tlen = t.length();

        // dp[i][j] means number of subsequences of s[0...i-1] matching t[0...j-1]
        int[][] dp = new int[slen + 1][tlen + 1];

        // Base case: If t is empty, s can always form it in 1 way (empty subsequence)
        for (int i = 0; i <= slen; i++) {
            dp[i][0] = 1;
        }

        for (int i = 1; i <= slen; i++) {
            for (int j = 1; j <= tlen; j++) {
                // If characters match, we sum the ways including s[i-1] and excluding it
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + dp[i - 1][j];
                }
                // If they don't match, we must exclude s[i-1]
                else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[slen][tlen];
    }


    // V0-2
    // IDEA: 2D DP + SPACE OPTIMIZED (gemini)
    public int numDistinct_0_2(String s, String t) {
        int[] dp = new int[t.length() + 1];
        dp[0] = 1;

        for (int i = 1; i <= s.length(); i++) {
            for (int j = t.length(); j >= 1; j--) {
                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[j] = dp[j] + dp[j - 1];
                }
            }
        }
        return dp[t.length()];
    }


    // V0-3
    // IDEA: 2D DP (gpt)
    public int numDistinct_0_3(String s, String t) {
        int m = s.length();
        int n = t.length();

        // dp[i][j]: # of distinct ways to form t[:j] from s[:i]
        long[][] dp = new long[m + 1][n + 1];

        // base cases
        for (int i = 0; i <= m; i++) {
            dp[i][0] = 1; // one way to form empty t
        }
        // no need to set dp[0][j] because default 0

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {

                // first inherit without using s[i-1]
                dp[i][j] = dp[i - 1][j];

                if (s.charAt(i - 1) == t.charAt(j - 1)) {
                    dp[i][j] += dp[i - 1][j - 1];
                }
            }
        }

        return (int) dp[m][n];
    }



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
