package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/binary-number-with-alternating-bits/

/**
 *  693. Binary Number with Alternating Bits
 *  Easy
 *
 *  Given a positive integer, check whether it has alternating bits: namely, if
 *  two adjacent bits will always have different values.
 *
 *  Example 1:
 *  Input: n = 5
 *  Output: true
 *  Explanation: The binary representation of 5 is: 101
 *
 *  Example 2:
 *  Input: n = 7
 *  Output: false
 *  Explanation: The binary representation of 7 is: 111.
 *
 *  Example 3:
 *  Input: n = 11
 *  Output: false
 *  Explanation: The binary representation of 11 is: 1011.
 *
 *  Constraints:
 *  1 <= n <= 2^31 - 1
 */
public class BinaryNumberWithAlternatingBits {

    // V0
    // IDEA: for alternating bits, n ^ (n >> 1) is all ones (e.g. 0b111...1),
    //       and x is all-ones iff x & (x + 1) == 0
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean hasAlternatingBits(int n) {
        long x = (long) n ^ ((long) n >> 1);
        return (x & (x + 1)) == 0;
    }

    // V1
    // IDEA: straightforward scan comparing each bit with the previous one
    /**
     * time = O(32) = O(1)
     * space = O(1)
     */
    public boolean hasAlternatingBits_1(int n) {
        int prev = n & 1;
        n >>= 1;
        while (n != 0) {
            int cur = n & 1;
            if (cur == prev) {
                return false;
            }
            prev = cur;
            n >>= 1;
        }
        return true;
    }

    // V2
    // IDEA: enumerate instead of inspect -- there are only ~31 positive ints with
    //       alternating bits (1, 10, 101, 1010, ... in binary). Generate them all
    //       (each one extends the previous by the opposite bit) and test
    //       membership.
    /**
     * time = O(31) = O(1)
     * space = O(1)
     */
    public boolean hasAlternatingBits_2(int n) {
        long x = 1L;   // "1"
        while (x <= Integer.MAX_VALUE) {
            if (x == n) {
                return true;
            }
            // NOTE !!! append the bit OPPOSITE to the current lowest one
            x = (x << 1) | (1L - (x & 1L));
        }
        return false;
    }
}
