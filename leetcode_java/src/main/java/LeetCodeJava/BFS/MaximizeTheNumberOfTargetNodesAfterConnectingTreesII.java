package LeetCodeJava.BFS;

// https://leetcode.com/problems/maximize-the-number-of-target-nodes-after-connecting-trees-ii/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * 3373. Maximize the Number of Target Nodes After Connecting Trees II
 * Hard
 *
 * There exist two undirected trees with n and m nodes, labeled from [0, n - 1] and [0, m - 1], respectively.
 *
 * You are given two 2D integer arrays edges1 and edges2 of lengths n - 1 and m - 1, respectively, where edges1[i] = [a_i, b_i] indicates that there is an edge between nodes a_i and b_i in the first tree and edges2[i] = [u_i, v_i] indicates that there is an edge between nodes u_i and v_i in the second tree.
 *
 * Node u is target to node v if the number of edges on the path from u to v is even. Note that a node is always target to itself.
 *
 * Return an array of n integers answer, where answer[i] is the maximum possible number of nodes that are target to node i of the first tree if you had to connect one node from the first tree to another node in the second tree.
 *
 * Note that queries are independent from each other. That is, for every query you will remove the added edge before proceeding to the next query.
 *
 *
 * Example 1:
 *
 * Input: edges1 = [[0,1],[0,2],[2,3],[2,4]], edges2 = [[0,1],[0,2],[0,3],[2,7],[1,4],[4,5],[4,6]]
 * Output: [8,7,7,8,8]
 * Explanation:
 * For i = 0, connect node 0 from the first tree to node 0 from the second tree.
 * For i = 1, connect node 1 from the first tree to node 4 from the second tree.
 * For i = 2, connect node 2 from the first tree to node 7 from the second tree.
 * For i = 3, connect node 3 from the first tree to node 0 from the second tree.
 * For i = 4, connect node 4 from the first tree to node 4 from the second tree.
 *
 * Example 2:
 *
 * Input: edges1 = [[0,1],[0,2],[0,3],[0,4]], edges2 = [[0,1],[1,2],[2,3]]
 * Output: [3,6,6,6,6]
 * Explanation:
 * For every i, connect node i of the first tree with any node of the second tree.
 *
 *
 * Constraints:
 *
 * 2 <= n, m <= 10^5
 * edges1.length == n - 1
 * edges2.length == m - 1
 * edges1[i].length == edges2[i].length == 2
 * 0 <= a_i, b_i < n
 * 0 <= u_i, v_i < m
 * The input is generated such that edges1 and edges2 represent valid trees.
 *
 */
public class MaximizeTheNumberOfTargetNodesAfterConnectingTreesII {

    // V0
    // IDEA: BFS 2-COLOURING: "EVEN DISTANCE" == "SAME COLOUR" IN A TREE
    //   the unique tree path between two nodes has even length exactly when
    //   they share a colour in the bipartite 2-colouring. So node i targets its
    //   whole colour class in tree 1. The bridge adds one edge (flips parity),
    //   and its tree-2 endpoint is ours to pick, so tree 2 adds the larger of
    //   its two colour classes -- the same constant for every i.
    /**
     * time = O(N + M)
     * space = O(N + M)
     */
    public int[] maxTargetNodes(int[][] edges1, int[][] edges2) {
        int[] colour1 = colourTree(edges1);
        int[] colour2 = colourTree(edges2);

        // classSize1[c] = number of tree-1 nodes with colour c
        int[] classSize1 = new int[2];
        for (int c : colour1) {
            classSize1[c]++;
        }
        int[] classSize2 = new int[2];
        for (int c : colour2) {
            classSize2[c]++;
        }
        int bonus = Math.max(classSize2[0], classSize2[1]);

        int[] answer = new int[colour1.length];
        for (int i = 0; i < colour1.length; i++) {
            answer[i] = classSize1[colour1[i]] + bonus;
        }
        return answer;
    }

    /** BFS 2-colouring of a tree from node 0; colour[i] is 0 or 1. */
    private int[] colourTree(int[][] edges) {
        int n = edges.length + 1;
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }

        int[] colour = new int[n];
        Arrays.fill(colour, -1);
        colour[0] = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(0);
        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int next : graph.get(node)) {
                if (colour[next] == -1) {
                    // neighbours always take the opposite colour
                    colour[next] = 1 - colour[node];
                    queue.offer(next);
                }
            }
        }
        return colour;
    }
}
