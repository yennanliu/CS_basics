package LeetCodeJava.DFS;

// https://leetcode.com/problems/maximum-subtree-of-the-same-color/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  3004. Maximum Subtree of the Same Color
 *  Medium
 *
 *  You are given a 2D integer array edges representing a tree with n nodes,
 *  numbered from 0 to n - 1, rooted at node 0, where edges[i] = [ui, vi] means
 *  there is an edge between the nodes vi and ui.
 *
 *  You are also given a 0-indexed integer array colors of size n, where
 *  colors[i] is the color assigned to node i.
 *
 *  We want to find a node v such that every node in the subtree of v has the
 *  same color. Return the size of such subtree with the maximum number of nodes
 *  possible.
 *
 *  Example 1:
 *    Input: edges = [[0,1],[0,2],[0,3]], colors = [1,1,2,3]
 *    Output: 1
 *    Explanation: The subtree rooted at node 0 has children with different
 *                 colors; every other subtree has size 1.
 *
 *  Example 3:
 *    Input: edges = [[0,1],[0,2],[2,3],[2,4]], colors = [1,2,3,3,3]
 *    Output: 3
 *    Explanation: The subtree rooted at node 2 is uniform and has size 3.
 *
 *  Constraints:
 *    n == colors.length
 *    1 <= n <= 5 * 10^4
 *    edges.length == n - 1
 *    edges[i].length == 2
 *    0 <= ui, vi < n
 *    1 <= colors[i] <= 10^5
 *    The input is generated such that the graph represented by edges is a tree.
 */
public class MaximumSubtreeOfTheSameColor {

    // V0
    // IDEA: POST-ORDER DFS - A SUBTREE IS UNIFORM IFF ALL ITS CHILDREN ARE
    //       for node v:
    //         size[v]    = 1 + sum of children sizes
    //         uniform[v] = every child subtree is uniform AND every child's
    //                      colour equals colors[v]
    //       both are pure post-order facts, so one bottom-up pass computes them
    //       and the answer is the largest size among the uniform nodes.
    //       the walk is ITERATIVE: n reaches 5*10^4 and the tree may be a path.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int maximumSubtreeSize(int[][] edges, int[] colors) {
        int n = colors.length;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<Integer>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        // iterative DFS from root 0 -> parent array + top-down order
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

        int[] size = new int[n];
        boolean[] uniform = new boolean[n];
        Arrays.fill(size, 1);
        Arrays.fill(uniform, true);

        int res = 1;
        // reverse order -> children settled before their parent
        for (int idx = n - 1; idx >= 0; idx--) {
            int u = order[idx];
            for (int v : adj.get(u)) {
                if (v == parent[u]) {
                    continue;
                }
                size[u] += size[v];
                if (!uniform[v] || colors[v] != colors[u]) {
                    uniform[u] = false;
                }
            }
            if (uniform[u]) {
                res = Math.max(res, size[u]);
            }
        }

        return res;
    }
}
