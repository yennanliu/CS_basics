package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-number-of-swaps-to-make-the-binary-string-alternating/description/

/**
 * 1864. Minimum Number of Swaps to Make the Binary String Alternating
 * Medium
 *
 * Given a binary string s, return the minimum number of character swaps to make
 * it alternating, or -1 if it is impossible.
 *
 * The string is called alternating if no two adjacent characters are equal. For
 * example, the strings "010" and "1010" are alternating, while the string "0100"
 * is not.
 *
 * Any two characters may be swapped, even if they are not adjacent.
 *
 *
 * Example 1:
 *
 * Input: s = "111000"
 * Output: 1
 * Explanation: Swap positions 1 and 4: "111000" -> "101010"
 * The string is now alternating.
 *
 * Example 2:
 *
 * Input: s = "010"
 * Output: 0
 * Explanation: The string is already alternating, no swaps are needed.
 *
 * Example 3:
 *
 * Input: s = "1110"
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 1000
 * s[i] is either '0' or '1'.
 *
 */
public class MinimumNumberOfSwapsToMakeTheBinaryStringAlternating {

    // V0
    // IDEA: COUNTING (only "0101..." and "1010..." exist; each swap fixes two mismatches)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minSwaps(String s) {
        int n = s.length();
        int zeros = 0;
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == '0') {
                zeros++;
            }
        }
        int ones = n - zeros;

        // an alternating string needs the two counts within 1 of each other
        if (Math.abs(zeros - ones) > 1) {
            return -1;
        }
        // the more frequent character must take the first position
        if (zeros > ones) {
            return swapsFor(s, '0');
        }
        if (ones > zeros) {
            return swapsFor(s, '1');
        }
        // equal counts: both patterns are possible, take the cheaper one
        return Math.min(swapsFor(s, '0'), swapsFor(s, '1'));
    }

    /** Swaps needed to turn s into the alternating pattern starting with firstChar. */
    private int swapsFor(String s, char firstChar) {
        char otherChar = (firstChar == '0') ? '1' : '0';
        int mismatches = 0;
        for (int i = 0; i < s.length(); i++) {
            char expected = (i % 2 == 0) ? firstChar : otherChar;
            if (s.charAt(i) != expected) {
                mismatches++;
            }
        }
        // one swap trades a misplaced '0' with a misplaced '1', fixing two positions
        return mismatches / 2;
    }
}
