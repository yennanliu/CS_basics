package LeetCodeJava.Math;

// https://leetcode.com/problems/check-divisibility-by-digit-sum-and-product/description/

/**
 * 3622. Check Divisibility by Digit Sum and Product
 * Easy
 *
 * You are given a positive integer n. Determine whether n is divisible by the sum
 * of the following two values:
 *
 * The digit sum of n (the sum of its digits).
 * The digit product of n (the product of its digits).
 *
 * Return true if n is divisible by this sum; otherwise, return false.
 *
 *
 * Example 1:
 *
 * Input: n = 99
 * Output: true
 * Explanation:
 * Since 99 is divisible by the sum (9 + 9 = 18) plus product (9 * 9 = 81) of its
 * digits (total 99), the output is true.
 *
 * Example 2:
 *
 * Input: n = 23
 * Output: false
 * Explanation:
 * Since 23 is not divisible by the sum (2 + 3 = 5) plus product (2 * 3 = 6) of its
 * digits (total 11), the output is false.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^6
 *
 */
public class CheckDivisibilityByDigitSumAndProduct {

    // V0
    // IDEA: DIGIT SCAN
    //       peel the decimal digits off, accumulate their sum and product, then test one
    //       divisibility. the leading digit is non-zero so the sum is >= 1 and the divisor
    //       sum + product is never 0, even when a zero digit makes the product 0.
    /**
     * time = O(log N)
     * space = O(1)
     */
    public boolean checkDivisibility(int n) {
        int digitSum = 0;
        long digitProduct = 1;
        int remaining = n;
        while (remaining > 0) {
            int digit = remaining % 10;
            remaining /= 10;
            digitSum += digit;
            digitProduct *= digit;
        }
        return n % (digitSum + digitProduct) == 0;
    }
}
