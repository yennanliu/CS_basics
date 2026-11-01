package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-cost-to-buy-apples/description/

import java.util.*;

/**
 * 2473. Minimum Cost to Buy Apples
 * Medium
 *
 * You are given a positive integer n representing n cities numbered from 1 to n. You are also given a 2D array roads, where roads[i] = [ai, bi, costi] indicates that there is a bidirectional road between cities ai and bi with a cost of traveling equal to costi.
 *
 * You can buy apples in any city you want, but some cities have different costs to buy apples. You are given the 1-based array appleCost where appleCost[i] is the cost of buying one apple from city i.
 *
 * You start at some city, traverse through various roads, and eventually buy exactly one apple from any city. After you buy that apple, you have to return back to the city you started at, but now the cost of all the roads will be multiplied by a given factor k.
 *
 * Given the integer k, return a 1-based array answer of size n where answer[i] is the minimum total cost to buy an apple if you start at city i.
 *
 *
 * Example 1:
 *
 * Input: n = 4, roads = [[1,2,4],[2,3,2],[2,4,5],[3,4,1],[1,3,4]], appleCost = [56,42,102,301], k = 2
 * Output: [54,42,48,51]
 * Explanation: The minimum cost for each starting city is the following:
 * - Starting at city 1: You take the path 1 -> 2, buy an apple at city 2, and finally take the path 2 -> 1. The total cost is 4 + 42 + 4 * 2 = 54.
 * - Starting at city 2: You directly buy an apple at city 2. The total cost is 42.
 * - Starting at city 3: You take the path 3 -> 2, buy an apple at city 2, and finally take the path 2 -> 3. The total cost is 2 + 42 + 2 * 2 = 48.
 * - Starting at city 4: You take the path 4 -> 3 -> 2 then you buy at city 2, and finally take the path 2 -> 3 -> 4. The total cost is 1 + 2 + 42 + 1 * 2 + 2 * 2 = 51.
 *
 * Example 2:
 *
 * Input: n = 3, roads = [[1,2,5],[2,3,1],[3,1,2]], appleCost = [2,3,1], k = 3
 * Output: [2,3,1]
 * Explanation: It is always optimal to buy the apple in the starting city.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 1000
 * 1 <= roads.length <= 2000
 * 1 <= ai, bi <= n
 * ai != bi
 * 1 <= costi <= 10^5
 * appleCost.length == n
 * 1 <= appleCost[i] <= 10^5
 * 1 <= k <= 100
 * There are no repeated edges.
 *
 */
public class MinimumCostToBuyApples {

    // V0
    // IDEA: DIJKSTRA FROM EVERY START CITY (a round trip costs dist * (k + 1))
    //  - buying in city u when starting from city i costs appleCost[u] + dist(i, u) * (k + 1)
    //    (the way out costs dist, the way home costs dist * k)
    //  - n <= 1000 and m <= 2000, so one Dijkstra per city is affordable
    /**
     * time = O(n * m * log m)
     * space = O(n + m)
     */
    public long[] minCost(int n, int[][] roads, int[] appleCost, int k) {
        // cities are 1-indexed in the input; store them 0-indexed
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] road : roads) {
            graph.get(road[0] - 1).add(new int[] {road[1] - 1, road[2]});
            graph.get(road[1] - 1).add(new int[] {road[0] - 1, road[2]});
        }

        long[] answer = new long[n];
        for (int startCity = 0; startCity < n; startCity++) {
            long[] dist = new long[n];
            Arrays.fill(dist, Long.MAX_VALUE);
            dist[startCity] = 0;
            long best = Long.MAX_VALUE;

            // heap entry: {distance, city}
            PriorityQueue<long[]> heap = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
            heap.offer(new long[] {0, startCity});
            while (!heap.isEmpty()) {
                long[] top = heap.poll();
                long d = top[0];
                int city = (int) top[1];
                if (d > dist[city]) {
                    continue;
                }
                // buy here: travel there and back (k times more expensive on the way back)
                long totalCost = appleCost[city] + d * (k + 1);
                best = Math.min(best, totalCost);
                for (int[] next : graph.get(city)) {
                    long candidate = d + next[1];
                    if (candidate < dist[next[0]]) {
                        dist[next[0]] = candidate;
                        heap.offer(new long[] {candidate, next[0]});
                    }
                }
            }
            answer[startCity] = best;
        }
        return answer;
    }
}
