package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-array-sum/description/

import java.util.Arrays;

/**
 * 3366. Minimum Array Sum
 * Medium
 *
 * You are given an integer array nums and three integers k, op1, and op2.
 *
 * You can perform the following operations on nums:
 *
 * Operation 1: Choose an index i and divide nums[i] by 2, rounding up to the nearest whole number.
 * You can perform this operation at most op1 times, and not more than once per index.
 * Operation 2: Choose an index i and subtract k from nums[i], but only if nums[i] is greater than
 * or equal to k. You can perform this operation at most op2 times, and not more than once per index.
 *
 * Note: Both operations can be applied to the same index, but at most once each.
 *
 * Return the minimum possible sum of all elements in nums after performing any number of operations.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,8,3,19,3], k = 3, op1 = 1, op2 = 1
 * Output: 23
 * Explanation:
 * Apply Operation 2 to nums[1] = 8, making nums[1] = 5.
 * Apply Operation 1 to nums[3] = 19, making nums[3] = 10.
 * The resulting array becomes [2, 5, 3, 10, 3], which has the minimum possible sum of 23 after
 * applying the operations.
 *
 * Example 2:
 *
 * Input: nums = [2,4,3], k = 3, op1 = 2, op2 = 1
 * Output: 3
 * Explanation:
 * Apply Operation 1 to nums[0] = 2, making nums[0] = 1.
 * Apply Operation 1 to nums[1] = 4, making nums[1] = 2.
 * Apply Operation 2 to nums[2] = 3, making nums[2] = 0.
 * The resulting array becomes [1, 2, 0], which has the minimum possible sum of 3 after applying
 * the operations.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 100
 * 0 <= nums[i] <= 10^5
 * 0 <= k <= 10^5
 * 0 <= op1, op2 <= nums.length
 *
 */
public class MinimumArraySum {

    private static final int INF = Integer.MAX_VALUE;

    // V0
    // IDEA: DP OVER (INDEX, OP1 LEFT, OP2 LEFT) — EACH ELEMENT HAS 4 FATES
    /**
     *  The two operations draw on shared budgets, so a per-element greedy fails.
     *
     *  DP def:
     *     - dp[a][b] = min sum of the elements processed so far with a op1's and b op2's left
     *  DP eq, for value v (half = ceil(v / 2)):
     *     - untouched:        dp'[a][b]       <- dp[a][b] + v
     *     - halve only:       dp'[a-1][b]     <- dp[a][b] + half
     *     - subtract only:    dp'[a][b-1]     <- dp[a][b] + v - k            (v >= k)
     *     - both, either order (they can differ):
     *                         dp'[a-1][b-1]   <- dp[a][b] + half - k         (half >= k)
     *                         dp'[a-1][b-1]   <- dp[a][b] + ceil((v - k)/2)  (v >= k)
     *  Init: dp[op1][op2] = 0
     *  Answer: min over all dp[a][b]
     *
     *  time = O(N * op1 * op2)
     *  space = O(op1 * op2)
     */
    public int minArraySum(int[] nums, int k, int op1, int op2) {
        int[][] dp = new int[op1 + 1][op2 + 1];
        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }
        dp[op1][op2] = 0;

        for (int value : nums) {
            int[][] next = new int[op1 + 1][op2 + 1];
            for (int[] row : next) {
                Arrays.fill(row, INF);
            }
            int half = (value + 1) / 2;

            for (int a = 0; a <= op1; a++) {
                for (int b = 0; b <= op2; b++) {
                    int current = dp[a][b];
                    if (current == INF) {
                        continue;
                    }
                    // leave the element alone
                    relax(next, a, b, current + value);
                    // halve only
                    if (a > 0) {
                        relax(next, a - 1, b, current + half);
                    }
                    // subtract only
                    if (b > 0 && value >= k) {
                        relax(next, a, b - 1, current + value - k);
                    }
                    // both operations, trying each order
                    if (a > 0 && b > 0) {
                        if (half >= k) {
                            relax(next, a - 1, b - 1, current + half - k);
                        }
                        if (value >= k) {
                            relax(next, a - 1, b - 1, current + (value - k + 1) / 2);
                        }
                    }
                }
            }
            dp = next;
        }

        int answer = INF;
        for (int[] row : dp) {
            for (int sum : row) {
                answer = Math.min(answer, sum);
            }
        }
        return answer;
    }

    /** Keeps the smaller of the stored sum and the candidate. */
    private void relax(int[][] table, int a, int b, int candidate) {
        if (candidate < table[a][b]) {
            table[a][b] = candidate;
        }
    }
}
