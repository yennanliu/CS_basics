package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-sum-of-four-digit-number-after-splitting-digits/description/

import java.util.Arrays;

/**
 * 2160. Minimum Sum of Four Digit Number After Splitting Digits
 * Easy
 *
 * You are given a positive integer num consisting of exactly four digits. Split num into two new integers
 * new1 and new2 by using the digits found in num. Leading zeros are allowed in new1 and new2, and all the
 * digits found in num must be used.
 *
 * For example, given num = 2932, you have the following digits: two 2's, one 9 and one 3. Some of the
 * possible pairs [new1, new2] are [22, 93], [23, 92], [223, 9] and [2, 329].
 *
 * Return the minimum possible sum of new1 and new2.
 *
 *
 * Example 1:
 *
 * Input: num = 2932
 * Output: 52
 *
 * Example 2:
 *
 * Input: num = 4009
 * Output: 13
 *
 *
 * Constraints:
 *
 * 1000 <= num <= 9999
 *
 */
public class MinimumSumOfFourDigitNumberAfterSplittingDigits {

    // V0
    // IDEA: SORT THE DIGITS; THE TWO SMALLEST BECOME THE TENS DIGITS OF TWO 2-DIGIT NUMBERS
    /**
     * time = O(1)
     * space = O(1)
     */
    public int minimumSum(int num) {
        int[] digits = new int[4];
        int remaining = num;
        for (int i = 0; i < 4; i++) {
            digits[i] = remaining % 10;
            remaining /= 10;
        }
        Arrays.sort(digits);

        // a 2+2 split beats 1+3 (a third digit would carry weight 100);
        // the sum is 10 * (tens digits) + (units digits), so the smallest two go in the tens place
        int first = digits[0] * 10 + digits[2];
        int second = digits[1] * 10 + digits[3];
        return first + second;
    }
}
