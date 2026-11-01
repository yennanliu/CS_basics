package LeetCodeJava.String;

// https://leetcode.com/problems/number-of-substrings-with-only-1s/description/

/**
 * 1513. Number of Substrings With Only 1s
 * Medium
 *
 * Given a binary string s, return the number of substrings with all characters 1's. Since the answer may be too large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: s = "0110111"
 * Output: 9
 * Explanation: There are 9 substring in total with only 1's characters.
 * "1" -> 5 times.
 * "11" -> 3 times.
 * "111" -> 1 time.
 *
 * Example 2:
 *
 * Input: s = "101"
 * Output: 2
 * Explanation: Substring "1" is shown 2 times in s.
 *
 * Example 3:
 *
 * Input: s = "111111"
 * Output: 21
 * Explanation: Each substring contains only 1's characters.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s[i] is either '0' or '1'.
 *
 */
public class NumberOfSubstringsWithOnly1s {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: COUNT RUNS INCREMENTALLY
    //   runLength = length of the run of 1s ending at index i; every all-1 substring ending at i
    //   is fixed by its start, and there are exactly runLength choices -> add runLength.
    //   a '0' resets the run. (a run of length L contributes L * (L + 1) / 2.)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int numSub(String s) {
        long total = 0;
        int runLength = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                runLength = 0;
            } else {
                runLength++;
                total = (total + runLength) % MOD;
            }
        }
        return (int) total;
    }
}
