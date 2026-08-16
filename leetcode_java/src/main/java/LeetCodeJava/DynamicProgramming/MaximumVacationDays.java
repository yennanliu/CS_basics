package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-vacation-days/description/

import java.util.Arrays;

/**
 * 568. Maximum Vacation Days
 * Hard
 * Lock: Prime
 *
 * LeetCode wants to give one of its best employees the option to travel among n cities
 * to collect algorithm problems. Your job is to schedule the traveling to maximize the
 * number of vacation days you could take.
 *
 * Rules and restrictions:
 *
 * 1. You can only travel among n cities, represented by indexes from 0 to n - 1.
 *    Initially, you are in the city indexed 0 on Monday.
 * 2. The cities are connected by flights, given as an n x n matrix. If there is no flight
 *    from city i to city j, flights[i][j] == 0; otherwise flights[i][j] == 1.
 *    Also, flights[i][i] == 0 for all i.
 * 3. You totally have k weeks (each week has seven days) to travel. You can only take
 *    flights on each week's Monday morning.
 * 4. For each city, you can only have restricted vacation days in different weeks, given
 *    an n x k matrix called days. days[i][j] is the maximum days you could take a
 *    vacation in city i in week j.
 * 5. You could stay in a city beyond the number of vacation days, but you should work on
 *    the extra days.
 * 6. If you fly from city A to city B and take the vacation on that day, the deduction
 *    towards vacation days will count towards the vacation days of city B in that week.
 *
 * Given the two matrices flights and days, return the maximum vacation days you could
 * take during k weeks.
 *
 * Example 1:
 *
 * Input: flights = [[0,1,1],[1,0,1],[1,1,0]], days = [[1,3,1],[6,0,3],[3,3,3]]
 * Output: 12
 *
 * Example 2:
 *
 * Input: flights = [[0,0,0],[0,0,0],[0,0,0]], days = [[1,1,1],[7,7,7],[7,7,7]]
 * Output: 3
 *
 * Example 3:
 *
 * Input: flights = [[0,1,1],[1,0,1],[1,1,0]], days = [[7,0,0],[0,7,0],[0,0,7]]
 * Output: 21
 *
 *
 * Constraints:
 *
 * n == flights.length
 * n == flights[i].length
 * n == days.length
 * k == days[i].length
 * 1 <= n, k <= 100
 * flights[i][j] is either 0 or 1.
 * 0 <= days[i][j] <= 7
 *
 */
public class MaximumVacationDays {

    // V0
    // IDEA: DP (week by week, state = the city you spend the week in)
    /**
     *  DP def:
     *    - dp[j] = max vacation days collected so far, ENDING week w in city j
     *              (NEG means city j is NOT reachable at that week)
     *
     *  DP eq:
     *    - dpNew[j] = max( dp[i] for i where i == j or flights[i][j] == 1 ) + days[j][w]
     *
     *  init : week 0, you START in city 0, so you may STAY (city 0) or fly to any city j
     *         with flights[0][j] == 1
     *
     *  NOTE !!! the `dp[i] == NEG -> skip` guard matters: without it an unreachable
     *           city would leak a bogus (NEG + days) value into the next week.
     *
     *  time  = O(k * n^2)
     *  space = O(n)
     */
    public int maxVacationDays(int[][] flights, int[][] days) {
        int n = flights.length;
        if (n == 0 || days.length == 0 || days[0].length == 0) {
            return 0;
        }
        int k = days[0].length;

        final int NEG = Integer.MIN_VALUE;

        // week 0
        int[] dp = new int[n];
        Arrays.fill(dp, NEG);
        for (int j = 0; j < n; j++) {
            if (j == 0 || flights[0][j] == 1) {
                dp[j] = days[j][0];
            }
        }

        for (int w = 1; w < k; w++) {
            int[] ndp = new int[n];
            Arrays.fill(ndp, NEG);

            for (int j = 0; j < n; j++) {
                for (int i = 0; i < n; i++) {
                    if (dp[i] == NEG) {
                        continue;
                    }
                    // STAY in city i (i == j) or FLY i -> j on Monday
                    if (i == j || flights[i][j] == 1) {
                        ndp[j] = Math.max(ndp[j], dp[i] + days[j][w]);
                    }
                }
            }
            dp = ndp;
        }

        int res = 0;
        for (int v : dp) {
            res = Math.max(res, v);
        }
        return res;
    }

}
