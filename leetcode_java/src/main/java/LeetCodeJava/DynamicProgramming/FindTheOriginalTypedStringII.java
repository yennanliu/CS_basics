package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-original-typed-string-ii/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3333. Find the Original Typed String II
 * Hard
 *
 * Alice is attempting to type a specific string on her computer. However, she tends to be
 * clumsy and may press a key for too long, resulting in a character being typed multiple times.
 *
 * You are given a string word, which represents the final output displayed on Alice's screen.
 * You are also given a positive integer k.
 *
 * Return the total number of possible original strings that Alice might have intended to type,
 * if she was trying to type a string of size at least k.
 *
 * Since the answer may be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: word = "aabbccdd", k = 7
 * Output: 5
 * Explanation:
 * The possible strings are: "aabbccdd", "aabbccd", "aabbcdd", "aabccdd", and "abbccdd".
 *
 * Example 2:
 *
 * Input: word = "aabbccdd", k = 8
 * Output: 1
 * Explanation:
 * The only possible string is "aabbccdd".
 *
 * Example 3:
 *
 * Input: word = "aaabbb", k = 3
 * Output: 8
 *
 *
 * Constraints:
 *
 * 1 <= word.length <= 5 * 10^5
 * word consists only of lowercase English letters.
 * 1 <= k <= 2000
 *
 */
public class FindTheOriginalTypedStringII {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: COUNT ALL CANDIDATES (PRODUCT OF RUN LENGTHS), SUBTRACT THE TOO-SHORT ONES WITH A PREFIX-SUM DP
    /**
     *  Each run of equal chars keeps between 1 and runLength chars, independently,
     *  so the total number of candidates is prod(runLength).
     *
     *  The length limit only matters when the number of runs m < k: then count the
     *  BAD candidates (total length < k) and subtract.
     *
     *  DP def:
     *     - dp[j] = ways for the runs processed so far to total length exactly j  (j < k)
     *  DP eq:
     *     - adding a run of length r: newDp[j] = dp[j-1] + dp[j-2] + ... + dp[j-r]
     *       -> a range sum over a prefix-sum array, O(1) per cell
     *  Init:
     *     - dp[0] = 1
     *
     *  time = O(N + m * K)
     *  space = O(m + K)
     */
    public int possibleStringCount(String word, int k) {
        // collect the run lengths
        List<Integer> runs = new ArrayList<>();
        int currentRun = 1;
        for (int i = 1; i < word.length(); i++) {
            if (word.charAt(i) == word.charAt(i - 1)) {
                currentRun++;
            } else {
                runs.add(currentRun);
                currentRun = 1;
            }
        }
        runs.add(currentRun);

        // every candidate: each run independently keeps 1..runLength chars
        long total = 1;
        for (int runLength : runs) {
            total = total * runLength % MOD;
        }

        // even the shortest candidate (one char per run) is long enough
        int runCount = runs.size();
        if (runCount >= k) {
            return (int) total;
        }

        // dp[j] = ways to reach total length j, only j < k matters
        long[] dp = new long[k];
        dp[0] = 1;
        for (int runLength : runs) {
            // prefix[j] = dp[0] + ... + dp[j-1]
            long[] prefix = new long[k + 1];
            for (int j = 0; j < k; j++) {
                prefix[j + 1] = (prefix[j] + dp[j]) % MOD;
            }
            long[] next = new long[k];
            for (int j = 1; j < k; j++) {
                // previous length must be in [j - runLength, j - 1]
                int low = Math.max(0, j - runLength);
                next[j] = (prefix[j] - prefix[low] + MOD) % MOD;
            }
            dp = next;
        }

        long bad = 0;
        for (int j = 0; j < k; j++) {
            bad = (bad + dp[j]) % MOD;
        }
        return (int) ((total - bad + MOD) % MOD);
    }
}
