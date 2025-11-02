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

    // V0-1
    // IDEA: DFS (fixed by gpt)
    public boolean isBipartite_0_1(int[][] graph) {
        // edge
        if (graph == null || graph.length == 0) {
            return true;
        }

        /**
         * We use 3 states:
         *   0 -> not colored yet
         *   1 -> color A
         *  -1 -> color B
         */
        int n = graph.length;
        int[] color = new int[n];

        // Handle disconnected graph: check every component
        for (int i = 0; i < n; i++) {
            if (color[i] == 0) {
                // Try coloring this component with DFS
                if (!canSplit(graph, color, i, 1)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean canSplit(int[][] graph, int[] color, int node, int c) {
        // if already colored, check consistency
        if (color[node] != 0) {
            return color[node] == c;
        }

        // color current node
        color[node] = c;

        // DFS all neighbors
        for (int nei : graph[node]) {
            if (!canSplit(graph, color, nei, -c)) {
                return false;
            }
        }

        return true;
    }

    // V0-2
    // IDEA: DFS (fixed by gemini)
    /**
     * Checks if the graph is bipartite using graph coloring (DFS).
     */
    public boolean isBipartite_0_2(int[][] graph) {
        if (graph == null || graph.length == 0) {
            return true;
        }

        int n = graph.length;

        // --- Fix 1: Use a 'colors' array for state ---
        // 0 = Unvisited
        // 1 = Color A
        //-1 = Color B
        int[] colors = new int[n];
        // Note: Arrays.fill(colors, 0) is redundant as 0 is the default.

        // --- Fix 2: Loop through all nodes (0 to n-1) ---
        // This is necessary in case the graph is disconnected (has multiple components).
        for (int i = 0; i < n; i++) {
            // If this node hasn't been colored yet, start a new DFS.
            if (colors[i] == 0) {
                // Start coloring with Color 1.
                if (!canColor(graph, colors, i, 1)) {
                    // If the DFS finds a conflict, the graph is not bipartite.
                    return false;
                }
            }
        }

        // If all components were successfully colored, the graph is bipartite.
        return true;
    }

    /**
     * Recursive DFS helper to color the graph.
     * @param graph The adjacency list.
     * @param colors The array tracking node colors.
     * @param node The current node to visit.
     * @param color The color (1 or -1) to assign to 'node'.
     * @return true if this component is bipartite, false if a conflict is found.
     */
    private boolean canColor(int[][] graph, int[] colors, int node, int color) {
        // 1. Color the current node.
        colors[node] = color;

        // 2. Visit all its neighbors (using the input graph directly).
        for (int neighbor : graph[node]) {

            // --- Fix 3: Check neighbor's color ---
            if (colors[neighbor] == 0) {
                // Case A: Neighbor is unvisited.
                // Recursively call with the *opposite* color.
                if (!canColor(graph, colors, neighbor, -color)) {
                    // If the recursive call found a conflict, propagate failure up.
                    return false;
                }
            } else if (colors[neighbor] == color) {
                // Case B: Neighbor is already colored... and has the SAME color.
                // This is a conflict! We found an odd-length cycle.
                return false;
            }
            // Case C: (colors[neighbor] == -color)
            // Neighbor is already colored with the opposite color. This is good,
            // so we do nothing and continue the loop.
        }

        // If no conflicts were found for this node and its entire branch, return true.
        return true;
    }

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
