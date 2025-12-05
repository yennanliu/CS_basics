package LeetCodeJava.Graph;

// https://leetcode.com/problems/find-edges-in-shortest-paths/description/

import java.util.*;

/**
 *  3123. Find Edges in Shortest Paths
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an undirected weighted graph of n nodes numbered from 0 to n - 1. The graph consists of m edges represented by a 2D array edges, where edges[i] = [ai, bi, wi] indicates that there is an edge between nodes ai and bi with weight wi.
 *
 * Consider all the shortest paths from node 0 to node n - 1 in the graph. You need to find a boolean array answer where answer[i] is true if the edge edges[i] is part of at least one shortest path. Otherwise, answer[i] is false.
 *
 * Return the array answer.
 *
 * Note that the graph may not be connected.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 6, edges = [[0,1,4],[0,2,1],[1,3,2],[1,4,3],[1,5,1],[2,3,1],[3,5,3],[4,5,2]]
 *
 * Output: [true,true,true,false,true,true,true,false]
 *
 * Explanation:
 *
 * The following are all the shortest paths between nodes 0 and 5:
 *
 * The path 0 -> 1 -> 5: The sum of weights is 4 + 1 = 5.
 * The path 0 -> 2 -> 3 -> 5: The sum of weights is 1 + 1 + 3 = 5.
 * The path 0 -> 2 -> 3 -> 1 -> 5: The sum of weights is 1 + 1 + 2 + 1 = 5.
 * Example 2:
 *
 *
 * Input: n = 4, edges = [[2,0,1],[0,1,1],[0,3,4],[3,2,2]]
 *
 * Output: [true,false,false,true]
 *
 * Explanation:
 *
 * There is one shortest path between nodes 0 and 3, which is the path 0 -> 2 -> 3 with the sum of weights 1 + 2 = 3.
 *
 *
 *
 * Constraints:
 *
 * 2 <= n <= 5 * 104
 * m == edges.length
 * 1 <= m <= min(5 * 104, n * (n - 1) / 2)
 * 0 <= ai, bi < n
 * ai != bi
 * 1 <= wi <= 105
 * There are no repeated edges.
 *
 */
public class FindEdgesInShortestPaths {

    // V0
//    public boolean[] findAnswer(int n, int[][] edges) {
//
//    }

    // V0-1
    // IDEA: Double Dijkstra's (fixed by gemini)
    /**
     *   IDEA:
     *
     *    Core Strategy:
     *
     *  1. Forward Dijkstra ($D_0$): Calculate the
     *    shortest distance from the source node (0) to all other nodes ($D_0[i]$).
     *
     *
     *  2. Backward Dijkstra ($D_{n-1}$): Calculate the shortest distance
     *     from the destination node ($n-1$)
     *     to all other nodes ($D_{n-1}[i]$).
     *
     *  3. Validation:
     *     An edge $(u, v)$ with weight $w$ belongs to a shortest path if and only if:
     *         $$D_0[u] + w + D_{n-1}[v] = D_0[n-1]$$
     *
     *    This formula verifies that the path length via that
     *    specific edge equals the global minimum shortest path length.
     *
     */
    /**
     * Finds all edges that belong to at least one shortest path between 0 and n-1.
     * Time Complexity: O(E log V) due to Dijkstra's algorithm.
     * Space Complexity: O(V + E)
     */
    public boolean[] findAnswer_0_1(int n, int[][] edges) {
        // --- Step 1: Build the Adjacency List (Graph) ---
        // Adjacency List: Map<Node, List<[Neighbor, Weight, Edge_Index]>>
        // We need the edge index to map back to the result array.
        List<int[]>[] adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            int w = edges[i][2];

            // Add edge from u to v
            adj[u].add(new int[] { v, w, i });
            // Add edge from v to u (undirected graph)
            adj[v].add(new int[] { u, w, i });
        }

        // --- Step 2: Run Dijkstra's Algorithm Twice ---

        // D_0: Shortest distance from node 0 to all other nodes.
        long[] distFromStart = dijkstra(n, adj, 0);

        // D_{n-1}: Shortest distance from node n-1 to all other nodes.
        // We use the reverse graph implicitly by running Dijkstra from the end node.
        long[] distToEnd = dijkstra(n, adj, n - 1);

        // If the destination is unreachable, the array is all false (which is the default).
        if (distFromStart[n - 1] == Long.MAX_VALUE) {
            return new boolean[edges.length];
        }

        long minTotalDist = distFromStart[n - 1];
        boolean[] result = new boolean[edges.length];

        // --- Step 3: Validate Edges ---
        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            int w = edges[i][2];

            // An edge (u, v) with weight w is part of a shortest path if:
            // distFromStart[u] + w + distToEnd[v] == minTotalDist
            // OR (for the reverse direction):
            // distFromStart[v] + w + distToEnd[u] == minTotalDist

            // We only need to check one direction of the edge (u->v) since the other direction
            // (v->u) is symmetric due to the undirected graph, and both checks cover all shortest paths.

            if (distFromStart[u] != Long.MAX_VALUE && distToEnd[v] != Long.MAX_VALUE) {
                if (distFromStart[u] + w + distToEnd[v] == minTotalDist) {
                    result[i] = true;
                }
            }

