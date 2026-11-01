package LeetCodeJava.BFS;

// https://leetcode.com/problems/minimum-cost-to-reach-destination-in-time/description/

import java.util.*;

/**
 * 1928. Minimum Cost to Reach Destination in Time
 * Hard
 *
 * There is a country of n cities numbered from 0 to n - 1 where all the cities are connected by
 * bi-directional roads. The roads are represented as a 2D integer array edges where
 * edges[i] = [xi, yi, timei] denotes a road between cities xi and yi that takes timei minutes to
 * travel. There may be multiple roads of differing travel times connecting the same two cities,
 * but no road connects a city to itself.
 *
 * Each time you pass through a city, you must pay a passing fee. This is represented as a
 * 0-indexed integer array passingFees of length n where passingFees[j] is the amount of dollars
 * you must pay when you pass through city j.
 *
 * In the beginning, you are at city 0 and want to reach city n - 1 in maxTime minutes or less.
 * The cost of your journey is the summation of passing fees for each city that you passed through
 * at some moment of your journey (including the source and destination cities).
 *
 * Given maxTime, edges, and passingFees, return the minimum cost to complete your journey, or -1
 * if you cannot complete it within maxTime minutes.
 *
 *
 * Example 1:
 *
 * Input: maxTime = 30, edges = [[0,1,10],[1,2,10],[2,5,10],[0,3,1],[3,4,10],[4,5,15]], passingFees = [5,1,2,20,20,3]
 * Output: 11
 * Explanation: The path to take is 0 -> 1 -> 2 -> 5, which takes 30 minutes and has $11 worth of passing fees.
 *
 * Example 2:
 *
 * Input: maxTime = 29, edges = [[0,1,10],[1,2,10],[2,5,10],[0,3,1],[3,4,10],[4,5,15]], passingFees = [5,1,2,20,20,3]
 * Output: 48
 * Explanation: The path to take is 0 -> 3 -> 4 -> 5, which takes 26 minutes and has $48 worth of passing fees.
 * You cannot take path 0 -> 1 -> 2 -> 5 since it would take too long.
 *
 * Example 3:
 *
 * Input: maxTime = 25, edges = [[0,1,10],[1,2,10],[2,5,10],[0,3,1],[3,4,10],[4,5,15]], passingFees = [5,1,2,20,20,3]
 * Output: -1
 * Explanation: There is no way to reach city 5 from city 0 within 25 minutes.
 *
 *
 * Constraints:
 *
 * 1 <= maxTime <= 1000
 * n == passingFees.length
 * 2 <= n <= 1000
 * n - 1 <= edges.length <= 1000
 * 0 <= xi, yi <= n - 1
 * 1 <= timei <= 1000
 * 1 <= passingFees[j] <= 1000
 * The graph may contain multiple edges between two nodes.
 * The graph does not contain self loops.
 *
 */
public class MinimumCostToReachDestinationInTime {

    private static final int INF = Integer.MAX_VALUE;

    // V0
    // IDEA: DP OVER (TIME, CITY) -- A LAYERED-GRAPH SHORTEST PATH RELAXED BY TIME
    //  cost[t][v] = min fees of a walk 0 -> v that takes EXACTLY t minutes
    //  base: cost[0][0] = passingFees[0]
    //  for t = 1..maxTime, for each edge (x, y, w) with w <= t:
    //      cost[t][x] = min(cost[t][x], cost[t - w][y] + fee[x])   (and symmetrically for y)
    //  t is processed in increasing order, so every cost[t - w][*] is already final.
    //  answer = min over t <= maxTime of cost[t][n - 1]
    /**
     * time = O(maxTime * E)
     * space = O(maxTime * N)
     */
    public int minCost(int maxTime, int[][] edges, int[] passingFees) {
        int n = passingFees.length;
        int[][] cost = new int[maxTime + 1][n];
        for (int t = 0; t <= maxTime; t++) {
            Arrays.fill(cost[t], INF);
        }
        cost[0][0] = passingFees[0];

        for (int t = 1; t <= maxTime; t++) {
            for (int[] edge : edges) {
                int x = edge[0];
                int y = edge[1];
                int travel = edge[2];
                if (travel > t) {
                    continue;
                }
                // arrive at x from y
                if (cost[t - travel][y] != INF) {
                    cost[t][x] = Math.min(cost[t][x], cost[t - travel][y] + passingFees[x]);
                }
                // arrive at y from x
                if (cost[t - travel][x] != INF) {
                    cost[t][y] = Math.min(cost[t][y], cost[t - travel][x] + passingFees[y]);
                }
            }
        }

        int best = INF;
        for (int t = 0; t <= maxTime; t++) {
            best = Math.min(best, cost[t][n - 1]);
        }
        return best == INF ? -1 : best;
    }

    // V1
    // IDEA: DIJKSTRA ON COST, PRUNED BY THE FASTEST ARRIVAL TIME SEEN PER CITY
    //  pop the cheapest (cost, time, city). A city is worth re-entering only if we
    //  arrive FASTER than before -- a cheaper arrival was already popped earlier, so
    //  a slower AND costlier one can never help. The first pop of n - 1 is the answer.
    /**
     * time = O(maxTime * E * log(maxTime * E))
     * space = O(N + E)
     */
    public int minCost_1(int maxTime, int[][] edges, int[] passingFees) {
        int n = passingFees.length;
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(new int[] {edge[1], edge[2]});
            graph.get(edge[1]).add(new int[] {edge[0], edge[2]});
        }

        int[] fastestArrival = new int[n];
        Arrays.fill(fastestArrival, INF);
        fastestArrival[0] = 0;

        // entries are {cost, time, city}, cheapest first
        PriorityQueue<int[]> heap = new PriorityQueue<>((first, second) -> Integer.compare(first[0], second[0]));
        heap.offer(new int[] {passingFees[0], 0, 0});

        while (!heap.isEmpty()) {
            int[] entry = heap.poll();
            int cost = entry[0];
            int time = entry[1];
            int city = entry[2];
            if (city == n - 1) {
                return cost;
            }
            for (int[] road : graph.get(city)) {
                int next = road[0];
                int arrival = time + road[1];
                if (arrival <= maxTime && arrival < fastestArrival[next]) {
                    fastestArrival[next] = arrival;
                    heap.offer(new int[] {cost + passingFees[next], arrival, next});
                }
            }
        }
        return -1;
    }
}
