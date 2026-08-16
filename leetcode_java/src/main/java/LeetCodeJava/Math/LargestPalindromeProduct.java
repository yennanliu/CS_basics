package LeetCodeJava.Math;

// https://leetcode.com/problems/largest-palindrome-product/description/
/**
 * 479. Largest Palindrome Product
 * Hard
 *
 * Given an integer n, return the largest palindromic integer that can be
 * represented as the product of two n-digits integers. Since the answer can be
 * very large, return it modulo 1337.
 *
 * Example 1:
 *
 * Input: n = 2
 * Output: 987
 * Explanation: 99 x 91 = 9009, 9009 % 1337 = 987
 *
 * Example 2:
 *
 * Input: n = 1
 * Output: 9
 *
 * Constraints:
 *
 * 1 <= n <= 8
 *
 */
public class LargestPalindromeProduct {

    // V0
    // IDEA: ENUMERATE PALINDROMES DOWNWARDS, THEN TRIAL-DIVIDE
    /**
     *  For n >= 2 the product of two n-digit numbers that is a palindrome has an
     *  EVEN number of digits (2n). So build the palindrome from its FIRST HALF:
     *
     *     half = 99  ->  palindrome = 9999
     *     half = 98  ->  palindrome = 9889
     *     ...
     *     half = 90  ->  palindrome = 9009   (= 99 * 91, the answer for n = 2)
     *
     *  Walking `half` DOWNWARDS walks the palindromes in DECREASING order, so the
     *  FIRST one that factors into two n-digit numbers IS the answer.
     *
     *  To test a palindrome x, try divisors t from the largest n-digit number down
     *  while t * t >= x (past that point the other factor would be the bigger one,
     *  already tested). If x % t == 0, then x / t <= t and x / t is still n digits.
     *
     *  n = 1 is the ODD ONE OUT (the answer 9 = 3 * 3 has one digit)
     *  -> handled by the final `return 9`.
     *
     *  NOTE !!! for n = 8 the palindrome reaches ~10^16, which OVERFLOWS int
     *           -> everything here must be `long`.
     *
     *  time  = O(10^n * 10^n) worst case, but in practice the answer is found
     *          after a handful of palindromes
     *  space = O(1)
     */
    public int largestPalindrome(int n) {
        long mx = (long) Math.pow(10, n) - 1; // largest n-digit number

        // `half` runs over the n-digit numbers, BIGGEST first
        for (long half = mx; half > mx / 10; half--) {
            // MIRROR `half` onto itself -> a 2n-digit palindrome
            long x = half;
            long b = half;
            while (b > 0) {
                x = x * 10 + b % 10;
                b /= 10;
            }

            for (long t = mx; t * t >= x; t--) {
                if (x % t == 0) {
                    return (int) (x % 1337);
                }
            }
        }

        // only reachable for n == 1
        return 9;
    }

}
