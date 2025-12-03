package LeetCodeJava.Graph;

// https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/description/?envType=list&envId=xoqag3yj
// https://leetcode.ca/all/323.html
// https://leetcode.ca/2016-10-18-323-Number-of-Connected-Components-in-an-Undirected-Graph/
/**
 * 323. Number of Connected Components in an Undirected Graph
 * Given n nodes labeled from 0 to n - 1 and a list of undirected edges (each edge is a pair of nodes), write a function to find the number of connected components in an undirected graph.
 *
 * Example 1:
 *
 * Input: n = 5 and edges = [[0, 1], [1, 2], [3, 4]]
 *
 *      0          3
 *      |          |
 *      1 --- 2    4
 *
 * Output: 2
 * Example 2:
 *
 * Input: n = 5 and edges = [[0, 1], [1, 2], [2, 3], [3, 4]]
 *
 *      0           4
 *      |           |
 *      1 --- 2 --- 3
 *
 * Output:  1
 * Note:
 * You can assume that no duplicate edges will appear in edges. Since all edges are undirected, [0, 1] is the same as [1, 0] and thus will not appear together in edges.
 *
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Amazon Facebook Google LinkedIn Microsoft Twitter
 *
 */
import dev.workspace8;

import java.util.*;

public class NumberOfConnectedComponentsUndirectedGraph {

    // V0
    // IDEA : UNION FIND
    // TODO : validate below
    // https://www.youtube.com/watch?v=8f1XPm4WOUc
    public int countComponents(int n, int[][] edges) {
        myUF2_ myUF2 = new myUF2_(n, edges);
        for (int[] e : edges) {
            int x = e[0];
            int y = e[1];
            // If union returns false, it means we encountered a cycle
            if (!myUF2.union(x, y)) {
                // If you want to return 0 when a cycle is detected, keep this line
                // But if you only want the number of components, you can remove it
                return 0; // Optionally remove if you don't want cycle detection
            }
        }

        return myUF2.getClusterCnt(); // Return the final count of clusters
    }

    public class myUF2_ {
        // Attributes
        int n;
        int[] parents;
        //int[] rank;
        int clusterCnt;

        public myUF2_(int n, int[][] edges) {
            this.n = n;
            this.parents = new int[n];
            //this.rank = new int[n];  // Union by rank to improve efficiency
            for (int i = 0; i < n; i++) {
                this.parents[i] = i;
                //this.rank[i] = 1; // Initialize rank
            }
            this.clusterCnt = n;
        }

        public boolean union(int x, int y) {
            int xRoot = findParent(x);
            int yRoot = findParent(y);

            if (xRoot == yRoot) {
                return false; // Cycle detected if both have the same root
            }

            // (optional) Union by rank: attach the smaller tree under the larger one
//            if (rank[xRoot] > rank[yRoot]) {
//                parents[yRoot] = xRoot;
//            } else if (rank[xRoot] < rank[yRoot]) {
//                parents[xRoot] = yRoot;
//            } else {
//                parents[yRoot] = xRoot;
//                rank[xRoot]++; // Increment rank if both roots are of the same rank
//            }

            clusterCnt -= 1; // Decrease cluster count when two components are merged
            return true;
        }

        public int findParent(int x) {
            if (parents[x] != x) {
                // Path compression: directly link nodes to their root
                parents[x] = findParent(parents[x]);
            }
            return parents[x];
        }

        public int getClusterCnt() {
            return clusterCnt;
        }
    }

    // v0-0-1
    // IDEA: UNION FIND (fixed by gemini)
    class MyUF2 {
        // attr
        int[] parents;
        int[] ranks; // Used to maintain tree balance
        int groupCnt;

        // constructor
        MyUF2(int size) {
            this.parents = new int[size];
            this.ranks = new int[size]; // Initialize ranks array

            // Initialize: Each element is its own parent.
            for (int i = 0; i < size; i++) {
                this.parents[i] = i;
            }

            // Ranks are initialized to 0 (all trees have height 0 initially).
            // Arrays.fill(this.ranks, 0); // Optional, as Java defaults ints to 0
            this.groupCnt = size; // Initially, there are 'size' separate groups.
        }

