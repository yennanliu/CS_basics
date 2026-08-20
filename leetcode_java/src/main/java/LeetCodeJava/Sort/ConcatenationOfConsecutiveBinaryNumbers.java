package LeetCodeJava.Sort;

// https://leetcode.com/problems/concatenation-of-consecutive-binary-numbers/

/**
 *  1680. Concatenation of Consecutive Binary Numbers
 *  Medium
 *
 *  Given an integer n, return the decimal value of the binary string formed by
 *  concatenating the binary representations of 1 to n in order, modulo 10^9 + 7.
 *
 *  Example 1:
 *    Input: n = 3
 *    Output: 27
 *    Explanation: in binary 1, 2, 3 are "1", "10", "11". Concatenated they give
 *                 "11011", which is 27 in decimal.
 *
 *  Example 2:
 *    Input: n = 12
 *    Output: 505379714
 *    Explanation: the concatenation is 118505380540, and 118505380540 mod 1e9+7
 *                 is 505379714.
 *
 *  Constraints:
 *    1 <= n <= 10^5
 */
public class ConcatenationOfConsecutiveBinaryNumbers {

    // V0
    // IDEA: BIT MANIPULATION, INCREMENTAL SHIFT (never build the giant string)
    //       appending the binary form of i to the accumulated value is just
    //         res = res * 2^len(i) + i     where len(i) = number of bits of i
    //       so keep a running value modulo 1e9+7 and shift it left by len(i)
    //       each step. len(i) only grows when i is a power of two, so a counter
    //       tracks it without recomputing bit lengths.
    //       the modulo is applied every step - the raw number has ~10^6 bits.
    /**
     * time = O(n)
     * space = O(1)
     */
    public int concatenatedBinary(int n) {
        final long MOD = 1000000007L;
        long res = 0;
        int bits = 0;                        // number of bits of the current i
        for (int i = 1; i <= n; i++) {
            if ((i & (i - 1)) == 0) {        // i is a power of two -> one more bit
                bits++;
            }
            res = ((res << bits) | i) % MOD;
        }
        return (int) res;
    }
}
