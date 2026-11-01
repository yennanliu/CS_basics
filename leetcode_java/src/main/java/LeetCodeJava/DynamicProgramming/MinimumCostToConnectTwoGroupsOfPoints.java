package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-cost-to-connect-two-groups-of-points/description/

import java.util.Arrays;
import java.util.List;

/**
 * 1595. Minimum Cost to Connect Two Groups of Points
 * Hard
 *
 * You are given two groups of points where the first group has size1 points, the second group has size2 points, and size1 >= size2.
 *
 * The cost of the connection between any two points are given in an size1 x size2 matrix where cost[i][j] is the cost of connecting point i of the first group and point j of the second group. The groups are connected if each point in both groups is connected to one or more points in the opposite group. In other words, each point in the first group must be connected to at least one point in the second group, and each point in the second group must be connected to at least one point in the first group.
 *
 * Return the minimum cost it takes to connect the two groups.
 *
 * Example 1:
 *
 * Input: cost = [[15, 96], [36, 2]]
 * Output: 17
 * Explanation: The optimal way of connecting the groups is:
 * 1--A
 * 2--B
 * This results in a total cost of 17.
 *
 * Example 2:
 *
 * Input: cost = [[1, 3, 5], [4, 1, 1], [1, 5, 3]]
 * Output: 4
 * Explanation: The optimal way of connecting the groups is:
 * 1--A
 * 2--B
 * 2--C
 * 3--A
 * This results in a total cost of 4.
 * Note that there are multiple points connected to point 2 in the first group and point A in the second group. This does not matter as there is no limit to the number of points that can be connected. We only care about the minimum total cost.
 *
 * Example 3:
 *
 * Input: cost = [[2, 5, 1], [3, 4, 7], [8, 1, 2], [6, 2, 4], [3, 8, 8]]
 * Output: 10
 *
 * Constraints:
 *
 * size1 == cost.length
 * size2 == cost[i].length
 * 1 <= size1, size2 <= 12
 * size1 >= size2
 * 0 <= cost[i][j] <= 100
 *
 */
public class MinimumCostToConnectTwoGroupsOfPoints {

    private static final int INF = Integer.MAX_VALUE / 2;

    // V0
    // IDEA: BITMASK DP OVER THE SMALLER (SECOND) GROUP
    //       dp[i][mask] = min cost where the first i points of group 1 are each connected, and
    //                     the set of group-2 points covered is EXACTLY mask.
    //       for point i (1-based) and some j in mask, let rest = mask without j. Edge (i, j)
    //       costs cost[i-1][j] and comes on top of one of:
    //         dp[i-1][mask] : j was already covered, i's first edge goes to j
    //         dp[i-1][rest] : j is newly covered by i's first edge
    //         dp[i][rest]   : i already has an edge in this row, (i, j) is an extra one
    //       answer = dp[m][all of group 2].
    /**
     * time = O(M * 2^N * N)
     * space = O(M * 2^N)
     */
    public int connectTwoGroups(List<List<Integer>> cost) {
        int m = cost.size();
        int n = cost.get(0).size();
        int fullMask = (1 << n) - 1;

        int[][] dp = new int[m + 1][1 << n];
        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }
        // nothing from group 1 used, nothing from group 2 covered
        dp[0][0] = 0;

        for (int i = 1; i <= m; i++) {
            for (int mask = 0; mask <= fullMask; mask++) {
                int best = INF;
                for (int j = 0; j < n; j++) {
                    int bit = 1 << j;
                    if ((mask & bit) == 0) {
                        continue;
                    }
                    int rest = mask ^ bit;
                    int before = Math.min(dp[i - 1][mask], Math.min(dp[i - 1][rest], dp[i][rest]));
                    if (before < INF) {
                        best = Math.min(best, before + cost.get(i - 1).get(j));
                    }
                }
                dp[i][mask] = best;
            }
        }
        return dp[m][fullMask];
    }
}
