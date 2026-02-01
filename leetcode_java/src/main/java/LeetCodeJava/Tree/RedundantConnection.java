package LeetCodeJava.Tree;

// https://leetcode.com/problems/redundant-connection/description/
// https://leetcode.cn/problems/redundant-connection/
// https://leetcode.ca/all/684.html

/**
 *  684. Redundant Connection
 * Solved
 * Medium
 * Topics
 * Companies
 * In this problem, a tree is an undirected graph that is connected and has no cycles.
 *
 * You are given a graph that started as a tree with n nodes labeled from 1 to n, with one additional edge added. The added edge has two different vertices chosen from 1 to n, and was not an edge that already existed. The graph is represented as an array edges of length n where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the graph.
 *
 * Return an edge that can be removed so that the resulting graph is a tree of n nodes. If there are multiple answers, return the answer that occurs last in the input.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: edges = [[1,2],[1,3],[2,3]]
 * Output: [2,3]
 * Example 2:
 *
 *
 * Input: edges = [[1,2],[2,3],[3,4],[1,4],[1,5]]
 * Output: [1,4]
 *
 *
 * Constraints:
 *
 * n == edges.length
 * 3 <= n <= 1000
 * edges[i].length == 2
 * 1 <= ai < bi <= edges.length
 * ai != bi
 * There are no repeated edges.
 * The given graph is connected.
 *
 */
import java.util.*;

public class RedundantConnection {

    // V0
    // IDEA: UNION FIND (fixed by gpt)
    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findRedundantConnection(int[][] edges) {

        // Initialize union-find data structure
        MyUF3 uf = new MyUF3(edges);

        // Union operation for each edge
        for (int[] e : edges) {
            int x = e[0];
            int y = e[1];
            // If they are already connected, we found the redundant edge
            if (!uf.union(x, y)) {
                return e; // The redundant edge is the one that causes the cycle
            }
        }

        return null; // No redundant edge found
    }

    public class MyUF3 {
        // Union-find data structure
        int[] parents;
        int[] size;
        int n;

        // Constructor to initialize the union-find data structure
        /**
         * time = O(N)
         * space = O(H)
         */
        public MyUF3(int[][] edges) {
            HashSet<Integer> set = new HashSet<>();
            for (int[] x : edges) {
                set.add(x[0]);
                set.add(x[1]);
            }
            this.n = set.size();

            // Initialize parent and size arrays
            this.parents = new int[n + 1]; // Using 1-based indexing
            this.size = new int[n + 1];
            for (int i = 1; i <= n; i++) {
                this.parents[i] = i;
                this.size[i] = 1;
            }
        }

        // Find the root of the set containing 'x' with path compression
        /**
         * time = O(N)
         * space = O(H)
         */
        public int getParent(int x) {
            /**
             * NOTE !!!
             *
             *  we use `!=` logic below to simplify code
             */
            if (x != this.parents[x]) {
                // Path compression: recursively find the parent and update the current node's
                // parent
                /**
                 *  NOTE !!!
                 *
                 *  we should update parent as `getParent(this.parents[x])`,
                 *  e.g. -> use `this.parents[x]` as parameter, send into getParent method,
                 *       -> then assign result to this.parents[x]
                 *
                 */
                this.parents[x] = getParent(this.parents[x]);
            }
            return this.parents[x];
        }

        // Union the sets containing x and y, return false if they are already connected
        /**
         * time = O(N)
         * space = O(H)
         */
        public boolean union(int x, int y) {
            int rootX = getParent(x);
            int rootY = getParent(y);

            // If they are already in the same set, a cycle is detected
            if (rootX == rootY) {
                return false;
            }

            // Union by size: attach the smaller tree to the root of the larger tree
            if (size[rootX] < size[rootY]) {
                parents[rootX] = rootY;
                size[rootY] += size[rootX];
            } else {
                parents[rootY] = rootX;
                size[rootX] += size[rootY];
            }
            return true;
        }
    }

