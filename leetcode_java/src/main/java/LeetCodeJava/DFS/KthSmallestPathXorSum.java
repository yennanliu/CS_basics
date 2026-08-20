package LeetCodeJava.DFS;

// https://leetcode.com/problems/kth-smallest-path-xor-sum/

import java.util.ArrayList;
import java.util.List;

/**
 *  3590. Kth Smallest Path XOR Sum
 *  Hard
 *
 *  You are given an undirected tree rooted at node 0 with n nodes numbered from 0 to
 *  n - 1. Each node i has an integer value vals[i], and its parent is given by par[i].
 *
 *  The path XOR sum from the root to a node u is the bitwise XOR of all vals[i] for
 *  nodes i on the path from the root node to node u, inclusive.
 *
 *  You are given a 2D integer array queries, where queries[j] = [u_j, k_j]. For each
 *  query, find the k_j-th smallest DISTINCT path XOR sum among all nodes in the subtree
 *  rooted at u_j. If there are fewer than k_j distinct path XOR sums in that subtree,
 *  the answer is -1.
 *
 *  Return an integer array where the j-th element is the answer to the j-th query.
 *
 *  Example 1:
 *    Input: par = [-1,0,0], vals = [1,1,1], queries = [[0,1],[0,2],[0,3]]
 *    Output: [0,1,-1]
 *    Explanation: path XORs are [1,0,0]; the distinct sorted set for subtree 0 is [0,1].
 *
 *  Example 2:
 *    Input: par = [-1,0,1], vals = [5,2,7], queries = [[0,1],[1,2],[1,3],[2,1]]
 *    Output: [0,7,-1,0]
 *
 *  Constraints:
 *    1 <= n == vals.length <= 5 * 10^4
 *    0 <= vals[i] <= 10^5
 *    par.length == n, par[0] == -1
 *    0 <= par[i] < n for i in [1, n - 1]
 *    1 <= queries.length <= 5 * 10^4
 *    queries[j] == [u_j, k_j]
 *    0 <= u_j < n
 *    1 <= k_j <= n
 *    The input is generated such that par represents a valid tree.
 */
public class KthSmallestPathXorSum {

    // V0
    // IDEA: MERGEABLE BINARY TRIE OVER THE XOR VALUES, MERGED BOTTOM-UP
    //       answering a query needs an order statistic over the DISTINCT path xors of
    //       one subtree, and a subtree's set is just the union of its children's plus
    //       its own value. So use a structure that can be unioned cheaply rather than
    //       rebuilt: a binary trie on the 17 bits of the value (vals[i] <= 10^5 < 2^17,
    //       and xor keeps values in range), each node storing how many DISTINCT values
    //       live beneath it.
    //       the union is the segment-tree-merge trick: merging two tries walks only the
    //       nodes they share and splices the rest in whole. Every recursive step
    //       permanently destroys one node, so the total work over the whole traversal is
    //       bounded by the number of nodes ever created, O(n * 17), however lopsided the
    //       tree is - strictly better than small-to-large, which re-inserts repeatedly.
    //       distinctness falls out at the bottom of the merge: when both sides reach the
    //       same leaf they are the same value, so one is dropped and the count stays 1;
    //       every internal count is then recomputed from its two children.
    //       with counts in place the k-th smallest is a descent from the root: the zero
    //       branch covers the smaller half, so if k fits inside its count go left,
    //       otherwise subtract that count, set the bit and go right.
    //       queries are answered OFFLINE, grouped by node, at the moment that node's
    //       merge finishes - so each trie is consumed exactly once.
    //       the traversal is ITERATIVE (a path-shaped 5*10^4-node tree).
    /**
     * time = O((n + q) * log(maxVal))
     * space = O(n * log(maxVal))
     */
    private static final int BITS = 17;

    private int[] zero;
    private int[] one;
    private int[] cnt;
    private int used;

    public int[] kthSmallest(int[] par, int[] vals, int[][] queries) {

        int n = par.length;

        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<Integer>());
        }
        for (int i = 1; i < n; i++) {
            children.get(par[i]).add(i);
        }

        // pre-order sweep gives every node's xor from the root
        int[] xor = new int[n];
        int[] order = new int[n];
        int orderSize = 0;
        int[] stack = new int[n];
        int top = 0;

        xor[0] = vals[0];
        stack[top++] = 0;
        while (top > 0) {
            int x = stack[--top];
            order[orderSize++] = x;
            for (Integer y : children.get(x)) {
                xor[y] = xor[x] ^ vals[y];
                stack[top++] = y;
            }
        }

        // flat trie storage; node 0 is the shared null node with count 0
        int cap = n * (BITS + 1) + 1;
        zero = new int[cap];
        one = new int[cap];
        cnt = new int[cap];
        used = 1;

        // group the queries by node (offline)
        List<List<int[]>> asked = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            asked.add(new ArrayList<int[]>());
        }
        for (int i = 0; i < queries.length; i++) {
            asked.get(queries[i][0]).add(new int[]{queries[i][1], i});
        }

        int[] ans = new int[queries.length];
        int[] rootOf = new int[n];

        // reverse pre-order is a valid post-order
        for (int i = orderSize - 1; i >= 0; i--) {
            int x = order[i];
            int r = insert(xor[x]);
            for (Integer y : children.get(x)) {
                r = merge(r, rootOf[y], BITS);
                rootOf[y] = 0;
            }
            rootOf[x] = r;
            for (int[] q : asked.get(x)) {
                ans[q[1]] = kth(r, q[0]);
            }
        }

        return ans;
    }

    private int insert(int v) {
        int free = used;
        int root = free;
        for (int b = BITS - 1; b >= 0; b--) {
            cnt[free] = 1;
            int nxt = free + 1;
            if (((v >> b) & 1) == 1) {
                one[free] = nxt;
            } else {
                zero[free] = nxt;
            }
            free = nxt;
        }
        cnt[free] = 1;
        used = free + 1;
        return root;
    }

    private int merge(int a, int b, int bit) {
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        if (bit == 0) {
            return a; // same leaf, same value: keep one copy
        }
        zero[a] = merge(zero[a], zero[b], bit - 1);
        one[a] = merge(one[a], one[b], bit - 1);
        cnt[a] = cnt[zero[a]] + cnt[one[a]];
        return a;
    }

    private int kth(int root, int k) {
        if (cnt[root] < k) {
            return -1;
        }
        int node = root;
        int res = 0;
        for (int b = BITS - 1; b >= 0; b--) {
            int lo = zero[node];
            int c = cnt[lo];
            if (k <= c) {
                node = lo;
            } else {
                k -= c;
                res |= 1 << b;
                node = one[node];
            }
        }
        return res;
    }
}
