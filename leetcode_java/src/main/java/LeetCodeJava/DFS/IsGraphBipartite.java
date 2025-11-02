package LeetCodeJava.DFS;

// https://leetcode.com/problems/is-graph-bipartite/description/

import java.util.Arrays;

/**
 * 785. Is Graph Bipartite?
 * Solved
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * There is an undirected graph with n nodes, where each node is numbered between 0 and n - 1. You are given a 2D array graph, where graph[u] is an array of nodes that node u is adjacent to. More formally, for each v in graph[u], there is an undirected edge between node u and node v. The graph has the following properties:
 *
 * There are no self-edges (graph[u] does not contain u).
 * There are no parallel edges (graph[u] does not contain duplicate values).
 * If v is in graph[u], then u is in graph[v] (the graph is undirected).
 * The graph may not be connected, meaning there may be two nodes u and v such that there is no path between them.
 * A graph is bipartite if the nodes can be partitioned into two independent sets A and B such that every edge in the graph connects a node in set A and a node in set B.
 *
 * Return true if and only if it is bipartite.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: graph = [[1,2,3],[0,2],[0,1,3],[0,2]]
 * Output: false
 * Explanation: There is no way to partition the nodes into two independent sets such that every edge connects a node in one and a node in the other.
 * Example 2:
 *
 *
 * Input: graph = [[1,3],[0,2],[1,3],[0,2]]
 * Output: true
 * Explanation: We can partition the nodes into two sets: {0, 2} and {1, 3}.
 *
 *
 * Constraints:
 *
 * graph.length == n
 * 1 <= n <= 100
 * 0 <= graph[u].length < n
 * 0 <= graph[u][i] <= n - 1
 * graph[u] does not contain u.
 * All the values of graph[u] are unique.
 * If graph[u] contains v, then graph[v] contains u.
 *
 *
 */
public class IsGraphBipartite {

    // V0
//    public boolean isBipartite(int[][] graph) {
//
//    }

    // V1
    // IDEA: DFS
    // https://leetcode.ca/2018-01-23-785-Is-Graph-Bipartite/
    private int[] color;
    private int[][] g;

    public boolean isBipartite_1(int[][] graph) {
        int n = graph.length;
        color = new int[n];
        g = graph;
        for (int i = 0; i < n; ++i) {
            if (color[i] == 0 && !dfs(i, 1)) {
                return false;
            }
        }
        return true;
    }

    private boolean dfs(int u, int c) {
        color[u] = c;
        for (int v : g[u]) {
            if (color[v] == 0) {
                if (!dfs(v, 3 - c)) {
                    return false;
                }
            } else if (color[v] == c) {
                return false;
            }
        }
        return true;
    }

    // V2
    // https://leetcode.com/problems/is-graph-bipartite/solutions/6906965/beat-100simplestcolor-the-graph-like-a-p-sdjb/
    public boolean isBipartite_2(int[][] graph) {
        int n = graph.length;
        int[] color = new int[n];
        Arrays.fill(color, -1);

        for (int i = 0; i < n; i++) {
            if (color[i] == -1 && !dfs(i, 0, color, graph)) {
                return false;
            }
        }
        return true;
    }

    private boolean dfs(int node, int c, int[] color, int[][] graph) {
        color[node] = c;
        for (int neighbor : graph[node]) {
            if (color[neighbor] == -1) {
                if (!dfs(neighbor, 1 - c, color, graph))
                    return false;
            } else if (color[neighbor] == color[node]) {
                return false;
            }
        }
        return true;
    }


    // V3
    // IDEA: DFS
    // https://leetcode.com/problems/is-graph-bipartite/solutions/7284419/100-beat-depth-first-search-by-balrampat-54zv/
    public boolean isBipartite_3(int[][] graph) {
        int n = graph.length;
        int[] color = new int[n];
        Arrays.fill(color, -1); // -1 means uncolored

        // For disconnected graphs, check each component
        for (int i = 0; i < n; i++) {
            if (color[i] == -1) {
                if (!dfs(graph, i, 0, color)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean dfs(int[][] graph, int node, int c, int[] color) {
        color[node] = c;

        for (int neighbor : graph[node]) {
            if (color[neighbor] == -1) {
                // Color neighbor with opposite color
                if (!dfs(graph, neighbor, 1 - c, color)) {
                    return false;
                }
            } else if (color[neighbor] == color[node]) {
                // Same color neighbor → not bipartite
                return false;
            }
        }

        return true;
    }


}
