package LeetCodeJava.DFS;

// https://leetcode.com/problems/minimum-score-after-removals-on-a-tree/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  2322. Minimum Score After Removals on a Tree
 *  Hard
 *
 *  There is an undirected connected tree with n nodes labeled from 0 to n - 1
 *  and n - 1 edges.
 *
 *  You are given a 0-indexed integer array nums of length n where nums[i]
 *  represents the value of the ith node. You are also given a 2D integer array
 *  edges of length n - 1 where edges[i] = [ai, bi] indicates that there is an
 *  edge between nodes ai and bi in the tree.
 *
 *  Remove two distinct edges of the tree to form three connected components.
 *  Get the XOR of all the values of the nodes for each of the three components;
 *  the difference between the largest XOR value and the smallest XOR value is
 *  the score of the pair.
 *
 *  Return the minimum score of any possible pair of edge removals.
 *
 *  Example 1:
 *    Input: nums = [1,5,5,4,11], edges = [[0,1],[1,2],[1,3],[3,4]]
 *    Output: 9
 *    Explanation: components {1,3,4} -> 10, {0} -> 1, {2} -> 5; 10 - 1 = 9.
 *
 *  Example 2:
 *    Input: nums = [5,5,2,4,4,2], edges = [[0,1],[1,2],[5,2],[4,3],[1,3]]
 *    Output: 0
 *    Explanation: all three components XOR to 0.
 *
 *  Constraints:
 *    n == nums.length
 *    3 <= n <= 1000
 *    1 <= nums[i] <= 10^8
 *    edges.length == n - 1
 *    edges[i].length == 2
 *    0 <= ai, bi < n
 *    ai != bi
 *    edges represents a valid tree.
 */
public class MinimumScoreAfterRemovalsOnATree {

    // V0
    // IDEA: ROOT THE TREE, THEN AN EDGE IS JUST "THE SUBTREE BELOW IT"
    //       root at 0. cutting the edge above node u detaches exactly u's
    //       subtree, so a pair of cuts is a pair of NON-ROOT nodes (u, v). the
    //       three XORs depend on whether the subtrees nest:
    //         v inside u : sub[v], sub[u] ^ sub[v], total ^ sub[u]
    //         u inside v : the mirror
    //         disjoint   : sub[u], sub[v], total ^ sub[u] ^ sub[v]
    //       nesting is tested in O(1) with DFS entry / exit stamps.
    //       n <= 1000 -> scanning all ~n^2/2 pairs is fine.
    //       the DFS is ITERATIVE (a path-shaped tree of 1000 nodes is deep).
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int minimumScore(int[] nums, int[][] edges) {
        int n = nums.length;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<Integer>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        int[] parent = new int[n];
        int[] order = new int[n];
        int[] tin = new int[n];
        int[] tout = new int[n];
        Arrays.fill(parent, -1);

        // iterative DFS with entry / exit stamps
        boolean[] seen = new boolean[n];
        int[] stack = new int[2 * n];
        boolean[] isExit = new boolean[2 * n];
        int sp = 0, cnt = 0, timer = 0;
        stack[sp] = 0;
        isExit[sp] = false;
        sp++;
        seen[0] = true;
        while (sp > 0) {
            sp--;
            int u = stack[sp];
            if (isExit[sp]) {
                tout[u] = timer++;
                continue;
            }
            tin[u] = timer++;
            order[cnt++] = u;
            // push the exit marker first so it pops after all children
            stack[sp] = u;
            isExit[sp] = true;
            sp++;
            for (int v : adj.get(u)) {
                if (!seen[v]) {
                    seen[v] = true;
                    parent[v] = u;
                    stack[sp] = v;
                    isExit[sp] = false;
                    sp++;
                }
            }
        }

        // sub[u] = XOR of u's whole subtree
        int[] sub = new int[n];
        for (int i = 0; i < n; i++) {
            sub[i] = nums[i];
        }
        for (int idx = n - 1; idx >= 1; idx--) {
            int u = order[idx];
            sub[parent[u]] ^= sub[u];
        }
        int total = sub[0];

        int res = Integer.MAX_VALUE;
        for (int i = 1; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int a, b, c;
                if (isAncestor(tin, tout, i, j)) {
                    // j sits inside i's subtree
                    a = sub[j];
                    b = sub[i] ^ sub[j];
                    c = total ^ sub[i];
                } else if (isAncestor(tin, tout, j, i)) {
                    a = sub[i];
                    b = sub[j] ^ sub[i];
                    c = total ^ sub[j];
                } else {
                    a = sub[i];
                    b = sub[j];
                    c = total ^ sub[i] ^ sub[j];
                }
                int max = Math.max(a, Math.max(b, c));
                int min = Math.min(a, Math.min(b, c));
                res = Math.min(res, max - min);
            }
        }

        return res;
    }

    private boolean isAncestor(int[] tin, int[] tout, int u, int v) {
        return tin[u] < tin[v] && tout[v] < tout[u];
    }
}
