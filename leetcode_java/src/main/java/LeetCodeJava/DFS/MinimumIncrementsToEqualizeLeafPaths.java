package LeetCodeJava.DFS;

// https://leetcode.com/problems/minimum-increments-to-equalize-leaf-paths/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  3593. Minimum Increments to Equalize Leaf Paths
 *  Medium
 *
 *  You are given an integer n and an undirected tree rooted at node 0 with n
 *  nodes numbered from 0 to n - 1. This is represented by a 2D array edges of
 *  length n - 1, where edges[i] = [ui, vi] indicates an edge between nodes ui
 *  and vi.
 *
 *  Each node i has an associated cost given by cost[i], representing the cost to
 *  traverse that node. The score of a path is the sum of the costs of all nodes
 *  along the path.
 *
 *  Your goal is to make the scores of all root-to-leaf paths equal by increasing
 *  the cost of any number of nodes by any non-negative amount.
 *
 *  Return the minimum number of nodes whose cost must be increased.
 *
 *  Example 1:
 *    Input: n = 3, edges = [[0,1],[0,2]], cost = [2,1,3]
 *    Output: 1
 *    Explanation: paths score 3 and 5; raising node 1 by 2 equalizes them.
 *
 *  Example 3:
 *    Input: n = 5, edges = [[0,4],[0,1],[1,2],[1,3]], cost = [3,4,1,1,7]
 *    Output: 1
 *    Explanation: paths score 10, 8, 8; raising node 1 by 2 equalizes them.
 *
 *  Constraints:
 *    2 <= n <= 10^5
 *    edges.length == n - 1
 *    edges[i] == [ui, vi]
 *    0 <= ui, vi < n
 *    cost.length == n
 *    1 <= cost[i] <= 10^9
 *    The input is generated such that edges represents a valid tree.
 */
public class MinimumIncrementsToEqualizeLeafPaths {

    // V0
    // IDEA: BOTTOM-UP MAX, CHARGING ONE OPERATION PER SHORT CHILD
    //       let f(u) be the largest score among the paths from u down to a leaf.
    //       every child subtree must be lifted to that same f value, and a
    //       single operation on the child's OWN node absorbs the whole shortfall
    //       at once (the amount raised is unlimited). so a child costs one
    //       operation iff it is strictly below the max - and lifting the child's
    //       node keeps every deeper path inside that subtree equal.
    //       the post-order walk is ITERATIVE (n up to 10^5, may be a chain).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minIncrease(int n, int[][] edges, int[] cost) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<Integer>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        // iterative DFS -> parent array + top-down order
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
            for (int v : adj.get(u)) {
                if (!seen[v]) {
                    seen[v] = true;
                    parent[v] = u;
                    stack[sp++] = v;
                }
            }
        }

        long[] best = new long[n];   // deepest path score from this node down
        int res = 0;

        for (int idx = n - 1; idx >= 0; idx--) {
            int u = order[idx];
            long maxChild = 0;
            boolean hasChild = false;
            for (int v : adj.get(u)) {
                if (v == parent[u]) {
                    continue;
                }
                hasChild = true;
                maxChild = Math.max(maxChild, best[v]);
            }
            if (hasChild) {
                for (int v : adj.get(u)) {
                    if (v == parent[u]) {
                        continue;
                    }
                    if (best[v] < maxChild) {
                        res++;
                    }
                }
            }
            best[u] = cost[u] + maxChild;
        }

        return res;
    }
}
