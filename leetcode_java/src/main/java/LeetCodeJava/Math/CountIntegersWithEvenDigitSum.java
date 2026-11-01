package LeetCodeJava.Math;

// https://leetcode.com/problems/count-integers-with-even-digit-sum/description/

/**
 * 2180. Count Integers With Even Digit Sum
 * Easy
 *
 * Given a positive integer num, return the number of positive integers less than or equal to num whose digit sums are even.
 *
 * The digit sum of a positive integer is the sum of all its digits.
 *
 * Example 1:
 *
 * Input: num = 4
 * Output: 2
 * Explanation:
 * The only integers less than or equal to 4 whose digit sums are even are 2 and 4.
 *
 * Example 2:
 *
 * Input: num = 30
 * Output: 14
 * Explanation:
 * The 14 integers less than or equal to 30 whose digit sums are even are
 * 2, 4, 6, 8, 11, 13, 15, 17, 19, 20, 22, 24, 26, and 28.
 *
 * Constraints:
 *
 * 1 <= num <= 1000
 *
 */
public class CountIntegersWithEvenDigitSum {

    // V0
    // IDEA: MATH / COUNTING - EVERY BLOCK OF 10 (SAME PREFIX) HOLDS EXACTLY 5 EVEN DIGIT SUMS
    /**
     * time = O(log num)
     * space = O(1)
     */
    public int countEven(int num) {
        // write num = 10 * prefix + lastDigit
        int prefix = num / 10;
        int lastDigit = num % 10;

        // full decades 0 .. 10 * prefix - 1 contribute 5 each;
        // minus 1 because the number 0 is counted there but is not positive
        int count = prefix * 5 - 1;

        // digit sum of the prefix decides which last digits give an even total
        int prefixDigitSum = 0;
        int rest = prefix;
        while (rest > 0) {
            prefixDigitSum += rest % 10;
            rest /= 10;
        }

        // tail 10 * prefix .. 10 * prefix + lastDigit:
        // count last digits d in [0, lastDigit] whose parity equals the prefix sum's parity
        for (int d = 0; d <= lastDigit; d++) {
            if ((prefixDigitSum + d) % 2 == 0) {
                count++;
            }
        }
        return count;
    }

    // V1
    // IDEA: BRUTE FORCE - num <= 1000, SO CHECK EVERY INTEGER'S DIGIT SUM DIRECTLY
    /**
     * time = O(num * log num)
     * space = O(1)
     */
    public int countEven_1(int num) {
        int count = 0;
        for (int x = 1; x <= num; x++) {
            // add up the digits of x
            int digitSum = 0;
            int rest = x;
            while (rest > 0) {
                digitSum += rest % 10;
                rest /= 10;
            }
            if (digitSum % 2 == 0) {
                count++;
            }
        }
        return count;
    }
}
