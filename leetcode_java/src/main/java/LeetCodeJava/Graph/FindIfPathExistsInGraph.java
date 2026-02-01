package LeetCodeJava.Graph;

// https://leetcode.com/problems/find-if-path-exists-in-graph/description/

import java.util.*;

/**
 *  1971. Find if Path Exists in Graph
 * Easy
 * Topics
 * premium lock icon
 * Companies
 * There is a bi-directional graph with n vertices, where each vertex is labeled from 0 to n - 1 (inclusive). The edges in the graph are represented as a 2D integer array edges, where each edges[i] = [ui, vi] denotes a bi-directional edge between vertex ui and vertex vi. Every vertex pair is connected by at most one edge, and no vertex has an edge to itself.
 *
 * You want to determine if there is a valid path that exists from vertex source to vertex destination.
 *
 * Given edges and the integers n, source, and destination, return true if there is a valid path from source to destination, or false otherwise.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 3, edges = [[0,1],[1,2],[2,0]], source = 0, destination = 2
 * Output: true
 * Explanation: There are two paths from vertex 0 to vertex 2:
 * - 0 → 1 → 2
 * - 0 → 2
 * Example 2:
 *
 *
 * Input: n = 6, edges = [[0,1],[0,2],[3,5],[5,4],[4,3]], source = 0, destination = 5
 * Output: false
 * Explanation: There is no path from vertex 0 to vertex 5.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 2 * 105
 * 0 <= edges.length <= 2 * 105
 * edges[i].length == 2
 * 0 <= ui, vi <= n - 1
 * ui != vi
 * 0 <= source, destination <= n - 1
 * There are no duplicate edges.
 * There are no self edges.
 */
public class FindIfPathExistsInGraph {

    // V0
    // IDEA: DFS (fixed by gemini)
    /**
     * time = O(V + E)
     * space = O(V)
     */

    public boolean validPath(int n, int[][] edges, int source, int destination) {
        // 1. Quick check: If source is destination, a path exists.
        if (source == destination)
            return true;

        // 2. Build Adjacency List
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(i, new ArrayList<>());
        }
        for (int[] e : edges) {
            map.get(e[0]).add(e[1]);
            map.get(e[1]).add(e[0]);
        }

        /** NOTE !!
         *
         *  we cloud either do `mark visited` op here,
         *  or before the recursive call in dfs help func
         *  (see below in dfsPathVisitor)
         *
         *  e.g.:
         *
         *   visited[next] = true;
         */
        // 3. Use a boolean array for 'visited' for better performance than a Set
        boolean[] visited = new boolean[n];

