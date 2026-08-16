package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-vacation-days/description/

import java.util.ArrayList;
import java.util.List;
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


    // V1
    // IDEA: TOP-DOWN MEMOISED RECURSION over (week, city)
    /**
     *  best(w, c) = days[c][w] + max over reachable next cities of best(w+1, next)
     *
     *  Only the reachable (week, city) pairs are ever evaluated, and the base case
     *  is the natural `no weeks left`.
     *
     *  time  = O(k * n^2)
     *  space = O(k * n)
     */
    private Integer[][] memoVac;

    public int maxVacationDays_1(int[][] flights, int[][] days) {
        int n = flights.length;
        if (n == 0 || days.length == 0 || days[0].length == 0) {
            return 0;
        }
        memoVac = new Integer[days[0].length][n];
        return bestFrom(flights, days, 0, 0);
    }

    private int bestFrom(int[][] flights, int[][] days, int week, int city) {
        int n = flights.length;
        int k = days[0].length;
        if (week == k) {
            return 0;
        }
        if (memoVac[week][city] != null) {
            return memoVac[week][city];
        }

        int best = Integer.MIN_VALUE;
        for (int nxt = 0; nxt < n; nxt++) {
            if (nxt == city || flights[city][nxt] == 1) {
                best = Math.max(best, bestFrom(flights, days, week + 1, nxt));
            }
        }
        int res = days[city][week] + best;
        memoVac[week][city] = res;
        return res;
    }

    // V2
    // IDEA: BACKWARD DP (fill the LAST week first)
    /**
     *  dp[c] = best days obtainable from week w onward, starting week w in city c.
     *  Sweeping w downward means the transition reads the FUTURE, which mirrors the
     *  recursion in V1 without a stack.
     *
     *  Also avoids V0's `is this city reachable yet?` guard entirely -- every city
     *  is a legal place to be in the future.
     *
     *  time  = O(k * n^2)
     *  space = O(n)
     */
    public int maxVacationDays_2(int[][] flights, int[][] days) {
        int n = flights.length;
        if (n == 0 || days.length == 0 || days[0].length == 0) {
            return 0;
        }
        int k = days[0].length;

        int[] dp = new int[n];
        for (int w = k - 1; w >= 0; w--) {
            int[] ndp = new int[n];
            for (int c = 0; c < n; c++) {
                int best = Integer.MIN_VALUE;
                for (int nxt = 0; nxt < n; nxt++) {
                    if (nxt == c || flights[c][nxt] == 1) {
                        best = Math.max(best, dp[nxt]);
                    }
                }
                ndp[c] = days[c][w] + best;
            }
            dp = ndp;
        }
        return dp[0];
    }

    // V3
    // IDEA: ADJACENCY LISTS instead of scanning the full n x n matrix
    /**
     *  Precompute, for every city, the list of cities reachable from it (itself
     *  included). The inner loop then iterates only over real edges rather than all
     *  n candidates.
     *
     *  O(k * E) instead of O(k * n^2) -- a large win on a sparse flight network.
     *
     *  time  = O(n^2 + k * E)
     *  space = O(n + E)
     */
    public int maxVacationDays_3(int[][] flights, int[][] days) {
        int n = flights.length;
        if (n == 0 || days.length == 0 || days[0].length == 0) {
            return 0;
        }
        int k = days[0].length;

        List<List<Integer>> reach = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            List<Integer> to = new ArrayList<>();
            to.add(i);                       // staying put is always allowed
            for (int j = 0; j < n; j++) {
                if (j != i && flights[i][j] == 1) {
                    to.add(j);
                }
            }
            reach.add(to);
        }

        final int NEG = Integer.MIN_VALUE;
        int[] dp = new int[n];
        Arrays.fill(dp, NEG);
        for (int j : reach.get(0)) {
            dp[j] = Math.max(dp[j], days[j][0]);
        }

        for (int w = 1; w < k; w++) {
            int[] ndp = new int[n];
            Arrays.fill(ndp, NEG);
            for (int c = 0; c < n; c++) {
                if (dp[c] == NEG) {
                    continue;
                }
                for (int nxt : reach.get(c)) {
                    ndp[nxt] = Math.max(ndp[nxt], dp[c] + days[nxt][w]);
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
