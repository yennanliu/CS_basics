package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-frequency-after-subarray-operation/description/

/**
 * 3434. Maximum Frequency After Subarray Operation
 * Medium
 *
 * You are given an array nums of length n. You are also given an integer k.
 *
 * You perform the following operation on nums once:
 *
 * Select a subarray nums[i..j] where 0 <= i <= j <= n - 1.
 * Select an integer x and add x to all the elements in nums[i..j].
 *
 * Find the maximum frequency of the value k after the operation.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5,6], k = 1
 * Output: 2
 * Explanation: After adding -5 to nums[2..5], 1 has a frequency of 2 in [1, 2, -2, -1, 0, 1].
 *
 * Example 2:
 *
 * Input: nums = [10,2,3,4,5,5,4,3,2,2], k = 10
 * Output: 4
 * Explanation: After adding 8 to nums[1..9], 10 has a frequency of 4 in
 * [10, 10, 11, 12, 13, 13, 12, 11, 10, 10].
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 10^5
 * 1 <= nums[i] <= 50
 * 1 <= k <= 50
 *
 */
public class MaximumFrequencyAfterSubarrayOperation {

    private static final int MAX_VALUE = 50;

    // V0
    // IDEA: ONE KADANE PER CANDIDATE VALUE v (+1 WHERE nums[i] == v, -1 WHERE nums[i] == k)
    /**
     *  Inside the chosen subarray exactly one original value v becomes k, and the
     *  elements that were already k stop being k. So for a fixed v, score each
     *  position +1 (== v), -1 (== k), 0 otherwise; the best subarray sum (Kadane,
     *  empty allowed) is the net gain.
     *
     *  Answer: count(k) + max over v of the best gain.
     *
     *  time  = O(50 * N)
     *  space = O(1)
     */
    public int maxFrequency(int[] nums, int k) {
        int countK = 0;
        for (int num : nums) {
            if (num == k) {
                countK++;
            }
        }

        int bestGain = 0;
        for (int candidate = 1; candidate <= MAX_VALUE; candidate++) {
            if (candidate == k) {
                continue;
            }
            // Kadane: running sum clamped at 0 (an empty subarray is always allowed)
            int running = 0;
            for (int num : nums) {
                if (num == candidate) {
                    running++;
                } else if (num == k) {
                    running--;
                }
                if (running < 0) {
                    running = 0;
                }
                bestGain = Math.max(bestGain, running);
            }
        }
        return countK + bestGain;
    }

    // V1
    // IDEA: ALL 50 KADANES IN ONE PASS VIA A SHARED OFFSET
    /**
     *  A position equal to k makes EVERY candidate lose 1 -> a shared offset++.
     *  A position equal to u makes only candidate u gain 1.
     *  The true running sum of u is max(raw[u] - offset, 0); the clamp at 0 is
     *  applied lazily when u is incremented: raw[u] = max(raw[u], offset) + 1.
     *  A -1 step can never set a record, so the best is only refreshed on increments.
     *
     *  time  = O(N)
     *  space = O(50)
     */
    public int maxFrequency_1(int[] nums, int k) {
        int[] raw = new int[MAX_VALUE + 1];
        int[] best = new int[MAX_VALUE + 1];
        int offset = 0;
        int countK = 0;

        for (int num : nums) {
            if (num == k) {
                countK++;
                offset++;
            } else {
                // re-clamp candidate num at 0 (relative to offset), then add 1
                int value = Math.max(raw[num], offset) + 1;
                raw[num] = value;
                best[num] = Math.max(best[num], value - offset);
            }
        }

        int bestGain = 0;
        for (int gain : best) {
            bestGain = Math.max(bestGain, gain);
        }
        return countK + bestGain;
    }
}
