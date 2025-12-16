package LeetCodeJava.DFS;

// https://leetcode.com/problems/reorder-routes-to-make-all-paths-lead-to-the-city-zero/description/

import java.util.*;

/**
 * 1466. Reorder Routes to Make All Paths Lead to the City Zero
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There are n cities numbered from 0 to n - 1 and n - 1 roads such that there is only one way to travel between two different cities (this network form a tree). Last year, The ministry of transport decided to orient the roads in one direction because they are too narrow.
 *
 * Roads are represented by connections where connections[i] = [ai, bi] represents a road from city ai to city bi.
 *
 * This year, there will be a big event in the capital (city 0), and many people want to travel to this city.
 *
 * Your task consists of reorienting some roads such that each city can visit the city 0. Return the minimum number of edges changed.
 *
 * It's guaranteed that each city can reach city 0 after reorder.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 6, connections = [[0,1],[1,3],[2,3],[4,0],[4,5]]
 * Output: 3
 * Explanation: Change the direction of edges show in red such that each node can reach the node 0 (capital).
 * Example 2:
 *
 *
 * Input: n = 5, connections = [[1,0],[1,2],[3,2],[3,4]]
 * Output: 2
 * Explanation: Change the direction of edges show in red such that each node can reach the node 0 (capital).
 * Example 3:
 *
 * Input: n = 3, connections = [[1,0],[2,0]]
 * Output: 0
 *
 *
 * Constraints:
 *
 * 2 <= n <= 5 * 104
 * connections.length == n - 1
 * connections[i].length == 2
 * 0 <= ai, bi <= n - 1
 * ai != bi
 *
 */
public class ReorderRoutesToMakeAllPathsLeadToTheCityZero {

    // V0
//    public int minReorder(int n, int[][] connections) {
//
//    }

    // V0-1
    // IDEA: DFS (fixed by gemini)
    // Global counter for the minimum number of reorders required
    /**
     * The fix uses DFS to traverse the graph starting from city 0,
     * treating it as an undirected graph, and checking the direction
     * of each edge as it is traversed.
     *
     *
     * ----
     *
     * Core Strategy (DFS on Undirected Graph)
     *
     * 1. Build Adjacency List (Undirected):
     *    Create a map where each key $i$ holds a list
     *    of its neighbors. Store a Pair or a custom object
     *    for each edge indicating the neighbor and the original direction of the road
     *    (e.g., $1$ for $i \to \text{neighbor}$, $-1$ for $i \leftarrow \text{neighbor}$).
     *
     *
     *  2.  Traverse from 0: Start a DFS from city 0.
     *
     *  3. Count Reversals:
     *
     *     When moving from the current city to the neighbor:
     *
     *      - If the original direction was current -> neighbor
     *         (i.e., you are traveling against the required path to 0),
     *         you must reverse this edge. Increment the count.
     *
     *     - If the original direction was current <- neighbor
     *        no reversal is needed.
     *
     *
     */
    private int reorderCount = 0;

    /**
     * Finds the minimum number of roads to reorder so all paths lead to city 0.
     * Time Complexity: O(N + E), where N is cities, E is connections.
     * Space Complexity: O(N + E) for the adjacency list and visited array.
     */
    public int minReorder_0_1(int n, int[][] connections) {

        // --- 1. Build Adjacency List (Undirected with Direction Info) ---
        // Map: city -> List of [neighbor, direction_flag]
        // direction_flag: 1 if city -> neighbor (original direction)
        // direction_flag: 0 if city <- neighbor (reverse direction)
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int i = 0; i < n; i++) {
            adj.put(i, new ArrayList<>());
        }

        for (int[] c : connections) {
            int u = c[0]; // Start city
            int v = c[1]; // End city

            // Road u -> v (Original direction, requires reversal for path v -> u)
            adj.get(u).add(new int[] { v, 1 }); // [neighbor, flag=1 (outbound)]

            // Road v <- u (Treat as undirected for traversal)
            adj.get(v).add(new int[] { u, 0 }); // [neighbor, flag=0 (inbound)]
        }

        boolean[] visited = new boolean[n];
        this.reorderCount = 0; // Reset counter

        // --- 2. Start DFS from city 0 ---
        dfsTraverse(0, adj, visited);

