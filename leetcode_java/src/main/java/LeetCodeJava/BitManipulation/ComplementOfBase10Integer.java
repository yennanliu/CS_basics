package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/complement-of-base-10-integer/description/

/**
 * 1009. Complement of Base 10 Integer
 * Easy
 *
 * The complement of an integer is the integer you get when you flip all the 0's to 1's and all
 * the 1's to 0's in its binary representation.
 *
 * For example, The integer 5 is "101" in binary and its complement is "010" which is the integer 2.
 *
 * Given an integer n, return its complement.
 *
 *
 * Example 1:
 *
 * Input: n = 5
 * Output: 2
 * Explanation: 5 is "101" in binary, with complement "010" in binary, which is 2 in base-10.
 *
 * Example 2:
 *
 * Input: n = 7
 * Output: 0
 * Explanation: 7 is "111" in binary, with complement "000" in binary, which is 0 in base-10.
 *
 * Example 3:
 *
 * Input: n = 10
 * Output: 5
 * Explanation: 10 is "1010" in binary, with complement "0101" in binary, which is 5 in base-10.
 *
 *
 * Constraints:
 *
 * 0 <= n < 10^9
 *
 *
 * Note: This question is the same as 476: Number Complement
 *
 */
public class ComplementOfBase10Integer {

    // V0
    // IDEA: BIT MANIPULATION - XOR WITH AN ALL-ONES MASK OF THE SAME BIT LENGTH
    //   n = 1010, mask = 1111, n ^ mask = 0101
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int bitwiseComplement(int n) {
        // "0" flips to "1"
        if (n == 0) {
            return 1;
        }

        // grow the mask 1 -> 11 -> 111 ... until it covers every bit of n
        int mask = 1;
        while (mask < n) {
            mask = (mask << 1) | 1;
        }

        // XOR with 1 flips a bit, so this flips exactly n's significant bits
        return n ^ mask;
    }

    // V1
    // IDEA: STRING - FLIP EVERY CHARACTER OF THE BINARY STRING, THEN PARSE IT BACK
    /**
     * time = O(log N)
     * space = O(log N)
     */
    public int bitwiseComplement_1(int n) {
        String bits = Integer.toBinaryString(n);
        StringBuilder flipped = new StringBuilder();
        for (int i = 0; i < bits.length(); i++) {
            if (bits.charAt(i) == '0') {
                flipped.append('1');
            } else {
                flipped.append('0');
            }
        }
        return Integer.parseInt(flipped.toString(), 2);
    }
}
