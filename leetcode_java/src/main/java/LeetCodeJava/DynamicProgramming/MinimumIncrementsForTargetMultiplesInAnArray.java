package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-increments-for-target-multiples-in-an-array/description/

import java.util.Arrays;

/**
 * 3444. Minimum Increments for Target Multiples in an Array
 * Hard
 *
 * You are given two arrays, nums and target.
 *
 * In a single operation, you may increment any element of nums by 1.
 *
 * Return the minimum number of operations required so that each element in target has at
 * least one multiple in nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3], target = [4]
 * Output: 1
 * Explanation: Increment 3 to 4 with just one operation, making 4 a multiple of itself.
 *
 * Example 2:
 *
 * Input: nums = [8,4], target = [10,5]
 * Output: 2
 * Explanation: Increment 8 to 10 with 2 operations, making 10 a multiple of both 5 and 10.
 *
 * Example 3:
 *
 * Input: nums = [7,9,10], target = [7]
 * Output: 0
 * Explanation: Target 7 already has a multiple in nums, so no additional operations are needed.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 5 * 10^4
 * 1 <= target.length <= 4
 * target.length <= nums.length
 * 1 <= nums[i], target[i] <= 10^4
 *
 */
public class MinimumIncrementsForTargetMultiplesInAnArray {

    private static final long INF = Long.MAX_VALUE / 2;

    // V0
    // IDEA: BITMASK DP OVER THE (AT MOST 4) TARGETS + LCM PER SUBSET
    /**
     *  One element satisfies a subset S of targets exactly when it becomes a multiple
     *  of lcm(S); the price of that is the distance up to the next multiple.
     *
     *  DP def:
     *     - dp[mask] = min increments so far to satisfy exactly the targets in mask
     *
     *  DP eq (per element v, each element covers at most one subset):
     *     - next[mask | S] = min(next[mask | S], dp[mask] + price(v, S))
     *       for every non-empty S disjoint from mask
     *     - price(v, S) = (lcm(S) - v % lcm(S)) % lcm(S)
     *
     *  NOTE !!! writing into a separate `next` array stops one element being spent twice.
     *
     *  Init: dp[0] = 0.  Answer: dp[(1 << t) - 1]
     *
     *  time  = O(N * 3^T), T <= 4
     *  space = O(2^T)
     */
    public int minimumIncrements(int[] nums, int[] target) {
        int t = target.length;
        int full = 1 << t;

        // lcm of every subset of targets (can reach ~10^16, so long)
        long[] subsetLcm = new long[full];
        subsetLcm[0] = 1;
        for (int mask = 1; mask < full; mask++) {
            // split off the lowest set bit: mask = rest + {target[bit]}
            int bit = Integer.numberOfTrailingZeros(mask);
            int rest = mask & ~(1 << bit);
            subsetLcm[mask] = lcm(subsetLcm[rest], target[bit]);
        }

        long[] dp = new long[full];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        long[] price = new long[full];
        for (int value : nums) {
            // cost to lift value to the next multiple of each subset's lcm
            for (int subset = 1; subset < full; subset++) {
                long l = subsetLcm[subset];
                price[subset] = (l - value % l) % l;
            }

            long[] next = dp.clone();
            for (int mask = 0; mask < full; mask++) {
                if (dp[mask] == INF) {
                    continue;
                }
                int uncovered = (full - 1) & ~mask;
                // enumerate every non-empty subset of the uncovered targets
                for (int subset = uncovered; subset > 0; subset = (subset - 1) & uncovered) {
                    long candidate = dp[mask] + price[subset];
                    int merged = mask | subset;
                    if (candidate < next[merged]) {
                        next[merged] = candidate;
                    }
                }
            }
            dp = next;
        }
        return (int) dp[full - 1];
    }

    /** Least common multiple of a and b. */
    private long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    /** Greatest common divisor (Euclid). */
    private long gcd(long a, long b) {
        while (b != 0) {
            long remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
