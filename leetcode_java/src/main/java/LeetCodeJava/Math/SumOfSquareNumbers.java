package LeetCodeJava.Math;

// https://leetcode.com/problems/sum-of-square-numbers/

/**
 *  633. Sum of Square Numbers
 *  Medium
 *
 *  Given a non-negative integer c, decide whether there're two integers a and b
 *  such that a^2 + b^2 = c.
 *
 *  Example 1:
 *    Input: c = 5
 *    Output: true
 *    Explanation: 1 * 1 + 2 * 2 = 5
 *
 *  Example 2:
 *    Input: c = 3
 *    Output: false
 *
 *  Constraints:
 *   - 0 <= c <= 2^31 - 1
 */
public class SumOfSquareNumbers {

    // V0
    // IDEA: TWO POINTERS on [0, sqrt(c)] -- shrink/expand by comparing a^2+b^2 with c
    /**
     * time = O(sqrt(c))
     * space = O(1)
     */
    public boolean judgeSquareSum(int c) {

        long left = 0;
        long right = (long) Math.sqrt((double) c);

        // Math.sqrt on a double may be off by one, push right up if needed
        while ((right + 1) * (right + 1) <= c) {
            right++;
        }

        while (left <= right) {
            long cur = left * left + right * right;
            if (cur == c) {
                return true;
            } else if (cur < c) {
                left++;
            } else {
                right--;
            }
        }

        return false;
    }
}
