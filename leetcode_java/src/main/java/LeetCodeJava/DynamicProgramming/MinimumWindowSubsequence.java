package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-window-subsequence/description/
// https://leetcode.ca/all/727.html

/**
 *
 * 727. Minimum Window Subsequence
 * Given strings S and T, find the minimum (contiguous) substring W of S, so that T is a subsequence of W.
 *
 * If there is no such window in S that covers all characters in T, return the empty string "". If there are multiple such minimum-length windows, return the one with the left-most starting index.
 *
 * Example 1:
 *
 * Input:
 * S = "abcdebdde", T = "bde"
 * Output: "bcde"
 * Explanation:
 * "bcde" is the answer because it occurs before "bdde" which has the same length.
 * "deb" is not a smaller window because the elements of T in the window must occur in order.
 *
 *
 * Note:
 *
 * All the strings in the input will only contain lowercase letters.
 * The length of S will be in the range [1, 20000].
 * The length of T will be in the range [1, 100].
 *
 *
 * Difficulty:
 * Hard
 * Lock:
 * Prime
 * Company:
 * Amazon Bloomberg eBay Google Houzz Microsoft
 *
 */

public class MinimumWindowSubsequence {

    // V0
    // TODO : implement
//    public String minWindow(String s1, String s2) {
//    }

    // V1
    // https://leetcode.ca/2017-11-26-727-Minimum-Window-Subsequence/
    // IDEA : DP
    public String minWindow_1(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[][] f = new int[m + 1][n + 1];
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    f[i][j] = j == 1 ? i : f[i - 1][j - 1];
                } else {
                    f[i][j] = f[i - 1][j];
                }
            }
        }
        int p = 0, k = m + 1;
        for (int i = 1; i <= m; ++i) {
            if (s1.charAt(i - 1) == s2.charAt(n - 1) && f[i][n] > 0) {
                int j = f[i][n] - 1;
                if (i - j < k) {
                    k = i - j;
                    p = j;
                }
            }
        }
        return k > m ? "" : s1.substring(p, p + k);
    }


    // V2
    // https://github.com/doocs/leetcode/blob/main/solution/0700-0799/0727.Minimum%20Window%20Subsequence/README.md
    public String minWindow_2(String s1, String s2) {
        int m = s1.length(), n = s2.length();
        int[][] f = new int[m + 1][n + 1];
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    f[i][j] = j == 1 ? i : f[i - 1][j - 1];
                } else {
                    f[i][j] = f[i - 1][j];
                }
            }
        }
        int p = 0, k = m + 1;
        for (int i = 1; i <= m; ++i) {
            if (s1.charAt(i - 1) == s2.charAt(n - 1) && f[i][n] > 0) {
                int j = f[i][n] - 1;
                if (i - j < k) {
                    k = i - j;
                    p = j;
                }
            }
        }
        return k > m ? "" : s1.substring(p, p + k);
    }

    // V3
    // https://www.cnblogs.com/grandyang/p/8684817.html
    // IDEA : DP (modified by GPT)
    // TODO : validate below
    public String minWindow_3_1(String S, String T) {
        int m = S.length();
        int n = T.length();
        int start = -1;
        int minLen = Integer.MAX_VALUE;
        int[][] dp = new int[m + 1][n + 1];

        // Initialize dp array with -1
        for (int[] row : dp) {
            java.util.Arrays.fill(row, -1);
        }

        // Base case: empty T can be matched starting at any index in S
        for (int i = 0; i <= m; ++i) {
            dp[i][0] = i;
        }

        // Fill the dp array
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= Math.min(i, n); ++j) {
                dp[i][j] = (S.charAt(i - 1) == T.charAt(j - 1)) ? dp[i - 1][j - 1] : dp[i - 1][j];
            }
            if (dp[i][n] != -1) {
                int len = i - dp[i][n];
                if (minLen > len) {
                    minLen = len;
                    start = dp[i][n];
                }
            }
        }

        return (start != -1) ? S.substring(start, start + minLen) : "";
    }

    // V3-1
    // https://www.cnblogs.com/grandyang/p/8684817.html
    // IDEA : 2 POINTERS (modified by GPT)
    // TODO : validate below
    public String minWindow_3_2(String S, String T) {
        int m = S.length();
        int n = T.length();
        int start = -1;
        int minLen = Integer.MAX_VALUE;
        int i = 0;
        int j = 0;

        while (i < m) {
            if (S.charAt(i) == T.charAt(j)) {
                if (++j == n) {
                    int end = i + 1;
                    while (--j >= 0) {
                        while (i >= 0 && S.charAt(i) != T.charAt(j)) {
                            i--;
                        }
                        i--;
                    }
                    i++;
                    j++;
                    if (end - i < minLen) {
                        minLen = end - i;
                        start = i;
                    }
                }
            }
            i++;
        }

        return (start != -1) ? S.substring(start, start + minLen) : "";
    }

}
