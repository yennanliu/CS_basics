package LeetCodeJava.Graph;

// https://leetcode.com/problems/optimize-water-distribution-in-a-village/description/

import java.util.*;

/**
 * 1168. Optimize Water Distribution in a Village
 * Hard
 *
 * There are n houses in a village. We want to supply water for all the houses by building wells and laying pipes.
 *
 * For each house i, we can either build a well inside it directly with cost wells[i - 1] (note the -1 due to
 * 0-indexing), or pipe in water from another well to it. The costs to lay pipes between houses are given by the
 * array pipes where each pipes[j] = [house1_j, house2_j, cost_j] represents the cost to connect house1_j and
 * house2_j together using a pipe. Connections are bidirectional, and there could be multiple valid connections
 * between the same two houses with different costs.
 *
 * Return the minimum total cost to supply water to all houses.
 *
 *
 * Example 1:
 *
 * Input: n = 3, wells = [1,2,2], pipes = [[1,2,1],[2,3,1]]
 * Output: 3
 * Explanation: The best strategy is to build a well in the first house with cost 1 and connect the other houses
 * to it with cost 2 so the total cost is 3.
 *
 * Example 2:
 *
 * Input: n = 2, wells = [1,1], pipes = [[1,2,1],[1,2,2]]
 * Output: 2
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^4
 * wells.length == n
 * 0 <= wells[i] <= 10^5
 * 1 <= pipes.length <= 10^4
 * pipes[j].length == 3
 * 1 <= house1_j, house2_j <= n
 * 0 <= cost_j <= 10^5
 * house1_j != house2_j
 *
 */
public class OptimizeWaterDistributionInAVillage {

    // V0
    // IDEA: MST (KRUSKAL) + VIRTUAL NODE 0
    // add a virtual "water source" node 0: building a well at house i is the same as
    // laying a pipe (0, i) with cost wells[i - 1]. The answer is the MST of the n + 1 nodes.
    /**
     * time = O((M + N) log (M + N))
     * space = O(M + N)
     */
    public int minCostToSupplyWater(int n, int[] wells, int[][] pipes) {
        // edge = {cost, u, v}
        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            edges.add(new int[] {wells[i], 0, i + 1});
        }
        for (int[] pipe : pipes) {
            edges.add(new int[] {pipe[2], pipe[0], pipe[1]});
        }
        // Kruskal: cheapest edges first
        edges.sort((a, b) -> Integer.compare(a[0], b[0]));

        int[] parent = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
        }

        int totalCost = 0;
        int components = n + 1;
        for (int[] edge : edges) {
            int rootU = find(parent, edge[1]);
            int rootV = find(parent, edge[2]);
            // skip an edge that would close a cycle
            if (rootU == rootV) {
                continue;
            }
            parent[rootU] = rootV;
            totalCost += edge[0];
            components--;
            if (components == 1) {
                break;
            }
        }
        return totalCost;
    }

    /** Returns the root of x, compressing the path on the way. */
    private int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }

    // V1
    // IDEA: MST (PRIM) WITH A MIN-HEAP, starting from the virtual node 0
    /**
     * time = O((M + N) log (M + N))
     * space = O(M + N)
     */
    public int minCostToSupplyWater_1(int n, int[] wells, int[][] pipes) {
        // graph.get(u) = list of {cost, v}
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            graph.get(0).add(new int[] {wells[i], i + 1});
            graph.get(i + 1).add(new int[] {wells[i], 0});
        }
        for (int[] pipe : pipes) {
            graph.get(pipe[0]).add(new int[] {pipe[2], pipe[1]});
            graph.get(pipe[1]).add(new int[] {pipe[2], pipe[0]});
        }

        boolean[] inTree = new boolean[n + 1];
        // heap of {cost to attach, node}
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        heap.offer(new int[] {0, 0});

        int totalCost = 0;
        int added = 0;
        while (!heap.isEmpty() && added <= n) {
            int[] top = heap.poll();
            int node = top[1];
            // a stale, more expensive way to reach an attached node
            if (inTree[node]) {
                continue;
            }
            inTree[node] = true;
            added++;
            totalCost += top[0];
            for (int[] next : graph.get(node)) {
                if (!inTree[next[1]]) {
                    heap.offer(next);
                }
            }
        }
        return totalCost;
    }
}
