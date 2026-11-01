package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-substring-partition-of-equal-character-frequency/description/

import java.util.*;

/**
 * 3144. Minimum Substring Partition of Equal Character Frequency
 * Medium
 *
 * Given a string s, you need to partition it into one or more balanced substrings. For example, if s == "ababcc" then ("abab", "c", "c"), ("ab", "abc", "c"), and ("ababcc") are all valid partitions, but ("a", "bab", "cc"), ("aba", "bc", "c"), and ("ab", "abcc") are not. The unbalanced substrings are bolded.
 *
 * Return the minimum number of substrings that you can partition s into.
 *
 * Note: A balanced string is a string where each character in the string occurs the same number of times.
 *
 *
 * Example 1:
 *
 * Input: s = "fabccddg"
 * Output: 3
 * Explanation:
 * We can partition the string s into 3 substrings in one of the following ways: ("fab, "ccdd", "g"), or ("fabc", "cd", "dg").
 *
 * Example 2:
 *
 * Input: s = "abababaccddb"
 * Output: 2
 * Explanation:
 * We can partition the string s into 2 substrings like so: ("abab", "abaccddb").
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s consists only of English lowercase letters.
 *
 */
public class MinimumSubstringPartitionOfEqualCharacterFrequency {

    // V0
    // IDEA: DP OVER PREFIXES, GROWING THE LAST PIECE LEFTWARDS
    //   dp[i] = fewest balanced pieces covering s[0 .. i-1].
    //   For each i, move the split point j from i-1 down to 0, keeping letter counts of
    //   s[j .. i-1]. The window is balanced iff distinct * maxCount == window length
    //   (every present letter reaches the max count and together they fill the window).
    //   Then dp[i] = min(dp[i], dp[j] + 1).
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int minimumSubstringsInPartition(String s) {
        int n = s.length();
        final int INF = Integer.MAX_VALUE;
        int[] dp = new int[n + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int i = 1; i <= n; i++) {
            int[] count = new int[26];
            int distinct = 0;
            int maxCount = 0;
            for (int j = i - 1; j >= 0; j--) {
                int letter = s.charAt(j) - 'a';
                if (count[letter] == 0) {
                    distinct++;
                }
                count[letter]++;
                maxCount = Math.max(maxCount, count[letter]);

                int windowLength = i - j;
                boolean balanced = distinct * maxCount == windowLength;
                if (balanced && dp[j] != INF) {
                    dp[i] = Math.min(dp[i], dp[j] + 1);
                }
            }
        }
        return dp[n];
    }
}
