package LeetCodeJava.Math;

// https://leetcode.com/problems/power-of-three/

/**
 *  326. Power of Three
 *  Easy
 *
 *  Given an integer n, return true if it is a power of three. Otherwise, return false.
 *  An integer n is a power of three, if there exists an integer x such that n == 3^x.
 *
 *  Example 1:
 *
 *  Input: n = 27
 *  Output: true
 *
 *  Example 2:
 *
 *  Input: n = 0
 *  Output: false
 *
 *  Example 3:
 *
 *  Input: n = -1
 *  Output: false
 *
 *  Constraints:
 *
 *  -2^31 <= n <= 2^31 - 1
 */
public class PowerOfThree {

    // V0
    // IDEA: keep dividing by 3 while divisible
    /**
     * time = O(log n)
     * space = O(1)
     */
    public boolean isPowerOfThree(int n) {
        if (n < 1) {
            return false;
        }
        while (n % 3 == 0) {
            n /= 3;
        }
        return n == 1;
    }

    // V1
    // IDEA: 1162261467 is the largest power of 3 fitting in a 32-bit int; since 3 is prime,
    //       any power of 3 must divide it
    /**
     * time = O(1)
     * space = O(1)
     */
    public boolean isPowerOfThree_1(int n) {
        return n > 0 && 1162261467 % n == 0;
    }
}
