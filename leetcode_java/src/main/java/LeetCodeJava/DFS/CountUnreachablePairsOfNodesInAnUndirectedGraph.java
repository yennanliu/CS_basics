package LeetCodeJava.DFS;

// https://leetcode.com/problems/count-unreachable-pairs-of-nodes-in-an-undirected-graph/description/

import java.util.*;

/**
 * 2316. Count Unreachable Pairs of Nodes in an Undirected Graph
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You are given an integer n. There is an undirected graph with n nodes, numbered from 0 to n - 1. You are given a 2D integer array edges where edges[i] = [ai, bi] denotes that there exists an undirected edge connecting nodes ai and bi.
 *
 * Return the number of pairs of different nodes that are unreachable from each other.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 3, edges = [[0,1],[0,2],[1,2]]
 * Output: 0
 * Explanation: There are no pairs of nodes that are unreachable from each other. Therefore, we return 0.
 * Example 2:
 *
 *
 * Input: n = 7, edges = [[0,2],[0,5],[2,4],[1,6],[5,4]]
 * Output: 14
 * Explanation: There are 14 pairs of nodes that are unreachable from each other:
 * [[0,1],[0,3],[0,6],[1,2],[1,3],[1,4],[1,5],[2,3],[2,6],[3,4],[3,5],[3,6],[4,6],[5,6]].
 * Therefore, we return 14.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 105
 * 0 <= edges.length <= 2 * 105
 * edges[i].length == 2
 * 0 <= ai, bi < n
 * ai != bi
 * There are no repeated edges.
 *
 */
public class CountUnreachablePairsOfNodesInAnUndirectedGraph {

    // V0
//    public long countPairs(int n, int[][] edges) {
//
//    }

    // V0-1
    // IDEA: DFS (gemini)
    /**  IDEA:
     *
     * # Depth-First Search (DFS): A Comprehensive Guide
     *
     * **Depth-First Search (DFS)** is a
     * fundamental graph traversal algorithm.
     * It starts at a source node and explores as far as possible
     * along each branch before backtracking.
     *
     * Think of it like exploring a maze: you follow one path until you hit a wall,
     * then turn back to the last junction and try a different direction.
     *
     * ---
     *
     * ## 1. How It Works
     * DFS uses a **Stack** data structure
     * (either explicitly or via the call stack in recursion) t
     * o keep track of the path.
     *
     * 1.  **Mark** the current node as visited.
     * 2.  **Traverse** each unvisited neighbor of the current node.
     * 3.  **Repeat** the process recursively for each neighbor.
     * 4.  **Backtrack** when a node has no more unvisited neighbors.
     *
     *
     *
     * ---
     *
     * ## 2. Key Characteristics
     * * **Data Structure:** Stack (Recursive or Iterative).
     * * **Completeness:** DFS is "deep" before it is "wide."
     * * **Pathfinding:** It finds *a* path, but not necessarily the *shortest* path (unlike BFS).
     *
     * ---
     *
     * ## 3. Implementation (Java)
     *
     * ### A. Recursive Approach
     * This is the most common and concise way to implement DFS.
     *
     * ```java
     * public void dfs(int node, List<Integer>[] adj, boolean[] visited) {
     *     // 1. Mark the node as visited
     *     visited[node] = true;
     *     System.out.println("Visited: " + node);
     *
     *     // 2. Explore all neighbors
     *     for (int neighbor : adj[node]) {
     *         if (!visited[neighbor]) {
     *             dfs(neighbor, adj, visited);
     *         }
     *     }
     * }
     *
     *
     */
    public long countPairs_0_1(int n, int[][] edges) {
        // 1. Build Adjacency List
        List<Integer>[] adj = new ArrayList[n];
        for (int i = 0; i < n; i++)
            adj[i] = new ArrayList<>();
        for (int[] edge : edges) {
            adj[edge[0]].add(edge[1]);
            adj[edge[1]].add(edge[0]);
        }

        boolean[] visited = new boolean[n];
        long totalUnreachablePairs = 0;
        long nodesProcessed = 0;

        // 2. Traverse each component
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {

                /**
                 *  long componentSize = dfs_0_1(i, adj, visited);
                 *
                 *
                 * We launch a DFS starting at node i.
                 * This DFS will traverse every node reachable from i and mark them all as visited.
                 * The function returns the `total number` of nodes found in this
                 * specific component (let's call this S).
                 *
                 */
                long componentSize = dfs_0_1(i, adj, visited);


                /**
                 * totalUnreachablePairs += componentSize * nodesProcessed;
                 *
                 *
                 *  This is the mathematical "trick" to avoid O(N^2) calculations.
                 *
                 *    -> componentSize:
                 *        The number of nodes in the "New Island" we just found.
                 *
                 *    -> nodesProcessed:
                 *       The total number of nodes in all the "Previous Islands"
                 *       we already finished exploring.
                 *
                 *
                 *   Logic:
                 *
                 *     Every single node in the new island is
                 *     unreachable from every single node in the previous islands.
                 *     By multiplying these two numbers, we get the count of all
                 *     new unreachable pairs formed between the current island
                 *     and everything we've seen before.
                 */
                // Each node in the current component is unreachable from
                // all nodes processed in previous components.
                totalUnreachablePairs += componentSize * nodesProcessed;

                // Update the count of processed nodes
                nodesProcessed += componentSize;
            }
        }

