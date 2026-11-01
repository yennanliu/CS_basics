package LeetCodeJava.Graph;

// https://leetcode.com/problems/distance-to-a-cycle-in-undirected-graph/description/

import java.util.*;

/**
 * 2204. Distance to a Cycle in Undirected Graph
 * Hard
 *
 *
 * You are given a positive integer n representing the number of nodes in a connected undirected graph containing exactly one cycle. The nodes are numbered from 0 to n - 1 (inclusive).
 *
 * You are also given a 2D integer array edges, where edges[i] = [node1_i, node2_i] denotes that there is a bidirectional edge connecting node1_i and node2_i in the graph.
 *
 * The distance between two nodes a and b is defined to be the minimum number of edges that are needed to go from a to b.
 *
 * Return an integer array answer of size n, where answer[i] is the minimum distance between the ith node and any node in the cycle.
 *
 *
 * Example 1:
 *
 * Input: n = 7, edges = [[1,2],[2,4],[4,3],[3,1],[0,1],[5,2],[6,5]]
 * Output: [1,0,0,0,0,1,2]
 * Explanation:
 * The nodes 1, 2, 3, and 4 form the cycle.
 * The distance from 0 to 1 is 1.
 * The distance from 1 to 1 is 0.
 * The distance from 2 to 2 is 0.
 * The distance from 3 to 3 is 0.
 * The distance from 4 to 4 is 0.
 * The distance from 5 to 2 is 1.
 * The distance from 6 to 2 is 2.
 *
 * Example 2:
 *
 * Input: n = 9, edges = [[0,1],[1,2],[0,2],[2,6],[6,7],[6,8],[0,3],[3,4],[3,5]]
 * Output: [0,0,0,1,2,2,1,2,2]
 * Explanation:
 * The nodes 0, 1, and 2 form the cycle.
 * The distance from 0 to 0 is 0.
 * The distance from 1 to 1 is 0.
 * The distance from 2 to 2 is 0.
 * The distance from 3 to 2 is 1.
 * The distance from 4 to 2 is 2.
 * The distance from 5 to 2 is 2.
 * The distance from 6 to 2 is 1.
 * The distance from 7 to 2 is 2.
 * The distance from 8 to 2 is 2.
 *
 *
 * Constraints:
 *
 * 3 <= n <= 10^5
 * edges.length == n
 * edges[i].length == 2
 * 0 <= node1_i, node2_i <= n - 1
 * node1_i != node2_i
 * The graph is connected.
 * The graph has exactly one cycle.
 * There is at most one edge between any pair of nodes.
 *
 */
public class DistanceToACycleInUndirectedGraph {

    // V0
    // IDEA: PEEL THE TREES OFF (KAHN ON DEGREE 1), THEN MULTI-SOURCE BFS FROM THE CYCLE
    //  - n nodes + n edges + connected = one cycle with trees hanging off it
    //  - repeatedly removing degree-1 nodes leaves exactly the cycle
    //  - BFS seeded with every cycle node at distance 0 gives each node its distance
    /**
     * time = O(V + E)
     * space = O(V + E)
     */
    public int[] distanceToCycle(int n, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        int[] degree = new int[n];
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
            degree[edge[0]]++;
            degree[edge[1]]++;
        }

        // step 1: strip the trees -- every leaf is removed, which may create new leaves
        boolean[] removed = new boolean[n];
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] == 1) {
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            int node = queue.poll();
            removed[node] = true;
            for (int next : graph.get(node)) {
                if (!removed[next]) {
                    degree[next]--;
                    if (degree[next] == 1) {
                        queue.offer(next);
                    }
                }
            }
        }

        // step 2: multi-source BFS from every surviving (cycle) node
        int[] answer = new int[n];
        Arrays.fill(answer, -1);
        for (int i = 0; i < n; i++) {
            if (!removed[i]) {
                answer[i] = 0;
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            int node = queue.poll();
            for (int next : graph.get(node)) {
                if (answer[next] == -1) {
                    answer[next] = answer[node] + 1;
                    queue.offer(next);
                }
            }
        }
        return answer;
    }
}
