package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/shortest-path-visiting-all-nodes/description/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 847. Shortest Path Visiting All Nodes
 * Hard
 *
 * You have an undirected, connected graph of n nodes labeled from 0 to n - 1. You are
 * given an array graph where graph[i] is a list of all the nodes connected with node i
 * by an edge.
 *
 * Return the length of the shortest path that visits every node. You may start and stop
 * at any node, you may revisit nodes multiple times, and you may reuse edges.
 *
 * Example 1:
 *
 * Input: graph = [[1,2,3],[0],[0],[0]]
 * Output: 4
 * Explanation: One possible path is [1,0,2,0,3]
 *
 * Example 2:
 *
 * Input: graph = [[1],[0,2,4],[1,3,4],[2],[1,2]]
 * Output: 4
 * Explanation: One possible path is [0,1,4,2,3]
 *
 * Constraints:
 *
 * n == graph.length
 * 1 <= n <= 12
 * 0 <= graph[i].length < n
 * graph[i] does not contain i.
 * If graph[a] contains b, then graph[b] contains a.
 * The input graph is always connected.
 *
 */
public class ShortestPathVisitingAllNodes {

    // V0
    // IDEA: BFS + BITMASK (multi-source, state = (node, visited set))
    /**
     *   A plain `visited node` set is NOT enough because we are allowed to REVISIT
     *   nodes. The real state is (current node, BITMASK of nodes seen so far).
     *   There are only n * 2^n <= 12 * 4096 such states, so BFS over them is cheap.
     *
     *   NOTE !!! EVERY node can be the start, so we seed the queue with all n states
     *            (i, 1 << i) at distance 0 -> the first time we pop a state whose mask
     *            is FULL, that distance IS the answer.
     *
     *   time  = O(n^2 * 2^n)
     *   space = O(n * 2^n)
     */
    public int shortestPathLength(int[][] graph) {
        int n = graph.length;
        int full = (1 << n) - 1;

        // multi-source: start from EVERY node at once
        Deque<int[]> queue = new ArrayDeque<>(); // {node, mask}
        boolean[][] visited = new boolean[n][1 << n];

        for (int i = 0; i < n; i++) {
            queue.offer(new int[] { i, 1 << i });
            visited[i][1 << i] = true;
        }

        int steps = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int t = 0; t < levelSize; t++) {
                int[] cur = queue.poll();
                int node = cur[0];
                int mask = cur[1];

                if (mask == full) {
                    return steps;
                }

                for (int nxt : graph[node]) {
                    int nmask = mask | (1 << nxt);
                    if (!visited[nxt][nmask]) {
                        visited[nxt][nmask] = true;
                        queue.offer(new int[] { nxt, nmask });
                    }
                }
            }
            steps += 1;
        }

        return -1;
    }

}
