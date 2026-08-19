package LeetCodeJava.Tree;

// https://leetcode.com/problems/number-of-ways-to-assign-edge-weights-ii/

import java.util.Arrays;

/**
 *  3559. Number of Ways to Assign Edge Weights II
 *  Hard
 *
 *  There is an undirected tree with n nodes labeled from 1 to n, rooted at
 *  node 1. The tree is represented by a 2D integer array edges of length
 *  n - 1, where edges[i] = [u_i, v_i] indicates an edge between u_i and v_i.
 *
 *  Initially, all edges have a weight of 0. You must assign each edge a weight
 *  of either 1 or 2. The cost of a path between any two nodes u and v is the
 *  total weight of all edges in the path connecting them.
 *
 *  You are given a 2D integer array queries. For each queries[i] = [u_i, v_i],
 *  determine the number of ways to assign weights to edges in the path such
 *  that the cost of the path between u_i and v_i is odd.
 *
 *  Return an array answer, where answer[i] is the number of valid assignments
 *  for queries[i], modulo 10^9 + 7.
 *
 *  Note: For each query, disregard all edges not in the path between u_i, v_i.
 *
 *  Example 1:
 *    Input: edges = [[1,2]], queries = [[1,1],[1,2]]
 *    Output: [0,1]
 *    Explanation: the empty path costs 0 (never odd) -> 0; the 1-edge path has
 *                 exactly one odd assignment -> 1.
 *
 *  Example 2:
 *    Input: edges = [[1,2],[1,3],[3,4],[3,5]], queries = [[1,4],[3,4],[2,5]]
 *    Output: [2,1,4]
 *
 *  Constraints:
 *    2 <= n <= 10^5
 *    edges.length == n - 1
 *    1 <= queries.length <= 10^5
 *    1 <= u_i, v_i <= n
 *    edges represents a valid tree.
 */
public class NumberOfWaysToAssignEdgeWeightsII {

    // V0
    // IDEA: PATH LENGTH VIA LCA, THEN THE PARITY COUNT IS 2^(L-1)
    //       weights are 1 or 2, so only the edges given a 1 change the parity:
    //       the path cost is odd exactly when an odd number of its L edges got
    //       a 1. the number of odd-sized subsets of L items is 2^(L-1) for
    //       every L >= 1 (fix the first L-1 edges freely, the last is forced),
    //       and 0 when the path is empty.
    //       so each query only needs L = depth[u] + depth[v] - 2*depth[lca].
    //       with 10^5 queries an O(log n) climb per LCA is the bottleneck, so
    //       the tree is flattened into an EULER TOUR: the LCA is the shallowest
    //       node visited between the two nodes' first appearances, and a sparse
    //       table over the tour depths makes that an O(1) range minimum.
    /**
     * time = O(N * log N + Q)
     * space = O(N * log N)
     */
    private int[] tdep;
    private int[] first;
    private int[] logTab;
    private int[][] sp;

    public int[] assignEdgeWeights(int[][] edges, int[][] queries) {
        final long MOD = 1000000007L;
        int n = edges.length + 1;

        int[] head = new int[n + 1];
        int[] nxt = new int[2 * (n - 1)];
        int[] to = new int[2 * (n - 1)];
        Arrays.fill(head, -1);
        int ec = 0;
        for (int[] e : edges) {
            to[ec] = e[1]; nxt[ec] = head[e[0]]; head[e[0]] = ec++;
            to[ec] = e[0]; nxt[ec] = head[e[1]]; head[e[1]] = ec++;
        }

        int[] depth = new int[n + 1];
        this.first = new int[n + 1];
        this.tdep = new int[2 * n];
        int m = 0;

        int[] iter = new int[n + 1];
        boolean[] entered = new boolean[n + 1];
        boolean[] seen = new boolean[n + 1];
        int[] stack = new int[n + 2];
        for (int i = 1; i <= n; i++) {
            iter[i] = head[i];
        }
        int sptr = 0;
        seen[1] = true;
        stack[sptr++] = 1;
        while (sptr > 0) {
            int u = stack[sptr - 1];
            if (!entered[u]) {
                entered[u] = true;
                this.first[u] = m;
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
                    stack[sptr++] = v;
                    adv = true;
                    break;
                }
            }
            if (!adv) {
                sptr--;
                if (sptr > 0) {
                    this.tdep[m] = depth[stack[sptr - 1]];
                    m++;
                }
            }
        }

        buildSparseTable(m);

        long[] pw = new long[n + 1];
        pw[0] = 1L;
        for (int i = 1; i <= n; i++) {
            pw[i] = pw[i - 1] * 2 % MOD;
        }

        int[] res = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int u = queries[i][0], v = queries[i][1];
            int d = minDepthBetween(this.first[u], this.first[v]);
            int L = depth[u] + depth[v] - 2 * d;
            res[i] = (L == 0) ? 0 : (int) pw[L - 1];
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
            this.sp[0][i] = this.tdep[i];
        }
        for (int j = 1; j < levels; j++) {
            int len = m - (1 << j) + 1;
            this.sp[j] = new int[len];
            int half = 1 << (j - 1);
            for (int i = 0; i < len; i++) {
                this.sp[j][i] = Math.min(this.sp[j - 1][i], this.sp[j - 1][i + half]);
            }
        }
    }

    // min tour depth over [i, k] -> the depth of the LCA
    private int minDepthBetween(int i, int k) {
        if (i > k) {
            int t = i; i = k; k = t;
        }
        int j = this.logTab[k - i + 1];
        return Math.min(this.sp[j][i], this.sp[j][k - (1 << j) + 1]);
    }
}