        return totalUnreachablePairs;
    }

    private long dfs_0_1(int u, List<Integer>[] adj, boolean[] visited) {
        visited[u] = true;
        long count = 1;
        for (int v : adj[u]) {
            if (!visited[v]) {
                count += dfs_0_1(v, adj, visited);
            }
        }
        return count;
    }

    // V0-2
    // IDEA: UNION FIND (gemini)
    int[] parent_0_2;
    int[] size_0_2;

    public long countPairs_0_2(int n, int[][] edges) {
        parent_0_2 = new int[n];
        size_0_2 = new int[n];
        for (int i = 0; i < n; i++) {
            parent_0_2[i] = i;
            size_0_2[i] = 1;
        }

        for (int[] edge : edges) {
            union_0_2(edge[0], edge[1]);
        }

        long totalPairs = 0;
        long nodesProcessed = 0;

        // Iterate through all nodes to find roots and their component sizes
        for (int i = 0; i < n; i++) {
            if (parent_0_2[i] == i) { // This node is a root
                long s = size_0_2[i];
                totalPairs += s * nodesProcessed;
                nodesProcessed += s;
            }
        }
        return totalPairs;
    }

    private int find_0_2(int i) {
        if (parent_0_2[i] == i)
            return i;
        return parent_0_2[i] = find_0_2(parent_0_2[i]); // Path compression
    }

    private void union_0_2(int i, int j) {
        int rootI = find(i);
        int rootJ = find(j);
        if (rootI != rootJ) {
            // Union by size
            if (size_0_2[rootI] < size_0_2[rootJ]) {
                parent_0_2[rootI] = rootJ;
                size_0_2[rootJ] += size_0_2[rootI];
            } else {
                parent_0_2[rootJ] = rootI;
                size_0_2[rootI] += size_0_2[rootJ];
            }
        }
    }


    // V0-3
    // IDEA: UNION FIND (gpt)
    public long countPairs_0_3(int n, int[][] edges) {
        DisjointSet dsu = new DisjointSet(n);

        // Union all edges
        for (int[] e : edges) {
            dsu.union(e[0], e[1]);
        }

        // Count nodes per component
        Map<Integer, Integer> sizeMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            sizeMap.put(root, sizeMap.getOrDefault(root, 0) + 1);
        }

        long result = 0;
        long processed = 0;

        /** NOTE !!! below trick  */
        for (int size : sizeMap.values()) {
            result += size * (n - size - processed);
            processed += size;
        }

        return result;
    }

    // Union-Find structure
    private static class DisjointSet {
        int[] parent;
        int[] rank;

        public DisjointSet(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        public void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb)
                return;

            if (rank[ra] < rank[rb]) {
                parent[ra] = rb;
            } else if (rank[ra] > rank[rb]) {
                parent[rb] = ra;
            } else {
                parent[rb] = ra;
                rank[ra]++;
            }
        }
    }



    // V1
    // IDEA: DFS
    // https://leetcode.com/problems/count-unreachable-pairs-of-nodes-in-an-undirected-graph/solutions/3337487/python-java-csimple-solution-easy-to-und-mu31/
    private List<Integer>[] graph;

    public long countPairs_1(int n, int[][] edges) {
        createGraph(n, edges);
        boolean[] visited = new boolean[n];
        int numVisitedNodes = 0;
        long numUnreachablePairsOfNodes = 0;

        for (int node = 0; node < n; ++node) {
            if (!visited[node]) {
                int numNodesInCurrentGroup = depthFirstSearch_countConnectedNodesInCurrentGroup(node, visited);
                numUnreachablePairsOfNodes += (long) numNodesInCurrentGroup * numVisitedNodes;
                numVisitedNodes += numNodesInCurrentGroup;
            }
        }
        return numUnreachablePairsOfNodes;
    }

    private int depthFirstSearch_countConnectedNodesInCurrentGroup(int node, boolean[] visited) {
        visited[node] = true;
        int numConnectedNodes = 1;

        for (int neighbor : graph[node]) {
            if (!visited[neighbor]) {
                numConnectedNodes += depthFirstSearch_countConnectedNodesInCurrentGroup(neighbor, visited);
            }
        }
        return numConnectedNodes;
    }

    private void createGraph(int n, int[][] edges) {
        graph = new List[n];
        for (int node = 0; node < n; ++node) {
            graph[node] = new ArrayList<>();
        }
        for (int i = 0; i < edges.length; ++i) {
            graph[edges[i][0]].add(edges[i][1]);
            graph[edges[i][1]].add(edges[i][0]);
        }
    }


    // V2
    // IDEA: DFS
    // https://leetcode.com/problems/count-unreachable-pairs-of-nodes-in-an-undirected-graph/solutions/3337574/image-explanation-3-approaches-dfs-cjava-5csg/
    List<List<Integer>> x = new ArrayList<>();

    public long countPairs_2(int n, int[][] edges) {
        for (int i = 0; i < n; i++)
            x.add(new ArrayList<>());
        for (int[] edge : edges) {
            x.get(edge[0]).add(edge[1]);
            x.get(edge[1]).add(edge[0]);
        }

        long sum = (Long.valueOf(n) * (n - 1)) / 2;
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++)
            if (!visited[i]) {
                int cnt = dfs(i, visited, new int[1]);
                sum -= (Long.valueOf(cnt) * (cnt - 1)) / 2;
            }
        return sum;
    }

    int dfs(int node, boolean[] visited, int[] count) {
        if (visited[node])
            return count[0];
        visited[node] = true;
        count[0]++;
        for (int curr : x.get(node))
            dfs(curr, visited, count);
        return count[0];
    }

    // V3
    // IDEA: BFS
    // https://leetcode.com/problems/count-unreachable-pairs-of-nodes-in-an-undirected-graph/solutions/3337954/java-easy-bfs-by-kalinga-lb4f/
    public long countPairs_3(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int i = 0; i < edges.length; i++) {
            adj.get(edges[i][0]).add(edges[i][1]);
            adj.get(edges[i][1]).add(edges[i][0]);
        }
        long sum = n;
        long res = 0;
        boolean vis[] = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (!vis[i]) {
                vis[i] = true;
                int count = bfs(i, vis, adj, 0);
                sum -= count;
                res += (sum * count);
            }
        }
        return res;
    }

    public int bfs(int i, boolean[] vis, List<List<Integer>> adj, int count) {
        Queue<Integer> qu = new LinkedList<>();
        qu.add(i);
        count++;
        while (!qu.isEmpty()) {
            int curr = qu.poll();
            for (int adjnode : adj.get(curr)) {
                if (!vis[adjnode]) {
                    qu.add(adjnode);
                    count++;
                    vis[adjnode] = true;
                }
            }
        }
        return count;
    }

    // V4
    // IDEA: UNION FIND
    // https://leetcode.com/problems/count-unreachable-pairs-of-nodes-in-an-undirected-graph/solutions/3339777/image-explanation-from-tle-to-100-union-8x4nb/
    int[] parent;
    int[] rank;

    int find(int x) {
        while (parent[x] != x) {
            x = parent[parent[x]];
        }
        return x;
    }

    void makeUnion(int x, int y) {
        int xPar = find(x);
        int yPar = find(y);
        if (xPar == yPar) {
            return;
        } else if (rank[xPar] < rank[yPar]) {
            parent[xPar] = yPar;
        } else if (rank[xPar] > rank[yPar]) {
            parent[yPar] = xPar;
        } else {
            parent[yPar] = xPar;
            rank[xPar]++;
        }
        return;
    }

    public long countPairs_4(int n, int[][] edges) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
        for (int[] edge : edges) {
            makeUnion(edge[0], edge[1]);
        }
        long[] componentMembers = new long[n];
        for (int i = 0; i < n; i++) {
            int par = find(i);
            componentMembers[par]++;
        }
        long pairs = 0;
        long remainingMemebers = n;
        for (int i = 0; i < n; i++) {
            if (componentMembers[i] == 0) {
                continue;
            }
            long currentComponents = componentMembers[i];
            remainingMemebers -= currentComponents;
            long currentPairs = currentComponents * remainingMemebers;
            pairs += currentPairs;
        }
        return pairs;
    }



    // V5
    // IDEA: DFS
    // https://leetcode.com/problems/count-unreachable-pairs-of-nodes-in-an-undirected-graph/solutions/3337653/easy-solutions-with-exaplanation-in-java-sny5/
    public long countPairs_5(int n, int[][] edges) {
        // Build the adjacency list of the graph
        List<Integer>[] adjList = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjList[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            adjList[u].add(v);
            adjList[v].add(u);
        }

        boolean[] visited = new boolean[n];
        int[] sizes = new int[n];
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                sizes[i] = dfs(i, adjList, visited);
            }
        }

        long ans = 0;
        long sum = 0;
        for (int size : sizes) {
            ans += sum * size;
            sum += size;
        }

        return ans;
    }

    private int dfs(int u, List<Integer>[] adjList, boolean[] visited) {
        visited[u] = true;
        int size = 1;
        for (int v : adjList[u]) {
            if (!visited[v]) {
                size += dfs(v, adjList, visited);
            }
        }
        return size;
    }



}
