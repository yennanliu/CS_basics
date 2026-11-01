package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-cost-path-with-edge-reversals/description/

import java.util.*;

/**
 * 3650. Minimum Cost Path with Edge Reversals
 * Medium
 *
 * You are given a directed, weighted graph with n nodes labeled from 0 to n -
 * 1, and an array edges where edges[i] = [ui, vi, wi] represents a directed
 * edge from node ui to node vi with cost wi.
 *
 * Each node ui has a switch that can be used at most once: when you arrive at
 * ui and have not yet used its switch, you may activate it on one of its
 * incoming edges vi → ui reverse that edge to ui → vi and immediately traverse
 * it.
 *
 * The reversal is only valid for that single move, and using a reversed edge
 * costs 2 * wi.
 *
 * Return the minimum total cost to travel from node 0 to node n - 1. If it is
 * not possible, return -1.
 *
 * Example 1:
 *
 * Input: n = 4, edges = [[0,1,3],[3,1,1],[2,3,4],[0,2,2]]
 * Output: 5
 * Explanation:
 * Use the path 0 → 1 (cost 3).
 * At node 1 reverse the original edge 3 → 1 into 1 → 3 and traverse it at cost
 * 2 * 1 = 2.
 * Total cost is 3 + 2 = 5.
 *
 * Example 2:
 *
 * Input: n = 4, edges = [[0,2,1],[2,1,1],[1,3,1],[2,3,3]]
 * Output: 3
 * Explanation:
 * No reversal is needed. Take the path 0 → 2 (cost 1), then 2 → 1 (cost 1),
 * then 1 → 3 (cost 1).
 * Total cost is 1 + 1 + 1 = 3.
 *
 * Constraints:
 *
 * 2 <= n <= 5 * 10^4
 * 1 <= edges.length <= 10^5
 * edges[i] = [ui, vi, wi]
 * 0 <= ui, vi <= n - 1
 * 1 <= wi <= 1000
 *
 */
public class MinimumCostPathWithEdgeReversals {

    // V0
    // IDEA: MODEL THE REVERSAL AS A SECOND, PRICIER EDGE + DIJKSTRA
    //  - a reversal only affects the one traversal, so it adds no state: each directed edge
    //    (u, v, w) becomes u -> v costing w and v -> u costing 2w
    //  - all costs are positive, so plain Dijkstra on the doubled edge set is the answer
    /**
     * time = O((V + E) log V)
     * space = O(V + E)
     */
    public int minCost(int n, int[][] edges) {
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(new int[] {edge[1], edge[2]});
            // walking the edge backwards = reversing it, at twice the cost
            graph.get(edge[1]).add(new int[] {edge[0], 2 * edge[2]});
        }

        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[0] = 0;

        // heap entry: {distance, node}
        PriorityQueue<long[]> heap = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        heap.offer(new long[] {0, 0});
        while (!heap.isEmpty()) {
            long[] top = heap.poll();
            long d = top[0];
            int node = (int) top[1];
            if (d > dist[node]) {
                continue;   // stale entry
            }
            if (node == n - 1) {
                return (int) d;
            }
            for (int[] next : graph.get(node)) {
                long candidate = d + next[1];
                if (candidate < dist[next[0]]) {
                    dist[next[0]] = candidate;
                    heap.offer(new long[] {candidate, next[0]});
                }
            }
        }
        return -1;
    }
}
