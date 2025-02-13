package LeetCodeJava.BFS;

// https://leetcode.com/problems/graph-valid-tree/description/?envType=list&envId=xoqag3yj
// https://leetcode.ca/all/261.html
/**
 * 261. Graph Valid Tree
 * Given n nodes labeled from 0 to n-1 and a list of undirected edges (each edge is a pair of nodes), write a function to check whether these edges make up a valid tree.
 *
 * Example 1:
 *
 * Input: n = 5, and edges = [[0,1], [0,2], [0,3], [1,4]]
 * Output: true
 * Example 2:
 *
 * Input: n = 5, and edges = [[0,1], [1,2], [2,3], [1,3], [1,4]]
 * Output: false
 * Note: you can assume that no duplicate edges will appear in edges. Since all edges are undirected, [0,1] is the same as [1,0] and thus will not appear together in edges.
 *
 * Difficulty:
 * Medium
 * Lock:
 * Prime
 * Company:
 * Adobe Amazon Facebook Google LinkedIn Pinterest Salesforce Zenefits
 */
import dev.workspace6;

import java.util.*;

public class GraphValidTree {

    // V0
    // TODO : implement it
    // https://www.youtube.com/watch?v=bXsUuownnoQ

    // V0-1
    // IDEA : QUICK FIND (gpt)
    public boolean validTree_0_1(int n, int[][] edges) {
        if (n == 0) {
            return false;
        }

        /**
         * Step 1) Initialize root array where each node is its own parent
         *
         *  NOTE !!!
         *   we init an array with n length (NOT from edges)
         */
        int[] root = new int[n];
        for (int i = 0; i < n; i++) {
            root[i] = i;
        }

        /**
         * Step 2) update relation (union find)
         */
        // Process each edge
        for (int[] edge : edges) {

            /**
             *  NOTE !!!
             *
             *    find node "parent" with 2 perspective
             *     1) from 1st element (e.g. edge[0])
             *     2) from 2nd element (e.g. edge[1])
             *
             *    so, if parent1 == parent2
             *     -> means there is a circular (because they have same parent, so nodes must "connect itself back" at some point),
             *     -> so input is NOT a valid tree
             */
            int root1 = find(root, edge[0]); // parent1
            int root2 = find(root, edge[1]); // parent2

            // If the roots are the same, there's a cycle
            if (root1 == root2) {
                /** NOTE !!!
                 *
                 *  KEY OF THE ALGO
                 *
                 *  ->  if a cycle, return false directly
                 */
                return false;
            } else {
                // Union the sets
                /** NOTE !!!  if not a cycle, then "compress" the route, e.g. make node as the other node's parent */
                root[root1] = root2;
            }
        }

        /** NOTE !!!
         *
         * Check if the number of edges is exactly n - 1
         */
        return edges.length == n - 1; // NOTE !!! this check
    }

    // Find function with path compression
    private int find(int[] root, int e) {
        if (root[e] == e) {
            return e;
        } else {
            root[e] = find(root, root[e]); // Path compression
            return root[e];
        }
    }

    // V0-2
    // IDEA: UNION FIND
    // TODO: validate
    public boolean validTree_0_2(int n, int[][] edges) {
        // edge
        if(n == 0){
            return false;
        }
        MyUF myUF = new MyUF(n, edges);
        // union
        for(int[] x: edges){
            /** NOTE !!! below checks */
            if(!myUF.union(x[0], x[1])){
                return false;
            }
        }

        /**
         *  NOTE !!!
         *
         *  FINAL CHECK
         *
         *   based on math,
         *   n nodes, should ONLY has `n-1` edges
         *   -> so there is NO cycle in graph
         *   -> so can form a valid tree
         *
         *
         *   -> Check if the number of edges is exactly n - 1
         */
        return n - 1 == edges.length;
    }

