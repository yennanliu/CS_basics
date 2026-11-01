package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-operations-to-form-subset-sum-i/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 4040. Minimum Operations to Form Subset Sum I
 * Medium
 *
 * You are given an integer array nums and an integer sum.
 *
 * In one operation you may choose an element with current value x and replace it
 * by either 2 * x or floor(x / 2).
 *
 * For each element, all of its multiplication operations must be performed before
 * any of its division operations.
 *
 * Return the minimum number of operations needed so that some subset of the
 * resulting array has a sum exactly equal to sum.
 * If it is impossible, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [5,6,10], sum = 4
 * Output: 3
 * Explanation: 5 -> 2 (one division) and 10 -> 5 -> 2 (two divisions) gives [2,6,2],
 * whose subset {2,2} sums to 4, for 3 operations in total.
 *
 * Example 2:
 *
 * Input: nums = [10,2], sum = 13
 * Output: 3
 * Explanation: 10 -> 5 (one division) and 2 -> 4 -> 8 (two multiplications) gives [5,8],
 * whose subset {5,8} sums to 13, for 3 operations in total.
 *
 *
 * Constraints:
 *
 * (not recorded in the contest notes — the DP indexes an array of size sum + 1,
 * so sum is small enough for that)
 *
 */
public class MinimumOperationsToFormSubsetSumI {

    private static final int INF = Integer.MAX_VALUE / 2;

    // V0
    // IDEA: GROUP 0/1 KNAPSACK OVER EACH ELEMENT'S REACHABLE VALUES
    //   each original x is a GROUP of (value it can become, operations it costs); at most one
    //   option per group is taken. the group is only the two PURE chains:
    //     x, 2x, 4x, ...        (k doublings, cost k)
    //     x, x/2, x/4, ...      (k halvings,  cost k)
    //   a mixed run (k doublings then j halvings) lands on x * 2^(k-j), which a pure chain
    //   reaches cheaper; divide-then-multiply could reach something new, but is forbidden.
    //   dp[s] = min operations so that some subset of the processed elements sums to s.
    //   dp is read and newDp written, so every option of one x competes against the state
    //   BEFORE x — that keeps each element used at most once.
    /**
     * time = O(N * sum * log(max(x, sum)))
     * space = O(sum)
     */
    public int minOperations(int[] nums, int sum) {
        // dp[s] = min operations to make some subset sum to s
        int[] dp = new int[sum + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int x : nums) {
            // a zero (or negative) value can never help reach a positive sum
            if (x <= 0) {
                continue;
            }

            List<int[]> options = reachableValues(x, sum);

            // 0/1 over the group: start from a copy so that options of the same x
            // never build on each other
            int[] newDp = Arrays.copyOf(dp, sum + 1);
            for (int[] option : options) {
                int value = option[0];
                int cost = option[1];
                for (int s = value; s <= sum; s++) {
                    if (dp[s - value] != INF) {
                        newDp[s] = Math.min(newDp[s], dp[s - value] + cost);
                    }
                }
            }
            dp = newDp;
        }

        return dp[sum] == INF ? -1 : dp[sum];
    }

    /** Every (value, cost) pair x can become that is in [1, sum]: x itself, its doublings and its halvings. */
    private List<int[]> reachableValues(int x, int sum) {
        List<int[]> options = new ArrayList<>();

        // keep x as it is, then x -> 2x -> 4x -> ... (stop once past sum, it only grows).
        // long: doubling a value near sum must not overflow before the check
        long value = x;
        int ops = 0;
        while (value <= sum) {
            options.add(new int[] {(int) value, ops});
            value *= 2;
            ops++;
        }

        // x -> x/2 -> x/4 -> ... (stop at 0, it can never help a sum)
        int halved = x;
        ops = 0;
        while (halved > 1) {
            halved /= 2;
            ops++;
            if (halved <= sum) {
                options.add(new int[] {halved, ops});
            }
        }

        return options;
    }
}
