package LeetCodeJava.DFS;

// https://leetcode.com/problems/count-the-number-of-complete-components/description

import java.util.*;

/**
 * 2685. Count the Number of Complete Components
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an integer n. There is an undirected graph with n vertices, numbered from 0 to n - 1. You are given a 2D integer array edges where edges[i] = [ai, bi] denotes that there exists an undirected edge connecting vertices ai and bi.
 *
 * Return the number of complete connected components of the graph.
 *
 * A connected component is a subgraph of a graph in which there exists a path between any two vertices, and no vertex of the subgraph shares an edge with a vertex outside of the subgraph.
 *
 * A connected component is said to be complete if there exists an edge between every pair of its vertices.
 *
 *
 *
 * Example 1:
 *
 *
 *
 * Input: n = 6, edges = [[0,1],[0,2],[1,2],[3,4]]
 * Output: 3
 * Explanation: From the picture above, one can see that all of the components of this graph are complete.
 * Example 2:
 *
 *
 *
 * Input: n = 6, edges = [[0,1],[0,2],[1,2],[3,4],[3,5]]
 * Output: 1
 * Explanation: The component containing vertices 0, 1, and 2 is complete since there is an edge between every pair of two vertices. On the other hand, the component containing vertices 3, 4, and 5 is not complete since there is no edge between vertices 4 and 5. Thus, the number of complete components in this graph is 1.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 50
 * 0 <= edges.length <= n * (n - 1) / 2
 * edges[i].length == 2
 * 0 <= ai, bi <= n - 1
 * ai != bi
 * There are no repeated edges.
 *
 *
 *
 */
public class CountTheNumberOfCompleteComponents {

    // V0
//    public int countCompleteComponents(int n, int[][] edges) {
//
//    }

    // V0-1
    // IDEA: DFS + Completeness check (fixed by gpt)
    /**
     *  Goal: return how many connected components in
     *        this graph are complete (fully connected).
     *
     */
    public int countCompleteComponents_0_1(int n, int[][] edges) {
        // Build graph
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            graph.put(i, new ArrayList<>());
        }
        for (int[] e : edges) {
            /**
             *
             * Since the graph is undirected, we add both directions:
             *
             *  - u → v
             *  - v → u
             *
             *  -----
             *
             *  example:
             *
             *  if edges = [[0,1], [1,2]],
             *  you’ll get:
             *
             *     0 → [1]
             *     1 → [0,2]
             *     2 → [1]
             */
            graph.get(e[0]).add(e[1]);
            graph.get(e[1]).add(e[0]);
        }

        /**  NOTE !!!
         *
         * -  visited[i]:
         *       keeps track of whether node i was already explored.
         *
         * - completeCount:
         *      stores how many `complete components` we find.
         */
        boolean[] visited = new boolean[n];
        int completeCount = 0;

        // Explore each component
        for (int i = 0; i < n; i++) {
            /**  NOTE !!!
             *
             *   ONLY call DFS on `not visited` node
             */
            if (!visited[i]) {

                /**
                 *   NOTE !!!
                 *
                 *  - nodes: collects all the nodes in this connected component.
                 *
                 *   - edgeCount: is an integer array of size 1
                 *               (used like a mutable integer reference,
                 *               since Java passes primitives by value).
                 *
                 *   - We start DFS at node i to fill both.
                 *
                 */
                List<Integer> nodes = new ArrayList<>();
                int[] edgeCount = new int[1]; // wrapper for reference
                dfs(i, graph, visited, nodes, edgeCount);

                /**
                 *   NOTE !!! After DFS, compute component stats
                 *
                 *
                 *   - nodeCount = how many nodes are in this component.
                 *   - actualEdges = how many undirected edges (since each was counted twice).
                 *
                 *    -> Example:
                 *      If nodes = [0, 1, 2] and edges = {0-1, 0-2, 1-2},
                 *      then:
                 *
                 *         ```
                 *         edgeCount[0] = 6  // because we counted both directions
                 *         actualEdges = 3
                 *         nodeCount = 3
                 */
                int nodeCount = nodes.size();
                // Each edge counted twice (u->v and v->u)
                int actualEdges = edgeCount[0] / 2;

                /**
                 *   NOTE !!! Check if it’s a complete component
                 *
                 *
                 *   A complete graph of k nodes has exactly:
                 *
                 *       E =  k * (k-1) / 2
                 *
                 *       edges
                 *
                 *
                 *   ->  If this component’s edge count matches that formula →
                 *       it’s a complete component, so we increment the answer.
                 *
                 */
                if (actualEdges == nodeCount * (nodeCount - 1) / 2) {
                    completeCount++;
                }
            }
        }

