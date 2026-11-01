package LeetCodeJava.BFS;

// https://leetcode.com/problems/shortest-path-with-alternating-colors/description/

import java.util.*;

/**
 * 1129. Shortest Path with Alternating Colors
 * Medium
 *
 * You are given an integer n, the number of nodes in a directed graph where the nodes
 * are labeled from 0 to n - 1. Each edge is red or blue in this graph, and there could be
 * self-edges and parallel edges.
 *
 * You are given two arrays redEdges and blueEdges where:
 *
 * redEdges[i] = [ai, bi] indicates that there is a directed red edge from node ai to node bi
 * in the graph, and
 * blueEdges[j] = [uj, vj] indicates that there is a directed blue edge from node uj to node vj
 * in the graph.
 *
 * Return an array answer of length n, where each answer[x] is the length of the shortest path
 * from node 0 to node x such that the edge colors alternate along the path,
 * or -1 if such a path does not exist.
 *
 *
 * Example 1:
 *
 * Input: n = 3, redEdges = [[0,1],[1,2]], blueEdges = []
 * Output: [0,1,-1]
 *
 * Example 2:
 *
 * Input: n = 3, redEdges = [[0,1]], blueEdges = [[2,1]]
 * Output: [0,1,-1]
 *
 *
 * Constraints:
 *
 * 1 <= n <= 100
 * 0 <= redEdges.length, blueEdges.length <= 400
 * redEdges[i].length == blueEdges[j].length == 2
 * 0 <= ai, bi, uj, vj < n
 *
 */
public class ShortestPathWithAlternatingColors {

    private static final int RED = 0;
    private static final int BLUE = 1;

    // V0
    // IDEA: BFS ON A DOUBLED STATE GRAPH, state = (node, color of the edge just used)
    //  from state (i, c) only edges of color 1 - c may be walked next.
    //  Start from both (0, RED) and (0, BLUE) so the first edge may be either color.
    /**
     * time = O(N + E)
     * space = O(N + E)
     */
    public int[] shortestAlternatingPaths(int n, int[][] redEdges, int[][] blueEdges) {
        // graph.get(color).get(node) = neighbours reachable by an edge of that color
        List<List<List<Integer>>> graph = new ArrayList<>();
        for (int color = 0; color < 2; color++) {
            List<List<Integer>> adjacency = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                adjacency.add(new ArrayList<>());
            }
            graph.add(adjacency);
        }
        for (int[] edge : redEdges) {
            graph.get(RED).get(edge[0]).add(edge[1]);
        }
        for (int[] edge : blueEdges) {
            graph.get(BLUE).get(edge[0]).add(edge[1]);
        }

        int[] answer = new int[n];
        Arrays.fill(answer, -1);

        boolean[][] visited = new boolean[n][2];
        Deque<int[]> queue = new ArrayDeque<>();
        queue.offer(new int[] {0, RED});
        queue.offer(new int[] {0, BLUE});
        visited[0][RED] = true;
        visited[0][BLUE] = true;

        int distance = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int k = 0; k < levelSize; k++) {
                int[] state = queue.poll();
                int node = state[0];
                int lastColor = state[1];

                // BFS: the first time a node is popped is its shortest distance
                if (answer[node] == -1) {
                    answer[node] = distance;
                }

                // the next edge must have the other color
                int nextColor = 1 - lastColor;
                for (int next : graph.get(nextColor).get(node)) {
                    if (!visited[next][nextColor]) {
                        visited[next][nextColor] = true;
                        queue.offer(new int[] {next, nextColor});
                    }
                }
            }
            distance++;
        }
        return answer;
    }
}
