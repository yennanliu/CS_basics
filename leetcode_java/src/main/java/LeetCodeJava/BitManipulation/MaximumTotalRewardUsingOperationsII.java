package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/maximum-total-reward-using-operations-ii/

import java.util.Arrays;

/**
 *  3181. Maximum Total Reward Using Operations II
 *  Hard
 *
 *  You are given an integer array rewardValues of length n, representing the values
 *  of rewards.
 *
 *  Initially, your total reward x is 0, and all indices are unmarked. You are
 *  allowed to perform the following operation any number of times:
 *
 *    - Choose an unmarked index i from the range [0, n - 1].
 *    - If rewardValues[i] is greater than your current total reward x, then add
 *      rewardValues[i] to x (i.e. x = x + rewardValues[i]), and mark the index i.
 *
 *  Return an integer denoting the maximum total reward you can collect by
 *  performing the operations optimally.
 *
 *  Example 1:
 *    Input: rewardValues = [1,1,3,3]
 *    Output: 4
 *    Explanation: mark indices 0 and 2 in order -> total reward 4.
 *
 *  Example 2:
 *    Input: rewardValues = [1,6,4,3,2]
 *    Output: 11
 *
 *  Constraints:
 *    1 <= rewardValues.length <= 5 * 10^4
 *    1 <= rewardValues[i] <= 5 * 10^4
 */
public class MaximumTotalRewardUsingOperationsII {

    // V0
    // IDEA: REACHABILITY DP HELD AS A BITSET IN A long[]
    //
    //  LC 3180's boolean array costs O(n * max) cell writes; here that is
    //  5*10^4 * 10^5 = 5 billion, far too many.
    //
    //  keep the reachable totals as a BITSET (bit t set == total t is reachable),
    //  then one value's whole transition is two word-parallel operations:
    //
    //      low  = reach & ((1 << v) - 1)     // totals strictly below v
    //      reach |= low << v                 // each of them can take v
    //
    //  each distinct value therefore costs O(max / 64) instead of O(max).
    //  the answer is the index of the highest set bit.
    //
    //  NOTE: python's big ints do this for free; in Java the bitset must be an
    //        explicit long[] with a hand-written "shift left by v bits" (word
    //        offset v/64 plus a bit offset v%64, carrying across words).
    //  NOTE: x < v whenever v is taken, so every reachable total is < 2 * max ->
    //        2 * max + 1 bits is enough.
    //  NOTE: process values in increasing order — a smaller value can never be
    //        taken after a bigger one.
    /**
     * time = O(n * log n + n * max / 64)
     * space = O(max / 64)
     */
    public int maxTotalReward(int[] rewardValues) {
        int[] vals = rewardValues.clone();
        Arrays.sort(vals);

        int mx = vals[vals.length - 1];
        int nBits = 2 * mx + 1;
        int nw = (nBits >> 6) + 1;

        long[] reach = new long[nw];
        reach[0] = 1L;                       // only total 0 is reachable
        long[] low = new long[nw];

        int prev = -1;
        for (int v : vals) {
            if (v == prev) {
                continue;                    // duplicates change nothing
            }
            prev = v;

            int wordOff = v >> 6;
            int bitOff = v & 63;

            // low = reach & ((1 << v) - 1)  -> keep only bits [0, v)
            for (int i = 0; i < wordOff; i++) {
                low[i] = reach[i];
            }
            low[wordOff] = reach[wordOff] & ((1L << bitOff) - 1L);
            for (int i = wordOff + 1; i < nw; i++) {
                low[i] = 0L;
            }

            // reach |= low << v
            for (int i = nw - 1; i >= wordOff; i--) {
                int src = i - wordOff;
                long word = low[src] << bitOff;
                if (bitOff != 0 && src - 1 >= 0) {
                    word |= low[src - 1] >>> (64 - bitOff);
                }
                reach[i] |= word;
            }
        }

        for (int i = nw - 1; i >= 0; i--) {
            if (reach[i] != 0L) {
                return (i << 6) + (63 - Long.numberOfLeadingZeros(reach[i]));
            }
        }
        return 0;
    }
}
