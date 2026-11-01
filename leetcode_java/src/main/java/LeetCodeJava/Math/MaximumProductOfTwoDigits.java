package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-product-of-two-digits/description/

/**
 * 3536. Maximum Product of Two Digits
 * Easy
 *
 * You are given a positive integer n.
 *
 * Return the maximum product of any two digits in n.
 *
 * Note: You may use the same digit twice if it appears more than once in n.
 *
 * Example 1:
 *
 * Input: n = 31
 *
 * Output: 3
 *
 * Explanation:
 *
 * The digits of n are [3, 1].
 *
 * The possible products of any two digits are: 3 * 1 = 3.
 *
 * The maximum product is 3.
 *
 * Example 2:
 *
 * Input: n = 22
 *
 * Output: 4
 *
 * Explanation:
 *
 * The digits of n are [2, 2].
 *
 * The possible products of any two digits are: 2 * 2 = 4.
 *
 * The maximum product is 4.
 *
 * Example 3:
 *
 * Input: n = 124
 *
 * Output: 8
 *
 * Explanation:
 *
 * The digits of n are [1, 2, 4].
 *
 * The possible products of any two digits are: 1 * 2 = 2, 1 * 4 = 4, 2 * 4 = 8.
 *
 * The maximum product is 8.
 *
 * Constraints:
 *
 * 10 <= n <= 10^9
 *
 */
public class MaximumProductOfTwoDigits {

    // V0
    // IDEA: TAKE THE TWO LARGEST DIGITS
    //       digits are non-negative, so the product of the two largest digits beats every other
    //       pair. taking two different POSITIONS respects "the same digit twice only if it
    //       appears twice". n >= 10 guarantees at least two digits.
    /**
     * time = O(D)    (D = number of digits, <= 10)
     * space = O(1)
     */
    public int maxProduct(int n) {
        int largest = -1;
        int secondLargest = -1;
        int remaining = n;
        while (remaining > 0) {
            int digit = remaining % 10;
            remaining /= 10;
            // keep the two largest digits seen so far
            if (digit > largest) {
                secondLargest = largest;
                largest = digit;
            } else if (digit > secondLargest) {
                secondLargest = digit;
            }
        }
        return largest * secondLargest;
    }
}