    // V1-1
    // https://neetcode.io/problems/redundant-connection
    // IDEA: Cycle Detection (DFS)
    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findRedundantConnection_1_1(int[][] edges) {
        int n = edges.length;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            adj.add(new ArrayList<>());
        }

        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            adj.get(u).add(v);
            adj.get(v).add(u);
            boolean[] visit = new boolean[n + 1];

            if (dfs(u, -1, adj, visit)) {
                return edge;
            }
        }
        return new int[0];
    }

    private boolean dfs(int node, int parent,
                        List<List<Integer>> adj, boolean[] visit) {
        if (visit[node]) {
            return true;
        }

        visit[node] = true;
        for (int nei : adj.get(node)) {
            if (nei == parent) {
                continue;
            }
            if (dfs(nei, node, adj, visit)) {
                return true;
            }
        }
        return false;
    }

    // V1-2
    // https://neetcode.io/problems/redundant-connection
    // IDEA: Depth First Search (Optimal)
    private boolean[] visit;
    private List<List<Integer>> adj;
    private Set<Integer> cycle;
    private int cycleStart;

    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findRedundantConnection_1_2(int[][] edges) {
        int n = edges.length;
        adj = new ArrayList<>();
        for (int i = 0; i <= n; i++)
            adj.add(new ArrayList<>());

        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            adj.get(u).add(v);
            adj.get(v).add(u);
        }

        visit = new boolean[n + 1];
        cycle = new HashSet<>();
        cycleStart = -1;
        dfs(1, -1);

        for (int i = edges.length - 1; i >= 0; i--) {
            int u = edges[i][0], v = edges[i][1];
            if (cycle.contains(u) && cycle.contains(v)) {
                return new int[]{u, v};
            }
        }
        return new int[0];
    }

    private boolean dfs(int node, int par) {
        if (visit[node]) {
            cycleStart = node;
            return true;
        }
        visit[node] = true;
        for (int nei : adj.get(node)) {
            if (nei == par) continue;
            if (dfs(nei, node)) {
                if (cycleStart != -1) cycle.add(node);
                if (node == cycleStart) {
                    cycleStart = -1;
                }
                return true;
            }
        }
        return false;
    }

    // V1-3
    // https://neetcode.io/problems/redundant-connection
    // IDEA:  Topological Sort (Kahn's Algorithm)
    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findRedundantConnection_1_3(int[][] edges) {
        int n = edges.length;
        int[] indegree = new int[n + 1];
        List<List<Integer>> adj = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) adj.add(new ArrayList<>());
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            adj.get(u).add(v);
            adj.get(v).add(u);
            indegree[u]++;
            indegree[v]++;
        }

        Queue<Integer> q = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            if (indegree[i] == 1) q.offer(i);
        }

        while (!q.isEmpty()) {
            int node = q.poll();
            indegree[node]--;
            for (int nei : adj.get(node)) {
                indegree[nei]--;
                if (indegree[nei] == 1) q.offer(nei);
            }
        }

        for (int i = edges.length - 1; i >= 0; i--) {
            int u = edges[i][0], v = edges[i][1];
            if (indegree[u] == 2 && indegree[v] > 0)
                return new int[]{u, v};
        }
        return new int[0];
    }

    // V1-4
    // https://neetcode.io/problems/redundant-connection
    // IDEA:  Disjoint Set Union
    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findRedundantConnection_1_4(int[][] edges) {
        int[] par = new int[edges.length + 1];
        int[] rank = new int[edges.length + 1];
        for (int i = 0; i < par.length; i++) {
            par[i] = i;
            rank[i] = 1;
        }

        for (int[] edge : edges) {
            if (!union(par, rank, edge[0], edge[1]))
                return new int[]{edge[0], edge[1]};
        }
        return new int[0];
    }

    private int find(int[] par, int n) {
        int p = par[n];
        while (p != par[p]) {
            par[p] = par[par[p]];
            p = par[p];
        }
        return p;
    }

    private boolean union(int[] par, int[] rank, int n1, int n2) {
        int p1 = find(par, n1);
        int p2 = find(par, n2);

        if (p1 == p2)
            return false;
        if (rank[p1] > rank[p2]) {
            par[p2] = p1;
            rank[p1] += rank[p2];
        } else {
            par[p1] = p2;
            rank[p2] += rank[p1];
        }
        return true;
    }

    // V2
    // https://www.youtube.com/watch?v=FXWRE67PLL0
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0684-redundant-connection.java
    int[] parent;

    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findRedundantConnection_2(int[][] edges) {
        parent = new int[edges.length];
        for (int i = 0; i < edges.length; i++) {
            parent[i] = i + 1;
        }

        for (int[] edge : edges) {
            if (find(edge[0]) == find(edge[1])) {
                return edge;
            } else {
                union(edge[0], edge[1]);
            }
        }

        return new int[2];
    }

    /**
     * time = O(N)
     * space = O(H)
     */
    public int find(int x) {
        if (x == parent[x - 1]) return x;
        return find(parent[x - 1]);
    }

    /**
     * time = O(N)
     * space = O(H)
     */
    public void union(int x, int y) {
        parent[find(y) - 1] = find(x);
    }

    // V3
    // IDEA : DFS
    // https://leetcode.com/problems/redundant-connection/editorial/
    Set<Integer> seen = new HashSet();
    int MAX_EDGE_VAL = 1000;

    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findRedundantConnection_3(int[][] edges) {
        ArrayList<Integer>[] graph = new ArrayList[MAX_EDGE_VAL + 1];
        for (int i = 0; i <= MAX_EDGE_VAL; i++) {
            graph[i] = new ArrayList();
        }

        for (int[] edge : edges) {
            // NOTE !!! clear seen before call dfs
            seen.clear();
            if (!graph[edge[0]].isEmpty() && !graph[edge[1]].isEmpty() && dfs(graph, edge[0], edge[1])) {
                return edge;
            }
            graph[edge[0]].add(edge[1]);
            graph[edge[1]].add(edge[0]);
        }
        throw new AssertionError();
    }

    /**
     * time = O(V + E)
     * space = O(V)
     */
    public boolean dfs(ArrayList<Integer>[] graph, int source, int target) {
        if (!seen.contains(source)) {
            seen.add(source);
            if (source == target) return true;
            for (int nei : graph[source]) {
                if (dfs(graph, nei, target)) return true;
            }
        }
        return false;
    }

    // V4
    // IDEA : UNION FIND
    // https://leetcode.com/problems/redundant-connection/editorial/
    int MAX_EDGE_VAL_1 = 1000;

    /**
     * time = O(N)
     * space = O(H)
     */
    public int[] findRedundantConnection_4(int[][] edges) {
        DSU dsu = new DSU(MAX_EDGE_VAL_1 + 1);
        for (int[] edge : edges) {
            if (!dsu.union(edge[0], edge[1])) return edge;
        }
        throw new AssertionError();
    }

    class DSU {
        int[] parent;
        int[] rank;

        /**
         * time = O(N)
         * space = O(H)
         */
        public DSU(int size) {
            parent = new int[size];
            for (int i = 0; i < size; i++) parent[i] = i;
            rank = new int[size];
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        /**
         * time = O(N)
         * space = O(H)
         */
        public boolean union(int x, int y) {
            int xr = find(x), yr = find(y);
            if (xr == yr) {
                return false;
            } else if (rank[xr] < rank[yr]) {
                parent[xr] = yr;
            } else if (rank[xr] > rank[yr]) {
                parent[yr] = xr;
            } else {
                parent[yr] = xr;
                rank[xr]++;
            }
            return true;
        }
    }

}
