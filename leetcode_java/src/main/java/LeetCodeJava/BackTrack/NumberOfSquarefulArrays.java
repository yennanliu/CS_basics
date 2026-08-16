package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/number-of-squareful-arrays/description/

import java.util.Arrays;

/**
 * 996. Number of Squareful Arrays
 * Hard
 *
 * An array is squareful if the sum of every pair of adjacent elements is a perfect square.
 *
 * Given an integer array nums, return the number of permutations of nums that are squareful.
 *
 * Two permutations perm1 and perm2 are different if there is some index i such that
 * perm1[i] != perm2[i].
 *
 * Example 1:
 *
 * Input: nums = [1,17,8]
 * Output: 2
 * Explanation: [1,8,17] and [17,8,1] are the valid permutations.
 *
 * Example 2:
 *
 * Input: nums = [2,2,2]
 * Output: 1
 *
 * Constraints:
 *
 * 1 <= nums.length <= 12
 * 0 <= nums[i] <= 10^9
 *
 */
public class NumberOfSquarefulArrays {

    // V0
    // IDEA: BACKTRACKING (permutations with duplicates) + PRUNING
    /**
     *  - n <= 12, so we enumerate permutations, but prune HARD: a candidate is
     *    only appended when (last_picked + candidate) is a PERFECT SQUARE.
     *
     *  - Duplicate handling (the classic `permutations II` trick):
     *      SORT first, then skip nums[i] if nums[i] == nums[i-1] and nums[i-1]
     *      is NOT currently used. That forces equal values to be consumed in
     *      left-to-right order, so each distinct permutation is counted ONCE.
     *
     *  - NOTE !!! nums[i] can be up to 10^9, so an adjacent SUM can reach 2 * 10^9,
     *    which OVERFLOWS int -> the sum must be computed as `long`.
     *
     *  time  = O(n!) worst case, far less in practice thanks to the square pruning
     *  space = O(n)
     */

    private int res;
    private boolean[] used;
    private int[] nums;
    private int lastPicked;
    private int pathLen;

    public int numSquarefulPerms(int[] nums) {
        Arrays.sort(nums);

        this.nums = nums;
        this.used = new boolean[nums.length];
        this.res = 0;
        this.pathLen = 0;
        this.lastPicked = 0;

        backtrack();
        return res;
    }

    private void backtrack() {
        int n = nums.length;

        if (pathLen == n) {
            res += 1;
            return;
        }

        for (int i = 0; i < n; i++) {
            if (used[i]) {
                continue;
            }

            /** NOTE !!!
             *
             *  skip duplicates at the SAME recursion depth.
             *  `!used[i - 1]` is what forces equal values into left-to-right order.
             */
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
                continue;
            }

            // adjacent sum must be a perfect square (the real pruning power)
            if (pathLen > 0 && !isSquare((long) lastPicked + (long) nums[i])) {
                continue;
            }

            int prevPicked = lastPicked;

            used[i] = true;
            lastPicked = nums[i];
            pathLen += 1;

            backtrack();

            // backtrack
            pathLen -= 1;
            lastPicked = prevPicked;
            used[i] = false;
        }
    }

    /** EXACT integer square root test (no float rounding issues up to 2 * 10^9) */
    private boolean isSquare(long v) {
        if (v < 0) {
            return false;
        }
        long r = (long) Math.sqrt((double) v);
        // fix up any float drift in both directions
        while (r > 0 && r * r > v) {
            r -= 1;
        }
        while ((r + 1) * (r + 1) <= v) {
            r += 1;
        }
        return r * r == v;
    }

}
