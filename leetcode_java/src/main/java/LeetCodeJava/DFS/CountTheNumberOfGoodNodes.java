package LeetCodeJava.DFS;

// https://leetcode.com/problems/count-the-number-of-good-nodes/

import java.util.ArrayList;
import java.util.List;

/**
 *  3249. Count the Number of Good Nodes
 *  Medium
 *
 *  There is an undirected tree with n nodes labeled from 0 to n - 1, and rooted at
 *  node 0. You are given a 2D integer array edges of length n - 1, where
 *  edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi.
 *
 *  A node is good if all the subtrees rooted at its children have the same size.
 *
 *  Return the number of good nodes in the given tree.
 *
 *  Example 1:
 *    Input: edges = [[0,1],[0,2],[1,3],[1,4],[2,5],[2,6]]
 *    Output: 7
 *    Explanation: all of the nodes of the given tree are good.
 *
 *  Example 2:
 *    Input: edges = [[0,1],[1,2],[2,3],[3,4],[0,5],[1,6],[2,7],[3,8]]
 *    Output: 6
 *
 *  Constraints:
 *    2 <= n <= 10^5
 *    edges.length == n - 1
 *    edges[i].length == 2
 *    0 <= edges[i][0], edges[i][1] <= n - 1
 *    The input is generated such that edges represents a valid tree.
 */
public class CountTheNumberOfGoodNodes {

    // V0
    // IDEA: POST-ORDER SUBTREE SIZES, THEN COMPARE EACH NODE'S CHILDREN
    //       subtree sizes come from one bottom-up pass:  size[u] = 1 + sum(children)
    //       and a node is good when all its children carry the SAME size. Leaves have
    //       no children, so they are vacuously good.
    //       the traversal is ITERATIVE (a 10^5-node path would blow the stack): a BFS
    //       order has every parent before its children, so walking it BACKWARDS is a
    //       valid bottom-up order.
    /**
     * time = O(n)
     * space = O(n)
     */
    public int countGoodNodes(int[][] edges) {

        int n = edges.length + 1;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<Integer>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        // top-down order + parents
        int[] order = new int[n];
        int[] parent = new int[n];
        parent[0] = -1;
        boolean[] seen = new boolean[n];
        seen[0] = true;

        int head = 0;
        int tail = 0;
        order[tail++] = 0;
        while (head < tail) {
            int cur = order[head++];
            for (Integer nxt : adj.get(cur)) {
                if (!seen[nxt]) {
                    seen[nxt] = true;
                    parent[nxt] = cur;
                    order[tail++] = nxt;
                }
            }
        }

        // bottom-up subtree sizes
        int[] size = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            int cur = order[i];
            size[cur] += 1;
            if (parent[cur] != -1) {
                size[parent[cur]] += size[cur];
            }
        }

        // a node is good when every child subtree has the same size
        int res = 0;
        for (int u = 0; u < n; u++) {
            int first = -1;
            boolean good = true;
            for (Integer v : adj.get(u)) {
                if (v == parent[u]) {
                    continue;
                }
                if (first == -1) {
                    first = size[v];
                } else if (size[v] != first) {
                    good = false;
                    break;
                }
            }
            if (good) {
                res++;
            }
        }

        return res;
    }
}
