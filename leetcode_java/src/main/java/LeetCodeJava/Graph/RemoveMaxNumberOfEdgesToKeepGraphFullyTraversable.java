package LeetCodeJava.Graph;

// https://leetcode.com/problems/remove-max-number-of-edges-to-keep-graph-fully-traversable/description/

import java.util.*;

/**
 * 1579. Remove Max Number of Edges to Keep Graph Fully Traversable
 * Hard
 *
 * Alice and Bob have an undirected graph of n nodes and three types of edges:
 *
 * Type 1: Can be traversed by Alice only.
 * Type 2: Can be traversed by Bob only.
 * Type 3: Can be traversed by both Alice and Bob.
 *
 * Given an array edges where edges[i] = [typei, ui, vi] represents a bidirectional edge of type typei
 * between nodes ui and vi, find the maximum number of edges you can remove so that after removing the
 * edges, the graph can still be fully traversed by both Alice and Bob. The graph is fully traversed by
 * Alice and Bob if starting from any node, they can reach all other nodes.
 *
 * Return the maximum number of edges you can remove, or return -1 if Alice and Bob cannot fully
 * traverse the graph.
 *
 *
 * Example 1:
 *
 * Input: n = 4, edges = [[3,1,2],[3,2,3],[1,1,3],[1,2,4],[1,1,2],[2,3,4]]
 * Output: 2
 *
 * Example 2:
 *
 * Input: n = 4, edges = [[3,1,2],[3,2,3],[1,1,4],[2,1,4]]
 * Output: 0
 *
 * Example 3:
 *
 * Input: n = 4, edges = [[3,2,3],[1,1,2],[2,3,4]]
 * Output: -1
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^5
 * 1 <= edges.length <= min(10^5, 3 * n * (n - 1) / 2)
 * edges[i].length == 3
 * 1 <= typei <= 3
 * 1 <= ui < vi <= n
 * All tuples (typei, ui, vi) are distinct.
 *
 */
public class RemoveMaxNumberOfEdgesToKeepGraphFullyTraversable {

    // V0
    // IDEA: UNION FIND x2 (add the shared type-3 edges first)
    // one DSU for Alice and one for Bob. A type-3 edge helps both, so it is never worse
    // than a private edge -> take every useful type-3 edge first, then top each DSU up with
    // its own private edges. An edge that merges nothing is redundant, i.e. removable.
    // If either DSU does not end as a single component the answer is -1.
    /**
     * time = O(E * alpha(N))
     * space = O(N)
     */
    public int maxNumEdgesToRemove(int n, int[][] edges) {
        UnionFind alice = new UnionFind(n);
        UnionFind bob = new UnionFind(n);
        int kept = 0;

        // pass 1: shared edges
        for (int[] edge : edges) {
            if (edge[0] == 3) {
                boolean mergedAlice = alice.union(edge[1], edge[2]);
                boolean mergedBob = bob.union(edge[1], edge[2]);
                if (mergedAlice || mergedBob) {
                    kept++;
                }
            }
        }

        // pass 2: private edges
        for (int[] edge : edges) {
            if (edge[0] == 1 && alice.union(edge[1], edge[2])) {
                kept++;
            } else if (edge[0] == 2 && bob.union(edge[1], edge[2])) {
                kept++;
            }
        }

        if (alice.components != 1 || bob.components != 1) {
            return -1;
        }
        return edges.length - kept;
    }

    /** Disjoint set over nodes 1..n that tracks how many components remain. */
    private static class UnionFind {
        private final int[] parent;
        private int components;

        UnionFind(int n) {
            parent = new int[n + 1];
            for (int i = 0; i <= n; i++) {
                parent[i] = i;
            }
            components = n;
        }

        int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];
                x = parent[x];
            }
            return x;
        }

        /** Merges the sets of a and b; returns false if they were already joined. */
        boolean union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) {
                return false;
            }
            parent[rootA] = rootB;
            components--;
            return true;
        }
    }
}
