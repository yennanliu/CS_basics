package LeetCodeJava.DFS;

// https://leetcode.com/problems/is-graph-bipartite/description/

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

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
    // IDEA: DFS + COLOR STATE + NEIGHBOR COLOR CHECK (fixed by gpt)
    /**  NOTE !!!
     *
     *  NOT need to build another graph (via hashMap)
     *  -> since the input (int[][] graph) already a valid graph,
     *  -> we can use it in DFS directly
     */
    /**  NOTE !!!
     *
     *  we call DFS with the given graph (int[][] graph) directly
     *
     *  since for graph,
     *
     *   idx: the node
     *   int[]: the neighbor
     *
     * ----
     *
     *   e.g. given  graph = [[1,2,3],[0,2],[0,1,3],[0,2]]
     *
     *    -> node 0, has neighbor: [1,2,3]
     *    -> node 1, has neighbor: [0,2]
     *    -> node 2, has neighbor: [0,1,3]
     *    ...
     */
    public boolean isBipartite(int[][] graph) {
        // edge
        if (graph == null || graph.length == 0) {
            return true;
        }

        /**
         * colorState:
         *   0 -> not colored yet
         *   1 -> color A
         *  -1 -> color B
         */
        int[] colorState = new int[graph.length];

        // Try DFS on all components (since graph may be disconnected)
        for (int i = 0; i < graph.length; i++) {
            /**
             *
             * NOTE !!! ONLY call the dfs if `uncolored`
             * e.g. if color state = 0
             */
            if (colorState[i] == 0) { // uncolored node
                if (!dfsNeighborColorCheck(graph, colorState, i, 1)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean dfsNeighborColorCheck(int[][] graph, int[] colorState, int node, int c) {

        /**
         * // NOTE !!! here we are NOT checking neighbor color
         * // but check if the same node has conflict on color
         * // e.g. since the node colored already, we check if
         * // the new color is DIFFERENT from the prev color
         */
        // if node already colored, check consistency
        if (colorState[node] != 0) {
            return colorState[node] == c;
        }

        /** NOTE !!! we color cur node */
        // assign color
        colorState[node] = c;

        // visit all neighbors
        for (int neighbor : graph[node]) {
            // NOTE !!! since we mark cur node as `1 color`
            // so we should check if its neighbor node can has `different color`
            // e.g. `-1 * color`
            if (!dfsNeighborColorCheck(graph, colorState, neighbor, -c)) {
                return false;
            }
        }

        return true;
    }

    // V0-0-1
    // IDEA: DFS
    public boolean isBipartite_0_0_1(int[][] graph) {
        int n = graph.length;
        // 0 = uncolored
        // 1 = color A
        // -1 = color B

        int[] colors = new int[n];
        // init with color = 0
        Arrays.fill(colors, 0);

        for (int i = 0; i < n; i++) {
            // dfsColor(Map<Integer, int[]> map, Integer node, int[] colors, int color)
            /** NOTE !!!
             *
             * NOTE !!! ONLY proceed to dfs if NOT yet colored
             */
            if (colors[i] == 0) {
                if (!dfsColor(graph, i, colors, 1)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean dfsColor(int[][] graph, Integer node, int[] colors, int newColor) {

        /** NOTE !!!
         *
         * NOTE !!!  if the node already colored
         *           -> we check if there a CONFLICTION,
         *              e.g. if the cur color DOES NOT equal to the newColor
         */
        if (colors[node] != 0) {
            return colors[node] == newColor;
        }

        // color node
        colors[node] = newColor;

        // loop over neighbor
        //int[] z = graph[node];
        for (int next : graph[node]) {
            /** NOTE !!!
             *
             * NOTE !!! DON'T NEED below if logic.
             *
             *   e.g.  below is WRONG:
             *
             *    if(colors[next] != 0){....}
             *
             *    -> since what we need is coloring the `non color` node
             *       , which has color = 0,
             *       the `if logic` block that process
             */
            if (!dfsColor(graph, next, colors, -1 * newColor)) {
                return false;
            }
        }

        return true;
    }

    // V0-0-2
    // IDEA: DFS (fixed by gpt)
    public boolean isBipartite_0_0_2(int[][] graph) {
        int n = graph.length;
        // 0 = uncolored, 1 = color A, -1 = color B
        int[] color = new int[n];

        for (int i = 0; i < n; i++) {
            if (color[i] == 0) {
                // Start DFS with color 1
                if (!dfs(graph, color, i, 1)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean dfs(int[][] graph, int[] color, int node, int c) {
        if (color[node] != 0) {
            return color[node] == c;
        }

        // assign team (color) to cur node
        color[node] = c;
        /**  NOTE !!!
         *
         *  we call DFS with the given graph (int[][] graph) directly
         *
         *  since for graph,
         *
         *   idx: the node
         *   int[]: the neighbor
         *
         * ----
         *
         *   e.g. given  graph = [[1,2,3],[0,2],[0,1,3],[0,2]]
         *
         *    -> node 0, has neighbor: [1,2,3]
         *    -> node 1, has neighbor: [0,2]
         *    -> node 2, has neighbor: [0,1,3]
         *    ...
         */
        for (int neighbor : graph[node]) {
            if (!dfs(graph, color, neighbor, -c)) {
                return false;
            }
        }

        return true;
    }

    // V0-0-3
    // IDEA: DFS (fixed by gemini)
    /**
     * Determines if the graph is bipartite using DFS 2-coloring.
     * Time Complexity: O(V + E) - we visit every node and edge once.
     * Space Complexity: O(V) for the color array and recursion stack.
     */
    public boolean isBipartite_0_0_3(int[][] graph) {
        int n = graph.length;

        // 0  = uncolored
        // 1  = Color A
        // -1 = Color B
        int[] colorList = new int[n];

        // We must loop through all nodes because the graph might be disconnected.
        for (int i = 0; i < n; i++) {
            /** NOTE !!
             *
             *  ONLY color the node which is NOT colored yet,
             *  or will get stackoverflow error
             */
            // CRITICAL FIX: Only start DFS if the node hasn't been colored yet.
            if (colorList[i] == 0) {
                /** NOTE !!
                 *
                 *  ALWAYS use `1` as new color
                 *  when call dfs from node looping
                 */
                // Try to color this new component starting with color 1.
                if (!dfsColor_0_0_3(graph, i, colorList, 1)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean dfsColor_0_0_3(int[][] graph, int node, int[] colorList, int newColor) {
        // Base Case: If the node is already colored
        if (colorList[node] != 0) {
            // Check if the existing color matches the color we are trying to assign.
            // If it doesn't match, the graph is NOT bipartite.
            return colorList[node] == newColor;
        }

        // 1. Assign the new color to the current node
        colorList[node] = newColor;

        // 2. Visit all neighbors and try to color them with the OPPOSITE color.
        for (int next : graph[node]) {
            // The opposite color of 1 is -1; the opposite of -1 is 1.
            /** NOTE !!
             *
             *  should use `-1 * newColor` as the new color
             *  within DFS call
             */
            if (!dfsColor_0_0_3(graph, next, colorList, -newColor)) {
                return false;
            }
        }

        return true;
    }


    // V0-1
    // IDEA: DFS (fixed by gpt)
    /**  NOTE !!!
     *
     *   this problem is checking `whether neighbors has SAME COLOR`, but NOT check
     *   is cycled / NOT cycled (e.g. is visited ?)
     *
     *   -> e.g.: Incorrect State Model:
     *
     *           A bipartite check isn't about "visiting" vs. "visited"
     *           (which is for cycle detection in directed graphs). It's about "coloring" nodes into two
     *           groups (e.g., color $1$ and color $-1$). A graph is bipartite if you can color every
     *           node such that no two adjacent nodes have the same color.
     *
     */
    public boolean isBipartite_0_1(int[][] graph) {
        // edge
        if (graph == null || graph.length == 0) {
            return true;
        }

        /** NOTE !!!
         *
         *
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
                // 1: color A
                /** NOTE !!!
                 *
                 *  Start coloring with Color 1.
                 */
                if (!canSplit(graph, color, i, 1)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**  NOTE !!! DFS helper:
     *
     * - int[][] graph:
     *       This is the adjacency list representation of the graph.
     *       graph[node] gives you all the neighbors of node.
     * - color:
     *   •	Keeps track of the assigned color for each node.
     * 	   •	color[i] == 0 → not yet colored
     * 	   •	color[i] == 1 → one color (say, “red”)
     * 	   •	color[i] == -1 → the opposite color (say, “blue”)
     * 	   •	This array is shared across recursive calls.
     * - node: The current node we’re trying to color/check.
     * - c: The color we want to assign to this node (either 1 or -1).
     */
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
                /** NOTE !!!
                 *
                 *  Start coloring with Color 1.
                 */
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

    // V0-3
    // IDEA: BFS (gpt)
    public boolean isBipartite_0_3(int[][] graph) {
        int n = graph.length;
        int[] color = new int[n]; // 0 = uncolored, 1 = red, -1 = blue

        // The graph may have multiple disconnected components
        for (int i = 0; i < n; i++) {
            if (color[i] == 0) {
                if (!canSplitBFS(graph, color, i)) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean canSplitBFS(int[][] graph, int[] color, int start) {
        Queue<Integer> q = new LinkedList<>();
        q.offer(start);
        color[start] = 1; // assign first color (red)

        while (!q.isEmpty()) {
            int node = q.poll();

            for (int nei : graph[node]) {
                if (color[nei] == 0) {
                    // not colored yet → assign opposite color
                    color[nei] = -color[node];
                    q.offer(nei);
                } else if (color[nei] == color[node]) {
                    // conflict: adjacent nodes have the same color
                    return false;
                }
            }
        }

        return true;
    }

    // V0-4
    // IDEA: DFS (fixed by gemini)
    /**
     * Checks if the given graph is bipartite using Depth-First Search (DFS) coloring.
     * * @param graph The input graph represented as an adjacency list, where graph[i]
     * is an array of neighbors of node i.
     * @return true if the graph is bipartite, false otherwise.
     */
    public boolean isBipartite_0_4(int[][] graph) {
        if (graph == null || graph.length == 0) {
            return true;
        }

        int N = graph.length;
        // colors array:
        // 0: Uncolored (Not visited)
        // 1: Color A (Part of one set)
        // -1: Color B (Part of the other set)
        int[] colors = new int[N];
        // Arrays.fill(colors, 0); // Already initialized to 0 by Java

        // Iterate through all nodes to handle potentially disconnected components.
        for (int i = 0; i < N; i++) {
            // Only start DFS if the node has not been colored yet (meaning it's the start of a new component).
            if (colors[i] == 0) {
                // Start coloring this component with Color A (1).
                if (!dfsColor(graph, i, colors, 1)) {
                    // If the DFS returns false, a conflict was found (non-bipartite).
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Helper function to color the graph using DFS and check for conflicts.
     * @param graph The adjacency list.
     * @param node The current node being visited.
     * @param colors The coloring array.
     * @param color The color to assign to the current node (1 or -1).
     * @return true if the subgraph remains bipartite, false if a conflict is found.
     */
    private boolean dfsColor(int[][] graph, int node, int[] colors, int color) {

        // 1. Assign the color to the current node
        colors[node] = color;

        // 2. Visit all neighbors
        for (int neighbor : graph[node]) {

            // Case A: Neighbor is uncolored (0).
            if (colors[neighbor] == 0) {
                // Recursively call DFS, assigning the opposite color (-color).
                if (!dfsColor(graph, neighbor, colors, -color)) {
                    return false; // Propagate the conflict upwards
                }

                // Case B: Neighbor is already colored. Check for conflict.
            } else if (colors[neighbor] == color) {
                // Conflict: Neighbor has the SAME color as the current node.
                // This violates the bipartite definition (adjacent nodes must have different colors).
                //

                return false;
            }
            // Case C: Neighbor is correctly colored (-color). Do nothing and continue.
        }

        // All neighbors checked without conflict.
        return true;
    }

    // V0-5
    // IDEA: DFS (fixed by gpt)
    public boolean isBipartite_0_5(int[][] graph) {
        int n = graph.length;

        // 0 = uncolored
        // 1 = color A
        // -1 = color B
        int[] colors = new int[n];

        for (int i = 0; i < n; i++) {
            if (colors[i] == 0) {
                // Start DFS with color 1
                if (!dfs_0_5(graph, colors, i, 1)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean dfs_0_5(int[][] graph, int[] colors, int node, int color) {
        // If already colored
        if (colors[node] != 0) {
            return colors[node] == color; // must match expected color
        }

        // Color current node
        colors[node] = color;

        // Visit neighbors
        for (int next : graph[node]) {
            if (!dfs_0_5(graph, colors, next, -color)) {
                return false;
            }
        }

        return true;
    }

    // V0-6
    // IDEA: BFS (gemini)
    /**
     * Checks if the given graph is bipartite using Breadth-First Search (BFS) coloring.
     * @param graph The input graph represented as an adjacency list.
     * @return true if the graph is bipartite, false otherwise.
     */
    public boolean isBipartite_0_6(int[][] graph) {
        if (graph == null || graph.length == 0) {
            return true;
        }

        int N = graph.length;
        // colors array:
        // 0: Uncolored (Not visited)
        // 1: Color A
        // -1: Color B
        int[] colors = new int[N];

        // Iterate through all nodes to handle potentially disconnected components.
        for (int i = 0; i < N; i++) {
            // Only start BFS if the node has not been colored yet (i.e., it's the start of a new component).
            if (colors[i] == 0) {

                // Initialize the queue and set the starting node's color.
                Queue<Integer> queue = new LinkedList<>();
                queue.add(i);
                colors[i] = 1; // Start with Color A

                // Perform BFS on the current component
                while (!queue.isEmpty()) {
                    int u = queue.poll(); // Current node
                    int currentColor = colors[u];
                    int nextColor = -currentColor; // The required color for neighbors

                    // Visit all neighbors of u
                    for (int v : graph[u]) {

                        if (colors[v] == 0) {
                            // Case 1: Neighbor is uncolored -> Color it and add to the queue.
                            colors[v] = nextColor;
                            queue.add(v);

                        } else if (colors[v] == currentColor) {
                            // Case 2: Conflict detected! Neighbor has the SAME color as the current node.
                            //

                            return false;
                        }
                        // Case 3: Neighbor is correctly colored (colors[v] == nextColor). Do nothing.
                    }
                }
            }
        }

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
