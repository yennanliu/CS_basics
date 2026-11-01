package LeetCodeJava.Graph;

// https://leetcode.com/problems/shortest-cycle-in-a-graph/description/

import java.util.*;

/**
 * 2608. Shortest Cycle in a Graph
 * Hard
 *
 * There is a bi-directional graph with n vertices, where each vertex is labeled from 0 to n - 1. The edges in the graph are represented by a given 2D integer array edges, where edges[i] = [ui, vi] denotes an edge between vertex ui and vertex vi. Every vertex pair is connected by at most one edge, and no vertex has an edge to itself.
 *
 * Return the length of the shortest cycle in the graph. If no cycle exists, return -1.
 *
 * A cycle is a path that starts and ends at the same node, and each edge in the path is used only once.
 *
 *
 * Example 1:
 *
 * Input: n = 7, edges = [[0,1],[1,2],[2,0],[3,4],[4,5],[5,6],[6,3]]
 * Output: 3
 * Explanation: The cycle with the smallest length is : 0 -> 1 -> 2 -> 0
 *
 * Example 2:
 *
 * Input: n = 4, edges = [[0,1],[0,2]]
 * Output: -1
 * Explanation: There are no cycles in this graph.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 1000
 * 1 <= edges.length <= 1000
 * edges[i].length == 2
 * 0 <= ui, vi < n
 * ui != vi
 * There are no repeated edges.
 *
 */
public class ShortestCycleInAGraph {

    // V0
    // IDEA: BFS FROM EVERY VERTEX (girth of an unweighted graph)
    //  - BFS from each root, remembering the parent each node was reached from
    //  - a non-tree edge (a, b) with b != parent(a) closes a cycle of length dist[a] + dist[b] + 1
    //  - that value can overestimate for one root but is exact for a root lying on the
    //    shortest cycle, so the minimum over all roots is the girth
    /**
     * time = O(n * (n + m))
     * space = O(n + m)
     */
    public int findShortestCycle(int n, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        int shortest = Integer.MAX_VALUE;
        for (int root = 0; root < n; root++) {
            int[] dist = new int[n];
            int[] parent = new int[n];
            Arrays.fill(dist, -1);
            dist[root] = 0;
            parent[root] = -1;

            Deque<Integer> queue = new ArrayDeque<>();
            queue.offer(root);
            while (!queue.isEmpty()) {
                int node = queue.poll();
                // BFS pops in non-decreasing depth; nothing deeper can beat the current best
                if ((long) dist[node] * 2 >= shortest) {
                    break;
                }
                for (int next : graph.get(node)) {
                    if (dist[next] == -1) {
                        // tree edge
                        dist[next] = dist[node] + 1;
                        parent[next] = node;
                        queue.offer(next);
                    } else if (next != parent[node]) {
                        // non-tree edge: closes a cycle through the root
                        shortest = Math.min(shortest, dist[node] + dist[next] + 1);
                    }
                }
            }
        }
        return shortest == Integer.MAX_VALUE ? -1 : shortest;
    }
}
