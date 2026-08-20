package LeetCodeJava.DFS;

// https://leetcode.com/problems/frog-position-after-t-seconds/

import java.util.ArrayList;
import java.util.List;

/**
 *  1377. Frog Position After T Seconds
 *  Hard
 *
 *  Given an undirected tree consisting of n vertices numbered from 1 to n. A frog
 *  starts jumping from vertex 1. In one second, the frog jumps from its current vertex
 *  to another unvisited vertex if they are directly connected. The frog can not jump
 *  back to a visited vertex. In case the frog can jump to several vertices, it jumps
 *  randomly to one of them with the same probability. Otherwise, when the frog can not
 *  jump to any unvisited vertex, it jumps forever on the same vertex.
 *
 *  The edges of the undirected tree are given in the array edges, where
 *  edges[i] = [ai, bi] means that exists an edge connecting the vertices ai and bi.
 *
 *  Return the probability that after t seconds the frog is on the vertex target.
 *  Answers within 10^-5 of the actual answer will be accepted.
 *
 *  Example 1:
 *    Input: n = 7, edges = [[1,2],[1,3],[1,7],[2,4],[2,6],[3,5]], t = 2, target = 4
 *    Output: 0.16666666666666666
 *    Explanation: 1/3 to jump to vertex 2, then 1/2 to jump to vertex 4 -> 1/6.
 *
 *  Example 2:
 *    Input: n = 7, edges = [[1,2],[1,3],[1,7],[2,4],[2,6],[3,5]], t = 1, target = 7
 *    Output: 0.3333333333333333
 *
 *  Constraints:
 *    1 <= n <= 100
 *    edges.length == n - 1
 *    edges[i].length == 2
 *    1 <= ai, bi <= n
 *    1 <= t <= 50
 *    1 <= target <= n
 */
public class FrogPositionAfterTSeconds {

    // V0
    // IDEA: DFS on the tree, carrying the accumulated probability downward
    //       - the tree is rooted at vertex 1, so "unvisited neighbours" == "children"
    //       - at a node with c children the probability splits evenly -> p / c
    //       - we are on `target` at second t only if
    //           (a) we arrive exactly when the clock runs out (timeLeft == 0), or
    //           (b) target has no children -> the frog is stuck there forever
    //       NOTE: mark visited BEFORE collecting the children, so the parent is never
    //             mistaken for a child.
    /**
     * time = O(n)
     * space = O(n)
     */
    private List<List<Integer>> g;
    private boolean[] visited;
    private int target;

    public double frogPosition(int n, int[][] edges, int t, int target) {

        this.g = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            g.add(new ArrayList<Integer>());
        }
        for (int[] e : edges) {
            g.get(e[0]).add(e[1]);
            g.get(e[1]).add(e[0]);
        }

        this.visited = new boolean[n + 1];
        this.target = target;

        return dfs(1, t, 1.0);
    }

    private double dfs(int u, int timeLeft, double p) {

        visited[u] = true;

        List<Integer> children = new ArrayList<>();
        for (Integer v : g.get(u)) {
            if (!visited[v]) {
                children.add(v);
            }
        }

        if (u == target) {
            // arrived exactly on time, or stuck here forever (leaf)
            return (timeLeft == 0 || children.isEmpty()) ? p : 0.0;
        }

        // out of time, or a dead end -> target is unreachable from here
        if (timeLeft == 0 || children.isEmpty()) {
            return 0.0;
        }

        double res = 0.0;
        for (Integer v : children) {
            res += dfs(v, timeLeft - 1, p / children.size());
        }
        return res;
    }
}
