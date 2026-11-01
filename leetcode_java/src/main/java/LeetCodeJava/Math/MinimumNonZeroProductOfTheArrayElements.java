package LeetCodeJava.Math;

// https://leetcode.com/problems/minimum-non-zero-product-of-the-array-elements/description/

/**
 * 1969. Minimum Non-Zero Product of the Array Elements
 * Medium
 *
 * You are given a positive integer p. Consider an array nums (1-indexed) that consists of the integers in the inclusive range [1, 2^p - 1] in their binary representations. You are allowed to do the following operation any number of times:
 *
 * Choose two elements x and y from nums.
 * Choose a bit in x and swap it with its corresponding bit in y. Corresponding bit refers to the bit that is in the same position in the other integer.
 *
 * For example, if x = 1101 and y = 0011, after swapping the 2nd bit from the right, we have x = 1111 and y = 0001.
 *
 * Find the minimum non-zero product of nums after performing the above operation any number of times. Return this product modulo 10^9 + 7.
 *
 * Note: The answer should be the minimum product before the modulo operation is done.
 *
 *
 * Example 1:
 *
 * Input: p = 1
 * Output: 1
 * Explanation: nums = [1].
 * There is only one element, so the product equals that element.
 *
 * Example 2:
 *
 * Input: p = 2
 * Output: 6
 * Explanation: nums = [01, 10, 11].
 * Any swap would either make the product 0 or stay the same.
 * Thus, the array product of 1 * 2 * 3 = 6 is already minimized.
 *
 * Example 3:
 *
 * Input: p = 3
 * Output: 1512
 * Explanation: nums = [001, 010, 011, 100, 101, 110, 111]
 * - In the first operation we can swap the leftmost bit of the second and fifth elements.
 *     - The resulting array is [001, 110, 011, 100, 001, 110, 111].
 * - In the second operation we can swap the middle bit of the third and fourth elements.
 *     - The resulting array is [001, 110, 001, 110, 001, 110, 111].
 * The array product is 1 * 6 * 1 * 6 * 1 * 6 * 7 = 1512, which is the minimum possible product.
 *
 *
 * Constraints:
 *
 * 1 <= p <= 60
 *
 */
public class MinimumNonZeroProductOfTheArrayElements {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: GREEDY / MATH - pair x with its complement 2^p-1-x and drain it into (1, 2^p-2)
    //  swaps keep the sum fixed; spreading values apart minimises the product.
    //  answer = (2^p - 1) * (2^p - 2)^(2^(p-1) - 1), with a huge exponent -> fast power.
    /**
     * time = O(P)
     * space = O(1)
     */
    public int minNonZeroProduct(int p) {
        // largest element: all p bits set (fits in a long for p <= 60)
        long maxElement = (1L << p) - 1;
        // every pair becomes (1, 2^p - 2)
        long pairValue = (maxElement - 1) % MOD;
        // number of such pairs: (2^p - 2) / 2
        long pairCount = (1L << (p - 1)) - 1;

        long result = (maxElement % MOD) * modPow(pairValue, pairCount) % MOD;
        return (int) result;
    }

    /** Computes base^exp mod MOD by binary exponentiation (0^0 = 1). */
    private long modPow(long base, long exp) {
        long result = 1;
        base %= MOD;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exp /= 2;
        }
        return result;
    }
}
