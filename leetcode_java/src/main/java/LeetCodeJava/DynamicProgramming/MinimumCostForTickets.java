package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-cost-for-tickets/description/

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 *  983. Minimum Cost For Tickets
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * You have planned some train traveling one year in advance. The days of the year in which you will travel are given as an integer array days. Each day is an integer from 1 to 365.
 *
 * Train tickets are sold in three different ways:
 *
 * a 1-day pass is sold for costs[0] dollars,
 * a 7-day pass is sold for costs[1] dollars, and
 * a 30-day pass is sold for costs[2] dollars.
 * The passes allow that many days of consecutive travel.
 *
 * For example, if we get a 7-day pass on day 2, then we can travel for 7 days: 2, 3, 4, 5, 6, 7, and 8.
 * Return the minimum number of dollars you need to travel every day in the given list of days.
 *
 *  Example 1:
 *
 * Input: days = [1,4,6,7,8,20], costs = [2,7,15]
 * Output: 11
 * Explanation: For example, here is one way to buy passes that lets you travel your travel plan:
 * On day 1, you bought a 1-day pass for costs[0] = $2, which covered day 1.
 * On day 3, you bought a 7-day pass for costs[1] = $7, which covered days 3, 4, ..., 9.
 * On day 20, you bought a 1-day pass for costs[0] = $2, which covered day 20.
 * In total, you spent $11 and covered all the days of your travel.
 * Example 2:
 *
 * Input: days = [1,2,3,4,5,6,7,8,9,10,30,31], costs = [2,7,15]
 * Output: 17
 * Explanation: For example, here is one way to buy passes that lets you travel your travel plan:
 * On day 1, you bought a 30-day pass for costs[2] = $15 which covered days 1, 2, ..., 30.
 * On day 31, you bought a 1-day pass for costs[0] = $2 which covered day 31.
 * In total, you spent $17 and covered all the days of your travel.
 *
 *
 * Constraints:
 *
 * 1 <= days.length <= 365
 * 1 <= days[i] <= 365
 * days is in strictly increasing order.
 * costs.length == 3
 * 1 <= costs[i] <= 1000
 *
 *
 */
public class MinimumCostForTickets {

    // V0
//    public int mincostTickets(int[] days, int[] costs) {
//
//    }


    // V0-1
    // IDEA: Top-down DP (Memoization) (GPT)
    /**
     * dp[i] =
     *    minimum cost to cover travel days
     *    from index i onward (向前)
     *
     */
    /**
     *
     * Complexity
     * Time: O(n²) worst case (because of the repeated scans)
     * Space: O(n)
     *
     * Since days.length <= 365, this easily passes.
     *
     *
     */
    public int mincostTickets_0_1(int[] days, int[] costs) {
        Integer[] memo = new Integer[days.length];
        return dfs(0, days, costs, memo);
    }

    private int dfs(int i, int[] days, int[] costs, Integer[] memo) {
        if (i >= days.length) {
            return 0;
        }

        if (memo[i] != null) {
            return memo[i];
        }

        // 1-day pass
        /** NOTE !!
         *
         *  recursion call here
         */
        int cost1 = costs[0] + dfs(i + 1, days, costs, memo);

        // 7-day pass
        int j = i;
        while (j < days.length && days[j] < days[i] + 7) {
            j++;
        }
        /** NOTE !!
         *
         *  recursion call here
         */
        int cost7 = costs[1] + dfs(j, days, costs, memo);

        // 30-day pass
        j = i;
        while (j < days.length && days[j] < days[i] + 30) {
            j++;
        }
        /** NOTE !!
         *
         *  recursion call here
         */
        int cost30 = costs[2] + dfs(j, days, costs, memo);

        memo[i] = Math.min(cost1, Math.min(cost7, cost30));
        return memo[i];
    }



