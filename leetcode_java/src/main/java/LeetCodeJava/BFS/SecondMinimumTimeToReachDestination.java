package LeetCodeJava.BFS;

// https://leetcode.com/problems/second-minimum-time-to-reach-destination/description/

import java.util.*;

/**
 * 2045. Second Minimum Time to Reach Destination
 * Hard
 *
 * A city is represented as a bi-directional connected graph with n vertices where each vertex is
 * labeled from 1 to n (inclusive). The edges in the graph are represented as a 2D integer array
 * edges, where each edges[i] = [ui, vi] denotes a bi-directional edge between vertex ui and
 * vertex vi. Every vertex pair is connected by at most one edge, and no vertex has an edge to
 * itself. The time taken to traverse any edge is time minutes.
 *
 * Each vertex has a traffic signal which changes its color from green to red and vice versa
 * every change minutes. All signals change at the same time. You can enter a vertex at any time,
 * but can leave a vertex only when the signal is green. You cannot wait at a vertex if the
 * signal is green.
 *
 * The second minimum value is defined as the smallest value strictly larger than the minimum
 * value.
 *
 * For example the second minimum value of [2, 3, 4] is 3, and the second minimum value of
 * [2, 2, 4] is 4.
 *
 * Given n, edges, time, and change, return the second minimum time it will take to go from
 * vertex 1 to vertex n.
 *
 * Notes:
 *
 * You can go through any vertex any number of times, including 1 and n.
 * You can assume that when the journey starts, all signals have just turned green.
 *
 *
 * Example 1:
 *
 * Input: n = 5, edges = [[1,2],[1,3],[1,4],[3,4],[4,5]], time = 3, change = 5
 * Output: 13
 * Explanation:
 * The minimum time taken is 9 minutes (path 1 -> 4 -> 5), and the second minimum is 13 minutes
 * (path 1 -> 3 -> 4 -> 5).
 *
 * Example 2:
 *
 * Input: n = 2, edges = [[1,2]], time = 3, change = 2
 * Output: 11
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^4
 * n - 1 <= edges.length <= min(2 * 10^4, n * (n - 1) / 2)
 * edges[i].length == 2
 * 1 <= ui, vi <= n
 * ui != vi
 * There are no duplicate edges.
 * Each vertex can be reached directly or indirectly from every other vertex.
 * 1 <= time, change <= 5 * 10^4
 *
 */
public class SecondMinimumTimeToReachDestination {

    private static final int INF = Integer.MAX_VALUE;

    // V0
    // IDEA: BFS TRACKING THE TWO SMALLEST EDGE COUNTS PER NODE, THEN REPLAY THE LIGHTS
    //  every edge costs the same `time` and the lights are global, so more edges never
    //  arrives earlier: the second minimum time comes from the second smallest edge count.
    //  BFS keeps firstDist (fewest edges) and secondDist (STRICTLY more) per node; each
    //  node is enqueued at most twice. Then walk that many edges, waiting out red lights.
    /**
     * time = O(V + E)
     * space = O(V + E)
     */
    public int secondMinimum(int n, int[][] edges, int time, int change) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        int[] firstDist = new int[n + 1];
        int[] secondDist = new int[n + 1];
        Arrays.fill(firstDist, INF);
        Arrays.fill(secondDist, INF);
        firstDist[1] = 0;

        // entries are {node, edge count used to reach it}
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] {1, 0});
        while (!queue.isEmpty()) {
            int[] entry = queue.poll();
            int node = entry[0];
            int nextDist = entry[1] + 1;
            for (int next : graph.get(node)) {
                if (nextDist < firstDist[next]) {
                    firstDist[next] = nextDist;
                    queue.offer(new int[] {next, nextDist});
                } else if (nextDist > firstDist[next] && nextDist < secondDist[next]) {
                    // strictly greater: an equal count is not a "second minimum"
                    secondDist[next] = nextDist;
                    queue.offer(new int[] {next, nextDist});
                }
            }
        }

        // convert the edge count into minutes
        int elapsed = 0;
        for (int step = 0; step < secondDist[n]; step++) {
            // an odd number of full `change` periods means the light is red: wait for green
            if ((elapsed / change) % 2 == 1) {
                elapsed = (elapsed / change + 1) * change;
            }
            elapsed += time;
        }
        return elapsed;
    }
}
