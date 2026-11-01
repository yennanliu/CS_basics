package LeetCodeJava.Math;

// https://leetcode.com/problems/count-the-digits-that-divide-a-number/description/

/**
 * 2520. Count the Digits That Divide a Number
 * Easy
 *
 * Given an integer num, return the number of digits in num that divide num.
 *
 * An integer val divides nums if nums % val == 0.
 *
 *
 * Example 1:
 *
 * Input: num = 7
 * Output: 1
 * Explanation: 7 divides itself, hence the answer is 1.
 *
 * Example 2:
 *
 * Input: num = 121
 * Output: 2
 * Explanation: 121 is divisible by 1, but not 2. Since 1 occurs twice as a digit, we return 2.
 *
 * Example 3:
 *
 * Input: num = 1248
 * Output: 4
 * Explanation: 1248 is divisible by all of its digits, hence the answer is 4.
 *
 *
 * Constraints:
 *
 * 1 <= num <= 10^9
 * num does not contain 0 as one of its digits.
 *
 */
public class CountTheDigitsThatDivideANumber {

    // V0
    // IDEA: DIGIT ENUMERATION - PEEL DIGITS WITH % 10 AND / 10, TEST EACH AGAINST THE ORIGINAL num
    //   no digit is 0 (constraint), and a repeated digit counts every time it occurs.
    /**
     * time = O(log num)
     * space = O(1)
     */
    public int countDigits(int num) {
        int count = 0;
        int rest = num;
        while (rest > 0) {
            int digit = rest % 10;
            rest /= 10;
            // always divide the ORIGINAL num, not the shrinking remainder
            if (num % digit == 0) {
                count++;
            }
        }
        return count;
    }
}