            // Check the reverse path (u->v is the same edge index i)
            if (distFromStart[v] != Long.MAX_VALUE && distToEnd[u] != Long.MAX_VALUE) {
                if (distFromStart[v] + w + distToEnd[u] == minTotalDist) {
                    result[i] = true;
                }
            }
        }

        return result;
    }

    /**
     * Standard Dijkstra's Algorithm implementation.
     * @param n Number of nodes
     * @param adj Adjacency list
     * @param startNode The source node for distance calculation
     * @return Array of shortest distances from startNode to all other nodes.
     */
    private long[] dijkstra(int n, List<int[]>[] adj, int startNode) {
        // Use long for distances to prevent overflow (weights up to 10^9)
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);

        // PriorityQueue stores [distance, node]
        // Prioritize nodes with the smallest distance.
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));

        dist[startNode] = 0;
        pq.offer(new long[] { 0, startNode });

        while (!pq.isEmpty()) {
            long d = pq.peek()[0];
            int u = (int) pq.poll()[1];

            // If we found a shorter path to u already, skip this entry
            if (d > dist[u]) {
                continue;
            }

            for (int[] edge : adj[u]) {
                int v = edge[0]; // neighbor
                int w = edge[2]; // weight (index 2 is weight, index 3 is edge index)

                // Correctly use the weight from the edge array (index 1 is the weight in the adj list)
                // The input edges array has weight at index 2, but the adj list stores it at index 1.
                int weightInAdj = edge[1];

                if (dist[u] + weightInAdj < dist[v]) {
                    dist[v] = dist[u] + weightInAdj;
                    pq.offer(new long[] { dist[v], v });
                }
            }
        }
        return dist;
    }


    // V1
    // IDEA:  Dijkstra's Algorithm + Set
    // https://leetcode.com/problems/find-edges-in-shortest-paths/solutions/5052485/java-dijstras-algorithm-set-by-makubex74-qg0u/
    class Edge {
        int node;
        int weight;

        Edge(int node, int weight) {
            this.node = node;
            this.weight = weight;
        }
    }

    public boolean[] findAnswer_1(int n, int[][] edges) {
        // Initialize graph as an array of lists
        List<Edge>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            graph[edge[0]].add(new Edge(edge[1], edge[2]));
            graph[edge[1]].add(new Edge(edge[0], edge[2]));
        }

        // Dijkstra's algorithm to find the shortest path distances
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[0] = 0;
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        pq.offer(new Edge(0, 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            int currentNode = current.node;
            int currentWeight = current.weight;

            if (currentWeight > dist[currentNode])
                continue;

            for (Edge neighbor : graph[currentNode]) {
                int newDist = dist[currentNode] + neighbor.weight;
                if (newDist < dist[neighbor.node]) {
                    dist[neighbor.node] = newDist;
                    pq.offer(new Edge(neighbor.node, newDist));
                }
            }
        }

        // Use a HashSet to track edges that are part of any shortest path
        Set<Long> shortestPathEdges = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(n - 1);

        while (!queue.isEmpty()) {
            int currentNode = queue.poll();
            for (Edge neighbor : graph[currentNode]) {
                if (dist[currentNode] - neighbor.weight == dist[neighbor.node]) {
                    long edgeKey = encodeEdge(neighbor.node, currentNode);
                    if (!shortestPathEdges.contains(edgeKey)) {
                        shortestPathEdges.add(edgeKey);
                        queue.add(neighbor.node);
                    }
                }
            }
        }

        // Generate the final answer
        boolean[] answer = new boolean[edges.length];
        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0], v = edges[i][1];
            long edgeForward = encodeEdge(u, v);
            long edgeBackward = encodeEdge(v, u);
            answer[i] = shortestPathEdges.contains(edgeForward) || shortestPathEdges.contains(edgeBackward);
        }

        return answer;
    }

    // Helper function to encode an edge (u, v) as a unique long key
    private long encodeEdge(int u, int v) {
        return ((long) u << 32) | (v & 0xFFFFFFFFL);
    }


    // V2
    // https://leetcode.com/problems/find-edges-in-shortest-paths/solutions/7199140/no-backtracking-just-simple-dijkstra-by-8n0ce/

    // V3
    // IDEA: Dijkstra
    // https://leetcode.com/problems/find-edges-in-shortest-paths/solutions/5060263/easy-java-solution-dijkstra-algorithm-be-csba/
    static ArrayList<int[]> graph[];

    static void addEdge(int a, int b, int wt) {
        graph[a].add(new int[] { b, wt });
        graph[b].add(new int[] { a, wt });
    }

    static int[] Dijkstra(int src, int n) {
        int ans[] = new int[n];
        int visited[] = new int[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> (a[1] - b[1]));
        Arrays.fill(ans, Integer.MAX_VALUE);
        Arrays.fill(visited, -1);

        ans[src] = 0;
        visited[src] = 1;
        pq.add(new int[] { src, 0 });

        while (pq.size() != 0) {
            int a[] = pq.remove();
            visited[a[0]] = 1;

            for (int[] x : graph[a[0]]) {
                if (visited[x[0]] != 1 && ans[x[0]] > ans[a[0]] + x[1]) {
                    ans[x[0]] = ans[a[0]] + x[1];
                    pq.add(new int[] { x[0], ans[x[0]] });
                }
            }
        }
        return ans;
    }

    public boolean[] findAnswer_3(int n, int[][] edges) {
        graph = new ArrayList[n];

        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] a : edges) {
            addEdge(a[0], a[1], a[2]);
        }

        int s1[] = Dijkstra(0, n);
        int s2[] = Dijkstra(n - 1, n);

        int d = s1[n - 1];

        int size = edges.length;
        boolean ans[] = new boolean[size];

        for (int i = 0; i < size; i++) {
            int a = edges[i][0];
            int b = edges[i][1];
            int wt = edges[i][2];

            if (s1[a] == Integer.MAX_VALUE)
                ans[i] = false;
            else if ((s1[a] + wt + s2[b]) == d || (s1[b] + wt + s2[a]) == d)
                ans[i] = true;
            else
                ans[i] = false;
        }
        return ans;
    }



}
