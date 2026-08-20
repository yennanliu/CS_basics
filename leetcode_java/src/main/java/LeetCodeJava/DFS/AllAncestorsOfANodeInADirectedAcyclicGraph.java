package LeetCodeJava.DFS;

// https://leetcode.com/problems/all-ancestors-of-a-node-in-a-directed-acyclic-graph/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  2192. All Ancestors of a Node in a Directed Acyclic Graph
 *  Medium
 *
 *  You are given a positive integer n representing the number of nodes of a Directed
 *  Acyclic Graph (DAG). The nodes are numbered from 0 to n - 1 (inclusive).
 *
 *  You are also given a 2D integer array edges, where edges[i] = [from_i, to_i] denotes
 *  that there is a unidirectional edge from from_i to to_i in the graph.
 *
 *  Return a list answer, where answer[i] is the list of ancestors of the ith node,
 *  sorted in ascending order.
 *
 *  A node u is an ancestor of another node v if u can reach v via a set of edges.
 *
 *  Example 1:
 *    Input: n = 8, edgeList = [[0,3],[0,4],[1,3],[2,4],[2,7],[3,5],[3,6],[3,7],[4,6]]
 *    Output: [[],[],[],[0,1],[0,2],[0,1,3],[0,1,2,3,4],[0,1,2,3]]
 *    Explanation: Nodes 0, 1, 2 have no ancestors; node 3 has ancestors 0 and 1; ...
 *
 *  Example 2:
 *    Input: n = 5, edgeList = [[0,1],[0,2],[0,3],[0,4],[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
 *    Output: [[],[0],[0,1],[0,1,2],[0,1,2,3]]
 *
 *  Constraints:
 *    1 <= n <= 1000
 *    0 <= edges.length <= min(2000, n * (n - 1) / 2)
 *    edges[i].length == 2
 *    0 <= from_i, to_i <= n - 1
 *    from_i != to_i
 *    There are no duplicate edges.
 *    The graph is a DAG.
 */
public class AllAncestorsOfANodeInADirectedAcyclicGraph {

    // V0
    // IDEA: ONE DFS PER SOURCE -- MARK THAT SOURCE ON EVERYTHING IT REACHES
    //       "u is an ancestor of v" is just "u reaches v", so run a DFS from every
    //       node u and append u to the answer list of each node it can reach.
    //       Looping u ASCENDING builds every answer list already sorted -> no sort.
    /**
     * time = O(V * (V + E))
     * space = O(V + E)
     */
    public List<List<Integer>> getAncestors(int n, int[][] edges) {

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<Integer>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
        }

        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            res.add(new ArrayList<Integer>());
        }

        boolean[] seen = new boolean[n];
        int[] stack = new int[n];

        for (int src = 0; src < n; src++) {

            Arrays.fill(seen, false);
            seen[src] = true;

            int top = 0;
            stack[top++] = src;

            while (top > 0) {
                int cur = stack[--top];
                for (Integer nxt : adj.get(cur)) {
                    if (!seen[nxt]) {
                        seen[nxt] = true;
                        // src reaches nxt -> src is an ancestor of nxt
                        res.get(nxt).add(src);
                        stack[top++] = nxt;
                    }
                }
            }
        }

        return res;
    }
}
