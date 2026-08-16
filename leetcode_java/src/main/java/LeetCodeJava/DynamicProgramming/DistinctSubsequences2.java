package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/distinct-subsequences-ii/description/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
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


    // V1
    // IDEA: RUNNING TOTAL (avoid re-summing the 26 buckets each step)
    /**
     *  V0 sums dp[0..25] on every character, which is 26 additions per position.
     *  Keeping the total as a variable and adjusting it by the DELTA of the one
     *  bucket that changes makes each step O(1).
     *
     *  -> O(n) instead of O(26 n).
     *
     *  time  = O(n)
     *  space = O(26)
     */
    public int distinctSubseqII_1(String s) {
        final long MOD = 1_000_000_007L;
        long[] endWith = new long[26];
        long total = 0;

        for (int t = 0; t < s.length(); t++) {
            int i = s.charAt(t) - 'a';
            long fresh = (total + 1) % MOD;          // append c to everything, plus "c"
            long delta = (fresh - endWith[i] + MOD) % MOD;
            endWith[i] = fresh;
            total = (total + delta) % MOD;
        }
        return (int) total;
    }

    // V2
    // IDEA: `LAST OCCURRENCE` FORMULATION -- dp over the prefix length
    /**
     *  dp[i] = number of distinct subsequences of the first i characters:
     *
     *      dp[i] = 2 * dp[i-1] - dp[last[c] - 1]
     *
     *  Doubling counts `take it or not`, and the subtraction removes the duplicates
     *  created the previous time this character appeared.
     *
     *  The classic inclusion-exclusion phrasing; it also yields the count for every
     *  PREFIX, which the bucket version discards.
     *
     *  time  = O(n)
     *  space = O(n)
     */
    public int distinctSubseqII_2(String s) {
        final long MOD = 1_000_000_007L;
        int n = s.length();
        long[] dp = new long[n + 1];
        dp[0] = 1;                     // the empty subsequence, removed at the end

        int[] last = new int[26];
        Arrays.fill(last, -1);

        for (int i = 1; i <= n; i++) {
            int c = s.charAt(i - 1) - 'a';
            dp[i] = dp[i - 1] * 2 % MOD;
            if (last[c] >= 0) {
                dp[i] = (dp[i] - dp[last[c]] + MOD) % MOD;
            }
            last[c] = i - 1;
        }
        return (int) ((dp[n] - 1 + MOD) % MOD);   // drop the empty subsequence
    }

    // V3
    // IDEA: BRUTE FORCE -- enumerate every subsequence into a set
    /**
     *  All 2^n subsequences, de-duplicated by a HashSet.
     *
     *  Only usable for n <= ~20, but it counts the objects the statement describes,
     *  which is what validates both linear recurrences.
     *
     *  time  = O(2^n * n)
     *  space = O(2^n * n)
     */
    public int distinctSubseqII_3(String s) {
        int n = s.length();
        Set<String> seen = new HashSet<>();
        for (int mask = 1; mask < (1 << n); mask++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) == 1) {
                    sb.append(s.charAt(i));
                }
            }
            seen.add(sb.toString());
        }
        return (int) (seen.size() % 1_000_000_007L);
    }

}
