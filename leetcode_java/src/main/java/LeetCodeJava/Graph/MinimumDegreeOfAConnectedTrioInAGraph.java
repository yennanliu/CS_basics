package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-degree-of-a-connected-trio-in-a-graph/description/

import java.util.*;

/**
 *  1761. Minimum Degree of a Connected Trio in a Graph
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an undirected graph. You are given an integer n which is the number of nodes in the graph and an array edges, where each edges[i] = [ui, vi] indicates that there is an undirected edge between ui and vi.
 *
 * A connected trio is a set of three nodes where there is an edge between every pair of them.
 *
 * The degree of a connected trio is the number of edges where one endpoint is in the trio, and the other is not.
 *
 * Return the minimum degree of a connected trio in the graph, or -1 if the graph has no connected trios.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 6, edges = [[1,2],[1,3],[3,2],[4,1],[5,2],[3,6]]
 * Output: 3
 * Explanation: There is exactly one trio, which is [1,2,3]. The edges that form its degree are bolded in the figure above.
 * Example 2:
 *
 *
 * Input: n = 7, edges = [[1,3],[4,1],[4,3],[2,5],[5,6],[6,7],[7,5],[2,6]]
 * Output: 0
 * Explanation: There are exactly three trios:
 * 1) [1,4,3] with degree 0.
 * 2) [2,5,6] with degree 2.
 * 3) [5,6,7] with degree 2.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 400
 * edges[i].length == 2
 * 1 <= edges.length <= n * (n-1) / 2
 * 1 <= ui, vi <= n
 * ui != vi
 * There are no repeated edges.
 *
 *
 *
 */
public class MinimumDegreeOfAConnectedTrioInAGraph {

    // V0
//    public int minTrioDegree(int n, int[][] edges) {
//
//    }

    // V0-1
    // IDEA: Adjacency + Enumeration (GPT)
    /** CORE IDEA:
     *
     * For every edge `(u, v)`:
     *
     * * A triangle exists if there’s a node `w` such that:
     *
     *   ```text
     *   w ∈ neighbors(u) AND w ∈ neighbors(v)
     *   ```
     * * So we just need the **intersection of neighbors**
     *
     * Then compute:
     *
     * ```text
     * degree = deg[u] + deg[v] + deg[w] - 6
     * ```
     *
     *
     */
    public int minTrioDegree_0_1(int n, int[][] edges) {
        // adjacency matrix for O(1) lookup
        boolean[][] adj = new boolean[n + 1][n + 1];
        int[] deg = new int[n + 1];

        // build graph
        for (int[] e : edges) {
            int u = e[0], v = e[1];
            adj[u][v] = true;
            adj[v][u] = true;
            deg[u]++;
            deg[v]++;
        }

        int ans = Integer.MAX_VALUE;

        // iterate over edges
        for (int[] e : edges) {
            int u = e[0], v = e[1];

            // find common neighbor
            for (int w = 1; w <= n; w++) {
                if (adj[u][w] && adj[v][w]) {
                    int degree = deg[u] + deg[v] + deg[w] - 6;
                    ans = Math.min(ans, degree);
                }
            }
        }

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }



    /** NOTE !!
     *
     *
     *   -> CAN'T do this LC with DFS
     *
     *
     *   DFS doesn’t naturally enforce
     *   “all 3 nodes are mutually connected,” and your current code:
     *
     *      doesn’t ensure 3 distinct nodes
     *      doesn’t check all 3 edges exist
     *      doesn’t compute the correct “degree” definition
     *
     */


