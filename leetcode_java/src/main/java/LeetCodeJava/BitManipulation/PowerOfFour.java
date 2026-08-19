package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/power-of-four/

/**
 *  342. Power of Four
 *  Easy
 *
 *  Given an integer n, return true if it is a power of four. Otherwise, return
 *  false. An integer n is a power of four if there exists an integer x such that
 *  n == 4^x.
 *
 *  Example 1:
 *   Input: n = 16
 *   Output: true
 *
 *  Example 2:
 *   Input: n = 5
 *   Output: false
 *
 *  Example 3:
 *   Input: n = 1
 *   Output: true
 *
 *  Constraints:
 *   -2^31 <= n <= 2^31 - 1
 *
 *  Follow up: Could you solve it without loops/recursion?
 */
public class PowerOfFour {

    // V0
    // IDEA: power of four == power of two whose single set bit sits on an EVEN
    //       position; 0x55555555 masks exactly those positions.
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean isPowerOfFour(int n) {
        return n > 0 && (n & (n - 1)) == 0 && (n & 0x55555555) != 0;
    }
}
