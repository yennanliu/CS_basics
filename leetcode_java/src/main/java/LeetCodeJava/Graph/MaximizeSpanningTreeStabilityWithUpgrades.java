package LeetCodeJava.Graph;

// https://leetcode.com/problems/maximize-spanning-tree-stability-with-upgrades/description/

import java.util.*;

/**
 * 3600. Maximize Spanning Tree Stability with Upgrades
 * Hard
 *
 * You are given an integer n, representing n nodes numbered from 0 to n - 1
 * and a list of edges, where edges[i] = [ui, vi, si, musti]:
 *
 * ui and vi indicates an undirected edge between nodes ui and vi.
 * si is the strength of the edge.
 * musti is an integer (0 or 1). If musti == 1, the edge must be included in
 * the spanning tree. These edges cannot be upgraded.
 *
 * You are also given an integer k, the maximum number of upgrades you can
 * perform. Each upgrade doubles the strength of an edge, and each eligible
 * edge (with musti == 0) can be upgraded at most once.
 *
 * The stability of a spanning tree is defined as the minimum strength score
 * among all edges included in it.
 *
 * Return the maximum possible stability of any valid spanning tree. If it is
 * impossible to connect all nodes, return -1.
 *
 * Note: A spanning tree of a graph with n nodes is a subset of the edges that
 * connects all nodes together (i.e. the graph is connected) without forming
 * any cycles, and uses exactly n - 1 edges.
 *
 *
 * Example 1:
 *
 * Input: n = 3, edges = [[0,1,2,1],[1,2,3,0]], k = 1
 * Output: 2
 * Explanation:
 * Edge [0,1] with strength = 2 must be included in the spanning tree.
 * Edge [1,2] is optional and can be upgraded from 3 to 6 using one upgrade.
 * The resulting spanning tree includes these two edges with strengths 2 and 6.
 * The minimum strength in the spanning tree is 2, which is the maximum
 * possible stability.
 *
 * Example 2:
 *
 * Input: n = 3, edges = [[0,1,4,0],[1,2,3,0],[0,2,1,0]], k = 2
 * Output: 6
 * Explanation:
 * Since all edges are optional and up to k = 2 upgrades are allowed.
 * Upgrade edges [0,1] from 4 to 8 and [1,2] from 3 to 6.
 * The resulting spanning tree includes these two edges with strengths 8 and 6.
 * The minimum strength in the tree is 6, which is the maximum possible
 * stability.
 *
 * Example 3:
 *
 * Input: n = 3, edges = [[0,1,1,1],[1,2,1,1],[2,0,1,1]], k = 0
 * Output: -1
 * Explanation:
 * All edges are mandatory and form a cycle, which violates the spanning tree
 * property of acyclicity. Thus, the answer is -1.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 * 1 <= edges.length <= 10^5
 * edges[i] = [ui, vi, si, musti]
 * 0 <= ui, vi < n
 * ui != vi
 * 1 <= si <= 10^5
 * musti is either 0 or 1
 * 0 <= k <= n
 * There are no duplicate edges.
 *
 */
public class MaximizeSpanningTreeStabilityWithUpgrades {

    // V0
    // IDEA: BINARY SEARCH THE STABILITY + UNION-FIND FEASIBILITY CHECK
    //  - "some valid spanning tree has stability >= limit" is monotone in limit
    //  - for a fixed limit: free edges (strength >= limit) are all contracted first; then each
    //    upgradable edge (2 * strength >= limit) that still merges two components costs one of
    //    the k upgrades -- this greedy is optimal (matroid argument)
    //  - must-edges cannot be upgraded, so the answer is capped by the weakest must-edge;
    //    must-edges forming a cycle, or a disconnected graph -> -1
    /**
     * time = O(m * alpha(n) * log(maxStrength))
     * space = O(n)
     */
    public int maxStability(int n, int[][] edges, int k) {
        // must-edges must form a forest, and the whole graph must be connected
        UnionFind check = new UnionFind(n);
        int mustCap = Integer.MAX_VALUE;
        int strongest = 0;
        for (int[] edge : edges) {
            strongest = Math.max(strongest, edge[2]);
            if (edge[3] == 1) {
                mustCap = Math.min(mustCap, edge[2]);
                if (!check.union(edge[0], edge[1])) {
                    return -1;   // must-edges contain a cycle
                }
            }
        }
        for (int[] edge : edges) {
            check.union(edge[0], edge[1]);
        }
        if (check.components > 1) {
            return -1;
        }

        // limit = 1 always works; nothing beats the strongest edge doubled, nor a must-edge
        int low = 1;
        int high = (int) Math.min((long) mustCap, 2L * strongest);
        while (low < high) {
            // upper middle, because a feasible mid moves low up to mid
            int mid = low + (high - low + 1) / 2;
            if (feasible(n, edges, k, mid)) {
                low = mid;
            } else {
                high = mid - 1;
            }
        }
        return low;
    }

    /** True if a spanning tree with every edge >= limit exists using at most k upgrades. */
    private boolean feasible(int n, int[][] edges, int k, int limit) {
        UnionFind uf = new UnionFind(n);
        // free edges first
        for (int[] edge : edges) {
            if (edge[2] >= limit) {
                uf.union(edge[0], edge[1]);
            }
        }
        // then spend upgrades only on edges that still merge two components
        int upgradesLeft = k;
        for (int[] edge : edges) {
            if (uf.components == 1 || upgradesLeft == 0) {
                break;
            }
            if (2L * edge[2] >= limit && uf.union(edge[0], edge[1])) {
                upgradesLeft--;
            }
        }
        return uf.components == 1;
    }

    /** Union-find with union by rank and path compression; tracks the component count. */
    private static class UnionFind {
        private final int[] parent;
        private final int[] rank;
        private int components;

        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            components = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];
                x = parent[x];
            }
            return x;
        }

        /** Returns false if a and b were already connected. */
        boolean union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) {
                return false;
            }
            if (rank[rootA] < rank[rootB]) {
                int swap = rootA;
                rootA = rootB;
                rootB = swap;
            }
            parent[rootB] = rootA;
            if (rank[rootA] == rank[rootB]) {
                rank[rootA]++;
            }
            components--;
            return true;
        }
    }
}
