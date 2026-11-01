package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/painting-the-walls/description/

import java.util.*;

/**
 * 2742. Painting the Walls
 * Hard
 *
 * You are given two 0-indexed integer arrays, cost and time, of size n representing the costs and
 * the time taken to paint n different walls respectively. There are two painters available:
 *
 * A paid painter that paints the ith wall in time[i] units of time and takes cost[i] units of money.
 * A free painter that paints any wall in 1 unit of time at a cost of 0. But the free painter can
 * only be used if the paid painter is already occupied.
 *
 * Return the minimum amount of money required to paint the n walls.
 *
 *
 * Example 1:
 *
 * Input: cost = [1,2,3,2], time = [1,2,3,2]
 * Output: 3
 * Explanation: The walls at index 0 and 1 will be painted by the paid painter, and it will take 3
 * units of time; meanwhile, the free painter will paint the walls at index 2 and 3, free of cost
 * in 2 units of time. Thus, the total cost is 1 + 2 = 3.
 *
 * Example 2:
 *
 * Input: cost = [2,3,4,2], time = [1,1,1,1]
 * Output: 4
 * Explanation: The walls at index 0 and 3 will be painted by the paid painter, and it will take 2
 * units of time; meanwhile, the free painter will paint the walls at index 1 and 2, free of cost
 * in 2 units of time. Thus, the total cost is 2 + 2 = 4.
 *
 *
 * Constraints:
 *
 * 1 <= cost.length <= 500
 * cost.length == time.length
 * 1 <= cost[i] <= 10^6
 * 1 <= time[i] <= 500
 *
 */
public class PaintingTheWalls {

    private static final long INF = Long.MAX_VALUE / 2;

    // V0
    // IDEA: 0/1 KNAPSACK - PAYING FOR WALL i "COVERS" time[i] + 1 WALLS (ITSELF + time[i] FREE ONES); MIN COST TO COVER n
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int paintWalls(int[] cost, int[] time) {
        int n = cost.length;

        // dp[w] = min cost to cover at least w walls (capped at n)
        long[] dp = new long[n + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int i = 0; i < n; i++) {
            int covered = time[i] + 1;
            // descending so each paid wall is used at most once
            for (int walls = n; walls > 0; walls--) {
                // covering more than needed is fine: clamp the previous requirement at 0
                int previous = Math.max(walls - covered, 0);
                if (dp[previous] + cost[i] < dp[walls]) {
                    dp[walls] = dp[previous] + cost[i];
                }
            }
        }
        return (int) dp[n];
    }
}
