package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-xor-sum-of-two-arrays/description/

import java.util.*;

/**
 * 1879. Minimum XOR Sum of Two Arrays
 * Hard
 *
 * You are given two integer arrays nums1 and nums2 of length n.
 *
 * The XOR sum of the two integer arrays is (nums1[0] XOR nums2[0]) + (nums1[1] XOR nums2[1]) + ... +
 * (nums1[n - 1] XOR nums2[n - 1]) (0-indexed).
 *
 * For example, the XOR sum of [1,2,3] and [3,2,1] is equal to (1 XOR 3) + (2 XOR 2) + (3 XOR 1) = 2 + 0 + 2 = 4.
 *
 * Rearrange the elements of nums2 such that the resulting XOR sum is minimized.
 *
 * Return the XOR sum after the rearrangement.
 *
 *
 * Example 1:
 *
 * Input: nums1 = [1,2], nums2 = [2,3]
 * Output: 2
 * Explanation: Rearrange nums2 so that it becomes [3,2].
 * The XOR sum is (1 XOR 3) + (2 XOR 2) = 2 + 0 = 2.
 *
 * Example 2:
 *
 * Input: nums1 = [1,0,3], nums2 = [5,3,4]
 * Output: 8
 * Explanation: Rearrange nums2 so that it becomes [5,4,3].
 * The XOR sum is (1 XOR 5) + (0 XOR 4) + (3 XOR 3) = 4 + 4 + 0 = 8.
 *
 *
 * Constraints:
 *
 * n == nums1.length
 * n == nums2.length
 * 1 <= n <= 14
 * 0 <= nums1[i], nums2[i] <= 10^7
 *
 */
public class MinimumXorSumOfTwoArrays {

    // V0
    // IDEA: BITMASK DP (min-cost perfect matching on a tiny bipartite graph)
    // match every nums1[i] to a distinct nums2[k]; n <= 14, so a subset DP is enough.
    // dp[mask] = min cost after matching the first bitCount(mask) items of nums1 against
    //            exactly the nums2 indices in mask.
    // transition: i = bitCount(mask) is the next nums1 item to place; for every k not in mask
    //   dp[mask | (1 << k)] = min(dp[mask | (1 << k)], dp[mask] + (nums1[i] ^ nums2[k]))
    // base: dp[0] = 0.
    /**
     * time = O(2^N * N)
     * space = O(2^N)
     */
    public int minimumXORSum(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int full = 1 << n;

        int[] dp = new int[full];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int mask = 0; mask < full; mask++) {
            if (dp[mask] == Integer.MAX_VALUE) {
                continue;
            }
            // number of nums1 items already placed = index of the next one
            int next = Integer.bitCount(mask);
            if (next == n) {
                continue;
            }
            for (int k = 0; k < n; k++) {
                // nums2[k] is already used
                if ((mask & (1 << k)) != 0) {
                    continue;
                }
                int nextMask = mask | (1 << k);
                int cost = dp[mask] + (nums1[next] ^ nums2[k]);
                dp[nextMask] = Math.min(dp[nextMask], cost);
            }
        }
        return dp[full - 1];
    }
}
