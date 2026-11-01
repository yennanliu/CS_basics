package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-alternating-subarray-sum-with-one-deletion/description/

/**
 * 4072. Maximum Alternating Subarray Sum With One Deletion
 * Medium
 *
 * You are given an integer array nums.
 *
 * You may delete at most one element from nums, then choose a subarray of the
 * resulting array.
 *
 * Return the maximum possible alternating sum of the chosen subarray.
 *
 * The alternating sum of an array is the sum of its elements at even indices
 * minus the sum of its elements at odd indices. The chosen subarray is
 * reindexed starting from 0 before calculating its alternating sum.
 *
 *
 * Example 1:
 *
 * Input: nums = [5,-5,1]
 * Output: 11
 * Explanation: Choose not to delete an element and select the entire array. Its alternating
 * sum is 5 - (-5) + 1 = 11, which is the maximum possible.
 *
 * Example 2:
 *
 * Input: nums = [10,-5,-100]
 * Output: 110
 * Explanation: Delete nums[1] = -5 to obtain [10,-100], then select the entire resulting
 * array. Its alternating sum is 10 - (-100) = 110, which is the maximum possible.
 *
 * Example 3:
 *
 * Input: nums = [4,7]
 * Output: 7
 * Explanation: Choose not to delete an element and select the subarray [7]. Its alternating
 * sum is 7, which is the maximum possible.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^5 <= nums[i] <= 10^5
 *
 */
public class MaximumAlternatingSubarraySumWithOneDeletion {

    /** Marks an impossible state; half of MIN so adding a value never wraps around. */
    private static final long NEG_INF = Long.MIN_VALUE / 2;

    // V0
    // IDEA: DP (KADANE WITH 4 STATES: sign of the last element x deletion used)
    //   only a deletion INSIDE the chosen subarray matters (deleting an end = a shorter
    //   subarray); it flips the sign of everything after it.
    //   every state = best alternating sum of a subarray ENDING at i:
    //     plus0  : nums[i] gets +, no deletion     plus0  = nums[i] + max(0, minus0[i-1])
    //     minus0 : nums[i] gets -, no deletion     minus0 = -nums[i] + plus0[i-1]
    //     plus1  : nums[i] gets +, one deletion    plus1  = nums[i] + max(minus1[i-1], minus0[i-2])
    //     minus1 : nums[i] gets -, one deletion    minus1 = -nums[i] + max(plus1[i-1], plus0[i-2])
    //   the [i-2] terms are "delete nums[i-1]": the previous KEPT element is nums[i-2].
    //   (brute force — delete each element, rescan — is O(N^2) and TLEs at 10^5)
    /**
     * time = O(N)
     * space = O(1)
     */
    public long maxAlternatingSum(int[] nums) {
        // edge case: the subarray must be non-empty
        if (nums == null || nums.length == 0) {
            return 0;
        }

        // states ending at i-1 ("prev") and at i-2 ("prev2", no-deletion states only).
        // long: the sum can reach 10^5 * 10^5 = 10^10
        long prevPlus0 = NEG_INF;
        long prevMinus0 = NEG_INF;
        long prevPlus1 = NEG_INF;
        long prevMinus1 = NEG_INF;
        long prev2Plus0 = NEG_INF;
        long prev2Minus0 = NEG_INF;

        long best = NEG_INF;

        for (int val : nums) {
            // no deletion: plain alternating Kadane — a + may start a new subarray,
            // a - can never start one (the first element is always +)
            long plus0 = val + Math.max(0, prevMinus0);
            long minus0 = -val + prevPlus0;

            // one deletion: either it is already behind us (state 1 at i-1),
            // or it is nums[i-1] itself (state 0 at i-2)
            long plus1 = val + Math.max(prevMinus1, prev2Minus0);
            long minus1 = -val + Math.max(prevPlus1, prev2Plus0);

            best = Math.max(best, Math.max(Math.max(plus0, minus0), Math.max(plus1, minus1)));

            // shift the window: i-1 becomes i-2, i becomes i-1
            prev2Plus0 = prevPlus0;
            prev2Minus0 = prevMinus0;
            prevPlus0 = plus0;
            prevMinus0 = minus0;
            prevPlus1 = plus1;
            prevMinus1 = minus1;
        }

        return best;
    }

    // V1
    // IDEA: LEFT / RIGHT DP + TRY DELETING EACH ELEMENT (the LC 1186 shape)
    //   for each deleted index j, glue the best piece ENDING at j-1 to the best piece
    //   STARTING at j+1 — the signs must alternate across the gap.
    //     leftPlus[i] / leftMinus[i]   : best subarray ending at i, nums[i] gets + / -
    //                                    (a left piece is the subarray's start, so it begins with +)
    //     rightPlus[i] / rightMinus[i] : best piece starting at i, nums[i] gets + / -
    //                                    (a right piece is a tail, so either sign may come first)
    //     delete j -> leftPlus[j-1] + rightMinus[j+1], or leftMinus[j-1] + rightPlus[j+1]
    //   two passes and O(N) space — V0 is the one-pass version; this is the standard
    //   "one deletion" decomposition and worth knowing.
    /**
     * time = O(N)
     * space = O(N)
     */
    public long maxAlternatingSum_1(int[] nums) {
        // edge case: the subarray must be non-empty
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;

        // left pass: best subarray ENDING at i (it starts with a +)
        long[] leftPlus = new long[n];
        long[] leftMinus = new long[n];
        leftPlus[0] = nums[0];
        leftMinus[0] = NEG_INF;
        for (int i = 1; i < n; i++) {
            leftPlus[i] = nums[i] + Math.max(0, leftMinus[i - 1]);
            leftMinus[i] = -nums[i] + leftPlus[i - 1];
        }

        // right pass: best piece STARTING at i (either sign may come first)
        long[] rightPlus = new long[n];
        long[] rightMinus = new long[n];
        rightPlus[n - 1] = nums[n - 1];
        rightMinus[n - 1] = -nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightPlus[i] = nums[i] + Math.max(0, rightMinus[i + 1]);
            rightMinus[i] = -nums[i] + Math.max(0, rightPlus[i + 1]);
        }

        // no deletion: the best subarray ending anywhere
        long best = NEG_INF;
        for (int i = 0; i < n; i++) {
            best = Math.max(best, Math.max(leftPlus[i], leftMinus[i]));
        }

        // delete nums[j] from the middle of the subarray
        for (int j = 1; j < n - 1; j++) {
            // the sign flips across the gap: + then -, or - then +
            long gluePlus = leftPlus[j - 1] + rightMinus[j + 1];
            long glueMinus = leftMinus[j - 1] + rightPlus[j + 1];
            best = Math.max(best, Math.max(gluePlus, glueMinus));
        }

        return best;
    }
}
