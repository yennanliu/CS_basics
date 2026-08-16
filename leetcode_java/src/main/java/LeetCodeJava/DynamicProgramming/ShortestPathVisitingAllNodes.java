package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/shortest-path-visiting-all-nodes/description/

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 847. Shortest Path Visiting All Nodes
 * Hard
 *
 * You have an undirected, connected graph of n nodes labeled from 0 to n - 1. You are
 * given an array graph where graph[i] is a list of all the nodes connected with node i
 * by an edge.
 *
 * Return the length of the shortest path that visits every node. You may start and stop
 * at any node, you may revisit nodes multiple times, and you may reuse edges.
 *
 * Example 1:
 *
 * Input: graph = [[1,2,3],[0],[0],[0]]
 * Output: 4
 * Explanation: One possible path is [1,0,2,0,3]
 *
 * Example 2:
 *
 * Input: graph = [[1],[0,2,4],[1,3,4],[2],[1,2]]
 * Output: 4
 * Explanation: One possible path is [0,1,4,2,3]
 *
 * Constraints:
 *
 * n == graph.length
 * 1 <= n <= 12
 * 0 <= graph[i].length < n
 * graph[i] does not contain i.
 * If graph[a] contains b, then graph[b] contains a.
 * The input graph is always connected.
 *
 */
public class ShortestPathVisitingAllNodes {

