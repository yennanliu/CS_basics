package LeetCodeJava.Graph;

// https://leetcode.com/problems/checking-existence-of-edge-length-limited-paths-ii/description/

import java.util.*;

/**
 * 1724. Checking Existence of Edge Length Limited Paths II
 * Hard
 *
 * An undirected graph of n nodes is defined by edgeList, where edgeList[i] = [ui, vi, disi] denotes an edge
 * between nodes ui and vi with distance disi. Note that there may be multiple edges between two nodes, and
 * the graph may not be connected.
 *
 * Implement the DistanceLimitedPathsExist class:
 *
 * DistanceLimitedPathsExist(int n, int[][] edgeList) Initializes the class with an undirected graph.
 * boolean query(int p, int q, int limit) Returns true if there exists a path from p to q such that each
 * edge on the path has a distance strictly less than limit, and otherwise false.
 *
 *
 * Example 1:
 *
 * Input
 * ["DistanceLimitedPathsExist", "query", "query", "query", "query"]
 * [[6, [[0, 2, 4], [0, 3, 2], [1, 2, 3], [2, 3, 1], [4, 5, 5]]], [2, 3, 2], [1, 3, 3], [2, 0, 3], [0, 5, 6]]
 * Output
 * [null, true, false, true, false]
 *
 * Explanation
 * distanceLimitedPathsExist.query(2, 3, 2); // return true. There is an edge from 2 to 3 of distance 1.
 * distanceLimitedPathsExist.query(1, 3, 3); // return false.
 * distanceLimitedPathsExist.query(2, 0, 3); // return true. 2 -> 3 -> 0 with distances < 3.
 * distanceLimitedPathsExist.query(0, 5, 6); // return false. There are no paths from 0 to 5.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^4
 * 0 <= edgeList.length <= 10^4
 * edgeList[i].length == 3
 * 0 <= ui, vi, p, q <= n-1
 * ui != vi
 * p != q
 * 1 <= disi, limit <= 10^9
 * At most 10^4 calls will be made to query.
 *
 */
public class CheckingExistenceOfEdgeLengthLimitedPaths2 {

    // V0
    // IDEA: PERSISTENT (VERSIONED) UNION FIND -- queries arrive online, so LC 1697's
    // offline sort-the-queries trick is unavailable.
    // Union the edges in ascending weight and stamp each link with the weight that made it:
    // version[x] = w means "x was attached to its parent at weight w". A find limited to
    // `limit` refuses to climb a link made at weight >= limit, which rebuilds the union-find
    // exactly as it was after all edges lighter than limit were merged.
    // NOTE: union by rank only, NO path compression -- compression would rewrite parents out
    // of weight order and destroy the version history (rank keeps the depth O(log N)).
    class DistanceLimitedPathsExist {

        private final int[] parent;
        private final int[] rank;
        // weight at which the node was linked under its parent; MAX_VALUE = still a root
        private final int[] version;

        /**
         * time = O(E log E)
         * space = O(N)
         */
        public DistanceLimitedPathsExist(int n, int[][] edgeList) {
            parent = new int[n];
            rank = new int[n];
            version = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                version[i] = Integer.MAX_VALUE;
            }

            int[][] edges = edgeList.clone();
            Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));
            for (int[] edge : edges) {
                union(edge[0], edge[1], edge[2]);
            }
        }

        /**
         * time = O(log N)
         * space = O(1)
         */
        public boolean query(int p, int q, int limit) {
            return find(p, limit) == find(q, limit);
        }

        /** Root of x using only links created at weight strictly below limit. */
        private int find(int x, int limit) {
            while (parent[x] != x && version[x] < limit) {
                x = parent[x];
            }
            return x;
        }

        /** Links the trees of a and b (by rank), stamping the link with weight. */
        private void union(int a, int b, int weight) {
            int rootA = find(a, Integer.MAX_VALUE);
            int rootB = find(b, Integer.MAX_VALUE);
            if (rootA == rootB) {
                return;
            }
            if (rank[rootA] > rank[rootB]) {
                parent[rootB] = rootA;
                version[rootB] = weight;
            } else {
                parent[rootA] = rootB;
                version[rootA] = weight;
                if (rank[rootA] == rank[rootB]) {
                    rank[rootB]++;
                }
            }
        }
    }
}
