package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-strength-of-k-disjoint-subarrays/description/

/**
 * 3077. Maximum Strength of K Disjoint Subarrays
 * Hard
 *
 * You are given a 0-indexed array of integers nums of length n, and a positive odd integer k.
 *
 * The strength of x subarrays is defined as strength = sum[1] * x - sum[2] * (x - 1) + sum[3] * (x - 2) - sum[4] * (x - 3) + ... + sum[x] * 1 where sum[i] is the sum of the elements in the ith subarray. Formally, strength is sum of (-1)^(i+1) * sum[i] * (x - i + 1) over all i's such that 1 <= i <= x.
 *
 * You need to select k disjoint subarrays from nums, such that their strength is maximum.
 *
 * Return the maximum possible strength that can be obtained.
 *
 * Note that the selected subarrays don't need to cover the entire array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,-1,2], k = 3
 * Output: 22
 * Explanation: The best possible way to select 3 subarrays is: nums[0..2], nums[3..3], and nums[4..4]. The strength is (1 + 2 + 3) * 3 - (-1) * 2 + 2 * 1 = 22.
 *
 * Example 2:
 *
 * Input: nums = [12,-2,-2,-2,-2], k = 5
 * Output: 64
 * Explanation: The only possible way to select 5 disjoint subarrays is: nums[0..0], nums[1..1], nums[2..2], nums[3..3], and nums[4..4]. The strength is 12 * 5 - (-2) * 4 + (-2) * 3 - (-2) * 2 + (-2) * 1 = 64.
 *
 * Example 3:
 *
 * Input: nums = [-1,-2,-3], k = 1
 * Output: -1
 * Explanation: The best possible way to select 1 subarray is: nums[0..0]. The strength is -1.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^4
 * -10^9 <= nums[i] <= 10^9
 * 1 <= k <= n
 * 1 <= n * k <= 10^6
 * k is odd.
 *
 */
public class MaximumStrengthOfKDisjointSubarrays {

    // V0
    // IDEA: DP OVER (SUBARRAYS USED, PREFIX LENGTH) WITH AN "OPEN" AND A "DONE" TABLE
    //   The j-th chosen subarray has a fixed coefficient (k - j + 1), positive for odd j and
    //   negative for even j, so every element inside it is worth coef * nums[i].
    //   open[i] = best total where the j-th subarray is still open and includes nums[i-1]
    //   done[i] = best total where j subarrays are finished within the first i elements
    //   open[i] = max(open[i-1], donePrev[i-1]) + coef * nums[i-1]   (extend, or start here)
    //   done[i] = max(done[i-1], open[i])
    //   donePrev is the (j-1) table, so the new subarray starts after the previous one ended.
    /**
     * time = O(N * K)
     * space = O(N)
     */
    public long maximumStrength(int[] nums, int k) {
        int n = nums.length;
        // marks an impossible state (e.g. fewer than j elements seen for j subarrays)
        final long IMPOSSIBLE = Long.MIN_VALUE / 4;

        // j = 0: nothing chosen, total 0 for every prefix
        long[] donePrev = new long[n + 1];

        for (int j = 1; j <= k; j++) {
            long coef = (long) (k - j + 1) * (j % 2 == 1 ? 1 : -1);
            long[] open = new long[n + 1];
            long[] done = new long[n + 1];
            open[0] = IMPOSSIBLE;
            done[0] = IMPOSSIBLE;
            for (int i = 1; i <= n; i++) {
                // extend the open subarray, or start the j-th one at nums[i-1]
                long base = Math.max(open[i - 1], donePrev[i - 1]);
                if (base == IMPOSSIBLE) {
                    open[i] = IMPOSSIBLE;
                } else {
                    open[i] = base + coef * nums[i - 1];
                }
                done[i] = Math.max(done[i - 1], open[i]);
            }
            donePrev = done;
        }
        return donePrev[n];
    }
}
