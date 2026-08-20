package LeetCodeJava.DFS;

// https://leetcode.com/problems/maximum-path-quality-of-a-graph/

import java.util.ArrayList;
import java.util.List;

/**
 *  2065. Maximum Path Quality of a Graph
 *  Hard
 *
 *  There is an undirected graph with n nodes numbered from 0 to n - 1
 *  (inclusive). You are given a 0-indexed integer array values where values[i]
 *  is the value of the ith node. You are also given a 0-indexed 2D integer
 *  array edges, where each edges[j] = [u_j, v_j, time_j] indicates that there
 *  is an undirected edge between the nodes u_j and v_j, and it takes time_j
 *  seconds to travel between the two nodes. Finally, you are given an integer
 *  maxTime.
 *
 *  A valid path in the graph is any path that starts at node 0, ends at node 0,
 *  and takes at most maxTime seconds to complete. You may visit the same node
 *  multiple times. The quality of a valid path is the sum of the values of the
 *  unique nodes visited in the path (each node's value is added at most once).
 *
 *  Return the maximum quality of a valid path.
 *
 *  Note: There are at most four edges connected to each node.
 *
 *  Example 1:
 *    Input: values = [0,32,10,43], edges = [[0,1,10],[1,2,15],[0,3,10]],
 *           maxTime = 49
 *    Output: 75
 *    Explanation: 0 -> 1 -> 0 -> 3 -> 0 costs 40 <= 49 and visits {0,1,3},
 *                 so the quality is 0 + 32 + 43 = 75.
 *
 *  Example 2:
 *    Input: values = [5,10,15,20], edges = [[0,1,10],[1,2,10],[0,3,10]],
 *           maxTime = 30
 *    Output: 25
 *    Explanation: 0 -> 3 -> 0 costs 20 and visits {0,3} -> 5 + 20 = 25.
 *
 *  Constraints:
 *    n == values.length
 *    1 <= n <= 1000
 *    0 <= values[i] <= 10^8
 *    0 <= edges.length <= 2000
 *    edges[j].length == 3
 *    0 <= u_j < v_j <= n - 1
 *    10 <= time_j, maxTime <= 100
 *    All the pairs [u_j, v_j] are unique.
 *    There are at most four edges connected to each node.
 */
public class MaximumPathQualityOfAGraph {

    private List<List<int[]>> graph;
    private int[] values;
    private boolean[] visited;
    private int maxTime;
    private int best;

    // V0
    // IDEA: EXHAUSTIVE DFS (the time budget bounds the walk length)
    //       every edge costs >= 10 and maxTime <= 100, so a valid walk has at
    //       most 10 edges; each node has at most 4 neighbours -> at most 4^10
    //       walks, small enough to enumerate them all.
    //       `visited` marks nodes already COUNTED: re-entering a visited node
    //       is allowed (it costs time) but adds no value, so only the first
    //       entry flips the flag and adds values[v].
    //       the answer is refreshed whenever we stand on node 0, since the walk
    //       must both start and end there.
    /**
     * time = O(4^(maxTime / minEdgeTime) + N + E)
     * space = O(N + E)
     */
    public int maximalPathQuality(int[] values, int[][] edges, int maxTime) {
        int n = values.length;
        this.values = values;
        this.maxTime = maxTime;
        this.best = 0;

        this.graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<int[]>());
        }
        for (int[] e : edges) {
            graph.get(e[0]).add(new int[]{e[1], e[2]});
            graph.get(e[1]).add(new int[]{e[0], e[2]});
        }

        this.visited = new boolean[n];
        visited[0] = true;
        dfs(0, 0, values[0]);
        return best;
    }

    private void dfs(int u, int cost, int quality) {
        if (u == 0) {
            best = Math.max(best, quality);
        }
        for (int[] nb : graph.get(u)) {
            int v = nb[0], t = nb[1];
            if (cost + t > maxTime) {
                continue;
            }
            if (visited[v]) {
                // already counted, walking there only burns time
                dfs(v, cost + t, quality);
            } else {
                visited[v] = true;
                dfs(v, cost + t, quality + values[v]);
                visited[v] = false;
            }
        }
    }
}