    public class MyUF{
        // attr
        int n;
        int cnt; // `cluster cnt`
        int[][] edges;
        int[] parents; // records node's parent
        // constructor
        public MyUF(int n, int[][] edges){
            this.n = n;
            this.cnt = n;
            this.edges = edges;
            this.parents = new int[n]; // ???
            for(int i = 0; i < n; i++){
                this.parents[i] = i;
            }
        }
        // method
        // union
        public boolean union(int a, int b){
            if(a == b){
                return true;
            }
            int aParent = this.find(a);
            int bParent = this.find(b);
            /**
             * NOTE !!!
             *
             *  is a node's parent == b node's parent
             *  -> there must be a CYCLE,
             *  -> CAN'T form a valid tree -> return false directly
             */
            if(aParent == bParent){
                return false;
            }
            // this.parents[bParent] = APartent;  // equivalent as below
            this.parents[aParent] = bParent;
            this.cnt -= 1;
            return true;
        }
        // find a node's parent
        public int find(int a){
            if(a == this.parents[a]){
                return a;
            }
            this.parents[a] = this.find(a);
            return this.parents[a];
        }

        // get `cluster cnt`
        public int getCnt(){
            return this.cnt;
        }
    }

    // V0-3
    // IDEA: UNION FIND V2 (GPT)
    // TODO: validate if correct
//    public boolean validTree_0_3(int n, int[][] edges) {
//        // A tree must have exactly n - 1 edges
//        if (edges.length != n - 1) {
//            return false;
//        }
//
//        UnionFind uf = new UnionFind(n);
//
//        for (int[] e : edges) {
//            if (!uf.union(e[0], e[1])) { // If union fails, there's a cycle
//                return false;
//            }
//        }
//
//        return true; // If we processed all edges without cycle, it's a valid tree
//    }
//
//    class UnionFind {
//        int[] parent;
//
//        public UnionFind(int n) {
//            parent = new int[n];
//            for (int i = 0; i < n; i++) {
//                parent[i] = i;
//            }
//        }
//
//        public int find(int x) {
//            if (parent[x] != x) {
//                parent[x] = find(parent[x]); // Path compression
//            }
//            return parent[x];
//        }
//
//        public boolean union(int x, int y) {
//            int rootX = find(x);
//            int rootY = find(y);
//
//            if (rootX == rootY) {
//                return false; // Cycle detected
//            }
//
//            parent[rootX] = rootY; // Merge sets
//            return true;
//        }
//    }

    // V0-4
    // IDEA : DFS + GRAPH
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0261-graph-valid-tree.java
    private Map<Integer, List<Integer>> adjacencyList = new HashMap<>();

    public boolean validTree_0_4(int n, int[][] edges) {
        if (n == 0 || n == 1) return true;

        if (edges.length == 0) return false;

        for (int[] edge : edges) {
            int node1 = edge[0];
            int node2 = edge[1];
            adjacencyList.putIfAbsent(node1, new ArrayList<>());
            adjacencyList.putIfAbsent(node2, new ArrayList<>());
            adjacencyList.get(node1).add(node2);
            adjacencyList.get(node2).add(node1);
        }

        Set<Integer> visited = new HashSet<>();

        return (
                depthFirstSearch(edges[0][0], -1, visited) && visited.size() == n
        );
    }

    private boolean depthFirstSearch(
            int node,
            int previous,
            Set<Integer> visited
    ) {
        if (visited.contains(node)) return false;

        visited.add(node);

        for (Integer neighbor : adjacencyList.get(node)) {
            if (neighbor == previous) continue;

            if (!depthFirstSearch(neighbor, node, visited)) return false;
        }

        return true;
    }

    // V1
    // IDEA : BFS
    // https://protegejj.gitbook.io/algorithm-practice/leetcode/graph/261-graph-valid-tree
    public boolean validTree_1(int n, int[][] edges) {

        // NOTE here !!! List<Set<Integer>> as List type
        List<Set<Integer>> adjList = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adjList.add(new HashSet<>());
        }

