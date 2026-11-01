package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/paint-house-iii/description/

import java.util.Arrays;

/**
 * 1473. Paint House III
 * Hard
 *
 * There is a row of m houses in a small city, each house must be painted with one of the n colors (labeled from 1 to n), some houses that have been painted last summer should not be painted again.
 *
 * A neighborhood is a maximal group of continuous houses that are painted with the same color.
 *
 * For example: houses = [1,2,2,3,3,2,1,1] contains 5 neighborhoods [{1}, {2,2}, {3,3}, {2}, {1,1}].
 *
 * Given an array houses, an m x n matrix cost and an integer target where:
 *
 * houses[i]: is the color of the house i, and 0 if the house is not painted yet.
 * cost[i][j]: is the cost of paint the house i with the color j + 1.
 *
 * Return the minimum cost of painting all the remaining houses in such a way that there are exactly target neighborhoods. If it is not possible, return -1.
 *
 *
 * Example 1:
 *
 * Input: houses = [0,0,0,0,0], cost = [[1,10],[10,1],[10,1],[1,10],[5,1]], m = 5, n = 2, target = 3
 * Output: 9
 * Explanation: Paint houses of this way [1,2,2,1,1]
 * This array contains target = 3 neighborhoods, [{1}, {2,2}, {1,1}].
 * Cost of paint all houses (1 + 1 + 1 + 1 + 5) = 9.
 *
 * Example 2:
 *
 * Input: houses = [0,2,1,2,0], cost = [[1,10],[10,1],[10,1],[1,10],[5,1]], m = 5, n = 2, target = 3
 * Output: 11
 * Explanation: Some houses are already painted, Paint the houses of this way [2,2,1,2,2]
 * This array contains target = 3 neighborhoods, [{2,2}, {1}, {2,2}].
 * Cost of paint the first and last house (10 + 1) = 11.
 *
 * Example 3:
 *
 * Input: houses = [3,1,2,3], cost = [[1,1,1],[1,1,1],[1,1,1],[1,1,1]], m = 4, n = 3, target = 3
 * Output: -1
 * Explanation: Houses are already painted with a total of 4 neighborhoods [{3},{1},{2},{3}] different of target = 3.
 *
 *
 * Constraints:
 *
 * m == houses.length == cost.length
 * n == cost[i].length
 * 1 <= m <= 100
 * 1 <= n <= 20
 * 1 <= target <= m
 * 0 <= houses[i] <= n
 * 1 <= cost[i][j] <= 10^4
 *
 */
public class PaintHouseIII {

    private static final int INF = Integer.MAX_VALUE / 2;

    // V0
    // IDEA: DP ON (HOUSE, COLOUR, NEIGHBOURHOODS SO FAR)
    //       dp[colour][groups] = cheapest cost for houses 0..i with house i painted `colour`
    //                            and exactly `groups` neighbourhoods.
    //       moving to house i+1 with colour c2: groups + 1 when c2 differs, unchanged otherwise.
    //       an already painted house has its colour forced and costs 0 (repainting is illegal).
    /**
     * time = O(M * N^2 * T)
     * space = O(N * T)
     */
    public int minCost(int[] houses, int[][] cost, int m, int n, int target) {
        // dp indexed by colour 1..n and neighbourhood count 0..target
        int[][] dp = newTable(n, target);
        if (houses[0] == 0) {
            for (int colour = 1; colour <= n; colour++) {
                dp[colour][1] = cost[0][colour - 1];
            }
        } else {
            dp[houses[0]][1] = 0;
        }

        for (int i = 1; i < m; i++) {
            int[][] next = newTable(n, target);
            for (int colour = 1; colour <= n; colour++) {
                // a painted house can only keep its own colour
                if (houses[i] != 0 && houses[i] != colour) {
                    continue;
                }
                int paintCost = (houses[i] != 0) ? 0 : cost[i][colour - 1];

                for (int groups = 1; groups <= target; groups++) {
                    int best = INF;
                    for (int prevColour = 1; prevColour <= n; prevColour++) {
                        // same colour continues the neighbourhood; a new colour starts one
                        int prevGroups = (prevColour == colour) ? groups : groups - 1;
                        if (prevGroups < 1) {
                            continue;
                        }
                        best = Math.min(best, dp[prevColour][prevGroups]);
                    }
                    if (best < INF) {
                        next[colour][groups] = best + paintCost;
                    }
                }
            }
            dp = next;
        }

        int answer = INF;
        for (int colour = 1; colour <= n; colour++) {
            answer = Math.min(answer, dp[colour][target]);
        }
        return answer == INF ? -1 : answer;
    }

    /** A (colours + 1) x (target + 1) table filled with INF. */
    private int[][] newTable(int colours, int target) {
        int[][] table = new int[colours + 1][target + 1];
        for (int[] row : table) {
            Arrays.fill(row, INF);
        }
        return table;
    }
}
