package LeetCodeJava.BFS;

// https://leetcode.com/problems/cheapest-flights-within-k-stops/

import java.util.*;

/**
 * 787. Cheapest Flights Within K Stops
 * Medium
 * Topics
 * Companies
 * There are n cities connected by some number of flights. You are given an array flights where flights[i] = [fromi, toi, pricei] indicates that there is a flight from city fromi to city toi with cost pricei.
 *
 * You are also given three integers src, dst, and k, return the cheapest price from src to dst with at most k stops. If there is no such route, return -1.
 *
 *
 *
 * Example 1:
 *
 *
 * Input: n = 4, flights = [[0,1,100],[1,2,100],[2,0,100],[1,3,600],[2,3,200]], src = 0, dst = 3, k = 1
 * Output: 700
 * Explanation:
 * The graph is shown above.
 * The optimal path with at most 1 stop from city 0 to 3 is marked in red and has cost 100 + 600 = 700.
 * Note that the path through cities [0,1,2,3] is cheaper but is invalid because it uses 2 stops.
 * Example 2:
 *
 *
 * Input: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 1
 * Output: 200
 * Explanation:
 * The graph is shown above.
 * The optimal path with at most 1 stop from city 0 to 2 is marked in red and has cost 100 + 100 = 200.
 * Example 3:
 *
 *
 * Input: n = 3, flights = [[0,1,100],[1,2,100],[0,2,500]], src = 0, dst = 2, k = 0
 * Output: 500
 * Explanation:
 * The graph is shown above.
 * The optimal path with no stops from city 0 to 2 is marked in red and has cost 500.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 100
 * 0 <= flights.length <= (n * (n - 1) / 2)
 * flights[i].length == 3
 * 0 <= fromi, toi < n
 * fromi != toi
 * 1 <= pricei <= 104
 * There will not be any multiple flights between two cities.
 * 0 <= src, dst, k < n
 * src != dst
 *
 *
 */
public class CheapestFlightsWithinKStops {

    // V0
    // TODO: implement again
//    public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
//
//    }

    // V0-1
    // IDEA: Dijkstra (fixed by gpt)
    public int findCheapestPrice_0_1(int n, int[][] flights, int src, int dst, int k) {
        // Edge case: no flights or invalid input
        if (n == 0 || flights == null || flights.length == 0) {
            return -1;
        }

        // If source equals destination, no travel is needed
        if (src == dst) {
            return 0;
        }

        // Initialize Dijkstra algorithm
        Dijkstra_3 dijkstra = new Dijkstra_3(flights, n);
        return dijkstra.getShortestPath(src, dst, k);
    }

    public class Dijkstra_3 {
        int[][] times; // Array of flights (edges)
        int n; // Number of nodes (cities)

        // Constructor to initialize the flight times and number of nodes
        public Dijkstra_3(int[][] times, int n) {
            this.times = times;
            this.n = n;
        }

        // Method to find the shortest path using Dijkstra's algorithm, considering at
        // most k stops
        public int getShortestPath(int src, int dst, int k) {
            // Step 1: Build the graph
            Map<Integer, List<int[]>> edges = new HashMap<>();
            for (int[] time : times) {
                edges.computeIfAbsent(time[0], key -> new ArrayList<>()).add(new int[] { time[1], time[2] });
            }

            // Step 2: Initialize the priority queue (min-heap) with the starting node
            PriorityQueue<int[]> minHeap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
            minHeap.offer(new int[] { 0, src, 0 }); // {cost, node, stops}

            // Step 3: Track the minimum cost for each node with up to k stops
            /**
             * k + 2 (Number of stops):
             *
             * -  The second dimension, k + 2, is used to represent the number of stops allowed
             *    to reach a particular node.
             *
             *   -> Why k + 2?
             *
             *        - The +1 is for the actual stops, i.e., up to k stops.
             *
             *        - The +1 is to account for the 0-th stop — meaning the "starting" position
             *          (where we haven't yet made any stops).
             *
             *        -  The extra +1 is added because if you can make k stops,
             *           you can still consider arriving at a node with k + 1 stops,
             *           where k + 1 is the maximum number of stops allowed in this problem.
             *
             */
            int[][] dist = new int[n][k + 2]; // dist[node][stops] -> minimum cost to reach node with 'stops' stops
            for (int[] row : dist) {
                Arrays.fill(row, Integer.MAX_VALUE);
            }
            dist[src][0] = 0; // No cost to reach the source node with 0 stops

            // Step 4: Process the priority queue
            while (!minHeap.isEmpty()) {
                int[] curr = minHeap.poll();
                int cost = curr[0], node = curr[1], stops = curr[2]; // cost = current travel time, node = current node,
                // stops = stops made

                // If we reach the destination node, return the cost
                if (node == dst) {
                    return cost;
                }

                // If we have already made k stops, we shouldn't process further
                if (stops > k)
                    continue;

                // Step 5: Process all neighbors of the current node
                if (edges.containsKey(node)) {
                    for (int[] next : edges.get(node)) {
                        int nextNode = next[0], nextCost = next[1];
                        int newCost = cost + nextCost;

                        // Only add the new path if it's better (lower cost) and we haven't visited with
                        // the same number of stops
                        if (newCost < dist[nextNode][stops + 1]) {
                            dist[nextNode][stops + 1] = newCost;
                            minHeap.offer(new int[] { newCost, nextNode, stops + 1 });
                        }
                    }
                }
            }

            // If no path is found with at most k stops
            return -1;
        }
    }

