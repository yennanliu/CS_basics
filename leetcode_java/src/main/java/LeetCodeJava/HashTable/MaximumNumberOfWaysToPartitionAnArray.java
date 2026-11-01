package LeetCodeJava.HashTable;

// https://leetcode.com/problems/maximum-number-of-ways-to-partition-an-array/description/

import java.util.*;

/**
 * 2025. Maximum Number of Ways to Partition an Array
 * Hard
 *
 * You are given a 0-indexed integer array nums of length n. The number of ways to partition
 * nums is the number of pivot indices that satisfy both conditions:
 *
 * 1 <= pivot < n
 * nums[0] + nums[1] + ... + nums[pivot - 1] == nums[pivot] + nums[pivot + 1] + ... + nums[n - 1]
 *
 * You are also given an integer k. You can choose to change the value of one element of nums
 * to k, or to leave the array unchanged.
 *
 * Return the maximum possible number of ways to partition nums to satisfy both conditions
 * after changing at most one element.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,-1,2], k = 3
 * Output: 1
 * Explanation: One optimal approach is to change nums[0] to k. The array becomes [3,-1,2].
 * There is one way to partition the array:
 * - For pivot = 2, we have the partition [3,-1 | 2]: 3 + -1 == 2.
 *
 * Example 2:
 *
 * Input: nums = [0,0,0], k = 1
 * Output: 2
 * Explanation: The optimal approach is to leave the array unchanged.
 * There are two ways to partition the array:
 * - For pivot = 1, we have the partition [0 | 0,0]: 0 == 0 + 0.
 * - For pivot = 2, we have the partition [0,0 | 0]: 0 + 0 == 0.
 *
 * Example 3:
 *
 * Input: nums = [22,4,-25,-20,-15,15,-16,7,19,-10,0,-13,-14], k = -33
 * Output: 4
 * Explanation: One optimal approach is to change nums[2] to k. The array becomes
 * [22,4,-33,-20,-15,15,-16,7,19,-10,0,-13,-14].
 * There are four ways to partition the array.
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 2 <= n <= 10^5
 * -10^5 <= k, nums[i] <= 10^5
 *
 */
public class MaximumNumberOfWaysToPartitionAnArray {

    // V0
    // IDEA: PREFIX SUMS + TWO COUNTERS OF THE "DIFFERENCE" 2 * prefix - total
    //
    //   pivot p (1 <= p < n) is balanced iff  diff(p) = 2 * prefix[p] - total == 0
    //
    //   changing nums[i] to k shifts total by delta = k - nums[i], and shifts
    //   prefix[p] by delta only for p > i. so after the change:
    //       p <= i :  diff'(p) = diff(p) - delta
    //       p >  i :  diff'(p) = diff(p) + delta
    //
    //   -> count for "change index i" = #{p <= i : diff(p) == delta}
    //                                 + #{p >  i : diff(p) == -delta}
    //
    //   sweep i from 0 to n-1, moving pivots from a `right` counter into a `left`
    //   counter, so both lookups are O(1). (re-counting every pivot for every i is
    //   O(n^2) and too slow at 10^5.)
    //
    //   "leave unchanged" is just the plain count of diff(p) == 0.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int waysToPartition(int[] nums, int k) {
        // edge case: a pivot needs at least 2 elements
        if (nums == null || nums.length < 2) {
            return 0;
        }

        int n = nums.length;

        // prefix[p] = nums[0] + ... + nums[p - 1]; long, since |sum| reaches 10^10
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefix[i + 1] = prefix[i] + nums[i];
        }
        long total = prefix[n];

        // right: diff -> how many pivots p > i have that diff
        // left : diff -> how many pivots p <= i have that diff
        Map<Long, Integer> right = new HashMap<>();
        Map<Long, Integer> left = new HashMap<>();
        for (int pivot = 1; pivot < n; pivot++) {
            long diff = 2 * prefix[pivot] - total;
            right.put(diff, right.getOrDefault(diff, 0) + 1);
        }

        // option 1: leave the array unchanged
        int best = right.getOrDefault(0L, 0);

        // option 2: change nums[i] to k, for every i
        for (int i = 0; i < n; i++) {
            long delta = (long) k - nums[i];

            // NOTE !!! left side needs diff == delta, right side needs diff == -delta
            int ways = left.getOrDefault(delta, 0) + right.getOrDefault(-delta, 0);
            best = Math.max(best, ways);

            // pivot p = i + 1 moves from the right side to the left side
            if (i + 1 < n) {
                long diff = 2 * prefix[i + 1] - total;
                right.put(diff, right.get(diff) - 1);
                left.put(diff, left.getOrDefault(diff, 0) + 1);
            }
        }

        return best;
    }
}
