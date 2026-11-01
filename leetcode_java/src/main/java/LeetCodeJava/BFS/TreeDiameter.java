package LeetCodeJava.BFS;

// https://leetcode.com/problems/tree-diameter/description/

import java.util.*;

/**
 * 1245. Tree Diameter
 * Medium
 *
 * The diameter of a tree is the number of edges in the longest path in that tree.
 *
 * There is an undirected tree of n nodes labeled from 0 to n - 1. You are given a 2D array
 * edges where edges.length == n - 1 and edges[i] = [ai, bi] indicates that there is an
 * undirected edge between nodes ai and bi in the tree.
 *
 * Return the diameter of the tree.
 *
 *
 * Example 1:
 *
 * Input: edges = [[0,1],[0,2]]
 * Output: 2
 * Explanation: The longest path of the tree is the path 1 - 0 - 2.
 *
 * Example 2:
 *
 * Input: edges = [[0,1],[1,2],[2,3],[1,4],[4,5]]
 * Output: 4
 * Explanation: The longest path of the tree is the path 3 - 2 - 1 - 4 - 5.
 *
 *
 * Constraints:
 *
 * n == edges.length + 1
 * 1 <= n <= 10^4
 * 0 <= ai, bi < n
 * ai != bi
 *
 */
public class TreeDiameter {

    // V0
    // IDEA: DOUBLE BFS (TWO-SWEEP)
    //  1) BFS from any node -> the farthest node is an endpoint of some diameter
    //  2) BFS from that endpoint -> the farthest distance IS the diameter
    //  BFS rather than DFS: the tree can be a 10^4-long path, too deep to recurse safely.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int treeDiameter(int[][] edges) {
        int n = edges.length + 1;
        List<List<Integer>> graph = buildGraph(n, edges);

        int[] first = farthestFrom(0, graph);
        int[] second = farthestFrom(first[0], graph);
        return second[1];
    }

    /** BFS from source; returns {farthest node, its distance from source}. */
    private int[] farthestFrom(int source, List<List<Integer>> graph) {
        int n = graph.size();
        int[] distance = new int[n];
        Arrays.fill(distance, -1);
        distance[source] = 0;

        Deque<Integer> queue = new ArrayDeque<>();
        queue.offer(source);
        int farthestNode = source;

        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (distance[current] > distance[farthestNode]) {
                farthestNode = current;
            }
            for (int next : graph.get(current)) {
                if (distance[next] == -1) {
                    distance[next] = distance[current] + 1;
                    queue.offer(next);
                }
            }
        }
        return new int[] {farthestNode, distance[farthestNode]};
    }

    // V1
    // IDEA: ONE-PASS DFS (LONGEST + SECOND LONGEST BRANCH AT EACH NODE)
    //  the best path through a node joins its two deepest child branches.
    //  NOTE: recursive -- fine for balanced trees, risky on a 10^4-long path.
    /**
     * time = O(N)
     * space = O(N)
     */
    public int treeDiameter_1(int[][] edges) {
        int n = edges.length + 1;
        List<List<Integer>> graph = buildGraph(n, edges);
        int[] best = new int[1];
        longestBranch(0, -1, graph, best);
        return best[0];
    }

    /** Returns the longest downward path (in edges) from node, updating best with the path through it. */
    private int longestBranch(int node, int parent, List<List<Integer>> graph, int[] best) {
        int longest = 0;
        int secondLongest = 0;
        for (int child : graph.get(node)) {
            if (child == parent) {
                continue;
            }
            int branch = longestBranch(child, node, graph, best) + 1;
            if (branch > longest) {
                secondLongest = longest;
                longest = branch;
            } else if (branch > secondLongest) {
                secondLongest = branch;
            }
        }
        best[0] = Math.max(best[0], longest + secondLongest);
        return longest;
    }

    /** Undirected adjacency list for n nodes. */
    private List<List<Integer>> buildGraph(int n, int[][] edges) {
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
}
