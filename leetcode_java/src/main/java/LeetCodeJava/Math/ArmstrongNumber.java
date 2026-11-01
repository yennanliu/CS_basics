package LeetCodeJava.Math;

// https://leetcode.com/problems/armstrong-number/description/

/**
 * 1134. Armstrong Number
 * Easy
 *
 * Given an integer n, return true if and only if it is an Armstrong number.
 *
 * The k-digit number n is an Armstrong number if and only if the kth power of each digit sums
 * to n.
 *
 *
 * Example 1:
 *
 * Input: n = 153
 * Output: true
 * Explanation: 153 is a 3-digit number, and 153 = 1^3 + 5^3 + 3^3.
 *
 * Example 2:
 *
 * Input: n = 123
 * Output: false
 * Explanation: 123 is a 3-digit number, and 123 != 1^3 + 2^3 + 3^3 = 36.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^8
 *
 */
public class ArmstrongNumber {

    // V0
    // IDEA: SIMULATION - COUNT THE DIGITS k, THEN SUM digit^k OVER ALL DIGITS
    /**
     * time = O(log N)
     * space = O(1)
     */
    public boolean isArmstrong(int n) {
        // 1) number of digits
        int digitCount = 0;
        for (int x = n; x > 0; x /= 10) {
            digitCount++;
        }

        // 2) sum of each digit raised to digitCount (long: 9 digits of 9^9 overflow int)
        long sum = 0;
        for (int x = n; x > 0; x /= 10) {
            sum += power(x % 10, digitCount);
        }

        // 3) compare with n
        return sum == n;
    }

    /** base^exponent by repeated multiplication. */
    private long power(int base, int exponent) {
        long result = 1;
        for (int i = 0; i < exponent; i++) {
            result *= base;
        }
        return result;
    }
}
