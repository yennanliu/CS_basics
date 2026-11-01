package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/minimum-number-of-flips-to-make-the-binary-string-alternating/description/

/**
 * 1888. Minimum Number of Flips to Make The Binary String Alternating
 * Medium
 *
 * You are given a binary string s. You are allowed to perform two types of operations
 * on the string in any sequence:
 *
 * Type-1: Remove the character at the start of the string s and append it to the end
 * of the string.
 * Type-2: Pick any character in s and flip its value, i.e., if its value is '0' it
 * becomes '1' and vice-versa.
 *
 * Return the minimum number of type-2 operations you need to perform such that s
 * becomes alternating.
 *
 * The string is called alternating if no two adjacent characters are equal.
 *
 * For example, the strings "010" and "1010" are alternating, while the string "0100"
 * is not.
 *
 *
 * Example 1:
 *
 * Input: s = "111000"
 * Output: 2
 * Explanation: Use the first operation two times to make s = "100011".
 * Then, use the second operation on the third and sixth elements to make s = "101010".
 *
 * Example 2:
 *
 * Input: s = "010"
 * Output: 0
 * Explanation: The string is already alternating.
 *
 * Example 3:
 *
 * Input: s = "1110"
 * Output: 1
 * Explanation: Use the second operation on the second element to make s = "1010".
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 10^5
 * s[i] is either '0' or '1'.
 *
 */
public class MinimumNumberOfFlipsToMakeTheBinaryStringAlternating {

    // V0
    // IDEA: DOUBLE THE STRING + SLIDING WINDOW OF SIZE n
    // a type-1 op is a rotation, and every rotation of s is a length-n window of s + s.
    // keep the mismatch counts against the two absolute-index patterns
    //   A: even index -> '0', odd -> '1'   ("0101...")
    //   B: the complement of A             ("1010...")
    // a character mismatches exactly one of A / B, so one comparison updates both counters.
    // a window starting at an odd index just swaps A and B, and we take the min of both anyway.
    /**
     * time = O(N)
     * space = O(N)   (the doubled string; index with i % n instead to make it O(1))
     */
    public int minFlips(String s) {
        // edge case: nothing to flip
        if (s == null || s.length() == 0) {
            return 0;
        }
        int n = s.length();
        String doubled = s + s;

        int costA = 0; // mismatches vs "0101..." over the current window
        int costB = 0; // mismatches vs "1010..." over the current window
        int best = n;

        for (int i = 0; i < doubled.length(); i++) {
            // add the character entering the window on the right
            if (doubled.charAt(i) != expectedA(i)) {
                costA += 1;
            } else {
                costB += 1;
            }

            // window longer than n -> remove the character falling off the left
            if (i >= n) {
                int leftIdx = i - n;
                if (doubled.charAt(leftIdx) != expectedA(leftIdx)) {
                    costA -= 1;
                } else {
                    costB -= 1;
                }
            }

            // a full window of length n ends at i
            if (i >= n - 1) {
                best = Math.min(best, Math.min(costA, costB));
            }
        }
        return best;
    }

    /** The character pattern A ("0101...") expects at absolute index idx. */
    private char expectedA(int idx) {
        return (idx % 2 == 0) ? '0' : '1';
    }
}
