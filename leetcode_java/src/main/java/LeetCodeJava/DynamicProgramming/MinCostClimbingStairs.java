package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/min-cost-climbing-stairs/
// https://leetcode.ca/all/746.html

import java.util.Arrays;

/**
 * 746. Min Cost Climbing Stairs
 * Easy
 * Topics
 * Companies
 * Hint
 * You are given an integer array cost where cost[i] is the cost of ith step on a staircase. Once you pay the cost, you can either climb one or two steps.
 *
 * You can either start from the step with index 0, or the step with index 1.
 *
 * Return the minimum cost to reach the top of the floor.
 *
 *
 *
 * Example 1:
 *
 * Input: cost = [10,15,20]
 * Output: 15
 * Explanation: You will start at index 1.
 * - Pay 15 and climb two steps to reach the top.
 * The total cost is 15.
 * Example 2:
 *
 * Input: cost = [1,100,1,1,1,100,1,1,100,1]
 * Output: 6
 * Explanation: You will start at index 0.
 * - Pay 1 and climb two steps to reach index 2.
 * - Pay 1 and climb two steps to reach index 4.
 * - Pay 1 and climb two steps to reach index 6.
 * - Pay 1 and climb one step to reach index 7.
 * - Pay 1 and climb two steps to reach index 9.
 * - Pay 1 and climb one step to reach the top.
 * The total cost is 6.
 *
 *
 * Constraints:
 *
 * 2 <= cost.length <= 1000
 * 0 <= cost[i] <= 999
 *
 */
public class MinCostClimbingStairs {

    // V0
    // IDEA: 1D DP (fixed by gemini)
    /**
     *  Metric,Complexity,Explanation
     * Time,O(N),We iterate through the cost array exactly once.
     * Space,O(N),We use a DP array of size N+1.
     *
     */
    /** NOTE !!!
     *
     *  The "Top" Definition:
     *
     *  The `top` of the stairs is actually `index n `
     *  (one step past the last element of the cost array).
     *
     *   -> To reach step i, you must have come from either i-1 or i-2.
     */
    public int minCostClimbingStairs(int[] cost) {
        int n = cost.length;

        // dp[i] is the minimum cost to reach step i
        // The array size is n + 1 because the "top" is one step past the last house
        int[] dp = new int[n + 1];

        /** NOTE !!!
         *
         * Base Cases: Since you can start at step 0 or step 1,
         * the cost to reach them is ` 0.`
         */
        // Base cases: You can start at index 0 or index 1 for free
        dp[0] = 0; // start before step 0
        dp[1] = 0; // or start before step 1

        for (int i = 2; i <= n; i++) {
            // To reach step i, you could:
            // 1. Come from i-1 (pay cost[i-1])
            // 2. Come from i-2 (pay cost[i-2])
            dp[i] = Math.min(dp[i - 1] + cost[i - 1],
                    dp[i - 2] + cost[i - 2]);
        }

        return dp[n];
    }

    // V0-0-1
    // IDEA: 1D DP + O(1) space complexity (fixed by gpt)
    /**
     * Metric,Complexity,Explanation
     * Time,O(N),A single pass through the cost array.
     * Space,O(1),Only two integers are used regardless of the input size.
     */
    public int minCostClimbingStairs_0_0_1(int[] cost) {
        int prev2 = 0; // dp[i-2]
        int prev1 = 0; // dp[i-1]

        for (int i = 2; i <= cost.length; i++) {
            int curr = Math.min(
                    prev1 + cost[i - 1],
                    prev2 + cost[i - 2]);
            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }



    // V0-1
    // IDEA: DP (fixed by gemini)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minCostClimbingStairs_0_1(int[] cost) {
        int n = cost.length;
        /**  NOTE !!!
         *
         *  DP definition:
         *
         *   -> dp[i] is the minimum cost to reach step i
         */
        // dp[i] is the minimum cost to reach step i
        int[] dp = new int[n + 1];

        // Base cases: You can start at index 0 or index 1 for free
        dp[0] = 0;
        dp[1] = 0;

        for (int i = 2; i <= n; i++) {
            /**  NOTE !!!
             *
             *  DP equation:
             *
             *    op1 = dp[i-2] + cost[i-2]
             *    op2 = dp[i-1] + cost[i-1]
             *
             *    dp[i] = min(op1, op2)
             *
             */
            // To reach step i, you either come from i-1 or i-2
            // You must pay the cost of the step you are jumping FROM
            int option1 = dp[i - 1] + cost[i - 1];
            int option2 = dp[i - 2] + cost[i - 2];
            dp[i] = Math.min(option1, option2);
        }

        return dp[n];
    }

