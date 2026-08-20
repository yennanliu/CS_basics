package LeetCodeJava.DFS;

// https://leetcode.com/problems/most-profitable-path-in-a-tree/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  2467. Most Profitable Path in a Tree
 *  Medium
 *
 *  There is an undirected tree with n nodes labeled from 0 to n - 1, rooted at
 *  node 0. You are given a 2D integer array edges of length n - 1 where
 *  edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi.
 *
 *  At every node i there is a gate. amount[i] is the price to open the gate at
 *  node i if amount[i] is negative, or the cash reward otherwise.
 *
 *  Initially Alice is at node 0 and Bob is at node bob. At every second Alice
 *  moves towards some leaf and Bob moves towards node 0. For every node on their
 *  path they either spend money or accept the reward; an already-open gate gives
 *  nothing. If they reach a node simultaneously they split the price / reward in
 *  half. Alice stops at a leaf, Bob stops at node 0.
 *
 *  Return the maximum net income Alice can have if she travels towards the
 *  optimal leaf node.
 *
 *  Example 1:
 *    Input: edges = [[0,1],[1,2],[1,3],[3,4]], bob = 3, amount = [-2,4,2,-4,6]
 *    Output: 6
 *    Explanation: Alice walks 0 -> 1 -> 3 -> 4 : -2 + 4/2 + 0 + 6 = 6.
 *
 *  Example 2:
 *    Input: edges = [[0,1]], bob = 1, amount = [-7280,2350]
 *    Output: -7280
 *    Explanation: Alice only opens the gate at node 0.
 *
 *  Constraints:
 *    2 <= n <= 10^5
 *    edges.length == n - 1
 *    edges[i].length == 2
 *    0 <= ai, bi < n
 *    ai != bi
 *    edges represents a valid tree.
 *    bob != 0
 *    0 <= bob < n
 *    amount.length == n
 *    amount[i] is an even integer in the range [-10^4, 10^4].
 */
public class MostProfitablePathInATree {

    // V0
    // IDEA: BOB'S ROUTE IS FIXED - TIME-STAMP IT, THEN LET ALICE PICK A LEAF
    //       Bob has no choice: the tree path from `bob` up to node 0 is unique,
    //       so record bobTime[v] = the second at which he opens gate v.
    //       Alice, standing on v at second t, then hits three cases:
    //         Bob never passes v, or arrives later -> Alice takes the full amount
    //         same second                          -> Alice takes half
    //         Bob got there first                  -> gate already open, 0
    //       (amounts are guaranteed EVEN, so the halving is exact.)
    //       the answer is the best running total over all leaves.
    //       both traversals are ITERATIVE (n up to 10^5).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int mostProfitablePath(int[][] edges, int bob, int[] amount) {
        int n = amount.length;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<Integer>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        // 1) BFS from the root -> parent array
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        boolean[] seen = new boolean[n];
        int[] queue = new int[n];
        int head = 0, tail = 0;
        queue[tail++] = 0;
        seen[0] = true;
        while (head < tail) {
            int u = queue[head++];
            for (int v : adj.get(u)) {
                if (!seen[v]) {
                    seen[v] = true;
                    parent[v] = u;
                    queue[tail++] = v;
                }
            }
        }

        // 2) stamp Bob's unique walk bob -> 0
        int[] bobTime = new int[n];
        Arrays.fill(bobTime, Integer.MAX_VALUE);
        int cur = bob, t = 0;
        while (cur != -1) {
            bobTime[cur] = t++;
            cur = parent[cur];
        }

        // 3) iterative DFS for Alice; carry (node, depth, income so far)
        int res = Integer.MIN_VALUE;
        int[] stNode = new int[n];
        int[] stDepth = new int[n];
        int[] stGain = new int[n];
        int sp = 0;
        stNode[sp] = 0;
        stDepth[sp] = 0;
        stGain[sp] = gainAt(0, 0, bobTime, amount);
        sp++;
        while (sp > 0) {
            sp--;
            int u = stNode[sp];
            int depth = stDepth[sp];
            int gain = stGain[sp];

            boolean isLeaf = true;
            for (int v : adj.get(u)) {
                if (v == parent[u]) {
                    continue;
                }
                isLeaf = false;
                stNode[sp] = v;
                stDepth[sp] = depth + 1;
                stGain[sp] = gain + gainAt(v, depth + 1, bobTime, amount);
                sp++;
            }
            if (isLeaf) {
                res = Math.max(res, gain);
            }
        }

        return res;
    }

    private int gainAt(int node, int aliceTime, int[] bobTime, int[] amount) {
        if (bobTime[node] < aliceTime) {
            // Bob already opened this gate
            return 0;
        }
        if (bobTime[node] == aliceTime) {
            return amount[node] / 2;
        }
        return amount[node];
    }
}
