package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/number-of-bit-changes-to-make-two-integers-equal/

/**
 *  3226. Number of Bit Changes to Make Two Integers Equal
 *  Easy
 *
 *  You are given two positive integers n and k.
 *
 *  You can choose any bit in the binary representation of n that is equal to 1 and
 *  change it to 0.
 *
 *  Return the number of changes needed to make n equal to k. If it is impossible,
 *  return -1.
 *
 *  Example 1:
 *    Input: n = 13, k = 4
 *    Output: 2
 *    Explanation: n = (1101)2, k = (0100)2; clear the first and fourth bits of n.
 *
 *  Example 2:
 *    Input: n = 21, k = 21
 *    Output: 0
 *
 *  Example 3:
 *    Input: n = 14, k = 13
 *    Output: -1
 *
 *  Constraints:
 *    1 <= n, k <= 10^6
 */
public class NumberOfBitChangesToMakeTwoIntegersEqual {

    // V0
    // IDEA: ONLY 1 -> 0 IS ALLOWED, SO k's BITS MUST BE A SUBSET OF n's
    //
    //  bits can be cleared but never set, so k is reachable only when every bit of k
    //  is already present in n — i.e. (n & k) == k. otherwise return -1.
    //
    //  when reachable, the bits to clear are exactly those set in n and not in k,
    //  which is (n ^ k), so the answer is its popcount.
    /**
     * time = O(1)
     * space = O(1)
     */
    public int minChanges(int n, int k) {
        if ((n & k) != k) {
            return -1;
        }
        return Integer.bitCount(n ^ k);
    }
}
