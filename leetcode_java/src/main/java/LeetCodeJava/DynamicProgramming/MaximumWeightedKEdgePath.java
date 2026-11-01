package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-weighted-k-edge-path/description/

import java.util.*;

/**
 * 3543. Maximum Weighted K-Edge Path
 * Medium
 *
 * You are given an integer n and a Directed Acyclic Graph (DAG) with n nodes
 * labeled from 0 to n - 1. This is represented by a 2D array edges, where edges[i]
 * = [u_i, v_i, w_i] indicates a directed edge from node u_i to v_i with weight
 * w_i.
 *
 * You are also given two integers, k and t.
 *
 * Your task is to determine the maximum possible sum of edge weights for any path
 * in the graph such that:
 *
 * The path contains exactly k edges.
 *
 * The total sum of edge weights in the path is strictly less than t.
 *
 * Return the maximum possible sum of weights for such a path. If no such path
 * exists, return -1.
 *
 * Example 1:
 *
 * Input: n = 3, edges = [[0,1,1],[1,2,2]], k = 2, t = 4
 *
 * Output: 3
 *
 * Explanation:
 *
 * The only path with k = 2 edges is 0 -> 1 -> 2 with weight 1 + 2 = 3 < t.
 *
 * Thus, the maximum possible sum of weights less than t is 3.
 *
 * Example 2:
 *
 * Input: n = 3, edges = [[0,1,2],[0,2,3]], k = 1, t = 3
 *
 * Output: 2
 *
 * Explanation:
 *
 * There are two paths with k = 1 edge:
 *
 * 0 -> 1 with weight 2 < t.
 *
 * 0 -> 2 with weight 3 = t, which is not strictly less than t.
 *
 * Thus, the maximum possible sum of weights less than t is 2.
 *
 * Example 3:
 *
 * Input: n = 3, edges = [[0,1,6],[1,2,8]], k = 1, t = 6
 *
 * Output: -1
 *
 * Explanation:
 *
 * There are two paths with k = 1 edge:
 *
 * 0 -> 1 with weight 6 = t, which is not strictly less than t.
 *
 * 1 -> 2 with weight 8 > t, which is not strictly less than t.
 *
 * Since there is no path with sum of weights strictly less than t, the answer is
 * -1.
 *
 * Constraints:
 *
 * 1 <= n <= 300
 *
 * 0 <= edges.length <= 300
 *
 * edges[i] = [u_i, v_i, w_i]
 *
 * 0 <= u_i, v_i < n
 *
 * u_i != v_i
 *
 * 1 <= w_i <= 10
 *
 * 0 <= k <= 300
 *
 * 1 <= t <= 600
 *
 * The input graph is guaranteed to be a DAG.
 *
 * There are no duplicate edges.
 *
 */
public class MaximumWeightedKEdgePath {

    // V0
    // IDEA: LAYERED DP, STATE = (NODE, PATH SUM) AFTER s EDGES
    //   the path has exactly k edges, so walk in layers. After s edges, the set
    //   of sums a path ending at v can have matters (not just the max), because
    //   of the strict "< t" cap. Weights are positive, so any sum >= t is dead
    //   and can be dropped -> at most t values per node.
    //   reachable[v][w] = a path of s edges ending at v has total weight w
    //   next[v][w + weight] |= reachable[u][w]   for every edge (u, v, weight)
    /**
     * time = O(K * E * T)
     * space = O(N * T)
     */
    public int maxWeight(int n, int[][] edges, int k, int t) {
        // layer 0: every node starts a path of 0 edges with sum 0
        boolean[][] reachable = new boolean[n][t];
        for (int node = 0; node < n; node++) {
            reachable[node][0] = true;
        }

        for (int step = 0; step < k; step++) {
            boolean[][] next = new boolean[n][t];
            for (int[] edge : edges) {
                int from = edge[0];
                int to = edge[1];
                int weight = edge[2];
                // a sum that reaches t can never become valid again
                for (int sum = 0; sum + weight < t; sum++) {
                    if (reachable[from][sum]) {
                        next[to][sum + weight] = true;
                    }
                }
            }
            reachable = next;
        }

        // largest sum below t of any k-edge path
        int best = -1;
        for (int node = 0; node < n; node++) {
            for (int sum = t - 1; sum > best; sum--) {
                if (reachable[node][sum]) {
                    best = sum;
                    break;
                }
            }
        }
        return best;
    }
}
