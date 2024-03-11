package LeetCodeJava.BFS;

// https://leetcode.com/problems/graph-valid-tree/description/?envType=list&envId=xoqag3yj

import java.util.*;

public class GraphValidTree {

    // V0
    // TODO : implement it

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