        return completeCount;
    }

    /**  DFS helper func
     *
     *  - Mark current node as visited.
     *  - Add it to the nodes list.
     *
     *  - For every neighbor nei:
     *     - Increase edgeCount[0] by one (we count directed edges).
     *      - If the neighbor hasn’t been visited, recurse into it.
     *
     * - > Note: Since edges are undirected, every edge is counted
     *           twice (once from each side).
     *           We’ll divide by 2 later.
     *
     */
    private void dfs(int node, Map<Integer, List<Integer>> graph, boolean[] visited,
                     List<Integer> nodes, int[] edgeCount) {
        visited[node] = true;
        nodes.add(node);
        for (int nei : graph.get(node)) {
            edgeCount[0]++; // count every edge direction
            if (!visited[nei]) {
                dfs(nei, graph, visited, nodes, edgeCount);
            }
        }
    }

    // V0-2
    // IDEA: DFS with Completeness Check (gemini)
    /**
     * Counts the number of connected components that are complete graphs.
     * A component with V vertices and E edges is complete if E == V * (V - 1) / 2.
     */
    public int countCompleteComponents_0_2(int n, int[][] edges) {
        // Edge case: If no nodes, no components.
        if (n == 0) {
            return 0;
        }

        // 1. Build the Adjacency List (Graph)
        // { node : [neighbor_1, neighbor_2, ...] }
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            graph.put(i, new ArrayList<>());
        }
        // Count total edges, since the input can contain duplicates or self-loops,
        // though typically LC problems guarantee simple graphs. We'll count edges carefully.
        int totalEdges = 0;

        // Use a Set to track edges to correctly count E for the entire graph (handling multi-edges)
        Set<String> edgeSet = new HashSet<>();

        for (int[] e : edges) {
            int u = e[0];
            int v = e[1];

            // Assuming no self-loops (u != v), which is standard for simple graphs.
            if (u != v) {
                // Add to adjacency list for traversal
                graph.get(u).add(v);
                graph.get(v).add(u);

                // Use a canonical representation for the edge (e.g., smaller_node + "_" + larger_node)
                String edgeKey = (u < v) ? u + "_" + v : v + "_" + u;
                if (edgeSet.add(edgeKey)) {
                    totalEdges++; // Count only unique edges
                }
            }
        }

        int completeComponentCount = 0;
        boolean[] visited = new boolean[n];

        // 2. Traverse Graph to Find Components
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {

                // Lists to hold the nodes (V) and edges (E) of the current component
                List<Integer> componentNodes = new ArrayList<>();
                int[] componentEdgeCount = {0}; // Use array for pass-by-reference effect

                // Use DFS to find all nodes and edges in the current component
                DFS(i, graph, visited, componentNodes, componentEdgeCount);

                // 3. Check for Completeness
                int V = componentNodes.size();
                int E = componentEdgeCount[0] / 2; // Since each edge was counted twice (u->v and v->u)

                // A component is complete if E == V * (V - 1) / 2
                long maxEdges = (long)V * (V - 1) / 2;

                if (E == maxEdges) {
                    completeComponentCount++;
                }
            }
        }

        return completeComponentCount;
    }

    /**
     * Standard DFS to find all nodes and count all edges within a connected component.
     */
    private void DFS(int u, Map<Integer, List<Integer>> graph, boolean[] visited,
                     List<Integer> nodes, int[] edgeCount) {

        visited[u] = true;
        nodes.add(u);

        // Count the degree of the current node u.
        // Summing degrees (E = sum(degree) / 2) is the easiest way to count E.
        edgeCount[0] += graph.get(u).size();

        for (int v : graph.get(u)) {
            if (!visited[v]) {
                DFS(v, graph, visited, nodes, edgeCount);
            }
        }
    }


    // V1-1
    // IDEA: DFS
    // https://leetcode.com/problems/count-the-number-of-complete-components/editorial/
    public int countCompleteComponents_1_1(int n, int[][] edges) {
        // Adjacency lists for each vertex
        List<Integer>[] graph = new ArrayList[n];

        // Initialize empty adjacency lists
        for (int vertex = 0; vertex < n; vertex++) {
            graph[vertex] = new ArrayList<>();
        }

        // Build adjacency lists from edges
        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }

        int completeCount = 0;
        Set<Integer> visited = new HashSet<>();

        // Process each unvisited vertex
        for (int vertex = 0; vertex < n; vertex++) {
            if (visited.contains(vertex))
                continue;

            // arr[0] = vertices count, arr[1] = total edges count
            int[] componentInfo = new int[2];
            dfs(vertex, graph, visited, componentInfo);

            // Check if component is complete - edges should be vertices * (vertices-1)
            if (componentInfo[0] * (componentInfo[0] - 1) == componentInfo[1]) {
                completeCount++;
            }
        }
        return completeCount;
    }

    private void dfs(
            int curr,
            List<Integer>[] graph,
            Set<Integer> visited,
            int[] componentInfo) {
        visited.add(curr);
        componentInfo[0]++; // Increment vertex count
        componentInfo[1] += graph[curr].size(); // Add edges from current vertex

        // Explore unvisited neighbors
        for (int next : graph[curr]) {
            if (!visited.contains(next)) {
                dfs(next, graph, visited, componentInfo);
            }
        }
    }


    // V1-2
    // IDEA: Adjacency List
    // https://leetcode.com/problems/count-the-number-of-complete-components/editorial/
    public int countCompleteComponents_1_2(int n, int[][] edges) {
        // Adjacency lists for each vertex
        List<Integer>[] graph = new ArrayList[n];
        // Map to store frequency of each unique adjacency list
        Map<List<Integer>, Integer> componentFreq = new HashMap<>();

        // Initialize adjacency lists with self-loops
        for (int vertex = 0; vertex < n; vertex++) {
            graph[vertex] = new ArrayList<>();
            graph[vertex].add(vertex);
        }

        // Build adjacency lists from edges
        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }

        // Count frequency of each unique adjacency pattern
        for (int vertex = 0; vertex < n; vertex++) {
            List<Integer> neighbors = graph[vertex];
            Collections.sort(neighbors);
            componentFreq.put(
                    neighbors,
                    componentFreq.getOrDefault(neighbors, 0) + 1
            );
        }

        // Count complete components where size equals frequency
        int completeCount = 0;
        for (Map.Entry<
                List<Integer>,
                Integer
                > entry : componentFreq.entrySet()) {
            if (entry.getKey().size() == entry.getValue()) {
                completeCount++;
            }
        }

        return completeCount;
    }


    // V1-3
    // IDEA: Breadth-First Search (BFS)
    // https://leetcode.com/problems/count-the-number-of-complete-components/editorial/
    public int countCompleteComponents_1_3(int n, int[][] edges) {
        // Create adjacency list representation of the graph
        List<Integer>[] graph = new ArrayList[n];
        for (int vertex = 0; vertex < n; vertex++) {
            graph[vertex] = new ArrayList<>();
        }

        // Build graph from edges
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            graph[u].add(v);
            graph[v].add(u);
        }

        boolean[] visited = new boolean[n];
        int completeComponents = 0;

        // Process each unvisited vertex
        for (int vertex = 0; vertex < n; vertex++) {
            if (!visited[vertex]) {
                // BFS to find all vertices in the current component
                List<Integer> component = new ArrayList<>();
                Queue<Integer> queue = new LinkedList<>();
                queue.add(vertex);
                visited[vertex] = true;

                while (!queue.isEmpty()) {
                    int current = queue.poll();
                    component.add(current);

                    // Process neighbors
                    for (int neighbor : graph[current]) {
                        if (!visited[neighbor]) {
                            queue.add(neighbor);
                            visited[neighbor] = true;
                        }
                    }
                }

                // Check if component is complete (all vertices have the right number of edges)
                boolean isComplete = true;
                for (int node : component) {
                    if (graph[node].size() != component.size() - 1) {
                        isComplete = false;
                        break;
                    }
                }

                if (isComplete) {
                    completeComponents++;
                }
            }
        }

        return completeComponents;
    }


    // V1-4
    // IDEA: Disjoint Set Union (Union-Find)
    // https://leetcode.com/problems/count-the-number-of-complete-components/editorial/
    public int countCompleteComponents_1_4(int n, int[][] edges) {
        // Initialize Union Find and edge counter
        UnionFind dsu = new UnionFind(n);
        Map<Integer, Integer> edgeCount = new HashMap<>();

        // Connect components using edges
        for (int[] edge : edges) {
            dsu.union(edge[0], edge[1]);
        }

        // Count edges in each component
        for (int[] edge : edges) {
            int root = dsu.find(edge[0]);
            edgeCount.put(root, edgeCount.getOrDefault(root, 0) + 1);
        }

        // Check if each component is complete
        int completeCount = 0;
        for (int vertex = 0; vertex < n; vertex++) {
            if (dsu.find(vertex) == vertex) { // If vertex is root
                int nodeCount = dsu.size[vertex];
                int expectedEdges = (nodeCount * (nodeCount - 1)) / 2;
                if (edgeCount.getOrDefault(vertex, 0) == expectedEdges) {
                    completeCount++;
                }
            }
        }
        return completeCount;
    }

    class UnionFind {

        int[] parent;
        int[] size; // Tracks size of each component

        UnionFind(int n) {
            parent = new int[n];
            size = new int[n];
            Arrays.fill(parent, -1);
            Arrays.fill(size, 1);
        }

        // Find root of component with path compression
        int find(int node) {
            if (parent[node] == -1) {
                return node;
            }
            return parent[node] = find(parent[node]);
        }

        // Union by size
        void union(int node1, int node2) {
            int root1 = find(node1);
            int root2 = find(node2);

            if (root1 == root2) {
                return;
            }

            // Merge smaller component into larger one
            if (size[root1] > size[root2]) {
                parent[root2] = root1;
                size[root1] += size[root2];
            } else {
                parent[root1] = root2;
                size[root2] += size[root1];
            }
        }
    }



}
