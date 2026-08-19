package LeetCodeJava.Tree;

// https://leetcode.com/problems/closest-node-to-path-in-tree/

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 *  2277. Closest Node to Path in Tree
 *  Hard
 *
 *  You are given a positive integer n representing the number of nodes in a tree,
 *  numbered from 0 to n - 1 (inclusive). You are also given a 2D integer array
 *  edges of length n - 1, where edges[i] = [node1_i, node2_i] denotes that there
 *  is a bidirectional edge connecting node1_i and node2_i in the tree.
 *
 *  You are given a 0-indexed integer array query of length m where
 *  query[i] = [start_i, end_i, node_i] means that for the ith query, you are
 *  tasked with finding the node on the path from start_i to end_i that is closest
 *  to node_i.
 *
 *  Return an integer array answer of length m, where answer[i] is the answer to
 *  the ith query.
 *
 *  Example 1:
 *    Input: n = 7, edges = [[0,1],[0,2],[0,3],[1,4],[2,5],[2,6]],
 *           query = [[5,3,4],[5,3,6]]
 *    Output: [0,2]
 *    Explanation: the path 5 -> 3 is [5,2,0,3]; node 0 is the closest to 4,
 *                 node 2 is the closest to 6.
 *
 *  Example 2:
 *    Input: n = 3, edges = [[0,1],[1,2]], query = [[0,1,2]]
 *    Output: [1]
 *
 *  Constraints:
 *    1 <= n <= 1000
 *    edges.length == n - 1
 *    1 <= query.length <= 1000
 *    query[i].length == 3
 *    0 <= start_i, end_i, node_i <= n - 1
 *    The graph is a tree.
 */
public class ClosestNodeToPathInTree {

    // V0
    // IDEA: n <= 1000 -> PRECOMPUTE ALL-PAIRS DISTANCES WITH n BFS RUNS.
    //       the tree is unweighted, so a BFS from every node fills
    //       dist[u][v] in O(n * (V + E)) ~ 10^6 steps.
    //       with that table, "v is on the path start -> end" becomes a
    //       distance identity (unique tree path):
    //           dist[start][v] + dist[v][end] == dist[start][end]
    //       so each query scans all n nodes and keeps the on-path one that
    //       minimises dist[node][v].
    /**
     * time = O(N^2 + M*N)
     * space = O(N^2)
     */
    public int[] closestNode(int n, int[][] edges, int[][] query) {
        List<List<Integer>> g = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            g.add(new ArrayList<Integer>());
        }
        for (int[] e : edges) {
            g.get(e[0]).add(e[1]);
            g.get(e[1]).add(e[0]);
        }

        int[][] dist = new int[n][n];
        for (int src = 0; src < n; src++) {
            int[] d = dist[src];
            for (int i = 0; i < n; i++) {
                d[i] = -1;
            }
            d[src] = 0;
            Queue<Integer> q = new LinkedList<>();
            q.add(src);
            while (!q.isEmpty()) {
                int cur = q.poll();
                for (int nxt : g.get(cur)) {
                    if (d[nxt] == -1) {
                        d[nxt] = d[cur] + 1;
                        q.add(nxt);
                    }
                }
            }
        }

        int[] res = new int[query.length];
        for (int i = 0; i < query.length; i++) {
            int start = query[i][0];
            int end = query[i][1];
            int node = query[i][2];
            int best = -1;
            int bestDist = Integer.MAX_VALUE;
            for (int v = 0; v < n; v++) {
                // NOTE !!! on-path test via the distance identity
                if (dist[start][v] + dist[v][end] != dist[start][end]) {
                    continue;
                }
                if (dist[node][v] < bestDist) {
                    bestDist = dist[node][v];
                    best = v;
                }
            }
            res[i] = best;
        }
        return res;
    }
}
