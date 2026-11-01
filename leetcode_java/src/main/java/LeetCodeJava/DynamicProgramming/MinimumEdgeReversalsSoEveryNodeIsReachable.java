package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/minimum-edge-reversals-so-every-node-is-reachable/description/

import java.util.*;

/**
 * 2858. Minimum Edge Reversals So Every Node Is Reachable
 * Hard
 *
 * There is a simple directed graph with n nodes labeled from 0 to n - 1. The graph would form a tree if its edges were bi-directional.
 *
 * You are given an integer n and a 2D integer array edges, where edges[i] = [ui, vi] represents a directed edge going from node ui to node vi.
 *
 * An edge reversal changes the direction of an edge, i.e., a directed edge going from node ui to node vi becomes a directed edge going from node vi to node ui.
 *
 * For every node i in the range [0, n - 1], your task is to independently calculate the minimum number of edge reversals required so it is possible to reach any other node starting from node i through a sequence of directed edges.
 *
 * Return an integer array answer, where answer[i] is the minimum number of edge reversals required so it is possible to reach any other node starting from node i through a sequence of directed edges.
 *
 *
 * Example 1:
 *
 * Input: n = 4, edges = [[2,0],[2,1],[1,3]]
 * Output: [1,1,0,2]
 * Explanation: The image above shows the graph formed by the edges.
 * For node 0: after reversing the edge [2,0], it is possible to reach any other node starting from node 0.
 * So, answer[0] = 1.
 * For node 1: after reversing the edge [2,1], it is possible to reach any other node starting from node 1.
 * So, answer[1] = 1.
 * For node 2: it is already possible to reach any other node starting from node 2.
 * So, answer[2] = 0.
 * For node 3: after reversing the edges [1,3] and [2,1], it is possible to reach any other node starting from node 3.
 * So, answer[3] = 2.
 *
 * Example 2:
 *
 * Input: n = 3, edges = [[1,2],[2,0]]
 * Output: [2,0,1]
 * Explanation: The image above shows the graph formed by the edges.
 * For node 0: after reversing the edges [2,0] and [1,2], it is possible to reach any other node starting from node 0.
 * So, answer[0] = 2.
 * For node 1: it is already possible to reach any other node starting from node 1.
 * So, answer[1] = 0.
 * For node 2: after reversing the edge [1, 2], it is possible to reach any other node starting from node 2.
 * So, answer[2] = 1.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 10^5
 * edges.length == n - 1
 * edges[i].length == 2
 * 0 <= ui == edges[i][0] < n
 * 0 <= vi == edges[i][1] < n
 * ui != vi
 * The input is generated such that if the edges were bi-directional, the graph would be a tree.
 *
 */
public class MinimumEdgeReversalsSoEveryNodeIsReachable {

    // V0
    // IDEA: RE-ROOTING TREE DP
    //   Treat each directed edge u->v as undirected with a walking cost:
    //   u->v costs 0 (natural direction), v->u costs 1 (must be reversed).
    //   Step 1: root at 0, ans[0] = number of edges walked against their direction.
    //   Step 2: move the root across an edge u->v with walking cost c (seen from u):
    //           ans[v] = ans[u] + 1 - 2 * c  (only that one edge changes its role).
    //   Both traversals are iterative (n up to 1e5, a path-shaped tree would overflow the stack).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int[] minEdgeReversals(int n, int[][] edges) {
        // adj[u] = list of {neighbour, cost of walking u -> neighbour}
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            int from = edge[0];
            int to = edge[1];
            adj.get(from).add(new int[] {to, 0});
            adj.get(to).add(new int[] {from, 1});
        }

        int[] ans = new int[n];

        // step 1: reversals needed when node 0 is the root
        int reversalsFromRoot = 0;
        boolean[] visited = new boolean[n];
        Deque<Integer> stack = new ArrayDeque<>();
        visited[0] = true;
        stack.push(0);
        while (!stack.isEmpty()) {
            int node = stack.pop();
            for (int[] next : adj.get(node)) {
                int neighbour = next[0];
                if (!visited[neighbour]) {
                    visited[neighbour] = true;
                    reversalsFromRoot += next[1];
                    stack.push(neighbour);
                }
            }
        }
        ans[0] = reversalsFromRoot;

        // step 2: re-root from each node to its children
        visited = new boolean[n];
        visited[0] = true;
        stack.push(0);
        while (!stack.isEmpty()) {
            int node = stack.pop();
            for (int[] next : adj.get(node)) {
                int neighbour = next[0];
                int cost = next[1];
                if (!visited[neighbour]) {
                    visited[neighbour] = true;
                    // cost 0: the edge now points back toward the old root -> +1
                    // cost 1: the edge already points away from the new root -> -1
                    ans[neighbour] = ans[node] + 1 - 2 * cost;
                    stack.push(neighbour);
                }
            }
        }
        return ans;
    }
}
