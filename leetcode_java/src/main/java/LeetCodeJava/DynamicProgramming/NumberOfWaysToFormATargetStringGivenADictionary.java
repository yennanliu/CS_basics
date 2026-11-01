package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/number-of-ways-to-form-a-target-string-given-a-dictionary/description/

/**
 * 1639. Number of Ways to Form a Target String Given a Dictionary
 * Hard
 *
 * You are given a list of strings of the same length words and a string target.
 *
 * Your task is to form target using the given words under the following rules:
 *
 * - target should be formed from left to right.
 * - To form the ith character (0-indexed) of target, you can choose the kth character of the jth string in words if target[i] = words[j][k].
 * - Once you use the kth character of the jth string of words, you can no longer use the xth character of any string in words where x <= k. In other words, all characters to the left of or at index k become unusuable for every string.
 * - Repeat the process until you form the string target.
 *
 * Notice that you can use multiple characters from the same string in words provided the conditions above are met.
 *
 * Return the number of ways to form target from words. Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: words = ["acca","bbbb","caca"], target = "aba"
 * Output: 6
 * Explanation: There are 6 ways to form target.
 * "aba" -> index 0 ("acca"), index 1 ("bbbb"), index 3 ("caca")
 * "aba" -> index 0 ("acca"), index 2 ("bbbb"), index 3 ("caca")
 * "aba" -> index 0 ("acca"), index 1 ("bbbb"), index 3 ("acca")
 * "aba" -> index 0 ("acca"), index 2 ("bbbb"), index 3 ("acca")
 * "aba" -> index 1 ("caca"), index 2 ("bbbb"), index 3 ("acca")
 * "aba" -> index 1 ("caca"), index 2 ("bbbb"), index 3 ("caca")
 *
 * Example 2:
 *
 * Input: words = ["abba","baab"], target = "bab"
 * Output: 4
 * Explanation: There are 4 ways to form target.
 * "bab" -> index 0 ("baab"), index 1 ("baab"), index 2 ("abba")
 * "bab" -> index 0 ("baab"), index 1 ("baab"), index 3 ("baab")
 * "bab" -> index 0 ("baab"), index 2 ("baab"), index 3 ("baab")
 * "bab" -> index 1 ("abba"), index 2 ("baab"), index 3 ("baab")
 *
 *
 * Constraints:
 *
 * 1 <= words.length <= 1000
 * 1 <= words[i].length <= 1000
 * All strings in words have the same length.
 * 1 <= target.length <= 1000
 * words[i] and target contain only lowercase English letters.
 *
 */
public class NumberOfWaysToFormATargetStringGivenADictionary {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: COLUMN COUNTS + 0/1 KNAPSACK-STYLE DP OVER COLUMNS
    //       all words have the same length, and once column k is used every column <= k is
    //       gone -> only "how many words have letter c at column k" matters: count[k][c].
    //       dp[i] = ways to have built target[0..i-1] using the columns processed so far.
    //       column k either is skipped, or supplies target[i]: dp[i+1] += dp[i] * count[k][target[i]]
    //       iterate i DOWNWARD so a column is used at most once (like 0/1 knapsack).
    /**
     * time = O(W * L + L * T)   (W words of length L, T = target length)
     * space = O(L * 26 + T)
     */
    public int numWays(String[] words, String target) {
        int targetLength = target.length();
        int wordLength = words[0].length();

        // count[k][c] = number of words with letter c at column k
        int[][] count = new int[wordLength][26];
        for (String word : words) {
            for (int k = 0; k < wordLength; k++) {
                count[k][word.charAt(k) - 'a']++;
            }
        }

        long[] dp = new long[targetLength + 1];
        dp[0] = 1;
        for (int k = 0; k < wordLength; k++) {
            // column k can supply at most target[k] (an earlier char needs an earlier column)
            int highest = Math.min(k, targetLength - 1);
            for (int i = highest; i >= 0; i--) {
                int choices = count[k][target.charAt(i) - 'a'];
                if (choices > 0) {
                    dp[i + 1] = (dp[i + 1] + dp[i] * choices) % MOD;
                }
            }
        }
        return (int) dp[targetLength];
    }
}
