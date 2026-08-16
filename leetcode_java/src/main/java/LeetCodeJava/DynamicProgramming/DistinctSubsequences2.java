package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/distinct-subsequences-ii/description/
/**
 * 940. Distinct Subsequences II
 * Hard
 *
 * Given a string s, return the number of distinct non-empty subsequences of s.
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 * A subsequence of a string is a new string that is formed from the original string by
 * deleting some (can be none) of the characters without disturbing the relative
 * positions of the remaining characters.
 *
 * Example 1:
 *
 * Input: s = "abc"
 * Output: 7
 * Explanation: The 7 distinct subsequences are "a", "b", "c", "ab", "ac", "bc", and
 * "abc".
 *
 * Example 2:
 *
 * Input: s = "aba"
 * Output: 6
 * Explanation: The 6 distinct subsequences are "a", "b", "ab", "aa", "ba", and "aba".
 *
 * Example 3:
 *
 * Input: s = "aaa"
 * Output: 3
 * Explanation: The 3 distinct subsequences are "a", "aa" and "aaa".
 *
 * Constraints:
 *
 * 1 <= s.length <= 2000
 * s consists of lowercase English letters.
 *
 */
public class DistinctSubsequences2 {

    // V0
    // IDEA: DP KEYED BY LAST CHARACTER (this is what kills the duplicates)
    /**
     *  DP def:
     *     - dp[c] = number of DISTINCT non-empty subsequences that END with character c
     *
     *  Two distinct subsequences ending with the SAME character are counted ONCE,
     *  because we OVERWRITE dp[c] instead of adding to it.
     *
     *  DP eq (for each character c of s):
     *     - dp[c] = sum(dp) + 1
     *         sum(dp) : APPEND c to every distinct subsequence built so far
     *         + 1     : the single-character subsequence "c"
     *
     *  NOTE !!! the OVERWRITE is the whole trick: every subsequence ending with c is
     *           re-derived from the LATEST occurrence of c, so earlier duplicates
     *           are dropped automatically. A `+=` here would double count.
     *
     *  Answer: sum(dp)
     *
     *  time  = O(26 * n)
     *  space = O(26)
     */
    public int distinctSubseqII(String s) {
        final long MOD = 1_000_000_007L;

        long[] dp = new long[26]; // dp[i] -> subsequences ending with ('a' + i)

        for (int t = 0; t < s.length(); t++) {
            int i = s.charAt(t) - 'a';

            long total = 0;
            for (long v : dp) {
                total = (total + v) % MOD;
            }

            // ASSIGNMENT (not +=) -> old subsequences ending with this char are replaced
            dp[i] = (total + 1) % MOD;
        }

        long res = 0;
        for (long v : dp) {
            res = (res + v) % MOD;
        }
        return (int) res;
    }

}
