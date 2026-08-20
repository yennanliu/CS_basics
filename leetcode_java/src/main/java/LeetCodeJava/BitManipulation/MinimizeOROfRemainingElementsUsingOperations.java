package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/minimize-or-of-remaining-elements-using-operations/

/**
 *  3022. Minimize OR of Remaining Elements Using Operations
 *  Hard
 *
 *  You are given a 0-indexed integer array nums and an integer k.
 *
 *  In one operation, you can pick any index i of nums such that
 *  0 <= i < nums.length - 1 and replace nums[i] and nums[i + 1] with a single
 *  occurrence of nums[i] & nums[i + 1], where & represents the bitwise AND operator.
 *
 *  Return the minimum possible value of the bitwise OR of the remaining elements of
 *  nums after applying at most k operations.
 *
 *  Example 1:
 *    Input: nums = [3,5,3,2,7], k = 2
 *    Output: 3
 *
 *  Example 2:
 *    Input: nums = [7,3,15,14,2,8], k = 4
 *    Output: 2
 *
 *  Example 3:
 *    Input: nums = [10,7,10,3,9,14,9,4], k = 1
 *    Output: 15
 *
 *  Constraints:
 *    1 <= nums.length <= 10^5
 *    0 <= nums[i] < 2^30
 *    0 <= k < nums.length
 */
public class MinimizeOROfRemainingElementsUsingOperations {

    // V0
    // IDEA: DECIDE THE ANSWER BIT BY BIT, HIGH TO LOW, WITH A GREEDY FEASIBILITY TEST
    //
    //  clearing a high bit is worth more than every lower bit combined, so walk the
    //  bits from 29 down and greedily ask: "can this bit be 0 *while* every higher
    //  bit already proven clearable stays 0 too?"
    //
    //  `mask` carries that running requirement — it holds the bits currently required
    //  to be zero. a bit joins it on the way down, and drops back out the moment the
    //  test says it is unavoidable.
    //
    //  the operations only ever merge ADJACENT elements, and merging a run replaces it
    //  by the AND of the whole run at a cost of (run length - 1) operations. so the
    //  test is: scan left to right AND-ing (x & mask) into the current run; the moment
    //  the running value hits 0 all required bits are cleared, so close the run and
    //  start a new one. every element that did NOT close a run cost one operation.
    //
    //  that greedy is optimal — closing a run as early as possible never wastes an
    //  operation — and any elements left in an unclosed tail can be folded into the
    //  previous run for free (ANDing only clears more bits). feasible iff cost <= k.
    /**
     * time = O(30 * n)
     * space = O(1)
     */
    public int minOrAfterOperations(int[] nums, int k) {
        int res = 0;
        int mask = 0;                        // bits currently required to be 0

        for (int bit = 29; bit >= 0; bit--) {
            mask |= 1 << bit;
            int cost = 0;
            int cur = 0;
            boolean closed = true;
            for (int x : nums) {
                cur = closed ? (x & mask) : (cur & x & mask);
                if (cur != 0) {
                    cost++;                  // this element had to be merged in
                    closed = false;
                } else {
                    closed = true;           // run closed, start a fresh one
                }
            }
            if (cost > k) {
                res |= 1 << bit;             // unavoidable -> drop it from the mask
                mask ^= 1 << bit;
            }
        }
        return res;
    }
}
