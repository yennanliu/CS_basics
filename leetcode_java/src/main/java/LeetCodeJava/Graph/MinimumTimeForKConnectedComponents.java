package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-time-for-k-connected-components/description/

import java.util.Arrays;

/**
 * 3608. Minimum Time for K Connected Components
 * Medium
 *
 * You are given an integer n and an undirected graph with n nodes labeled from 0
 * to n - 1. This is represented by a 2D array edges, where
 * edges[i] = [ui, vi, timei] indicates an undirected edge between nodes ui and vi
 * that can be removed at timei.
 *
 * You are also given an integer k.
 *
 * Initially, the graph may be connected or disconnected. Your task is to find the
 * minimum time t such that after removing all edges with time <= t, the graph
 * contains at least k connected components.
 *
 * Return the minimum time t.
 *
 *
 * Example 1:
 *
 * Input: n = 2, edges = [[0,1,3]], k = 2
 * Output: 3
 *
 * Example 2:
 *
 * Input: n = 3, edges = [[0,1,2],[1,2,4]], k = 3
 * Output: 4
 *
 * Example 3:
 *
 * Input: n = 3, edges = [[0,2,5]], k = 2
 * Output: 0
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * 0 <= edges.length <= 10^5
 * edges[i] = [ui, vi, timei]
 * 0 <= ui, vi < n
 * ui != vi
 * 1 <= timei <= 10^9
 * 1 <= k <= n
 * There are no duplicate edges.
 *
 */
public class MinimumTimeForKConnectedComponents {

    // V0
    // IDEA: REVERSE KRUSKAL -- ADD EDGES BACK FROM THE LATEST TIME DOWN (UNION-FIND)
    //   deleting from a union-find is awkward, so run time backwards: insert edges in
    //   DESCENDING time. the component count only drops as edges come back; the first
    //   insert that drops it below k is at time t, and removing every edge with
    //   time <= t leaves at least k components while any smaller t leaves fewer.
    //   if the count never drops below k, the graph already has k components: t = 0.
    /**
     * time = O(E log E + (N + E) * alpha(N))
     * space = O(N + E)
     */
    public int minTime(int n, int[][] edges, int k) {
        int[][] sorted = edges.clone();
        // latest removal time first
        Arrays.sort(sorted, (a, b) -> Integer.compare(b[2], a[2]));

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
                // this edge must be removed too: the answer is its time
                if (components < k) {
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
        // point every node on the path straight at the root
        while (parent[x] != root) {
            int next = parent[x];
            parent[x] = root;
            x = next;
        }
        return root;
    }
}
