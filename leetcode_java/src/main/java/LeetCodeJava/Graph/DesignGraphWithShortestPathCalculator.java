package LeetCodeJava.Graph;

// https://leetcode.com/problems/design-graph-with-shortest-path-calculator/description/

import java.util.*;

/**
 * 2642. Design Graph With Shortest Path Calculator
 * Solved
 * Hard
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * There is a directed weighted graph that consists of n nodes numbered from 0 to n - 1. The edges of the graph are initially represented by the given array edges where edges[i] = [fromi, toi, edgeCosti] meaning that there is an edge from fromi to toi with the cost edgeCosti.
 *
 * Implement the Graph class:
 *
 * Graph(int n, int[][] edges) initializes the object with n nodes and the given edges.
 * addEdge(int[] edge) adds an edge to the list of edges where edge = [from, to, edgeCost]. It is guaranteed that there is no edge between the two nodes before adding this one.
 * int shortestPath(int node1, int node2) returns the minimum cost of a path from node1 to node2. If no path exists, return -1. The cost of a path is the sum of the costs of the edges in the path.
 *
 *
 * Example 1:
 *
 *
 * Input
 * ["Graph", "shortestPath", "shortestPath", "addEdge", "shortestPath"]
 * [[4, [[0, 2, 5], [0, 1, 2], [1, 2, 1], [3, 0, 3]]], [3, 2], [0, 3], [[1, 3, 4]], [0, 3]]
 * Output
 * [null, 6, -1, null, 6]
 *
 * Explanation
 * Graph g = new Graph(4, [[0, 2, 5], [0, 1, 2], [1, 2, 1], [3, 0, 3]]);
 * g.shortestPath(3, 2); // return 6. The shortest path from 3 to 2 in the first diagram above is 3 -> 0 -> 1 -> 2 with a total cost of 3 + 2 + 1 = 6.
 * g.shortestPath(0, 3); // return -1. There is no path from 0 to 3.
 * g.addEdge([1, 3, 4]); // We add an edge from node 1 to node 3, and we get the second diagram above.
 * g.shortestPath(0, 3); // return 6. The shortest path from 0 to 3 now is 0 -> 1 -> 3 with a total cost of 2 + 4 = 6.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 100
 * 0 <= edges.length <= n * (n - 1)
 * edges[i].length == edge.length == 3
 * 0 <= fromi, toi, from, to, node1, node2 <= n - 1
 * 1 <= edgeCosti, edgeCost <= 106
 * There are no repeated edges and no self-loops in the graph at any point.
 * At most 100 calls will be made for addEdge.
 * At most 100 calls will be made for shortestPath.
 *
 *
 *
 */
public class DesignGraphWithShortestPathCalculator {

    // V0
//    class Graph {
//
//        public Graph(int n, int[][] edges) {
//
//        }
//
//        public void addEdge(int[] edge) {
//
//        }
//
//        public int shortestPath(int node1, int node2) {
//
//        }
//    }

    // V0-1
    // IDEA: Dijkstra (fixed by gpt)
    class Graph_0_1 {
        /** NOTE !!! map structure
         *
         *  // map: { node : [ [neighbor_1, cost_1], [neighbor_2, cost_2], .... ] }
         */
        // Adjacency list: { node -> [ [neighbor, cost], [neighbor, cost], ... ] }
        private Map<Integer, List<int[]>> map;

        public Graph_0_1(int n, int[][] edges) {
            map = new HashMap<>();
            for (int i = 0; i < n; i++) {
                map.put(i, new ArrayList<>());
            }

            /** NOTE !!! we need to init edges */
            // add initial edges
            for (int[] e : edges) {
                addEdge(e);
            }
        }

        public void addEdge(int[] edge) {
            int from = edge[0];
            int to = edge[1];
            int cost = edge[2];
            map.get(from).add(new int[] { to, cost });
        }

