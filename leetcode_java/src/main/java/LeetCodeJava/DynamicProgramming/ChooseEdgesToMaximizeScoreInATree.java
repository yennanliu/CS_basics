package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/choose-edges-to-maximize-score-in-a-tree/description/

import java.util.*;

/**
 * 2378. Choose Edges to Maximize Score in a Tree
 * Medium
 *
 * (premium / locked problem)
 *
 * You are given a weighted rooted tree consisting of n nodes represented as a 0-indexed 2D integer array edges of size n where edges[i] = [par_i, weight_i] represents the parent node of node i (i.e., the root is node 0) and the weight of the edge between node i and its parent. Since the root does not have a parent, you have edges[0] = [-1, -1].
 *
 * Choose some edges from the tree such that no two chosen edges are adjacent and the sum of the weights of the chosen edges is maximized.
 *
 * Return the maximum sum of the chosen edges.
 *
 * Note:
 *
 * You are allowed to not choose any edges in the tree, the sum of weights in this case will be 0.
 * Two edges are adjacent if they share a common node.
 *
 *
 * Example 1:
 *
 * Input: edges = [[-1,-1],[0,5],[0,10],[2,6],[2,4]]
 * Output: 11
 * Explanation: The above diagram shows the edges that we have to choose colored in red.
 * The total score is 5 + 6 = 11.
 * It can be shown that no better score can be obtained.
 *
 * Example 2:
 *
 * Input: edges = [[-1,-1],[0,5],[0,-6],[0,7]]
 * Output: 7
 * Explanation: We choose the edge with weight 7.
 * Note that we cannot choose more than one edge because all edges are adjacent to each other.
 *
 *
 * Constraints:
 *
 * n == edges.length
 * 1 <= n <= 10^5
 * edges[i].length == 2
 * par_0 == weight_0 == -1
 * 0 <= par_i <= n - 1 for all i > 0.
 * par_i != i
 * -10^6 <= weight_i <= 10^6
 * It can be shown that the answer is a valid integer.
 *
 */
public class ChooseEdgesToMaximizeScoreInATree {

    // V0
    // IDEA: TREE DP (MAX-WEIGHT MATCHING) — free[u] = u NOT MATCHED TO A CHILD, matched[u] = u MATCHED TO ONE CHILD
    /**
     * time = O(N)
     * space = O(N)
     */
    public long maxScore(int[][] edges) {
        int n = edges.length;

        // edges[i] = {parent, weight} of node i; node 0 is the root (parent -1)
        List<List<Integer>> children = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            children.add(new ArrayList<>());
        }
        for (int i = 1; i < n; i++) {
            children.get(edges[i][0]).add(i);
        }

        // BFS order from the root; processing it in reverse visits children before parents
        List<Integer> order = new ArrayList<>();
        order.add(0);
        for (int idx = 0; idx < order.size(); idx++) {
            order.addAll(children.get(order.get(idx)));
        }

        // free[u]    = best score in u's subtree when the edge u-child is never chosen
        // matched[u] = best score in u's subtree when exactly one edge u-child is chosen
        long[] free = new long[n];
        long[] matched = new long[n];

        for (int idx = n - 1; idx >= 0; idx--) {
            int u = order.get(idx);
            long sumOfBest = 0;
            long bestGain = Long.MIN_VALUE;
            for (int child : children.get(u)) {
                long childBest = Math.max(free[child], matched[child]);
                sumOfBest += childBest;
                // choosing edge u-child forces child to stay free
                long gain = edges[child][1] + free[child] - childBest;
                bestGain = Math.max(bestGain, gain);
            }
            free[u] = sumOfBest;
            matched[u] = (bestGain == Long.MIN_VALUE) ? sumOfBest : sumOfBest + bestGain;
        }

        // weights may be negative, so choosing no edge at all (score 0) is always allowed
        return Math.max(free[0], matched[0]);
    }
}