    // V1-1
    // https://neetcode.io/problems/cheapest-flight-path
    // IDEA: Dijkstra
    public int findCheapestPrice_1_1(int n, int[][] flights, int src, int dst, int k) {
        int INF = Integer.MAX_VALUE;
        List<int[]>[] adj = new ArrayList[n];
        int[][] dist = new int[n][k + 5];
        for (int i = 0; i < n; i++) Arrays.fill(dist[i], INF);

        for (int i = 0; i < n; i++) adj[i] = new ArrayList<>();
        for (int[] flight : flights) {
            adj[flight[0]].add(new int[]{flight[1], flight[2]});
        }

        dist[src][0] = 0;
        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
                Comparator.comparingInt(a -> a[0])
        );
        minHeap.offer(new int[]{0, src, -1});

        while (!minHeap.isEmpty()) {
            int[] top = minHeap.poll();
            int cst = top[0], node = top[1], stops = top[2];
            if (node == dst) return cst;
            if (stops == k || dist[node][stops + 1] < cst) continue;
            for (int[] neighbor : adj[node]) {
                int nei = neighbor[0], w = neighbor[1];
                int nextCst = cst + w;
                int nextStops = stops + 1;
                if (dist[nei][nextStops + 1] > nextCst) {
                    dist[nei][nextStops + 1] = nextCst;
                    minHeap.offer(new int[]{nextCst, nei, nextStops});
                }
            }
        }
        return -1;
    }


    // V1-2
    // https://neetcode.io/problems/cheapest-flight-path
    // IDEA: Bellman Ford Algorithm
    public int findCheapestPrice_1_2(int n, int[][] flights, int src, int dst, int k) {
        int[] prices = new int[n];
        Arrays.fill(prices, Integer.MAX_VALUE);
        prices[src] = 0;

        for (int i = 0; i <= k; i++) {
            int[] tmpPrices = Arrays.copyOf(prices, n);

            for (int[] flight : flights) {
                int s = flight[0];
                int d = flight[1];
                int p = flight[2];

                if (prices[s] == Integer.MAX_VALUE) {
                    continue;
                }

                if (prices[s] + p < tmpPrices[d]) {
                    tmpPrices[d] = prices[s] + p;
                }
            }

            prices = tmpPrices;
        }

        return prices[dst] == Integer.MAX_VALUE ? -1 : prices[dst];
    }


    // V1-3
    // https://neetcode.io/problems/cheapest-flight-path
    // IDEA: Shortest Path Faster Algorithm
    public int findCheapestPrice_1_3(int n, int[][] flights, int src, int dst, int k) {
        int[] prices = new int[n];
        Arrays.fill(prices, Integer.MAX_VALUE);
        prices[src] = 0;
        List<int[]>[] adj = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int[] flight : flights) {
            adj[flight[0]].add(new int[] { flight[1], flight[2] });
        }

        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[] { 0, src, 0 });

        while (!q.isEmpty()) {
            int[] curr = q.poll();
            int cst = curr[0], node = curr[1], stops = curr[2];
            if (stops > k) continue;

            for (int[] neighbor : adj[node]) {
                int nei = neighbor[0], w = neighbor[1];
                int nextCost = cst + w;
                if (nextCost < prices[nei]) {
                    prices[nei] = nextCost;
                    q.offer(new int[] { nextCost, nei, stops + 1 });
                }
            }
        }
        return prices[dst] == Integer.MAX_VALUE ? -1 : prices[dst];
    }


    // V2

}