        public int shortestPath(int node1, int node2) {

            /** NOTE !!!
             *
             *  we set up PQ in `shortestPath` method (when the method is called);
             *
             *  -> instead of setting PQ up in the graph constructor
             */
            // Dijkstra setup
            /**
             *  PQ:
             *  1. // small PQ:  { [node, cost], .... } ???
             *  2. NOTE !!! this is a small PQ, sort on cost
             */
            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
            pq.offer(new int[] { node1, 0 });

            /** NOTE !!!
             *
             *  we define the `minDist[node]` (a hashMap)
             *  -> to record the shortest known distance to reach `node`
             */
            // minDist[node] = shortest known distance to reach `node`
            Map<Integer, Integer> minDist = new HashMap<>();
            minDist.put(node1, 0);

            while (!pq.isEmpty()) {
                int[] cur = pq.poll();
                int curNode = cur[0];
                int curCost = cur[1];

                // Early exit if we've reached destination
                if (curNode == node2) {
                    return curCost;
                }

                // Skip if we've already found a better path
                if (curCost > minDist.getOrDefault(curNode, Integer.MAX_VALUE)) {
                    continue;
                }

                // Explore neighbors
                for (int[] nei : map.getOrDefault(curNode, new ArrayList<>())) {
                    int nextNode = nei[0];
                    int edgeCost = nei[1];
                    int nextCost = curCost + edgeCost; // <--- RELAXATION STEP

                    // If this path is cheaper → update
                    if (nextCost < minDist.getOrDefault(nextNode, Integer.MAX_VALUE)) {
                        minDist.put(nextNode, nextCost);
                        pq.offer(new int[] { nextNode, nextCost });
                    }
                }
            }

            return -1; // unreachable
        }
    }

    // V0-2
    // IDEA: Dijkstra (fixed by gemini)
    class Graph_0_2 {

        // Adjacency List: node -> List of {neighbor, cost} pairs
        private final Map<Integer, List<int[]>> adj;
        private final int N; // Number of nodes (0 to N-1)

        // The PriorityQueue is local to shortestPath for correct behavior,
        // as it needs to be fresh for every path search.

        public Graph_0_2(int n, int[][] edges) {
            this.N = n;
            this.adj = new HashMap<>();

            // Initialize the adjacency list for all n nodes (0 to n-1)
            for (int i = 0; i < n; i++) {
                this.adj.put(i, new ArrayList<>());
            }

            // Add initial edges
            for (int[] edge : edges) {
                addEdge(edge);
            }
        }

        public void addEdge(int[] edge) {
            int from = edge[0];
            int to = edge[1];
            int cost = edge[2];

            // Correctly add the neighbor and its cost to the adjacency list
            // Note: The graph is mutable, so repeated addEdge calls are possible.
            this.adj.get(from).add(new int[] { to, cost });
        }

        public int shortestPath(int node1, int node2) {
            // --- FIX 1: Initialize Distance Array (dist) ---
            // Array to store the minimum distance from node1 to every other node.
            int[] dist = new int[N];
            // Use a value representing infinity.
            Arrays.fill(dist, Integer.MAX_VALUE);

            // Min-Heap Priority Queue: Stores {cost, node}
            // Use a local PQ for each call to shortestPath
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

            // Initialize distance for the start node and add it to the PQ
            dist[node1] = 0;
            pq.offer(new int[] { 0, node1 }); // {cost, node}

            // --- FIX 2: Correct Dijkstra's Algorithm implementation ---
            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int curCost = current[0]; // Cost from node1 to curNode
                int u = current[1]; // Current node

                // If we found a shorter path to 'u' previously, skip this outdated entry
                if (curCost > dist[u]) {
                    continue;
                }

                // Optimization: If we reached the target, return its distance
                if (u == node2) {
                    return curCost;
                }

                // Iterate through all neighbors (v) of the current node (u)
                for (int[] neighborEdge : adj.getOrDefault(u, Collections.emptyList())) {
                    int v = neighborEdge[0]; // Neighbor node
                    int edgeCost = neighborEdge[1]; // Cost of edge u -> v

                    // Calculate the cost of the path through u to v
                    int newPathCost = curCost + edgeCost;

                    // --- FIX 3: Correct Relaxation Step ---
                    // If a shorter path to 'v' is found (compared to dist[v])
                    if (newPathCost < dist[v]) {
                        dist[v] = newPathCost;

                        // Add the neighbor to the PQ with the new, lower cost
                        pq.offer(new int[] { newPathCost, v });
                    }
                }
            }

