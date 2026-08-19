package LeetCodeJava.Tree;

// https://leetcode.com/problems/number-of-good-paths/

import java.util.Arrays;
import java.util.Comparator;

/**
 *  2421. Number of Good Paths
 *  Hard
 *
 *  There is a tree (i.e. a connected, undirected graph with no cycles)
 *  consisting of n nodes numbered from 0 to n - 1 and exactly n - 1 edges.
 *
 *  You are given a 0-indexed integer array vals of length n where vals[i]
 *  denotes the value of the ith node. You are also given a 2D integer array
 *  edges where edges[i] = [ai, bi] denotes an undirected edge connecting
 *  nodes ai and bi.
 *
 *  A good path is a simple path that satisfies:
 *    - The starting node and the ending node have the same value.
 *    - All nodes between the starting node and the ending node have values
 *      less than or equal to the starting node's value.
 *
 *  Return the number of distinct good paths. A path and its reverse count as
 *  the same path. A single node is also a valid path.
 *
 *  Example 1:
 *    Input: vals = [1,3,2,1,3], edges = [[0,1],[0,2],[2,3],[2,4]]
 *    Output: 6
 *    Explanation: 5 single-node paths, plus 1 -> 0 -> 2 -> 4.
 *
 *  Example 2:
 *    Input: vals = [1,1,2,2,3], edges = [[0,1],[1,2],[2,3],[2,4]]
 *    Output: 7
 *    Explanation: 5 single-node paths, plus 0 -> 1 and 2 -> 3.
 *
 *  Constraints:
 *    n == vals.length
 *    1 <= n <= 3 * 10^4
 *    0 <= vals[i] <= 10^5
 *    edges.length == n - 1
 *    edges represents a valid tree.
 */
public class NumberOfGoodPaths {

    // V0
    // IDEA: UNION-FIND, ADDING EDGES IN INCREASING "MAX ENDPOINT VALUE" ORDER
    //       sort the edges by max(vals[a], vals[b]) and union in that order.
    //       invariant: when an edge with key v is processed, every node already
    //       in either component has value <= v (anything larger could only be
    //       attached by a later edge).
    //       so per component track
    //           top[root] = the component's maximum value
    //           cnt[root] = how many of its nodes hold that maximum
    //       merging two components with EQUAL maxima pairs every top node of
    //       one with every top node of the other through the new edge:
    //           res += cnt[ra] * cnt[rb]
    //       otherwise the smaller-max component contributes nothing and is
    //       simply absorbed. the n single-node paths are counted up front.
    /**
     * time = O(N * log N)
     * space = O(N)
     */
    private int[] parent;

    public int numberOfGoodPaths(int[] vals, int[][] edges) {
        final int n = vals.length;
        this.parent = new int[n];
        int[] top = new int[n];
        int[] cnt = new int[n];
        for (int i = 0; i < n; i++) {
            this.parent[i] = i;
            top[i] = vals[i];
            cnt[i] = 1;
        }

        final int[] v = vals;
        Integer[] order = new Integer[edges.length];
        for (int i = 0; i < edges.length; i++) {
            order[i] = i;
        }
        Arrays.sort(order, new Comparator<Integer>() {
            @Override
            public int compare(Integer x, Integer y) {
                int kx = Math.max(v[edges[x][0]], v[edges[x][1]]);
                int ky = Math.max(v[edges[y][0]], v[edges[y][1]]);
                return Integer.compare(kx, ky);
            }
        });

        int res = n;   // every single node is a good path
        for (int idx = 0; idx < order.length; idx++) {
            int a = edges[order[idx]][0];
            int b = edges[order[idx]][1];
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) {
                continue;
            }
            if (top[ra] == top[rb]) {
                res += cnt[ra] * cnt[rb];
                this.parent[rb] = ra;
                cnt[ra] += cnt[rb];
            } else if (top[ra] > top[rb]) {
                this.parent[rb] = ra;
            } else {
                this.parent[ra] = rb;
            }
        }
        return res;
    }

    private int find(int x) {
        while (this.parent[x] != x) {
            this.parent[x] = this.parent[this.parent[x]];   // path halving
            x = this.parent[x];
        }
        return x;
    }
}
