package LeetCodeJava.Math;

// https://leetcode.com/problems/reverse-integer/

/**
 *  7. Reverse Integer
 *  Medium
 *
 *  Given a signed 32-bit integer x, return x with its digits reversed.
 *  If reversing x causes the value to go outside the signed 32-bit integer
 *  range [-2^31, 2^31 - 1], then return 0.
 *
 *  Assume the environment does not allow you to store 64-bit integers
 *  (signed or unsigned).
 *
 *  Example 1:
 *    Input: x = 123
 *    Output: 321
 *
 *  Example 2:
 *    Input: x = -123
 *    Output: -321
 *
 *  Example 3:
 *    Input: x = 120
 *    Output: 21
 *
 *  Constraints:
 *    -2^31 <= x <= 2^31 - 1
 */
public class ReverseInteger {

    // V0
    // IDEA: pop last digit, push into res, check 32-bit overflow BEFORE multiplying
    /**
     * time = O(log x)
     * space = O(1)
     */
    public int reverse(int x) {

        int res = 0;
        while (x != 0) {
            int digit = x % 10;   // java truncates toward zero, sign kept
            x /= 10;

            // overflow check without using 64-bit
            if (res > Integer.MAX_VALUE / 10
                    || (res == Integer.MAX_VALUE / 10 && digit > 7)) {
                return 0;
            }
            if (res < Integer.MIN_VALUE / 10
                    || (res == Integer.MIN_VALUE / 10 && digit < -8)) {
                return 0;
            }

            res = res * 10 + digit;
        }

        return res;
    }

    // V1
    // IDEA: accumulate in a long then range-check once (simpler, needs 64-bit)
    /**
     * time = O(log x)
     * space = O(1)
     */
    public int reverse_1(int x) {
        long res = 0;
        long n = x;
        while (n != 0) {
            res = res * 10 + n % 10;
            n /= 10;
        }
        if (res > Integer.MAX_VALUE || res < Integer.MIN_VALUE) {
            return 0;
        }
        return (int) res;
    }
}
