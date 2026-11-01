package LeetCodeJava.BFS;

// https://leetcode.com/problems/divide-nodes-into-the-maximum-number-of-groups/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * 2493. Divide Nodes Into the Maximum Number of Groups
 * Hard
 *
 * You are given a positive integer n representing the number of nodes in an undirected graph. The nodes are labeled from 1 to n.
 *
 * You are also given a 2D integer array edges, where edges[i] = [ai, bi] indicates that there is a bidirectional edge between nodes ai and bi. Notice that the given graph may be disconnected.
 *
 * Divide the nodes of the graph into m groups (1-indexed) such that:
 *
 * Each node in the graph belongs to exactly one group.
 * For each pair of nodes in the graph that are connected by an edge [ai, bi], if ai belongs to the group with index x, and bi belongs to the group with index y, then |y - x| = 1.
 *
 * Return the maximum number of groups (i.e., maximum m) into which you can divide the nodes. Return -1 if it is impossible to group the nodes with the given conditions.
 *
 *
 * Example 1:
 *
 * Input: n = 6, edges = [[1,2],[1,4],[1,5],[2,6],[2,3],[4,6]]
 * Output: 4
 * Explanation: As shown in the image we:
 * - Add node 5 to the first group.
 * - Add node 1 to the second group.
 * - Add nodes 2 and 4 to the third group.
 * - Add nodes 3 and 6 to the fourth group.
 * We can see that every edge is satisfied.
 * It can be shown that that if we create a fifth group and move any node from the third or fourth group to it, at least on of the edges will not be satisfied.
 *
 * Example 2:
 *
 * Input: n = 3, edges = [[1,2],[2,3],[3,1]]
 * Output: -1
 * Explanation: If we add node 1 to the first group, node 2 to the second group, and node 3 to the third group to satisfy the first two edges, we can see that the third edge will not be satisfied.
 * It is not possible to assign the nodes to groups in such a way that satisfies all the edges.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 500
 * 1 <= edges.length <= 10^4
 * edges[i].length == 2
 * 1 <= ai, bi <= n
 * ai != bi
 * There is at most one edge between any pair of vertices.
 *
 */
public class DivideNodesIntoTheMaximumNumberOfGroups {

    // V0
    // IDEA: BFS LAYERING FROM EVERY START, BEST PER CONNECTED COMPONENT
    //   "adjacent nodes' groups differ by exactly 1" means the group index is a
    //   BFS depth from whichever node is in group 1. For one start, the number of
    //   groups = that BFS's layer count; a component takes its best start.
    //   An edge joining two nodes at the SAME depth is an odd cycle -> -1.
    //   Components are independent, so the answer is the sum of their maxima.
    //   n <= 500, so a BFS from every node is cheap.
    /**
     * time = O(V * (V + E))
     * space = O(V + E)
     */
    public int magnificentSets(int n, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        // label each node with its connected component id (1-based)
        int[] component = new int[n + 1];
        int componentCount = 0;
        for (int start = 1; start <= n; start++) {
            if (component[start] != 0) {
                continue;
            }
            componentCount++;
            component[start] = componentCount;
            Queue<Integer> queue = new ArrayDeque<>();
            queue.offer(start);
            while (!queue.isEmpty()) {
                int node = queue.poll();
                for (int next : graph.get(node)) {
                    if (component[next] == 0) {
                        component[next] = componentCount;
                        queue.offer(next);
                    }
                }
            }
        }

        // best layer count for each component, over every possible start
        int[] bestOfComponent = new int[componentCount + 1];
        for (int source = 1; source <= n; source++) {
            int layers = countLayers(graph, n, source);
            if (layers == -1) {
                return -1;
            }
            int id = component[source];
            bestOfComponent[id] = Math.max(bestOfComponent[id], layers);
        }

        int total = 0;
        for (int id = 1; id <= componentCount; id++) {
            total += bestOfComponent[id];
        }
        return total;
    }

    /** Number of BFS layers starting at source, or -1 if an odd cycle is found. */
    private int countLayers(List<List<Integer>> graph, int n, int source) {
        int[] depth = new int[n + 1];
        Arrays.fill(depth, 0);
        depth[source] = 1;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(source);
        int maxDepth = 1;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int next : graph.get(node)) {
                if (depth[next] == 0) {
                    depth[next] = depth[node] + 1;
                    maxDepth = Math.max(maxDepth, depth[next]);
                    queue.offer(next);
                } else if (depth[next] == depth[node]) {
                    // two neighbours in the same group: not bipartite
                    return -1;
                }
            }
        }
        return maxDepth;
    }
}
