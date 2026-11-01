package LeetCodeJava.Graph;

// https://leetcode.com/problems/minimum-cost-to-reach-city-with-discounts/description/

import java.util.*;

/**
 * 2093. Minimum Cost to Reach City With Discounts
 * Medium
 *
 * A series of highways connect n cities numbered from 0 to n - 1. You are given a 2D integer array highways
 * where highways[i] = [city1_i, city2_i, toll_i] indicates that there is a highway that connects city1_i and
 * city2_i, allowing a car to go from city1_i to city2_i and vice versa for a cost of toll_i.
 *
 * You are also given an integer discounts which represents the number of discounts you have. You can use a
 * discount to travel across the ith highway for a cost of toll_i / 2 (integer division). Each discount may
 * only be used once, and you can only use at most one discount per highway.
 *
 * Return the minimum total cost to go from city 0 to city n - 1, or -1 if it is not possible to go from
 * city 0 to city n - 1.
 *
 *
 * Example 1:
 *
 * Input: n = 5, highways = [[0,1,4],[2,1,3],[1,4,11],[3,2,3],[3,4,2]], discounts = 1
 * Output: 9
 * Explanation:
 * Go from 0 to 1 for a cost of 4.
 * Go from 1 to 4 and use a discount for a cost of 11 / 2 = 5.
 * The minimum cost to go from 0 to 4 is 4 + 5 = 9.
 *
 * Example 2:
 *
 * Input: n = 4, highways = [[1,3,17],[1,2,7],[3,2,5],[0,1,6],[3,0,20]], discounts = 20
 * Output: 8
 * Explanation:
 * Go from 0 to 1 and use a discount for a cost of 6 / 2 = 3.
 * Go from 1 to 2 and use a discount for a cost of 7 / 2 = 3.
 * Go from 2 to 3 and use a discount for a cost of 5 / 2 = 2.
 * The minimum cost to go from 0 to 3 is 3 + 3 + 2 = 8.
 *
 * Example 3:
 *
 * Input: n = 4, highways = [[0,1,3],[2,3,2]], discounts = 0
 * Output: -1
 * Explanation:
 * It is impossible to go from 0 to 3 so return -1.
 *
 *
 * Constraints:
 *
 * 2 <= n <= 1000
 * 1 <= highways.length <= 1000
 * highways[i].length == 3
 * 0 <= city1_i, city2_i <= n - 1
 * city1_i != city2_i
 * 0 <= toll_i <= 10^5
 * 0 <= discounts <= 500
 * There are no duplicate highways.
 *
 */
public class MinimumCostToReachCityWithDiscounts {

    // V0
    // IDEA: DIJKSTRA ON THE EXPANDED STATE (city, discounts already used)
    // a node of the search is (city, used). From (u, used) each highway (u, v, toll) offers:
    //   (v, used)      paying toll
    //   (v, used + 1)  paying toll / 2, only while used < discounts
    // The first time the heap pops any state at city n - 1, its cost is the answer.
    /**
     * time = O(E * D * log(N * D)), D = discounts
     * space = O(N * D)
     */
    public int minimumCost(int n, int[][] highways, int discounts) {
        // graph.get(u) = list of {v, toll}
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] highway : highways) {
            graph.get(highway[0]).add(new int[] {highway[1], highway[2]});
            graph.get(highway[1]).add(new int[] {highway[0], highway[2]});
        }

        // best[city][used] = cheapest known cost to stand at city having used `used` discounts
        int[][] best = new int[n][discounts + 1];
        for (int[] row : best) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }
        best[0][0] = 0;

        // heap entry = {cost, city, used}
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        heap.offer(new int[] {0, 0, 0});
        while (!heap.isEmpty()) {
            int[] top = heap.poll();
            int cost = top[0];
            int city = top[1];
            int used = top[2];
            if (city == n - 1) {
                return cost;
            }
            // stale entry
            if (cost > best[city][used]) {
                continue;
            }
            for (int[] next : graph.get(city)) {
                int nextCity = next[0];
                int toll = next[1];

                // pay the full toll
                int fullCost = cost + toll;
                if (fullCost < best[nextCity][used]) {
                    best[nextCity][used] = fullCost;
                    heap.offer(new int[] {fullCost, nextCity, used});
                }

                // spend one discount on this highway
                if (used < discounts) {
                    int discountedCost = cost + toll / 2;
                    if (discountedCost < best[nextCity][used + 1]) {
                        best[nextCity][used + 1] = discountedCost;
                        heap.offer(new int[] {discountedCost, nextCity, used + 1});
                    }
                }
            }
        }
        return -1;
    }
}
