package LeetCodeJava.String;

// https://leetcode.com/problems/longer-contiguous-segments-of-ones-than-zeros/description/
/**
 * 1869. Longer Contiguous Segments of Ones than Zeros
 * Easy
 *
 * Given a binary string s, return true if the longest contiguous segment of 1's is strictly longer
 * than the longest contiguous segment of 0's in s, or return false otherwise.
 *
 * For example, in s = "110100010" the longest continuous segment of 1s has length 2, and the longest
 * continuous segment of 0s has length 3.
 *
 * Note that if there are no 0's, then the longest continuous segment of 0's is considered to have a
 * length 0. The same applies if there is no 1's.
 *
 *
 * Example 1:
 *
 * Input: s = "1101"
 * Output: true
 *
 * Example 2:
 *
 * Input: s = "111000"
 * Output: false
 *
 * Example 3:
 *
 * Input: s = "110100010"
 * Output: false
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s[i] is either '0' or '1'.
 *
 */
public class LongerContiguousSegmentsOfOnesThanZeros {

    // V0
    // IDEA: SINGLE PASS RUN LENGTH (track the longest run of each character)
    // a character that never appears keeps a best run of 0, matching the statement's rule.
    /**
     * time = O(N)
     * space = O(1)
     */
    public boolean checkZeroOnes(String s) {
        int longestOnes = 0;
        int longestZeros = 0;
        int run = 0;
        for (int i = 0; i < s.length(); i++) {
            // the streak continues on the same character, otherwise restarts at 1
            if (i > 0 && s.charAt(i) == s.charAt(i - 1)) {
                run++;
            } else {
                run = 1;
            }
            if (s.charAt(i) == '1') {
                longestOnes = Math.max(longestOnes, run);
            } else {
                longestZeros = Math.max(longestZeros, run);
            }
        }
        return longestOnes > longestZeros;
    }
}
