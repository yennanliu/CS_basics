package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/subtree-inversion-sum/description/

import java.util.*;

/**
 * 3544. Subtree Inversion Sum
 * Hard
 *
 * You are given an undirected tree rooted at node 0, with n nodes numbered from 0
 * to n - 1. The tree is represented by a 2D integer array edges of length n - 1,
 * where edges[i] = [u_i, v_i] indicates an edge between nodes u_i and v_i.
 *
 * You are also given an integer array nums of length n, where nums[i] represents
 * the value at node i, and an integer k.
 *
 * You may perform inversion operations on a subset of nodes subject to the
 * following rules:
 *
 * Subtree Inversion Operation:
 *
 * When you invert a node, every value in the subtree rooted at that node is
 * multiplied by -1.
 *
 * Distance Constraint on Inversions:
 *
 * You may only invert a node if it is "sufficiently far" from any other inverted
 * node.
 *
 * Specifically, if you invert two nodes a and b such that one is an ancestor of
 * the other (i.e., if LCA(a, b) = a or LCA(a, b) = b), then the distance (the
 * number of edges on the unique path between them) must be at least k.
 *
 * Return the maximum possible sum of the tree's node values after applying
 * inversion operations.
 *
 * Example 1:
 *
 * Input: edges = [[0,1],[0,2],[1,3],[1,4],[2,5],[2,6]], nums = [4,-8,-6,3,7,-2,5],
 * k = 2
 *
 * Output: 27
 *
 * Explanation:
 *
 * Apply inversion operations at nodes 0, 3, 4 and 6.
 *
 * The final nums array is [-4, 8, 6, 3, 7, 2, 5], and the total sum is 27.
 *
 * Example 2:
 *
 * Input: edges = [[0,1],[1,2],[2,3],[3,4]], nums = [-1,3,-2,4,-5], k = 2
 *
 * Output: 9
 *
 * Explanation:
 *
 * Apply the inversion operation at node 4.
 *
 * The final nums array becomes [-1, 3, -2, 4, 5], and the total sum is 9.
 *
 * Example 3:
 *
 * Input: edges = [[0,1],[0,2]], nums = [0,-1,-2], k = 3
 *
 * Output: 3
 *
 * Explanation:
 *
 * Apply inversion operations at nodes 1 and 2.
 *
 * Constraints:
 *
 * 2 <= n <= 5 * 10^4
 *
 * edges.length == n - 1
 *
 * edges[i] = [u_i, v_i]
 *
 * 0 <= u_i, v_i < n
 *
 * nums.length == n
 *
 * -5 * 10^4 <= nums[i] <= 5 * 10^4
 *
 * 1 <= k <= 50
 *
 * The input is generated such that edges represents a valid tree.
 *
 */
public class SubtreeInversionSum {

    // V0
    // IDEA: TREE DP ON THE DISTANCE TO THE NEAREST INVERTED ANCESTOR, KEEPING BEST AND WORST
    //   inverting v flips its whole subtree, so v contributes nums[v] * (-1)^(number of
    //   inverted nodes on the root -> v path). v may be inverted only when the nearest
    //   inverted ancestor is >= k edges up, so the state is d = that distance, capped at k.
    //   Under a flipped sign the best total of a subtree is minus its WORST total, so
    //   instead of a sign dimension carry both:
    //     best[v][d] / worst[v][d] = max / min total of subtree(v) under sign +1
    //   With childBest[d] / childWorst[d] = sums over children:
    //     keep v   : nums[v] + childBest[min(d + 1, k)]
    //     invert v : -nums[v] - childWorst[1]          (only when d == k)
    //   worst mirrors it with min and best / worst swapped.
    //   n is up to 5 * 10^4, so the post-order is iterative, not recursive.
    /**
     * time = O(N * K)
     * space = O(N * K)
     */
    public long subtreeInversionSum(int[][] edges, int[] nums, int k) {
        int n = nums.length;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        // iterative DFS from the root: order lists every parent before its children
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        boolean[] visited = new boolean[n];
        List<Integer> order = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(0);
        visited[0] = true;
        while (!stack.isEmpty()) {
            int node = stack.pop();
            order.add(node);
            for (int next : graph.get(node)) {
                if (!visited[next]) {
                    visited[next] = true;
                    parent[next] = node;
                    stack.push(next);
                }
            }
        }

        // childBest[v][d] / childWorst[v][d] = summed best / worst of v's children at distance d
        long[][] childBest = new long[n][];
        long[][] childWorst = new long[n][];
        for (int v = 0; v < n; v++) {
            childBest[v] = new long[k + 1];
            childWorst[v] = new long[k + 1];
        }

        long answer = 0;
        // reversed order = children are finished before their parent
        for (int idx = n - 1; idx >= 0; idx--) {
            int v = order.get(idx);
            long value = nums[v];
            long[] sumBest = childBest[v];
            long[] sumWorst = childWorst[v];

            long[] best = new long[k + 1];
            long[] worst = new long[k + 1];
            // d < k: v may not be inverted, children are one step further away
            for (int d = 1; d < k; d++) {
                best[d] = value + sumBest[d + 1];
                worst[d] = value + sumWorst[d + 1];
            }
            // d == k: v may stay, or be inverted (its children are then at distance 1
            // under a flipped sign, so their best becomes minus their worst)
            best[k] = Math.max(value + sumBest[k], -value - sumWorst[1]);
            worst[k] = Math.min(value + sumWorst[k], -value - sumBest[1]);

            int p = parent[v];
            if (p < 0) {
                // the root has no inverted ancestor at all, which counts as "far enough"
                answer = best[k];
            } else {
                for (int d = 1; d <= k; d++) {
                    childBest[p][d] += best[d];
                    childWorst[p][d] += worst[d];
                }
                // free the finished child's tables
                childBest[v] = null;
                childWorst[v] = null;
            }
        }
        return answer;
    }
}
