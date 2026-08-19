package LeetCodeJava.Greedy;

/**
 *  1375. Number of Times Binary String Is Prefix-Aligned
 *  (formerly "Bulb Switcher III")
 *  Medium
 *
 *  You have a 1-indexed binary string of length n where all the bits are 0 initially.
 *  We will flip all the bits of this binary string (i.e. change from 0 to 1) one by one.
 *  You are given a 1-indexed integer array flips where flips[i] indicates that the bit
 *  at index flips[i] will be flipped in the i-th step.
 *
 *  A binary string is prefix-aligned if, after the i-th step, all the bits in the
 *  inclusive range [1, i] are ones and all the other bits are zeros.
 *
 *  Return the number of times the binary string is prefix-aligned during the flipping process.
 *
 *  Example 1:
 *    Input: flips = [3,2,4,1,5]
 *    Output: 2
 *    Explanation: prefix-aligned after step 4 ("1111 0") and step 5 ("11111").
 *
 *  Example 2:
 *    Input: flips = [4,1,2,3]
 *    Output: 1
 *
 *  Constraints:
 *    n == flips.length
 *    1 <= n <= 5 * 10^4
 *    flips is a permutation of the integers in the range [1, n].
 */
public class BulbSwitcherIII {

    // V0
    // IDEA: after step i we have flipped exactly i bits; the prefix [1, i] is fully on
    //       iff the largest index flipped so far equals i (no gap can exist otherwise)
    /**
     * time = O(n), space = O(1)
     */
    public int numTimesAllBlue(int[] flips) {
        if (flips == null || flips.length == 0) {
            return 0;
        }

        int res = 0;
        int maxFlipped = 0;

        for (int i = 0; i < flips.length; i++) {
            maxFlipped = java.lang.Math.max(maxFlipped, flips[i]);
            // i + 1 bits flipped so far; aligned exactly when the highest one is at i + 1
            if (maxFlipped == i + 1) {
                res++;
            }
        }

        return res;
    }
}
