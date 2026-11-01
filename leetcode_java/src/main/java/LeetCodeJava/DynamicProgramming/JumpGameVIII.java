package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/jump-game-viii/description/

import java.util.*;

/**
 * 2297. Jump Game VIII
 * Medium
 *
 * (premium / locked problem)
 *
 * You are given a 0-indexed integer array nums of length n. You are initially standing at index 0. You can jump from index i to index j where i < j if:
 *
 * nums[i] <= nums[j] and nums[k] < nums[i] for all indexes k in the range (i, j), or
 * nums[i] > nums[j] and nums[k] >= nums[i] for all indexes k in the range (i, j).
 *
 * You are also given an integer array costs of length n where costs[i] denotes the cost of jumping to index i.
 *
 * Return the minimum cost to jump to the index n - 1.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,2,4,4,1], costs = [3,7,6,4,2]
 * Output: 8
 * Explanation: You start at index 0.
 * - Jump to index 2 with a cost of costs[2] = 6.
 * - Jump to index 4 with a cost of costs[4] = 2.
 * The total cost is 8.
 * It can be proven that 8 is the minimum cost needed.
 * Two other possible paths are from index 0 -> 1 -> 4 and index 0 -> 2 -> 3 -> 4.
 * These have costs of 9 and 12, respectively.
 *
 * Example 2:
 *
 * Input: nums = [0,1,2], costs = [1,1,1]
 * Output: 2
 * Explanation: Start at index 0.
 * - Jump to index 1 with a cost of costs[1] = 1.
 * - Jump to index 2 with a cost of costs[2] = 1.
 * The total cost is 2. Note that you cannot jump directly from index 0 to index 2 because nums[1] <= nums[0].
 *
 *
 * Constraints:
 *
 * n == nums.length == costs.length
 * 1 <= n <= 10^5
 * 0 <= nums[i], costs[i] <= 10^5
 *
 */
public class JumpGameVIII {

    // V0
    // IDEA: MONOTONIC STACK FOR THE TWO "NEXT JUMP" TARGETS + FORWARD DP
    /**
     * time = O(N)
     * space = O(N)
     */
    public long minCost(int[] nums, int[] costs) {
        int n = nums.length;

        // from i, the only useful jumps are to the nearest j > i with
        //   nums[j] >= nums[i] (all between are smaller), or
        //   nums[j] <  nums[i] (all between are >= nums[i])
        int[] nextGreaterOrEqual = new int[n];
        int[] nextSmaller = new int[n];
        Arrays.fill(nextGreaterOrEqual, -1);
        Arrays.fill(nextSmaller, -1);

        // decreasing stack: popped when a value >= it arrives
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] <= nums[i]) {
                nextGreaterOrEqual[stack.pop()] = i;
            }
            stack.push(i);
        }

        // non-decreasing stack: popped when a strictly smaller value arrives
        stack.clear();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] > nums[i]) {
                nextSmaller[stack.pop()] = i;
            }
            stack.push(i);
        }

        // dp[i] = min cost to reach index i
        long[] dp = new long[n];
        Arrays.fill(dp, Long.MAX_VALUE);
        dp[0] = 0;

        // jumps only go forward, so dp[i] is final when we reach i
        for (int i = 0; i < n; i++) {
            if (dp[i] == Long.MAX_VALUE) {
                continue;
            }
            int[] targets = {nextGreaterOrEqual[i], nextSmaller[i]};
            for (int j : targets) {
                if (j != -1) {
                    dp[j] = Math.min(dp[j], dp[i] + costs[j]);
                }
            }
        }
        return dp[n - 1];
    }
}
