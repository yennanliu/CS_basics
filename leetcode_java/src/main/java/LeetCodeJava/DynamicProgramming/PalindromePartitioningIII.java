package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/palindrome-partitioning-iii/description/

import java.util.Arrays;

/**
 * 1278. Palindrome Partitioning III
 * Hard
 *
 * You are given a string s containing lowercase letters and an integer k. You need to :
 *
 * First, change some characters of s to other lowercase English letters.
 * Then divide s into k non-empty disjoint substrings such that each substring is a palindrome.
 *
 * Return the minimal number of characters that you need to change to divide the string.
 *
 *
 * Example 1:
 *
 * Input: s = "abc", k = 2
 * Output: 1
 * Explanation: You can split the string into "ab" and "c", and change 1 character in "ab"
 * to make it palindrome.
 *
 * Example 2:
 *
 * Input: s = "aabbc", k = 3
 * Output: 0
 * Explanation: You can split the string into "aa", "bb" and "c", all of them are palindrome.
 *
 * Example 3:
 *
 * Input: s = "leetcode", k = 8
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= k <= s.length <= 100.
 * s only contains lowercase English letters.
 *
 */
public class PalindromePartitioningIII {

    // V0
    // IDEA: 2-STAGE DP (PALINDROME COST TABLE + PARTITION DP)
    /**
     * 1) cost[i][j] = changes to turn s[i..j] into a palindrome
     *      cost[i][j] = cost[i+1][j-1] + (s[i] != s[j] ? 1 : 0)
     *
     * 2) dp[i][parts] = min changes to split the prefix s[0..i-1] into `parts` palindromes
     *      dp[i][parts] = min over m in [parts-1, i-1] of dp[m][parts-1] + cost[m][i-1]
     *      (the last palindrome is s[m..i-1])
     *
     * base: dp[0][0] = 0; answer = dp[n][k]
     */
    /**
     * time = O(N^2 * K)
     * space = O(N^2)
     */
    public int palindromePartition(String s, int k) {
        int n = s.length();
        // every character on its own is a palindrome
        if (k >= n) {
            return 0;
        }

        // 1) cost table; i goes down so cost[i+1][j-1] is ready
        int[][] cost = new int[n][n];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                int mismatch = (s.charAt(i) == s.charAt(j)) ? 0 : 1;
                cost[i][j] = cost[i + 1][j - 1] + mismatch;
            }
        }

        // 2) partition DP
        int INF = Integer.MAX_VALUE;
        int[][] dp = new int[n + 1][k + 1];
        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }
        dp[0][0] = 0;

        for (int i = 1; i <= n; i++) {
            // cannot make more parts than there are characters
            for (int parts = 1; parts <= Math.min(i, k); parts++) {
                for (int m = parts - 1; m < i; m++) {
                    if (dp[m][parts - 1] == INF) {
                        continue;
                    }
                    dp[i][parts] = Math.min(dp[i][parts], dp[m][parts - 1] + cost[m][i - 1]);
                }
            }
        }
        return dp[n][k];
    }
}