    // V0-2
    // IDEA: Calendar DP (GPT)
    /**
     *
     *  - Since travel days are within [1, 365], use DP by calendar day.
     *
     *
     *
     * - DP def:
     *     dp[d] = minimum cost to cover all travel up to day d
     *
     *
     * - DP eq:
     *
     * Transition
     *
     *    - If d is not a travel day:
     *
     *           dp[d] = dp[d - 1]
     *
     *    - Otherwise:
     *
     *           dp[d] = min(
     *              dp[d - 1] + cost1,
     *              dp[max(0, d - 7)] + cost7,
     *             dp[max(0, d - 30)] + cost30
     *            )
     *
     */
    public int mincostTickets_0_2(int[] days, int[] costs) {

        Set<Integer> travelDays = new HashSet<>();
        for (int day : days) {
            travelDays.add(day);
        }

        int lastDay = days[days.length - 1];
        int[] dp = new int[lastDay + 1];

        for (int day = 1; day <= lastDay; day++) {

            if (!travelDays.contains(day)) {
                dp[day] = dp[day - 1];
                continue;
            }

            int oneDay = dp[Math.max(0, day - 1)] + costs[0];

            int sevenDay = dp[Math.max(0, day - 7)] + costs[1];

            int thirtyDay = dp[Math.max(0, day - 30)] + costs[2];

            dp[day] = Math.min(
                    oneDay,
                    Math.min(sevenDay, thirtyDay));
        }

        return dp[lastDay];
    }



    // V1-1
    // IDEA: TOP DOWN DP
    // https://leetcode.com/problems/minimum-cost-for-tickets/editorial/
    HashSet<Integer> isTravelNeeded = new HashSet<>();

    private int solve(int[] dp, int[] days, int[] costs, int currDay) {
        // If we have iterated over travel days, return 0.
        if (currDay > days[days.length - 1]) {
            return 0;
        }

        // If we don't need to travel on this day, move on to next day.
        if (!isTravelNeeded.contains(currDay)) {
            return solve(dp, days, costs, currDay + 1);
        }

        // If already calculated, return from here with the stored answer.
        if (dp[currDay] != -1) {
            return dp[currDay];
        }

        /** NOTE !!
         *
         *  recursion call here
         */
        int oneDay = costs[0] + solve(dp, days, costs, currDay + 1);
        /** NOTE !!
         *
         *  recursion call here
         */
        int sevenDay = costs[1] + solve(dp, days, costs, currDay + 7);
        /** NOTE !!
         *
         *  recursion call here
         */
        int thirtyDay = costs[2] + solve(dp, days, costs, currDay + 30);

        // Store the cost with the minimum of the three options.
        return dp[currDay] = Math.min(oneDay, Math.min(sevenDay, thirtyDay));
    }

    public int mincostTickets_1_1(int[] days, int[] costs) {
        // The last day on which we need to travel.
        int lastDay = days[days.length - 1];
        int dp[] = new int[lastDay + 1];
        Arrays.fill(dp, -1);

        // Mark the days on which we need to travel.
        for (int day : days) {
            isTravelNeeded.add(day);
        }

        return solve(dp, days, costs, 1);
    }



    // V1-2
    // IDEA: BOTTOM UP DP
    // https://leetcode.com/problems/minimum-cost-for-tickets/editorial/
    public int mincostTickets_1_2(int[] days, int[] costs) {
        // The last day on which we need to travel.
        int lastDay = days[days.length - 1];
        int dp[] = new int[lastDay + 1];
        Arrays.fill(dp, 0);

        int i = 0;
        for (int day = 1; day <= lastDay; day++) {
            // If we don't need to travel on this day, the cost won't change.
            if (day < days[i]) {
                dp[day] = dp[day - 1];
            } else {
                // Buy a pass on this day, and move on to the next travel day.
                i++;
                // Store the cost with the minimum of the three options.
                dp[day] = Math.min(dp[day - 1] + costs[0],
                        Math.min(dp[Math.max(0, day - 7)] + costs[1],
                                dp[Math.max(0, day - 30)] + costs[2]));
            }
        }

        return dp[lastDay];
    }



    // V2





}
