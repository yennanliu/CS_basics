package LeetCodeJava.Graph;

// https://leetcode.com/problems/number-of-restricted-paths-from-first-to-last-node/description/

import java.util.*;

/**
 * 1786. Number of Restricted Paths From First to Last Node
 * Medium
 *
 * There is an undirected weighted connected graph. You are given a positive integer n which denotes that
 * the graph has n nodes labeled from 1 to n, and an array edges where each edges[i] = [ui, vi, weighti]
 * denotes that there is an edge between nodes ui and vi with weight equal to weighti.
 *
 * A path from node start to node end is a sequence of nodes [z0, z1, z2, ..., zk] such that z0 = start and
 * zk = end and there is an edge between zi and zi+1 where 0 <= i <= k-1.
 *
 * The distance of a path is the sum of the weights on the edges of the path. Let distanceToLastNode(x)
 * denote the shortest distance of a path between node n and node x. A restricted path is a path that also
 * satisfies that distanceToLastNode(zi) > distanceToLastNode(zi+1) where 0 <= i <= k-1.
 *
 * Return the number of restricted paths from node 1 to node n. Since that number may be too large, return
 * it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 5, edges = [[1,2,3],[1,3,3],[2,3,1],[1,4,2],[5,2,2],[3,5,1],[5,4,10]]
 * Output: 3
 * Explanation: The three restricted paths are:
 * 1) 1 --> 2 --> 5
 * 2) 1 --> 2 --> 3 --> 5
 * 3) 1 --> 3 --> 5
 *
 * Example 2:
 *
 * Input: n = 7, edges = [[1,3,1],[4,1,2],[7,3,4],[2,5,3],[5,6,1],[6,7,2],[7,5,3],[2,6,4]]
 * Output: 1
 * Explanation: The only restricted path is 1 --> 3 --> 7.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 2 * 10^4
 * n - 1 <= edges.length <= 4 * 10^4
 * edges[i].length == 3
 * 1 <= ui, vi <= n
 * ui != vi
 * 1 <= weighti <= 10^5
 * There is at most one edge between any two nodes.
 * There is at least one path between any two nodes.
 *
 */
public class NumberOfRestrictedPathsFromFirstToLastNode {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DIJKSTRA FROM NODE n, THEN DP IN ORDER OF INCREASING DISTANCE
    // dist[x] = shortest distance from x to n (undirected, so one Dijkstra from n gives all).
    // Legal moves u -> v need dist[u] > dist[v], so they form a DAG ordered by dist:
    //   ways[n] = 1, ways[u] = sum of ways[v] over neighbours v with dist[v] < dist[u].
    // Visiting nodes in increasing dist means every ways[v] read is already final.
    /**
     * time = O((N + E) log N)
     * space = O(N + E)
     */
    public int countRestrictedPaths(int n, int[][] edges) {
        // graph.get(u) = list of {v, weight}
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(new int[] {edge[1], edge[2]});
            graph.get(edge[1]).add(new int[] {edge[0], edge[2]});
        }

        // Dijkstra from n; distances fit in long (2*10^4 nodes * 10^5 weight)
        long[] dist = new long[n + 1];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[n] = 0;
        PriorityQueue<long[]> heap = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        heap.offer(new long[] {0, n});
        while (!heap.isEmpty()) {
            long[] top = heap.poll();
            int node = (int) top[1];
            // stale entry
            if (top[0] > dist[node]) {
                continue;
            }
            for (int[] next : graph.get(node)) {
                long candidate = top[0] + next[1];
                if (candidate < dist[next[0]]) {
                    dist[next[0]] = candidate;
                    heap.offer(new long[] {candidate, next[0]});
                }
            }
        }

        // nodes in increasing distance to n
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i + 1;
        }
        Arrays.sort(order, (a, b) -> Long.compare(dist[a], dist[b]));

        // ways[u] = number of restricted paths from u to n
        long[] ways = new long[n + 1];
        ways[n] = 1;
        for (int node : order) {
            if (node == n) {
                continue;
            }
            long total = 0;
            for (int[] next : graph.get(node)) {
                if (dist[next[0]] < dist[node]) {
                    total = (total + ways[next[0]]) % MOD;
                }
            }
            ways[node] = total;
        }
        return (int) ways[1];
    }
}
