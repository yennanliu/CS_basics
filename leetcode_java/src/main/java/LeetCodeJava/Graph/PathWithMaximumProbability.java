package LeetCodeJava.Graph;

// https://leetcode.com/problems/path-with-maximum-probability/

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 *  1514. Path with Maximum Probability
 *  Medium
 *
 *  You are given an undirected weighted graph of n nodes (0-indexed), represented by
 *  an edge list where edges[i] = [a, b] is an undirected edge connecting nodes a and b
 *  with a probability of success of traversing that edge succProb[i].
 *
 *  Given two nodes start_node and end_node, find the path with the maximum probability
 *  of success to go from start_node to end_node and return its success probability.
 *
 *  If there is no path from start_node to end_node, return 0.
 *  Your answer will be accepted if it differs from the correct answer by at most 1e-5.
 *
 *  Example 1:
 *    Input: n = 3, edges = [[0,1],[1,2],[0,2]], succProb = [0.5,0.5,0.2], start = 0, end = 2
 *    Output: 0.25000
 *
 *  Example 2:
 *    Input: n = 3, edges = [[0,1],[1,2],[0,2]], succProb = [0.5,0.5,0.3], start = 0, end = 2
 *    Output: 0.30000
 *
 *  Constraints:
 *    2 <= n <= 10^4
 *    0 <= start_node, end_node < n
 *    0 <= edges.length <= 2*10^4
 *    0 <= succProb[i] <= 1
 */
public class PathWithMaximumProbability {

    // V0
    // IDEA: Dijkstra with a max-heap - relax by multiplying probabilities instead of adding weights.
    /**
     * time = O((V + E) log V)
     * space = O(V + E)
     */
    public double maxProbability(int n, int[][] edges, double[] succProb, int start_node, int end_node) {
        List<List<double[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int i = 0; i < edges.length; i++) {
            int u = edges[i][0];
            int v = edges[i][1];
            graph.get(u).add(new double[]{v, succProb[i]});
            graph.get(v).add(new double[]{u, succProb[i]});
        }

        double[] best = new double[n];
        best[start_node] = 1.0;

        // max-heap on probability: {node, prob}
        PriorityQueue<double[]> pq = new PriorityQueue<>(new Comparator<double[]>() {
            @Override
            public int compare(double[] a, double[] b) {
                return Double.compare(b[1], a[1]);
            }
        });
        pq.offer(new double[]{start_node, 1.0});

        while (!pq.isEmpty()) {
            double[] cur = pq.poll();
            int node = (int) cur[0];
            double p = cur[1];
            if (node == end_node) {
                return p;
            }
            if (p < best[node]) {
                continue; // stale entry
            }
            for (double[] nxt : graph.get(node)) {
                int to = (int) nxt[0];
                double np = p * nxt[1];
                if (np > best[to]) {
                    best[to] = np;
                    pq.offer(new double[]{to, np});
                }
            }
        }
        return 0.0;
    }

    // V1
    // IDEA: Bellman-Ford style relaxation - repeat until no probability improves.
    /**
     * time = O(V * E)
     * space = O(V)
     */
    public double maxProbability_1(int n, int[][] edges, double[] succProb, int start_node, int end_node) {
        double[] best = new double[n];
        best[start_node] = 1.0;

        for (int iter = 0; iter < n - 1; iter++) {
            boolean updated = false;
            for (int i = 0; i < edges.length; i++) {
                int u = edges[i][0];
                int v = edges[i][1];
                double w = succProb[i];
                if (best[u] * w > best[v]) {
                    best[v] = best[u] * w;
                    updated = true;
                }
                if (best[v] * w > best[u]) {
                    best[u] = best[v] * w;
                    updated = true;
                }
            }
            if (!updated) {
                break;
            }
        }
        return best[end_node];
    }
}