    // V0-2
    // IDEA: DP (gpt)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minCostClimbingStairs_0_2(int[] cost) {
        if (cost == null || cost.length == 0) {
            return 0;
        }

        int n = cost.length;
        /**  NOTE !!!
         *
         *  DP definition:
         *
         *   -> dp[i] is the minimum cost to reach step i
         */
        int[] dp = new int[n + 1];

        // Base cases:
        // Cost to reach step 0 or 1 is 0 (you start before paying any cost)
        dp[0] = 0;
        dp[1] = 0;

        for (int i = 2; i <= n; i++) {
            /**  NOTE !!!
             *
             *  DP equation:
             *
             *    op1 = dp[i-2] + cost[i-2]
             *    op2 = dp[i-1] + cost[i-1]
             *
             *    dp[i] = min(op1, op2)
             *
             */
            dp[i] = Math.min(
                    dp[i - 1] + cost[i - 1],
                    dp[i - 2] + cost[i - 2]);
        }

        return dp[n];
    }



    // V1-1
    // https://neetcode.io/problems/min-cost-climbing-stairs
    // IDEA: Recursion
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minCostClimbingStairs_1_1(int[] cost) {

        return Math.min(dfs(cost, 0), dfs(cost, 1));
    }

    private int dfs(int[] cost, int i) {
        if (i >= cost.length) {
            return 0;
        }
        return cost[i] + Math.min(dfs(cost, i + 1),
                dfs(cost, i + 2));
    }

    // V1-2
    // https://neetcode.io/problems/min-cost-climbing-stairs
    // IDEA: Dynamic Programming (Top-Down)
    int[] memo;

    /**
     * time = O(N)
     * space = O(N)
     */
    public int minCostClimbingStairs_1_2(int[] cost) {
        memo = new int[cost.length];
        Arrays.fill(memo, -1);
        return Math.min(dfs_1_2(cost, 0), dfs_1_2(cost, 1));
    }

    private int dfs_1_2(int[] cost, int i) {
        if (i >= cost.length) {
            return 0;
        }
        if (memo[i] != -1) {
            return memo[i];
        }
        memo[i] = cost[i] + Math.min(dfs_1_2(cost, i + 1),
                dfs_1_2(cost, i + 2));
        return memo[i];
    }

    // V1-3
    // https://neetcode.io/problems/min-cost-climbing-stairs
    // IDEA: Dynamic Programming (Bottom-Up)
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minCostClimbingStairs_1_3(int[] cost) {
        int n = cost.length;
        int[] dp = new int[n + 1];

        for (int i = 2; i <= n; i++) {
            dp[i] = Math.min(dp[i - 1] + cost[i - 1],
                    dp[i - 2] + cost[i - 2]);
        }

        return dp[n];
    }


    // V2
    // https://leetcode.ca/2017-12-15-746-Min-Cost-Climbing-Stairs/
    // IDEA: DP
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minCostClimbingStairs_2(int[] cost) {
        int f = 0, g = 0;
        for (int i = 2; i <= cost.length; ++i) {
            int gg = Math.min(f + cost[i - 2], g + cost[i - 1]);
            f = g;
            g = gg;
        }
        return g;
    }





}