    // V0
    // IDEA: BFS + BITMASK (multi-source, state = (node, visited set))
    /**
     *   A plain `visited node` set is NOT enough because we are allowed to REVISIT
     *   nodes. The real state is (current node, BITMASK of nodes seen so far).
     *   There are only n * 2^n <= 12 * 4096 such states, so BFS over them is cheap.
     *
     *   NOTE !!! EVERY node can be the start, so we seed the queue with all n states
     *            (i, 1 << i) at distance 0 -> the first time we pop a state whose mask
     *            is FULL, that distance IS the answer.
     *
     *   time  = O(n^2 * 2^n)
     *   space = O(n * 2^n)
     */
    public int shortestPathLength(int[][] graph) {
        int n = graph.length;
        int full = (1 << n) - 1;

        // multi-source: start from EVERY node at once
        Deque<int[]> queue = new ArrayDeque<>(); // {node, mask}
        boolean[][] visited = new boolean[n][1 << n];

        for (int i = 0; i < n; i++) {
            queue.offer(new int[] { i, 1 << i });
            visited[i][1 << i] = true;
        }

        int steps = 0;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int t = 0; t < levelSize; t++) {
                int[] cur = queue.poll();
                int node = cur[0];
                int mask = cur[1];

                if (mask == full) {
                    return steps;
                }

                for (int nxt : graph[node]) {
                    int nmask = mask | (1 << nxt);
                    if (!visited[nxt][nmask]) {
                        visited[nxt][nmask] = true;
                        queue.offer(new int[] { nxt, nmask });
                    }
                }
            }
            steps += 1;
        }

        return -1;
    }


    // V1
    // IDEA: FLOYD-WARSHALL + HELD-KARP (TSP path DP)
    /**
     *  Precompute all-pairs shortest paths, then run the classic Held-Karp DP over
     *  (visited set, last node) on that dense metric.
     *
     *  Separates `how far apart are two nodes?` from `in what order do I visit
     *  them?`, which is the standard TSP decomposition -- and it works unchanged on
     *  a WEIGHTED graph, where the BFS of V0 would not.
     *
     *  time  = O(n^3 + n^2 * 2^n)
     *  space = O(n * 2^n)
     */
    public int shortestPathLength_1(int[][] graph) {
        int n = graph.length;
        final int INF = Integer.MAX_VALUE / 4;

        int[][] d = new int[n][n];
        for (int[] row : d) {
            Arrays.fill(row, INF);
        }
        for (int i = 0; i < n; i++) {
            d[i][i] = 0;
            for (int j : graph[i]) {
                d[i][j] = 1;
            }
        }
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (d[i][k] + d[k][j] < d[i][j]) {
                        d[i][j] = d[i][k] + d[k][j];
                    }
                }
            }
        }

        int[][] dp = new int[1 << n][n];
        for (int[] row : dp) {
            Arrays.fill(row, INF);
        }
        for (int i = 0; i < n; i++) {
            dp[1 << i][i] = 0;
        }

        for (int mask = 1; mask < (1 << n); mask++) {
            for (int last = 0; last < n; last++) {
                if (dp[mask][last] >= INF || ((mask >> last) & 1) == 0) {
                    continue;
                }
                for (int nxt = 0; nxt < n; nxt++) {
                    if (((mask >> nxt) & 1) == 1) {
                        continue;
                    }
                    int nm = mask | (1 << nxt);
                    if (dp[mask][last] + d[last][nxt] < dp[nm][nxt]) {
                        dp[nm][nxt] = dp[mask][last] + d[last][nxt];
                    }
                }
            }
        }

        int best = INF;
        for (int last = 0; last < n; last++) {
            best = Math.min(best, dp[(1 << n) - 1][last]);
        }
        return best;
    }

    // V2
    // IDEA: DIJKSTRA over (node, mask) with unit weights
    /**
     *  Same state graph as V0, explored with a priority queue instead of a queue.
     *
     *  Identical results while every edge costs 1, but it is the version to keep if
     *  the edges ever gain weights -- the BFS layer argument would break, this one
     *  would not.
     *
     *  time  = O(n^2 * 2^n log)
     *  space = O(n * 2^n)
     */
    public int shortestPathLength_2(int[][] graph) {
        int n = graph.length;
        int full = (1 << n) - 1;
        int[][] dist = new int[n][1 << n];
        for (int[] row : dist) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(x -> x[0]));
        for (int i = 0; i < n; i++) {
            dist[i][1 << i] = 0;
            pq.add(new int[] { 0, i, 1 << i });
        }

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int cost = cur[0];
            int node = cur[1];
            int mask = cur[2];

            if (mask == full) {
                return cost;
            }
            if (cost > dist[node][mask]) {
                continue;
            }
            for (int nxt : graph[node]) {
                int nm = mask | (1 << nxt);
                if (cost + 1 < dist[nxt][nm]) {
                    dist[nxt][nm] = cost + 1;
                    pq.add(new int[] { cost + 1, nxt, nm });
                }
            }
        }
        return -1;
    }

    // V3
    // IDEA: ITERATIVE DEEPENING over the answer length
    /**
     *  Try path lengths 0, 1, 2, ... and DFS to each limit with the `remaining
     *  unvisited count - 1` admissible bound as a cut-off.
     *
     *  Memory drops to O(depth) instead of O(n * 2^n) -- the usual IDDFS trade,
     *  and worth having when 2^n states would not fit.
     *
     *  time  = O(n^2 * 2^n) with a larger constant
     *  space = O(n)
     */
    public int shortestPathLength_3(int[][] graph) {
        int n = graph.length;
        int full = (1 << n) - 1;
        if (n == 1) {
            return 0;
        }

        for (int limit = 0; ; limit++) {
            for (int start = 0; start < n; start++) {
                if (dfsLimit(graph, start, 1 << start, 0, limit, full)) {
                    return limit;
                }
            }
        }
    }

    private boolean dfsLimit(int[][] graph, int node, int mask, int depth,
                             int limit, int full) {
        if (mask == full) {
            return depth == limit;
        }
        // BOUND: at least (unvisited count) more moves are needed
        int missing = Integer.bitCount(full) - Integer.bitCount(mask);
        if (depth + missing > limit) {
            return false;
        }
        for (int nxt : graph[node]) {
            if (dfsLimit(graph, nxt, mask | (1 << nxt), depth + 1, limit, full)) {
                return true;
            }
        }
        return false;
    }

}
