package LeetCodeJava.DFS;

// https://leetcode.com/problems/critical-connections-in-a-network/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  1192. Critical Connections in a Network
 *  Hard
 *
 *  There are n servers numbered from 0 to n - 1 connected by undirected server-to-server
 *  connections forming a network where connections[i] = [ai, bi] represents a connection
 *  between servers ai and bi. Any server can reach other servers directly or indirectly
 *  through the network.
 *
 *  A critical connection is a connection that, if removed, will make some servers unable to
 *  reach some other server.
 *
 *  Return all critical connections in the network in any order.
 *
 *  Example 1:
 *  Input: n = 4, connections = [[0,1],[1,2],[2,0],[1,3]]
 *  Output: [[1,3]]
 *
 *  Example 2:
 *  Input: n = 2, connections = [[0,1]]
 *  Output: [[0,1]]
 *
 *  Constraints:
 *  2 <= n <= 10^5
 *  n - 1 <= connections.length <= 10^5
 *  0 <= ai, bi <= n - 1
 *  ai != bi
 *  There are no repeated connections.
 */
public class CriticalConnectionsInANetwork {

    // V0
    // IDEA: TARJAN bridge finding - dfs assigning a discovery time `disc[u]` and the lowest
    //       discovery time `low[u]` reachable from u's subtree; edge (u, v) is a bridge when
    //       low[v] > disc[u] (v's subtree has no back-edge above u)
    /**
     * time = O(V + E)
     * space = O(V + E)
     */
    public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<Integer>());
        }
        for (List<Integer> c : connections) {
            graph.get(c.get(0)).add(c.get(1));
            graph.get(c.get(1)).add(c.get(0));
        }

        int[] disc = new int[n];
        int[] low = new int[n];
        Arrays.fill(disc, -1);
        List<List<Integer>> res = new ArrayList<>();
        dfs(0, -1, graph, disc, low, new int[]{0}, res);
        return res;
    }

    private void dfs(int u, int parent, List<List<Integer>> graph, int[] disc, int[] low,
                     int[] timer, List<List<Integer>> res) {
        disc[u] = low[u] = timer[0]++;
        for (Integer v : graph.get(u)) {
            if (v == parent) {
                continue; // skip the edge we came from
            }
            if (disc[v] == -1) {
                dfs(v, u, graph, disc, low, timer, res);
                low[u] = Math.min(low[u], low[v]);
                if (low[v] > disc[u]) {
                    List<Integer> bridge = new ArrayList<>();
                    bridge.add(u);
                    bridge.add(v);
                    res.add(bridge);
                }
            } else {
                low[u] = Math.min(low[u], disc[v]); // back edge
            }
        }
    }
}
