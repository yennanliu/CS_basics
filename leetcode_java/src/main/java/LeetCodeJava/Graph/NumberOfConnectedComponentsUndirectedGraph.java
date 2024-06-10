package LeetCodeJava.Graph;

// https://leetcode.com/problems/number-of-connected-components-in-an-undirected-graph/description/?envType=list&envId=xoqag3yj
// https://leetcode.ca/all/323.html
// https://leetcode.ca/2016-10-18-323-Number-of-Connected-Components-in-an-Undirected-Graph/

import java.util.*;

public class NumberOfConnectedComponentsUndirectedGraph {

    // V0
    // IDEA : UNION FIND
    // TODO : implement it
    // https://www.youtube.com/watch?v=8f1XPm4WOUc
//    public int countComponents(int n, int[][] edges) {
//        return 0;
//    }

    // V1
    // IDEA : UNION FIND
    // https://github.com/neetcode-gh/leetcode/blob/main/java/0323-number-of-connected-components-in-an-undirected-graph.java
    // https://www.youtube.com/watch?v=8f1XPm4WOUc
    private int[] parent;
    private int[] rank;

    public int countComponents_1(int n, int[][] edges) {
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

    // V1
    // IDEA : UNION FIND (gpt)
    // TODO : validate
    static class UnionFind {
        private int[] root;
        private int[] rank;

        public UnionFind(int size) {
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

    public int countComponents_1_1(int n, int[][] edges) {
        UnionFind uf = new UnionFind(n);

        for (int[] edge : edges) {
            uf.union(edge[0], edge[1]);
        }

        return uf.getCount();
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
