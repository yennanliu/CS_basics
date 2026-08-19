package LeetCodeJava.Tree;

// https://leetcode.com/problems/minimum-weighted-subgraph-with-the-required-paths-ii/

import java.util.Arrays;

/**
 *  3553. Minimum Weighted Subgraph With the Required Paths II
 *  Hard
 *
 *  You are given an undirected weighted tree with n nodes, numbered from 0 to
 *  n - 1. It is represented by a 2D integer array edges of length n - 1, where
 *  edges[i] = [u_i, v_i, w_i] indicates that there is an edge between nodes
 *  u_i and v_i with weight w_i.
 *
 *  Additionally, you are given a 2D integer array queries, where
 *  queries[j] = [src1_j, src2_j, dest_j].
 *
 *  Return an array answer of length equal to queries.length, where answer[j]
 *  is the minimum total weight of a subtree such that it is possible to reach
 *  dest_j from both src1_j and src2_j using edges in this subtree.
 *
 *  Example 1:
 *    Input: edges = [[0,1,2],[1,2,3],[1,3,5],[1,4,4],[2,5,6]],
 *           queries = [[2,3,4],[0,2,5]]
 *    Output: [12,11]
 *    Explanation: answer[0] = 3 + 5 + 4 = 12; answer[1] = 2 + 3 + 6 = 11.
 *
 *  Example 2:
 *    Input: edges = [[1,0,8],[0,2,7]], queries = [[0,1,2]]
 *    Output: [15]
 *
 *  Constraints:
 *    3 <= n <= 10^5
 *    edges[i].length == 3, 1 <= w_i <= 10^4
 *    1 <= queries.length <= 10^5
 *    queries[j].length == 3, src1_j, src2_j, dest_j pairwise distinct
 *    edges represents a valid tree.
 */
public class MinimumWeightedSubgraphWithTheRequiredPathsII {

    // V0
    // IDEA: THE STEINER TREE OF THREE NODES IS HALF THE SUM OF THEIR PAIRWISE
    //       PATHS, SO EACH QUERY IS 3 LCA LOOKUPS
    //       in a tree the cheapest connected subgraph joining a, b and c is the
    //       union of the three pairwise paths, and those paths all meet at one
    //       node (the "median" of the triple). every edge of the union lies on
    //       exactly TWO of the three paths, hence
    //           weight = (d(a,b) + d(b,c) + d(a,c)) / 2
    //       and no per-query traversal is needed at all.
    //       what is needed is d(u,v) = dist[u] + dist[v] - 2 * dist[lca(u,v)],
    //       i.e. a fast LCA. with 3 * 10^5 lookups an O(log n) climb each is
    //       already costly, so the tree is flattened into an EULER TOUR: the
    //       LCA of u and v is the shallowest node visited between their first
    //       appearances, and a sparse table over the tour depths answers that
    //       range minimum in O(1).
    //       both the tour and the root distances come from one ITERATIVE DFS
    //       (n reaches 10^5, deep enough to blow a recursive stack).
    /**
     * time = O(N * log N + Q)
     * space = O(N * log N)
     */
    private int[] tour;
    private int[] tdep;
    private int[] first;
    private int[] logTab;
    private int[][] sp;

    public int[] minimumWeight(int[][] edges, int[][] queries) {
        int n = edges.length + 1;

        // adjacency via head/next arrays
        int[] head = new int[n];
        int[] nxt = new int[2 * (n - 1)];
        int[] to = new int[2 * (n - 1)];
        int[] wt = new int[2 * (n - 1)];
        Arrays.fill(head, -1);
        int ec = 0;
        for (int[] e : edges) {
            to[ec] = e[1]; wt[ec] = e[2]; nxt[ec] = head[e[0]]; head[e[0]] = ec++;
            to[ec] = e[0]; wt[ec] = e[2]; nxt[ec] = head[e[1]]; head[e[1]] = ec++;
        }

        long[] dist = new long[n];
        int[] depth = new int[n];
        this.first = new int[n];
        this.tour = new int[2 * n];
        this.tdep = new int[2 * n];
        int m = 0;

        int[] iter = new int[n];
        boolean[] entered = new boolean[n];
        boolean[] seen = new boolean[n];
        int[] stack = new int[n + 1];
        for (int i = 0; i < n; i++) {
            iter[i] = head[i];
        }
        int sptr = 0;
        seen[0] = true;
        stack[sptr++] = 0;
        while (sptr > 0) {
            int u = stack[sptr - 1];
            if (!entered[u]) {
                entered[u] = true;
                this.first[u] = m;
                this.tour[m] = u;
                this.tdep[m] = depth[u];
                m++;
            }
            boolean adv = false;
            while (iter[u] != -1) {
                int e = iter[u];
                iter[u] = nxt[e];
                int v = to[e];
                if (!seen[v]) {
                    seen[v] = true;
                    depth[v] = depth[u] + 1;
                    dist[v] = dist[u] + wt[e];
                    stack[sptr++] = v;
                    adv = true;
                    break;
                }
            }
            if (!adv) {
                sptr--;
                if (sptr > 0) {
                    int p = stack[sptr - 1];
                    this.tour[m] = p;
                    this.tdep[m] = depth[p];
                    m++;
                }
            }
        }

        buildSparseTable(m);

        int[] res = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int a = queries[i][0], b = queries[i][1], c = queries[i][2];
            long dab = dist[a] + dist[b] - 2 * dist[lca(a, b)];
            long dbc = dist[b] + dist[c] - 2 * dist[lca(b, c)];
            long dac = dist[a] + dist[c] - 2 * dist[lca(a, c)];
            res[i] = (int) ((dab + dbc + dac) / 2);
        }
        return res;
    }

    private void buildSparseTable(int m) {
        this.logTab = new int[m + 1];
        for (int i = 2; i <= m; i++) {
            this.logTab[i] = this.logTab[i >> 1] + 1;
        }
        int levels = this.logTab[m] + 1;
        this.sp = new int[levels][];
        this.sp[0] = new int[m];
        for (int i = 0; i < m; i++) {
            this.sp[0][i] = i;
        }
        for (int j = 1; j < levels; j++) {
            int len = m - (1 << j) + 1;
            this.sp[j] = new int[len];
            int half = 1 << (j - 1);
            for (int i = 0; i < len; i++) {
                int a = this.sp[j - 1][i];
                int b = this.sp[j - 1][i + half];
                this.sp[j][i] = (this.tdep[a] <= this.tdep[b]) ? a : b;
            }
        }
    }

    private int lca(int u, int v) {
        int i = this.first[u], k = this.first[v];
        if (i > k) {
            int t = i; i = k; k = t;
        }
        int j = this.logTab[k - i + 1];
        int a = this.sp[j][i];
        int b = this.sp[j][k - (1 << j) + 1];
        return (this.tdep[a] <= this.tdep[b]) ? this.tour[a] : this.tour[b];
    }
}
