package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-operations-to-reduce-an-integer-to-0/description/

/**
 * 2571. Minimum Operations to Reduce an Integer to 0
 * Medium
 *
 * You are given a positive integer n, you can do the following operation any number of times:
 *
 * Add or subtract a power of 2 from n.
 *
 * Return the minimum number of operations to make n equal to 0.
 *
 * A number x is power of 2 if x == 2^i where i >= 0.
 *
 *
 * Example 1:
 *
 * Input: n = 39
 * Output: 3
 * Explanation: We can do the following operations:
 * - Add 2^0 = 1 to n, so now n = 40.
 * - Subtract 2^3 = 8 from n, so now n = 32.
 * - Subtract 2^5 = 32 from n, so now n = 0.
 * It can be shown that 3 is the minimum number of operations we need to make n equal to 0.
 *
 * Example 2:
 *
 * Input: n = 54
 * Output: 3
 * Explanation: We can do the following operations:
 * - Add 2^1 = 2 to n, so now n = 56.
 * - Add 2^3 = 8 to n, so now n = 64.
 * - Subtract 2^6 = 64 from n, so now n = 0.
 * So the minimum number of operations is 3.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 *
 */
public class MinimumOperationsToReduceAnIntegerTo0 {

    // V0
    // IDEA: GREEDY ON THE LOWEST BITS (non-adjacent form)
    //       lowest bit 0         -> shift it away for free
    //       lowest two bits 11   -> add 1: carries the whole run of ones away in one op
    //       lowest two bits 01   -> subtract 1: clears the isolated one
    /**
     * time = O(log N)
     * space = O(1)
     */
    public int minOperations(int n) {
        int ops = 0;
        while (n > 0) {
            // n % 2 is the lowest bit, n % 4 the lowest two bits
            if (n % 2 == 0) {
                // trailing zero costs nothing
                n = n / 2;
            } else if (n % 4 == 3) {
                // run of >= 2 ones at the bottom -> one +1 turns it into a single higher one
                n += 1;
                ops++;
            } else {
                // isolated one at the bottom -> one -1 clears it
                n -= 1;
                ops++;
            }
        }
        return ops;
    }
}