        return reorderCount;
    }

    /**
     * DFS function to traverse the graph and count necessary reorders.
     * @param current The current city node.
     * @param adj The adjacency list.
     * @param visited The visited array.
     */
    private void dfsTraverse(int current, Map<Integer, List<int[]>> adj, boolean[] visited) {
        visited[current] = true;

        for (int[] edge : adj.get(current)) {
            int neighbor = edge[0];
            int directionFlag = edge[1]; // 1 for current -> neighbor, 0 for current <- neighbor

            if (!visited[neighbor]) {

                // If the original road was CURRENT -> NEIGHBOR (flag == 1),
                // it means we are trying to use an edge that points AWAY from city 0
                // (since we are traversing away from 0).
                // To make the path lead TO 0, this edge must be reversed.
                if (directionFlag == 1) {
                    this.reorderCount++;
                }

                // Continue DFS traversal
                dfsTraverse(neighbor, adj, visited);
            }
        }
    }


    // V0-2
    // IDEA: BFS (gemini)
    /**
     * Finds the minimum number of roads to reorder so all paths lead to city 0 using BFS.
     * Time Complexity: O(N + E), where N is cities, E is connections.
     * Space Complexity: O(N + E) for the adjacency list, queue, and visited array.
     */
    public int minReorder_0_2(int n, int[][] connections) {

        // --- 1. Build Adjacency List (Undirected with Direction Info) ---
        // Map: city -> List of [neighbor, direction_flag]
        // flag = 1: Original road was city -> neighbor (Outbound from city)
        // flag = 0: Original road was city <- neighbor (Inbound to city)
        Map<Integer, List<int[]>> adj = new HashMap<>();
        for (int i = 0; i < n; i++) {
            adj.put(i, new ArrayList<>());
        }

        for (int[] c : connections) {
            int u = c[0]; // Start city
            int v = c[1]; // End city

            // Road u -> v (Original direction)
            adj.get(u).add(new int[] { v, 1 }); // [neighbor, flag=1 (outbound from u)]

            // Road v <- u (Undirected link for traversal)
            adj.get(v).add(new int[] { u, 0 }); // [neighbor, flag=0 (inbound to v)]
        }

        int reorderCount = 0;
        boolean[] visited = new boolean[n];
        Queue<Integer> q = new LinkedList<>();

        // --- 2. Start BFS from city 0 ---
        q.add(0);
        visited[0] = true;

        while (!q.isEmpty()) {
            int current = q.poll();

            // Check all neighbors of the current city
            for (int[] edge : adj.get(current)) {
                int neighbor = edge[0];
                int directionFlag = edge[1];

                if (!visited[neighbor]) {

                    // The core logic is the same as in DFS: we are traversing from the center (0) outwards.
                    // We only care about the original direction of the edge we just crossed.

                    // If the original road was CURRENT -> NEIGHBOR (flag == 1),
                    // this road is pointing AWAY from the path leading to city 0.
                    // It must be reversed to point towards 0.
                    if (directionFlag == 1) {
                        reorderCount++;
                    }

                    // Mark as visited and enqueue for the next level
                    visited[neighbor] = true;
                    q.add(neighbor);
                }
            }
        }

        return reorderCount;
    }


    // V1-1
    // IDEA: DFS
    // https://leetcode.com/problems/reorder-routes-to-make-all-paths-lead-to-the-city-zero/editorial/
    int count = 0;

    public void dfs(int node, int parent, Map<Integer, List<List<Integer>>> adj) {
        if (!adj.containsKey(node)) {
            return;
        }
        for (List<Integer> nei : adj.get(node)) {
            int neighbor = nei.get(0);
            int sign = nei.get(1);
            if (neighbor != parent) {
                count += sign;
                dfs(neighbor, node, adj);
            }
        }
    }

    public int minReorder_1_1(int n, int[][] connections) {
        Map<Integer, List<List<Integer>>> adj = new HashMap<>();
        for (int[] connection : connections) {
            adj.computeIfAbsent(connection[0], k -> new ArrayList<List<Integer>>()).add(
                    Arrays.asList(connection[1], 1));
            adj.computeIfAbsent(connection[1], k -> new ArrayList<List<Integer>>()).add(
                    Arrays.asList(connection[0], 0));
        }
        dfs(0, -1, adj);
        return count;
    }


    // V1-2
    // IDEA: BFS
    // https://leetcode.com/problems/reorder-routes-to-make-all-paths-lead-to-the-city-zero/editorial/
    int count_1_2 = 0;

    public void bfs(int node, int n, Map<Integer, List<List<Integer>>> adj) {
        Queue<Integer> q = new LinkedList<>();
        boolean[] visit = new boolean[n];
        q.offer(node);
        visit[node] = true;

        while (!q.isEmpty()) {
            node = q.poll();
            if (!adj.containsKey(node)) {
                continue;
            }
            for (List<Integer> nei : adj.get(node)) {
                int neighbor = nei.get(0);
                int sign = nei.get(1);
                if (!visit[neighbor]) {
                    count_1_2 += sign;
                    visit[neighbor] = true;
                    q.offer(neighbor);
                }
            }
        }
    }

    public int minReorder_1_2(int n, int[][] connections) {
        Map<Integer, List<List<Integer>>> adj = new HashMap<>();
        for (int[] connection : connections) {
            adj.computeIfAbsent(connection[0], k -> new ArrayList<List<Integer>>()).add(
                    Arrays.asList(connection[1], 1));
            adj.computeIfAbsent(connection[1], k -> new ArrayList<List<Integer>>()).add(
                    Arrays.asList(connection[0], 0));
        }
        bfs(0, n, adj);
        return count_1_2;
    }


    // V2
    // IDEA: DFS
    // https://leetcode.ca/2019-12-05-1466-Reorder-Routes-to-Make-All-Paths-Lead-to-the-City-Zero/
    private List<int[]>[] g;

    public int minReorder_2(int n, int[][] connections) {
        g = new List[n];
        Arrays.setAll(g, k -> new ArrayList<>());
        for (int[] e : connections) {
            int a = e[0], b = e[1];
            g[a].add(new int[] {b, 1});
            g[b].add(new int[] {a, 0});
        }
        return dfs(0, -1);
    }

    private int dfs(int a, int fa) {
        int ans = 0;
        for (int[] e : g[a]) {
            int b = e[0], c = e[1];
            if (b != fa) {
                ans += c + dfs(b, a);
            }
        }
        return ans;
    }


    // V3


}
