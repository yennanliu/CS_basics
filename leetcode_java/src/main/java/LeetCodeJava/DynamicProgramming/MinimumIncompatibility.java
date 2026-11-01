package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-incompatibility/description/

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 1681. Minimum Incompatibility
 * Hard
 *
 * You are given an integer array nums and an integer k. You are asked to distribute this array into k
 * subsets of equal size such that there are no two equal elements in the same subset.
 *
 * A subset's incompatibility is the difference between the maximum and minimum elements in that array.
 *
 * Return the minimum possible sum of incompatibilities of the k subsets after distributing the array
 * optimally, or return -1 if it is not possible.
 *
 * A subset is a group integers that appear in the array with no particular order.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,1,4], k = 2
 * Output: 4
 * Explanation: The optimal distribution of subsets is [1,2] and [1,4].
 * The incompatibility is (2-1) + (4-1) = 4.
 * Note that [1,1] and [2,4] would result in a smaller sum, but the first subset contains 2 equal
 * elements.
 *
 * Example 2:
 *
 * Input: nums = [6,3,8,1,3,1,2,2], k = 4
 * Output: 6
 * Explanation: The optimal distribution of subsets is [1,2], [2,3], [6,8], and [1,3].
 * The incompatibility is (2-1) + (3-2) + (8-6) + (3-1) = 6.
 *
 * Example 3:
 *
 * Input: nums = [5,3,3,6,3,3], k = 3
 * Output: -1
 * Explanation: It is impossible to distribute nums into 3 subsets where no two elements are equal in
 * the same subset.
 *
 *
 * Constraints:
 *
 * 1 <= k <= nums.length <= 16
 * nums.length is divisible by k
 * 1 <= nums[i] <= nums.length
 *
 */
public class MinimumIncompatibility {

    private static final int INF = Integer.MAX_VALUE / 2;

    // V0
    // IDEA: BITMASK DP + PRECOMPUTED GROUP COSTS
    //       n <= 16 -> a mask is the set of indices already placed into groups.
    //       groupCost[g] = max - min of the group g when g has n/k indices with distinct
    //                      values, else -1 (not a valid group).
    //       dp[mask] = min total incompatibility to place exactly the indices in mask.
    //       from mask, the next group must contain the LOWEST free index (fixing it avoids
    //       counting the same partition in every group order) plus n/k - 1 other free indices.
    //       a value appearing more than k times can never be split -> -1.
    /**
     * time = O(3^N)
     * space = O(2^N)
     */
    public int minimumIncompatibility(int[] nums, int k) {
        int n = nums.length;
        int groupSize = n / k;

        // pigeonhole: some group would hold the value twice
        Map<Integer, Integer> frequency = new HashMap<>();
        for (int value : nums) {
            frequency.put(value, frequency.getOrDefault(value, 0) + 1);
            if (frequency.get(value) > k) {
                return -1;
            }
        }

        int fullMask = (1 << n) - 1;

        // groupCost[g] = incompatibility of the group of indices g, or -1 if g is not valid
        int[] groupCost = new int[1 << n];
        Arrays.fill(groupCost, -1);
        for (int group = 1; group <= fullMask; group++) {
            if (Integer.bitCount(group) != groupSize) {
                continue;
            }
            Set<Integer> values = new HashSet<>();
            int low = Integer.MAX_VALUE;
            int high = Integer.MIN_VALUE;
            boolean distinct = true;
            for (int j = 0; j < n; j++) {
                if (((group >> j) & 1) == 0) {
                    continue;
                }
                if (!values.add(nums[j])) {
                    distinct = false;
                    break;
                }
                low = Math.min(low, nums[j]);
                high = Math.max(high, nums[j]);
            }
            if (distinct) {
                groupCost[group] = high - low;
            }
        }

        int[] dp = new int[1 << n];
        Arrays.fill(dp, INF);
        dp[0] = 0;
        for (int mask = 0; mask < fullMask; mask++) {
            if (dp[mask] == INF) {
                continue;
            }

            // candidates = free indices, keeping only the first index of each value
            // (two free indices with the same value are interchangeable)
            int candidates = 0;
            Set<Integer> seenValues = new HashSet<>();
            for (int j = 0; j < n; j++) {
                boolean free = ((mask >> j) & 1) == 0;
                if (free && seenValues.add(nums[j])) {
                    candidates |= 1 << j;
                }
            }
            if (Integer.bitCount(candidates) < groupSize) {
                continue;
            }

            // the lowest set bit of candidates = the lowest free index, which is always in the group
            int lowest = candidates & (-candidates);
            int others = candidates ^ lowest;

            // enumerate every subset of `others` (including the empty one) via (sub - 1) & others
            int sub = others;
            while (true) {
                int group = sub | lowest;
                if (groupCost[group] != -1) {
                    int nextMask = mask | group;
                    dp[nextMask] = Math.min(dp[nextMask], dp[mask] + groupCost[group]);
                }
                if (sub == 0) {
                    break;
                }
                sub = (sub - 1) & others;
            }
        }
        return dp[fullMask] == INF ? -1 : dp[fullMask];
    }
}
