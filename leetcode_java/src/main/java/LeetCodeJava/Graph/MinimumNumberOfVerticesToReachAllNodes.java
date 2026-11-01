package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-number-of-vertices-to-reach-all-nodes/description/

import java.util.*;

/**
 * 1557. Minimum Number of Vertices to Reach All Nodes
 * Medium
 *
 * Given a directed acyclic graph, with n vertices numbered from 0 to n-1, and an array edges where
 * edges[i] = [fromi, toi] represents a directed edge from node fromi to node toi.
 *
 * Find the smallest set of vertices from which all nodes in the graph are reachable.
 * It's guaranteed that a unique solution exists.
 *
 * Notice that you can return the vertices in any order.
 *
 *
 * Example 1:
 *
 * Input: n = 6, edges = [[0,1],[0,2],[2,5],[3,4],[4,2]]
 * Output: [0,3]
 * Explanation: It's not possible to reach all the nodes from a single vertex. From 0 we can reach [0,1,2,5].
 * From 3 we can reach [3,4,2,5]. So we output [0,3].
 *
 * Example 2:
 *
 * Input: n = 5, edges = [[0,1],[2,1],[3,1],[1,4],[2,4]]
 * Output: [0,2,3]
 * Explanation: Notice that vertices 0, 3 and 2 are not reachable from any other node, so we must include them.
 * Also any of these vertices can reach nodes 1 and 4.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 * 1 <= edges.length <= min(10^5, n * (n - 1) / 2)
 * edges[i].length == 2
 * 0 <= fromi, toi < n
 * All pairs (fromi, toi) are distinct.
 *
 */
public class MinimumNumberOfVerticesToReachAllNodes {

    // V0
    // IDEA: IN-DEGREE (a node with an incoming edge is reachable from elsewhere)
    // in a DAG a node with in-degree 0 can never be reached from another node, so it must
    // be in the answer; every other node has a parent, and walking parents upwards always
    // ends at an in-degree-0 node -> those nodes alone are enough.
    /**
     * time = O(N + E)
     * space = O(N)
     */
    public List<Integer> findSmallestSetOfVertices(int n, List<List<Integer>> edges) {
        // hasParent[v] = some edge points into v
        boolean[] hasParent = new boolean[n];
        for (List<Integer> edge : edges) {
            hasParent[edge.get(1)] = true;
        }

        List<Integer> result = new ArrayList<>();
        for (int node = 0; node < n; node++) {
            if (!hasParent[node]) {
                result.add(node);
            }
        }
        return result;
    }
}
