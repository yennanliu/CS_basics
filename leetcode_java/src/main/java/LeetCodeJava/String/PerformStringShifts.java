package LeetCodeJava.String;

// https://leetcode.com/problems/perform-string-shifts/description/

/**
 * 1427. Perform String Shifts
 * Easy
 *
 * You are given a string s containing lowercase English letters, and a matrix
 * shift, where shift[i] = [direction_i, amount_i]:
 *
 * - direction_i can be 0 (for left shift) or 1 (for right shift).
 * - amount_i is the amount by which string s is to be shifted.
 * - A left shift by 1 means remove the first character of s and append it to the end.
 * - Similarly, a right shift by 1 means remove the last character of s and add it
 *   to the beginning.
 *
 * Return the final string after all operations.
 *
 *
 * Example 1:
 *
 * Input: s = "abc", shift = [[0,1],[1,2]]
 * Output: "cab"
 * Explanation:
 * [0,1] means shift to left by 1. "abc" -> "bca"
 * [1,2] means shift to right by 2. "bca" -> "cab"
 *
 * Example 2:
 *
 * Input: s = "abcdefg", shift = [[1,1],[1,1],[0,2],[1,3]]
 * Output: "efgabcd"
 *
 *
 * Constraints:
 *
 * 1 <= s.length <= 100
 * s only contains lower case English letters.
 * 1 <= shift.length <= 100
 * shift[i].length == 2
 * direction_i is either 0 or 1.
 * 0 <= amount_i <= 100
 *
 */
public class PerformStringShifts {

    // V0
    // IDEA: MATH (collapse all shifts into ONE net left-shift)
    //       a right shift by a == a left shift by (n - a), so sum a net LEFT offset,
    //       reduce it mod n, then rotate once
    /**
     * time = O(N + M)   (N = s.length(), M = shift.length)
     * space = O(N)
     */
    public String stringShift(String s, int[][] shift) {
        int n = s.length();

        // net LEFT offset: left shifts add, right shifts subtract
        int leftOffset = 0;
        for (int[] operation : shift) {
            int direction = operation[0];
            int amount = operation[1];
            if (direction == 0) {
                leftOffset += amount;
            } else {
                leftOffset -= amount;
            }
        }

        // Java's % keeps the sign of the dividend, so fold a negative result back into [0, n)
        leftOffset = ((leftOffset % n) + n) % n;

        // a left rotation by k moves s[k..] to the front
        return s.substring(leftOffset) + s.substring(0, leftOffset);
    }
}
