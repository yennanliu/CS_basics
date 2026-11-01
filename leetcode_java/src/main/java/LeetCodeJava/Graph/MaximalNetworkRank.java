package LeetCodeJava.Graph;

// https://leetcode.com/problems/maximal-network-rank/description/

import java.util.*;

/**
 * 1615. Maximal Network Rank
 * Medium
 *
 * There is an infrastructure of n cities with some number of roads connecting these cities.
 * Each roads[i] = [ai, bi] indicates that there is a bidirectional road between cities ai and bi.
 *
 * The network rank of two different cities is defined as the total number of directly connected roads
 * to either city. If a road is directly connected to both cities, it is only counted once.
 *
 * The maximal network rank of the infrastructure is the maximum network rank of all pairs of different cities.
 *
 * Given the integer n and the array roads, return the maximal network rank of the entire infrastructure.
 *
 *
 * Example 1:
 *
 * Input: n = 4, roads = [[0,1],[0,3],[1,2],[1,3]]
 * Output: 4
 *
 * Example 2:
 *
 * Input: n = 5, roads = [[0,1],[0,3],[1,2],[1,3],[2,3],[2,4]]
 * Output: 5
 *
 * Example 3:
 *
 * Input: n = 8, roads = [[0,1],[1,2],[2,3],[2,4],[5,6],[5,7]]
 * Output: 5
 *
 *
 * Constraints:
 *
 * 2 <= n <= 100
 * 0 <= roads.length <= n * (n - 1) / 2
 * roads[i].length == 2
 * 0 <= ai, bi <= n-1
 * ai != bi
 * Each pair of cities has at most one road connecting them.
 *
 */
public class MaximalNetworkRank {

    // V0
    // IDEA: DEGREE COUNT + INCLUSION-EXCLUSION OVER ALL PAIRS
    // rank(a, b) = degree[a] + degree[b] - (1 if a and b share a road) -- the shared road
    // would otherwise be counted twice. n <= 100, so checking every pair is cheap.
    /**
     * time = O(N^2 + E)
     * space = O(N^2)
     */
    public int maximalNetworkRank(int n, int[][] roads) {
        int[] degree = new int[n];
        boolean[][] linked = new boolean[n][n];
        for (int[] road : roads) {
            degree[road[0]]++;
            degree[road[1]]++;
            linked[road[0]][road[1]] = true;
            linked[road[1]][road[0]] = true;
        }

        int best = 0;
        for (int a = 0; a < n; a++) {
            for (int b = a + 1; b < n; b++) {
                int rank = degree[a] + degree[b];
                // the road between a and b is counted once, not twice
                if (linked[a][b]) {
                    rank--;
                }
                best = Math.max(best, rank);
            }
        }
        return best;
    }
}
