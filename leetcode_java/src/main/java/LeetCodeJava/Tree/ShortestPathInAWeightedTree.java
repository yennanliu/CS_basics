package LeetCodeJava.Tree;

// https://leetcode.com/problems/shortest-path-in-a-weighted-tree/

import java.util.Arrays;

/**
 *  3515. Shortest Path in a Weighted Tree
 *  Hard
 *
 *  You are given an integer n and an undirected, weighted tree rooted at node
 *  1 with n nodes numbered from 1 to n. This is represented by a 2D array
 *  edges of length n - 1, where edges[i] = [u_i, v_i, w_i] indicates an
 *  undirected edge from node u_i to v_i with weight w_i.
 *
 *  You are also given a 2D integer array queries of length q, where each
 *  queries[i] is either:
 *    [1, u, v, w'] - Update the weight of the edge between nodes u and v to
 *                    w', where (u, v) is guaranteed to be an existing edge.
 *    [2, x]        - Compute the shortest path distance from the root node 1
 *                    to node x.
 *
 *  Return an integer array answer, where answer[i] is the shortest path
 *  distance from node 1 to x for the ith query of type [2, x].
 *
 *  Example 1:
 *    Input: n = 2, edges = [[1,2,7]], queries = [[2,2],[1,1,2,4],[2,2]]
 *    Output: [7,4]
 *
 *  Example 2:
 *    Input: n = 3, edges = [[1,2,2],[1,3,4]],
 *           queries = [[2,1],[2,3],[1,1,3,7],[2,2],[2,3]]
 *    Output: [0,4,2,7]
 *
 *  Constraints:
 *    1 <= n <= 10^5
 *    edges.length == n - 1, 1 <= w_i <= 10^4
 *    1 <= queries.length == q <= 10^5
 *    (u, v) is always an edge from edges, 1 <= w' <= 10^4
 */
public class ShortestPathInAWeightedTree {

    // V0
    // IDEA: EULER TOUR TURNS "EDGE UPDATE" INTO A RANGE ADD ON A FENWICK TREE
    //       in a rooted tree the distance from the root to x is just the sum of
    //       the edge weights on that one path, so
    //           dist(x) = dist(parent) + w(parent, x)
    //       changing one edge (p, c) by delta therefore changes dist(y) by
    //       exactly delta for every y in the subtree of c, and nothing else.
    //       a DFS preorder numbers each subtree as one contiguous block
    //       [tin, tout], so "add delta to a whole subtree" becomes "add delta
    //       to a range" and "read dist(x)" becomes "read one position" -> a
    //       Fenwick tree over the DIFFERENCE array does range-add / point-query
    //       in O(log n) each.
    //       the only bookkeeping left is deciding which endpoint of an updated
    //       edge is the child (the one whose parent is the other endpoint) and
    //       remembering each edge's current weight so the delta is computable.
    //       NOTE: the DFS uses an explicit stack - n reaches 10^5, deep enough
    //             to overflow a recursive one on a path-shaped tree.
    /**
     * time = O((N + Q) * log N)
     * space = O(N)
     */
    private long[] bit;
    private int size;   // == n + 1, the Fenwick length

    public int[] treeQueries(int n, int[][] edges, int[][] queries) {
        int[] head = new int[n + 1];
        int em = 2 * (n - 1 > 0 ? n - 1 : 1);
        int[] nxt = new int[em];
        int[] to = new int[em];
        int[] wt = new int[em];
        Arrays.fill(head, -1);
        int ec = 0;
        for (int[] e : edges) {
            to[ec] = e[1]; wt[ec] = e[2]; nxt[ec] = head[e[0]]; head[e[0]] = ec++;
            to[ec] = e[0]; wt[ec] = e[2]; nxt[ec] = head[e[1]]; head[e[1]] = ec++;
        }

        int[] par = new int[n + 1];
        int[] wpar = new int[n + 1];     // weight of the edge to the parent
        int[] order = new int[n];
        boolean[] seen = new boolean[n + 1];
        int[] stack = new int[n + 1];
        int sp = 0, oc = 0;
        seen[1] = true;
        stack[sp++] = 1;
        while (sp > 0) {                 // iterative DFS preorder
            int u = stack[--sp];
            order[oc++] = u;
            for (int e = head[u]; e != -1; e = nxt[e]) {
                int v = to[e];
                if (!seen[v]) {
                    seen[v] = true;
                    par[v] = u;
                    wpar[v] = wt[e];
                    stack[sp++] = v;
                }
            }
        }

        int[] tin = new int[n + 1];
        for (int i = 0; i < oc; i++) {
            tin[order[i]] = i;
        }
        int[] sz = new int[n + 1];
        Arrays.fill(sz, 1);
        for (int i = oc - 1; i >= 0; i--) {
            int u = order[i];
            if (u != 1) {
                sz[par[u]] += sz[u];
            }
        }
        int[] tout = new int[n + 1];
        for (int i = 0; i < oc; i++) {
            int u = order[i];
            tout[u] = tin[u] + sz[u] - 1;
        }

        // difference array whose prefix sums are the root distances
        long[] diff = new long[n + 2];
        for (int i = 0; i < oc; i++) {
            int u = order[i];
            if (u != 1) {
                diff[tin[u]] += wpar[u];
                diff[tout[u] + 1] -= wpar[u];
            }
        }
        this.size = n + 1;
        this.bit = new long[n + 2];      // O(n) Fenwick construction
        for (int i = 1; i <= n + 1; i++) {
            this.bit[i] += diff[i - 1];
            int j = i + (i & (-i));
            if (j <= n + 1) {
                this.bit[j] += this.bit[i];
            }
        }

        int cntRead = 0;
        for (int[] q : queries) {
            if (q[0] == 2) {
                cntRead++;
            }
        }
        int[] res = new int[cntRead];
        int ri = 0;
        for (int[] q : queries) {
            if (q[0] == 1) {
                int u = q[1], v = q[2], w = q[3];
                int c = (par[v] == u) ? v : u;
                int delta = w - wpar[c];
                if (delta != 0) {
                    wpar[c] = w;
                    add(tin[c], delta);
                    add(tout[c] + 1, -delta);
                }
            } else {
                res[ri++] = (int) prefix(tin[q[1]]);
            }
        }
        return res;
    }

    private void add(int i, long delta) {
        i += 1;
        while (i <= this.size) {
            this.bit[i] += delta;
            i += i & (-i);
        }
    }

    private long prefix(int i) {
        i += 1;
        long s = 0L;
        while (i > 0) {
            s += this.bit[i];
            i -= i & (-i);
        }
        return s;
    }
}
