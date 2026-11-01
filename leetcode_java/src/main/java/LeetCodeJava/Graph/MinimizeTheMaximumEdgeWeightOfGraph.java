package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimize-the-maximum-edge-weight-of-graph/description/

import java.util.*;

/**
 * 3419. Minimize the Maximum Edge Weight of Graph
 * Medium
 *
 * You are given two integers, n and threshold, as well as a directed weighted
 * graph of n nodes numbered from 0 to n - 1. The graph is represented by a 2D
 * integer array edges, where edges[i] = [A_i, B_i, W_i] indicates that there is an
 * edge going from node A_i to node B_i with weight W_i.
 *
 * You have to remove some edges from this graph (possibly none), so that it
 * satisfies the following conditions:
 *
 * Node 0 must be reachable from all other nodes.
 * The maximum edge weight in the resulting graph is minimized.
 * Each node has at most threshold outgoing edges.
 *
 * Return the minimum possible value of the maximum edge weight after removing the
 * necessary edges. If it is impossible for all conditions to be satisfied, return
 * -1.
 *
 * Example 1:
 *
 * Input: n = 5, edges = [[1,0,1],[2,0,2],[3,0,1],[4,3,1],[2,1,1]], threshold = 2
 *
 * Output: 1
 *
 * Explanation:
 *
 * Remove the edge 2 -> 0. The maximum weight among the remaining edges is 1.
 *
 * Example 2:
 *
 * Input: n = 5, edges = [[0,1,1],[0,2,2],[0,3,1],[0,4,1],[1,2,1],[1,4,1]], threshold = 1
 *
 * Output: -1
 *
 * Explanation:
 *
 * It is impossible to reach node 0 from node 2.
 *
 * Example 3:
 *
 * Input: n = 5, edges = [[1,2,1],[1,3,3],[1,4,5],[2,3,2],[3,4,2],[4,0,1]], threshold = 1
 *
 * Output: 2
 *
 * Explanation:
 *
 * Remove the edges 1 -> 3 and 1 -> 4. The maximum weight among the remaining edges
 * is 2.
 *
 * Example 4:
 *
 * Input: n = 5, edges = [[1,2,1],[1,3,3],[1,4,5],[2,3,2],[4,0,1]], threshold = 1
 *
 * Output: -1
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 * 1 <= threshold <= n - 1
 * 1 <= edges.length <= min(10^5, n * (n - 1) / 2).
 * edges[i].length == 3
 * 0 <= A_i, B_i < n
 * A_i != B_i
 * 1 <= W_i <= 10^6
 * There may be multiple edges between a pair of nodes, but they must have unique
 * weights.
 *
 */
public class MinimizeTheMaximumEdgeWeightOfGraph {

    // V0
    // IDEA: REVERSE THE EDGES, THEN A MINIMAX (BOTTLENECK) DIJKSTRA FROM 0
    //  - the out-degree cap never binds: keeping one outgoing edge per node along its path
    //    to 0 leaves out-degree 1 <= threshold
    //  - "0 reachable from v" = "v reachable from 0" in the reversed graph; the cost of v is
    //    the smallest possible largest edge on a 0 -> v route (bottleneck distance)
    //  - Dijkstra works with relaxation max(dist[u], w) instead of a sum
    //  - answer = worst bottleneck over all nodes, or -1 if any node is unreachable
    /**
     * time = O((V + E) log V)
     * space = O(V + E)
     */
    public int minMaxWeight(int n, int[][] edges, int threshold) {
        // reversed graph: edge a -> b with weight w is stored as b -> a
        List<List<int[]>> reversed = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            reversed.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            reversed.get(edge[1]).add(new int[] {edge[0], edge[2]});
        }

        int[] bottleneck = new int[n];
        Arrays.fill(bottleneck, Integer.MAX_VALUE);
        bottleneck[0] = 0;

        // heap entry: {bottleneck, node}
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        heap.offer(new int[] {0, 0});
        int settledCount = 0;
        int answer = 0;
        while (!heap.isEmpty()) {
            int[] top = heap.poll();
            int d = top[0];
            int node = top[1];
            if (d > bottleneck[node]) {
                continue;   // stale entry
            }
            settledCount++;
            answer = Math.max(answer, d);
            for (int[] next : reversed.get(node)) {
                // the path's bottleneck is its largest edge
                int candidate = Math.max(d, next[1]);
                if (candidate < bottleneck[next[0]]) {
                    bottleneck[next[0]] = candidate;
                    heap.offer(new int[] {candidate, next[0]});
                }
            }
        }
        return settledCount == n ? answer : -1;
    }
}
