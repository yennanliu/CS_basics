package LeetCodeJava.BFS;

// https://leetcode.com/problems/find-minimum-diameter-after-merging-two-trees/description/

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/**
 * 3203. Find Minimum Diameter After Merging Two Trees
 * Hard
 *
 * There exist two undirected trees with n and m nodes, numbered from 0 to n - 1 and from 0 to m - 1, respectively. You are given two 2D integer arrays edges1 and edges2 of lengths n - 1 and m - 1, respectively, where edges1[i] = [ai, bi] indicates that there is an edge between nodes ai and bi in the first tree and edges2[i] = [ui, vi] indicates that there is an edge between nodes ui and vi in the second tree.
 *
 * You must connect one node from the first tree with another node from the second tree with an edge.
 *
 * Return the minimum possible diameter of the resulting tree.
 *
 * The diameter of a tree is the length of the longest path between any two nodes in the tree.
 *
 *
 * Example 1:
 *
 * Input: edges1 = [[0,1],[0,2],[0,3]], edges2 = [[0,1]]
 * Output: 3
 * Explanation:
 * We can obtain a tree of diameter 3 by connecting node 0 from the first tree with any node from the second tree.
 *
 * Example 2:
 *
 * Input: edges1 = [[0,1],[0,2],[0,3],[2,4],[2,5],[3,6],[2,7]], edges2 = [[0,1],[0,2],[0,3],[2,4],[2,5],[3,6],[2,7]]
 * Output: 5
 * Explanation:
 * We can obtain a tree of diameter 5 by connecting node 0 from the first tree with node 0 from the second tree.
 *
 *
 * Constraints:
 *
 * 1 <= n, m <= 10^5
 * edges1.length == n - 1
 * edges2.length == m - 1
 * edges1[i].length == edges2[i].length == 2
 * edges1[i] = [ai, bi]
 * 0 <= ai, bi < n
 * edges2[i] = [ui, vi]
 * 0 <= ui, vi < m
 * The input is generated such that edges1 and edges2 represent valid trees.
 *
 */
public class FindMinimumDiameterAfterMergingTwoTrees {

    // V0
    // IDEA: CONNECT THE TWO CENTRES -> max(d1, d2, ceil(d1/2) + ceil(d2/2) + 1)
    //   the merged tree's longest path either stays in tree 1 (d1), stays in
    //   tree 2 (d2), or crosses the new edge (ecc1 + 1 + ecc2). The smallest
    //   eccentricity a node can have is the radius ceil(d / 2), at the centre.
    //   Each diameter comes from the classic double BFS: farthest node from any
    //   start, then the farthest distance from that node.
    /**
     * time = O(N + M)
     * space = O(N + M)
     */
    public int minimumDiameterAfterMerge(int[][] edges1, int[][] edges2) {
        int diameter1 = diameter(edges1);
        int diameter2 = diameter(edges2);
        // ceil(d / 2) == (d + 1) / 2 for non-negative d
        int crossing = (diameter1 + 1) / 2 + (diameter2 + 1) / 2 + 1;
        return Math.max(Math.max(diameter1, diameter2), crossing);
    }

    /** Diameter (in edges) of a tree given by its edge list. */
    private int diameter(int[][] edges) {
        int n = edges.length + 1;
        if (n == 1) {
            return 0;
        }
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        // the farthest node from any start is one end of a diameter
        int[] first = farthest(graph, 0);
        int[] second = farthest(graph, first[0]);
        return second[1];
    }

    /** BFS from source; returns {farthest node, its distance}. */
    private int[] farthest(List<List<Integer>> graph, int source) {
        int n = graph.size();
        int[] distance = new int[n];
        Arrays.fill(distance, -1);
        distance[source] = 0;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.offer(source);
        int farNode = source;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            if (distance[node] > distance[farNode]) {
                farNode = node;
            }
            for (int next : graph.get(node)) {
                if (distance[next] == -1) {
                    distance[next] = distance[node] + 1;
                    queue.offer(next);
                }
            }
        }
        return new int[] { farNode, distance[farNode] };
    }
}
