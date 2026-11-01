package LeetCodeJava.Math;

// https://leetcode.com/problems/hexadecimal-and-hexatrigesimal-conversion/description/

/**
 * 3602. Hexadecimal and Hexatrigesimal Conversion
 * Easy
 *
 * You are given an integer n.
 *
 * Return the concatenation of the hexadecimal representation of n^2 and the hexatrigesimal representation of n^3.
 *
 * A hexadecimal number is defined as a base-16 numeral system that uses the digits 0 - 9 along with the uppercase letters A - F to represent values from 0 to 15.
 *
 * A hexatrigesimal number is defined as a base-36 numeral system that uses the digits 0 - 9 along with the uppercase letters A - Z to represent values from 0 to 35.
 *
 *
 * Example 1:
 *
 * Input: n = 13
 * Output: "A91P1"
 * Explanation:
 * n^2 = 13 * 13 = 169. In hexadecimal, 169 is A9.
 * n^3 = 13 * 13 * 13 = 2197. In hexatrigesimal, 2197 is 1P1.
 * Concatenating both gives us A9 + 1P1 = A91P1.
 *
 * Example 2:
 *
 * Input: n = 36
 * Output: "5101000"
 * Explanation:
 * n^2 = 36 * 36 = 1296. In hexadecimal, 1296 is 510.
 * n^3 = 36 * 36 * 36 = 46656. In hexatrigesimal, 46656 is 1000.
 * Concatenating both gives us 510 + 1000 = 5101000.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 1000
 *
 */
public class HexadecimalAndHexatrigesimalConversion {

    private static final String DIGITS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    // V0
    // IDEA: ONE GENERIC BASE-CONVERSION HELPER, CALLED TWICE
    //       repeated division peels off the least significant digit first, so the digits come
    //       out reversed and are flipped at the end. bases 16 and 36 share the "0-9 then A-Z"
    //       alphabet. n <= 1000 -> n^3 <= 10^9 fits in a long.
    /**
     * time = O(log N)
     * space = O(log N)
     */
    public String concatHex36(int n) {
        long square = (long) n * n;
        long cube = square * n;
        return toBase(square, 16) + toBase(cube, 36);
    }

    /** Uppercase representation of x in the given base. */
    private String toBase(long x, int base) {
        if (x == 0) {
            return "0";
        }
        StringBuilder reversed = new StringBuilder();
        long remaining = x;
        while (remaining > 0) {
            int digit = (int) (remaining % base);
            reversed.append(DIGITS.charAt(digit));
            remaining /= base;
        }
        return reversed.reverse().toString();
    }
}