        for (int[] edge : edges) {
            // NOTE here !!!
            adjList.get(edge[0]).add(edge[1]);
            adjList.get(edge[1]).add(edge[0]);
        }

        // NOTE here !!!
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        // NOTE here !!!
        queue.add(0);

        while (!queue.isEmpty()) {

            int curNode = queue.remove();

            // NOTE here !!!
            // found loop
            if (visited[curNode]) {
                return false;
            }

            // NOTE here !!!
            visited[curNode] = true;

            // NOTE here !!!
            for (int nextNode : adjList.get(curNode)) {
                queue.add(nextNode);
                // NOTE here !!!
                adjList.get(nextNode).remove(curNode);
            }
        }

        // NOTE here !!!
        for (boolean e : visited) {
            if (!e) {
                return false;
            }
        }
        return true;
    }

    // V2
    // IDEA : UNION FIND
    // https://leetcode.ca/2016-08-17-261-Graph-Valid-Tree/
    private int[] p;

    public boolean validTree_2(int n, int[][] edges) {
        p = new int[n];
        for (int i = 0; i < n; ++i) {
            p[i] = i;
        }
        for (int[] e : edges) {
            int a = e[0], b = e[1];
            if (find(a) == find(b)) {
                return false;
            }
            p[find(a)] = find(b);
            --n;
        }
        return n == 1;
    }

    private int find(int x) {
        if (p[x] != x) {
            p[x] = find(p[x]);
        }
        return p[x];
    }

    // V3
    // IDEA : DFS
    // https://protegejj.gitbook.io/algorithm-practice/leetcode/graph/261-graph-valid-tree
    public boolean validTree_3(int n, int[][] edges) {
        List<Set<Integer>> adjList = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            adjList.add(new HashSet<>());
        }

        for (int[] edge : edges) {
            adjList.get(edge[0]).add(edge[1]);
            adjList.get(edge[1]).add(edge[0]);
        }

        boolean[] visited = new boolean[n];

        // Check if graph has cycle
        if (hasCycle(0, visited, adjList, -1)) {
            return false;
        }

        // Check if graph is connected
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                return false;
            }
        }
        return true;
    }

    public boolean hasCycle(int node, boolean[] visited, List<Set<Integer>> adjList, int parent) {
        visited[node] = true;

        for (int nextNode : adjList.get(node)) {
            // (1) If nextNode is visited but it is not the parent of the curNode, then there is cycle
            // (2) If nextNode is not visited but we still find the cycle later on, return true;
            if ((visited[nextNode] && parent != nextNode) || (!visited[nextNode] && hasCycle(nextNode, visited, adjList, node))) {
                return true;
            }
        }
        return false;
    }

    // V4
    // IDEA : UNION FIND
    // https://protegejj.gitbook.io/algorithm-practice/leetcode/graph/261-graph-valid-tree
    public boolean validTree_4(int n, int[][] edges) {
        UnionFind uf = new UnionFind(n);

        for (int[] edge : edges) {
            // Find Loop
            if (!uf.union(edge[0], edge[1])) {
                return false;
            }
        }
        // Make sure the graph is connected
        return uf.count == 1;
    }

    class UnionFind {
        int[] sets;
        int[] size;
        int count;

        public UnionFind(int n) {
            sets = new int[n];
            size = new int[n];
            count = n;

            for (int i = 0; i < n; i++) {
                sets[i] = i;
                size[i] = 1;
            }
        }

        public int find(int node) {
            while (node != sets[node]) {
                node = sets[node];
            }
            return node;
        }

        public boolean union(int i, int j) {
            int node1 = find(i);
            int node2 = find(j);

            if (node1 == node2) {
                return false;
            }

            if (size[node1] < size[node2]) {
                sets[node1] = node2;
                size[node2] += size[node1];
            }
            else {
                sets[node2] = node1;
                size[node1] += size[node2];
            }
            --count;
            return true;
        }
    }

}
