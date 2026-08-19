package LeetCodeJava.Tree;

// https://leetcode.com/problems/number-of-ways-to-assign-edge-weights-i/

import java.util.Arrays;

/**
 *  3558. Number of Ways to Assign Edge Weights I
 *  Medium
 *
 *  There is an undirected tree with n nodes labeled from 1 to n, rooted at
 *  node 1. The tree is represented by a 2D integer array edges of length
 *  n - 1, where edges[i] = [u_i, v_i] indicates an edge between u_i and v_i.
 *
 *  Initially, all edges have a weight of 0. You must assign each edge a weight
 *  of either 1 or 2. The cost of a path between any two nodes u and v is the
 *  total weight of all edges in the path connecting them.
 *
 *  Select any one node x at the maximum depth. Return the number of ways to
 *  assign edge weights in the path from node 1 to x such that its total cost
 *  is odd. Since the answer may be large, return it modulo 10^9 + 7.
 *
 *  Note: Ignore all edges not in the path from node 1 to x.
 *
 *  Example 1:
 *    Input: edges = [[1,2]]
 *    Output: 1
 *    Explanation: weight 1 makes the cost odd, 2 makes it even -> 1 way.
 *
 *  Example 2:
 *    Input: edges = [[1,2],[1,3],[3,4],[3,5]]
 *    Output: 2
 *    Explanation: the max depth is 2 (nodes 4 and 5). For the 2-edge path,
 *                 (1,2) and (2,1) give an odd cost -> 2 ways.
 *
 *  Constraints:
 *    2 <= n <= 10^5
 *    edges.length == n - 1
 *    1 <= u_i, v_i <= n
 *    edges represents a valid tree.
 */
public class NumberOfWaysToAssignEdgeWeightsI {

    // V0
    // IDEA: ONLY THE PARITY MATTERS, SO EXACTLY HALF THE ASSIGNMENTS WORK
    //       weights are 1 or 2, so an edge flips the parity of the path cost
    //       only when it is given a 1 -> the cost is odd exactly when an odd
    //       number of the L path edges got a 1.
    //       the number of odd-sized subsets of L items is 2^(L-1) for every
    //       L >= 1 (fix the first L-1 edges freely, the last one is forced).
    //       so the whole problem reduces to finding L = the maximum depth,
    //       which one DFS/BFS from the root delivers. every deepest node gives
    //       the same L, which is why the statement lets any of them be picked.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int assignEdgeWeights(int[][] edges) {
        final long MOD = 1000000007L;
        int n = edges.length + 1;

        int[] head = new int[n + 1];
        int[] nxt = new int[2 * (n - 1 > 0 ? n - 1 : 1)];
        int[] to = new int[2 * (n - 1 > 0 ? n - 1 : 1)];
        Arrays.fill(head, -1);
        int ec = 0;
        for (int[] e : edges) {
            to[ec] = e[1]; nxt[ec] = head[e[0]]; head[e[0]] = ec++;
            to[ec] = e[0]; nxt[ec] = head[e[1]]; head[e[1]] = ec++;
        }

        int[] depth = new int[n + 1];
        boolean[] seen = new boolean[n + 1];
        int[] stack = new int[n + 1];
        int sp = 0;
        seen[1] = true;
        stack[sp++] = 1;
        int best = 0;
        while (sp > 0) {
            int u = stack[--sp];
            if (depth[u] > best) {
                best = depth[u];
            }
            for (int e = head[u]; e != -1; e = nxt[e]) {
                int v = to[e];
                if (!seen[v]) {
                    seen[v] = true;
                    depth[v] = depth[u] + 1;
                    stack[sp++] = v;
                }
            }
        }

        if (best == 0) {
            return 0;
        }
        return (int) modPow(2L, best - 1, MOD);
    }

    private long modPow(long base, long exp, long mod) {
        long r = 1L;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1L) == 1L) {
                r = r * base % mod;
            }
            base = base * base % mod;
            exp >>= 1;
        }
        return r;
    }
}
