package LeetCodeJava.Math;

// https://leetcode.com/problems/super-pow/

/**
 *  372. Super Pow
 *  Medium
 *
 *  Your task is to calculate a^b mod 1337 where a is a positive integer and b is an
 *  extremely large positive integer given in the form of an array.
 *
 *  Example 1:
 *
 *  Input: a = 2, b = [3]
 *  Output: 8
 *
 *  Example 2:
 *
 *  Input: a = 2, b = [1,0]
 *  Output: 1024
 *
 *  Example 3:
 *
 *  Input: a = 2147483647, b = [2,0,0]
 *  Output: 1198
 *
 *  Constraints:
 *
 *  1 <= a <= 2^31 - 1
 *  1 <= b.length <= 2000
 *  0 <= b[i] <= 9
 *  b does not contain leading zeros.
 */
public class SuperPow {

    private static final int MOD = 1337;

    // V0
    // IDEA: Horner's rule on the digits: a^(10*d + e) = (a^d)^10 * a^e (all mod 1337)
    /**
     * time = O(n)   n = b.length (each step does O(1) modular multiplications)
     * space = O(1)
     */
    public int superPow(int a, int[] b) {
        int base = a % MOD;
        int result = 1;
        for (int digit : b) {
            result = modPow(result, 10) * modPow(base, digit) % MOD;
        }
        return result;
    }

    // x^n mod 1337, n small (<= 10)
    private int modPow(int x, int n) {
        x %= MOD;
        long result = 1;
        long cur = x;
        while (n > 0) {
            if ((n & 1) == 1) {
                result = result * cur % MOD;
            }
            cur = cur * cur % MOD;
            n >>= 1;
        }
        return (int) result;
    }
}
