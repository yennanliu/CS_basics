package LeetCodeJava.BFS;

// https://leetcode.com/problems/graph-valid-tree/description/?envType=list&envId=xoqag3yj

import java.util.*;

public class GraphValidTree {

    // V0
    // TODO : implement it

    // V0'
    // IDEA : QUICK FIND (gpt)
    public boolean validTree_0(int n, int[][] edges) {
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
                /** NOTE !!!  if a cycle, return false directly */
                return false;
            } else {
                // Union the sets
                /** NOTE !!!  if not a cycle, then "compress" the route, e.g. make node as the other node's parent */
                root[root1] = root2;
            }
        }

        /** Check if the number of edges is exactly n - 1 */
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
