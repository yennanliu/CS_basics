package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/number-complement/description/
/**
 * 476. Number Complement
 * Easy
 *
 * The complement of an integer is the integer you get when you flip all the 0's to
 * 1's and all the 1's to 0's in its binary representation.
 *
 * - For example, The integer 5 is "101" in binary and its complement is "010"
 *   which is the integer 2.
 *
 * Given an integer num, return its complement.
 *
 * Example 1:
 *
 * Input: num = 5
 * Output: 2
 * Explanation: The binary representation of 5 is 101 (no leading zero bits), and
 * its complement is 010. So you need to output 2.
 *
 * Example 2:
 *
 * Input: num = 1
 * Output: 0
 * Explanation: The binary representation of 1 is 1 (no leading zero bits), and its
 * complement is 0. So you need to output 0.
 *
 * Constraints:
 *
 * 1 <= num < 2^31
 *
 * Note: This question is the same as 1009: Complement of Base 10 Integer.
 *
 */
public class NumberComplement {

    // V0
    // IDEA: XOR WITH AN ALL-ONES MASK OF THE SAME BIT WIDTH
    /**
     *  Flipping every bit == XOR with 1. We only want to flip the bits that are
     *  ACTUALLY part of num (no leading zeros), so build a mask of exactly
     *  `bit length of num` ones:
     *
     *     num  = 5  = 101
     *     mask = (1 << 3) - 1 = 111
     *     5 ^ 7 = 010 = 2
     *
     *  NOTE !!! num can reach 2^31 - 1, so its bit length can be 31 and
     *           `1 << 31` OVERFLOWS int (it goes negative) -> shift on a `long`.
     *
     *  time  = O(1)
     *  space = O(1)
     */
    public int findComplement(int num) {
        // bit length of num (num >= 1, so this is >= 1)
        int bitLength = 32 - Integer.numberOfLeadingZeros(num);

        long mask = (1L << bitLength) - 1;

        return (int) (num ^ mask);
    }


    // V1
    // IDEA: GROW AN ALL-ONES MASK BY SHIFTING
    /**
     *  Build the mask one bit at a time: `mask = mask << 1 | 1` until it covers
     *  num. No leading-zero intrinsic, no pow -- just the loop.
     *
     *  NOTE !!! the accumulator is a `long`, because for num = 2^31 - 1 the mask
     *           would overflow a signed int on the final shift.
     *
     *  time  = O(log num)
     *  space = O(1)
     */
    public int findComplement_1(int num) {
        long mask = 0;
        while (mask < num) {
            mask = (mask << 1) | 1;
        }
        return (int) (num ^ mask);
    }

    // V2
    // IDEA: Integer.highestOneBit -> mask = (highest << 1) - 1
    /**
     *  The JDK already gives the top set bit. Doubling it and subtracting one
     *  produces the all-ones mask of exactly that width in two operations.
     *
     *  The shortest of the four; `(highest << 1)` is done in `long` for the same
     *  overflow reason as V1.
     *
     *  time  = O(1)
     *  space = O(1)
     */
    public int findComplement_2(int num) {
        long highest = Integer.highestOneBit(num);
        long mask = (highest << 1) - 1;
        return (int) (num ^ mask);
    }

    // V3
    // IDEA: FLIP THE BINARY STRING
    /**
     *  Render num in binary, swap every character, parse it back.
     *
     *  Slow and allocation-heavy, but it is the version that most literally does
     *  what the problem says -- handy for explaining the bit trick to someone who
     *  does not yet trust it.
     *
     *  time  = O(log num)
     *  space = O(log num)
     */
    public int findComplement_3(int num) {
        String bits = Integer.toBinaryString(num);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bits.length(); i++) {
            sb.append(bits.charAt(i) == '0' ? '1' : '0');
        }
        return (int) Long.parseLong(sb.toString(), 2);
    }

}