        // Start DFS
        return dfsPathVisitor(source, destination, map, visited);
    }

    private boolean dfsPathVisitor(int node, int destination, Map<Integer, List<Integer>> map, boolean[] visited) {
        // Base case: destination reached
        if (node == destination)
            return true;

        // Mark current node as visited
        visited[node] = true;

        // Visit neighbors
        for (int next : map.get(node)) {
            if (!visited[next]) {
                // CRITICAL FIX: If a path is found through this neighbor, return true.
                // If not, DO NOT return false; keep checking other neighbors.
                /**  e.g. below is WRONG, we should keep checking other neighbors.
                 *
                 *    if (!dfsPathVisitor(next, destination, map, visited)) {
                 *                     return false;
                 *   }
                 *
                 */
                /** NOTE !!
                 *
                 *  we cloud either do `mark visited` op here,
                 *  or before the recursive call in dfs help func
                 */
                //visited[next] = true;
                if (dfsPathVisitor(next, destination, map, visited)) {
                    return true;
                }
            }
        }

        return false;
    }

    // V0-2
    // IDEA: DFS
    /**
     * time = O(V + E)
     * space = O(V)
     */

    public boolean validPath_0_2(int n, int[][] edges, int source, int destination) {
        if (source == destination) {
            return true;
        }
        
        // { node: [neighbor_1, neighbor_2, ..] }
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(i, new ArrayList<>());
        }
        for (int[] e : edges) {
            int ui = e[0];
            int vi = e[1];

            map.get(ui).add(vi);
            map.get(vi).add(ui);
        }

        // NOTE !!! visit from `source`
        return dfsHelper99(map, source, destination, new boolean[n]);
    }

    private boolean dfsHelper99(Map<Integer, List<Integer>> map, int node, int destination, boolean[] visited) {
        // edge
        if (node == destination) {
            return true;
        }

        // mark as visited
        visited[node] = true;

        // visit neighbors
        for (int next : map.get(node)) {
            if (!visited[next]) {
                // ??? early exit ?? if found a `working` path,
                //     return true directly
                if (dfsHelper99(map, next, destination, visited)) {
                    return true;
                }
            }
        }

        return false;
    }


    // V1-1
    // IDEA: BFS
    // https://leetcode.ca/2021-08-17-1971-Find-if-Path-Exists-in-Graph/
    /**
     * time = O(V + E)
     * space = O(V)
     */

    public boolean validPath_1_1(int n, int[][] edges, int start, int end) {
        Map<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();
        for (int[] edge : edges) {
            int node0 = edge[0], node1 = edge[1];
            List<Integer> list0 = map.getOrDefault(node0, new ArrayList<Integer>());
            List<Integer> list1 = map.getOrDefault(node1, new ArrayList<Integer>());
            list0.add(node1);
            list1.add(node0);
            map.put(node0, list0);
            map.put(node1, list1);
        }
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<Integer>();
        visited[start] = true;
        queue.offer(start);
        while (!queue.isEmpty()) {
            int node = queue.poll();
            if (node == end)
                return true;
            List<Integer> list = map.getOrDefault(node, new ArrayList<Integer>());
            for (int next : list) {
                if (!visited[next]) {
                    visited[next] = true;
                    queue.offer(next);
                }
            }
        }
        return false;
    }


    // V1-2
    // IDEA: UNION FIND (?
    // https://leetcode.ca/2021-08-17-1971-Find-if-Path-Exists-in-Graph/
    private int[] p;
    /**
     * time = O(E * α(V))
     * space = O(V)
     */

    public boolean validPath_1_2(int n, int[][] edges, int source, int destination) {
        p = new int[n];
        for (int i = 0; i < n; ++i) {
            p[i] = i;
        }
        for (int[] e : edges) {
            p[find(e[0])] = find(e[1]);
        }
        return find(source) == find(destination);
    }

    private int find(int x) {
        if (p[x] != x) {
            p[x] = find(p[x]);
        }
        return p[x];
    }


    // V2-1
    // IDEA: DFS
    // https://leetcode.com/problems/find-if-path-exists-in-graph/solutions/5052207/fasterlesser4-methodsdetailed-approachdf-jo0x/
    /**
     * time = O(V + E)
     * space = O(V)
     */

    public boolean validPath_2_1(int n, int[][] edges, int source, int destination) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
        }

        Set<Integer> visited = new HashSet<>();
        return dfs(source, destination, graph, visited);
    }

    private boolean dfs(int node, int destination, Map<Integer, List<Integer>> graph, Set<Integer> visited) {
        if (node == destination) {
            return true;
        }
        visited.add(node);
        for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.contains(neighbor)) {
                if (dfs(neighbor, destination, graph, visited)) {
                    return true;
                }
            }
        }
        return false;
    }


    // V2-2
    // IDEA: BFS
    // https://leetcode.com/problems/find-if-path-exists-in-graph/solutions/5052207/fasterlesser4-methodsdetailed-approachdf-jo0x/
    /**
     * time = O(V + E)
     * space = O(V)
     */

    public boolean validPath_2_2(int n, int[][] edges, int source, int destination) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
        }

        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.offer(source);
        visited.add(source);

        while (!queue.isEmpty()) {
            int node = queue.poll();
            if (node == destination) {
                return true;
            }
            List<Integer> neighbors = graph.getOrDefault(node, new ArrayList<>());
            for (int neighbor : neighbors) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }

        return false;
    }


    // V2-3
    // IDEA: UNION FIND
    // https://leetcode.com/problems/find-if-path-exists-in-graph/solutions/5052207/fasterlesser4-methodsdetailed-approachdf-jo0x/
    private int[] parent;
    private int[] rank;

    /**
     * time = O(E * α(V))


     * space = O(V)


     */


    public boolean validPath_2_3(int n, int[][] edges, int source, int destination) {
        parent = new int[n];
        rank = new int[n];

        // Initialize parent pointers and ranks
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 1;
        }

        // Union-find operations based on given edges
        for (int[] edge : edges) {
            union(edge[0], edge[1]);
        }

        // Check if source and destination belong to the same set
        return find_2_3(source) == find_2_3(destination);
    }

    private int find_2_3(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Path compression
        }
        return parent[x];
    }

    private void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX != rootY) {
            if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
        }
    }


    // V2-4
    // IDEA: GRAPH
    // https://leetcode.com/problems/find-if-path-exists-in-graph/solutions/5052207/fasterlesser4-methodsdetailed-approachdf-jo0x/
    /**
     * time = O(E * α(V))
     * space = O(V)
     */

    public boolean validPath_2_4(int n, int[][] edges, int source, int destination) {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            graph.computeIfAbsent(u, k -> new ArrayList<>()).add(v);
            graph.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
        }

        int[] distances = new int[n];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;

        PriorityQueue<int[]> priorityQueue = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        priorityQueue.offer(new int[] { 0, source });

        while (!priorityQueue.isEmpty()) {
            int[] current = priorityQueue.poll();
            int currentDistance = current[0];
            int currentNode = current[1];

            if (currentNode == destination) {
                return true;
            }

            if (currentDistance > distances[currentNode]) {
                continue;
            }

            for (int neighbor : graph.getOrDefault(currentNode, new ArrayList<>())) {
                int distance = currentDistance + 1; // Assuming unweighted graph
                if (distance < distances[neighbor]) {
                    distances[neighbor] = distance;
                    priorityQueue.offer(new int[] { distance, neighbor });
                }
            }
        }

        return false;
    }


    // V3



}
