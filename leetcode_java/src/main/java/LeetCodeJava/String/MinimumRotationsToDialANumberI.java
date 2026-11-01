package LeetCodeJava.String;

// https://leetcode.com/problems/minimum-rotations-to-dial-a-number-i/description/

/**
 * 4070. Minimum Rotations to Dial a Number I
 * Easy
 *
 * You are given a string s of length 10 consisting of digits.
 *
 * The dial contains the digits 0 through 9 in order and is circular, so 0 and
 * 9 are adjacent. The pointer initially points to 0.
 *
 * To dial each digit of s in order, rotate the pointer until it points to that
 * digit. Each rotation moves the pointer to an adjacent digit, and you may
 * rotate in either direction. Dialing a digit that the pointer already points
 * to requires no rotations.
 *
 * Return the minimum total number of rotations needed to dial every digit of s.
 *
 *
 * Example 1:
 *
 * Input: s = "0192837465"
 * Output: 25
 * Explanation:
 * From -> To : Rotations
 * 0 -> 0 : 0,  0 -> 1 : 1,  1 -> 9 : 2,  9 -> 2 : 3,  2 -> 8 : 4,
 * 8 -> 3 : 5,  3 -> 7 : 4,  7 -> 4 : 3,  4 -> 6 : 2,  6 -> 5 : 1
 * The total is 0 + 1 + 2 + 3 + 4 + 5 + 4 + 3 + 2 + 1 = 25.
 *
 * Example 2:
 *
 * Input: s = "1200210200"
 * Output: 12
 * Explanation:
 * The total is 1 + 1 + 2 + 0 + 2 + 1 + 1 + 2 + 2 + 0 = 12.
 *
 *
 * Constraints:
 *
 * s.length == 10
 * s consists only of digits '0' to '9'
 *
 */
public class MinimumRotationsToDialANumberI {

    private static final int DIAL_SIZE = 10;

    // V0
    // IDEA: SIMULATION + CIRCULAR DISTANCE = min(direct, 10 - direct)
    //
    //   each step is independent: wherever the pointer ends up is fixed (it must
    //   point at the digit just dialed), so the total is minimised by taking the
    //   cheapest move at every step.
    //
    //   on a circle of 10 digits, the two ways from pos to val cover the whole
    //   circle between them, so they sum to 10:
    //       direct = |val - pos|     (move without passing the 9/0 seam)
    //       wrap   = 10 - direct     (go the other way, across the seam)
    //
    //   e.g. pos = 1, val = 9 -> direct = 8, wrap = 2 -> 2 rotations (1 -> 0 -> 9)
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minRotations(String s) {
        // edge case: nothing to dial
        if (s == null || s.isEmpty()) {
            return 0;
        }

        int totalRotations = 0;
        int pos = 0; // the pointer starts at 0

        for (int i = 0; i < s.length(); i++) {
            int val = s.charAt(i) - '0';

            int direct = Math.abs(val - pos);
            // NOTE !!! the other direction is 10 - direct, whether val > pos or val < pos
            int wrap = DIAL_SIZE - direct;

            totalRotations += Math.min(direct, wrap);

            // the pointer now rests on the digit just dialed
            pos = val;
        }

        return totalRotations;
    }
}