            // If the loop finishes, dist[node2] holds the shortest path cost.
            // If it's still MAX_VALUE, node2 is unreachable.
            return dist[node2] == Integer.MAX_VALUE ? -1 : dist[node2];
        }
    }


    // V1
    // IDEA: Dijkstra (fixed by gpt)
    class Graph_1 {

        private Map<Integer, List<int[]>> graph;

        public Graph_1(int n, int[][] edges) {
            graph = new HashMap<>();
            for (int i = 0; i < n; i++) {
                graph.put(i, new ArrayList<>());
            }

            for (int[] edge : edges) {
                addEdge(edge);
            }
        }

        public void addEdge(int[] edge) {
            int from = edge[0];
            int to = edge[1];
            int cost = edge[2];
            graph.get(from).add(new int[]{to, cost});
        }

        public int shortestPath(int node1, int node2) {
            // Standard Dijkstra’s Algorithm
            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]);
            pq.offer(new int[]{node1, 0}); // {node, totalCost}

            Map<Integer, Integer> minDist = new HashMap<>();
            minDist.put(node1, 0);

            while (!pq.isEmpty()) {
                int[] cur = pq.poll();
                int node = cur[0];
                int cost = cur[1];

                // If we reached target node
                if (node == node2) return cost;

                // Skip outdated entry
                if (cost > minDist.getOrDefault(node, Integer.MAX_VALUE)) continue;

                for (int[] nei : graph.getOrDefault(node, new ArrayList<>())) {
                    int next = nei[0];
                    int nextCost = cost + nei[1];

                    // Relax edge
                    if (nextCost < minDist.getOrDefault(next, Integer.MAX_VALUE)) {
                        minDist.put(next, nextCost);
                        pq.offer(new int[]{next, nextCost});
                    }
                }
            }
            return -1; // unreachable
        }

    }

    // V2
    // IDEA: Dijkstra, fixed by gemini
    class Graph_2 {

        // Adjacency List: node -> List of {neighbor, cost} pairs
        // Using List<int[]> is cleaner than List<Integer[]> for primitive arrays.
        private final Map<Integer, List<int[]>> adj;
        private final int N; // Number of nodes (0 to N-1)

        /**
         * Constructor: Builds the initial graph from the edges array.
         */
        public Graph_2(int n, int[][] edges) {
            this.N = n;
            this.adj = new HashMap<>();

            // Initialize the map to ensure every node is present, even if isolated.
            for (int i = 0; i < n; i++) {
                adj.put(i, new ArrayList<>());
            }

            // Add initial edges
            for (int[] edge : edges) {
                addEdge(edge);
            }
        }

        /**
         * Adds a new directed edge to the graph.
         */
        public void addEdge(int[] edge) {
            int from = edge[0];
            int to = edge[1];
            int cost = edge[2];

            // --- FIX: Correctly add the neighbor and its cost to the adjacency list ---
            // We use int[] {to, cost} to represent the edge.
            this.adj.get(from).add(new int[]{to, cost});
        }

        /**
         * Finds the shortest path from node1 to node2 using Dijkstra's algorithm.
         */
        public int shortestPath(int node1, int node2) {

            // Array to store the minimum distance from node1 to every other node.
            // Initialize distances to a very large number (infinity).
            // Long is safer, but problem constraints allow Integer.MAX_VALUE
            int[] dist = new int[N];
            Arrays.fill(dist, Integer.MAX_VALUE);

            // Distance from the source node to itself is 0.
            dist[node1] = 0;

            // Min-Heap Priority Queue for Dijkstra's. Stores {cost, node}.
            // We prioritize lower cost.
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

            // Start Dijkstra from node1 with cost 0.
            pq.offer(new int[]{0, node1}); // {cost, node}

            // --- Standard Dijkstra's Algorithm ---
            while (!pq.isEmpty()) {
                int[] current = pq.poll();
                int currentCost = current[0];
                int u = current[1];

                // If we found a shorter path to 'u' already, skip this outdated entry.
                if (currentCost > dist[u]) {
                    continue;
                }

                // Optimization: If we reached the target, return its distance.
                if (u == node2) {
                    return currentCost;
                }

                // Iterate through all neighbors (v) of the current node (u)
                for (int[] neighborEdge : adj.getOrDefault(u, Collections.emptyList())) {
                    int v = neighborEdge[0];
                    int edgeCost = neighborEdge[1];

                    // Relaxation step: new path cost = path to u + edge cost u->v
                    /**
                     *
                     * Relaxation is the process of checking whether the current
                     * known shortest path to a vertex can be improved
                     * by taking a different route through another vertex.
                     *
                     * -----
                     *
                     *
                     * Excellent — that’s one of the core ideas in Dijkstra’s algorithm:
                     * the relaxation step is what progressively updates the shortest known distance to each node.
                     *
                     * Let’s unpack that clearly. 👇
                     *
                     * ⸻
                     *
                     * 🧠 Concept: Relaxation
                     *
                     * Definition
                     *
                     * Relaxation is the process of checking whether the current known shortest path to a vertex can be improved by taking a different route through another vertex.
                     *
                     * ⸻
                     *
                     * In Dijkstra’s terms:
                     *
                     * Let’s say we are visiting node u (current node),
                     * and v is one of its neighbors with edge weight w(u, v).
                     *
                     * We currently know:
                     * 	•	dist[u]: shortest distance from start node → u
                     * 	•	dist[v]: shortest distance from start node → v (may be infinity if not reached yet)
                     *
                     * Then we check if:
                     *
                     * dist[v] > dist[u] + w(u, v)
                     *
                     * If true → we’ve found a shorter path to v through u,
                     * so we relax that edge by updating:
                     *
                     * dist[v] = dist[u] + w(u, v)
                     *
                     * and we push v back into the priority queue with the new cost.
                     *
                     * ⸻
                     *
                     * ✅ Example
                     *
                     * Imagine this weighted directed graph:
                     *
                     * 0 --(4)--> 1
                     * 0 --(3)--> 2
                     * 2 --(1)--> 1
                     * 1 --(2)--> 3
                     * 2 --(4)--> 3
                     *
                     * Start:
                     * dist[0]=0, all others = ∞
                     *
                     * 1️⃣ Pop (0,0): neighbors
                     * 	•	For (0→1,4): dist[1] = 4
                     * 	•	For (0→2,3): dist[2] = 3
                     *
                     * PQ = [(2,3), (1,4)]
                     *
                     * 2️⃣ Pop (2,3): neighbors
                     * 	•	For (2→1,1): check dist[1] > dist[2]+1 → 4 > 4 → equal → no update
                     * 	•	For (2→3,4): dist[3] > 3+4 → ∞ > 7 → ✅ relax → dist[3]=7
                     *
                     * PQ = [(1,4), (3,7)]
                     *
                     * 3️⃣ Pop (1,4): neighbor (1→3,2)
                     * 	•	Check dist[3] > 4+2 → 7 > 6 → ✅ relax → dist[3]=6
                     *
                     * PQ = [(3,6)]
                     * → shortest path to node 3 = 6
                     *
                     * ⸻
                     *
                     * 💡 Code line in your Dijkstra:
                     *
                     * int nextCost = cost + nei[1];
                     * if (nextCost < minDist.getOrDefault(next, Integer.MAX_VALUE)) {
                     *     minDist.put(next, nextCost);
                     *     pq.offer(new int[]{next, nextCost});
                     * }
                     *
                     * This is exactly the “relaxation step”:
                     * 	•	Compute the new possible distance via the current node (cost + edge weight)
                     * 	•	If it’s shorter → update the record + enqueue it for further exploration.
                     *
                     * ⸻
                     *
                     * 🔁 In summary
                     *
                     * Term	Meaning
                     * Relaxation	Updating a node’s shortest known distance when a better one is found
                     * Formula	dist[v] = min(dist[v], dist[u] + w(u, v))
                     * Purpose	To propagate the shortest path info through the graph
                     * Used in	Dijkstra, Bellman-Ford, A*, etc.
                     *
                     */
                    int newPathCost = currentCost + edgeCost;

                    // If a shorter path to 'v' is found
                    if (newPathCost < dist[v]) {
                        dist[v] = newPathCost;

                        // Add the neighbor to the PQ with the new, lower cost.
                        pq.offer(new int[]{newPathCost, v});
                    }
                }
            }

            // After the loop, dist[node2] holds the shortest path cost.
            // If it's still MAX_VALUE, node2 is unreachable.
            return dist[node2] == Integer.MAX_VALUE ? -1 : dist[node2];
        }
    }

    // V3

    // V4


}
