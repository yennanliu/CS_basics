package LeetCodeJava.DFS;

// https://leetcode.com/problems/all-paths-from-source-lead-to-destination/

import java.util.ArrayList;
import java.util.List;

/**
 *  1059. All Paths from Source Lead to Destination
 *  Medium
 *
 *  Given the edges of a directed graph where edges[i] = [ai, bi] indicates there is an
 *  edge between nodes ai and bi, and two nodes source and destination of this graph,
 *  determine whether or not all paths starting from source eventually end at
 *  destination, that is:
 *
 *    - At least one path exists from the source node to the destination node.
 *    - If a path exists from the source node to a node with no outgoing edges,
 *      then that node is equal to destination.
 *    - The number of possible paths from source to destination is a finite number.
 *
 *  Return true if and only if all roads from source lead to destination.
 *
 *  Example 1:
 *    Input: n = 3, edges = [[0,1],[0,2]], source = 0, destination = 2
 *    Output: false
 *    Explanation: It is possible to reach and get stuck on both node 1 and node 2.
 *
 *  Example 2:
 *    Input: n = 4, edges = [[0,1],[0,3],[1,2],[2,1]], source = 0, destination = 3
 *    Output: false
 *    Explanation: we can either end at node 3, or loop over nodes 1 and 2 forever.
 *
 *  Example 3:
 *    Input: n = 4, edges = [[0,1],[0,2],[1,3],[2,3]], source = 0, destination = 3
 *    Output: true
 *
 *  Constraints:
 *    1 <= n <= 10^4
 *    0 <= edges.length <= 10^4
 *    edges[i].length == 2
 *    0 <= ai, bi <= n - 1
 *    0 <= source <= n - 1
 *    0 <= destination <= n - 1
 *    The given graph may have self-loops and parallel edges.
 */
public class AllPathsFromSourceLeadToDestination {

    // V0
    // IDEA: DFS + 3 COLOR (white / gray / black) CYCLE DETECTION
    //       state[i] == 0 : not visited
    //       state[i] == 1 : on the current DFS stack -> seeing it again is a CYCLE -> false
    //       state[i] == 2 : already proven "all paths from i reach destination"
    //       A node with NO outgoing edge is good only if it IS the destination, which
    //       also forces the destination itself to have no outgoing edge (otherwise we
    //       could walk away from it).
    /**
     * time = O(V + E)
     * space = O(V + E)
     */
    public boolean leadsToDestination(int n, int[][] edges, int source, int destination) {

        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<Integer>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
        }

        int[] state = new int[n];
        return dfs(source, destination, adj, state);
    }

    private boolean dfs(int cur, int destination, List<List<Integer>> adj, int[] state) {

        if (state[cur] == 1) {
            return false; // back edge -> infinite loop
        }
        if (state[cur] == 2) {
            return true; // already validated
        }

        List<Integer> nexts = adj.get(cur);
        if (nexts.isEmpty()) {
            // a dead end is acceptable only when it is the destination
            return cur == destination;
        }
        if (cur == destination) {
            // destination with outgoing edges -> we can leave it again
            return false;
        }

        state[cur] = 1;
        for (Integer nxt : nexts) {
            if (!dfs(nxt, destination, adj, state)) {
                return false;
            }
        }
        state[cur] = 2;
        return true;
    }
}
