package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-profit-from-valid-topological-order-in-dag/description/

import java.util.*;

/**
 * 3530. Maximum Profit from Valid Topological Order in DAG
 * Hard
 *
 * You are given a Directed Acyclic Graph (DAG) with n nodes labeled from 0 to n -
 * 1, represented by a 2D array edges, where edges[i] = [u_i, v_i] indicates a
 * directed edge from node u_i to v_i. Each node has an associated score given in
 * an array score, where score[i] represents the score of node i.
 *
 * You must process the nodes in a valid topological order. Each node is assigned a
 * 1-based position in the processing order.
 *
 * The profit is calculated by summing up the product of each node's score and its
 * position in the ordering.
 *
 * Return the maximum possible profit achievable with an optimal topological order.
 *
 * A topological order of a DAG is a linear ordering of its nodes such that for
 * every directed edge u → v, node u comes before v in the ordering.
 *
 * Example 1:
 *
 * Input: n = 2, edges = [[0,1]], score = [2,3]
 *
 * Output: 8
 *
 * Explanation:
 *
 * Node 1 depends on node 0, so a valid order is [0, 1].
 *
 * Node | Processing Order | Score | Multiplier | Profit Calculation
 * -----+------------------+-------+------------+-------------------
 *    0 | 1st              |     2 |          1 | 2 x 1 = 2
 *    1 | 2nd              |     3 |          2 | 3 x 2 = 6
 *
 * The maximum total profit achievable over all valid topological orders is 2 + 6 =
 * 8.
 *
 * Example 2:
 *
 * Input: n = 3, edges = [[0,1],[0,2]], score = [1,6,3]
 *
 * Output: 25
 *
 * Explanation:
 *
 * Nodes 1 and 2 depend on node 0, so the most optimal valid order is [0, 2, 1].
 *
 * Node | Processing Order | Score | Multiplier | Profit Calculation
 * -----+------------------+-------+------------+-------------------
 *    0 | 1st              |     1 |          1 | 1 x 1 = 1
 *    2 | 2nd              |     3 |          2 | 3 x 2 = 6
 *    1 | 3rd              |     6 |          3 | 6 x 3 = 18
 *
 * The maximum total profit achievable over all valid topological orders is 1 + 6 +
 * 18 = 25.
 *
 * Constraints:
 *
 * 1 <= n == score.length <= 22
 *
 * 1 <= score[i] <= 10^5
 *
 * 0 <= edges.length <= n * (n - 1) / 2
 *
 * edges[i] == [u_i, v_i] denotes a directed edge from u_i to v_i.
 *
 * 0 <= u_i, v_i < n
 *
 * u_i != v_i
 *
 * The input graph is guaranteed to be a DAG.
 *
 * There are no duplicate edges.
 *
 */
public class MaximumProfitFromValidTopologicalOrderInDAG {

    // V0
    // IDEA: BITMASK DP OVER THE SET OF PROCESSED NODES
    //   a partial order only needs to remember WHICH nodes are processed: the next
    //   node's position is popcount(mask) + 1, and the nodes that may come next
    //   depend on the set alone. n <= 22, so 2^n states are affordable.
    //   dp[mask] = best profit after processing exactly the nodes in mask
    //   dp[mask | bit(i)] = max(.., dp[mask] + score[i] * (popcount(mask) + 1))
    //   whenever every predecessor of i is already inside mask.
    /**
     * time = O(N * 2^N)
     * space = O(2^N)
     */
    public int maxProfit(int n, int[][] edges, int[] score) {
        // predecessors[v] = bitmask of the nodes that must come before v
        int[] predecessors = new int[n];
        for (int[] edge : edges) {
            predecessors[edge[1]] |= (1 << edge[0]);
        }

        int totalStates = 1 << n;
        int[] dp = new int[totalStates];
        // -1 marks a set of nodes that is not a valid prefix of any topological order
        Arrays.fill(dp, -1);
        dp[0] = 0;

        // mask | bit is always larger than mask, so increasing numeric order
        // processes every state after all the states that lead into it
        for (int mask = 0; mask < totalStates; mask++) {
            if (dp[mask] < 0) {
                continue;
            }
            int position = Integer.bitCount(mask) + 1;
            for (int node = 0; node < n; node++) {
                int nodeBit = 1 << node;
                boolean alreadyUsed = (mask & nodeBit) != 0;
                // node is ready when all of its predecessors are a subset of mask
                boolean ready = (predecessors[node] & mask) == predecessors[node];
                if (alreadyUsed || !ready) {
                    continue;
                }
                int nextMask = mask | nodeBit;
                int candidate = dp[mask] + score[node] * position;
                if (candidate > dp[nextMask]) {
                    dp[nextMask] = candidate;
                }
            }
        }
        return dp[totalStates - 1];
    }
}
