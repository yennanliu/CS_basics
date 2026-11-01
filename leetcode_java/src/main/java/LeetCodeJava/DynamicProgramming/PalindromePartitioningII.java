package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/palindrome-partitioning-ii/description/

/**
 * 132. Palindrome Partitioning II
 * Hard
 *
 * Given a string s, partition s such that every substring of the partition is a palindrome.
 *
 * Return the minimum cuts needed for a palindrome partitioning of s.
 *
 *
 * Example 1:
 *
 * Input: s = "aab"
 * Output: 1
 * Explanation: The palindrome partitioning ["aa","b"] could be produced using 1 cut.
 *
 * Example 2:
 *
 * Input: s = "a"
 * Output: 0
 *
 * Example 3:
 *
 * Input: s = "ab"
 * Output: 1
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 2000
 * s consists of lowercase English letters only.
 *
 */
public class PalindromePartitioningII {

    // V0
    // IDEA: DP (PALINDROME TABLE + MIN-CUT PER PREFIX, BUILT IN ONE PASS)
    //   cut[i]       = min cuts for the prefix s[0..i]
    //   isPal[j][i]  = whether s[j..i] is a palindrome
    //   for every j <= i that closes a palindrome at i: cut[i] = min(cut[i], cut[j - 1] + 1),
    //   and j == 0 means the whole prefix is one palindrome -> 0 cuts (NOT cut[-1] + 1)
    /**
     * time = O(N^2)
     * space = O(N^2)
     */
    public int minCut(String s) {
        // edge case: an empty string needs no cut
        if (s == null || s.length() == 0) {
            return 0;
        }

        int n = s.length();
        boolean[][] isPal = new boolean[n][n];
        int[] cut = new int[n];

        for (int i = 0; i < n; i++) {
            // worst case: cut before every character of s[0..i]
            int minCuts = i;

            for (int j = 0; j <= i; j++) {
                // s[j..i] is a palindrome if its ends match and the inside is one too
                // (an inside shorter than 2 chars is trivially a palindrome).
                // isPal[j + 1][i - 1] is ready: it has a smaller right end, filled earlier
                boolean endsMatch = s.charAt(j) == s.charAt(i);
                if (endsMatch && (i - j < 2 || isPal[j + 1][i - 1])) {
                    isPal[j][i] = true;

                    if (j == 0) {
                        // the whole prefix is a palindrome: no cut at all
                        minCuts = 0;
                    } else {
                        // cut right before j, then s[j..i] is the last piece
                        minCuts = Math.min(minCuts, cut[j - 1] + 1);
                    }
                }
            }

            cut[i] = minCuts;
        }

        return cut[n - 1];
    }
}
