package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/sqrtx/

/**
 *  69. Sqrt(x)
 *  Easy
 *
 *  Given a non-negative integer x, return the square root of x rounded down to
 *  the nearest integer. The returned integer should be non-negative as well.
 *
 *  You must not use any built-in exponent function or operator.
 *
 *  Example 1:
 *
 *  Input: x = 4
 *  Output: 2
 *
 *  Example 2:
 *
 *  Input: x = 8
 *  Output: 2
 *  Explanation: The square root of 8 is 2.82842..., and since we round it down
 *  to the nearest integer, 2 is returned.
 *
 *  Constraints:
 *
 *  0 <= x <= 2^31 - 1
 */
public class SqrtX {

    // V0
    // IDEA: binary search on answer, find the biggest m with m * m <= x
    /**
     * time = O(log x)
     * space = O(1)
     */
    public int mySqrt(int x) {
        if (x <= 1) {
            return x;
        }
        int l = 1;
        int r = x;
        int res = 1;
        while (l <= r) {
            int mid = l + (r - l) / 2;
            // NOTE !!! use long to avoid int overflow on mid * mid
            long sq = (long) mid * mid;
            if (sq == x) {
                return mid;
            } else if (sq < x) {
                res = mid;
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return res;
    }

    // V1
    // IDEA: Newton's method  (x_{k+1} = (x_k + n / x_k) / 2)
    /**
     * time = O(log x)
     * space = O(1)
     */
    public int mySqrt_1(int x) {
        if (x <= 1) {
            return x;
        }
        long r = x;
        while (r * r > x) {
            r = (r + x / r) / 2;
        }
        return (int) r;
    }
}
