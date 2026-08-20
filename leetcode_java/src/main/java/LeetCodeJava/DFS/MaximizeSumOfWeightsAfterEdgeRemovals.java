package LeetCodeJava.DFS;

// https://leetcode.com/problems/maximize-sum-of-weights-after-edge-removals/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  3367. Maximize Sum of Weights after Edge Removals
 *  Hard
 *
 *  There exists an undirected tree with n nodes numbered 0 to n - 1. You are
 *  given a 2D integer array edges of length n - 1, where edges[i] = [u_i, v_i,
 *  w_i] indicates that there is an edge between nodes u_i and v_i with weight
 *  w_i in the tree.
 *
 *  Your task is to remove zero or more edges such that:
 *    - Each node has an edge with at most k other nodes, where k is given.
 *    - The sum of the weights of the remaining edges is maximized.
 *
 *  Return the maximum possible sum of weights for the remaining edges after
 *  making the necessary removals.
 *
 *  Example 1:
 *    Input: edges = [[0,1,4],[0,2,2],[2,3,12],[2,4,6]], k = 2
 *    Output: 22
 *    Explanation: Node 2 has edges with 3 other nodes. We remove the edge
 *                 [0,2,2], so no node has more than k = 2 edges.
 *
 *  Example 2:
 *    Input: edges = [[0,1,5],[1,2,10],[0,3,15],[3,4,20],[3,5,5],[0,6,10]], k = 3
 *    Output: 65
 *    Explanation: No node has more than k = 3 edges, so nothing is removed.
 *
 *  Constraints:
 *    2 <= n <= 10^5
 *    1 <= k <= n - 1
 *    edges.length == n - 1
 *    edges[i].length == 3
 *    0 <= edges[i][0], edges[i][1] <= n - 1
 *    1 <= edges[i][2] <= 10^6
 *    The input is generated such that edges form a valid tree.
 */
public class MaximizeSumOfWeightsAfterEdgeRemovals {

    // V0
    // IDEA: TREE DP ON "IS THE EDGE TO MY PARENT KEPT ?"
    //       root the tree. the only thing the outside world cares about for a
    //       node u is whether the edge up to its parent survives, because that
    //       consumes one of u's k slots:
    //
    //         keep0[u] = best inside u's subtree when the parent edge is DROPPED
    //                    (u may keep up to k child edges)
    //         keep1[u] = best when the parent edge is KEPT
    //                    (u may keep only k - 1 child edges)
    //
    //       for a child v the gain of keeping its edge is
    //           gain = (keep1[v] + w) - keep0[v]
    //       so start from sum(keep0 over children) and add the largest POSITIVE
    //       gains up to the slot limit -> O(deg log deg) per node.
    //       the traversal is ITERATIVE (n up to 10^5, the tree may be a chain).
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public long maximizeSumOfWeights(int[][] edges, int k) {
        int n = edges.length + 1;

        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<int[]>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(new int[]{e[1], e[2]});
            adj.get(e[1]).add(new int[]{e[0], e[2]});
        }

        // iterative DFS to get a parent array + a "children before parents" order
        int[] parent = new int[n];
        int[] order = new int[n];
        Arrays.fill(parent, -1);
        boolean[] seen = new boolean[n];
        int[] stack = new int[n];
        int sp = 0, cnt = 0;
        stack[sp++] = 0;
        seen[0] = true;
        while (sp > 0) {
            int u = stack[--sp];
            order[cnt++] = u;
            for (int[] nb : adj.get(u)) {
                if (!seen[nb[0]]) {
                    seen[nb[0]] = true;
                    parent[nb[0]] = u;
                    stack[sp++] = nb[0];
                }
            }
        }

        long[] keep0 = new long[n];
        long[] keep1 = new long[n];

        for (int idx = n - 1; idx >= 0; idx--) {
            int u = order[idx];

            long base = 0;
            List<Long> gains = new ArrayList<>();
            for (int[] nb : adj.get(u)) {
                int v = nb[0];
                if (v == parent[u]) {
                    continue;
                }
                base += keep0[v];
                long gain = keep1[v] + nb[1] - keep0[v];
                if (gain > 0) {
                    gains.add(gain);
                }
            }

            // ascending sort, then read from the tail = biggest gains first
            int m = gains.size();
            long[] arr = new long[m];
            for (int i = 0; i < m; i++) {
                arr[i] = gains.get(i);
            }
            Arrays.sort(arr);

            // prefix[i] = sum of the i largest gains
            long[] prefix = new long[m + 1];
            for (int i = 1; i <= m; i++) {
                prefix[i] = prefix[i - 1] + arr[m - i];
            }

            keep0[u] = base + prefix[Math.min(k, m)];
            keep1[u] = base + prefix[Math.min(k - 1, m)];
        }

        return keep0[0];
    }
}
