package LeetCodeJava.Greedy;

// https://leetcode.com/problems/make-array-non-decreasing-or-non-increasing/description/
// https://leetcode.ca/2022-02-09-2263-Make-Array-Non-decreasing-or-Non-increasing/

import java.util.Collections;
import java.util.PriorityQueue;

/**
 * 2263. Make Array Non-decreasing or Non-increasing
 * Hard
 *
 * You are given a 0-indexed integer array nums. In one operation, you can:
 *
 * Choose an index i in the range 0 <= i < nums.length
 * Set nums[i] to nums[i] + 1 or nums[i] - 1
 *
 * Return the minimum number of operations to make nums non-decreasing or non-increasing.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,2,4,5,0]
 * Output: 4
 *
 * Example 2:
 *
 * Input: nums = [2,2,3,4]
 * Output: 0
 *
 * Example 3:
 *
 * Input: nums = [0]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] <= 1000
 *
 * Follow up: Can you solve it in O(n*log(n)) time complexity?
 *
 */
public class MakeArrayNonDecreasingOrNonIncreasing {

    // V0
    // IDEA: SLOPE TRICK / MAX-HEAP (L1 ISOTONIC REGRESSION); NON-INCREASING = SAME RUN ON THE REVERSED ARRAY
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int convertArray(int[] nums) {
        int n = nums.length;
        int[] reversed = new int[n];
        for (int i = 0; i < n; i++) {
            reversed[i] = nums[n - 1 - i];
        }
        return Math.min(costNonDecreasing(nums), costNonDecreasing(reversed));
    }

    /** Minimum total |change| to make arr non-decreasing (slope trick). */
    private int costNonDecreasing(int[] arr) {
        // max-heap of the "kink" points of the convex prefix cost function
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());
        int cost = 0;

        for (int value : arr) {
            maxHeap.offer(value);
            int top = maxHeap.peek();
            // the largest earlier value is above `value`: the two must meet somewhere in [value, top],
            // costing (top - value) whichever point is chosen; the top kink moves down to `value`
            if (top > value) {
                cost += top - value;
                maxHeap.poll();
                maxHeap.offer(value);
            }
        }
        return cost;
    }

    // V1
    // IDEA: DP OVER THE FINAL VALUE (0..1000) WITH A RUNNING PREFIX MINIMUM
    /**
     * time = O(N * V), V = 1001 possible values
     * space = O(V)
     */
    public int convertArray_1(int[] nums) {
        int n = nums.length;
        int[] reversed = new int[n];
        for (int i = 0; i < n; i++) {
            reversed[i] = nums[n - 1 - i];
        }
        return Math.min(dpNonDecreasing(nums), dpNonDecreasing(reversed));
    }

    /** dp[v] = min cost for the prefix so far when its last value is exactly v. */
    private int dpNonDecreasing(int[] arr) {
        final int MAX_VALUE = 1000;
        int[] dp = new int[MAX_VALUE + 1];

        for (int value : arr) {
            // recurrence: newDp[v] = min(dp[u] for u <= v) + |value - v|
            // computed in place, keeping the prefix minimum of the OLD dp as v grows
            int bestBefore = Integer.MAX_VALUE;
            for (int v = 0; v <= MAX_VALUE; v++) {
                bestBefore = Math.min(bestBefore, dp[v]);
                dp[v] = bestBefore + Math.abs(value - v);
            }
        }

        int best = Integer.MAX_VALUE;
        for (int v = 0; v <= MAX_VALUE; v++) {
            best = Math.min(best, dp[v]);
        }
        return best;
    }
}