    // V1-1
    // IDEA: Adjacency + Enumeration (GPT)
    /** NOTE !!
     *
     *  CORE:
     *
     *   -> A trio = 3 nodes where every pair is connected (a triangle)
     *
     *
     *  Key idea
     *
     *    - Build adjacency sets
     *
     *    - Try all triples (i, j, k) such that:
     *         - i < j < k
     *         - all edges exist → triangle
     *
     *    - Compute degree:
     *          ```
     *          degree = deg[i] + deg[j] + deg[k] - 6
     *          ```
     *
     * (we subtract the 3 internal edges counted twice)
     *
     *
     */
    public int minTrioDegree_1_1(int n, int[][] edges) {
        // adjacency matrix (fast lookup)
        boolean[][] connected = new boolean[n + 1][n + 1];
        int[] degree = new int[n + 1];

        for (int[] e : edges) {
            int u = e[0], v = e[1];
            connected[u][v] = true;
            connected[v][u] = true;
            degree[u]++;
            degree[v]++;
        }

        int ans = Integer.MAX_VALUE;

        // enumerate all trios
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (!connected[i][j])
                    continue;

                for (int k = j + 1; k <= n; k++) {
                    if (connected[i][k] && connected[j][k]) {
                        int trioDegree = degree[i] + degree[j] + degree[k] - 6;
                        ans = Math.min(ans, trioDegree);
                    }
                }
            }
        }

        return ans == Integer.MAX_VALUE ? -1 : ans;
    }


    // V1-2
    // IDEA: Adjacency Matrix (GEMINI)
    public int minTrioDegree_1_2(int n, int[][] edges) {
        // 1. Use Adjacency Matrix for O(1) edge lookup
        // n is up to 400, so 401x401 boolean array is fine (~160KB)
        boolean[][] isConnected = new boolean[n + 1][n + 1];
        int[] degree = new int[n + 1];

        for (int[] edge : edges) {
            isConnected[edge[0]][edge[1]] = true;
            isConnected[edge[1]][edge[0]] = true;
            degree[edge[0]]++;
            degree[edge[1]]++;
        }

        int minDegree = Integer.MAX_VALUE;

        // 2. Triple loop to find every possible Trio (u, v, w)
        // Ensure u < v < w to avoid redundant checks
        for (int u = 1; u <= n; u++) {
            for (int v = u + 1; v <= n; v++) {
                if (!isConnected[u][v])
                    continue; // Optimization: skip if u-v not connected

                for (int w = v + 1; w <= n; w++) {
                    if (isConnected[u][w] && isConnected[v][w]) {
                        // We found a trio!
                        // Calculate degree: (deg(u)-2) + (deg(v)-2) + (deg(w)-2)
                        // Simplified: deg(u) + deg(v) + deg(w) - 6
                        int currentDegree = degree[u] + degree[v] + degree[w] - 6;
                        minDegree = Math.min(minDegree, currentDegree);
                    }
                }
            }
        }

        return minDegree == Integer.MAX_VALUE ? -1 : minDegree;
    }




    // V2
    // IDEA: DP
    // https://leetcode.com/problems/minimum-degree-of-a-connected-trio-in-a-graph/solutions/1686998/java-simple-solution-faster-than-96-by-s-kkvx/
    public int minTrioDegree_2(int n, int[][] edges) {
        // to store edge information
        boolean[][] graph = new boolean[n + 1][n + 1];
        //to store inDegrees to a node
        // (NOTE: here inDegree and outDegree are same because it is Undirected graph)
        int[] inDegree = new int[n + 1];

        for (int[] edge : edges) {
            graph[edge[0]][edge[1]] = true;
            graph[edge[1]][edge[0]] = true;

            inDegree[edge[0]]++;
            inDegree[edge[1]]++;
        }

        int result = Integer.MAX_VALUE;
        for (int i = 1; i <= n; i++) {
            for (int j = i + 1; j <= n; j++) {
                if (graph[i][j]) {
                    for (int k = j + 1; k <= n; k++) {
                        if (graph[i][k] && graph[j][k]) {

                            result = Math.min(
                                    result,
                                    inDegree[i] + inDegree[j] + inDegree[k] - 6
                            );

                        }
                    }
                }
            }
        }

        return result == Integer.MAX_VALUE ? -1 : result;
    }


    // V3
    // IDEA: HASHSET, HASHMAP, GRAPH
    // https://leetcode.com/problems/minimum-degree-of-a-connected-trio-in-a-graph/solutions/1064857/my-java-solution-by-sorting-the-edge-sor-potd/
    public int minTrioDegree_3(int n, int[][] edges) {
        int minimumDegree = Integer.MAX_VALUE;

        // arrange the array edges such that if e[0] > e[1], swap e[1] and e[0]
        for (int[] e : edges) {
            if (e[0] > e[1]) {
                int temp = e[0];
                e[0] = e[1];
                e[1] = temp;
            }
        }
        // now sort the edges. 2 conditions to check
        // if e1[0] == e2[0] then sort based on e1[1] and e2[1]
        // else do the sorting based on e1[0] and e2[0]
        Arrays.sort(edges, (e1, e2) -> e1[0] == e2[0] ? e1[1] - e2[1] : e1[0] - e2[0]);

        // build the graph and we dont want to consider duplicates, so use a set to track the neighbours
        Map<Integer, Set<Integer>> graph = new HashMap<>();
        for (int[] e : edges) {
            graph.computeIfAbsent(e[0], x -> new HashSet<>()).add(e[1]);
            graph.computeIfAbsent(e[1], x -> new HashSet<>()).add(e[0]);
        }

        // again go through the edges
        // e[0] = first e[1] = second now from i = second + 1 to n -> where third = i, then check
        // if graph have third
        // if the third have neighbours first in it.
        // if the third have neighbours second in it.
        // if it holds true, then compute each of the size eg : graph.get(first).size() + secondSize + thirdSize - 6
        // then greedily consider the value for degree

        for (int[] e : edges) {
            int first = e[0];
            int second = e[1];
            for (int i = second + 1; i <= n; i++) {
                int third = i;
                if (graph.containsKey(third) && graph.get(third).contains(first) && graph.get(third).contains(second)) {
                    int currentDegree = graph.get(first).size() + graph.get(second).size() + graph.get(third).size()
                            - 6;
                    minimumDegree = Math.min(minimumDegree, currentDegree);
                }
            }
        }

        // for the last check if our minimumDegree is still Integer.MAX_Value return -1 else return minimumDegree
        return minimumDegree == Integer.MAX_VALUE ? -1 : minimumDegree;
    }



    // V4



    // V5



}
