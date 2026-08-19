package LeetCodeJava.Math;

// https://leetcode.com/problems/factorial-trailing-zeroes/

/**
 *  172. Factorial Trailing Zeroes
 *  Medium
 *
 *  Given an integer n, return the number of trailing zeroes in n!.
 *  Note that n! = n * (n - 1) * (n - 2) * ... * 3 * 2 * 1.
 *
 *  Example 1:
 *    Input: n = 3
 *    Output: 0        (3! = 6, no trailing zero)
 *
 *  Example 2:
 *    Input: n = 5
 *    Output: 1        (5! = 120, one trailing zero)
 *
 *  Example 3:
 *    Input: n = 0
 *    Output: 0
 *
 *  Constraints:
 *    0 <= n <= 10^4
 *
 *  Follow up: Could you write a solution that works in logarithmic time complexity?
 */
public class FactorialTrailingZeroes {

    // V0
    // IDEA: a trailing zero needs a 2*5 pair; 2s are plentiful, so just count factors of 5
    /**
     * time = O(log n)
     * space = O(1)
     */
    public int trailingZeroes(int n) {

        int count = 0;
        // n/5 + n/25 + n/125 + ...
        for (long p = 5; p <= n; p *= 5) {
            count += n / p;
        }
        return count;
    }
}
