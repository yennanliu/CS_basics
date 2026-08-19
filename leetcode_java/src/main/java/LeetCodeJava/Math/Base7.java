package LeetCodeJava.Math;

// https://leetcode.com/problems/base-7/

/**
 *  504. Base 7
 *  Easy
 *
 *  Given an integer num, return a string of its base 7 representation.
 *
 *  Example 1:
 *    Input: num = 100
 *    Output: "202"
 *
 *  Example 2:
 *    Input: num = -7
 *    Output: "-10"
 *
 *  Constraints:
 *    -10^7 <= num <= 10^7
 */
public class Base7 {

    // V0
    // IDEA: repeated divmod by 7, collect digits then reverse; handle sign separately
    /**
     * time = O(log n)
     * space = O(log n)
     */
    public String convertToBase7(int num) {
        if (num == 0) {
            return "0";
        }
        boolean neg = num < 0;
        long x = java.lang.Math.abs((long) num);
        StringBuilder sb = new StringBuilder();
        while (x > 0) {
            sb.append(x % 7);
            x /= 7;
        }
        if (neg) {
            sb.append('-');
        }
        return sb.reverse().toString();
    }
}
