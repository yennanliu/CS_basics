package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimize-maximum-component-cost/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.TreeSet;

/**
 * 3613. Minimize Maximum Component Cost
 * Medium
 *
 * You are given an undirected connected graph with n nodes labeled from 0 to n - 1
 * and a 2D integer array edges where edges[i] = [ui, vi, wi] denotes an undirected
 * edge between node ui and node vi with weight wi, and an integer k.
 *
 * You are allowed to remove any number of edges from the graph such that the
 * resulting graph has at most k connected components.
 *
 * The cost of a component is defined as the maximum edge weight in that component.
 * If a component has no edges, its cost is 0.
 *
 * Return the minimum possible value of the maximum cost among all components after
 * such removals.
 *
 *
 * Example 1:
 *
 * Input: n = 5, edges = [[0,1,4],[1,2,3],[1,3,2],[3,4,6]], k = 2
 * Output: 4
 *
 * Example 2:
 *
 * Input: n = 4, edges = [[0,1,5],[1,2,5],[2,3,5]], k = 1
 * Output: 5
 *
 *
 * Constraints:
 *
 * 1 <= n <= 5 * 10^4
 * 0 <= edges.length <= 10^5
 * edges[i].length == 3
 * 0 <= ui, vi < n
 * 1 <= wi <= 10^6
 * 1 <= k <= n
 * The input graph is connected.
 *
 */
public class MinimizeMaximumComponentCost {

    // V0
    // IDEA: KRUSKAL -- ADD EDGES CHEAPEST FIRST UNTIL ONLY k COMPONENTS REMAIN
    //   the cost is the heaviest edge kept. for a budget w, keeping every edge of
    //   weight <= w is never worse, and the component count only falls as w grows.
    //   so add edges in ascending weight; each merging edge drops the count by one,
    //   and the weight of the merge that reaches k components is the answer.
    /**
     * time = O(E log E + (N + E) * alpha(N))
     * space = O(N + E)
     */
    public int minCost(int n, int[][] edges, int k) {
        // every node may stand alone: no edge kept, cost 0
        if (k >= n) {
            return 0;
        }

        int[][] sorted = edges.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[2], b[2]));

        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        int components = n;
        for (int[] edge : sorted) {
            int rootU = find(parent, edge[0]);
            int rootV = find(parent, edge[1]);
            if (rootU != rootV) {
                parent[rootU] = rootV;
                components--;
                if (components <= k) {
                    return edge[2];
                }
            }
        }
        return 0;
    }

    /** Find with path compression (iterative). */
    private int find(int[] parent, int x) {
        int root = x;
        while (parent[root] != root) {
            root = parent[root];
        }
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }

    // V1
    // IDEA: BINARY SEARCH THE BUDGET + DFS COMPONENT COUNT
    //   the monotone predicate V0 short-circuits, spelled out: for a budget w keep
    //   every edge of weight <= w and count components with an iterative DFS; binary
    //   search the sorted distinct weights for the smallest w with count <= k.
    /**
     * time = O(E log E + (N + E) log E)
     * space = O(N + E)
     */
    public int minCost_1(int n, int[][] edges, int k) {
        if (k >= n) {
            return 0;
        }

        // adjacency list: graph.get(u) holds {v, weight}
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        TreeSet<Integer> weightSet = new TreeSet<>();
        for (int[] edge : edges) {
            graph.get(edge[0]).add(new int[]{edge[1], edge[2]});
            graph.get(edge[1]).add(new int[]{edge[0], edge[2]});
            weightSet.add(edge[2]);
        }
        List<Integer> weights = new ArrayList<>(weightSet);

        // smallest index whose budget leaves <= k components
        int left = 0;
        int right = weights.size() - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (countComponents(graph, n, weights.get(mid), k) <= k) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return weights.get(left);
    }

    /** Components using only edges of weight <= limit; stops early once it exceeds k. */
    private int countComponents(List<List<int[]>> graph, int n, int limit, int k) {
        boolean[] visited = new boolean[n];
        int components = 0;
        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }
            components++;
            if (components > k) {
                return components; // already too fragmented
            }
            visited[start] = true;
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(start);
            while (!stack.isEmpty()) {
                int node = stack.pop();
                for (int[] next : graph.get(node)) {
                    if (next[1] <= limit && !visited[next[0]]) {
                        visited[next[0]] = true;
                        stack.push(next[0]);
                    }
                }
            }
        }
        return components;
    }
}