        /**
         * Finds the root parent of set x and performs Path Compression.
         * Time: O(α(N)), near constant time.
         * @param x The element whose root is sought.
         * @return The root parent of x.
         */
        public int findParent(int x) {
            // Base case: x is the root of its set.
            if (this.parents[x] != x) {
                // Path Compression: Set the parent of x directly to the root.
                this.parents[x] = this.findParent(this.parents[x]);
            }
            // Return the final root parent (either x or the compressed parent).
            return this.parents[x];
        }

        /**
         * Merges the sets containing x and y, using Union by Rank for balance.
         * Time: O(α(N)), near constant time.
         * @param x Element in the first set.
         * @param y Element in the second set.
         */
        public void union(int x, int y) {
            int parentX = this.findParent(x);
            int parentY = this.findParent(y);

            // They are already in the same set/component.
            if (parentX == parentY) {
                return;
            }

            // Union by Rank: Attach the smaller rank tree to the root of the larger rank tree.
            if (this.ranks[parentX] < this.ranks[parentY]) {
                // Attach X's tree to Y.
                this.parents[parentX] = parentY;
            } else if (this.ranks[parentX] > this.ranks[parentY]) {
                // Attach Y's tree to X.
                this.parents[parentY] = parentX;
            } else {
                // Ranks are equal: Attach X to Y and increment Y's rank.
                this.parents[parentX] = parentY;
                this.ranks[parentY]++;
            }

            // One group has been merged, so the total count decreases.
            this.groupCnt--;
        }

        public int getGroupCnt() {
            return this.groupCnt;
        }
    }

    public int countComponents_0_0_1(int n, int[][] edges) {
        // n is the number of nodes (0 to n-1).

        // 1. Initialize Union-Find structure with n elements.
        MyUF2 uf2 = new MyUF2(n);

        // 2. Perform union operation for every edge.
        for (int[] e : edges) {
            // Union the two nodes connected by the edge.
            uf2.union(e[0], e[1]);
        }

        // 3. The number of remaining groups is the number of connected components.
        return uf2.getGroupCnt();
    }


    // V0-1
    // IDEA: UNION FIND (without RANK) (gpt)
    // TODO: validate
    // private int[] p_;
    public int countComponents_0_1(int n, int[][] edges) {
        UnionFind2 uf2 = new UnionFind2(n, edges);
        // union
        for(int[] e: edges){
            int start = e[0];
            int end = e[1];
            uf2.union(start, end);
        }

        return uf2.indCnt;
    }

    public class UnionFind2 {
        // Attributes
        int n;
        int indCnt;
        int[] parents;

        // Constructor
        public UnionFind2(int n, int[][] edges) {
            this.n = n;
            this.indCnt = n;
            this.parents = new int[n];

            // Initialize each node as its own parent
            for (int i = 0; i < this.n; i++) {
                this.parents[i] = i;
            }
        }

        // Union method to merge two components
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            // If they are already in the same component, do nothing
            if (rootX == rootY) {
                return;
            }

            // Union without using rank or size - just connect one tree to the other
            parents[rootY] = rootX;  // or parents[rootX] = rootY, both will work

            // Decrease the component count since x and y are now connected
            indCnt--;
        }

        // Find the root of the component containing x
        public int find(int x) {
            if (x != parents[x]) {
                parents[x] = find(parents[x]); // Path compression
            }
            return parents[x];
        }

