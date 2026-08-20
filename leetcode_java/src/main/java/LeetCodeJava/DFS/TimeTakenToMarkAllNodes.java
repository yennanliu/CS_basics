package LeetCodeJava.DFS;

// https://leetcode.com/problems/time-taken-to-mark-all-nodes/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  3241. Time Taken to Mark All Nodes
 *  Hard
 *
 *  There exists an undirected tree with n nodes numbered 0 to n - 1. You are
 *  given a 2D integer array edges of length n - 1, where edges[i] = [ui, vi]
 *  indicates that there is an edge between nodes ui and vi in the tree.
 *
 *  Initially, all nodes are unmarked. For each node i:
 *    - If i is odd, the node gets marked at time x if there is at least one node
 *      adjacent to it which was marked at time x - 1.
 *    - If i is even, the node gets marked at time x if there is at least one
 *      node adjacent to it which was marked at time x - 2.
 *
 *  Return an array times where times[i] is the time when all nodes get marked in
 *  the tree, if you mark node i at time t = 0. The answers are independent.
 *
 *  Example 1:
 *    Input: edges = [[0,1],[0,2]]
 *    Output: [2,4,3]
 *    Explanation: for i = 0, node 1 is marked at t = 1 and node 2 at t = 2.
 *
 *  Example 3:
 *    Input: edges = [[2,4],[0,1],[2,3],[0,2]]
 *    Output: [4,6,3,5,5]
 *
 *  Constraints:
 *    2 <= n <= 10^5
 *    edges.length == n - 1
 *    edges[i].length == 2
 *    0 <= edges[i][0], edges[i][1] <= n - 1
 *    The input is generated such that edges represents a valid tree.
 */
public class TimeTakenToMarkAllNodes {

    // V0
    // IDEA: REROOTING - ONE DOWNWARD PASS, THEN PUSH THE ANSWER OUTWARDS
    //       marking spreads along edges with a per-node delay: ENTERING node v
    //       costs 1 if v is odd and 2 if it is even. from a fixed root the
    //       finishing time is the deepest weighted path:
    //           down[u] = max over children v of (down[v] + cost(v))
    //       answering for EVERY start would be n separate passes, so REROOT:
    //       carry up[u] = the best time reachable by leaving u through its
    //       parent, and combine it with the siblings when descending. a child
    //       must not see its OWN contribution, which is why the TOP TWO child
    //       values are kept - the best one for every child except the one that
    //       produced it, which falls back to the runner-up.
    //           up[v] = cost(u) + max(up[u], bestOther(u, v))
    //           times[u] = max(down[u], up[u])
    //       both passes are ITERATIVE (n up to 10^5).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] timeTaken(int[][] edges) {
        int n = edges.length + 1;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<Integer>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        // BFS order from root 0 -> parents always before children
        int[] parent = new int[n];
        int[] order = new int[n];
        Arrays.fill(parent, -1);
        boolean[] seen = new boolean[n];
        int head = 0, tail = 0;
        order[tail++] = 0;
        seen[0] = true;
        while (head < tail) {
            int u = order[head++];
            for (int v : adj.get(u)) {
                if (!seen[v]) {
                    seen[v] = true;
                    parent[v] = u;
                    order[tail++] = v;
                }
            }
        }

        int[] down = new int[n];
        int[] best1 = new int[n];      // largest (down[c] + cost(c))
        int[] best1Kid = new int[n];   // the child that produced best1
        int[] best2 = new int[n];      // runner up
        Arrays.fill(best1Kid, -1);

        // downward pass: reverse BFS order -> children before parents
        for (int idx = n - 1; idx >= 0; idx--) {
            int u = order[idx];
            for (int v : adj.get(u)) {
                if (v == parent[u]) {
                    continue;
                }
                int cand = down[v] + cost(v);
                if (cand > best1[u]) {
                    best2[u] = best1[u];
                    best1[u] = cand;
                    best1Kid[u] = v;
                } else if (cand > best2[u]) {
                    best2[u] = cand;
                }
            }
            down[u] = best1[u];
        }

        // rerooting pass: BFS order -> a parent's `up` is ready before its kids
        int[] up = new int[n];
        int[] res = new int[n];
        for (int idx = 0; idx < n; idx++) {
            int u = order[idx];
            res[u] = Math.max(down[u], up[u]);
            for (int v : adj.get(u)) {
                if (v == parent[u]) {
                    continue;
                }
                int bestOther = (best1Kid[u] == v) ? best2[u] : best1[u];
                up[v] = cost(u) + Math.max(up[u], bestOther);
            }
        }

        return res;
    }

    // entering node v costs 1 when v is odd, 2 when v is even
    private int cost(int v) {
        return (v % 2 == 1) ? 1 : 2;
    }
}
