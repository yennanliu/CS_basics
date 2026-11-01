package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/difference-between-maximum-and-minimum-price-sum/description/

import java.util.*;

/**
 * 2538. Difference Between Maximum and Minimum Price Sum
 * Hard
 *
 * There exists an undirected and initially unrooted tree with n nodes indexed from 0 to n - 1.
 * You are given the integer n and a 2D integer array edges of length n - 1, where
 * edges[i] = [a_i, b_i] indicates that there is an edge between nodes a_i and b_i in the tree.
 *
 * Each node has an associated price. You are given an integer array price, where price[i] is the
 * price of the ith node.
 *
 * The price sum of a given path is the sum of the prices of all nodes lying on that path.
 *
 * The tree can be rooted at any node root of your choice. The incurred cost after choosing root is
 * the difference between the maximum and minimum price sum amongst all paths starting at root.
 *
 * Return the maximum possible cost amongst all possible root choices.
 *
 *
 * Example 1:
 *
 * Input: n = 6, edges = [[0,1],[1,2],[1,3],[3,4],[3,5]], price = [9,8,7,6,10,5]
 * Output: 24
 * Explanation: Root the tree at node 2.
 * - The path with the maximum price sum contains nodes [2,1,3,4]: the prices are [7,8,6,10], and the sum is 31.
 * - The path with the minimum price sum contains the node [2] with the price [7].
 * The difference between the maximum and minimum price sum is 24.
 *
 * Example 2:
 *
 * Input: n = 3, edges = [[0,1],[1,2]], price = [1,1,1]
 * Output: 2
 * Explanation: Root the tree at node 0.
 * - The path with the maximum price sum contains nodes [0,1,2], and the sum is 3.
 * - The path with the minimum price sum contains node [0] with a price [1].
 * The difference between the maximum and minimum price sum is 2.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * edges.length == n - 1
 * 0 <= a_i, b_i <= n - 1
 * edges represents a valid tree.
 * price.length == n
 * 1 <= price[i] <= 10^5
 *
 */
public class DifferenceBetweenMaximumAndMinimumPriceSum {

    // V0
    // IDEA: TREE DP - BEST PATH WITH ONE ENDPOINT DROPPED (withLeaf / withoutLeaf PER NODE), ITERATIVE POST-ORDER
    /**
     * time = O(N)
     * space = O(N)
     */
    public long maxOutput(int n, int[][] edges, int[] price) {
        // prices are positive, so the cheapest path from a root is the root alone:
        // cost(root) = (max path sum from root) - price[root].
        // so the answer is the max over all paths of (path sum - price of one endpoint).

        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        // iterative DFS from node 0 (n can be 10^5, too deep for recursion);
        // `order` is a pre-order, so walking it backwards visits children before parents
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[n];
        int[] order = new int[n];
        int orderSize = 0;

        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        visited[0] = true;
        while (!stack.isEmpty()) {
            int node = stack.pop();
            order[orderSize++] = node;
            for (int next : graph.get(node)) {
                if (!visited[next]) {
                    visited[next] = true;
                    parent[next] = node;
                    stack.push(next);
                }
            }
        }

        // withLeaf[i]    = best downward path from i, counting every node
        // withoutLeaf[i] = best downward path from i, excluding its bottom-most node
        long[] withLeaf = new long[n];
        long[] withoutLeaf = new long[n];
        long result = 0;

        for (int idx = n - 1; idx >= 0; idx--) {
            int node = order[idx];
            // starting values let `node` itself be the dropped endpoint
            long bestFull = price[node];
            long bestCut = 0;

            for (int child : graph.get(node)) {
                if (child == parent[node]) {
                    continue;
                }
                // join the branches seen so far with this child's branch: one half keeps its
                // endpoint and the other half drops it
                result = Math.max(result, bestFull + withoutLeaf[child]);
                result = Math.max(result, bestCut + withLeaf[child]);

                bestFull = Math.max(bestFull, price[node] + withLeaf[child]);
                bestCut = Math.max(bestCut, price[node] + withoutLeaf[child]);
            }
            withLeaf[node] = bestFull;
            withoutLeaf[node] = bestCut;
        }
        return result;
    }
}