        // Check if x and y are connected
        public boolean isConnected(int x, int y) {
            return find(x) == find(y);
        }
    }

    // V0-2
    // IDEA: UNION FIND (with RANK) (gpt)
    // TODO : validate
    static class UnionFind_0_2 {
        private int[] root;
        private int[] rank;

        public UnionFind_0_2(int size) {
            root = new int[size];
            rank = new int[size];
            for (int i = 0; i < size; i++) {
                root[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int x) {
            if (root[x] == x) {
                return x;
            }
            root[x] = find(root[x]); // Path compression
            return root[x];
        }

        public boolean union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            /**
             *  NOTE !!!
             *
             *   -> RANK is NOT necessary
             *   -> it's a technics optimize efficience
             *
             *   1) Great question! Technically, you don't need the rank array to implement a working
             *   Union-Find (or Disjoint Set Union, DSU) structure. The union by rank technique is an optimization that
             *   helps keep the tree shallow by attaching the smaller tree (based on its rank or size) under the larger one. This results
             *   in more efficient operations, especially for large datasets.
             *
             *
             *   2) Can you go without the rank?
             *   Yes, you can, but the downside is that, without the rank
             *   (or an equivalent method like union by size), the tree structures can become deeper,
             *   and the performance of the find operation can degrade from nearly
             *   constant time to linear time in the worst case
             *   (i.e., the tree could become a straight line). This would lead to slower operations as the tree grows.
             *
             *   3) What would happen if you removed the rank?
             *   If you remove the rank array and union by rank, the union operation could
             *   simply link one root to the other directly without considering which tree is larger.
             *   For example, you could always set parents[rootY] = rootX without checking the rank.
             *   This would still work but result in potentially deeper trees, causing slower find operations.
             *
             *
             */
            if (rootX != rootY) {
                // Union by rank
                if (rank[rootX] > rank[rootY]) {
                    root[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    root[rootX] = rootY;
                } else {
                    root[rootY] = rootX;
                    rank[rootX] += 1;
                }
                return true;
            }
            return false;
        }

        public int getCount() {
            Set<Integer> uniqueRoots = new HashSet<>();
            for (int i = 0; i < root.length; i++) {
                uniqueRoots.add(find(i));
            }
            return uniqueRoots.size();
        }
    }

    public int countComponents_0_2(int n, int[][] edges) {
        UnionFind_0_2 uf = new UnionFind_0_2(n);

        for (int[] edge : edges) {
            uf.union(edge[0], edge[1]);
        }

        return uf.getCount();
    }

    // V0-3
    // IDEA: DFS (fixed by gpt)
    // TODO: validate below:
    public int countComponents_0_3(int n, int[][] edges) {
        if (n <= 1) return n;

        // Build the undirected graph
        Map<Integer, List<Integer>> graph = new HashMap<>();
        for (int i = 0; i < n; i++) {
            graph.put(i, new ArrayList<>());
        }

        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]); // Undirected!
        }

        Set<Integer> visited = new HashSet<>();
        int componentCount = 0;

        for (int i = 0; i < n; i++) {
            /**
             *  NOTE !!! below
             *
             *  instead of `collect every connected nodes`,
             *  we can simplify mark visited node,
             *  and update res (res += 1) after every dfs call
             */
            if (!visited.contains(i)) {
                dfs(i, graph, visited);
                componentCount++; // Each DFS from an unvisited node is a new component
            }
        }

        return componentCount;
    }

    private void dfs(int node, Map<Integer, List<Integer>> graph, Set<Integer> visited) {
        if (visited.contains(node)) return;

        visited.add(node);
        for (int neighbor : graph.get(node)) {
            if (!visited.contains(neighbor)) {
                dfs(neighbor, graph, visited);
            }
        }
    }

    // V0-4
    // IDEA: DFS
    // TODO: validate / fix below:
