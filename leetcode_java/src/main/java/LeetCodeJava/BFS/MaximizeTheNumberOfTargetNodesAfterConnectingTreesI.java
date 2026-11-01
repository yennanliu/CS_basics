package LeetCodeJava.BFS;

// https://leetcode.com/problems/maximize-the-number-of-target-nodes-after-connecting-trees-i/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * 3372. Maximize the Number of Target Nodes After Connecting Trees I
 * Medium
 *
 * There exist two undirected trees with n and m nodes, labeled from [0, n - 1] and [0, m - 1], respectively.
 *
 * You are given two 2D integer arrays edges1 and edges2 of lengths n - 1 and m - 1, respectively, where edges1[i] = [a_i, b_i] indicates that there is an edge between nodes a_i and b_i in the first tree and edges2[i] = [u_i, v_i] indicates that there is an edge between nodes u_i and v_i in the second tree. You are also given an integer k.
 *
 * Node u is target to node v if the number of edges on the path from u to v is less than or equal to k. Note that a node is always target to itself.
 *
 * Return an array of n integers answer, where answer[i] is the maximum possible number of nodes target to node i of the first tree if you have to connect one node from the first tree to another node in the second tree.
 *
 * Note that queries are independent from each other. That is, for every query you will remove the added edge before proceeding to the next query.
 *
 *
 * Example 1:
 *
 * Input: edges1 = [[0,1],[0,2],[2,3],[2,4]], edges2 = [[0,1],[0,2],[0,3],[2,7],[1,4],[4,5],[4,6]], k = 2
 * Output: [9,7,9,8,8]
 * Explanation:
 * For i = 0, connect node 0 from the first tree to node 0 from the second tree.
 * For i = 1, connect node 1 from the first tree to node 0 from the second tree.
 * For i = 2, connect node 2 from the first tree to node 4 from the second tree.
 * For i = 3, connect node 3 from the first tree to node 4 from the second tree.
 * For i = 4, connect node 4 from the first tree to node 4 from the second tree.
 *
 * Example 2:
 *
 * Input: edges1 = [[0,1],[0,2],[0,3],[0,4]], edges2 = [[0,1],[1,2],[2,3]], k = 1
 * Output: [6,3,3,3,3]
 * Explanation:
 * For every i, connect node i of the first tree with any node of the second tree.
 *
 *
 * Constraints:
 *
 * 2 <= n, m <= 1000
 * edges1.length == n - 1
 * edges2.length == m - 1
 * edges1[i].length == edges2[i].length == 2
 * 0 <= a_i, b_i < n
 * 0 <= u_i, v_i < m
 * The input is generated such that edges1 and edges2 represent valid trees.
 * 0 <= k <= 1000
 *
 */
public class MaximizeTheNumberOfTargetNodesAfterConnectingTreesI {

    // V0
    // IDEA: BFS FROM EVERY NODE: COUNT WITHIN k IN TREE 1 + BEST "WITHIN k - 1" IN TREE 2
    //   the bridge costs one edge, so a tree-2 node is a target of i only if it
    //   is within k - 1 of the bridge's tree-2 endpoint. That endpoint is ours
    //   to choose and does not depend on i, so tree 2 adds the same best count
    //   to every answer. k = 0 makes the bridge useless ("within -1" = 0).
    //   n, m <= 1000, so one BFS per node is affordable.
    /**
     * time = O(N^2 + M^2)
     * space = O(N + M)
     */
    public int[] maxTargetNodes(int[][] edges1, int[][] edges2, int k) {
        List<List<Integer>> tree1 = buildTree(edges1);
        List<List<Integer>> tree2 = buildTree(edges2);

        int[] near1 = countWithin(tree1, k);
        int[] near2 = countWithin(tree2, k - 1);

        // best bridge endpoint in tree 2
        int bonus = 0;
        for (int count : near2) {
            bonus = Math.max(bonus, count);
        }

        int[] answer = new int[near1.length];
        for (int i = 0; i < near1.length; i++) {
            answer[i] = near1[i] + bonus;
        }
        return answer;
    }

    /** Adjacency list of a tree with edges.length + 1 nodes. */
    private List<List<Integer>> buildTree(int[][] edges) {
        int n = edges.length + 1;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        return graph;
    }

    /** For each node, how many nodes are within `limit` edges of it (0 for all if limit < 0). */
    private int[] countWithin(List<List<Integer>> graph, int limit) {
        int n = graph.size();
        int[] counts = new int[n];
        if (limit < 0) {
            return counts;
        }
        for (int source = 0; source < n; source++) {
            int[] distance = new int[n];
            Arrays.fill(distance, -1);
            distance[source] = 0;
            Queue<Integer> queue = new ArrayDeque<>();
            queue.offer(source);
            int reached = 0;
            while (!queue.isEmpty()) {
                int node = queue.poll();
                reached++;
                for (int next : graph.get(node)) {
                    // only enqueue nodes still inside the distance limit
                    if (distance[next] == -1 && distance[node] + 1 <= limit) {
                        distance[next] = distance[node] + 1;
                        queue.offer(next);
                    }
                }
            }
            counts[source] = reached;
        }
        return counts;
    }
}
