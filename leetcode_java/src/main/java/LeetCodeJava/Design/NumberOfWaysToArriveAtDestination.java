package LeetCodeJava.Design;

// https://leetcode.com/problems/number-of-ways-to-arrive-at-destination/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 *  1976. Number of Ways to Arrive at Destination
 *  Medium
 *
 *  You are in a city that consists of n intersections numbered from 0 to n - 1 with
 *  bi-directional roads between some intersections. The inputs are generated such that you
 *  can reach any intersection from any other intersection and that there is at most one road
 *  between any two intersections.
 *
 *  You are given an integer n and a 2D integer array roads where
 *  roads[i] = [ui, vi, timei] means that there is a road between intersections ui and vi
 *  that takes timei minutes to travel.
 *
 *  You want to know in how many ways you can travel from intersection 0 to intersection
 *  n - 1 in the shortest amount of time.
 *
 *  Return the number of ways you can arrive at your destination in the shortest amount of
 *  time. Since the answer may be large, return it modulo 10^9 + 7.
 *
 *  Example 1:
 *  Input: n = 7, roads = [[0,6,7],[0,1,2],[1,2,3],[1,3,3],[6,3,3],[3,5,1],[6,5,1],
 *                         [2,5,1],[0,4,5],[4,6,2]]
 *  Output: 4      // the shortest time is 7 minutes, reachable in 4 ways
 *
 *  Example 2:
 *  Input: n = 2, roads = [[1,0,10]]
 *  Output: 1      // only one way: 0 -> 1
 *
 *  Constraints:
 *
 *   1 <= n <= 200
 *   n - 1 <= roads.length <= n * (n - 1) / 2
 *   roads[i].length == 3
 *   0 <= ui, vi <= n - 1, ui != vi
 *   1 <= timei <= 10^9
 */
public class NumberOfWaysToArriveAtDestination {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DIJKSTRA + PATH COUNTING
    //       run Dijkstra from node 0 keeping ways[v] = number of shortest paths to v.
    //       when we relax an edge:  strictly better -> copy ways;  equal -> add ways.
    /**
     * time = O(E log V)
     * space = O(V + E)
     */
    public int countPaths(int n, int[][] roads) {
        List<List<long[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] r : roads) {
            graph.get(r[0]).add(new long[]{r[1], r[2]});
            graph.get(r[1]).add(new long[]{r[0], r[2]});
        }

        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        long[] ways = new long[n];
        dist[0] = 0L;
        ways[0] = 1L;

        // {node, dist}
        PriorityQueue<long[]> pq = new PriorityQueue<>(new Comparator<long[]>() {
            @Override
            public int compare(long[] a, long[] b) {
                return Long.compare(a[1], b[1]);
            }
        });
        pq.add(new long[]{0L, 0L});

        boolean[] done = new boolean[n];
        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            int u = (int) cur[0];
            if (done[u]) {
                continue;
            }
            done[u] = true;
            for (long[] nxt : graph.get(u)) {
                int v = (int) nxt[0];
                long nd = dist[u] + nxt[1];
                if (nd < dist[v]) {
                    dist[v] = nd;
                    ways[v] = ways[u];
                    pq.add(new long[]{v, nd});
                } else if (nd == dist[v]) {
                    ways[v] = (ways[v] + ways[u]) % MOD;
                }
            }
        }
        return (int) (ways[n - 1] % MOD);
    }
}
