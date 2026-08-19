package LeetCodeJava.Tree;

// https://leetcode.com/problems/find-weighted-median-node-in-tree/

import java.util.ArrayDeque;
import java.util.Deque;

/**
 *  3585. Find Weighted Median Node in Tree
 *  Hard
 *
 *  You are given an integer n and an undirected, weighted tree rooted at node 0
 *  with n nodes numbered from 0 to n - 1. This is represented by a 2D array edges
 *  of length n - 1, where edges[i] = [u_i, v_i, w_i] indicates an edge from node
 *  u_i to v_i with weight w_i.
 *
 *  The weighted median node is defined as the first node x on the path from u_i
 *  to v_i such that the sum of edge weights from u_i to x is greater than or
 *  equal to half of the total path weight.
 *
 *  You are given a 2D integer array queries. For each queries[j] = [u_j, v_j],
 *  determine the weighted median node along the path from u_j to v_j.
 *
 *  Return an array ans, where ans[j] is the node index of the weighted median
 *  for queries[j].
 *
 *  Example 1:
 *    Input: n = 2, edges = [[0,1,7]], queries = [[1,0],[0,1]]
 *    Output: [0,1]
 *    Explanation: path 1 -> 0 has total weight 7, half 3.5, and 7 >= 3.5 so the
 *                 median is node 0. Symmetrically for [0,1].
 *
 *  Example 3:
 *    Input: n = 5, edges = [[0,1,2],[0,2,5],[1,3,1],[2,4,3]],
 *           queries = [[3,4],[1,2]]
 *    Output: [2,2]
 *    Explanation: path 3 -> 1 -> 0 -> 2 -> 4 has total weight 11, half 5.5;
 *                 the prefix 3 -> 2 is 8 >= 5.5, so the median is node 2.
 *
 *  Constraints:
 *    2 <= n <= 10^5
 *    edges.length == n - 1
 *    edges[i] == [u_i, v_i, w_i]
 *    0 <= u_i, v_i < n
 *    1 <= w_i <= 10^9
 *    1 <= queries.length <= 10^5
 *    queries[j] == [u_j, v_j]
 *    0 <= u_j, v_j < n
 *    The input is generated such that edges represents a valid tree.
 */
public class FindWeightedMedianNodeInTree {

    // V0
    // IDEA: ROOT DISTANCES + LCA, THEN ONE BINARY-LIFTING JUMP PER QUERY
    //
    //   root the tree once and store dist[x], the weighted distance from node 0.
    //   with l = lca(u, v) the path splits into an upward leg of length
    //   A = dist[u] - dist[l] and a downward leg of length B = dist[v] - dist[l],
    //   and the total path weight is A + B. every comparison below is DOUBLED so
    //   the "half" stays exact integer arithmetic and never a float.
    //
    //   which leg holds the median is decided in O(1) by testing l itself: the
    //   prefix from u down to l measures A, so l qualifies exactly when
    //   2A >= A + B, i.e. when A >= B.
    //
    //   on the UPWARD leg the prefix from u to an ancestor x is dist[u] - dist[x],
    //   so 2*(dist[u] - dist[x]) >= A + B rewrites to
    //   2*dist[x] <= 2*dist[l] + A - B. climbing from u the left side only
    //   shrinks, so the FAILING nodes form an unbroken run starting at u;
    //   binary lifting climbs to the last failing node and the answer is its
    //   parent (u itself always fails when u != v, so that parent exists).
    //
    //   on the DOWNWARD leg the prefix from u to x is A + dist[x] - dist[l], and
    //   the test becomes 2*dist[x] >= 2*dist[l] + B - A. now the DEEPER nodes
    //   qualify, so climbing from v the qualifying nodes form an unbroken run
    //   and the answer is the highest one still strictly below l.
    /**
     * time = O((N + Q) * log N)
     * space = O(N * log N)
     */
    public int[] findMedian(int n, int[][] edges, int[][] queries) {
        int LOG = Math.max(1, 32 - Integer.numberOfLeadingZeros(n));

        // adjacency as CSR-ish arrays (n up to 1e5)
        int[] head = new int[n];
        int[] nxt = new int[2 * (n - 1) + 2];
        int[] to = new int[2 * (n - 1) + 2];
        int[] wt = new int[2 * (n - 1) + 2];
        for (int i = 0; i < n; i++) {
            head[i] = -1;
        }
        int cnt = 0;
        for (int[] e : edges) {
            to[cnt] = e[1]; wt[cnt] = e[2]; nxt[cnt] = head[e[0]]; head[e[0]] = cnt++;
            to[cnt] = e[0]; wt[cnt] = e[2]; nxt[cnt] = head[e[1]]; head[e[1]] = cnt++;
        }

        int[] parent = new int[n];
        int[] depth = new int[n];
        long[] dist = new long[n];
        boolean[] seen = new boolean[n];
        parent[0] = -1;
        seen[0] = true;
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        while (!stack.isEmpty()) {
            int x = stack.pop();
            for (int e = head[x]; e != -1; e = nxt[e]) {
                int y = to[e];
                if (!seen[y]) {
                    seen[y] = true;
                    parent[y] = x;
                    depth[y] = depth[x] + 1;
                    dist[y] = dist[x] + wt[e];
                    stack.push(y);
                }
            }
        }

        int[][] up = new int[LOG][n];
        up[0] = parent;
        for (int k = 1; k < LOG; k++) {
            for (int i = 0; i < n; i++) {
                int p = up[k - 1][i];
                up[k][i] = (p == -1) ? -1 : up[k - 1][p];
            }
        }

        int[] ans = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int u = queries[i][0];
            int v = queries[i][1];
            if (u == v) {
                ans[i] = u;
                continue;
            }

            int l = lca(u, v, depth, up, parent, LOG);
            long a = dist[u] - dist[l];
            long b = dist[v] - dist[l];

            if (a >= b) {
                // median sits on the u -> l leg; climb past every FAILING node
                long limit = 2 * dist[l] + a - b;
                int cur = u;
                for (int k = LOG - 1; k >= 0; k--) {
                    int p = up[k][cur];
                    if (p != -1 && depth[p] >= depth[l] && 2 * dist[p] > limit) {
                        cur = p;
                    }
                }
                ans[i] = parent[cur];
            } else {
                // median sits on the l -> v leg; climb while still QUALIFYING
                long limit = 2 * dist[l] + b - a;
                int cur = v;
                for (int k = LOG - 1; k >= 0; k--) {
                    int p = up[k][cur];
                    if (p != -1 && depth[p] > depth[l] && 2 * dist[p] >= limit) {
                        cur = p;
                    }
                }
                ans[i] = cur;
            }
        }
        return ans;
    }

    private int lca(int a, int b, int[] depth, int[][] up, int[] parent, int LOG) {
        if (depth[a] < depth[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        int diff = depth[a] - depth[b];
        int k = 0;
        while (diff != 0) {
            if ((diff & 1) == 1) {
                a = up[k][a];
            }
            diff >>= 1;
            k++;
        }
        if (a == b) {
            return a;
        }
        for (int j = LOG - 1; j >= 0; j--) {
            if (up[j][a] != up[j][b]) {
                a = up[j][a];
                b = up[j][b];
            }
        }
        return parent[a];
    }
}
