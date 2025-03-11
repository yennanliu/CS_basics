package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/edit-distance/description/
/**
 * 72. Edit Distance
 * Solved
 * Medium
 * Topics
 * Companies
 * Given two strings word1 and word2, return the minimum number of operations required to convert word1 to word2.
 *
 * You have the following three operations permitted on a word:
 *
 * Insert a character
 * Delete a character
 * Replace a character
 *
 *
 * Example 1:
 *
 * Input: word1 = "horse", word2 = "ros"
 * Output: 3
 * Explanation:
 * horse -> rorse (replace 'h' with 'r')
 * rorse -> rose (remove 'r')
 * rose -> ros (remove 'e')
 * Example 2:
 *
 * Input: word1 = "intention", word2 = "execution"
 * Output: 5
 * Explanation:
 * intention -> inention (remove 't')
 * inention -> enention (replace 'i' with 'e')
 * enention -> exention (replace 'n' with 'x')
 * exention -> exection (replace 'n' with 'c')
 * exection -> execution (insert 'u')
 *
 *
 * Constraints:
 *
 * 0 <= word1.length, word2.length <= 500
 * word1 and word2 consist of lowercase English letters.
 *
 *
 */
public class EditDistance {

    // V0
//    public int minDistance(String word1, String word2) {
//    }

    // V1-1
    // https://neetcode.io/problems/edit-distance
    // IDEA: RECURSION
    public int minDistance_1_1(String word1, String word2) {
        int m = word1.length(), n = word2.length();

        return dfs(0, 0, word1, word2, m, n);
    }

    private int dfs(int i, int j, String word1, String word2, int m, int n) {
        if (i == m) return n - j;
        if (j == n) return m - i;

        if (word1.charAt(i) == word2.charAt(j)) {
            return dfs(i + 1, j + 1, word1, word2, m, n);
        }

        int res = Math.min(dfs(i + 1, j, word1, word2, m, n),
                dfs(i, j + 1, word1, word2, m, n));
        res = Math.min(res, dfs(i + 1, j + 1, word1, word2, m, n));
        return res + 1;
    }

    // V1-2
    // https://neetcode.io/problems/edit-distance
    // IDEA:  Dynamic Programming (Top-Down)
    private int[][] dp;
    public int minDistance_1_2(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                dp[i][j] = -1;
            }
        }
        return dfs_1_2(0, 0, word1, word2, m, n);
    }

    private int dfs_1_2(int i, int j, String word1, String word2, int m, int n) {
        if (i == m) return n - j;
        if (j == n) return m - i;
        if (dp[i][j] != -1) return dp[i][j];

        if (word1.charAt(i) == word2.charAt(j)) {
            dp[i][j] = dfs_1_2(i + 1, j + 1, word1, word2, m, n);
        } else {
            int res = Math.min(dfs(i + 1, j, word1, word2, m, n),
                    dfs_1_2(i, j + 1, word1, word2, m, n));
            res = Math.min(res, dfs_1_2(i + 1, j + 1, word1, word2, m, n));
            dp[i][j] = res + 1;
        }
        return dp[i][j];
    }

    // V1-3
    // https://neetcode.io/problems/edit-distance
    // IDEA: Dynamic Programming (Bottom-Up)
    public int minDistance_1_3(String word1, String word2) {
        int[][] dp = new int[word1.length() + 1][word2.length() + 1];

        for (int j = 0; j <= word2.length(); j++) {
            dp[word1.length()][j] = word2.length() - j;
        }
        for (int i = 0; i <= word1.length(); i++) {
            dp[i][word2.length()] = word1.length() - i;
        }

        for (int i = word1.length() - 1; i >= 0; i--) {
            for (int j = word2.length() - 1; j >= 0; j--) {
                if (word1.charAt(i) == word2.charAt(j)) {
                    dp[i][j] = dp[i + 1][j + 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i + 1][j],
                            Math.min(dp[i][j + 1], dp[i + 1][j + 1]));
                }
            }
        }
        return dp[0][0];
    }

    // V1-4
    // https://neetcode.io/problems/edit-distance
    // IDEA: Dynamic Programming (Space Optimized)
    public int minDistance_1_4(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        if (m < n) {
            int temp = m;
            m = n;
            n = temp;
            String t = word1;
            word1 = word2;
            word2 = t;
        }

        int[] dp = new int[n + 1];
        int[] nextDp = new int[n + 1];

        for (int j = 0; j <= n; j++) {
            dp[j] = n - j;
        }

        for (int i = m - 1; i >= 0; i--) {
            nextDp[n] = m - i;
            for (int j = n - 1; j >= 0; j--) {
                if (word1.charAt(i) == word2.charAt(j)) {
                    nextDp[j] = dp[j + 1];
                } else {
                    nextDp[j] = 1 + Math.min(dp[j],
                            Math.min(nextDp[j + 1], dp[j + 1]));
                }
            }
            System.arraycopy(nextDp, 0, dp, 0, n + 1);
        }

        return dp[0];
    }

    // V1-5
    // https://neetcode.io/problems/edit-distance
    // IDEA: Dynamic Programming (Optimal)
    public int minDistance_1_5(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        if (m < n) {
            String temp = word1; word1 = word2; word2 = temp;
            m = word1.length(); n = word2.length();
        }

        int[] dp = new int[n + 1];
        for (int i = 0; i <= n; i++) dp[i] = n - i;

        for (int i = m - 1; i >= 0; i--) {
            int nextDp = dp[n];
            dp[n] = m - i;
            for (int j = n - 1; j >= 0; j--) {
                int temp = dp[j];
                if (word1.charAt(i) == word2.charAt(j)) {
                    dp[j] = nextDp;
                } else {
                    dp[j] = 1 + Math.min(dp[j],
                            Math.min(dp[j + 1], nextDp));
                }
                nextDp = temp;
            }
        }
        return dp[0];
    }


    // V2
    // https://leetcode.ca/2016-02-10-72-Edit-Distance/
    // IDEA: DP
    public int minDistance_2(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] f = new int[m + 1][n + 1];
        for (int j = 1; j <= n; ++j) {
            f[0][j] = j;
        }
        for (int i = 1; i <= m; ++i) {
            f[i][0] = i;
            for (int j = 1; j <= n; ++j) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    f[i][j] = f[i - 1][j - 1];
                } else {
                    f[i][j] = Math.min(f[i - 1][j], Math.min(f[i][j - 1], f[i - 1][j - 1])) + 1;
                }
            }
        }
        return f[m][n];
    }

}
