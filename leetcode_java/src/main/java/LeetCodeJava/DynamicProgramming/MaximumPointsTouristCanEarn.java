package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-points-tourist-can-earn/description/

import java.util.*;

/**
 * 3332. Maximum Points Tourist Can Earn
 * Medium
 *
 * You are given two integers, n and k, along with two 2D integer arrays, stayScore and travelScore.
 *
 * A tourist is visiting a country with n cities, where each city is directly connected to every other city. The tourist's journey consists of exactly k 0-indexed days, and they can choose any city as their starting point.
 *
 * Each day, the tourist has two choices:
 *
 * Stay in the current city: If the tourist stays in their current city curr during day i, they will earn stayScore[i][curr] points.
 * Move to another city: If the tourist moves from their current city curr to city dest, they will earn travelScore[curr][dest] points.
 *
 * Return the maximum possible points the tourist can earn.
 *
 *
 * Example 1:
 *
 * Input: n = 2, k = 1, stayScore = [[2,3]], travelScore = [[0,2],[1,0]]
 * Output: 3
 * Explanation:
 * The tourist earns the maximum number of points by starting in city 1 and staying in that city.
 *
 * Example 2:
 *
 * Input: n = 3, k = 2, stayScore = [[3,4,2],[2,1,2]], travelScore = [[0,2,1],[2,0,4],[3,2,0]]
 * Output: 8
 * Explanation:
 * The tourist earns the maximum number of points by starting in city 1, staying in that city on day 0, and traveling to city 2 on day 1.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 200
 * 1 <= k <= 200
 * n == travelScore.length == travelScore[i].length == stayScore[i].length
 * k == stayScore.length
 * 1 <= stayScore[i][j] <= 100
 * 0 <= travelScore[i][j] <= 100
 * travelScore[i][i] == 0
 *
 */
public class MaximumPointsTouristCanEarn {

    // V0
    // IDEA: DP OVER (DAY, CURRENT CITY), ONE ROLLING ROW
    /**
     *  DP def:
     *     dp[c] = best total achievable while standing in city c at the START of
     *             the current day. Seeding dp[c] = 0 for EVERY c encodes "any
     *             city may be the starting point".
     *
     *  DP eq (each day, from city c):
     *     stay        -> next[c] = max(next[c], dp[c] + stayScore[day][c])
     *     travel to d -> next[d] = max(next[d], dp[c] + travelScore[c][d])
     *
     *  Init:   dp[c] = 0
     *  Answer: max(dp) after k days
     *
     *  time = O(K * N^2)
     *  space = O(N)
     */
    public int maxScore(int n, int k, int[][] stayScore, int[][] travelScore) {
        int[] dp = new int[n];

        for (int day = 0; day < k; day++) {
            int[] next = new int[n];
            Arrays.fill(next, Integer.MIN_VALUE);

            for (int city = 0; city < n; city++) {
                int current = dp[city];

                // stay in the same city today
                next[city] = Math.max(next[city], current + stayScore[day][city]);

                // or move to another city today
                for (int dest = 0; dest < n; dest++) {
                    if (dest == city) {
                        continue;
                    }
                    next[dest] = Math.max(next[dest], current + travelScore[city][dest]);
                }
            }
            dp = next;
        }

        int best = 0;
        for (int total : dp) {
            best = Math.max(best, total);
        }
        return best;
    }
}
