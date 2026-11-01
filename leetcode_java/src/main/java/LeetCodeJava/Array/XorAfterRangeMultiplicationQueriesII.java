package LeetCodeJava.Array;

// https://leetcode.com/problems/xor-after-range-multiplication-queries-ii/description/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 3655. XOR After Range Multiplication Queries II
 * Hard
 *
 * You are given an integer array nums of length n and a 2D integer array queries
 * of size q, where queries[i] = [li, ri, ki, vi].
 *
 * For each query, you must apply the following operations in order:
 *
 * Set idx = li.
 * While idx <= ri:
 *   Update: nums[idx] = (nums[idx] * vi) % (10^9 + 7).
 *   Set idx += ki.
 *
 * Return the bitwise XOR of all elements in nums after processing all queries.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,1,1], queries = [[0,2,1,4]]
 * Output: 4
 *
 * Example 2:
 *
 * Input: nums = [2,3,1,5,4], queries = [[1,4,2,3],[0,2,1,2]]
 * Output: 31
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 1 <= q == queries.length <= 10^5
 * queries[i] = [li, ri, ki, vi]
 * 0 <= li <= ri < n
 * 1 <= ki <= n
 * 1 <= vi <= 10^5
 *
 */
public class XorAfterRangeMultiplicationQueriesII {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: SQRT DECOMPOSITION ON THE STEP + MULTIPLICATIVE DIFFERENCE ARRAY PER SMALL STEP
    //   a query with a LARGE step (k > sqrt(n)) touches at most sqrt(n) cells: replay it.
    //   a SMALL step can touch the whole array, so batch all queries of one step k:
    //   on a factor array, post v at l and v^-1 at the first progression index past r
    //   (l + (count) * k); then a prefix PRODUCT along stride k
    //   (factor[i] *= factor[i - k]) replays every query of that step at once.
    //   v^-1 = v^(MOD-2) by Fermat, since MOD is prime.
    /**
     * time = O((N + Q) * sqrt(N) + Q * log(MOD))
     * space = O(N + Q)
     */
    public int xorAfterQueries(int[] nums, int[][] queries) {
        int n = nums.length;
        int threshold = (int) Math.sqrt(n) + 1;

        long[] values = new long[n];
        for (int i = 0; i < n; i++) {
            values[i] = nums[i];
        }

        // small-step queries grouped by their step
        Map<Integer, List<int[]>> smallStepQueries = new HashMap<>();
        for (int[] query : queries) {
            int step = query[2];
            if (step > threshold) {
                // large step: at most n / step <= sqrt(n) cells, apply directly
                for (int idx = query[0]; idx <= query[1]; idx += step) {
                    values[idx] = values[idx] * query[3] % MOD;
                }
            } else {
                smallStepQueries.computeIfAbsent(step, x -> new ArrayList<>()).add(query);
            }
        }

        long[] factor = new long[n];
        for (Map.Entry<Integer, List<int[]>> entry : smallStepQueries.entrySet()) {
            int step = entry.getKey();
            for (int i = 0; i < n; i++) {
                factor[i] = 1;
            }

            // mark where each query's multiplier starts and stops along its progression
            for (int[] query : entry.getValue()) {
                int left = query[0];
                int right = query[1];
                long multiplier = query[3];
                factor[left] = factor[left] * multiplier % MOD;

                int touched = (right - left) / step + 1;      // cells the query hits
                long pastEnd = left + (long) touched * step;  // first progression index after r
                if (pastEnd < n) {
                    int stop = (int) pastEnd;
                    factor[stop] = factor[stop] * modPow(multiplier, MOD - 2) % MOD;
                }
            }

            // prefix product along stride `step`, then apply
            for (int i = 0; i < n; i++) {
                if (i >= step) {
                    factor[i] = factor[i] * factor[i - step] % MOD;
                }
                values[i] = values[i] * factor[i] % MOD;
            }
        }

        int result = 0;
        for (long value : values) {
            result ^= (int) value;
        }
        return result;
    }

    /** base^exponent mod MOD by fast exponentiation. */
    private long modPow(long base, long exponent) {
        long result = 1;
        base %= MOD;
        while (exponent > 0) {
            // the current lowest bit of the exponent decides whether this power is used
            if (exponent % 2 == 1) {
                result = result * base % MOD;
            }
            base = base * base % MOD;
            exponent /= 2;
        }
        return result;
    }
}
