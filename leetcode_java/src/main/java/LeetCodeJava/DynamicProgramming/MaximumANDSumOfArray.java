package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-and-sum-of-array/description/

import java.util.*;

/**
 * 2172. Maximum AND Sum of Array
 * Hard
 *
 * You are given an integer array nums of length n and an integer numSlots such that 2 * numSlots >= n. There are numSlots slots numbered from 1 to numSlots.
 *
 * You have to place all n integers into the slots such that each slot contains at most two numbers. The AND sum of a given placement is the sum of the bitwise AND of every number with its respective slot number.
 *
 * For example, the AND sum of placing the numbers [1, 3] into slot 1 and [4, 6] into slot 2 is equal to (1 AND 1) + (3 AND 1) + (4 AND 2) + (6 AND 2) = 1 + 1 + 0 + 2 = 4.
 *
 * Return the maximum possible AND sum of nums given numSlots slots.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5,6], numSlots = 3
 * Output: 9
 * Explanation: One possible placement is [1, 4] into slot 1, [2, 6] into slot 2, and [3, 5] into slot 3.
 * This gives the maximum AND sum of (1 AND 1) + (4 AND 1) + (2 AND 2) + (6 AND 2) + (3 AND 3) + (5 AND 3) = 1 + 0 + 2 + 2 + 3 + 1 = 9.
 *
 * Example 2:
 *
 * Input: nums = [1,3,10,4,7,1], numSlots = 9
 * Output: 24
 * Explanation: One possible placement is [1, 1] into slot 1, [3] into slot 3, [4] into slot 4, [7] into slot 7, and [10] into slot 9.
 * This gives the maximum AND sum of (1 AND 1) + (1 AND 1) + (3 AND 3) + (4 AND 4) + (7 AND 7) + (10 AND 9) = 1 + 1 + 3 + 4 + 7 + 8 = 24.
 * Note that slots 2, 5, 6, and 8 are empty which is permitted.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= numSlots <= 9
 * 1 <= n <= 2 * numSlots
 * 1 <= nums[i] <= 15
 *
 */
public class MaximumANDSumOfArray {

    // V0
    // IDEA: BITMASK DP OVER HALF-SLOTS (each slot split into two seats, dp[mask] = best AND sum)
    //  - seat p belongs to slot p / 2 + 1, so every seat holds at most one number
    //  - numbers are placed in order, so bitCount(mask) is the index of the next number
    /**
     * time = O(4^numSlots * numSlots)
     * space = O(4^numSlots)
     */
    public int maximumANDSum(int[] nums, int numSlots) {
        int n = nums.length;
        int seats = 2 * numSlots;
        int size = 1 << seats;

        // dp[mask] = best AND sum after filling exactly the seats in mask; -1 = unreachable
        int[] dp = new int[size];
        Arrays.fill(dp, -1);
        dp[0] = 0;

        int best = 0;
        for (int mask = 0; mask < size; mask++) {
            if (dp[mask] < 0) {
                continue;
            }
            // how many numbers have been placed so far = index of the next number
            int placed = Integer.bitCount(mask);
            if (placed == n) {
                best = Math.max(best, dp[mask]);
                continue;
            }
            for (int seat = 0; seat < seats; seat++) {
                // seat already taken
                if ((mask & (1 << seat)) != 0) {
                    continue;
                }
                int nextMask = mask | (1 << seat);
                int slotNumber = seat / 2 + 1;
                int value = dp[mask] + (nums[placed] & slotNumber);
                if (value > dp[nextMask]) {
                    dp[nextMask] = value;
                }
            }
        }
        return best;
    }
}
