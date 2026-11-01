package LeetCodeJava.Greedy;

// https://leetcode.com/problems/smallest-value-of-the-rearranged-number/description/

import java.util.Arrays;

/**
 * 2165. Smallest Value of the Rearranged Number
 * Medium
 *
 * You are given an integer num. Rearrange the digits of num such that its value is minimized and it does
 * not contain any leading zeros.
 *
 * Return the rearranged number with minimal value.
 *
 * Note that the sign of the number does not change after rearranging the digits.
 *
 *
 * Example 1:
 *
 * Input: num = 310
 * Output: 103
 *
 * Example 2:
 *
 * Input: num = -7605
 * Output: -7650
 *
 *
 * Constraints:
 *
 * -10^15 <= num <= 10^15
 *
 */
public class SmallestValueOfTheRearrangedNumber {

    // V0
    // IDEA: THE SIGN DECIDES THE SORT DIRECTION; LEADING ZEROS ONLY MATTER WHEN POSITIVE
    /**
     * time = O(D log D), D = number of digits
     * space = O(D)
     */
    public long smallestNumber(long num) {
        if (num == 0) {
            return 0;
        }

        boolean negative = num < 0;
        char[] digits = Long.toString(Math.abs(num)).toCharArray();
        Arrays.sort(digits);

        if (negative) {
            // negative: the magnitude should be as LARGE as possible -> digits descending
            StringBuilder descending = new StringBuilder(new String(digits)).reverse();
            return -Long.parseLong(descending.toString());
        }

        // positive: ascending digits, but a leading zero is illegal -
        // swap the first non-zero digit into position 0, leaving the zeros right behind it
        if (digits[0] == '0') {
            int firstNonZero = 0;
            while (digits[firstNonZero] == '0') {
                firstNonZero++;
            }
            digits[0] = digits[firstNonZero];
            digits[firstNonZero] = '0';
        }
        return Long.parseLong(new String(digits));
    }
}
