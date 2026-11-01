package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/the-most-similar-path-in-a-graph/description/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 1548. The Most Similar Path in a Graph
 * Hard
 *
 * We have n cities and m bi-directional roads where roads[i] = [a_i, b_i] connects city a_i with city b_i. Each city has a name consisting of exactly three upper-case English letters given in the string array names. Starting at any city x, you can reach any city y where y != x (i.e., the cities and the roads are forming an undirected connected graph).
 *
 * You will be given a string array targetPath. You should find a path in the graph of the same length and with the minimum edit distance to targetPath.
 *
 * You need to return the order of the nodes in the path with the minimum edit distance. The path should be of the same length of targetPath and should be valid (i.e., there should be a direct road between ans[i] and ans[i + 1]). If there are multiple answers return any one of them.
 *
 * The edit distance here is the number of positions i at which the chosen path's city name differs from targetPath[i].
 *
 *
 * Example 1:
 *
 * Input: n = 5, roads = [[0,2],[0,3],[1,2],[1,3],[1,4],[2,4]], names = ["ATL","PEK","LAX","DXB","HND"], targetPath = ["ATL","DXB","HND","LAX"]
 * Output: [0,2,4,2]
 * Explanation: [0,2,4,2], [0,3,0,2] and [0,3,1,2] are accepted answers.
 * [0,2,4,2] is equivalent to ["ATL","LAX","HND","LAX"] which has edit distance = 1 with targetPath.
 * [0,3,0,2] is equivalent to ["ATL","DXB","ATL","LAX"] which has edit distance = 1 with targetPath.
 * [0,3,1,2] is equivalent to ["ATL","DXB","PEK","LAX"] which has edit distance = 1 with targetPath.
 *
 * Example 2:
 *
 * Input: n = 4, roads = [[1,0],[2,0],[3,0],[2,1],[3,1],[3,2]], names = ["ATL","PEK","LAX","DXB"], targetPath = ["ABC","DEF","GHI","JKL","MNO","PQR","STU","VWX"]
 * Output: [0,1,0,1,0,1,0,1]
 * Explanation: Any path in this graph has edit distance = 8 with targetPath.
 *
 * Example 3:
 *
 * Input: n = 6, roads = [[0,1],[1,2],[2,3],[3,4],[4,5]], names = ["ATL","PEK","LAX","ATL","DXB","HND"], targetPath = ["ATL","DXB","HND","DXB","ATL","LAX","PEK"]
 * Output: [3,4,5,4,3,2,1]
 * Explanation: [3,4,5,4,3,2,1] is the only path with edit distance = 0 with targetPath.
 * It's equivalent to ["ATL","DXB","HND","DXB","ATL","LAX","PEK"]
 *
 *
 * Constraints:
 *
 * 2 <= n <= 100
 * m == roads.length
 * n - 1 <= m <= (n * (n - 1) / 2)
 * 0 <= a_i, b_i <= n - 1
 * a_i != b_i
 * The graph is guaranteed to be connected and each pair of nodes may have at most one direct road.
 * names.length == n
 * names[i].length == 3
 * names[i] consists of upper-case English letters.
 * There can be two cities with the same name.
 * 1 <= targetPath.length <= 100
 * targetPath[i].length == 3
 * targetPath[i] consists of upper-case English letters.
 *
 *
 * Follow up: If each node can be visited only once in the path, What should you change in your solution?
 *
 */
public class TheMostSimilarPathInAGraph {

    private static final int INF = Integer.MAX_VALUE / 2;

    // V0
    // IDEA: DP OVER (PATH POSITION, CITY) + PARENT POINTERS (Viterbi-style)
    //       a node is a city, an edge a road.
    //       dp[i][city] = min edit distance of a valid path of length i+1 ending at city.
    //       dp[i][city] = mismatch(i, city) + min over neighbours prev of dp[i-1][prev]
    //       parent[i][city] remembers that prev, so the path is rebuilt backwards from the
    //       best final city.
    /**
     * time = O(M * (N + E))   (M = targetPath length, E = roads)
     * space = O(M * N)
     */
    public List<Integer> mostSimilar(int n, int[][] roads, String[] names, String[] targetPath) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int city = 0; city < n; city++) {
            graph.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            graph.get(road[0]).add(road[1]);
            graph.get(road[1]).add(road[0]);
        }

        int m = targetPath.length;
        int[][] dp = new int[m][n];
        int[][] parent = new int[m][n];

        // a path of length 1 can start at any city
        for (int city = 0; city < n; city++) {
            dp[0][city] = names[city].equals(targetPath[0]) ? 0 : 1;
        }

        for (int i = 1; i < m; i++) {
            for (int city = 0; city < n; city++) {
                int mismatch = names[city].equals(targetPath[i]) ? 0 : 1;
                // cheapest neighbour to have come from
                int best = INF;
                int bestPrev = -1;
                for (int prev : graph.get(city)) {
                    if (dp[i - 1][prev] < best) {
                        best = dp[i - 1][prev];
                        bestPrev = prev;
                    }
                }
                dp[i][city] = best + mismatch;
                parent[i][city] = bestPrev;
            }
        }

        // best final city
        int last = 0;
        for (int city = 1; city < n; city++) {
            if (dp[m - 1][city] < dp[m - 1][last]) {
                last = city;
            }
        }

        // walk the parent pointers backwards to rebuild the path
        Integer[] path = new Integer[m];
        for (int i = m - 1; i >= 0; i--) {
            path[i] = last;
            last = parent[i][last];
        }
        return new ArrayList<>(Arrays.asList(path));
    }
}