//    List<List<Integer>> connectedNode = new ArrayList<>();
//    public int countComponents(int n, int[][] edges) {
//        // edge
//        if(n <= 1){
//            return n;
//        }
//        if(edges == null || edges.length == 0){
//            return 0; // ??
//        }
//
//        Map<Integer, List<Integer>> neighborMap = new HashMap<>();
//        for(int[] e: edges){
//
//            int from = e[0];
//            int to = e[1];
//
//            List<Integer> list_1 = neighborMap.getOrDefault(from, new ArrayList<>());
//            list_1.add(to);
//            neighborMap.put(from, list_1);
//
//            List<Integer> list_2 = neighborMap.getOrDefault(to, new ArrayList<>());
//            list_1.add(from);
//            neighborMap.put(to, list_2);
//        }
//
//        //List<List<Integer>> connectedNode = new ArrayList<>();
//        HashSet<Integer> visited = new HashSet<>();
//        // visit all nodes, and update visited, connectedNode
//        for(int i = 0; i < n; i++){
//            connectedNode.add(connectHelper(i, new ArrayList<>(), neighborMap, visited));
//        }
//
//        return connectedNode.size();
//    }
//
//    public List<Integer> connectHelper(int x, List<Integer> cur, Map<Integer, List<Integer>> neighborMap, HashSet<Integer> visited){
//        if(visited.contains(x) && neighborMap.containsKey(x)){
//            return null; // ??
//        }
////        if(neighborMap.containsKey(x)){
////            return null; // ??
////        }
//
//        cur.add(x);
//        visited.add(x);
//
//        if(neighborMap.containsKey(x)){
//            for(int val: neighborMap.get(x)){
//                connectHelper(val, cur, neighborMap, visited);
//            }
//        }
//
//        //connectedNode;
//        return cur; // ???
//    }

    // V1-1
    // https://neetcode.io/problems/count-connected-components
    // IDEA: DFS
    public int countComponents_1_1(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        boolean[] visit = new boolean[n];
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }

        int res = 0;
        for (int node = 0; node < n; node++) {
            /**
             *  NOTE !!! below
             *
             *  instead of `collect every connected nodes`,
             *  we can simplify mark visited node,
             *  and update res (res += 1) after every dfs call
             */
            if (!visit[node]) {
                dfs(adj, visit, node);
                res++;
            }
        }
        return res;
    }

    private void dfs(List<List<Integer>> adj, boolean[] visit, int node) {
        visit[node] = true;
        for (int nei : adj.get(node)) {
            if (!visit[nei]) {
                dfs(adj, visit, nei);
            }
        }
    }

    // V1-2
    // https://neetcode.io/problems/count-connected-components
    // IDEA: BFS
    public int countComponents_1_2(int n, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        boolean[] visit = new boolean[n];
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }

        int res = 0;
        for (int node = 0; node < n; node++) {
            if (!visit[node]) {
                bfs(adj, visit, node);
                res++;
            }
        }
        return res;
    }

    private void bfs(List<List<Integer>> adj, boolean[] visit, int node) {
        Queue<Integer> q = new LinkedList<>();
        q.offer(node);
        visit[node] = true;
        while (!q.isEmpty()) {
            int cur = q.poll();
            for (int nei : adj.get(cur)) {
                if (!visit[nei]) {
                    visit[nei] = true;
                    q.offer(nei);
                }
            }
        }
    }

    // V1-3
    // https://neetcode.io/problems/count-connected-components
    // IDEA: Disjoint Set Union (Rank | Size)
    class DSU {
        int[] parent;
        int[] rank;

        public DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 1;
            }
        }

        public int find(int node) {
            int cur = node;
            while (cur != parent[cur]) {
                parent[cur] = parent[parent[cur]];
                cur = parent[cur];
            }
            return cur;
        }

        public boolean union(int u, int v) {
            int pu = find(u);
            int pv = find(v);
            if (pu == pv) {
                return false;
            }
            if (rank[pv] > rank[pu]) {
                int temp = pu;
                pu = pv;
                pv = temp;
            }
            parent[pv] = pu;
            rank[pu] += rank[pv];
            return true;
        }
    }

    public int countComponents_1_3(int n, int[][] edges) {
        DSU dsu = new DSU(n);
        int res = n;
        for (int[] edge : edges) {
            if (dsu.union(edge[0], edge[1])) {
                res--;
            }
        }
        return res;
    }


    // V1-4
    // IDEA : UNION FIND (with RANK)
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0323-number-of-connected-components-in-an-undirected-graph.java
    // https://www.youtube.com/watch?v=8f1XPm4WOUc
    private int[] parent;
    private int[] rank;

    public int countComponents_1_4(int n, int[][] edges) {
        parent = new int[n];
        rank = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 1;
        }

        int result = n;
        for (int i = 0; i < edges.length; i++) {
            if (union_1(edges[i][0], edges[i][1]) == 1) {
                result--;
            }
        }

        return result;
    }

    private int find_1(int node) {
        int result = node;

        while (parent[result] != result) {
            parent[result] = parent[parent[result]];
            result = parent[result];
        }

        return result;
    }

    private int union_1(int n1, int n2) {
        int p1 = this.find_1(n1);
        int p2 = this.find_1(n2);

        if (p1 == p2) {
            return 0;
        }

        if (rank[p2] > rank[p1]) {
            parent[p1] = p2;
            rank[p2] += rank[p1];
        } else {
            parent[p2] = p1;
            rank[p1] += rank[p2];
        }

        return 1;
    }

    // V2
    // IDEA : UNION FIND
    // https://leetcode.ca/2016-10-18-323-Number-of-Connected-Components-in-an-Undirected-Graph/
    private int[] p;

    public int countComponents_2(int n, int[][] edges) {
        p = new int[n];
        for (int i = 0; i < n; ++i) {
            p[i] = i;
        }
        for (int[] e : edges) {
            int a = e[0], b = e[1];
            p[find(a)] = find(b);
        }
        int ans = 0;
        for (int i = 0; i < n; ++i) {
            if (i == find(i)) {
                ++ans;
            }
        }
        return ans;
    }

    private int find(int x) {
        if (p[x] != x) {
            p[x] = find(p[x]);
        }
        return p[x];
    }

    // V3
    // IDEA : DFS
    // https://www.cnblogs.com/cnoodle/p/14197652.html
    public int countComponents_3(int n, int[][] edges) {
        int count = 0;
        List<List<Integer>> g = new ArrayList<>();
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            g.get(e[0]).add(e[1]);
            g.get(e[1]).add(e[0]);
        }

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                count++;
                dfs(visited, i, g);
            }
        }
        return count;
    }

    private void dfs(boolean[] visited, int node, List<List<Integer>> g) {
        visited[node] = true;
        for (int next : g.get(node)) {
            if (!visited[next]) {
                dfs(visited, next, g);
            }
        }
    }

    // V4
    // IDEA :  UNION FIND
    // https://www.cnblogs.com/cnoodle/p/14197652.html
    public int countComponents_4(int n, int[][] edges) {
        int count = n;
        int[] parents = new int[n];
        for (int i = 0; i < n; i++) {
            parents[i] = i;
        }
        for (int[] edge : edges) {
            int p = find(parents, edge[0]);
            int q = find(parents, edge[1]);
            if (p != q) {
                parents[p] = q;
                count--;
            }
        }
        return count;
    }

    private int find(int[] parents, int i) {
        while (parents[i] != i) {
            parents[i] = parents[parents[i]]; // route compression
            i = parents[i];
        }
        return i;
    }

    // V5
    // IDEA :  BFS
    // https://www.cnblogs.com/cnoodle/p/14197652.html
    public int countComponents_5(int n, int[][] edges) {
        int count = 0;
        List<List<Integer>> g = new ArrayList<>();
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            g.get(e[0]).add(e[1]);
            g.get(e[1]).add(e[0]);
        }

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                count++;
                Queue<Integer> queue = new LinkedList<>();
                queue.offer(i);
                while (!queue.isEmpty()) {
                    int index = queue.poll();
                    visited[index] = true;
                    for (int next : g.get(index)) {
                        if (!visited[next]) {
                            queue.offer(next);
                        }
                    }
                }
            }
        }
        return count;
    }



}
