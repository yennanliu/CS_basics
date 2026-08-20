package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/minimum-array-end/

/**
 *  3133. Minimum Array End
 *  Medium
 *
 *  You are given two integers n and x. You have to construct an array of positive
 *  integers nums of size n where for every 0 <= i < n - 1, nums[i + 1] is greater
 *  than nums[i], and the result of the bitwise AND operation between all elements of
 *  nums is x.
 *
 *  Return the minimum possible value of nums[n - 1].
 *
 *  Example 1:
 *    Input: n = 3, x = 4
 *    Output: 6
 *    Explanation: nums can be [4,5,6], whose last element is minimal at 6.
 *
 *  Example 2:
 *    Input: n = 2, x = 7
 *    Output: 15
 *    Explanation: nums can be [7,15].
 *
 *  Constraints:
 *    1 <= n, x <= 10^8
 */
public class MinimumArrayEnd {

    // V0
    // IDEA: EVERY ELEMENT IS A SUPERSET OF x's BITS -> COUNT IN THE FREE BITS
    //
    //  the AND of all elements equals x, so every element has all of x's bits set.
    //  the elements strictly increase and the last one must be as small as possible,
    //  which means the n values should be the n SMALLEST supersets of x:
    //
    //      x itself, then x with 0, 1, 2, ... written into the free (zero) bit slots
    //
    //  so the answer is x with the binary digits of (n - 1) scattered into x's
    //  zero-bit slots, lowest slot first — the free slots behave like an ordinary
    //  binary counter.
    //
    //  NOTE: the answer easily exceeds 32 bits (n and x both up to 10^8 push the
    //        highest used slot past bit 31), so everything must be long and every
    //        mask must be `1L << bit`.
    /**
     * time = O(log n + log x)
     * space = O(1)
     */
    public long minEnd(int n, int x) {
        long res = x;
        long xl = x;
        long k = (long) n - 1;               // how far to count in the free positions
        int bit = 0;                         // position being examined in `res`
        while (k > 0) {
            if (((xl >> bit) & 1L) == 0L) {  // a free slot: take the next bit of k
                if ((k & 1L) == 1L) {
                    res |= 1L << bit;
                }
                k >>= 1;
            }
            bit++;
        }
        return res;
    }
}
