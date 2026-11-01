package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-steps-to-convert-string-with-operations/description/

import java.util.*;

/**
 * 3579. Minimum Steps to Convert String with Operations
 * Hard
 *
 * You are given two strings, word1 and word2, of equal length. You need to
 * transform word1 into word2.
 *
 * For this, divide word1 into one or more contiguous substrings. For each
 * substring substr you can perform the following operations:
 *
 * 1. Replace: Replace the character at any one index of substr with another
 *    lowercase English letter.
 * 2. Swap: Swap any two characters in substr.
 * 3. Reverse Substring: Reverse substr.
 *
 * Each of these counts as one operation and each character of each substring
 * can be used in each type of operation at most once (i.e. no single index may
 * be involved in more than one replace, one swap, or one reverse).
 *
 * Return the minimum number of operations required to transform word1 into
 * word2.
 *
 *
 * Example 1:
 *
 * Input: word1 = "abcdf", word2 = "dacbe"
 * Output: 4
 * Explanation:
 * Divide word1 into "ab", "c", and "df". The operations are:
 * - For the substring "ab",
 *   - Perform operation of type 3 on "ab" -> "ba".
 *   - Perform operation of type 1 on "ba" -> "da".
 * - For the substring "c" do no operations.
 * - For the substring "df",
 *   - Perform operation of type 1 on "df" -> "bf".
 *   - Perform operation of type 1 on "bf" -> "be".
 *
 * Example 2:
 *
 * Input: word1 = "abceded", word2 = "baecfef"
 * Output: 4
 * Explanation:
 * Divide word1 into "ab", "ce", and "ded". The operations are:
 * - For the substring "ab",
 *   - Perform operation of type 2 on "ab" -> "ba".
 * - For the substring "ce",
 *   - Perform operation of type 2 on "ce" -> "ec".
 * - For the substring "ded",
 *   - Perform operation of type 1 on "ded" -> "fed".
 *   - Perform operation of type 1 on "fed" -> "fef".
 *
 * Example 3:
 *
 * Input: word1 = "abcdef", word2 = "fedabc"
 * Output: 2
 * Explanation:
 * Divide word1 into "abcdef". The operations are:
 * - For the substring "abcdef",
 *   - Perform operation of type 3 on "abcdef" -> "fedcba".
 *   - Perform operation of type 2 on "fedcba" -> "fedabc".
 *
 *
 * Constraints:
 *
 * 1 <= word1.length == word2.length <= 100
 * word1 and word2 consist only of lowercase English letters.
 *
 */
public class MinimumStepsToConvertStringWithOperations {

    // V0
    // IDEA: PARTITION DP OVER SUBSTRINGS + GREEDY PAIRING OF OPPOSITE MISMATCHES
    //   cutting word1 into pieces is free and pieces do not interact:
    //   best[i] = min over j < i of best[j] + min(cost(j, i-1), 1 + cost of the reversed piece)
    //   inside a piece: matching indices stay; a mismatch costs one replace, but a
    //   swap fixes two mismatches at once when one index needs a -> b and another
    //   needs b -> a. So cost = mismatches - (number of such opposite pairs), and
    //   pairing greedily with a running count of open (a -> b) demands is optimal.
    //   A reverse is worth at most once per piece, and doing it first covers every schedule.
    /**
     * time = O(N^3)
     * space = O(N)
     */
    public int minOperations(String word1, String word2) {
        int n = word1.length();
        int[] best = new int[n + 1];
        Arrays.fill(best, Integer.MAX_VALUE);
        best[0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                int plain = pieceCost(word1, word2, j, i - 1, false);
                int reversed = 1 + pieceCost(word1, word2, j, i - 1, true);
                best[i] = Math.min(best[i], best[j] + Math.min(plain, reversed));
            }
        }
        return best[n];
    }

    /** Min replaces / swaps turning word1[left..right] (optionally reversed) into word2[left..right]. */
    private int pieceCost(String word1, String word2, int left, int right, boolean reverse) {
        // openDemand[a * 26 + b] = unmatched indices that need letter a turned into b
        int[] openDemand = new int[26 * 26];
        int operations = 0;
        for (int i = left; i <= right; i++) {
            int source = reverse ? right - (i - left) : i;
            int from = word1.charAt(source) - 'a';
            int to = word2.charAt(i) - 'a';
            if (from == to) {
                continue;
            }
            if (openDemand[to * 26 + from] > 0) {
                // an earlier index needs to -> from: one swap fixes both (already paid 1)
                openDemand[to * 26 + from]--;
            } else {
                openDemand[from * 26 + to]++;
                operations++;
            }
        }
        return operations;
    }
}
