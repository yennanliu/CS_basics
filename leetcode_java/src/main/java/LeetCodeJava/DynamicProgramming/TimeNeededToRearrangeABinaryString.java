package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/time-needed-to-rearrange-a-binary-string/description/

/**
 * 2380. Time Needed to Rearrange a Binary String
 * Medium
 *
 * You are given a binary string s. In one second, all occurrences of "01" are simultaneously replaced with "10". This process repeats until no occurrences of "01" exist.
 *
 * Return the number of seconds needed to complete this process.
 *
 *
 * Example 1:
 *
 * Input: s = "0110101"
 * Output: 4
 * Explanation:
 * After one second, s becomes "1011010".
 * After another second, s becomes "1101100".
 * After the third second, s becomes "1110100".
 * After the fourth second, s becomes "1111000".
 * No occurrence of "01" exists, and the process needed 4 seconds to complete,
 * so we return 4.
 *
 * Example 2:
 *
 * Input: s = "11100"
 * Output: 0
 * Explanation:
 * No occurrence of "01" exists in s, and the processes needed 0 seconds to complete,
 * so we return 0.
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s[i] is either '0' or '1'.
 *
 */
public class TimeNeededToRearrangeABinaryString {

    // V0
    // IDEA: DP OVER THE ONES — EACH '1' FINISHES AT max(PREVIOUS ONE'S TIME + 1, ZEROS BEFORE IT)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int secondsToRemoveOccurrences(String s) {
        int seconds = 0;
        int zeros = 0;

        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                zeros++;
            } else if (zeros > 0) {
                // this '1' must pass every zero before it (one per second),
                // and cannot finish until one second after the '1' ahead of it
                seconds = Math.max(seconds + 1, zeros);
            }
            // a '1' with no zero before it never moves
        }
        return seconds;
    }

    // V1
    // IDEA: BRUTE-FORCE SIMULATION — REPLACE EVERY "01" WITH "10" UNTIL NONE IS LEFT
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int secondsToRemoveOccurrences_1(String s) {
        char[] chars = s.toCharArray();
        int seconds = 0;

        boolean changed = true;
        while (changed) {
            changed = false;
            // all swaps of one second happen simultaneously, so skip past a swapped pair
            int i = 0;
            while (i < chars.length - 1) {
                if (chars[i] == '0' && chars[i + 1] == '1') {
                    chars[i] = '1';
                    chars[i + 1] = '0';
                    changed = true;
                    i += 2;
                } else {
                    i++;
                }
            }
            if (changed) {
                seconds++;
            }
        }
        return seconds;
    }
}
