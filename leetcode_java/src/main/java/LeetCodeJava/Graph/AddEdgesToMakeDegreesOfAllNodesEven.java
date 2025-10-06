package LeetCodeJava.Graph;

// https://leetcode.com/problems/add-edges-to-make-degrees-of-all-nodes-even/description/

import java.util.*;

/**
 * 2508. Add Edges to Make Degrees of All Nodes Even
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There is an undirected graph consisting of n nodes numbered from 1 to n. You are given the integer n and a 2D array edges where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi. The graph can be disconnected.
 *
 * You can add at most two additional edges (possibly none) to this graph so that there are no repeated edges and no self-loops.
 *
 * Return true if it is possible to make the degree of each node in the graph even, otherwise return false.
 *
 * The degree of a node is the number of edges connected to it.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 5, edges = [[1,2],[2,3],[3,4],[4,2],[1,4],[2,5]]
 * Output: true
 * Explanation: The above diagram shows a valid way of adding an edge.
 * Every node in the resulting graph is connected to an even number of edges.
 * Example 2:
 *
 *
 * Input: n = 4, edges = [[1,2],[3,4]]
 * Output: true
 * Explanation: The above diagram shows a valid way of adding two edges.
 * Example 3:
 *
 *
 * Input: n = 4, edges = [[1,2],[1,3],[1,4]]
 * Output: false
 * Explanation: It is not possible to obtain a valid graph with adding at most 2 edges.
 *
 *
 * Constraints:
 *
 * 3 <= n <= 105
 * 2 <= edges.length <= 105
 * edges[i].length == 2
 * 1 <= ai, bi <= n
 * ai != bi
 * There are no repeated edges.
 *
 */
public class AddEdgesToMakeDegreesOfAllNodesEven {

    // V0
//    public boolean isPossible(int n, List<List<Integer>> edges) {
//
//    }

    // V1

    // V2
    // https://leetcode.com/problems/add-edges-to-make-degrees-of-all-nodes-even/solutions/7118040/add-edges-to-make-degrees-of-all-nodes-e-1og4/
    public boolean isPossible_2(int n, List<List<Integer>> edges) {
        Set<Integer>[] g = new Set[n + 1];
        Arrays.setAll(g, k -> new HashSet<>());
        for (List<Integer> e : edges) {
            int a = e.get(0), b = e.get(1);
            g[a].add(b);
            g[b].add(a);
        }
        List<Integer> vs = new ArrayList<>();
        for (int i = 1; i <= n; ++i) {
            if (g[i].size() % 2 == 1) {
                vs.add(i);
            }
        }
        if (vs.size() == 0) {
            return true;
        }
        if (vs.size() == 2) {
            int a = vs.get(0), b = vs.get(1);
            if (!g[a].contains(b)) {
                return true;
            }
            for (int c = 1; c <= n; ++c) {
                if (a != c && b != c && !g[a].contains(c) && !g[c].contains(b)) {
                    return true;
                }
            }
            return false;
        }
        if (vs.size() == 4) {
            int a = vs.get(0), b = vs.get(1), c = vs.get(2), d = vs.get(3);
            if (!g[a].contains(b) && !g[c].contains(d)) {
                return true;
            }
            if (!g[a].contains(c) && !g[b].contains(d)) {
                return true;
            }
            if (!g[a].contains(d) && !g[b].contains(c)) {
                return true;
            }
            return false;
        }
        return false;
    }


}
