package LeetCodeJava.DFS;

// https://leetcode.com/problems/count-number-of-possible-root-nodes/

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  2581. Count Number of Possible Root Nodes
 *  Hard
 *
 *  Alice has an undirected tree with n nodes labeled from 0 to n - 1. The tree is
 *  represented as a 2D integer array edges of length n - 1 where edges[i] = [ai, bi]
 *  indicates that there is an edge between nodes ai and bi in the tree.
 *
 *  Alice wants Bob to find the root of the tree. She allows Bob to make several
 *  guesses. In one guess he chooses two distinct integers u and v such that there
 *  exists an edge [u, v] in the tree, and tells Alice that u is the parent of v.
 *
 *  Bob's guesses are represented by a 2D integer array guesses where
 *  guesses[j] = [uj, vj] indicates Bob guessed uj to be the parent of vj.
 *
 *  Alice, being lazy, does not reply to each guess but just says that at least k of
 *  his guesses are true.
 *
 *  Given edges, guesses and the integer k, return the number of possible nodes that
 *  can be the root of Alice's tree. If there is no such tree, return 0.
 *
 *  Example 1:
 *    Input: edges = [[0,1],[1,2],[1,3],[4,2]], guesses = [[1,3],[0,1],[1,0],[2,4]], k = 3
 *    Output: 3
 *    Explanation: rooting at 0, 1 or 2 makes 3 guesses correct.
 *
 *  Example 2:
 *    Input: edges = [[0,1],[1,2],[2,3],[3,4]], guesses = [[1,0],[3,4],[2,1],[3,2]], k = 1
 *    Output: 5
 *
 *  Constraints:
 *    edges.length == n - 1
 *    2 <= n <= 10^5
 *    1 <= guesses.length <= 10^5
 *    0 <= ai, bi, uj, vj <= n - 1
 *    ai != bi, uj != vj
 *    edges represents a valid tree, guesses[j] is an edge of the tree, guesses is unique.
 *    0 <= k <= guesses.length
 */
public class CountNumberOfPossibleRootNodes {

    // V0
    // IDEA: REROOTING TREE DP (2 iterative passes)
    //       score(r) = how many guesses (u, v) really are "u is parent of v" when the
    //       tree is rooted at r. Recomputing per r is O(n^2). But moving the root
    //       along ONE edge flips only that edge's parent/child relation:
    //         score(child) = score(parent)
    //                        - ((parent, child) is a guess)   // this guess dies
    //                        + ((child, parent) is a guess)   // this guess is born
    //       pass 1 : root at 0, count score(0) directly.
    //       pass 2 : walk down from 0 propagating the delta, counting score >= k.
    //       guesses are unique -> a plain SET of encoded (u, v) pairs is enough.
    //       both passes are ITERATIVE (n reaches 10^5, the tree can be a path).
    /**
     * time = O(n + g)
     * space = O(n + g)
     */
    public int rootCount(int[][] edges, int[][] guesses, int k) {

        int n = edges.length + 1;

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<Integer>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        Set<Long> guessSet = new HashSet<>();
        for (int[] g : guesses) {
            guessSet.add(encode(g[0], g[1], n));
        }

        // ---- pass 1 : score when rooted at 0, plus a top-down node order ----
        int[] parent = new int[n];
        int[] order = new int[n];
        parent[0] = -1;

        int head = 0;
        int tail = 0;
        order[tail++] = 0;
        boolean[] seen = new boolean[n];
        seen[0] = true;

        int score0 = 0;
        while (head < tail) {
            int cur = order[head++];
            for (Integer nxt : adj.get(cur)) {
                if (!seen[nxt]) {
                    seen[nxt] = true;
                    parent[nxt] = cur;
                    order[tail++] = nxt;
                    if (guessSet.contains(encode(cur, nxt, n))) {
                        score0++;
                    }
                }
            }
        }

        // ---- pass 2 : reroot along the same order (parents come first) ----
        int[] score = new int[n];
        score[0] = score0;

        int res = score0 >= k ? 1 : 0;
        for (int i = 1; i < n; i++) {
            int cur = order[i];
            int p = parent[cur];
            int s = score[p];
            if (guessSet.contains(encode(p, cur, n))) {
                s--;
            }
            if (guessSet.contains(encode(cur, p, n))) {
                s++;
            }
            score[cur] = s;
            if (s >= k) {
                res++;
            }
        }

        return res;
    }

    private long encode(int u, int v, int n) {
        return (long) u * n + v;
    }
}
