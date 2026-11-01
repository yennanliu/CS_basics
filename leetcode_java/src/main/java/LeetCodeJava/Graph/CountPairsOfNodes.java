package LeetCodeJava.Graph;

// https://leetcode.com/problems/count-pairs-of-nodes/description/

import java.util.*;

/**
 * 1782. Count Pairs Of Nodes
 * Hard
 *
 * You are given an undirected graph defined by an integer n, the number of nodes, and a 2D integer array
 * edges, the edges in the graph, where edges[i] = [ui, vi] indicates that there is an undirected edge
 * between ui and vi. You are also given an integer array queries.
 *
 * Let incident(a, b) be defined as the number of edges that are connected to either node a or b.
 *
 * The answer to the j^th query is the number of pairs of nodes (a, b) that satisfy both of the following
 * conditions:
 *
 * a < b
 * incident(a, b) > queries[j]
 *
 * Return an array answers such that answers.length == queries.length and answers[j] is the answer of the
 * j^th query.
 *
 * Note that there can be multiple edges between the same two nodes.
 *
 *
 * Example 1:
 *
 * Input: n = 4, edges = [[1,2],[2,4],[1,3],[2,3],[2,1]], queries = [2,3]
 * Output: [6,5]
 *
 * Example 2:
 *
 * Input: n = 5, edges = [[1,5],[1,5],[3,4],[2,5],[1,3],[5,1],[2,3],[2,5]], queries = [1,2,3,4,5]
 * Output: [10,10,9,8,6]
 *
 *
 * Constraints:
 *
 * 2 <= n <= 2 * 10^4
 * 1 <= edges.length <= 10^5
 * 1 <= ui, vi <= n
 * ui != vi
 * 1 <= queries.length <= 20
 * 0 <= queries[j] < edges.length
 *
 */
public class CountPairsOfNodes {

    // V0
    // IDEA: SORTED DEGREES + TWO POINTERS, THEN FIX UP THE SHARED EDGES
    // incident(a, b) = degree[a] + degree[b] - shared(a, b), since edges between a and b are
    // counted twice.
    //   step 1: ignore shared edges; with sorted degrees, two pointers count the pairs with
    //           degree[a] + degree[b] > q in O(N).
    //   step 2: only pairs that share an edge were over-counted, and there are at most E of
    //           them; drop each one whose degree sum passes q but whose true value does not.
    // At most 20 queries, so O(Q * (N + E)) is fine.
    /**
     * time = O(E + N log N + Q * (N + E))
     * space = O(N + E)
     */
    public int[] countPairs(int n, int[][] edges, int[] queries) {
        int[] degree = new int[n + 1];
        // key = smaller * (n + 1) + larger -> number of edges between the two nodes
        Map<Long, Integer> shared = new HashMap<>();
        for (int[] edge : edges) {
            degree[edge[0]]++;
            degree[edge[1]]++;
            int small = Math.min(edge[0], edge[1]);
            int large = Math.max(edge[0], edge[1]);
            long pairKey = (long) small * (n + 1) + large;
            shared.put(pairKey, shared.getOrDefault(pairKey, 0) + 1);
        }

        int[] sortedDegree = new int[n];
        for (int node = 1; node <= n; node++) {
            sortedDegree[node - 1] = degree[node];
        }
        Arrays.sort(sortedDegree);

        int[] answers = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int limit = queries[q];

            // step 1: pairs whose degree sum exceeds the limit
            int count = 0;
            int left = 0;
            int right = n - 1;
            while (left < right) {
                if (sortedDegree[left] + sortedDegree[right] > limit) {
                    // every index in [left, right) pairs with right
                    count += right - left;
                    right--;
                } else {
                    left++;
                }
            }

            // step 2: remove pairs pushed over the limit only by double-counted edges
            for (Map.Entry<Long, Integer> entry : shared.entrySet()) {
                int u = (int) (entry.getKey() / (n + 1));
                int v = (int) (entry.getKey() % (n + 1));
                int degreeSum = degree[u] + degree[v];
                if (degreeSum > limit && degreeSum - entry.getValue() <= limit) {
                    count--;
                }
            }
            answers[q] = count;
        }
        return answers;
    }
}
