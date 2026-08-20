package LeetCodeJava.Sort;

// https://leetcode.com/problems/maximum-star-sum-of-a-graph/

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  2497. Maximum Star Sum of a Graph
 *  Medium
 *
 *  There is an undirected graph consisting of n nodes numbered from 0 to n - 1.
 *  You are given a 0-indexed integer array vals of length n where vals[i] denotes
 *  the value of the ith node.
 *
 *  You are also given a 2D integer array edges where edges[i] = [ai, bi] denotes
 *  that there exists an undirected edge connecting nodes ai and bi.
 *
 *  A star graph is a subgraph of the given graph having a center node containing
 *  0 or more neighbors. The star sum is the sum of the values of all the nodes
 *  present in the star graph.
 *
 *  Given an integer k, return the maximum star sum of a star graph containing at
 *  most k edges.
 *
 *  Example 1:
 *    Input: vals = [1,2,3,4,10,-10,-20],
 *           edges = [[0,1],[1,2],[1,3],[3,4],[3,5],[3,6]], k = 2
 *    Output: 16
 *    Explanation: the best star is centered at 3 and includes its neighbors 1 and 4.
 *
 *  Example 2:
 *    Input: vals = [-5], edges = [], k = 0
 *    Output: -5
 *
 *  Constraints:
 *    n == vals.length
 *    1 <= n <= 10^5
 *    -10^4 <= vals[i] <= 10^4
 *    0 <= edges.length <= min(n * (n - 1) / 2, 10^5)
 *    edges[i].length == 2
 *    0 <= ai, bi <= n - 1, ai != bi
 *    0 <= k <= n - 1
 */
public class MaximumStarSumOfAGraph {

    // V0
    // IDEA: PER-CENTER GREEDY - KEEP ONLY POSITIVE NEIGHBOURS, SORT DESC, TAKE TOP k
    //       the centers are independent: for a fixed center i the star sum is
    //       vals[i] + (sum of chosen neighbours' values) with any subset of size
    //       <= k allowed. so pick the k LARGEST neighbour values, and only the
    //       positive ones (a negative neighbour never helps).
    //
    //       NOTE: "at most k edges" means a lone center (0 edges) is a valid star,
    //             so the answer can be negative -> start from -inf, never 0.
    /**
     * time = O(V + E log E)
     * space = O(V + E)
     */
    public int maxStarSum(int[] vals, int[][] edges, int k) {
        int n = vals.length;

        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            int a = e[0];
            int b = e[1];
            if (vals[b] > 0) {
                g.get(a).add(vals[b]);
            }
            if (vals[a] > 0) {
                g.get(b).add(vals[a]);
            }
        }

        int res = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            List<Integer> nei = g.get(i);
            Collections.sort(nei, Collections.reverseOrder());
            int cur = vals[i];
            for (int j = 0; j < k && j < nei.size(); j++) {
                cur += nei.get(j);
            }
            res = Math.max(res, cur);
        }
        return res;
    }
}
