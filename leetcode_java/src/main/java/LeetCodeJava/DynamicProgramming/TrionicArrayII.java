package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/trionic-array-ii/description/

import java.util.*;

/**
 * 3640. Trionic Array II
 * Hard
 *
 * You are given an integer array nums of length n.
 *
 * A trionic subarray is a contiguous subarray nums[l...r] (with 0 <= l < r <
 * n) for which there exist indices l < p < q < r such that:
 *
 * nums[l...p] is strictly increasing,
 *
 * nums[p...q] is strictly decreasing,
 *
 * nums[q...r] is strictly increasing.
 *
 * Return the maximum sum of any trionic subarray in nums.
 *
 * Example 1:
 *
 * Input: nums = [0,-2,-1,-3,0,2,-1]
 * Output: -4
 * Explanation:
 * Pick l = 1, p = 2, q = 3, r = 5:
 * nums[l...p] = nums[1...2] = [-2, -1] is strictly increasing (-2 < -1).
 * nums[p...q] = nums[2...3] = [-1, -3] is strictly decreasing (-1 > -3)
 * nums[q...r] = nums[3...5] = [-3, 0, 2] is strictly increasing (-3 < 0 < 2).
 * Sum = (-2) + (-1) + (-3) + 0 + 2 = -4.
 *
 * Example 2:
 *
 * Input: nums = [1,4,2,7]
 * Output: 14
 * Explanation:
 * Pick l = 0, p = 1, q = 2, r = 3:
 * nums[l...p] = nums[0...1] = [1, 4] is strictly increasing (1 < 4).
 * nums[p...q] = nums[1...2] = [4, 2] is strictly decreasing (4 > 2).
 * nums[q...r] = nums[2...3] = [2, 7] is strictly increasing (2 < 7).
 * Sum = 1 + 4 + 2 + 7 = 14.
 *
 * Constraints:
 *
 * 4 <= n = nums.length <= 10^5
 * -10^9 <= nums[i] <= 10^9
 * It is guaranteed that at least one trionic subarray exists.
 *
 */
public class TrionicArrayII {

    // marks a phase that cannot end at the current index
    private static final long NONE = Long.MIN_VALUE;

    // V0
    // IDEA: LINEAR DP OVER THE THREE PHASES (UP -> DOWN -> UP), KADANE-STYLE
    //   for every index i keep the best sum of a partial trionic subarray ENDING at i:
    //     rising   : strictly increasing run ending at i (>= 1 element); may drop a
    //                negative prefix, like Kadane - the only place a restart is allowed
    //     firstUp  : phase 1 complete (>= 2 elements, increasing)
    //     down     : phase 2, at least one step down after firstUp
    //     secondUp : phase 3, at least one step up after the dip -> a full trionic subarray
    //   nums[i] > nums[i-1]: rising = nums[i] + max(rising, 0), firstUp = rising + nums[i],
    //                        secondUp = max(down, secondUp) + nums[i], down = NONE
    //   nums[i] < nums[i-1]: down = max(firstUp, down) + nums[i], the others reset
    //   equal neighbours break every strict run.
    /**
     * time = O(N)
     * space = O(1)
     */
    public long maxSumTrionic(int[] nums) {
        int n = nums.length;
        long rising = nums[0];
        long firstUp = NONE;
        long down = NONE;
        long secondUp = NONE;
        long best = NONE;

        for (int i = 1; i < n; i++) {
            long value = nums[i];
            long prevRising = rising;
            long prevFirstUp = firstUp;
            long prevDown = down;
            long prevSecondUp = secondUp;

            if (nums[i] > nums[i - 1]) {
                // keep the increasing prefix only while it helps
                rising = value + Math.max(prevRising, 0);
                firstUp = prevRising + value;
                down = NONE;
                long bestBefore = Math.max(prevDown, prevSecondUp);
                secondUp = (bestBefore == NONE) ? NONE : bestBefore + value;
            } else if (nums[i] < nums[i - 1]) {
                rising = value;
                firstUp = NONE;
                long bestBefore = Math.max(prevFirstUp, prevDown);
                down = (bestBefore == NONE) ? NONE : bestBefore + value;
                secondUp = NONE;
            } else {
                // equal values: no strict run survives
                rising = value;
                firstUp = NONE;
                down = NONE;
                secondUp = NONE;
            }
            best = Math.max(best, secondUp);
        }
        return best;
    }
}
