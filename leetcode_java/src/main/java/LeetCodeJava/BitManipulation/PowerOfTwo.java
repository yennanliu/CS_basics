package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/power-of-two/

/**
 *  231. Power of Two
 *  Easy
 *
 *  Given an integer n, return true if it is a power of two. Otherwise, return
 *  false. An integer n is a power of two if there exists an integer x such that
 *  n == 2^x.
 *
 *  Example 1:
 *   Input: n = 1
 *   Output: true
 *
 *  Example 2:
 *   Input: n = 16
 *   Output: true
 *
 *  Example 3:
 *   Input: n = 3
 *   Output: false
 *
 *  Constraints:
 *   -2^31 <= n <= 2^31 - 1
 *
 *  Follow up: Could you solve it without loops/recursion?
 */
public class PowerOfTwo {

    // V0
    // IDEA: a power of two has exactly one set bit, so n & (n - 1) clears it and
    //       yields 0 (guard n > 0 to reject 0 and negatives).
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
}
