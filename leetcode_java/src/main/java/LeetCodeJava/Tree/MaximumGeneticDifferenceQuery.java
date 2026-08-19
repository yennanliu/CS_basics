package LeetCodeJava.Tree;

// https://leetcode.com/problems/maximum-genetic-difference-query/

import java.util.Arrays;

/**
 *  1938. Maximum Genetic Difference Query
 *  Hard
 *
 *  There is a rooted tree consisting of n nodes numbered 0 to n - 1. Each
 *  node's number denotes its unique genetic value (i.e. the genetic value of
 *  node x is x). The genetic difference between two genetic values is defined
 *  as the bitwise-XOR of their values. You are given the integer array
 *  parents, where parents[i] is the parent for node i. If node x is the root
 *  of the tree, then parents[x] == -1.
 *
 *  You are also given the array queries where queries[i] = [nodei, vali]. For
 *  each query i, find the maximum genetic difference between vali and pi,
 *  where pi is the genetic value of any node that is on the path between nodei
 *  and the root (including nodei and the root). More formally, you want to
 *  maximize vali XOR pi.
 *
 *  Return an array ans where ans[i] is the answer to the ith query.
 *
 *  Example 1:
 *    Input: parents = [-1,0,1,1], queries = [[0,2],[3,2],[2,5]]
 *    Output: [2,3,7]
 *    Explanation: [0,2] -> 2 XOR 0 = 2; [3,2] -> 2 XOR 1 = 3;
 *                 [2,5] -> 5 XOR 2 = 7.
 *
 *  Example 2:
 *    Input: parents = [3,7,-1,2,0,7,0,2], queries = [[4,6],[1,15],[0,5]]
 *    Output: [6,14,7]
 *
 *  Constraints:
 *    2 <= parents.length <= 10^5
 *    parents[root] == -1
 *    1 <= queries.length <= 3 * 10^4
 *    0 <= nodei <= parents.length - 1
 *    0 <= vali <= 2 * 10^5
 */
public class MaximumGeneticDifferenceQuery {

    // V0
    // IDEA: OFFLINE DFS + BINARY TRIE HOLDING EXACTLY THE CURRENT ROOT PATH
    //       a query at node u may only use values on the path root -> u. so run
    //       ONE DFS over the tree and keep a bit-trie of the values currently
    //       on the stack:
    //         entering u : insert u (counter +1 along its 18-bit path), then
    //                      answer every query attached to u with a max-XOR walk
    //         leaving  u : delete u (counter -1) so siblings never see it
    //       max-XOR walk: from the top bit down, greedily follow the OPPOSITE
    //       bit of val whenever that branch still has a live counter (that sets
    //       the bit in the answer), otherwise follow the same bit.
    //       NOTE: REFERENCE COUNTS (not booleans) are what make the delete
    //             correct when several path values share a prefix.
    //       NOTE: depth can be 10^5, so the DFS is ITERATIVE with enter/exit
    //             markers on the stack.
    /**
     * time = O((N + Q) * 18)
     * space = O(N * 18)
     */
    private static final int BITS = 18;   // values fit in 2 * 10^5 < 2^18

    private int[] ch0;
    private int[] ch1;
    private int[] cnt;
    private int trieSize;

    public int[] maxGeneticDifference(int[] parents, int[][] queries) {
        int n = parents.length;
        int q = queries.length;

        int cap = n * BITS + 2;
        this.ch0 = new int[cap];
        this.ch1 = new int[cap];
        this.cnt = new int[cap];
        Arrays.fill(this.ch0, -1);
        Arrays.fill(this.ch1, -1);
        this.trieSize = 1;   // node 0 = trie root

        // children adjacency via head/next arrays
        int[] head = new int[n];
        int[] nxt = new int[n];
        Arrays.fill(head, -1);
        int root = 0;
        for (int i = 0; i < n; i++) {
            int p = parents[i];
            if (p == -1) {
                root = i;
            } else {
                nxt[i] = head[p];
                head[p] = i;
            }
        }

        // queries bucketed per node, also via head/next arrays
        int[] qHead = new int[n];
        int[] qNext = new int[q];
        Arrays.fill(qHead, -1);
        for (int i = 0; i < q; i++) {
            int node = queries[i][0];
            qNext[i] = qHead[node];
            qHead[node] = i;
        }

        int[] res = new int[q];
        // stack holds node*2 (enter) / node*2+1 (leave)
        int[] stack = new int[2 * n + 5];
        int sp = 0;
        stack[sp++] = root * 2;
        while (sp > 0) {
            int top = stack[--sp];
            int u = top >> 1;
            if ((top & 1) == 1) {
                update(u, -1);
                continue;
            }
            update(u, 1);
            for (int qi = qHead[u]; qi != -1; qi = qNext[qi]) {
                res[qi] = bestXor(queries[qi][1]);
            }
            stack[sp++] = u * 2 + 1;
            for (int c = head[u]; c != -1; c = nxt[c]) {
                stack[sp++] = c * 2;
            }
        }
        return res;
    }

    private void update(int x, int delta) {
        int cur = 0;
        for (int b = BITS - 1; b >= 0; b--) {
            int v = (x >> b) & 1;
            int child = (v == 0) ? this.ch0[cur] : this.ch1[cur];
            if (child == -1) {
                child = this.trieSize++;
                if (v == 0) {
                    this.ch0[cur] = child;
                } else {
                    this.ch1[cur] = child;
                }
            }
            cur = child;
            this.cnt[cur] += delta;
        }
    }

    private int bestXor(int x) {
        int cur = 0;
        int res = 0;
        for (int b = BITS - 1; b >= 0; b--) {
            int v = (x >> b) & 1;
            int want = v ^ 1;
            int nx = (want == 0) ? this.ch0[cur] : this.ch1[cur];
            if (nx != -1 && this.cnt[nx] > 0) {
                res |= 1 << b;
                cur = nx;
            } else {
                cur = (v == 0) ? this.ch0[cur] : this.ch1[cur];
            }
        }
        return res;
    }
}
