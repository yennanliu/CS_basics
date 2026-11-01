package LeetCodeJava.BFS;

// https://leetcode.com/problems/reachable-nodes-with-restrictions/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * 2368. Reachable Nodes With Restrictions
 * Medium
 *
 * There is an undirected tree with n nodes labeled from 0 to n - 1 and n - 1 edges.
 *
 * You are given a 2D integer array edges of length n - 1 where edges[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the tree. You are also given an integer array restricted which represents restricted nodes.
 *
 * Return the maximum number of nodes you can reach from node 0 without visiting a restricted node.
 *
 * Note that node 0 will not be a restricted node.
 *
 *
 * Example 1:
 *
 * Input: n = 7, edges = [[0,1],[1,2],[3,1],[4,0],[0,5],[5,6]], restricted = [4,5]
 * Output: 4
 * Explanation: The diagram above shows the tree.
 * We have that [0,1,2,3] is the only possible set of nodes you can reach from 0 without visiting a restricted node.
 *
 * Example 2:
 *
 * Input: n = 7, edges = [[0,1],[0,2],[0,5],[0,4],[3,2],[6,5]], restricted = [4,2,1]
 * Output: 3
 * Explanation: The diagram above shows the tree.
 * We have that [0,5,6] is the only possible set of nodes you can reach from 0 without visiting a restricted node.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 * edges.length == n - 1
 * edges[i].length == 2
 * 0 <= ai, bi < n
 * ai != bi
 * edges represents a valid tree.
 * 1 <= restricted.length < n
 * 1 <= restricted[i] < n
 * All the values of restricted are unique.
 *
 */
public class ReachableNodesWithRestrictions {

    // V0
    // IDEA: BFS FROM NODE 0, TREATING RESTRICTED NODES AS WALLS
    //   the graph is a tree, so nothing is reachable "around" a blocked node:
    //   never enqueueing it cuts off its whole branch. Answer = nodes visited.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int reachableNodes(int n, int[][] edges, int[] restricted) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        // marking restricted nodes as already visited keeps BFS out of them
        boolean[] visited = new boolean[n];
        for (int node : restricted) {
            visited[node] = true;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(0);
        visited[0] = true;
        int reachable = 0;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            reachable++;
            for (int next : graph.get(node)) {
                if (!visited[next]) {
                    visited[next] = true;
                    queue.offer(next);
                }
            }
        }
        return reachable;
    }
}
