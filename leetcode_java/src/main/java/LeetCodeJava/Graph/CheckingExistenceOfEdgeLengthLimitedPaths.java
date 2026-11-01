package LeetCodeJava.Graph;

// https://leetcode.com/problems/checking-existence-of-edge-length-limited-paths/description/

import java.util.*;

/**
 * 1697. Checking Existence of Edge Length Limited Paths
 * Hard
 *
 * An undirected graph of n nodes is defined by edgeList, where edgeList[i] = [ui, vi, disi] denotes an
 * edge between nodes ui and vi with distance disi. Note that there may be multiple edges between two
 * nodes.
 *
 * Given an array queries, where queries[j] = [pj, qj, limitj], your task is to determine for each
 * queries[j] whether there is a path between pj and qj such that each edge on the path has a distance
 * strictly less than limitj.
 *
 * Return a boolean array answer, where answer.length == queries.length and the jth value of answer is
 * true if there is a path for queries[j] is true, and false otherwise.
 *
 *
 * Example 1:
 *
 * Input: n = 3, edgeList = [[0,1,2],[1,2,4],[2,0,8],[1,0,16]], queries = [[0,1,2],[0,2,5]]
 * Output: [false,true]
 *
 * Example 2:
 *
 * Input: n = 5, edgeList = [[0,1,10],[1,2,5],[2,3,9],[3,4,13]], queries = [[0,4,14],[1,4,13]]
 * Output: [true,false]
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 * 1 <= edgeList.length, queries.length <= 10^5
 * edgeList[i].length == 3
 * queries[j].length == 3
 * 0 <= ui, vi, pj, qj <= n - 1
 * ui != vi
 * pj != qj
 * 1 <= disi, limitj <= 10^9
 * There may be multiple edges between two nodes.
 *
 */
public class CheckingExistenceOfEdgeLengthLimitedPaths {

    // V0
    // IDEA: OFFLINE QUERIES + UNION FIND (sort edges and queries by weight)
    // query (p, q, limit) is true iff p and q are connected using only edges with weight < limit.
    // Those subgraphs are nested, so answer the queries in increasing limit and keep growing
    // one union-find: each edge is unioned exactly once.
    // NOTE: answers go back in the ORIGINAL query order, so sort query indices, not queries.
    /**
     * time = O(E log E + Q log Q)
     * space = O(N + Q)
     */
    public boolean[] distanceLimitedPathsExist(int n, int[][] edgeList, int[][] queries) {
        int[][] edges = edgeList.clone();
        Arrays.sort(edges, (a, b) -> Integer.compare(a[2], b[2]));

        Integer[] order = new Integer[queries.length];
        for (int i = 0; i < queries.length; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(queries[a][2], queries[b][2]));

        int[] parent = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        boolean[] answer = new boolean[queries.length];
        int edgeIndex = 0;
        for (int queryIndex : order) {
            int limit = queries[queryIndex][2];
            // add every edge strictly lighter than this limit
            while (edgeIndex < edges.length && edges[edgeIndex][2] < limit) {
                int rootU = find(parent, edges[edgeIndex][0]);
                int rootV = find(parent, edges[edgeIndex][1]);
                if (rootU != rootV) {
                    parent[rootU] = rootV;
                }
                edgeIndex++;
            }
            answer[queryIndex] = find(parent, queries[queryIndex][0]) == find(parent, queries[queryIndex][1]);
        }
        return answer;
    }

    /** Returns the root of x, compressing the path on the way. */
    private int find(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];
            x = parent[x];
        }
        return x;
    }
}
