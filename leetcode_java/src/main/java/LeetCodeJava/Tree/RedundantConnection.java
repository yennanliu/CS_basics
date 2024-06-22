package LeetCodeJava.Tree;

// https://leetcode.com/problems/redundant-connection/description/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RedundantConnection {

    // V0
    // TODO : implement

    // V1
    // https://www.youtube.com/watch?v=FXWRE67PLL0
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0684-redundant-connection.java
    int[] parent;

    public int[] findRedundantConnection_1(int[][] edges) {
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

    public int find(int x) {
        if (x == parent[x - 1]) return x;
        return find(parent[x - 1]);
    }

    public void union(int x, int y) {
        parent[find(y) - 1] = find(x);
    }

    // V2
    // IDEA : DFS
    // https://leetcode.com/problems/redundant-connection/editorial/
    Set<Integer> seen = new HashSet();
    int MAX_EDGE_VAL = 1000;

    public int[] findRedundantConnection_2(int[][] edges) {
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

    // V3
    // IDEA : UNION FIND
    // https://leetcode.com/problems/redundant-connection/editorial/
    int MAX_EDGE_VAL_1 = 1000;

    public int[] findRedundantConnection_3(int[][] edges) {
        DSU dsu = new DSU(MAX_EDGE_VAL_1 + 1);
        for (int[] edge : edges) {
            if (!dsu.union(edge[0], edge[1])) return edge;
        }
        throw new AssertionError();
    }

    class DSU {
        int[] parent;
        int[] rank;

        public DSU(int size) {
            parent = new int[size];
            for (int i = 0; i < size; i++) parent[i] = i;
            rank = new int[size];
        }

        public int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

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
