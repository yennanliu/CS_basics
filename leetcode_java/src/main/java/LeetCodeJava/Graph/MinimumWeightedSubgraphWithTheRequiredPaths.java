package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-weighted-subgraph-with-the-required-paths/description/

import java.util.*;

/**
 * 2203. Minimum Weighted Subgraph With the Required Paths
 * Hard
 *
 * You are given an integer n denoting the number of nodes of a weighted directed graph. The nodes are numbered from 0 to n - 1.
 *
 * You are also given a 2D integer array edges where edges[i] = [from_i, to_i, weight_i] denotes that there exists a directed edge from from_i to to_i with weight weight_i.
 *
 * Lastly, you are given three distinct integers src1, src2, and dest denoting three distinct nodes of the graph.
 *
 * Return the minimum weight of a subgraph of the graph such that it is possible to reach dest from both src1 and src2 via a set of edges of this subgraph. In case such a subgraph does not exist, return -1.
 *
 * A subgraph is a graph whose vertices and edges are subsets of the original graph. The weight of a subgraph is the sum of weights of its constituent edges.
 *
 *
 * Example 1:
 *
 * Input: n = 6, edges = [[0,2,2],[0,5,6],[1,0,3],[1,4,5],[2,1,1],[2,3,3],[2,3,4],[3,4,2],[4,5,1]], src1 = 0, src2 = 1, dest = 5
 * Output: 9
 * Explanation:
 * The above figure represents the input graph.
 * The blue edges represent one of the subgraphs that yield the optimal answer.
 * Note that the subgraph [[1,0,3],[0,5,6]] also yields the optimal answer. It is not possible to get a subgraph with less weight.
 *
 * Example 2:
 *
 * Input: n = 3, edges = [[0,1,1],[2,1,1]], src1 = 0, src2 = 1, dest = 2
 * Output: -1
 * Explanation:
 * The above figure represents the input graph.
 * It can be seen that there does not exist any path from node 1 to node 2, hence there are no subgraphs satisfying all the constraints.
 *
 *
 * Constraints:
 *
 * 3 <= n <= 10^5
 * 0 <= edges.length <= 10^5
 * edges[i].length == 3
 * 0 <= from_i, to_i, src1, src2, dest <= n - 1
 * from_i != to_i
 * src1, src2, and dest are pairwise distinct.
 * 1 <= weight_i <= 10^5
 *
 */
public class MinimumWeightedSubgraphWithTheRequiredPaths {

    // V0
    // IDEA: THREE DIJKSTRAS + ONE MEETING POINT (the optimal subgraph is a "Y")
    //  - the two paths src1 -> m and src2 -> m merge at some node m, then share m -> dest
    //  - answer = min over m of distFrom(src1)[m] + distFrom(src2)[m] + distTo(dest)[m]
    //  - distTo(dest) is Dijkstra from dest on the REVERSED graph
    /**
     * time = O((V + E) log V)
     * space = O(V + E)
     */
    public long minimumWeight(int n, int[][] edges, int src1, int src2, int dest) {
        // graph: node -> list of {neighbour, weight}; reversed keeps every edge flipped
        List<List<int[]>> graph = new ArrayList<>();
        List<List<int[]>> reversed = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
            reversed.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(new int[] {edge[1], edge[2]});
            reversed.get(edge[1]).add(new int[] {edge[0], edge[2]});
        }

        long[] fromSrc1 = dijkstra(n, src1, graph);
        long[] fromSrc2 = dijkstra(n, src2, graph);
        long[] toDest = dijkstra(n, dest, reversed);

        long best = INF;
        for (int meet = 0; meet < n; meet++) {
            // skip meeting points that one of the three paths cannot use
            if (fromSrc1[meet] == INF || fromSrc2[meet] == INF || toDest[meet] == INF) {
                continue;
            }
            best = Math.min(best, fromSrc1[meet] + fromSrc2[meet] + toDest[meet]);
        }
        return best == INF ? -1 : best;
    }

    private static final long INF = Long.MAX_VALUE;

    /** Standard Dijkstra; returns the shortest distance from source to every node (INF = unreachable). */
    private long[] dijkstra(int n, int source, List<List<int[]>> graph) {
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[source] = 0;

        // heap entry: {distance, node}
        PriorityQueue<long[]> heap = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        heap.offer(new long[] {0, source});
        while (!heap.isEmpty()) {
            long[] top = heap.poll();
            long d = top[0];
            int node = (int) top[1];
            // stale entry: a shorter distance was already settled
            if (d > dist[node]) {
                continue;
            }
            for (int[] next : graph.get(node)) {
                long candidate = d + next[1];
                if (candidate < dist[next[0]]) {
                    dist[next[0]] = candidate;
                    heap.offer(new long[] {candidate, next[0]});
                }
            }
        }
        return dist;
    }
}
