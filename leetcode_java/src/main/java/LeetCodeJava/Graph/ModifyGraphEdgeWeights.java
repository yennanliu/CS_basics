package LeetCodeJava.Graph;

// https://leetcode.com/problems/modify-graph-edge-weights/description/

import java.util.*;

/**
 * 2699. Modify Graph Edge Weights
 * Hard
 *
 * You are given an undirected weighted connected graph containing n nodes labeled from 0 to n - 1, and an integer array edges where edges[i] = [ai, bi, wi] indicates that there is an edge between nodes ai and bi with weight wi.
 *
 * Some edges have a weight of -1 (wi = -1), while others have a positive weight (wi > 0).
 *
 * Your task is to modify all edges with a weight of -1 by assigning them positive integer values in the range [1, 2 * 10^9] so that the shortest distance between the nodes source and destination becomes equal to an integer target. If there are multiple modifications that make the shortest distance between source and destination equal to target, any of them will be considered correct.
 *
 * Return an array containing all edges (even unmodified ones) in any order if it is possible to make the shortest distance from source to destination equal to target, or an empty array if it's impossible.
 *
 * Note: You are not allowed to modify the weights of edges with initial positive weights.
 *
 *
 * Example 1:
 *
 * Input: n = 5, edges = [[4,1,-1],[2,0,-1],[0,3,-1],[4,3,-1]], source = 0, destination = 1, target = 5
 * Output: [[4,1,1],[2,0,1],[0,3,3],[4,3,1]]
 * Explanation: The graph above shows a possible modification to the edges, making the distance from 0 to 1 equal to 5.
 *
 * Example 2:
 *
 * Input: n = 3, edges = [[0,1,-1],[0,2,5]], source = 0, destination = 2, target = 6
 * Output: []
 * Explanation: The graph above contains the initial edges. It is not possible to make the distance from 0 to 2 equal to 6 by modifying the edge with weight -1. So, an empty array is returned.
 *
 * Example 3:
 *
 * Input: n = 4, edges = [[1,0,4],[1,2,3],[2,3,5],[0,3,-1]], source = 0, destination = 2, target = 6
 * Output: [[1,0,4],[1,2,3],[2,3,5],[0,3,1]]
 * Explanation: The graph above shows a modified graph having the shortest distance from 0 to 2 as 6.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 100
 * 1 <= edges.length <= n * (n - 1) / 2
 * edges[i].length == 3
 * 0 <= ai, bi < n
 * wi = -1 or 1 <= wi <= 10^7
 * ai != bi
 * 0 <= source, destination < n
 * 1 <= target <= 10^9
 * The graph is connected, and there are no self-loops or repeated edges
 *
 */
public class ModifyGraphEdgeWeights {

    // V0
    // IDEA: DIJKSTRA, ACTIVATING THE -1 EDGES ONE BY ONE (greedy)
    //  - Dijkstra with only the fixed edges: d < target -> impossible; d == target -> every
    //    -1 edge becomes MAX_WEIGHT; d > target -> shorten using the -1 edges
    //  - turn each -1 edge on with weight 1 and rerun Dijkstra; the first time d <= target the
    //    new shortest path uses that edge, so raising it by (target - d) hits target exactly;
    //    every later -1 edge becomes MAX_WEIGHT
    //  - n <= 100, so the O(n^2) adjacency-matrix Dijkstra (no heap) is used
    /**
     * time = O(m * n^2)
     * space = O(n^2)
     */
    public int[][] modifiedGraphEdges(int n, int[][] edges, int source, int destination, int target) {
        // weight[a][b] = current weight of edge a-b, MAX_WEIGHT = no edge (acts as infinity)
        long[][] weight = new long[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(weight[i], MAX_WEIGHT);
        }
        for (int[] edge : edges) {
            if (edge[2] > 0) {
                weight[edge[0]][edge[1]] = edge[2];
                weight[edge[1]][edge[0]] = edge[2];
            }
        }

        long d = dijkstra(n, weight, source, destination);
        if (d < target) {
            // a path already shorter than target exists; adding edges only shortens it
            return new int[0][];
        }

        boolean done = (d == target);
        for (int[] edge : edges) {
            if (edge[2] > 0) {
                continue;
            }
            if (done) {
                // too heavy to ever lie on a shortest path
                edge[2] = MAX_WEIGHT;
                continue;
            }
            // switch this edge on with the smallest possible weight
            edge[2] = 1;
            weight[edge[0]][edge[1]] = 1;
            weight[edge[1]][edge[0]] = 1;
            d = dijkstra(n, weight, source, destination);
            if (d <= target) {
                // inflate this edge so the shortest path is exactly target
                done = true;
                edge[2] += (int) (target - d);
                weight[edge[0]][edge[1]] = edge[2];
                weight[edge[1]][edge[0]] = edge[2];
            }
        }
        return done ? edges : new int[0][];
    }

    private static final int MAX_WEIGHT = 2_000_000_000;

    /** O(n^2) Dijkstra on an adjacency matrix; returns dist(source, destination). */
    private long dijkstra(int n, long[][] weight, int source, int destination) {
        long[] dist = new long[n];
        Arrays.fill(dist, MAX_WEIGHT);
        dist[source] = 0;
        boolean[] settled = new boolean[n];

        for (int round = 0; round < n; round++) {
            // pick the closest unsettled node
            int closest = -1;
            for (int j = 0; j < n; j++) {
                if (!settled[j] && (closest == -1 || dist[j] < dist[closest])) {
                    closest = j;
                }
            }
            settled[closest] = true;
            // relax every edge out of it
            for (int j = 0; j < n; j++) {
                if (dist[closest] + weight[closest][j] < dist[j]) {
                    dist[j] = dist[closest] + weight[closest][j];
                }
            }
        }
        return dist[destination];
    }
}
