package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-cost-of-trip-with-k-highways/description/

import java.util.*;

/**
 * 2247. Maximum Cost of Trip With K Highways
 * Hard
 *
 * (premium / locked problem)
 *
 * A series of highways connect n cities numbered from 0 to n - 1. You are given a 2D integer array highways where highways[i] = [city1_i, city2_i, toll_i] indicates that there is a highway that connects city1_i and city2_i, allowing a car to go from city1_i to city2_i and vice versa for a cost of toll_i.
 *
 * You are also given an integer k. You are going on a trip that crosses exactly k highways. You may start at any city, but you may only visit each city at most once during your trip.
 *
 * Return the maximum cost of your trip. If there is no trip that meets the requirements, return -1.
 *
 *
 * Example 1:
 *
 * Input: n = 5, highways = [[0,1,4],[2,1,3],[1,4,11],[3,2,3],[3,4,2]], k = 3
 * Output: 17
 * Explanation:
 * One possible trip is to go from 0 -> 1 -> 4 -> 3. The cost of this trip is 4 + 11 + 2 = 17.
 * Another possible trip is to go from 4 -> 1 -> 2 -> 3. The cost of this trip is 11 + 3 + 3 = 17.
 * It can be proven that 17 is the maximum possible cost of any valid trip.
 * Note that the trip 4 -> 1 -> 0 -> 1 is not allowed because you visit the city 1 twice.
 *
 * Example 2:
 *
 * Input: n = 4, highways = [[0,1,3],[2,3,2]], k = 2
 * Output: -1
 * Explanation: There are no valid trips of length 2, so return -1.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 15
 * 1 <= highways.length <= 50
 * highways[i].length == 3
 * 0 <= city1_i, city2_i <= n - 1
 * city1_i != city2_i
 * 0 <= toll_i <= 100
 * 1 <= k <= 50
 * There are no duplicate highways.
 *
 */
public class MaximumCostOfTripWithKHighways {

    // V0
    // IDEA: BITMASK DP OVER (VISITED CITIES, CURRENT CITY) — HELD-KARP STYLE LONGEST PATH
    /**
     * time = O(2^N * N^2)
     * space = O(2^N * N)
     */
    public int maximumCost(int n, int[][] highways, int k) {
        // a trip of k highways visits k + 1 distinct cities
        if (k >= n) {
            return -1;
        }

        // weight[u][v] = toll of the highway u-v, or -1 if none
        int[][] weight = new int[n][n];
        for (int[] row : weight) {
            Arrays.fill(row, -1);
        }
        for (int[] highway : highways) {
            weight[highway[0]][highway[1]] = highway[2];
            weight[highway[1]][highway[0]] = highway[2];
        }

        // dp[mask][city] = max cost of a path visiting exactly the cities in mask, ending at city; -1 = unreachable
        int size = 1 << n;
        int[][] dp = new int[size][n];
        for (int[] row : dp) {
            Arrays.fill(row, -1);
        }
        // base case: a trip can start at any city
        for (int city = 0; city < n; city++) {
            dp[1 << city][city] = 0;
        }

        int best = -1;
        for (int mask = 1; mask < size; mask++) {
            int visited = Integer.bitCount(mask);
            for (int city = 0; city < n; city++) {
                if (dp[mask][city] < 0) {
                    continue;
                }
                if (visited == k + 1) {
                    // exactly k highways used; no need to extend further
                    best = Math.max(best, dp[mask][city]);
                    continue;
                }
                for (int next = 0; next < n; next++) {
                    boolean alreadyVisited = ((mask >> next) & 1) == 1;
                    if (weight[city][next] < 0 || alreadyVisited) {
                        continue;
                    }
                    int nextMask = mask | (1 << next);
                    dp[nextMask][next] = Math.max(dp[nextMask][next], dp[mask][city] + weight[city][next]);
                }
            }
        }
        return best;
    }
}
