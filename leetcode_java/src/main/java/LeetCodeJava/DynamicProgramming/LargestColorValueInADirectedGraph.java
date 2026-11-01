package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/largest-color-value-in-a-directed-graph/description/

import java.util.*;

/**
 * 1857. Largest Color Value in a Directed Graph
 * Hard
 *
 * There is a directed graph of n colored nodes and m edges. The nodes are numbered from 0 to n - 1.
 *
 * You are given a string colors where colors[i] is a lowercase English letter representing the
 * color of the ith node in this graph (0-indexed). You are also given a 2D array edges where
 * edges[j] = [aj, bj] indicates that there is a directed edge from node aj to node bj.
 *
 * A valid path in the graph is a sequence of nodes x1 -> x2 -> x3 -> ... -> xk such that there is
 * a directed edge from xi to xi+1 for every 1 <= i < k. The color value of the path is the number
 * of nodes that are colored the most frequently occurring color along that path.
 *
 * Return the largest color value of any valid path in the given graph, or -1 if the graph contains a cycle.
 *
 *
 * Example 1:
 *
 * Input: colors = "abaca", edges = [[0,1],[0,2],[2,3],[3,4]]
 * Output: 3
 * Explanation: The path 0 -> 2 -> 3 -> 4 contains 3 nodes that are colored "a".
 *
 * Example 2:
 *
 * Input: colors = "a", edges = [[0,0]]
 * Output: -1
 * Explanation: There is a cycle from 0 to 0.
 *
 *
 * Constraints:
 *
 * n == colors.length
 * m == edges.length
 * 1 <= n <= 10^5
 * 0 <= m <= 10^5
 * colors consists of lowercase English letters.
 * 0 <= aj, bj < n
 *
 */
public class LargestColorValueInADirectedGraph {

    // V0
    // IDEA: TOPOLOGICAL SORT (KAHN) + DP OVER 26 COLORS
    //
    //   dp[u][c] = the max number of color-c nodes on any path ENDING at u.
    //   process nodes in topological order: when u is popped, its dp row is final,
    //   so push it forward into every child (dp[v][c] = max(dp[v][c], dp[u][c])).
    //   a node's own color is added once all its parents are done (in-degree hits 0).
    //   if fewer than n nodes are popped, the graph has a cycle -> -1.
    /**
     * time = O((N + M) * 26)
     * space = O(N * 26)
     */
    public int largestPathValue(String colors, int[][] edges) {
        int n = colors.length();
        final int ALPHABET = 26;

        // build adjacency list and in-degrees
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        int[] inDegree = new int[n];
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            inDegree[edge[1]]++;
        }

        int[][] dp = new int[n][ALPHABET];
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                // a source node's path is just itself
                dp[i][colors.charAt(i) - 'a'] = 1;
            }
        }

        int processed = 0;
        int result = 0;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            processed++;

            // dp[node] is final now: every parent has already pushed into it
            for (int c = 0; c < ALPHABET; c++) {
                result = Math.max(result, dp[node][c]);
            }

            for (int next : graph.get(node)) {
                // extend every path ending at node by the edge node -> next
                for (int c = 0; c < ALPHABET; c++) {
                    dp[next][c] = Math.max(dp[next][c], dp[node][c]);
                }
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    // all parents merged: now count next's own color
                    dp[next][colors.charAt(next) - 'a']++;
                    queue.offer(next);
                }
            }
        }

        // some node never reached in-degree 0 -> it sits on a cycle
        if (processed < n) {
            return -1;
        }
        return result;
    }
}
